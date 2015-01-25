package com.alnpet.api.setting;

import java.util.ArrayList;
import java.util.List;

import org.unidal.web.mvc.view.annotation.EntityMeta;
import org.unidal.web.mvc.view.annotation.ModelMeta;

import com.alnpet.api.ApiModel;
import com.alnpet.model.entity.Setting;

@ModelMeta("model")
public class Model extends ApiModel<Action, Context> {
	@EntityMeta(value = "setting", multiple = true, names = "settings")
	private List<Setting> m_settings = new ArrayList<Setting>();

	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public void setSetting(String key, String value) {
		m_settings.add(new Setting(key).setValue(value));
	}
}
