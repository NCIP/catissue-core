package edu.wustl.catissuecore.actionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.logger.Logger;


public class ListSpecimenEventParametersForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ListSpecimenEventParametersForm.class);
	private String specimenId;
	
	private String specimenEventParameter;
    
	
	
	/**
	 * @return Returns the specimenEventParameter.
	 */
	public String getSpecimenEventParameter() 
	{
		return specimenEventParameter;
	}
	/**
	 * @param specimenEventParameter The specimenEventParameter to set.
	 */
	public void setSpecimenEventParameter(String specimenEventParameter)
	{
		this.specimenEventParameter = specimenEventParameter;
	}
	/**
	 * @return Returns the specimenId.
	 */
	public String getSpecimenId() 
	{
		return specimenId;
	}
	/**
	 * @param specimenId The specimenId to set.
	 */
	public void setSpecimenId(String specimenId)
	{
		this.specimenId = specimenId;
	}
    /**
     * No argument constructor for UserForm class 
     */
    public ListSpecimenEventParametersForm()
    {
//        reset();
    }

    /**
     * Populates all the fields from the domain object to the form bean.
     * @param abstractDomain An AbstractDomain Object  
     */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
    }

    /**
     * Checks the operation to be performed is add operation.
     * @return Returns true if operation is equal to "add", else it returns false
     * */
    public boolean isAddOperation()
    {
        return(getOperation().equals(Constants.ADD));
    }
    
    /**
     * @return LIST_SPECIMEN_EVENT_PARAMETERS_FORM_ID Returns the id assigned to form bean
     */
    public int getFormId()
    {
        return Constants.LIST_SPECIMEN_EVENT_PARAMETERS_FORM_ID;
    }
    
    /**
     * Resets the values of all the fields.
     * Is called by the overridden reset method defined in ActionForm.  
     * */
    protected void reset()
    {
    }

    /**
	 * Overrides the validate method of ActionForm.
	 * @return error ActionErrors instance
	 * @param mapping Actionmapping instance
	 * @param request HttpServletRequest instance
	 */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errors = new ActionErrors();
//        Validator validator = new Validator();
        try
        {
        }
        catch(Exception excp)
        {
            logger.error(excp.getMessage(),excp);
        }
        return errors;
     }
}
