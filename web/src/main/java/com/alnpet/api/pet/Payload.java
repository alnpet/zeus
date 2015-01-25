package com.alnpet.api.pet;

import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

import com.alnpet.api.ApiPage;

public class Payload implements ActionPayload<ApiPage, Action> {
	private ApiPage m_page;

	@FieldMeta("op")
	private Action m_action;

	// register
	@FieldMeta("name")
	private String m_name;

	@FieldMeta("gender")
	private String m_gender;

	@FieldMeta("age")
	private double m_age;

	@FieldMeta("weight")
	private double m_weight;

	@FieldMeta("category")
	private String m_category;

	@FieldMeta("device")
	private String m_device;

	// bind
	@FieldMeta("token")
	private String m_token;

	@FieldMeta("type")
	private String m_type;

	@FieldMeta("nickname")
	private String m_nickname;

	@FieldMeta("phone")
	private String m_phone;

	@FieldMeta("email")
	private String m_email;

	@FieldMeta("uid")
	private String m_uid;

	@Override
	public Action getAction() {
		return m_action;
	}

	public double getAge() {
		return m_age;
	}

	public String getCategory() {
		return m_category;
	}

	public String getDevice() {
		return m_device;
	}

	public String getEmail() {
		return m_email;
	}

	public String getGender() {
		return m_gender;
	}

	public String getName() {
		return m_name;
	}

	public String getNickname() {
		return m_nickname;
	}

	@Override
	public ApiPage getPage() {
		return m_page;
	}

	public String getPhone() {
		return m_phone;
	}

	public String getToken() {
		return m_token;
	}

	public String getType() {
		return m_type;
	}

	public String getUid() {
		return m_uid;
	}

	public double getWeight() {
		return m_weight;
	}

	private boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	@Override
	public void setPage(String page) {
		m_page = ApiPage.getByName(page, ApiPage.PET);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}

		switch (m_action) {
		case REGISTER:
			if (isEmpty(m_name)) {
				ctx.addError("name.required");
			}

			if (isEmpty(m_category)) {
				ctx.addError("category.required");
			}

			break;
		case BIND:
			if (isEmpty(m_token)) {
				ctx.addError("token.required");
			}

			if ("user".equals(m_type)) {
				if (isEmpty(m_nickname)) {
					ctx.addError("nickname.required");
				}

				if (isEmpty(m_email)) {
					ctx.addError("email.required");
				}
			} else if ("portrait".equals(m_type)) {
				// TODO
			} else if ("device".equals(m_type)) {
				if (isEmpty(m_device)) {
					ctx.addError("device.required");
				}
			} else {
				ctx.addError("type.required");
			}

			break;
		default:
			break;
		}
	}
}
