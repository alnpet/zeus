package com.alnpet.api.setting;

import java.util.List;

import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

import com.alnpet.api.ApiPage;
import com.site.helper.Splitters;

public class Payload implements ActionPayload<ApiPage, Action> {
	private ApiPage m_page;

	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("token")
	private String m_token;

	@FieldMeta("keys")
	private String m_keys;

	@Override
	public Action getAction() {
		return m_action;
	}

	public List<String> getKeys() {
		return Splitters.by(',').noEmptyItem().trim().split(m_keys);
	}

	@Override
	public ApiPage getPage() {
		return m_page;
	}

	public String getToken() {
		return m_token;
	}

	private boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	@Override
	public void setPage(String page) {
		m_page = ApiPage.getByName(page, ApiPage.SETTING);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}

		if (isEmpty(m_token)) {
			ctx.addError("token.required");
		}

		if (isEmpty(m_keys)) {
			ctx.addError("keys.required");
		}
	}
}
