package com.alnpet.biz.payment;

import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;
import org.unidal.web.mvc.payload.annotation.PathMeta;

import com.alnpet.biz.BizPage;

public class Payload implements ActionPayload<BizPage, Action> {
	private BizPage m_page;

	@FieldMeta("op")
	private Action m_action;

	@PathMeta("path")
	private String[] m_path;

	@FieldMeta("oid")
	private int m_orderId;

	@Override
	public Action getAction() {
		return m_action;
	}

	public int getOrderId() {
		return m_orderId;
	}

	@Override
	public BizPage getPage() {
		return m_page;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	@Override
	public void setPage(String page) {
		m_page = BizPage.getByName(page, BizPage.PAYMENT);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}

		if (m_path != null && m_path.length > 0) {
			m_action = Action.getByName(m_path[0], m_action);
		}
	}
}
