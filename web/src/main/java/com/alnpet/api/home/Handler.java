package com.alnpet.api.home;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.ServletException;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.api.ApiHandler;
import com.alnpet.api.ApiPage;
import com.alnpet.toc.entity.Api;
import com.alnpet.toc.entity.Sample;
import com.alnpet.toc.entity.Toc;
import com.alnpet.toc.transform.BaseVisitor;
import com.alnpet.toc.transform.DefaultSaxParser;

public class Handler extends ApiHandler<Context> implements Initializable {
	@Inject
	private JspViewer m_jspViewer;

	private Toc m_toc;

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
		Payload payload = ctx.getPayload();
		String tab = payload.getTab();

		model.setAction(Action.VIEW);
		model.setPage(ApiPage.HOME);

		if ("api".equals(tab)) {
			model.setToc(m_toc);
		}

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}

	@Override
	public void initialize() throws InitializationException {
		InputStream in = getClass().getResourceAsStream("toc.xml");

		if (in == null) {
			throw new IllegalStateException("Resource(toc.xml) is missing!");
		}

		try {
			m_toc = DefaultSaxParser.parse(in);
		} catch (Exception e) {
			throw new InitializationException("Unable to parse resource(toc.xml)!", e);
		}

		m_toc.accept(new TrimVisitor());
	}

	static class TrimVisitor extends BaseVisitor {
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
	}
}
