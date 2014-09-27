package com.alnpet.api.activity;

import com.alnpet.api.ApiPage;
import org.unidal.web.mvc.ViewModel;

public class Model extends ViewModel<ApiPage, Action, Context> {
	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}
}
