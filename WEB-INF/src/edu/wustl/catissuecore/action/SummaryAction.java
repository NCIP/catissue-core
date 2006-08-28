/*
 * Created on Jul 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.QueryBizLogic;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SummaryAction extends Action
{
    
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        //preparing QueryBizLogic to query
        QueryBizLogic bizLogic = (QueryBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SIMPLE_QUERY_INTERFACE_ID);
        
        //Total specimens
        String totalSpecimenCount = bizLogic.getTotalSpecimenCount();
        request.setAttribute(Constants.SPECIMEN_COUNT, totalSpecimenCount);
        
        //Class Count
       //TODO test for null values
        String tissueCount = bizLogic.getSpecimenTypeCount(Constants.TISSUE);
        String cellCount = bizLogic.getSpecimenTypeCount(Constants.CELL);
        String molCount = bizLogic.getSpecimenTypeCount(Constants.MOLECULAR );
        String fluidCount = bizLogic.getSpecimenTypeCount(Constants.FLUID);
        
        //set the total count for different type of specimen
        request.setAttribute(Constants.TISSUE+Constants.SPECIMEN_TYPE_COUNT, tissueCount);
        request.setAttribute(Constants.CELL+Constants.SPECIMEN_TYPE_COUNT, cellCount);
        request.setAttribute(Constants.MOLECULAR+Constants.SPECIMEN_TYPE_COUNT, molCount);
        request.setAttribute(Constants.FLUID+Constants.SPECIMEN_TYPE_COUNT, fluidCount);
        
        //Type Count
        Collection tissueTypeDetails = bizLogic.getSpecimenTypeDetailsCount(Constants.TISSUE);
        Collection cellTypeDetails = bizLogic.getSpecimenTypeDetailsCount(Constants.CELL);
        Collection fluidTypeDetails = bizLogic.getSpecimenTypeDetailsCount(Constants.FLUID);
        Collection molecularTypeDetails = bizLogic.getSpecimenTypeDetailsCount(Constants.MOLECULAR);
        
        //set the collections for different type of specimen
        request.setAttribute(Constants.TISSUE+Constants.SPECIMEN_TYPE_DETAILS, tissueTypeDetails);
        request.setAttribute(Constants.CELL+Constants.SPECIMEN_TYPE_DETAILS, cellTypeDetails);
        request.setAttribute(Constants.MOLECULAR+Constants.SPECIMEN_TYPE_DETAILS, molecularTypeDetails);
        request.setAttribute(Constants.FLUID+Constants.SPECIMEN_TYPE_DETAILS, fluidTypeDetails);
        
        // Quantity
        //TODO test for null values
        String tissueQuantity = bizLogic.getSpecimenTypeQuantity(Constants.TISSUE);
        String cellQuantity = bizLogic.getSpecimenTypeQuantity(Constants.CELL);
        String molQuantity = bizLogic.getSpecimenTypeQuantity(Constants.MOLECULAR);
        String fluidQuantity = bizLogic.getSpecimenTypeQuantity(Constants.FLUID);
        
        //set the total quantity for different type of specimen
        request.setAttribute(Constants.TISSUE+Constants.SPECIMEN_TYPE_QUANTITY, tissueQuantity);
        request.setAttribute(Constants.CELL+Constants.SPECIMEN_TYPE_QUANTITY, cellQuantity);
        request.setAttribute(Constants.MOLECULAR+Constants.SPECIMEN_TYPE_QUANTITY, molQuantity);
        request.setAttribute(Constants.FLUID+Constants.SPECIMEN_TYPE_QUANTITY, fluidQuantity);
        
        return mapping.findForward(Constants.SUCCESS);
    }

}
