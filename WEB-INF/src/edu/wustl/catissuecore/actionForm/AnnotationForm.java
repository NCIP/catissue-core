/*
 * Created on Jan 5, 2007
 * @author
 *
 */
package edu.wustl.catissuecore.actionForm;

import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @author preeti_munot
 *
 */
public class AnnotationForm extends ActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String annotationGroupsXML;
	private String annotationEntitiesXML;
	private List systemEntitiesList;
	private String selectedStaticEntityId;
	
	public String getSelectedStaticEntityId()
	{
		return this.selectedStaticEntityId;
	}
	public void setSelectedStaticEntityId(String selectedStaticEntityId)
	{
		this.selectedStaticEntityId = selectedStaticEntityId;
	}
	public List getSystemEntitiesList()
	{
		return this.systemEntitiesList;
	}
	public void setSystemEntitiesList(List systemEntitiesList)
	{
		this.systemEntitiesList = systemEntitiesList;
	}
	public String getAnnotationEntitiesXML()
	{
		return this.annotationEntitiesXML;
	}
	public void setAnnotationEntitiesXML(String annotationEntitiesXML)
	{
		this.annotationEntitiesXML = annotationEntitiesXML;
	}
	public String getAnnotationGroupsXML()
	{
		return this.annotationGroupsXML;
	}
	public void setAnnotationGroupsXML(String annotationGroupsXML)
	{
		this.annotationGroupsXML = annotationGroupsXML;
	}
}
