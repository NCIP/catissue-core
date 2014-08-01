
package com.krishagni.catissueplus.core.administrative.services.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.events.CreateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.DisableStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.GetStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqAllStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetails;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDisabledEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerGotEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.barcodegenerator.BarcodeGenerator;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.labelgenerator.LabelGenerator;

public class StorageContainerServiceImpl implements StorageContainerService {

	private static final String STORAGE_CONTAINER = "storage container";

	private static final String BARCODE = "barcode";

	private static final String CONTAINER_NAME = "name";

	private static final String DEFAULT_BARCODE_TOKEN = "CONTAINER_LABEL";

	private DaoFactory daoFactory;

	private StorageContainerFactory storageContainerFactory;

	private LabelGenerator<StorageContainer> containerLabelGenerator;

	private BarcodeGenerator<StorageContainer> containerBarcodeGenerator;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setStorageContainerFactory(StorageContainerFactory storageContainerFactory) {
		this.storageContainerFactory = storageContainerFactory;
	}

	public void setContainerLabelGenerator(LabelGenerator<StorageContainer> containerLabelGenerator) {
		this.containerLabelGenerator = containerLabelGenerator;
	}

	public void setContainerBarcodeGenerator(BarcodeGenerator<StorageContainer> containerBarcodeGenerator) {
		this.containerBarcodeGenerator = containerBarcodeGenerator;
	}

