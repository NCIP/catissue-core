/*
 * Created on Jul 3, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sun.msv.datatype.xsd.regex.REUtil;

import edu.wustl.catissuecore.actionForm.SimilarContainersForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenClass;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

/**
 * @author chetan_bh
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimilarContainersAction extends SecureAction {

	/* (non-Javadoc)
	 * @see edu.wustl.common.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected ActionForward executeSecureAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Logger.out.debug("SimilarContainersAction : executeSecureAction() form: type "+form.getClass());
		
		SimilarContainersForm similarContainersForm = (SimilarContainersForm) form;
		
		IBizLogic ibizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		
		long typeId = similarContainersForm.getTypeId();		
		request.setAttribute("typeId",new Long(typeId));
		List containerTypeList = ibizLogic.retrieve(StorageType.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(typeId));
		Iterator iter = containerTypeList.iterator();
		while(iter.hasNext())
		{
			String containerType = ((StorageType)iter.next()).getType();			
			similarContainersForm.setStorageContainerType(containerType);
		}		
		
		long siteId = similarContainersForm.getSiteId();	
		request.setAttribute("siteId",new Long(siteId));
		//System.out.println("SimilarContainersAction :: siteId "+siteId);
		
		if(siteId == -1)  // get site id from parent container id 
		{
			long parenContId = similarContainersForm.getParentContainerId();
			List pcList = ibizLogic.retrieve(StorageContainer.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(parenContId));
			if(pcList.size() > 0)
			{
				StorageContainer pc = (StorageContainer)pcList.get(0);
				siteId = pc.getSite().getSystemIdentifier().longValue();
				request.setAttribute("siteId",new Long(siteId));
				//System.out.println("SimilarContainersAction :: siteId : got "+siteId);
			}
		}
		List siteList = ibizLogic.retrieve(Site.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(siteId));
		Iterator siteIter = siteList.iterator();
		while(siteIter.hasNext())
		{			
			String site = ((Site)siteIter.next()).getName();			
			similarContainersForm.setSiteName(site);
		}
		
		//		Populating the Site Array
    	String []siteDisplayField = {"name"};
    	String valueField = "systemIdentifier";
    	List list = ibizLogic.getList(Site.class.getName(),siteDisplayField, valueField, true);
    	request.setAttribute(Constants.SITELIST,list);
		
    	long[] collectionProtocolIds = similarContainersForm.getCollectionIds();
    	List collecProList = new ArrayList();
    	//System.out.println("collectionProtocolIds $$$*** "+collectionProtocolIds.length);
    	for(int i = 0 ;i < collectionProtocolIds.length; i++)
    	{
    		List cpList = ibizLogic.retrieve(CollectionProtocol.class.getName(),valueField,new Long(collectionProtocolIds[i]));
        	Iterator cpIter = cpList.iterator();
        	while(cpIter.hasNext())
        	{
        		String cpName = ((CollectionProtocol) cpIter.next()).getTitle();
        		NameValueBean nvb = new NameValueBean(cpName,new Long(collectionProtocolIds[i]));
        		//collecProList.add(cpName);
        		collecProList.add(nvb);
        	}
    	}
    	request.setAttribute(Constants.PROTOCOL_LIST,collecProList);    	
    	
    	long[] holdsIds = similarContainersForm.getHoldsStorageTypeIds();		
    	List holdsList = new ArrayList();
    	//System.out.println("holdsIds $$$*** "+holdsIds.length);
    	for(int i = 0 ;i < holdsIds.length; i++)
    	{
    		List hList = ibizLogic.retrieve(StorageType.class.getName(),valueField,new Long(holdsIds[i]));
        	Iterator hIter = hList.iterator();
        	while(hIter.hasNext())
        	{
        		String cpName = ((StorageType) hIter.next()).getType();
        		NameValueBean nvb = new NameValueBean(cpName,new Long(holdsIds[i]));
        		
        		//holdsList.add(cpName);
        		holdsList.add(nvb);
        	}
    	}
    	request.setAttribute(Constants.HOLDS_LIST1,holdsList);
    	
    	long[] holdsSpecimenIds = similarContainersForm.getHoldsSpecimenClassTypeIds();		
    	List holdsSpecimenList = new ArrayList();
    	//System.out.println("holdsSpecimenIds $$$*** "+holdsSpecimenIds.length);
    	for(int i = 0 ;i < holdsSpecimenIds.length; i++)
    	{
    		List hsList = ibizLogic.retrieve(SpecimenClass.class.getName(),valueField,new Long(holdsSpecimenIds[i]));
        	Iterator hsIter = hsList.iterator();
        	while(hsIter.hasNext())
        	{
        		String cpName = ((SpecimenClass) hsIter.next()).getName();
        		NameValueBean nvb = new NameValueBean(cpName,new Long(holdsSpecimenIds[i]));
        		
        		//holdsList.add(cpName);
        		holdsSpecimenList.add(nvb);
        	}
    	}    	
    	request.setAttribute(Constants.HOLDS_LIST2,holdsSpecimenList);
    	
    	//System.out.println("collectionProtocolIds "+collecProList+"  holdsIds "+holdsList+" holdsSpecimenList "+holdsSpecimenList);
		
		int siteOrParentCont = similarContainersForm.getCheckedButton();
		Logger.out.debug("similarContainersForm.getValue(checkedButton) "+similarContainersForm.getValue("checkedButton") );
		Logger.out.debug("siteOrParentCont "+siteOrParentCont);
		if(similarContainersForm.getValue("checkedButton") != null)
		{
			request.setAttribute("value(checkedButton)",similarContainersForm.getValue("checkedButton"));
			
		}else
		{
			request.setAttribute("value(checkedButton)",new Integer(siteOrParentCont));
			similarContainersForm.setValue("checkedButton",Integer.toString(siteOrParentCont));
		}		
    	
		request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);
    	
    	String storageContainerId = null;		
		if(request.getAttribute(Constants.SYSTEM_IDENTIFIER) != null)
		{
			storageContainerId = String.valueOf(request.getAttribute(Constants.SYSTEM_IDENTIFIER));
		}
		else
		{
			storageContainerId = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		}
		//System.out.println("SimilarContainersAction : storageContainerId "+storageContainerId);
		
		if(storageContainerId != null)
		{
			similarContainersForm.setStorageContainerId(storageContainerId);
			
			similarContainersForm.setNoOfContainers(request.getParameter("noOfContainers"));
			similarContainersForm.setTypeId(Long.parseLong(request.getParameter("typeId")));
			similarContainersForm.setDefaultTemperature(request.getParameter("defaultTemperature"));
		}else
		{
			similarContainersForm.setDefaultTemperature(request.getParameter("defaultTemperature"));
		}
		
		int noOfContainers = Integer.parseInt((String)request.getParameter("noOfContainers"));
		if(siteOrParentCont == 1)
    	{			
    		for(int i = 1; i <= noOfContainers; i++)
    		{
    			request.setAttribute("value(simCont:" + i + "_siteId)",new Long(siteId));
    		}
    	}
		
		String pageOf = request.getParameter(Constants.PAGEOF);		
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);		
		
		// code to set Max(IDENTIFIER) in storage container table 
		// used for suffixing Unique numbers to auto-generated container name
		long maxId = bizLogic.getNextContainerNumber();
		request.setAttribute(Constants.MAX_IDENTIFIER,Long.toString(maxId));
		
		Map containerMap = bizLogic.getAllocatedContaienrMapForContainer(new Long(request.getParameter("typeId")).longValue());
		
		String contName = similarContainersForm.getContainerName();
		String barcode = similarContainersForm.getBarcode();
		
		Logger.out.debug("contName "+contName+" barcode "+barcode +" <<<<---");
		similarContainersForm.setValue("simCont:1_name",contName);
		similarContainersForm.setValue("simCont:1_barcode",barcode);
		
		String[] startingPoints = new String[3];
		startingPoints[0] = Long.toString(similarContainersForm.getParentContainerId());
		startingPoints[1] = Integer.toString(similarContainersForm.getPositionDimensionOne());
		startingPoints[2] = Integer.toString(similarContainersForm.getPositionDimensionTwo());
		Logger.out.debug("similarContainersForm.getParentContainerId() #$% "+similarContainersForm.getParentContainerId());
    	if(similarContainersForm.getParentContainerId() != 0l)
    	{
    		Vector initialValues = getInitalValues(startingPoints,containerMap,noOfContainers);
    		request.setAttribute("initValues",initialValues);
    	}
		
        	if(request.getAttribute("validated") == null)
        	{
        		pageOf = validate(request,similarContainersForm);
        		request.setAttribute("validated","");
        		if(similarContainersForm.getCheckedButton() == 2 &&  ! (checkAvailability(containerMap,noOfContainers)))
        	    {
        			ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
        			if(errors == null)
        			{
        				errors = new ActionErrors();
        			}
        			System.out.println("errors "+errors+", ActionErrors.GLOBAL_ERROR "+ActionErrors.GLOBAL_ERROR+", new ActionError(\"errors.storageContainer.overflow\") "+new ActionError("errors.storageContainer.overflow"));
        			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.storageContainer.overflow"));
        			pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
        			saveErrors(request,errors);
        	    }
        	}
        	
       for(int i = 1; i <= noOfContainers; i++)
       {
        	request.setAttribute("simCont:" + i + "_siteId",new Long(siteId));
       }
        
        request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
        request.setAttribute(Constants.PAGEOF,pageOf);
		//System.out.println("SimilarContainersAction pageOf "+pageOf);
        return mapping.findForward(pageOf);
	}
	
	private boolean checkAvailability(Map dataMap, int noOfContainersNeeded)
	{
		int counter=0;
		Iterator dMapIter = dataMap.keySet().iterator();
		while(dMapIter.hasNext())
		{
			Map xMap = (Map) dataMap.get(dMapIter.next());
			Iterator xMapIter = xMap.keySet().iterator();
			while(xMapIter.hasNext())
			{
				List yList = (List) xMap.get(xMapIter.next());
				counter += yList.size();
			}
		}
		if(noOfContainersNeeded > counter)
		{
			return false;
		}
		return true;
	}
	
	private String validate(HttpServletRequest request, SimilarContainersForm form)
	{
        Validator validator = new Validator();
		ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
		
		String pageOf = Constants.PAGEOF_CREATE_SIMILAR_CONTAINERS;
		
		if(errors == null)
		{
			errors = new ActionErrors();
		}
        
		if (form.getTypeId() == -1)
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("storageContainer.type")));
			pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
		}
		
		//		 validations for temperature
		if (!validator.isEmpty(form.getDefaultTemperature()) && (!validator.isDouble(form.getDefaultTemperature(), false)))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
					ApplicationProperties.getValue("storageContainer.temperature")));
			pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
		}
		
        if(form.getCheckedButton() == 1)
        {
        	if(!validator.isValidOption(Long.toString(form.getSiteId())))
        	{
        		errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("storageContainer.site")));
        		pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
        	}
        }
        else
        {
        	if(form.getParentContainerId() <= 0 )
        	{
        		errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("storageContainer.parentContainer")));
        		pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
        	}
        }
        
        if(!validator.isNumeric(form.getNoOfContainers()))
        {
        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("similarcontainers.noOfContainers")));
        	pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
        }
        
        //      VALIDATIONS FOR DIMENSION 1.
		if (validator.isEmpty(String.valueOf(form.getOneDimensionCapacity())))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.required",
					ApplicationProperties.getValue("storageContainer.oneDimension")));
			pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
		}
		else
		{
			if (!validator.isNumeric(String.valueOf(form.getOneDimensionCapacity())))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
						ApplicationProperties.getValue("storageContainer.oneDimension")));
				pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
			}
		}

		//Validations for dimension 2
		if (!validator.isEmpty(String.valueOf(form.getTwoDimensionCapacity()))
				&& (!validator.isNumeric(String.valueOf(form.getTwoDimensionCapacity()))))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
					ApplicationProperties.getValue("storageContainer.twoDimension")));
			pageOf = Constants.PAGEOF_STORAGE_CONTAINER;
		}        
        
        saveErrors(request,errors);
        return pageOf;
	}
	
//	public static void main(String args[])
//	{
//		Map dMap = new TreeMap();
//		Map xMap = new TreeMap();
//		List yList = new Vector();
//		int counter = 0;
//		int counter1 = 0;
//		for(int i = 0 ; i < 3 ; i++)
//		{
//			xMap = new TreeMap();
//			for(int j = 0; j <2; j++)
//			{
//				yList = new Vector();
//				for(int k = 0; k < 3; k++)
//				{
//					yList.add(new Integer(counter++));
//				}
//				xMap.put(new Integer(counter1++),yList);
//			}
//			dMap.put(new Integer(i),xMap);
//		}
//		
//		String[] startingPoint = new String[] {"0","1","4"};
//		int noOfCont = 7;
//		
//		Vector results = SimilarContainersAction.getInitalValues(startingPoint,dMap,noOfCont);
//		for(int i =0; i< results.size(); i++)
//		{
//			String[] result = (String[])results.get(i);
//			for(int j= 0; j<result.length;j++)
//			{
//				System.out.println(i+" result "+result[j]);
//			}
//			System.out.println();
//		}
//		
//		System.out.println("results "+results);
//	}
	
	/**
	 * @param startingPoint
	 * @param dMap
	 * @param noOfContainers
	 * @return
	 */
	private Vector getInitalValues(String[] startingPoint, Map dMap, int noOfContainers)
	{
		//System.out.println("dmAp "+dMap+" noOfCont "+noOfContainers);
		Logger.out.info("Starting point 1:"+startingPoint[0]);
		Logger.out.info("Starting point 2:"+startingPoint[1]);
		Logger.out.info("Starting point 3:"+startingPoint[2]);
		Vector returner = new Vector();
		String[] initValues = new String[3];
		Iterator dMapIter = dMap.keySet().iterator();
		NameValueBean dMapKey;
		NameValueBean xMapKey;
		NameValueBean yListKey;
		do{
			dMapKey = (NameValueBean)dMapIter.next();
		}while(! (dMapKey.getValue().equals(startingPoint[0])) );
		Map xMap = (Map)dMap.get(dMapKey);
		//System.out.println("dMpaKey "+dMapKey.toString()+" xMap "+xMap);
		
		Iterator xMapIter = xMap.keySet().iterator(); 
		
		do{
			xMapKey = (NameValueBean)xMapIter.next();
		}while(! (xMapKey.getValue().equals(startingPoint[1])));
		List yList = (List)xMap.get(xMapKey);
		//System.out.println("xMapKey "+xMapKey.toString()+" yList "+yList);
		
		Iterator yListIter = yList.iterator();
		do{
			yListKey = (NameValueBean)yListIter.next(); 
		}while(! yListKey.getValue().equals(startingPoint[2]));
		
		initValues[0] = dMapKey.getValue();
		initValues[1] = xMapKey.getValue();
		initValues[2] = yListKey.getValue();
		
		returner.add(initValues);
		
		for(int i= 1; i<noOfContainers; i++)
		{
			initValues = new String[] {"","",""};
			
			if(yListIter.hasNext())
			{
				yListKey = (NameValueBean)yListIter.next();
				initValues[0] = dMapKey.getValue();
				initValues[1] = xMapKey.getValue();
				initValues[2] =  yListKey.getValue();
			}else
			{
				if(xMapIter.hasNext())
				{
					xMapKey = (NameValueBean)xMapIter.next(); 
					yList = (List) xMap.get(xMapKey);
					yListIter = yList.iterator();
					yListKey = (NameValueBean)yListIter.next();
					
					initValues[0] = dMapKey.getValue();
					initValues[1] = xMapKey.getValue();
					initValues[2] = yListKey.getValue();
					
				}else
				{
					if(dMapIter.hasNext())
					{
						dMapKey = (NameValueBean)dMapIter.next();
						xMap = (Map) dMap.get(dMapKey);
						xMapIter = xMap.keySet().iterator();
						xMapKey = (NameValueBean)xMapIter.next();
						yList = (List) xMap.get(xMapKey);
						yListIter = yList.iterator();
						yListKey = (NameValueBean)yListIter.next();
						
						initValues[0] = dMapKey.getValue();
						initValues[1] = xMapKey.getValue();
						initValues[2] = yListKey.getValue();
						
					}
				}
			}
			
			returner.add(initValues);
		}
		
		return returner;
	}
	
	
}
