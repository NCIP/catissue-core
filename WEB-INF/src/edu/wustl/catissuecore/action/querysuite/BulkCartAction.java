package edu.wustl.catissuecore.action.querysuite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.querysuite.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * @author santhoshkumar_c
 *
 */
public class BulkCartAction extends QueryShoppingCartAction
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
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		HttpSession session = request.getSession();
		String target = null;;
		String operation = request.getParameter(Constants.OPERATION);						
		
		QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);
											
	    if (Constants.ADD_TO_ORDER_LIST.equals(operation))
		{			
		 	removeSessionAttributes(session);
			List<AttributeInterface> cartAttributeList = cart.getCartAttributeList();
			 
			if (cartAttributeList != null)
			{
				Map <String,Set<String>> entityIdsMap = getOrderableEntityIds(cartAttributeList,
						getCheckboxValues(searchForm), cart);
				getMapDetails(session, entityIdsMap);
			}
			
			target = new String(Constants.REQUEST_TO_ORDER);
		}
		else
			if (Constants.BULK_TRANSFERS.equals(operation) || Constants.BULK_DISPOSALS.equals(operation))
		{
			target = bulkOperations(request, searchForm, operation);
		}
		else if(Constants.EDIT_MULTIPLE_SPECIMEN.equals(operation))
		{
			target = editMultipleSpecimen(searchForm, session, operation);
		}		
		return mapping.findForward(target);
	}

	/**
	 * @param searchForm
	 * @param session
	 * @param operation
	 * @return
	 */
	private String editMultipleSpecimen(AdvanceSearchForm searchForm, HttpSession session, String operation) 
	{
		String target;
		if(session.getAttribute(Constants.SPECIMEN_ID) != null)
			session.removeAttribute(Constants.SPECIMEN_ID);		
		QueryShoppingCart cart = (QueryShoppingCart) session
		.getAttribute(Constants.QUERY_SHOPPING_CART);
		
		QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		Set<String> specimenIds = new HashSet<String>(bizLogic.getEntityIdsList(cart, 
				Arrays.asList(Constants.specimenNameArray), getCheckboxValues(searchForm)));
		session.setAttribute(Constants.SPECIMEN_ID, specimenIds);
		
		target = new String(operation);
		return target;
	}

	/**
	 * @param request
	 * @param searchForm
	 * @param operation
	 * @return
	 */
	private String bulkOperations(HttpServletRequest request, AdvanceSearchForm searchForm, String operation) 
	{
		String target;
		HttpSession session = request.getSession();
		QueryShoppingCart cart = (QueryShoppingCart) session
		.getAttribute(Constants.QUERY_SHOPPING_CART);
		QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		List<String> specimenIds = new ArrayList<String>(bizLogic.getEntityIdsList(cart,
				Arrays.asList(Constants.specimenNameArray), getCheckboxValues(searchForm)));
		request.setAttribute(Constants.SPECIMEN_ID, specimenIds);
		request.setAttribute(Constants.OPERATION, operation);
		target = new String(operation);
		return target;
	}

	/**
	 * @param session
	 */
	private void removeSessionAttributes(HttpSession session) 
	{	
		session.removeAttribute(Constants.REQUESTED_FOR_BIOSPECIMENS);			
		session.removeAttribute(Constants.DEFINE_ARRAY_FORM_OBJECTS);				
		session.removeAttribute(Constants.SPECIMEN_ARRAY_ID);		
		session.removeAttribute(Constants.PATHALOGICAL_CASE_ID);				
		session.removeAttribute(Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID);		
		session.removeAttribute(Constants.SURGICAL_PATHALOGY_CASE_ID);
	}	

	/**
	 * @param session
	 * @param entityIdsMap
	 */
	private void getMapDetails(HttpSession session, Map<String, Set<String>> entityIdsMap) 
	{	
		Set<String> specimenIdsSet = entityIdsMap.get(Constants.SPECIMEN_NAME);
		Set<String> specimenArrayIdsSet = entityIdsMap.get(Constants.SPECIMEN_ARRAY_CLASS_NAME);
		Set<String> pathalogicalCaseIdsSet = entityIdsMap
			.get(Constants.IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME);
		Set<String> deidentifiedPathalogicalCaseIdsSet = entityIdsMap
			.get(Constants.DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME);
		Set<String> surgicalPathalogicalCaseIdsSet = entityIdsMap
			.get(Constants.SURGICAL_PATHALOGY_REPORT_CLASS_NAME);
		
		List<String> specimenArrayIds = new ArrayList<String>();
		List<String> specimenIds = new ArrayList<String>();
		List<String> pathalogicalCaseIds = new ArrayList<String>();
		List<String> deidentifiedPathalogicalCaseIds = new ArrayList<String>();
		List<String> surgicalPathalogicalCaseIds = new ArrayList<String>();
		
		specimenIds.addAll(specimenIdsSet);
		specimenArrayIds.addAll(specimenArrayIdsSet);
		pathalogicalCaseIds.addAll(pathalogicalCaseIdsSet);
		deidentifiedPathalogicalCaseIds.addAll(deidentifiedPathalogicalCaseIdsSet);
		surgicalPathalogicalCaseIds.addAll(surgicalPathalogicalCaseIdsSet);
		
		session.setAttribute(Constants.SPECIMEN_ID, specimenIds);
		session.setAttribute(Constants.SPECIMEN_ARRAY_ID, specimenArrayIds);
		session.setAttribute(Constants.PATHALOGICAL_CASE_ID, pathalogicalCaseIds);
		session.setAttribute(Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID, deidentifiedPathalogicalCaseIds);
		session.setAttribute(Constants.SURGICAL_PATHALOGY_CASE_ID, surgicalPathalogicalCaseIds);
	}
		
	/**
	 * @return
	 */
	private Map<String, Set<String>> getEntityMap()
	{
		Map <String,Set<String>> entityIdsMap = new HashMap<String, Set<String>>();
		entityIdsMap.put(Constants.SPECIMEN_ARRAY_CLASS_NAME, new HashSet<String>());
		entityIdsMap.put(Constants.IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME, new HashSet<String>());
		entityIdsMap.put(Constants.DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME, new HashSet<String>());
		entityIdsMap.put(Constants.SURGICAL_PATHALOGY_REPORT_CLASS_NAME, new HashSet<String>());
		entityIdsMap.put(Constants.SPECIMEN_NAME, new HashSet<String>());
		return entityIdsMap;
	}
	
	/**
	 * @param cartAttributeList
	 * @param chkBoxValues
	 * @param cart
	 * @return
	 */
	private Map<String, Set<String>> getOrderableEntityIds(List<AttributeInterface> cartAttributeList, 
			List<Integer> chkBoxValues, QueryShoppingCart cart) 
	{
		Map <String,Set<String>> entityIdsMap = getEntityMap();
		QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic(); 
		List<String> orderableEntityNameList = Arrays.asList(Constants.entityNameArray);
		for (AttributeInterface attribute : cartAttributeList)
		{
			
			if ((Constants.ID.equals(attribute.getName()))
					&& ((orderableEntityNameList))
							.contains(attribute.getEntity().getName()))
			{
				String entityName = attribute.getEntity().getName();
				
				Set<String> tempEntityIdsList = bizLogic.getEntityIdsList(cart,
						Arrays.asList(entityName), chkBoxValues);
				tempEntityIdsList = bizLogic.getListOfOrderItem(tempEntityIdsList);
				Set<String> idMap = entityIdsMap.get(entityName);
				idMap.addAll(tempEntityIdsList);
			}
		}
		return entityIdsMap;
	}

}
