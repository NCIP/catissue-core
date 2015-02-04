
package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.events.AllDistributionProtocolsEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetailEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class DistributionProtocolServiceImpl implements DistributionProtocolService {

	private static final String SHORT_TITLE = "distribution protocol short title";

	private static final String TITLE = "distribution protocol title";

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
	public DistributionProtocolCreatedEvent createDistributionProtocol(CreateDistributionProtocolEvent req) {
		try {
			DistributionProtocol distributionProtocol = distributionProtocolFactory.create(
					req.getDistributionProtocolDetails());
			ObjectCreationException oce = new ObjectCreationException();
			ensureUniqueTitle(distributionProtocol.getTitle(), oce);
			ensureUniqueShortTitle(distributionProtocol.getShortTitle(), oce);

			oce.checkErrorAndThrow();
			daoFactory.getDistributionProtocolDao().saveOrUpdate(distributionProtocol);
			return DistributionProtocolCreatedEvent.ok(DistributionProtocolDetails.from(distributionProtocol));
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
			DistributionProtocol existing;

			if (req.getId() != null) {
				Long id = req.getId();
				existing = daoFactory.getDistributionProtocolDao().getById(id);

				if (existing == null) {
					return DistributionProtocolUpdatedEvent.notFound(id);
				}
			} else {
				String title = req.getTitle();
				existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(title);

				if (existing == null) {
					return DistributionProtocolUpdatedEvent.notFound(title);
				}
			}

			ObjectCreationException oce = new ObjectCreationException();
			DistributionProtocol distributionProtocol = distributionProtocolFactory.create(req.getDetails());
			checkTitle(existing.getTitle(), distributionProtocol.getTitle(), oce);
			checkShortTitle(existing.getShortTitle(), distributionProtocol.getShortTitle(), oce);
			oce.checkErrorAndThrow();
			
			existing.update(distributionProtocol);
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return DistributionProtocolUpdatedEvent.ok(DistributionProtocolDetails.from(existing));
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
			DistributionProtocol existing;

			if (req.getId() != null) {
				existing = daoFactory.getDistributionProtocolDao().getById(req.getId());
				if (existing == null) {
					return DistributionProtocolDeletedEvent.notFound(req.getId());
				}
			} else {
				existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(req.getTitle());
				if (existing == null) {
					return DistributionProtocolDeletedEvent.notFound(req.getTitle());
				}
			}

			existing.delete();
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return DistributionProtocolDeletedEvent.ok(DistributionProtocolDetails.from(existing));
		}
		catch (Exception e) {
			return DistributionProtocolDeletedEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public AllDistributionProtocolsEvent getAllDistributionProtocols(ReqAllDistributionProtocolEvent req) {
		return AllDistributionProtocolsEvent.ok(
				DistributionProtocolDetails.from(daoFactory.getDistributionProtocolDao()
				.getAllDistributionProtocol(req.getStartAt(), req.getMaxResults())));
	}

	@Override
	@PlusTransactional
	public DistributionProtocolDetailEvent getDistributionProtocol(ReqDistributionProtocolEvent req) {
		DistributionProtocol existing;
		
		if (req.getId() != null) {
			existing = daoFactory.getDistributionProtocolDao().getById(req.getId());
			if (existing == null) {
				return DistributionProtocolDetailEvent.notFound(req.getId());
			}
		} else {
			existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(req.getTitle());
			if (existing == null) {
				return DistributionProtocolDetailEvent.notFound(req.getTitle());
			}
		}

		return DistributionProtocolDetailEvent.ok(DistributionProtocolDetails.from(existing));
	}

	private void checkTitle(String oldTitle, String newTitle, ObjectCreationException oce) {
		if (!oldTitle.equals(newTitle)) {
			ensureUniqueTitle(newTitle, oce);
		}
	}

	private void checkShortTitle(String oldShortTitle, String newShortTitle, ObjectCreationException oce) {
		if (!oldShortTitle.equals(newShortTitle)) {
			ensureUniqueShortTitle(newShortTitle, oce);
		}
	}

	private void ensureUniqueShortTitle(String shortTitle, ObjectCreationException oce) {
		if (daoFactory.getDistributionProtocolDao().getByShortTitle(shortTitle) != null) {
			oce.addError(DistributionProtocolErrorCode.DUPLICATE_PROTOCOL_SHORT_TITLE, SHORT_TITLE);
		}
	}

	private void ensureUniqueTitle(String title, ObjectCreationException oce) {
		if (daoFactory.getDistributionProtocolDao().getDistributionProtocol(title) != null) {
			oce.addError(DistributionProtocolErrorCode.DUPLICATE_PROTOCOL_TITLE, TITLE);
		}
	}

}
