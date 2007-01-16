/**
 * <p>Title: RequestDetailsForm Class>
 * <p>Description:	This class contains attributes to display on RequestDetails.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Oct 06,2006
 */

package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import edu.wustl.catissuecore.bean.DefinedArrayDetailsBean;
import edu.wustl.catissuecore.bean.RequestDetailsBean;
import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

public class RequestDetailsForm extends AbstractActionForm
{
	
	// The status which the user wants to update in one go.
	private String status ;
	// The Map containg submitted values for 'assigned quantity', 'assigned status' and 'request for'. 
	protected Map values = new HashMap();
	// The administrator comments.
	private String administratorComments ;
	// The Order Id required to retrieve the corresponding order items from the database.
	private long id;	
	//The Site associated with the distribution.
	private String site;
	/**
	 * The distribution protocol associated with that order.
	 */
	private String distributionProtocolId;
	
	
	/**
	 * @return the site
	 */
	public String getSite()
	{
		return site;
	}

	
	/**
	 * @param site the site to set
	 */
	public void setSite(String site)
	{
		this.site = site;
	}

	//For 'EDIT' operation in CommonAddEditAction.
	/**
	 * @return boolean. 'true' if operation is add.
	 */
	public boolean isAddOperation()
	{
		return false;
	}
	
	/**
	 * @return the id
	 */
	public long getId()
	{
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	/**
	 * @return the administratorComments
	 */
	public String getAdministratorComments()
	{
		return administratorComments;
	}

	/**
	 * @param administratorComments the administratorComments to set
	 */
	public void setAdministratorComments(String administratorComments)
	{
		this.administratorComments = administratorComments;
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllValues()
	{
		return values.values();
	}

	/** Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	
	public void setValue(String key, Object value)
	{
		if (isMutable())
		{
			values.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key)
	{
		return values.get(key);
	}

	/**
	 * @return values
	 */
	public Map getValues()
	{
		return values;
	}

	/**
	 * @param values Map
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}
	/**
	 * @return int formId 
	 */
	public int getFormId()
	{
		
		return Constants.REQUEST_DETAILS_FORM_ID;
	}
	/**
	 * @param 
	 */
	protected void reset()
	{
		
	}
	/**
	 * @param abstractDomain object
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		values = new HashMap();
		int requestDetailsBeanCounter = 0;
		int arrayRequestBeanCounter = 0;
		int arrayDetailsBeanCounter = 0;
		int existingArrayBeanCounter = 0;
		OrderDetails order = (OrderDetails)abstractDomain;
		Collection orderItemColl = order.getOrderItemCollection();
		Iterator iter = orderItemColl.iterator();
		while(iter.hasNext())
		{		
			OrderItem orderItem = (OrderItem)iter.next();
			//Making keys
	//		String requestFor = "RequestDetailsBean:"+i+"_requestFor"; 
	//	 	String assignQty = "RequestDetailsBean:"+i+"_assignedQty";
			String assignStatus = "";
			String description = "";
			if(((orderItem instanceof ExistingSpecimenOrderItem) || (orderItem instanceof DerivedSpecimenOrderItem) || (orderItem instanceof PathologicalCaseOrderItem)) && (orderItem.getOrder().getId() != null))
			{
			 	assignStatus = "RequestDetailsBean:"+requestDetailsBeanCounter+"_assignedStatus"; 	
				description = "RequestDetailsBean:"+requestDetailsBeanCounter+"_description";
				requestDetailsBeanCounter++;
			}
			else if(orderItem instanceof ExistingSpecimenArrayOrderItem)
			{
				assignStatus = "ExistingArrayDetailsBean:"+existingArrayBeanCounter+"_assignedStatus"; 	
				description = "ExistingArrayDetailsBean:"+existingArrayBeanCounter+"_description";
				existingArrayBeanCounter++;
			}
			else if(orderItem instanceof NewSpecimenArrayOrderItem)
			{
				assignStatus = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_assignedStatus";
				arrayRequestBeanCounter++;
			}
			// ArrayDetailsBean if order id in null for order items.
			else if(orderItem.getOrder().getId() == null)
			{
				assignStatus = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_assignedStatus"; 	
				description = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_description";
				arrayDetailsBeanCounter++;
			}				
			values.put(assignStatus,orderItem.getStatus());
			values.put(description, orderItem.getDescription());			
		}
	}

	
	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return status;
	}

	
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}


	
	/**
	 * @return the distributionProtocolId
	 */
	public String getDistributionProtocolId()
	{
		return distributionProtocolId;
	}


	
	/**
	 * @param distributionProtocolId the distributionProtocolId to set
	 */
	public void setDistributionProtocolId(String distributionProtocolId)
	{
		this.distributionProtocolId = distributionProtocolId;
	}
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errors = new ActionErrors();
		Validator validator = new Validator();
		
		//getting values from a map.
		RequestDetailsBean requestDetailsBean = null;	
		DefinedArrayDetailsBean definedArrayDetailsBean = null;
		
		boolean specimenItem = false;		
		boolean arrayDetailsItem = false;
		
		
		MapDataParser mapDataParser = new MapDataParser("edu.wustl.catissuecore.bean");
		Collection beanObjSet = null;
		try
		{
			beanObjSet = mapDataParser.generateData(this.values);
		}
		catch (Exception e)
		{
			Logger.out.debug(""+e);			
		}
		Iterator iter = beanObjSet.iterator();		
		
		while(iter.hasNext())
		{
			Object obj = iter.next();
			//For specimen order item.
			if(obj instanceof RequestDetailsBean)
			{
				 requestDetailsBean = (RequestDetailsBean)obj;
				 specimenItem = true;
			}
			
			//For defined array details.
			else if(obj instanceof DefinedArrayDetailsBean)
			{
				definedArrayDetailsBean = (DefinedArrayDetailsBean)obj;
				arrayDetailsItem = true;
			}
			if(specimenItem)
			{
				if(requestDetailsBean.getAssignedQty() != null && !requestDetailsBean.getAssignedQty().equalsIgnoreCase(""))
				{
					if(!validator.isDouble(requestDetailsBean.getAssignedQty()))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("itemrecord.quantity")));
						break;
					}
				}
			}
			else if(arrayDetailsItem)
			{
				if(definedArrayDetailsBean.getAssignedQuantity() != null && !definedArrayDetailsBean.getAssignedQuantity().equalsIgnoreCase(""))
				{
					if(!validator.isDouble(definedArrayDetailsBean.getAssignedQuantity()))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",ApplicationProperties.getValue("itemrecord.quantity")));
						break;
					}
				}
				
			}
		}
		return errors;
	}
	
}
