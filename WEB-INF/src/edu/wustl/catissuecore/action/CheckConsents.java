/**
 * <p>Title: CheckConsents Class>
 * <p>Description:	Ajax Action Class for Checking if consents available or not.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Virender Mehta
 * @version 1.00
 * Created on Jan 18,2007
 */
package edu.wustl.catissuecore.action;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.dbManager.DAOException;


/**
 * @author Virender Mehta
 *
 */
public class CheckConsents extends BaseAction
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
		PrintWriter out = response.getWriter();
		String showConsents = request.getParameter(Constants.SHOW_CONSENTS); 
		if(showConsents!=null && showConsents.equalsIgnoreCase(Constants.YES))
		{
			String barcodeLable=null;
			int barcodeLabelBasedDistribution=1;
			String labelBarcodeDistributionValue = request.getParameter(Constants.DISTRIBUTION_ON);//lableBarcode
			if(labelBarcodeDistributionValue.equalsIgnoreCase(Constants.BARCODE_DISTRIBUTION))//"1"
			{
				barcodeLabelBasedDistribution=1;
			}
			else
			{
				barcodeLabelBasedDistribution=2;
			}
			barcodeLable = request.getParameter(Constants.BARCODE_LABLE);	        		        	
	        DistributionBizLogic bizLogic = (DistributionBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.DISTRIBUTION_FORM_ID);
	        try
	        {
	        	bizLogic.getSpecimenId(barcodeLable,barcodeLabelBasedDistribution);
	        }
	        catch (DAOException dao)
	        {
	        	ActionErrors errors = new ActionErrors();
				ActionError error = new ActionError(dao.getMessage());
				errors.add(ActionErrors.GLOBAL_ERROR, error);
				saveErrors(request, errors);
				out.print(Constants.INVALID);//Invalid
				return null;
	        }
		    //Getting SpecimenCollectionGroup object
	        Specimen specimen = getConsentListForSpecimen(barcodeLable, barcodeLabelBasedDistribution);
	        CollectionProtocol collectionProtocol=specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol();
	        if(specimen.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))//disabled
	        {
	        	out.print(Constants.DISABLED);//disabled
	        }
	        else if(collectionProtocol.getConsentsWaived().booleanValue())
	        {
	        	out.print(Constants.CONSENT_WAIVED);//Consent Waived
	        }
	        else if(specimen.getConsentTierStatusCollection()==null||specimen.getConsentTierStatusCollection().isEmpty())
	        {
	        	//Writing to response
	    		out.print(Constants.NO_CONSENTS);//No Consents
	        }
	        else
	        {
	    		out.print(Constants.SHOW_CONSENTS);//ShowConsents
	        }
		}
		return null;
	}
	        
	/**
	 * This function returns SpecimenCollectionGroup object by reading Barcode value
	 * @param barcode  Barcode is the Unique number,using barcode this function return specimenCollectionGroup object
	 * @return specimenCollectionGroup SpecimenCollectionGroup object
	 */
    private Specimen getConsentListForSpecimen(String barcodeLabel,int barcodeLabelBasedDistribution) throws DAOException
	{
		NewSpecimenBizLogic  newSpecimenBizLogic = (NewSpecimenBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		String colName=null;
		if(barcodeLabelBasedDistribution==Constants.BARCODE_BASED_DISTRIBUTION)
		{
			colName=Constants.SYSTEM_BARCODE;//"barcode"	
		}
		else
		{
			colName=Constants.SYSTEM_LABEL;//"label"
		}
		List specimenList  = newSpecimenBizLogic.retrieve(Specimen.class.getName(), colName, barcodeLabel);
		Specimen specimen = (Specimen)specimenList.get(0);
		return specimen;
	}
 }
