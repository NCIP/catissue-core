/**
 * <p>Title: DistributionForm Class</p>
 * <p>Description:  This Class handles the Distribution..
 * <p> It extends the EventParametersForm class.
 * Copyright:    Copyright (c) 2005
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Jyoti Singh
 * @version 1.00
 * Created on Aug 10, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.DistributedItem;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.MultipleSpecimenValidationUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 *
 * Description:  This Class handles the Distribution..
 */
public class DistributionForm extends AbstractActionForm implements ConsentTierData
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(DistributionForm.class);

	private long specimenId;
	/**
	 * Time in hours for the Event Parameter.
	 * */
	protected String timeInHours;

	/**
	 * Time in minutes for the Event Parameter.
	 * */
	protected String timeInMinutes;

	/**
	 * Date of the Event Parameter.
	 * */
	protected String dateOfEvent;

	/**
	 * Id of the User.
	 */
	protected long userId;

	/**
	 * Comments on the event parameter.
	 */
	protected String comments;

	//Consent Tracking Module---Virender Mehta
	/**
	 * Map for Storing responses for Consent Tiers.
	 */
	protected Map consentResponseForDistributionValues = new HashMap();
	/**
	 * No of Consent Tier
	 */
	private int consentTierCounter = 0;
	/**
	 * Signed Consent URL
	 */
	protected String signedConsentUrl = "";
	/**
	 * Witness name that may be PI
	 */
	protected String witnessName;
	/**
	 * Consent Date, Date on which Consent is Signed
	 */
	protected String consentDate = "";
	/**
	 * Counter for sequence no
	 */
	private int outerCounter = 0;

	//Consent Tracking Module---(Virender Mehta)

	//private String fromSite;
	private String toSite;

	private int counter = 0;
	private String distributionProtocolId;
	private boolean idChange = false;
	private int rowNo = 0;

	private Integer distributionType = Integer.valueOf(Constants.SPECIMEN_DISTRIBUTION_TYPE);

	private Integer distributionBasedOn = Integer.valueOf(Constants.LABEL_BASED_DISTRIBUTION);

	/**
	 * Map to handle values of all Events
	 */
	protected Map values = new HashMap();

	/**
	 * @return DISTRIBUTION_FORM_ID return distribution form id
	 */
	@Override
	public int getFormId()
	{
		return Constants.DISTRIBUTION_FORM_ID;
	}

	/**
	 * @param abstractDomain An AbstractDomainObject object
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		//super.setAllValues(abstractDomain);

		logger.debug("setAllValues of DistributionForm");

		final Distribution distObj = (Distribution) abstractDomain;

		this.comments = CommonUtilities.toString(distObj.getComment());
		this.setId(distObj.getId().longValue());

		final Calendar calender = Calendar.getInstance();
		if (distObj.getTimestamp() != null)
		{
			calender.setTime(distObj.getTimestamp());
			this.timeInHours = CommonUtilities.toString(Integer.toString(calender
					.get(Calendar.HOUR_OF_DAY)));
			this.timeInMinutes = CommonUtilities.toString(Integer.toString(calender
					.get(Calendar.MINUTE)));
			this.dateOfEvent = CommonUtilities.parseDateToString(distObj.getTimestamp(),
					CommonServiceLocator.getInstance().getDatePattern());
		}
		this.userId = distObj.getDistributedBy().getId().longValue();

		/*if (distributionObject.getSpecimen() != null)
		{
			specimenId = distributionObject.getSpecimen().getId().longValue();
		}*/

		this.distributionProtocolId = String.valueOf(distObj.getDistributionProtocol().getId());
		this.toSite = String.valueOf(distObj.getToSite().getId());
		this.setActivityStatus(CommonUtilities.toString(distObj.getActivityStatus()));
		logger.debug("this.activityStatus " + this.getActivityStatus());

		if (distObj.getDistributedItemCollection()!=null && distObj.getDistributedItemCollection().size() != 0)
		{
			final Iterator<DistributedItem> itr = distObj.getDistributedItemCollection().iterator();
			while (itr.hasNext())
			{
				final DistributedItem distributedItem = (DistributedItem) itr.next();
				if (distributedItem.getSpecimen() == null)
				{
					this.distributionType = Integer
							.valueOf(Constants.SPECIMEN_ARRAY_DISTRIBUTION_TYPE);
					//populateMapForArray(distributionObject.getSpecimenArrayCollection());
				}
				else
				{
					this.distributionType = Integer.valueOf(Constants.SPECIMEN_DISTRIBUTION_TYPE);
					this.populateMapForSpecimen(distObj.getDistributedItemCollection());
				}

			}

		}

		logger.debug("Display Map Values" + this.values);

		//At least one row should be displayed in ADD MORE therefore
		/*		if(counter == 0)
		 counter = 1;
		 */}

	/**
	 * Populates map for Array
	 * @param specimenArrayCollection Collection of Specimen Array
	 */
	//	private void populateMapForArray(Collection specimenArrayCollection)
	//	{
	//		if (specimenArrayCollection != null)
	//		{
	//			values = new HashMap();
	//			Iterator it = specimenArrayCollection.iterator();
	//			int i = 1;
	//			while (it.hasNext())
	//			{
	//
	//				String key1 = "SpecimenArray:" + i + "_id";
	//				String key2 = "DistributedItem:" + i + "_Specimen_barcode";
	//				String key3 = "DistributedItem:" + i + "_Specimen_label";
	//
	//				String key4 = "DistributedItem:" + i + "_quantity";
	//
	//				SpecimenArray array = (SpecimenArray) it.next();
	//				values.put(key1, array.getId().toString());
	//				values.put(key2, array.getBarcode());
	//				values.put(key3, array.getName());
	//				values.put(key4, "1");
	//				i++;
	//			}
	//			counter = specimenArrayCollection.size();
	//		}
	//	}
	/**
	 * Populates Map for specimen
	 * @param distItemCol Collection of distributed items
	 */
	private void populateMapForSpecimen(final Collection distItemCol)
	{
		if (distItemCol != null)
		{
			this.values = new HashMap();

			final Iterator itr = distItemCol.iterator();
			int cnt = 1;

			final String DIST_ITEM = "DistributedItem:";
			while (itr.hasNext())
			{
				final DistributedItem dItem = (DistributedItem) itr.next();
				final Specimen specimen = dItem.getSpecimen();
				if(specimen != null)
				{
					final String key1 = DIST_ITEM + cnt + "_id";
					final String key2 = DIST_ITEM + cnt + "_Specimen_id";
					final String key3 = DIST_ITEM + cnt + "_quantity";
					final String key9 = DIST_ITEM + cnt + "_availableQty";
					final String key10 = DIST_ITEM + cnt + "_previousQuantity";
					final String key12 = DIST_ITEM + cnt + "_Specimen_barcode";
					final String key13 = DIST_ITEM + cnt + "_Specimen_label";

					final Double quantity = dItem.getQuantity();
				//dItem.setPreviousQty(quantity);

					this.values.put(key1, CommonUtilities.toString(dItem.getId()));
					this.values.put(key2, CommonUtilities.toString(specimen.getId()));
					this.values.put(key3, quantity);
					this.values.put(key9, this.getAvailableQty(specimen));
					this.values.put(key10, quantity);
					this.values.put(key12, specimen.getBarcode());
					this.values.put(key13, specimen.getLabel());
					cnt++;
				}
			}
			this.counter = distItemCol.size();
		}
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		//resolved bug# 4352
		//ActionErrors errors = super.validate(mapping, request);
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		logger.debug("Inside validate function");
		try
		{
			MultipleSpecimenValidationUtil.validateDate(errors, validator, this.userId,
					this.dateOfEvent, this.timeInHours, this.timeInMinutes);
			if (this.specimenId == -1L)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						"Specimen Id"));
			}
			// Mandar 10-apr-06 : bugid :353
			// Error messages should be in the same sequence as the sequence of fields on the page.
			if (!validator.isValidOption(this.distributionProtocolId))
			{
				logger.debug("dist prot");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("distribution.protocol")));
			}
			if (!validator.isValidOption("" + this.userId))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
						ApplicationProperties.getValue("distribution.distributedBy")));
			}

			//  date validation according to bug id  722 and 730
			final String errorKey = validator.validateDate(this.dateOfEvent, true);
			if (errorKey.trim().length() > 0)
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorKey,
						ApplicationProperties.getValue("eventparameters.dateofevent")));
			}
			if (Validator.isEmpty(this.toSite) || this.toSite.equalsIgnoreCase("undefined"))
			{
				logger.debug("to site");
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.invalid",
						ApplicationProperties.getValue("distribution.toSite")));
			}

			//Validations for Add-More Block
			if (this.values.keySet().isEmpty())
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.one.item.required",
						ApplicationProperties.getValue("distribution.distributedItem")));
			}

			final Iterator itr = this.values.keySet().iterator();
			while (itr.hasNext())
			{
				final String key = (String) itr.next();
				final String value = (String) this.values.get(key);

				if (key.indexOf("Specimen_id") != -1 && !validator.isValidOption(value))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("itemrecord.specimenId")));
				}

				if (key.indexOf("_quantity") != -1)
				{
					if ((validator.isEmpty(value)))
					{
						logger.debug("Quantity empty**************");
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.item.required", ApplicationProperties
										.getValue("itemrecord.quantity")));
					}
					else if (!validator.isDouble(value))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties.getValue("itemrecord.quantity")));
					}
				}

				//}  if  quantity
			}
		}
		catch (final Exception excp)
		{
			DistributionForm.logger.error(excp.getMessage(),excp);
			excp.printStackTrace() ;
		}

		return errors;
	}

	/**
	 * @return Returns the specimenId.
	 */
	public long getSpecimenId()
	{
		return this.specimenId;
	}

	/**
	 * @param specimenId The specimenId to set.
	 */
	public void setSpecimenId(final long specimenId)
	{
		this.specimenId = specimenId;
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments()
	{
		return this.comments;
	}

	/**
	 * @param comments The comments to set.
	 */
	public void setComments(final String comments)
	{
		this.comments = comments;
	}

	/**
	 * @return Returns the dateOfEvent.
	 */
	public String getDateOfEvent()
	{
		return this.dateOfEvent;
	}

	/**
	 * @param dateOfEvent The dateOfEvent to set.
	 */
	public void setDateOfEvent(final String dateOfEvent)
	{
		this.dateOfEvent = dateOfEvent;
	}

	/**
	 * @return Returns the timeInHours.
	 */
	public String getTimeInHours()
	{
		return this.timeInHours;
	}

	/**
	 * @param timeInHours The timeInHours to set.
	 */
	public void setTimeInHours(final String timeInHours)
	{
		this.timeInHours = timeInHours;
	}

	/**
	 * @return Returns the timeInMinutes.
	 */
	public String getTimeInMinutes()
	{
		return this.timeInMinutes;
	}

	/**
	 * @param timeInMinutes The timeInMinutes to set.
	 */
	public void setTimeInMinutes(final String timeInMinutes)
	{
		this.timeInMinutes = timeInMinutes;
	}

	/**
	 * @return Returns the userId.
	 */
	public long getUserId()
	{
		return this.userId;
	}

	/**
	 * @param userId The userId to set.
	 */
	public void setUserId(final long userId)
	{
		this.userId = userId;
	}

	/**
	 * @return Returns the distributionProtocolId.
	 */
	public String getDistributionProtocolId()
	{
		return this.distributionProtocolId;
	}

	/**
	 * @param distributionProtocolId The distributionProtocolId to set.
	 */
	public void setDistributionProtocolId(final String distributionProtocolId)
	{
		this.distributionProtocolId = distributionProtocolId;
	}

	//	/**
	//	 * @return fromSite
	//	 */
	//	public String getFromSite() {
	//		return fromSite;
	//	}
	//
	//	/**
	//	 * @param fromSite
	//	 */
	//	public void setFromSite(String fromSite) {
	//		this.fromSite = fromSite;
	//	}

	/**
	 * @return counter
	 */
	public int getCounter()
	{
		return this.counter;
	}

	/**
	 * @param counter The counter to set.
	 */
	public void setCounter(final int counter)
	{
		this.counter = counter;
	}

	/**
	 * @return toSite get To site
	 */
	public String getToSite()
	{
		return this.toSite;
	}

	/**
	 * @param toSite Set site
	 */
	public void setToSite(final String toSite)
	{
		this.toSite = toSite;
	}

	/**
	 * Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	public void setValue(final String key, final Object value)
	{
		if (this.isMutable())
		{
			this.values.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 *
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(final String key)
	{
		return this.values.get(key);
	}

	/**
	 * @param values The values to set.
	 */
	public void setValues(final Map values)
	{
		this.values = values;
	}

	/**
	 * @return values
	 */
	public Map getValues()
	{
		return this.values;
	}

	/**
	 * Resets the values of all the fields.
	 * This method defined in ActionForm is overridden in this class.
	 */
	@Override
	protected void reset()
	{
		//        super.reset();
		//        this.distributionProtocolId = null;
		//        this.fromSite = null;
		//        this.toSite = null;
		//        this.counter =1;

	}

	/**
	 * @return Returns the idChange.
	 */
	public boolean isIdChange()
	{
		return this.idChange;
	}

	/**
	 * @param idChange The idChange to set.
	 */
	public void setIdChange(final boolean idChange)
	{
		this.idChange = idChange;
	}

	/**
	 * @return rowNo Returns the rowNo.
	 */
	public int getRowNo()
	{
		return this.rowNo;
	}

	/**
	 * @param rowNo The rowNo to set.
	 */
	public void setRowNo(final int rowNo)
	{
		this.rowNo = rowNo;
	}

	/*
	 * Unused code so commented ---- Ashwin Gupta
	 *
	 private static String getUnitSpan(Specimen specimen)
	 {

	 if(specimen instanceof TissueSpecimen)
	 {
	 return Constants.UNIT_GM;

	 }
	 else if(specimen instanceof CellSpecimen)
	 {
	 return Constants.UNIT_CC;

	 }
	 else if(specimen instanceof MolecularSpecimen)
	 {
	 return Constants.UNIT_MG;

	 }
	 else if(specimen instanceof FluidSpecimen)
	 {
	 return Constants.UNIT_ML;
	 }
	 return null;
	 }
	 */

	/**
	 * This method returns UnitSpan for Specimen
	 */
	/*private static String getDomainObjectUnitSpan(Specimen specimen)
	 {

	 if(specimen instanceof TissueSpecimen)
	 {
	 return Constants.UNIT_GM;

	 }
	 else if(specimen instanceof CellSpecimen)
	 {
	 return Constants.UNIT_CC;

	 }
	 else if(specimen instanceof MolecularSpecimen)
	 {
	 return Constants.UNIT_MG;

	 }
	 else if(specimen instanceof FluidSpecimen)
	 {
	 return Constants.UNIT_ML;
	 }
	 return null;
	 }
	 */
	/**
	 *@param specimen Specimen class instance
	 *@return availableQuantity Quantity remaining
	 */
	public Object getAvailableQty(final Specimen specimen)
	{
		//Retrieve the Available quantity for the particular specimen
		/* Aniruddha : 16/06/2006 -- TO BE DELETED --
		 * if(specimen instanceof TissueSpecimen)
		 {
		 TissueSpecimen tissueSpecimen = (TissueSpecimen) specimen;
		 logger.debug("tissueSpecimenAvailableQuantityInGram "+tissueSpecimen.getAvailableQuantityInGram());
		 return tissueSpecimen.getAvailableQuantityInGram();

		 }
		 else if(specimen instanceof CellSpecimen)
		 {
		 CellSpecimen cellSpecimen = (CellSpecimen) specimen;
		 return cellSpecimen.getAvailableQuantityInCellCount();

		 }
		 else if(specimen instanceof MolecularSpecimen)
		 {
		 MolecularSpecimen molecularSpecimen = (MolecularSpecimen) specimen;
		 return molecularSpecimen.getAvailableQuantityInMicrogram();

		 }
		 else if(specimen instanceof FluidSpecimen)
		 {
		 FluidSpecimen fluidSpecimen = (FluidSpecimen) specimen;
		 return fluidSpecimen.getAvailableQuantityInMilliliter();
		 }*/

		//Aniruddha : NEEDS TO TAKE CARE OFF CALLING METHOD
		return specimen.getAvailableQuantity();

		//return null;
	}

	/**
	 * This method returns AvailableQunatity for Specimen
	 * @param specimen Base class specimen
	 * @return specimen.getAvailableQuantity().getValue()
	 */
	public Object getDomainObjectAvailableQty(final Specimen specimen)
	{
		return specimen.getAvailableQuantity();
	}

	/**
	 * This method returns ClassName for Specimen
	 * @param specimen Base class specimen
	 */
	public final String getDomainObjectClassName(final Specimen specimen)
	{
		String className = null;

		if (specimen instanceof CellSpecimen)
		{
			className = "Cell";
		}
		else if (specimen instanceof MolecularSpecimen)
		{
			className = "Molecular";
		}
		else if (specimen instanceof FluidSpecimen)
		{
			className = "Fluid";
		}
		else if (specimen instanceof TissueSpecimen)
		{
			className = "Tissue";
		}

		return className;
	}

	/**
	 * This method sets Identifier of Objects inserted by AddNew activity in Form-Bean which initialized AddNew action
	 * @param addNewFor - FormBean ID of the object inserted
	 *  @param addObjId - Identifier of the Object inserted
	 */
	@Override
	public void setAddNewObjectIdentifier(final String addNewFor, final Long addObjId)
	{
		if ("distributionProtocolId".equals(addNewFor))
		{
			this.setDistributionProtocolId(addObjId.toString());
		}
		else if ("userId".equals(addNewFor))
		{
			this.setUserId(addObjId.longValue());
		}
		else if ("toSite".equals(addNewFor))
		{
			this.setToSite(addObjId.toString());
		}
	}

	/**
	 * @return distributionBasedOn Distribution Based On label or barcode
	 */
	public Integer getDistributionBasedOn()
	{
		return this.distributionBasedOn;
	}

	/**
	 * @param distributionBasedOn Distribution Based On label or barcode
	 */
	public void setDistributionBasedOn(Integer distributionBasedOn)
	{
		this.distributionBasedOn = distributionBasedOn;
	}

	/**
	 * @return distributionType Type of Distribution
	 */
	public Integer getDistributionType()
	{
		return this.distributionType;
	}

	/**
	 *
	 * @param distributionType Type of Distribution
	 */
	public void setDistributionType(final Integer distributionType)
	{
		this.distributionType = distributionType;
	}

	//Consent Tracking Module---(Virender Mehta)
	/**
	 * For Sequence no
	 * @return outerCounter
	 */
	public int getOuterCounter()
	{
		return this.outerCounter;
	}

	/**
	 * For Sequence no
	 * @param outerCounter For Sequence no
	 */
	public void setOuterCounter(final int outerCounter)
	{
		this.outerCounter = outerCounter;
	}

	/**
	 * @return consentDate The Date on Which Consent is Signed
	 */
	public String getConsentDate()
	{
		return this.consentDate;
	}

	/**
	 * @param consentDate The Date on Which Consent is Signed
	 */
	public void setConsentDate(final String consentDate)
	{
		this.consentDate = consentDate;
	}

	/**
	 *@return consentTierCounter  This will keep track of count of Consent Tier
	 */
	public int getConsentTierCounter()
	{
		return this.consentTierCounter;
	}

	/**
	 *@param consentTierCounter  This will keep track of count of Consent Tier
	 */
	public void setConsentTierCounter(final int cTCounter)
	{
		this.consentTierCounter = cTCounter;
	}

	/**
	 * @return signedConsentUrl The reference to the electric signed document(eg PDF file)
	 */
	public String getSignedConsentUrl()
	{
		return this.signedConsentUrl;
	}

	/**
	 * @param signedConsentUrl The reference to the electric signed document(eg PDF file)
	 */
	public void setSignedConsentUrl(final String signedConsentUrl)
	{
		this.signedConsentUrl = signedConsentUrl;
	}

	/**
	 * @return witnessName The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */
	public String getWitnessName()
	{
		return this.witnessName;
	}

	/**
	 * @param witnessName The name of the witness to the consent Signature(PI or coordinator of the Collection Protocol)
	 */
	public void setWitnessName(final String witnessName)
	{
		this.witnessName = witnessName;
	}

	/**
	 * @return consentResponseForDistributionValues  The comments associated with Response at Distribution level
	 */
	public Map getConsentResponseForDistributionValues()
	{
		return this.consentResponseForDistributionValues;
	}

	/**
	 * @param consentResponseForDistributionValues  The comments associated with Response at Distribution level
	 */
	public void setConsentResponseForDistributionValues(final Map cRFDV)
	{
		this.consentResponseForDistributionValues = cRFDV;
	}

	/**
	 * @param key Key prepared for saving data.
	 * @return consentResponseForDistributionValues.get(key)
	 */
	public void setConsentResponseForDistributionValue(final String key, final Object value)
	{
		if (this.isMutable())
		{
			this.consentResponseForDistributionValues.put(key, value);
		}
	}

	/**
	 * @param key Key prepared for saving data.
	 * @return consentResponseForSpecimenValues.get(key)
	 */
	public Object getConsentResponseForDistributionValue(final String key)
	{
		return this.consentResponseForDistributionValues.get(key);
	}

	/**
	 * @return values in map consentResponseForDistributionValues
	 */
	public Collection getAllConsentResponseForDistributionValue()
	{
		return this.consentResponseForDistributionValues.values();
	}

	/**
	 * This function creates Array of String of keys and add them into the consentTiersList.
	 * @return consentTiersList
	 */
	public Collection getConsentTiers()
	{
		final Collection consentTiersList = new ArrayList();
		String[] strArray = null;
		final int noOfConsents = this.getConsentTierCounter();
		final String CRDV = "consentResponseForDistributionValues(ConsentBean:";
		for (int counter = 0; counter < noOfConsents; counter++)
		{
			strArray = new String[6];
			strArray[0] = CRDV + counter + "_consentTierID)";
			strArray[1] = CRDV + counter + "_statement)";
			strArray[2] = CRDV + counter + "_participantResponse)";
			strArray[3] = CRDV + counter + "_participantResponseID)";
			strArray[4] = CRDV + counter + "_specimenLevelResponse)";
			strArray[5] = CRDV + counter + "_specimenLevelResponseID)";
			consentTiersList.add(strArray);
		}
		return consentTiersList;
	}

	/**
	 * This function returns empty string,we don't need this method but its definition is compulsory because this
	 * function is declared in the implemented interface
	 * @return ""
	 */
	public String getConsentTierMap()
	{
		return "";
	}
	//Consent Tracking Module---(Virender Mehta)

}