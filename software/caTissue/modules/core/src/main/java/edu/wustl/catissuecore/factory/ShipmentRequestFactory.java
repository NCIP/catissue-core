package edu.wustl.catissuecore.factory;

import java.util.HashSet;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.shippingtracking.ShipmentRequest;

public class ShipmentRequestFactory implements InstanceFactory<ShipmentRequest>
{
	private static ShipmentRequestFactory shipmentRequestFactory;

	private ShipmentRequestFactory()
	{
		super();
	}

	public static synchronized ShipmentRequestFactory getInstance()
	{
		if(shipmentRequestFactory==null){
			shipmentRequestFactory = new ShipmentRequestFactory();
		}
		return shipmentRequestFactory;
	}

	public ShipmentRequest createClone(ShipmentRequest obj)
	{
		ShipmentRequest shipmentRequest = createObject();
		return shipmentRequest;
	}


	public ShipmentRequest createObject()
	{
		ShipmentRequest shipmentRequest =  new ShipmentRequest();
		initDefaultValues(shipmentRequest);
		return shipmentRequest;
	}

	public void initDefaultValues(ShipmentRequest obj)
	{
		obj.setContainerCollection(new HashSet<StorageContainer>());
		obj.setSpecimenCollection(new HashSet<Specimen>());
		//obj.setRequestProcessed(Boolean.FALSE);
	}
}