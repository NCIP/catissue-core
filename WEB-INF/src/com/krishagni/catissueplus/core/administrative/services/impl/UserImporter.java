package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.events.UserDetail;
import com.krishagni.catissueplus.core.administrative.services.UserService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class UserImporter implements ObjectImporter<UserDetail, UserDetail> {
	
	private UserService userSvc;
	
	public void setUserSvc(UserService userSvc) {
		this.userSvc = userSvc;
	}

	@Override
	public ResponseEvent<UserDetail> importObject(RequestEvent<ImportObjectDetail<UserDetail>> req) {
		try {
			ImportObjectDetail<UserDetail> detail = req.getPayload();
			RequestEvent<UserDetail> userReq = new RequestEvent<UserDetail>(detail.getObject());
			if (detail.isCreate()) {
				return userSvc.createUser(userReq);
			} else {
				return userSvc.patchUser(userReq);
			}						
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
