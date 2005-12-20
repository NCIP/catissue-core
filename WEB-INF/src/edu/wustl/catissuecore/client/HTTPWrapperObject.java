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

import edu.wustl.common.domain.AbstractDomainObject;

public class HTTPWrapperObject implements Serializable
{
	private AbstractDomainObject domainObject;
	private String operation;
	
	public HTTPWrapperObject(){}
	
	public HTTPWrapperObject(AbstractDomainObject domainObject,String operation)
	{
		this.domainObject = domainObject;
		this.operation = operation;
	}	
	
	public AbstractDomainObject getDomainObject()
	{
		return domainObject;
	}
	
	public void setDomainObject(AbstractDomainObject domainObject)
	{
		this.domainObject = domainObject;
	}
	
	public String getOperation()
	{
		return operation;
	}
	
	public void setOperation(String operation)
	{
		this.operation = operation;
	}
}