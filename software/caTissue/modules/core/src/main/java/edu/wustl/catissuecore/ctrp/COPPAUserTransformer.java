package edu.wustl.catissuecore.ctrp;

import edu.wustl.catissuecore.domain.Address;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.AppUtility;
import gov.nih.nci.coppa.po.Person;

public class COPPAUserTransformer implements COPPATransformer {

	public User transform(Person person) throws Exception {
		User remoteUser = new User();

		remoteUser.setFirstName(COPPAUtil.getFirstName(person));
		remoteUser.setLastName(COPPAUtil.getLastName(person));
		remoteUser.setEmailAddress(COPPAUtil.getEmailAddress(person));
		Address remoteAddress = new Address();
		remoteAddress.setStreet(COPPAUtil.getStreetAddress(person));
		remoteAddress.setCity(COPPAUtil.getCity(person));
		remoteAddress.setState(COPPAUtil.getState(person));
		remoteAddress.setCountry(COPPAUtil.getCountry(person));
		remoteAddress.setZipCode(COPPAUtil.getZipCode(person));
		remoteAddress.setPhoneNumber(COPPAUtil.getTelephoneNumber(person));
		remoteAddress.setFaxNumber(COPPAUtil.getFaxNumber(person));
		remoteUser.setAddress(remoteAddress);
		// remoteUser.setDirtyEditFlag(false);
		// remoteUser.setRemoteManagedFlag(true);
		return remoteUser;
	}

	@Override
	public Object transform(Object obj) throws Exception {
		return (transform((Person) obj));
	}

	public Object[] transformObjectGroup(Object objGroup[]) throws Exception {
		Object[] transformedObjects = null;
		if (!AppUtility.isEmpty(objGroup)) {
			transformedObjects = new Object[objGroup.length];
			int index = 0;
			for (Object obj : objGroup) {
				transformedObjects[index++] = transform((Person) obj);
			}
		}
		return transformedObjects;
	}

}
