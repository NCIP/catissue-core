/**
 * <p>Title: AliquotAction Class>
 * <p>Description:	AliquotAction initializes all the fields of the page, Aliquots.jsp.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on May 12, 2006
 */
package edu.wustl.catissuecore.action;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AliquotForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;


/**
 * AliquotAction initializes all the fields of the page, Aliquots.jsp.
 * @author aniruddha_phadnis
 */
public class AliquotAction extends BaseAction //SecureAction
{
	/**
     * Overrides the execute method of Action class.
     */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AliquotForm aliquotForm = (AliquotForm)form;
		
		String specimenId = null;
		
		if(request.getAttribute(Constants.SYSTEM_IDENTIFIER) != null)
		{
			specimenId = String.valueOf(request.getAttribute(Constants.SYSTEM_IDENTIFIER));
		}
		else
		{
			specimenId = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		}
		
		if(specimenId != null)
		{
			aliquotForm.setSpecimenId(specimenId);
			aliquotForm.setCheckedButton("1");
			aliquotForm.setNoOfAliquots(request.getParameter("noOfAliquots"));
			aliquotForm.setQuantityPerAliquot(request.getParameter("quantityPerAliquot"));
		}
		
		//Extracting the values of the operation & pageOf parameters.
        String operation = request.getParameter(Constants.OPERATION);
        String pageOf = request.getParameter(Constants.PAGEOF);
        
        StorageContainerBizLogic bizLogic = (StorageContainerBizLogic)BizLogicFactory.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
        Map containerMap = bizLogic.getAllocatedContainerMap();
        
        if(Constants.PAGEOF_CREATE_ALIQUOT.equals(request.getParameter(Constants.PAGEOF)))
        {
        	pageOf = validate(request,aliquotForm);
        	
        	if(Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
        	{
	        	pageOf = checkForSpecimen(request,aliquotForm);
	        	
	        	if(Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
	        	{	
	        		int aliquotCount = Integer.parseInt(aliquotForm.getNoOfAliquots());
	        		pageOf = checkForSufficientAvailablePositions(request,containerMap,aliquotCount);
	        		
	        		if(Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
	        		{
	        			populateAliquotsStorageLocations(aliquotForm,containerMap);
	        		}
	        	}
        	}
        }
        
        request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP,containerMap);
        request.setAttribute(Constants.PAGEOF,pageOf);
        
        String [] displayNameField = {Constants.SYSTEM_IDENTIFIER};
		List specimenIdList = bizLogic.getList(Specimen.class.getName(), displayNameField, Constants.SYSTEM_IDENTIFIER, true);
		request.setAttribute(Constants.SPECIMEN_ID_LIST,specimenIdList);
		
        return mapping.findForward(pageOf);
	}
	
	//This method checks whether the specimen with given identifier exists or not.
	private String checkForSpecimen(HttpServletRequest request, AliquotForm form) throws Exception
	{
		DefaultBizLogic bizLogic = BizLogicFactory.getDefaultBizLogic();
		String specimenId = form.getSpecimenId();
		List specimenList = new ArrayList();
		String errorString = "";
		
		if(form.getCheckedButton().equals("1"))
		{
			specimenList = bizLogic.retrieve(Specimen.class.getName(),Constants.SYSTEM_IDENTIFIER,new Long(specimenId));
			errorString = Constants.SYSTEM_IDENTIFIER;
		}
		else
		{
			String barcode = form.getBarcode().trim();
			specimenList = bizLogic.retrieve(Specimen.class.getName(),"barcode",barcode);
			errorString = "barcode";
		}
		
		if(specimenList.isEmpty())
		{
			ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
			
			if(errors == null)
			{
				errors = new ActionErrors();
			}
			
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("aliquots.specimen.notExists",errorString));
	        saveErrors(request,errors);
	        
	        return Constants.PAGEOF_ALIQUOT;
		}
		else
		{
			Specimen specimen = (Specimen)specimenList.get(0);
			populateParentSpecimenData(form,specimen);
			
			String pageOf = checkQuantityPerAliquot(request,form);
			
			if(Constants.PAGEOF_CREATE_ALIQUOT.equals(pageOf))
			{
				boolean isDouble = Utility.isQuantityDouble(form.getSpecimenClass(),form.getType());
				
				if(!distributeAvailableQuantity(form,isDouble))
				{
					ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
					
					if(errors == null)
					{
						errors = new ActionErrors();
					}
					
					errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.qtyInsufficient"));
			        saveErrors(request,errors);
			        
					return Constants.PAGEOF_ALIQUOT;
				}
				else
				{
					return Constants.PAGEOF_CREATE_ALIQUOT;
				}
			}
			else
			{
				return Constants.PAGEOF_ALIQUOT;
			}
		}
	}
	
