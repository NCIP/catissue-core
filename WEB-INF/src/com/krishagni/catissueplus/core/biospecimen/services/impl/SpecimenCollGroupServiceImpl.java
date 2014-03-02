
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

@Service(value = "specimenCollGroupService")
public class SpecimenCollGroupServiceImpl implements SpecimenCollGroupService {

	private DaoFactory daoFactory;
	private SpecimenService specimenSvc;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event) {

		try {
			return AllSpecimensSummaryEvent.ok(daoFactory.getSpecimenCollectionGroupDao().getSpecimensList(event.getParentId()));
		}
		catch (CatissueException e) {
			return AllSpecimensSummaryEvent.serverError(e);
		}
	}

	@Override
	public void deleteGroups(Long participantId) {
		specimenSvc.deleteSpecimens(participantId);
		daoFactory.getSpecimenCollectionGroupDao().deleteGroups(participantId);
	}

}
