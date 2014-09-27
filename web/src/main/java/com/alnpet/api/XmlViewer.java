package com.alnpet.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ViewModel;
import org.unidal.web.mvc.view.model.DefaultModelHandler;

public class XmlViewer extends DefaultModelHandler {
	public void view(ViewModel<?, ?, ?> model) throws IOException {
		ActionContext<?> ctx = model.getActionContext();
		HttpServletResponse res = ctx.getHttpServletResponse();

		handleXmlDownload(res, model, ctx);
	}
}
