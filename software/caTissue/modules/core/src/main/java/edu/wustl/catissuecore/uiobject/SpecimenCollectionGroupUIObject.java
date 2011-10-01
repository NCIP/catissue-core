package edu.wustl.catissuecore.uiobject;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.UIObject;

public class SpecimenCollectionGroupUIObject implements UIObject {

	/**
	 * To apply changes to specimen based on consent status changes. Default
	 * Apply none to allow normal behavior.
	 */
	private String applyChangesTo=Constants.APPLY_NONE;

	/**
	 * Condition indicating whether to propagate collection events and received
	 * events to specimens under this scg.
	 */
	private boolean applyEventsToSpecimens;

	/**
	 * To perform operation based on withdraw button clicked.
	 * Default No Action to allow normal behavior.
	 */
	protected String consentWithdrawalOption=Constants.WITHDRAW_RESPONSE_NOACTION;

	/**
	 * @return the consentWithdrawalOption
	 */
	public String getConsentWithdrawalOption() {
		return consentWithdrawalOption;
	}

	/**
	 * @param consentWithdrawalOption the consentWithdrawalOption to set
	 */
	public void setConsentWithdrawalOption(String consentWithdrawalOption) {
		this.consentWithdrawalOption = consentWithdrawalOption;
	}

	/**
	 * isCPBasedSpecimenEntryChecked.
	 */
	private Boolean isCPBasedSpecimenEntryChecked=Boolean.TRUE;

	/**
	 * isToInsertAnticipatorySpecimens added for new migration tool.
	 */
	//private Boolean isToInsertAnticipatorySpecimens=Boolean.TRUE;

	/**
	 * To apply changes to specimen based on consent status changes. Default
	 * empty.
	 *//*
	private String stringOfResponseKeys="";
*/
	/**
	 * Get IsToInsertAnticipatorySpecimens value.
	 *
	 * @return the isToInsertAnticipatorySpecimens
	 */
//	public Boolean getIsToInsertAnticipatorySpecimens() {
//		return isToInsertAnticipatorySpecimens;
//	}
//
//	/**
//	 * Set IsToInsertAnticipatorySpecimens value.
//	 *
//	 * @param isToInsertAnticipatorySpecimens
//	 *            the isToInsertAnticipatorySpecimens to set
//	 */
//	public void setIsToInsertAnticipatorySpecimens(
//			Boolean isToInsertAnticipatorySpecimens) {
//		this.isToInsertAnticipatorySpecimens = isToInsertAnticipatorySpecimens;
//	}

	/**
	 * @return the isCPBasedSpecimenEntryChecked
	 */
	public Boolean getIsCPBasedSpecimenEntryChecked() {
		return this.isCPBasedSpecimenEntryChecked;
	}

	/**
	 * @param isCPBasedSpecimenEntryChecked
	 *            the isCPBasedSpecimenEntryChecked to set
	 */
	public void setIsCPBasedSpecimenEntryChecked(
			Boolean isCPBasedSpecimenEntryChecked) {
		this.isCPBasedSpecimenEntryChecked = isCPBasedSpecimenEntryChecked;
	}

	/**
	 * @return the applyEventsToSpecimens
	 */
	public boolean isApplyEventsToSpecimens() {
		return this.applyEventsToSpecimens;
	}

	/**
	 * @param applyEventsToSpecimens
	 *            the applyEventsToSpecimens to set
	 */
	public void setApplyEventsToSpecimens(boolean applyEventsToSpecimens) {
		this.applyEventsToSpecimens = applyEventsToSpecimens;
	}

	/**
	 * Get Apply Changes To.
	 *
	 * @return String type.
	 */
	public String getApplyChangesTo() {
		return this.applyChangesTo;
	}

	/**
	 * Set Apply Changes To.
	 *
	 * @param applyChangesTo
	 *            of String type.
	 */
	public void setApplyChangesTo(String applyChangesTo) {
		this.applyChangesTo = applyChangesTo;
	}

	/**
	 * Get String Of ResponseKeys.
	 *
	 * @return of String type.
	 */
	/*public String getStringOfResponseKeys() {
		return this.stringOfResponseKeys;
	}*/

	/**
	 * Set String Of ResponseKeys.
	 *
	 * @param stringOfResponseKeys
	 *            of String type.
	 */
	/*public void setStringOfResponseKeys(String stringOfResponseKeys) {
		this.stringOfResponseKeys = stringOfResponseKeys;
	}*/
}
