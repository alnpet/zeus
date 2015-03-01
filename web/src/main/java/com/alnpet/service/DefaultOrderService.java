package com.alnpet.service;

import java.util.List;

import org.unidal.dal.jdbc.DalException;
import org.unidal.dal.jdbc.DalNotFoundException;
import org.unidal.lookup.annotation.Inject;

import com.alnpet.dal.trx.Address;
import com.alnpet.dal.trx.AddressDao;
import com.alnpet.dal.trx.AddressEntity;
import com.alnpet.dal.trx.Coupon;
import com.alnpet.dal.trx.CouponDao;
import com.alnpet.dal.trx.CouponEntity;
import com.alnpet.dal.trx.Order;
import com.alnpet.dal.trx.OrderDao;
import com.alnpet.dal.trx.OrderEntity;

public class DefaultOrderService implements OrderService {
	@Inject
	private AddressDao m_addressDao;

	@Inject
	private CouponDao m_couponDao;

	@Inject
	private OrderDao m_orderDao;

	private Order createOrder(int userId, Address address, Coupon coupon) throws DalException {
		Order order = new Order();

		order.setUserId(userId);
		order.setAddressId(address.getId());

		order.setPrice(coupon.getPrice());
		order.setCoupon(coupon.getCode());

		order.setStatus(1);

		m_orderDao.insert(order);
		return order;
	}

	private Coupon findCoupon(String code) throws DalException {
		if (code != null && code.length() > 0) {
			try {
				return m_couponDao.findByCode(code, CouponEntity.READSET_FULL);
			} catch (DalNotFoundException e) {
				throw new RuntimeException("");
			}
		}

		Coupon coupon = new Coupon();

		coupon.setPrice(118);
		return coupon;
	}

	private Address findOrCreateAddress(int userId, String name, String state, String city, String postalCode,
	      String addressLine1, String addressLine2) throws DalException {
		List<Address> addresses = m_addressDao.findAllByUserId(userId, AddressEntity.READSET_FULL);

		for (Address address : addresses) {
			if (!address.getName().equals(name)) {
				continue;
			}

			if (!address.getState().equals(state)) {
				continue;
			}

			if (!address.getCity().equals(city)) {
				continue;
			}

			if (!address.getPostalCode().equals(postalCode)) {
				continue;
			}

			if (!address.getAddressLine1().equals(addressLine1)) {
				continue;
			}

			return address;
		}

		Address a = new Address();

		a.setUserId(userId);
		a.setName(name);
		a.setState(state);
		a.setCity(city);
		a.setPostalCode(postalCode);
		a.setAddressLine1(addressLine1);
		a.setAddressLine2(addressLine2);
		a.setStatus(1);

		m_addressDao.insert(a);
		return a;
	}

	@Override
	public Order findOrder(int orderId) throws Exception {
		return m_orderDao.findByPK(orderId, OrderEntity.READSET_FULL);
	}

	@Override
	public Order placeOrder(int userId, String code, String name, String state, String city, String postalCode,
	      String addressLine1, String addressLine2) throws Exception {
		Address address = findOrCreateAddress(userId, name, state, city, postalCode, addressLine1, addressLine2);
		Coupon coupon = findCoupon(code);
		Order order = createOrder(userId, address, coupon);

		return order;
	}
}
