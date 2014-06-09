
package com.krishagni.catissueplus.labelgenerator.impl;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.CaTissueAppContext;
import com.krishagni.catissueplus.core.common.util.BucketPool;
import com.krishagni.catissueplus.labelgenerator.LabelGenerator;

public class DefaultSpecimenLabelGenerator implements LabelGenerator<Specimen> {

	@Override
	public String generateLabel(Specimen specimen) {
		ApplicationContext caTissueContext = CaTissueAppContext.getInstance();
		BucketPool bucketPool = (BucketPool) caTissueContext.getBean("bucketPool");
		Long nextValue = bucketPool.getNextValue("SYS_UID");
		return nextValue.toString();
	}

}
