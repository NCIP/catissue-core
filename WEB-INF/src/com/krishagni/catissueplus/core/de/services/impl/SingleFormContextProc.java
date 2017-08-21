package com.krishagni.catissueplus.core.de.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.de.domain.FormErrorCode;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.repository.FormDao;
import com.krishagni.catissueplus.core.de.services.FormContextProcessor;

import krishagni.catissueplus.beans.FormContextBean;

@Configurable
public class SingleFormContextProc implements FormContextProcessor {

	@Autowired
	private FormDao formDao;

	@Override
	public void onSaveOrUpdate(FormContextBean formCtxt) {
		List<FormCtxtSummary> contexts = formDao.getFormContexts(formCtxt.getCpId(), formCtxt.getEntityType());
		if (CollectionUtils.isNotEmpty(contexts)) {
			throw OpenSpecimenException.userError(FormErrorCode.MULTIPLE_CTXS_NOT_ALLOWED);
		}

		return;
	}

	@Override
	public void onRemove(FormContextBean formCtxt) {

	}
}