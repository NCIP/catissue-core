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
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;

/**
 * This class initializes the fields in the  Distribution Add/Edit webpage.
 * @author aniruddha_phadnis
 */
public class  DistributionAction extends SpecimenEventParametersAction
{
	protected void setRequestParameters(HttpServletRequest request)
	{
		
		try
		{
			super.setRequestParameters(request);
			
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
	        String [] displayName = {"title"};
	        
	        List protocolList = dao.getList(sourceObjectName, displayName, valueField);
			request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);
			
			
			
			//SET THE CELL SPECIMEN LIST
			String [] displayNameField = {"systemIdentifier"};
			List specimenList = dao.getList(CellSpecimen.class.getName(), displayNameField, valueField);
			String [] cellSpecimenIds = new String[specimenList.size()];
			cellSpecimenIds[0] = Constants.SELECT_OPTION;
			
			for(int i=1;i<specimenList.size();i++)
			{
				NameValueBean bean = (NameValueBean)specimenList.get(i);
				cellSpecimenIds[i] = bean.getValue();
				i++;
			}
			
			request.setAttribute(Constants.CELL_SPECIMEN_ID_LIST, cellSpecimenIds);
			
			//SET THE FLUID SPECIMEN LIST
			specimenList = dao.getList(FluidSpecimen.class.getName(), displayNameField, valueField);
			String [] fluidSpecimenIds = new String[specimenList.size()];
			fluidSpecimenIds[0] = Constants.SELECT_OPTION;
			
			for(int i=1;i<specimenList.size();i++)
			{
				NameValueBean bean = (NameValueBean)specimenList.get(i);
				fluidSpecimenIds[i] = bean.getValue();
				i++;
			}
			
			request.setAttribute(Constants.FLUID_SPECIMEN_ID_LIST, fluidSpecimenIds);
			
			//SET THE MOLECULAR SPECIMEN LIST
			specimenList = dao.getList(MolecularSpecimen.class.getName(), displayNameField, valueField);
			String [] molecularSpecimenIds = new String[specimenList.size()];
			molecularSpecimenIds[0] = Constants.SELECT_OPTION;
			
			for(int i=1;i<specimenList.size();i++)
			{
				NameValueBean bean = (NameValueBean)specimenList.get(i);
				molecularSpecimenIds[i] = bean.getValue();
				i++;
			}
			
			request.setAttribute(Constants.MOLECULAR_SPECIMEN_ID_LIST, molecularSpecimenIds);
			
			//SET THE TISSUE SPECIMEN LIST
			specimenList = dao.getList(TissueSpecimen.class.getName(), displayNameField, valueField);
			String [] tissueSpecimenIds = new String[specimenList.size()];
			tissueSpecimenIds[0] = Constants.SELECT_OPTION;
			
			for(int i=1;i<specimenList.size();i++)
			{
				NameValueBean bean = (NameValueBean)specimenList.get(i);
				tissueSpecimenIds[i] = bean.getValue();
				i++;
			}
			
			request.setAttribute(Constants.TISSUE_SPECIMEN_ID_LIST, tissueSpecimenIds);
			
			request.setAttribute(Constants.SPECIMEN_CLASS_LIST,Constants.SPECIMEN_TYPE_VALUES);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}