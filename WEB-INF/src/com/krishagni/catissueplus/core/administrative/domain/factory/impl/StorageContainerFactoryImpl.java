package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.PvAttributes.SPECIMEN_CLASS;
import static com.krishagni.catissueplus.core.common.service.PvValidator.areValid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerTypeErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.ContainerHierarchyDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
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
		setType(detail, container, ose);
		setTemperature(detail, container, ose);
		setCapacity(detail, container, ose);
		setLabelingSchemes(detail, container, ose);
		setSiteAndParentContainer(detail, container, ose);
		setPosition(detail, container, ose);
		setCreatedBy(detail, container, ose);
		setActivityStatus(detail, container, ose);
		setComments(detail, container, ose);
		setStoreSpecimenEnabled(detail, container, ose);
		setAllowedSpecimenClasses(detail, container, ose);
		setAllowedSpecimenTypes(detail, container, ose);
		setAllowedCps(detail, container, ose);
		setComputedRestrictions(container);
		
		ose.checkAndThrow();
		return container;
	}

	@Override
	public StorageContainer createStorageContainer(StorageContainer existing, StorageContainerDetail detail) {
		StorageContainer container = new StorageContainer();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		container.setId(existing.getId());

		setName(detail, existing, container, ose);
		setBarcode(detail, existing, container, ose);
		setType(detail, existing, container, ose);
		setTemperature(detail, existing, container, ose);
		setCapacity(detail, existing, container, ose);
		setLabelingSchemes(detail, existing, container, ose);
		setSiteAndParentContainer(detail, existing, container, ose);
		setPosition(detail, existing, container, ose);
		setCreatedBy(detail, existing, container, ose);
		setActivityStatus(detail, existing, container, ose);
		setComments(detail, existing, container, ose);
		setStoreSpecimenEnabled(detail, existing, container, ose);
		setAllowedSpecimenClasses(detail, existing, container, ose);
		setAllowedSpecimenTypes(detail, existing, container, ose);
		setAllowedCps(detail, existing, container, ose);
		setComputedRestrictions(container);
		
		ose.checkAndThrow();
		return container;
	}

	@Override
	public StorageContainer createStorageContainer(String name, ContainerHierarchyDetail input) {
		ContainerType type = getType(input.getTypeId(), input.getTypeName());
		StorageContainerDetail detail = getStorageContainerDetail(type);
		detail.setName(name);
		detail.setSiteName(input.getSiteName());
		detail.setStorageLocation(input.getStorageLocation());
		detail.setAllowedSpecimenClasses(input.getAllowedSpecimenClasses());
		detail.setAllowedSpecimenTypes(input.getAllowedSpecimenTypes());
		detail.setAllowedCollectionProtocols(input.getAllowedCollectionProtocols());
		
		if (input.getNoOfColumns() > 0) {
			detail.setNoOfColumns(input.getNoOfColumns());
		}

		if (input.getNoOfRows() > 0) {
			detail.setNoOfRows(input.getNoOfRows());
		}

		if (StringUtils.isNotBlank(input.getColumnLabelingScheme())) {
			detail.setColumnLabelingScheme(input.getColumnLabelingScheme());
		}

		if (StringUtils.isNotBlank(input.getRowLabelingScheme())) {
			detail.setRowLabelingScheme(input.getRowLabelingScheme());
		}

		if (input.getTemperature() != null) {
			detail.setTemperature(input.getTemperature());
		}

		if (input.getStoreSpecimensEnabled() != null) {
			detail.setStoreSpecimensEnabled(input.getStoreSpecimensEnabled());
		}
		
		StorageContainer container = createStorageContainer(detail);
		container.setType(type);
		return container;
	}
	
	@Override
	public StorageContainer createStorageContainer(String name, ContainerType type, StorageContainer parentContainer) {
		StorageContainerDetail detail = getStorageContainerDetail(type);
		detail.setName(name);
		detail.setSiteName(parentContainer.getSite().getName());

		StorageContainer container = createStorageContainer(detail);
		container.setParentContainer(parentContainer);

		StorageContainerPosition position = parentContainer.nextAvailablePosition();
		position.setOccupyingContainer(container);
		container.setPosition(position);
		setComputedRestrictions(container);
		container.setType(type);
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
	
	private void setName(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("name")) {
			setName(detail, container, ose);
		} else {
			container.setName(existing.getName());
		}
	}
	
	private void setBarcode(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		container.setBarcode(detail.getBarcode());
	}
	
	private void setBarcode(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("barcode")) {
			setBarcode(detail, container, ose);
		} else {
			container.setBarcode(existing.getBarcode());
		}
	}
	
	private void setType(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		if (detail.getTypeId() == null && StringUtils.isBlank(detail.getTypeName())) {
			return;
		}
		
		container.setType(getType(detail.getTypeId(), detail.getTypeName()));
	}
	
	private void setType(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("typeId") || detail.isAttrModified("typeName")) {
			setType(detail, container, ose);
		} else {
			container.setType(existing.getType());
		}
	}	
	
	private void setTemperature(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		if (detail.getTemperature() != null || detail.isAttrModified("temperature")) {
			//
			// Either user has explicitly blanked out or specified temperature value
			//
			container.setTemperature(detail.getTemperature());
		} else if (container.getType() != null) {
			//
			// User has not specified any value for temperature; therefore pick it from
			// container type
			//
			container.setTemperature(container.getType().getTemperature());
		} else {
			//
			// fall through case - when nothing above, just set it to null
			//
			container.setTemperature(null);
		}
	}
	
	private void setTemperature(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("temperature")) {
			setTemperature(detail, container, ose);
		} else {
			container.setTemperature(existing.getTemperature());
		}
	}
		
	private void setCapacity(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		setNoOfColumns(detail, container, ose);
		setNoOfRows(detail, container, ose);
	}
	
	private void setCapacity(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("noOfColumns")) {
			setNoOfColumns(detail, container, ose);
		} else {
			container.setNoOfColumns(existing.getNoOfColumns());
		}
		
		if (detail.isAttrModified("noOfRows")) {
			setNoOfRows(detail, container, ose);
		} else {
			container.setNoOfRows(existing.getNoOfRows());
		}
	}
	
	private void setNoOfColumns(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		int noOfCols = detail.getNoOfColumns();

		if (noOfCols <= 0 && container.getType() != null) {
			noOfCols = container.getType().getNoOfColumns();
		}

		if (noOfCols <= 0) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_CAPACITY);
		}
		
		container.setNoOfColumns(noOfCols);		
	}
	
	private void setNoOfRows(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		int noOfRows = detail.getNoOfRows();

		if (noOfRows <= 0 && container.getType() != null) {
			noOfRows = container.getType().getNoOfRows();
		}

		if (noOfRows <= 0) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_CAPACITY);
		}
				
		container.setNoOfRows(noOfRows);		
	}
	
	private void setLabelingSchemes(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		setColumnLabelingScheme(detail, container, ose);
		setRowLabelingScheme(detail, container, ose);
	}
	
	private void setLabelingSchemes(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("columnLabelingScheme")) {
			setColumnLabelingScheme(detail, container, ose);
		} else {
			container.setColumnLabelingScheme(existing.getColumnLabelingScheme());
		}
		
		if (detail.isAttrModified("rowLabelingScheme")) {
			setRowLabelingScheme(detail, container, ose);
		} else {
			container.setRowLabelingScheme(existing.getRowLabelingScheme());
		}
	}
	
	private void setColumnLabelingScheme(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String columnLabelingScheme = detail.getColumnLabelingScheme();

		if (StringUtils.isBlank(columnLabelingScheme)) {
			ContainerType type = container.getType();
			columnLabelingScheme = type != null ? type.getColumnLabelingScheme() : StorageContainer.NUMBER_LABELING_SCHEME;
		}
		
		if (!StorageContainer.isValidScheme(columnLabelingScheme)) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_LABELING_SCHEME);
		}
		
		container.setColumnLabelingScheme(columnLabelingScheme);		
	}
	
	private void setRowLabelingScheme(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String rowLabelingScheme = detail.getRowLabelingScheme();
		if (StringUtils.isBlank(rowLabelingScheme)) {
			ContainerType type = container.getType();
			rowLabelingScheme = type != null ? type.getRowLabelingScheme() : container.getColumnLabelingScheme();
		}
		
		if (!StorageContainer.isValidScheme(rowLabelingScheme)) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_LABELING_SCHEME);
		}
		
		container.setRowLabelingScheme(rowLabelingScheme);		
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
		} else if (parentContainer != null && !parentContainer.getSite().equals(site)) {
			ose.addError(StorageContainerErrorCode.INVALID_SITE_AND_PARENT_CONT);
		}
	}
	
	private void setSiteAndParentContainer(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("siteName") || detail.isAttrModified("storageLocation")) {
			setSiteAndParentContainer(detail, container, ose);
		} else {
			container.setSite(existing.getSite());
			container.setParentContainer(existing.getParentContainer());
		}
	}
	
	private Site setSite(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String siteName = detail.getSiteName();
		if (StringUtils.isBlank(siteName)) {
			return null;
		}
				
		Site site = daoFactory.getSiteDao().getSiteByName(siteName);
		if (site == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);			
		}
			
		container.setSite(site);
		return site;		
	}
	
	private StorageContainer setParentContainer(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		StorageContainer parentContainer = null;
		Object key = null;
		
		StorageLocationSummary storageLocation = detail.getStorageLocation();
		if (storageLocation == null) {
			return null;
		}
		
		if (storageLocation.getId() != null) {
			parentContainer = daoFactory.getStorageContainerDao().getById(storageLocation.getId());
			key = storageLocation.getId();
		} else if (StringUtils.isNotBlank(storageLocation.getName())) {
			parentContainer = daoFactory.getStorageContainerDao().getByName(storageLocation.getName());
			key = storageLocation.getName();
		}
		
		if (parentContainer == null) { 
			if (key != null) {
				ose.addError(StorageContainerErrorCode.PARENT_CONT_NOT_FOUND, key);
			}
			
			return null;
		}
		
		container.setParentContainer(parentContainer);
		return parentContainer;
	}
	
	private void setPosition(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		StorageContainer parentContainer = container.getParentContainer();
		String posOne = null, posTwo = null;
		
		StorageLocationSummary storageLocation = detail.getStorageLocation();
		if (storageLocation != null) {
			posOne = storageLocation.getPositionX();
			posTwo = storageLocation.getPositionY();
		}
				
		if (parentContainer == null) { // top-level container; therefore no position
			return;
		}
		
		StorageContainerPosition position = null;
		if (StringUtils.isNotBlank(posOne) && StringUtils.isNotBlank(posTwo)) {
			if (parentContainer.canContainerOccupyPosition(container.getId(), posOne, posTwo)) {
				position = parentContainer.createPosition(posOne, posTwo);
			} else {
				ose.addError(StorageContainerErrorCode.NO_FREE_SPACE, parentContainer.getName());
			}
		} else {
			position = parentContainer.nextAvailablePosition();
			if (position == null) {
				ose.addError(StorageContainerErrorCode.NO_FREE_SPACE, parentContainer.getName());
			} 
		} 
		
		if (position != null) {
			position.setOccupyingContainer(container);
			container.setPosition(position);			
		}
	}
	
	private void setPosition(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("storageLocation")) {
			setPosition(detail, container, ose);
		} else {
			container.setPosition(existing.getPosition());
		}
	}
	
	private void setCreatedBy(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Long userId = null;
		if (detail.getCreatedBy() == null) {
			userId = AuthUtil.getCurrentUser().getId();
		} else {
			userId = detail.getCreatedBy().getId();
		}

		User user = daoFactory.getUserDao().getById(userId);
		if (user == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		container.setCreatedBy(user);
	}
	
	private void setCreatedBy(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("createdBy")) {
			setCreatedBy(detail, container, ose);
		} else {
			container.setCreatedBy(existing.getCreatedBy());
		}
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
	
	private void setActivityStatus(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("activityStatus")) {
			setActivityStatus(detail, container, ose);
		} else {
			container.setActivityStatus(existing.getActivityStatus());
		}
	}
	
	private void setComments(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		container.setComments(detail.getComments());
	}	
	
	private void setComments(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("comments")) {
			setComments(detail, container, ose);
		} else {
			container.setComments(existing.getComments());
		}
	}

	private void setStoreSpecimenEnabled(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		boolean storeSpecimensEnabled = detail.isStoreSpecimensEnabled();
		if (detail.getStoreSpecimensEnabled() == null && container.getType() != null) {
			storeSpecimensEnabled = container.getType().isStoreSpecimenEnabled();
		}

		container.setStoreSpecimenEnabled(storeSpecimensEnabled);
	}

	private void setStoreSpecimenEnabled(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("storeSpecimensEnabled")) {
			setStoreSpecimenEnabled(detail, container, ose);
		} else {
			container.setStoreSpecimenEnabled(existing.isStoreSpecimenEnabled());
		}
	}

	private void setAllowedSpecimenClasses(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Set<String> allowedSpecimenClasses = detail.getAllowedSpecimenClasses();		
		if (!areValid(SPECIMEN_CLASS, allowedSpecimenClasses)) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_CLASS);
			return;
		}
						
		container.setAllowedSpecimenClasses(allowedSpecimenClasses);		
	}
	
	private void setAllowedSpecimenClasses(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("allowedSpecimenClasses")) {
			setAllowedSpecimenClasses(detail, container, ose);
		} else {
			container.setAllowedSpecimenClasses(existing.getAllowedSpecimenClasses());
		}
	}
	
	private void setAllowedSpecimenTypes(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Set<String> allowedSpecimenTypes = detail.getAllowedSpecimenTypes();
		if (!areValid(SPECIMEN_CLASS, 1, allowedSpecimenTypes)) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_TYPE);
			return;
		}
						
		container.setAllowedSpecimenTypes(allowedSpecimenTypes);		
	}
	
	private void setAllowedSpecimenTypes(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("allowedSpecimenTypes")) {
			setAllowedSpecimenTypes(detail, container, ose);
		} else {
			container.setAllowedSpecimenTypes(existing.getAllowedSpecimenTypes());
		}
	}
	
	private void setAllowedCps(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Set<String> allowedCps = detail.getAllowedCollectionProtocols();
		
		List<CollectionProtocol> cps = new ArrayList<CollectionProtocol>();		
		if (CollectionUtils.isNotEmpty(allowedCps) && container.getSite() != null) {
			cps = daoFactory.getCollectionProtocolDao().getCpsByShortTitle(allowedCps, container.getSite().getName());
			if (cps.size() != allowedCps.size()) {
				ose.addError(StorageContainerErrorCode.INVALID_CPS);
				return;
			}			
		}

		container.setAllowedCps(new HashSet<CollectionProtocol>(cps));		
	}
	
	private void setAllowedCps(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("allowedCollectionProtocols")) {
			setAllowedCps(detail, container, ose);
		} else {
			container.setAllowedCps(existing.getAllowedCps());
		}		
	}
	
	private void setComputedRestrictions(StorageContainer container) {
		container.setCompAllowedSpecimenClasses(container.computeAllowedSpecimenClasses());
		container.setCompAllowedSpecimenTypes(container.computeAllowedSpecimenTypes());
		container.setCompAllowedCps(container.computeAllowedCps());
	}
	
	private ContainerType getType(Long id, String name) {
		ContainerType type = null;
		Object key = null;

		if (id != null) {
			type = daoFactory.getContainerTypeDao().getById(id);
			key = id;
		} else if (StringUtils.isNotBlank(name)) {
			type = daoFactory.getContainerTypeDao().getByName(name);
			key = name;
		} else {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.TYPE_REQUIRED);
		}
		
		if (type == null) {
			throw OpenSpecimenException.userError(ContainerTypeErrorCode.NOT_FOUND, key);
		}

		return type;
	}
	

	private StorageContainerDetail getStorageContainerDetail(ContainerType type) {
		StorageContainerDetail detail = new StorageContainerDetail();
		detail.setNoOfColumns(type.getNoOfColumns());
		detail.setNoOfRows(type.getNoOfRows());
		detail.setColumnLabelingScheme(type.getColumnLabelingScheme());
		detail.setRowLabelingScheme(type.getRowLabelingScheme());
		detail.setTemperature(type.getTemperature());
		detail.setStoreSpecimensEnabled(type.isStoreSpecimenEnabled());
		return detail;
	}
}
