package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerTypeErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerTypeFactory;
import com.krishagni.catissueplus.core.administrative.events.ContainerTypeDetail;
import com.krishagni.catissueplus.core.administrative.events.ContainerTypeSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.util.Status;

public class ContainerTypeFactoryImpl implements ContainerTypeFactory {
	private DaoFactory daoFactory;

	private LabelGenerator nameGenerator;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setNameGenerator(LabelGenerator nameGenerator) {
		this.nameGenerator = nameGenerator;
	}

	@Override
	public ContainerType createContainerType(ContainerTypeDetail detail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		ContainerType containerType = new ContainerType();
		containerType.setId(detail.getId());
		containerType.setTemperature(detail.getTemperature());
		containerType.setStoreSpecimenEnabled(detail.isStoreSpecimenEnabled());

		setName(detail, containerType, ose);
		setNameFormat(detail, containerType, ose);
		setDimension(detail, containerType, ose);
		setPositionLabelingMode(detail, containerType, ose);
		setLabelingSchemes(detail, containerType, ose);
		setCanHold(detail, containerType, ose);
		setActivityStatus(detail, containerType, ose);
		
		ose.checkAndThrow();
		return containerType;
	}

	private void setName(ContainerTypeDetail detail, ContainerType containerType, OpenSpecimenException ose) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(ContainerTypeErrorCode.NAME_REQUIRED);
			return;
		}
		
		containerType.setName(name);
	}

	private void setNameFormat(ContainerTypeDetail detail, ContainerType containerType, OpenSpecimenException ose) {
		String nameFormat = detail.getNameFormat();
		if (StringUtils.isBlank(nameFormat)) {
			ose.addError(ContainerTypeErrorCode.NAME_FORMAT_REQUIRED);
			return;
		}

		if (!nameGenerator.isValidLabelTmpl(nameFormat)) {
			ose.addError(ContainerTypeErrorCode.INVALID_NAME_FORMAT, nameFormat);
			return;
		}

		containerType.setNameFormat(nameFormat);
	}

	private void setDimension(ContainerTypeDetail detail, ContainerType containerType, OpenSpecimenException ose) {
		setNoOfColumns(detail, containerType, ose);
		setNoOfRows(detail, containerType, ose);
	}
	
	private void setNoOfColumns(ContainerTypeDetail detail, ContainerType containerType, OpenSpecimenException ose) {
		int noOfCols = detail.getNoOfColumns();		
		if (noOfCols <= 0) {
			ose.addError(ContainerTypeErrorCode.INVALID_CAPACITY, noOfCols);
		}
		
		containerType.setNoOfColumns(noOfCols);	
	}
	
	private void setNoOfRows(ContainerTypeDetail detail, ContainerType containerType, OpenSpecimenException ose) {
		int noOfRows = detail.getNoOfRows();
		if (noOfRows <= 0) {
			ose.addError(ContainerTypeErrorCode.INVALID_CAPACITY, noOfRows);
		}
				
		containerType.setNoOfRows(noOfRows);		
	}

	private void setPositionLabelingMode(ContainerTypeDetail detail, ContainerType containerType, OpenSpecimenException ose) {
		try {
			if (StringUtils.isNotBlank(detail.getPositionLabelingMode())) {
				containerType.setPositionLabelingMode(StorageContainer.PositionLabelingMode.valueOf(detail.getPositionLabelingMode()));
			}
		} catch (Exception e) {
			ose.addError(ContainerTypeErrorCode.INVALID_POSITION_LABELING_MODE, detail.getPositionLabelingMode());
		}
	}

	private void setLabelingSchemes(ContainerTypeDetail detail, ContainerType containerType, OpenSpecimenException ose) {
		if (containerType.getPositionLabelingMode() == StorageContainer.PositionLabelingMode.LINEAR) {
			containerType.setColumnLabelingScheme(StorageContainer.NUMBER_LABELING_SCHEME);
			containerType.setRowLabelingScheme(StorageContainer.NUMBER_LABELING_SCHEME);
			return;
		}

		setColumnLabelingScheme(detail, containerType, ose);
		setRowLabelingScheme(detail, containerType, ose);
	}
	
	private void setColumnLabelingScheme(ContainerTypeDetail detail, ContainerType containerType, OpenSpecimenException ose) {
		String columnLabelingScheme = detail.getColumnLabelingScheme();
		if (StringUtils.isBlank(columnLabelingScheme)) {
			columnLabelingScheme = StorageContainer.NUMBER_LABELING_SCHEME;
		}
		
		if (!StorageContainer.isValidScheme(columnLabelingScheme)) {
			ose.addError(ContainerTypeErrorCode.INVALID_LABELING_SCHEME, columnLabelingScheme);
		}
		
		containerType.setColumnLabelingScheme(columnLabelingScheme);		
	}
	
	private void setRowLabelingScheme(ContainerTypeDetail detail, ContainerType containerType, OpenSpecimenException ose) {
		String rowLabelingScheme = detail.getRowLabelingScheme();
		if (StringUtils.isBlank(rowLabelingScheme)) {
			rowLabelingScheme = containerType.getColumnLabelingScheme();
		}
		
		if (!StorageContainer.isValidScheme(rowLabelingScheme)) {
			ose.addError(ContainerTypeErrorCode.INVALID_LABELING_SCHEME, rowLabelingScheme);
		}
		
		containerType.setRowLabelingScheme(rowLabelingScheme);		
	}
	
	private void setCanHold(ContainerTypeDetail detail, ContainerType containerType, OpenSpecimenException ose) {
		ContainerTypeSummary typeDetail = detail.getCanHold();
		if (typeDetail == null) {
			return;
		}

		if (containerType.isStoreSpecimenEnabled()) {
			ose.addError(ContainerTypeErrorCode.CANNOT_HOLD_CONTAINER, containerType.getName());
			return;
		}
		
		Object key = null;
		ContainerType canHold = null;
		if (typeDetail.getId() != null) {
			canHold = daoFactory.getContainerTypeDao().getById(typeDetail.getId());
			key = typeDetail.getId();
		} else if (StringUtils.isNotBlank(typeDetail.getName())) {
			canHold = daoFactory.getContainerTypeDao().getByName(typeDetail.getName());
			key = typeDetail.getName();
		}
		
		if (canHold == null) {
			ose.addError(ContainerTypeErrorCode.NOT_FOUND, key);
			return;
		}
		
		containerType.setCanHold(canHold);
	}
	
	private void setActivityStatus(ContainerTypeSummary detail, ContainerType containerType, OpenSpecimenException ose) {
		String status = detail.getActivityStatus();
		
		if (StringUtils.isBlank(status)) {
			containerType.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		} else if (Status.isValidActivityStatus(status)) {
			containerType.setActivityStatus(status);
		} else {
			ose.addError(ActivityStatusErrorCode.INVALID);
		}
	}
}
