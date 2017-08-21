package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import com.krishagni.catissueplus.core.biospecimen.repository.LabelPrintJobDao;
import com.krishagni.catissueplus.core.common.domain.LabelPrintJob;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class LabelPrintJobDaoImpl extends AbstractDao<LabelPrintJob> implements LabelPrintJobDao {

	@Override
	public Class<LabelPrintJob> getType() {
		return LabelPrintJob.class;
	}

}
