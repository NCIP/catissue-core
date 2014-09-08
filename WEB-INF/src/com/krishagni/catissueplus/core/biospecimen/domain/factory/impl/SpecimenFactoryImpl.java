
package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.CommonValidator.isBlank;
import static com.krishagni.catissueplus.core.common.CommonValidator.isValidPv;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Biohazard;
import com.krishagni.catissueplus.core.biospecimen.domain.ExternalIdentifier;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenCollectionGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ScgErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenFactory;
import com.krishagni.catissueplus.core.biospecimen.events.AliquotDetail;
import com.krishagni.catissueplus.core.biospecimen.events.BiohazardDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ExternalIdentifierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenPatchDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;

public class SpecimenFactoryImpl implements SpecimenFactory {

	private static final String SCG = "specimen collection group";

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

	private static final String SPECIMEN_AVAILABLE_QUANTITY = "Specimen available quantity ";

	private static final String ALIQUOT = "Aliquot";

	private static final String QTY_PER_ALIQUOT = "Quantity Per Aliquot";

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
		setComment(specimenDetail.getComment(), specimen);
		setCreatedOn(specimenDetail.getCreatedOn(), specimen, errorHandler);
		setContainerPositions(specimenDetail, specimen, errorHandler);
		setBiohazardCollection(specimenDetail, specimen, errorHandler);
		setExternalIdsCollection(specimenDetail, specimen, errorHandler);
		errorHandler.checkErrorAndThrow();
		return specimen;
	}

	@Override
	public Specimen patch(Specimen specimen, SpecimenPatchDetail detail) {
		ObjectCreationException exceptionHandler = new ObjectCreationException();
		if (detail.isTissueSiteModified()) {
			setTissueSite(detail.getTissueSite(), specimen, exceptionHandler);
		}
		if (detail.isTissueSideModified()) {
			setTissueSide(detail.getTissueSide(), specimen, exceptionHandler);
		}
		if (detail.isPathologicalStatusModified()) {
			setPathologyStatus(detail.getPathologicalStatus(), specimen, exceptionHandler);
		}
		if (detail.isActivityStatusModified()) {
			setActivityStatus(detail.getActivityStatus(), specimen, exceptionHandler);
		}
		if (detail.isCollectionStatusModified()) {
			setCollectionStatus(detail.getCollectionStatus(), specimen, exceptionHandler);
		}
		if (detail.isCreatedOnModified()) {
			setCreatedOn(detail.getCreatedOn(), specimen, exceptionHandler);
		}
		//			if (detail.isSpecimenClassModified()) {
		//				setLabel(String.valueOf(entry.getValue()), specimen, exceptionHandler);
		//			}
		//			if ("specimenType".equals(entry.getKey())) {
		//				setLabel(String.valueOf(entry.getValue()), specimen, exceptionHandler);
		//			}
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
		if(!"New".equals(specimenDetail.getLineage())){ 
		if (specimenDetail.getParentSpecimenId() == null) {
			errorHandler.addError(SpecimenErrorCode.MISSING_ATTR_VALUE, PARENT);
			return;
		}
		Specimen parentSpecimen = daoFactory.getSpecimenDao().getSpecimen(specimenDetail.getParentSpecimenId());
		if (parentSpecimen == null) {
			errorHandler.addError(ScgErrorCode.INVALID_ATTR_VALUE, PARENT_SPECIMEN);
			return;
		}
		specimen.setParentSpecimen(parentSpecimen);
		}
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

	private void setComment(String comment, Specimen specimen) {
		specimen.setComment(comment);
	}

	private void setCreatedOn(Date createdOn, Specimen specimen, ObjectCreationException errorHandler) {
		specimen.setCreatedOn(createdOn);
	}

	private void setCollectionStatus(String collectionStatus, Specimen specimen, ObjectCreationException errorHandler) {
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

	@Override
	public Set<Specimen> createAliquots(Specimen specimen, AliquotDetail aliquotDetail) {

		Double quantityPerAliquot = aliquotDetail.getQuantityPerAliquot();
		if (quantityPerAliquot < 0) {
			ObjectCreationException.raiseError(SpecimenErrorCode.INVALID_ATTR_VALUE, QTY_PER_ALIQUOT);
		}
		//If quantity per aliquot is not provided or 0 then divide available quantity in equal parts. 
		if (quantityPerAliquot == null || Double.compare(quantityPerAliquot, 0) == 0) {
			quantityPerAliquot = specimen.getAvailableQuantity() / aliquotDetail.getNoOfAliquots();
			aliquotDetail.setQuantityPerAliquot(quantityPerAliquot);
		}
		else {
			checkSpecimenAvailableQuantity(specimen, aliquotDetail);
		}
		Set<Specimen> aliquots = new HashSet<Specimen>();

		for (int i = 1; i <= aliquotDetail.getNoOfAliquots(); i++) {
			Specimen aliquot = createAliquot(specimen, aliquotDetail);
			aliquots.add(aliquot);
		}

		//Calculate remaining specimen quantity after aliquot creation.  
		Double availableQuantity = specimen.getAvailableQuantity()
				- (aliquotDetail.getQuantityPerAliquot() * aliquotDetail.getNoOfAliquots());
		specimen.setAvailableQuantity(availableQuantity);
		return aliquots;
	}

	private void checkSpecimenAvailableQuantity(Specimen specimen, AliquotDetail aliquotDetail) {
		Double specimenAvailableQty = specimen.getAvailableQuantity();
		Double requireQty = aliquotDetail.getQuantityPerAliquot() * aliquotDetail.getNoOfAliquots();
		if (requireQty > specimenAvailableQty) {
			ObjectCreationException.raiseError(SpecimenErrorCode.INSUFFICIENT_SPECIMEN_QTY, SPECIMEN_AVAILABLE_QUANTITY);
		}

	}

	public Specimen createAliquot(Specimen specimen, AliquotDetail aliquotDetail) {
		Specimen aliquot = new Specimen();

		aliquot.setTissueSite(specimen.getTissueSite());
		aliquot.setTissueSide(specimen.getTissueSide());
		aliquot.setPathologicalStatus(specimen.getPathologicalStatus());
		aliquot.setInitialQuantity(aliquotDetail.getQuantityPerAliquot());
		aliquot.setAvailableQuantity(aliquotDetail.getQuantityPerAliquot());
		aliquot.setLineage(ALIQUOT);
		aliquot.setParentSpecimen(specimen);
		aliquot.setSpecimenClass(specimen.getSpecimenClass());
		aliquot.setSpecimenType(specimen.getSpecimenType());
		aliquot.setConcentrationInMicrogramPerMicroliter(specimen.getConcentrationInMicrogramPerMicroliter());
		aliquot.setLabel(null);
		aliquot.setActivityStatus(specimen.getActivityStatus());
		aliquot.setIsAvailable(specimen.getIsAvailable());
		aliquot.setBarcode(null);
		aliquot.setComment(specimen.getComment());
		aliquot.setCreatedOn(new Date());
		aliquot.setCollectionStatus(Status.SPECIMEN_COLLECTION_STATUS_COLLECTED.getStatus());
		aliquot.setSpecimenCollectionGroup(specimen.getSpecimenCollectionGroup());
		aliquot.setBiohazardCollection(new HashSet<Biohazard>(specimen.getBiohazardCollection()));

		HashSet<ExternalIdentifier> externalIdentifierColl = new HashSet<ExternalIdentifier>();
		for (ExternalIdentifier externalIdentifier : specimen.getExternalIdentifierCollection()) {
			ExternalIdentifier newExtIndentifier = new ExternalIdentifier();
			newExtIndentifier.setName(externalIdentifier.getName());
			newExtIndentifier.setValue(externalIdentifier.getValue());
			newExtIndentifier.setSpecimen(aliquot);
			externalIdentifierColl.add(newExtIndentifier);
		}
		aliquot.setExternalIdentifierCollection(externalIdentifierColl);
		return aliquot;

	}
}
