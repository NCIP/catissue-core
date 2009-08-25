
package edu.wustl.catissuecore.actionForm;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
 * orderSpecimenForm for placing an order
 * 
 * @author deepti_phadnis
 * 
 */

public class OrderSpecimenForm extends AbstractActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3669148482862079544L;

	/**
	 * String containing the type of specimen whether existing or derived
	 */
	private String typeOfSpecimen = "existingSpecimen";

	/**
	 * Map containing the key-value pairs
	 */
	private Map values = new LinkedHashMap();

	/**
	 * String containing the class of specimen 
	 */
	private String className;

	/**
	 * String containing the type of specimen 
	 */
	private String type;

	/**
	 * String array containing the selected items
	 */
	private String[] selectedItems = null;

	/**
	 * String array containing the items to be removed
	 */
	private String[] itemsToRemove = null;

	/**
	 * String containing the unit
	 */
	private String unit;

	/**
	 * OrderForm object
	 */
	private OrderForm orderForm;

	/**
	 * String containing the concentration
	 */
	private String concentration;

	/**
	 * String containing the distrbution Protocol
	 */
	private String distrbutionProtocol;

	/**
	 * String containing the name of array to which to add the specimen
	 */
	private String addToArray = "";

	/**
	 * List containing Define Array objects 
	 */
	private List defineArrayObj;

	/**
	 * @return List of defineArrayObj
	 */
	public List getDefineArrayObj()
	{
		return this.defineArrayObj;
	}

	/**
	 * @param defineArrayObj List containing Define Array objects 
	 */
	public void setDefineArrayObj(List defineArrayObj)
	{
		this.defineArrayObj = defineArrayObj;
	}

	/**
	 * 
	 * @return String containing whether speicmens got to be added to list or
	 *         array
	 */
	public String getAddToArray()
	{
		return this.addToArray;
	}

	/**
	 * @param addToArray String containing the name of array to which to add the specimen
	 */
	public void setAddToArray(String addToArray)
	{
		this.addToArray = addToArray;
	}

	// For 'ADD' operation in CommonAddEditAction.
	/**
	 * @return boolean true
	 */
	@Override
	public boolean isAddOperation()
	{
		return true;
	}

	/**
	 * @return concentration
	 */
	public String getConcentration()
	{
		return this.concentration;
	}

	/**
	 * @param concentration String containing the concentration
	 */
	public void setConcentration(String concentration)
	{
		this.concentration = concentration;
	}

	/**
	 * @return unit of requested quantity
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
	 * @return string of specimen selected
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
	 * @return typeOfSpecimen i.e.derived or existing
	 */
	public String getTypeOfSpecimen()
	{
		return this.typeOfSpecimen;
	}

	/**
	 * @param typeOfSpecimen String containing the type of specimen whether existing or derived
	 */
	public void setTypeOfSpecimen(String typeOfSpecimen)
	{
		this.typeOfSpecimen = typeOfSpecimen;
	}

	/**
	 * @param abstractDomain AbstractDomainObject
	 */
	public void setAllValues(AbstractDomainObject abstractDomain)
	{

	}

	/**
	 * @return Constants.ORDER_FORM_ID i.e. the FormId
	 */
	@Override
	public int getFormId()
	{
		return Constants.ORDER_FORM_ID;
	}

	/**
	 * function for resetting 
	 */
	@Override
	protected void reset()
	{

		this.typeOfSpecimen = null;
		this.selectedItems = null;
	}

	/** ** for map *** */
	/**
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 */
	@Override
	public void reset(ActionMapping mapping, HttpServletRequest request)
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
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 * @return errors ActionErrors
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		//Validator validator = new Validator();
		final HttpSession session = request.getSession();
		final Map dataMap = (Map) session.getAttribute(Constants.REQUESTED_BIOSPECIMENS);
		if (this.selectedItems != null)
		{
			boolean isNumber = true;
			final List defineArrayFormList = (List) session.getAttribute("DefineArrayFormObjects");

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
				if (dataMap == null)
				{
					if (this.selectedItems.length > capacity)
					{
						errors.add("addToArray", new ActionError("errors.order.capacityless"));
						this.values.clear();
					}
				}
				else
				{
					if (dataMap.containsKey(this.addToArray))
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
			}

			if (this.typeOfSpecimen.equals("true"))
			{
				if (this.className.equals("-1") || this.className.equals("-- Select --"))
				{
					errors.add("className", new ActionError("errors.specimenClass.required"));
					this.values.clear();
				}
				if (this.type.equals("-1") || this.type.equals("-- Select --"))
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
			if (this.values != null && this.values.size() != 0)
			{
				String cnt = null;
				String reqQuantKey = null;
				String keyClass = null;

				for (final String selectedItem : this.selectedItems)
				{
					cnt = selectedItem;
					reqQuantKey = "OrderSpecimenBean:" + cnt + "_requestedQuantity";
					final String disSiteKey = "OrderSpecimenBean:" + cnt + "_distributionSite";
					final String availQuantKey = "OrderSpecimenBean:" + cnt + "_availableQuantity";
					final String specimenName = (String) this.values.get("OrderSpecimenBean:" + cnt
							+ "_specimenName");
					final String collectionStatuskey = (String) this.values
							.get("OrderSpecimenBean:" + cnt + "_collectionStatus");
					final String isAvailablekey = (String) this.values.get("OrderSpecimenBean:"
							+ cnt + "_isAvailable");
					if (dataMap != null && dataMap.containsKey(this.addToArray))
					{
						final List orderItems = (List) dataMap.get(this.addToArray);
						if (!orderItems.isEmpty() && orderItems.size() > 0)
						{
							final OrderSpecimenBean orderSpecimenBean = (OrderSpecimenBean) orderItems
									.get(0);
							if (!orderSpecimenBean.getDistributionSite().equals(
									this.values.get(disSiteKey)))
							{
								errors.add("values",
										new ActionError("errors.same.distributionSite"));
								this.values.clear();
								break;
							}
						}

					}

					if ((this.values.get(disSiteKey)) == null
							|| (this.values.get(disSiteKey)).equals("N/A"))
					{
						errors.add("values", new ActionError("errors.specimenPosition.required",
								specimenName));
						this.values.clear();
						break;
					}

					if ((this.values.get(disSiteKey)) == null || !this.isSiteSimiliar())
					{
						errors.add("values", new ActionError("errors.same.distributionSite"));
						this.values.clear();
						break;
					}

					if (!Constants.COLLECTION_STATUS_COLLECTED.equals(collectionStatuskey))
					{
						errors.add("values", new ActionError("errors.collectionStatus",
								specimenName));
						this.values.clear();
						break;
					}

					if ((isAvailablekey) == null || (isAvailablekey).equals(""))
					{
						errors.add("values", new ActionError("errors.isAvailable", specimenName));
						this.values.clear();
						break;
					}
					else
					{
						//						Boolean isAvailable = Boolean.valueOf(isAvailablekey.toString());
						if (!(Boolean.valueOf(isAvailablekey.toString())))
						{
							errors.add("values",
									new ActionError("errors.isAvailable", specimenName));
							this.values.clear();
							break;
						}
					}

					if ((this.values.get(availQuantKey)) == null
							|| (this.values.get(availQuantKey)).equals(""))
					{
						errors.add("values", new ActionError("errors.availableQuantity.required",
								specimenName));
						this.values.clear();
						break;
					}
					else
					{
						final Double reqQnty = new Double(this.values.get(availQuantKey).toString());
						if (reqQnty < 0.0 || reqQnty == 0.0)
						{
							errors.add("values", new ActionError(
									"errors.availableQuantity.required", specimenName));
							this.values.clear();
							break;
						}
					}

					if ((this.values.get(reqQuantKey)) == null
							|| (this.values.get(reqQuantKey)).equals(""))
					{
						errors.add("values", new ActionError("errors.requestedQuantity.required"));
						this.values.clear();
						break;
					}

					else
					{
						isNumber = AppUtility.isNumeric(this.values.get(reqQuantKey).toString());
						if (!(isNumber))
						{
							errors.add("values", new ActionError(
									"errors.requestedQuantityBeNumeric.required"));
							this.values.clear();
							break;
						}
						else
						{
							final Double reqQnty = new Double(this.values.get(reqQuantKey)
									.toString());
							if (reqQnty < 0.0 || reqQnty == 0.0)
							{
								errors.add("values", new ActionError(
										"errors.requestedQuantity.required"));
								this.values.clear();
								break;
							}
						}
					}

					keyClass = "OrderSpecimenBean:" + cnt + "_specimenClass";
					if (this.typeOfSpecimen.equals("false")
							&& !this.addToArray.equalsIgnoreCase("None")
							&& defineArrayFormObj != null
							&& !defineArrayFormObj.getArrayClass().equalsIgnoreCase(
									this.values.get(keyClass).toString()))
					{
						errors.add("addToArray", new ActionError("errors.order.properclass"));
						this.values.clear();
						break;
					}

				}
			}
		}
		return errors;
	}

	/**
	 * @return
	 */
	private boolean isSiteSimiliar()
	{
		final boolean isSiteSimiliar = true;
		if (this.values != null && this.values.size() != 0)
		{
			for (final String cnt : this.selectedItems)
			{
				final String distributionSite = (String) this.values.get("OrderSpecimenBean:" + cnt
						+ "_distributionSite");
				for (final  String count : this.selectedItems)
				{
					final String disSiteInner = (String) this.values.get("OrderSpecimenBean:"
							+ count + "_distributionSite");
					if (!disSiteInner.equals(distributionSite))
					{
						return false;
					}
				}
			}

		}

		return isSiteSimiliar;
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
	 * @param key String
	 * @return Object
	 */
	public Object getValue(String key)
	{
		return this.values.get(key);
	}

	/**
	 * @return values in map
	 */
	public Collection getAllValues()
	{
		return this.values.values();
	}

	/**
	 * @param values Map containing key-value pairs
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	/**
	 * @return Map
	 */
	public Map getValues()
	{
		return this.values;
	}

	/**
	 * @return specimens to be removed
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
	 * @return OrderForm
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
	 * @return name of distribution protocol
	 */
	public String getDistrbutionProtocol()
	{
		return this.distrbutionProtocol;
	}

	/**
	 * @param distrbutionProtocol String containing the distrbution Protocol
	 */
	public void setDistrbutionProtocol(String distrbutionProtocol)
	{
		this.distrbutionProtocol = distrbutionProtocol;
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
	 * @return Returns the type.
	 */
	public String getType()
	{
		return this.type;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

}
