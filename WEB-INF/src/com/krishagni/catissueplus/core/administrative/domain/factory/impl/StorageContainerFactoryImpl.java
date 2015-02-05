package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

public class StorageContainerFactoryImpl implements StorageContainerFactory {
	private DaoFactory daoFactory;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public StorageContainer createStorageContainer(StorageContainerDetail detail) {
		StorageContainer container = new StorageContainer();
		ObjectCreationException oce = new ObjectCreationException();
		
		container.setId(detail.getId());
		setName(detail, container, oce);
		setBarcode(detail, container, oce);
		setTemperature(detail, container, oce);
		setCapacity(detail, container, oce);
		setLabelingSchemes(detail, container, oce);
		setSiteAndParentContainer(detail, container, oce);
		setPosition(detail, container, oce);
		setCreatedBy(detail, container, oce);
		setActivityStatus(detail, container, oce);
		setComments(detail, container, oce);
		setAllowedSpecimenClasses(detail, container, oce);
		setAllowedSpecimenTypes(detail, container, oce);
		setAllowedCps(detail, container, oce);
		
		oce.checkErrorAndThrow();
		return container;
	}
	
	private void setName(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			oce.addError(StorageContainerErrorCode.MISSING_ATTR_VALUE, "name");
			return;
		}
		
