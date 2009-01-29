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

public class ConsentResponseBean implements Serializable{
	
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
	protected String consentWithdrawalOption=Constants.WITHDRAW_RESPONSE_NOACTION;
	
	/**
	 * Consent response
	 */
	protected Collection <ConsentBean> consentResponse;
	
	public ConsentResponseBean(long collectionProtocolID, String signedConsentUrl , long witnessId, String consentDate , Collection <ConsentBean> consentResponse, String consentWithdrawalOption){
		this.collectionProtocolID = collectionProtocolID;
		this.signedConsentUrl = signedConsentUrl;
		this.witnessId = witnessId;
		this.consentDate = consentDate;
		this.consentResponse = consentResponse;
		this.consentWithdrawalOption = consentWithdrawalOption;
		
	}
	
	public long getCollectionProtocolID() {
		return collectionProtocolID;
	}
	public void setCollectionProtocolID(long collectionProtocolID) {
		this.collectionProtocolID = collectionProtocolID;
	}
	public String getSignedConsentUrl() {
		return signedConsentUrl;
	}
	public void setSignedConsentUrl(String signedConsentUrl) {
		this.signedConsentUrl = signedConsentUrl;
	}
	public long getWitnessId() {
		return witnessId;
	}
	public void setWitnessId(long witnessId) {
		this.witnessId = witnessId;
	}
	public String getConsentDate() {
		return consentDate;
	}
	public void setConsentDate(String consentDate) {
		this.consentDate = consentDate;
	}
	public Collection <ConsentBean> getConsentResponse() {
		return consentResponse;
	}
	public void setConsentResponse(Collection <ConsentBean> consentResponse) {
		this.consentResponse = consentResponse;
	}

	public String getConsentWithdrawalOption() {
		return consentWithdrawalOption;
	}

	public void setConsentWithdrawalOption(String consentWithdrawalOption) {
		this.consentWithdrawalOption = consentWithdrawalOption;
	}
}
