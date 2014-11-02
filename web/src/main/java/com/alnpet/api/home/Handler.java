package com.alnpet.api.home;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.ServletException;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;
import org.xml.sax.SAXException;

import com.alnpet.api.ApiHandler;
import com.alnpet.api.ApiPage;
import com.alnpet.toc.entity.Api;
import com.alnpet.toc.entity.Release;
import com.alnpet.toc.entity.Sample;
import com.alnpet.toc.entity.Toc;
import com.alnpet.toc.transform.BaseVisitor;
import com.alnpet.toc.transform.DefaultSaxParser;
import com.dianping.cat.Cat;

public class Handler extends ApiHandler<Context> implements Initializable {
	@Inject
	private JspViewer m_jspViewer;

	private TocLoader m_loader;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "home")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		// display only, no action here
	}

	@Override
	@OutboundActionMeta(name = "home")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);

		model.setAction(Action.VIEW);
		model.setPage(ApiPage.HOME);

		try {
			model.setToc(m_loader.load());
		} catch (Exception e) {
			Cat.logError(e);
		}

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}

	@Override
	public void initialize() throws InitializationException {
		m_loader = new TocLoader();

		try {
			m_loader.load();
		} catch (Exception e) {
			throw new InitializationException("Unable to parse resource(toc.xml)!", e);
		}
	}

	static class TocLoader {
		private Toc m_toc;

		private File m_file;

		private long m_lastModified;

		public Toc load() throws SAXException, IOException {
			if (m_toc == null) {
				URL url = getClass().getResource("toc.xml");

				if (url == null) {
					throw new IllegalStateException("Resource(toc.xml) is missing!");
				}

				if (url.getProtocol().equals("file")) {
					m_file = new File(url.getPath());
					m_lastModified = m_file.lastModified();
					m_toc = DefaultSaxParser.parse(new FileInputStream(m_file));
				} else {
					InputStream in = getClass().getResourceAsStream("toc.xml");

					m_toc = DefaultSaxParser.parse(in);
				}

				m_toc.accept(new TocVisitor());
			} else if (m_file != null) {
				if (m_file.lastModified() != m_lastModified) {
					try {
						m_toc = DefaultSaxParser.parse(new FileInputStream(m_file));
						m_lastModified = m_file.lastModified();
						m_toc.accept(new TocVisitor());
					} catch (Exception e) {
						Cat.logError(e);
						// ignore it
					}
				}
			}

			return m_toc;
		}
	}

	static class TocVisitor extends BaseVisitor {
		@Override
		public void visitApi(Api api) {
			for (Sample sample : new ArrayList<Sample>(api.getSamples())) {
				if (sample.getRequest() == null) {
					api.getSamples().remove(sample);
				}
			}

			super.visitApi(api);
		}

		@Override
		public void visitSample(Sample sample) {
			sample.setRequest(sample.getRequest().trim());
			sample.setResponse(sample.getResponse().trim());

			super.visitSample(sample);
		}

		@Override
		public void visitToc(Toc toc) {
			Collections.sort(toc.getReleases(), new Comparator<Release>() {
				@Override
				public int compare(Release o1, Release o2) {
					return -o1.getDate().compareTo(o2.getDate());
				}
			});

			super.visitToc(toc);
		}
	}
}
