
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.bizlogic.QueryShoppingCartBizLogic;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.exception.BizLogicException;

/**
 * @author santhoshkumar_c
 */
public class BulkCartAction extends QueryShoppingCartAction
{

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	protected ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		final AdvanceSearchForm searchForm = (AdvanceSearchForm) form;
		final HttpSession session = request.getSession();
		String target = Constants.SUCCESS;
		final String operation = request.getParameter(Constants.OPERATION);
		String pageOf=request.getParameter("requestFromPage");
		Object specimenIds = getSpecimenIds(searchForm,session,pageOf,operation);
		final QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);

		if (Constants.ADD_TO_ORDER_LIST.equals(operation))
		{
			this.removeSessionAttributes(session);
			if(!"specimenListView".equals(pageOf))
			{					
				this.getOrderableEntityIds(searchForm, session, cart);
			}
			else
			{
				session.setAttribute(Constants.SPECIMEN_ID, specimenIds);
			}
			target = Constants.REQUEST_TO_ORDER;
		}
		else if (Constants.BULK_TRANSFERS.equals(operation)
				|| Constants.BULK_DISPOSALS.equals(operation))
		{
			//target = this.bulkOperations(request, searchForm, operation);
			request.setAttribute(Constants.SPECIMEN_ID, (LinkedList<String>)specimenIds);
			request.setAttribute(Constants.OPERATION, operation);
			target = operation;
		}
		else if (Constants.EDIT_MULTIPLE_SPECIMEN.equals(operation))
		{
			//target = this.editMultipleSpecimen(searchForm, session, operation);
			session.setAttribute(Constants.SPECIMEN_ID,specimenIds);
			target = operation;
		}
		else if (edu.wustl.catissuecore.util.shippingtracking.Constants.CREATE_SHIPMENT
				.equals(operation)
				|| edu.wustl.catissuecore.util.shippingtracking.Constants.CREATE_SHIPMENT_REQUEST
						.equals(operation))
		{

				target = this.createShipment(searchForm, session, operation,pageOf,specimenIds);

		}
		else if (Constants.REQUEST_TO_DISTRIBUTE.equals(operation))
		{
			if("specimenListView".equals(pageOf))
			{
				session.setAttribute(Constants.SPECIMEN_ID, (LinkedList<String>)specimenIds);
			}
			else
			{
				this.getOrderableEntityIds(searchForm, session, cart);
			}
			target = Constants.REQUEST_TO_DISTRIBUTE;
		}
		else if (Constants.PRINT_LABELS.equals(operation))
		{
			session.setAttribute(Constants.SPECIMEN_ID, specimenIds);
			final HashMap forwardToPrintMap = new HashMap();
			forwardToPrintMap.put(Constants.PRINT_SPECIMEN_FROM_LISTVIEW,specimenIds);
			request.setAttribute("forwardToPrintMap", forwardToPrintMap);
			target = operation;
		}
		request.setAttribute(Constants.PAGE_OF, null);
		return mapping.findForward(target);
	}

	/**
	 * @param searchForm
	 *            : searchForm
	 * @param session
	 *            : session
	 * @param cart
	 *            : cart
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private void getOrderableEntityIds(AdvanceSearchForm searchForm, HttpSession session,
			QueryShoppingCart cart) throws BizLogicException
	{
		final List<AttributeInterface> cartAttributeList = cart.getCartAttributeList();

		if (cartAttributeList != null)
		{
			final Map<String, Set<String>> entityIdsMap = this.getOrderableEntityIds(
					cartAttributeList, this.getCheckboxValues(searchForm), cart);
			this.getMapDetails(session, entityIdsMap);
		}
	}

	private String createShipment(LinkedList<String> specimenIds, String operation,HttpSession session)
	{
		final List<String> specimenLabels = new ArrayList<String>();
		specimenLabels.addAll(specimenIds);
		
		session.setAttribute(Constants.SPECIMEN_LABELS_LIST, specimenLabels);
		return operation;
	}
	/**
	 * @param searchForm
	 *            : searchForm
	 * @param session
	 *            : session
	 * @param operation
	 *            : operation
	 * @return String : String
	 */
	private String createShipment(AdvanceSearchForm searchForm, HttpSession session,
			String operation,String pageOf, Object specimenIds)
	{
		String target = "";

		QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);

		this.removeSessionAttributes(session);
	   if(pageOf.equals("specimenListView"))
	{
	
		   session.setAttribute(Constants.SPECIMEN_LABELS_LIST, specimenIds);
	
	}
	   else
	   {
		final List<AttributeInterface> cartAttributeList = cart.getCartAttributeList();

		if (cartAttributeList != null)
		{
			final Map<String, Set<String>> entityIdsMap = this.getShippingEntityNames(
					cartAttributeList, this.getCheckboxValues(searchForm), cart);
			this.getMapDetailsForShipment(session, entityIdsMap);
		}
	   
	   }

		target = operation;

		return target;
	}

	/**
	 * @param session
	 *            : session
	 * @param entityIdsMap
	 *            : entityIdsMap
	 */
	private void getMapDetailsForShipment(HttpSession session, Map<String, Set<String>> entityIdsMap)
	{
		final Set<String> specimenLabelsSet = entityIdsMap.get(Constants.SPECIMEN_NAME);
		
		final Set<String> containerNamesSet = entityIdsMap
				.get(Constants.STORAGE_CONTAINER_CLASS_NAME);

		final List<String> specimenLabels = new ArrayList<String>();
		final List<String> containerNames = new ArrayList<String>();

		specimenLabels.addAll(specimenLabelsSet);
		containerNames.addAll(containerNamesSet);

		session.setAttribute(Constants.SPECIMEN_LABELS_LIST, specimenLabels);
		session.setAttribute(Constants.CONTAINER_NAMES_LIST, containerNames);
	}

	/**
	 * @param cartAttributeList
	 *            : cartAttributeList
	 * @param chkBoxValues
	 *            : chkBoxValues
	 * @param cart
	 *            : cart
	 * @return Map < String , Set < String >> : Map < String , Set < String >>
	 */
	private Map<String, Set<String>> getShippingEntityNames(
			List<AttributeInterface> cartAttributeList, List<Integer> chkBoxValues,
			QueryShoppingCart cart)
	{
		final Map<String, Set<String>> entityIdsMap = this.getShippingEntityMap();
		final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		final List<String> orderableEntityNameList = Arrays.asList(Constants.entityNameArray);
		for (final AttributeInterface attribute : cartAttributeList)
		{

			if ((Constants.SYSTEM_LABEL.equals(attribute.getName()))
					&& ((orderableEntityNameList)).contains(attribute.getEntity().getName()))
			{
				final String entityName = attribute.getEntity().getName();

				final Set<String> tempEntityIdsList = bizLogic.getEntityLabelsList(cart, Arrays
						.asList(entityName), chkBoxValues, Constants.SYSTEM_LABEL);
				final Set<String> idMap = entityIdsMap.get(entityName);
				idMap.addAll(tempEntityIdsList);
			}

			if ((Constants.SYSTEM_NAME.equals(attribute.getName()))
			&& attribute.getEntity().getName().equals(StorageContainer.class.getName()))
			{
				final String entityName = attribute.getEntity().getName();

				final Set<String> tempEntityIdsList = bizLogic.getEntityLabelsList(cart, Arrays
						.asList(entityName), chkBoxValues, Constants.SYSTEM_NAME);
				final Set<String> idMap = entityIdsMap.get(entityName);
				idMap.addAll(tempEntityIdsList);
			}
		}
		return entityIdsMap;
	}

	/**
	 * @return Map < String , Set < String >> : Map < String , Set < String >>
	 */
	private Map<String, Set<String>> getShippingEntityMap()
	{
		final Map<String, Set<String>> entityIdsMap = new LinkedHashMap<String, Set<String>>();
		entityIdsMap.put(Constants.SPECIMEN_NAME, new LinkedHashSet<String>());
		entityIdsMap.put(Constants.STORAGE_CONTAINER_CLASS_NAME, new LinkedHashSet<String>());
		return entityIdsMap;
	}

	/**
	 * @param searchForm
	 *            : searchForm
	 * @param session
	 *            : session
	 * @param operation
	 *            : operation
	 * @return String : String
	 */
	/*private String editMultipleSpecimen(AdvanceSearchForm searchForm, HttpSession session,
			String operation)
	{
		String target;
		session.setAttribute(Constants.SPECIMEN_ID,
		this.getSpecimenIDs( searchForm, session, operation ));
		target = operation;
		return target;
	}*/
	/**
	 * This method is used to create set of specimen IDs.
	* @param searchForm - AdvanceSearchForm
	 * @param session - HttpSession
	 * @param operation - operation
	 * @return set of specimen IDs
	 */
	/*private Set<String> getSpecimenIDs(AdvanceSearchForm searchForm, HttpSession session,String operation)
	{
		if (session.getAttribute(Constants.SPECIMEN_ID) != null)
		{
			session.removeAttribute(Constants.SPECIMEN_ID);
		}
		final QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);

		final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		final Set<String> specimenIds = new LinkedHashSet<String>(bizLogic.getEntityIdsList(cart,
				Arrays.asList(Constants.specimenNameArray),
				this.getCheckboxValues(searchForm)));
		return specimenIds;

	}*/
	/**
	 *  This method creates map which contains specimen IDs to print.
	 * @param searchForm - AdvanceSearchForm
	 * @param session - HttpSession
	 * @param operation - operation
	 * @param request - HttpServletRequest
	 * @return target
	 */
	/*private String printSpecimensFromListView(AdvanceSearchForm searchForm, HttpSession session,
			String operation,HttpServletRequest request)
	{
		String target;
		session.setAttribute(Constants.SPECIMEN_ID, this.getSpecimenIDs( searchForm, session, operation ));
		final HashMap forwardToPrintMap = new HashMap();
		forwardToPrintMap.put(Constants.PRINT_SPECIMEN_FROM_LISTVIEW,
		this.getSpecimenIDs( searchForm, session, operation ));
		request.setAttribute("forwardToPrintMap", forwardToPrintMap);
		target = operation;
		return target;
	}*/
	/**
	 * @param request
	 *            : request
	 * @param searchForm
	 *            : searchForm
	 * @param operation
	 *            : operation
	 * @return String : String
	 */
	private String bulkOperations(HttpServletRequest request, AdvanceSearchForm searchForm,
			String operation)
	{
		String target;
		final HttpSession session = request.getSession();
		final QueryShoppingCart cart = (QueryShoppingCart) session
				.getAttribute(Constants.QUERY_SHOPPING_CART);
		final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		final LinkedList<String> specimenIds = new LinkedList<String>(bizLogic.getEntityIdsList(cart,
				Arrays.asList(Constants.specimenNameArray), this.getCheckboxValues(searchForm)));
		request.setAttribute(Constants.SPECIMEN_ID, specimenIds);
		request.setAttribute(Constants.OPERATION, operation);
		target = operation;
		return target;
	}

	/**
	 * @param session
	 *            : session
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
	 *            : session
	 * @param entityIdsMap
	 *            : entityIdsMap
	 */
	private void getMapDetails(HttpSession session, Map<String, Set<String>> entityIdsMap)
	{
		final Set<String> specimenIdsSet = entityIdsMap.get(Constants.SPECIMEN_NAME);
		final Set<String> specimenArrayIdsSet = entityIdsMap
				.get(Constants.SPECIMEN_ARRAY_CLASS_NAME);
		final Set<String> pathalogicalCaseIdsSet = entityIdsMap
				.get(Constants.IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME);
		final Set<String> deidentifiedPathalogicalCaseIdsSet = entityIdsMap
				.get(Constants.DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME);
		final Set<String> surgicalPathalogicalCaseIdsSet = entityIdsMap
				.get(Constants.SURGICAL_PATHALOGY_REPORT_CLASS_NAME);

		final List<String> specimenArrayIds = new ArrayList<String>();
		final List<String> specimenIds = new ArrayList<String>();
		final List<String> pathalogicalCaseIds = new ArrayList<String>();
		final List<String> deidentifiedPathalogicalCaseIds = new ArrayList<String>();
		final List<String> surgicalPathalogicalCaseIds = new ArrayList<String>();

		specimenIds.addAll(specimenIdsSet);
		specimenArrayIds.addAll(specimenArrayIdsSet);
		pathalogicalCaseIds.addAll(pathalogicalCaseIdsSet);
		deidentifiedPathalogicalCaseIds.addAll(deidentifiedPathalogicalCaseIdsSet);
		surgicalPathalogicalCaseIds.addAll(surgicalPathalogicalCaseIdsSet);

		session.setAttribute(Constants.SPECIMEN_ID, specimenIds);
		session.setAttribute(Constants.SPECIMEN_ARRAY_ID, specimenArrayIds);
		session.setAttribute(Constants.PATHALOGICAL_CASE_ID, pathalogicalCaseIds);
		session.setAttribute(Constants.DEIDENTIFIED_PATHALOGICAL_CASE_ID,
				deidentifiedPathalogicalCaseIds);
		session.setAttribute(Constants.SURGICAL_PATHALOGY_CASE_ID, surgicalPathalogicalCaseIds);
	}

	/**
	 * @return Map < String , Set < String >> : Map < String , Set < String >>
	 */
	private Map<String, Set<String>> getEntityMap()
	{
		final Map<String, Set<String>> entityIdsMap = new LinkedHashMap<String, Set<String>>();
		entityIdsMap.put(Constants.SPECIMEN_ARRAY_CLASS_NAME, new LinkedHashSet<String>());
		entityIdsMap.put(Constants.IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
				new LinkedHashSet<String>());
		entityIdsMap.put(Constants.DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
				new LinkedHashSet<String>());
		entityIdsMap.put(Constants.SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
				new LinkedHashSet<String>());
		entityIdsMap.put(Constants.SPECIMEN_NAME, new LinkedHashSet<String>());
		return entityIdsMap;
	}

	/**
	 * @param cartAttributeList
	 *            : cartAttributeList
	 * @param chkBoxValues
	 *            : chkBoxValues
	 * @param cart
	 *            : cart
	 * @return Map < String , Set < String >> : Map < String , Set < String >>
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private Map<String, Set<String>> getOrderableEntityIds(
			List<AttributeInterface> cartAttributeList, List<Integer> chkBoxValues,
			QueryShoppingCart cart) throws BizLogicException
	{
		final Map<String, Set<String>> entityIdsMap = this.getEntityMap();
		final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
		final List<String> orderableEntityNameList = Arrays.asList(Constants.entityNameArray);
		for (final AttributeInterface attribute : cartAttributeList)
		{

			if ((Constants.ID.equals(attribute.getName()))
					&& ((orderableEntityNameList)).contains(attribute.getEntity().getName()))
			{
				String entityName = attribute.getEntity().getName();

				Set<String> tempEntityIdsList = bizLogic.getEntityIdsList(cart, Arrays
						.asList(entityName), chkBoxValues);

				if (Constants.TISSUE_SPECIMEN.equals(entityName)
						|| Constants.MOLECULAR_SPECIMEN.equals(entityName)
						|| Constants.FLUID_SPECIMEN.equals(entityName)
						|| Constants.CELL_SPECIMEN.equals(entityName))
				{
					entityName = Constants.SPECIMEN_CLASSNAME;
				}
				if (Constants.SPECIMEN_CLASSNAME.equals(entityName))
				{
					tempEntityIdsList = bizLogic.getListOfOrderItem(tempEntityIdsList);
				}
				final Set<String> idMap = entityIdsMap.get(entityName);
				idMap.addAll(tempEntityIdsList);
			}
		}
		return entityIdsMap;
	}
	private Object  getSpecimenIds(AdvanceSearchForm searchForm, HttpSession session,String pageOf,String operation) 
	{
		Set<String> specimenIdSet = new LinkedHashSet<String>();
		if("specimenListView".equals(pageOf))
		{	
		  if(Constants.EDIT_MULTIPLE_SPECIMEN.equals(operation))
		  {	  
			  String specIds = searchForm.getOrderedString();
				StringTokenizer tokenizer = new StringTokenizer(specIds,",");
				
				while(tokenizer.hasMoreTokens())
				{
					specimenIdSet.add(tokenizer.nextToken());
				}
		  }	
		  else
		  {
			  	String specIds = searchForm.getOrderedString();
				StringTokenizer tokenizer = new StringTokenizer(specIds,",");
				List<String> specimenIdList = new LinkedList<String>();
				while(tokenizer.hasMoreTokens())
				{
					specimenIdList.add(tokenizer.nextToken());
				}
//				HttpSession session = request.getSession();
//				session.setAttribute(Constants.SPECIMEN_ID, specimenIdList);
				return specimenIdList;
		  }
		}
		else
		{
			//final HttpSession session = request.getSession();
			if (session.getAttribute(Constants.SPECIMEN_ID) != null) {
				session.removeAttribute(Constants.SPECIMEN_ID);
			}
			final QueryShoppingCart cart = (QueryShoppingCart) session
					.getAttribute(Constants.QUERY_SHOPPING_CART);
			final QueryShoppingCartBizLogic bizLogic = new QueryShoppingCartBizLogic();
			if (Constants.EDIT_MULTIPLE_SPECIMEN.equals(operation)) {
				
				specimenIdSet = new LinkedHashSet<String>(bizLogic.getEntityIdsList(cart,
								Arrays.asList(Constants.specimenNameArray),
								this.getCheckboxValues(searchForm)));

			} else {

				final LinkedList<String> specimenIds = new LinkedList<String>(bizLogic.getEntityIdsList(cart,
						Arrays.asList(Constants.specimenNameArray), this.getCheckboxValues(searchForm)));
				return specimenIds;
				
			}
									
		}
//		HttpSession session = request.getSession();
//		session.setAttribute(Constants.SPECIMEN_ID, specimenIdList);
		return specimenIdSet;
	}
}