package com.krishagni.catissueplus.core.bo.factory.impl;

import com.krishagni.catissueplus.core.bo.events.BulkOperationRequest;
import com.krishagni.catissueplus.core.bo.events.BulkOperationResponse;
import com.krishagni.catissueplus.core.bo.factory.BulkOperationController;
import com.krishagni.catissueplus.core.bo.factory.BulkOperationControllerFactory;
import com.krishagni.catissueplus.bulkoperator.util.BulkProcessor;
import com.krishagni.catissueplus.bulkoperator.util.ServiceAction;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;

import edu.wustl.common.beans.SessionDataBean;

public class BulkOperationProcessor implements BulkProcessor {

	@Override
	public Object processObject(Object obj, ServiceAction serviceAction, SessionDataBean sessionDataBean) {
		BulkOperationControllerFactory factory = BulkOperationManager.getInstance().getFactory(obj.getClass().getSimpleName());
		if (factory == null) {
			throw new RuntimeException("Bulkprocessor not configured. Internal Server Error.");
		}
		
		BulkOperationController controller = factory.getController();
		BulkOperationRequest req = new BulkOperationRequest(obj, sessionDataBean);
		BulkOperationResponse res = null;
		
		switch(serviceAction) {
		case ADD:
			res = controller.saveObject(req);
			break;
			
		case DELETE:
			res = controller.deleteObject(req);
			break;
			
		case UPDATE:
			res = controller.updateObject(req);
			break;
		}
		
		if (res != null && res.getStatus() != EventStatus.OK) {
			String message = buildResponseMessage(res);
			throw new RuntimeException(message);
		}
		
		return res.getObject();
	}

	private String buildResponseMessage(BulkOperationResponse res) {
		if (res.getErroneousFields() != null && res.getErroneousFields().length > 0) {
			String message = res.getMessage() + " ";
			for (ErroneousField error : res.getErroneousFields()) {
				message += "[ " +  error.getErrorMessage() + " ] [ " + error.getFieldName() + " ], ";
			}
			
			return message;
		} 
		return res.getMessage();
	}
	
}