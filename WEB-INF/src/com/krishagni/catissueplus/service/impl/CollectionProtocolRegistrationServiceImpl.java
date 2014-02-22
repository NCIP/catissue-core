
package com.krishagni.catissueplus.service.impl;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.dao.CollectionProtocolRegistrationDao;
import com.krishagni.catissueplus.errors.CaTissueException;
import com.krishagni.catissueplus.events.specimencollectiongroups.AllSpecimenCollGroupsSummaryEvent;
import com.krishagni.catissueplus.events.specimencollectiongroups.ReqSpecimenCollGroupSummaryEvent;
import com.krishagni.catissueplus.service.CollectionProtocolRegistrationService;

@Service(value = "CollectionProtocolRegistrationServiceImpl")
public class CollectionProtocolRegistrationServiceImpl implements CollectionProtocolRegistrationService {

	private CollectionProtocolRegistrationDao collectionProtocolRegistrationDao;

	public CollectionProtocolRegistrationDao getCollectionProtocolRegistrationDao() {
		return collectionProtocolRegistrationDao;
	}

	public void setCollectionProtocolRegistrationDao(CollectionProtocolRegistrationDao collectionProtocolRegistrationDao) {
		this.collectionProtocolRegistrationDao = collectionProtocolRegistrationDao;
	}

	@Override
	@PlusTransactional
	public AllSpecimenCollGroupsSummaryEvent getSpecimenCollGroupsList(
			ReqSpecimenCollGroupSummaryEvent reqSpecimenCollGroupSummaryEvent) {

		try
		{
			return AllSpecimenCollGroupsSummaryEvent.ok(collectionProtocolRegistrationDao
					.getSpecimenCollectiongroupsList(reqSpecimenCollGroupSummaryEvent.getCollectionProtocolRegistrationId()));
		}
		catch (CaTissueException e)
		{
			return AllSpecimenCollGroupsSummaryEvent.serverError(e);
		}
		
	}

}
