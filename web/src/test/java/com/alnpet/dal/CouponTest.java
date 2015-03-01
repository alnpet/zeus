package com.alnpet.dal;

import org.junit.Test;
import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.ComponentTestCase;

import com.alnpet.dal.trx.Coupon;
import com.alnpet.dal.trx.CouponDao;

public class CouponTest extends ComponentTestCase {
	@Test
	public void prepareCoupons() throws DalException {
		CouponDao dao = lookup(CouponDao.class);

		dao.insert(newCoupon("C1", "W6TK59ED29N8C", 0.01, 100));
		dao.insert(newCoupon("C2", "HMH5DST2ZBEDN", 0.02, 100));
	}

	private Coupon newCoupon(String code, String paypalButtonId, double price, int totalQuantity) {
		Coupon c = new Coupon();

		c.setCode(code);
		c.setPaypalButtonId(paypalButtonId);
		c.setPrice(price);
		c.setRemainingQuantity(totalQuantity);
		c.setTotalQuantity(totalQuantity);

		return c;
	}
}
