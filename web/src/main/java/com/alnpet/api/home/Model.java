package com.alnpet.api.home;

import com.alnpet.api.ApiModel;
import com.alnpet.toc.entity.Toc;

public class Model extends ApiModel<Action, Context> {
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
