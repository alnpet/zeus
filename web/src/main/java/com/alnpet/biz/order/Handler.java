package com.alnpet.biz.order;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.alnpet.biz.BizPage;
import com.alnpet.dal.biz.CouponDao;
import com.alnpet.dal.biz.CouponEntity;
import com.alnpet.dal.biz.Order;
import com.alnpet.dal.biz.User;
import com.alnpet.dal.biz.UserDao;
import com.alnpet.dal.biz.UserEntity;
import com.alnpet.service.OrderService;

public class Handler implements PageHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	@Inject
	private OrderService m_orderService;

	@Inject
	private UserDao m_userDao;

	@Inject
	private CouponDao m_couponDao;

	private User findOrCreateUser(String name, String email) throws DalException {
		try {
			return m_userDao.findByEmail(email, UserEntity.READSET_FULL);
		} catch (DalException e) {
			// ignore
		}

		User user = new User().setName(name).setEmail(email);

		m_userDao.insert(user);
		return user;
	}

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "order")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		Payload payload = ctx.getPayload();
		Action action = payload.getAction();

		switch (action) {
		case ADD:
			if (!ctx.hasErrors()) {
				String coupon = payload.getCoupon();

				if (!validateCoupon(coupon)) {
					ctx.addError("coupon.invalid").addArgument("coupon", coupon);
				} else {
					try {
						User user = findOrCreateUser(payload.getName(), payload.getEmail());
						Order order = m_orderService.placeOrder(user.getId(), coupon, payload.getName(), payload.getState(),
						      payload.getCity(), payload.getPostalCode(), payload.getAddressLine1(),
						      payload.getAddressLine2());

						ctx.redirect(BizPage.PAYMENT, "oid=" + order.getId());
					} catch (Exception e) {
						ctx.addError("order.failed", e);
					}
				}
			}
		default:
			break;
		}
	}

	@Override
	@OutboundActionMeta(name = "order")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);

		model.setAction(Action.VIEW);
		model.setPage(BizPage.ORDER);

		if (!ctx.isProcessStopped()) {
			m_jspViewer.view(ctx, model);
		}
	}

	private boolean validateCoupon(String code) {
		if (code != null && code.length() > 0) {
			try {
				m_couponDao.findByCode(code, CouponEntity.READSET_FULL);

				return true;
			} catch (DalException e) {
				return false;
			}
		} else {
			// no coupon
			return true;
		}
	}
}
