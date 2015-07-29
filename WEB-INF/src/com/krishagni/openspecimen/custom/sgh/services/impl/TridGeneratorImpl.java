package com.krishagni.openspecimen.custom.sgh.services.impl;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.openspecimen.custom.sgh.services.TridGenerator;


public class TridGeneratorImpl implements TridGenerator{

private DaoFactory daoFactory;
	
	private final String TRID_PREFIX = "z";

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public String getNextTrid() {
		return TRID_PREFIX + daoFactory.getUniqueIdGenerator().getUniqueId("TRID", "SGH");
	}

}
