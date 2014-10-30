package com.alnpet.api.category;

import java.util.List;

import org.unidal.web.mvc.view.annotation.EntityMeta;
import org.unidal.web.mvc.view.annotation.ModelMeta;

import com.alnpet.api.ApiModel;
import com.alnpet.dal.core.Category;

@ModelMeta("model")
public class Model extends ApiModel<Action, Context> {
	@EntityMeta("categories")
	private List<Category> m_categories;

	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public List<Category> getCategories() {
		return m_categories;
	}

	public void setCategories(List<Category> categories) {
		m_categories = categories;
	}
}
