
package edu.wustl.catissuecore.actionForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.OrderSpecimenBean;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * @author renuka_bajpai
 *
 */
public class OrderBiospecimenArrayForm extends AbstractActionForm
{

	/**
	 * A String containing type of Array
	 */
	private String typeOfArray = "existingArray";

	/**
	 * A String array containing selectedItems
	 */
	private String[] selectedItems = null;

	private OrderForm orderForm;

	private Map values = new HashMap();

	/**
	 * A String containing Distribution Protocol
	 */
	private String distrbutionProtocol;

	/**
	 * A List containing the DefineArray objects
	 */
	private List defineArrayObj;

	/**
	 * A String array containing itemsToRemove 
	 */
	private String[] itemsToRemove = null;

	/**
	 * @return itemsToRemove
	 */
	public String[] getItemsToRemove()
	{
		return itemsToRemove;
	}

	/**
	 * @param itemsToRemove String array containing itemsToRemove 
	 */
	public void setItemsToRemove(String[] itemsToRemove)
	{
		this.itemsToRemove = itemsToRemove;
	}

	/**
	 * @return List of defineArrayObj
	 */
	public List getDefineArrayObj()
	{
		return defineArrayObj;
	}

	/**
	 * @param defineArrayObj List containing the DefineArray objects
	 */
	public void setDefineArrayObj(List defineArrayObj)
	{
		this.defineArrayObj = defineArrayObj;
	}

	/**
	 * @return boolean true 
	 */
	public boolean isAddOperation()

	{
		return true;
	}

	/**
	 * @return name of distribution protocol
	 */
	public String getDistrbutionProtocol()
	{
		return distrbutionProtocol;
	}

	/**
	 * @param distrbutionProtocol String containing Distribution Protocol
	 */
	public void setDistrbutionProtocol(String distrbutionProtocol)
	{
		this.distrbutionProtocol = distrbutionProtocol;
	}

	/**
	 * @return object of OrderForm
	 */
	public OrderForm getOrderForm()
	{
		return orderForm;
	}

	/**
	 * @param orderForm OrderForm object
	 */
	public void setOrderForm(OrderForm orderForm)
	{
		this.orderForm = orderForm;
	}

	/**
	 * @return type of array- whether existing array or tissue slide from a
	 *         block
	 */
	public String getTypeOfArray()
	{
		return typeOfArray;
	}

	/**
	 * @param typeOfArray String containing type of Array
	 */
	public void setTypeOfArray(String typeOfArray)
	{
		this.typeOfArray = typeOfArray;
	}

	/**
	 * @return FormId
	 */
	public int getFormId()
	{
		return Constants.ORDER_ARRAY_FORM_ID;
	}

	/**
	 * reset function
	 */
	protected void reset()
	{
	}

	/**
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
	}

	/**
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @return errors ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		ActionErrors errors = new ActionErrors();
		HttpSession session = request.getSession();
		Map dataMap = (Map) session.getAttribute(Constants.REQUESTED_BIOSPECIMENS);

		if (selectedItems != null)
		{
			boolean isNumber = true;
			if (values != null && values.size() != 0)
			{
				String cnt = null;
				int reqQntyError = 0;
				//String reqQntyValue = null;
				for (int i = 0; i < selectedItems.length; i++)
				{
					cnt = selectedItems[i];
					String disSiteKey = "OrderSpecimenBean:" + cnt + "_distributionSite";
					String key = "OrderSpecimenBean:" + cnt + "_requestedQuantity";

					//to check for site:Only those specimen Array can be ordered which belongs to same site
					if (dataMap != null && dataMap.containsKey("None"))
					{
						List orderItems = (List) dataMap.get("None");
						if (!orderItems.isEmpty() && orderItems.size() > 0)
						{
							OrderSpecimenBean orderSpecimenBean = (OrderSpecimenBean) orderItems
									.get(0);
							if (!orderSpecimenBean.getDistributionSite().equals(
									values.get(disSiteKey)))
							{
								errors.add("values",
										new ActionError("errors.same.distributionSite"));
								values.clear();
								break;
							}
						}

					}

					if ((values.get(disSiteKey)) == null || !isSameSite())
					{
						errors.add("values", new ActionError(
								"errors.specimenArray.same.distributionSite.required"));
						values.clear();
						break;
					}

					if (typeOfArray.equals("false"))
					{
						values.put(key, "0.0");
					}

					else
					{
						if ((values.get(key)) == null || (values.get(key)).equals(""))
						{
							reqQntyError = 1;
							break;
						}

						else
						{
							isNumber = AppUtility.isNumeric(values.get(key).toString());
							if (!(isNumber))
							{
								reqQntyError = 2;
								break;
							}
							else
							{
								Double reqQnty = new Double(values.get(key).toString());
								if (reqQnty < 0.0 || reqQnty == 0.0)
								{
									reqQntyError = 1;
									break;
								}
							}
						}
					}
				}
				if (reqQntyError == 1)
				{
					errors.add("values", new ActionError("errors.requestedQuantity.required"));
					values.clear();
				}
				if (reqQntyError == 2)
				{
					errors.add("values", new ActionError(
							"errors.requestedQuantityBeNumeric.required"));
					values.clear();
				}
			}
		}
		return errors;
	}

	private boolean isSameSite()
	{
		boolean isSameSite = true;
		if (values != null && values.size() != 0)
		{
			for (int i = 0; i < selectedItems.length; i++)
			{
				String cnt = selectedItems[i];
				String disSite = (String) values.get("OrderSpecimenBean:" + cnt
						+ "_distributionSite");
				for (int j = 0; j < selectedItems.length; j++)
				{
					String count = selectedItems[j];
					String disSiteInner = (String) values.get("OrderSpecimenBean:" + count
							+ "_distributionSite");
					if (!disSiteInner.equals(disSite))
					{
						return false;
					}
				}
			}

		}

		return isSameSite;
	}

	/**
	 * @param abstractDomain AbstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{
	}

	/**
	 * @return String array selectedItems-indices of selected items
	 */
	public String[] getSelectedItems()
	{
		return selectedItems;
	}

	/**
	 * @param selectedItems String array containing selectedItems
	 */
	public void setSelectedItems(String[] selectedItems)
	{
		this.selectedItems = selectedItems;
	}

	/**
	 * @param key String containing the key of map 
	 * @param value String containing the value of map 
	 */
	public void setValue(String key, Object value)
	{
		if (isMutable())
		{
			values.put(key, value);
		}
	}

	/**
	 * @param key String containing the key of map 
	 * @return value in map corresponding to the key
	 */
	public Object getValue(String key)
	{
		return values.get(key);
	}

	/**
	 * @return map values
	 */
	public Map getValues()
	{
		return values;
	}

	/**
	 * @param values Map containing the key-value pairs 
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
