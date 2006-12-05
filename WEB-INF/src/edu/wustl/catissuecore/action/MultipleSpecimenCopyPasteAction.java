
package edu.wustl.catissuecore.action;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CreateSpecimenForm;
import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.CopyPasteOperationValidatorModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.XMLPropertyHandler;

public class MultipleSpecimenCopyPasteAction extends BaseAppletAction
{

	/**
	 * This method is used to copy data from Selected components(comments,events,biohazards,external identifiers,derive)
	 * It updates the the Buttons map maps 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception - Exception
	 */
	public ActionForward copy(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		Map formBeanMap = (HashMap) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY);
		Map specimenMap = (HashMap) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY);
		Map eventsMap = (HashMap) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_EVENT_MAP_KEY);
		Map buttonsMap = new HashMap();

		Map appletDataMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);
		CopyPasteOperationValidatorModel validatorModel = (CopyPasteOperationValidatorModel) appletDataMap.get(AppletConstants.VALIDATOR_MODEL);
		Map dataListsMap = new HashMap();
		String columns = XMLPropertyHandler.getValue(Constants.MULTIPLE_SPECIMEN_COLUMNS_PER_PAGE);
		dataListsMap.put(Constants.MULTIPLE_SPECIMEN_COLUMNS_PER_PAGE ,columns);
		MultipleSpecimenTableModel multipleSpecimenTableModel = new MultipleSpecimenTableModel(0, dataListsMap);

		List selectedCopiedRows = validatorModel.getSelectedCopiedRows();
		List selectedCopiedCols = validatorModel.getSelectedCopiedCols();
		for (int j = 0; j < selectedCopiedCols.size(); j++)
		{
			int copiedColumn = ((Integer) selectedCopiedCols.get(j)).intValue();
			for (int i = 0; i < selectedCopiedRows.size(); i++)
			{
				int copiedRow = ((Integer) selectedCopiedRows.get(i)).intValue();

				if (copiedRow >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
				{
					String key = copiedRow + AppletConstants.MULTIPLE_SPECIMEN_ROW_COLUMN_SEPARATOR + copiedColumn;
					Object value = null;
					if (copiedRow == AppletConstants.SPECIMEN_COMMENTS_ROW_NO || copiedRow == AppletConstants.SPECIMEN_BIOHAZARDS_ROW_NO
							|| copiedRow == AppletConstants.SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO)
					{
						value = specimenMap.get(multipleSpecimenTableModel.getKey(copiedRow, copiedColumn));
						buttonsMap.put(key, value);
						String keyForCount = multipleSpecimenTableModel.getKey(copiedRow, copiedColumn) + Constants.APPEND_COUNT;
						buttonsMap.put(keyForCount, specimenMap.get(keyForCount));
					}
					else if (copiedRow == AppletConstants.SPECIMEN_EVENTS_ROW_NO)
					{
						value = eventsMap.get(multipleSpecimenTableModel.getKey(copiedRow, copiedColumn));
						buttonsMap.put(key, value);
					}
					else if (copiedRow == AppletConstants.SPECIMEN_DERIVE_ROW_NO)
					{
						value = formBeanMap.get(multipleSpecimenTableModel.getKey(copiedRow, copiedColumn));
						buttonsMap.put(key, value);
					}
				}

			}
		}
		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_BUTTONS_MAP_KEY, buttonsMap);
		writeMapToResponse(response, null);
		return null;
	}

	/**
	 * This method is used to paste data to Selected components(comments,events,biohazards,external identifiers,derive)
	 * It updates the three maps 
	 * 1. Multiple Specimen map
	 * 2. Multiple Specimen form bean map
	 * 3. Multiple Specimen events map  
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception - Exception
	 */
	public ActionForward paste(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Map formBeanMap = (HashMap) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY);
		Map specimenMap = (HashMap) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY);
		Map eventsMap = (HashMap) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_EVENT_MAP_KEY);
		Map buttonsMap = (HashMap) request.getSession().getAttribute(Constants.MULTIPLE_SPECIMEN_BUTTONS_MAP_KEY);

		Map appletDataMap = (Map) request.getAttribute(Constants.INPUT_APPLET_DATA);
		CopyPasteOperationValidatorModel validatorModel = (CopyPasteOperationValidatorModel) appletDataMap.get(AppletConstants.VALIDATOR_MODEL);
		Map dataListsMap = new HashMap();
		String columns = XMLPropertyHandler.getValue(Constants.MULTIPLE_SPECIMEN_COLUMNS_PER_PAGE);
		dataListsMap.put(Constants.MULTIPLE_SPECIMEN_COLUMNS_PER_PAGE ,columns);
		MultipleSpecimenTableModel multipleSpecimenTableModel = new MultipleSpecimenTableModel(0, dataListsMap);

		List selectedCopiedRows = validatorModel.getSelectedCopiedRows();
		List selectedCopiedCols = validatorModel.getSelectedCopiedCols();
		List selectedPastedRows = validatorModel.getSelectedPastedRows();
		List selectedPastedCols = validatorModel.getSelectedPastedCols();

		/**
		 *  Calculate all rows to be pasted in case user has selected a single row to be pasted
		 */
		if (selectedPastedRows.size() == 1)
		{
			int rowValue = ((Integer) selectedPastedRows.get(0)).intValue();
			for (int i = 1; i < selectedCopiedRows.size(); i++)
			{
				selectedPastedRows.add(new Integer(rowValue + i));
			}
		}
		/**
		 *  Calculate all columns to be pasted in case user has selected a single column to be pasted
		 */
		if (selectedPastedCols.size() == 1)
		{
			int rowValue = ((Integer) selectedPastedCols.get(0)).intValue();
			for (int i = 1; i < selectedCopiedCols.size(); i++)
			{
				selectedPastedCols.add(new Integer(rowValue + i));
			}
		}

		for (int j = 0; j < selectedCopiedCols.size(); j++)
		{
			int copiedColumn = ((Integer) selectedCopiedCols.get(j)).intValue();
			int pastedColumn = ((Integer) selectedPastedCols.get(j)).intValue();
			System.out.println("\n\npastedColumn : "+ pastedColumn);
			for (int i = 0; i < selectedCopiedRows.size(); i++)
			{
				int copiedRow = ((Integer) selectedCopiedRows.get(i)).intValue();
				int pastedRow = ((Integer) selectedPastedRows.get(i)).intValue();

				if (copiedRow >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
				{
					/**
					 * If columns selected to paste is 1 and multiple columns are copied,
					 * increament the column count as per copied columns
					 */

					String key = copiedRow + AppletConstants.MULTIPLE_SPECIMEN_ROW_COLUMN_SEPARATOR + copiedColumn;
					Object value = null;
					if (copiedRow == AppletConstants.SPECIMEN_COMMENTS_ROW_NO || copiedRow == AppletConstants.SPECIMEN_BIOHAZARDS_ROW_NO
							|| copiedRow == AppletConstants.SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO)
					{
						value = buttonsMap.get(key);
						specimenMap.put(multipleSpecimenTableModel.getKey(pastedRow, pastedColumn), value);
						String keyForCount = multipleSpecimenTableModel.getKey(copiedRow, copiedColumn) + Constants.APPEND_COUNT;
						specimenMap.put(multipleSpecimenTableModel.getKey(pastedRow, pastedColumn) + Constants.APPEND_COUNT, buttonsMap.get(keyForCount));
					}
					else if (copiedRow == AppletConstants.SPECIMEN_EVENTS_ROW_NO)
					{
						value = buttonsMap.get(key);
						eventsMap.put(multipleSpecimenTableModel.getKey(pastedRow, pastedColumn), value);
					}
					else if (copiedRow == AppletConstants.SPECIMEN_DERIVE_ROW_NO)
					{
						value = buttonsMap.get(key);
						ArrayList listOfFormBean = (ArrayList) value;
						ArrayList newList = new ArrayList();
						if(listOfFormBean!=null)
						{
						for (int k = 0; k < listOfFormBean.size(); k++)
						{
							CreateSpecimenForm createForm = (CreateSpecimenForm) ((CreateSpecimenForm) listOfFormBean.get(k)).clone();
							newList.add(createForm);
						}
						formBeanMap.put(multipleSpecimenTableModel.getKey(pastedRow, pastedColumn), newList);
						}
					}
				}

			}
		}

		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY, formBeanMap);
		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY, specimenMap);
		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_EVENT_MAP_KEY, eventsMap);
		writeMapToResponse(response, null);
		return null;
	}

	//================================
//	 --------- Changes By  Mandar : 05Dec06 for Bug 2866. ---  Extending SecureAction.  start
	   protected ActionForward invokeMethod(String methodName, ActionMapping mapping, ActionForm form,
           HttpServletRequest request, HttpServletResponse response) throws Exception 
	   {
		   if(methodName.trim().length() > 0  )
		   {
			   Method method = getMethod(methodName,this.getClass());
			   if(method != null)
			   {
				   Object args[] = {mapping, form, request, response};
				   return (ActionForward) method.invoke(this, args);
			   }
			   else
			   	   return null;
		   }
		   return null;
	   }
//		 --------- Changes By  Mandar : 05Dec06 for Bug 2866. ---  Extending SecureAction.  end

}