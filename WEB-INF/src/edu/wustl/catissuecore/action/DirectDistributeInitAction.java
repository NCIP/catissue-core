package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.emory.mathcs.backport.java.util.Collections;
import edu.wustl.catissuecore.actionForm.OrderBiospecimenArrayForm;
import edu.wustl.catissuecore.actionForm.OrderForm;
import edu.wustl.catissuecore.actionForm.OrderPathologyCaseForm;
import edu.wustl.catissuecore.actionForm.OrderSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;
import edu.wustl.catissuecore.util.SpecimenComparator;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Variables;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.global.Permissions;
import edu.wustl.security.privilege.PrivilegeCache;
import edu.wustl.security.privilege.PrivilegeManager;

public class DirectDistributeInitAction extends BaseAction
{

	 /**
     * @param mapping ActionMapping object
     * @param form ActionForm object
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @return ActionForward object
     * @throws Exception object
     */
    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	String typeOf = request.getParameter("typeOf");
    	OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
		
    	SessionDataBean sessionData = getSessionData(request);
    	PrivilegeManager privilegeManager = PrivilegeManager.getInstance();
		PrivilegeCache privilegeCache = privilegeManager.getPrivilegeCache(sessionData.getUserName());
		
		User user = getUser(sessionData.getUserName(), sessionData.getUserId());
		List siteIdsList = (List)orderBizLogic.getUserSitesWithDistributionPrev(user,privilegeCache);
		boolean isValidTodistribute = false;
    	
    	if(typeOf.equals(Constants.SPECIMEN_ORDER_FORM_TYPE))
		{
    		OrderSpecimenForm orderSpecForm =(OrderSpecimenForm)form;
    		List specimenCollection = (List)orderBizLogic.getSpecimenDataFromDatabase(request);
    		isValidTodistribute = isValidToDistributeSpecimen(specimenCollection,siteIdsList);
    		Collections.sort(specimenCollection, new SpecimenComparator());
    		orderSpecForm.setValues(putValueInSpecimenMap(specimenCollection));
    		OrderForm orderFrom = (OrderForm) request.getSession().getAttribute("OrderForm");
    		orderSpecForm.setOrderForm(orderFrom);
    		orderSpecForm.setPageOf("specimen");
		}
    	else if(typeOf.equals(Constants.ARRAY_ORDER_FORM_TYPE))
    	{
    		
    		OrderBiospecimenArrayForm orderArrayForm = (OrderBiospecimenArrayForm) form;
    		List specimenArrayCollection = (List)orderBizLogic.getSpecimenArrayDataFromDatabase(request);
    		isValidTodistribute = isValidToDistributeSpecArray(specimenArrayCollection,siteIdsList);
    		orderArrayForm.setValues(putValueInArrayMap(specimenArrayCollection));
			//Obtain OrderForm instance from the session.
			OrderForm orderFrom = (OrderForm) request.getSession().getAttribute("OrderForm");
			orderArrayForm.setOrderForm(orderFrom);
			orderArrayForm.setPageOf("specimenArray");
    	}
    	else
    	{
    		OrderPathologyCaseForm pathologyForm = (OrderPathologyCaseForm) form;
    		List pathologyCollection = (List)orderBizLogic.getPathologyDataFromDatabase(request);
    		isValidTodistribute = isValidToDistributePathoCase(pathologyCollection,privilegeCache,sessionData);
    		pathologyForm.setValues(putValueInPathologyMap(pathologyCollection));
    		OrderForm orderFrom = (OrderForm) request.getSession().getAttribute("OrderForm");
    		pathologyForm.setOrderForm(orderFrom);
    		pathologyForm.setPageOf("pathologyCase");
    	}
    	if(!orderBizLogic.isSuperAdmin(user) && !isValidTodistribute)
		{
			ActionErrors errors = new ActionErrors();
	        ActionError error = new ActionError("access.denied.to.distribute");
	        errors.add(ActionErrors.GLOBAL_ERROR, error);
	        saveErrors(request, errors);
	        return mapping.findForward("failure");
		}
		
