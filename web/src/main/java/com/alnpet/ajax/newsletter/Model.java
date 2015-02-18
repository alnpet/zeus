package com.alnpet.ajax.newsletter;

import com.alnpet.ajax.AjaxPage;
import org.unidal.web.mvc.ViewModel;

public class Model extends ViewModel<AjaxPage, Action, Context> {
	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}
}
