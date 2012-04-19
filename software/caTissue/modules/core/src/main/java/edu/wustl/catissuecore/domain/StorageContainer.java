package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;

import edu.wustl.common.bizlogic.IActivityStatus;
/**
	* 
	**/

public class StorageContainer extends Container implements Serializable, IActivityStatus, ISpecimenTypeDomain
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
	/**
	* 
	**/
	
	private Collection<String> holdsSpecimenClassCollection;
	/**
	* Retrieves the value of the holdsSpecimenClassCollection attribute
	* @return holdsSpecimenClassCollection
	**/

	public Collection<String> getHoldsSpecimenClassCollection(){
		return holdsSpecimenClassCollection;
	}

	/**
	* Sets the value of holdsSpecimenClassCollection attribute
	**/

	public void setHoldsSpecimenClassCollection(Collection<String> holdsSpecimenClassCollection){
		this.holdsSpecimenClassCollection = holdsSpecimenClassCollection;
	}
	
	/**
	* 
	**/
	
	private Collection<String> holdsSpecimenTypeCollection;
	/**
	* Retrieves the value of the holdsSpecimenTypeCollection attribute
	* @return holdsSpecimenTypeCollection
	**/

	public Collection<String> getHoldsSpecimenTypeCollection(){
		return holdsSpecimenTypeCollection;
	}

	/**
	* Sets the value of holdsSpecimenTypeCollection attribute
	**/

	public void setHoldsSpecimenTypeCollection(Collection<String> holdsSpecimenTypeCollection){
		this.holdsSpecimenTypeCollection = holdsSpecimenTypeCollection;
	}
	
	/**
	* tempratureInCentigrade.
	**/
	
	private Double temperatureInCentigrade;
	/**
	* Retrieves the value of the temperatureInCentigrade attribute
	* @return temperatureInCentigrade
	**/

	public Double getTemperatureInCentigrade(){
		return temperatureInCentigrade;
	}

	/**
	* Sets the value of temperatureInCentigrade attribute
	**/

	public void setTemperatureInCentigrade(Double temperatureInCentigrade){
		this.temperatureInCentigrade = temperatureInCentigrade;
	}
	
	/**
	* An associated edu.wustl.catissuecore.domain.SpecimenArrayType object's collection 
	**/
			
	private Collection<SpecimenArrayType> holdsSpecimenArrayTypeCollection;
	/**
	* Retrieves the value of the holdsSpecimenArrayTypeCollection attribute
	* @return holdsSpecimenArrayTypeCollection
	**/

	public Collection<SpecimenArrayType> getHoldsSpecimenArrayTypeCollection(){
		return holdsSpecimenArrayTypeCollection;
	}

	/**
	* Sets the value of holdsSpecimenArrayTypeCollection attribute
	**/

	public void setHoldsSpecimenArrayTypeCollection(Collection<SpecimenArrayType> holdsSpecimenArrayTypeCollection){
		this.holdsSpecimenArrayTypeCollection = holdsSpecimenArrayTypeCollection;
	}
		
	/**
	* An associated edu.wustl.catissuecore.domain.Site object
	**/
			
	private Site site;
	/**
	* Retrieves the value of the site attribute
	* @return site
	**/
	
	public Site getSite(){
		return site;
	}
	/**
	* Sets the value of site attribute
	**/

	public void setSite(Site site){
		this.site = site;
	}
			
	/**
	* An associated edu.wustl.catissuecore.domain.SpecimenPosition object's collection 
	**/
			
	private Collection<SpecimenPosition> specimenPositionCollection;
	/**
	* Retrieves the value of the specimenPositionCollection attribute
	* @return specimenPositionCollection
	**/

	public Collection<SpecimenPosition> getSpecimenPositionCollection(){
		return specimenPositionCollection;
	}

	/**
	* Sets the value of specimenPositionCollection attribute
	**/

	public void setSpecimenPositionCollection(Collection<SpecimenPosition> specimenPositionCollection){
		this.specimenPositionCollection = specimenPositionCollection;
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
	* An associated edu.wustl.catissuecore.domain.StorageType object's collection 
	**/
			
	private Collection<StorageType> holdsStorageTypeCollection;
	/**
	* Retrieves the value of the holdsStorageTypeCollection attribute
	* @return holdsStorageTypeCollection
	**/

	public Collection<StorageType> getHoldsStorageTypeCollection(){
		return holdsStorageTypeCollection;
	}

	/**
	* Sets the value of holdsStorageTypeCollection attribute
	**/

	public void setHoldsStorageTypeCollection(Collection<StorageType> holdsStorageTypeCollection){
		this.holdsStorageTypeCollection = holdsStorageTypeCollection;
	}
		
	/**
	* An associated edu.wustl.catissuecore.domain.StorageType object
	**/
			
	private StorageType storageType;
	/**
	* Retrieves the value of the storageType attribute
	* @return storageType
	**/
	
	public StorageType getStorageType(){
		return storageType;
	}
	/**
	* Sets the value of storageType attribute
	**/

	public void setStorageType(StorageType storageType){
		this.storageType = storageType;
	}
			
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof StorageContainer) 
		{
			StorageContainer c =(StorageContainer)obj; 			 
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
	
	private String oneDimensionLabellingScheme;
	private String twoDimensionLabellingScheme;
	
	public String getOneDimensionLabellingScheme()
	{
		return oneDimensionLabellingScheme;
	}

	public void setOneDimensionLabellingScheme(String oneDimensionLabellingScheme)
	{
		this.oneDimensionLabellingScheme = oneDimensionLabellingScheme;
	}
	
	public String getTwoDimensionLabellingScheme()
	{
		return twoDimensionLabellingScheme;
	}
	
	public void setTwoDimensionLabellingScheme(String twoDimensionLabelllingScheme)
	{
		this.twoDimensionLabellingScheme = twoDimensionLabelllingScheme;
	}

}