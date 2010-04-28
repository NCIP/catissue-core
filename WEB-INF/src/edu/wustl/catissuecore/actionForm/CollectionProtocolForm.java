/**
* <p>Title: CollectionProtocolForm Class>
* <p>Description:  CollectionProtocolForm Class is used to encapsulate all the request parameters passed
* from User Add/Edit webpage. </p>
* Copyright:    Copyright (c) year
* Company: Washington University, School of Medicine, St. Louis.

* @author Gautam Shetty
* @version 1.00
* Created on Mar 3, 2005
*/

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * CollectionProtocolForm Class is used to encapsulate all the request
 * parameters passed from collection protocol Add/Edit webpage.
 *
 * @author Mandar Deshmukh
 * @author gautam_shetty
 */
public class CollectionProtocolForm extends SpecimenProtocolForm
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** logger Logger - Generic logger. */
	private static final Logger LOGGER = Logger.getCommonLogger(CollectionProtocolForm.class);

	/** The coordinator ids. */
	protected long[] coordinatorIds;

	/** Counter that contains number of rows in the 'Add More' functionality. outer block */
	private int outerCounter = 1;

	/** Patch Id : Collection_Event_Protocol_Order_4 Description : To get CollectionProtocol Events in order (Changed from HashMap to LinkedHashMap). */
	/**
	 * Counter that contains number of rows in the 'Add More' functionality. inner block
	 */
	protected Map innerLoopValues = new LinkedHashMap();

	/** whether Aliquote in same container. */
	protected boolean aliqoutInSameContainer = false;

	//Consent tracking(Virender Mehta)
	/** Unsigned Form Url for the Consents. */
	protected String unsignedConsentURLName;

	/** Map for Storing Values of Consent Tiers. */
	protected Map consentValues = new HashMap();

	/** No of Consent Tier. */
	private int consentTierCounter = 0;

	/** CheckBox for consent is checked or not. */
	private boolean consentWaived = false;
	//Consent tracking(Virender Mehta)

	/** The site ids. */
	protected long[] siteIds;

	/**
	 * No argument constructor for CollectionProtocolForm class.
	 */
	public CollectionProtocolForm()
	{
		super();
	}

	/**
	 * Sets the value.
	 *
	 * @param key  Value of Key
	 * @param value Value corrosponding to the Key
	 */
	@Override
	public void setValue(final String key, final Object value)
	{
		if (this.isMutable())
		{
			this.values.put(key, value);
		}
	}

	/**
	 * Gets the value.
	 *
	 * @param key This is used to get corresponding Value from the Map
	 *
	 * @return This is used to get corresponding Value from the Map
	 */
	@Override
	public Object getValue(final String key)
	{
		return this.values.get(key);
	}

	/**
	 * Gets the all values.
	 *
	 * @return values in map
	 */
	@Override
	public Collection getAllValues()
	{
		return this.values.values();
	}

	/**
	 * Gets the values.
	 *
	 * @return values
	 */
	@Override
	public Map getValues()
	{
		return this.values;
	}

	/**
	 * Sets the values.
	 *
	 * @param values Set the values
	 */
	@Override
	public void setValues(final Map values)
	{
		this.values = values;
	}

	/**
	 * Gets the inner loop values.
	 *
	 * @return innerLoopValues
	 */
	public Map getInnerLoopValues()
	{
		return this.innerLoopValues;
	}

	/**
	 * Sets the inner loop values.
	 *
	 * @param innerLoopValues The innerLoopValues to set.
	 */
	public void setInnerLoopValues(final Map innerLoopValues)
	{
		this.innerLoopValues = innerLoopValues;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 *
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setIvl(final String key, final Object value)///changes here
	{
		if (this.isMutable())
		{
			this.innerLoopValues.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 *
	 * @param key the required key.
	 *
	 * @return the object to which this map maps the specified key.
	 */
	public Object getIvl(final String key)
	{
		return this.innerLoopValues.get(key);
	}

	/**
	 * Gets the outer counter.
	 *
	 * @return Returns the outerCounter.
	 */
	public int getOuterCounter()
	{
		return this.outerCounter;
	}

	/**
	 * Sets the outer counter.
	 *
	 * @param outerCounter The outerCounter to set.
	 */
	public void setOuterCounter(final int outerCounter)
	{
		this.outerCounter = outerCounter;
	}

	/**
	 * Method to set class attributes.
	 */
	@Override
	protected void reset()
	{
		//		super.reset();
		//		coordinatorIds = null;
		//		this.outerCounter = 1;
		//		this.values  = new HashMap();
	}

	/**
	 * Gets the coordinator ids.
	 *
	 * @return Returns the protocolcoordinator ids.
	 */
	public long[] getCoordinatorIds()
	{
		return this.coordinatorIds;
	}

	/**
	 * Sets the coordinator ids.
	 *
	 * @param coordinatorIds The coordinatorIds to set.
	 */
	public void setCoordinatorIds(long[] coordinatorIds)
	{
		this.coordinatorIds = coordinatorIds;
	}

	/**
	 * Copies the data from an AbstractDomain object to a DistributionProtocolForm object.
	 *
	 * @param abstractDomain An AbstractDomain object.
	 */
	@Override
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		super.setAllValues(abstractDomain);
		final CollectionProtocol cProtocol = (CollectionProtocol) abstractDomain;
		if (cProtocol.getAliquotInSameContainer() != null)
		{
			this.aliqoutInSameContainer = cProtocol.getAliquotInSameContainer().booleanValue();
		}

		//For Consent Tracking
		this.unsignedConsentURLName = cProtocol.getUnsignedConsentDocumentURL();

		if (cProtocol.getConsentsWaived() == null)
		{
			this.consentWaived = false;
		}
		else
		{
			this.consentWaived = cProtocol.getConsentsWaived().booleanValue();
		}

		//Bug #13312
		this.sequenceNumber = cProtocol.getSequenceNumber();
		this.type = cProtocol.getType();
		this.studyCalendarEventPoint = cProtocol.getStudyCalendarEventPoint();

		//this.consentValues = prepareConsentTierMap(cProtocol.getConsentTierCollection());
	}

	/**
	 * For Consent Tracking
	 * Setting the consentValuesMap.
	 *
	 * @param mapping the mapping
	 * @param request the request
	 *
	 * @return tempMap
	 */
	//	private Map prepareConsentTierMap(Collection consentTierColl)
	//	{
	//		Map tempMap = new HashMap();
	//		if(consentTierColl!=null)
	//		{
	//			Iterator consentTierCollIter = consentTierColl.iterator();
	//			int i = 0;
	//			while(consentTierCollIter.hasNext())
	//			{
	//				ConsentTier consent = (ConsentTier)consentTierCollIter.next();
	//				String statement = "ConsentBean:"+i+"_statement";
	//				String preDefinedStatementkey = "ConsentBean:"+i+"_predefinedConsents";
	//				String statementkey = "ConsentBean:"+i+"_consentTierID";
	//				tempMap.put(statement, consent.getStatement());
	//				tempMap.put(preDefinedStatementkey, consent.getStatement());
	//				tempMap.put(statementkey, consent.getId());
	//				i++;
	//			}
	//			consentTierCounter = consentTierColl.size();
	//		}
	//		return tempMap;
	//	}
	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		LOGGER.debug("OPERATION : ----- : " + this.getOperation());
		ActionErrors errors = super.validate(mapping, request);
		//		Validator validator = new Validator();
		try
		{

			//Check for PI can not be coordinator of the protocol.
			if (this.coordinatorIds != null && this.principalInvestigatorId != -1)
			{
				for (final long protocolCoordinatorId : this.coordinatorIds)
				{
					if (protocolCoordinatorId == this.principalInvestigatorId)
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.pi.coordinator.same"));
						break;
					}
				}
			}

			LOGGER.debug("Protocol Coordinators : " + this.coordinatorIds);
		}
		catch (final Exception excp)
		{
			// use of logger as per bug 79
			CollectionProtocolForm.LOGGER.error(excp.getMessage(), excp);
			//logger.debug(excp);
			errors = new ActionErrors();
		}
		return errors;
	}

	/**
	 * Returns the id assigned to form bean.
	 *
	 * @return COLLECTION_PROTOCOL_FORM_ID
	 */
	@Override
	public int getFormId()
	{
		return Constants.COLLECTION_PROTOCOL_FORM_ID;
	}

	/**
	 * This is the main method, main( ) is the method called when a Java application begins.
	 *
	 * @param addNewFor the add new for
	 * @param addObjectIdentifier the add object identifier
	 */
	//	public static void main(String[] args)
	//	{
	//		int maxCount=1;
	////		int maxIntCount=1;
	//
	//		CollectionProtocolForm collectionProtocolForm = null;
	//
	//		Object obj = new Object();//request.getAttribute("collectionProtocolForm");
	//
	//		if(obj != null && obj instanceof CollectionProtocolForm)
	//		{
	//			collectionProtocolForm = (CollectionProtocolForm)obj;
	//			maxCount = collectionProtocolForm.getOuterCounter();
	//		}
	//
	//		for(int counter=1;counter<=maxCount;counter++)
	//		{
	//			String commonLabel = "value(CollectionProtocolEvent:" + counter;
	//
	//			String cid = "ivl(" + counter + ")";
	//			String functionName = "insRow('" + commonLabel + "','" + cid +"')";
	//
	//			if(collectionProtocolForm!=null)
	//			{
	//				Object o = collectionProtocolForm.getIvl(cid);
	//				maxIntCount = Integer.parseInt(o.toString());
	//			}
	//		}
	//	}
	//
	/**
	 * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
	 * @param addNewFor - FormBean ID of the object inserted
	 *  @param addObjectIdentifier - Identifier of the Object inserted
	 */
	@Override
	public void setAddNewObjectIdentifier(String addNewFor, Long addObjectIdentifier)
	{
		if ("principalInvestigator".equals(addNewFor))
		{
			this.setPrincipalInvestigatorId(addObjectIdentifier.longValue());
		}
		else if ("protocolCoordinator".equals(addNewFor))
		{
			final long[] pcoordIDs = {Long.parseLong(addObjectIdentifier.toString())};

			this.setCoordinatorIds(pcoordIDs);
		}
	}

	/**
	 * Checks if is aliqout in same container.
	 *
	 * @return Returns the aliqoutInSameContainer.
	 */
	public boolean isAliqoutInSameContainer()
	{
		return this.aliqoutInSameContainer;
	}

	/**
	 * Sets the aliqout in same container.
	 *
	 * @param aliqoutInSameContainer The aliqoutInSameContainer to set.
	 */
	public void setAliqoutInSameContainer(boolean aliqoutInSameContainer)
	{
		this.aliqoutInSameContainer = aliqoutInSameContainer;
	}

	//	For Consent Tracking Start

	/**
	 * Gets the unsigned consent url name.
	 *
	 * @return unsignedConsentURLName  Get Unsigned Signed URL name
	 */
	public String getUnsignedConsentURLName()
	{
		return this.unsignedConsentURLName;
	}

	/**
	 * Sets the unsigned consent url name.
	 *
	 * @param unsignedConsentURLName  Set Unsigned Signed URL name
	 */
	public void setUnsignedConsentURLName(String unsignedConsentURLName)
	{
		this.unsignedConsentURLName = unsignedConsentURLName;
	}

	/**
	 * Sets the consent value.
	 *
	 * @param key Key
	 * @param value Value
	 */
	public void setConsentValue(final String key, final Object value)
	{
		if (this.isMutable())
		{
			this.consentValues.put(key, value);
		}
	}

	/**
	 * Gets the consent value.
	 *
	 * @param key Key
	 *
	 * @return Statements
	 */
	public Object getConsentValue(final String key)
	{
		return this.consentValues.get(key);
	}

	/**
	 * Gets the consent values.
	 *
	 * @return consentValues   Set Consents into the Map
	 */
	public Map getConsentValues()
	{
		return this.consentValues;
	}

	/**
	 * Sets the consent values.
	 *
	 * @param consentValues Set Consents into the Map
	 */
	public void setConsentValues(final Map consentValues)
	{
		this.consentValues = consentValues;
	}

	/**
	 * Gets the consent tier counter.
	 *
	 * @return consentTierCounter  This will keep track of count of Consent Tier
	 */
	public int getConsentTierCounter()
	{
		return this.consentTierCounter;
	}

	/**
	 * Sets the consent tier counter.
	 *
	 * @param consentTierCounter  This will keep track of count of Consent Tier
	 */
	public void setConsentTierCounter(int consentTierCounter)
	{
		this.consentTierCounter = consentTierCounter;
	}

	/**
	 * If consent waived is true then no need to check consents prior to distribution.
	 *
	 * @return consentWaived
	 */
	public boolean isConsentWaived()
	{
		return this.consentWaived;
	}

	/**
	 * If consent waived is true then no need to check consents prior to distribution.
	 *
	 * @param consentWaived If consent waived is true then no need to check consents prior to distribution
	 */
	public void setConsentWaived(final boolean consentWaived)
	{
		this.consentWaived = consentWaived;
	}

	/**
	 * studyCalendarEventPoint.
	 *
	 * @return studyCalendarEventPoint.
	 */
	public Double getStudyCalendarEventPoint()
	{
		return this.studyCalendarEventPoint;
	}

	//	For Consent Tracking End

	/**
	 * Gets the site ids.
	 *
	 * @return the site ids
	 */
	public long[] getSiteIds()
	{
		return this.siteIds;
	}

	/**
	 * Sets the site ids.
	 *
	 * @param siteIds the new site ids
	 */
	public void setSiteIds(final long[] siteIds)
	{
		this.siteIds = siteIds;
	}

	/**
	 * parentCollectionProtocol.
	 *
	 * @param parentCollectionProtocol parentCollectionProtocol.
	 */
	public void setParentCollectionProtocol(CollectionProtocol parentCollectionProtocol)
	{
		this.parentCollectionProtocol = parentCollectionProtocol;
	}

	/**
	 * Set sequence no.
	 *
	 * @param sequenceNumber sequenceNumber.
	 */
	public void setSequenceNumber(Integer sequenceNumber)
	{
		this.sequenceNumber = sequenceNumber;
	}


	/**
	 * type.
	 *
	 * @return type.
	 */
	public String getType()
	{
		return this.type;
	}





	/**
	 * Set type.
	 *
	 * @param type the type
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * Set study calendar point.
	 *
	 * @param studyCalendarEventPoint studyCalendarEventPoint.
	 */
	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint)
	{
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	/** Parent Collection Protocol. */
	protected CollectionProtocol parentCollectionProtocol;

	/** Sequence Number. */
	protected Integer sequenceNumber;

	/** Collection Protocol type - Arm, Cycle, Phase. */
	protected String type;


	/** Defines the relative time point in days. */
	protected Double studyCalendarEventPoint;

	/** Parent collection protocol Identifier. */
	protected Long parentCollectionProtocolId;

	/**
	 * get parentCollecetionProtocol id.
	 *
	 * @return parentCollecetionProtocol id.
	 */
	public Long getParentCollectionProtocolId()
	{
		return this.parentCollectionProtocolId;
	}

	/**
	 * Set parentCollecetionProtocol id.
	 *
	 * @param parentCollectionProtocolId the parent collection protocol id
	 */
	public void setParentCollectionProtocolId(Long parentCollectionProtocolId)
	{
		this.parentCollectionProtocolId = parentCollectionProtocolId;
	}

	/** protocolCoordinatorIds : This will hold the clinical Diagnosis values. */
	private String[] protocolCoordinatorIds;

	/**
	 * This will give the clinical diagnosis values.
	 *
	 * @return protocolCoordinatorIds : clinicalDiagnosis.
	 */
	public String[] getProtocolCoordinatorIds()
	{
		return protocolCoordinatorIds;
	}

	/**
	 * This will set the clinical diagnosis values.
	 *
	 * @param protocolCoordinatorIds clinicalDiagnosis.
	 */
	public void setProtocolCoordinatorIds(String[] protocolCoordinatorIds)
	{
		this.protocolCoordinatorIds = protocolCoordinatorIds;
	}

	/**
	 * parentCollectionProtocol.
	 *
	 * @return parentCollectionProtocol.
	 */
	public CollectionProtocol getParentCollectionProtocol()
	{
		return this.parentCollectionProtocol;
	}

	/**
	 * sequenceNumber.
	 *
	 * @return sequence no.
	 */
	public Integer getSequenceNumber()
	{
		return this.sequenceNumber;
	}


}