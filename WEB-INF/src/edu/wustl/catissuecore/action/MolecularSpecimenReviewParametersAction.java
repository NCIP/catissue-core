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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
	protected void setRequestParameters(HttpServletRequest request) throws Exception
	{
		/** 
		* Sets the isRNA attribute. It is used to display "Ratio 28S To 18S" field
		* only for Specimen of Type = "Molecular" and subType = "RNA". 
		*/
        String specimenID = (String)request.getAttribute(Constants.SPECIMEN_ID); 

        if(specimenID != null)
        {
            DefaultBizLogic  specimenBizLogic = (DefaultBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
            List specimenList = specimenBizLogic.retrieve(Specimen.class.getName(),Constants.SYSTEM_IDENTIFIER,specimenID);
            if(!specimenList.isEmpty() )
            {
	            Specimen specimen =(Specimen)specimenList.get(0);
	            String strClass = specimen.getClassName();
	            String strType = specimen.getType();
	            
	            if(strClass.equals(Constants.MOLECULAR) && strType.equals(Constants.RNA ))
	            	request.setAttribute(Constants.IS_RNA, Constants.TRUE );
            }
            else
            {
            	request.setAttribute(Constants.IS_RNA, Constants.FALSE  );
            }
        }
        else
        {
        	request.setAttribute(Constants.IS_RNA, Constants.FALSE );
        }
	}
}
