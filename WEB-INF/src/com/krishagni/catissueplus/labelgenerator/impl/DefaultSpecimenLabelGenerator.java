
package com.krishagni.catissueplus.labelgenerator.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.labelgenerator.LabelGenerator;

public class DefaultSpecimenLabelGenerator extends AbstractLabelGenerator<Specimen> implements LabelGenerator<Specimen> {

	private static String SPECIMEN_UNIQUE_ID = "SPECIMEN_UNIQUE_ID";

	@Override
	public String generateLabel(Specimen specimen) {
		return getUniqueId(SPECIMEN_UNIQUE_ID);
	}
}
