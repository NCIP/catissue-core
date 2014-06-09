
package com.krishagni.catissueplus.core.administrative.services.impl;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.events.CreateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetails;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.administrative.events.DisableStorageContainerEvent;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class StorageContainerServiceImpl implements StorageContainerService {

	private static final String STORAGE_CONTAINER = "storage container";

	private static final String BARCODE = "barcode";

	private DaoFactory daoFactory;

	private StorageContainerFactory storageContainerFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setStorageContainerFactory(StorageContainerFactory storageContainerFactory) {
		this.storageContainerFactory = storageContainerFactory;
	}

	@Override
	@PlusTransactional
	public StorageContainerCreatedEvent createStorageContainer(CreateStorageContainerEvent event) {
		try {
			StorageContainer storageContainer = storageContainerFactory.createStorageContainer(event.getDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			ensureUniqueName(storageContainer.getName(), exceptionHandler);
			ensureUniqueBarcode(storageContainer.getBarcode(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();
			daoFactory.getStorageContainerDao().saveOrUpdate(storageContainer);
			return StorageContainerCreatedEvent.ok(StorageContainerDetails.fromDomain(storageContainer));
		}
		catch (ObjectCreationException ce) {
			return StorageContainerCreatedEvent.invalidRequest(StorageContainerErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return StorageContainerCreatedEvent.serverError(e);
		}
	}

	private void ensureUniqueName(String name, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getStorageContainerDao().isUniqueContainerName(name)) {
			exceptionHandler.addError(StorageContainerErrorCode.DUPLICATE_CONTAINER_NAME, STORAGE_CONTAINER);
		}
	}

	private void ensureUniqueBarcode(String barcode, ObjectCreationException exceptionHandler) {
		if (barcode != null && !daoFactory.getStorageContainerDao().isUniqueBarcode(barcode)) {
			exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, BARCODE);
			return;
		}
	}

	@Override
	@PlusTransactional
	public StorageContainerUpdatedEvent updateStorageContainer(UpdateStorageContainerEvent event) {
		try {
			Long storageContainerId = event.getDetails().getId();
			StorageContainer oldStorageContainer = daoFactory.getStorageContainerDao()
					.getStorageContainer(storageContainerId);
			if (oldStorageContainer == null) {
				return StorageContainerUpdatedEvent.notFound(storageContainerId);
			}
			StorageContainer storageContainer = storageContainerFactory.createStorageContainer(event.getDetails());

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			checkUniqueName(oldStorageContainer, storageContainer, exceptionHandler);
			checkUniqueBarcode(oldStorageContainer, storageContainer, exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			oldStorageContainer.update(storageContainer);
			daoFactory.getStorageContainerDao().saveOrUpdate(oldStorageContainer);
			return StorageContainerUpdatedEvent.ok(StorageContainerDetails.fromDomain(oldStorageContainer));
		}
		catch (ObjectCreationException ce) {
			return StorageContainerUpdatedEvent.invalidRequest(StorageContainerErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return StorageContainerUpdatedEvent.serverError(e);
		}
	}

	private void checkUniqueBarcode(StorageContainer oldStorageContainer, StorageContainer storageContainer,
			ObjectCreationException exceptionHandler) {
		if (!oldStorageContainer.getBarcode().equals(storageContainer.getBarcode())) {
			ensureUniqueName(storageContainer.getName(), exceptionHandler);
		}
	}

	private void checkUniqueName(StorageContainer oldStorageContainer, StorageContainer storageContainer,
			ObjectCreationException exceptionHandler) {
		if (!oldStorageContainer.getName().equals(storageContainer.getName())) {
			ensureUniqueBarcode(storageContainer.getBarcode(), exceptionHandler);
		}
	}

	@Override
	@PlusTransactional
	public StorageContainerDisabledEvent disableStorageContainer(DisableStorageContainerEvent event) {
		try {
			StorageContainer oldStorageContainer = daoFactory.getStorageContainerDao().getStorageContainer(event.getId());
			if (oldStorageContainer == null) {
				return StorageContainerDisabledEvent.notFound(event.getId());
			}
			oldStorageContainer.disable();
			daoFactory.getStorageContainerDao().saveOrUpdate(oldStorageContainer);
			return StorageContainerDisabledEvent.ok();
		}
		catch (Exception e) {
			return StorageContainerDisabledEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public StorageContainerUpdatedEvent patchStorageContainer(PatchStorageContainerEvent event) {
		try {
			Long storageContainerId = event.getStorageContainerId();
			StorageContainer oldStorageContainer = daoFactory.getStorageContainerDao().getStorageContainer(storageContainerId);
			if (oldStorageContainer == null) {
				return StorageContainerUpdatedEvent.notFound(storageContainerId);
			}
			StorageContainer storageContainer = storageContainerFactory.patchStorageContainer(oldStorageContainer, event.getStorageContainerDetails());

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			checkUniqueName(oldStorageContainer, storageContainer, exceptionHandler);
			checkUniqueBarcode(oldStorageContainer, storageContainer, exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			daoFactory.getStorageContainerDao().saveOrUpdate(storageContainer);
			return StorageContainerUpdatedEvent.ok(StorageContainerDetails.fromDomain(storageContainer));
		}
		catch (ObjectCreationException ce) {
			return StorageContainerUpdatedEvent.invalidRequest(StorageContainerErrorCode.ERRORS.message(), ce.getErroneousFields());
		}
		catch (Exception e) {
			return StorageContainerUpdatedEvent.serverError(e);
		}
	}

}
