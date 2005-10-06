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
import java.util.ArrayList;
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
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.TissueSpecimen;
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
	        String valueField = Constants.SYSTEM_IDENTIFIER;
	        
	        List siteList = dao.getList(sourceObjectName, displayNameFields, valueField, true);
			 
			request.setAttribute(Constants.FROMSITELIST, siteList);
			request.setAttribute(Constants.TOSITELIST, siteList);
			
			//Sets the Distribution Protocol Id List.
	        sourceObjectName = DistributionProtocol.class.getName();
	        String [] displayName = {"title"};
	        
	        List protocolList = dao.getList(sourceObjectName, displayName, valueField, true);
			request.setAttribute(Constants.DISTRIBUTIONPROTOCOLLIST, protocolList);

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
    	
    	//List of keys used in map of ActionForm
		List key = new ArrayList();
    	key.add("DistributedItem:i_Specimen_className");
    	key.add("DistributedItem:i_Specimen_systemIdentifier");
    	key.add("DistributedItem:i_quantity");
    	
    	//Gets the map from ActionForm
    	Map map = dForm.getValues();
    	
    	//Calling DeleteRow of BaseAction class
    	DeleteRow(key,map,request);
    	
		
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
			dForm.setValue("DistributedItem:"+rowNo+"_availableQty",getAvailableQty(specimen));
		
			Logger.out.debug("Map values after speci chars are set: "+dForm.getValues());
			dForm.setIdChange(false);
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
		}
	}
	public Object getAvailableQty(Specimen specimen)
	{
		if(specimen instanceof TissueSpecimen)
		{
			TissueSpecimen tissueSpecimen = (TissueSpecimen) specimen;
			Logger.out.debug("tissueSpecimenAvailableQuantityInGram "+tissueSpecimen.getAvailableQuantityInGram());
			return tissueSpecimen.getAvailableQuantityInGram();
		}
		else if(specimen instanceof CellSpecimen)
		{
			CellSpecimen cellSpecimen = (CellSpecimen) specimen;
			return cellSpecimen.getAvailableQuantityInCellCount();
		}
		else if(specimen instanceof MolecularSpecimen)
		{
			MolecularSpecimen molecularSpecimen = (MolecularSpecimen) specimen;
			return molecularSpecimen.getAvailableQuantityInMicrogram();
		}
		else if(specimen instanceof FluidSpecimen)
		{
			FluidSpecimen fluidSpecimen = (FluidSpecimen) specimen;
			return fluidSpecimen.getAvailableQuantityInMilliliter();
		}
		return null;
	}
	
}