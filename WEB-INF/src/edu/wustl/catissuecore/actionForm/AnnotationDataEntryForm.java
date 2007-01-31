/*
 * Created on Jan 17, 2007
 * @author
 *
 */
package edu.wustl.catissuecore.actionForm;

import java.util.List;

import org.apache.struts.action.ActionForm;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnnotationDataEntryForm extends ActionForm
{
	private static final long serialVersionUID = 5496516855615880241L;
	protected List annotationsList;
	protected String definedAnnotationsDataXML;
	protected String selectedAnnotation;
	protected String parentEntityId;
	
	public String getDefinedAnnotationsDataXML()
	{
		return this.definedAnnotationsDataXML;
	}
	public void setDefinedAnnotationsDataXML(String definedAnnotationsDataXML)
	{
		this.definedAnnotationsDataXML = definedAnnotationsDataXML;
	}
	public List getAnnotationsList()
	{
		return this.annotationsList;
	}
	public void setAnnotationsList(List annotationsList)
	{
		this.annotationsList = annotationsList;
	}
	public String getSelectedAnnotation()
	{
		return this.selectedAnnotation;
	}
	public void setSelectedAnnotation(String selectedAnnotation)
	{
		this.selectedAnnotation = selectedAnnotation;
	}
	public String getParentEntityId()
	{
		return this.parentEntityId;
	}
	public void setParentEntityId(String parentEntityId)
	{
		this.parentEntityId = parentEntityId;
	}
}
