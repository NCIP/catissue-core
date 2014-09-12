
package com.krishagni.catissueplus.core.tokens.impl;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.util.KeyGenFactory;
import com.krishagni.catissueplus.core.tokens.LabelToken;

public class SpecimenUniqueId implements LabelToken<Specimen> {

	private static final String SPECIMEN_UNIQUE_ID = "SPECIMEN_UNIQUE_ID";

	@Override
	public String getTokenValue(Specimen specimen) {
		ApplicationContext caTissueContext = OpenSpecimenAppCtxProvider.getAppCtx();
		KeyGenFactory keyFactory = (KeyGenFactory) caTissueContext.getBean("keyFactory");
		Long value = keyFactory.getValueByKey(SPECIMEN_UNIQUE_ID, SPECIMEN_UNIQUE_ID);
		return value.toString();
	}

}
