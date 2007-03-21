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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.util.OrderingSystemUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

public class RequestDetailsForm extends AbstractActionForm
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	 * The tab page which should be visible to the user.
	 */
	private int tabIndex;
	/**
	 * The map to display the list of specimens in request For drop down.
	 */
	private Map requestForDropDownMap = new HashMap();
	/**
	 * 
	 */
	private String specimenId;
	/** Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */
	
	public void setRequestFor(String key, Object value)
	{
		if (isMutable())
		{
			requestForDropDownMap.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public List getRequestFor(String key)
	{
		return ((List)(requestForDropDownMap.get(key)));
	}
	
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
	public void setAllValuesForOrder(AbstractDomainObject abstractDomain,HttpServletRequest request)
	{
		int requestDetailsBeanCounter = 0;
		int arrayRequestBeanCounter = 0;
		int arrayDetailsBeanCounter = 0;
		int existingArrayBeanCounter = 0;
		OrderDetails order = (OrderDetails)abstractDomain;
		Collection orderItemColl = order.getOrderItemCollection();
		Iterator iter = orderItemColl.iterator();
		List totalSpecimenListInRequestForDropDown = new ArrayList();
		while(iter.hasNext())
		{		
			OrderItem orderItem = (OrderItem)iter.next();
			//Making keys	
			String assignStatus = "";
			String description = "";
		
			String requestedItem = "";
			String requestedQty = "";
			String availableQty = "";
			String specimenClass = "";
			String specimenType = "";
			
			String orderItemId = "";
			String requestFor = "";
			String assignQty = "";
			String instanceOf = "";
			String specimenId = "";
			String distributedItemId = "";
			String specimenList = "";
			String specimenCollGrpId = "";
			
			String actualSpecimenClass = "";
			String actualSpecimenType = "";
			//For array
			String arrayId = "";
			
			if(((orderItem instanceof ExistingSpecimenOrderItem) || (orderItem instanceof DerivedSpecimenOrderItem) || (orderItem instanceof PathologicalCaseOrderItem)))
			{
				SpecimenOrderItem specimenOrderItem = (SpecimenOrderItem)orderItem;
				if(specimenOrderItem.getNewSpecimenArrayOrderItem() == null)
				{
				 	assignStatus = "RequestDetailsBean:"+requestDetailsBeanCounter+"_assignedStatus"; 	
					description = "RequestDetailsBean:"+requestDetailsBeanCounter+"_description";
					requestedQty = "RequestDetailsBean:"+requestDetailsBeanCounter+"_requestedQty";
							
					orderItemId = "RequestDetailsBean:"+requestDetailsBeanCounter+"_orderItemId";
					
					requestedItem = "RequestDetailsBean:"+requestDetailsBeanCounter+"_requestedItem";					
					availableQty = "RequestDetailsBean:"+requestDetailsBeanCounter+"_availableQty";
					specimenClass = "RequestDetailsBean:"+requestDetailsBeanCounter+"_className";
					specimenType = "RequestDetailsBean:"+requestDetailsBeanCounter+"_type";
					
					requestFor = "RequestDetailsBean:"+requestDetailsBeanCounter+"_requestFor";					
					specimenId = "RequestDetailsBean:"+requestDetailsBeanCounter+"_specimenId";
					assignQty = "RequestDetailsBean:"+requestDetailsBeanCounter+"_assignedQty";
					instanceOf = "RequestDetailsBean:"+requestDetailsBeanCounter+"_instanceOf";
					distributedItemId = "RequestDetailsBean:"+requestDetailsBeanCounter+"_distributedItemId";
					specimenCollGrpId = "RequestDetailsBean:"+requestDetailsBeanCounter+"_specimenCollGroupId";
					specimenList = "RequestForDropDownList:"+requestDetailsBeanCounter;
					
					actualSpecimenClass = "RequestDetailsBean:"+requestDetailsBeanCounter+"_actualSpecimenClass";
					actualSpecimenType = "RequestDetailsBean:"+requestDetailsBeanCounter+"_actualSpecimenType";
					
					populateValuesMap(orderItem,requestedItem,availableQty,specimenClass,specimenType,requestFor,specimenId,assignQty,instanceOf,specimenList,specimenCollGrpId,totalSpecimenListInRequestForDropDown,actualSpecimenClass,actualSpecimenType);
					requestDetailsBeanCounter++;
				}				
				else
				{
					assignStatus = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_assignedStatus"; 	
					description = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_description";
					requestedQty = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_requestedQuantity";
					orderItemId = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_orderItemId";
					
					requestedItem = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_requestedItem";					
					availableQty = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_availableQuantity";
					specimenClass = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_className";
					specimenType = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_type";
					
					requestFor = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_requestFor";					
					specimenId = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_specimenId";
					assignQty = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_assignedQuantity";
					instanceOf = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_instanceOf";
					//distributedItemId = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_distributedItemId";
					specimenList = "RequestForDropDownListArray:"+arrayDetailsBeanCounter;
					specimenCollGrpId = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_specimenCollGroupId";
					
					actualSpecimenClass = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_actualSpecimenClass";
					actualSpecimenType = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_actualSpecimenType";
					
					populateValuesMap(orderItem,requestedItem,availableQty,specimenClass,specimenType,requestFor,specimenId,assignQty,instanceOf,specimenList,specimenCollGrpId,totalSpecimenListInRequestForDropDown,actualSpecimenClass,actualSpecimenType);
					arrayDetailsBeanCounter++;
				}
			}
			else if(orderItem instanceof ExistingSpecimenArrayOrderItem)
			{
				assignStatus = "ExistingArrayDetailsBean:"+existingArrayBeanCounter+"_assignedStatus"; 	
				description = "ExistingArrayDetailsBean:"+existingArrayBeanCounter+"_description";				
				requestedQty = "ExistingArrayDetailsBean:"+existingArrayBeanCounter+"_requestedQuantity";
				orderItemId = "ExistingArrayDetailsBean:"+existingArrayBeanCounter+"_orderItemId";
				
				requestedItem = "ExistingArrayDetailsBean:"+existingArrayBeanCounter+"_bioSpecimenArrayName";
				arrayId = "ExistingArrayDetailsBean:"+existingArrayBeanCounter+"_arrayId";
				assignQty = "ExistingArrayDetailsBean:"+existingArrayBeanCounter+"_assignedQuantity";
				distributedItemId = "ExistingArrayDetailsBean:"+existingArrayBeanCounter+"_distributedItemId";
				
				ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem)orderItem;
				values.put(requestedItem, existingSpecimenArrayOrderItem.getSpecimenArray().getName());
				values.put(arrayId, existingSpecimenArrayOrderItem.getSpecimenArray().getId().toString());
				values.put(assignQty, "0.0");
								
				existingArrayBeanCounter++;
			}
			else if(orderItem instanceof NewSpecimenArrayOrderItem)
			{
				assignStatus = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_assignedStatus";
				orderItemId = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_orderItemId";
				
				requestedItem = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_arrayName";
				specimenClass = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_arrayClass";
				specimenType = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_arrayType";
				String positionDimensionOne = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_oneDimensionCapacity";
				String positionDimensionTwo = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_twoDimensionCapacity";
				
				arrayId = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_arrayId";
				distributedItemId = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_distributedItemId";
				String createArrayCondition = "DefinedArrayRequestBean:"+arrayRequestBeanCounter+"_createArrayButtonDisabled";
				
				NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem)orderItem;
				values.put(requestedItem, newSpecimenArrayOrderItem.getName());
				values.put(positionDimensionOne, newSpecimenArrayOrderItem.getSpecimenArrayType().getCapacity().getOneDimensionCapacity().toString());
				values.put(positionDimensionTwo, newSpecimenArrayOrderItem.getSpecimenArrayType().getCapacity().getTwoDimensionCapacity().toString());
				values.put(specimenClass, newSpecimenArrayOrderItem.getSpecimenArrayType().getSpecimenClass());
				values.put(specimenType, newSpecimenArrayOrderItem.getSpecimenArrayType().getName());
				
				SpecimenArray specimenArrayObj = newSpecimenArrayOrderItem.getSpecimenArray();
				if(specimenArrayObj != null)
				{
					values.put(arrayId, specimenArrayObj.getId().toString());
				}
				Collection specimenOrderItemCollection = newSpecimenArrayOrderItem.getSpecimenOrderItemCollection();		
				//Calculating the condition to enable or disable "Create Array Button"
				String condition = OrderingSystemUtil.determineCreateArrayCondition(specimenOrderItemCollection);
						
				if(orderItem.getStatus().trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED))
				{
					condition = "true"; 
				}
				values.put(createArrayCondition, condition);
				
				arrayRequestBeanCounter++;				
			}						
			values.put(assignStatus,orderItem.getStatus());
			values.put(description, orderItem.getDescription());	
			if(orderItem.getRequestedQuantity() != null)
			{//condition is for define array
				values.put(requestedQty, orderItem.getRequestedQuantity().getValue().toString());	
			}
			values.put(orderItemId,orderItem.getId());
			if(orderItem.getDistributedItem() != null)
			{
				values.put(distributedItemId, orderItem.getDistributedItem().getId().toString());
			}
			else
			{
				values.put(distributedItemId, "");
			}
		}
		request.getSession().removeAttribute("finalSpecimenList");
		request.getSession().setAttribute("finalSpecimenList",totalSpecimenListInRequestForDropDown);
	}
	/**
	 * @param orderItem
	 * @param requestedItem
	 * @param availableQty
	 * @param specimenClass
	 * @param specimenType
	 */
	private void populateValuesMap(OrderItem orderItem,String requestedItem,String availableQty,String specimenClass,String specimenType,String requestFor,String specimenId,String assignQty,String instanceOf,String specimenList,String specimenCollGrpId,List totalSpecimenListInRequestForDropDown,String actualSpecimenClass,String actualSpecimenType)
	{
		if(orderItem instanceof ExistingSpecimenOrderItem)
		{
			ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem)orderItem;
			values.put(requestedItem, existingSpecimenOrderItem.getSpecimen().getLabel());
			values.put(availableQty, existingSpecimenOrderItem.getSpecimen().getAvailableQuantity().getValue());
			values.put(specimenClass, existingSpecimenOrderItem.getSpecimen().getClassName());
			values.put(specimenType, existingSpecimenOrderItem.getSpecimen().getType());			
			values.put(specimenId,existingSpecimenOrderItem.getSpecimen().getId().toString());
			values.put(instanceOf, "Existing");
			if(existingSpecimenOrderItem.getDistributedItem() != null)
		  	{		  	
				values.put(assignQty, existingSpecimenOrderItem.getDistributedItem().getQuantity().toString());	
		  	}
			values.put(actualSpecimenClass,existingSpecimenOrderItem.getSpecimen().getClassName());
			values.put(actualSpecimenType, existingSpecimenOrderItem.getSpecimen().getType());
		}
		else if(orderItem instanceof DerivedSpecimenOrderItem)
		{
			DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem)orderItem;
			values.put(requestedItem, derivedSpecimenOrderItem.getParentSpecimen().getLabel());
			Collection childrenSpecimenList = OrderingSystemUtil.getAllChildrenSpecimen(derivedSpecimenOrderItem.getParentSpecimen(),derivedSpecimenOrderItem.getParentSpecimen().getChildrenSpecimen());
		    List finalChildrenSpecimenList = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childrenSpecimenList,derivedSpecimenOrderItem.getSpecimenClass(),derivedSpecimenOrderItem.getSpecimenType());
		    Iterator i = finalChildrenSpecimenList.iterator();
		    while(i.hasNext())
		    {//	Ajax  conditions
		    	totalSpecimenListInRequestForDropDown.add(i.next());
		    }
		    List childrenSpecimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(finalChildrenSpecimenList);
			if(childrenSpecimenListToDisplay.size() != 0)
		  	{
				values.put(availableQty, (((Specimen)finalChildrenSpecimenList.get(0)).getAvailableQuantity().getValue().toString()));
			}
		  	else
		  	{
		  		values.put(availableQty, "NA");//derivedSpecimenorderItem.getSpecimen().getAvailableQuantity().getValue().toString()	  		
		  	}			
			values.put(specimenClass, derivedSpecimenOrderItem.getSpecimenClass());
			values.put(specimenType, derivedSpecimenOrderItem.getSpecimenType());			
			values.put(specimenId,derivedSpecimenOrderItem.getParentSpecimen().getId().toString());
			values.put(instanceOf, "Derived");
			if(derivedSpecimenOrderItem.getDistributedItem() != null)
		  	{		  	
				values.put(assignQty, derivedSpecimenOrderItem.getDistributedItem().getQuantity().toString());
				values.put(requestFor, derivedSpecimenOrderItem.getDistributedItem().getSpecimen().getId());				
		  	}
			values.put(actualSpecimenClass,derivedSpecimenOrderItem.getParentSpecimen().getClassName());
			values.put(actualSpecimenType, derivedSpecimenOrderItem.getParentSpecimen().getType());
			requestForDropDownMap.put(specimenList, childrenSpecimenListToDisplay);			
		}	
		else if(orderItem instanceof PathologicalCaseOrderItem)
		{
			PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem)orderItem;
			values.put(requestedItem, pathologicalCaseOrderItem.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
			//Fetching requestFor list
			List totalChildrenSpecimenColl = OrderingSystemUtil.getRequestForListForPathologicalCases(pathologicalCaseOrderItem.getSpecimenCollectionGroup(), pathologicalCaseOrderItem);
			Iterator i = totalChildrenSpecimenColl.iterator();
			while(i.hasNext())
			{//	Ajax  conditions
				totalSpecimenListInRequestForDropDown.add(i.next());
			}
			List childrenSpecimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(totalChildrenSpecimenColl);
			requestForDropDownMap.put(specimenList, childrenSpecimenListToDisplay);
			values.put(specimenCollGrpId,pathologicalCaseOrderItem.getSpecimenCollectionGroup().getId().toString());
			if(childrenSpecimenListToDisplay.size() != 0)
		  	{
				values.put(availableQty, (((Specimen)totalChildrenSpecimenColl.get(0)).getAvailableQuantity().getValue().toString()));
			}
		  	else
		  	{
		  		values.put(availableQty, "NA");//derivedSpecimenorderItem.getSpecimen().getAvailableQuantity().getValue().toString()	  		
		  	}			
			if(childrenSpecimenListToDisplay.isEmpty()
				|| (pathologicalCaseOrderItem.getSpecimenClass() != null && pathologicalCaseOrderItem.getSpecimenType() != null && !pathologicalCaseOrderItem.getSpecimenClass().trim().equalsIgnoreCase("") && !pathologicalCaseOrderItem.getSpecimenType().trim().equalsIgnoreCase("")))
		    {
		    	values.put(instanceOf, "DerivedPathological");
		    }
		    else
		    {
		    	values.put(instanceOf, "Pathological");
		    }			
			if(pathologicalCaseOrderItem.getDistributedItem() != null)
		  	{		  	
				values.put(assignQty, pathologicalCaseOrderItem.getDistributedItem().getQuantity().toString());
				values.put(requestFor, pathologicalCaseOrderItem.getDistributedItem().getSpecimen().getId());				
		  	}	
			values.put(specimenClass, pathologicalCaseOrderItem.getSpecimenClass());
			values.put(specimenType, pathologicalCaseOrderItem.getSpecimenType());	
			values.put(actualSpecimenClass,pathologicalCaseOrderItem.getSpecimenClass());
			values.put(actualSpecimenType, pathologicalCaseOrderItem.getSpecimenType());
		}
	}
	private void populatePathologicalCaseOrderItem()
	{
		
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
	
	/**
	 * @return ActionErrors object
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 */
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
			Logger.out.debug("in request details form: map data parser exception:"+e);			
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


	
	/**
	 * @return the tabIndex
	 */
	public int getTabIndex()
	{
		return tabIndex;
	}


	
	/**
	 * @param tabIndex the tabIndex to set
	 */
	public void setTabIndex(int tabIndex)
	{
		this.tabIndex = tabIndex;
	}


	
	/**
	 * @return the requestForDropDownMap
	 */
	public Map getRequestForDropDownMap()
	{
		return requestForDropDownMap;
	}


	
	/**
	 * @param requestForDropDownMap the requestForDropDownMap to set
	 */
	public void setRequestForDropDownMap(Map requestForDropDownMap)
	{
		this.requestForDropDownMap = requestForDropDownMap;
	}

	
	/**
	 * @return the specimenId
	 */
	public String getSpecimenId()
	{
		return specimenId;
	}

	
	/**
	 * @param specimenId the specimenId to set
	 */
	public void setSpecimenId(String specimenId)
	{
		this.specimenId = specimenId;
	}

	@Override
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
		
		
	}
	
}
