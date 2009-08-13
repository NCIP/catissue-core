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
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.dao.DAO;

/**
 * SpecimenArrayAliquotAction initializes all the fields of the page
 * SpecimenArrayAliquots.jsp.
 *
 * @author jitendra_agrawal
 */
public class SpecimenArrayAliquotAction extends SecureAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form : actionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */

	@Override
	public ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DAO dao = null;
		String pageOf = null;
		try
		{
			final SessionDataBean sessionDataBean = (SessionDataBean) request
			.getSession().getAttribute(Constants.SESSION_DATA);
			dao=AppUtility.openDAOSession(sessionDataBean);
			final SpecimenArrayAliquotForm specimenArrayAliquotForm = (SpecimenArrayAliquotForm) form;
			pageOf = request.getParameter(Constants.PAGE_OF);
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) factory
					.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);
			final SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
					Constants.SESSION_DATA);
			// Bean List for the dropdown for the storage location
			final List<NameValueBean> storagePositionListForSpecimenArrayAliquot = AppUtility
					.getStoragePositionTypeListForTransferEvent();
			request.setAttribute("storagePositionListForSpecimenArrayAliquot",
					storagePositionListForSpecimenArrayAliquot);
			// boolean to indicate whether the suitable containers to be shown in
			// dropdown
			// is exceeding the max limit.
			final String exceedingMaxLimit = "false";
			if (specimenArrayAliquotForm.getButtonClicked().equalsIgnoreCase("submit"))
			{
				final Map tempAliquotMap = new HashMap();
				if (specimenArrayAliquotForm.getCheckedButton().equals("1"))
				{
					tempAliquotMap.put("label", specimenArrayAliquotForm.
							getParentSpecimenArrayLabel());
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
				// arePropertiesChanged is used to identify if any of label/barcode,
				// aliquot count are changed
				boolean arePropertiesChanged = false;
				final Map tempAliquotMap = (HashMap) request.getSession()
						.getAttribute("tempAliquotMap");
				final String label = (String) tempAliquotMap.get("label");
				final String barcode = (String) tempAliquotMap.get("barcode");
				if (specimenArrayAliquotForm.getCheckedButton().equals("1"))
				{
					if (label == null
							|| !label.trim().equalsIgnoreCase(
									specimenArrayAliquotForm.
									getParentSpecimenArrayLabel().trim()))
					{
						arePropertiesChanged = true;
					}
				}
				else
				{
					if (barcode == null
							|| !barcode.trim().equalsIgnoreCase(
									specimenArrayAliquotForm.getBarcode().trim()))
					{
						arePropertiesChanged = true;
					}
	
				}
				final String aliquotcount = (String) tempAliquotMap.get("aliquotcount");
				if (!aliquotcount.trim().equalsIgnoreCase(
						specimenArrayAliquotForm.getAliquotCount().trim()))
				{
					arePropertiesChanged = true;
				}
	
				/**
				 * Repopulate the form with storage container locations in case user
				 * has changed any of label/barcode, aliquot count, quantity per
				 * aliquot.
				 */
				if (arePropertiesChanged == true)
				{
					specimenArrayAliquotForm.setParentSpecimenArrayLabel(label);
					specimenArrayAliquotForm.setAliquotCount(aliquotcount);
					specimenArrayAliquotForm.setBarcode(barcode);
	
					ActionErrors errors = this.getActionErrors(request);
	
					if (errors == null)
					{
						errors = new ActionErrors();
					}
					if (arePropertiesChanged == true)
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.specimenArrayAliquots.reSubmit"));
					}
	
					TreeMap containerMap = new TreeMap();
					this.checkForSpecimenArray(request, specimenArrayAliquotForm,dao);
					// int aliquotCount =
					// Integer.parseInt(specimenArrayAliquotForm.getAliquotCount());
					final Long id = (Long) request.getAttribute(Constants.STORAGE_TYPE_ID);
					containerMap = bizLogic.getAllocatedContaienrMapForSpecimenArray(id.longValue(), 0,
							sessionData, exceedingMaxLimit,dao);
					this.populateAliquotsStorageLocations(specimenArrayAliquotForm, containerMap);
					request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
					request.setAttribute(Constants.PAGE_OF,
							Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT);
					this.saveErrors(request, errors);
					return mapping.findForward(Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT);
	
				}
				else
				{
					// TODO
					specimenArrayAliquotForm.setButtonClicked("none");
					return mapping.findForward(Constants.COMMON_ADD_EDIT);
				}
	
			}
	
			if (Constants.PAGE_OF_SPECIMEN_ARRAY_ALIQUOT_SUMMARY.equals(pageOf))
			{
				final Map map = (Map) request.getAttribute("forwardToHashMap");
	
				if (map != null)
				{
					// TODO
					specimenArrayAliquotForm.setSpecimenClass(CommonUtilities.toString(map
							.get(Constants.ALIQUOT_SPECIMEN_CLASS)));
					specimenArrayAliquotForm.setSpecimenArrayType(CommonUtilities.toString(map
							.get(Constants.ALIQUOT_SPECIMEN_ARRAY_TYPE)));
					specimenArrayAliquotForm.setAliquotCount(CommonUtilities.toString(map
							.get(Constants.ALIQUOT_ALIQUOT_COUNTS)));
					final Collection specimenTypesCollection = (Collection) map
							.get(Constants.ALIQUOT_SPECIMEN_TYPES);
					final List specimenTypeList = this.setSpecimenTypes(specimenTypesCollection,
							specimenArrayAliquotForm);
					request.setAttribute(Constants.SPECIMEN_TYPE_LIST, specimenTypeList);
					specimenArrayAliquotForm.setSpecimenArrayAliquotMap(map);
				}
	
				final ActionErrors errors = this.getActionErrors(request);
	
				if (errors == null || errors.size() == 0)
				{
					final ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("aliquots.success"));
					this.saveMessages(request, messages);
				}
				return mapping.findForward(pageOf);
			}
	
			Map containerMap = new HashMap();
			if (Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT.equals(request
					.getParameter(Constants.PAGE_OF)))
			{
				pageOf = this.validate(request, specimenArrayAliquotForm);
	
				if (Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT.equals(pageOf))
				{
					pageOf = this.checkForSpecimenArray(request, specimenArrayAliquotForm,dao);
	
					if (Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT.equals(pageOf))
					{
						final int aliquotCount = Integer.parseInt(specimenArrayAliquotForm
								.getAliquotCount());
						final Long id = (Long) request.getAttribute(Constants.STORAGE_TYPE_ID);
						containerMap = bizLogic.getAllocatedContaienrMapForSpecimenArray(
								id.longValue(), 0, sessionData, exceedingMaxLimit,dao);
						pageOf = this.checkForSufficientAvailablePositions(request, containerMap,
								aliquotCount);
	
						if (Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT.equals(pageOf))
						{
							final ActionErrors errors = (ActionErrors) request
									.getAttribute(Globals.ERROR_KEY);
							if (errors == null || errors.size() == 0)
							{
								this.populateAliquotsStorageLocations
								(specimenArrayAliquotForm,
										containerMap);
							}
						}
					}
				}
			}
			request.setAttribute(Constants.EXCEEDS_MAX_LIMIT, exceedingMaxLimit);
			request.setAttribute(Constants.AVAILABLE_CONTAINER_MAP, containerMap);
			request.setAttribute(Constants.PAGE_OF, pageOf);
		}
		finally
		{
			dao.closeSession();
		}
		return mapping.findForward(pageOf);
	}

	/**
	 *
	 * @param request : request
	 * @param form : form
	 * @return String : String
	 */
	private String validate(HttpServletRequest request, SpecimenArrayAliquotForm form)
	{

		return Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT;
	}

	/**
	 * @param request
	 *            HttpServletRequest
	 * @param form
	 *            SpecimenArrayAliquotForm
	 * @return String : String
	 * @throws BizLogicException : BizLogicException
	 * @throws Exception : Exception
	 */
	private String checkForSpecimenArray(HttpServletRequest request, SpecimenArrayAliquotForm form,DAO dao)
			throws BizLogicException, Exception
	{
		List specimenArrayList = new ArrayList();
		String errorString = "";
		final String specimenArrayLabel = form.getParentSpecimenArrayLabel();
		final int aliquotCount = Integer.parseInt(form.getAliquotCount());
		if (form.getCheckedButton().equals("1"))
		{

			specimenArrayList = dao.retrieve(SpecimenArray.class.getName(),
					Constants.SYSTEM_NAME, specimenArrayLabel);
			errorString = Constants.SYSTEM_LABEL;
		}
		else
		{
			final String barcode = form.getBarcode().trim();
			specimenArrayList = dao.retrieve(SpecimenArray.class.getName(),
					Constants.SYSTEM_BARCODE, barcode);
			errorString = Constants.SYSTEM_BARCODE;
		}

		if (specimenArrayList.isEmpty())
		{
			final ActionErrors errors = this.getActionErrors(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.specimenArrayAliquots.notExists", errorString));
			this.saveErrors(request, errors);
			return Constants.PAGE_OF_SPECIMEN_ARRAY_ALIQUOT;
		}
		else
		{
			final SpecimenArray specimenArray = (SpecimenArray) specimenArrayList
					.get(0);
			/**
			 * Name : Virender Reviewer: Prafull Retriving
			 * specimenArrayTypeObject replaced SpecimenArrayType arrayType =
			 * specimenArray.getSpecimenArrayType();
			 */
			if (Status.ACTIVITY_STATUS_DISABLED.toString().equals(
					specimenArray.getActivityStatus())) {
				final ActionErrors errors = this.getActionErrors(request);
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
						"errors.specimenArrayAliquots.disabled",
						"Parent Specimen Array"));
				this.saveErrors(request, errors);
				return Constants.PAGE_OF_SPECIMEN_ARRAY_ALIQUOT;
				// throw BizLogicException(
				// "Fail to create Aliquots, Parent SpecimenArray" + " " +
				// ApplicationProperties.getValue("error.object.disabled"));
			}
			final List arrayTypeList = (List) dao.retrieveAttribute(
					SpecimenArray.class, "id", specimenArray.getId(),
					"specimenArrayType");
			if ((arrayTypeList != null) && (arrayTypeList.size() > 0)) {
				final SpecimenArrayType arrayType =(SpecimenArrayType) arrayTypeList.get(0);
				form.setSpecimenArrayType(arrayType.getName());
				form.setSpecimenClass(arrayType.getSpecimenClass());

				/**
				 * Name: Virender Mehta Reviewer: Prafull Retrive Child Specimen
				 * Collection from parent Specimen String[] specimenTypeArr =
				 * new String[arrayType.getSpecimenTypeCollection().size()];
				 */
				final Collection specimenTypeCollection = (Collection) dao
						.retrieveAttribute(SpecimenArrayType.class, "id",
								arrayType.getId(),
								"elements(specimenTypeCollection)");
				// String[] specimenTypeArr = new
				// String[specimenTypeCollection.size()];

				final List specimenTypeList = this.setSpecimenTypes(
						specimenTypeCollection, form);
				request.setAttribute(Constants.SPECIMEN_TYPE_LIST,
						specimenTypeList);

				request.setAttribute(Constants.STORAGE_TYPE_ID, arrayType
						.getId());

				final Map aliquotMap = form.getSpecimenArrayAliquotMap();

				final SpecimenArrayAliquotsBizLogic aliquotBizLogic = (SpecimenArrayAliquotsBizLogic) AbstractFactoryConfig
						.getInstance().getBizLogicFactory().getBizLogic(
								Constants.SPECIMEN_ARRAY_ALIQUOT_FORM_ID);
				final long nextAvailablenumber = aliquotBizLogic
						.getNextAvailableNumber("CATISSUE_SPECIMEN_ARRAY");

				/**
				 * Putting the default label values in the AliquotMap
				 */
				for (int i = 1; i <= aliquotCount; i++) {

					final String labelKey = "SpecimenArray:" + i + "_label";
					final String aliquotLabel = specimenArrayLabel + "_"
							+ (nextAvailablenumber + i - 1);
					aliquotMap.put(labelKey, aliquotLabel);
				}

				form.setSpecimenArrayAliquotMap(aliquotMap);
				form.setSpecimenArrayId("" + specimenArray.getId());
			}
		}

		return Constants.PAGE_OF_SPECIMEN_ARRAY_CREATE_ALIQUOT;
	}

	/**
	 * This method checks whether there are sufficient storage locations are
	 * available or not.
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param containerMap
	 *            Map
	 * @param aliquotCount
	 *            int
	 * @return String
	 */
	private String checkForSufficientAvailablePositions(HttpServletRequest request,
			Map containerMap, int aliquotCount)
	{
		int counter = 0;
		if (containerMap.isEmpty())
		{
			final ActionErrors errors = this.getActionErrors(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.specimenArrayAliquots.locationsNotSufficient"));
			this.saveErrors(request, errors);
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
			final ActionErrors errors = this.getActionErrors(request);
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"errors.specimenArrayAliquots.locationsNotSufficient"));
			this.saveErrors(request, errors);
			return Constants.PAGE_OF_SPECIMEN_ARRAY_ALIQUOT;
		}
	}

	/**
	 * This function populates the availability map with available storage
	 * locations.
	 *
	 * @param form
	 *            SpecimenArrayAliquotForm
	 * @param containerMap
	 *            Map
	 */
	private void populateAliquotsStorageLocations(SpecimenArrayAliquotForm form, Map containerMap)
	{
		final Map aliquotMap = form.getSpecimenArrayAliquotMap();
		final String noOfAliquots = form.getAliquotCount();
		StorageContainerUtil.populateAliquotMap("SpecimenArray", containerMap, aliquotMap,
				noOfAliquots);
		form.setSpecimenArrayAliquotMap(aliquotMap);

	}

	/**
	 * 	 * This method returns the ActionErrors object present in the request scope.
		 * If it is absent method creates & returns new ActionErrors object.
	 * @param request : request
	 * @return ActionErrors : ActionErrors
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
	 * @param specimenTypeCollection
	 *            Collection
	 * @param form
	 *            SpecimenArrayAliquotForm
	 * @return List : List
	 */
	private List setSpecimenTypes(Collection specimenTypeCollection, SpecimenArrayAliquotForm form)
	{
		final String[] specimenTypeArr = new String[specimenTypeCollection.size()];
		final List specimenTypeList = new ArrayList();
		int i = 0;
		String specimenType = null;
		NameValueBean nameValueBean = null;
		for (final Iterator iter = specimenTypeCollection.iterator(); iter.hasNext(); i++)
		{
			specimenType = (String) iter.next();
			specimenTypeArr[i] = specimenType;
			nameValueBean = new NameValueBean(specimenType, specimenType);
			specimenTypeList.add(nameValueBean);
		}
		form.setSpecimenTypes(specimenTypeArr);
		return specimenTypeList;
	}

}
