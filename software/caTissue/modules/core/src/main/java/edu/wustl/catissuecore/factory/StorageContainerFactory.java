package edu.wustl.catissuecore.factory;

import java.util.HashSet;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;


public class StorageContainerFactory implements InstanceFactory<StorageContainer>
{
	private static StorageContainerFactory storageContainerFactory;

	protected StorageContainerFactory()
	{
		super();
	}

	public static synchronized StorageContainerFactory getInstance()
	{
		if(storageContainerFactory==null){
			storageContainerFactory = new StorageContainerFactory();
		}
		return storageContainerFactory;
	}

	public StorageContainer createClone(StorageContainer obj)
	{
		StorageContainer sc=createObject();
		sc.setId(obj.getId());
		sc.setActivityStatus(obj.getActivityStatus());
		sc.setName(obj.getName());
		sc.setBarcode(obj.getBarcode());
		if (obj.getLocatedAtPosition() != null)
		{
			if (sc.getLocatedAtPosition() == null)
			{
				InstanceFactory<ContainerPosition> instFact = DomainInstanceFactory.getInstanceFactory(ContainerPosition.class);
				sc.setLocatedAtPosition(instFact.createObject());
			}
			sc.getLocatedAtPosition().setParentContainer(obj.getLocatedAtPosition()
					.getParentContainer());
			sc.getLocatedAtPosition().setPositionDimensionOne(obj.getLocatedAtPosition()
					.getPositionDimensionOne());
			sc.getLocatedAtPosition().setPositionDimensionTwo(obj.getLocatedAtPosition()
					.getPositionDimensionTwo());
			sc.getLocatedAtPosition().setOccupiedContainer(sc);
		}
		sc.setFull(obj.getFull());
		sc.setSite(obj.getSite());
		InstanceFactory<Capacity> instFact = DomainInstanceFactory.getInstanceFactory(Capacity.class);
		sc.setCapacity(instFact.createClone(obj.getCapacity()));

		sc.setStorageType(obj.getStorageType());
		sc.setTemperatureInCentigrade(obj.getTemperatureInCentigrade());
		sc.setCollectionProtocolCollection(obj.getCollectionProtocolCollection());
		sc.setHoldsStorageTypeCollection(obj.getHoldsStorageTypeCollection());
		sc.setHoldsSpecimenClassCollection(obj.getHoldsSpecimenClassCollection());
		sc.setHoldsSpecimenTypeCollection(obj.getHoldsSpecimenTypeCollection());
		sc
				.setHoldsSpecimenArrayTypeCollection(obj
						.getHoldsSpecimenArrayTypeCollection());
		return sc;
	}

	public StorageContainer createObject()
	{
		StorageContainer sc= new StorageContainer();
		initDefaultValues(sc);
		return sc;
	}

	public void initDefaultValues(StorageContainer obj)
	{
		obj.setOccupiedPositions(new HashSet<ContainerPosition>());
		obj.setCollectionProtocolCollection(new HashSet<CollectionProtocol>());
		obj.setHoldsStorageTypeCollection(new HashSet<StorageType>());
		obj.setHoldsSpecimenClassCollection(new HashSet<String>());
		obj.setHoldsSpecimenTypeCollection(new HashSet<String>());
		obj.setHoldsSpecimenArrayTypeCollection(new HashSet<SpecimenArrayType>());
		//obj.setPositionChanged(Boolean.FALSE);
	}

}
