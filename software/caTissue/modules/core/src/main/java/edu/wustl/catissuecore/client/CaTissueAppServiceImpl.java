
package edu.wustl.catissuecore.client;

import java.util.List;
import java.util.Map;

import edu.wustl.bulkoperator.appservice.AbstractBulkOperationAppService;
import edu.wustl.bulkoperator.util.BulkOperationException;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.common.exception.ApplicationException;

public class CaTissueAppServiceImpl extends AbstractBulkOperationAppService
{

	private CaCoreAppServicesDelegator appService;
	private String userName;

	public CaTissueAppServiceImpl(boolean isAuthenticationRequired, String userName, String password)
			throws BulkOperationException, Exception
	{
		super(isAuthenticationRequired, userName, password);
	}

	@Override
	public void authenticate(String userName, String password) throws BulkOperationException
	{
		try
		{
			if (isAuthRequired && password != null)
			{
				if (!appService.authenticate(userName, password))
				{
					throw new BulkOperationException(
							"Could not login with given username/password.Please check the credentials");
				}
			}
			this.userName = userName;

		}
		catch (Exception appExp)
		{
			throw new BulkOperationException(appExp.getMessage(), appExp);
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
	protected Object insertObject(Object domainObject) throws Exception
	{
		try
		{
			Object returnedObject = appService.insertObject(userName, domainObject);
			return returnedObject;
		}
		catch (ApplicationException appExp)
		{
			throw appExp;
		}
		catch (Exception exp)
		{
			throw exp;
		}
	}

	@Override
	protected Object searchObject(Object str) throws Exception
	{
		Object returnedObject = null;
		try
		{
			String hql = (String) str;
			List result = AppUtility.executeQuery(hql);

			if (!result.isEmpty())
			{
				returnedObject = result.get(0);
			}
		}
		catch (Exception appExp)
		{
			throw new Exception(appExp.getMessage(), appExp);
		}
		return returnedObject;
	}

	@Override
	protected Object updateObject(Object domainObject) throws Exception
	{
		try
		{
			Object returnedObject = appService.updateObject(userName, domainObject);
			return returnedObject;
		}
		catch (ApplicationException appExp)
		{
			throw appExp;
		}
		catch (Exception exp)
		{
			throw exp;
		}
	}

	@Override
	protected Long hookStaticDynExtObject(Object arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long insertData(String arg0, Map<String, Object> arg1)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long insertDEObject(String arg0, String arg1,
			Map<String, Object> arg2) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}