	//This method checks whether there are sufficient storage locations are available or not.
	private String checkForSufficientAvailablePositions(HttpServletRequest request, Map containerMap,int aliquotCount)
	{
		int counter = 0;
		
		if(containerMap.isEmpty())
		{
			return Constants.PAGEOF_ALIQUOT;
		}
		else
		{
			Object [] containerId = containerMap.keySet().toArray();
			
			for(int i=0;i<containerId.length;i++)
			{
				Map xDimMap = (Map)containerMap.get(Integer.valueOf(containerId[i].toString()));
				
				if(!xDimMap.isEmpty())
				{
					Object [] xDim = xDimMap.keySet().toArray();
					
					for(int j=0;j<xDim.length;j++)
					{
						List yDimList = (List)xDimMap.get(Integer.valueOf(xDim[j].toString()));
						counter = counter + yDimList.size();
						
						if(counter >= aliquotCount)
						{
							i = containerId.length;
							break;
						}
					}
				}
			}
		}
		
		if(counter >= aliquotCount)
		{
			return Constants.PAGEOF_CREATE_ALIQUOT;
		}
		else
		{
			ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
			
			if(errors == null)
			{
				errors = new ActionErrors();
			}
			
			errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.locations.notSufficient"));
	        saveErrors(request,errors);
			
			return Constants.PAGEOF_ALIQUOT;
		}
	}
	
	//This method populates parent specimen's data in formbean
	private void populateParentSpecimenData(AliquotForm form,Specimen specimen)
	{
		form.setSpecimenClass(specimen.getClassName());
		form.setType(specimen.getType());
		SpecimenCharacteristics chars = specimen.getSpecimenCharacteristics();
		form.setTissueSite(chars.getTissueSite());
		form.setTissueSide(chars.getTissueSide());
		form.setPathologicalStatus(chars.getPathologicalStatus());
		form.setInitialAvailableQuantity(getAvailableQuantity(specimen));
		form.setAvailableQuantity(getAvailableQuantity(specimen));
		
		if(specimen instanceof MolecularSpecimen)
		{
			String concentration = Utility.toString(((MolecularSpecimen)specimen).getConcentrationInMicrogramPerMicroliter());
			form.setConcentration(concentration);
		}		
	}
	
	//This method returns the available quantity as per the specimen type
	private String getAvailableQuantity(Specimen specimen)
	{
		String availableQuantity = "";
		
		if(specimen instanceof CellSpecimen)
		{
			availableQuantity = String.valueOf(((CellSpecimen)specimen).getAvailableQuantityInCellCount());
		}
		else if(specimen instanceof FluidSpecimen)
		{
			availableQuantity = String.valueOf(((FluidSpecimen)specimen).getAvailableQuantityInMilliliter());
		}
		else if(specimen instanceof MolecularSpecimen)
		{
			availableQuantity = String.valueOf(((MolecularSpecimen)specimen).getAvailableQuantityInMicrogram());
		}
		else if(specimen instanceof TissueSpecimen)
		{
			availableQuantity = String.valueOf(((TissueSpecimen)specimen).getAvailableQuantityInGram());
		}
		
		return availableQuantity;
	}	
	
	/*This method distributes the available quantity among the aliquots. On successful
	  distribution the function return true else false. */
	private boolean distributeAvailableQuantity(AliquotForm form,boolean isDouble)
	{
		int aliquotCount = Integer.parseInt(form.getNoOfAliquots());
		String distributedQuantity;
		
		if(isDouble)
		{
			double availableQuantity = Double.parseDouble(form.getAvailableQuantity());
			double dQuantity;
			DecimalFormat dFormat = new DecimalFormat( "#.000" );
			
			if(form.getQuantityPerAliquot() == null || form.getQuantityPerAliquot().trim().length() == 0)
			{
				dQuantity = availableQuantity / aliquotCount;
				dQuantity = Double.parseDouble(dFormat.format(dQuantity));
			}
			else
			{
				dQuantity = Double.parseDouble(form.getQuantityPerAliquot());
			}
			
			distributedQuantity = String.valueOf(dQuantity);
			availableQuantity = availableQuantity - Double.parseDouble(dFormat.format((dQuantity * aliquotCount)));
			
			if(availableQuantity < 0)
			{
				return false;
			}
			
			form.setAvailableQuantity(String.valueOf(availableQuantity));
		}
		else
		{
			int availableQuantity = (int)Double.parseDouble(form.getAvailableQuantity());
			int iQauntity = (int)(availableQuantity / aliquotCount);
			
			if(form.getQuantityPerAliquot() == null || form.getQuantityPerAliquot().trim().length() == 0)
			{
				iQauntity = (int)(availableQuantity / aliquotCount);
			}
			else
			{
				iQauntity = (int)Double.parseDouble(form.getQuantityPerAliquot());
			}
			
			distributedQuantity = String.valueOf(iQauntity);
			availableQuantity = availableQuantity - (iQauntity * aliquotCount);
			
			if(availableQuantity < 0)
			{
				return false;
			}
			
			form.setAvailableQuantity(String.valueOf(availableQuantity));
		}
		
		Map aliquotMap = form.getAliquotMap();
		
		for(int i=1;i<=aliquotCount;i++)
		{
			String qtyKey = "Specimen:" + i + "_quantity";
			aliquotMap.put(qtyKey,distributedQuantity);
		}

		form.setAliquotMap(aliquotMap);
		return true;
	}
	
