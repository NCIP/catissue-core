package com.krishagni.catissueplus.core.audit;

import org.hibernate.envers.RevisionListener;

import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class EntityRevisionListenerImpl implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		Long userId = null;
		Revision revEntity = (Revision) revisionEntity;
		
		if(AuthUtil.getCurrentUser() != null){
			userId = AuthUtil.getCurrentUser().getId();
		}
		revEntity.setUserId(userId);
		revEntity.setIpAddress(AuthUtil.getRemoteAddr());
		
	}
}