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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.SAXException;

import edu.wustl.catissuecore.actionForm.DefineArrayForm;
import edu.wustl.catissuecore.bean.RequestViewBean;
import edu.wustl.catissuecore.bean.SpecimenOrderBean;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.OrderGrid;
import edu.wustl.catissuecore.dto.OrderItemSubmissionDTO;
import edu.wustl.catissuecore.dto.RowDTO;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.dao.DAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;
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
			requestViewBean.setDistributionProtocol(order.getDistributionProtocol().getShortTitle());
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
				order.getRequestedDate(), CommonServiceLocator.getInstance().getTimeStampPattern()));
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
	public static List getAllSpecimen(Specimen specimen,Boolean getChildSpecimens) throws BizLogicException
	{
		final List allSpecimenList = new ArrayList();
		Set childSpecimens = new HashSet();
		allSpecimenList.add(specimen);
		if(getChildSpecimens)
		{
			childSpecimens = (Set) new DefaultBizLogic().retrieveAttribute(AbstractSpecimen.class,
					specimen.getId(), "elements(childSpecimenCollection)");
			final Iterator childSpec = childSpecimens.iterator();
			while (childSpec.hasNext())
			{
				final List subChildNodesList = getAllSpecimen((Specimen) childSpec.next(), getChildSpecimens);
				for (int i = 0; i < subChildNodesList.size(); i++)
				{
					//Order only collected specimens.
					Specimen specimenInner = (Specimen)subChildNodesList.get(i);
					if(!Status.ACTIVITY_STATUS_PENDING.toString().equals(specimenInner.getCollectionStatus()))
					{
						allSpecimenList.add(subChildNodesList.get(i));
					}
				}
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
				final List childSpecimenCollection = OrderingSystemUtil.getAllSpecimen(specimen,Boolean.FALSE);
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
	
	public static String getUnit(SpecimenOrderBean specimen)
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

	private static String getTissueSpecUnit(SpecimenOrderBean specimen)
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

	private static String setSpecimenQuantityUnit(SpecimenOrderBean specimen)
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
	
	public static Specimen getListOfSpecimen(Long specimenId) throws ApplicationException 
	{
		Specimen specimen = new Specimen();
		specimen.setId(specimenId);
		DAO dao = null;
		try
		{
			dao = AppUtility.openDAOSession(null);
			
			ColumnValueBean bean = new ColumnValueBean(specimenId);
			List<ColumnValueBean> columnBeanList = new ArrayList<ColumnValueBean>();
			columnBeanList.add(bean);
			String specimenConsentHql = "select sp.consentTierStatusCollection from" +
			" edu.wustl.catissuecore.domain.Specimen as sp where sp.id = ?";
			String cprHql = "select specimen.specimenCollectionGroup.collectionProtocolRegistration.id, " +
					"specimen.specimenCollectionGroup.collectionProtocolRegistration.consentSignatureDate," +
					"specimen.specimenCollectionGroup.collectionProtocolRegistration.signedConsentDocumentURL," +
					"specimen.specimenCollectionGroup.collectionProtocolRegistration.consentWitness.id," +
					"specimen.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.id," +
					"specimen.specimenCollectionGroup.collectionProtocolRegistration.collectionProtocol.consentsWaived from " +
					"edu.wustl.catissuecore.domain.Specimen as specimen where specimen.id = ?";
			
			
			List cprAttrList = dao.executeQuery(cprHql,columnBeanList);
			
			
			SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			User user = new User();
			CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
			CollectionProtocol cp = new CollectionProtocol();
			Object[] obj2 = (Object[])cprAttrList.get(0);
			cpr.setId(Long.valueOf(String.valueOf(obj2[0])));
			cp.setConsentsWaived(Boolean.FALSE);
			if(obj2[1] != null)
			{
				cpr.setConsentSignatureDate((Date)(obj2[1]));
			}
			if(obj2[2] != null)
			{
				cpr.setSignedConsentDocumentURL(String.valueOf(obj2[2]));
			}
			if(obj2[3] != null)
			{
				user.setId(Long.valueOf(String.valueOf(obj2[3])));
			}
			if(obj2[4] != null)
			{
				cp.setId(Long.valueOf(String.valueOf(obj2[4])));
			}
			if(obj2[5] != null)
			{
				cp.setConsentsWaived(Boolean.valueOf(String.valueOf(obj2[5])));
			}
			cpr.setConsentWitness(user);
			
			cpr.setCollectionProtocol(cp);
			scg.setCollectionProtocolRegistration(cpr);
			specimen.setSpecimenCollectionGroup(scg);
			if(!cp.getConsentsWaived())
			{
				List spConsentList = dao.executeQuery(specimenConsentHql,columnBeanList);
			//	specimen.setConsentTierStatusCollection(spConsentList);
			}
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
		
		return specimen;
	}
	
	public static SpecimenOrderBean getSpecimenOrderBean(
			final SpecimenOrderItem existingSpecimenorderItem, DAO dao)
			throws ApplicationException {
		SpecimenOrderBean bean = new SpecimenOrderBean();
		String sql = "select spec.identifier, spec.specimen_class, spec.specimen_type, spec.label, spec.AVAILABLE_QUANTITY " +
				" from catissue_specimen spec " +
				" join catissue_existing_sp_ord_item cat on spec.identifier = cat.specimen_id " +
				" where cat.identifier=?" +
				" union "+
				"select spec.identifier, spec.specimen_class, spec.specimen_type, spec.label, spec.AVAILABLE_QUANTITY " +
				" from catissue_specimen spec " +
				" join catissue_derieved_sp_ord_item cat on spec.identifier = cat.specimen_id " +
				" where cat.identifier=?";
		ColumnValueBean bean2 = new ColumnValueBean(
				existingSpecimenorderItem.getId());
		List attr = new ArrayList();
		attr.add(bean2);
		attr.add(bean2);
		// List list = dao.executeQuery(sql);
		List list = dao.executeQuery(sql, attr);
		List inrList = new ArrayList();
		if (list != null && !list.isEmpty()) {
			inrList = (List) list.get(0);
			bean.setId((String) inrList.get(0));
			bean.setSpecimenClass((String) inrList.get(1));
			bean.setSpecimenType((String) inrList.get(2));
			bean.setLabel((String) inrList.get(3));
			bean.setAvailableQty((String) inrList.get(4));
			List list3 = new ArrayList<NameValueBean>();
			list3.add(new NameValueBean(bean.getLabel(), bean.getId()));
			bean.setChildSpecimens(list3);
		}

			bean.setConsentAvl(true);


		return bean;
	}
	
	public static Map<Long, SpecimenOrderBean> getSpecItemDetails(Long orderDetId, JDBCDAO dao) throws DAOException
	{
		Map<Long, SpecimenOrderBean> map = new HashMap<Long, SpecimenOrderBean>();
		String sql ="select cat.identifier,spec.identifier, spec.specimen_class, spec.specimen_type, spec.label, spec.AVAILABLE_QUANTITY " +
				" from catissue_specimen spec, catissue_existing_sp_ord_item cat,catissue_order_item cot where " +
				" spec.identifier =cat.specimen_id and " +
				" cat.identifier=cot.identifier and order_id=? " +
				" union "+
				"select cat.identifier,spec.identifier, spec.specimen_class, spec.specimen_type, spec.label, spec.AVAILABLE_QUANTITY " +
				" from catissue_specimen spec, catissue_derieved_sp_ord_item cat,catissue_order_item cot where " +
				" spec.identifier =cat.specimen_id and " +
				" cat.identifier=cot.identifier and order_id=? ";
		ColumnValueBean bean = new ColumnValueBean(orderDetId);
		List attrList = new ArrayList();
		attrList.add(bean);
		attrList.add(bean);
		// List list = dao.executeQuery(sql);
		List list = dao.executeQuery(sql, attrList);
		if(list != null && list.size() > 0)
		{
			for (Object object : list) 
			{
				SpecimenOrderBean specBean = new SpecimenOrderBean();
				
				List obj = (List)object;
				Long key = Long.valueOf((String)obj.get(0));
				specBean.setId((String)obj.get(1));
				specBean.setSpecimenClass((String) obj.get(2));
				specBean.setSpecimenType((String) obj.get(3));
				specBean.setLabel((String) obj.get(4));
				specBean.setAvailableQty((String) obj.get(5));
				specBean.setConsentAvl(false);
				map.put(key,specBean);
			}
		}
		String secSql = "select cat.identifier,cp.consents_waived " +
				" from catissue_specimen spec, catissue_specimen_coll_group scg, " +
				" catissue_coll_prot_reg cpr, catissue_collection_protocol cp, " +
				" catissue_consent_tier_status cts,catissue_existing_sp_ord_item cat,catissue_order_item cot,catissue_consent_tier ct " +
				" where cts.specimen_id=cat.specimen_id and " +
				" cat.identifier=cot.identifier and order_id=? " +
				" and spec.identifier =cat.specimen_id " +
				" and spec.specimen_collection_group_id=scg.identifier " +
				" and cpr.identifier=scg.collection_protocol_reg_id " +
				" and cp.identifier=cpr.collection_protocol_id and ct.identifier = cts.consent_tier_id " +
				" union "+
				"select cat.identifier,cp.consents_waived " +
				" from catissue_specimen spec, catissue_specimen_coll_group scg, " +
				" catissue_coll_prot_reg cpr, catissue_collection_protocol cp, " +
				" catissue_consent_tier_status cts,catissue_derieved_sp_ord_item cat,catissue_order_item cot,catissue_consent_tier ct " +
				" where cts.specimen_id=cat.specimen_id and " +
				" cat.identifier=cot.identifier and order_id=? " +
				" and spec.identifier =cat.specimen_id " +
				" and spec.specimen_collection_group_id=scg.identifier " +
				" and cpr.identifier=scg.collection_protocol_reg_id " +
				" and cp.identifier=cpr.collection_protocol_id and ct.identifier = cts.consent_tier_id";
		// List list = dao.executeQuery(sql);
		List conRespList = dao.executeQuery(secSql, attrList);
		if(conRespList != null && conRespList.size() > 0)
		{
			for (Object object : conRespList)
			{
				List obj = (List)object;
				Long key = Long.valueOf((String)obj.get(0));
				SpecimenOrderBean sBean = map.get(key);
				sBean.setConsentAvl(true);
				boolean consentWaived = Boolean.valueOf((String)obj.get(1));
				sBean.setConsentWaived(consentWaived);
			}
		}
		return map;
	}

	public static Map<Long, Specimen> populateSpecimenMap(Long orderId) throws ApplicationException 
	{
		JDBCDAO jdbcdao = null;
		Map<Long, Specimen> specMap = new HashMap<Long, Specimen>();
		try
		{
			jdbcdao = AppUtility.openJDBCSession();
//			String consentSql="select cts.specimen_id,cts.identifier, cts.consent_tier_id,cts.status " +
//					" from catissue_consent_tier_status cts,catissue_existing_sp_ord_item cat,catissue_order_item cot " +
//					" where cts.specimen_id=cat.specimen_id and " +
//					" cat.identifier=cot.identifier and order_id=?";
			String consentSql = "select cts.specimen_id,cts.identifier, cts.consent_tier_id,cts.status,cp.consents_waived, " +
					" cpr.collection_protocol_id, cpr.identifier, ct.statement " +
					" from catissue_specimen spec, catissue_specimen_coll_group scg," +
					" catissue_coll_prot_reg cpr, catissue_collection_protocol cp," +
					" catissue_consent_tier_status cts,catissue_existing_sp_ord_item cat,catissue_order_item cot,catissue_consent_tier ct " +
					" where cts.specimen_id=cat.specimen_id and " +
					" cat.identifier=cot.identifier and order_id=? " +
					" and spec.identifier=cat.specimen_id " +
					" and spec.specimen_collection_group_id=scg.identifier " +
					" and cpr.identifier=scg.collection_protocol_reg_id " +
					" and cp.identifier=cpr.collection_protocol_id and ct.identifier = cts.consent_tier_id " +
					"union "+
					"select cts.specimen_id,cts.identifier, cts.consent_tier_id,cts.status,cp.consents_waived, " +
					" cpr.collection_protocol_id, cpr.identifier, ct.statement " +
					" from catissue_specimen spec, catissue_specimen_coll_group scg," +
					" catissue_coll_prot_reg cpr, catissue_collection_protocol cp," +
					" catissue_consent_tier_status cts,catissue_derieved_sp_ord_item cat,catissue_order_item cot,catissue_consent_tier ct " +
					" where cts.specimen_id=cat.specimen_id and " +
					" cat.identifier=cot.identifier and order_id=? " +
					" and spec.identifier=cat.specimen_id " +
					" and spec.specimen_collection_group_id=scg.identifier " +
					" and cpr.identifier=scg.collection_protocol_reg_id " +
					" and cp.identifier=cpr.collection_protocol_id and ct.identifier = cts.consent_tier_id";
			
			ColumnValueBean bean = new ColumnValueBean(orderId);
			List<ColumnValueBean> list = new ArrayList<ColumnValueBean>();
			list.add(bean);
			list.add(bean);
			List resultList = jdbcdao.executeQuery(consentSql, list);
			specMap = setListInMap(resultList);
			
//			if(!specMap.isEmpty())
//			{
//				String cprSql = "select spec.identifier, cpr.identifier,cpr.consent_sign_date,cpr.consent_doc_url,cpr.consent_witness,cpr.collection_protocol_id,cp.consents_waived " +
//						" from catissue_specimen spec, catissue_specimen_coll_group scg, catissue_coll_prot_reg cpr, catissue_collection_protocol cp, " +
//						" catissue_existing_sp_ord_item cat,catissue_order_item cot " +
//						" where spec.identifier=cat.specimen_id and " +
//						" cat.identifier=cot.identifier and order_id=? " +
//						" and spec.specimen_collection_group_id=scg.identifier " +
//						" and cpr.identifier=scg.collection_protocol_reg_id " +
//						" and cp.identifier=cpr.collection_protocol_id ";
//				
//				List cprResultList = jdbcdao.executeQuery(cprSql, list);
//				
//				updateSpecimenMap(specMap,cprResultList);
//			}
		}
		finally
		{
			AppUtility.closeJDBCSession(jdbcdao);
		}
		return specMap;
	}

	private static void updateSpecimenMap(Map<Long, Specimen> specMap,List cprResultList) 
	{
		Long specId;
		for (Object object : cprResultList) 
		{
			List rstLst = (List)object;
			specId = Long.valueOf(rstLst.get(0).toString());
			Specimen specimen = specMap.get(specId);
			
			SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
			User user = new User();
			CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
			CollectionProtocol cp = new CollectionProtocol();
			
			cpr.setId(Long.valueOf(String.valueOf(rstLst.get(1))));
			cp.setConsentsWaived(Boolean.FALSE);
			if(rstLst.get(2) != null)
			{
//				cpr.setConsentSignatureDate(new Date(rstLst.get(2).toString()));
			}
			if(rstLst.get(3) != null)
			{
				cpr.setSignedConsentDocumentURL(String.valueOf(rstLst.get(3)));
			}
			if(rstLst.get(4) != null)
			{
//				user.setId(Long.valueOf(String.valueOf(rstLst.get(4))));
			}
			if(rstLst.get(5) != null)
			{
				cp.setId(Long.valueOf(String.valueOf(rstLst.get(5))));
			}
			if(rstLst.get(6) != null)
			{
				cp.setConsentsWaived(Boolean.valueOf(String.valueOf(rstLst.get(6))));
			}
			cpr.setConsentWitness(user);
			
			cpr.setCollectionProtocol(cp);
			scg.setCollectionProtocolRegistration(cpr);
			specimen.setSpecimenCollectionGroup(scg);
			if(cp.getConsentsWaived())
			{
		//		specimen.getConsentTierStatusCollection().clear();
			}
		}
		
	}

	private static Map<Long, Specimen> setListInMap(List resultList) 
	{
		Map<Long, Specimen> specMap = new HashMap<Long, Specimen>();
		if(resultList != null && resultList.size() > 0)
		{
			Long specimenID;
			for (Object object : resultList) 
			{
				ConsentTierStatus status = new ConsentTierStatus();
				ConsentTier consentTier = new ConsentTier();
				
				List row = (List)object;
				specimenID = Long.valueOf(row.get(0).toString());
				consentTier.setId(Long.valueOf(row.get(2).toString()));
				consentTier.setStatement(String.valueOf(row.get(7)));
				status.setConsentTier(consentTier);
				status.setStatus(row.get(3).toString());
				status.setId(Long.valueOf(row.get(1).toString()));
				
				List<ConsentTierStatus> list = new ArrayList<ConsentTierStatus>();
				list.add(status);
				if(specMap.containsKey(specimenID))
				{
			//		specMap.get(specimenID).getConsentTierStatusCollection().add(status);
				}
				else
				{
					Specimen specimen = new Specimen();
					specimen.setId(specimenID);
				//	specimen.setConsentTierStatusCollection(list);
					specMap.put(specimenID, specimen);
					SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
					CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
					CollectionProtocol cp = new CollectionProtocol();
					cp.setId(Long.valueOf(String.valueOf(row.get(5))));
					cpr.setId(Long.valueOf(String.valueOf(row.get(6))));
					cp.setConsentsWaived(Boolean.FALSE);
					if(row.get(4) != null)
					{
						cp.setConsentsWaived(Boolean.valueOf(String.valueOf(row.get(4))));
					}
					
					cpr.setCollectionProtocol(cp);
					scg.setCollectionProtocolRegistration(cpr);
					specimen.setSpecimenCollectionGroup(scg);
					if(cp.getConsentsWaived())
					{
				//		specimen.getConsentTierStatusCollection().clear();
					}
					
				}
			}
		}
		return specMap;
	}
	public static ArrayList<OrderItemSubmissionDTO> getOrderItemDTOs(String orderItemXMLString) throws IOException, SAXException
	{
		final String rulesFileLocation ="OrderItemRule.xml";
		DigesterLoader digesterLoader = DigesterLoader.newLoader(new XmlRulesModule(rulesFileLocation));
		Digester digester = digesterLoader.newDigester();
		OrderGrid orderIGrid=digester.parse(new StringReader(orderItemXMLString));
		return convertGridToDTO(orderIGrid);
	}
	private static ArrayList<OrderItemSubmissionDTO> convertGridToDTO(OrderGrid orderGrid)
	{
		Collection<RowDTO> rowDTOs=orderGrid.getRowDTOs();
		Collection<OrderItemSubmissionDTO> orderItemSubmissionDTOs=new ArrayList<OrderItemSubmissionDTO>();
		
		for (RowDTO rowDTO : rowDTOs) {
			List<String> cells=rowDTO.getCells();
			OrderItemSubmissionDTO orderItemSubmissionDTO=new OrderItemSubmissionDTO();
			orderItemSubmissionDTO.setSpecimenLabel(cells.get(0));
			orderItemSubmissionDTO.setRequestedQty(Double.parseDouble(cells.get(4)));
			orderItemSubmissionDTO.setDistQty(Double.parseDouble(cells.get(6)));
			orderItemSubmissionDTO.setOrderitemId(Long.parseLong(cells.get(9)));
			orderItemSubmissionDTO.setSpecimenId(Long.parseLong(cells.get(10)));
			orderItemSubmissionDTO.setStatus(cells.get(7));
			orderItemSubmissionDTO.setComments(cells.get(8));
			orderItemSubmissionDTOs.add(orderItemSubmissionDTO);	
		}
		
		return (ArrayList<OrderItemSubmissionDTO>)orderItemSubmissionDTOs;
	}

}
