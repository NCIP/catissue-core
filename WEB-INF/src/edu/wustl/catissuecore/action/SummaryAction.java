/*
 * Created on Jul 15, 2005
 * @author mandar_deshmukh
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SummaryForm;
import edu.wustl.catissuecore.bean.SummaryAdminDetails;
import edu.wustl.catissuecore.bean.SummaryPartDetails;
import edu.wustl.catissuecore.bean.SummarySpDetails;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SummaryBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;


/**
 * @author mandar_deshmukh
 * This class instantiates the QueryBizLogic class and retrieves data
 * from database and populates the SummaryForm
 */
public class SummaryAction extends Action
{
	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(SummaryAction.class);

    
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
			final SummaryBizLogic bizLogic = (SummaryBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.SUMMARY_BIZLOGIC_ID);

			//Populating the Map<String, Object> with data from database for summary report
			final Map<String, Object> summaryDataMap = bizLogic.getTotalSummaryDetails();

			//Populate the Summary Form
			summaryForm.setTotalSpCount(summaryDataMap.get("TotalSpecimenCount").toString());
			
			// Specimen Details
			final SummarySpDetails spDetails = new SummarySpDetails();
			
			spDetails.setCellCount(summaryDataMap.get("CellCount").toString());
			spDetails.setCellQuantity(summaryDataMap.get("CellQuantity").toString());
			spDetails.setCellTypeDetails((Collection<Object>) summaryDataMap.get("CellTypeDetails"));
			
			spDetails.setFluidCount(summaryDataMap.get("FluidCount").toString());
			spDetails.setFluidQuantity(summaryDataMap.get("FluidQuantity").toString());
			spDetails.setFluidTypeDetails((Collection<Object>) summaryDataMap.get("FluidTypeDetails"));
			
			spDetails.setMoleculeCount(summaryDataMap.get("MoleculeCount").toString());
			spDetails.setMoleculeQuantity(summaryDataMap.get("MoleculeQuantity").toString());
			spDetails.setMolTypeDetails((Collection<Object>) summaryDataMap.get("MoleculeTypeDetails"));
			
			spDetails.setTissueCount(summaryDataMap.get("TissueCount").toString());
			spDetails.setTissueQuantity(summaryDataMap.get("TissueQuantity").toString());
			spDetails.setTissueTypeDetails((Collection<Object>) summaryDataMap.get("TissueTypeDetails"));
			
			spDetails.setPatStDetails((Collection<Object>) summaryDataMap.get(Constants.SP_PATHSTAT));
			spDetails.setTSiteDetails((Collection<Object>) summaryDataMap.get(Constants.SP_TSITE));
			
			
			summaryForm.setSpecDetails(spDetails);
			
			// Participant Details
			final SummaryPartDetails prDetails = new SummaryPartDetails();
			prDetails.setPByCDDetails((Collection<Object>)summaryDataMap.get(Constants.P_BYCD));
			prDetails.setPByCSDetails((Collection<Object>)summaryDataMap.get(Constants.P_BYCS));
			prDetails.setTotPartCount(summaryDataMap.get(Constants.TOTAL_PART_COUNT).toString());
			
			summaryForm.setPartDetails(prDetails);
			
			// Administrative details
			final SummaryAdminDetails adminDetails = new SummaryAdminDetails();
			adminDetails.setColSites(summaryDataMap.get(Constants.COLL_SITE_COUNT).toString());
			adminDetails.setLabSites(summaryDataMap.get(Constants.LAB_SITE_COUNT).toString());
			adminDetails.setRepSites(summaryDataMap.get(Constants.REPO_SITE_COUNT).toString());

			adminDetails.setCpTot(summaryDataMap.get(Constants.TOTAL_CP_COUNT).toString());
			adminDetails.setDpTot(summaryDataMap.get(Constants.TOTAL_DP_COUNT).toString());
			adminDetails.setRegUsers(summaryDataMap.get(Constants.TOTAL_USER_COUNT).toString());
			
			adminDetails.setAdminInfo((List<List>) summaryDataMap.get(Constants.USER_DATA));
			
			summaryForm.setAdminDetails(adminDetails);

		}
		catch (Exception e)
		{
			logger.error(e.getMessage(),e);
		}		
		request.setAttribute("summaryForm", summaryForm);
		if(true);
		//	throw new Exception("Mandar : defined excp");
		return mapping.findForward(Constants.SUCCESS);
    }
} 
       
