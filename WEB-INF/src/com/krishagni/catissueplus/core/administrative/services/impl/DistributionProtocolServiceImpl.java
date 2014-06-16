
package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.events.CreateDistributionProtocolEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetails;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolPatchedEvent;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchDistributionProtocolEvent;
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
	public DistributionProtocolCreatedEvent createDistributionProtocol(CreateDistributionProtocolEvent event) {
		try {
			DistributionProtocol distributionProtocol = distributionProtocolFactory.create(event
					.getDistributionProtocolDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueTitle(distributionProtocol.getTitle(), exceptionHandler);
			ensureUniqueShortTitle(distributionProtocol.getShortTitle(), exceptionHandler);

			exceptionHandler.checkErrorAndThrow();
			daoFactory.getDistributionProtocolDao().saveOrUpdate(distributionProtocol);
			return DistributionProtocolCreatedEvent.ok(DistributionProtocolDetails.fromDomain(distributionProtocol));
		}
		catch (ObjectCreationException exception) {
			return DistributionProtocolCreatedEvent.invalidRequest(exception.getMessage(), exception.getErroneousFields());
		}
		catch (Exception e) {
			return DistributionProtocolCreatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public DistributionProtocolUpdatedEvent updateDistributionProtocol(UpdateDistributionProtocolEvent event) {
		try {
			Long id = event.getId();
			DistributionProtocol oldDistributionProtocol = daoFactory.getDistributionProtocolDao()
					.getDistributionProtocol(id);

			if (oldDistributionProtocol == null) {
				return DistributionProtocolUpdatedEvent.notFound(id);
			}

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			DistributionProtocol distributionProtocol = distributionProtocolFactory.create(event.getDetails());
			validateShortTitle(oldDistributionProtocol.getShortTitle(), distributionProtocol.getShortTitle(),
					exceptionHandler);
			validateTitle(oldDistributionProtocol.getTitle(), distributionProtocol.getTitle(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();
			oldDistributionProtocol.update(distributionProtocol);
			daoFactory.getDistributionProtocolDao().saveOrUpdate(oldDistributionProtocol);

			return DistributionProtocolUpdatedEvent.ok(DistributionProtocolDetails.fromDomain(oldDistributionProtocol));
		}
		catch (ObjectCreationException exception) {
			return DistributionProtocolUpdatedEvent.invalidRequest(DistributionProtocolErrorCode.ERRORS.message(),
					exception.getErroneousFields());
		}
		catch (Exception ex) {
			return DistributionProtocolUpdatedEvent.serverError(ex);
		}
	}

	@Override
	@PlusTransactional
	public DistributionProtocolPatchedEvent patchDistributionProtocol(PatchDistributionProtocolEvent reqEvent) {
		try {
			Long id = reqEvent.getId();
			DistributionProtocol oldDistributionProtocol = daoFactory.getDistributionProtocolDao()
					.getDistributionProtocol(id);
			if (oldDistributionProtocol == null) {
				return DistributionProtocolPatchedEvent.notFound(id);
			}
			String oldShortTitle = oldDistributionProtocol.getShortTitle();
			String oldTitle = oldDistributionProtocol.getTitle();
			DistributionProtocol distributionProtocol = distributionProtocolFactory.patch(oldDistributionProtocol,
					reqEvent.getDetails());

			ObjectCreationException exceptionHandler = new ObjectCreationException();

			validateShortTitle(oldShortTitle, reqEvent.getDetails().getShortTitle(), exceptionHandler);
			validateTitle(oldTitle, reqEvent.getDetails().getTitle(), exceptionHandler);

			exceptionHandler.checkErrorAndThrow();

			daoFactory.getDistributionProtocolDao().saveOrUpdate(distributionProtocol);
			return DistributionProtocolPatchedEvent.ok(DistributionProtocolDetails.fromDomain(distributionProtocol));
		}
		catch (ObjectCreationException exception) {
			return DistributionProtocolPatchedEvent.invalidRequest(DistributionProtocolErrorCode.ERRORS.message(),
					exception.getErroneousFields());
		}
		catch (Exception ex) {
			return DistributionProtocolPatchedEvent.serverError(ex);
		}
	}

	private void validateTitle(String oldTitle, String newTitle, ObjectCreationException exceptionHandler) {
		if (!oldTitle.equalsIgnoreCase(newTitle)) {
			ensureUniqueTitle(newTitle, exceptionHandler);
		}
	}

	private void validateShortTitle(String oldShortTitle, String newShortTitle, ObjectCreationException exceptionHandler) {
		if (!oldShortTitle.equalsIgnoreCase(newShortTitle)) {
			ensureUniqueShortTitle(newShortTitle, exceptionHandler);
		}
	}

	private void ensureUniqueShortTitle(String shortTitle, ObjectCreationException exceptionHandler) {

		if (!daoFactory.getDistributionProtocolDao().isUniqueShortTitle(shortTitle)) {
			exceptionHandler.addError(DistributionProtocolErrorCode.DUPLICATE_PROTOCOL_SHORT_TITLE, SHORT_TITLE);
		}

	}

	private void ensureUniqueTitle(String title, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getDistributionProtocolDao().isUniqueTitle(title)) {
			exceptionHandler.addError(DistributionProtocolErrorCode.DUPLICATE_PROTOCOL_TITLE, TITLE);
		}

	}

}
