/**
 * <p>Title: RequestDetailsForm Class>
 * <p>Description:	This class contains attributes to display on RequestDetails.jsp Page</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Ashish Gupta
 * @version 1.00
 * Created on Oct 06,2006
 */

package edu.wustl.catissuecore.actionForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bean.DefinedArrayDetailsBean;
import edu.wustl.catissuecore.bean.RequestDetailsBean;
import edu.wustl.catissuecore.bizlogic.OrderBizLogic;
import edu.wustl.catissuecore.domain.ConsentTierStatus;
import edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.NewSpecimenArrayOrderItem;
import edu.wustl.catissuecore.domain.OrderDetails;
import edu.wustl.catissuecore.domain.OrderItem;
import edu.wustl.catissuecore.domain.PathologicalCaseOrderItem;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.SpecimenOrderItem;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.OrderingSystemUtil;
import edu.wustl.catissuecore.util.SpecimenComparator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.util.HibernateMetaData;

/**
 * @author renuka_bajpai
 *
 */
public class RequestDetailsForm extends AbstractActionForm
{

	private static final long serialVersionUID = 1L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(RequestDetailsForm.class);
	// The status which the user wants to update in one go.
	private String status;
	// The Map containg submitted values for 'assigned quantity', 'assigned status' and 'request for'.
	protected Map values = new LinkedHashMap();
	// The administrator comments.
	private String administratorComments;
	// The Order Id required to retrieve the corresponding order items from the database.
	private long id;
	//The Site associated with the distribution.
	private String site;
	/**
	 * The distribution protocol associated with that order.
	 */
	private String distributionProtocolId;

	/**
	 * Name of the order
	 */
	private String orderName;

	/**
	 * Requested Date.
	 */
	private String requestedDate;

	/**
	 *
	 * @return
	 */
	public String getOrderName()
	{
		return this.orderName;
	}

	/**
	 *
	 * @param orderName
	 */
	public void setOrderName(String orderName)
	{
		this.orderName = orderName;
	}

	private Boolean isDirectDistribution = Boolean.FALSE;

	public Boolean getIsDirectDistribution()
	{
		return this.isDirectDistribution;
	}

	/**
	 *
	 * @param isDirectDistribution
	 */
	public void setIsDirectDistribution(Boolean isDirectDistribution)
	{
		this.isDirectDistribution = isDirectDistribution;
	}

	/**
	 * The tab page which should be visible to the user.
	 */
	private int tabIndex;
	/**
	 * The map to display the list of specimens in request For drop down.
	 */
	private Map requestForDropDownMap = new LinkedHashMap();
	/**
	 *
	 */
	private String specimenId;

	private Boolean mailNotification = Boolean.FALSE;

	/** Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */

