package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
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
		setCpe(detail, requirement, oce);
		
		// TODO:
		requirement.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());

		oce.checkErrorAndThrow();		
		return requirement;
	}

	@Override
	public SpecimenRequirement createDerived(DerivedSpecimenRequirement req) {
		Long parentId = req.getParentSrId();
		SpecimenRequirement parent = null;
		if (parentId != null) {
			parent = daoFactory.getSpecimenRequirementDao().getById(parentId);
		}
		
		ObjectCreationException oce = new ObjectCreationException();
		if (parent == null) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "parentSrId");
			throw oce;
		}
		
		SpecimenRequirement derived = parent.copy();
		derived.setLineage("Derived");		
		setSpecimenClass(req.getSpecimenClass(), derived, oce);
		setSpecimenType(req.getType(), derived, oce);
		setInitialQty(req.getQuantity(), derived, oce);
		setStorageType(req.getStorageType(), derived, oce);
		setConcentration(req.getConcentration(), derived, oce);
		
		if (StringUtils.isNotBlank(req.getLabelFmt())) {
			derived.setLabelFormat(req.getLabelFmt());
		}
		
		if (StringUtils.isNotBlank(req.getName())) {
			derived.setName(req.getName());
		}
		
		oce.checkErrorAndThrow();
		
		derived.setParentSpecimenRequirement(parent);
		return derived;
	}
	
	@Override
	public List<SpecimenRequirement> createAliquots(AliquotSpecimensRequirement req) {
		Long parentSrId = req.getParentSrId();
		SpecimenRequirement parent = daoFactory.getSpecimenRequirementDao().getById(parentSrId);
		
		ObjectCreationException oce = new ObjectCreationException();
		if (parent == null) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "parentSrId");
			throw oce;
		}
						
		Double total = req.getNoOfAliquots() * req.getQtyPerAliquot();
		if (total > parent.getQtyAfterAliquotsUse()) {
			oce.addError(SpecimenErrorCode.INSUFFICIENT_SPECIMEN_QTY, "count");
		}
		
		List<SpecimenRequirement> aliquots = new ArrayList<SpecimenRequirement>();
		for (int i = 0; i < req.getNoOfAliquots(); ++i) {
			SpecimenRequirement aliquot = parent.copy();
			aliquot.setLineage("Aliquot"); // TODO: have an enum			
			setStorageType(req.getStorageType(), aliquot, oce);
			
			if (StringUtils.isNotBlank(req.getLabelFmt())) {
				aliquot.setLabelFormat(req.getLabelFmt());
			}
			
			aliquot.setInitialQuantity(req.getQtyPerAliquot());			
			aliquot.setParentSpecimenRequirement(parent);
			aliquots.add(aliquot);
			
			oce.checkErrorAndThrow(); // throw on first iteration of error occurrence
		}
				
		return aliquots;
	}
	
	private void setSpecimenClass(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		setSpecimenClass(detail.getSpecimenClass(), sr, oce);
	}
	
	private void setSpecimenClass(String specimenClass, SpecimenRequirement sr, ObjectCreationException oce) {
		ensureNotEmpty(specimenClass, "specimenClass", oce);		
		sr.setSpecimenClass(specimenClass);		
		// TODO: Check whether class is valid
	}
	
	private void setSpecimenType(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		setSpecimenType(detail.getType(), sr, oce);
	}
	
	private void setSpecimenType(String type, SpecimenRequirement sr, ObjectCreationException oce) {
		ensureNotEmpty(type, "specimenType", oce);		
		sr.setSpecimenType(type);
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
		setStorageType(detail.getStorageType(), sr, oce);
		// TODO: check for valid storage type
	}

	private void setStorageType(String storageType, SpecimenRequirement sr, ObjectCreationException oce) {
		ensureNotEmpty(storageType, "storageType", oce);
		sr.setStorageType(storageType);
		// TODO: check for valid storage type
	}
	
	private void setInitialQty(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		setInitialQty(detail.getInitialQty(), sr, oce);
	}
		
	private void setInitialQty(Double initialQty, SpecimenRequirement sr, ObjectCreationException oce) {
		if (initialQty == null || initialQty < 0) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "initialQty");
			return;
		}
		
		sr.setInitialQuantity(initialQty);
	}

	private void setConcentration(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		setConcentration(detail.getConcentration(), sr, oce);
	}
	
	private void setConcentration(Double concentration, SpecimenRequirement sr, ObjectCreationException oce) {
		if (sr.getAnatomicSite() == null || !sr.getSpecimenClass().equals("Molecular")) {
			return;
		}
		
		if (concentration == null || concentration < 0) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "concentration");
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

	private void setCpe(SpecimenRequirementDetail detail, SpecimenRequirement sr, ObjectCreationException oce) {
		Long eventId = detail.getEventId();
		if (eventId == null) {
			oce.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, "eventId");
			return;
		}
		
		CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(eventId);
		if (cpe == null) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "eventId");
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