/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.metadata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.wustl.common.beans.SessionDataBean;

public class HookingInformation
{
	private Object staticObject;
	private SessionDataBean sessionDataBean;
	private Long dynamicExtensionObjectId;
	private Long rootContainerId;
	private Collection<Attribute> attributeCollection = new ArrayList<Attribute>();
	private Map<String,Object> dataHookingInformation=new HashMap<String, Object>();
	private String categoryName=null;
	private String entityGroupName;
	private String entityName;


	public String getEntityGroupName()
	{
		return entityGroupName;
	}
	public void setEntityGroupName(String entityGroupName)
	{
		this.entityGroupName = entityGroupName;
	}
	public String getEntityName()
	{
		return entityName;
	}
	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}
	public String getCategoryName()
	{
		return categoryName;
	}


	public void setCategoryName(String categoryName)
	{
		this.categoryName = categoryName;
	}

	public Long getRootContainerId()
	{
		return rootContainerId;
	}

	public void setRootContainerId(Long rootContainerId)
	{
		this.rootContainerId = rootContainerId;
	}


	public Map<String, Object> getDataHookingInformation()
	{
		return dataHookingInformation;
	}


	public void setDataHookingInformation(Map<String, Object> map)
	{
		dataHookingInformation = map;
	}

	public HookingInformation()
	{

	}

	public SessionDataBean getSessionDataBean()
	{
		return sessionDataBean;
	}

	public void setSessionDataBean(SessionDataBean sessionDataBean)
	{
		this.sessionDataBean = sessionDataBean;
	}

	public final Long getDynamicExtensionObjectId()
	{
		return dynamicExtensionObjectId;
	}

	public final void setDynamicExtensionObjectId(Long dynamicExtensionObjectId)
	{
		this.dynamicExtensionObjectId = dynamicExtensionObjectId;
	}

	public final Object getStaticObject()
	{
		return staticObject;
	}

	public final void setStaticObject(Object staticObject)
	{
		this.staticObject = staticObject;
	}

	public Collection<Attribute> getAttributeCollection()
	{
		return attributeCollection;
	}

	public void setAttributeCollection(Collection<Attribute> attributeCollection)
	{
		this.attributeCollection = attributeCollection;
	}
	public void addAttribute(Attribute attribute) {
	    this.attributeCollection.add(attribute);
	}
	
	public void setEntityType() {
		String entityType;
		
		if (dataHookingInformation.get("collectionProtocol") != null ) {
			entityType = "Participant";
		} else if (dataHookingInformation.get("specimenId") != null || 
				   dataHookingInformation.get("specimenLabel") != null ||
				   dataHookingInformation.get("specimenBarcode") != null) {
			entityType = "Specimen";
		} else if (dataHookingInformation.get("specimenIdForEvent") != null || 
				   dataHookingInformation.get("specimenLabelForEvent") != null ||
				   dataHookingInformation.get("specimenBarcodeForEvent") != null) {
			entityType = "SpecimenEvent";
		} else if (dataHookingInformation.get("scgId") != null || 
				   dataHookingInformation.get("scgName") != null ||
				   dataHookingInformation.get("scgBarcode") != null) {
			entityType = "SpecimenCollectionGroup";
		} else {
			throw new RuntimeException("Unrecognized integration fields");
		}
		
		
		dataHookingInformation.put("entityType", entityType);
	}
}