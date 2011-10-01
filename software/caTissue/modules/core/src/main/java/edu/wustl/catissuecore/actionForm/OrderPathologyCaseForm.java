
package edu.wustl.catissuecore.actionForm;

import java.util.HashMap;
import java.util.Iterator;
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

public class OrderPathologyCaseForm extends AbstractActionForm implements IAddArrayableOrderDetailsForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1707993243973326753L;

	/**
	 * Map containing the key-value pairs
	 */
	private Map values = new HashMap();

	/**
	 * String containing the class of specimen
	 */
	private String className;

	/**
	 * String containing the type of specimen
	 */
	private String type;

	/**
	 * String containing the tissue site
	 */
	private String tissueSite;

	/**
	 * String containing the pathological status
	 */
	private String pathologicalStatus;

	/**
	 * String containing the type of pathological case
	 */
	private String typeOfCase = "derivative";

	/**
	 * String containing the distribution protocol
	 */
	private String distrbutionProtocol;

	/**
	 * String containing the name of array to which to add the pathological case
	 */
	private String addToArray = "";

	/**
	 * List containing DefineArray objects
	 */
	private List defineArrayObj;

	/**
	 * OrderForm object
	 */
	private OrderForm orderForm;

	/**
	 * String array containing the selected items
	 */
	private String[] selectedItems = null;

	/**
	 * String containing unit
	 */
	private String unit;

	/**
	 * String containing concentration
	 */
	private String concentration;

	/**
	 * String array containing the items to be removed
	 */
	private String[] itemsToRemove = null;

	/**
	 * @return itemsToRemove
	 */
	public String[] getItemsToRemove()
	{
		return this.itemsToRemove;
	}

	/**
	 * @param itemsToRemove String array containing the items to be removed
	 */
	public void setItemsToRemove(String[] itemsToRemove)
	{
		this.itemsToRemove = itemsToRemove;
	}

	/**
	 * @return concentration
	 */
	public String getConcentration()
	{
		return this.concentration;
	}

	/**
	 * @param concentration String containing concentration
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * @return String array containing the selected items
	 */
	public String[] getSelectedItems()
	{
		return this.selectedItems;
	}

	/**
	 * @param selectedItems String array containing the selected items
	 */
	public void setSelectedItems(String[] selectedItems)
	{
		this.selectedItems = selectedItems;
	}

	/**
	 * @return name of array in which to add the specimen
	 */
	public String getAddToArray()
	{
		return this.addToArray;
	}

	/**
	 * @param addToArray String containing the name of array to which to add the pathological case
	 */
	public void setAddToArray(String addToArray)
	{
		this.addToArray = addToArray;
	}

	/**
	 * @return list of define array objects
	 */
	public List getDefineArrayObj()
	{
		return this.defineArrayObj;
	}

	/**
	 * @param defineArrayObj List containing DefineArray objects
	 */
	public void setDefineArrayObj(List defineArrayObj)
	{
		this.defineArrayObj = defineArrayObj;
	}

	/**
	 * @return OrderForm object
	 */
	public OrderForm getOrderForm()
	{
		return this.orderForm;
	}

	/** 
	 * @param orderForm OrderForm object
	 */
	public void setOrderForm(OrderForm orderForm)
	{
		this.orderForm = orderForm;
	}

	/**
	 * @return pathological status of the specimen
	 */
	public String getPathologicalStatus()
	{
		return this.pathologicalStatus;
	}

	/**
	 * @param pathologicalStatus String containing the pathological status
	 */
	public void setPathologicalStatus(String pathologicalStatus)
	{
		this.pathologicalStatus = pathologicalStatus;
	}

	/**
	 * @return Returns the className.
	 */
	public String getClassName()
	{
		return this.className;
	}

	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * @return type of specimen
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * @param type String containing the type of specimen
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @return tissue site of specimen
	 */
	public String getTissueSite()
	{
		return this.tissueSite;
	}

	/**
	 * @param tissueSite String containing the tissue site
	 */
	public void setTissueSite(String tissueSite)
	{
		this.tissueSite = tissueSite;
	}

	/**
	 * @return map values
	 */
	public Map getValues()
	{
		return this.values;
	}

	/**
	 * @param values Map containing key-value pairs
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	/**
	 * reset function
	 */
	@Override
	protected void reset()
	{
	}

	/**
	 * @param key String
	 * @param value Object
	 */
	public void setValue(String key, Object value)
	{
		if (this.isMutable())
		{
			this.values.put(key, value);
		}
	}

	/**
	 * @param key String
	 * @return value in map corresponding to the key
	 */
	public Object getValue(String key)
	{
		return this.values.get(key);
	}

	/**
	 * @param abstractDomain AbstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{

	}

	/**
	 * @return Constants.ORDER_PATHOLOGY_FORM_ID
	 */
	@Override
	public int getFormId()
	{
		return Constants.ORDER_PATHOLOGY_FORM_ID;
	}

	/**
	 * @return type of case
	 */
	public String getTypeOfCase()
	{
		return this.typeOfCase;
	}

	/**
	 * @param typeOfCase String containing the type of pathological case
	 */
	public void setTypeOfCase(String typeOfCase)
	{
		this.typeOfCase = typeOfCase;
	}

	/**
	 * @return name of distribution protocol
	 */
	public String getDistrbutionProtocol()
	{
		return this.distrbutionProtocol;
	}

	/**
	 * @param distrbutionProtocol String containing the distribution protocol
	 */
	public void setDistrbutionProtocol(String distrbutionProtocol)
	{
		this.distrbutionProtocol = distrbutionProtocol;
	}

	/**
	 * @return unit
	 */
	public String getUnit()
	{
		return this.unit;
	}

	/**
	 * @param unit String containing the unit
	 */
	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	/**
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @return errors ActionErrors
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{

		final ActionErrors errors = new ActionErrors();

		if (this.selectedItems != null)
		{
			final HttpSession session = request.getSession();
			final List defineArrayFormList = (List) session.getAttribute("DefineArrayFormObjects");
			final Map dataMap = (Map) session.getAttribute(Constants.REQUESTED_BIOSPECIMENS);
			DefineArrayForm defineArrayFormObj = null;
			int capacity = 0;

			if (defineArrayFormList != null)
			{
				defineArrayFormObj = this.searchDefineArrayList(defineArrayFormList,
						this.addToArray);
				capacity = Integer.parseInt(defineArrayFormObj.getDimenmsionX())
						* Integer.parseInt(defineArrayFormObj.getDimenmsionY());
			}
			if (!this.addToArray.equalsIgnoreCase("None"))
			{
				if (dataMap != null && dataMap.containsKey(this.addToArray))
				{
					final List orderItems = (List) dataMap.get(this.addToArray);
					if (orderItems.size() + this.selectedItems.length > capacity)
					{
						errors.add("addToArray", new ActionError("errors.order.capacityless"));
						this.values.clear();
					}
				}
				else
				{
					if (this.selectedItems.length > capacity)
					{
						errors.add("addToArray", new ActionError("errors.order.capacityless"));
						this.values.clear();
					}
				}
			}

			if (this.typeOfCase.equals("false"))
			{
				if (this.className.equals("-1") || this.className.equals("-- Select --")
						|| this.className.equals(""))
				{
					errors.add("specimenClass", new ActionError("errors.specimenClass.required"));
					this.values.clear();

				}
				if (this.type.equals("-1") || this.type.equals("-- Select --")
						|| this.type.equals(""))
				{
					errors.add("type", new ActionError("errors.specimenType.required"));
					this.values.clear();
				}
				if (!this.addToArray.equalsIgnoreCase("None") && defineArrayFormObj != null
						&& !defineArrayFormObj.getArrayClass().equals(this.className))
				{
					errors.add("addToArray", new ActionError("errors.order.properclass"));
					this.values.clear();
				}
			}
			else
			{
				if (!this.addToArray.equalsIgnoreCase("None") && defineArrayFormObj != null
						&& !defineArrayFormObj.getArrayClass().equals("Tissue"))
				{
					errors.add("addToArray", new ActionError("errors.order.properclass"));
					this.values.clear();
				}
			}

			if (this.pathologicalStatus.equals("-1")
					|| this.pathologicalStatus.equals("-- Select --"))
			{
				errors.add("pathologicalStatus", new ActionError(
						"errors.pathologicalStatus.required"));
				this.values.clear();
			}
			if (this.tissueSite.equals("-1") || this.tissueSite.equals("-- Select --"))
			{
				errors.add("tissueSite", new ActionError("errors.tissueSite.required"));
				this.values.clear();
			}

			boolean isNumber = true;
			if (this.values != null && this.values.size() != 0)
			{
				String cnt = null;
				//int reqQntyError = 0;
				for (final String selectedItem : this.selectedItems)
				{
					cnt = selectedItem;
					final String key = "OrderSpecimenBean:" + cnt + "_requestedQuantity";
					final String colprotKey = "OrderSpecimenBean:" + cnt + "_collectionProtocol";

					//					to check for Same CP:Only those SPR can be ordered which belongs to same CP
					if (dataMap != null && dataMap.containsKey("None"))
					{
						final List orderItems = (List) dataMap.get("None");
						if (!orderItems.isEmpty() && orderItems.size() > 0)
						{
							final OrderSpecimenBean orderSpecimenBean = (OrderSpecimenBean) orderItems
									.get(0);
							if (!orderSpecimenBean.getCollectionProtocol().equals(
									this.values.get(colprotKey)))
							{
								errors.add("values", new ActionError(
										"errors.same.collectionProtocol"));
								this.values.clear();
								break;
							}
						}

					}

					if ((this.values.get(key)) == null || (this.values.get(key)).equals(""))
					{
						errors.add("values", new ActionError("errors.requestedQuantity.required"));
						this.values.clear();
						break;
					}
					else
					{
						isNumber = AppUtility.isNumeric(this.values.get(key).toString());
						if (!(isNumber))
						{
							errors.add("values", new ActionError(
									"errors.requestedQuantityBeNumeric.required"));
							this.values.clear();
							break;
						}
						else
						{
							final Double reqQnty = new Double(this.values.get(key).toString());
							if (reqQnty < 0.0 || reqQnty == 0.0)
							{
								errors.add("values", new ActionError(
										"errors.requestedQuantity.required"));
								this.values.clear();
								break;
							}
						}
					}
				}
			}
		}
		return errors;
	}

	/**
	 * @param defineArrayFormList List containing DefineArrayForm objects
	 * @param arrayName String containing name of array
	 * @return defineArrayFormObj 
	 */
	private DefineArrayForm searchDefineArrayList(List defineArrayFormList, String arrayName)
	{
		final Iterator iter = defineArrayFormList.iterator();
		DefineArrayForm defineArrayFormObj = null;
		while (iter.hasNext())
		{
			defineArrayFormObj = (DefineArrayForm) iter.next();
			if (defineArrayFormObj.getArrayName().equalsIgnoreCase(arrayName))
			{
				break;
			}
		}
		return defineArrayFormObj;
	}

	/**
	 * @return boolean true
	 */
	@Override
	public boolean isAddOperation()
	{
		return true;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
