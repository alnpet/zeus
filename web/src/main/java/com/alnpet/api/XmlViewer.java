package com.alnpet.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.unidal.web.mvc.view.model.DefaultModelHandler;

import com.alnpet.api.pet.Context;
import com.alnpet.api.pet.Model;

public class XmlViewer extends DefaultModelHandler {
	public void view(Model model) throws IOException {
		Context ctx = model.getActionContext();
		HttpServletResponse res = ctx.getHttpServletResponse();

		handleXmlDownload(res, model, ctx);
	}
}
