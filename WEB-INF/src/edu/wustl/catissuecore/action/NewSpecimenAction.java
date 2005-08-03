/**
 * <p>Title: NewSpecimenAction Class>
 * <p>Description:	NewSpecimenAction initializes the fields in the New Specimen page.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Biohazard;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * NewSpecimenAction initializes the fields in the New Specimen page.
 * @author gautam_shetty
 */
public class NewSpecimenAction extends Action
{
    
    /**
     * Overrides the execute method of Action class.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit User Page. 
        request.setAttribute(Constants.OPERATION, operation);
        
        String pageOf = request.getParameter(Constants.PAGEOF);
        
        request.setAttribute(Constants.PAGEOF,pageOf);
        
        AbstractBizLogic dao = BizLogicFactory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
        
        String sourceObjectName = SpecimenCollectionGroup.class.getName();
        String[] displayNameFields = {"systemIdentifier"};
        String valueField = "systemIdentifier";
        
        List specimenList = dao.getList(sourceObjectName, displayNameFields, valueField);
        request.setAttribute(Constants.SPECIMENCOLLECTIONLIST, specimenList);
        
        sourceObjectName = Biohazard.class.getName();
        String [] displayNameFields1 = {"name"};
        valueField = "systemIdentifier";
        
        List biohazardList = dao.getList(sourceObjectName, displayNameFields1, valueField);
        request.setAttribute(Constants.SPECIMENCOLLECTIONLIST, biohazardList);
        
        String [] specimenCollectionGroupArray = {"1","2","3","4"};
        
        request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_LIST,specimenCollectionGroupArray);
        
        request.setAttribute(Constants.SPECIMEN_TYPE_LIST, Constants.SPECIMEN_TYPE_VALUES);
        
        request.setAttribute(Constants.SPECIMEN_SUB_TYPE_LIST, Constants.SPECIMEN_SUB_TYPE_VALUES);
        
        request.setAttribute(Constants.TISSUE_SITE_LIST,Constants.TISSUE_SITE_VALUES);
        
        request.setAttribute(Constants.TISSUE_SIDE_LIST,Constants.TISSUE_SIDE_VALUES);
        
        request.setAttribute(Constants.PATHOLOGICAL_STATUS_LIST, Constants.PATHOLOGICAL_STATUS_VALUES);
        
        request.setAttribute(Constants.BIOHAZARD_NAME_LIST, Constants.BIOHAZARD_NAME_VALUES);
        
        request.setAttribute(Constants.BIOHAZARD_TYPE_LIST, Constants.BIOHAZARD_TYPE_ARRAY);
        
        return mapping.findForward(Constants.SUCCESS);
    }

}
