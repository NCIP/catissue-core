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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DistributionBizLogic;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

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
	        
	        List siteList = dao.getList(sourceObjectName, displayNameFields, valueField, true);
			 
			request.setAttribute(Constants.FROMSITELIST, siteList);
			request.setAttribute(Constants.TOSITELIST, siteList);
			
			//Sets the Distribution Protocol Id List.
	        sourceObjectName = DistributionProtocol.class.getName();
	        String [] displayName = {"title"};
	        
	        List protocolList = dao.getList(sourceObjectName, displayName, valueField, true);
			request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);
			/*SET THE CELL SPECIMEN LIST
			String [] displayNameField = {"systemIdentifier"};
			List specimenList = dao.getList(CellSpecimen.class.getName(), displayNameField, valueField, true);
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
			specimenList = dao.getList(FluidSpecimen.class.getName(), displayNameField, valueField, true);
			String [] fluidSpecimenIds = new String[specimenList.size()];
			fluidSpecimenIds[0] = Constants.SELECT_OPTION;
			
			for(int i=1;i<specimenList.size();i++)
			{
				NameValueBean bean = (NameValueBean)specimenList.get(i);
				fluidSpecimenIds[i] = bean.getValue();
				Logger.out.debug("fluidSpecimenIds "+fluidSpecimenIds[i]);
				i++;
			}
			
			request.setAttribute(Constants.FLUID_SPECIMEN_ID_LIST, fluidSpecimenIds);
			
			//SET THE MOLECULAR SPECIMEN LIST
			specimenList = dao.getList(MolecularSpecimen.class.getName(), displayNameField, valueField, true);
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
			specimenList = dao.getList(TissueSpecimen.class.getName(), displayNameField, valueField, true);
			String [] tissueSpecimenIds = new String[specimenList.size()];
			tissueSpecimenIds[0] = Constants.SELECT_OPTION;
			
			for(int i=1;i<specimenList.size();i++)
			{
				NameValueBean bean = (NameValueBean)specimenList.get(i);
				tissueSpecimenIds[i] = bean.getValue();
				i++;
			}
			
			request.setAttribute(Constants.TISSUE_SPECIMEN_ID_LIST, tissueSpecimenIds);
			
			request.setAttribute(Constants.SPECIMEN_CLASS_LIST,Constants.SPECIMEN_TYPE_VALUES);*/
			
			
			//SET THE SPECIMEN Ids LIST
			String [] displayNameField = {Constants.SYSTEM_IDENTIFIER};
			List specimenList = dao.getList(Specimen.class.getName(), displayNameField, valueField, true);
			request.setAttribute(Constants.SPECIMEN_ID_LIST,specimenList);
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
		}
	}
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
		super.executeSecureAction(mapping,form,request,response);
		DistributionForm dForm = (DistributionForm)form;
		if(dForm.isIdChange())
		{
			setSpecimenChars(dForm,request);
		}
		Logger.out.debug("executeSecureAction");
		return mapping.findForward((String)request.getParameter(Constants.PAGEOF));
    }
	
	private void setSpecimenChars(DistributionForm dForm,HttpServletRequest request)
	{
		try
		{
			DistributionBizLogic dao = (DistributionBizLogic)BizLogicFactory.getBizLogic(Constants.DISTRIBUTION_FORM_ID);
			
			String specimenIdKey = request.getParameter("specimenIdKey");
			
			int start = specimenIdKey.indexOf(":");
			int end = specimenIdKey.indexOf("_");
			int rowNo = Integer.parseInt(specimenIdKey.substring(start+1,end));
			//int a = Integer.parseInt()
			Logger.out.debug("row number of the dist item: "+rowNo);
			List list = dao.retrieve(Specimen.class.getName(),Constants.SYSTEM_IDENTIFIER,dForm.getValue("DistributedItem:"+rowNo
																													+"_Specimen_systemIdentifier"));
			Logger.out.debug("DistributedItem:1_Specimen_systemIdentifier"+dForm.getValue("DistributedItem:"+rowNo+"_Specimen_systemIdentifier"));
			Specimen specimen =(Specimen)list.get(0);
			SpecimenCharacteristics specimenCharacteristics = specimen.getSpecimenCharacteristics();
			
			Logger.out.debug("SpecimenCharacteristics: "+specimenCharacteristics.getTissueSite()+","+
					specimenCharacteristics.getTissueSide()+","+specimenCharacteristics.getPathologicalStatus());
			dForm.setValue("DistributedItem:"+rowNo+"_Specimen_className",specimen.getClassName());
			dForm.setValue("DistributedItem:"+rowNo+"_tissueSite",specimenCharacteristics.getTissueSite());
			dForm.setValue("DistributedItem:"+rowNo+"_tissueSide",specimenCharacteristics.getTissueSide());
			dForm.setValue("DistributedItem:"+rowNo+"_pathologicalStatus",specimenCharacteristics.getPathologicalStatus());
			
			Map map = dForm.getValues();
			Logger.out.debug("Map values after speci chars are set: "+map);
			dForm.setIdChange(false);
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
		}
	}
}