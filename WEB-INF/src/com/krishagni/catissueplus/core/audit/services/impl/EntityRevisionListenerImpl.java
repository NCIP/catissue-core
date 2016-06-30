package com.krishagni.catissueplus.core.audit.services.impl;

import java.io.Serializable;

import org.hibernate.envers.EntityTrackingRevisionListener;
import org.hibernate.envers.RevisionType;

import com.krishagni.catissueplus.core.audit.domain.EntityRevision;
import com.krishagni.catissueplus.core.audit.domain.EntityRevisionDetail;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class EntityRevisionListenerImpl implements EntityTrackingRevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		EntityRevision revision = (EntityRevision) revisionEntity;
		
		if(AuthUtil.getCurrentUser() != null){
			revision.setUserId(AuthUtil.getCurrentUser().getId());
		}

		revision.setIpAddress(AuthUtil.getRemoteAddr());
	}

	@Override
	public void entityChanged(Class entityClass, String entityName, Serializable entityId, RevisionType revisionType, Object revisionEntity) {
		if (!(entityId instanceof Long)) {
			return;
		}

		EntityRevisionDetail detail = new EntityRevisionDetail();
		detail.setRevision((EntityRevision)revisionEntity);
		detail.setEntityName(entityClass.getName());
		detail.setType(revisionType.getRepresentation());
		detail.setEntityId((Long)entityId);
		((EntityRevision)revisionEntity).getDetails().add(detail);
	}
}