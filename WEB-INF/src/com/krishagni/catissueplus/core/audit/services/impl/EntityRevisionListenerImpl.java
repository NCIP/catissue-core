package com.krishagni.catissueplus.core.audit.services.impl;

import org.hibernate.envers.RevisionListener;

import com.krishagni.catissueplus.core.audit.domain.EntityRevision;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class EntityRevisionListenerImpl implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		EntityRevision revision = (EntityRevision) revisionEntity;
		
		if(AuthUtil.getCurrentUser() != null){
			revision.setUserId(AuthUtil.getCurrentUser().getId());
		}

		revision.setIpAddress(AuthUtil.getRemoteAddr());
	}

}