/**
 * 
 */
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.query.actionForm.QueryAdvanceSearchForm;
import edu.wustl.query.querysuite.QueryShoppingCart;

/**
 * @author supriya_dankh Handles all the actions related to shopping cart.
 */
abstract public class QueryShoppingCartAction extends BaseAction
{
	
	/** sets the options enabled or disabled depending on the entities and their attributes present 
	 * in the cart.
	 * @param request
	 * @param cart
	 */
	protected  void setCartView(HttpServletRequest request, QueryShoppingCart cart)
	{ 
		String isSpecimenIdPresent = Constants.FALSE;
		String isContainerPresent = Constants.FALSE;
		if (cart != null)
		{
			List<AttributeInterface> cartAttributeList = cart.getCartAttributeList();
			if (cartAttributeList != null)
			{
				List<String> orderableEntityNameList = Arrays.asList(Constants.entityNameArray);
				for (AttributeInterface attribute :cartAttributeList)
				{
					if ((attribute.getName().equals(Constants.ID))
							&& ((orderableEntityNameList)).contains(attribute
									.getEntity().getName()))
					{
						isSpecimenIdPresent = Constants.TRUE;
						
						if(!Arrays.asList(Constants.specimenNameArray).contains(attribute
								.getEntity().getName()))
						{
							request.setAttribute(Constants.IS_SPECIMENARRAY_PRESENT,
									Constants.TRUE);
						}
					}
					if ((attribute.getName().equals(Constants.ID))
							&& attribute.getEntity().getName().equals(StorageContainer.class.getName()))
					{
						isContainerPresent = Constants.TRUE;
							request.setAttribute(Constants.IS_CONTAINER_PRESENT,
									Constants.TRUE);
					}
				}
				request.setAttribute(Constants.IS_SPECIMENID_PRESENT, isSpecimenIdPresent);
				return;
			}
		}
		//List Empty Message added in QueryShoppingCart.jsp page
//		ActionErrors errors = new ActionErrors();
//		ActionError error = new ActionError("ShoppingCart.emptyCartTitle");
//		errors.add(ActionErrors.GLOBAL_ERROR, error);
//		saveErrors(request, errors);
	}
	
	/**
	 * Separates a index of checkbox present in object obj.
	 * 
     * @param obj.
     * @return index.
	 */
	protected Integer getIndex(Object obj)
	{
		String str = obj.toString();
    	StringTokenizer strTokens = new StringTokenizer(str,Constants.UNDERSCORE);
    	strTokens.nextToken();
    	int index = Integer.parseInt(strTokens.nextToken());
		return new Integer(index);
	}

	protected  List<Integer> getCheckboxValues(QueryAdvanceSearchForm searchForm) {
		Map map = searchForm.getValues();
		Set chkBoxValuesSet = map.keySet();
		List<Integer> chkBoxValues = new ArrayList<Integer>();
		for(Object checkedValue: chkBoxValuesSet)
		{
			chkBoxValues.add(getIndex(checkedValue));
		}
		return chkBoxValues;
	}

}