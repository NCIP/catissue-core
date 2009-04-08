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
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.exception.DAOException;


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
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter out = response.getWriter();
		String showConsents = request.getParameter(Constants.SHOW_CONSENTS); 
		if(showConsents!=null && showConsents.equalsIgnoreCase(Constants.YES))
		{
			// Checking consent for participant page in collection protocol registration
			//Abhishek Mehta
			String collectionProtocolId = request.getParameter(Constants.CP_SEARCH_CP_ID);
			if(collectionProtocolId != null) 
			{
				if(collectionProtocolId.equalsIgnoreCase("-1"))
				{
					out.print(Constants.NO_CONSENTS_DEFINED);//No Consents
				}
				else
				{
					CollectionProtocolBizLogic collectionProtocolBizLogic = (CollectionProtocolBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.COLLECTION_PROTOCOL_FORM_ID);
					Object object  = collectionProtocolBizLogic.retrieve(CollectionProtocol.class.getName(), new Long(collectionProtocolId));		
					CollectionProtocol collectionProtocol = (CollectionProtocol)object;
					Collection consentTierCollection = (Collection)collectionProtocolBizLogic.retrieveAttribute(CollectionProtocol.class.getName(), collectionProtocol.getId(), "elements(consentTierCollection)");
					
					if(consentTierCollection.isEmpty())
			        {
			        	//Writing to response
			    		out.print(Constants.NO_CONSENTS_DEFINED);//No Consents
			        }
			        else
			        {
			    		out.print(Constants.PARTICIPANT_CONSENT_ENTER_RESPONSE);//Consent Response
			        }
				}
			}
			else // Checking consent for distribution page
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
		        catch (BizLogicException dao)
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
		        Long specimenId= (Long) specimen.getId();
		        
		        String colProtHql = "select scg.collectionProtocolRegistration.collectionProtocol"+ 
				" from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg," +
				" edu.wustl.catissuecore.domain.Specimen as spec " +
				" where spec.specimenCollectionGroup.id=scg.id and spec.id="+specimenId;
			
		        List collectionProtocolList= AppUtility.executeQuery(colProtHql);
		        CollectionProtocol collectionProtocol =null;
		        if(collectionProtocolList!=null)
		        {
		          collectionProtocol = (CollectionProtocol) collectionProtocolList.get(0);
		        }
		        Collection consentTierStatusCollection =(Collection)bizLogic.retrieveAttribute(Specimen.class.getName(), specimen.getId(),"elements(consentTierStatusCollection)");
		        if(specimen.getActivityStatus().equalsIgnoreCase(Constants.DISABLED))//disabled
		        {
		        	out.print(Constants.DISABLED);//disabled
		        }
		        else if(collectionProtocol.getConsentsWaived().booleanValue())
		        {
		        	out.print(Constants.CONSENT_WAIVED);//Consent Waived
		        }
		        else if(consentTierStatusCollection.isEmpty())
		        {
		        	//Writing to response
		    		out.print(Constants.NO_CONSENTS);//No Consents
		        }
		        else
		        {
		    		out.print(Constants.SHOW_CONSENTS);//ShowConsents
		        }
			}
		}
		return null;
	}
	        
	/**
	 * This function returns SpecimenCollectionGroup object by reading Barcode value
	 * @param barcode  Barcode is the Unique number,using barcode this function return specimenCollectionGroup object
	 * @return specimenCollectionGroup SpecimenCollectionGroup object
	 */
    private Specimen getConsentListForSpecimen(String barcodeLabel,int barcodeLabelBasedDistribution) throws BizLogicException
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
