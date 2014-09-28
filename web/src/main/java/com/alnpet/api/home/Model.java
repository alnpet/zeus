package com.alnpet.api.home;

import org.unidal.web.mvc.ViewModel;

import com.alnpet.api.ApiPage;
import com.alnpet.toc.entity.Toc;

public class Model extends ViewModel<ApiPage, Action, Context> {
	private Toc m_toc;

	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public Toc getToc() {
		return m_toc;
	}

	public void setToc(Toc toc) {
		m_toc = toc;
	}
}
