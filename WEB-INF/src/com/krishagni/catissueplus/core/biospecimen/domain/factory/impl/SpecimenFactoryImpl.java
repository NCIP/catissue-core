
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.BiohazardDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ExternalIdentifierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;

public class SpecimenFactoryImpl implements SpecimenFactory {

	private static final String SCG = "specimen collection group";

	private static final String LABEL = "label";

	private static final String PARENT = "parent";

	private static final String PARENT_SPECIMEN = "parent specimen";

	private static final String SPECIMEN_REQUIREMENT = "specimen requirement";

	private static final String TISSUE_SIDE = "tissue side";

	private static final String TISSUE_SITE = "tissue site";

	private static final String SPECIMEN_CLASS = "specimen class";

	private static final String SPECIMEN_TYPE = "specimen type";

	private static final String PATHOLOGY_STATUS = "pathology status";

	private static final String COLLECTION_STATUS = "collection status";

	private static final String CONTAINER_NAME = "container name";

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Specimen createSpecimen(SpecimenDetail specimenDetail) {
		ObjectCreationException errorHandler = new ObjectCreationException();
		Specimen specimen = new Specimen();
		setScg(specimenDetail, specimen, errorHandler);
		setParentSpecimen(specimenDetail, specimen, errorHandler); //check for parent in this method
		setActivityStatus(specimenDetail.getActivityStatus(), specimen, errorHandler);
		setSpecimenRequirement(specimenDetail, specimen, errorHandler);
		setCollectionStatus(specimenDetail.getCollectionStatus(), specimen, errorHandler);
		setTissueSite(specimenDetail.getTissueSite(), specimen, errorHandler);
		setTissueSide(specimenDetail.getTissueSide(), specimen, errorHandler);
		setPathologyStatus(specimenDetail.getPathologicalStatus(), specimen, errorHandler);
		setQuantity(specimenDetail, specimen, errorHandler);
		setClassAndType(specimenDetail, specimen, errorHandler);
		setLabel(specimenDetail.getLabel(), specimen, errorHandler);
		setBarcode(specimenDetail.getBarcode(), specimen, errorHandler);
		setComment(specimenDetail.getComment(), specimen);
		setCreatedOn(specimenDetail.getCreatedOn(), specimen, errorHandler);
		setContainerPositions(specimenDetail, specimen, errorHandler);
		setBiohazardCollection(specimenDetail, specimen, errorHandler);
		setExternalIdsCollection(specimenDetail, specimen, errorHandler);
		errorHandler.checkErrorAndThrow();
		return specimen;
	}

