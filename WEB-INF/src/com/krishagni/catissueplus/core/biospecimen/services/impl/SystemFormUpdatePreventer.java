package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.de.domain.FormErrorCode;
import com.krishagni.catissueplus.core.de.repository.FormDao;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.napi.FormEventsListener;

public class SystemFormUpdatePreventer implements FormEventsListener {

	private FormDao formDao;

	public SystemFormUpdatePreventer(FormDao formDao) {
		this.formDao = formDao;
	}

	@Override
	public void onCreate(Container form) {

	}

	@Override
	public void preUpdate(Container form) {
		if (AuthUtil.getCurrentUser().isSysUser()) {
			return;
		}

		if (formDao.isSystemForm(form.getId())) {
			throw OpenSpecimenException.userError(FormErrorCode.SYS_FORM_UPDATE_NOT_ALLOWED);
		}
	}

	@Override
	public void onUpdate(Container form) {

	}

	@Override
	public void onDelete(Container form) {

	}
}
