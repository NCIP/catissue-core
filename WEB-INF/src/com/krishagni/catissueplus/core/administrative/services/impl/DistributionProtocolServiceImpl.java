
package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetailEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqDistributionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class DistributionProtocolServiceImpl implements DistributionProtocolService {

	private DaoFactory daoFactory;

	private DistributionProtocolFactory distributionProtocolFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDistributionProtocolFactory(DistributionProtocolFactory distributionProtocolFactory) {
		this.distributionProtocolFactory = distributionProtocolFactory;
	}
	
	@Override
	@PlusTransactional
	public DistributionProtocolsEvent getDistributionProtocols(ReqDistributionProtocolsEvent req) {
		DpListCriteria criteria = new DpListCriteria()
				.query(req.getSearchString())
				.startAt(req.getStartAt())
				.maxResults(req.getMaxResults());

		return DistributionProtocolsEvent.ok(
				DistributionProtocolDetail.from(daoFactory.getDistributionProtocolDao().getDistributionProtocols(criteria)));
	}
	
	@Override
	@PlusTransactional
	public DistributionProtocolDetailEvent getDistributionProtocol(ReqDistributionProtocolEvent req) {
		Long protocolId = req.getId();
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(protocolId);
		if (existing == null) {
			return DistributionProtocolDetailEvent.notFound(protocolId);
		}
		
		return DistributionProtocolDetailEvent.ok(DistributionProtocolDetail.from(existing));
	}

	@Override
	@PlusTransactional
	public DistributionProtocolCreatedEvent createDistributionProtocol(CreateDistributionProtocolEvent req) {
		try {
			DistributionProtocol distributionProtocol = distributionProtocolFactory.createDistributionProtocol(req.getProtocol());
			ensureUniqueConstraints(distributionProtocol);
			
			daoFactory.getDistributionProtocolDao().saveOrUpdate(distributionProtocol);
			return DistributionProtocolCreatedEvent.ok(DistributionProtocolDetail.from(distributionProtocol));
		} catch (ObjectCreationException oce) {
			return DistributionProtocolCreatedEvent.badRequest(oce);
		} catch (Exception e) {
			return DistributionProtocolCreatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public DistributionProtocolUpdatedEvent updateDistributionProtocol(UpdateDistributionProtocolEvent req) {
		try {
			Long protocolId = req.getId();
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(protocolId);
			if (existing == null) {
				return DistributionProtocolUpdatedEvent.notFound(protocolId);
			}
		
			req.getProtocol().setId(req.getId());
			DistributionProtocol distributionProtocol = distributionProtocolFactory.createDistributionProtocol(req.getProtocol());
			ensureUniqueConstraints(distributionProtocol);
			
			existing.update(distributionProtocol);
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return DistributionProtocolUpdatedEvent.ok(DistributionProtocolDetail.from(existing));
		} catch (ObjectCreationException oce) {
			return DistributionProtocolUpdatedEvent.badRequest(oce);
		} catch (Exception ex) {
			return DistributionProtocolUpdatedEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public DistributionProtocolDeletedEvent deleteDistributionProtocol(DeleteDistributionProtocolEvent req) {
		try {
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(req.getId());
			if (existing == null) {
				return DistributionProtocolDeletedEvent.notFound(req.getId());
			}

			existing.delete();
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return DistributionProtocolDeletedEvent.ok(DistributionProtocolDetail.from(existing));
		}
		catch (Exception e) {
			return DistributionProtocolDeletedEvent.serverError(e);
		}
	}
	
	private void ensureUniqueConstraints(DistributionProtocol distributionProtocol) {
		ObjectCreationException oce = new ObjectCreationException();
		
		if (!isUniqueTitle(distributionProtocol)) {
			oce.addError(DistributionProtocolErrorCode.NOT_UNIQUE, "title");
		}
		
		if (!isUniqueShortTitle(distributionProtocol)) {
			oce.addError(DistributionProtocolErrorCode.NOT_UNIQUE, "short-title");
		}
		
		oce.checkErrorAndThrow();
	}
	
	private boolean isUniqueTitle(DistributionProtocol distributionProtocol) {
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(distributionProtocol.getTitle());
		if (existing == null) {
			return true; // no dp by this name
		} else if (distributionProtocol.getId() == null) {
			return false; // a different dp by this name exists 
		} else if (existing.getId().equals(distributionProtocol.getId())) {
			return true; // same dp
		} else {
			return false;
		}
	}
	
	private boolean isUniqueShortTitle(DistributionProtocol distributionProtocol) {
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getByShortTitle(distributionProtocol.getShortTitle());
		if (existing == null) {
			return true; // no dp by this short title
		} else if (distributionProtocol.getId() == null) {
			return false; // a different dp by this short-title exists 
		} else if (existing.getId().equals(distributionProtocol.getId())) {
			return true; // same dp
		} else {
			return false;
		}
	}
}
