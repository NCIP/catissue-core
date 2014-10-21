package com.krishagni.catissueplus.core.bo.factory.impl;

import com.krishagni.catissueplus.core.bo.factory.BulkOperationController;
import com.krishagni.catissueplus.core.bo.factory.BulkOperationControllerFactory;
import com.krishagni.catissueplus.core.bo.service.impl.CprBulkOperationController;

public class CprControllerFactory implements BulkOperationControllerFactory {

	@Override
	public BulkOperationController getController() {
		CprBulkOperationController cprController = new CprBulkOperationController();
		return cprController;
	}
}
