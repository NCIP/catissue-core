package com.krishagni.catissueplus.core.administrative.services.impl;


import com.krishagni.catissueplus.core.administrative.domain.Shipment.Status;
import com.krishagni.catissueplus.core.administrative.events.ShipmentDetail;
import com.krishagni.catissueplus.core.administrative.services.ShipmentService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class ShipmentImporter implements ObjectImporter<ShipmentDetail, ShipmentDetail> {	
	private ShipmentService shipmentSvc;

	public void setShipmentSvc(ShipmentService shipmentSvc) {
		this.shipmentSvc = shipmentSvc;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<ShipmentDetail> importObject(RequestEvent<ImportObjectDetail<ShipmentDetail>> req) {
		try {
			ImportObjectDetail<ShipmentDetail> detail = req.getPayload();
			detail.getObject().setSendMail(false);
			RequestEvent<ShipmentDetail> shipmentReq = new RequestEvent<ShipmentDetail>(detail.getObject());
			
			if (detail.isCreate()) {
				return createShipment(shipmentReq);
			} else {
				return updateShipment(shipmentReq);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private ResponseEvent<ShipmentDetail> createShipment(RequestEvent<ShipmentDetail> shipmentReq) {
		String status = shipmentReq.getPayload().getStatus();
		boolean isReceived = Status.RECEIVED.equals(Status.fromName(status));
		if (isReceived) {
			shipmentReq.getPayload().setStatus("SHIPPED");
		}
		
		ResponseEvent<ShipmentDetail> resp = shipmentSvc.createShipment(shipmentReq);
		resp.throwErrorIfUnsuccessful();
		
		if (isReceived) {
			shipmentReq.getPayload().setId(resp.getPayload().getId());
			shipmentReq.getPayload().setStatus("RECEIVED");
			resp = updateShipment(shipmentReq);
		}
		
		return resp;
	}
	
	private ResponseEvent<ShipmentDetail> updateShipment(RequestEvent<ShipmentDetail> shipmentReq) {
		ResponseEvent<ShipmentDetail> resp = shipmentSvc.updateShipment(shipmentReq);
		resp.throwErrorIfUnsuccessful();
		
		return resp;
	}
	
}
