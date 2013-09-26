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

import edu.wustl.catissuecore.domain.Address;


public class AddressFactory implements InstanceFactory<Address>
{
	private static AddressFactory addressFactory;

	private AddressFactory()
	{
		super();
	}

	public static synchronized AddressFactory getInstance()
	{
		if(addressFactory==null)
		{
			addressFactory = new AddressFactory();
		}
		return addressFactory;
	}



	public Address createClone(Address obj)
	{
		Address address = createObject();
		address.setStreet(obj.getStreet());
		address.setCity(obj.getCity());
		address.setState(obj.getState());
		address.setCountry(obj.getCountry());
		address.setZipCode(obj.getZipCode());
		address.setPhoneNumber(obj.getPhoneNumber());
		address.setFaxNumber(obj.getFaxNumber());

		return address;
	}

	public Address createObject()
	{
		Address address = new Address();
		initDefaultValues(address);
		return address;
	}

	public void initDefaultValues(Address obj)
	{
	}

}
