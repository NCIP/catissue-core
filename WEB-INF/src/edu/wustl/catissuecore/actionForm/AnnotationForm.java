/*
 * Created on Jan 5, 2007
 * @author
 *
 */
package edu.wustl.catissuecore.actionForm;

import java.io.Serializable;
import java.util.List;

import org.apache.struts.action.ActionForm;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author preeti_munot
 *
 */
public class AnnotationForm extends AbstractActionForm implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String annotationGroupsXML;
	private String annotationEntitiesXML;
	private List systemEntitiesList;
	private String selectedStaticEntityId;
    private List  conditionalInstancesList;
    private String[] conditionVal;
	
	
    public String[] getConditionVal()
    {
        return conditionVal;
    }
    
    public void setConditionVal(String[] conditionVal)
    {
        this.conditionVal = conditionVal;
    }
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

    public int getFormId()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        // TODO Auto-generated method stub
        
    }

    protected void reset()
    {
        // TODO Auto-generated method stub
        
    }
    
    public List getConditionalInstancesList()
    {
        return conditionalInstancesList;
    }
    
    public void setConditionalInstancesList(List conditionalInstancesList)
    {
        this.conditionalInstancesList = conditionalInstancesList;
    }
}
