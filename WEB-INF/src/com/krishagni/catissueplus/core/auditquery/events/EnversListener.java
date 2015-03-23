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
		EnversRevisionEntity revEntity = (EnversRevisionEntity) revisionEntity;
		
		HttpServletRequest curRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		revEntity.setUserId(AuthUtil.getCurrentUser().getId());
		revEntity.setIpAddress(curRequest.getRemoteAddr());
	}
}