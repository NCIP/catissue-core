
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.HashSet;
import java.util.Set;

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
		setActivityStatus(specimenDetail, specimen, errorHandler);
		setSpecimenRequirement(specimenDetail, specimen, errorHandler);
		setCollectionStatus(specimenDetail, specimen, errorHandler);
		setTissueSite(specimenDetail, specimen, errorHandler);
		setTissueSide(specimenDetail, specimen, errorHandler);
		setPathologyStatus(specimenDetail, specimen, errorHandler);
		setQuantity(specimenDetail, specimen, errorHandler);
		setClassAndType(specimenDetail, specimen, errorHandler);
		setLabel(specimenDetail, specimen, errorHandler);
		setBarcode(specimenDetail, specimen, errorHandler);
		setComment(specimenDetail, specimen);
		setCreatedOn(specimenDetail, specimen, errorHandler);
		setContainerPositions(specimenDetail, specimen, errorHandler);
		setBiohazardCollection(specimenDetail, specimen, errorHandler);
		setExternalIdsCollection(specimenDetail, specimen, errorHandler);
		errorHandler.checkErrorAndThrow();
		return specimen;
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

	private void setActivityStatus(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		if (isValidPv(specimenDetail.getActivityStatus(), Status.ACTIVITY_STATUS.toString())) {
			specimen.setActivityStatus(specimenDetail.getActivityStatus());
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.toString());
	}

	private void setTissueSite(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		if (isValidPv(specimenDetail.getTissueSite(), TISSUE_SITE)) {
			specimen.setTissueSite(specimenDetail.getTissueSite());
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, TISSUE_SITE);
	}

	private void setTissueSide(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		if (isValidPv(specimenDetail.getTissueSide(), TISSUE_SIDE)) {
			specimen.setTissueSide(specimenDetail.getTissueSide());
			return;
		}
		errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, TISSUE_SIDE);
	}

	private void setPathologyStatus(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		if (isValidPv(specimenDetail.getPathologicalStatus(), PATHOLOGY_STATUS)) {
			specimen.setPathologicalStatus(specimenDetail.getPathologicalStatus());
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

	private void setLabel(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		if (specimen.isCollected() && isBlank(specimenDetail.getLabel())) {
			errorHandler.addError(ScgErrorCode.MISSING_ATTR_VALUE, LABEL);
		}
		specimen.setLabel(specimenDetail.getLabel());
	}

	private void setBarcode(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		specimen.setBarcode(specimenDetail.getBarcode());
	}

	private void setComment(SpecimenDetail specimenDetail, Specimen specimen) {
		specimen.setComment(specimenDetail.getComment());
	}

	private void setCreatedOn(SpecimenDetail specimenDetail, Specimen specimen, ObjectCreationException errorHandler) {
		specimen.setCreatedOn(specimenDetail.getCreatedOn());
	}

	private void setCollectionStatus(SpecimenDetail specimenDetail, Specimen specimen,
			ObjectCreationException errorHandler) {
		if (isValidPv(specimenDetail.getCollectionStatus(), COLLECTION_STATUS)) {
			specimen.setCollectionStatus(specimenDetail.getCollectionStatus());
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