		return mapping.findForward("success");
    }
  
    /**
	 * It returns the user object
	 * @param dao
	 * @param userName
	 * @param userId
	 * @return
	 * @throws DAOException
	 */
	private User getUser(String userName,Long  userId) throws ApplicationException
	{	
		DAO dao = null;
		try
		{
			dao = AppUtility.openDAOSession();
			User user = (User) dao.retrieveById(User.class.getName(), userId);
			return user;
		}
		finally
		{
			AppUtility.closeDAOSession(dao);
		}
		
	}
	
	private boolean isValidToDistributeSpecimen(List specimenCollection,List siteIdsList)
	{
		boolean isValidToDistribute = true;
		
		Iterator<Specimen> specItr = specimenCollection.iterator();
		
		while(specItr.hasNext())
		{
			Specimen specimen = specItr.next();
			SpecimenPosition specimenPosition = specimen.getSpecimenPosition();
			if(specimenPosition != null)
			{	
				if(!siteIdsList.contains(specimenPosition.getStorageContainer().getSite().getId()))
				{
					isValidToDistribute = false;
					break;
				}
			}
		}
		
		
		return isValidToDistribute;
	}
	
	
	private boolean isValidToDistributeSpecArray(List specArrayCollection,List siteIdsList)
	{
		boolean isValidToDistribute = true;
		
		Iterator<SpecimenArray> specArrayItr = specArrayCollection.iterator();
		
		while(specArrayItr.hasNext())
		{
			SpecimenArray specimenArray = specArrayItr.next();
		
			StorageContainer storageContainer = (StorageContainer) specimenArray.getLocatedAtPosition().getParentContainer();
			if(!siteIdsList.contains(storageContainer.getSite().getId()))
			{
				isValidToDistribute = false;
				break;	
			}
		}
		
		return isValidToDistribute;
	}
	
	
	private boolean isValidToDistributePathoCase(List pathologyReports,PrivilegeCache privilegeCache,
			SessionDataBean sessionDataBean) throws ApplicationException
	{
		boolean isValidToDistribute = true;
		
		Iterator pathologyReportsIter = pathologyReports.iterator();
	
		try
		{
			while(pathologyReportsIter.hasNext())
			{
			SurgicalPathologyReport surgPathReports = (SurgicalPathologyReport)pathologyReportsIter.next();
			SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)surgPathReports.getSpecimenCollectionGroup();
			
			if(specimenCollectionGroup != null)
			{
							
				Long cpId = specimenCollectionGroup.getCollectionProtocolRegistration()
				.getCollectionProtocol().getId();
				String objectId = Constants.COLLECTION_PROTOCOL_CLASS_NAME+"_"+cpId;
				boolean isAuthorized = privilegeCache.hasPrivilege(objectId, Variables.privilegeDetailsMap.get(Constants.DISTRIBUTE_SPECIMENS));
				if(!isAuthorized)
				{
					isAuthorized = AppUtility.checkForAllCurrentAndFutureCPs(Permissions.DISTRIBUTION, sessionDataBean, cpId.toString());
				}
				
				if(!isAuthorized)
				{
					isValidToDistribute = false;
					break;
				}
			}
		}
		}
		catch(SMException smExp)
		{
			throw AppUtility.getApplicationException(smExp, "sm.operation.error",
			"Error in checking has privilege");
		}
		
		return isValidToDistribute;
	}
    /** 
	 * @param orderSpecimenFormObject OrderSpecimenForm instance
	 * @param request HttpServletRequest instance
	 * @return specimenMap HashMap instance
	 */
	private Map putValueInSpecimenMap(List specimenCollection)
	{
		Map specimenMap=new LinkedHashMap();
		Iterator specCollIter = specimenCollection.iterator();
		int counter = 0;
		boolean isValidToDistribute = false;
		while(specCollIter.hasNext())
    	{
			Specimen specimen = (Specimen)specCollIter.next();
			specimenMap.put("OrderSpecimenBean:"+counter+"_specimenId", specimen.getId().toString());
			
			//confirm abt this what is name here TODO
			specimenMap.put("OrderSpecimenBean:"+counter+"_specimenName",specimen.getLabel().toString());
			specimenMap.put("OrderSpecimenBean:"+counter+"_availableQuantity", specimen.getAvailableQuantity().toString());
			specimenMap.put("OrderSpecimenBean:"+counter+"_requestedQuantity", specimen.getAvailableQuantity().toString());
			specimenMap.put("OrderSpecimenBean:"+counter+"_description", "");
			
			//look for this TODO 
			specimenMap.put("OrderSpecimenBean:"+counter+"_unitRequestedQuantity","" );
					
			specimenMap.put("OrderSpecimenBean:"+counter+"_specimenClass",specimen.getClassName());
			specimenMap.put("OrderSpecimenBean:"+counter+"_specimenType",specimen.getSpecimenType());
			specimenMap.put("OrderSpecimenBean:"+counter+"_isDerived","false");
			
			specimenMap.put("OrderSpecimenBean:"+counter+"_checkedToRemove","");
			specimenMap.put("OrderSpecimenBean:"+counter+"_typeOfItem","specimen");
			specimenMap.put("OrderSpecimenBean:"+counter+"_distributionSite", "");
			specimenMap.put("OrderSpecimenBean:"+counter+"_arrayName","None");
			counter++;
    	}
		
		return specimenMap;
	}
	
	
	/**
	 * @param orderArrayFormObject OrderBiospecimenArrayForm instance
	 * @param request HttpServletRequest object
	 * @return arrayMap HashMap instance
	 */
	private Map putValueInArrayMap(List specimenArrays)
	{
		Map arrayMap=new HashMap();
		Iterator specArrayCollIter = specimenArrays.iterator();
		int counter = 0;
		while(specArrayCollIter.hasNext())
    	{
			SpecimenArray specimenArray = (SpecimenArray)specArrayCollIter.next();
			arrayMap.put("OrderSpecimenBean:"+counter+"_specimenId",specimenArray.getId().toString() );
			arrayMap.put("OrderSpecimenBean:"+counter+"_specimenName", specimenArray.getName());
			arrayMap.put("OrderSpecimenBean:"+counter+"_availableQuantity", "");
			arrayMap.put("OrderSpecimenBean:"+counter+"_requestedQuantity", "0.0");
			arrayMap.put("OrderSpecimenBean:"+counter+"_description", "");
			arrayMap.put("OrderSpecimenBean:"+counter+"_unitRequestedQuantity", "");
			arrayMap.put("OrderSpecimenBean:"+counter+"_isDerived","false");
			arrayMap.put("OrderSpecimenBean:"+counter+"_typeOfItem","specimenArray");
			arrayMap.put("OrderSpecimenBean:"+counter+"_arrayName","None");
			arrayMap.put("OrderSpecimenBean:"+counter+"_distributionSite", "");
			counter++;
	   	}
		return arrayMap;

	}
	
	/**
	 * @param pathologyFormObject OrderPathologyCaseForm object
	 * @param request HttpServletRequest object
	 * @return pathologyMap HashMap object
	 */
	private Map putValueInPathologyMap(List pathologyReports)
	{
		Map pathologyMap=new HashMap();
		Iterator pathologyReportsIter = pathologyReports.iterator();
		int counter = 0;
		while(pathologyReportsIter.hasNext())
    	{
			SurgicalPathologyReport surgPathReports = (SurgicalPathologyReport)pathologyReportsIter.next();
			pathologyMap.put("OrderSpecimenBean:"+counter+"_specimenId", surgPathReports.getId().toString());
			pathologyMap.put("OrderSpecimenBean:"+counter+"_specimenName", surgPathReports.getSpecimenCollectionGroup().getSurgicalPathologyNumber());
			pathologyMap.put("OrderSpecimenBean:"+counter+"_availableQuantity", "");
			pathologyMap.put("OrderSpecimenBean:"+counter+"_requestedQuantity", "1");
			pathologyMap.put("OrderSpecimenBean:"+counter+"_description","");
			pathologyMap.put("OrderSpecimenBean:"+counter+"_specimenCollectionGroup", surgPathReports.getSpecimenCollectionGroup().getId().toString());
			pathologyMap.put("OrderSpecimenBean:"+counter+"_collectionProtocol", surgPathReports.getSpecimenCollectionGroup().
									getCollectionProtocolRegistration().getCollectionProtocol().getTitle());

			pathologyMap.put("OrderSpecimenBean:"+counter+"_isDerived","true");
			pathologyMap.put("OrderSpecimenBean:"+counter+"_specimenClass","Tissue");
			pathologyMap.put("OrderSpecimenBean:"+counter+"_specimenType","Fixed Tissue Block");
			pathologyMap.put("OrderSpecimenBean:"+counter+"_unitRequestedQuantity","count");
			pathologyMap.put("OrderSpecimenBean:"+counter+"_typeOfItem","pathologyCase");
    		pathologyMap.put("OrderSpecimenBean:"+counter+"_arrayName","None");
    		pathologyMap.put("OrderSpecimenBean:"+counter+"_pathologicalStatus","");
    		pathologyMap.put("OrderSpecimenBean:"+counter+"_tissueSite","");
    		counter++;
    	    		
    	}
		return pathologyMap;

	}
	

}
