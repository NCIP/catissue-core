
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.PvAttributes.*;
import static com.krishagni.catissueplus.core.common.service.PvValidator.areValid;
import static com.krishagni.catissueplus.core.common.service.PvValidator.isValid;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenReceivedEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ReceivedEventDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenEventDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenResolver;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.NumUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.de.domain.DeObject;

public class SpecimenFactoryImpl implements SpecimenFactory {

	private DaoFactory daoFactory;

	private SpecimenResolver specimenResolver;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setSpecimenResolver(SpecimenResolver specimenResolver) {
		this.specimenResolver = specimenResolver;
	}

	@Override
	public Specimen createSpecimen(SpecimenDetail detail, Specimen parent) {
		return createSpecimen(null, detail, parent);
	}

	@Override
	public Specimen createSpecimen(Specimen existing, SpecimenDetail detail, Specimen parent) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		if (parent == null) {
			parent = getSpecimen(detail.getParentId(), detail.getCpShortTitle(), detail.getParentLabel(), ose);
			ose.checkAndThrow();
		}
		
		Visit visit = getVisit(detail, existing, parent, ose);
		SpecimenRequirement sr = getSpecimenRequirement(detail, existing, visit, ose);
		ose.checkAndThrow();
		
		if (sr != null && !sr.getCollectionProtocolEvent().equals(visit.getCpEvent())) {
			ose.addError(SpecimenErrorCode.INVALID_VISIT);
		}
		
		if (parent != null && !parent.getVisit().equals(visit)) {
			ose.addError(SpecimenErrorCode.INVALID_VISIT);
		}

		ose.checkAndThrow();
		
		Specimen specimen = null;
		if (sr != null) {
			specimen = sr.getSpecimen();
		} else {
			specimen = new Specimen();
		}
		
		
		if (existing != null) {
			specimen.setId(existing.getId());
		} else {
			specimen.setId(detail.getId());
		}
		
		specimen.setVisit(visit);
		specimen.setForceDelete(detail.isForceDelete());
		specimen.setPrintLabel(detail.isPrintLabel());
		
		setCollectionStatus(detail, existing, specimen, ose);
		setLineage(detail, existing, specimen, ose);
		setParentSpecimen(detail, existing, parent, specimen, ose);

		setLabel(detail, existing, specimen, ose);
		setBarcode(detail, existing, specimen, ose);
		setActivityStatus(detail, existing, specimen, ose);
						
		setAnatomicSite(detail, existing, specimen, ose);
		setLaterality(detail, existing, specimen, ose);
		setPathologicalStatus(detail, existing, specimen, ose);
		setSpecimenClass(detail, existing, specimen, ose);
		setSpecimenType(detail, existing, specimen, ose);
		setQuantity(detail, existing, specimen, ose);
		setConcentration(detail, existing, specimen, ose);
		setBiohazards(detail, existing, specimen, ose);
		setFreezeThawCycles(detail, existing, specimen, ose);
		setComments(detail, existing, specimen, ose);

		if (sr != null && 
				(!sr.getSpecimenClass().equals(specimen.getSpecimenClass()) ||
					!sr.getSpecimenType().equals(specimen.getSpecimenType()))) {
			specimen.setSpecimenRequirement(null);
		}
		
		setSpecimenPosition(detail, existing, specimen, ose);
		setCollectionDetail(detail, existing, specimen, ose);
		setReceiveDetail(detail, existing, specimen, ose);
		setCreatedOn(detail, existing, specimen, ose);
		setPooledSpecimen(detail, existing, specimen, ose);
		setExtension(detail, existing, specimen, ose);

