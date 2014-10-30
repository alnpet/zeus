package com.alnpet.api.pet;

import org.unidal.web.mvc.view.annotation.ModelMeta;

import com.alnpet.api.ApiModel;

@ModelMeta("model")
public class Model extends ApiModel<Action, Context> {
	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}
}
