/**
 * <p>Title: ConsentResponseForm Class>
 * <p>Description: This class contains attributes to display on ParticipantConsentTracking.jsp. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Abhishek Mehta
 * @version 1.00
 * Created on Sept 5, 2007
 */

package edu.wustl.catissuecore.bean;

import java.io.Serializable;
import java.util.Collection;

import edu.wustl.catissuecore.util.global.Constants;
/**
 * @author janhavi_hasabnis
 */
public class ConsentResponseBean implements Serializable
{

	private static final long serialVersionUID = 1L;

	/**
	 * collection protocol Id
	 */
	protected long collectionProtocolID;

	/**
	 * Signed Consent URL
	 */
	protected String signedConsentUrl;

	/**
	 * Witness name that may be PI
	 */
	protected long witnessId;

	/**
	 * Consent Date, Date on which Consent is Signed
	 */
	protected String consentDate;

	/**
	 * Consent Withdraw status
	 */
	protected String consentWithdrawalOption = Constants.WITHDRAW_RESPONSE_NOACTION;

	/**
	 * Consent response
	 */
	protected Collection < ConsentBean > consentResponse;
	/**
	 * @param collectionProtocolIDParam - collectionProtocolIDParam
	 * @param signedConsentUrlParam - signedConsentUrlParam
	 * @param witnessIdParam - witnessIdParam
	 * @param consentDateParam - consentDateParam
	 * @param consentResponseParam - consentResponseParam
	 * @param consentWithdrawalOptionParam - consentWithdrawalOptionParam
	 */
	public ConsentResponseBean(long collectionProtocolIDParam, String signedConsentUrlParam, long witnessIdParam,
			String consentDateParam, Collection < ConsentBean > consentResponseParam,
			String consentWithdrawalOptionParam)
	{
		this.collectionProtocolID = collectionProtocolIDParam;
		this.signedConsentUrl = signedConsentUrlParam;
		this.witnessId = witnessIdParam;
		this.consentDate = consentDateParam;
		this.consentResponse = consentResponseParam;
		this.consentWithdrawalOption = consentWithdrawalOptionParam;

	}
	/**
	 * @return - collectionProtocolID
	 */
	public long getCollectionProtocolID()
	{
		return this.collectionProtocolID;
	}
	/**
	 * @param collectionProtocolIDParam - collectionProtocolIDParam
	 */
	public void setCollectionProtocolID(long collectionProtocolIDParam)
	{
		this.collectionProtocolID = collectionProtocolIDParam;
	}
	/**
	 * @return - signedConsentUrl
	 */
	public String getSignedConsentUrl()
	{
		return this.signedConsentUrl;
	}
	/**
	 * @param signedConsentUrlParam - signedConsentUrlParam
	 */
	public void setSignedConsentUrl(String signedConsentUrlParam)
	{
		this.signedConsentUrl = signedConsentUrlParam;
	}
	/**
	 * @return - witnessId
	 */
	public long getWitnessId()
	{
		return this.witnessId;
	}
	/**
	 * @param witnessIdParam - witnessIdParam
	 */
	public void setWitnessId(long witnessIdParam)
	{
		this.witnessId = witnessIdParam;
	}
	/**
	 * @return - consentDate
	 */
	public String getConsentDate()
	{
		return this.consentDate;
	}
	/**
	 * @param consentDateParam - consentDateParam
	 */
	public void setConsentDate(String consentDateParam)
	{
		this.consentDate = consentDateParam;
	}
	/**
	 * @return - consentResponse
	 */
	public Collection < ConsentBean > getConsentResponse()
	{
		return this.consentResponse;
	}
    /**
     * @param consentResponseParam - consentResponseParam
     */
	public void setConsentResponse(Collection < ConsentBean > consentResponseParam)
	{
		this.consentResponse = consentResponseParam;
	}
	/**
	 * @return - consentWithdrawalOption
	 */
	public String getConsentWithdrawalOption()
	{
		return this.consentWithdrawalOption;
	}
	/**
	 * @param consentWithdrawalOptionParam - consentWithdrawalOptionParam
	 */
	public void setConsentWithdrawalOption(String consentWithdrawalOptionParam)
	{
		this.consentWithdrawalOption = consentWithdrawalOptionParam;
	}
}
