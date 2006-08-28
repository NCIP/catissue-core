/**
 * <p>Title: CreateSpecimenAction Class>
 * <p>Description:	CreateSpecimenAction initializes the fields in the Create Specimen page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
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

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CreateSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDE;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.cde.PermissibleValue;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;


/**
 * CreateSpecimenAction initializes the fields in the Create Specimen page.
 * @author aniruddha_phadnis
 */
public class CreateSpecimenAction extends SecureAction
{
    
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	CreateSpecimenForm createForm = (CreateSpecimenForm) form;
    	
    	//List of keys used in map of ActionForm
		List key = new ArrayList();
    	key.add("ExternalIdentifier:i_name");
    	key.add("ExternalIdentifier:i_value");
    	
    	//Gets the map from ActionForm
    	Map map = createForm.getExternalIdentifier();
    	
    	//Calling DeleteRow of BaseAction class
    	MapDataParser.deleteRow(key,map,request.getParameter("status"));
    	
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        String pageOf = request.getParameter(Constants.PAGEOF);
        /*
        // ---- chetan 15-06-06 ----
        StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
        Map containerMap = bizLogic.getAllocatedContainerMap();
        request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
        // -------------------------
        request.setAttribute(Constants.PAGEOF,pageOf);
        */
        
        CreateSpecimenBizLogic dao = (CreateSpecimenBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.CREATE_SPECIMEN_FORM_ID);
        
        
        String [] fields = {"id"};
            
        // Setting the parent specimen list
        List parentSpecimenList = dao.getList(Specimen.class.getName(), fields, fields[0], true); 	 	
	 	request.setAttribute(Constants.PARENT_SPECIMEN_ID_LIST,parentSpecimenList);
	 	
	 	//Setting the specimen class list
	 	List specimenClassList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_CLASS,null);
    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);
    	
    	//Setting the specimen type list
    	List specimenTypeList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_SPECIMEN_TYPE,null);
    	request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
        
    	//Setting biohazard list
    	List biohazardList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_BIOHAZARD,null);
    	request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, biohazardList);
        	
    	Logger.out.debug("1");
    	// get the Specimen class and type from the cde
    	CDE specimenClassCDE = CDEManager.getCDEManager().getCDE(Constants.CDE_NAME_SPECIMEN_CLASS);
    	Set setPV = specimenClassCDE.getPermissibleValues();
    	Logger.out.debug("2");
    	Iterator itr = setPV.iterator();
    
    	specimenClassList =  new ArrayList();
    	Map subTypeMap = new HashMap();
    	Logger.out.debug("\n\n\n\n**********MAP DATA************\n");
    	specimenClassList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
    	
    	while(itr.hasNext())
    	{
    		List innerList =  new ArrayList();
    		Object obj = itr.next();
    		PermissibleValue pv = (PermissibleValue)obj;
    		String tmpStr = pv.getValue();
    		Logger.out.debug(tmpStr);
    		specimenClassList.add(new NameValueBean( tmpStr,tmpStr));
    		
			Set list1 = pv.getSubPermissibleValues();
			Logger.out.debug("list1 "+list1);
        	Iterator itr1 = list1.iterator();
        	innerList.add(new NameValueBean(Constants.SELECT_OPTION,"-1"));
        	while(itr1.hasNext())
        	{
        		Object obj1 = itr1.next();
        		PermissibleValue pv1 = (PermissibleValue)obj1;
        		// set specimen type
        		String tmpInnerStr = pv1.getValue(); 
        		Logger.out.debug("\t\t"+tmpInnerStr);
        		innerList.add(new NameValueBean( tmpInnerStr,tmpInnerStr));  
        	}
        	subTypeMap.put(pv.getValue(),innerList);
    	} // class and values set
    	Logger.out.debug("\n\n\n\n**********MAP DATA************\n");
    	
    	// sets the Class list
    	request.setAttribute(Constants.SPECIMEN_CLASS_LIST, specimenClassList);

    	// set the map to subtype
    	request.setAttribute(Constants.SPECIMEN_TYPE_MAP, subTypeMap);
    	Logger.out.debug("************************************\n\n\nDone**********\n");

        //*************  ForwardTo implementation *************
        HashMap forwardToHashMap=(HashMap)request.getAttribute("forwardToHashMap");
        
        if(forwardToHashMap !=null)
        {
            Long parentSpecimenId=(Long)forwardToHashMap.get("parentSpecimenId");
            Logger.out.debug("ParentSpecimenID found in forwardToHashMap========>>>>>>"+parentSpecimenId);
            
            if(parentSpecimenId != null)
            {
            	createForm.setParentSpecimenId(parentSpecimenId.toString());
	            createForm.setPositionInStorageContainer("" );
    	    	createForm.setQuantity("");
        		createForm.setPositionDimensionOne("" );
        		createForm.setPositionDimensionTwo( "");
	        	createForm.setStorageContainer("" ); 
    	    	map.clear(); 
        		createForm.setExternalIdentifier(map);
        		createForm.setExIdCounter(1 ); 
        	}
        }
        //*************  ForwardTo implementation *************

        return mapping.findForward(Constants.SUCCESS);
    }
}
