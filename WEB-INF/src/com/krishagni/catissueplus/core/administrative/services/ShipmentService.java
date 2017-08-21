package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.ShipmentDetail;
import com.krishagni.catissueplus.core.administrative.events.ShipmentListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;

public interface ShipmentService {
	public ResponseEvent<List<ShipmentDetail>> getShipments(RequestEvent<ShipmentListCriteria> req);

	public ResponseEvent<Long> getShipmentsCount(RequestEvent<ShipmentListCriteria> req);

	public ResponseEvent<ShipmentDetail> getShipment(RequestEvent<Long> req);
	
	public ResponseEvent<ShipmentDetail> createShipment(RequestEvent<ShipmentDetail> req);
	
	public ResponseEvent<ShipmentDetail> updateShipment(RequestEvent<ShipmentDetail> req);
	
	public ResponseEvent<QueryDataExportResult> exportReport(RequestEvent<Long> req);
}
