package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;

import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.participant.domain.ISite;
/**
	*
	**/

public class Site extends AbstractDomainObject implements Serializable, IActivityStatus, ISite
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;


	/**
	* Defines whether this Site record can be queried (Active) or not queried (Inactive) by any actor.
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
	* EmailAddress Address of the site.
	**/

	private String emailAddress;
	/**
	* Retrieves the value of the emailAddress attribute
	* @return emailAddress
	**/

	public String getEmailAddress(){
		return emailAddress;
	}

	/**
	* Sets the value of emailAddress attribute
	**/

	public void setEmailAddress(String emailAddress){
		this.emailAddress = emailAddress;
	}

	/**
	* System generated unique identifier.
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
	* Name of the physical location.
	**/

	private String name;
	/**
	* Retrieves the value of the name attribute
	* @return name
	**/

	public String getName(){
		return name;
	}

	/**
	* Sets the value of name attribute
	**/

	public void setName(String name){
		this.name = name;
	}

	/**
	* Function of the site (e.g. Collection site, repository, or laboratory).
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
	* An associated edu.wustl.catissuecore.domain.User object
	**/

	private User coordinator;
	/**
	* Retrieves the value of the coordinator attribute
	* @return coordinator
	**/

	public User getCoordinator(){
		return coordinator;
	}
	/**
	* Sets the value of coordinator attribute
	**/

	public void setCoordinator(User coordinator){
		this.coordinator = coordinator;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup object's collection
	**/

	private Collection<AbstractSpecimenCollectionGroup> abstractSpecimenCollectionGroupCollection;
	/**
	* Retrieves the value of the abstractSpecimenCollectionGroupCollection attribute
	* @return abstractSpecimenCollectionGroupCollection
	**/

	public Collection<AbstractSpecimenCollectionGroup> getAbstractSpecimenCollectionGroupCollection(){
		return abstractSpecimenCollectionGroupCollection;
	}

	/**
	* Sets the value of abstractSpecimenCollectionGroupCollection attribute
	**/

	public void setAbstractSpecimenCollectionGroupCollection(Collection<AbstractSpecimenCollectionGroup> abstractSpecimenCollectionGroupCollection){
		this.abstractSpecimenCollectionGroupCollection = abstractSpecimenCollectionGroupCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.User object's collection
	**/

	private Collection<User> assignedSiteUserCollection;
	/**
	* Retrieves the value of the assignedSiteUserCollection attribute
	* @return assignedSiteUserCollection
	**/

	public Collection<User> getAssignedSiteUserCollection(){
		return assignedSiteUserCollection;
	}

	/**
	* Sets the value of assignedSiteUserCollection attribute
	**/

	public void setAssignedSiteUserCollection(Collection<User> assignedSiteUserCollection){
		this.assignedSiteUserCollection = assignedSiteUserCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.CollectionProtocol object's collection
	**/

	private Collection<CollectionProtocol> collectionProtocolCollection;
	/**
	* Retrieves the value of the collectionProtocolCollection attribute
	* @return collectionProtocolCollection
	**/

	public Collection<CollectionProtocol> getCollectionProtocolCollection(){
		return collectionProtocolCollection;
	}

	/**
	* Sets the value of collectionProtocolCollection attribute
	**/

	public void setCollectionProtocolCollection(Collection<CollectionProtocol> collectionProtocolCollection){
		this.collectionProtocolCollection = collectionProtocolCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.Address object
	**/

	private Address address;
	/**
	* Retrieves the value of the address attribute
	* @return address
	**/

	public Address getAddress(){
		return address;
	}
	/**
	* Sets the value of address attribute
	**/

	public void setAddress(Address address){
		this.address = address;
	}
	
	private String ctepId;
	
	

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof Site)
		{
			Site c =(Site)obj;
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

	public String getFacilityId() {
		return null;
	}

	public void setFacilityId(String arg0) {

	}

	/**
	 * @return the ctepId
	 */
	public String getCtepId() {
		return ctepId;
	}

	/**
	 * @param ctepId the ctepId to set
	 */
	public void setCtepId(String ctepId) {
		this.ctepId = ctepId;
	}

	/**
	* Indicates whether the remotely managed entity is locally modified
	**/
	
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Site [activityStatus=" + activityStatus + ", emailAddress="
				+ emailAddress + ", id=" + id + ", name=" + name + ", type="
				+ type + ", address=" + address + ", ctepId=" + ctepId 
				+ ", dirtyEditFlag=" + dirtyEditFlag + ", remoteManagedFlag=" + remoteManagedFlag 
				+ ", remoteId=" + remoteId + "]";
	}
	
	

}