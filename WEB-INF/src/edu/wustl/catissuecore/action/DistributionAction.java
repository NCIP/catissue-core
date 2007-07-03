/**
 * <p>Title: Distribution Class>
 * <p>Description:        This class initializes the fields in the  Distribution Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Aug 10, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the  Distribution Add/Edit webpage.
 * @author aniruddha_phadnis
 */
public class DistributionAction extends SpecimenEventParametersAction
{

	/**
	 * @param request object of HttpServletRequest
	 * @throws Exception generic exception
	 */
	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
		DistributionBizLogic dao = (DistributionBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		//Sets the Site list.
		String sourceObjectName = Site.class.getName();
		String[] displayNameFields = {"name"};
		String valueField = Constants.SYSTEM_IDENTIFIER;

		List siteList = dao.getList(sourceObjectName, displayNameFields, valueField, true);

		request.setAttribute(Constants.FROM_SITE_LIST, siteList);
		request.setAttribute(Constants.TO_SITE_LIST, siteList);

		//Sets the Distribution Protocol Id List.
		sourceObjectName = DistributionProtocol.class.getName();

		String[] displayName = {"title"};

		List protocolList = dao.getList(sourceObjectName, displayName, valueField, true);
		request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);

		//SET THE SPECIMEN Ids LIST
		String[] displayNameField = {Constants.SYSTEM_IDENTIFIER};
		List specimenList = dao.getList(Specimen.class.getName(), displayNameField, valueField, true);
		request.setAttribute(Constants.SPECIMEN_ID_LIST, specimenList);

		//			Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

		List distributionType = new ArrayList();
		distributionType.add(new NameValueBean("Specimen", new Integer(Constants.SPECIMEN_DISTRIBUTION_TYPE)));
		distributionType.add(new NameValueBean("Specimen Array", new Integer(Constants.SPECIMEN_ARRAY_DISTRIBUTION_TYPE)));

		List distributionBasedOn = new ArrayList();
		distributionBasedOn.add(new NameValueBean("Barcode", new Integer(Constants.BARCODE_BASED_DISTRIBUTION)));
		distributionBasedOn.add(new NameValueBean("Label", new Integer(Constants.LABEL_BASED_DISTRIBUTION)));

