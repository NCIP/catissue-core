/**
 * <p>Title: PathologicalCaseOrderItem Class>
 * <p>Description:   Class for PathologicalCaseOrderItem.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on October 16,2006
 */
package edu.wustl.catissuecore.domain;
/**
 * @version 1.0
 * @created 16-Oct-2006 6:25:49 PM
 * 
 * Represents  Pathology Order Item.
 * @hibernate.joined-subclass table="CATISSUE_PATH_CASE_ORDER_ITEM" 
 * @hibernate.joined-subclass-key
 * column="IDENTIFIER"
 */
public class PathologicalCaseOrderItem extends NewSpecimenOrderItem 
{

	private static final long serialVersionUID = -2329235774308406889L;
	/**
	 * The pathological Status.
	 */
	protected String pathologicalStatus;
	/**
	 * The tissue Site.
	 */
	protected String tissueSite;
	/**
	 * The specimen Collection Group.
	 */
	protected SpecimenCollectionGroup specimenCollectionGroup;

	/**
	 * The default constructor.
	 */
	public PathologicalCaseOrderItem()
	{
	}

	/**
	 * Returns the specimen class of the requested new/derived specimen
	 * @hibernate.property  name="specimenClass" type="string" length="100" column="SPECIMEN_CLASS"
	 * @return Specimen Class of the new/derived specimen
	 */
	public String getSpecimenClass()
	{
		return specimenClass;
	}
	
	/**
	 * Sets the specimen class of the requested new/derived specimen.
	 * @param specimenClass String
	 */
	public void setSpecimenClass(String specimenClass)
	{
		this.specimenClass = specimenClass;
	}

	/**
	 * Returns the specimen type of the requested new/derived specimen
	 * @hibernate.property name="specimenType" length="100" type="string" column="SPECIMEN_TYPE"
	 * @return Specimen Type of the new/derived specimen
	 */
	public String getSpecimenType()
	{
		return specimenType;
	}

	/**
	 * Sets the specimen type of the new/derived specimens
	 * @param specimenType String
	 */
	public void setSpecimenType(String specimenType)
	{
		this.specimenType = specimenType;
	}

	
	/**
	 * @hibernate.property name="pathologicalStatus" length="100" type="string" column="PATHOLOGICAL_STATUS"
	 * @return pathologicalStatus.
	 */
	public String getPathologicalStatus()
	{
		return pathologicalStatus;
	}

	
	/**
	 * @param pathologicalStatus the pathologicalStatus to set
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	
	/**
	 * The specimenCollectionGroup associated with the order item in SpecimenOrderItem.
	 * @hibernate.many-to-one column="SPECIMEN_COLL_GROUP_ID" class="edu.wustl.catissuecore.domain.SpecimenCollectionGroup"
	 * constrained="true"
	 * @return the specimenCollectionGroup
	 */
	public SpecimenCollectionGroup getSpecimenCollectionGroup()
	{
		return specimenCollectionGroup;
	}

	
	/**
	 * @param specimenCollectionGroup the specimenCollectionGroup to set
	 */
	public void setSpecimenCollectionGroup(SpecimenCollectionGroup specimenCollectionGroup)
	{
		this.specimenCollectionGroup = specimenCollectionGroup;
	}

	
	/**
	 * @hibernate.property name="tissueSite" length="100" type="string" column="TISSUE_SITE"
	 * @return tissueSite String.
	 */
	
	public String getTissueSite()
	{
		return tissueSite;
	}

	
	/**
	 * @param tissueSite the tissueSite to set
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}


}