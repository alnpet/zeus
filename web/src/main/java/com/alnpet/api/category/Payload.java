package com.alnpet.api.category;

import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

import com.alnpet.api.ApiPage;

public class Payload implements ActionPayload<ApiPage, Action> {
	private ApiPage m_page;

	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("token")
	private String m_token;

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	@Override
	public Action getAction() {
		return m_action;
	}

	@Override
	public ApiPage getPage() {
		return m_page;
	}

	public String getToken() {
		return m_token;
	}

	@Override
	public void setPage(String page) {
		m_page = ApiPage.getByName(page, ApiPage.CATEGORY);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}

		if (m_token == null) {
			ctx.addError("token.required");
		}

	}
}