		request.setAttribute(Constants.DISTRIBUTION_TYPE_LIST, distributionType);
		request.setAttribute(Constants.DISTRIBUTION_BASED_ON, distributionBasedOn);
	}

	/**
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 * @throws Exception generic exception
	 * @return value for ActionForward object
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping,
        ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws Exception 
    {
        super.executeSecureAction(mapping, form, request, response);

        DistributionForm dForm = (DistributionForm) form;


        //Populate Distributed Items data in the Distribution page if specimen ID is changed. 
        if (dForm.isIdChange()) 
        {
            String test = null;
            test.getBytes();
            setSpecimenCharateristics(dForm, request);
        }
        
        String distributionBasedOn = request.getParameter("distributionBasedOnOption"); 
        if(distributionBasedOn != null && !distributionBasedOn.equals("") && distributionBasedOn.equals("unknown")) 
        {
        		dForm.setDistributionBasedOn(new Integer(0));
        }

        /* If forwarded from speciman page**/
        HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
        if(forwardToHashMap==null)
        {
    	   String specimenLabel = request.getParameter(Constants.SYSTEM_LABEL);
    	   if(specimenLabel!=null)
    	   {
	    	   	IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
	    		List list = bizLogic.retrieve(Specimen.class.getName(), Constants.SYSTEM_LABEL, specimenLabel);
	    		if(list!=null&&!list.isEmpty()) 
	    		{
	    			Specimen specimen = (Specimen) list.get(0);
	    			forwardToHashMap = new HashMap();
	    			forwardToHashMap.put("specimenObjectKey",specimen);
	    		}
    	   }
        }

        if (forwardToHashMap != null) 
        {
            Object specimenObjectOrList = forwardToHashMap.get("specimenObjectKey");
            if(specimenObjectOrList != null)
            {
	            if (specimenObjectOrList instanceof Specimen) 
	            {
	            	/* this code is for setting specimenId  for showing specimen selected in tree */
	            	Specimen sp = (Specimen) specimenObjectOrList;
	            	String spId = sp.getId().toString();
	            	request.setAttribute(Constants.SPECIMEN_ID,spId);
	            	
	                addDistributionSample((DistributionForm) form, 1,
	                    (Specimen) specimenObjectOrList);
	            }
	            else 
	            {
	                List specimenIdList = (List) specimenObjectOrList;
	                DistributionBizLogic dao = (DistributionBizLogic) BizLogicFactory.getInstance()
	                                                                                 .getBizLogic(Constants.DISTRIBUTION_FORM_ID);
	
	                for (int i = 0; i < specimenIdList.size(); i++) 
	                {
	                    Long specimenId = Long.getLong((String) specimenIdList.get(i));
	                    List list = dao.retrieve(Specimen.class.getName(),
	                            Constants.SYSTEM_IDENTIFIER, specimenId);
	                    Specimen specimen = (Specimen) list.get(0);
	                    addDistributionSample((DistributionForm) form, i + 1,
	                        (Specimen) specimen);
	                }
	            }
            }
            else //If forwarded from Aliquout Summary page;
            {
            	IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
            	String noOfAliquots = (String) forwardToHashMap.get("noOfAliquots");
            	String labelKey;
            	List specimenList;
            	for (int i = 0; i < Integer.valueOf(noOfAliquots).intValue(); i++) 
            	{
            		labelKey = "Specimen:" + (i+1) + "_label";
            		String label = (String) forwardToHashMap.get(labelKey);            		
            		specimenList = bizLogic.retrieve(Specimen.class.getName(), Constants.SYSTEM_LABEL, label);
            		if (!specimenList.isEmpty())
            		{
            			Specimen specimen = (Specimen) specimenList.get(0);
            			addDistributionSample((DistributionForm) form, i + 1,
    	                        (Specimen) specimen);
            		}
            		
            	}
            	
            }
        }

        Logger.out.debug("executeSecureAction");
        String pageOf = request.getParameter(Constants.PAGEOF);
		request.setAttribute(Constants.PAGEOF, pageOf);

        return mapping.findForward((String) request.getParameter(
                Constants.PAGEOF));
    }	
	
	/**
	 * 
	 * @param dForm object of DistributionForm
	 * @param itemNo value for item no in int
	 * @param specimen object of Specimen
	 */
	private void addDistributionSample(DistributionForm dForm, int itemNo, Specimen specimen)
	{
		String keyPrefix = "DistributedItem:" + itemNo + "_";

		dForm.setValue(keyPrefix + "Specimen_id", specimen.getId());
		dForm.setValue(keyPrefix + "Specimen_barcode", specimen.getBarcode());
		dForm.setValue(keyPrefix + "Specimen_label", specimen.getLabel());
		dForm.setValue(keyPrefix + "availableQty", specimen.getAvailableQuantity());

		dForm.setCounter(dForm.getCounter() + 1);
		dForm.setDistributionBasedOn(new Integer(0));
	}

	/**
	 * @param dForm object of DistributionForm
	 * @param request object of HttpServletRequest
	 * @throws DAOException DAO exception
	 */
	private void setSpecimenCharateristics(DistributionForm dForm, HttpServletRequest request) throws DAOException
	{
		//Set specimen characteristics according to the specimen ID changed
		DistributionBizLogic dao = (DistributionBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.DISTRIBUTION_FORM_ID);

		String specimenIdKey = request.getParameter("specimenIdKey");

		//Parse key to get row number. Key is in the format DistributedItem:rowNo_Specimen_id
		MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.Distribution");
		int rowNo = parser.parseKeyAndGetRowNo(specimenIdKey);

		//int a = Integer.parseInt()
		Logger.out.debug("row number of the dist item: " + rowNo);

		String specimenId = (String) dForm.getValue("DistributedItem:" + rowNo + "_Specimen_id");

		//if '--Select--' is selected in the drop down of Specimen Id, empty the row values for that distributed item
		if (specimenId.equals("-1"))
		{
			dForm.setValue("DistributedItem:" + rowNo + "_availableQty", null);
		}
		//retrieve the row values for the distributed item for the selected specimen id
		else
		{
			List list = dao.retrieve(Specimen.class.getName(), Constants.SYSTEM_IDENTIFIER, specimenId);
			Specimen specimen = (Specimen) list.get(0);
			dForm.setValue("DistributedItem:" + rowNo + "_availableQty", dForm.getAvailableQty(specimen));
		}

		Logger.out.debug("Map values after speci chars are set: " + dForm.getValues());
		//Set back the idChange boolean to false.
		dForm.setIdChange(false);
	}
}