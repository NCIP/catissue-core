package com.krishagni.catissueplus.core.bo.factory.impl;

import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.events.ParticipantRegistrationDetails;
import com.krishagni.catissueplus.core.biospecimen.events.ScgDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.bo.factory.BulkOperationControllerFactory;

public class BulkOperationManager {
	private Map<String,BulkOperationControllerFactory> bulkOperationMap = new HashMap<String,BulkOperationControllerFactory>();
	
	private static BulkOperationManager instance;
	
	private BulkOperationManager() {
		//to defeat instantiation
		//static registrations
		bulkOperationMap.put(ParticipantRegistrationDetails.class.getSimpleName(), new CprControllerFactory());
//		bulkOperationMap.put(SpecimenDetail.class.getSimpleName(), new SpecimenControllerFactory());
//		bulkOperationMap.put(ScgDetail.class.getSimpleName(), new ScgControllerFactory());
	}
	
	public static BulkOperationManager getInstance() {
		if (instance == null) {
			instance = new BulkOperationManager();
		}
		
		return instance;
	}

	public void registerFactory(Object o, BulkOperationControllerFactory serviceProvider) {
		bulkOperationMap.put(o.getClass().getSimpleName(), serviceProvider);
	}
	
	public BulkOperationControllerFactory getFactory(String simpleClassName) {
		return bulkOperationMap.get(simpleClassName);
	}
}