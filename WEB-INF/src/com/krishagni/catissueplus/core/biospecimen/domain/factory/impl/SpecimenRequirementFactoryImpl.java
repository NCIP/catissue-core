package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenRequirementFactory;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.util.Status;

public class SpecimenRequirementFactoryImpl implements SpecimenRequirementFactory {
	
	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public SpecimenRequirement createSpecimenRequirement(SpecimenRequirementDetail detail) {
		SpecimenRequirement requirement = new SpecimenRequirement();
		ObjectCreationException oce = new ObjectCreationException();

	
		requirement.setId(detail.getId());
		requirement.setName(detail.getName());
		requirement.setLineage("New"); // TODO:
		requirement.setLabelFormat(detail.getLabelFmt());
		
		setSpecimenClass(detail, requirement, oce);
		setSpecimenType(detail, requirement, oce);
		setAnatomicSite(detail, requirement, oce);
		setLaterality(detail, requirement, oce);
		setPathologyStatus(detail, requirement, oce);
		setStorageType(detail, requirement, oce);
		setInitialQty(detail, requirement, oce);
		setConcentration(detail, requirement, oce);
		setCollector(detail, requirement, oce);
		setCollectionProcedure(detail, requirement, oce);
		setCollectionContainer(detail, requirement, oce);
		setReceiver(detail, requirement, oce);
		setReceivedQuality(detail, requirement, oce);
		setCpe(detail, requirement, oce);
		
		// TODO:
		requirement.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());

		oce.checkErrorAndThrow();		
		return requirement;
	}
	
	private void setSpecimenClass(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		String specimenClass = ensureNotEmpty(detail.getSpecimenClass(), "specimenClass", oce);		
		sr.setSpecimenClass(specimenClass);		
		// TODO: Check whether class is valid
	}
	
	private void setSpecimenType(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		String specimenType = ensureNotEmpty(detail.getType(), "specimenType", oce);		
		sr.setSpecimenType(specimenType);
		// TODO: Check whether type is valid for class;
	}
	
	private void setAnatomicSite(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		String anatomicSite = ensureNotEmpty(detail.getAnatomicSite(), "anatomicSite", oce);
		sr.setAnatomicSite(anatomicSite);
		// TODO: check for valid site value
	}
	
	private void setLaterality(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		String laterality = ensureNotEmpty(detail.getLaterality(), "laterality", oce);
		sr.setLaterality(laterality);
		// TODO: check for valid laterality value
	}
	
	private void setPathologyStatus(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		String pathologyStatus = ensureNotEmpty(detail.getPathologyStatus(), "pathologyStatus", oce);
		sr.setPathologyStatus(pathologyStatus);
		// TODO: check for valid pathology status
	}
	
	private void setStorageType(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		String storageType = ensureNotEmpty(detail.getStorageType(), "storageType", oce);
		sr.setStorageType(storageType);
		// TODO: check for valid storage type
	}
	
	private void setInitialQty(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		Double initialQty = detail.getInitialQty();
		if (initialQty == null || initialQty < 0) {
			oce.addError(null, "initialQty");
			return;
		}
		
		sr.setInitialQuantity(initialQty);
	}

	private void setConcentration(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		Double concentration = detail.getConcentration();
		if (concentration == null || concentration < 0) {
			oce.addError(null, "concentration");
			return;
		}
		
		sr.setConcentration(concentration);
	}
	
	private void setCollector(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		sr.setCollector(ensureValidUser(detail.getCollector(), "collector", oce));
	}

	private void setCollectionProcedure(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		String collProc = ensureNotEmpty(detail.getCollectionProcedure(), "collectionProcedure", oce);
		sr.setCollectionProcedure(collProc);
		// TODO: check for valid collection proc
	}

	private void setCollectionContainer(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		String collContainer = ensureNotEmpty(detail.getCollectionContainer(), "collectionContainer", oce);
		sr.setCollectionContainer(collContainer);
		// TODO: check for valid collection container
	}

	private void setReceiver(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		sr.setReceiver(ensureValidUser(detail.getReceiver(), "receiver", oce));
	}

	private void setReceivedQuality(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		String receivedQuality = ensureNotEmpty(detail.getReceivedQuality(), "receivedQuality", oce);
		sr.setReceivedQuality(receivedQuality);
		// TODO: check for valid collection container
	}
	
	private void setCpe(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		Long eventId = detail.getEventId();
		if (eventId == null) {
			oce.addError(null, "eventId");
			return;
		}
		
		CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(eventId);
		if (cpe == null) {
			oce.addError(null, "eventId");
		}
		
		sr.setCollectionProtocolEvent(cpe);
	}
		
	private String ensureNotEmpty(String value, String field, ObjectCreationException oce) {
		if (StringUtils.isEmpty(value)) {
			oce.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, field);
			return null;
		}
		
		return value;
	}
	
	private User ensureValidUser(UserSummary userSummary, String field, ObjectCreationException oce) {
		if (userSummary == null || userSummary.getId() == null) {
			oce.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, field);
			return null;
		}
		
		User user = daoFactory.getUserDao().getUser(userSummary.getId());
		if (user == null) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, field);
		}
		
		return user;		
	}	
}