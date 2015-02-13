
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

public class SpecimenFactoryImpl implements SpecimenFactory {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Specimen createSpecimen(SpecimenDetail detail) {
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
		
		specimen.setVisit(visit);
		setLabel(detail, specimen, ose);
		setBarcode(detail, specimen, ose);
		setActivityStatus(detail, specimen, ose);
		setLineage(detail, specimen, ose);
		setParentSpecimen(detail, specimen, ose);
		setCollectionStatus(detail, specimen, ose);
		setAnatomicSite(detail, specimen, ose);
		setLaterality(detail, specimen, ose);
		setPathologicalStatus(detail, specimen, ose);
		setQuantity(detail, specimen, ose);
		setSpecimenClass(detail, specimen, ose);
		setSpecimenType(detail, specimen, ose);
		setCreatedOn(detail, specimen, ose);
		
//		setContainerPositions(specimenDetail, specimen, errorHandler);
		
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
		if (StringUtils.isBlank(detail.getLabel())) {
			ose.addError(SpecimenErrorCode.LABEL_REQUIRED);
			return;
		}
		
		specimen.setLabel(detail.getLabel());
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
	
	private void setParentSpecimen(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		if (specimen.getLineage().equals(Specimen.NEW)) {
			return;
		}
		
		Long parentId = detail.getParentId();
		String parentLabel = detail.getParentLabel();
		
		Specimen parent = null;
		if (parentLabel != null) {
			parent = daoFactory.getSpecimenDao().getSpecimenByLabel(parentLabel);
		} else if (parentId != null) {
			parent = daoFactory.getSpecimenDao().getById(parentId);
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
			!status.equals(Specimen.NOT_COLLECTED)) {
			ose.addError(SpecimenErrorCode.INVALID_COLL_STATUS);
		}

		specimen.setCollectionStatus(status);
	}
	
	private void setAnatomicSite(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		String anatomicSite = detail.getAnatomicSite();
		if (anatomicSite == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!isValidPv(anatomicSite, "anatomic-site")) {
			ose.addError(SpecimenErrorCode.INVALID_ANATOMIC_SITE);
			return;
		}
		
		specimen.setTissueSite(anatomicSite);		
	}

	private void setLaterality(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {		
		String laterality = detail.getLaterality();
		if (laterality == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!isValidPv(laterality, "laterality")) {
			ose.addError(SpecimenErrorCode.INVALID_LATERALITY);
			return;
		}
		
		specimen.setTissueSide(laterality);
	}
	
	private void setPathologicalStatus(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		String pathology = detail.getPathology();
		if (pathology == null && specimen.getSpecimenRequirement() != null) {
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
		
		if (qty == null) {
			ose.addError(SpecimenErrorCode.INVALID_QTY);
			return;
		}
		
		if (qty <= 0) {
			ose.addError(SpecimenErrorCode.INVALID_QTY);
			return;
		}
		
		specimen.setInitialQuantity(qty);
	}
	
	private void setSpecimenClass(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		String specimenClass = detail.getSpecimenClass();
		if (specimenClass == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!isValidPv(specimenClass, "specimen-class")) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_CLASS);
			return;
		}
		
		specimen.setSpecimenClass(specimenClass);		
	}
	
	private void setSpecimenType(SpecimenDetail detail, Specimen specimen, OpenSpecimenException ose) {
		String type = detail.getType();
		if (type == null && specimen.getSpecimenRequirement() != null) {
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

		if (specimen.getVisit() == null) {
			return;
		}

		Date visitDate = specimen.getVisit().getVisitDate();
		if (visitDate.after(specimen.getCreatedOn())) {
			ose.addError(SpecimenErrorCode.INVALID_CREATION_DATE);
		}
	}	
}
