
package com.krishagni.catissueplus.core.auth.services.impl;

import com.krishagni.catissueplus.core.auth.events.LoginDetail;
import com.krishagni.catissueplus.core.auth.services.AuthenticationService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;

public abstract class AbstractAuthProvider implements AuthenticationService {
	protected DaoFactory getDaoFactory() {
		return (DaoFactory) OpenSpecimenAppCtxProvider.getAppCtx().getBean("biospecimenDaoFactory");
	}

	@Override
	public abstract void authenticate(LoginDetail loginDetail);
}
