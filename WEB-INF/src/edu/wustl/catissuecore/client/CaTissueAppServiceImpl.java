
package edu.wustl.catissuecore.client;

import java.util.List;

import edu.wustl.bulkoperator.appservice.MigrationAppService;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.util.global.AppUtility;

public class CaTissueAppServiceImpl extends MigrationAppService
{

	private CaCoreAppServicesDelegator appService;
	private String userName;

	public CaTissueAppServiceImpl(boolean isAuthenticationRequired, String userName, String password)
			throws BulkOperationException
	{
		super(isAuthenticationRequired, userName, password);
	}

	@Override
	public void authenticate(String userName, String password) throws BulkOperationException
	{
		try
		{
			if(isAuthenticationRequired && password!=null)
			{
				if(!appService.delegateLogin(userName, password))
				{
					throw new BulkOperationException("Could not login with given username/password.Please check the credentials");
				}
			}
			this.userName = userName;
			
		}
		catch(Exception appExp)
		{
			throw new BulkOperationException(appExp.getMessage(),appExp);
		}
	}

	@Override
	public void initialize(String userName, String password) throws BulkOperationException
	{
		appService = new CaCoreAppServicesDelegator();
		authenticate(userName, password);
	}

	@Override
	public void deleteObject(Object arg0) throws BulkOperationException
	{
	}

	@Override
	protected Object insertObject(Object domainObject) throws BulkOperationException
	{
		try
		{
			Object returnedObject = appService.delegateAdd(userName, domainObject);
			return returnedObject;
		}
		catch(Exception appExp)
		{
			//appExp.printStackTrace();
			throw new BulkOperationException(appExp.getMessage(), appExp);	
		}
	}

	@Override
	protected Object searchObject(Object str) throws BulkOperationException
	{
		Object returnedObject = null;
		try
		{
			String hql = (String)str;
			List result = AppUtility.executeQuery(hql);
			
			if(!result.isEmpty())
			{
				returnedObject = result.get(0);
			}
		}
		catch(Exception appExp)
		{
			//appExp.printStackTrace();
			throw new BulkOperationException(appExp.getMessage(), appExp);	
		}
		return returnedObject;
	}

	@Override
	protected Object updateObject(Object domainObject) throws BulkOperationException
	{
		try
		{
			Object returnedObject = appService.delegateEdit(userName, domainObject);
			return returnedObject;
		}
		catch(Exception appExp)
		{
			//appExp.printStackTrace();
			throw new BulkOperationException(appExp.getMessage(), appExp);	
		}
	}

}
