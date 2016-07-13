package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.Shipment;
import com.krishagni.catissueplus.core.administrative.events.ShipmentListCriteria;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ShipmentDao extends Dao<Shipment> {
	public List<Shipment> getShipments(ShipmentListCriteria crit);

	public Long getShipmentsCount(ShipmentListCriteria crit);

	public Shipment getShipmentByName(String name);
	
	public List<Specimen> getShippedSpecimensByLabels(List<String> specimenLabels);

	public Map<String, Object> getShipmentIds(String key, Object value);
}
