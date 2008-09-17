package edu.wustl.catissuecore.action.querysuite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author santhoshkumar_c
 *
 */
public class ViewCartAction extends QueryShoppingCartAction 
{
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping,
	 *      org.apache.struts.action.ActionForm,
	 *      javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception
	{
		//AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		HttpSession session = request.getSession();
		String target=null;

		QueryShoppingCart cart = (QueryShoppingCart) session
			.getAttribute(Constants.QUERY_SHOPPING_CART);			
	
		// Check if user wants to view the cart.		  
		request.setAttribute(Constants.EVENT_PARAMETERS_LIST,Constants.EVENT_PARAMETERS);
		setCartView(request, cart);
		target = new String(Constants.VIEW);
		session.removeAttribute(Constants.HYPERLINK_COLUMN_MAP);
		return mapping.findForward(target);
	}
}
