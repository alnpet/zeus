package com.alnpet.biz.payment;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.biz.BizPage;
import com.alnpet.dal.biz.Order;
import com.alnpet.service.OrderService;

public class Handler implements PageHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	@Inject
	private OrderService m_orderService;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "payment")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();

		switch (action) {
		case SUCCESS:
			break;
		case CANCEL:
			break;
		default:
			break;
		}
	}

	@Override
	@OutboundActionMeta(name = "payment")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();
		Model model = new Model(ctx);

		model.setAction(Action.VIEW);
		model.setPage(BizPage.PAYMENT);

		switch (action) {
		case VIEW:
			try {
				Order order = m_orderService.findOrder(payload.getOrderId());

				model.setOrder(order);
			} catch (Exception e) {
				ctx.addError("order.notFound").addArgument("order", payload.getOrderId());
			}

			break;
		default:
			break;
		}

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}
}
