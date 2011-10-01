package edu.wustl.catissuecore.domain;

import java.util.Collection;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;

import java.io.Serializable;
/**
	*
	**/

public class CollectionProtocolRegistration extends AbstractDomainObject implements Serializable, IActivityStatus
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;


	/**
	* Defines whether this CollectionProtocolRegistration record can be queried Active) or not queried (Inactive) by any actor.
	**/

	private String activityStatus;
	/**
	* Retrieves the value of the activityStatus attribute
	* @return activityStatus
	**/

	public String getActivityStatus(){
		return activityStatus;
	}

	/**
	* Sets the value of activityStatus attribute
	**/

	public void setActivityStatus(String activityStatus){
		this.activityStatus = activityStatus;
	}

	/**
	* barcode attribute added for Suite 1.1.
	**/

	private String barcode;
	/**
	* Retrieves the value of the barcode attribute
	* @return barcode
	**/

	public String getBarcode(){
		return barcode;
	}

	/**
	* Sets the value of barcode attribute
	**/

	public void setBarcode(String barcode){
		this.barcode = barcode;
	}

	/**
	* The date on which consent document was signed.
	**/

	private java.util.Date consentSignatureDate;
	/**
	* Retrieves the value of the consentSignatureDate attribute
	* @return consentSignatureDate
	**/

	public java.util.Date getConsentSignatureDate(){
		return consentSignatureDate;
	}

	/**
	* Sets the value of consentSignatureDate attribute
	**/

	public void setConsentSignatureDate(java.util.Date consentSignatureDate){
		this.consentSignatureDate = consentSignatureDate;
	}

	/**
	* System generated unique id.
	**/

	private Long id;
	/**
	* Retrieves the value of the id attribute
	* @return id
	**/

	public Long getId(){
		return id;
	}

	/**
	* Sets the value of id attribute
	**/

	public void setId(Long id){
		this.id = id;
	}

	/**
	* offset.
	**/

	private Integer offset;
	/**
	* Retrieves the value of the offset attribute
	* @return offset
	**/

	public Integer getOffset(){
		return offset;
	}

	/**
	* Sets the value of offset attribute
	**/

	public void setOffset(Integer offset){
		this.offset = offset;
	}

	/**
	* A unique number given by a User to a Participant registered to a Collection Protocol.
	**/

	private String protocolParticipantIdentifier;
	/**
	* Retrieves the value of the protocolParticipantIdentifier attribute
	* @return protocolParticipantIdentifier
	**/

	public String getProtocolParticipantIdentifier(){
		return protocolParticipantIdentifier;
	}

	/**
	* Sets the value of protocolParticipantIdentifier attribute
	**/

	public void setProtocolParticipantIdentifier(String protocolParticipantIdentifier){
		this.protocolParticipantIdentifier = protocolParticipantIdentifier;
	}

	/**
	* Date on which the Participant is registered to the Collection Protocol.
	**/

	private java.util.Date registrationDate;
	/**
	* Retrieves the value of the registrationDate attribute
	* @return registrationDate
	**/

	public java.util.Date getRegistrationDate(){
		return registrationDate;
	}

	/**
	* Sets the value of registrationDate attribute
	**/

	public void setRegistrationDate(java.util.Date registrationDate){
		this.registrationDate = registrationDate;
	}

	/**
	* The signed consent document URL.
	**/

	private String signedConsentDocumentURL;
	/**
	* Retrieves the value of the signedConsentDocumentURL attribute
	* @return signedConsentDocumentURL
	**/

	public String getSignedConsentDocumentURL(){
		return signedConsentDocumentURL;
	}

	/**
	* Sets the value of signedConsentDocumentURL attribute
	**/

	public void setSignedConsentDocumentURL(String signedConsentDocumentURL){
		this.signedConsentDocumentURL = signedConsentDocumentURL;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.SpecimenCollectionGroup object's collection
	**/

	private Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection;
	/**
	* Retrieves the value of the specimenCollectionGroupCollection attribute
	* @return specimenCollectionGroupCollection
	**/

	public Collection<SpecimenCollectionGroup> getSpecimenCollectionGroupCollection(){
		return specimenCollectionGroupCollection;
	}

	/**
	* Sets the value of specimenCollectionGroupCollection attribute
	**/

	public void setSpecimenCollectionGroupCollection(Collection<SpecimenCollectionGroup> specimenCollectionGroupCollection){
		this.specimenCollectionGroupCollection = specimenCollectionGroupCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.User object
	**/

	private User consentWitness;
	/**
	* Retrieves the value of the consentWitness attribute
	* @return consentWitness
	**/

	public User getConsentWitness(){
		return consentWitness;
	}
	/**
	* Sets the value of consentWitness attribute
	**/

	public void setConsentWitness(User consentWitness){
		this.consentWitness = consentWitness;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.CollectionProtocol object
	**/

	private CollectionProtocol collectionProtocol;
	/**
	* Retrieves the value of the collectionProtocol attribute
	* @return collectionProtocol
	**/

	public CollectionProtocol getCollectionProtocol(){
		return collectionProtocol;
	}
	/**
	* Sets the value of collectionProtocol attribute
	**/

	public void setCollectionProtocol(CollectionProtocol collectionProtocol){
		this.collectionProtocol = collectionProtocol;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.Participant object
	**/

	private Participant participant;
	/**
	* Retrieves the value of the participant attribute
	* @return participant
	**/

	public Participant getParticipant(){
		return participant;
	}
	/**
	* Sets the value of participant attribute
	**/

	public void setParticipant(Participant participant){
		this.participant = participant;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.ConsentTierResponse object's collection
	**/

	private Collection<ConsentTierResponse> consentTierResponseCollection;
	/**
	* Retrieves the value of the consentTierResponseCollection attribute
	* @return consentTierResponseCollection
	**/

	public Collection<ConsentTierResponse> getConsentTierResponseCollection(){
		return consentTierResponseCollection;
	}

	/**
	* Sets the value of consentTierResponseCollection attribute
	**/

	public void setConsentTierResponseCollection(Collection<ConsentTierResponse> consentTierResponseCollection){
		this.consentTierResponseCollection = consentTierResponseCollection;
	}

	/*String isConsentAvailable;

	public String getIsConsentAvailable()
	{
		return isConsentAvailable;
	}


	public void setIsConsentAvailable(String isConsentAvailable)
	{
		this.isConsentAvailable = isConsentAvailable;
	}*/

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof CollectionProtocolRegistration)
		{
			CollectionProtocolRegistration c =(CollectionProtocolRegistration)obj;
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}
	
	private String gridId;
	/**
	 * @return the gridId
	 */
	public String getGridId() {
		return gridId;
	}

	/**
	 * @param gridId the gridId to set
	 */
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CollectionProtocolRegistration [activityStatus="
				+ activityStatus + ", barcode=" + barcode
				+ ", consentSignatureDate=" + consentSignatureDate + ", id="
				+ id + ", offset=" + offset
				+ ", protocolParticipantIdentifier="
				+ protocolParticipantIdentifier + ", registrationDate="
				+ registrationDate + ", signedConsentDocumentURL="
				+ signedConsentDocumentURL + ", gridId=" + gridId + "]";
	}
	
	



	/**
	 * isToInsertAnticipatorySCGs Added for the new migration for
	 * not creating the anticipated SCG's.
	 */
	protected Boolean isToInsertAnticipatorySCGs = true;
	/**
	 * Get IsToInsertAnticipatorySCGs value.
	 * @return the isToInsertAnticipatorySCGs
	 */
	public Boolean getIsToInsertAnticipatorySCGs()
	{
		return isToInsertAnticipatorySCGs;
	}
	/**
	 * Set IsToInsertAnticipatorySCGs value.
	 * @param isToInsertAnticipatorySCGs the isToInsertAnticipatorySCGs to set
	 */
	public void setIsToInsertAnticipatorySCGs(Boolean isToInsertAnticipatorySCGs)
	{
		this.isToInsertAnticipatorySCGs = isToInsertAnticipatorySCGs;
	}

}