package com.alnpet.biz.order;

import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

import com.alnpet.biz.BizPage;

public class Payload implements ActionPayload<BizPage, Action> {
	private BizPage m_page;

	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("name")
	private String m_name;

	@FieldMeta("email")
	private String m_email;

	@FieldMeta("state")
	private String m_state;

	@FieldMeta("city")
	private String m_city;

	@FieldMeta("postalCode")
	private String m_postalCode;

	@FieldMeta("addressLine1")
	private String m_addressLine1;

	@FieldMeta("addressLine2")
	private String m_addressLine2;

	@FieldMeta("coupon")
	private String m_coupon;

	@Override
	public Action getAction() {
		return m_action;
	}

	public String getAddressLine1() {
		return m_addressLine1;
	}

	public String getAddressLine2() {
		return m_addressLine2;
	}

	public String getCity() {
		return m_city;
	}

	public String getCoupon() {
		return m_coupon;
	}

	public String getEmail() {
		return m_email;
	}

	public String getName() {
		return m_name;
	}

	@Override
	public BizPage getPage() {
		return m_page;
	}

	public String getPostalCode() {
		return m_postalCode;
	}

	public String getState() {
		return m_state;
	}

	private boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	@Override
	public void setPage(String page) {
		m_page = BizPage.getByName(page, BizPage.ORDER);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}

		if (m_action == Action.ADD) {
			if (isEmpty(m_email)) {
				ctx.addError("email.required");
			} else {
				int pos1 = m_email.indexOf('@');
				int pos2 = m_email.lastIndexOf('.');

				if (pos1 < 0 || pos2 < 0 || pos1 > pos2) {
					ctx.addError("email.invalid").addArgument("email", m_email);
				}
			}

			if (isEmpty(m_name)) {
				ctx.addError("name.required");
			}

			if (isEmpty(m_state)) {
				ctx.addError("state.required");
			}

			if (isEmpty(m_city)) {
				ctx.addError("city.required");
			}

			if (isEmpty(m_postalCode)) {
				ctx.addError("postalCode.required");
			}

			if (isEmpty(m_addressLine1)) {
				ctx.addError("addressLine1.required");
			}
		}
	}
}
