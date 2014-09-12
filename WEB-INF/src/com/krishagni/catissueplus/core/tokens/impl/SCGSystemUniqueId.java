
package com.krishagni.catissueplus.core.tokens.impl;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.util.KeyGenFactory;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class SCGSystemUniqueId implements LabelToken<SpecimenCollectionGroup> {

	private static String SCG_UNIQUE_ID = "SCG_UID";

	@Override
	public String getTokenValue(SpecimenCollectionGroup scg) {
		ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
		KeyGenFactory keyFactory = (KeyGenFactory) caTissueContext.getBean("keyFactory");
		Long value = keyFactory.getValueByKey(SCG_UNIQUE_ID, SCG_UNIQUE_ID);
		return value.toString();
	}

}
