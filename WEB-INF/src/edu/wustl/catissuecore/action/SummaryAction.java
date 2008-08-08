/*
 * Created on Jul 15, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.client.HappyClient;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.actionForm.SummaryForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * @author sagar_baldwa
 * This class instantiates the QueryBizLogic class and retrieves data
 * from database and populates the SummaryForm
 */
public class SummaryAction extends Action
{
    
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, 
     * org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, 
     * javax.servlet.http.HttpServletResponse)
     */
    @SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
    	SummaryForm summaryForm = null;
		try 
		{
			summaryForm = (SummaryForm) form;
			// preparing QueryBizLogic to query
			QueryBizLogic bizLogic = (QueryBizLogic) BizLogicFactory
					.getInstance().getBizLogic(
							Constants.SIMPLE_QUERY_INTERFACE_ID);

			//Populating the Map<String, Object> with data from database for summary report
			Map<String, Object> summaryDataMap = bizLogic
					.getTotalSummaryDetails();

			//Extract the Map data
			Object totalSpecimenCount = summaryDataMap
					.get("TotalSpecimenCount");
			Object tissueCount = summaryDataMap.get("TissueCount");
			Object cellCount = summaryDataMap.get("CellCount");
			Object moleculeCount = summaryDataMap.get("MoleculeCount");
			Object fluidCount = summaryDataMap.get("FluidCount");
			Collection<Object> tissueTypeDetails = (Collection<Object>) summaryDataMap
					.get("TissueTypeDetails");
			Collection<Object> cellTypeDetails = (Collection<Object>) summaryDataMap
					.get("CellTypeDetails");
			Collection<Object> moleculeTypeDetails = (Collection<Object>) summaryDataMap
					.get("MoleculeTypeDetails");
			Collection<Object> fluidTypeDetails = (Collection<Object>) summaryDataMap
					.get("FluidTypeDetails");
			Object tissueQuantity = summaryDataMap.get("TissueQuantity");
			Object cellQuantity = summaryDataMap.get("CellQuantity");
			Object moleculeQuantity = summaryDataMap.get("MoleculeQuantity");
			Object fluidQuantity = summaryDataMap.get("FluidQuantity");

			//Populate the Summary Form
			summaryForm.setTotalSpecimenCount(totalSpecimenCount.toString());
			summaryForm.setTissueCount(tissueCount.toString());
			summaryForm.setCellCount(cellCount.toString());
			summaryForm.setMoleculeCount(moleculeCount.toString());
			summaryForm.setFluidCount((String) fluidCount);
			summaryForm.setTissueTypeDetails(tissueTypeDetails);
			summaryForm.setCellTypeDetails(cellTypeDetails);
			summaryForm.setMoleculeTypeDetails(moleculeTypeDetails);
			summaryForm.setFluidTypeDetails(fluidTypeDetails);
			summaryForm.setTissueQuantity((String) tissueQuantity);
			summaryForm.setCellQuantity((String) cellQuantity);
			summaryForm.setMoleculeQuantity((String) moleculeQuantity);
			summaryForm.setFluidQuantity((String) fluidQuantity);
		}
		catch (Exception e)
		{
			System.out.println(e);
		}		
		request.setAttribute("summaryForm", summaryForm);
		return mapping.findForward(Constants.SUCCESS);
    }
} 
       
