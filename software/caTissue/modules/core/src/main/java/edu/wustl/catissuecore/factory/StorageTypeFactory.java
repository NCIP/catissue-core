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

import edu.wustl.catissuecore.domain.SpecimenArrayType;
import edu.wustl.catissuecore.domain.StorageType;


public class StorageTypeFactory implements InstanceFactory<StorageType>
{
	private static StorageTypeFactory storageTypeFactory;

	private StorageTypeFactory() {
		super();
	}

	public static synchronized StorageTypeFactory getInstance() {
		if(storageTypeFactory == null) {
			storageTypeFactory = new StorageTypeFactory();
		}
		return storageTypeFactory;
	}

	public StorageType createClone(StorageType t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public StorageType createObject()
	{
		StorageType storageType=new StorageType();
		initDefaultValues(storageType);
		return storageType;
	}

	public void initDefaultValues(StorageType obj)
	{
		obj.setHoldsStorageTypeCollection(new HashSet<StorageType>());
		obj.setHoldsSpecimenClassCollection(new HashSet<String>());
		obj.setHoldsSpecimenTypeCollection(new HashSet<String>());
		obj.setHoldsSpecimenArrayTypeCollection(new HashSet<SpecimenArrayType>());
	}

}
