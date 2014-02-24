
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.dao.SpecimenCollectionGroupDao;
import com.krishagni.catissueplus.errors.CaTissueException;
import com.krishagni.catissueplus.events.specimens.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.events.specimens.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.service.SpecimenCollGroupService;

@Service(value = "specimenCollGroupService")
public class SpecimenCollGroupServiceImpl implements SpecimenCollGroupService {

	private SpecimenCollectionGroupDao specimenCollectionGroupDao;

	public SpecimenCollectionGroupDao getSpecimenCollectionGroupDao() {
		return specimenCollectionGroupDao;
	}

	public void setSpecimenCollectionGroupDao(SpecimenCollectionGroupDao specimenCollectionGroupDao) {
		this.specimenCollectionGroupDao = specimenCollectionGroupDao;
	}

	@Override
	@PlusTransactional
	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event) {

		try {
			return AllSpecimensSummaryEvent.ok(specimenCollectionGroupDao.getSpecimensList(event.getParentId()));
		}
		catch (CaTissueException e) {
			return AllSpecimensSummaryEvent.serverError(e);
		}
	}

}
