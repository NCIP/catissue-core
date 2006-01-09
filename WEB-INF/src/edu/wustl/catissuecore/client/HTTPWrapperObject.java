/**
 * <p>Title: HTTPWrapperObject Class>
 * <p>Description:	This class provides a wrapper object which constitutes
 * an object of AbstractDomainObject & operation that is to be performed.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Dec 20, 2005
 */

package edu.wustl.catissuecore.client;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

import edu.wustl.catissuecore.actionForm.ActionFormFactory;
import edu.wustl.catissuecore.actionForm.DepartmentForm;
import edu.wustl.catissuecore.actionForm.LoginForm;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;

public class HTTPWrapperObject implements Serializable
{
    private static final long serialVersionUID = -4958330782397508598L;
    private ActionForm formBean;
	private String operation;
	
	public HTTPWrapperObject(){}
	
	public HTTPWrapperObject(Object domainObject,String operation) throws Exception
	{
	    if(operation.equals(Constants.LOGIN))
		{
			User user = (User)domainObject;
			LoginForm loginForm = new LoginForm();
			loginForm.setLoginName(user.getLoginName());
			loginForm.setPassword(user.getPassword());
			formBean = loginForm;
		}
		else
		{
		    AbstractActionForm abstractForm = ActionFormFactory.getFormBean(domainObject);
		    abstractForm.setOperation(operation);				
			abstractForm.setAllVal(domainObject);
			formBean = abstractForm;
		}
			
		this.operation = operation;
	}
	
	public ActionForm getForm()
	{
	    return formBean; 
	}
	
	public String getOperation()
	{
	    return this.operation;
	}
}