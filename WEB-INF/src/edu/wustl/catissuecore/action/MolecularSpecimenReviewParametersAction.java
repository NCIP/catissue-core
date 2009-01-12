/**
 * <p>Title: MolecularSpecimenReviewParametersAction Class</p>
 * <p>Description:	This class initializes the fields in the MolecularSpecimenReviewParameters Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on November 21, 2005
 */
package edu.wustl.catissuecore.action;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.actionForm.MolecularSpecimenReviewParametersForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;

/**
 * @author mandar_deshmukh
 * 
 * This class initializes the fields in the MolecularSpecimenReviewParameters
 * Add/Edit webpage.
 */
public class MolecularSpecimenReviewParametersAction extends SpecimenEventParametersAction
{
	/**
	 * @param request object of HttpServletRequest
	 * @throws Exception generic exception
	 */
	protected void setRequestParameters(HttpServletRequest request, EventParametersForm eventParametersForm) throws Exception
	{
		String operation = (String) request.getAttribute(Constants.OPERATION);
        String formName,specimenId=null;
        String isRNA = null;
        
        boolean readOnlyValue;
        MolecularSpecimenReviewParametersForm molecularSpecimenReviewParametersForm=(MolecularSpecimenReviewParametersForm) eventParametersForm;
        if (molecularSpecimenReviewParametersForm.getOperation().equals(Constants.EDIT))
        {
            formName = Constants.MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_EDIT_ACTION;
            readOnlyValue = true;
        }
        else
        {
            formName = Constants.MOLECULAR_SPECIMEN_REVIEW_PARAMETERS_ADD_ACTION;
			specimenId = (String) request.getAttribute(Constants.SPECIMEN_ID);
			isRNA = (String) request.getAttribute(Constants.IS_RNA);
			
            readOnlyValue = false;
        }
        String changeAction = "setFormAction('" + formName + "');";
        request.setAttribute("isRNA", isRNA);
        request.setAttribute("changeAction", changeAction);
        request.setAttribute("formName", formName);
		/** 
		* Sets the isRNA attribute. It is used to display "Ratio 28S To 18S" field
		* only for Specimen of Type = "Molecular" and subType = "RNA". 
		*/
        String specimenID = (String)request.getAttribute(Constants.SPECIMEN_ID);
       
        if(((request.getAttribute("isRNA") != null) && (request.getAttribute("isRNA").equals("true"))) || ((request.getAttribute("molecularSpecimenReviewParametersForm") != null) && !(molecularSpecimenReviewParametersForm.getIsRNA() == null) && (molecularSpecimenReviewParametersForm.getIsRNA().equals("true"))))
		{ 
        	molecularSpecimenReviewParametersForm.setCheckRNA("true");
		}
        else
        {
        	molecularSpecimenReviewParametersForm.setCheckRNA("false");
        }
       

        if(specimenID != null)
        {
            DefaultBizLogic  specimenBizLogic = (DefaultBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
            Object object = specimenBizLogic.retrieve(Specimen.class.getName(), new Long(specimenID));
            if(object != null)
            {
	            Specimen specimen =(Specimen)object;
	            String strClass = specimen.getClassName();
	            String strType = specimen.getSpecimenType();
	            
	            if(strClass.equals(Constants.MOLECULAR) && strType.equals(Constants.RNA))
	            {
	            	request.setAttribute("isRNA", Constants.TRUE);
	            }
            }
            else
            {
            	request.setAttribute("isRNA", Constants.FALSE);
            }
        }
        else
        {
        	request.setAttribute("isRNA", Constants.FALSE);
        }
	}
	
}
