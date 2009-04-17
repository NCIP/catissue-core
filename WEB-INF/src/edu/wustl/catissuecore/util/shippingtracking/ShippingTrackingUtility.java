package edu.wustl.catissuecore.util.shippingtracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.actionForm.shippingtracking.BaseShipmentForm;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.common.beans.NameValueBean;
/**
 * this is the utility class for shipping and tracking.
 */
public class ShippingTrackingUtility
{
	/**
	 * creates the transit container.
	 * @param shipmentForm form containing all values.
	 * @return object of StorageContainer class.
	 */
	public static StorageContainer createInTransitContainer(BaseShipmentForm shipmentForm)
	{
		StorageContainer container=new StorageContainer();
		// Set storage type for the container type being created
		StorageType storageType=new StorageType();
		storageType.setName(Constants.SHIPMENT_CONTAINER_TYPE_NAME);
		container.setStorageType(storageType);
		//Set site for the container being created
		Site site=new Site();
		site.setName(Constants.IN_TRANSIT_SITE_NAME);
		container.setSite(site);
		//Set capacity, create a container of dimensions (number of specimens to be stored) X 1
		Capacity capacity=new Capacity();
		capacity.setOneDimensionCapacity(shipmentForm.getSpecimenCounter());
		capacity.setTwoDimensionCapacity(1);
		container.setCapacity(capacity);
		container.getHoldsSpecimenArrayTypeCollection();
		container.setName(Constants.IN_TRANSIT_CONTAINER_NAME_PREFIX+shipmentForm.getLabel());
		container.setSpecimenPositionCollection(new HashSet<SpecimenPosition>());
		return container;
	}
	/**
	 * creates the transit container.
	 * @param containerName string containing the name of the container.
	 * @return object of StorageContainer class.
	 */
	//Overload the method createInTransitContainer(BaseShipmentForm shipmentForm)
	public static StorageContainer createInTransitContainer(String containerName)
	{
		StorageContainer container=new StorageContainer();
		// Set storage type for the container type being created
		StorageType storageType=new StorageType();
		storageType.setName(Constants.SHIPMENT_CONTAINER_TYPE_NAME);
		container.setStorageType(storageType);
		//Set site for the container being created
		Site site=new Site();
		site.setName(Constants.IN_TRANSIT_SITE_NAME);
		container.setSite(site);
		//Set capacity, create a container of dimensions (number of specimens to be stored) X 1
		Capacity capacity=new Capacity();
		//Keep the capacity 1 initially and increment on adding a new specimen
		capacity.setOneDimensionCapacity(1);
		capacity.setTwoDimensionCapacity(1);
		container.setCapacity(capacity);
		container.setName(containerName);
		container.setSpecimenPositionCollection(new HashSet<SpecimenPosition>());
		return container;
	}
	/**
	 * gives the transit container.
	 * @param containerCollection collection of containers.
	 * @return object of StorageContainer class.
	 */
	public static StorageContainer getInTransitContainer(Collection containerCollection)
	{
		StorageContainer container=null;
		Iterator<StorageContainer> containerIterator=containerCollection.iterator();
		while(containerIterator.hasNext())
		{
			StorageContainer storageContainer=containerIterator.next();
			if(storageContainer!=null && storageContainer.getStorageType()!=null
					&& storageContainer.getStorageType().getName()!=null
					&& storageContainer.getStorageType().getName().equals(
							Constants.SHIPMENT_CONTAINER_TYPE_NAME))
			{
				container=storageContainer;
				break;
			}
		}
		return container;
	}
	/**
	 * checks for presence of specimen.
	 * @param specimenPositionCollection collection of specimen position.
	 * @param specimenLabel label of specimen to be looked for.
	 * @return true/false result of the operation.
	 */
	public static boolean isSpecimenPresent(Collection<SpecimenPosition> specimenPositionCollection,
			String specimenLabel)
	{
		boolean isPresent=false;
		Iterator<SpecimenPosition> specimenPosIterator=specimenPositionCollection.iterator();
		while(specimenPosIterator.hasNext())
		{
			SpecimenPosition position=specimenPosIterator.next();
			if(position!=null && position.getSpecimen()!=null
					&& position.getSpecimen().getLabel().equals(specimenLabel))
			{
				isPresent=true;
				break;
			}
		}
		return isPresent;
	}
	/**
	 * fetches a container from a collection of containers.
	 * @param containerCollection collection of StorageContainer objects.
	 * @param containerName name of the container to be fetched.
	 * @return the container fetched.
	 */
	public static StorageContainer getContainerFromCollection(Collection<StorageContainer> containerCollection,
			String containerName)
	{
		StorageContainer storageContainer=null;
		Iterator<StorageContainer> containerIterator=containerCollection.iterator();
		while(containerIterator.hasNext())
		{
			StorageContainer container=containerIterator.next();
			if(container!=null && container.getName()!=null
					&& container.getName().equals(containerName))
			{
				storageContainer=container;
				break;
			}
		}
		return storageContainer;
	}
	/**
	 * gets the specimen position from the collection.
	 * @param specimenPositionCollection collection of specimen position.
	 * @param fieldValue value to be searched.
	 * @return specimen position.
	 */
	public static SpecimenPosition getSpecimenPositionFromCollection(
			Collection<SpecimenPosition> specimenPositionCollection,String fieldValue)
	{
		SpecimenPosition position=null;
		Iterator<SpecimenPosition> specimenPosIterator=specimenPositionCollection.iterator();
		while(specimenPosIterator.hasNext())
		{
			SpecimenPosition specimenPosition=specimenPosIterator.next();
			if(specimenPosition.getSpecimen()!=null
					&& specimenPosition.getSpecimen().getLabel()!=null
					&& specimenPosition.getSpecimen().getLabel().equals(fieldValue))
			{
				position=specimenPosition;
				break;
			}
		}
		return position;
	}
	/**
	 * gets the site list.
	 * @param permittedSitesForUser collection of sites.
	 * @return list containing objects of NaneValueBean class.
	 */
	public static List<NameValueBean> getSitesAsNVBList(Collection<Site> permittedSitesForUser)
	{
		List<NameValueBean> siteNVBList=new ArrayList<NameValueBean>();
		if(permittedSitesForUser!=null && permittedSitesForUser.size()>0)
		{
			Iterator<Site> siteIterator=permittedSitesForUser.iterator();
			while(siteIterator.hasNext())
			{
				Site site=siteIterator.next();
				NameValueBean valueBean=new NameValueBean(site.getName(),site.getId());
				siteNVBList.add(valueBean);
			}
		}
		return siteNVBList;
	}
	/**
	 * gets the display name.
	 * @param nvbList collection of NameValueBean objects.
	 * @param value value to be checked.
	 * @return display name.
	 */
	public static Object getDisplayName(Collection<NameValueBean> nvbList, String value)
	{
		String displayName="";
		if(nvbList!=null && value!=null)
		{
			for (NameValueBean nvb : nvbList)
			{
				if(nvb.getValue().equals(value))
				{
					displayName=nvb.getName();
					break;
				}
			}
		}
		return displayName;
	}
}
