/**
 * <p>Title: ConsentResponseForm Class>
 * <p>Description: ConsentResponseForm Class is used to encapsulate all the request parameters passed 
 * from Collection Protocol Registration's response link on Participant Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Abhishek Mehta
 * @version 1.00
 * Created on Sept 5, 2007
 */

package edu.wustl.catissuecore.actionForm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author renuka_bajpai
 *
 */
public class ConsentResponseForm extends AbstractActionForm
		implements
			Serializable,
			ConsentTierData
{

	private static final long serialVersionUID = -7258847401432740404L;

	/**
	* collection protocol Id
	*/
	protected long collectionProtocolID;

	/**
	 * Signed Consent URL
	 */
	protected String signedConsentUrl = "";

	/**
	 * Witness name that may be PI
	 */
	protected long witnessId;

	/**
	 * Consent Date, Date on which Consent is Signed
	 */
	protected String consentDate = "";

	protected Map consentResponseValues = new LinkedHashMap();

	private int consentTierCounter = 0;

	/*
	 * To perform operation based on withdraw button clicked.
	 * Default No Action to allow normal behaviour. 
	 */
	protected String withdrawlButtonStatus = Constants.WITHDRAW_RESPONSE_NOACTION;

	public void setAllValues(AbstractDomainObject abstractDomain)
	{

	}

	@Override
	public int getFormId()
	{
		return Constants.CONSENT_FORM_ID;
	}

	@Override
	protected void reset()
	{
		// TODO Auto-generated method stub

	}

	/**
	* @param key Key prepared for saving data.
	* @param value Values correspponding to key
	*/
	public void setConsentResponseValue(String key, Object value)
	{
		if (this.isMutable())
		{
			this.consentResponseValues.put(key, value);
		}
	}

	/**
	 * 
	 * @param key Key prepared for saving data.
	 * @return consentResponseValues
	 */
	public Object getConsentResponseValue(String key)
	{
		return this.consentResponseValues.get(key);
	}

	/**
	 * @return values in map consentResponseValues
	 */
	public Collection getAllConsentResponseValue()
	{
		return this.consentResponseValues.values();
	}

	/**
	 * @return consentResponseValues The reference to the participant Response at CollectionprotocolReg Level
	 */
	public Map getConsentResponseValues()
	{
		return this.consentResponseValues;
	}

	/**
	 * @param consentResponseValues The reference to the participant Response at CollectionprotocolReg Level
	 */
	public void setConsentResponseValues(Map consentResponseValues)
	{
		this.consentResponseValues = consentResponseValues;
	}

	public int getConsentTierCounter()
	{
		return this.consentTierCounter;
	}

	public void setConsentTierCounter(int consentTierCounter)
	{
		this.consentTierCounter = consentTierCounter;
	}

	public long getCollectionProtocolID()
	{
		return this.collectionProtocolID;
	}

	public void setCollectionProtocolID(long collectionProtocolID)
	{
		this.collectionProtocolID = collectionProtocolID;
	}

	public String getSignedConsentUrl()
	{
		return this.signedConsentUrl;
	}

	public void setSignedConsentUrl(String signedConsentUrl)
	{
		this.signedConsentUrl = signedConsentUrl;
	}

	public long getWitnessId()
	{
		return this.witnessId;
	}

	public void setWitnessId(long witnessId)
	{
		this.witnessId = witnessId;
	}

	public String getConsentDate()
	{
		return this.consentDate;
	}

	public void setConsentDate(String consentDate)
	{
		this.consentDate = consentDate;
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
		for (int consentCounter = 0; consentCounter < noOfConsents; consentCounter++)
		{
			strArray = new String[4];
			strArray[0] = "consentResponseValue(ConsentBean:" + consentCounter + "_consentTierID)";
			strArray[1] = "consentResponseValue(ConsentBean:" + consentCounter + "_statement)";
			strArray[2] = "consentResponseValue(ConsentBean:" + consentCounter
					+ "_participantResponse)";
			strArray[3] = "consentResponseValue(ConsentBean:" + consentCounter
					+ "_participantResponseID)";

			consentTiersList.add(strArray);
		}
		return consentTiersList;
	}

	/**
	 * This funtion returns the format of the response Key prepared. 
	 * @return consentResponseValue(ConsentBean:`_participantResponse)
	 */
	public String getConsentTierMap()
	{
		return "consentResponseValue(ConsentBean:`_participantResponse)";
	}

	public String getWithdrawlButtonStatus()
	{
		return this.withdrawlButtonStatus;
	}

	public void setWithdrawlButtonStatus(String withdrawlButtonStatus)
	{
		this.withdrawlButtonStatus = withdrawlButtonStatus;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
