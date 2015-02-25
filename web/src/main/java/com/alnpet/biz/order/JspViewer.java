package com.alnpet.biz.order;

import com.alnpet.biz.BizPage;
import org.unidal.web.mvc.view.BaseJspViewer;

public class JspViewer extends BaseJspViewer<BizPage, Action, Context, Model> {
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
