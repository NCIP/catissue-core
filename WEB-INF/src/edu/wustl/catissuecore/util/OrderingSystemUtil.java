/**
 * <p>Title: OrderingSystemUtil Class>
 * <p>Description:	AppUtility class for Ordering System</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Oct 09,2006
 */

package edu.wustl.catissuecore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.wustl.catissuecore.actionForm.DefineArrayForm;
import edu.wustl.catissuecore.bean.RequestViewBean;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.dao.util.HibernateMetaData;

public final class OrderingSystemUtil
{

	/*
	 * creates a singleton object
	 */

	private static OrderingSystemUtil orderingSysUtil = new OrderingSystemUtil();

	/*
	 * Private constructor
	 */
	private OrderingSystemUtil()
	{

	}

	/*
	 * returns a singleton object 
	 */
	public static OrderingSystemUtil getInstance()
	{
		return orderingSysUtil;
	}

	public static void getPossibleStatusForDistribution(List<NameValueBean> possibleStatusList)
	{
		possibleStatusList.add(new NameValueBean(
				Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION,
				Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION));
		possibleStatusList.add(new NameValueBean(
				Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST,
				Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST));
		possibleStatusList.add(new NameValueBean(
				Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE,
				Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE));
		possibleStatusList.add(new NameValueBean(
				Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE,
				Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE));
		possibleStatusList.add(new NameValueBean(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED,
				Constants.ORDER_REQUEST_STATUS_DISTRIBUTED));
		possibleStatusList.add(new NameValueBean(
				Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE,
				Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE));
	}

