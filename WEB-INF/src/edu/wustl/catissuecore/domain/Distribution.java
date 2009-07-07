/**
 * <p>Title: Distribution Class</p>
 * <p>Description: An event that results in transfer of a specimen from a Repository to a Laboratory.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.logger.Logger;

/**
 * An event that results in transfer of a specimen from a Repository to a Laboratory.
 * @hibernate.class table="CATISSUE_DISTRIBUTION"
 * @author Mandar Deshmukh
 */
public class Distribution extends AbstractDomainObject implements java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(Distribution.class);

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * id.
	 */
	protected Long id;
	// Change for API Search   --- Ashwin 04/10/2006
	/**
	 * New location(site) of the item.
	 */
	protected Site toSite;

	/**
	 * Original location(site) of the item.
	 */
	//protected Site fromSite = new Site();
	/**
	 * DistributionProtocol associated with this Distribution.
	 */
	protected DistributionProtocol distributionProtocol;

	/**
	 * Collection of Items distributed in this distribution.
	 */
	protected Collection distributedItemCollection = new HashSet();

	/**
	 * Defines whether this SpecimenProtocol record can be queried (Active) or not
	 * queried (Inactive) by any actor.
	 */

	protected String activityStatus;
	/**
	 * OrderDetails associated with the order_item.
	 */

	protected OrderDetails orderDetails;
	/**
	 * User who performs the event.
	 */

	protected User distributedBy;
	/**
	 * Date and time of the event.
	 */

	protected Date timestamp;
	/**
	 * Text comment on event.
	 */

	protected String comment;

	/**
	 * @return the orderDetails
	 * @hibernate.many-to-one column="ORDER_ID" class="edu.wustl.catissuecore.
	 * domain.OrderDetails" constrained="true"
	 */
	public OrderDetails getOrderDetails()
	{
		return this.orderDetails;
	}

	/**
	 * @param orderDetails the orderId to set
	 */
	public void setOrderDetails(OrderDetails orderDetails)
	{
		this.orderDetails = orderDetails;
	}

	/**
	 * Default Constructor.
	 */
	public Distribution()
	{
		super();
	}

	/**
	 * Returns System generated unique id.
	 * @return System generated unique id.
	 * @see #setId(Integer)
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_DISTRIBUTION_SEQ"
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Set the identifier.
	 * (non-Javadoc)
	 * @see edu.wustl.common.domain.AbstractDomainObject#setId(java.lang.Long)
	 * @param identifier system identifier.
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Parameterized Constructor.
	 * @param form AbstractActionForm.
	 */
	public Distribution(AbstractActionForm form)
	{
		super();
		this.setAllValues(form);
	}

	// ---- Method Section

	/**
	 * Returns the destination/target Site of the Distribution.
	 * @hibernate.many-to-one column="TO_SITE_ID"
	 * class="edu.wustl.catissuecore.domain.Site" constrained="true"
	 * @return the destination/target Site of the Distribution.
	 */
	public Site getToSite()
	{
		return this.toSite;
	}

	/**
	 * @param toSite
	 *  The toSite to set.
	 */
	public void setToSite(Site toSite)
	{
		this.toSite = toSite;
	}

	//	/**
	//	 * Returns the original/source Site of the Distribution.
	//	 * @hibernate.many-to-one column="FROM_SITE_ID"
	//	 * class="edu.wustl.catissuecore.domain.Site" constrained="true"
	//	 * @return the original/source Site of the Distribution.
	//	 */
	//	public Site getFromSite()
	//	{
	//		return fromSite;
	//	}
	//
	//	/**
	//	 * @param fromSite
	//	 *  The fromSite to set.
	//	 */
	//	public void setFromSite(Site fromSite)
	//	{
	//		this.fromSite = fromSite;
	//	}

	/**
	 * Returns the distributionProtocol of the distribution.
	 * @hibernate.many-to-one column="DISTRIBUTION_PROTOCOL_ID"
	 * class="edu.wustl.catissuecore.domain.DistributionProtocol"
	 * constrained="true"
	 * @return the distributionProtocol of the distribution.
	 */
	public DistributionProtocol getDistributionProtocol()
	{
		return this.distributionProtocol;
	}

	/**
	 * @param distributionProtocol
	 * The distributionProtocol to set.
	 */
	public void setDistributionProtocol(DistributionProtocol distributionProtocol)
	{
		this.distributionProtocol = distributionProtocol;
	}

	/**
	 * Returns the collection of DistributedItems for this Distribution.
	 * @hibernate.set name="distributedItemCollection"
	 *                table="CATISSUE_DISTRIBUTED_ITEM" cascade="save-update"
	 *                inverse="true" lazy="false"
	 * @hibernate.collection-key column="DISTRIBUTION_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.DistributedItem"
	 * @return Returns the collection of DistributedItems for this Distribution.
	 */
	public Collection getDistributedItemCollection()
	{
		return this.distributedItemCollection;
	}

	/**
	 * Returns the activityStatus.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @return Returns the activityStatus.
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * @param activityStatus The activityStatus to set.
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * @param distributedItemCollection
	 *  The distributedItemCollection to set.
	 */
	public void setDistributedItemCollection(Collection distributedItemCollection)
	{
		this.distributedItemCollection = distributedItemCollection;
	}

	/**
	 * @return Returns the user.
	 */
	public User getDistributedBy()
	{
		return this.distributedBy;
	}

	/**
	 * @param distributedBy The user to set.
	 */
	public void setDistributedBy(User distributedBy)
	{
		this.distributedBy = distributedBy;
	}

	/**
	 * @return Returns the comment.
	 */
	public String getComment()
	{
		return this.comment;
	}

	/**
	 * @param comment The comments to set.
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return Returns the timestamp.
	 */
	public Date getTimestamp()
	{
		return this.timestamp;
	}

	/**
	 * @param timestamp The timestamp to set.
	 */
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 * Set All Values.
	 * @param abstractForm IValueObject.
	 */
	@Override
	public void setAllValues(IValueObject abstractForm)
	{
		try
		{
			//super.setAllValues(abstractForm);
			//this.specimen = null;
			final DistributionForm form = (DistributionForm) abstractForm;
			try
			{
				if (SearchUtil.isNullobject(this.distributedBy))
				{
					this.distributedBy = new User();
				}
				if (SearchUtil.isNullobject(this.timestamp))
				{
					this.timestamp = Calendar.getInstance().getTime();
				}

				this.comment = form.getComments();

				this.distributedBy.setId(Long.valueOf(form.getUserId()));

				if (form.getDateOfEvent() != null && form.getDateOfEvent().trim().length() != 0)
				{
					final Calendar calendar = Calendar.getInstance();

					final Date date = CommonUtilities.parseDate(form.getDateOfEvent(),
							CommonUtilities.datePattern(form.getDateOfEvent()));
					calendar.setTime(date);
					this.timestamp = calendar.getTime();
					calendar.set(Calendar.HOUR_OF_DAY, Integer.
							parseInt(form.getTimeInHours()));
					calendar.set(Calendar.MINUTE, Integer.parseInt(form.getTimeInMinutes()));
					this.timestamp = calendar.getTime();
					//this.timestamp is added twice, if there is some exception in
					//Integer.parseInt(form.getTimeInHours()) or Integer.
					//parseInt(form.getTimeInMinutes())
					//current timestamp will be set
				}
			}
			catch (final Exception excp)
			{
				Logger.out.error(excp.getMessage(), excp);
				final ErrorKey errorKey = ErrorKey.getErrorKey("assign.data.error");
				throw new AssignDataException(errorKey, null, "Distribution.java :");
			}

			if (SearchUtil.isNullobject(this.toSite))
			{
				this.toSite = new Site();
			}

			if (SearchUtil.isNullobject(this.distributionProtocol))
			{
				this.distributionProtocol = new DistributionProtocol();
			}

			this.toSite.setId(Long.valueOf(form.getToSite()));
			//fromSite.setId(new Long(form.getFromSite()));
			this.distributionProtocol.setId(Long.valueOf(form.getDistributionProtocolId()));
			this.activityStatus = form.getActivityStatus();

			Map map = form.getValues();
			logger.debug("map " + map);
			map = this.fixMap(map);
			logger.debug("fixedMap " + map);
			final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.domain");
			final Collection itemCollectionMap = parser.generateData(map);
			final Collection finalItemCollecitonMap = new HashSet();
			final Iterator itr = itemCollectionMap.iterator();
			while (itr.hasNext())
			{
				final DistributedItem distributedItem = (DistributedItem) itr.next();
				if (distributedItem.getSpecimen() != null)
				{
					finalItemCollecitonMap.add(distributedItem);
				}
				if (distributedItem.getSpecimenArray() != null)
				{
					finalItemCollecitonMap.add(distributedItem);
				}
			}
			this.distributedItemCollection = finalItemCollecitonMap;
			logger.debug("distributedItemCollection " + this.distributedItemCollection);
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage(), excp);
		}
	}

	/**
	 * Fix Map.
	 * @param orgMap Map.
	 * @return Map.
	 */
	protected Map fixMap(Map orgMap)
	{
		final Iterator iterator = orgMap.keySet().iterator();
		final Map newMap = new HashMap();
		while (iterator.hasNext())
		{
			final String key = (String) iterator.next();
			final String value = (String) orgMap.get(key);
			if (key.endsWith("_id") || key.endsWith("uantity"))
			{
				newMap.put(key, value);
			}
		}
		return newMap;
	}

	/**
	 * Returns message label to display on success add or edit.
	 * @return String
	 */
	@Override
	public String getMessageLabel()
	{
		// Change for API Search   --- Ashwin 04/10/2006
		if (SearchUtil.isNullobject(this.distributionProtocol))
		{
			this.distributionProtocol = new DistributionProtocol();
		}
		final StringBuffer message = new StringBuffer();
		message.append(this.distributionProtocol.title + " ");
		if (this.distributedBy != null)
		{
			if (this.distributedBy.lastName != null && !this.distributedBy.lastName.equals("")
					&& this.distributedBy.firstName != null
					&& !this.distributedBy.firstName.equals(""))
			{
				message.append(this.distributedBy.lastName + "," + this.distributedBy.firstName);
			}
			if (this.distributedBy.lastName != null && !this.distributedBy.lastName.equals(""))
			{
				message.append(this.distributedBy.lastName);
			}
			if (this.distributedBy.firstName != null && !this.distributedBy.firstName.equals(""))
			{
				message.append(this.distributedBy.firstName);
			}
		}
		if (this.distributedBy != null)
		{
			message.append(this.distributedBy);
		}
		return message.toString();
	}
}