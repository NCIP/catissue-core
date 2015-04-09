package com.krishagni.catissueplus.core.auditquery.events;

import org.hibernate.envers.RevisionListener;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.krishagni.catissueplus.core.auditquery.domain.EnversRevisionEntity;
import com.krishagni.catissueplus.core.common.util.AuthUtil;

public class EnversListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		Long userId = null;
		EnversRevisionEntity revEntity = (EnversRevisionEntity) revisionEntity;
		
		HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		if(AuthUtil.getCurrentUser() != null){
			userId = AuthUtil.getCurrentUser().getId();
		}
		revEntity.setUserId(userId);
		revEntity.setIpAddress(curRequest.getRemoteAddr());
	}
}