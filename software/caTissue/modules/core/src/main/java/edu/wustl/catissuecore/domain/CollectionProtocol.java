package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.common.bizlogic.IActivityStatus;

import java.io.Serializable;
/**
	*
	**/

public class CollectionProtocol extends SpecimenProtocol implements Serializable, IActivityStatus
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;


	/**
	* whether Aliquote in same container.
	**/

	private Boolean aliquotInSameContainer;
	/**
	* Retrieves the value of the aliquotInSameContainer attribute
	* @return aliquotInSameContainer
	**/

	public Boolean getAliquotInSameContainer(){
		return aliquotInSameContainer;
	}

	/**
	* Sets the value of aliquotInSameContainer attribute
	**/

	public void setAliquotInSameContainer(Boolean aliquotInSameContainer){
		this.aliquotInSameContainer = aliquotInSameContainer;
	}

	/**
	* whether consents are waived?
	**/

	private Boolean consentsWaived;
	/**
	* Retrieves the value of the consentsWaived attribute
	* @return consentsWaived
	**/

	public Boolean getConsentsWaived(){
		return consentsWaived;
	}

	/**
	* Sets the value of consentsWaived attribute
	**/

	public void setConsentsWaived(Boolean consentsWaived){
		this.consentsWaived = consentsWaived;
	}

	/**
	* Sequence Number.
	**/

	private Integer sequenceNumber;
	/**
	* Retrieves the value of the sequenceNumber attribute
	* @return sequenceNumber
	**/

	public Integer getSequenceNumber(){
		return sequenceNumber;
	}

	/**
	* Sets the value of sequenceNumber attribute
	**/

	public void setSequenceNumber(Integer sequenceNumber){
		this.sequenceNumber = sequenceNumber;
	}

	/**
	* Defines the relative time point in days.
	**/

	private Double studyCalendarEventPoint;
	/**
	* Retrieves the value of the studyCalendarEventPoint attribute
	* @return studyCalendarEventPoint
	**/

	public Double getStudyCalendarEventPoint(){
		return studyCalendarEventPoint;
	}

	/**
	* Sets the value of studyCalendarEventPoint attribute
	**/

	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint){
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	/**
	* Collection Protocol type - Arm, Cycle, Phase.
	**/

	private String type;
	/**
	* Retrieves the value of the type attribute
	* @return type
	**/

	public String getType(){
		return type;
	}

	/**
	* Sets the value of type attribute
	**/

	public void setType(String type){
		this.type = type;
	}

	/**
	* The unsigned document URL for the collection protocol.
	**/

	private String unsignedConsentDocumentURL;
	/**
	* Retrieves the value of the unsignedConsentDocumentURL attribute
	* @return unsignedConsentDocumentURL
	**/

	public String getUnsignedConsentDocumentURL(){
		return unsignedConsentDocumentURL;
	}

	/**
	* Sets the value of unsignedConsentDocumentURL attribute
	**/

	public void setUnsignedConsentDocumentURL(String unsignedConsentDocumentURL){
		this.unsignedConsentDocumentURL = unsignedConsentDocumentURL;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.CollectionProtocolEvent object's collection
	**/

	private Collection<CollectionProtocolEvent> collectionProtocolEventCollection;
	/**
	* Retrieves the value of the collectionProtocolEventCollection attribute
	* @return collectionProtocolEventCollection
	**/

	public Collection<CollectionProtocolEvent> getCollectionProtocolEventCollection(){
		return collectionProtocolEventCollection;
	}

	/**
	* Sets the value of collectionProtocolEventCollection attribute
	**/

	public void setCollectionProtocolEventCollection(Collection<CollectionProtocolEvent> collectionProtocolEventCollection){
		this.collectionProtocolEventCollection = collectionProtocolEventCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.CollectionProtocol object's collection
	**/

	private Collection<CollectionProtocol> childCollectionProtocolCollection;
	/**
	* Retrieves the value of the childCollectionProtocolCollection attribute
	* @return childCollectionProtocolCollection
	**/

	public Collection<CollectionProtocol> getChildCollectionProtocolCollection(){
		return childCollectionProtocolCollection;
	}

	/**
	* Sets the value of childCollectionProtocolCollection attribute
	**/

	public void setChildCollectionProtocolCollection(Collection<CollectionProtocol> childCollectionProtocolCollection){
		this.childCollectionProtocolCollection = childCollectionProtocolCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.StudyFormContext object's collection
	**/

	private Collection<StudyFormContext> studyFormContextCollection;
	/**
	* Retrieves the value of the studyFormContextCollection attribute
	* @return studyFormContextCollection
	**/

	public Collection<StudyFormContext> getStudyFormContextCollection(){
		return studyFormContextCollection;
	}

	/**
	* Sets the value of studyFormContextCollection attribute
	**/

	public void setStudyFormContextCollection(Collection<StudyFormContext> studyFormContextCollection){
		this.studyFormContextCollection = studyFormContextCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.CollectionProtocolRegistration object's collection
	**/

	private Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection;
	/**
	* Retrieves the value of the collectionProtocolRegistrationCollection attribute
	* @return collectionProtocolRegistrationCollection
	**/

	public Collection<CollectionProtocolRegistration> getCollectionProtocolRegistrationCollection(){
		return collectionProtocolRegistrationCollection;
	}

	/**
	* Sets the value of collectionProtocolRegistrationCollection attribute
	**/

	public void setCollectionProtocolRegistrationCollection(Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection){
		this.collectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.Site object's collection
	**/

	private Collection<Site> siteCollection;
	/**
	* Retrieves the value of the siteCollection attribute
	* @return siteCollection
	**/

	public Collection<Site> getSiteCollection(){
		return siteCollection;
	}

	/**
	* Sets the value of siteCollection attribute
	**/

	public void setSiteCollection(Collection<Site> siteCollection){
		this.siteCollection = siteCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.ClinicalDiagnosis object's collection
	**/

	private Collection<ClinicalDiagnosis> clinicalDiagnosisCollection;
	/**
	* Retrieves the value of the clinicalDiagnosisCollection attribute
	* @return clinicalDiagnosisCollection
	**/

	public Collection<ClinicalDiagnosis> getClinicalDiagnosisCollection(){
		return clinicalDiagnosisCollection;
	}

	/**
	* Sets the value of clinicalDiagnosisCollection attribute
	**/

	public void setClinicalDiagnosisCollection(Collection<ClinicalDiagnosis> clinicalDiagnosisCollection){
		this.clinicalDiagnosisCollection = clinicalDiagnosisCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.DistributionProtocol object's collection
	**/

	private Collection<DistributionProtocol> distributionProtocolCollection;
	/**
	* Retrieves the value of the distributionProtocolCollection attribute
	* @return distributionProtocolCollection
	**/

	public Collection<DistributionProtocol> getDistributionProtocolCollection(){
		return distributionProtocolCollection;
	}

	/**
	* Sets the value of distributionProtocolCollection attribute
	**/

	public void setDistributionProtocolCollection(Collection<DistributionProtocol> distributionProtocolCollection){
		this.distributionProtocolCollection = distributionProtocolCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.User object's collection
	**/

	private Collection<User> coordinatorCollection;
	/**
	* Retrieves the value of the coordinatorCollection attribute
	* @return coordinatorCollection
	**/

	public Collection<User> getCoordinatorCollection(){
		return coordinatorCollection;
	}

	/**
	* Sets the value of coordinatorCollection attribute
	**/

	public void setCoordinatorCollection(Collection<User> coordinatorCollection){
		this.coordinatorCollection = coordinatorCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.User object's collection
	**/

	private Collection<User> assignedProtocolUserCollection;
	/**
	* Retrieves the value of the assignedProtocolUserCollection attribute
	* @return assignedProtocolUserCollection
	**/

	public Collection<User> getAssignedProtocolUserCollection(){
		return assignedProtocolUserCollection;
	}

	/**
	* Sets the value of assignedProtocolUserCollection attribute
	**/

	public void setAssignedProtocolUserCollection(Collection<User> assignedProtocolUserCollection){
		this.assignedProtocolUserCollection = assignedProtocolUserCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.ConsentTier object's collection
	**/

	private Collection<ConsentTier> consentTierCollection;
	/**
	* Retrieves the value of the consentTierCollection attribute
	* @return consentTierCollection
	**/

	public Collection<ConsentTier> getConsentTierCollection(){
		return consentTierCollection;
	}

	/**
	* Sets the value of consentTierCollection attribute
	**/

	public void setConsentTierCollection(Collection<ConsentTier> consentTierCollection){
		this.consentTierCollection = consentTierCollection;
	}

	/**
	 * Parent Collection Protocol.
	 */
	protected CollectionProtocol parentCollectionProtocol;


	public CollectionProtocol getParentCollectionProtocol()
	{
		return parentCollectionProtocol;
	}


	public void setParentCollectionProtocol(CollectionProtocol parentCollectionProtocol)
	{
		this.parentCollectionProtocol = parentCollectionProtocol;
	}

	private Boolean dirtyEditFlag;
	/**
	* Retrieves the value of the dirtyEditFlag attribute
	* @return dirtyEditFlag
	**/

	public Boolean getDirtyEditFlag(){
		return dirtyEditFlag;
	}

	/**
	* Sets the value of dirtyEditFlag attribute
	**/

	public void setDirtyEditFlag(Boolean dirtyEditFlag){
		this.dirtyEditFlag = dirtyEditFlag;
	}

	/**
	* Identifier of the entity in remote system
	**/
	
	private Long remoteId;
	/**
	* Retrieves the value of the remoteId attribute
	* @return remoteId
	**/

	public Long getRemoteId(){
		return remoteId;
	}

	/**
	* Sets the value of remoteId attribute
	**/

	public void setRemoteId(Long remoteId){
		this.remoteId = remoteId;
	}
	
	/**
	* Indicates whether the entity is remotely managed entity
	**/
	
	private Boolean remoteManagedFlag;
	/**
	* Retrieves the value of the remoteManagedFlag attribute
	* @return remoteManagedFlag
	**/

	public Boolean getRemoteManagedFlag(){
		return remoteManagedFlag;
	}

	/**
	* Sets the value of remoteManagedFlag attribute
	**/

	public void setRemoteManagedFlag(Boolean remoteManagedFlag){
		this.remoteManagedFlag = remoteManagedFlag;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof CollectionProtocol)
		{
			CollectionProtocol c =(CollectionProtocol)obj;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CollectionProtocol ["
				+ (aliquotInSameContainer != null ? "aliquotInSameContainer="
						+ aliquotInSameContainer + ", " : "")
				+ (consentsWaived != null ? "consentsWaived=" + consentsWaived
						+ ", " : "")
				+ (sequenceNumber != null ? "sequenceNumber=" + sequenceNumber
						+ ", " : "")
				+ (studyCalendarEventPoint != null ? "studyCalendarEventPoint="
						+ studyCalendarEventPoint + ", " : "")
				+ (type != null ? "type=" + type + ", " : "")
				+ (unsignedConsentDocumentURL != null ? "unsignedConsentDocumentURL="
						+ unsignedConsentDocumentURL + ", "
						: "")
				+ (activityStatus != null ? "activityStatus=" + activityStatus
						+ ", " : "")
				+ (aliquotLabelFormat != null ? "aliquotLabelFormat="
						+ aliquotLabelFormat + ", " : "")
				+ (derivativeLabelFormat != null ? "derivativeLabelFormat="
						+ derivativeLabelFormat + ", " : "")
				+ (descriptionURL != null ? "descriptionURL=" + descriptionURL
						+ ", " : "")
				+ (endDate != null ? "endDate=" + endDate + ", " : "")
				+ (enrollment != null ? "enrollment=" + enrollment + ", " : "")
				+ (id != null ? "id=" + id + ", " : "")
				+ (irbIdentifier != null ? "irbIdentifier=" + irbIdentifier
						+ ", " : "")
				+ (shortTitle != null ? "shortTitle=" + shortTitle + ", " : "")
				+ (specimenLabelFormat != null ? "specimenLabelFormat="
						+ specimenLabelFormat + ", " : "")
				+ (startDate != null ? "startDate=" + startDate + ", " : "")
				+ (title != null ? "title=" + title : "") + "]";
	}

	/**
	* An associated edu.wustl.catissuecore.domain.CPGridGrouperPrivilege object's collection
	**/
	private Set<CPGridGrouperPrivilege> gridGrouperPrivileges = new HashSet<CPGridGrouperPrivilege>();
	
	/**
	* Retrieves the set of grid grouper privilege attributes
	* @return gridGrouperPrivileges
	**/
	public Set<CPGridGrouperPrivilege> getGridGrouperPrivileges() {
		return gridGrouperPrivileges;
	}

	/**
	* Sets the value of grid grouper privilege attribute
	**/
	public void setGridGrouperPrivileges(
			Set<CPGridGrouperPrivilege> gridGrouperPrivileges) {
		this.gridGrouperPrivileges = gridGrouperPrivileges;
	}
	
}