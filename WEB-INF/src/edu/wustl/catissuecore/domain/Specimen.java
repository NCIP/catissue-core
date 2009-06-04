/**
 * <p>Title: Specimen Class>
 * <p>Description:  A single unit of tissue, body fluid, or derivative
 * biological macromolecule that is collected or created from a Participant </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.actionForm.CollectionEventParametersForm;
import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.actionForm.NewSpecimenForm;
import edu.wustl.catissuecore.actionForm.ReceivedEventParametersForm;
import edu.wustl.catissuecore.actionForm.SpecimenForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * A single unit of tissue, body fluid, or derivative biological macromolecule
 * that is collected or created from a Participant.
 * @hibernate.class table="CATISSUE_SPECIMEN"
 * @hibernate.discriminator column="SPECIMEN_CLASS"
 */
public class Specimen extends AbstractSpecimen implements Serializable, IActivityStatus
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(Specimen.class);

	/**
	 * specimenPosition.
	 */
	protected SpecimenPosition specimenPosition;

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = -905954650055370532L;

	/**
	 * A label name of this specimen.
	 */
	private String label;

	/**
	 * Defines whether this Specimen record can be queried (Active).
	 * or not queried (Inactive) by any actor.
	 */
	private String activityStatus;

	/**
	 * Is this specimen still physically available in the tissue bank?
	 */
	private Boolean isAvailable;

	/**
	 * Barcode assigned to the specimen.
	 */
	protected String barcode;
	/**
	 * Comment on specimen.
	 */
	private String comment;

	/**
	 * Comment on specimen.
	 */
	private Date createdOn;

	/**
	 * The available quantity of a specimen.
	 */
	private Double availableQuantity;
	/**
	 * The status of Specimen.
	 */
	private String collectionStatus;
	/**
	 * Collection of attributes of a Specimen that renders it potentially harmful to a User.
	 */
	private Collection<Biohazard> biohazardCollection = new HashSet<Biohazard>();
	/**
	 * Specimen Position i.e position dimention one and  position dimention two
	 */
	//protected SpecimenPosition specimenPosition;
	/**
	 * Collection of a pre-existing, externally defined id associated with a specimen.
	 */
	private Collection<ExternalIdentifier> externalIdentifierCollection = new HashSet<ExternalIdentifier>();

	/**
	 * An event that results in the collection of one or more specimen from a participant.
	 */
	private SpecimenCollectionGroup specimenCollectionGroup;

	/**
	 * specimenRequirement.
	 */
	private SpecimenRequirement specimenRequirement;
	/**
	 * Parent is changed or not.
	 */
	protected transient boolean isParentChanged = false;

	/**
	 * Total number of aliquot Specimen.
	 */
	private transient int noOfAliquots;

	/**
	 * map of aliquot specimen.
	 */
	private transient Map aliqoutMap = new HashMap();

	/**
	 * After creating aliquot specimen, dispose parent specimen or not.
	 */
	private transient boolean disposeParentSpecimen = false;

	/**
	 * The consent tier status for multiple participants for a particular specimen.
	 */
	private Collection<ConsentTierStatus> consentTierStatusCollection;

	/**
	 * To perform operation based on withdraw button clicked.
	 * Default No Action to allow normal flow.
	 */
	private String consentWithdrawalOption = Constants.WITHDRAW_RESPONSE_NOACTION;

	/**
	 * To apply changes to child specimen based on consent status changes.
	 * Default Apply none to allow normal flow.
	 */
	private String applyChangesTo = Constants.APPLY_NONE;

	/**
	 * @return the consentTierStatusCollection
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.
	 * ConsentTierStatus" lazy="true" cascade="save-update"
	 * @hibernate.set name="consentTierStatusCollection" table="CATISSUE_CONSENT_TIER_STATUS"
	 * @hibernate.collection-key column="SPECIMEN_ID"
	 */
	public Collection<ConsentTierStatus> getConsentTierStatusCollection()
	{
		return consentTierStatusCollection;
	}

	/**
	 * @param consentTierStatusCollection the consentTierStatusCollection to set
	 */
	public void setConsentTierStatusCollection(Collection<ConsentTierStatus> consentTierStatusCollection)
	{
		this.consentTierStatusCollection = consentTierStatusCollection;
	}

	/**
	 * Default Specimen.
	 */
	public Specimen()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form AbstractActionForm
	 * @throws AssignDataException AssignDataException.
	 */
	public Specimen(AbstractActionForm form) throws AssignDataException
	{
		super();
		setAllValues(form);
	}

	/**
	 * Returns true if this specimen still physically available
	 * in the tissue bank else returns false.
	 * @hibernate.property name="available" type="boolean" column="AVAILABLE"
	 * @return true if this specimen still physically available
	 * in the tissue bank else returns false.
	 * @see #setAvailable(Boolean)
	 */
	public Boolean getIsAvailable()
	{
		return isAvailable;
	}

	/**
	 * Sets true if this specimen still physically available
	 * in the tissue bank else returns false.
	 * @param isAvailable true if this specimen still physically available else false.
	 * @see #getAvailable()
	 */

	public void setIsAvailable(Boolean isAvailable)
	{
		this.isAvailable = isAvailable;
	}

	/**
	 * Returns the barcode assigned to the specimen.
	 * @hibernate.property name="barcode" type="string" column="BARCODE" length="255" unique="true"
	 * @return the barcode assigned to the specimen.
	 * @see #setBarcode(String)
	 */
	public String getBarcode()
	{
		return barcode;
	}

	/**
	 * Sets the barcode assigned to the specimen.
	 * @param barcode - the barcode assigned to the specimen.
	 * @see #getBarcode()
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
		String nullString = null;	//for PMD error.
		if (Constants.DOUBLE_QUOTES.equals(barcode))
		{
			this.barcode = nullString;
		}
	}

	/**
	 * Returns the comments on the specimen.
	 * @hibernate.property name="comment" type="string" column="COMMENTS" length="2000"
	 * @return the comments on the specimen.
	 * @see #setComment(String)
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * Sets the comment on the specimen.
	 * @param comment - The comments to set.
	 * @see #getComment()
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * Returns whether this Specimen record can be queried (Active) or not queried (Inactive) by any actor.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @return "Active" if this Specimen record can be queried or "Inactive" if cannot be queried.
	 * @see #setActivityStatus(String)
	 */
	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	 * Sets whether this Specimen record can be queried (Active) or not queried (Inactive) by any actor.
	 * @param activityStatus "Active" if this Specimen record can be queried else "Inactive".
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Returns the collection of attributes of a Specimen
	 * that renders it potentially harmful to a User.
	 * @hibernate.set name="biohazardCollection" table="CATISSUE_SPECIMEN_BIOHZ_REL"
	 * cascade="none" inverse="false" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.Biohazard" column="BIOHAZARD_ID"
	 * @return the collection of attributes of a Specimen
	 * that renders it potentially harmful to a User.
	 * @see #setBiohazardCollection(Set)
	 */
	public Collection<Biohazard> getBiohazardCollection()
	{
		return biohazardCollection;
	}

	/**
	 * Sets the collection of attributes of a Specimen
	 * that renders it potentially harmful to a User.
	 * @param biohazardCollection the collection of attributes of a Specimen
	 * that renders it potentially harmful to a User.
	 * @see #getBiohazardCollection()
	 */
	public void setBiohazardCollection(Collection<Biohazard> biohazardCollection)
	{
		this.biohazardCollection = biohazardCollection;
	}

	/**
	 * Returns the physically discreet container that is used to store a specimen  e.g. Box, Freezer etc.
	 * @hibernate.many-to-one column="STORAGE_CONTAINER_IDENTIFIER"
	 * class="edu.wustl.catissuecore.domain.StorageContainer" constrained="true"
	 * @return the physically discreet container that is used to store a specimen  e.g. Box, Freezer etc.
	 * @see #setStorageContainer(StorageContainer)
	 */
	//	public StorageContainer getStorageContainer()
	//	{
	//		return storageContainer;
	//	}
	//
	//	/**
	//	 * Sets the physically discreet container that is used to store a specimen  e.g. Box, Freezer etc.
	//	 * @param storageContainer the physically discreet container that is used to store a specimen
	//	 * e.g. Box, Freezer etc.
	//	 * @see #getStorageContainer()
	//	 */
	//	public void setStorageContainer(StorageContainer storageContainer)
	//	{
	//		this.storageContainer = storageContainer;
	//	}
	/**
	 * Returns the collection of a pre-existing, externally defined id associated with a specimen.
	 * @hibernate.set name="externalIdentifierCollection" table="CATISSUE_EXTERNAL_IDENTIFIER"
	 * cascade="save-update" inverse="true" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ExternalIdentifier"
	 * @return the collection of a pre-existing, externally defined id associated with a specimen.
	 * @see #setExternalIdentifierCollection(Set)
	 */
	public Collection<ExternalIdentifier> getExternalIdentifierCollection()
	{
		return externalIdentifierCollection;
	}

	/**
	 * Sets the collection of a pre-existing, externally defined id
	 * associated with a specimen.
	 * @param externalIdentifierCollection the collection of a pre-existing,
	 * externally defined id associated with a specimen.
	 * @see #getExternalIdentifierCollection()
	 */
	public void setExternalIdentifierCollection(Collection externalIdentifierCollection)
	{
		this.externalIdentifierCollection = externalIdentifierCollection;
	}

	/**
	 * Returns the event that results in the collection of one or more specimen from a participant.
	 * @hibernate.many-to-one column="SPECIMEN_COLLECTION_GROUP_ID"
	 * class="edu.wustl.catissuecore.domain.SpecimenCollectionGroup" constrained="true"
	 * @return the event that results in the collection of one or more specimen from a participant.
	 * @see #setSpecimenCollectionGroup(SpecimenCollectionGroup)
	 */
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return specimenCollectionGroup;
	}

	/**
	 * Sets the event that results in the collection of one or more specimen from a participant.
	 * @param specimenCollectionGroup the event that results in the collection of one or more
	 * specimen from a participant.
	 * @see #getSpecimenCollectionGroup()
	 */
	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

	/**
	 * @return isParentChanged boolean true or false
	 */
	public boolean isParentChanged()
	{
		return isParentChanged;
	}

	/**
	 * @param isParentChanged boolean true or false
	 */
	public void setParentChanged(boolean isParentChanged)
	{
		this.isParentChanged = isParentChanged;
	}

	/**
	 * Returns the label name of specimen.
	 * @hibernate.property name="label" type="string"
	 * column="LABEL" length="255"
	 * @return the label name of specimen.
	 * @see #setLabel(String)
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * Sets the label name of specimen.
	 * @param label The label name of specimen.
	 * @see #getLabel()
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * This function Copies the data from an NewSpecimenForm object to a Specimen object.
	 * @param valueObject - A formbean object containing the information about the Specimen.
	 * @throws AssignDataException AssignDataException.
	 */
	public void setAllValues(IValueObject valueObject) throws AssignDataException
	{
		AbstractActionForm abstractForm = (AbstractActionForm) valueObject;
		String nullString = null;	//for PMD error.
		if (SearchUtil.isNullobject(specimenPosition))
		{
			specimenPosition = null;
		}

		if (SearchUtil.isNullobject(specimenCollectionGroup))
		{
			specimenCollectionGroup = new SpecimenCollectionGroup();
		}

		if (SearchUtil.isNullobject(specimenCharacteristics))
		{
			specimenCharacteristics = new SpecimenCharacteristics();
		}

		if (SearchUtil.isNullobject(initialQuantity))
		{
			initialQuantity = new Double(0);
		}

		if (SearchUtil.isNullobject(availableQuantity))
		{
			availableQuantity = new Double(0);
		}
		try
		{
			if (abstractForm instanceof AliquotForm)
			{
				AliquotForm form = (AliquotForm) abstractForm;
				// Dispose parent specimen Bug 3773
				this.setDisposeParentSpecimen(form.getDisposeParentSpecimen());
				Validator validator = new Validator();

				this.aliqoutMap = form.getAliquotMap();
				this.noOfAliquots = Integer.parseInt(form.getNoOfAliquots());
				this.parentSpecimen = new Specimen();
				this.collectionStatus = Constants.COLLECTION_STATUS_COLLECTED;
				this.lineage = Constants.ALIQUOT;

				/**
				 * Patch ID: 3835_1_2
				 * See also: 1_1 to 1_5
				 * Description : Set createdOn date for aliquot.
				 */

				this.createdOn = Utility.parseDate(form.getCreatedDate(),
						CommonServiceLocator.getInstance().getDatePattern());

				if (!validator.isEmpty(form.getSpecimenLabel()))
				{
					((Specimen) parentSpecimen).setLabel(form.getSpecimenLabel());
					parentSpecimen.setId(Long.valueOf(form.getSpecimenID()));
				}
				else if (!validator.isEmpty(form.getBarcode()))
				{
					parentSpecimen.setId(Long.valueOf(form.getSpecimenID()));
					((Specimen) this.parentSpecimen).setBarcode(form.getBarcode());
				}
			}
			else
			{
				String qty = ((SpecimenForm) abstractForm).getQuantity();
				if (qty != null && qty.trim().length() > 0)
				{
					this.initialQuantity = new Double(((SpecimenForm)abstractForm).
							getQuantity());
				}
				else
				{
					this.initialQuantity = new Double(0);
				}
				this.label = ((SpecimenForm) abstractForm).getLabel();

				if (abstractForm.isAddOperation())
				{
					this.availableQuantity = new Double(this.initialQuantity);
				}
				else
				{
					this.availableQuantity = new Double(((SpecimenForm) abstractForm).
							getAvailableQuantity());
				}

				Validator validator = new Validator();
				if (abstractForm instanceof NewSpecimenForm)
				{
					NewSpecimenForm form = (NewSpecimenForm) abstractForm;
					if (!(form.getSpecimenCollectionGroupId() == null &&
							form.getSpecimenCollectionGroupId().equals("")))
					{
						this.specimenCollectionGroup.id = Long.valueOf(form.
								getSpecimenCollectionGroupId());
					}

					if (!(form.getSpecimenCollectionGroupName() == null &&
							form.getSelectedContainerName().equals("")))
					{
						this.specimenCollectionGroup.name =form.getSelectedContainerName();
					}
					/**For Migration End**/
					this.activityStatus = form.getActivityStatus();
					if (form.isAddOperation())
					{
						this.collectionStatus = Constants.COLLECTION_STATUS_COLLECTED;
					}
					else
					{
						this.collectionStatus = form.getCollectionStatus();
					}

					if (validator.isEmpty(form.getBarcode()))
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
						if (parentSpecimen == null)
						{
							String parentSpecimenId = form.getParentSpecimenId();
							// specimen created from another specimen
							if (parentSpecimenId != null && !parentSpecimenId.trim().
								equals("") && Long.parseLong(parentSpecimenId) > 0)
							{
								isParentChanged = true;
							}
						}
						else
						//specimen created from another specimen
						{
							if (!((Specimen) parentSpecimen).getLabel().
								equalsIgnoreCase(form.getParentSpecimenName()))
							{
								isParentChanged = true;
							}
						}
						/**
						 * Patch ID: 3835_1_3
						 * See also: 1_1 to 1_5
						 * Description : Set createdOn date in edit mode for new specimen
						 */
						this.createdOn = Utility.parseDate(form.getCreatedDate(),
							CommonServiceLocator.getInstance().getDatePattern());
					}

					logger.debug("isParentChanged " + isParentChanged);

					//Setting the SpecimenCharacteristics
					this.pathologicalStatus = form.getPathologicalStatus();
					specimenCharacteristics.tissueSide = form.getTissueSide();
					specimenCharacteristics.tissueSite = form.getTissueSite();

					//Getting the Map of External Identifiers
					Map extMap = form.getExternalIdentifier();

					MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");

					Collection extCollection = parser.generateData(extMap);
					this.externalIdentifierCollection = extCollection;

					Map bioMap = form.getBiohazard();
					logger.debug("PRE FIX MAP " + bioMap);
					bioMap = fixMap(bioMap);
					logger.debug("POST FIX MAP " + bioMap);

					//Getting the Map of Biohazards
					parser = new MapDataParser("edu.wustl.catissuecore.domain");
					Collection bioCollection = parser.generateData(bioMap);

					logger.debug("BIO-COL : " + bioCollection);

					this.biohazardCollection = bioCollection;

					//Mandar : autoevents 14-july-06 start

					if (form.isAddOperation())
					{
						logger.debug("Setting Collection event in specimen domain object");
						//seting collection event values
						CollectionEventParametersForm collectionEvent =
							new CollectionEventParametersForm();
						collectionEvent.setCollectionProcedure(
								form.getCollectionEventCollectionProcedure());
						collectionEvent.setComments(form.getCollectionEventComments());
						collectionEvent.setContainer(form.getCollectionEventContainer());
						collectionEvent.setTimeInHours(
								form.getCollectionEventTimeInHours());
						collectionEvent.setTimeInMinutes(
								form.getCollectionEventTimeInMinutes());
						collectionEvent.setDateOfEvent(
								form.getCollectionEventdateOfEvent());
						collectionEvent.setUserId(form.getCollectionEventUserId());
						collectionEvent.setOperation(form.getOperation());

						CollectionEventParameters collectionEventParameters =
							new CollectionEventParameters();
						collectionEventParameters.setAllValues(collectionEvent);

						collectionEventParameters.setSpecimen(this);
						logger.debug("Before specimenEventCollection.size(): " +
								specimenEventCollection.size());
						specimenEventCollection.add(collectionEventParameters);
						logger.debug("After specimenEventCollection.size(): " +
								specimenEventCollection.size());

						logger.debug("...14-July-06... : CollectionEvent set");

						logger.debug("Setting Received event in specimen domain object");
						//setting received event values
						ReceivedEventParametersForm receivedEvent =
							new ReceivedEventParametersForm();
						receivedEvent.setComments(form.getReceivedEventComments());
						receivedEvent.setDateOfEvent(form.getReceivedEventDateOfEvent());
						receivedEvent.setReceivedQuality(
								form.getReceivedEventReceivedQuality());
						receivedEvent.setUserId(form.getReceivedEventUserId());
						receivedEvent.setTimeInMinutes(
								form.getReceivedEventTimeInMinutes());
						receivedEvent.setTimeInHours(form.getReceivedEventTimeInHours());
						receivedEvent.setOperation(form.getOperation());

						ReceivedEventParameters receivedEventParameters =
							new ReceivedEventParameters();
						receivedEventParameters.setAllValues(receivedEvent);
						receivedEventParameters.setSpecimen(this);

						/**
						 * Patch ID: 3835_1_4
						 * See also: 1_1 to 1_5
						 * Description :createdOn should be collection event date
						 * for new specimen.
						 */
						this.createdOn = Utility.parseDate(form.getCollectionEventdateOfEvent(),
								CommonServiceLocator.getInstance().getDatePattern());

						logger.debug("Before specimenEventCollection.size(): " + specimenEventCollection.size());
						specimenEventCollection.add(receivedEventParameters);
						logger.debug("After specimenEventCollection.size(): " + specimenEventCollection.size());

						logger.debug("...14-July-06... : ReceivedEvent set");
					}

					if (form.isAddOperation())
					{
						if (this.specimenPosition == null ||
								this.specimenPosition.storageContainer == null)
						{
							this.specimenPosition = new SpecimenPosition();
							this.specimenPosition.storageContainer =
								new StorageContainer();
						}
						if (form.getStContSelection() == 1)
						{
							//	this.storageContainer = null;
							this.specimenPosition = null;
							this.getSpecimenRequirement().setStorageType(Constants.STORAGE_TYPE_POSITION_VIRTUAL);//bug 12662
						}
						if (form.getStContSelection() == 2)
						{
							long stContainerId = Long.parseLong(
									form.getStorageContainer());
							this.specimenPosition.storageContainer.setId(
									stContainerId);
							if (this.specimenPosition == null)
							{
								this.specimenPosition = new SpecimenPosition();
							}
							this.specimenPosition.positionDimensionOne =
								Integer.valueOf(form.getPositionDimensionOne());
							this.specimenPosition.positionDimensionTwo =
								Integer.valueOf(form.getPositionDimensionTwo());
							this.specimenPosition.specimen = this;

							//this.specimenPosition.storageContainer = this.storageContainer;
						}
						else if (form.getStContSelection() == 3)
						{
							this.specimenPosition.storageContainer.setName(form.getSelectedContainerName());
							if (form.getPos1() != null && !form.getPos1().trim().equals("") && form.getPos2() != null
									&& !form.getPos2().trim().equals(""))
							{
								if (this.specimenPosition == null)
								{
									this.specimenPosition = new SpecimenPosition();
								}
								this.specimenPosition.positionDimensionOne = Integer.valueOf(form.getPos1());
								this.specimenPosition.positionDimensionTwo = Integer.valueOf(form.getPos2());
								this.specimenPosition.specimen = this;
								//this.specimenPosition.storageContainer = this.storageContainer;
							}

						}
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
								this.getSpecimenRequirement().setStorageType(Constants.STORAGE_TYPE_POSITION_VIRTUAL);//bug 12662
							}
							if (form.getStContSelection() == 2)
							{
								long stContainerId = Long.parseLong(form.getStorageContainer());
								/*	if (this.specimenPosition == null || this.specimenPosition.storageContainer == null)
								{
									this.specimenPosition = new SpecimenPosition();
									this.specimenPosition.storageContainer = new StorageContainer();
								}*/
								this.specimenPosition.storageContainer.setId(stContainerId);
								this.specimenPosition.positionDimensionOne = Integer.valueOf(form.getPositionDimensionOne());
								this.specimenPosition.positionDimensionTwo = Integer.valueOf(form.getPositionDimensionTwo());
								this.specimenPosition.specimen = this;
							}
							else if (form.getStContSelection() == 3)
							{

								if (form.getPos1() != null && !form.getPos1().trim().equals("") && form.getPos2() != null
										&& !form.getPos2().trim().equals(""))
								{
									/*if (this.specimenPosition == null || this.specimenPosition.storageContainer == null)
									{
										this.specimenPosition = new SpecimenPosition();
										this.specimenPosition.storageContainer = new StorageContainer();
									}*/
									this.specimenPosition.storageContainer.setName(form.getSelectedContainerName());
									this.specimenPosition.positionDimensionOne = Integer.valueOf(form.getPos1());
									this.specimenPosition.positionDimensionTwo = Integer.valueOf(form.getPos2());
									this.specimenPosition.specimen = this;
								}
								// bug 11479 S
								else
								{
									this.specimenPosition.storageContainer.setName(form.getSelectedContainerName());
								}
							}
							else
							{
								this.specimenPosition = null;
							}
						}
						else
						{
							this.specimenPosition.storageContainer.setName(form.getSelectedContainerName());
							this.specimenPosition.positionDimensionOne = Integer.valueOf(form.getPositionDimensionOne());
							this.specimenPosition.positionDimensionTwo = Integer.valueOf(form.getPositionDimensionTwo());
							this.specimenPosition.specimen = this;
						}
					}
					if (form.isParentPresent())
					{
						logger.info(Constants.DOUBLE_QUOTES);
						/*lazy change parent Specimen link is set to false
						 * so not required to set */
						/*
						parentSpecimen = new CellSpecimen();
						parentSpecimen.setId(new Long(form.getParentSpecimenId()));
						parentSpecimen.setLabel(form.getParentSpecimenName());
						*/
					}
					else
					{
						parentSpecimen = null;
						//specimenCollectionGroup = null;
						this.specimenCollectionGroup.setId(Long.valueOf(form.getSpecimenCollectionGroupId()));
						//this.specimenCollectionGroup.setGroupName(form.getSpecimenCollectionGroupName());
						/*lazy change */
						/*IBizLogic iBizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
						List scgList = iBizLogic.retrieve(SpecimenCollectionGroup.class.getName(), "name", form.getSpecimenCollectionGroupName());
						if (!scgList.isEmpty())
						{
							this.specimenCollectionGroup = (SpecimenCollectionGroup) scgList.get(0);
						}
						if(parentSpecimen.getSpecimenCollectionGroup()!=null)
						{
							this.specimenCollectionGroup = parentSpecimen.getSpecimenCollectionGroup();
						}*/
					}
				}
				else if (abstractForm instanceof CreateSpecimenForm)
				{
					CreateSpecimenForm form = (CreateSpecimenForm) abstractForm;
					//bug no.4265
					this.setDisposeParentSpecimen(form.getDisposeParentSpecimen());
					if (this.getLineage() == null)
					{
						this.setLineage("Derived");
					}
					this.activityStatus = form.getActivityStatus();
					this.collectionStatus = Constants.COLLECTION_STATUS_COLLECTED;

					if (validator.isEmpty(form.getBarcode()))
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
					 */
					this.createdOn = Utility.parseDate(form.getCreatedDate(), CommonServiceLocator.getInstance().getDatePattern());

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
					Map extMap = form.getExternalIdentifier();

					MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");

					Collection extCollection = parser.generateData(extMap);
					this.externalIdentifierCollection = extCollection;

					//setting the value of storage container
					if (form.isAddOperation())
					{
						if (this.specimenPosition == null || this.specimenPosition.storageContainer == null)
						{
							this.specimenPosition = new SpecimenPosition();
							this.specimenPosition.storageContainer = new StorageContainer();
						}
						if (form.getStContSelection() == 1)
						{
							//	this.storageContainer = null;
							this.specimenPosition = null;
						}
						if (form.getStContSelection() == 2)
						{
							this.specimenPosition.storageContainer.setId(Long.valueOf(form.getStorageContainer()));
							this.specimenPosition.positionDimensionOne = Integer.valueOf(form.getPositionDimensionOne());
							this.specimenPosition.positionDimensionTwo = Integer.valueOf(form.getPositionDimensionTwo());
							this.specimenPosition.specimen = this;
							//			this.specimenPosition.storageContainer = this.storageContainer;
						}
						else if (form.getStContSelection() == 3)
						{
							this.specimenPosition.storageContainer.setName(form.getSelectedContainerName());
							if (form.getPos1() != null && !form.getPos1().trim().equals("") && form.getPos2() != null
									&& !form.getPos2().trim().equals(""))
							{
								this.specimenPosition.positionDimensionOne = Integer.valueOf(form.getPos1());
								this.specimenPosition.positionDimensionTwo = Integer.valueOf(form.getPos2());
								this.specimenPosition.specimen = this;
								//				this.specimenPosition.storageContainer = this.storageContainer;
							}
						}
					}
				}
			}
		}
		catch (Exception excp)
		{
			Logger.out.error(excp.getMessage(), excp);
			ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
			throw new AssignDataException(errorKey,null ,"Specimen.java :");

		}
		//Setting the consentTier responses. (Virender Mehta)
		if (abstractForm instanceof NewSpecimenForm)
		{
			NewSpecimenForm form = (NewSpecimenForm) abstractForm;
			this.consentTierStatusCollection = prepareParticipantResponseCollection(form);
			// ----------- Mandar --16-Jan-07
			this.consentWithdrawalOption = form.getWithdrawlButtonStatus();
			// ----- Mandar : ---23-jan-07 For bug 3464.
			this.applyChangesTo = form.getApplyChangesTo();
		}
	}

	/**
	 * For Consent Tracking.
	 * Setting the Domain Object.
	 * @param  form CollectionProtocolRegistrationForm.
	 * @return consentResponseColl.
	 */
	private Collection prepareParticipantResponseCollection(NewSpecimenForm form)
	{
		MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
		Collection beanObjColl = null;
		try
		{
			beanObjColl = mapdataParser.generateData(form.getConsentResponseForSpecimenValues());
		}
		catch (Exception e)
		{
			logger.error(e.getStackTrace());
			e.printStackTrace();
		}
		Iterator iter = beanObjColl.iterator();
		Collection consentResponseColl = new HashSet();
		while (iter.hasNext())
		{
			ConsentBean consentBean = (ConsentBean) iter.next();
			ConsentTierStatus consentTierstatus = new ConsentTierStatus();
			//Setting response
			consentTierstatus.setStatus(consentBean.getSpecimenLevelResponse());
			if (consentBean.getSpecimenLevelResponseID() != null &&
					consentBean.getSpecimenLevelResponseID().trim().length() > 0)
			{
				consentTierstatus.setId(Long.parseLong(consentBean.getSpecimenLevelResponseID()));
			}
			//Setting consent tier
			ConsentTier consentTier = new ConsentTier();
			consentTier.setId(Long.valueOf(consentBean.getConsentTierID()));
			consentTierstatus.setConsentTier(consentTier);
			consentResponseColl.add(consentTierstatus);
		}
		return consentResponseColl;
	}

	/**
	 * fixMap.
	 * @param orgMap Map.
	 * @return Map.
	 */
	protected Map fixMap(Map orgMap)
	{
		Map newMap = new HashMap();
		Iterator iterator = orgMap.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String) iterator.next();
			//Logger.out.debug("key "+key);

			if (key.indexOf("persisted") == -1)
			{
				String value = String.valueOf(orgMap.get(key));
				newMap.put(key, value);
			}
		}
		return newMap;
	}

	/**
	 * getObjectId.
	 * @return String.
	 */
	public String getObjectId()
	{
		logger.debug(this.getClass().getName() + " is an instance of Specimen class");
		return Specimen.class.getName() + "_" + this.getId();
	}

	/**
	 * Returns the available quantity of a specimen.
	 * @return The available quantity of a specimen.
	 * @see #setAvailableQuantity(Quantity)
	 */
	public Double getAvailableQuantity()
	{
		return availableQuantity;
	}

	/**
	 * Sets the available quantity of a specimen.
	 * @param availableQuantity the available quantity of a specimen.
	 * @see #getAvailableQuantity()
	 */
	public void setAvailableQuantity(Double availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}

	/**
	 * Returns the quantity of a specimen.
	 * @return The quantity of a specimen.
	 * @see #setinitialQuantity(Quantity)
	 */
	public Double getInitialQuantity()
	{
		return initialQuantity;
	}

	/**
	 * Sets the quantity of a specimen.
	 * @param initialQuantity - The quantity of a specimen.
	 * @see #getInitialQuantity()
	 */
	public void setInitialQuantity(Double initialQuantity)
	{
		this.initialQuantity = initialQuantity;
	}

	/**
	 * Returns the Histoathological character of specimen.
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 * @hibernate.property name="pathologicalStatus" type="string"
	 * column="PATHOLOGICAL_STATUS" length="50"
	 * @return the Histoathological character of specimen.
	 * @see #setPathologicalStatus(String)
	 */
	public String getPathologicalStatus()
	{
		return pathologicalStatus;
	}

	/**
	 * Sets the Histoathological character of specimen.
	 * e.g. Non-Malignant, Malignant, Non-Malignant Diseased, Pre-Malignant.
	 * @param pathologicalStatus the Histoathological character of specimen.
	 * @see #getPathologicalStatus()
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/**
	 * Returns the map that contains distinguished fields per aliquots.
	 * @return The map that contains distinguished fields per aliquots.
	 * @see #setAliquotMap(Map)
	 */
	public Map getAliqoutMap()
	{
		return aliqoutMap;
	}

	/**
	 * Sets the map of distinguished fields of aliquots.
	 * @param aliqoutMap - A map of distinguished fields of aliquots.
	 * @see #getAliquotMap()
	 */
	public void setAliqoutMap(Map aliqoutMap)
	{
		this.aliqoutMap = aliqoutMap;
	}

	/**
	 * Returns the no. of aliquots to be created.
	 * @return The no. of aliquots to be created.
	 * @see #setNoOfAliquots(int)
	 */
	public int getNoOfAliquots()
	{
		return noOfAliquots;
	}

	/**
	 * Sets the no. of aliquots to be created.
	 * @param noOfAliquots The no. of aliquots to be created.
	 * @see #getNoOfAliquots()
	 */
	public void setNoOfAliquots(int noOfAliquots)
	{
		this.noOfAliquots = noOfAliquots;
	}

	/**
	 * Returns the historical information about the specimen.
	 * @hibernate.property name="lineage" type="string"
	 * column="LINEAGE" length="50"
	 * @return The historical information about the specimen.
	 * @see #setLineage(String)
	 */
	public String getLineage()
	{
		return lineage;
	}

	/**
	 * Sets the historical information about the specimen.
	 * @param lineage The historical information about the specimen.
	 * @see #getLineage()
	 */
	public void setLineage(String lineage)
	{
		this.lineage = lineage;
	}

	/**
	 * Returns message label to display on success add or edit.
	 * @return String
	 */
	public String getMessageLabel()
	{
		return this.label;
	}

	/**
	 * Get ConsentWithdrawalOption.
	 * @return String.
	 */
	public String getConsentWithdrawalOption()
	{
		return consentWithdrawalOption;
	}

	/**
	 * Set ConsentWithdrawalOption.
	 * @param consentWithdrawalOption String.
	 */
	public void setConsentWithdrawalOption(String consentWithdrawalOption)
	{
		this.consentWithdrawalOption = consentWithdrawalOption;
	}

	/**
	 * Get ApplyChangesTo.
	 * @return String.
	 */
	public String getApplyChangesTo()
	{
		return applyChangesTo;
	}

	/**
	 * Set ApplyChangesTo.
	 * @param applyChangesTo String.
	 */
	public void setApplyChangesTo(String applyChangesTo)
	{
		this.applyChangesTo = applyChangesTo;
	}

	/**
	 * @return Returns the disposeParentSpecimen.
	 */
	public boolean getDisposeParentSpecimen()
	{
		return disposeParentSpecimen;
	}

	/**
	 * @param disposeParentSpecimen The disposeParentSpecimen to set.
	 */
	public void setDisposeParentSpecimen(boolean disposeParentSpecimen)
	{
		this.disposeParentSpecimen = disposeParentSpecimen;
	}

	/**
	 * Name: Sachin Lale.
	 * Bug ID: 3835
	 * Patch ID: 3835_2
	 * See also: 1-4
	 * Description : Addeed createdOn field for derived and aliqut Specimen.
	 * Returns the date on which the Participant is
	 * registered to the Collection Protocol.
	 * @hibernate.property name="createdOn" column="CREATED_ON_DATE" type="date"
	 * @return the date on which the Dervive/aliqut Specimen is created
	 * @see #setCreatedOn(Date)
	 */
	public Date getCreatedOn()
	{
		return createdOn;
	}

	/**
	 * Sets the date on which the Participant is
	 * registered to the Collection Protocol.
	 * @param createdOn - registrationDate the date on which the Participant is
	 * registered to the Collection Protocol.
	 * @see #getRegistrationDate()
	 */
	public void setCreatedOn(Date createdOn)
	{
		this.createdOn = createdOn;
	}

	/**
	 * Get CollectionStatus.
	 * @return String.
	 */
	public String getCollectionStatus()
	{
		return collectionStatus;
	}

	/**
	 * Set CollectionStatus.
	 * @param collectionStatus String.
	 */
	public void setCollectionStatus(String collectionStatus)
	{
		this.collectionStatus = collectionStatus;
	}

	/**
	 * Parameterized Constructor.
	 * @param reqSpecimen SpecimenRequirement.
	 */
	public Specimen(SpecimenRequirement reqSpecimen)
	{
		super();
		this.activityStatus = Status.ACTIVITY_STATUS_ACTIVE.toString();
		this.initialQuantity = new Double(reqSpecimen.getInitialQuantity());
		this.availableQuantity = new Double(0);
		this.lineage = reqSpecimen.getLineage();
		this.pathologicalStatus = reqSpecimen.getPathologicalStatus();
		this.collectionStatus = Constants.COLLECTION_STATUS_PENDING;
		if (reqSpecimen.getSpecimenCharacteristics() != null)
		{
			this.specimenCharacteristics = new SpecimenCharacteristics(
					reqSpecimen.getSpecimenCharacteristics());
		}
		this.specimenType = reqSpecimen.getSpecimenType();
		this.specimenClass = reqSpecimen.getClassName();
		this.isAvailable = Boolean.FALSE;
		this.specimenRequirement = reqSpecimen;
	}

	/**
	 * Set DefaultSpecimenEventCollection.
	 * @param userID Long.
	 */
	public void setDefaultSpecimenEventCollection(Long userID)
	{
		Collection specimenEventCollection = new HashSet();
		User user = new User();
		user.setId(userID);
		CollectionEventParameters collectionEventParameters =
			EventsUtil.populateCollectionEventParameters(user);
		collectionEventParameters.setSpecimen(this);
		specimenEventCollection.add(collectionEventParameters);

		ReceivedEventParameters receivedEventParameters = EventsUtil.populateReceivedEventParameters(user);
		receivedEventParameters.setSpecimen(this);
		specimenEventCollection.add(receivedEventParameters);
		setSpecimenEventCollection(specimenEventCollection);
	}

	/**
	 * Set ConsentTierStatusCollectionFromSCG.
	 * @param specimenCollectionGroup SpecimenCollectionGroup.
	 */
	public void setConsentTierStatusCollectionFromSCG(SpecimenCollectionGroup
			specimenCollectionGroup)
	{
		Collection consentTierStatusCollectionN = this.getConsentTierStatusCollection();
		if (consentTierStatusCollectionN == null)
		{
			consentTierStatusCollectionN = new HashSet();
		}

		Collection consentTierStatusCollection = specimenCollectionGroup.getConsentTierStatusCollection();
		Collection specConsTierColl = this.getConsentTierStatusCollection();
		boolean hasMoreConsents = false;
		if (consentTierStatusCollection != null && !consentTierStatusCollection.isEmpty())
		{
			Iterator iterator = consentTierStatusCollection.iterator();
			Iterator specCoIterator = null;
			if (specConsTierColl != null)
			{
				specCoIterator = specConsTierColl.iterator();
				hasMoreConsents = specCoIterator.hasNext();
			}
			while (iterator.hasNext())
			{
				ConsentTierStatus consentTierStatus = (ConsentTierStatus) iterator.next();
				ConsentTierStatus consentTierstatusN = null;

				if (hasMoreConsents)
				{
					consentTierstatusN = (ConsentTierStatus) specCoIterator.next();
					consentTierstatusN.setConsentTier(consentTierStatus.getConsentTier());
					consentTierstatusN.setStatus(consentTierStatus.getStatus());//bug 7637
					hasMoreConsents = specCoIterator.hasNext();
				}
				else
				{
					consentTierstatusN = new ConsentTierStatus(consentTierStatus);
					consentTierStatusCollectionN.add(consentTierstatusN);
				}
			}
		}
		setConsentTierStatusCollection(consentTierStatusCollectionN);
	}

	/**
	 * @return the specimenPosition
	 * @hibernate.one-to-one class="edu.wustl.catissuecore.domain.SpecimenPosition" cascade="save-update"
	 */
	public SpecimenPosition getSpecimenPosition()
	{
		return specimenPosition;
	}

	/**
	 * @param specimenPosition the specimenPosition to set
	 */
	public void setSpecimenPosition(SpecimenPosition specimenPosition)
	{
		this.specimenPosition = specimenPosition;
	}

	/**
	 * Get SpecimenRequirement.
	 * @return SpecimenRequirement.
	 */
	public SpecimenRequirement getSpecimenRequirement()
	{
		return specimenRequirement;
	}

	/**
	 * Set SpecimenRequirement.
	 * @param requirementSpecimen SpecimenRequirement.
	 */
	public void setSpecimenRequirement(SpecimenRequirement requirementSpecimen)
	{
		this.specimenRequirement = requirementSpecimen;
	}

	//bug no. 7690
	/**
	 * Set PropogatingSpecimenEventCollection.
	 * @param specimenEventColl Collection.
	 * @param userId Long.
	 * @param specimen Specimen.
	 */
	public void setPropogatingSpecimenEventCollection(Collection specimenEventColl,
			Long userId, Specimen specimen)
	{
		User user = new User();
		user.setId(userId);
		String collProcedure = Constants.CP_DEFAULT;
		String collContainer = Constants.CP_DEFAULT;
		String recQty = Constants.CP_DEFAULT;
		Date collTimestamp = new Date(System.currentTimeMillis());
		Date recTimestamp = new Date(System.currentTimeMillis());
		User collEventUser = user;
		User recEventUser = user;

		Iterator eventCollItr = specimen.getSpecimenCollectionGroup().
			getSpecimenEventParametersCollection().iterator();
		while (eventCollItr.hasNext())
		{
			SpecimenEventParameters eventParam = (SpecimenEventParameters) eventCollItr.next();
			if (eventParam instanceof CollectionEventParameters)
			{
				collProcedure = ((CollectionEventParameters) eventParam).getCollectionProcedure();
				collContainer = ((CollectionEventParameters) eventParam).getContainer();
				collTimestamp = ((CollectionEventParameters) eventParam).getTimestamp();
				collEventUser = ((CollectionEventParameters) eventParam).getUser();
			}
			if (eventParam instanceof ReceivedEventParameters)
			{
				recQty = ((ReceivedEventParameters) eventParam).getReceivedQuality();
				recTimestamp = ((ReceivedEventParameters) eventParam).getTimestamp();
				recEventUser = ((ReceivedEventParameters) eventParam).getUser();
			}
		}

		Collection specimenEventCollection = new HashSet();
		Iterator itr = specimenEventColl.iterator();
		while (itr.hasNext())
		{
			SpecimenEventParameters eventParam = (SpecimenEventParameters) itr.next();
			if (eventParam instanceof CollectionEventParameters)
			{
				CollectionEventParameters collEventParam = (CollectionEventParameters) eventParam;
				if (collEventParam != null)
				{
					CollectionEventParameters collectionEventParameters =
						new CollectionEventParameters(collEventParam);
					collectionEventParameters.setSpecimen(this);
					collectionEventParameters.setTimestamp(collTimestamp);
					collectionEventParameters.setUser(collEventUser);
					if (!Constants.CP_DEFAULT.equals(collProcedure))
					{
						collectionEventParameters.setCollectionProcedure(collProcedure);
					}
					if (!Constants.CP_DEFAULT.equals(collContainer))
					{
						collectionEventParameters.setContainer(collContainer);
					}
					specimenEventCollection.add(collectionEventParameters);
				}
			}
			if (eventParam instanceof ReceivedEventParameters)
			{
				ReceivedEventParameters recEventParam = (ReceivedEventParameters) eventParam;
				if (recEventParam != null)
				{
					ReceivedEventParameters receivedEventParameters =
						new ReceivedEventParameters(recEventParam);
					receivedEventParameters.setSpecimen(this);
					receivedEventParameters.setTimestamp(recTimestamp);
					receivedEventParameters.setUser(recEventUser);
					if (!Constants.CP_DEFAULT.equals(recQty))
					{
						receivedEventParameters.setReceivedQuality(recQty);
					}
					specimenEventCollection.add(receivedEventParameters);
				}
			}
		}
		this.setSpecimenEventCollection(specimenEventCollection);
	}
}