package edu.wustl.catissuecore.factory;

import java.util.HashSet;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Password;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;


public class UserFactory implements InstanceFactory<User>
{
	private static UserFactory userFactory;

	public static synchronized UserFactory getInstance() {
		if(userFactory == null) {
			userFactory = new UserFactory();
		}
		return userFactory;
	}


	public User createClone(User t)
	{
		return null;
	}

	public User createObject()
	{
		User user =new User();
		initDefaultValues(user);
		return user;
	}

	public void initDefaultValues(User user)
	{
		user.setCollectionProtocolCollection(new HashSet<CollectionProtocol>());
		user.setAssignedProtocolCollection( new HashSet<CollectionProtocol>());
		user.setPasswordCollection(new HashSet<Password>());
		user.setSiteCollection(new HashSet<Site>());
//		user.setRoleId("");
		user.setFirstTimeLogin(Boolean.FALSE);
	}

}
