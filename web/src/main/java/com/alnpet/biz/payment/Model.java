package com.alnpet.biz.payment;

import org.unidal.web.mvc.ViewModel;

import com.alnpet.biz.BizPage;
import com.alnpet.dal.trx.Order;

public class Model extends ViewModel<BizPage, Action, Context> {
	private Order m_order;

	public Model(Context ctx) {
		super(ctx);
	}

	@Override
	public Action getDefaultAction() {
		return Action.VIEW;
	}

	public Order getOrder() {
		return m_order;
	}

	public void setOrder(Order order) {
		m_order = order;
	}
}
