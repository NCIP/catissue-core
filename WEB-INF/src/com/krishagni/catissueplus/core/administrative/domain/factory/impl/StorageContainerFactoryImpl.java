
package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerLabelSchemeType;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetails;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;

public class StorageContainerFactoryImpl implements StorageContainerFactory {

	private static final String SITE = "site";

	private static final String COLLECTION_PROTOCOL = "collection protocol";

	private static final String STORAGE_CONTAINER = "storage container";

	private static final String USER = "user";

	private static final String CONTAINER_NAME = "container name";

	private static final String ACTIVITY_STATUS = "activity status";

	private static final String SPECIMEN_TYPE = "specimen type";

	private static final String ONE_DIMENSION_CAPACITY = "one dimension capacity";

	private static final String TWO_DIMENSION_CAPACITY = "two dimension capacity";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public StorageContainer createStorageContainer(StorageContainerDetails details) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		StorageContainer storageContainer = new StorageContainer();
		setName(storageContainer, details.getName(), exceptionHandler);
		setComments(storageContainer, details.getComments());
		setActivityStatus(storageContainer, details.getActivityStatus(), exceptionHandler);
		setSiteName(storageContainer, details.getSiteName(), exceptionHandler);
		setBarcode(storageContainer, details.getBarcode(), exceptionHandler);
		setCollectionProtocols(storageContainer, details.getCpTitleCollection(), exceptionHandler);
		setCreatedByUser(storageContainer, details.getCreatedBy(), exceptionHandler);
		setHoldsSpecimenTypes(storageContainer, details.getHoldsSpecimenTypes(), exceptionHandler);
		setOneDimensionCapacity(storageContainer, details.getOneDimensionCapacity(), exceptionHandler);
		setTwoDimensionCapacity(storageContainer, details.getTwoDimensionCapacity(), exceptionHandler);
		setParentContainer(storageContainer, details.getParentContainerName(), exceptionHandler);
		setTempratureInCentigrade(storageContainer, details.getTempratureInCentigrade());
		setOneDimensionLabelingScheme(storageContainer, details.getOneDimentionLabelingScheme(), exceptionHandler);
		setTwoDimensionLabelingScheme(storageContainer, details.getTwoDimentionLabelingScheme(), exceptionHandler);
		exceptionHandler.checkErrorAndThrow();
		return storageContainer;
	}

	@Override
	public StorageContainer patchStorageContainer(StorageContainer storageContainer, StorageContainerDetails details) {
		ObjectCreationException exception = new ObjectCreationException();
		if (details.isNameModified()) {
			setName(storageContainer, details.getName(), exception);
		}

		if (details.isCommentsModified()) {
			setComments(storageContainer, details.getComments());
		}

		if (details.isSiteNameModified()) {
			setSiteName(storageContainer, details.getSiteName(), exception);
		}

		if (details.isBarcodeModified()) {
			setBarcode(storageContainer, details.getBarcode(), exception);
		}

		if (details.isHoldsSpecimenTypesModified()) {
			setHoldsSpecimenTypes(storageContainer, details.getHoldsSpecimenTypes(), exception);
		}

		if (details.isActivityStatusModified()) {
			setActivityStatus(storageContainer, details.getActivityStatus(), exception);
		}

		if (details.isCpTitleCollectionModified()) {
			setCollectionProtocols(storageContainer, details.getCpTitleCollection(), exception);
		}

		if (details.isCreatedByModified()) {
			setCreatedByUser(storageContainer, details.getCreatedBy(), exception);
		}

		if (details.isParentContainerNameModified()) {
			setParentContainer(storageContainer, details.getParentContainerName(), exception);
		}

		if (details.isOneDimensionCapacityModified()) {
			setOneDimensionCapacity(storageContainer, details.getOneDimensionCapacity(), exception);
		}

		if (details.isTwoDimensionCapacityModified()) {
			setTwoDimensionCapacity(storageContainer, details.getTwoDimensionCapacity(), exception);
		}

		if (details.isTempratureModified()) {
			setTempratureInCentigrade(storageContainer, details.getTempratureInCentigrade());
		}

		if (details.isOneDimentionLabelingSchemeModified()) {
			setOneDimensionLabelingScheme(storageContainer, details.getOneDimentionLabelingScheme(), exception);
		}

		if (details.isTwoDimensionCapacityModified()) {
			setTwoDimensionLabelingScheme(storageContainer, details.getTwoDimentionLabelingScheme(), exception);
		}

		exception.checkErrorAndThrow();
		return storageContainer;
	}

	private void setTwoDimensionLabelingScheme(StorageContainer storageContainer, String twoDimentionLabelingScheme,
			ObjectCreationException exception) {
		if(!isBlank(twoDimentionLabelingScheme) && ContainerLabelSchemeType.getEnumNameForValue(twoDimentionLabelingScheme) != null) {
			storageContainer.setTwoDimentionLabelingScheme(twoDimentionLabelingScheme);
		}
	}

	private void setOneDimensionLabelingScheme(StorageContainer storageContainer, String oneDimentionLabelingScheme,
			ObjectCreationException exception) {
		if(!isBlank(oneDimentionLabelingScheme) && ContainerLabelSchemeType.getEnumNameForValue(oneDimentionLabelingScheme) != null) {
			storageContainer.setOneDimentionLabelingScheme(oneDimentionLabelingScheme);
		}
	}

	private void setHoldsSpecimenTypes(StorageContainer storageContainer, Set<String> holdsSpecimenTypes,
			ObjectCreationException exceptionHandler) {

		for (String specType : holdsSpecimenTypes) {
			if (!CommonValidator.isValidPv(specType, SPECIMEN_TYPE)) {
				exceptionHandler.addError(UserErrorCode.INVALID_ATTR_VALUE, SPECIMEN_TYPE);
				return;
			}
		}
		storageContainer.setHoldsSpecimenTypes(holdsSpecimenTypes);
	}

	private void setName(StorageContainer storageContainer, String name, ObjectCreationException exceptionHandler) {
		if (isBlank(name)) {
			exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, CONTAINER_NAME);
			return;
		}
		storageContainer.setName(name);
	}

	private void setComments(StorageContainer storageContainer, String comments) {
		storageContainer.setComments(comments);
	}

	private void setActivityStatus(StorageContainer storageContainer, String activityStatus,
			ObjectCreationException exceptionHandler) {
		if (!CommonValidator.isValidPv(activityStatus, ACTIVITY_STATUS)) {
			exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, ACTIVITY_STATUS);
			return;
		}
		storageContainer.setActivityStatus(activityStatus);
	}

	private void setSiteName(StorageContainer storageContainer, String siteName, ObjectCreationException exceptionHandler) {
		Site site = daoFactory.getSiteDao().getSite(siteName);
		if (site == null) {
			exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, SITE);
			return;
		}
		storageContainer.setSite(site);
	}

	private void setBarcode(StorageContainer storageContainer, String barcode, ObjectCreationException exceptionHandler) {
		storageContainer.setBarcode(barcode);
	}

	private void setCollectionProtocols(StorageContainer storageContainer, Collection<String> cpTitleCollection,
			ObjectCreationException exceptionHandler) {
		if (cpTitleCollection.isEmpty()) {
			return;
		}

		Set<CollectionProtocol> collectionProtocols = new HashSet<CollectionProtocol>();
		for (String cptitle : cpTitleCollection) {
			CollectionProtocol collectionProtocol = daoFactory.getCollectionProtocolDao().getCPByTitle(cptitle);
			if (collectionProtocol == null) {
				exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, COLLECTION_PROTOCOL);
				return;
			}
			collectionProtocols.add(collectionProtocol);
		}
		storageContainer.setHoldsCPs(collectionProtocols);
	}

	private void setCreatedByUser(StorageContainer storageContainer, Long createdBy,
			ObjectCreationException exceptionHandler) {
		User user = daoFactory.getUserDao().getUser(createdBy);
		if (user == null) {
			exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, USER);
			return;
		}
		storageContainer.setCreatedBy(user);
	}

	private void setOneDimensionCapacity(StorageContainer storageContainer, Integer oneDimensionCapacity,
			ObjectCreationException exceptionHandler) {
		if (!isValidNumber(oneDimensionCapacity)) {
			exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, ONE_DIMENSION_CAPACITY);
			return;
		}
		storageContainer.setOneDimensionCapacity(oneDimensionCapacity);
	}

	private void setTwoDimensionCapacity(StorageContainer storageContainer, Integer twoDimensionCapacity,
			ObjectCreationException exceptionHandler) {
		if (!isValidNumber(twoDimensionCapacity)) {
			exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, TWO_DIMENSION_CAPACITY);
			return;
		}
		storageContainer.setTwoDimensionCapacity(twoDimensionCapacity);
	}

	private boolean isValidNumber(Integer capacity) {
		if (capacity == null || capacity <= 0) {
			return false;
		}
		return true;
	}

	private void setParentContainer(StorageContainer storageContainer, String parentContainerName,
			ObjectCreationException exceptionHandler) {
		if (parentContainerName != null) {
			StorageContainer parentContainer = daoFactory.getStorageContainerDao().getStorageContainerByName(
					parentContainerName);
			if (parentContainer == null) {
				exceptionHandler.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, STORAGE_CONTAINER);
				return;
			}
			storageContainer.setParentContainer(parentContainer);
		}
	}

	private void setTempratureInCentigrade(StorageContainer storageContainer, Double tempratureInCentigrade) {
		storageContainer.setTempratureInCentigrade(tempratureInCentigrade);
	}

}
