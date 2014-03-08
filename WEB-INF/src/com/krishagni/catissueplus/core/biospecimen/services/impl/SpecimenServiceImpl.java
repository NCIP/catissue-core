
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.AllSpecimensSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteParticipantEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteRegistrationEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.events.DeleteSpecimenGroupsEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqSpecimenSummaryEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.CatissueException;

public class SpecimenServiceImpl implements SpecimenService {

	private DaoFactory daoFactory;

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
			return AllSpecimensSummaryEvent.ok(daoFactory.getSpecimenDao().getSpecimensList(event.getScgId()));
		}
		catch (CatissueException e) {
			return AllSpecimensSummaryEvent.serverError(e);
		}
	}

	@Override
	public void delete(DeleteParticipantEvent event) {
		if (event.isIncludeChildren()) {
			daoFactory.getSpecimenDao().deleteByParticipant(event.getId());
			return;
		}
		else if (daoFactory.getSpecimenDao().checkActiveChildrenForParticipant(event.getId())) {
			throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
		}
		daoFactory.getSpecimenDao().deleteByParticipant(event.getId());

	}

	@Override
	public void delete(DeleteRegistrationEvent event) {
		if (event.isIncludeChildren()) {
			daoFactory.getSpecimenDao().deleteByRegistration(event.getId());
			return;
		}
		else if (daoFactory.getSpecimenDao().checkActiveChildrenForRegistration(event.getId())) {
			throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
		}
		daoFactory.getSpecimenDao().deleteByRegistration(event.getId());

	}

	@Override
	public void delete(DeleteSpecimenGroupsEvent event) {
		if (event.isIncludeChildren()) {
			daoFactory.getSpecimenDao().deleteBycollectionGroup(event.getId());
			return;
		}
		else if (daoFactory.getSpecimenDao().checkActiveChildrenForCollGroup(event.getId())) {
			throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
		}
		daoFactory.getSpecimenDao().deleteByRegistration(event.getId());

	}

	@Override
	public void delete(DeleteSpecimenEvent event) {
		if (event.isIncludeChildren()) {
			daoFactory.getSpecimenDao().delete(event.getId());
			return;
		}
		else if (daoFactory.getSpecimenDao().checkActiveChildren(event.getId())) {
			throw new CatissueException(ParticipantErrorCode.ACTIVE_CHILDREN_FOUND);
		}
		daoFactory.getSpecimenDao().deleteByRegistration(event.getId());
	}
}