	public void setRequestFor(String key, Object value)
	{
		if (this.isMutable())
		{
			this.requestForDropDownMap.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public List getRequestFor(String key)
	{
		return ((List) (this.requestForDropDownMap.get(key)));
	}

	/**
	 * @return Returns the mailNotification.
	 */
	public Boolean getMailNotification()
	{
		return this.mailNotification;
	}

	/**
	 * @param mailNotification The mailNotification to set.
	 */
	public void setMailNotification(Boolean mailNotification)
	{
		this.mailNotification = mailNotification;
	}

	/**
	 * @return the site
	 */
	public String getSite()
	{
		return this.site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(String site)
	{
		this.site = site;
	}

	//For 'EDIT' operation in CommonAddEditAction.
	/**
	 * @return boolean. 'true' if operation is add.
	 */
	public boolean isAddOperation()
	{
		return false;
	}

	/**
	 * @return the id
	 */
	public long getId()
	{
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id)
	{
		this.id = id;
	}

	/**
	 * @return the administratorComments
	 */
	public String getAdministratorComments()
	{
		return this.administratorComments;
	}

	/**
	 * @param administratorComments the administratorComments to set
	 */
	public void setAdministratorComments(String administratorComments)
	{
		this.administratorComments = administratorComments;
	}

	/**
	 * @return Returns the values.
	 */
	public Collection getAllValues()
	{
		return this.values.values();
	}

	/** Associates the specified object with the specified key in the map.
	 * @param key the key to which the object is mapped.
	 * @param value the object which is mapped.
	 */

	public void setValue(String key, Object value)
	{
		if (this.isMutable())
		{
			this.values.put(key, value);
		}
	}

	/**
	 * Returns the object to which this map maps the specified key.
	 * @param key the required key.
	 * @return the object to which this map maps the specified key.
	 */
	public Object getValue(String key)
	{
		return this.values.get(key);
	}

	/**
	 * @return values
	 */
	public Map getValues()
	{
		return this.values;
	}

	/**
	 * @param values Map
	 */
	public void setValues(Map values)
	{
		this.values = values;
	}

	/**
	 * @return int formId.
	 */
	public int getFormId()
	{

		return Constants.REQUEST_DETAILS_FORM_ID;
	}

	/**
	 * reset.
	 */
	protected void reset()
	{

	}

	/**
	 * @param abstractDomain object
	 * @throws BizLogicException BizLogic Exception
	 */
	public void setAllValuesForOrder(AbstractDomainObject abstractDomain,
			HttpServletRequest request, DAO dao) throws BizLogicException
			{
		int requestDetailsBeanCounter = 0;
		int existingArrayBeanCounter = 0;
		final OrderDetails order = (OrderDetails) abstractDomain;
		final Collection<OrderItem>  orderItemColl = order.getOrderItemCollection();
		final List<OrderItem>  orderItemList = new ArrayList<OrderItem> (orderItemColl);
		// Sorting by OrderItem.id
		Collections.sort(orderItemList, new IdComparator());
		final Iterator<OrderItem>  iter = orderItemList.iterator();
		final List totalSpecimenListInRequestForDropDown = new ArrayList();
		final Map definedArrayMap = new LinkedHashMap();
		while (iter.hasNext())
		{
			final OrderItem orderItem = (OrderItem) iter.next();
			//Making keys
			String assignStatus = "";
			String description = "";

			String requestedItem = "";
			String requestedQty = "";
			String availableQty = "";
			String specimenClass = "";
			String specimenType = "";

			String orderItemId = "";
			String requestFor = "";
			String assignQty = "";
			String instanceOf = "";
			String specimenId = "";
			String distributedItemId = "";
			String specimenList = "";
			String specimenCollGrpId = "";
			String consentVerificationkey = "";
			String rowStatuskey = "";
			String selectedSpecimenTypeKey = "";

			String actualSpecimenClass = "";
			String actualSpecimenType = "";
			//For array
			String arrayId = "";
			String canDistributeKey = "";
			String selectedSpecimenQuantityUnit = "";
			String selectedSpecimenQuantity = "";

			if (((orderItem instanceof ExistingSpecimenOrderItem)
					|| (orderItem instanceof DerivedSpecimenOrderItem) || (orderItem instanceof PathologicalCaseOrderItem)))
			{
				final SpecimenOrderItem specimenOrderItem = (SpecimenOrderItem) orderItem;
				if (specimenOrderItem.getNewSpecimenArrayOrderItem() == null)
				{
					assignStatus = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_assignedStatus";
					description = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_description";
					requestedQty = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_requestedQty";

					orderItemId = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_orderItemId";

					requestedItem = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_requestedItem";
					availableQty = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_availableQty";
					specimenClass = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_className";
					specimenType = "RequestDetailsBean:" + requestDetailsBeanCounter + "_type";

					requestFor = "RequestDetailsBean:" + requestDetailsBeanCounter + "_requestFor";
					specimenId = "RequestDetailsBean:" + requestDetailsBeanCounter + "_specimenId";
					consentVerificationkey = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_consentVerificationkey";
					canDistributeKey = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_canDistribute";

					rowStatuskey = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_rowStatuskey";

					assignQty = "RequestDetailsBean:" + requestDetailsBeanCounter + "_assignedQty";
					instanceOf = "RequestDetailsBean:" + requestDetailsBeanCounter + "_instanceOf";
					distributedItemId = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_distributedItemId";
					specimenCollGrpId = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_specimenCollGroupId";
					specimenList = "RequestForDropDownList:" + requestDetailsBeanCounter;

					actualSpecimenClass = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_actualSpecimenClass";
					actualSpecimenType = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_actualSpecimenType";
					selectedSpecimenTypeKey = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_selectedSpecimenType";
					selectedSpecimenQuantityUnit = "RequestDetailsBean:"
						+ requestDetailsBeanCounter + "_specimenQuantityUnit";
					selectedSpecimenQuantity = "RequestDetailsBean:" + requestDetailsBeanCounter
					+ "_selectedSpecimenQuantity";

					this.populateValuesMap(orderItem, requestedItem, availableQty, specimenClass,
							specimenType, requestFor, specimenId, assignQty, instanceOf,
							specimenList, specimenCollGrpId, totalSpecimenListInRequestForDropDown,
							actualSpecimenClass, actualSpecimenType, assignStatus,
							consentVerificationkey, canDistributeKey, rowStatuskey,
							selectedSpecimenTypeKey, selectedSpecimenQuantityUnit,
							selectedSpecimenQuantity, dao);
					requestDetailsBeanCounter++;
				}
				else
				{

					List defineArrayContentsList = null;
					if (definedArrayMap.get(specimenOrderItem.getNewSpecimenArrayOrderItem()) == null)
					{
						defineArrayContentsList = new ArrayList();
					}
					else
					{
						defineArrayContentsList = (List) definedArrayMap.get(specimenOrderItem
								.getNewSpecimenArrayOrderItem());
					}
					defineArrayContentsList.add(specimenOrderItem);
					definedArrayMap.put(specimenOrderItem.getNewSpecimenArrayOrderItem(),
							defineArrayContentsList);
				}
			}
			else if (orderItem instanceof ExistingSpecimenArrayOrderItem)
			{
				assignStatus = "ExistingArrayDetailsBean:" + existingArrayBeanCounter
				+ "_assignedStatus";
				description = "ExistingArrayDetailsBean:" + existingArrayBeanCounter
				+ "_description";
				requestedQty = "ExistingArrayDetailsBean:" + existingArrayBeanCounter
				+ "_requestedQuantity";
				orderItemId = "ExistingArrayDetailsBean:" + existingArrayBeanCounter
				+ "_orderItemId";

				requestedItem = "ExistingArrayDetailsBean:" + existingArrayBeanCounter
				+ "_bioSpecimenArrayName";
				arrayId = "ExistingArrayDetailsBean:" + existingArrayBeanCounter + "_arrayId";
				assignQty = "ExistingArrayDetailsBean:" + existingArrayBeanCounter
				+ "_assignedQuantity";
				distributedItemId = "ExistingArrayDetailsBean:" + existingArrayBeanCounter
				+ "_distributedItemId";

				final ExistingSpecimenArrayOrderItem existingSpecimenArrayOrderItem = (ExistingSpecimenArrayOrderItem) orderItem;
				this.values.put(requestedItem, existingSpecimenArrayOrderItem.getSpecimenArray()
						.getName());
				this.values.put(arrayId, existingSpecimenArrayOrderItem.getSpecimenArray().getId()
						.toString());
				this.values.put(assignQty, "0.0");

				existingArrayBeanCounter++;
			}
			/*else if (orderItem instanceof NewSpecimenArrayOrderItem)
			{
				NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) orderItem;
				List defineArrayContentsList = null;
				if (definedArrayMap.get(newSpecimenArrayOrderItem) == null)
				{
					defineArrayContentsList = new ArrayList();
					definedArrayMap.put(newSpecimenArrayOrderItem, defineArrayContentsList);
				}
			}*/
			this.putCommonValuesInValuesMap(orderItem, assignStatus, description, requestedQty,
					assignQty, orderItemId, distributedItemId);
		}
		// Putting defined array values in Values map
		if (definedArrayMap.size() > 0)
		{
			this.makeValuesMapForDefinedArray(definedArrayMap,
					totalSpecimenListInRequestForDropDown, dao);
		}
		request.getSession().removeAttribute("finalSpecimenList");
		request.getSession().setAttribute("finalSpecimenList",
				totalSpecimenListInRequestForDropDown);
			}

	/**
	 * @param definedArrayMap
	 * @throws BizLogicException
	 */
	private void makeValuesMapForDefinedArray(Map definedArrayMap,
			List totalSpecimenListInRequestForDropDown, DAO dao) throws BizLogicException
			{
		final Set keySet = definedArrayMap.keySet();
		final Iterator iter = keySet.iterator();
		int arrayRequestBeanCounter = 0;
		int arrayDetailsBeanCounter = 0;
		while (iter.hasNext())
		{
			final NewSpecimenArrayOrderItem newSpecimenArrayOrderItem = (NewSpecimenArrayOrderItem) iter
			.next();
			this.makeKeysForNewSpecimenArrayOrderItem(arrayRequestBeanCounter,
					newSpecimenArrayOrderItem, dao);
			final List specimenOrderItemList = (List) definedArrayMap
			.get(newSpecimenArrayOrderItem);

			final String noOfItems = "DefinedArrayRequestBean:" + arrayRequestBeanCounter
			+ "_noOfItems";
			this.values.put(noOfItems, "" + specimenOrderItemList.size());

			final Iterator specimenItemListIter = specimenOrderItemList.iterator();
			while (specimenItemListIter.hasNext())
			{
				final SpecimenOrderItem specimenOrderItem = (SpecimenOrderItem) specimenItemListIter
				.next();
				this.makeKeysForDefinedArrayContents(arrayDetailsBeanCounter, specimenOrderItem,
						totalSpecimenListInRequestForDropDown, dao);
				arrayDetailsBeanCounter++;
			}
			arrayRequestBeanCounter++;
		}
			}

	/**
	 * @param arrayRequestBeanCounter
	 * @param newSpecimenArrayOrderItem
	 */
	private void makeKeysForNewSpecimenArrayOrderItem(int arrayRequestBeanCounter,
			NewSpecimenArrayOrderItem newSpecimenArrayOrderItem, DAO dao)
	{
		final String assignStatus = "DefinedArrayRequestBean:" + arrayRequestBeanCounter
		+ "_assignedStatus";
		final String orderItemId = "DefinedArrayRequestBean:" + arrayRequestBeanCounter
		+ "_orderItemId";

		final String requestedItem = "DefinedArrayRequestBean:" + arrayRequestBeanCounter
		+ "_arrayName";
		final String specimenClass = "DefinedArrayRequestBean:" + arrayRequestBeanCounter
		+ "_arrayClass";
		final String specimenType = "DefinedArrayRequestBean:" + arrayRequestBeanCounter
		+ "_arrayType";
		final String positionDimensionOne = "DefinedArrayRequestBean:" + arrayRequestBeanCounter
		+ "_oneDimensionCapacity";
		final String positionDimensionTwo = "DefinedArrayRequestBean:" + arrayRequestBeanCounter
		+ "_twoDimensionCapacity";

		final String arrayId = "DefinedArrayRequestBean:" + arrayRequestBeanCounter + "_arrayId";
		final String distributedItemId = "DefinedArrayRequestBean:" + arrayRequestBeanCounter
		+ "_distributedItemId";
		final String createArrayCondition = "DefinedArrayRequestBean:" + arrayRequestBeanCounter
		+ "_createArrayButtonDisabled";

		this.values.put(requestedItem, newSpecimenArrayOrderItem.getName());
		this.values.put(positionDimensionOne, newSpecimenArrayOrderItem.getSpecimenArrayType()
				.getCapacity().getOneDimensionCapacity().toString());
		this.values.put(positionDimensionTwo, newSpecimenArrayOrderItem.getSpecimenArrayType()
				.getCapacity().getTwoDimensionCapacity().toString());
		this.values.put(specimenClass, newSpecimenArrayOrderItem.getSpecimenArrayType()
				.getSpecimenClass());
		this.values.put(specimenType, newSpecimenArrayOrderItem.getSpecimenArrayType().getName());

		final SpecimenArray specimenArrayObj = newSpecimenArrayOrderItem.getSpecimenArray();
		if (specimenArrayObj != null)
		{
			this.values.put(arrayId, specimenArrayObj.getId().toString());
		}
		final Collection<SpecimenOrderItem> specimenOrderItemCollection = newSpecimenArrayOrderItem
		.getSpecimenOrderItemCollection();
		//Calculating the condition to enable or disable "Create Array Button"
		String condition = OrderingSystemUtil
		.determineCreateArrayCondition(specimenOrderItemCollection);

		if (newSpecimenArrayOrderItem.getStatus().trim().equalsIgnoreCase(
				Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
				|| (newSpecimenArrayOrderItem.getStatus().trim()
						.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
		{
			condition = "true";
		}
		this.values.put(createArrayCondition, condition);
		this.putCommonValuesInValuesMap(newSpecimenArrayOrderItem, assignStatus, "", "", "",
				orderItemId, distributedItemId);

	}

	/**
	 * @param arrayDetailsBeanCounter
	 * @param specimenOrderItem
	 * @param totalSpecimenListInRequestForDropDown
	 * @throws BizLogicException BizLogic Exception
	 */
	private void makeKeysForDefinedArrayContents(int arrayDetailsBeanCounter,
			SpecimenOrderItem specimenOrderItem, List totalSpecimenListInRequestForDropDown, DAO dao)
	throws BizLogicException
	{
		final String assignStatus = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_assignedStatus";
		final String description = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_description";
		final String requestedQty = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_requestedQuantity";
		final String orderItemId = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_orderItemId";

		final String requestedItem = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_requestedItem";
		final String availableQty = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_availableQuantity";
		final String specimenClass = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_className";
		final String specimenType = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter + "_type";

		final String requestFor = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_requestFor";
		final String specimenId = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_specimenId";
		final String assignQty = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_assignedQuantity";
		final String instanceOf = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_instanceOf";
		//distributedItemId = "DefinedArrayDetailsBean:"+arrayDetailsBeanCounter+"_distributedItemId";
		final String specimenList = "RequestForDropDownListArray:" + arrayDetailsBeanCounter;
		final String specimenCollGrpId = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_specimenCollGroupId";

		final String actualSpecimenClass = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_actualSpecimenClass";
		final String actualSpecimenType = "DefinedArrayDetailsBean:" + arrayDetailsBeanCounter
		+ "_actualSpecimenType";

		this.populateValuesMap(specimenOrderItem, requestedItem, availableQty, specimenClass,
				specimenType, requestFor, specimenId, assignQty, instanceOf, specimenList,
				specimenCollGrpId, totalSpecimenListInRequestForDropDown, actualSpecimenClass,
				actualSpecimenType, assignStatus, "", "", "", "", "", "", dao);
		this.putCommonValuesInValuesMap(specimenOrderItem, assignStatus, description, requestedQty,
				assignQty, orderItemId, "");
	}

	/**
	 * @param orderItem
	 * @param assignStatus
	 * @param description
	 * @param requestedQty
	 * @param orderItemId
	 * @param distributedItemId
	 */
	private void putCommonValuesInValuesMap(OrderItem orderItem, String assignStatus,
			String description, String requestedQty, String assignQty, String orderItemId,
			String distributedItemId)
	{
		if (this.values.get(assignStatus) == null)
		{
			this.values.put(assignStatus, orderItem.getStatus());
		}
		this.values.put(description, orderItem.getDescription());
		if (orderItem.getRequestedQuantity() != null)
		{//condition is for define array
			this.values.put(requestedQty, orderItem.getRequestedQuantity().toString());
		}
		this.values.put(orderItemId, orderItem.getId());
		if (orderItem.getDistributedItem() != null)
		{
			this.values.put(distributedItemId, orderItem.getDistributedItem().getId().toString());

		}
		else
		{
			this.values.put(distributedItemId, "");
			if (orderItem.getRequestedQuantity() != null)
			{
				this.values.put(assignQty, orderItem.getRequestedQuantity().toString());
			}
		}
	}

	/**
	 * @param orderItem
	 * @param requestedItem
	 * @param availableQty
	 * @param specimenClass
	 * @param specimenType
	 * @throws BizLogicException BizLogic Exception
	 */
	private void populateValuesMap(OrderItem orderItem, String requestedItem, String availableQty,
			String specimenClass, String specimenType, String requestFor, String specimenId,
			String assignQty, String instanceOf, String specimenList, String specimenCollGrpId,
			List totalSpecimenListInRequestForDropDown, String actualSpecimenClass,
			String actualSpecimenType, String assignStatus, String consentVerificationkey,
			String canDistributeKey, String rowStatuskey, String selectedSpecimenTypeKey,
			String selectedSpecimenQuantityUnit, String selectedSpecimenQuantity, DAO dao)
	throws BizLogicException
	{
		if (orderItem instanceof ExistingSpecimenOrderItem)
		{

			ExistingSpecimenOrderItem existingSpecimenOrderItem = (ExistingSpecimenOrderItem) orderItem;
			//			OrderBizLogic orderBizLogic = (OrderBizLogic) BizLogicFactory.getInstance().getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
			existingSpecimenOrderItem = (ExistingSpecimenOrderItem) HibernateMetaData
			.getProxyObjectImpl(existingSpecimenOrderItem);
			//orderBizLogic.getSpecimen(existingSpecimenOrderItem.getSpecimen().getId(),dao);
			this.values.put(requestedItem, existingSpecimenOrderItem.getSpecimen().getLabel());
			this.values.put(availableQty, existingSpecimenOrderItem.getSpecimen()
					.getAvailableQuantity());
			this.values.put(specimenClass, existingSpecimenOrderItem.getSpecimen()
					.getSpecimenClass());
			this.values
			.put(specimenType, existingSpecimenOrderItem.getSpecimen().getSpecimenType());
			this.values.put(specimenId, existingSpecimenOrderItem.getSpecimen().getId().toString());
			this.values.put(instanceOf, "Existing");
			if (existingSpecimenOrderItem.getStatus().equals(
					Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
					|| existingSpecimenOrderItem.getStatus().equals(
							Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE))
			{
				this.values.put(canDistributeKey, Constants.TRUE);
				this.values.put(rowStatuskey, "disable");

			}
			else
			{
				this.values.put(canDistributeKey, Constants.FALSE);
				this.values.put(rowStatuskey, "enable");
			}

			final Collection<ConsentTierStatus> col = existingSpecimenOrderItem.getSpecimen()
			.getConsentTierStatusCollection();
			if(col!=null)
			{
				final Iterator<ConsentTierStatus> itr = col.iterator();
				if (itr.hasNext())
				{
					this.values.put(consentVerificationkey, Constants.VIEW_CONSENTS);
				}
				else
				{
					this.values.put(consentVerificationkey, Constants.NO_CONSENTS);
				}
			}
			//values.put(consentVerificationkey, "No Consents");
			//Fix me second condition added by vaishali
			if (existingSpecimenOrderItem.getDistributedItem() != null
					&& existingSpecimenOrderItem.getDistributedItem().getQuantity() != null)
			{
				this.values.put(assignQty, existingSpecimenOrderItem.getDistributedItem()
						.getQuantity().toString());
			}
			this.values.put(actualSpecimenClass, existingSpecimenOrderItem.getSpecimen()
					.getSpecimenClass());
			this.values.put(actualSpecimenType, existingSpecimenOrderItem.getSpecimen()
					.getSpecimenType());

			// setting default status
			if (existingSpecimenOrderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_NEW))
			{
				if (existingSpecimenOrderItem.getNewSpecimenArrayOrderItem() == null)
				{
					this.values.put(assignStatus,
							Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION);
				}
				else
				{
					this.values.put(assignStatus,
							Constants.ORDER_REQUEST_STATUS_READY_FOR_ARRAY_PREPARATION);
				}
			}

			this.values.put(requestFor, existingSpecimenOrderItem.getSpecimen().getId());
			this.values.put(selectedSpecimenTypeKey, existingSpecimenOrderItem.getSpecimen()
					.getSpecimenType());
			this.values.put(selectedSpecimenQuantity, existingSpecimenOrderItem.getSpecimen()
					.getAvailableQuantity().toString());
			this.values.put(selectedSpecimenQuantityUnit, OrderingSystemUtil
					.getUnit(existingSpecimenOrderItem.getSpecimen()));

			List allSpecimen = new ArrayList();
			allSpecimen = OrderingSystemUtil
			.getAllSpecimen(existingSpecimenOrderItem.getSpecimen());
			final SpecimenComparator comparator = new SpecimenComparator();
			Collections.sort(allSpecimen, comparator);
			final List childrenSpecimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(
					allSpecimen, existingSpecimenOrderItem.getSpecimen());
			this.requestForDropDownMap.put(specimenList, childrenSpecimenListToDisplay);

		}
		else if (orderItem instanceof DerivedSpecimenOrderItem)
		{
			final DerivedSpecimenOrderItem derivedSpecimenOrderItem = (DerivedSpecimenOrderItem) orderItem;
			this.values.put(requestedItem, derivedSpecimenOrderItem.getParentSpecimen().getLabel());
			//Collection childrenSpecimenList = OrderingSystemUtil.getAllChildrenSpecimen(derivedSpecimenOrderItem.getParentSpecimen().getChildSpecimenCollection());
			//List finalChildrenSpecimenList = OrderingSystemUtil.getChildrenSpecimenForClassAndType(childrenSpecimenList, derivedSpecimenOrderItem
			//	.getSpecimenClass(), derivedSpecimenOrderItem.getSpecimenType());
			//Iterator i = finalChildrenSpecimenList.iterator();
			//while (i.hasNext())
			//{//	Ajax  conditions
			//totalSpecimenListInRequestForDropDown.add(i.next());
			//}

			List allSpecimen = new ArrayList();
			if (derivedSpecimenOrderItem.getNewSpecimenArrayOrderItem() != null)
			{
				final Collection childrenSpecimenList = OrderingSystemUtil
				.getAllChildrenSpecimen(derivedSpecimenOrderItem.getParentSpecimen()
						.getChildSpecimenCollection());
				allSpecimen = OrderingSystemUtil.getChildrenSpecimenForClassAndType(
						childrenSpecimenList, derivedSpecimenOrderItem.getSpecimenClass(),
						derivedSpecimenOrderItem.getSpecimenType());

			}
			else
			{
				allSpecimen = OrderingSystemUtil.getAllSpecimen(derivedSpecimenOrderItem
						.getParentSpecimen());
			}

			final SpecimenComparator comparator = new SpecimenComparator();
			Collections.sort(allSpecimen, comparator);
			final List childrenSpecimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(
					allSpecimen, null);
			this.values.put(availableQty, derivedSpecimenOrderItem.getParentSpecimen()
					.getAvailableQuantity().toString());
			this.values.put(selectedSpecimenTypeKey, "NA");

			if (orderItem.getStatus().equals(Constants.ORDER_REQUEST_STATUS_NEW))
			{
				this.values.put(assignStatus,
						Constants.ORDER_REQUEST_STATUS_PENDING_FOR_DISTRIBUTION);
			}

			final Collection col = derivedSpecimenOrderItem.getParentSpecimen()
			.getConsentTierStatusCollection();
			final Iterator itr = col.iterator();

			if (!allSpecimen.isEmpty())
			{
				this.values.put(availableQty, (((Specimen) allSpecimen.get(0))
						.getAvailableQuantity().toString()));
				if (itr.hasNext())
				{
					this.values.put(consentVerificationkey, Constants.VIEW_CONSENTS);
				}
				else
				{
					this.values.put(consentVerificationkey, Constants.NO_CONSENTS);
				}

			}
			else
			{
				this.values.put(availableQty, "");//derivedSpecimenorderItem.getSpecimen().getAvailableQuantity().getValue().toString()

				this.values.put(consentVerificationkey, Constants.NO_CONSENTS);

			}

			if (allSpecimen.size() != 0
					&& derivedSpecimenOrderItem.getNewSpecimenArrayOrderItem() != null)
			{
				final Specimen spec = ((Specimen) allSpecimen.get(0));
				this.values.put(requestFor, spec.getId());
			}
			else
			{
				this.values.put(requestFor, "#");
			}

			if (derivedSpecimenOrderItem.getStatus().equals(
					Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
					|| derivedSpecimenOrderItem
					.getStatus()
					.equals(
							assignStatus
							.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
			{
				this.values.put(canDistributeKey, Constants.TRUE);
				this.values.put(rowStatuskey, "disable");

			}
			else
			{
				this.values.put(canDistributeKey, Constants.FALSE);
				this.values.put(rowStatuskey, "enable");
			}
			this.values.put(specimenClass, derivedSpecimenOrderItem.getSpecimenClass());
			this.values.put(specimenType, derivedSpecimenOrderItem.getSpecimenType());
			this.values.put(specimenId, derivedSpecimenOrderItem.getParentSpecimen().getId()
					.toString());
			//	values.put(consentVerificationkey, "View");
			this.values.put(instanceOf, "Derived");

			//fix me second condition added by vaishali
			if (derivedSpecimenOrderItem.getDistributedItem() != null
					&& derivedSpecimenOrderItem.getDistributedItem().getQuantity() != null)
			{
				this.values.put(assignQty, derivedSpecimenOrderItem.getDistributedItem()
						.getQuantity().toString());

			}

			this.values.put(actualSpecimenClass, derivedSpecimenOrderItem.getParentSpecimen()
					.getSpecimenClass());
			this.values.put(actualSpecimenType, derivedSpecimenOrderItem.getParentSpecimen()
					.getSpecimenType());
			this.requestForDropDownMap.put(specimenList, childrenSpecimenListToDisplay);
		}
		else if (orderItem instanceof PathologicalCaseOrderItem)
		{
			final PathologicalCaseOrderItem pathologicalCaseOrderItem = (PathologicalCaseOrderItem) orderItem;
			this.values.put(requestedItem, pathologicalCaseOrderItem.getSpecimenCollectionGroup()
					.getSurgicalPathologyNumber());
			//Fetching requestFor list
			final List totalSpecimenColl = OrderingSystemUtil.getAllSpecimensForPathologicalCases(
					pathologicalCaseOrderItem.getSpecimenCollectionGroup(),
					pathologicalCaseOrderItem);
			final Iterator i = totalSpecimenColl.iterator();
			while (i.hasNext())
			{//	Ajax  conditions
				totalSpecimenListInRequestForDropDown.add(i.next());
			}
			final List specimenListToDisplay = OrderingSystemUtil.getNameValueBeanList(
					totalSpecimenColl, null);
			Logger.out.debug("size of specimenListToDisplay :::" + specimenListToDisplay.size());
			this.requestForDropDownMap.put(specimenList, specimenListToDisplay);
			this.values.put(specimenCollGrpId, pathologicalCaseOrderItem
					.getSpecimenCollectionGroup().getId().toString());
			if (totalSpecimenColl.size() != 0)
			{
				final Specimen spec = ((Specimen) totalSpecimenColl.get(0));

				final Collection col = spec.getConsentTierStatusCollection();
				final Iterator itr = col.iterator();
				if (itr.hasNext())
				{
					this.values.put(consentVerificationkey, Constants.VIEW_CONSENTS);
				}
				else
				{
					this.values.put(consentVerificationkey, Constants.NO_CONSENTS);
				}

				this.values.put(requestFor, spec.getId());
				this.values.put(selectedSpecimenTypeKey, spec.getSpecimenType());
				this.values.put(selectedSpecimenQuantityUnit, OrderingSystemUtil.getUnit(spec));
				this.values.put(selectedSpecimenQuantity, spec.getAvailableQuantity().toString());

			}
			else
			{
				this.values.put(requestFor, "#");
				this.values.put(selectedSpecimenTypeKey, "NA");
				this.values.put(consentVerificationkey, Constants.NO_CONSENTS);
			}
			if (specimenListToDisplay.isEmpty()
					|| (pathologicalCaseOrderItem.getSpecimenClass() != null
							&& pathologicalCaseOrderItem.getSpecimenType() != null
							&& !pathologicalCaseOrderItem.getSpecimenClass().trim()
							.equalsIgnoreCase("") && !pathologicalCaseOrderItem
							.getSpecimenType().trim().equalsIgnoreCase("")))
			{
				this.values.put(instanceOf, "DerivedPathological");
			}
			else
			{
				this.values.put(instanceOf, "Pathological");
			}
			if (pathologicalCaseOrderItem.getDistributedItem() != null)
			{
				this.values.put(assignQty, pathologicalCaseOrderItem.getDistributedItem()
						.getQuantity().toString());

			}

			this.values.put(specimenClass, pathologicalCaseOrderItem.getSpecimenClass());
			this.values.put(specimenType, pathologicalCaseOrderItem.getSpecimenType());
			this.values.put(actualSpecimenClass, pathologicalCaseOrderItem.getSpecimenClass());
			this.values.put(actualSpecimenType, pathologicalCaseOrderItem.getSpecimenType());
			if (pathologicalCaseOrderItem.getStatus().equals(
					Constants.ORDER_REQUEST_STATUS_DISTRIBUTED)
					|| pathologicalCaseOrderItem
					.getStatus()
					.equals(
							assignStatus
							.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
			{
				this.values.put(canDistributeKey, Constants.TRUE);
				this.values.put(rowStatuskey, "disable");

			}
			else
			{
				this.values.put(canDistributeKey, Constants.FALSE);
				this.values.put(rowStatuskey, "enable");
			}
			this.values.put(availableQty, "");
		}
	}

	/**
	 * @return the status
	 */
	public String getStatus()
	{
		return this.status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * @return the distributionProtocolId
	 */
	public String getDistributionProtocolId()
	{
		return this.distributionProtocolId;
	}

	/**
	 * @param distributionProtocolId the distributionProtocolId to set
	 */
	public void setDistributionProtocolId(String distributionProtocolId)
	{
		this.distributionProtocolId = distributionProtocolId;
	}

	/**
	 * @return ActionErrors object
	 * @param mapping ActionMapping
	 * @param request HttpServletRequest
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		final ActionErrors errors = new ActionErrors();
		final Validator validator = new Validator();

		final String noOfRecords = (String) request.getParameter("noOfRecords");
		final int recordCount = Integer.parseInt(noOfRecords);

		if (this.getDistributionProtocolId() == null
				|| this.getDistributionProtocolId().equalsIgnoreCase("")
				|| this.getDistributionProtocolId().equalsIgnoreCase("-1"))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.distribution.protocol"));
		}

		if (this.orderName == null || this.orderName.equals(""))
		{
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.order.name"));
		}

		for (int i = 0; i < recordCount; i++)
		{
			final String consentVerificationkey = "RequestDetailsBean:" + i
			+ "_consentVerificationkey";
			final String verificationStatus = (String) this.getValue(consentVerificationkey);
			final String assignStatusKey = "RequestDetailsBean:" + i + "_assignedStatus";
			final String assignStatus = (String) this.getValue(assignStatusKey);
			final String canDistribute = (String) this.getValue("RequestDetailsBean:" + i
					+ "_canDistribute");
			this.setValue("RequestDetailsBean:" + i + "_availableQty", this.getValue(
					"RequestDetailsBean:" + i + "_availableQty").toString());
			this.setValue("RequestDetailsBean:" + i + "_requestFor", this.getValue(
					"RequestDetailsBean:" + i + "_requestFor").toString());
			this.setValue("RequestDetailsBean:" + i + "_orderItemId", this.getValue(
					"RequestDetailsBean:" + i + "_orderItemId").toString());

			if (verificationStatus != null)
			{
				if ((verificationStatus.equalsIgnoreCase(Constants.VIEW_CONSENTS) && assignStatus
						.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED))
						|| (verificationStatus.equalsIgnoreCase(Constants.VIEW_CONSENTS) && assignStatus
								.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
				{
					errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.verify.Consent"));
					break;
				}
			}
			if (canDistribute != null
					&& Constants.FALSE.equals(canDistribute)
					&& (assignStatus.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED) || assignStatus
							.equalsIgnoreCase(Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
			{
				errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
				"errors.distribution.quantity.should.equal"));
				break;
			}

		}
		//getting values from a map.
		RequestDetailsBean requestDetailsBean = null;
		DefinedArrayDetailsBean definedArrayDetailsBean = null;

		boolean specimenItem = false;
		boolean arrayDetailsItem = false;

		final MapDataParser mapDataParser = new MapDataParser("edu.wustl.catissuecore.bean");
		Collection beanObjSet = null;
		try
		{
			beanObjSet = mapDataParser.generateData(this.values);
		}
		catch (final Exception e)
		{
			RequestDetailsForm.logger.info("in request details form:" +
					" map data parser exception:" + e.getMessage(), e);
			e.printStackTrace();
		}
		final Iterator iter = beanObjSet.iterator();

		while (iter.hasNext())
		{
			final Object obj = iter.next();
			//For specimen order item.
			if (obj instanceof RequestDetailsBean)
			{
				requestDetailsBean = (RequestDetailsBean) obj;
				specimenItem = true;
			}

			//For defined array details.
			else if (obj instanceof DefinedArrayDetailsBean)
			{
				definedArrayDetailsBean = (DefinedArrayDetailsBean) obj;
				arrayDetailsItem = true;
			}
			if (specimenItem)
			{

				if (requestDetailsBean.getAssignedQty() != null
						&& !requestDetailsBean.getAssignedQty().equalsIgnoreCase(""))
				{
					if (!validator.isDouble(requestDetailsBean.getAssignedQty()))
					{
						errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.item.format",
								ApplicationProperties.getValue("itemrecord.quantity")));
						break;
					}
				}
				if (requestDetailsBean.getInstanceOf().equals("Derived")
						|| requestDetailsBean.getInstanceOf().equals("Pathological")
						|| requestDetailsBean.getInstanceOf().equals("DerivedPathological"))
				{
					try
					{
						final IFactory factory = AbstractFactoryConfig.getInstance()
						.getBizLogicFactory();
						final OrderBizLogic orderBizLogic = (OrderBizLogic) factory
						.getBizLogic(Constants.REQUEST_LIST_FILTERATION_FORM_ID);
						Specimen specimen = null;
						if (requestDetailsBean.getRowStatuskey().equals("enable")
								&& (requestDetailsBean.getAssignedStatus().equals(
										Constants.ORDER_REQUEST_STATUS_DISTRIBUTED) || requestDetailsBean
										.getAssignedStatus()
										.equals(
												Constants.ORDER_REQUEST_STATUS_DISTRIBUTED_AND_CLOSE)))
						{
							if (requestDetailsBean.getRequestFor().equals("#"))
							{
								errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
								"errors.distribution.derivedspecimen.no.specimen"));
								break;

							}
							else
							{

								specimen = (Specimen) orderBizLogic.getSpecimenObject(Long
										.valueOf(requestDetailsBean.getRequestFor()));

								if (!(specimen.getSpecimenClass().equals(
										requestDetailsBean.getClassName()) && specimen
										.getSpecimenType().equals(requestDetailsBean.getType())))
								{

									errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.distribution.derivedspecimen.type.class"));
									break;
								}

							}
						}

					}
					catch (final BizLogicException e)
					{
						RequestDetailsForm.logger.error("Bizlogic exception while getting " +
								"IFactory instance:" + e, e);
						e.printStackTrace();
					}
					catch (final NumberFormatException e)
					{
						RequestDetailsForm.logger.error("RequestDetailsForm.java"+e, e);
						e.printStackTrace();
					}
				}
				else if (arrayDetailsItem)
				{
					if (definedArrayDetailsBean.getAssignedQuantity() != null
							&& !definedArrayDetailsBean.getAssignedQuantity().equalsIgnoreCase(""))
					{
						if (!validator.isDouble(definedArrayDetailsBean.getAssignedQuantity()))
						{
							errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
									"errors.item.format", ApplicationProperties
									.getValue("itemrecord.quantity")));
							break;
						}
					}

				}
			}
		}
		return errors;
	}

	/**
	 * @return the tabIndex
	 */
	public int getTabIndex()
	{
		return this.tabIndex;
	}

	/**
	 * @param tabIndex the tabIndex to set
	 */
	public void setTabIndex(int tabIndex)
	{
		this.tabIndex = tabIndex;
	}

	/**
	 * @return the requestForDropDownMap
	 */
	public Map getRequestForDropDownMap()
	{
		return this.requestForDropDownMap;
	}

	/**
	 * @param requestForDropDownMap the requestForDropDownMap to set
	 */
	public void setRequestForDropDownMap(Map requestForDropDownMap)
	{
		this.requestForDropDownMap = requestForDropDownMap;
	}

	/**
	 * @return the specimenId
	 */
	public String getSpecimenId()
	{
		return this.specimenId;
	}

	/**
	 * @param specimenId the specimenId to set
	 */
	public void setSpecimenId(String specimenId)
	{
		this.specimenId = specimenId;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.common.actionForm.IValueObject#setAllValues(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setAllValues(AbstractDomainObject arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @param requestedDate the requestedDate to set
	 */
	public void setRequestedDate(String requestedDate)
	{
		this.requestedDate = requestedDate;
	}

	/**
	 * @return the requestedDate
	 */
	public String getRequestedDate()
	{
		return requestedDate;
	}

}
