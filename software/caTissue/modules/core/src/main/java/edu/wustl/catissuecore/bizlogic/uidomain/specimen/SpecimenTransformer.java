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

	

	/*public void setAllValues(IValueObject valueObject) throws AssignDataException
	{
		final AbstractActionForm abstractForm = (AbstractActionForm) valueObject;
		final String nullString = null; //for PMD error.
		try
		{
			if (abstractForm instanceof AliquotForm)
			{
				final AliquotForm form = (AliquotForm) abstractForm;
				// Dispose parent specimen Bug 3773
				this.setDisposeParentSpecimen(form.getDisposeParentSpecimen());
				new Validator();

				this.aliqoutMap = form.getAliquotMap();
				this.noOfAliquots = Integer.parseInt(form.getNoOfAliquots());
				this.parentSpecimen = new Specimen();
				this.collectionStatus = Constants.COLLECTION_STATUS_COLLECTED;
				this.lineage = Constants.ALIQUOT;

				*//**
				 * Patch ID: 3835_1_2
				 * See also: 1_1 to 1_5
				 * Description : Set createdOn date for aliquot.
				 *//*

				this.createdOn = CommonUtilities.parseDate(form.getCreatedDate(),
						CommonServiceLocator.getInstance().getDatePattern());

				if (!Validator.isEmpty(form.getSpecimenLabel()))
				{
					((Specimen) this.parentSpecimen).setLabel(form.getSpecimenLabel());
					this.parentSpecimen.setId(Long.valueOf(form.getSpecimenID()));
				}
				else if (!Validator.isEmpty(form.getBarcode()))
				{
					this.parentSpecimen.setId(Long.valueOf(form.getSpecimenID()));
					((Specimen) this.parentSpecimen).setBarcode(form.getBarcode());
				}
			}
			else
			{
				if (abstractForm instanceof NewSpecimenForm)
				{
					final NewSpecimenForm form = (NewSpecimenForm) abstractForm;
					if (!(form.getSpecimenCollectionGroupId() == null && form
							.getSpecimenCollectionGroupId().equals("")))
					{
						this.specimenCollectionGroup.id = Long.valueOf(form
								.getSpecimenCollectionGroupId());
					}
					if(form.getSpecimenCollectionGroupName()!=null && !form.getSpecimenCollectionGroupName().equals(""))
					{
						this.specimenCollectionGroup.name = form.getSpecimenCollectionGroupName();
					}
					*//**For Migration End**//*
					this.activityStatus = form.getActivityStatus();
					if (form.isAddOperation())
					{
						this.collectionStatus = Constants.COLLECTION_STATUS_COLLECTED;
					}
					else
					{
						this.collectionStatus = form.getCollectionStatus();
					}

					if (Validator.isEmpty(form.getBarcode()))
					{
						this.barcode = nullString;
					}
					else
					{
						this.barcode = form.getBarcode();
					}

					this.comment = form.getComments();
					this.specimenClass = form.getClassName();
					this.specimenType = form.getType();

					if (form.isAddOperation())
					{
						this.isAvailable = Boolean.TRUE;
					}
					else
					{
						this.isAvailable = Boolean.valueOf(form.isAvailable());
					}

					//in case of edit
					if (!form.isAddOperation())
					{
						//specimen is a new specimen
						if (this.parentSpecimen == null)
						{
							final String parentSpecimenId = form.getParentSpecimenId();
							// specimen created from another specimen
							if (parentSpecimenId != null && !parentSpecimenId.trim().equals("")
									&& Long.parseLong(parentSpecimenId) > 0)
							{
								this.isParentChanged = true;
							}
						}
						else
							//specimen created from another specimen
						{
							if (!((Specimen) this.parentSpecimen).getLabel().equalsIgnoreCase(
									form.getParentSpecimenName()))
							{
								this.isParentChanged = true;
							}
						}
						*//**
						 * Patch ID: 3835_1_3
						 * See also: 1_1 to 1_5
						 * Description : Set createdOn date in edit mode for new specimen
						 *//*
						this.createdOn = CommonUtilities.parseDate(form.getCreatedDate(),
								CommonServiceLocator.getInstance().getDatePattern());
					}

					logger.debug("isParentChanged " + this.isParentChanged);

					//Setting the SpecimenCharacteristics
					this.pathologicalStatus = form.getPathologicalStatus();
					this.specimenCharacteristics.tissueSide = form.getTissueSide();
					this.specimenCharacteristics.tissueSite = form.getTissueSite();

					//Getting the Map of External Identifiers
					final Map extMap = form.getExternalIdentifier();

					MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");

					final Collection extCollection = parser.generateData(extMap);
					this.externalIdentifierCollection = extCollection;

					Map bioMap = form.getBiohazard();
					logger.debug("PRE FIX MAP " + bioMap);
					bioMap = this.fixMap(bioMap);
					logger.debug("POST FIX MAP " + bioMap);

					//Getting the Map of Biohazards
					parser = new MapDataParser("edu.wustl.catissuecore.domain");
					final Collection bioCollection = parser.generateData(bioMap);

					logger.debug("BIO-COL : " + bioCollection);

					this.biohazardCollection = bioCollection;

					//Mandar : autoevents 14-july-06 start

					if (form.isAddOperation())
					{
						logger.debug("Setting Collection event in specimen domain object");
						//seting collection event values
						final CollectionEventParametersForm collectionEvent = new CollectionEventParametersForm();
						collectionEvent.setCollectionProcedure(form
								.getCollectionEventCollectionProcedure());
						collectionEvent.setComments(form.getCollectionEventComments());
						collectionEvent.setContainer(form.getCollectionEventContainer());
						collectionEvent.setTimeInHours(form.getCollectionEventTimeInHours());
						collectionEvent.setTimeInMinutes(form.getCollectionEventTimeInMinutes());
						collectionEvent.setDateOfEvent(form.getCollectionEventdateOfEvent());
						collectionEvent.setUserId(form.getCollectionEventUserId());
						collectionEvent.setOperation(form.getOperation());

						final CollectionEventParameters collectionEventParameters = new CollectionEventParameters();
						collectionEventParameters.setAllValues(collectionEvent);

						collectionEventParameters.setSpecimen(this);
						logger.debug("Before specimenEventCollection.size(): "
								+ this.specimenEventCollection.size());
						this.specimenEventCollection.add(collectionEventParameters);
						logger.debug("After specimenEventCollection.size(): "
								+ this.specimenEventCollection.size());

						logger.debug("...14-July-06... : CollectionEvent set");

						logger.debug("Setting Received event in specimen domain object");
						//setting received event values
						final ReceivedEventParametersForm receivedEvent = new ReceivedEventParametersForm();
						receivedEvent.setComments(form.getReceivedEventComments());
						receivedEvent.setDateOfEvent(form.getReceivedEventDateOfEvent());
						receivedEvent.setReceivedQuality(form.getReceivedEventReceivedQuality());
						receivedEvent.setUserId(form.getReceivedEventUserId());
						receivedEvent.setTimeInMinutes(form.getReceivedEventTimeInMinutes());
						receivedEvent.setTimeInHours(form.getReceivedEventTimeInHours());
						receivedEvent.setOperation(form.getOperation());

						final ReceivedEventParameters receivedEventParameters = new ReceivedEventParameters();
						receivedEventParameters.setAllValues(receivedEvent);
						receivedEventParameters.setSpecimen(this);

						*//**
						 * Patch ID: 3835_1_4
						 * See also: 1_1 to 1_5
						 * Description :createdOn should be collection event date
						 * for new specimen.
						 *//*
						this.createdOn = CommonUtilities.parseDate(form
								.getCollectionEventdateOfEvent(), CommonServiceLocator
								.getInstance().getDatePattern());

						logger.debug("Before specimenEventCollection.size(): "
								+ this.specimenEventCollection.size());
						this.specimenEventCollection.add(receivedEventParameters);
						logger.debug("After specimenEventCollection.size(): "
								+ this.specimenEventCollection.size());

						logger.debug("...14-July-06... : ReceivedEvent set");
					}

					if (form.isAddOperation())
					{
						this.setSpecimenPosition(form);
					}
					else
					{
						if (this.specimenPosition == null)
						{
							this.specimenPosition = new SpecimenPosition();
							this.specimenPosition.storageContainer = new StorageContainer();

							if (form.getStContSelection() == 1)
							{
								this.specimenPosition = null;
							}
							if (form.getStContSelection() == 2)
							{
								final long stContainerId = Long.parseLong(form
										.getStorageContainer());

								this.specimenPosition.storageContainer.setId(stContainerId);
								this.specimenPosition.positionDimensionOne = Integer.valueOf(form
										.getPositionDimensionOne());
								this.specimenPosition.positionDimensionTwo = Integer.valueOf(form
										.getPositionDimensionTwo());
								this.specimenPosition.specimen = this;
							}
							else if (form.getStContSelection() == 3)
							{

								if (form.getPos1() != null && !form.getPos1().trim().equals("")
										&& form.getPos2() != null
										&& !form.getPos2().trim().equals(""))
								{
									if (this.specimenPosition == null
											|| this.specimenPosition.storageContainer == null)
									{
										this.specimenPosition
										= new SpecimenPosition();
										this.specimenPosition.storageContainer
										= new StorageContainer();
									}
									this.specimenPosition.storageContainer.setName(form
											.getSelectedContainerName());
									this.specimenPosition.positionDimensionOne = Integer
									.valueOf(form.getPos1());
									this.specimenPosition.positionDimensionTwo = Integer
									.valueOf(form.getPos2());
									this.specimenPosition.specimen = this;
								}
								// bug 11479 S
								else
								{
									this.specimenPosition.storageContainer.setName(form
											.getSelectedContainerName());
								}
							}
							else
							{
								this.specimenPosition = null;
							}
						}
						else
						{
							this.specimenPosition.storageContainer.setName(form
									.getSelectedContainerName());
							this.specimenPosition.positionDimensionOne = Integer.valueOf(form
									.getPositionDimensionOne());
							this.specimenPosition.positionDimensionTwo = Integer.valueOf(form
									.getPositionDimensionTwo());
							this.specimenPosition.specimen = this;
						}
					}
					if (form.isParentPresent())
					{
						logger.info(Constants.DOUBLE_QUOTES);
						lazy change parent Specimen link is set to false
						 * so not required to set

						parentSpecimen = new CellSpecimen();
						parentSpecimen.setId(new Long(form.getParentSpecimenId()));
						parentSpecimen.setLabel(form.getParentSpecimenName());

					}
					else
					{
						this.parentSpecimen = null;
						//specimenCollectionGroup = null;
						this.specimenCollectionGroup.setId(Long.valueOf(form
								.getSpecimenCollectionGroupId()));
						//this.specimenCollectionGroup.setGroupName
						//(form.getSpecimenCollectionGroupName());
						lazy change
						IBizLogic iBizLogic
						   = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
						List scgList
						 = iBizLogic.retrieve(SpecimenCollectionGroup.class.getName(),
						  "name", form.getSpecimenCollectionGroupName());
						if (!scgList.isEmpty())
						{
							this.specimenCollectionGroup
							 = (SpecimenCollectionGroup) scgList.get(0);
						}
						if(parentSpecimen.getSpecimenCollectionGroup()!=null)
						{
							this.specimenCollectionGroup
							 = parentSpecimen.getSpecimenCollectionGroup();
						}
				}
				}
				else if (abstractForm instanceof CreateSpecimenForm)
				{
					final CreateSpecimenForm form = (CreateSpecimenForm) abstractForm;
					//bug no.4265
					this.setDisposeParentSpecimen(form.getDisposeParentSpecimen());
					if (this.getLineage() == null)
					{
						this.setLineage("Derived");
					}
					this.activityStatus = form.getActivityStatus();
					this.collectionStatus = Constants.COLLECTION_STATUS_COLLECTED;

					if (Validator.isEmpty(form.getBarcode()))
					{
						this.barcode = nullString;
					}
					else
					{
						this.barcode = form.getBarcode();
					}

					this.comment = form.getComments();
					//this.positionDimensionOne = new Integer(form.getPositionDimensionOne());
					//this.positionDimensionTwo = new Integer(form.getPositionDimensionTwo());
					this.specimenType = form.getType();
					this.specimenClass = form.getClassName();

					/**
						 * Patch ID: 3835_1_5
						 * See also: 1_1 to 1_5
						 * Description : Set createdOn date for derived specimen .

						this.createdOn = CommonUtilities.parseDate(form.getCreatedDate(),
								CommonServiceLocator.getInstance().getDatePattern());

						if (form.isAddOperation())
						{
							this.isAvailable = Boolean.TRUE;
						}
						else
						{
							this.isAvailable = Boolean.valueOf(form.isAvailable());
						}

						//this.storageContainer.setId(new Long(form.getStorageContainer()));
						this.parentSpecimen = new CellSpecimen();

						//this.parentSpecimen.setId(new Long(form.getParentSpecimenId()));
						((Specimen) this.parentSpecimen).setLabel(form.getParentSpecimenLabel());
						((Specimen) this.parentSpecimen).setBarcode(form.getParentSpecimenBarcode());
						//Getting the Map of External Identifiers
						final Map extMap = form.getExternalIdentifier();

						final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");

						final Collection extCollection = parser.generateData(extMap);
						this.externalIdentifierCollection = extCollection;

						//setting the value of storage container
						if (form.isAddOperation())
						{
							this.setSpecimenPosition(form);
						}
					}
				}
			}
			catch (final Exception excp)
			{
				Specimen.logger.error(excp.getMessage(), excp);
				final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
				throw new AssignDataException(errorKey, null, "Specimen.java :");

			}
			//Setting the consentTier responses. (Virender Mehta)
			if (abstractForm instanceof NewSpecimenForm)
			{
				final NewSpecimenForm form = (NewSpecimenForm) abstractForm;
				this.consentTierStatusCollection = this.prepareParticipantResponseCollection(form);
				// ----------- Mandar --16-Jan-07
				this.consentWithdrawalOption = form.getWithdrawlButtonStatus();
				// ----- Mandar : ---23-jan-07 For bug 3464.
				this.applyChangesTo = form.getApplyChangesTo();
			}
		}
	}*/

}
