package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class StorageContainerImporter implements ObjectImporter<StorageContainerDetail, StorageContainerDetail> {
	
	private StorageContainerService containerSvc;
	
	public void setContainerSvc(StorageContainerService containerSvc) {
		this.containerSvc = containerSvc;
	}

	@Override
	public ResponseEvent<StorageContainerDetail> importObject(RequestEvent<ImportObjectDetail<StorageContainerDetail>> req) {
		try {
			ImportObjectDetail<StorageContainerDetail> detail = req.getPayload();
			RequestEvent<StorageContainerDetail> containerReq = new RequestEvent<StorageContainerDetail>(detail.getObject());
			if (detail.isCreate()) {
				return containerSvc.createStorageContainer(containerReq);
			} else {
				return containerSvc.patchStorageContainer(containerReq);
			}			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
