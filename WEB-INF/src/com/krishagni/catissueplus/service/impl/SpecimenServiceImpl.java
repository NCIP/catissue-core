
package com.krishagni.catissueplus.service.impl;

import org.springframework.stereotype.Service;

import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.dao.SpecimenDao;
import com.krishagni.catissueplus.errors.CaTissueException;
import com.krishagni.catissueplus.events.specimens.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.events.specimens.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.service.SpecimenService;

@Service(value = "SpecimenService")
public class SpecimenServiceImpl implements SpecimenService {

	private SpecimenDao specimenDao;

	public SpecimenDao getSpecimenDao() {
		return specimenDao;
	}

	public void setSpecimenDao(SpecimenDao specimenDao) {
		this.specimenDao = specimenDao;
	}

	@Override
	@PlusTransactional
	public AllSpecimensSummaryEvent getSpecimensList(ReqSpecimenSummaryEvent event) {
		try {
			return AllSpecimensSummaryEvent.ok(specimenDao.getSpecimensList(event.getParentId()));
		}
		catch (CaTissueException e) {
			return AllSpecimensSummaryEvent.serverError(e);
		}
	}

}
