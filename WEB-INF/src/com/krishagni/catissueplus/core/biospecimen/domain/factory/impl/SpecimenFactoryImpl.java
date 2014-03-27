
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.BiohazardDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ExternalIdentifierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.CatissueErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErroneousField;
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

	private List<ErroneousField> erroneousFields;

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public Specimen createSpecimen(SpecimenDetail specimenDetail, ObjectCreationException exceptionHandler) {
		erroneousFields = new ArrayList<ErroneousField>();
		Specimen specimen = new Specimen();
		setParent(specimenDetail, specimen); //check for parent in this method
		setActivityStatus(specimenDetail, specimen);
		setSpecimenRequirement(specimenDetail, specimen);
		setCollectionStatus(specimenDetail, specimen);
		setTissueSite(specimenDetail, specimen);
		setTissueSide(specimenDetail, specimen);
		setPathologyStatus(specimenDetail, specimen);
		setQuantity(specimenDetail, specimen);
		setClassAndType(specimenDetail, specimen);
		setLabel(specimenDetail, specimen);
		setBarcode(specimenDetail, specimen);
		setComment(specimenDetail, specimen);
		setCreatedOn(specimenDetail, specimen);
		setContainerPositions(specimenDetail, specimen);
		setBiohazardCollection(specimenDetail, specimen);
		setExternalIdsCollection(specimenDetail, specimen);
		exceptionHandler.addError(erroneousFields);
		return specimen;
	}

	private void setParent(SpecimenDetail specimenDetail, Specimen specimen) {
		if (specimenDetail.getScgId() == null) {
			setParentSpecimen(specimenDetail, specimen);
		}
		else {
			SpecimenCollectionGroup scg = daoFactory.getScgDao().getscg(specimenDetail.getScgId());
			if (scg == null) {
				addError(ScgErrorCode.INVALID_ATTR_VALUE, SCG);
				return;
			}
			specimen.setSpecimenCollectionGroup(scg);
		}
	}

	private void setSpecimenRequirement(SpecimenDetail specimenDetail, Specimen specimen) {
		if (specimenDetail.getRequirementId() == null) {
			return;
		}
		SpecimenRequirement requirement = daoFactory.getCollectionProtocolDao().getSpecimenRequirement(
				specimenDetail.getRequirementId());
		if (requirement == null) {
			addError(ScgErrorCode.INVALID_ATTR_VALUE, SPECIMEN_REQUIREMENT);
			return;
		}
		specimen.setSpecimenRequirement(requirement);
	}

	private void setActivityStatus(SpecimenDetail specimenDetail, Specimen specimen) {
		if (isValidPv(specimenDetail.getActivityStatus(), Status.ACTIVITY_STATUS.toString())) {
			specimen.setActivityStatus(specimenDetail.getActivityStatus());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, Status.ACTIVITY_STATUS.toString());
	}

	private void setTissueSite(SpecimenDetail specimenDetail, Specimen specimen) {
		if (isValidPv(specimenDetail.getTissueSite(), TISSUE_SITE)) {
			specimen.setTissueSite(specimenDetail.getTissueSite());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, TISSUE_SITE);
	}

	private void setTissueSide(SpecimenDetail specimenDetail, Specimen specimen) {
		if (isValidPv(specimenDetail.getTissueSide(), TISSUE_SIDE)) {
			specimen.setTissueSide(specimenDetail.getTissueSide());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, TISSUE_SIDE);
	}

	private void setPathologyStatus(SpecimenDetail specimenDetail, Specimen specimen) {
		if (isValidPv(specimenDetail.getPathologicalStatus(), PATHOLOGY_STATUS)) {
			specimen.setPathologicalStatus(specimenDetail.getPathologicalStatus());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, TISSUE_SIDE);
	}

	private void setQuantity(SpecimenDetail specimenDetail, Specimen specimen) {
		specimen.setInitialQuantity(specimenDetail.getInitialQuantity());
		specimen.setAvailableQuantity(specimenDetail.getAvailableQuantity());
	}

	private void setClassAndType(SpecimenDetail specimenDetail, Specimen specimen) {
		if (isValidPv(specimenDetail.getSpecimenClass(), SPECIMEN_CLASS)) {
			specimen.setSpecimenClass(specimenDetail.getSpecimenClass());
			if (isValidPv(specimenDetail.getSpecimenClass(), specimenDetail.getSpecimenType(), SPECIMEN_TYPE)) {
				specimen.setSpecimenType(specimenDetail.getSpecimenType());
				return;
			}
			addError(ScgErrorCode.INVALID_ATTR_VALUE, SPECIMEN_TYPE);
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, SPECIMEN_CLASS);
	}

	private void setLabel(SpecimenDetail specimenDetail, Specimen specimen) {
		if (specimen.isCollected() && isBlank(specimenDetail.getLabel())) {
			addError(ScgErrorCode.MISSING_ATTR_VALUE, LABEL);
		}
		specimen.setLabel(specimenDetail.getLabel());
	}

	private void setBarcode(SpecimenDetail specimenDetail, Specimen specimen) {
		specimen.setBarcode(specimenDetail.getBarcode());
	}

	private void setComment(SpecimenDetail specimenDetail, Specimen specimen) {
		specimen.setComment(specimenDetail.getComment());
	}

	private void setCreatedOn(SpecimenDetail specimenDetail, Specimen specimen) {
		specimen.setCreatedOn(specimenDetail.getCreatedOn());
	}

	private void setCollectionStatus(SpecimenDetail specimenDetail, Specimen specimen) {
		if (isValidPv(specimenDetail.getCollectionStatus(), COLLECTION_STATUS)) {
			specimen.setCollectionStatus(specimenDetail.getCollectionStatus());
			return;
		}
		addError(ScgErrorCode.INVALID_ATTR_VALUE, COLLECTION_STATUS);
	}

	private void setContainerPositions(SpecimenDetail specimenDetail, Specimen specimen) {
		if (isBlank(specimenDetail.getContainerName()) && isBlank(specimenDetail.getPos1())
				&& isBlank(specimenDetail.getPos2())) {
			//			specimen.setSpecimenPosition(null);
			return;
		}
		StorageContainer container = daoFactory.getContainerDao().getContainer(specimenDetail.getContainerName());
		if (container == null) {
			addError(ScgErrorCode.INVALID_ATTR_VALUE, CONTAINER_NAME);
			return;
		}
		//TODO check for valid container positions
		//		ContainerUtil.checkAndAssignPositions(container,specimenDetail.getPos1(),specimenDetail.getPos2(),specimen);
	}

	private void setBiohazardCollection(SpecimenDetail specimenDetail, Specimen specimen) {
		if (specimenDetail.getBiohazardDetails() == null && specimenDetail.getBiohazardDetails().isEmpty()) {
			return;
		}
		for (BiohazardDetail detail : specimenDetail.getBiohazardDetails()) {
			//TODO yet to handle Biohazard
		}
	}

	private void setExternalIdsCollection(SpecimenDetail specimenDetail, Specimen specimen) {
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

	private void setParentSpecimen(SpecimenDetail specimenDetail, Specimen specimen) {
		if (specimenDetail.getParentSpecimenId() == null) {
			addError(ScgErrorCode.MISSING_ATTR_VALUE, PARENT);
			return;
		}
		Specimen parentSpecimen = daoFactory.getSpecimenDao().getSpecimen(specimenDetail.getParentSpecimenId());
		if (parentSpecimen == null) {
			addError(ScgErrorCode.INVALID_ATTR_VALUE, PARENT_SPECIMEN);
			return;
		}
		specimen.setParentSpecimen(parentSpecimen);
		specimen.setSpecimenCollectionGroup(parentSpecimen.getSpecimenCollectionGroup());
	}

	private void addError(CatissueErrorCode event, String field) {
		erroneousFields.add(new ErroneousField(event, field));
	}
}
