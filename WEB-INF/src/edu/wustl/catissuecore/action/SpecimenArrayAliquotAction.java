/*
 * Created on Sep 21, 2006
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.SpecimenArrayAliquotForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayAliquotsBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.global.Status;


/**
 * SpecimenArrayAliquotAction initializes all the fields of the page SpecimenArrayAliquots.jsp.
 * @author jitendra_agrawal
 */
public class SpecimenArrayAliquotAction extends SecureAction
{

	/**
	 * Overrides the execute method of Action class.
	 */
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{	
		SpecimenArrayAliquotForm specimenArrayAliquotForm = (SpecimenArrayAliquotForm) form;
		String pageOf = request.getParameter(Constants.PAGE_OF);		
		StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
		SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(Constants.SESSION_DATA);
		//Bean List for the dropdown for the storage location
		List<NameValueBean> storagePositionListForSpecimenArrayAliquot = AppUtility.getStoragePositionTypeListForTransferEvent();
		request.setAttribute("storagePositionListForSpecimenArrayAliquot", storagePositionListForSpecimenArrayAliquot);
		//boolean to indicate whether the suitable containers to be shown in dropdown 
		//is exceeding the max limit.
		String exceedingMaxLimit = "false";
		if (specimenArrayAliquotForm.getButtonClicked().equalsIgnoreCase("submit"))
		{
			Map tempAliquotMap = new HashMap();
			if (specimenArrayAliquotForm.getCheckedButton().equals("1"))
			{
				tempAliquotMap.put("label", specimenArrayAliquotForm.getParentSpecimenArrayLabel());
			}
			else
			{
				tempAliquotMap.put("barcode", specimenArrayAliquotForm.getBarcode());
			}
			tempAliquotMap.put("aliquotcount", specimenArrayAliquotForm.getAliquotCount());					
			request.getSession().setAttribute("tempAliquotMap", tempAliquotMap);
		}
		else if (specimenArrayAliquotForm.getButtonClicked().equalsIgnoreCase("create"))
		{
			//arePropertiesChanged is used to identify if any of  label/barcode, aliquot count are changed
			boolean arePropertiesChanged = false;
			Map tempAliquotMap = (HashMap) request.getSession().getAttribute("tempAliquotMap");
			String label = (String) tempAliquotMap.get("label");
			String barcode = (String) tempAliquotMap.get("barcode");
			if (specimenArrayAliquotForm.getCheckedButton().equals("1"))
			{
				if (label == null || !label.trim().equalsIgnoreCase(specimenArrayAliquotForm.getParentSpecimenArrayLabel().trim()))
				{
					arePropertiesChanged = true;
				}
			}
			else
			{
				if (barcode == null || !barcode.trim().equalsIgnoreCase(specimenArrayAliquotForm.getBarcode().trim()))
				{
					arePropertiesChanged = true;
				}

			}
			String aliquotcount = (String) tempAliquotMap.get("aliquotcount");
			if (!aliquotcount.trim().equalsIgnoreCase(specimenArrayAliquotForm.getAliquotCount().trim()))
			{
				arePropertiesChanged = true;
			}
			
			/**
			 * Repopulate the form with storage container locations in case user has changed 
			 * any of label/barcode, aliquot count, quantity per aliquot.
			 */
			if (arePropertiesChanged == true) 
			{
				specimenArrayAliquotForm.setParentSpecimenArrayLabel(label);
				specimenArrayAliquotForm.setAliquotCount(aliquotcount);
				specimenArrayAliquotForm.setBarcode(barcode);
				
				
				ActionErrors errors = getActionErrors(request);

				if (errors == null)
				{
					errors = new ActionErrors();
				}
				if (arePropertiesChanged == true)
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.specimenArrayAliquots.reSubmit"));
				}
				
				TreeMap containerMap = new TreeMap();
				checkForSpecimenArray(request, specimenArrayAliquotForm);
				int aliquotCount = Integer.parseInt(specimenArrayAliquotForm.getAliquotCount());
				Long id = (Long)request.getAttribute(Constants.STORAGE_TYPE_ID);
				containerMap = bizLogic.getAllocatedContaienrMapForSpecimenArray(id.longValue(), 0,sessionData,exceedingMaxLimit);				
				populateAliquotsStorageLocations(specimenArrayAliquotForm, containerMap);				
				request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
				request.setAttribute(Constants.PAGE_OF, Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT);
				saveErrors(request, errors);
				return mapping.findForward(Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT);

			}
			else
			{
				//TODO
				specimenArrayAliquotForm.setButtonClicked("none");
				return mapping.findForward(Constants.COMMON_ADD_EDIT);
			}

		}	
		
