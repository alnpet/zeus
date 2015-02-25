package com.alnpet.biz.order;

import com.alnpet.biz.BizPage;
import org.unidal.web.mvc.ViewModel;

public class Model extends ViewModel<BizPage, Action, Context> {
	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}
}