	@Override
	public Specimen patch(Specimen specimen, Map<String, Object> specimenProps) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		Iterator<Entry<String, Object>> entries = specimenProps.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, Object> entry = entries.next();
			if ("label".equals(entry.getKey())) {
				setLabel(String.valueOf(entry.getValue()), specimen, exceptionHandler);
			}
			if ("barcode".equals(entry.getKey())) {
				setBarcode(String.valueOf(entry.getValue()), specimen, exceptionHandler);
			}
			if ("tissueSite".equals(entry.getKey())) {
				setTissueSite(String.valueOf(entry.getValue()), specimen, exceptionHandler);
			}
			if ("tissueSide".equals(entry.getKey())) {
				setTissueSide(String.valueOf(entry.getValue()), specimen, exceptionHandler);
			}
			if ("pathologyStatus".equals(entry.getKey())) {
				setPathologyStatus(String.valueOf(entry.getValue()), specimen, exceptionHandler);
			}
			if ("activityStatus".equals(entry.getKey())) {
				setActivityStatus(String.valueOf(entry.getValue()), specimen, exceptionHandler);
			}
			if ("collectionStatus".equals(entry.getKey())) {
				setCollectionStatus(String.valueOf(entry.getValue()), specimen, exceptionHandler);
			}
			if ("createdOn".equals(entry.getKey())) {
				setLabel(String.valueOf(entry.getValue()), specimen, exceptionHandler);
			}
			if ("specimenClass".equals(entry.getKey())) {
				setLabel(String.valueOf(entry.getValue()), specimen, exceptionHandler);
			}
			if ("specimenType".equals(entry.getKey())) {
				setLabel(String.valueOf(entry.getValue()), specimen, exceptionHandler);
			}
		}
		return null;
	}

	private void setScg(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		if (specimenDetail.getScgId() == null) {
			errorHandler.addError(ScgErrorCode.MISSING_ATTR_VALUE, SCG);
			return;
		}
		SpecimenCollectionGroup scg = daoFactory.getScgDao().getscg(specimenDetail.getScgId());
		if (scg == null) {
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, SCG);
			return;
		}
		specimen.setSpecimenCollectionGroup(scg);
	}

	private void setParentSpecimen(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		if (specimenDetail.getParentSpecimenId() == null && !"New".equals(specimenDetail.getLineage())) {
			errorHandler.addError(ScgErrorCode.MISSING_ATTR_VALUE, PARENT);
			return;
		}
		Specimen parentSpecimen = daoFactory.getSpecimenDao().getSpecimen(specimenDetail.getParentSpecimenId());
		if (parentSpecimen == null) {
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, PARENT_SPECIMEN);
			return;
		}
		specimen.setParentSpecimen(parentSpecimen);
	}

	private void setSpecimenRequirement(SpecimenDetail specimenDetail, Specimen specimen,
			ObjectCreationException errorHandler) {
		if (specimenDetail.getRequirementId() == null) {
			return;
		}
		SpecimenRequirement requirement = daoFactory.getCollectionProtocolDao().getSpecimenRequirement(
				specimenDetail.getRequirementId());
		if (requirement == null) {
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, SPECIMEN_REQUIREMENT);
			return;
		}
		specimen.setSpecimenRequirement(requirement);
	}

	private void setActivityStatus(String activityStatus, Specimen specimen, ObjectCreationException errorHandler) {
		if (isValidPv(activityStatus, Status.ACTIVITY_STATUS.toString())) {
			specimen.setActivityStatus(activityStatus);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.toString());
	}

	private void setTissueSite(String tissueSite, Specimen specimen, ObjectCreationException errorHandler) {
		if (isValidPv(tissueSite, TISSUE_SITE)) {
			specimen.setTissueSite(tissueSite);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, TISSUE_SITE);
	}

	private void setTissueSide(String tissueSide, Specimen specimen, ObjectCreationException errorHandler) {
		if (isValidPv(tissueSide, TISSUE_SIDE)) {
			specimen.setTissueSide(tissueSide);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, TISSUE_SIDE);
	}

	private void setPathologyStatus(String pathStatus, Specimen specimen, ObjectCreationException errorHandler) {
		if (isValidPv(pathStatus, PATHOLOGY_STATUS)) {
			specimen.setPathologicalStatus(pathStatus);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, TISSUE_SIDE);
	}

	private void setQuantity(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		specimen.setInitialQuantity(specimenDetail.getInitialQuantity());
		specimen.setAvailableQuantity(specimenDetail.getAvailableQuantity());
	}

	private void setClassAndType(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		if (isValidPv(specimenDetail.getSpecimenClass(), SPECIMEN_CLASS)) {
			specimen.setSpecimenClass(specimenDetail.getSpecimenClass());
			if (isValidPv(specimenDetail.getSpecimenClass(), specimenDetail.getSpecimenType(), SPECIMEN_TYPE)) {
				specimen.setSpecimenType(specimenDetail.getSpecimenType());
				return;
			}
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, SPECIMEN_TYPE);
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, SPECIMEN_CLASS);
	}

	private void setLabel(String label, Specimen specimen, ObjectCreationException errorHandler) {
		if (specimen.isCollected() && isBlank(label)) {
			errorHandler.addError(ScgErrorCode.MISSING_ATTR_VALUE, LABEL);
		}
		specimen.setLabel(label);
	}

	private void setBarcode(String barcode, Specimen specimen, ObjectCreationException errorHandler) {
		specimen.setBarcode(barcode);
	}

	private void setComment(String comment, Specimen specimen) {
		specimen.setComment(comment);
	}

	private void setCreatedOn(Date createdOn, Specimen specimen, ObjectCreationException errorHandler) {
		specimen.setCreatedOn(createdOn);
	}

	private void setCollectionStatus(String collectionStatus, Specimen specimen,
			ObjectCreationException errorHandler) {
		if (isValidPv(collectionStatus, COLLECTION_STATUS)) {
			specimen.setCollectionStatus(collectionStatus);
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, COLLECTION_STATUS);
	}

	private void setContainerPositions(SpecimenDetail specimenDetail, Specimen specimen,
			ObjectCreationException errorHandler) {
		if (isBlank(specimenDetail.getContainerName()) && isBlank(specimenDetail.getPos1())
				&& isBlank(specimenDetail.getPos2())) {
			//			specimen.setSpecimenPosition(null);
			return;
		}
		StorageContainer container = daoFactory.getContainerDao().getContainer(specimenDetail.getContainerName());
		if (container == null) {
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, CONTAINER_NAME);
			return;
		}
		//TODO check for valid container positions
		//		ContainerUtil.checkAndAssignPositions(container,specimenDetail.getPos1(),specimenDetail.getPos2(),specimen);
	}

	private void setBiohazardCollection(SpecimenDetail specimenDetail, Specimen specimen,
			ObjectCreationException errorHandler) {
		if (specimenDetail.getBiohazardDetails() == null && specimenDetail.getBiohazardDetails().isEmpty()) {
			return;
		}
		for (BiohazardDetail detail : specimenDetail.getBiohazardDetails()) {
			//TODO yet to handle Biohazard
		}
	}

	private void setExternalIdsCollection(SpecimenDetail specimenDetail, Specimen specimen,
			ObjectCreationException errorHandler) {
		//need to call the common update collection method
		if (specimenDetail.getExternalIdentifierDetails() == null
				&& specimenDetail.getExternalIdentifierDetails().isEmpty()) {
			return;
		}
		Set<ExternalIdentifier> externalIdentifiers = new HashSet<ExternalIdentifier>();
		//		SetUpdater.<String> newInstance().update(externalIdentifiers, specimenDetail.getExternalIdentifierDetails());

		for (ExternalIdentifierDetail extIdDetail : specimenDetail.getExternalIdentifierDetails()) {
			ExternalIdentifier identifier = new ExternalIdentifier();
			identifier.setName(extIdDetail.getName());
			identifier.setValue(extIdDetail.getValue());
			//			identifier.setSpecimen(specimen);
			externalIdentifiers.add(identifier);
		}
		specimen.setExternalIdentifierCollection(externalIdentifiers);
	}

	//	private void addError(CatissueErrorCode event, String field) {
	//		exception.addError(event, field);
	//	}
}
