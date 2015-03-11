
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo.StorageLocationSummary;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class SpecimenFactoryImpl implements SpecimenFactory {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Specimen createSpecimen(SpecimenDetail detail, Specimen parent) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		SpecimenRequirement sr = getSpecimenRequirement(detail, ose);
		Visit visit = getVisit(detail, ose);
		
		if (sr != null && visit != null) {
			if (!sr.getCollectionProtocolEvent().getId().equals(visit.getCpEvent().getId())) {
				ose.addError(SpecimenErrorCode.INVALID_VISIT);
				throw ose;
			}			
		}
		
		Specimen specimen = null;
		if (sr != null) {
			specimen = sr.getSpecimen();
		} else {
			specimen = new Specimen();
		}
		
		specimen.setId(detail.getId());
		specimen.setVisit(visit);
		
		setCollectionStatus(detail, specimen, ose);
		setLineage(detail, specimen, ose);
		setParentSpecimen(detail, parent, specimen, ose);
				
		setLabel(detail, specimen, ose);
		setBarcode(detail, specimen, ose);
		setActivityStatus(detail, specimen, ose);
						
		setAnatomicSite(detail, specimen, ose);
		setLaterality(detail, specimen, ose);
		setPathologicalStatus(detail, specimen, ose);
		setQuantity(detail, specimen, ose);
		setSpecimenClass(detail, specimen, ose);
		setSpecimenType(detail, specimen, ose);
		setCreatedOn(detail, specimen, ose);
				
		if (sr != null && 
				(!sr.getSpecimenClass().equals(specimen.getSpecimenClass()) ||
					!sr.getSpecimenType().equals(specimen.getSpecimenType()))) {
			specimen.setSpecimenRequirement(null);
		}
		
		setSpecimenPosition(detail, specimen, ose);

		ose.checkAndThrow();
		return specimen;
	}

	private void setBarcode(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if(StringUtils.isBlank(detail.getBarcode())) {
			return;
		}

		specimen.setBarcode(detail.getBarcode());
	}

	private void setActivityStatus(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (StringUtils.isBlank(detail.getActivityStatus())) {
			specimen.setActive();
		} else if (isValidPv(detail.getActivityStatus(), Status.ACTIVITY_STATUS.getStatus())) {
			specimen.setActivityStatus(detail.getActivityStatus());
		} else {
			ose.addError(ActivityStatusErrorCode.INVALID);
		}
	}
	
	private void setLabel(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (StringUtils.isNotBlank(detail.getLabel())) {
			specimen.setLabel(detail.getLabel());
		} else if (specimen.isCollected()) { // blank and status is collected
			ose.addError(SpecimenErrorCode.LABEL_REQUIRED);
		}		
	}

	private Visit getVisit(SpecimenDetail detail, OpenSpecimenException ose) {
		Long visitId = detail.getVisitId();
		if (visitId == null) {
			ose.addError(SpecimenErrorCode.VISIT_REQUIRED);
			return null;
		}
		
		Visit visit = daoFactory.getVisitsDao().getById(visitId);
		if (visit == null) {
			ose.addError(VisitErrorCode.NOT_FOUND);
			return null;
		}
		
		return visit;
	}
	
	private void setLineage(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		String lineage = detail.getLineage();
		if (lineage == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!lineage.equals(Specimen.NEW) && 
		    !lineage.equals(Specimen.ALIQUOT) && 
		    !lineage.equals(Specimen.DERIVED)) {
			ose.addError(SpecimenErrorCode.INVALID_LINEAGE);
			return;
		}
		
		specimen.setLineage(lineage);
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
		
		boolean parentSpecified = true;
		if (StringUtils.isNotBlank(parentLabel)) {
			parent = daoFactory.getSpecimenDao().getByLabel(parentLabel);
		} else if (parentId != null) {
			parent = daoFactory.getSpecimenDao().getById(parentId);
		} else if (specimen.getVisit() != null && specimen.getSpecimenRequirement() != null) {
			Long visitId = specimen.getVisit().getId();
			Long srId = specimen.getSpecimenRequirement().getId();
			parent = daoFactory.getSpecimenDao().getParentSpecimenByVisitAndSr(visitId, srId);
		} else {
			parentSpecified = false;
		}
		
		if (parent == null) {
			ose.addError(parentSpecified ? SpecimenErrorCode.NOT_FOUND : SpecimenErrorCode.PARENT_REQUIRED);
		}
		
		specimen.setParentSpecimen(parent);
	}
	
	private SpecimenRequirement getSpecimenRequirement(SpecimenDetail detail, OpenSpecimenException ose) {
		Long reqId = detail.getReqId();
		if (reqId == null) {
			return null;
		}
		
		SpecimenRequirement sr = daoFactory.getSpecimenRequirementDao().getById(reqId);
		if (sr == null) {
			ose.addError(SrErrorCode.NOT_FOUND);
			return null;
		}
		
		return sr;
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
		}

		specimen.setCollectionStatus(status);
	}
	
	private void setAnatomicSite(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (specimen.getParentSpecimen() != null) {
			specimen.setTissueSite(specimen.getParentSpecimen().getTissueSite());
			return;
		}
		
		if (specimen.isAliquot() || specimen.isDerivative()) {
			return; // invalid parent scenario
		}
		
		String anatomicSite = detail.getAnatomicSite();
		if (StringUtils.isBlank(anatomicSite) && specimen.getSpecimenRequirement() != null) {
			specimen.setTissueSite(specimen.getSpecimenRequirement().getAnatomicSite());
			return;
		} else if (StringUtils.isBlank(anatomicSite)) {
			ose.addError(SpecimenErrorCode.ANATOMIC_SITE_REQUIRED);
			return;
		}
		
		if (!isValidPv(anatomicSite, "anatomic-site")) {
			ose.addError(SpecimenErrorCode.INVALID_ANATOMIC_SITE);
			return;
		}
		
		specimen.setTissueSite(anatomicSite);		
	}

	private void setLaterality(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (specimen.getParentSpecimen() != null) {
			specimen.setTissueSide(specimen.getParentSpecimen().getTissueSide());
			return;
		}
		
		if (specimen.isAliquot() || specimen.isDerivative()) {
			return; // invalid parent scenario
		}
		
		String laterality = detail.getLaterality();
		if (StringUtils.isBlank(laterality) && specimen.getSpecimenRequirement() != null) {
			specimen.setTissueSide(specimen.getSpecimenRequirement().getLaterality());
			return;
		} else if (StringUtils.isBlank(laterality)) {
			ose.addError(SpecimenErrorCode.LATERALITY_REQUIRED);
			return;
		}
		
		if (!isValidPv(laterality, "laterality")) {
			ose.addError(SpecimenErrorCode.INVALID_LATERALITY);
			return;
		}
		
		specimen.setTissueSide(laterality);
	}
	
	private void setPathologicalStatus(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (specimen.getParentSpecimen() != null && specimen.isAliquot()) {
			specimen.setPathologicalStatus(specimen.getParentSpecimen().getPathologicalStatus());
			return;
		}
		
		if (specimen.isAliquot()) {
			return; // parent specimen not specified
		}
		
		String pathology = detail.getPathology();
		if (StringUtils.isBlank(pathology) && specimen.getSpecimenRequirement() != null) {
			specimen.setPathologicalStatus(specimen.getSpecimenRequirement().getPathologyStatus());
			return;
		} else if (StringUtils.isBlank(pathology)) {
			ose.addError(SpecimenErrorCode.PATHOLOGY_STATUS_REQUIRED);
			return;
		}

		if (!isValidPv(detail.getPathology(), "pathological-status")) {
			ose.addError(SpecimenErrorCode.INVALID_PATHOLOGY_STATUS);
			return;
		}
		
		specimen.setPathologicalStatus(detail.getPathology());
	}
	
	private void setQuantity(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		Double qty = detail.getInitialQty();
		if (qty == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!specimen.isCollected()) { 
			//
			// Do not need quantity when specimen is not collected
			//
			return;
		}
		
		if (qty == null || qty <= 0) {
			ose.addError(SpecimenErrorCode.INVALID_QTY);
			return;
		}
		
		
		specimen.setInitialQuantity(qty);
						
		Double availableQty = detail.getAvailableQty();
		if (availableQty == null) {
			availableQty = qty;
		}
		
		if (availableQty > qty || availableQty < 0) {
			ose.addError(SpecimenErrorCode.INVALID_QTY);
			return;
		}
		
		specimen.setAvailableQuantity(availableQty);
		
		if (detail.getAvailable() == null) {
			specimen.setIsAvailable(availableQty > 0);
		} else {
			specimen.setIsAvailable(detail.getAvailable());
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
		if (StringUtils.isBlank(specimenClass) && specimen.getSpecimenRequirement() != null) {
			specimen.setSpecimenClass(specimen.getSpecimenRequirement().getSpecimenClass());
			return;
		} else if (StringUtils.isBlank(specimenClass)) {
			ose.addError(SpecimenErrorCode.SPECIMEN_CLASS_REQUIRED);
			return;
		}
		
		if (!isValidPv(specimenClass, "specimen-class")) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_CLASS);
			return;
		}
		
		specimen.setSpecimenClass(specimenClass);		
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
		if (StringUtils.isBlank(type) && specimen.getSpecimenRequirement() != null) {
			specimen.setSpecimenType(specimen.getSpecimenRequirement().getSpecimenType());
			return;
		} else if (StringUtils.isBlank(type)) {
			ose.addError(SpecimenErrorCode.SPECIMEN_TYPE_REQUIRED);
			return;
		}
		
		if (!isValidPv(specimen.getSpecimenClass(), type, "specimen-type")) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_TYPE);
			return;
		}
		
		specimen.setSpecimenType(type);		
	}
	
	private void setCreatedOn(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (detail.getCreatedOn() == null) {
			specimen.setCreatedOn(Calendar.getInstance().getTime());
		} else {
			specimen.setCreatedOn(detail.getCreatedOn());
		}

		// TODO: Whether create date can be before visit
//		if (specimen.getVisit() == null) {
//			return;
//		}
//
//		Date visitDate = specimen.getVisit().getVisitDate();
//		if (visitDate.after(specimen.getCreatedOn())) {
//			ose.addError(SpecimenErrorCode.INVALID_CREATION_DATE);
//		}
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
		
		if (location.id != null && location.id != -1) {
			container = daoFactory.getStorageContainerDao().getById(location.id);			
		} else {
			container = daoFactory.getStorageContainerDao().getByName(location.name);
		} 
		
		if (container == null) {
			ose.addError(StorageContainerErrorCode.NOT_FOUND);
			return;
		}
		
		if (!container.canContain(specimen)) {
			ose.addError(StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN);			
		}
		
		StorageContainerPosition position = null;
		String posOne = location.positionX, posTwo = location.positionY;
		if (StringUtils.isNotBlank(posOne) && StringUtils.isNotBlank(posTwo)) {
			if (container.canSpecimenOccupyPosition(specimen.getId(), posOne, posTwo)) {
				position = container.createPosition(posOne, posTwo);
			} else {
				ose.addError(StorageContainerErrorCode.NO_FREE_SPACE);
			}
		} else {
			position = container.nextAvailablePosition();
			if (position == null) {
				ose.addError(StorageContainerErrorCode.NO_FREE_SPACE);
			} 
		} 
		
		if (position != null) {
			position.setOccupyingSpecimen(specimen);
			specimen.setPosition(position);
		}
	}
	
	private boolean isVirtual(StorageLocationSummary location) {
		if (location == null) {
			return true;
		}
		
		if (location.id != null && location.id != -1) {
			return false;
		}
		
		if (StringUtils.isNotBlank(location.name)) {
			return false;			
		}
		
		return true;
	}
}
