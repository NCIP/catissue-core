package edu.wustl.catissuecore.uiobject;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.UIObject;

public class CollectionProtocolRegistrationUIObject implements UIObject {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * To perform operation based on withdraw button clicked.
	 * Default No Action to allow normal behavior.
	 */
	protected String consentWithdrawalOption=Constants.WITHDRAW_RESPONSE_NOACTION;

	/**
	 * isConsentAvailable.
	 */
	protected String isConsentAvailable;

	/**
	 * isToInsertAnticipatorySCGs Added for the new migration for
	 * not creating the anticipated SCG's.
	 */
//	protected Boolean isToInsertAnticipatorySCGs=Boolean.TRUE;
//
//	/**
//	 * Get IsToInsertAnticipatorySCGs value.
//	 * @return the isToInsertAnticipatorySCGs
//	 */
//	public Boolean getIsToInsertAnticipatorySCGs()
//	{
//		return isToInsertAnticipatorySCGs;
//	}
//	/**
//	 * Set IsToInsertAnticipatorySCGs value.
//	 * @param isToInsertAnticipatorySCGs the isToInsertAnticipatorySCGs to set
//	 */
//	public void setIsToInsertAnticipatorySCGs(Boolean isToInsertAnticipatorySCGs)
//	{
//		this.isToInsertAnticipatorySCGs = isToInsertAnticipatorySCGs;
//	}

	/**
	 * Get the Consent Withdrawal Option.
	 * @return String type.
	 */
	public String getConsentWithdrawalOption()
	{
		return this.consentWithdrawalOption;
	}

	/**
	 * Set the Consent Withdrawal Option.
	 * @param consentWithdrawalOption of String type.
	 */
	public void setConsentWithdrawalOption(String consentWithdrawalOption)
	{
		this.consentWithdrawalOption = consentWithdrawalOption;
	}

	/**
	 * Get the available consent.
	 * @return String type.
	 */
	public String getIsConsentAvailable()
	{
		return this.isConsentAvailable;
	}

	/**
	 * Set the available consent.
	 * @param isConsentAvailable of String type.
	 */
	public void setIsConsentAvailable(String isConsentAvailable)
	{
		this.isConsentAvailable = isConsentAvailable;
	}
}
