package com.alnpet.api.activity;

import org.unidal.web.mvc.view.annotation.EntityMeta;
import org.unidal.web.mvc.view.annotation.ModelMeta;

import com.alnpet.api.ApiModel;
import com.alnpet.model.entity.Activities;

@ModelMeta("model")
public class Model extends ApiModel<Action, Context> {
	@EntityMeta
	private Activities m_activities;

	public Model(Context ctx) {
		super(ctx);
	}

	public Activities getActivities() {
		return m_activities;
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public void setActivities(Activities activities) {
		m_activities = activities;
	}
}
