/**
 * <p>Title: GetConsents Class>
 * <p>Description:	Ajax Action Class for Checking if consents available or not.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Virender Mehta
 * @version 1.00
 * Created on Jan 18,2007
 */
package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CollectionProtocolRegistrationForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ConsentTier;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * @author Virender Mehta
 *
 */
public class GetConsents extends BaseAction
{

   /**
     * Overrides the execute method in Action class.
     * @param mapping ActionMapping object
     * @param form ActionForm object
     * @param request HttpServletRequest object
     * @param response HttpServletResponse object
     * @return ActionForward object
     * @throws Exception object
     */
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		CollectionProtocolRegistrationForm collectionProtocolRegistrationForm = (CollectionProtocolRegistrationForm)form;
		String cp_id=request.getParameter("id");
		String showConsents = request.getParameter(Constants.SHOW_CONSENTS);	
		if(!cp_id.equals("-1"))
		{
			if(showConsents!=null && showConsents.equalsIgnoreCase(Constants.YES))
			{
				//Adding name,value pair in NameValueBean
				//Getting witness name list for CollectionProtocolID
				List witnessList = witnessNameList(cp_id);
				//Getting ResponseList if Operation=Edit then "Withdraw" is added to the List 
				List responseList= Utility.responceList(Constants.ADD);
				List requestConsentList = getConsentList(cp_id);
				Map tempMap=prepareConsentMap(requestConsentList);
				collectionProtocolRegistrationForm.setConsentResponseValues(tempMap);
				collectionProtocolRegistrationForm.setConsentTierCounter(requestConsentList.size());
				
				request.setAttribute("witnessList", witnessList);			
				request.setAttribute("responseList", responseList);
				request.setAttribute(Constants.PAGEOF,"pageOfCollectionProtocolRegistration");
				request.setAttribute(Constants.OPERATION,Constants.ADD);
			}		
		} 
	    return mapping.findForward("success");
	}
	
		
	
/**
 * Adding name,value pair in NameValueBean for Witness Name
 * @param collProtId Get Witness List for this ID
 * @return consentWitnessList
 */ 
private List witnessNameList(String collProtId) throws DAOException
{		
	IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
	String colName = Constants.ID;
	List collProtList = bizLogic.retrieve(CollectionProtocol.class.getName(), colName, collProtId);		
	CollectionProtocol collectionProtocol = (CollectionProtocol)collProtList.get(0);
	//Setting the consent witness
	String witnessFullName="";
	List consentWitnessList = new ArrayList();
	consentWitnessList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
	Collection userColl = collectionProtocol.getCoordinatorCollection();
	Iterator iter = userColl.iterator();
	while(iter.hasNext())
	{
		User user = (User)iter.next();
		witnessFullName = user.getLastName()+", "+user.getFirstName();
		consentWitnessList.add(new NameValueBean(witnessFullName,user.getId()));
	}		
	//Setting the PI
	User principalInvestigator = collectionProtocol.getPrincipalInvestigator();
	String piFullName=principalInvestigator.getLastName()+", "+principalInvestigator.getFirstName();
	consentWitnessList.add(new NameValueBean(piFullName,principalInvestigator.getId()));
	
	return consentWitnessList;
}	

/**
 * Adding name,value pair in NameValueBean for Witness Name
 * @param collProtId Get Witness List for this ID
 * @return consentWitnessList
 */ 
public List getConsentList(String collectionProtocolID) throws DAOException
{   	
	CollectionProtocolBizLogic collectionProtocolBizLogic = (CollectionProtocolBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
	String colName = "id";		
	List collProtList  = collectionProtocolBizLogic.retrieve(CollectionProtocol.class.getName(), colName, collectionProtocolID);		
	CollectionProtocol collectionProtocol = (CollectionProtocol)collProtList.get(0);
	//Setting consent tiers
	Set consentList = (Set)collectionProtocol.getConsentTierCollection();
	List finalConsentList = new ArrayList(consentList);
	return finalConsentList;
}
/**
 * Prepare map for Showing Consents for a CollectionprotocolID when Operation=Add
 * @param requestConsentList This is the List of Consents for a selected  CollectionProtocolID
 * @return tempMap
 */
private Map prepareConsentMap(List requestConsentList)
{
	Map tempMap = new HashMap(); 
	if(requestConsentList!=null)
	{
		int i=0;
		Iterator consentTierCollectionIter = requestConsentList.iterator();
		String idKey=null;
		String statementKey=null;
		while(consentTierCollectionIter.hasNext())
		{
			ConsentTier consent=(ConsentTier)consentTierCollectionIter.next();
			idKey="ConsentBean:"+i+"_consentTierID";
			statementKey="ConsentBean:"+i+"_statement";
									
			tempMap.put(idKey, consent.getId());
			tempMap.put(statementKey,consent.getStatement());
			i++;
		}
	}
	return tempMap;
}
        
	
 }
