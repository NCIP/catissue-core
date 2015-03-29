package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.AliquotSpecimensRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.DerivedSpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenRequirementFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenRequirementDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.util.Status;

public class SpecimenRequirementFactoryImpl implements SpecimenRequirementFactory {
	
	private DaoFactory daoFactory;
	
	private LabelGenerator specimenLabelGenerator;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public LabelGenerator getSpecimenLabelGenerator() {
		return specimenLabelGenerator;
	}

	public void setSpecimenLabelGenerator(LabelGenerator specimenLabelGenerator) {
		this.specimenLabelGenerator = specimenLabelGenerator;
	}

	@Override
	public SpecimenRequirement createSpecimenRequirement(SpecimenRequirementDetail detail) {
		SpecimenRequirement requirement = new SpecimenRequirement();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

	
		requirement.setId(detail.getId());
		requirement.setName(detail.getName());
		requirement.setLineage("New"); // TODO:
		
		setLabelFormat(detail, requirement, ose);
		setSpecimenClass(detail, requirement, ose);
		setSpecimenType(detail, requirement, ose);
		setAnatomicSite(detail, requirement, ose);
		setLaterality(detail, requirement, ose);
		setPathologyStatus(detail, requirement, ose);
		setStorageType(detail, requirement, ose);
		setInitialQty(detail, requirement, ose);
		setConcentration(detail, requirement, ose);
		setCollector(detail, requirement, ose);
		setCollectionProsedure(detail, requirement, ose);
		setCollectionContainer(detail, requirement, ose);
		setReceiver(detail, requirement, ose);		
		setCpe(detail, requirement, ose);
		
		// TODO:
		requirement.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());

