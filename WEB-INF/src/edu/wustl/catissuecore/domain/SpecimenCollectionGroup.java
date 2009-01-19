/**
 * <p>Title: SpecimenCollectionGroup Class>
 * <p>Description: An event that results in the collection
 * of one or more specimen from a participant.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.EventsUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * An event that results in the collection.
 * of one or more specimen from a participant.
 * @hibernate.class table="CATISSUE_SPECIMEN_COLL_GROUP"
 * @author gautam_shetty
 */
public class SpecimenCollectionGroup extends AbstractSpecimenCollectionGroup implements Serializable
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SpecimenCollectionGroup.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 8543074529678284997L;

	/**
	 * name assigned to Specimen Collection Group.
	 */
	protected String name;

	/**
	 * The Specimens in this SpecimenCollectionGroup.
	 */
	protected Collection<Specimen> specimenCollection = new LinkedHashSet<Specimen>();

	/**
	 * Name : Ashish Gupta.
	 * Reviewer Name : Sachin Lale.
	 * Bug ID: 2741
	 * Patch ID: 2741_1
	 * Description: Condition indicating whether to propagate collection events and
	 * received events to specimens under this scg.
	 */
	/**
	 * Condition indicating whether to propagate collection events and received events
	 * to specimens under this scg.
	 */
	protected transient boolean applyEventsToSpecimens = false;

	/**
	 * Surgical Pathology Number of the associated pathology report, earlier was Present in Clinical Report.
	 */
	protected String surgicalPathologyNumber;

	/**
	 * Name: Sachin Lale.
	 * Bug ID: 3052
	 * Patch ID: 3052_1
	 * See also: 1-4
	 * Description : A comment field at the Specimen Collection Group level.
	 */
	protected String comment;
	/**
	 * A registration of a Participant to a Collection Protocol.
	 */
	protected CollectionProtocolRegistration collectionProtocolRegistration;

	/**
	 * An identified surgical pathology report associated with
	 * current specimen collection group.
	 */
	protected IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport;
	/**
	 * A deidentified surgical pathology report associated with
	 * current specimen collection group.
	 */

	protected DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport;

	/**
	 * The consent tier status by multiple participants for a particular specimen collection group.
	 */
	protected Collection<ConsentTierStatus> consentTierStatusCollection;

	/**
	 * To perform operation based on withdraw button clicked.
	 * Default No Action to allow normal behaviour.
	 */
	protected String consentWithdrawalOption = Constants.WITHDRAW_RESPONSE_NOACTION;

	/**
	 * To apply changes to specimen based on consent status changes.
	 * Default Apply none to allow normal behaviour.
	 */
	protected String applyChangesTo = Constants.APPLY_NONE;

	/**
	 * To apply changes to specimen based on consent status changes.
	 * Default empty.
	 */
	protected String stringOfResponseKeys = "";

	/**
	 * Name : Ashish Gupta.
	 * Reviewer Name : Sachin Lale.
	 * Bug ID: 2741
	 * Patch ID: 2741_2.
	 * Description: 1 to many Association between SCG and SpecimenEventParameters.
	 */

	/**
	 * Collection and Received events associated with this SCG.
	 */
	protected Collection specimenEventParametersCollection = new HashSet();

	/**
	 * A required specimen collection event associated with a Collection Protocol.
	 */
	protected CollectionProtocolEvent collectionProtocolEvent;

	/**
	 * collectionStatus.
	 */
	protected String collectionStatus;

	/**
	 * offset.
	 */
	protected Integer offset;

	/**
	 * barcode attribute added for Suite 1.1.
	 */
	protected String barcode;

	/**
	 * encounterTimestamp.
	 */
	protected Date encounterTimestamp;

	/**
	 * isCPBasedSpecimenEntryChecked.
	 */
	protected boolean isCPBasedSpecimenEntryChecked = true;

	/**
	 * @return the isCPBasedSpecimenEntryChecked
	 */
	public boolean getIsCPBasedSpecimenEntryChecked()
	{
		return isCPBasedSpecimenEntryChecked;
	}

	/**
	 * @param isCPBasedSpecimenEntryChecked the isCPBasedSpecimenEntryChecked to set
	 */
	public void setIsCPBasedSpecimenEntryChecked(boolean isCPBasedSpecimenEntryChecked)
	{
		this.isCPBasedSpecimenEntryChecked = isCPBasedSpecimenEntryChecked;
	}

	/**
	 * Returns the required specimen collection event
	 * associated with a Collection Protocol.
	 * @hibernate.many-to-one column="COLLECTION_PROTOCOL_EVENT_ID"
	 * class="edu.wustl.catissuecore.domain.CollectionProtocolEvent" constrained="true"
	 * @return the required specimen collection event.
	 * associated with a Collection Protocol.
	 * @see #setCollectionProtocolEvent(CollectionProtocolEvent)
	 */
	public CollectionProtocolEvent getCollectionProtocolEvent()
	{
		return collectionProtocolEvent;
	}

	/**
	 * Sets the required specimen collection event
	 * associated with a Collection Protocol.
	 * @param collectionProtocolEvent the required specimen collection event
	 * associated with a Collection Protocol.
	 * @see #getCollectionProtocolEvent()
	 */
	public void setCollectionProtocolEvent(CollectionProtocolEvent collectionProtocolEvent)
	{
		this.collectionProtocolEvent = collectionProtocolEvent;
	}

	/**
	 * @return the specimenEventParametersCollection
	 * @hibernate.set cascade="save-update" inverse="true" table="CATISSUE_SPECIMEN_EVENT_PARAM" lazy="false"
	 * @hibernate.collection-one-to-many
	 * class="edu.wustl.catissuecore.domain.SpecimenEventParameters"
	 * @hibernate.collection-key column="SPECIMEN_COLL_GRP_ID"
	 */
	public Collection getSpecimenEventParametersCollection()
	{
		return specimenEventParametersCollection;
	}

	/**
	 * @param specimenEventParametersCollection the specimenEventParametersCollection to set.
	 */
	public void setSpecimenEventParametersCollection(Collection specimenEventParametersCollection)
	{
		this.specimenEventParametersCollection = specimenEventParametersCollection;
	}

	/**
	 * @return the consentTierStatusCollection
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ConsentTierStatus"
	 * lazy="true" cascade="save-update"
	 * @hibernate.set table="CATISSUE_CONSENT_TIER_STATUS" name="consentTierStatusCollection"
	 * @hibernate.collection-key column="SPECIMEN_COLL_GROUP_ID"
	 */
	public Collection<ConsentTierStatus> getConsentTierStatusCollection()
	{
		return consentTierStatusCollection;
	}

	/**
	 * @param consentTierStatusCollection the consentTierStatusCollection to set
	 */
	public void setConsentTierStatusCollection(Collection consentTierStatusCollection)
	{
		this.consentTierStatusCollection = consentTierStatusCollection;
	}

	/**
	 * Get Consent Withdrawal Option.
	 * @return String.
	 */
	public String getConsentWithdrawalOption()
	{
		return consentWithdrawalOption;
	}

	/**
	 * Set Consent Withdrawal Option.
	 * @param consentWithdrawalOption of String type.
	 */
	public void setConsentWithdrawalOption(String consentWithdrawalOption)
	{
		this.consentWithdrawalOption = consentWithdrawalOption;
	}

	/**
	 * Get Apply Changes To.
	 * @return String type.
	 */
	public String getApplyChangesTo()
	{
		return applyChangesTo;
	}

	/**
	 * Set Apply Changes To.
	 * @param applyChangesTo of String type.
	 */
	public void setApplyChangesTo(String applyChangesTo)
	{
		this.applyChangesTo = applyChangesTo;
	}

	/**
	 * Get String Of ResponseKeys.
	 * @return of String type.
	 */
	public String getStringOfResponseKeys()
	{
		return stringOfResponseKeys;
	}

	/**
	 * Set String Of ResponseKeys.
	 * @param stringOfResponseKeys of String type.
	 */
	public void setStringOfResponseKeys(String stringOfResponseKeys)
	{
		this.stringOfResponseKeys = stringOfResponseKeys;
	}

	/**
	 * Default Constructor.
	 */
	public SpecimenCollectionGroup()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form of AbstractActionForm type.
	 * @throws AssignDataException AssignDataException.
	 */
	public SpecimenCollectionGroup(AbstractActionForm form) throws AssignDataException
	{
		super();
		logger.debug("<<< Before setting Values >>>");
		setAllValues(form);
	}

	/**
	 * Returns the surgicalPathologyNumber of the report at the time of specimen collection.
	 * @hibernate.property name="surgicalPathologyNumber" type="string"
	 * column="SURGICAL_PATHOLOGY_NUMBER" length="50"
	 * @return surgical pathology number of the report at the time of specimen collection.
	 * @see #setSurgicalPathologyNumber(String)
	 */
	public String getSurgicalPathologyNumber()
	{
		return surgicalPathologyNumber;
	}

	/**
	 * Sets the surgical pathology number of the report at the time of specimen collection.
	 * @param surgicalPathologyNumber the surgical pathology report of
	 * the report at the time of specimen collection.
	 * @see #getSurgicalPathologyNumber()
	 */
	public void setSurgicalPathologyNumber(String surgicalPathologyNumber)
	{
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}

	/**
	 * Returns the registration of a Participant to a Collection Protocol.
	 * @hibernate.many-to-one column="COLLECTION_PROTOCOL_REG_ID"
	 * class="edu.wustl.catissuecore.domain.CollectionProtocolRegistration" constrained="true"
	 * @return the registration of a Participant to a Collection Protocol.
	 * @see #setCollectionProtocolRegistration(CollectionProtocolRegistration)
	 */
	public CollectionProtocolRegistration getCollectionProtocolRegistration()
	{
		return collectionProtocolRegistration;
	}

	/**
	 * Sets the registration of a Participant to a Collection Protocol.
	 * @param collectionProtocolRegistration the registration of a Participant
	 * to a Collection Protocol.
	 * @see #getCollectionProtocolRegistration()
	 */
	public void setCollectionProtocolRegistration(CollectionProtocolRegistration
			collectionProtocolRegistration)
	{
		this.collectionProtocolRegistration = collectionProtocolRegistration;
	}

	/**
	 * Returns message label to display on success add or edit.
	 * @return String
	 */
	public String getMessageLabel()
	{
		return this.name;
	}

	/**
	 * Name: Sachin Lale.
	 * Bug ID: 3052
	 * Patch ID: 3052_2
	 * Seea also: 1-4 and 1_1 to 1_5
	 * Returns the Specimen Collection Group comment .
	 * @hibernate.property name="comment" type="string" column="COMMENTS" length="2000"
	 * @return comment of String type.
	 * @see #setComment(String)
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * @param comment of String type.
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * Returns deidentified surgical pathology report of the current specimen collection group.
	 * @hibernate.one-to-one  name="deidentifiedSurgicalPathologyReport"
	 * class="edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport"
	 * property-ref="specimenCollectionGroup" not-null="false" cascade="save-update"
	 * @return DeidentifiedSurgicalPathologyReport object.
	 */
	public DeidentifiedSurgicalPathologyReport getDeIdentifiedSurgicalPathologyReport()
	{
		return deIdentifiedSurgicalPathologyReport;
	}

	/**
	 * Sets the deidentified surgical pathology report associated with the specimen collection group.
	 * @param deIdentifiedSurgicalPathologyReport deidentified report object.
	 */
	public void setDeIdentifiedSurgicalPathologyReport(DeidentifiedSurgicalPathologyReport
			deIdentifiedSurgicalPathologyReport)
	{
		this.deIdentifiedSurgicalPathologyReport = deIdentifiedSurgicalPathologyReport;
	}

	/**
	 * Returns deidentified surgical pathology report of the current specimen collection group.
	 * @hibernate.one-to-one  name="identifiedSurgicalPathologyReport"
	 * class="edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport"
	 * propertyref="specimenCollectionGroup" not-null="false" cascade="save-update"
	 * @return IdentifiedSurgicalPathologyReport object.
	 */
	public IdentifiedSurgicalPathologyReport getIdentifiedSurgicalPathologyReport()
	{
		return identifiedSurgicalPathologyReport;
	}

	/**
	 * Sets the identified surgical pathology report associated with the specimen collection group.
	 * @param identifiedSurgicalPathologyReport identified report object.
	 */
	public void setIdentifiedSurgicalPathologyReport(IdentifiedSurgicalPathologyReport
			identifiedSurgicalPathologyReport)
	{
		this.identifiedSurgicalPathologyReport = identifiedSurgicalPathologyReport;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(
	 * edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	/**
	 * Set all values on form.
	 * @param valueObject of IValueObject type.
	 * @throws AssignDataException AssignDataException.
	 */
	public void setAllValues(IValueObject valueObject) throws AssignDataException
	{
		super.setAllValues(valueObject);
		AbstractActionForm abstractForm = (AbstractActionForm) valueObject;
		SpecimenCollectionGroupForm form = (SpecimenCollectionGroupForm) abstractForm;
		try
		{
			this.setName(form.getName());
			this.barcode = form.getBarcode();
			if (Constants.TRUE.equals(form.getRestrictSCGCheckbox()))
			{
				this.isCPBasedSpecimenEntryChecked = Boolean.TRUE;
			}
			else
			{
				this.isCPBasedSpecimenEntryChecked = Boolean.FALSE;
			}

			// Bug no. 7390
			// adding the collection status in the add specimen collection group page
			// removed the addOperation() if loop
			if (form.getCollectionStatus() == null)
			{
				this.setCollectionStatus(Constants.COLLECTION_STATUS_PENDING);
			}
			else
			{
				this.setCollectionStatus(form.getCollectionStatus());
			}

			/**
			 * Name: Sachin Lale
			 * Bug ID: 3052
			 * Patch ID: 3052_1
			 * See also: 1_1 to 1_5
			 * Description : A comment field is set from form bean to domain object.
			 */
			this.setComment(form.getComment());

			collectionProtocolEvent = new CollectionProtocolEvent();
			collectionProtocolEvent.setId(Long.valueOf(form.getCollectionProtocolEventId()));

			logger.debug("form.getParticipantsMedicalIdentifierId() " +
					form.getParticipantsMedicalIdentifierId());

			this.setSurgicalPathologyNumber(form.getSurgicalPathologyNumber());

			collectionProtocolRegistration = new CollectionProtocolRegistration();
			collectionProtocolRegistration.setId(form.getCollectionProtocolRegistrationId());
			/**
			 * Name: Vijay Pande
			 * Reviewer Name: Aarti Sharma
			 * Variable checkedButton name is changed to radioButton hence its getter
			 * method name is changed
			 */
			if (form.getRadioButtonForParticipant() == 1)
			{
				//value of radio button is 2 when participant name is selected
				Participant participant = new Participant();
				/**For Migration Start**/
				//form.setParticipantId(Utility.getParticipantId(form.getParticipantName()));
				/**For Migration End**/
				participant.setId(Long.valueOf(form.getParticipantId()));
				collectionProtocolRegistration.setParticipant(participant);
				collectionProtocolRegistration.setProtocolParticipantIdentifier(null);

				ParticipantMedicalIdentifier participantMedicalIdentifier =
					new ParticipantMedicalIdentifier();
				participantMedicalIdentifier.setId(Long.valueOf(
						form.getParticipantsMedicalIdentifierId()));
			}
			else
			{
				collectionProtocolRegistration.setProtocolParticipantIdentifier(
						form.getProtocolParticipantIdentifier());
				collectionProtocolRegistration.setParticipant(null);
			}

			CollectionProtocol collectionProtocol = new CollectionProtocol();
			collectionProtocol.setId(Long.valueOf(form.getCollectionProtocolId()));
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);

			/**
			 * Setting the consentTier responses for SCG Level.
			 * Virender Mehta
			 */
			this.consentTierStatusCollection = prepareParticipantResponseCollection(form);

			this.consentWithdrawalOption = form.getWithdrawlButtonStatus();
			this.applyChangesTo = form.getApplyChangesTo();
			this.stringOfResponseKeys = form.getStringOfResponseKeys();

			/**
			 * Name : Ashish Gupta.
			 * Reviewer Name : Sachin Lale.
			 * Bug ID: 2741
			 * Patch ID: 2741_3
			 * Description: Populating events in SCG
			 */
			//Adding Events
			setEventsFromForm(form, form.getOperation());
			//Adding events to Specimens
			if (form.isApplyEventsToSpecimens())
			{
				applyEventsToSpecimens = true;
			}
			this.offset = Integer.valueOf(form.getOffset());
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new AssignDataException(e);
		}
	}

	/**
	 * Name : Ashish Gupta.
	 * Reviewer Name : Sachin Lale.
	 * Bug ID: 2741
	 * Patch ID: 2741_4
	 * Description: Method to populate Events in SCG.
	 */
	/**
	 * @param form of SpecimenCollectionGroupForm type.
	 * @param operation of String type.
	 * This function populates all events for the given scg.
	 */
	private void setEventsFromForm(SpecimenCollectionGroupForm form, String operation)
	{
		CollectionEventParameters collectionEventParameters = null;
		ReceivedEventParameters receivedEventParameters = null;
		Collection tempColl = new HashSet();
		if (operation.equals(Constants.ADD))
		{
			collectionEventParameters = new CollectionEventParameters();
			receivedEventParameters = new ReceivedEventParameters();
		}
		else
		{
			Iterator iter = specimenEventParametersCollection.iterator();
			while (iter.hasNext())
			{
				Object temp = iter.next();
				if (temp instanceof CollectionEventParameters)
				{
					collectionEventParameters = (CollectionEventParameters) temp;
				}
				else if (temp instanceof ReceivedEventParameters)
				{
					receivedEventParameters = (ReceivedEventParameters) temp;
				}
			}
			if (form.getCollectionEventId() != 0)
			{
				collectionEventParameters.setId(Long.valueOf(form.getCollectionEventId()));
				receivedEventParameters.setId(Long.valueOf(form.getReceivedEventId()));
			}
		}
		//creating new events when there are no events associated with the scg
		if (collectionEventParameters == null && receivedEventParameters == null)
		{
			collectionEventParameters = new CollectionEventParameters();
			receivedEventParameters = new ReceivedEventParameters();
		}
		setEventParameters(collectionEventParameters, receivedEventParameters, form);

		tempColl.add(collectionEventParameters);
		tempColl.add(receivedEventParameters);
		if (operation.equals(Constants.ADD))
		{
			this.specimenEventParametersCollection.add(collectionEventParameters);
			this.specimenEventParametersCollection.add(receivedEventParameters);
		}
		else
		{
			this.specimenEventParametersCollection = tempColl;
		}
	}

	/**
	 * @param collectionEventParameters CollectionEventParameters.
	 * @param receivedEventParameters ReceivedEventParameters.
	 * @param form SpecimenCollectionGroupForm.
	 */
	private void setEventParameters(CollectionEventParameters collectionEventParameters,
			ReceivedEventParameters receivedEventParameters, SpecimenCollectionGroupForm form)
	{
		collectionEventParameters.setCollectionProcedure(form.getCollectionEventCollectionProcedure());
		collectionEventParameters.setComment(form.getCollectionEventComments());
		collectionEventParameters.setContainer(form.getCollectionEventContainer());
		Date timestamp = EventsUtil.setTimeStamp(form.getCollectionEventdateOfEvent(),
				form.getCollectionEventTimeInHours(), form.getCollectionEventTimeInMinutes());
		collectionEventParameters.setTimestamp(timestamp);
		User user = null;
		if (form.getCollectionEventUserId() != 0)
		{
			user = new User();
			user.setId(Long.valueOf(form.getCollectionEventUserId()));
		}
		collectionEventParameters.setUser(user);
		collectionEventParameters.setSpecimenCollectionGroup(this);

		//Received Events
		receivedEventParameters.setComment(form.getReceivedEventComments());
		User receivedUser = null;
		if (form.getReceivedEventUserId() != 0)
		{
			receivedUser = new User();
			receivedUser.setId(Long.valueOf(form.getReceivedEventUserId()));
		}
		receivedEventParameters.setUser(receivedUser);
		receivedEventParameters.setReceivedQuality(form.getReceivedEventReceivedQuality());
		Date receivedTimestamp = EventsUtil.setTimeStamp(form.getReceivedEventDateOfEvent(),
				form.getReceivedEventTimeInHours(), form.getReceivedEventTimeInMinutes());
		receivedEventParameters.setTimestamp(receivedTimestamp);
		receivedEventParameters.setSpecimenCollectionGroup(this);
	}

	/**
	 * For Consent Tracking.
	 * Setting the Domain Object.
	 * @param  form CollectionProtocolRegistrationForm.
	 * @return consentResponseColl.
	 */
	private Collection prepareParticipantResponseCollection(SpecimenCollectionGroupForm form)
	{
		MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
		Collection beanObjColl = null;
		try
		{
			beanObjColl = mapdataParser.generateData(form.getConsentResponseForScgValues());
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
		}
		Iterator iter = beanObjColl.iterator();
		Collection consentResponseColl = new HashSet();
		while (iter.hasNext())
		{
			ConsentBean consentBean = (ConsentBean) iter.next();
			ConsentTierStatus consentTierstatus = new ConsentTierStatus();
			//Setting response
			consentTierstatus.setStatus(consentBean.getSpecimenCollectionGroupLevelResponse());
			if (consentBean.getSpecimenCollectionGroupLevelResponseID() != null
				&& consentBean.getSpecimenCollectionGroupLevelResponseID().trim().length() > 0)
			{
				consentTierstatus.setId(Long.parseLong(consentBean.
						getSpecimenCollectionGroupLevelResponseID()));
			}
			//Setting consent tier
			ConsentTier consentTier = new ConsentTier();
			consentTier.setId(Long.parseLong(consentBean.getConsentTierID()));
			consentTierstatus.setConsentTier(consentTier);
			consentResponseColl.add(consentTierstatus);
		}
		return consentResponseColl;
	}

	/**
	 * @return the applyEventsToSpecimens
	 */
	public boolean isApplyEventsToSpecimens()
	{
		return applyEventsToSpecimens;
	}

	/**
	 * @param applyEventsToSpecimens the applyEventsToSpecimens to set
	 */
	public void setApplyEventsToSpecimens(boolean applyEventsToSpecimens)
	{
		this.applyEventsToSpecimens = applyEventsToSpecimens;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup#getGroupName()
	 */
	/**
	 * Get Group Name.
	 * @return String.
	 */
	@Override
	public String getGroupName()
	{
		return this.getName();
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup#setGroupName(java.lang.String)
	 */
	/**
	 * Set Group Name.
	 * @param gpName of String type.
	 * @throws BizLogicException Business Logic Exception.
	 */
	@Override
	protected void setGroupName(String gpName) throws BizLogicException
	{

		if (gpName == null)
		{
			throw new BizLogicException("group name can't be null for " +
					"SpecimenCollectionGroup Object");
		}
		this.setName(gpName);
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
	 * @param collectionStatus of String type.
	 */
	public void setCollectionStatus(String collectionStatus)
	{
		this.collectionStatus = collectionStatus;
	}

	/**
	 * Parameterized Constructor.
	 * @param collectionProtocolEvent of CollectionProtocolEvent type.
	 */
	public SpecimenCollectionGroup(CollectionProtocolEvent collectionProtocolEvent)
	{
		super();
		this.collectionProtocolEvent = collectionProtocolEvent;
		this.activityStatus = collectionProtocolEvent.getActivityStatus();
		this.clinicalDiagnosis = collectionProtocolEvent.getClinicalDiagnosis();
		this.clinicalStatus = collectionProtocolEvent.getClinicalStatus();
		this.collectionStatus = Constants.COLLECTION_STATUS_PENDING;
	}

	/**
	 * Set ConsentTier Status Collection From CPR.
	 * @param collectionProtocolRegistration of CollectionProtocolRegistration type.
	 */
	public void setConsentTierStatusCollectionFromCPR(CollectionProtocolRegistration
			collectionProtocolRegistration)
	{
		Collection consentTierStatusCollectionN = this.getConsentTierStatusCollection();
		if (consentTierStatusCollectionN == null)
		{
			consentTierStatusCollectionN = new HashSet();
		}
		Collection consentTierResponseCollection = collectionProtocolRegistration.
			getConsentTierResponseCollection();
		Collection scgConsTierColl = this.getConsentTierStatusCollection();
		boolean hasMoreConsents = false;

		if (consentTierResponseCollection != null && !consentTierResponseCollection.isEmpty())
		{
			Iterator iterator = consentTierResponseCollection.iterator();
			Iterator scgIterator = null;
			if (scgConsTierColl != null)
			{
				scgIterator = scgConsTierColl.iterator();
				hasMoreConsents = scgIterator.hasNext();
			}
			while (iterator.hasNext())
			{
				ConsentTierResponse consentTierResponse = (ConsentTierResponse) iterator.next();
				ConsentTierStatus consentTierstatusN;
				if (hasMoreConsents)
				{
					consentTierstatusN = (ConsentTierStatus) scgIterator.next();
				}
				else
				{
					consentTierstatusN = new ConsentTierStatus();
					consentTierStatusCollectionN.add(consentTierstatusN);
				}
				ConsentTier consentTier = new ConsentTier(consentTierResponse.getConsentTier());
				consentTierstatusN.setConsentTier(consentTier);
				consentTierstatusN.setStatus(consentTierResponse.getResponse());

			}
		}
		this.setConsentTierStatusCollection(consentTierStatusCollectionN);
	}

	/**
	 * Set Default Specimen Group Name.
	 * @param collectionProtocol of CollectionProtocol type.
	 * @param ParticipantId of integer type.
	 * @param SCGId of integer type.
	 */
	public void setDefaultSpecimenGroupName(CollectionProtocol collectionProtocol,
			int ParticipantId, int SCGId)
	{
		String collectionProtocolTitle = collectionProtocol.getTitle();
		String maxCollTitle = collectionProtocolTitle;
		if (collectionProtocolTitle.length() > Constants.COLLECTION_PROTOCOL_TITLE_LENGTH)
		{
			maxCollTitle = collectionProtocolTitle.substring(0,
					Constants.COLLECTION_PROTOCOL_TITLE_LENGTH - 1);
		}
		setName(maxCollTitle + "_" + ParticipantId + "_" + SCGId);
	}

	/**
	 * Get Offset.
	 * @return Integer.
	 */
	public Integer getOffset()
	{
		return offset;
	}

	/**
	 * Set Offset.
	 * @param offset of Integer type.
	 */
	public void setOffset(Integer offset)
	{
		this.offset = offset;
	}

	/**
	 * Returns the collection Specimens in this SpecimenCollectionGroup.
	 * @hibernate.set name="specimenCollection" table="CATISSUE_SPECIMEN"
	 * cascade="none" inverse="true" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_COLLECTION_GROUP_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Specimen"
	 * @return the collection Specimens in this SpecimenCollectionGroup.
	 * @see #setSpecimenCollection(Collection)
	 */
	public Collection<Specimen> getSpecimenCollection()
	{
		return specimenCollection;
	}

	/**
	 * Sets the collection Specimens in this SpecimenCollectionGroup.
	 * @param specimenCollection the collection Specimens in this SpecimenCollectionGroup.
	 * @see #getSpecimenCollection()
	 */
	public void setSpecimenCollection(Collection<Specimen> specimenCollection)
	{
		this.specimenCollection = specimenCollection;
	}

	/**
	 * Returns the system generated unique Specimen Collection Group name.
	 * @hibernate.property name="name" column="NAME" type="string" length="255"
	 * @return the system generated unique name.
	 * @see #setName(String)
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Returns the unique barcode of the Specimen Collection Group.
	 * @hibernate.property name="barcode" column="BARCODE" type="string" length="255"
	 * @return the system generated unique name.
	 * @see #setName(String)
	 */
	public String getBarcode()
	{
		return barcode;
	}

	/**
	 * @param barcode The barcode to set.
	 */
	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
		String nullString = null;
		if (Constants.DOUBLE_QUOTES.equals(barcode))
		{
			this.barcode = nullString;
		}
	}

	/**
	 * Get EncounterTimestamp.
	 * @return Date.
	 */
	public Date getEncounterTimestamp()
	{
		return encounterTimestamp;
	}

	/**
	 * Set EncounterTimestamp.
	 * @param encounterTimestamp of Date type.
	 */
	public void setEncounterTimestamp(Date encounterTimestamp)
	{
		this.encounterTimestamp = encounterTimestamp;
	}
}