	@Override
	@PlusTransactional
	public StorageContainerCreatedEvent createStorageContainer(CreateStorageContainerEvent event) {
		try {
			StorageContainer storageContainer = storageContainerFactory.createStorageContainer(event.getDetails());
			ObjectCreationException exceptionHandler = new ObjectCreationException();
			setName(event.getDetails().getName(), storageContainer, exceptionHandler);
			setBarcode(event.getDetails().getBarcode(), storageContainer, exceptionHandler);
			ensureUniqueName(storageContainer.getName(), exceptionHandler);
			ensureUniqueBarcode(storageContainer.getBarcode(), exceptionHandler);
			exceptionHandler.checkErrorAndThrow();
			daoFactory.getStorageContainerDao().saveOrUpdate(storageContainer);
			return StorageContainerCreatedEvent.ok(StorageContainerDetails.fromDomain(storageContainer));
		}
		catch (ObjectCreationException ce) {
			return StorageContainerCreatedEvent.invalidRequest(StorageContainerErrorCode.ERRORS.message(),
					ce.getErroneousFields());
		}
		catch (Exception e) {
			return StorageContainerCreatedEvent.serverError(e);
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
			updateName(event.getDetails().getName(), storageContainer, oldStorageContainer, exceptionHandler);
			updateBarcode(event.getDetails().getBarcode(), storageContainer, oldStorageContainer, exceptionHandler);
			checkUniqueName(oldStorageContainer, storageContainer, exceptionHandler);
			checkUniqueBarcode(oldStorageContainer, storageContainer, exceptionHandler);
			exceptionHandler.checkErrorAndThrow();

			oldStorageContainer.update(storageContainer);
			daoFactory.getStorageContainerDao().saveOrUpdate(oldStorageContainer);
			return StorageContainerUpdatedEvent.ok(StorageContainerDetails.fromDomain(oldStorageContainer));
		}
		catch (ObjectCreationException ce) {
			return StorageContainerUpdatedEvent.invalidRequest(StorageContainerErrorCode.ERRORS.message(),
					ce.getErroneousFields());
		}
		catch (Exception e) {
			return StorageContainerUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public StorageContainerDisabledEvent disableStorageContainer(DisableStorageContainerEvent event) {
		try {
			StorageContainer oldStorageContainer = null;
			if (event.getName() != null) {
				oldStorageContainer = daoFactory.getStorageContainerDao().getStorageContainerByName(event.getName());
				if (oldStorageContainer == null) {
					return StorageContainerDisabledEvent.notFound(event.getName());
				}
			}
			else {
				oldStorageContainer = daoFactory.getStorageContainerDao().getStorageContainer(event.getId());
				if (oldStorageContainer == null) {
					return StorageContainerDisabledEvent.notFound(event.getId());
				}
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
			StorageContainer oldStorageContainer = daoFactory.getStorageContainerDao()
					.getStorageContainer(storageContainerId);
			if (oldStorageContainer == null) {
				return StorageContainerUpdatedEvent.notFound(storageContainerId);
			}
			StorageContainer storageContainer = storageContainerFactory.patchStorageContainer(oldStorageContainer,
					event.getStorageContainerDetails());

			ObjectCreationException exceptionHandler = new ObjectCreationException();
			if (event.getStorageContainerDetails().isNameModified()) {
				updateName(event.getStorageContainerDetails().getName(), storageContainer, oldStorageContainer,
						exceptionHandler);
			}
			if (event.getStorageContainerDetails().isBarcodeModified()) {
				updateBarcode(event.getStorageContainerDetails().getBarcode(), storageContainer, oldStorageContainer,
						exceptionHandler);
			}
			checkUniqueName(oldStorageContainer, storageContainer, exceptionHandler);
			checkUniqueBarcode(oldStorageContainer, storageContainer, exceptionHandler);
			exceptionHandler.checkErrorAndThrow(); 
			
			daoFactory.getStorageContainerDao().saveOrUpdate(storageContainer);
			return StorageContainerUpdatedEvent.ok(StorageContainerDetails.fromDomain(storageContainer));
		}
		catch (ObjectCreationException ce) {
			return StorageContainerUpdatedEvent.invalidRequest(StorageContainerErrorCode.ERRORS.message(),
					ce.getErroneousFields());
		}
		catch (Exception e) {
			return StorageContainerUpdatedEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public GetAllStorageContainersEvent getStorageContainers(ReqAllStorageContainersEvent event) {
		List<StorageContainer> containerList = daoFactory.getStorageContainerDao().getAllStorageContainers(event.getMaxResults());
		List<StorageContainerSummary> details = new ArrayList<StorageContainerSummary>();
		for (StorageContainer container : containerList) {
			details.add(StorageContainerSummary.fromDomain(container));
		}
		return GetAllStorageContainersEvent.ok(details);
	}

	@Override
	@PlusTransactional
	public StorageContainerGotEvent getStorageContainer(GetStorageContainerEvent event) {
		try {
			StorageContainer storageContainer = null;
			if (event.getName() != null) {
				storageContainer = daoFactory.getStorageContainerDao().getStorageContainerByName(event.getName());
				if (storageContainer == null) {
					return StorageContainerGotEvent.notFound(event.getName());
				}
			} else {
				storageContainer = daoFactory.getStorageContainerDao().getStorageContainer(event.getId());
				if (storageContainer == null) {
					return StorageContainerGotEvent.notFound(event.getId());
				}
			}
			return StorageContainerGotEvent.ok(StorageContainerDetails.fromDomain(storageContainer));
		}
		catch (Exception e) {
			return StorageContainerGotEvent.serverError(e);
		}
	}

	private void setName(String name, StorageContainer container, ObjectCreationException exceptionHandler) {

		String labelFormat = "CONTAINER_UID";
		if (isBlank(labelFormat)) {
			if (isBlank(name)) {
				exceptionHandler.addError(StorageContainerErrorCode.MISSING_ATTR_VALUE, CONTAINER_NAME);
				return;
			}
			container.setName(name);
		}
		else {
			if (!isBlank(name)) {
				exceptionHandler.addError(StorageContainerErrorCode.AUTO_GENERATED_LABEL, CONTAINER_NAME);
				return;
			}
			container.setName(containerLabelGenerator.generateLabel(labelFormat, container));
		}
	}

	private void updateName(String name, StorageContainer container, StorageContainer oldContainer,
			ObjectCreationException exceptionHandler) {

		String labelFormat = "CONTAINER_UID";
		if (isBlank(labelFormat)) {
			if (isBlank(name)) {
				exceptionHandler.addError(StorageContainerErrorCode.MISSING_ATTR_VALUE, CONTAINER_NAME);
				return;
			}
			container.setName(name);
		}
		else if (!oldContainer.getName().equalsIgnoreCase(name)) {
			exceptionHandler.addError(StorageContainerErrorCode.AUTO_GENERATED_LABEL, CONTAINER_NAME);
			return;
		}
	}

	private void setBarcode(String barcode, StorageContainer container, ObjectCreationException exceptionHandler) {
		//TODO: Get Barcode Format
		String barcodeFormat = null;
		if (isBlank(barcodeFormat)) {
			if (isBlank(barcode)) {
				container.setBarcode(containerBarcodeGenerator.generateBarcode(DEFAULT_BARCODE_TOKEN, container));
				return;
			}
			container.setBarcode(barcode);
		}
		else {
			if (!isBlank(container.getName())) {
				exceptionHandler.addError(StorageContainerErrorCode.AUTO_GENERATED_BARCODE, BARCODE);
				return;
			}
			container.setBarcode(containerBarcodeGenerator.generateBarcode(barcodeFormat, container));
		}
	}

	private void updateBarcode(String barcode, StorageContainer container, StorageContainer oldContainer,
			ObjectCreationException exceptionHandler) {
		//TODO: Get Barcode Format
		String barcodeFormat = null;
		if (isBlank(barcodeFormat)) {
			if (isBlank(barcode)) {
				exceptionHandler.addError(StorageContainerErrorCode.MISSING_ATTR_VALUE, BARCODE);
				return;
			}
			container.setBarcode(barcode);
		}

		else if (!oldContainer.getBarcode().equals(container.getBarcode())) {
			exceptionHandler.addError(StorageContainerErrorCode.AUTO_GENERATED_BARCODE, BARCODE);
			return;
		}

	}
	
	private void ensureUniqueName(String name, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getStorageContainerDao().isUniqueContainerName(name)) {
			exceptionHandler.addError(StorageContainerErrorCode.DUPLICATE_CONTAINER_NAME, STORAGE_CONTAINER);
		}
	}

	private void ensureUniqueBarcode(String barcode, ObjectCreationException exceptionHandler) {
		if (!daoFactory.getStorageContainerDao().isUniqueBarcode(barcode)) {
			exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, BARCODE);
			return;
		}
	}
	
	private void checkUniqueBarcode(StorageContainer oldStorageContainer, StorageContainer storageContainer,
			ObjectCreationException exceptionHandler) {
		if (!oldStorageContainer.getBarcode().equals(storageContainer.getBarcode())) {
			ensureUniqueBarcode(storageContainer.getBarcode(), exceptionHandler);
		}
	}

	private void checkUniqueName(StorageContainer oldStorageContainer, StorageContainer storageContainer,
			ObjectCreationException exceptionHandler) {
		if (oldStorageContainer.getBarcode() != null && !oldStorageContainer.getName().equals(storageContainer.getName())) {
			ensureUniqueName(storageContainer.getName(), exceptionHandler);

		}
	}


}
