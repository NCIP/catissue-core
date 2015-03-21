package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenLabelPrintJob;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenLabelPrintJobDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenLabelPrintJobDaoImpl extends AbstractDao<SpecimenLabelPrintJob> implements SpecimenLabelPrintJobDao {

	@Override
	public Class<SpecimenLabelPrintJob> getType() {
		return SpecimenLabelPrintJob.class;
	}

}
