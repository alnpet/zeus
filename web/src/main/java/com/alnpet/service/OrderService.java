package com.alnpet.service;

import com.alnpet.dal.trx.Order;

public interface OrderService {
	public Order placeOrder(int userId, String code, String name, String state, String city, String postalCode,
	      String addressLine1, String addressLine2) throws Exception;

	public Order findOrder(int orderId) throws Exception;
}