		ose.checkAndThrow();		
		return requirement;
	}

	@Override
	public SpecimenRequirement createDerived(DerivedSpecimenRequirement req) {
		Long parentId = req.getParentSrId();
		SpecimenRequirement parent = null;
		if (parentId != null) {
			parent = daoFactory.getSpecimenRequirementDao().getById(parentId);
		}
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		if (parent == null) {
			ose.addError(SrErrorCode.PARENT_NOT_FOUND);
			throw ose;
		}
		
		SpecimenRequirement derived = parent.copy();
		derived.setLineage("Derived");
		setSpecimenClass(req.getSpecimenClass(), derived, ose);
		setSpecimenType(req.getType(), derived, ose);
		setInitialQty(req.getQuantity(), derived, ose);
		setStorageType(req.getStorageType(), derived, ose);
		setConcentration(req.getConcentration(), derived, ose);
		
		if (StringUtils.isNotBlank(req.getLabelFmt())) {
			derived.setLabelFormat(req.getLabelFmt());
		}
		
		if (StringUtils.isNotBlank(req.getName())) {
			derived.setName(req.getName());
		}
		
		ose.checkAndThrow();
		
		derived.setParentSpecimenRequirement(parent);
		return derived;
	}
	
	@Override
	public List<SpecimenRequirement> createAliquots(AliquotSpecimensRequirement req) {
		Long parentSrId = req.getParentSrId();
		SpecimenRequirement parent = null;
		
		if (parentSrId != null) {
			parent = daoFactory.getSpecimenRequirementDao().getById(parentSrId);
		}
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		if (parent == null) {
			ose.addError(SrErrorCode.PARENT_NOT_FOUND);
			throw ose;
		}
		
		if (req.getNoOfAliquots() == null || req.getNoOfAliquots() < 1L) {
			ose.addError(SrErrorCode.INVALID_ALIQUOT_CNT);
			throw ose;
		}
		
		if (req.getQtyPerAliquot() == null || req.getQtyPerAliquot() <= 0L) {
			ose.addError(SrErrorCode.INVALID_QTY);
		}
		
		Double total = req.getNoOfAliquots() * req.getQtyPerAliquot();
		if (total > parent.getQtyAfterAliquotsUse()) {
			ose.addError(SrErrorCode.INSUFFICIENT_QTY);
		}
		
		List<SpecimenRequirement> aliquots = new ArrayList<SpecimenRequirement>();
		for (int i = 0; i < req.getNoOfAliquots(); ++i) {
			SpecimenRequirement aliquot = parent.copy();
			aliquot.setLineage(Specimen.ALIQUOT); 			
			setStorageType(req.getStorageType(), aliquot, ose);
			
			if (StringUtils.isNotBlank(req.getLabelFmt())) {
				aliquot.setLabelFormat(req.getLabelFmt());
			}
			
			aliquot.setInitialQuantity(req.getQtyPerAliquot());
			aliquot.setParentSpecimenRequirement(parent);
			aliquots.add(aliquot);
			
			ose.checkAndThrow();
		}

		return aliquots;
	}
	
	private void setLabelFormat(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getLabelFmt())) {
			return;
		}
		
		if (!specimenLabelGenerator.isValidLabelTmpl(detail.getLabelFmt())) {
			ose.addError(SrErrorCode.INVALID_LABEL_FMT);
		}
		
		sr.setLabelFormat(detail.getLabelFmt());
	}
	
	private void setSpecimenClass(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setSpecimenClass(detail.getSpecimenClass(), sr, ose);
	}
	
	private void setSpecimenClass(String specimenClass, SpecimenRequirement sr, OpenSpecimenException ose) {
		ensureNotEmpty(specimenClass, SrErrorCode.SPECIMEN_CLASS_REQUIRED, ose);
		sr.setSpecimenClass(specimenClass);
		// TODO: Check whether class is valid
		
	}
	
	private void setSpecimenType(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setSpecimenType(detail.getType(), sr, ose);
	}
	
	private void setSpecimenType(String type, SpecimenRequirement sr, OpenSpecimenException ose) {
		ensureNotEmpty(type, SrErrorCode.SPECIMEN_TYPE_REQUIRED, ose);		
		sr.setSpecimenType(type);
		// TODO: Check whether type is valid for class;		
	}
	
	private void setAnatomicSite(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		String anatomicSite = ensureNotEmpty(detail.getAnatomicSite(), SrErrorCode.ANATOMIC_SITE_REQUIRED, ose);
		sr.setAnatomicSite(anatomicSite);
		// TODO: check for valid site value
	}
	
	private void setLaterality(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		String laterality = ensureNotEmpty(detail.getLaterality(), SrErrorCode.LATERALITY_REQUIRED, ose);
		sr.setLaterality(laterality);
		// TODO: check for valid laterality value
	}
	
	private void setPathologyStatus(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		String pathologyStatus = ensureNotEmpty(detail.getPathologyStatus(), SrErrorCode.PATHOLOGY_STATUS_REQUIRED, ose);
		sr.setPathologyStatus(pathologyStatus);
		// TODO: check for valid pathology status
	}
	
	private void setStorageType(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setStorageType(detail.getStorageType(), sr, ose);
		// TODO: check for valid storage type
	}

	private void setStorageType(String storageType, SpecimenRequirement sr, OpenSpecimenException ose) {
		ensureNotEmpty(storageType, SrErrorCode.STORAGE_TYPE_REQUIRED, ose);
		sr.setStorageType(storageType);
		// TODO: check for valid storage type
	}
	
	private void setInitialQty(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setInitialQty(detail.getInitialQty(), sr, ose);
	}
		
	private void setInitialQty(Double initialQty, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (initialQty == null || initialQty < 0) {
			ose.addError(SrErrorCode.INVALID_QTY);
			return;
		}
		
		sr.setInitialQuantity(initialQty);
	}

	private void setConcentration(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		setConcentration(detail.getConcentration(), sr, ose);
	}
	
	private void setConcentration(Double concentration, SpecimenRequirement sr, OpenSpecimenException ose) {
		if (sr.getAnatomicSite() == null || !"Molecular".equals(sr.getSpecimenClass())) { 
			return;
		}
		
		if (concentration != null && concentration < 0) { 
			ose.addError(SrErrorCode.CONCENTRATION_MUST_BE_POSITIVE);
			return;
		}
		
		sr.setConcentration(concentration);
	}
	
	private void setCollector(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		sr.setCollector(ensureValidUser(detail.getCollector(), SrErrorCode.COLLECTOR_NOT_FOUND, ose));
	}

	private void setCollectionProsedure(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		String collProc = ensureNotEmpty(detail.getCollectionProcedure(), SrErrorCode.COLL_PROC_REQUIRED, ose);
		sr.setCollectionProcedure(collProc);
		// TODO: check for valid collection proc
	}

	private void setCollectionContainer(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		String collContainer = ensureNotEmpty(detail.getCollectionContainer(), SrErrorCode.COLL_CONT_REQUIRED, ose);
		sr.setCollectionContainer(collContainer);
		// TODO: check for valid collection container
	}

	private void setReceiver(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		sr.setReceiver(ensureValidUser(detail.getReceiver(), SrErrorCode.RECEIVER_NOT_FOUND, ose));
	}

	private void setCpe(SpecimenRequirementDetail detail, SpecimenRequirement sr, OpenSpecimenException ose) {
		Long eventId = detail.getEventId();
		if (eventId == null) {
			ose.addError(SrErrorCode.CPE_REQUIRED);
			return;
		}
		
		CollectionProtocolEvent cpe = daoFactory.getCollectionProtocolDao().getCpe(eventId);
		if (cpe == null) {
			ose.addError(CpeErrorCode.NOT_FOUND);
		}
		
		sr.setCollectionProtocolEvent(cpe);
	}
		
	private String ensureNotEmpty(String value, ErrorCode required, OpenSpecimenException ose) {
		if (StringUtils.isBlank(value)) {
			ose.addError(required);
			return null;
		}
		
		return value;
	}
	
	private User ensureValidUser(UserSummary userSummary, ErrorCode notFound, OpenSpecimenException ose) {
		if (userSummary == null) {
			return null;
		}
		
		User user = null;
		if (userSummary.getId() != null) {
			user = daoFactory.getUserDao().getById(userSummary.getId());
		} else if (userSummary.getLoginName() != null && userSummary.getDomain() != null) {
			user = daoFactory.getUserDao().getUser(userSummary.getLoginName(), userSummary.getDomain());
		}
		
		if (user == null) {
			ose.addError(notFound);
		}
		
		return user;		
	}	
}