		if (Constants.PAGE_OF_SPECIMEN_ARRAY_ALIQUOT_SUMMARY.equals(pageOf))
		{	
			Map map = (Map) request.getAttribute("forwardToHashMap");
		
			if (map != null)
			{
				//TODO
				specimenArrayAliquotForm.setSpecimenClass(Utility.toString(map.get(Constants.ALIQUOT_SPECIMEN_CLASS)));				
				specimenArrayAliquotForm.setSpecimenArrayType(Utility.toString(map.get(Constants.ALIQUOT_SPECIMEN_ARRAY_TYPE)));
				specimenArrayAliquotForm.setAliquotCount(Utility.toString(map.get(Constants.ALIQUOT_ALIQUOT_COUNTS)));								
				Collection specimenTypesCollection = (Collection) map.get(Constants.ALIQUOT_SPECIMEN_TYPES);
				List specimenTypeList = setSpecimenTypes(specimenTypesCollection, specimenArrayAliquotForm);				
				request.setAttribute(Constants.SPECIMEN_TYPE_LIST,specimenTypeList);				
				specimenArrayAliquotForm.setSpecimenArrayAliquotMap(map);
			}

			ActionErrors errors = getActionErrors(request);

			if (errors == null || errors.size() == 0)
			{
				ActionMessages messages = new ActionMessages();
				messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("aliquots.success"));
				saveMessages(request, messages);
			}
			return mapping.findForward(pageOf);
		}
		
		
		Map containerMap = new HashMap();		
		if (Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT.equals(request.getParameter(Constants.PAGE_OF)))
		{			
			pageOf = validate(request, specimenArrayAliquotForm);

			if (Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT.equals(pageOf))
			{
				pageOf = checkForSpecimenArray(request, specimenArrayAliquotForm);

				if (Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT.equals(pageOf))
				{
					int aliquotCount = Integer.parseInt(specimenArrayAliquotForm.getAliquotCount());
					Long id = (Long)request.getAttribute(Constants.STORAGE_TYPE_ID);
					containerMap = bizLogic.getAllocatedContaienrMapForSpecimenArray(id.longValue(), 0,sessionData,exceedingMaxLimit);					
					pageOf = checkForSufficientAvailablePositions(request, containerMap, aliquotCount);

					if (Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT.equals(pageOf))
					{
						ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
						if (errors == null || errors.size() == 0)
						{
						   populateAliquotsStorageLocations(specimenArrayAliquotForm, containerMap);
						}
					}					
				}
			}
		}
		request.setAttribute(Constants.EXCEEDS_MAX_LIMIT,exceedingMaxLimit);
		request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
		request.setAttribute(Constants.PAGE_OF, pageOf);
		return mapping.findForward(pageOf);
	}
	
	
	private String validate(HttpServletRequest request, SpecimenArrayAliquotForm form)
	{		
		
		return Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT; 
	}
	
	/**
	 * 
	 * @param request HttpServletRequest
	 * @param form SpecimenArrayAliquotForm
	 * @return String
	 * @throws Exception
	 */
	private String checkForSpecimenArray(HttpServletRequest request, SpecimenArrayAliquotForm form) throws BizLogicException,Exception
	{	
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);		
		List specimenArrayList = new ArrayList();
		String errorString = "";
		String specimenArrayLabel = form.getParentSpecimenArrayLabel();
		int aliquotCount = Integer.parseInt(form.getAliquotCount());
		if (form.getCheckedButton().equals("1"))
		{ 
			
			specimenArrayList = bizLogic.retrieve(SpecimenArray.class.getName(), Constants.SYSTEM_NAME, specimenArrayLabel);
			errorString = Constants.SYSTEM_LABEL;
		} 
		else
		{
			String barcode = form.getBarcode().trim();
			specimenArrayList = bizLogic.retrieve(SpecimenArray.class.getName(), Constants.SYSTEM_BARCODE, barcode);
			errorString = Constants.SYSTEM_BARCODE;
		}
		
		if (specimenArrayList.isEmpty())
		{
			ActionErrors errors = getActionErrors(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.specimenArrayAliquots.notExists", errorString));
			saveErrors(request, errors);
			return Constants.PAGE_OF_SPECIMEN_ARRAY_ALIQUOT;
		} 
		else
		{
			SpecimenArray specimenArray = (SpecimenArray) specimenArrayList.get(0);
			/**
			 * Name : Virender
			 * Reviewer: Prafull
			 * Retriving specimenArrayTypeObject
			 * replaced SpecimenArrayType arrayType = specimenArray.getSpecimenArrayType();
			 */
			if(Status.ACTIVITY_STATUS_DISABLED.toString().equals(specimenArray.getActivityStatus()))
			{
				ActionErrors errors = getActionErrors(request);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.specimenArrayAliquots.disabled","Parent Specimen Array"));
				saveErrors(request,errors);
				return Constants.PAGE_OF_SPECIMEN_ARRAY_ALIQUOT;
				//throw BizLogicException("Fail to create Aliquots, Parent SpecimenArray" + " " + ApplicationProperties.getValue("error.object.disabled"));
			}
			SpecimenArrayType arrayType = (SpecimenArrayType)bizLogic.retrieveAttribute(SpecimenArray.class.getName(),specimenArray.getId(),"specimenArrayType");
			form.setSpecimenArrayType(arrayType.getName());	
			form.setSpecimenClass(arrayType.getSpecimenClass());
			
			/**
			 * Name: Virender Mehta
			 * Reviewer: Prafull
			 * Retrive Child Specimen Collection from parent Specimen
			 * String[] specimenTypeArr = new String[arrayType.getSpecimenTypeCollection().size()]; 
			 */
			Collection specimenTypeCollection = (Collection)bizLogic.retrieveAttribute(SpecimenArrayType.class.getName(),arrayType.getId(),"elements(specimenTypeCollection)");
			String[] specimenTypeArr = new String[specimenTypeCollection.size()];
			
			List specimenTypeList = setSpecimenTypes(specimenTypeCollection, form);
			request.setAttribute(Constants.SPECIMEN_TYPE_LIST,specimenTypeList);
			
			request.setAttribute(Constants.STORAGE_TYPE_ID, arrayType.getId());
			
			Map aliquotMap = form.getSpecimenArrayAliquotMap();
			
			SpecimenArrayAliquotsBizLogic aliquotBizLogic = (SpecimenArrayAliquotsBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_ARRAY_ALIQUOT_FORM_ID);
			long nextAvailablenumber = aliquotBizLogic.getNextAvailableNumber("CATISSUE_SPECIMEN_ARRAY");
			
			/**
			 *  Putting the default label values in the AliquotMap 
			 */
			for (int i = 1; i <= aliquotCount; i++)
			{				
				
				String labelKey = "SpecimenArray:" + i + "_label";
				String aliquotLabel = specimenArrayLabel + "_" + (nextAvailablenumber + i - 1);
				aliquotMap.put(labelKey, aliquotLabel);
			}

			form.setSpecimenArrayAliquotMap(aliquotMap);
			form.setSpecimenArrayId(""+ specimenArray.getId());		
			
		}
		
		return Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT; 
	}
	
	/**
	 * This method checks whether there are sufficient storage locations are available or not.
	 * @param request HttpServletRequest
	 * @param containerMap Map
	 * @param aliquotCount int
	 * @return String
	 */
	private String checkForSufficientAvailablePositions(HttpServletRequest request, Map containerMap, int aliquotCount)
	{	
		int counter = 0;
		if (containerMap.isEmpty())
		{
			ActionErrors errors = getActionErrors(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.specimenArrayAliquots.locationsNotSufficient"));
			saveErrors(request, errors);
			return Constants.PAGE_OF_SPECIMEN_ARRAY_ALIQUOT;
		}
		else
		{
			counter = StorageContainerUtil.checkForLocation(containerMap, aliquotCount, counter);
		}
		if (counter >= aliquotCount)
		{
			return Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT;
		}
		else
		{
			ActionErrors errors = getActionErrors(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.specimenArrayAliquots.locationsNotSufficient"));
			saveErrors(request, errors);
			return Constants.PAGE_OF_SPECIMEN_ARRAY_ALIQUOT;
		}		 
	}

	/**
	 * This function populates the availability map with available storage locations
	 * @param form SpecimenArrayAliquotForm
	 * @param containerMap Map
	 */
	private void populateAliquotsStorageLocations(SpecimenArrayAliquotForm form, Map containerMap)
	{
		Map aliquotMap = form.getSpecimenArrayAliquotMap();
		String noOfAliquots = form.getAliquotCount();
		StorageContainerUtil.populateAliquotMap("SpecimenArray",containerMap, aliquotMap,noOfAliquots);
		form.setSpecimenArrayAliquotMap(aliquotMap);

	}
		
	/**
	 * This method returns the ActionErrors object present in the request scope.
	 * If it is absent method creates & returns new ActionErrors object.
	 * @param request
	 * @return
	 */
	private ActionErrors getActionErrors(HttpServletRequest request)
	{
		ActionErrors errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
		if (errors == null)
		{
			errors = new ActionErrors();
		}
		return errors;
	}
	
	/**
	 * 
	 * @param specimenTypeCollection Collection
	 * @param form SpecimenArrayAliquotForm
	 * @retutn List 
	 */
	private List setSpecimenTypes(Collection specimenTypeCollection, SpecimenArrayAliquotForm form)
	{
		String[] specimenTypeArr = new String[specimenTypeCollection.size()];
		List specimenTypeList = new ArrayList();
		int i = 0;
		String specimenType = null;
		NameValueBean nameValueBean = null;
		for (Iterator iter = specimenTypeCollection.iterator(); iter
				.hasNext();i++) {
			specimenType = (String) iter.next();
			specimenTypeArr[i] = specimenType;
			nameValueBean = new NameValueBean(specimenType,specimenType);
			specimenTypeList.add(nameValueBean);
		}
		form.setSpecimenTypes(specimenTypeArr);		
		return specimenTypeList;
	}
	
}
