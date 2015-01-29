
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

public class SpecimenFactoryImpl implements SpecimenFactory {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Specimen createSpecimen(SpecimenDetail detail) {
		ObjectCreationException oce = new ObjectCreationException();

		SpecimenRequirement sr = getSpecimenRequirement(detail, oce);
		Visit visit = getVisit(detail, oce);
		
		if (sr != null && visit != null) {
			if (!sr.getCollectionProtocolEvent().getId().equals(visit.getCpEvent().getId())) {
				oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "visitId");
				throw oce;
			}			
		}
		
		Specimen specimen = null;
		if (sr != null) {
			specimen = sr.getSpecimen();
		} else {
			specimen = new Specimen();
		}
		
		specimen.setVisit(visit);
		setLabel(detail, specimen, oce);
		setBarcode(detail, specimen, oce);
		setActivityStatus(detail, specimen, oce);
		setLineage(detail, specimen, oce);
		setParentSpecimen(detail, specimen, oce);
		setCollectionStatus(detail, specimen, oce);
		setAnatomicSite(detail, specimen, oce);
		setLaterality(detail, specimen, oce);
		setPathologicalStatus(detail, specimen, oce);
		setQuantity(detail, specimen, oce);
		setSpecimenClass(detail, specimen, oce);
		setSpecimenType(detail, specimen, oce);
		setCreatedOn(detail, specimen, oce);
		
//		setContainerPositions(specimenDetail, specimen, errorHandler);
		
		oce.checkErrorAndThrow();
		return specimen;
	}

	private void setBarcode(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
		if(StringUtils.isBlank(detail.getBarcode())) {
			return;
		}

		specimen.setBarcode(detail.getBarcode());
	}

	private void setActivityStatus(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
		if (StringUtils.isBlank(detail.getActivityStatus())) {
			specimen.setActive();
		} else if (isValidPv(detail.getActivityStatus(), Status.ACTIVITY_STATUS.getStatus())) {
			specimen.setActivityStatus(detail.getActivityStatus());
		} else {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.toString());
		}
	}
	
	private void setLabel(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
		if (StringUtils.isBlank(detail.getLabel())) {
			oce.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, "label");
			return;
		}
		
		specimen.setLabel(detail.getLabel());
	}

	private Visit getVisit(SpecimenDetail detail, ObjectCreationException oce) {
		Long visitId = detail.getVisitId();
		if (visitId == null) {
			oce.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, "visitId");
			return null;
		}
		
		Visit visit = daoFactory.getVisitsDao().getById(visitId);
		if (visit == null) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "visitId");
			return null;
		}
		
		return visit;
	}
	
	private void setLineage(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
		String lineage = detail.getLineage();
		if (lineage == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!lineage.equals(Specimen.NEW) && 
		    !lineage.equals(Specimen.ALIQUOT) && 
		    !lineage.equals(Specimen.DERIVED)) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "lineage");
			return;
		}
		
		specimen.setLineage(lineage);
	}
	
	private void setParentSpecimen(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
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
	
	private SpecimenRequirement getSpecimenRequirement(SpecimenDetail detail, ObjectCreationException oce) {
		Long reqId = detail.getReqId();
		if (reqId == null) {
			return null;
		}
		
		SpecimenRequirement sr = daoFactory.getSpecimenRequirementDao().getById(reqId);
		if (sr == null) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "reqId");
			return null;
		}
		
		return sr;
	}
	
	private void setCollectionStatus(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
		String status = detail.getStatus();
		if (StringUtils.isBlank(status)) {
			status = Specimen.COLLECTED;
		}
		
		if (!status.equals(Specimen.COLLECTED) && 
			!status.equals(Specimen.PENDING) && 
			!status.equals(Specimen.NOT_COLLECTED)) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "status");
		}

		specimen.setCollectionStatus(status);
	}
	
	private void setAnatomicSite(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
		String anatomicSite = detail.getAnatomicSite();
		if (anatomicSite == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!isValidPv(anatomicSite, "anatomic-site")) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "anatomicSite");
			return;
		}
		
		specimen.setTissueSite(anatomicSite);		
	}

	private void setLaterality(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {		
		String laterality = detail.getLaterality();
		if (laterality == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!isValidPv(laterality, "laterality")) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "laterality");
			return;
		}
		
		specimen.setTissueSide(laterality);
	}
	
	private void setPathologicalStatus(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
		String pathology = detail.getPathology();
		if (pathology == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!isValidPv(detail.getPathology(), "pathological-status")) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "pathology");
			return;
		}
		
		specimen.setPathologicalStatus(detail.getPathology());
	}
	
	private void setQuantity(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
		Double qty = detail.getInitialQty();
		if (qty == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (qty == null) {
			oce.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, "initialQty");
			return;
		}
		
		if (qty <= 0) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "initialQty");
			return;
		}
		
		specimen.setInitialQuantity(qty);
	}
	
	private void setSpecimenClass(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
		String specimenClass = detail.getSpecimenClass();
		if (specimenClass == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!isValidPv(specimenClass, "specimen-class")) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "specimen-class");
			return;
		}
		
		specimen.setSpecimenClass(specimenClass);		
	}
	
	private void setSpecimenType(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
		String type = detail.getType();
		if (type == null && specimen.getSpecimenRequirement() != null) {
			return;
		}
		
		if (!isValidPv(specimen.getSpecimenClass(), type, "specimen-type")) {
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "specimen-type");
			return;
		}
		
		specimen.setSpecimenType(type);		
	}
	
	private void setCreatedOn(SpecimenDetail detail, Specimen specimen, ObjectCreationException oce) {
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
			oce.addError(SpecimenErrorCode.INVALID_ATTR_VALUE, "createdOn");
		}
	}	
}