	//This function populates the availability map with available storage locations
	private void populateAliquotsStorageLocations(AliquotForm form,Map containerMap)
	{
		Map aliquotMap = form.getAliquotMap();
		int counter = 1;
		
		if(!containerMap.isEmpty())
		{
			Object [] containerId = containerMap.keySet().toArray();
			
			for(int i=0;i<containerId.length;i++)
			{
				Map xDimMap = (Map)containerMap.get(Integer.valueOf(containerId[i].toString()));
				
				if(!xDimMap.isEmpty())
				{
					Object [] xDim = xDimMap.keySet().toArray();
					
					for(int j=0;j<xDim.length;j++)
					{
						List yDimList = (List)xDimMap.get(Integer.valueOf(xDim[j].toString()));
						
						for(int k=0;k<yDimList.size();k++)
						{
							if(counter <= Integer.parseInt(form.getNoOfAliquots()))
							{
								String containerKey = "Specimen:" + counter + "_StorageContainer_systemIdentifier";
								String pos1Key = "Specimen:" + counter + "_positionDimensionOne";
								String pos2Key = "Specimen:" + counter + "_positionDimensionTwo";
								
								aliquotMap.put(containerKey,String.valueOf(containerId[i]));
								aliquotMap.put(pos1Key,String.valueOf(xDim[j]));
								aliquotMap.put(pos2Key,String.valueOf(yDimList.get(k)));
								
								counter++;
							}
							else
							{
								j = xDim.length;
								i = containerId.length;
								break;
							}
						}
					}
				}
			}
		}

		form.setAliquotMap(aliquotMap);
	}
	
	//This function checks the quantity per aliquot is valid or not
	private String checkQuantityPerAliquot(HttpServletRequest request, AliquotForm form) throws Exception
	{
        Validator validator = new Validator();
		String quantityPerAliquot = form.getQuantityPerAliquot();
		ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
		
		if(errors == null)
		{
			errors = new ActionErrors();
		}
		
		if(quantityPerAliquot != null && quantityPerAliquot.trim().length() != 0)
        {
			if(Utility.isQuantityDouble(form.getSpecimenClass(),form.getType()))
			{
		        if(!validator.isDouble(quantityPerAliquot.trim()))
		        {
		        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("aliquots.qtyPerAliquot")));
		        	saveErrors(request,errors);
		        	return Constants.PAGEOF_ALIQUOT;
		        }
			}
			else
			{
				if(!validator.isNumeric(quantityPerAliquot.trim()))
		        {
		        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("aliquots.qtyPerAliquot")));
		        	saveErrors(request,errors);
		        	return Constants.PAGEOF_ALIQUOT;
		        }
			}
        }
		
		return Constants.PAGEOF_CREATE_ALIQUOT;
	}
	
	//This method validates the formbean.
	private String validate(HttpServletRequest request, AliquotForm form)
	{
        Validator validator = new Validator();
		ActionErrors errors = (ActionErrors)request.getAttribute(Globals.ERROR_KEY);
		
		String pageOf = Constants.PAGEOF_CREATE_ALIQUOT;
		
		if(errors == null)
		{
			errors = new ActionErrors();
		}
                 
        if(form.getCheckedButton().equals("1"))
        {
        	if(!validator.isValidOption(form.getSpecimenId()))
        	{
        		errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("createSpecimen.parent")));
        		pageOf = Constants.PAGEOF_ALIQUOT;
        	}
        }
        else
        {
        	if(form.getBarcode() == null || form.getBarcode().trim().length() == 0)
        	{
        		errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.required",ApplicationProperties.getValue("specimen.barcode")));
        		pageOf = Constants.PAGEOF_ALIQUOT;
        	}
        }
        
        if(!validator.isNumeric(form.getNoOfAliquots()))
        {
        	errors.add(ActionErrors.GLOBAL_ERROR,new ActionError("errors.item.format",ApplicationProperties.getValue("aliquots.noOfAliquots")));
        	pageOf = Constants.PAGEOF_ALIQUOT;
        }
        
        saveErrors(request,errors);
        return pageOf;
	}
}
