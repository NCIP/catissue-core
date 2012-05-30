
package edu.wustl.catissuecore.actionForm.shippingtracking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.BaseShipment;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;
import edu.wustl.catissuecore.util.shippingtracking.Constants;
import edu.wustl.common.domain.AbstractDomainObject;

/**
 * this class holds the fields for shipment receiving form.
 */
public class ShipmentReceivingForm extends ShipmentForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5353240406627212866L;
	/**
	 * Serializable class requires Serial version id.
	 */
	//private static long serialVersionUID = 1L;
	/**
	 * specimens in shipment.
	 */
	protected List<Specimen> specimenCollection = new ArrayList<Specimen>();
	/**
	 * container in shipment.
	 */
	protected List<StorageContainer> containerCollection = new ArrayList<StorageContainer>();
	/**
	 * Sender site.
	 */
	protected String senderSiteName;
	/**
	 * Receiver site.
	 */
	protected String receiverSiteName;
	/**
	 * Sender contact person.
	 */
	protected String senderContactPersonName;
	/**
	 * Receiver contact person.
	 */
	protected String receiverContactPersonName;
	/**
	 * Indicates control comes from the page to display shipment receiving page.
	 */
	private String fromPage;

	/**
	 * @return specimens contained in shipment.
	 */
	public List<Specimen> getSpecimenCollection()
	{
		return this.specimenCollection;
	}

	/**
	 * @param specimenCollection specimens in shipment.
	 */

	public void setSpecimenCollection(List<Specimen> specimenCollection)
	{
		this.specimenCollection = specimenCollection;
	}

	/**
	 * @return containers contained in shipment.
	 */
	public List<StorageContainer> getContainerCollection()
	{
		return this.containerCollection;
	}

	/**
	 * @param containerCollection containers in shipment.
	 */
	public void setContainerCollection(List<StorageContainer> containerCollection)
	{
		this.containerCollection = containerCollection;
	}

	/**
	 * Get shipment sender site name.
	 * @return senderSiteName Shipment sender site name.
	 */
	@Override
	public String getSenderSiteName()
	{
		return this.senderSiteName;
	}

	/**
	 * Set shipment sender site name.
	 * @param senderSiteName Shipment sender site name.
	 */
	@Override
	public void setSenderSiteName(String senderSiteName)
	{
		this.senderSiteName = senderSiteName;
	}

	/**
	 * Get shipment receiver site name.
	 * @return receiverSiteName Shipment receiver site name.
	 */
	@Override
	public String getReceiverSiteName()
	{
		return this.receiverSiteName;
	}

	/**
	 * Set shipment receiver site name.
	 * @param receiverSiteName Shipment receiver site name.
	 */
	@Override
	public void setReceiverSiteName(String receiverSiteName)
	{
		this.receiverSiteName = receiverSiteName;
	}

	/**
	 * Get shipment sender contact person name.
	 * @return senderContactPersonName Shipment sender contact person name.
	 */
	public String getSenderContactPersonName()
	{
		return this.senderContactPersonName;
	}

	/**
	 * Set shipment sender contact person name.
	 * @param senderContactPersonName Shipment sender contact person name.
	 */
	public void setSenderContactPersonName(String senderContactPersonName)
	{
		this.senderContactPersonName = senderContactPersonName;
	}

	/**
	 * Get shipment receiver contact person name.
	 * @return receiverContactPersonName Shipment receiver contact person name.
	 */
	public String getReceiverContactPersonName()
	{
		return this.receiverContactPersonName;
	}

	/**
	 * Set shipment receiver contact person name.
	 * @param receiverContactPersonName Shipment receiver contact person name.
	 */
	public void setReceiverContactPersonName(String receiverContactPersonName)
	{
		this.receiverContactPersonName = receiverContactPersonName;
	}

	/**
	 * Get the controls comes from page to display shipment receiving page.
	 * @return fromPage
	 */
	public String getFromPage()
	{
		return this.fromPage;
	}

	/**
	 * Set the fromPage.
	 * @param fromPage the string containing page name.
	 */
	public void setFromPage(String fromPage)
	{
		this.fromPage = fromPage;
	}

	/**
	 * Resets the values of all the fields.
	 * Is called by the overridden reset method defined in ActionForm.
	 */
	@Override
	protected void reset()
	{
		/*	super.reset();
			this.senderSiteName = null;
			this.receiverSiteName = null;
			this.senderContactPersonName = null;
			this.receiverContactPersonName = null;
			this.specimenCollection = new ArrayList<Specimen>();
			this.containerCollection = new ArrayList<StorageContainer>();
			this.fromPage = null;*/
	}

	/**
	 * Returns the id assigned to form bean.
	 * @return shipment receiving form id.
	 */
	@Override
	public int getFormId()
	{
		return Constants.SHIPMENT_RECEIVING_FORM_ID;
	}

	/**
	 * this method overrides corresponding method of base class.
	 * @param arg0 the domain object to set.
	 */
	@Override
	public void setAllValues(AbstractDomainObject arg0)
	{
		super.setAllValues(arg0);
		// Set details - sender, receiver, senderSite, receiverSite.
		this.setSendingDetails(arg0);
	}

	/**
	 * this is the method that will be called to save
	 * the indexed properties when the form is saved.
	 * @param index the index using which the specimen object is to be retreived.
	 * @return Specimen object.
	 */
	public Specimen getSpecimenItem(int index)
	{
		// make sure that orderList is not null
		if (this.specimenCollection == null)
		{
			this.specimenCollection = new ArrayList<Specimen>();
		}
		// indexes do not come in order, populate empty spots
		while (index >= this.specimenCollection.size())
		{
			this.specimenCollection.add(new Specimen());
		}
		// return the requested item
		return this.specimenCollection.get(index);
	}

	/**
	 * this is the method that will be called to save
	 * the indexed properties when the form is saved.
	 * @param index index to retreive container from collection.
	 * @return StorageContainer object.
	 */
	public StorageContainer getContainerItem(int index)
	{
		// make sure that orderList is not null
		if (this.containerCollection == null)
		{
			this.containerCollection = new ArrayList<StorageContainer>();
		}
		// indexes do not come in order, populate empty spots
		while (index >= this.containerCollection.size())
		{
			this.containerCollection.add(new StorageContainer());
		}
		// return the requested item
		return this.containerCollection.get(index);
	}

	/**
	 * Set the shipment's necessary details.
	 * @param arg0 the object to set.
	 */
	private void setSendingDetails(AbstractDomainObject arg0)
	{
		if (arg0 instanceof Shipment)
		{
			final Shipment shipment = (Shipment) arg0;
			if (shipment.getSenderSite() != null)
			{
				this.senderSiteName = shipment.getSenderSite().getName();
			}
			else
			{
				this.senderSiteName = "";
			}
			if (shipment.getReceiverSite() != null)
			{
				this.receiverSiteName = shipment.getReceiverSite().getName();
			}
			else
			{
				this.senderSiteName = "";
			}
			if (shipment.getSenderContactPerson() != null)
			{
				this.senderContactPersonName = shipment.getSenderContactPerson().getLastName()
						+ ", " + shipment.getSenderContactPerson().getFirstName();
			}
			else
			{
				this.senderContactPersonName = "";
			}
			if (shipment.getReceiverContactPerson() != null)
			{
				this.receiverContactPersonName = shipment.getReceiverContactPerson().getLastName()
						+ ", " + shipment.getReceiverContactPerson().getFirstName();
			}
			else
			{
				this.receiverContactPersonName = "";
			}
			//			this.senderSiteName = (shipment.getSenderSite() != null ? shipment.getSenderSite().getName() : "");
			//			this.receiverSiteName = (shipment.getReceiverSite() != null ? shipment.getReceiverSite().getName() : "");
			//			this.senderContactPersonName = (shipment.getSenderContactPerson() != null ? shipment.getSenderContactPerson().getLastName() + ", " + shipment.getSenderContactPerson().getFirstName() : "");
			//			this.receiverContactPersonName = (shipment.getReceiverContactPerson() != null ? shipment.getReceiverContactPerson().getLastName() + ", " + shipment.getReceiverContactPerson().getFirstName() : "");
		}
	}

	/**
	 * this method populates the shipment content details.
	 * @param shipment object of baseshipment class.
	 */
	@Override
	protected void populateShipmentContentsDetails(BaseShipment shipment)
	{
		if (shipment.getContainerCollection() != null)
		{
			final Iterator<StorageContainer> containerIterator = shipment.getContainerCollection()
					.iterator();
			while (containerIterator.hasNext())
			{
				final StorageContainer container = containerIterator.next();
				if (container.getStorageType() != null
						&& !container.getStorageType().getName().equals(
								Constants.SHIPMENT_CONTAINER_TYPE_NAME))
				{
					this.containerCollection.add(container);
				}
				else
				{
					this.populateSpecimenDetails(container.getSpecimenPositionCollection());
				}
			}
		}
	}

	/**
	 * this method populates the specimen details.
	 * @param specimenPositionCollection the collection containing the position of specimen.
	 */
	private void populateSpecimenDetails(Collection<SpecimenPosition> specimenPositionCollection)
	{
		final Iterator<SpecimenPosition> spPosIterator = specimenPositionCollection.iterator();
		while (spPosIterator.hasNext())
		{
			final SpecimenPosition position = spPosIterator.next();
			this.specimenCollection.add(position.getSpecimen());
		}
	}
}
