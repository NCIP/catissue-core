package edu.wustl.catissuecore.uiobject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.UIObject;

public class SpecimenUIObject implements UIObject
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parent is changed or not.
	 */
	protected boolean isParentChanged;

	/**
	 * map of aliquot specimen.
	 */
	private Map aliqoutMap = new HashMap();

	/**
	 * After creating aliquot specimen, dispose parent specimen or not.
	 */
	private boolean disposeParentSpecimen;

	/**
	 * To perform operation based on withdraw button clicked.
	 * Default No Action to allow normal flow.
	 */
	private String consentWithdrawalOption=Constants.WITHDRAW_RESPONSE_NOACTION;

	/**
	 * To apply changes to child specimen based on consent status changes.
	 * Default Apply none to allow normal flow.
	 */
	private String applyChangesTo=Constants.APPLY_NONE;

	private String parentSpecimenLabel=null;

	/**
	 * Created on date of specimen.
	 */
	private String createdOn;

	private String parentSpecimenId;

	private String disposeStatus;

	/**
	* The reason for which the specimen is disposed.
	**/

	private String reason;
	/**
	* Retrieves the value of the reason attribute
	* @return reason
	**/

	public String getReason(){
		return reason;
	}

	/**
	* Sets the value of reason attribute
	**/

	public void setReason(String reason){
		this.reason = reason;
	}


	public String getDisposeStatus() {
		return disposeStatus;
	}


	public void setDisposeStatus(String disposeStatus) {
		this.disposeStatus = disposeStatus;
	}


	/**
	 * @return isParentChanged boolean true or false
	 */
	public boolean isParentChanged()
	{
		return this.isParentChanged;
	}

	/**
	 * Is Specimen dispose?
	 */
	protected boolean dispose;

	public boolean isDispose() {
		return dispose;
	}


	public void setDispose(boolean dispose) {
		this.dispose = dispose;
	}

	/** Holds true if the transfer event has occurred. */
	private String transferStatus;

	/**
	* The reason for which the specimen is transfered.
	**/

	private String reasonForTransfer;

	/** transferFromPosition; */

	private String transferFromPosition;

	/** Reference to dimensional position one of the specimen in previous storage container before transfer. */
	protected String fromPositionDimensionOne;

	/** Reference to dimensional position two of the specimen in previous storage container before transfer. */
	protected String fromPositionDimensionTwo;

	/** Reference to dimensional position one of the specimen in new storage container after transfer. */
	protected String positionDimensionOne;

	/** Reference to dimensional position two of the specimen in new storage container after transfer. */
	protected String positionDimensionTwo;

	/** Storage Container to which the transfer is made. */
	protected String storageContainer;

	/** Storage Container from which the transfer is made. */
	protected String fromStorageContainerId;




	/**
	 * @return the fromPositionDimensionOne
	 */
	public String getFromPositionDimensionOne() {
		return fromPositionDimensionOne;
	}

	/**
	 * @param fromPositionDimensionOne the fromPositionDimensionOne to set
	 */
	public void setFromPositionDimensionOne(String fromPositionDimensionOne) {
		this.fromPositionDimensionOne = fromPositionDimensionOne;
	}

	/**
	 * @return the fromPositionDimensionTwo
	 */
	public String getFromPositionDimensionTwo() {
		return fromPositionDimensionTwo;
	}

	/**
	 * @param fromPositionDimensionTwo the fromPositionDimensionTwo to set
	 */
	public void setFromPositionDimensionTwo(String fromPositionDimensionTwo) {
		this.fromPositionDimensionTwo = fromPositionDimensionTwo;
	}

	/**
	 * @return the positionDimensionOne
	 */
	public String getPositionDimensionOne() {
		return positionDimensionOne;
	}

	/**
	 * @param positionDimensionOne the positionDimensionOne to set
	 */
	public void setPositionDimensionOne(String positionDimensionOne) {
		this.positionDimensionOne = positionDimensionOne;
	}

	/**
	 * @return the positionDimensionTwo
	 */
	public String getPositionDimensionTwo() {
		return positionDimensionTwo;
	}

	/**
	 * @param positionDimensionTwo the positionDimensionTwo to set
	 */
	public void setPositionDimensionTwo(String positionDimensionTwo) {
		this.positionDimensionTwo = positionDimensionTwo;
	}

	/**
	 * @return the storageContainer
	 */
	public String getStorageContainer() {
		return storageContainer;
	}

	/**
	 * @param storageContainer the storageContainer to set
	 */
	public void setStorageContainer(String storageContainer) {
		this.storageContainer = storageContainer;
	}

	/**
	 * @return the fromStorageContainerId
	 */
	public String getFromStorageContainerId() {
		return fromStorageContainerId;
	}

	/**
	 * @param fromStorageContainerId the fromStorageContainerId to set
	 */
	public void setFromStorageContainerId(String fromStorageContainerId) {
		this.fromStorageContainerId = fromStorageContainerId;
	}

	/**
	 * @return the transferFromPosition
	 */
	public String getTransferFromPosition() {
		return transferFromPosition;
	}

	/**
	 * @param transferFromPosition the transferFromPosition to set
	 */
	public void setTransferFromPosition(String transferFromPosition) {
		this.transferFromPosition = transferFromPosition;
	}

	/**
	 * @return the transferStatus
	 */
	public String getTransferStatus() {
		return transferStatus;
	}

	/**
	 * @param transferStatus the transferStatus to set
	 */
	public void setTransferStatus(String transferStatus) {
		this.transferStatus = transferStatus;
	}

	/**
	 * @return the reasonForTransfer
	 */
	public String getReasonForTransfer() {
		return reasonForTransfer;
	}

	/**
	 * @param reasonForTransfer the reasonForTransfer to set
	 */
	public void setReasonForTransfer(String reasonForTransfer) {
		this.reasonForTransfer = reasonForTransfer;
	}


	/**
	 * @param isParentChanged boolean true or false
	 */
	public void setParentChanged(boolean isParentChanged)
	{
		this.isParentChanged = isParentChanged;
	}
	/**
	 * Returns the map that contains distinguished fields per aliquots.
	 * @return The map that contains distinguished fields per aliquots.
	 * @see #setAliquotMap(Map)
	 */
	public Map getAliqoutMap()
	{
		return this.aliqoutMap;
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
	 * Get ConsentWithdrawalOption.
	 * @return String.
	 */
	public String getConsentWithdrawalOption()
	{
		return this.consentWithdrawalOption;
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
		return this.applyChangesTo;
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
		return this.disposeParentSpecimen;
	}

	/**
	 * @param disposeParentSpecimen The disposeParentSpecimen to set.
	 */
	public void setDisposeParentSpecimen(boolean disposeParentSpecimen)
	{
		this.disposeParentSpecimen = disposeParentSpecimen;
	}

	public String getParentSpecimenLabel() {
		return parentSpecimenLabel;
	}

	public void setParentSpecimenLabel(String parentSpecimenLabel) {
		this.parentSpecimenLabel = parentSpecimenLabel;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public String getParentSpecimenId() {
		return parentSpecimenId;
	}

	public void setParentSpecimenId(String parentSpecimenId) {
		this.parentSpecimenId = parentSpecimenId;
	}

	private String timeInHours;
	private String timeInMins;
	
	public String getTimeInHours()
	{
		return timeInHours;
	}

	
	public void setTimeInHours(String timeInHours)
	{
		this.timeInHours = timeInHours;
	}

	
	public String getTimeInMins()
	{
		return timeInMins;
	}

	
	public void setTimeInMins(String timeInMins)
	{
		this.timeInMins = timeInMins;
	}

}
