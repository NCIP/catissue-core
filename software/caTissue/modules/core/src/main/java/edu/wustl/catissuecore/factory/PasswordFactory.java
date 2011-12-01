package edu.wustl.catissuecore.factory;

import java.util.Date;

import edu.wustl.catissuecore.passwordutil.Password;


public class PasswordFactory implements InstanceFactory<Password>
{
	private static PasswordFactory passwordFactory;

	private PasswordFactory() {
		super();
	}

	public static synchronized PasswordFactory getInstance() {
		if(passwordFactory == null) {
			passwordFactory = new PasswordFactory();
		}
		return passwordFactory;
	}

	public Password createClone(Password password)
	{
		Password pass=createObject();
		pass.setPassword(password.getPassword());
		pass.setUser(password.getUser());
		pass.setUpdateDate(new Date());
		return pass;
	}

	public Password createObject()
	{
		Password password=new Password();
		return password;
	}

	public void initDefaultValues(Password password)
	{
	}

}
