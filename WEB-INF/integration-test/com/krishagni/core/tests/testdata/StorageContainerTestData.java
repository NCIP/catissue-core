package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPositionDetail;

public class StorageContainerTestData {
//	public static CreateStorageContainerEvent getCreateStorageContainerEvent() {
//		CreateStorageContainerEvent req = new CreateStorageContainerEvent();
//		req.setSessionDataBean(CprTestData.getSessionDataBean());
//		req.setContainer(getStorageContainer("default-container", 2,2 ));
//		return req;
//	}
	
	public static StorageContainerDetail getStorageContainer(String name, int oneDimCap, int twoDimCap) {
		StorageContainerDetail detail = new StorageContainerDetail();
		detail.setName(name);
		detail.setDimensionOneCapacity(oneDimCap);
		detail.setDimensionTwoCapacity(twoDimCap);
		detail.setCreatedBy(CommonUtils.getUser(1L,"first-name", "last-name", "admin@admin.com" ));
		detail.setBarcode("default-barcode");
		detail.setComments("default-comments");
		detail.setDimensionOneLabelingScheme("Alphabets Lower Case");
		detail.setDimensionTwoLabelingScheme("Alphabets Lower Case");
		detail.setActivityStatus("Active");
		detail.setSiteName("SITE1");
		detail.setTemperature(20.0D);
		
		detail.getAllowedSpecimenClasses().add("Cell");
		detail.getAllowedSpecimenClasses().add("Fluid");
		detail.getAllowedSpecimenClasses().add("Molecular");
		
		detail.getAllowedSpecimenTypes().add("Bile");
		detail.getAllowedSpecimenTypes().add("DNA");
		detail.getAllowedSpecimenTypes().add("Bone Marrow Plasma");
		
		detail.getAllowedCollectionProtocols().add("short-title-1");
		detail.getAllowedCollectionProtocols().add("short-title-2");
		detail.getAllowedCollectionProtocols().add("short-title-3");
		
		return detail;
	}
	
	public static StorageContainerPositionDetail getPosition(String pos1, String pos2) { 
		StorageContainerPositionDetail detail = new StorageContainerPositionDetail();
		detail.setContainerId(1L);
		detail.setOccupyingEntityId(2L);
		detail.setPosOne(pos1);
		detail.setPosTwo(pos2);
		return detail;
	}
}
