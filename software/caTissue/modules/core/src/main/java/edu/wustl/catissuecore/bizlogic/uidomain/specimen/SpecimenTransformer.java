package edu.wustl.catissuecore.bizlogic.uidomain.specimen;

import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import gov.nih.nci.logging.api.util.StringUtils;

public abstract class SpecimenTransformer<U extends SpecimenForm> extends AbstractSpecimenTransformer<U, Specimen> {

	/*
	 * checking for form type and creating appropriate Specimen subclass code
	 * belongs here.
	 */
	public final Specimen createDomainObject(U uiRepOfDomain) {
		String type = uiRepOfDomain.getClassName();
		Specimen specimen;
		if (Constants.TISSUE.equals(type)) {
			InstanceFactory<TissueSpecimen> instFact = DomainInstanceFactory.getInstanceFactory(TissueSpecimen.class);
			specimen = instFact.createObject();//new TissueSpecimen();
		} else if (Constants.FLUID.equals(type)) {
			InstanceFactory<FluidSpecimen> instFact = DomainInstanceFactory.getInstanceFactory(FluidSpecimen.class);
			specimen = instFact.createObject();// new FluidSpecimen();
		} else if (Constants.CELL.equals(type)) {
			InstanceFactory<CellSpecimen> instFact = DomainInstanceFactory.getInstanceFactory(CellSpecimen.class);
			specimen = instFact.createObject();//new CellSpecimen();
		} else if (Constants.MOLECULAR.equals(type)) {
			InstanceFactory<MolecularSpecimen> instFact = DomainInstanceFactory.getInstanceFactory(MolecularSpecimen.class);
			specimen = instFact.createObject();//new MolecularSpecimen();
		} else {
			throw new IllegalArgumentException("unknown specimen form type");
		}
		overwriteDomainObject(specimen, uiRepOfDomain);
		return specimen;
	}

	public void overwriteDomainObject(Specimen domainObject, U uiRepOfDomain) {
		super.overwriteDomainObject(domainObject, uiRepOfDomain);

		final String qty = uiRepOfDomain.getQuantity();

		if (qty != null && qty.trim().length() > 0) {
			domainObject.setInitialQuantity(new Double(uiRepOfDomain.getQuantity()));
		} else {
			domainObject.setInitialQuantity(new Double(0));
		}
		domainObject.setLabel(uiRepOfDomain.getLabel());
		if(!StringUtils.isBlank(uiRepOfDomain.getGlobalSpecimenIdentifer()))
		{
			domainObject.setGlobalSpecimenIdentifier(uiRepOfDomain.getGlobalSpecimenIdentifer());
		}

		if (uiRepOfDomain.isAddOperation()) {
			domainObject.setAvailableQuantity(new Double(domainObject.getInitialQuantity()));
		} else {
			domainObject.setAvailableQuantity(new Double(uiRepOfDomain.getAvailableQuantity()));
		}

		if (domainObject instanceof MolecularSpecimen) {
			if (Constants.DOUBLE_QUOTES.equals(uiRepOfDomain.getConcentration())) {
				// TODO
				// MolecularSpecimen.logger.debug("Concentration is " +
						// uiRepOfDomain.getConcentration());
			} else {
				((MolecularSpecimen) domainObject).setConcentrationInMicrogramPerMicroliter(new Double(uiRepOfDomain
						.getConcentration()));
			}
		}

		// check class name
		// catch (final Exception excp)
		// {
		// TissueSpecimen.logger.error(excp.getMessage(),excp);
		// excp.printStackTrace();
		// final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
		// throw new AssignDataException(errorKey, null, "TissueSpecimen.java
		// :");
		// }
	}

	/**
	 * This method will be called to set the specimen position.
	 *
	 * @param form
	 */
	protected void setSpecimenPosition(Specimen domainObject, U form) {
		if (domainObject.getSpecimenPosition() == null
				|| domainObject.getSpecimenPosition().getStorageContainer() == null) {
			InstanceFactory<SpecimenPosition> instFact = DomainInstanceFactory.getInstanceFactory(SpecimenPosition.class);
			domainObject.setSpecimenPosition(instFact.createObject());
			InstanceFactory<StorageContainer> scInstFact = DomainInstanceFactory.getInstanceFactory(StorageContainer.class);
			domainObject.getSpecimenPosition().setStorageContainer(scInstFact.createObject());
		}
		if (form.getStContSelection() == 1) {
			// domainObject.storageContainer = null;
			domainObject.setSpecimenPosition(null);
		}
		if (form.getStContSelection() == 2) {
			final long stContainerId = Long.parseLong(form.getStorageContainer());
			domainObject.getSpecimenPosition().getStorageContainer().setId(stContainerId);
			/*
			 * if (domainObject.specimenPosition == null) {
			 * domainObject.specimenPosition = new SpecimenPosition(); }
			 */
			domainObject.getSpecimenPosition().setPositionDimensionOne(Integer.valueOf(form.getPositionDimensionOne()));
			domainObject.getSpecimenPosition().setPositionDimensionTwo(Integer.valueOf(form.getPositionDimensionTwo()));
			domainObject.getSpecimenPosition().setSpecimen(domainObject);

			// domainObject.specimenPosition.storageContainer =
			// domainObject.storageContainer;
		} else if (form.getStContSelection() == 3) {
			domainObject.getSpecimenPosition().getStorageContainer().setName(form.getSelectedContainerName());
			if (form.getPos1() != null && !form.getPos1().trim().equals("") && form.getPos2() != null
					&& !form.getPos2().trim().equals("")) {
				/*
				 * if (domainObject.specimenPosition == null) {
				 * domainObject.specimenPosition = new SpecimenPosition();s}
				 */
				domainObject.getSpecimenPosition().setPositionDimensionOne(Integer.valueOf(form.getPos1()));
				domainObject.getSpecimenPosition().setPositionDimensionTwo(Integer.valueOf(form.getPos2()));
				domainObject.getSpecimenPosition().setSpecimen(domainObject);
				// domainObject.specimenPosition.storageContainer
				// = domainObject.storageContainer;
			}

		}
	}
}
