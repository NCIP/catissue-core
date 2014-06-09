
package com.krishagni.catissueplus.labelgenerator.impl;

import org.springframework.context.ApplicationContext;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.common.CaTissueAppContext;
import com.krishagni.catissueplus.core.common.util.BucketPool;
import com.krishagni.catissueplus.labelgenerator.LabelGenerator;

public class SCGLabelGenerator implements LabelGenerator<SpecimenCollectionGroup> {

	private static String SCG_SYSTEM_UNIQUE_ID = "SCG_SYS_UID";

	private static String BUCKET_POOL = "bucketPool";

	private static int MAX_TITLE_SIZE_IN_LABEL = 30;

	@Override
	public String generateLabel(SpecimenCollectionGroup scg) {
		String cpShortTitle = scg.getCollectionProtocolRegistration().getCollectionProtocol().getShortTitle();
		String lblCpShortTitle = cpShortTitle.length() > MAX_TITLE_SIZE_IN_LABEL ? cpShortTitle.substring(0,
				(MAX_TITLE_SIZE_IN_LABEL - 1)) : cpShortTitle;
		Long participantId = scg.getCollectionProtocolRegistration().getParticipant().getId();
		ApplicationContext caTissueContext = CaTissueAppContext.getInstance();
		BucketPool bucketPool = (BucketPool) caTissueContext.getBean(BUCKET_POOL);
		Long uniqueId = bucketPool.getNextValue(SCG_SYSTEM_UNIQUE_ID);
		String label = lblCpShortTitle + "_" + participantId + "_" + uniqueId;
		return label;
	}

}
