package com.alnpet.ajax.newsletter;

import com.alnpet.ajax.AjaxPage;
import org.unidal.web.mvc.view.BaseJspViewer;

public class JspViewer extends BaseJspViewer<AjaxPage, Action, Context, Model> {
	@Override
	protected String getJspFilePath(Context ctx, Model model) {
		Action action = model.getAction();

		switch (action) {
		case VIEW:
			return JspFile.VIEW.getPath();
		default:
			break;
		}

		throw new RuntimeException("Unknown action: " + action);
	}
}
