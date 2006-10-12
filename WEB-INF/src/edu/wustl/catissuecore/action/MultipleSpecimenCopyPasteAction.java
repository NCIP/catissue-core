
package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import edu.wustl.catissuecore.applet.AppletConstants;
import edu.wustl.catissuecore.applet.CopyPasteOperationValidatorModel;
import edu.wustl.catissuecore.applet.model.MultipleSpecimenTableModel;
import edu.wustl.catissuecore.util.global.Constants;

public class MultipleSpecimenCopyPasteAction extends DispatchAction
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

		// TODO
		CopyPasteOperationValidatorModel validatorModel = new CopyPasteOperationValidatorModel();
		MultipleSpecimenTableModel multipleSpecimenTableModel = new MultipleSpecimenTableModel(0, new HashMap());

		List selectedCopiedRows = validatorModel.getSelectedCopiedRows();
		List selectedCopiedCols = validatorModel.getSelectedCopiedCols();
		for (int j = 0; j < selectedCopiedCols.size(); j++)
		{
			for (int i = 0; i < selectedCopiedRows.size(); i++)
			{
				int copiedRow = ((Integer) selectedCopiedRows.get(i)).intValue();
				int copiedColumn = ((Integer) selectedCopiedCols.get(i)).intValue();
				if (copiedRow >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
				{
					String key = copiedRow + "@" + copiedColumn;
					Object value = null;
					if (copiedRow == AppletConstants.SPECIMEN_COMMENTS_ROW_NO || copiedRow == AppletConstants.SPECIMEN_BIOHAZARDS_ROW_NO
							|| copiedRow == AppletConstants.SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO)
					{
						value = specimenMap.get(multipleSpecimenTableModel.getKey(copiedRow, copiedColumn));
						buttonsMap.put(key, value);
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

		CopyPasteOperationValidatorModel validatorModel = new CopyPasteOperationValidatorModel();
		MultipleSpecimenTableModel multipleSpecimenTableModel = new MultipleSpecimenTableModel(0, new HashMap());

		List selectedCopiedRows = validatorModel.getSelectedCopiedRows();
		List selectedCopiedCols = validatorModel.getSelectedCopiedCols();
		List selectedPastedRows = validatorModel.getSelectedPastedRows();
		List selectedPastedCols = validatorModel.getSelectedPastedCols();

		

		for (int j = 0; j < selectedCopiedCols.size(); j++)
		{
			for (int i = 0; i < selectedCopiedRows.size(); i++)
			{
				int copiedRow = ((Integer) selectedCopiedRows.get(i)).intValue();
				int copiedColumn = ((Integer) selectedCopiedCols.get(i)).intValue();
				int pastedRow = ((Integer) selectedPastedRows.get(i)).intValue();
				int pastedColumn = ((Integer) selectedPastedCols.get(i)).intValue();
				boolean singleColumn = false;
				if (selectedPastedCols.size() == 1)
				{
					singleColumn = true;
				}
				if (copiedRow >= AppletConstants.SPECIMEN_COMMENTS_ROW_NO)
				{
					/**
					 * If columns selected to paste is 1 and multiple columns are copied,
					 * increament the column count as per copied columns
					 */
					
					int columnNumber = pastedColumn;
					if (singleColumn)
					{
						columnNumber += j;
					}
					String key = copiedRow + "@" + copiedColumn;
					Object value = null;
					if (copiedRow == AppletConstants.SPECIMEN_COMMENTS_ROW_NO || copiedRow == AppletConstants.SPECIMEN_BIOHAZARDS_ROW_NO
							|| copiedRow == AppletConstants.SPECIMEN_EXTERNAL_IDENTIFIERS_ROW_NO)
					{
						value = buttonsMap.get(key);
						specimenMap.put(multipleSpecimenTableModel.getKey(copiedRow, columnNumber), value);
					}
					else if (copiedRow == AppletConstants.SPECIMEN_EVENTS_ROW_NO)
					{
						value = buttonsMap.get(key);
						eventsMap.put(multipleSpecimenTableModel.getKey(copiedRow, columnNumber), value);
					}
					else if (copiedRow == AppletConstants.SPECIMEN_DERIVE_ROW_NO)
					{
						value = buttonsMap.get(key);
						formBeanMap.put(multipleSpecimenTableModel.getKey(copiedRow, columnNumber), value);
					}
				}

			}
		}

		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_FORM_BEAN_MAP_KEY, formBeanMap);
		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_MAP_KEY, specimenMap);
		request.getSession().setAttribute(Constants.MULTIPLE_SPECIMEN_EVENT_MAP_KEY, eventsMap);
		return null;
	}

}