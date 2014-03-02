
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenCollGroupService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

@Service(value = "CollectionProtocolRegistrationServiceImpl")
public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService {

	private DaoFactory daoFactory;
	
	private SpecimenCollGroupService specimenCollGroupSvc;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public AllSpecimenCollGroupsSummaryEvent getSpecimenCollGroupsList(
			ReqSpecimenCollGroupSummaryEvent reqSpecimenCollGroupSummaryEvent) {

		try {
			return AllSpecimenCollGroupsSummaryEvent.ok(daoFactory.getRegistrationDao().getSpecimenCollectiongroupsList(
					reqSpecimenCollGroupSummaryEvent.getCollectionProtocolRegistrationId()));
		}
		catch (CatissueException e) {
			return AllSpecimenCollGroupsSummaryEvent.serverError(e);
		}

	}

	@Override
	public void delete(Long participantId) {
		specimenCollGroupSvc.deleteGroups(participantId);
		daoFactory.getRegistrationDao().delete(participantId);
	}


}