		container.setName(name);
	}
	
	private void setBarcode(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		container.setBarcode(detail.getBarcode());
	}
	
	private void setTemperature(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		container.setTemperature(detail.getTemperature());
	}
	
	private void setCapacity(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		int dimensionOneCapacity = detail.getDimensionOneCapacity();		
		if (dimensionOneCapacity <= 0) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "dimensionOneCapacity");			
		}
		
		int dimensionTwoCapacity = detail.getDimensionTwoCapacity();
		if (dimensionTwoCapacity <= 0) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "dimensionTwoCapacity");
		}
		
		container.setDimensionOneCapacity(dimensionOneCapacity);
		container.setDimensionTwoCapacity(dimensionTwoCapacity);		
	}
	
	private void setLabelingSchemes(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		String dimensionOneLabelingScheme = detail.getDimensionOneLabelingScheme();
		if (StringUtils.isBlank(dimensionOneLabelingScheme)) {
			dimensionOneLabelingScheme = StorageContainer.NUMBER_LABELING_SCHEME;
		}
		
		if (!StorageContainer.isValidScheme(dimensionOneLabelingScheme)) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "dimensionOneLabelingScheme");
		}
		
		container.setDimensionOneLabelingScheme(dimensionOneLabelingScheme);
		
		String dimensionTwoLabelingScheme = detail.getDimensionTwoLabelingScheme();
		if (StringUtils.isBlank(dimensionTwoLabelingScheme)) {
			dimensionTwoLabelingScheme = container.getDimensionOneLabelingScheme();
		}
		
		if (!StorageContainer.isValidScheme(dimensionTwoLabelingScheme)) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "dimensionTwoLabelingScheme");
		}
		
		container.setDimensionTwoLabelingScheme(dimensionTwoLabelingScheme);
	}
		
	private void setSiteAndParentContainer(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		Site site = setSite(detail, container, oce);
		StorageContainer parentContainer = setParentContainer(detail, container, oce);
		
		if (site == null && parentContainer == null) {
			oce.addError(StorageContainerErrorCode.MISSING_ATTR_VALUE, "siteName or parentContainerName");
			return;
		}
		
		if (site == null) {
			container.setSite(parentContainer.getSite());
		} else if (parentContainer != null && !parentContainer.getSite().getId().equals(site.getId())) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "parentContainerName");
		}
	}
	
	private Site setSite(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		String siteName = detail.getSiteName();
		if (StringUtils.isBlank(siteName)) {
			return null;
		}
				
		Site site = daoFactory.getSiteDao().getSite(siteName);
		if (site == null) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "siteName");			
		}
			
		container.setSite(site);
		return site;		
	}
	
	private StorageContainer setParentContainer(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {		
		String parentName = detail.getParentContainerName();
		if (StringUtils.isBlank(parentName)) {
			return null;
		}
		
		StorageContainer parentContainer = daoFactory.getStorageContainerDao().getStorageContainerByName(parentName);
		if (parentContainer == null) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "parentContainerName");
		}
			
		container.setParentContainer(parentContainer);
		return parentContainer;
	}
	
	private void setPosition(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		StorageContainer parentContainer = container.getParentContainer();
		String posOne = null, posTwo = null;

		if (detail.getPosition() != null) {
			posOne = detail.getPosition().getPosOne();
			posTwo = detail.getPosition().getPosTwo();
		}
				
		if (parentContainer == null) { // top-level container; therefore no position
			return;
		}
		
		StorageContainerPosition position = null;
		if (StringUtils.isNotBlank(posOne) && StringUtils.isNotBlank(posTwo)) {
			if (parentContainer.canContainerOccupyPosition(container.getId(), posOne, posTwo)) {
				position = parentContainer.createPosition(posOne, posTwo);
			} else {
				oce.addError(StorageContainerErrorCode.NO_FREE_SPACE, "position");
			}
		} else {
			position = parentContainer.nextAvailablePosition();
			if (position == null) {
				oce.addError(StorageContainerErrorCode.NO_FREE_SPACE, "parentContainerName");
			} 
		} 
		
		if (position != null) {
			position.setOccupyingContainer(container);
			container.setPosition(position);			
		}
	}
	
	private void setCreatedBy(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		if (detail.getCreatedBy() == null) {
			return;
		}
		
		Long userId = detail.getCreatedBy().getId();		
		if (userId == null) {
			return;
		}
				
		User user = daoFactory.getUserDao().getById(userId);
		if (user == null) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "createdBy.id");
			return;
		}
		
		container.setCreatedBy(user);
	}
	
	private void setActivityStatus(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		String activityStatus = detail.getActivityStatus();
		if (activityStatus == null) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (!Status.isValidActivityStatus(activityStatus)) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "activityStatus");
			return;
		}
		
		container.setActivityStatus(activityStatus);
	}
	
	private void setComments(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		container.setComments(detail.getComments());
	}	
	
	private void setAllowedSpecimenClasses(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		Set<String> allowedSpecimenClasses = detail.getAllowedSpecimenClasses();
		if (CollectionUtils.isEmpty(allowedSpecimenClasses)) {
			return;
		}
		
		if (!CommonValidator.isValidPv(allowedSpecimenClasses.toArray(new String[0]), "specimen-class")) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "allowedSpecimenClasses");
			return;
		}
		
		container.setAllowedSpecimenClasses(allowedSpecimenClasses);
	}
	
	private void setAllowedSpecimenTypes(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		Set<String> allowedSpecimenTypes = detail.getAllowedSpecimenTypes();
		if (CollectionUtils.isEmpty(allowedSpecimenTypes)) {
			return;
		}
		
		if (!CommonValidator.isValidPv(allowedSpecimenTypes.toArray(new String[0]), "specimen-type")) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "allowedSpecimenTypes");
			return;
		}
		
		container.setAllowedSpecimenTypes(allowedSpecimenTypes);
	}
	
	private void setAllowedCps(StorageContainerDetail detail, StorageContainer container, ObjectCreationException oce) {
		Set<String> allowedCps = detail.getAllowedCollectionProtocols();
		if (CollectionUtils.isEmpty(allowedCps)) {
			return;
		}

		List<CollectionProtocol> cps = daoFactory.getCollectionProtocolDao().getCpsByShortTitle(allowedCps);
		if (cps.size() != allowedCps.size()) {
			oce.addError(StorageContainerErrorCode.INVALID_ATTR_VALUE, "allowedCps");
			return;
		}
		
		container.setAllowedCps(new HashSet<CollectionProtocol>(cps));
	}
	
}
