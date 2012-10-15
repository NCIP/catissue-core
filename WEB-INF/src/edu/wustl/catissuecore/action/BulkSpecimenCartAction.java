package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

public class BulkSpecimenCartAction extends BaseAction
{

	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		final HttpSession session = request.getSession();
		String target = Constants.SUCCESS;
		final String operation = request.getParameter(Constants.OPERATION);
		List specimenIds = getSpecimenIds(searchForm,request);

		if (Constants.ADD_TO_ORDER_LIST.equals(operation))
		{
			target = Constants.REQUEST_TO_ORDER;
		}
		else if (Constants.BULK_TRANSFERS.equals(operation)
				|| Constants.BULK_DISPOSALS.equals(operation))
		{
			request.setAttribute(Constants.SPECIMEN_ID, specimenIds);
			request.setAttribute(Constants.OPERATION, operation);
			target = operation;
		}
		else if (Constants.EDIT_MULTIPLE_SPECIMEN.equals(operation))
		{
			session.setAttribute(Constants.SPECIMEN_ID,
					specimenIds);
					target = operation;
		}
		else if (edu.wustl.catissuecore.util.shippingtracking.Constants.CREATE_SHIPMENT
				.equals(operation)
				|| edu.wustl.catissuecore.util.shippingtracking.Constants.CREATE_SHIPMENT_REQUEST
						.equals(operation))
		{
//			target = this.createShipment(searchForm, session, operation);
		}
		else if (Constants.REQUEST_TO_DISTRIBUTE.equals(operation))
		{
			session.setAttribute(Constants.SPECIMEN_ID, specimenIds);
			target = Constants.REQUEST_TO_DISTRIBUTE;
		}
		else if (Constants.PRINT_LABELS.equals(operation))
		{
			session.setAttribute(Constants.SPECIMEN_ID,specimenIds);
			final HashMap forwardToPrintMap = new HashMap();
			forwardToPrintMap.put(Constants.PRINT_SPECIMEN_FROM_LISTVIEW,specimenIds);
			request.setAttribute("forwardToPrintMap", forwardToPrintMap);
			target = operation;
		}

		return mapping.findForward(target);

	}

	private List getSpecimenIds(AdvanceSearchForm form, HttpServletRequest request) 
	{
		String specIds = form.getOrderedString();
		StringTokenizer tokenizer = new StringTokenizer(specIds,",");
		List<String> specimenIdList = new LinkedList<String>();
		while(tokenizer.hasMoreTokens())
		{
			specimenIdList.add(tokenizer.nextToken());
		}
//		HttpSession session = request.getSession();
//		session.setAttribute(Constants.SPECIMEN_ID, specimenIdList);
		return specimenIdList;
	}

}
