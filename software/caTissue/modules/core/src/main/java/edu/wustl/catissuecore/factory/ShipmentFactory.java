/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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