		ose.checkAndThrow();
		return specimen;
	}

	private void setBarcode(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if(StringUtils.isBlank(detail.getBarcode())) {
			return;
		}

		specimen.setBarcode(detail.getBarcode());
	}
	
	private void setBarcode(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("barcode")) {
			setBarcode(detail, specimen, ose);
		} else {
			specimen.setBarcode(existing.getBarcode());
		}
	}

	private void setActivityStatus(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		String status = detail.getActivityStatus();
		if (StringUtils.isBlank(status)) {
			specimen.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		} else if (Status.isValidActivityStatus(status)) {
			specimen.setActivityStatus(status);
		} else {
			ose.addError(ActivityStatusErrorCode.INVALID);
		}
	}
	
	private void setActivityStatus(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("activityStatus")) {
			setActivityStatus(detail, specimen, ose);
		} else {
			specimen.setActivityStatus(existing.getActivityStatus());
		}
	}
	
	private void setLabel(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		specimen.setLabel(detail.getLabel());
	}
	
	private void setLabel(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("label")) {
			setLabel(detail, specimen, ose);
		} else {
			specimen.setLabel(existing.getLabel());
		}
	}

	private Visit getVisit(SpecimenDetail detail, Specimen existing, Specimen parent, OpenSpecimenException ose) {
		Long visitId = detail.getVisitId();
		String visitName = detail.getVisitName();
		
		Visit visit = null;
		if (visitId != null) {
			visit = daoFactory.getVisitsDao().getById(visitId);
		} else if (StringUtils.isNotBlank(visitName)) {
			visit = daoFactory.getVisitsDao().getByName(visitName);
		} else if (existing != null) {
			visit = existing.getVisit();
		} else if (parent != null) {
			visit = parent.getVisit();
		} else {
			ose.addError(SpecimenErrorCode.VISIT_REQUIRED);
			return null;
		}
		
		if (visit == null) {
			ose.addError(VisitErrorCode.NOT_FOUND);
			return null;
		}
		
		return visit;
	}
	
	private void setLineage(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		String lineage = detail.getLineage();
		if (lineage == null) {
			if (specimen.getSpecimenRequirement() == null) {
				lineage = Specimen.NEW;
			} else {
				lineage = specimen.getSpecimenRequirement().getLineage();
			}
		}
		
		if (!Specimen.isValidLineage(lineage)) {
			ose.addError(SpecimenErrorCode.INVALID_LINEAGE);
			return;
		}
		
		specimen.setLineage(lineage);
	}
	
	private void setLineage(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("lineage")) {
			setLineage(detail, specimen, ose);
		} else {
			specimen.setLineage(existing.getLineage());
		}
	}
	
	private void setParentSpecimen(SpecimenDetail detail, Specimen parent, Specimen specimen, OpenSpecimenException ose) {
		if (specimen.getLineage().equals(Specimen.NEW)) {
			return;
		}
		
		if (parent != null) {
			specimen.setParentSpecimen(parent);
			return;
		}
		
		Long parentId = detail.getParentId();
		String parentLabel = detail.getParentLabel();
		if (parentId != null || StringUtils.isNotBlank(parentLabel)) {
			parent = getSpecimen(parentId, detail.getCpShortTitle(), parentLabel, ose);
		} else if (specimen.getVisit() != null && specimen.getSpecimenRequirement() != null) {			
			Long visitId = specimen.getVisit().getId();
			Long srId = specimen.getSpecimenRequirement().getId();			
			parent = daoFactory.getSpecimenDao().getParentSpecimenByVisitAndSr(visitId, srId);
			if (parent == null) {
				ose.addError(SpecimenErrorCode.PARENT_NF_BY_VISIT_AND_SR, visitId, srId);
			}
		} else {
			ose.addError(SpecimenErrorCode.PARENT_REQUIRED);
		}
		
		specimen.setParentSpecimen(parent);
	}

	private Specimen getSpecimen(Long id, String cpShortTitle, String label, OpenSpecimenException ose) {
		Specimen specimen = null;
		if (id != null || StringUtils.isNotBlank(label)) {
			specimen = specimenResolver.getSpecimen(id, cpShortTitle, label, ose);
		}

		return specimen;
	}
	
	private void setParentSpecimen(SpecimenDetail detail, Specimen existing, Specimen parent, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("parentLabel")) {
			setParentSpecimen(detail, parent, specimen, ose);
		} else {
			specimen.setParentSpecimen(existing.getParentSpecimen());
		}
	}

	private SpecimenRequirement getSpecimenRequirement(SpecimenDetail detail, Specimen existing, Visit visit, OpenSpecimenException ose) {
		Long reqId = detail.getReqId();
		String reqCode = detail.getReqCode();

		SpecimenRequirement existingReq = existing != null ? existing.getSpecimenRequirement() : null;
		if (reqId == null && !isReqCodeSpecified(detail, visit)) {
			return existingReq;
		}

		Long existingReqId = existingReq != null ? existingReq.getId() : null;
		if (reqId != null && reqId.equals(existingReqId)) {
			return existingReq;
		}
		
		String existingReqCode = existingReq != null ? existingReq.getCode() : null;
		if (reqCode != null && reqCode.equals(existingReqCode)) {
			return existingReq;
		}
		
		SpecimenRequirement sr = null;
		if (reqId != null) {
			sr = daoFactory.getSpecimenRequirementDao().getById(reqId);
		} else {
			sr = daoFactory.getSpecimenRequirementDao().getByCpEventLabelAndSrCode(
				detail.getCpShortTitle(), visit.getCpEvent().getEventLabel(), reqCode);
		}
		
		if (sr == null) {
			ose.addError(SrErrorCode.NOT_FOUND);
			return null;
		}
		
		return sr;
	}

	private boolean isReqCodeSpecified(SpecimenDetail detail, Visit visit) {
		return StringUtils.isNotBlank(detail.getCpShortTitle()) && // cp
			visit != null && visit.getCpEvent() != null &&         // visit
			StringUtils.isNotBlank(detail.getReqCode());           // req code
	}
	
	private void setCollectionStatus(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		String status = detail.getStatus();
		if (StringUtils.isBlank(status)) {
			status = Specimen.COLLECTED;
		}
		
		if (!status.equals(Specimen.COLLECTED) && 
			!status.equals(Specimen.PENDING) && 
			!status.equals(Specimen.MISSED_COLLECTION)) {
			ose.addError(SpecimenErrorCode.INVALID_COLL_STATUS);
			return;
		}

		specimen.setCollectionStatus(status);
	}
	
	private void setCollectionStatus(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("status")) {
			setCollectionStatus(detail, specimen, ose);
		} else {
			specimen.setCollectionStatus(existing.getCollectionStatus());
		}
	}
	
	private void setAnatomicSite(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		String anatomicSite = detail.getAnatomicSite();

		Specimen parent = specimen.getParentSpecimen();
		if (parent != null) {
			if (StringUtils.isBlank(anatomicSite) || anatomicSite.equals(parent.getTissueSite())) {
				specimen.setTissueSite(parent.getTissueSite());
			} else {
				ose.addError(SpecimenErrorCode.ANATOMIC_SITE_NOT_SAME_AS_PARENT, anatomicSite, parent.getTissueSite());
			}

			return;
		}

		if (specimen.isAliquot() || specimen.isDerivative()) {
			return; // invalid parent scenario
		}
		
		if (StringUtils.isBlank(anatomicSite)) {
			if (specimen.getSpecimenRequirement() == null) {
				specimen.setTissueSite(Specimen.NOT_SPECIFIED);
			}
			
			return;				
		}
		
		if (!isValid(SPECIMEN_ANATOMIC_SITE, anatomicSite, true)) {
			ose.addError(SpecimenErrorCode.INVALID_ANATOMIC_SITE, anatomicSite);
			return;
		}

		specimen.setTissueSite(anatomicSite);		
	}
	
	private void setAnatomicSite(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("anatomicSite")) {
			setAnatomicSite(detail, specimen, ose);
		} else {
			specimen.setTissueSite(existing.getTissueSite());
		}
	}

	private void setLaterality(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		String laterality = detail.getLaterality();

		Specimen parent = specimen.getParentSpecimen();
		if (parent != null) {
			if (StringUtils.isBlank(laterality) || laterality.equals(parent.getTissueSide())) {
				specimen.setTissueSide(parent.getTissueSide());
			} else {
				ose.addError(SpecimenErrorCode.LATERALITY_NOT_SAME_AS_PARENT, laterality, parent.getTissueSide());
			}

			return;
		}

		if (specimen.isAliquot() || specimen.isDerivative()) {
			return; // invalid parent scenario
		}
		
		if (StringUtils.isBlank(laterality)) {
			if (specimen.getSpecimenRequirement() == null) {
				specimen.setTissueSide(Specimen.NOT_SPECIFIED);
			}
			
			return;
		}
		
		if (!isValid(SPECIMEN_LATERALITY, laterality)) {
			ose.addError(SpecimenErrorCode.INVALID_LATERALITY);
			return;
		}
		
		specimen.setTissueSide(laterality);
	}
	
	private void setLaterality(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("laterality")) {
			setLaterality(detail, specimen, ose);
		} else {
			specimen.setTissueSide(existing.getTissueSide());
		}
	}
	
	private void setPathologicalStatus(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {		
		String pathology = detail.getPathology();

		//
		// Pick pathology status from parent if either of following is true
		// 1. specimen being created is aliquot
		// 2. specimen being created is unplanned derivative whose pathology status is blank or not specified
		//
		boolean pathFromParent = specimen.isAliquot() ||
			(specimen.isDerivative() &&
			 StringUtils.isBlank(pathology) &&
			 specimen.getSpecimenRequirement() == null);

		if (specimen.getParentSpecimen() != null && pathFromParent) {
			specimen.setPathologicalStatus(specimen.getParentSpecimen().getPathologicalStatus());
			return;
		}
		
		if (pathFromParent) {
			return; // invalid parent specimen scenario
		}
		
		if (StringUtils.isBlank(pathology)) {
			if (specimen.getSpecimenRequirement() == null) {
				specimen.setPathologicalStatus(Specimen.NOT_SPECIFIED);
			}
			
			return;
		}

		if (!isValid(PATH_STATUS, pathology)) {
			ose.addError(SpecimenErrorCode.INVALID_PATHOLOGY_STATUS);
			return;
		}
		
		specimen.setPathologicalStatus(pathology);
	}
	
	private void setPathologicalStatus(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("pathology")) {
			setPathologicalStatus(detail, specimen, ose);
		} else {
			specimen.setPathologicalStatus(existing.getPathologicalStatus());
		}
	}
	
	private void setQuantity(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("initialQty")) {
			setInitialQty(detail, specimen, ose);
		} else {
			specimen.setInitialQuantity(existing.getInitialQuantity());
		}
		
		if (existing == null || existing.isPending() || detail.isAttrModified("availableQty")) {
			setAvailableQty(detail, existing, specimen, ose);
		} else {
			specimen.setAvailableQuantity(existing.getAvailableQuantity());
		}		
	}
	
	private void setInitialQty(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		BigDecimal qty = detail.getInitialQty();
		if (NumUtil.lessThanZero(qty)) {
			ose.addError(SpecimenErrorCode.INVALID_QTY);
			return;
		}

		if (specimen.isAliquot() && qty == null) {
			if (specimen.getSpecimenRequirement() != null) {
				qty = specimen.getSpecimenRequirement().getInitialQuantity();
			}
			
			if (qty == null) {
				ose.addError(SpecimenErrorCode.ALIQUOT_QTY_REQ);
				return;
			}
		}

		specimen.setInitialQuantity(qty);
	}
	
	private void setAvailableQty(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		BigDecimal availableQty = detail.getAvailableQty();
		if (availableQty == null && (existing == null || existing.isPending())) {
			availableQty = specimen.getInitialQuantity();
		}
		
		if (NumUtil.lessThanZero(availableQty)){
			ose.addError(SpecimenErrorCode.INVALID_QTY);
			return;
		}

		if (specimen.isAliquot() && availableQty == null) {
			ose.addError(SpecimenErrorCode.ALIQUOT_QTY_REQ);
			return;
		}

		if (NumUtil.lessThan(specimen.getInitialQuantity(), availableQty)) {
			ose.addError(SpecimenErrorCode.AVBL_QTY_GT_INIT_QTY);
			return;
		}

		specimen.setAvailableQuantity(availableQty);
	}
	
	private void setConcentration(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("concentration")) {
			setConcentration(detail, specimen, ose);			
		} else {
			specimen.setConcentration(existing.getConcentration());
		}
	}
	
	private void setConcentration(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		Specimen parent = specimen.getParentSpecimen();
		if (specimen.isAliquot() && parent != null) {
			specimen.setConcentration(parent.getConcentration());
		} else if (!specimen.isAliquot()) {
			specimen.setConcentration(detail.getConcentration());
		}
	}
	
	private void setSpecimenClass(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (specimen.getParentSpecimen() != null && specimen.isAliquot()) {
			specimen.setSpecimenClass(specimen.getParentSpecimen().getSpecimenClass());
			return;
		}
		
		if (specimen.isAliquot()) {
			return; // parent not specified case
		}
				
		String specimenClass = detail.getSpecimenClass();
		if (StringUtils.isBlank(specimenClass)) {
			if (specimen.getSpecimenRequirement() == null) {
				ose.addError(SpecimenErrorCode.SPECIMEN_CLASS_REQUIRED);
			}
			
			return;
		}
		
		if (!isValid(SPECIMEN_CLASS, specimenClass)) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_CLASS);
			return;
		}
		
		specimen.setSpecimenClass(specimenClass);		
	}
	
	private void setSpecimenClass(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("specimenClass")) {
			setSpecimenClass(detail, specimen, ose);
		} else {
			specimen.setSpecimenClass(existing.getSpecimenClass());
		}
	}
	
	private void setSpecimenType(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (specimen.getParentSpecimen() != null && specimen.isAliquot()) {
			specimen.setSpecimenType(specimen.getParentSpecimen().getSpecimenType());
			return;
		}
		
		if (specimen.isAliquot()) {
			return; // parent not specified case
		}
		
		String type = detail.getType();
		if (StringUtils.isBlank(type)) {
			if (specimen.getSpecimenRequirement() == null) {
				ose.addError(SpecimenErrorCode.SPECIMEN_TYPE_REQUIRED);
			}
			
			return;
		}
		
		if (!isValid(SPECIMEN_CLASS, detail.getSpecimenClass(), type)) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_TYPE);
			return;
		}
		
		specimen.setSpecimenType(type);		
	}
	
	private void setSpecimenType(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("type")) {
			setSpecimenType(detail, specimen, ose);
		} else {
			specimen.setSpecimenType(existing.getSpecimenType());
		}
	}
	
	private void setCreatedOn(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (!specimen.isCollected()) {
			//
			// Created on date/time doesn't have any meaning unless the specimen is collected
			//
			return;
		}

		if (specimen.isPrimary() && detail.getReceivedEvent() != null) {
			specimen.setCreatedOn(detail.getReceivedEvent().getTime());
		} else if (detail.getCreatedOn() != null) {
			specimen.setCreatedOn(detail.getCreatedOn());
		} else {
			specimen.setCreatedOn(Calendar.getInstance().getTime());
		}
	}
	
	private void setCreatedOn(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("createdOn")) {
			setCreatedOn(detail, specimen, ose); 
		} else {
			specimen.setCreatedOn(existing.getCreatedOn());
		}
	}
	
	private void setBiohazards(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		Specimen parentSpecimen = specimen.getParentSpecimen();
		
		if (specimen.isAliquot()) {
			if (parentSpecimen != null) {
				specimen.setBiohazards(new HashSet<String>(parentSpecimen.getBiohazards()));
			}
			
			return;
		}
		
		Set<String> biohazards = detail.getBiohazards();
		if (CollectionUtils.isEmpty(biohazards)) {
			return;
		}
		
		if (!areValid(BIOHAZARD, biohazards)) {
			ose.addError(SpecimenErrorCode.INVALID_BIOHAZARDS);
			return;
		}
		
		specimen.setBiohazards(detail.getBiohazards());
	}
	
	private void setBiohazards(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("biohazards")) {
			setBiohazards(detail, specimen, ose);
		} else {
			specimen.setBiohazards(existing.getBiohazards());
		}
	}

	private void setFreezeThawCycles(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (detail.getFreezeThawCycles() == null) {
			return;
		}

		if (detail.getFreezeThawCycles() < 0) {
			ose.addError(SpecimenErrorCode.INVALID_FREEZE_THAW_CYCLES, detail.getFreezeThawCycles());
			return;
		}

		specimen.setFreezeThawCycles(detail.getFreezeThawCycles());
	}

	private void setFreezeThawCycles(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("freezeThawCycles")) {
			setFreezeThawCycles(detail, specimen, ose);
		} else {
			specimen.setFreezeThawCycles(existing.getFreezeThawCycles());
		}
	}

	private void setComments(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("comments")) {
			specimen.setComment(detail.getComments());
		} else {
			specimen.setComment(existing.getComment());
		}
	}

	private void setSpecimenPosition(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		StorageContainer container = null;

		StorageLocationSummary location = detail.getStorageLocation();
		if (isVirtual(location) || !specimen.isCollected()) {
			//
			// When specimen location is virtual or specimen is 
			// not collected - pending / missed collection
			//
			return;
		}

		Object key = null;
		if (location.getId() != null && location.getId() != -1) {
			key = location.getId();
			container = daoFactory.getStorageContainerDao().getById(location.getId());			
		} else {
			key = location.getName();
			container = daoFactory.getStorageContainerDao().getByName(location.getName());
		} 
		
		if (container == null) {
			ose.addError(StorageContainerErrorCode.NOT_FOUND, key);
			return;
		}
		
		if (!container.canContain(specimen)) {
			ose.addError(StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN, container.getName(), specimen.getLabelOrDesc());
			return;
		}

		String posOne = location.getPositionX(), posTwo = location.getPositionY();
		if (container.getPositionLabelingMode() == StorageContainer.PositionLabelingMode.LINEAR && location.getPosition() != 0) {
			posTwo = String.valueOf((location.getPosition() - 1) / container.getNoOfColumns() + 1);
			posOne = String.valueOf((location.getPosition() - 1) % container.getNoOfColumns() + 1);
		}

		StorageContainerPosition position = null;
		if (StringUtils.isNotBlank(posOne) && StringUtils.isNotBlank(posTwo)) {
			if (StringUtils.isBlank(location.getReservationId())) {
				if (container.canSpecimenOccupyPosition(specimen.getId(), posOne, posTwo)) {
					position = container.createPosition(posOne, posTwo);
					container.setLastAssignedPos(position);
				} else {
					ose.addError(StorageContainerErrorCode.NO_FREE_SPACE, container.getName());
				}
			} else {
				position = container.getReservedPosition(posTwo, posOne, location.getReservationId());
				if (position != null) {
					container.getOccupiedPositions().remove(position);
					position = container.createPosition(posOne, posTwo);
				} else if (container.canSpecimenOccupyPosition(specimen.getId(), posOne, posTwo)) {
					position = container.createPosition(posOne, posTwo);
				} else {
					// TODO: no free space, improve error code
					ose.addError(StorageContainerErrorCode.NO_FREE_SPACE, container.getName());
				}

				if (position != null) {
					container.setLastAssignedPos(position);
				}
			}
		} else {
			position = container.nextAvailablePosition(true);
			if (position == null) {
				ose.addError(StorageContainerErrorCode.NO_FREE_SPACE, container.getName());
			} 
		} 
		
		if (position != null) {
			position.setOccupyingSpecimen(specimen);
			specimen.setPosition(position);
		}
	}
	
	private void setSpecimenPosition(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("storageLocation")) {
			setSpecimenPosition(detail, specimen, ose);
		} else if (existing.getPosition() != null) {
			//
			// Validate container restrictions if specimen class or specimen type is modified
			//
			if (detail.isAttrModified("specimenClass") || detail.isAttrModified("specimenType")) {
				StorageContainer container = existing.getPosition().getContainer();
				if (!container.canContain(specimen)) {
					ose.addError(StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN, container.getName(), specimen.getLabelOrDesc());
					return;
				}
			}
			
			specimen.setPosition(existing.getPosition());
		}
	}
	
	private boolean isVirtual(StorageLocationSummary location) {
		if (location == null) {
			return true;
		}
		
		if (location.getId() != null && location.getId() != -1) {
			return false;
		}
		
		if (StringUtils.isNotBlank(location.getName())) {
			return false;			
		}
		
		return true;
	}
	
	private void setCollectionDetail(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (specimen.isAliquot() || specimen.isDerivative()) {
			return;
		}
				
		CollectionEventDetail collDetail = detail.getCollectionEvent();
		if (collDetail == null) {
			return;
		}
		
		SpecimenCollectionEvent event = SpecimenCollectionEvent.createFromSr(specimen);
		setEventAttrs(collDetail, event, ose);

		String collCont = collDetail.getContainer();
		if (StringUtils.isNotBlank(collCont)) {
			if (isValid(CONTAINER, collCont)) {
				event.setContainer(collCont);				
			} else {
				ose.addError(SpecimenErrorCode.INVALID_COLL_CONTAINER);
			}
		}
			
		String proc = collDetail.getProcedure();
		if (StringUtils.isNotBlank(proc)) {
			if (isValid(COLL_PROC, proc)) {
				event.setProcedure(proc);
			} else {
				ose.addError(SpecimenErrorCode.INVALID_COLL_PROC);
			}
		}
		
		specimen.setCollectionEvent(event);
	}
	
	private void setCollectionDetail(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("collectionEvent")) {
			setCollectionDetail(detail, specimen, ose);
		} else {
			specimen.setCollectionEvent(existing.getCollectionEvent());
		}
	}
	
	private void setReceiveDetail(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (specimen.isAliquot() || specimen.isDerivative()) {
			return;			
		}
		
		ReceivedEventDetail recvDetail = detail.getReceivedEvent();
		if (recvDetail == null) {
			return;
		}
		
		SpecimenReceivedEvent event = SpecimenReceivedEvent.createFromSr(specimen);
		setEventAttrs(recvDetail, event, ose);
		
		String recvQuality = recvDetail.getReceivedQuality();
		if (StringUtils.isNotBlank(recvQuality)) {
			if (isValid(RECV_QUALITY, recvQuality)) {
				event.setQuality(recvQuality);
			} else {
				ose.addError(SpecimenErrorCode.INVALID_RECV_QUALITY);
			}
		}
		
		specimen.setReceivedEvent(event);
	}
	
	private void setReceiveDetail(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("receivedEvent")) {
			setReceiveDetail(detail, specimen, ose);
		} else {
			specimen.setReceivedEvent(existing.getReceivedEvent());
		}
	}
	
	private void setEventAttrs(SpecimenEventDetail detail, SpecimenEvent event, OpenSpecimenException ose) {
		User user = getUser(detail, ose);
		if (user != null) {
			event.setUser(user);
		}
		
		if (detail.getTime() != null) {
			event.setTime(detail.getTime());
		}
		
		if (StringUtils.isNotBlank(detail.getComments())) {
			event.setComments(detail.getComments());
		}		
	}
	
	private void setExtension(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		DeObject extension = DeObject.createExtension(detail.getExtensionDetail(), specimen);
		specimen.setExtension(extension);
	}
	
	private void setExtension(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("extensionDetail")) {
			setExtension(detail, specimen, ose);
		} else {
			specimen.setExtension(existing.getExtension());
		}
	}
	
	private User getUser(SpecimenEventDetail detail, OpenSpecimenException ose) {
		if (detail.getUser() == null) {
			return null;			
		}
		
		Long userId = detail.getUser().getId();		
		String emailAddress = detail.getUser().getEmailAddress();
		
		User user = null;
		if (userId != null) {
			user = daoFactory.getUserDao().getById(userId);
		} else if (StringUtils.isNotBlank(emailAddress)) {
			user = daoFactory.getUserDao().getUserByEmailAddress(emailAddress);
		}
				
		if (user == null) {
			ose.addError(UserErrorCode.NOT_FOUND);			
		}
		
		return user;
	}

	private void setPooledSpecimen(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (!specimen.isPrimary()) {
			return;
		}

		SpecimenRequirement sr = specimen.getSpecimenRequirement();
		if (sr == null || !sr.isSpecimenPoolReq()) {
			return;
		}

		Specimen pooledSpecimen = null;
		Long pooledSpecimenId = detail.getPooledSpecimenId();
		if (pooledSpecimenId != null) {
			pooledSpecimen = daoFactory.getSpecimenDao().getById(pooledSpecimenId);
			if (pooledSpecimen == null) {
				ose.addError(SpecimenErrorCode.NOT_FOUND, pooledSpecimenId);
			}
		} else if (sr != null && sr.getPooledSpecimenRequirement() != null) {
			Long visitId = specimen.getVisit().getId();
			Long pooledSpecimenReqId = sr.getPooledSpecimenRequirement().getId();
			pooledSpecimen = daoFactory.getSpecimenDao().getSpecimenByVisitAndSr(visitId, pooledSpecimenReqId);
			if (pooledSpecimen == null) {
				if (specimen.getId() != null) {
					ose.addError(SpecimenErrorCode.NO_POOLED_SPMN);
				} else {
					pooledSpecimen = sr.getPooledSpecimenRequirement().getSpecimen();
					pooledSpecimen.setVisit(specimen.getVisit());
					pooledSpecimen.setCollectionStatus(Specimen.PENDING);
				}
			}
		}

		specimen.setPooledSpecimen(pooledSpecimen);
	}

	private void setPooledSpecimen(SpecimenDetail detail, Specimen existing, Specimen specimen, OpenSpecimenException ose) {
		if (existing == null || detail.isAttrModified("pooledSpecimenId")) {
			setPooledSpecimen(detail, specimen, ose);
		} else {
			specimen.setPooledSpecimen(existing.getPooledSpecimen());
		}
	}
}
