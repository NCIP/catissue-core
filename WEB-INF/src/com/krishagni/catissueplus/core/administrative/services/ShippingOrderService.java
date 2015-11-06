package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ShippingOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.ShippingOrderListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ShippingOrderService {
	public ResponseEvent<List<ShippingOrderDetail>> getOrders(RequestEvent<ShippingOrderListCriteria> req);
	
	public ResponseEvent<ShippingOrderDetail> getOrder(RequestEvent<Long> req);
	
	public ResponseEvent<ShippingOrderDetail> createOrder(RequestEvent<ShippingOrderDetail> req);
	
	public ResponseEvent<ShippingOrderDetail> updateOrder(RequestEvent<ShippingOrderDetail> req);
	
	public ResponseEvent<ShippingOrderDetail> receiveOrder(RequestEvent<ShippingOrderDetail> req);
}