	/**
	 * @param initialStatus The initial status from Db.
	 * @return possibleStatusList
	 */
	public static List<NameValueBean> getPossibleStatusList(String initialStatus)
	{
		final List<NameValueBean> possibleStatusList = new ArrayList();
		if (initialStatus.trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_NEW))
		{
			possibleStatusList.add(new NameValueBean(Constants.ORDER_REQUEST_STATUS_NEW,
					Constants.ORDER_REQUEST_STATUS_NEW));
			possibleStatusList.add(new NameValueBean(
					Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW,
					Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW));
			possibleStatusList.add(new NameValueBean(
					Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION,
					Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION));
			getPossibleStatusForDistribution(possibleStatusList);
		}
		if (initialStatus.trim().equalsIgnoreCase(
				Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW))
		{
			possibleStatusList.add(new NameValueBean(
					Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW,
					Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW));
			possibleStatusList.add(new NameValueBean(
					Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION,
					Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION));
			getPossibleStatusForDistribution(possibleStatusList);
		}
		else if (initialStatus.trim().equalsIgnoreCase(
				Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION))
		{
			possibleStatusList.add(new NameValueBean(
					Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION,
					Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION));
			getPossibleStatusForDistribution(possibleStatusList);

		}
		else if (initialStatus.trim().equalsIgnoreCase(
				Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION))
		{
			getPossibleStatusForDistribution(possibleStatusList);
		}
		else if (initialStatus.trim().equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED))
		{
			possibleStatusList.add(new NameValueBean(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED,
					Constants.ORDER_REQUEST_STATUS_DISTRIBUTED));
		}
		else if (initialStatus.trim().equalsIgnoreCase(
				Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST))
		{
			possibleStatusList.add(new NameValueBean(
					Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST,
					Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST));
		}
		else if (initialStatus.trim().equalsIgnoreCase(
				Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE))
		{
			possibleStatusList.add(new NameValueBean(
					Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE,
					Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE));
		}
		else if (initialStatus.trim().equalsIgnoreCase(
				Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE))
		{
			possibleStatusList.add(new NameValueBean(
					Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE,
					Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE));
		}
		else if (initialStatus.trim().equalsIgnoreCase(
				Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE))
		{
			possibleStatusList.add(new NameValueBean(
					Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE,
					Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE));
		}
		return possibleStatusList;
	}

	/**
	 * @param order OrderDetails
	 * @return RequestViewBean object.
	 */
	public static RequestViewBean getRequestViewBeanToDisplay(OrderDetails order)
	{
		final RequestViewBean requestViewBean = new RequestViewBean();

		if (order.getDistributionProtocol() != null && order.getName() != null)
		{
			requestViewBean.setDistributionProtocol(order.getDistributionProtocol().getTitle());
			requestViewBean.setDistributionProtocolId(order.getDistributionProtocol().getId()
					.toString());
			requestViewBean.setOrderName(order.getName());
			requestViewBean.setRequestedBy(order.getDistributionProtocol()
					.getPrincipalInvestigator().getLastName()
					+ ", "
					+ order.getDistributionProtocol().getPrincipalInvestigator().getFirstName());
			requestViewBean.setEmail(order.getDistributionProtocol().getPrincipalInvestigator()
					.getEmailAddress());
		}
		else
		{
			requestViewBean.setDistributionProtocol("");
			requestViewBean.setDistributionProtocolId("");
			requestViewBean.setOrderName("Order_" + order.getId().toString());
			requestViewBean.setRequestedBy("");
			requestViewBean.setEmail("");
		}
		requestViewBean.setRequestedDate(CommonUtilities.parseDateToString(
				order.getRequestedDate(), CommonServiceLocator.getInstance().getDatePattern()));
		return requestViewBean;
	}

	/**
	 * @param rootNode Specimen
	 * @param childNodes Collection
	 * @return List. ArrayList of children Specimen objects.
	 */
	public static List getAllChildrenSpecimen(Collection childNodes)
	{
		List returnColl = null;
		final ArrayList childrenSpecimenList = new ArrayList();
		//If no childNodes present then tree will contain only the root node.
		if (childNodes != null)
		{
			//Otherwise
			final Iterator<Specimen> specimenItr = childNodes.iterator();
			while (specimenItr.hasNext())
			{
				final Specimen specimen = specimenItr.next();
				getAllChildrenSpecimen(specimen.getChildSpecimenCollection());
				childrenSpecimenList.add(specimen);
			}
			returnColl = childrenSpecimenList;
		}
		return returnColl;
	}

	/**
	 * This method gets All Specimen.
	 * @param specimen Specimen
	 * @return all Specimen List
	 * @throws BizLogicException BizLogic Exception
	 */
	public static List getAllSpecimen(Specimen specimen) throws BizLogicException
	{
		final List allSpecimenList = new ArrayList();
		Set childSpecimens = new HashSet();
		allSpecimenList.add(specimen);
		childSpecimens = (Set) new DefaultBizLogic().retrieveAttribute(AbstractSpecimen.class,
				specimen.getId(), "elements(childSpecimenCollection)");
		final Iterator childSpec = childSpecimens.iterator();
		while (childSpec.hasNext())
		{
			final List subChildNodesList = getAllSpecimen((Specimen) childSpec.next());
			for (int i = 0; i < subChildNodesList.size(); i++)
			{
				allSpecimenList.add(subChildNodesList.get(i));
			}
		}
		return allSpecimenList;
	}

	/**
	 * @param childrenSpecimenList Collection
	 * @param className String
	 * @param type String
	 * @return List. The namevaluebean list of children specimen of particular 'class' and 'type' to display.
	 */
	public static List getChildrenSpecimenForClassAndType(Collection childrenSpecimenList,
			String className, String type)
	{
		final List finalChildrenSpecimenList = new ArrayList();
		final Iterator childrenSpecimenListIterator = childrenSpecimenList.iterator();
		while (childrenSpecimenListIterator.hasNext())
		{
			Specimen childrenSpecimen = (Specimen) childrenSpecimenListIterator.next();
			childrenSpecimen = (Specimen) HibernateMetaData.getProxyObjectImpl(childrenSpecimen);
			if (childrenSpecimen.getSpecimenClass().trim().equalsIgnoreCase(className)
					&& childrenSpecimen.getSpecimenType().trim().equalsIgnoreCase(type)
					&& childrenSpecimen.getAvailableQuantity() > 0)
			{
				finalChildrenSpecimenList.add(childrenSpecimen);
			}
		}
		return finalChildrenSpecimenList;
	}

	/**
	 * @param listToConvert Collection
	 * @return List. The namevaluebean list of children specimen to display.
	 */
	public static List<NameValueBean> getNameValueBeanList(Collection listToConvert,
			Specimen requestFor)
	{
		final List<NameValueBean> nameValueBeanList = new ArrayList<NameValueBean>();

		if (requestFor != null)
		{
			nameValueBeanList.add(0, new NameValueBean(requestFor.getLabel(), requestFor.getId()
					.toString()));
		}
		else
		{
			nameValueBeanList.add(0, new NameValueBean(" ", "#"));
		}

		final Iterator<Specimen> iter = listToConvert.iterator();
		while (iter.hasNext())
		{
			final Specimen specimen = iter.next();
			final NameValueBean nameValueBean = new NameValueBean(specimen.getLabel(), specimen
					.getId().toString());
			if (!nameValueBeanList.contains(nameValueBean))
			{
				nameValueBeanList.add(nameValueBean);
			}
		}

		return nameValueBeanList;
	}

	/**
	 * @param listToConvert Collection
	 * @return List. The namevaluebean list of children specimen to display.
	 */
	public static List<NameValueBean> getNameValueBean(Specimen specimen)
	{
		final List<NameValueBean> nameValueBeanList = new ArrayList();
		nameValueBeanList.add(new NameValueBean(specimen.getLabel(), specimen.getId().toString()));
		final Iterator iter = specimen.getChildSpecimenCollection().iterator();
		while (iter.hasNext())
		{
			final Specimen childSpecimen = (Specimen) iter.next();
			nameValueBeanList.add(new NameValueBean(childSpecimen.getLabel(), childSpecimen.getId()
					.toString()));
		}

		return nameValueBeanList;
	}

	/**
	 * @param specimenCollectionGroup
	 * @param pathologicalCaseOrderItem
	 * @return
	 */
	/*public static List getRequestForListForPathologicalCases(SpecimenCollectionGroup specimenCollectionGroup,PathologicalCaseOrderItem pathologicalCaseOrderItem)
	{
		Collection childrenSpecimenList = specimenCollectionGroup.getSpecimenCollection();
		List totalChildrenSpecimenColl = new ArrayList();
		//List childrenSpecimenListToDisplay = new ArrayList();
		
		Iterator childrenSpecimenListIterator = childrenSpecimenList.iterator();
		while (childrenSpecimenListIterator.hasNext())
		{
			Specimen specimen = (Specimen)childrenSpecimenListIterator.next();
			List childSpecimenCollection = OrderingSystemUtil.getAllChildrenSpecimen(specimen.getChildSpecimenCollection());
			List finalChildrenSpecimenCollection = null;
			if(pathologicalCaseOrderItem.getSpecimenClass() != null && pathologicalCaseOrderItem.getSpecimenType() != null && !pathologicalCaseOrderItem.getSpecimenClass().trim().equalsIgnoreCase("") && !pathologicalCaseOrderItem.getSpecimenType().trim().equalsIgnoreCase(""))
		    {	//"Derived"	
				finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childSpecimenCollection,pathologicalCaseOrderItem.getSpecimenClass(),pathologicalCaseOrderItem.getSpecimenType());
			}	    	
		    else
		    {  	//"Block" . Specimen class = "Tissue" , Specimen Type = "Block".
		    	finalChildrenSpecimenCollection = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childSpecimenCollection,"Tissue","Block");
		    }
			//adding specimen to the collection
			if(specimen.getClassName().equalsIgnoreCase(pathologicalCaseOrderItem.getSpecimenClass()) && specimen.getSpecimenType().equalsIgnoreCase(pathologicalCaseOrderItem.getSpecimenType()))
			{
				finalChildrenSpecimenCollection.add(specimen);
			}
			if(finalChildrenSpecimenCollection!=null)
			{
				Iterator finalChildrenSpecimenCollectionIterator = finalChildrenSpecimenCollection.iterator();
				while(finalChildrenSpecimenCollectionIterator.hasNext())
				{	    		
					totalChildrenSpecimenColl.add((Specimen)(finalChildrenSpecimenCollectionIterator.next()));
				}	    			
			}
		} 	
		
		return totalChildrenSpecimenColl;
	}*/

	/**
	 * @param specimenCollectionGroup
	 * @param pathologicalCaseOrderItem
	 * @return
	 * @throws BizLogicException BizLogic Exception
	 */
	public static List getAllSpecimensForPathologicalCases(
			SpecimenCollectionGroup specimenCollectionGroup,
			PathologicalCaseOrderItem pathologicalCaseOrderItem) throws BizLogicException
	{
		final Collection specimenList = specimenCollectionGroup.getSpecimenCollection();
		final List totalSpecimenColl = new ArrayList();

		final Iterator specimenListIterator = specimenList.iterator();
		while (specimenListIterator.hasNext())
		{
			final Specimen specimen = (Specimen) specimenListIterator.next();
			if (specimen.getClassName().equalsIgnoreCase(
					pathologicalCaseOrderItem.getSpecimenClass())
					&& specimen.getSpecimenType().equalsIgnoreCase(
							pathologicalCaseOrderItem.getSpecimenType()))
			{
				totalSpecimenColl.add(specimen);
				final List childSpecimenCollection = OrderingSystemUtil.getAllSpecimen(specimen);
				if (childSpecimenCollection != null)
				{
					final Iterator childSpecimenCollectionIterator = childSpecimenCollection
							.iterator();
					while (childSpecimenCollectionIterator.hasNext())
					{
						totalSpecimenColl.add((childSpecimenCollectionIterator.next()));
					}
				}
			}
		}

		return totalSpecimenColl;
	}

	/**
	 * @param specimenOrderItemCollection collection
	 * @param arrayRequestBean objetc
	 * @return DefinedArrayRequestBean object
	 */
	public static String determineCreateArrayCondition(Collection specimenOrderItemCollection)
	{
		final Iterator iter = specimenOrderItemCollection.iterator();
		int readyCounter = 0;
		int rejectedCounter = 0;
		int pendingCounter = 0;
		int newCounter = 0;
		while (iter.hasNext())
		{
			final SpecimenOrderItem specimenOrderItem = (SpecimenOrderItem) iter.next();
			if (specimenOrderItem.getStatus().trim().equalsIgnoreCase(
					Constants.ORDER_REQUEST_STATUS_NEW))
			{
				newCounter++;
			}
			else if (specimenOrderItem.getStatus().trim().equalsIgnoreCase(
					Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION)
					|| specimenOrderItem.getStatus().trim().equalsIgnoreCase(
							Constants.ORDER_REQUEST_STATUS_PENDING_PROTOCOL_REVIEW)
					|| specimenOrderItem.getStatus().trim().equalsIgnoreCase(
							Constants.ORDER_REQUEST_STATUS_PENDING_SPECIMEN_PREPARATION))
			{
				pendingCounter++;
			}
			else if (specimenOrderItem.getStatus().trim().equalsIgnoreCase(
					Constants.ORDER_REQUEST_STATUS_REJECTED_INAPPROPRIATE_REQUEST)
					|| specimenOrderItem.getStatus().trim().equalsIgnoreCase(
							Constants.ORDER_REQUEST_STATUS_REJECTED_SPECIMEN_UNAVAILABLE)
					|| specimenOrderItem.getStatus().trim().equalsIgnoreCase(
							Constants.ORDER_REQUEST_STATUS_REJECTED_UNABLE_TO_CREATE))
			{
				rejectedCounter++;
			}
			else if (specimenOrderItem.getStatus().trim().equalsIgnoreCase(
					Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION))
			{
				readyCounter++;
			}
		}
		if ((newCounter > 0) || (pendingCounter > 0)
				|| (specimenOrderItemCollection.size() == rejectedCounter)
				|| (specimenOrderItemCollection.size() != rejectedCounter + readyCounter))
		{
			return "true";
		}
		else
		{
			return "false";
		}
	}

	public static String getUnit(Specimen specimen)
	{
		String specimenQuantityUnit = "";
		if (specimen.getSpecimenClass().equals("Tissue"))
		{
			specimenQuantityUnit = getTissueSpecUnit(specimen);
		}
		else if (specimen.getSpecimenClass().equals("Fluid"))
		{
			specimenQuantityUnit = Constants.UNIT_ML;
		}
		else if (specimen.getSpecimenClass().equals("Cell"))
		{
			specimenQuantityUnit = Constants.UNIT_CC;
		}
		else if (specimen.getSpecimenClass().equals("Molecular"))
		{
			specimenQuantityUnit = Constants.UNIT_MG;
		}
		return specimenQuantityUnit;
	}

	/**
	 * method returns specimenQuantityUnit for Particular specimen type.
	 * @param specimen
	 * @return
	 */
	private static String getTissueSpecUnit(Specimen specimen)
	{
		String specimenQuantityUnit = "";
		if (specimen.getSpecimenType().equals(Constants.FROZEN_TISSUE_SLIDE)
				|| specimen.getSpecimenType().equals(Constants.FIXED_TISSUE_BLOCK)
				|| specimen.getSpecimenType().equals(Constants.FROZEN_TISSUE_BLOCK)
				|| specimen.getSpecimenType().equals(Constants.NOT_SPECIFIED)
				|| specimen.getSpecimenType().equals(Constants.FIXED_TISSUE_SLIDE))
		{
			specimenQuantityUnit = Constants.UNIT_CN;

		}
		else
		{
			specimenQuantityUnit = setSpecimenQuantityUnit(specimen);
		}
		return specimenQuantityUnit;
	}

	private static String setSpecimenQuantityUnit(Specimen specimen)
	{
		String specimenQuantityUnit;
		if (specimen.getSpecimenType().equals(Constants.MICRODISSECTED))
		{
			specimenQuantityUnit = Constants.UNIT_CL;
		}
		else
		{
			specimenQuantityUnit = Constants.UNIT_GM;
		}
		return specimenQuantityUnit;
	}

	public static void setSpecimenTypeAndClass(HttpServletRequest request)
	{
		//Setting specimen class list
		List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_CLASS, null);
		//request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		//Setting the specimen type list
		final List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(
				Constants.CDE_NAME_SPECIMEN_TYPE, null);
		request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);

		// Get the Specimen class and type from the cde
		final CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(
				Constants.CDE_NAME_SPECIMEN_CLASS);
		final Set setPV = specimenClassCDE.getPermissibleValues();

		specimenClassList = new ArrayList();
		final Map subTypeMap = AppUtility.getSubTypeMap(setPV, specimenClassList);

		// sets the Class list
		request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

		// set the map to subtype
		request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);
	}

	public static List getDefinedArraysList(final HttpSession session)
	{

		final List defineArraysList = new ArrayList();
		defineArraysList.add(new NameValueBean("None", "None"));

		if (session.getAttribute("DefineArrayFormObjects") != null)
		{
			final List arrayList = (ArrayList) session.getAttribute("DefineArrayFormObjects");
			final Iterator arrayListItr = arrayList.iterator();
			while (arrayListItr.hasNext())
			{
				final DefineArrayForm defineArrayFormObj = (DefineArrayForm) arrayListItr.next();
				defineArraysList.add(new NameValueBean(defineArrayFormObj.getArrayName(),
						defineArrayFormObj.getArrayName()));
			}
		}
		return defineArraysList;
	}
}
