package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ShippingOrder;
import com.krishagni.catissueplus.core.administrative.events.ShippingOrderListCriteria;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ShippingOrderDao extends Dao<ShippingOrder> {
	public List<ShippingOrder> getShippingOrders(ShippingOrderListCriteria crit);
	
	public ShippingOrder getOrderByName(String name);
	
	public List<Specimen> getShippedSpecimens(List<String> specimenLabels);
}
