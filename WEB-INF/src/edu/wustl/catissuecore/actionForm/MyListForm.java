package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


public class MyListForm extends AbstractActionForm
{
	protected String name;
	protected String classOfSpecimen;
	protected String type;
	protected String site;
	protected String availableQuantity;
	protected String unitAvailableQuantity;
	
	
	public String getAvailableQuantity()
	{
		return availableQuantity;
	}
	
	public void setAvailableQuantity(String availableQuantity)
	{
		this.availableQuantity = availableQuantity;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getSite()
	{
		return site;
	}
	
	public void setSite(String site)
	{
		this.site = site;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}

    public void setAllValues(AbstractDomainObject abstractDomain)
    {
    }

    public int getFormId()
    {
        //return Constants.SITE_FORM_ID;
    	return 0;
    }
    
    protected void reset()
    {
    }
	
	public void reset(ActionMapping mapping, HttpServletRequest request)
    {
	
		this.name = null;
        this.classOfSpecimen = null;
        this.type = null;
        this.site = null;
        this.availableQuantity = null;

    }

	
	public String getClassOfSpecimen()
	{
		return classOfSpecimen;
	}

	
	public void setClassOfSpecimen(String classOfSpecimen)
	{
		this.classOfSpecimen = classOfSpecimen;
	}

	public String getUnitAvailableQuantity() {
		return unitAvailableQuantity;
	}

	public void setUnitAvailableQuantity(String unitAvailableQuantity) {
		this.unitAvailableQuantity = unitAvailableQuantity;
	}
	
}
