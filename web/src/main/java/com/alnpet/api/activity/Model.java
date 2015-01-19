package com.alnpet.api.activity;

import org.unidal.web.mvc.view.annotation.EntityMeta;
import org.unidal.web.mvc.view.annotation.ModelMeta;

import com.alnpet.api.ApiModel;
import com.alnpet.model.entity.Activities;
import com.alnpet.model.entity.Activity;

@ModelMeta("model")
public class Model extends ApiModel<Action, Context> {
	@EntityMeta
	private Activities m_activities;
	
	@EntityMeta
	private Activity m_activity;

	public Model(Context ctx) {
		super(ctx);
	}

	public Activities getActivities() {
		return m_activities;
	}

	public Activity getActivity() {
		return m_activity;
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public void setActivities(Activities activities) {
		m_activities = activities;
	}

	public void setActivity(Activity activity) {
		m_activity = activity;
	}
}
