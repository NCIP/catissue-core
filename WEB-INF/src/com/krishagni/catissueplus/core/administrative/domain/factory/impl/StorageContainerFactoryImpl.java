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
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.CommonValidator;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
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
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		container.setId(detail.getId());
		setName(detail, container, ose);
		setBarcode(detail, container, ose);
		setTemperature(detail, container, ose);
		setCapacity(detail, container, ose);
		setLabelingSchemes(detail, container, ose);
		setSiteAndParentContainer(detail, container, ose);
		setPosition(detail, container, ose);
		setCreatedBy(detail, container, ose);
		setActivityStatus(detail, container, ose);
		setComments(detail, container, ose);
		setAllowedSpecimenClasses(detail, container, ose);
		setAllowedSpecimenTypes(detail, container, ose);
		setAllowedCps(detail, container, ose);
		
		ose.checkAndThrow();
		return container;
	}
	
	private void setName(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(StorageContainerErrorCode.NAME_REQUIRED);
			return;
		}
		
		container.setName(name);
	}
	
	private void setBarcode(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		container.setBarcode(detail.getBarcode());
	}
	
	private void setTemperature(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		container.setTemperature(detail.getTemperature());
	}
	
	private void setCapacity(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		int dimensionOneCapacity = detail.getDimensionOneCapacity();		
		if (dimensionOneCapacity <= 0) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_CAPACITY);			
		}
		
		int dimensionTwoCapacity = detail.getDimensionTwoCapacity();
		if (dimensionTwoCapacity <= 0) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_CAPACITY);
		}
		
		container.setDimensionOneCapacity(dimensionOneCapacity);
		container.setDimensionTwoCapacity(dimensionTwoCapacity);		
	}
	
	private void setLabelingSchemes(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String dimensionOneLabelingScheme = detail.getDimensionOneLabelingScheme();
		if (StringUtils.isBlank(dimensionOneLabelingScheme)) {
			dimensionOneLabelingScheme = StorageContainer.NUMBER_LABELING_SCHEME;
		}
		
		if (!StorageContainer.isValidScheme(dimensionOneLabelingScheme)) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_LABELING_SCHEME);
		}
		
		container.setDimensionOneLabelingScheme(dimensionOneLabelingScheme);
		
		String dimensionTwoLabelingScheme = detail.getDimensionTwoLabelingScheme();
		if (StringUtils.isBlank(dimensionTwoLabelingScheme)) {
			dimensionTwoLabelingScheme = container.getDimensionOneLabelingScheme();
		}
		
		if (!StorageContainer.isValidScheme(dimensionTwoLabelingScheme)) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_LABELING_SCHEME);
		}
		
		container.setDimensionTwoLabelingScheme(dimensionTwoLabelingScheme);
	}
		
	private void setSiteAndParentContainer(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Site site = setSite(detail, container, ose);
		StorageContainer parentContainer = setParentContainer(detail, container, ose);
		
		if (site == null && parentContainer == null) {
			ose.addError(StorageContainerErrorCode.REQUIRED_SITE_OR_PARENT_CONT);
			return;
		}
		
		if (site == null) {
			container.setSite(parentContainer.getSite());
		} else if (parentContainer != null && !parentContainer.getSite().getId().equals(site.getId())) {
			ose.addError(StorageContainerErrorCode.INVALID_SITE_AND_PARENT_CONT);
		}
	}
	
	private Site setSite(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String siteName = detail.getSiteName();
		if (StringUtils.isBlank(siteName)) {
			return null;
		}
				
		Site site = daoFactory.getSiteDao().getSite(siteName);
		if (site == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);			
		}
			
		container.setSite(site);
		return site;		
	}
	
	private StorageContainer setParentContainer(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {		
		String parentName = detail.getParentContainerName();
		if (StringUtils.isBlank(parentName)) {
			return null;
		}
		
		StorageContainer parentContainer = daoFactory.getStorageContainerDao().getStorageContainerByName(parentName);
		if (parentContainer == null) {
			ose.addError(StorageContainerErrorCode.PARENT_CONT_NOT_FOUND);
		}
			
		container.setParentContainer(parentContainer);
		return parentContainer;
	}
	
	private void setPosition(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
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
				ose.addError(StorageContainerErrorCode.NO_FREE_SPACE);
			}
		} else {
			position = parentContainer.nextAvailablePosition();
			if (position == null) {
				ose.addError(StorageContainerErrorCode.NO_FREE_SPACE);
			} 
		} 
		
		if (position != null) {
			position.setOccupyingContainer(container);
			container.setPosition(position);			
		}
	}
	
	private void setCreatedBy(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		if (detail.getCreatedBy() == null) {
			return;
		}
		
		Long userId = detail.getCreatedBy().getId();		
		if (userId == null) {
			return;
		}
				
		User user = daoFactory.getUserDao().getById(userId);
		if (user == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		container.setCreatedBy(user);
	}
	
	private void setActivityStatus(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (activityStatus == null) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		container.setActivityStatus(activityStatus);
	}
	
	private void setComments(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		container.setComments(detail.getComments());
	}	
	
	private void setAllowedSpecimenClasses(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Set<String> allowedSpecimenClasses = detail.getAllowedSpecimenClasses();
		if (CollectionUtils.isEmpty(allowedSpecimenClasses)) {
			return;
		}
		
		if (!CommonValidator.isValidPv(allowedSpecimenClasses.toArray(new String[0]), "specimen-class")) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_CLASS);
			return;
		}
		
		container.setAllowedSpecimenClasses(allowedSpecimenClasses);
	}
	
	private void setAllowedSpecimenTypes(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Set<String> allowedSpecimenTypes = detail.getAllowedSpecimenTypes();
		if (CollectionUtils.isEmpty(allowedSpecimenTypes)) {
			return;
		}
		
		if (!CommonValidator.isValidPv(allowedSpecimenTypes.toArray(new String[0]), "specimen-type")) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_TYPE);
			return;
		}
		
		container.setAllowedSpecimenTypes(allowedSpecimenTypes);
	}
	
	private void setAllowedCps(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Set<String> allowedCps = detail.getAllowedCollectionProtocols();
		if (CollectionUtils.isEmpty(allowedCps)) {
			return;
		}

		List<CollectionProtocol> cps = daoFactory.getCollectionProtocolDao().getCpsByShortTitle(allowedCps);
		if (cps.size() != allowedCps.size()) {
			ose.addError(CpErrorCode.NOT_FOUND);
			return;
		}
		
		container.setAllowedCps(new HashSet<CollectionProtocol>(cps));
	}
	
}
