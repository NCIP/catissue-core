/**
 * <p>Title: Distribution Class>
 * <p>Description:	This class initializes the fields in the  Distribution Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Aug 10, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the  Distribution Add/Edit webpage.
 * @author aniruddha_phadnis
 */
public class  DistributionAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
			
		DistributionBizLogic dao = (DistributionBizLogic)BizLogicFactory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
		
		//Sets the Site list.
		String sourceObjectName = Site.class.getName();
		String[] displayNameFields = {"name"};
		String valueField = Constants.SYSTEM_IDENTIFIER;
		
		List siteList = dao.getList(sourceObjectName, displayNameFields, valueField, true);
		
		request.setAttribute(Constants.FROM_SITE_LIST, siteList);
		request.setAttribute(Constants.TO_SITE_LIST, siteList);
		
		//Sets the Distribution Protocol Id List.
		sourceObjectName = DistributionProtocol.class.getName();
		String [] displayName = {"title"};
		
		List protocolList = dao.getList(sourceObjectName, displayName, valueField, true);
		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);
		
		//SET THE SPECIMEN Ids LIST
		String [] displayNameField = {Constants.SYSTEM_IDENTIFIER};
		List specimenList = dao.getList(Specimen.class.getName(), displayNameField, valueField, true);
		request.setAttribute(Constants.SPECIMEN_ID_LIST,specimenList);
		
		//			Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
		
	}
	protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
		super.executeSecureAction(mapping,form,request,response);
		
		DistributionForm dForm = (DistributionForm)form;
    	
    	//List of keys used in map of ActionForm
		List key = new ArrayList();
    	key.add("DistributedItem:i_Specimen_className");
    	key.add("DistributedItem:i_Specimen_systemIdentifier");
    	key.add("DistributedItem:i_quantity");
    	key.add("DistributedItem:i_tissueSite");
    	key.add("DistributedItem:i_tissueSide");
    	key.add("DistributedItem:i_pathologicalStatus");
    	key.add("DistributedItem:i_availableQty");
    	key.add("DistributedItem:i_Specimen_type");
    	
		// Mandar : code for Addnew Site data 24-Jan-06
		String siteID = (String)request.getAttribute(Constants.ADD_NEW_SITE_ID);
		if(siteID != null && siteID.trim().length() > 0 )
		{
			Logger.out.debug(">>>>>>>>>>><<<<<<<<<<<<<<<<>>>>>>>>>>>>> ToSite ID in Distribution Action : "+ siteID  );
			dForm.setToSite(siteID);
		}
		// -- 24-Jan-06 end
  	
    	//Gets the map from ActionForm
    	Map map = dForm.getValues();
    	
    	//Calling DeleteRow of BaseAction class
    	MapDataParser.deleteRow(key,map,request.getParameter("status"));
    	
		//Populate Distributed Items data in the Distribution page if specimen ID is changed. 
		if(dForm.isIdChange())
		{
			setSpecimenCharateristics(dForm,request);
		}
		Logger.out.debug("executeSecureAction");
		return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
	
	private void setSpecimenCharateristics(DistributionForm dForm,HttpServletRequest request) throws DAOException
	{
			//Set specimen characteristics according to the specimen ID changed
			DistributionBizLogic dao = (DistributionBizLogic)BizLogicFactory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
			
			String specimenIdKey = request.getParameter("specimenIdKey");
			//Parse key to get row number. Key is in the format DistributedItem:rowNo_Specimen_systemIdentifier
			MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.Distribution");
			int rowNo = parser.parseKeyAndGetRowNo(specimenIdKey);
			
			//int a = Integer.parseInt()
			Logger.out.debug("row number of the dist item: "+rowNo);
			String specimenId = (String)dForm.getValue("DistributedItem:"+rowNo+"_Specimen_systemIdentifier");
			//if '--Select--' is selected in the drop down of Specimen Id, empty the row values for that distributed item
			if(specimenId.equals("-1"))
			{
				dForm.setValue("DistributedItem:"+rowNo+"_Specimen_className",null);
				dForm.setValue("DistributedItem:"+rowNo+"_Specimen_type",null);
				dForm.setValue("DistributedItem:"+rowNo+"_tissueSite",null);
				dForm.setValue("DistributedItem:"+rowNo+"_tissueSide",null);
				dForm.setValue("DistributedItem:"+rowNo+"_pathologicalStatus",null);
				dForm.setValue("DistributedItem:"+rowNo+"_availableQty",null);
			}
			//retrieve the row values for the distributed item for the selected specimen id
			else
			{		
				List list = dao.retrieve(Specimen.class.getName(),Constants.SYSTEM_IDENTIFIER,specimenId);
				Specimen specimen =(Specimen)list.get(0);
				SpecimenCharacteristics specimenCharacteristics = specimen.getSpecimenCharacteristics();
				Logger.out.debug("SpecimenCharacteristics: "+specimenCharacteristics.getTissueSite()+","+
						specimenCharacteristics.getTissueSide()+","+specimenCharacteristics.getPathologicalStatus());
				// Set the values retrieved in the form to populate in the jsp.
				dForm.setValue("DistributedItem:"+rowNo+"_Specimen_className",specimen.getClassName());
				dForm.setValue("DistributedItem:"+rowNo+"_Specimen_type",specimen.getType());
				dForm.setValue("DistributedItem:"+rowNo+"_tissueSite",specimenCharacteristics.getTissueSite());
				dForm.setValue("DistributedItem:"+rowNo+"_tissueSide",specimenCharacteristics.getTissueSide());
				dForm.setValue("DistributedItem:"+rowNo+"_pathologicalStatus",specimenCharacteristics.getPathologicalStatus());
				dForm.setValue("DistributedItem:"+rowNo+"_availableQty",dForm.getAvailableQty(specimen));
				
			}
		
			Logger.out.debug("Map values after speci chars are set: "+dForm.getValues());
			//Set back the idChange boolean to false.
			dForm.setIdChange(false);
	}
		
}