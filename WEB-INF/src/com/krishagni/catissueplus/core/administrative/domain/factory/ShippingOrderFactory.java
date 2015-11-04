package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.ShippingOrder;
import com.krishagni.catissueplus.core.administrative.domain.ShippingOrder.Status;
import com.krishagni.catissueplus.core.administrative.events.ShippingOrderDetail;

public interface ShippingOrderFactory {
	public ShippingOrder createShippingOrder(ShippingOrderDetail detail, Status status);
	
}
