package com.krishagni.catissueplus.core.bo.factory.impl;

import com.krishagni.catissueplus.core.bo.factory.BulkOperationController;
import com.krishagni.catissueplus.core.bo.factory.BulkOperationControllerFactory;
import com.krishagni.catissueplus.core.bo.service.impl.SpecimenBulkOperationController;

public class SpecimenControllerFactory implements
		BulkOperationControllerFactory {

	@Override
	public BulkOperationController getController() {
		return new SpecimenBulkOperationController();
	}

}
