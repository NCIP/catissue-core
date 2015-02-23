
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class VisitServiceImpl implements VisitService {
	private DaoFactory daoFactory;

	private VisitFactory visitFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setVisitFactory(VisitFactory visitFactory) {
		this.visitFactory = visitFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> getVisit(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			
			Visit visit = null;
			if (crit.getId() != null) {
				visit = daoFactory.getVisitsDao().getById(crit.getId());
			} else if (StringUtils.isNotBlank(crit.getName())) {
				visit = daoFactory.getVisitsDao().getVisitByName(crit.getName());
			}
			
			if (visit == null) {
				return ResponseEvent.userError(VisitErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(VisitDetail.from(visit));			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}	
	
	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> addVisit(RequestEvent<VisitDetail> req) {
		try {
			Visit visit = visitFactory.createVisit(req.getPayload());
			setName(visit);

			daoFactory.getVisitsDao().saveOrUpdate(visit);
			return ResponseEvent.response(VisitDetail.from(visit));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception ex) {
			return ResponseEvent.serverError(ex);
		}
	}

	private void setName(Visit visit) {
		visit.setName(UUID.randomUUID().toString()); // TODO: replace with actual logic
	}
}
