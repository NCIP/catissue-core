/**
 * <p>Title: Distribution Class>
 * <p>Description:	This class initializes the fields in the  Distribution Add/Edit webpage.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Aug 10, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This class initializes the fields in the  Distribution Add/Edit webpage.
 * @author aniruddha_phadnis
 */
public class  DistributionAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request)
	{
		super.setRequestParameters(request);
		
		try
		{
			DistributionBizLogic dao = (DistributionBizLogic)BizLogicFactory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
			
			//Sets the Site list.
	        String sourceObjectName = Site.class.getName();
	        String[] displayNameFields = {"name"};
	        String valueField = "systemIdentifier";
	        
	        List siteList = dao.getList(sourceObjectName, displayNameFields, valueField);
			 
			request.setAttribute(Constants.FROMSITELIST, siteList);
			request.setAttribute(Constants.TOSITELIST, siteList);
			
			//Sets the Distribution Protocol Id List.
	        sourceObjectName = DistributionProtocol.class.getName();
	        String [] displayNameField = {"systemIdentifier"};
	        
	        List protocolList = dao.getList(sourceObjectName, displayNameField, valueField);
			request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);
			
			//Sets the Specimen list.
	        sourceObjectName = Specimen.class.getName();
	        
	        List specimenList = dao.getList(sourceObjectName, displayNameField, valueField);
			request.setAttribute(Constants.ITEMLIST, specimenList);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}