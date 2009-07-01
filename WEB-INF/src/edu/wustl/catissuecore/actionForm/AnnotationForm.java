/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */

package edu.wustl.catissuecore.actionForm;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author preeti_munot
 * 
 */
public class AnnotationForm extends AbstractActionForm implements Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(AnnotationForm.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String annotationGroupsXML;

	private String annotationEntitiesXML;

	private List systemEntitiesList;

	private String selectedStaticEntityId;

	private List conditionalInstancesList;

	private String[] conditionVal;

	private String deoperation;

	public String[] getConditionVal()
	{
		return conditionVal;
	}

	public void setConditionVal(final String[] conditionVal)
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

	public void setDeoperation(final String deoperation)
	{
		this.deoperation = deoperation;
	}

	public String getDeoperation()
	{
		return deoperation;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();
		try
		{
			if ((deoperation != null && "add".equals(deoperation))
					&& (validator.isValidOption(String.valueOf(selectedStaticEntityId)) == false))
			{
				//				if (validator.isValidOption(String.valueOf(selectedStaticEntityId)) == false) 
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
							ApplicationProperties.getValue("anno.Entity")));
				}

			}
		}
		catch (Exception excp)
		{
			logger.error(excp.getMessage(), excp);
		}
		return errors;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}
}
