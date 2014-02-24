package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.errors.CaTissueException;
import com.krishagni.catissueplus.events.specimens.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.events.specimens.ReqSpecimenSummaryEvent;

public class SpecimenServiceImpl implements SpecimenService {
	
	private DaoFactory daoFactory;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
//	@Override
//	@PlusTransactional
//	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event) {
//		try {
//			return AllSpecimensSummaryEvent.ok(daoFactory.getSpecimenDao().getSpecimensList(event.getParentId()));
//		}
//		catch (CaTissueException e) {
//			return AllSpecimensSummaryEvent.serverError(e);
//		}
//	}
}
