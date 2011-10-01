package edu.wustl.catissuecore.factory;

import java.util.HashSet;

import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.Shipment;

public class ShipmentFactory implements InstanceFactory<Shipment>
{
	private static ShipmentFactory shipmentFactory;

	private ShipmentFactory()
	{
		super();
	}

	public static synchronized ShipmentFactory getInstance()
	{
		if(shipmentFactory==null){
			shipmentFactory = new ShipmentFactory();
		}
		return shipmentFactory;
	}

	public Shipment createClone(Shipment obj)
	{
		Shipment shipment = createObject();
		return shipment;
	}

	public Shipment createObject()
	{
		Shipment shipment = new Shipment();
		initDefaultValues(shipment);
		return shipment;
	}

	public void initDefaultValues(Shipment obj)
	{
		obj.setContainerCollection(new HashSet<StorageContainer>());
	}
}
