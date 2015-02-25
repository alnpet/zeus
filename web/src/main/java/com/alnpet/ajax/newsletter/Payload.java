package com.alnpet.ajax.newsletter;

import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

import com.alnpet.ajax.AjaxPage;

public class Payload implements ActionPayload<AjaxPage, Action> {
	private AjaxPage m_page;

	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("name")
	private String m_name;

	@FieldMeta("email")
	private String m_email;

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	@Override
	public Action getAction() {
		return m_action;
	}

	@Override
	public AjaxPage getPage() {
		return m_page;
	}

	public String getName() {
		return m_name;
	}

	public String getEmail() {
		return m_email;
	}

	@Override
	public void setPage(String page) {
		m_page = AjaxPage.getByName(page, AjaxPage.NEWSLETTER);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}

		if (m_action == Action.SIGNUP) {
			if (m_name == null || m_name.isEmpty()) {
				ctx.addError("name.required");
			}

			if (m_email == null || m_email.isEmpty()) {
				ctx.addError("email.required");
			} else {
				int pos1 = m_email.indexOf('@');
				int pos2 = m_email.lastIndexOf('.');

				if (pos1 < 0 || pos2 < 0 || pos1 > pos2) {
					ctx.addError("email.invalid").addArgument("email", m_email);
				}
			}
		}
	}
}
