/**
 *
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.wustl.catissuecore.actionForm.AdvanceSearchForm;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.querysuite.QueryShoppingCart;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;

/**
 * @author supriya_dankh Handles all the actions related to shopping cart.
 */
abstract public class QueryShoppingCartAction extends BaseAction
{

	/**
	 * sets the options enabled or disabled depending on the entities and their
	 * attributes present in the cart.
	 *
	 * @param request : request
	 * @param cart : cart
	 */
	protected void setCartView(HttpServletRequest request, QueryShoppingCart cart)
	{
		String isSpecimenIdPresent = Constants.FALSE;
		if (cart != null)
		{
			final List<AttributeInterface> cartAttributeList = cart.getCartAttributeList();
			if (cartAttributeList != null)
			{
				final List<String> orderableEntityNameList = Arrays
						.asList(Constants.entityNameArray);
				for (final AttributeInterface attribute : cartAttributeList)
				{
					if ((attribute.getName().equals(Constants.ID))
							&& ((orderableEntityNameList))
									.contains(attribute.getEntity().getName()))
					{
						isSpecimenIdPresent = Constants.TRUE;

						if (!Arrays.asList(Constants.specimenNameArray).contains(
								attribute.getEntity().getName()))
						{
							request
									.setAttribute(Constants.IS_SPECIMENARRAY_PRESENT,
											Constants.TRUE);
						}
					}
					if ((attribute.getName().equals(Constants.ID))
							&& attribute.getEntity().getName().equals(
									StorageContainer.class.getName()))
					{
						request.setAttribute(Constants.IS_CONTAINER_PRESENT, Constants.TRUE);
					}
				}
				request.setAttribute(Constants.IS_SPECIMENID_PRESENT, isSpecimenIdPresent);
				return;
			}
		}
		// List Empty Message added in QueryShoppingCart.jsp page
		// ActionErrors errors = new ActionErrors();
		// ActionError error = new ActionError("ShoppingCart.emptyCartTitle");
		// errors.add(ActionErrors.GLOBAL_ERROR, error);
		// saveErrors(request, errors);
	}

	/**
	 * Separates a index of checkbox present in object obj.
	 *
	 * @param obj
	 *            .
	 * @return index.
	 */
	protected Integer getIndex(Object obj)
	{
		final String str = obj.toString();
		final StringTokenizer strTokens = new StringTokenizer(str, Constants.UNDERSCORE);
		strTokens.nextToken();
		final int index = Integer.parseInt(strTokens.nextToken());
		return new Integer(index);
	}

	/**
	 *
	 * @param searchForm : searchForm
	 * @return List < Integer > : List < Integer >
	 */
	protected List<Integer> getCheckboxValues(AdvanceSearchForm searchForm)
	{
		final List<Integer> chkBoxValues = new ArrayList<Integer>();
		//This maintains order of selected specimens.
		if (searchForm.getOrderedString() != null && searchForm.getOrderedString().trim() != "")
		{
			final String[] orderedString = (searchForm.getOrderedString()).split(",");
			for (final String element : orderedString)
			{
				final int index = Integer.parseInt(element);
				chkBoxValues.add(index - 1);
			}
		}
		else
		{
			final Map map = searchForm.getValues();
			final Set chkBoxValuesSet = map.keySet();
			for (final Object checkedValue : chkBoxValuesSet)
			{
				chkBoxValues.add(this.getIndex(checkedValue));
			}
			// Sorting indices so that the order is retained
			Collections.sort(chkBoxValues);
		}
		return chkBoxValues;
	}

}