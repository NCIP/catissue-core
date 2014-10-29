/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-migration-tool/LICENSE.txt for details.
 */

package com.krishagni.catissueplus.bulkoperator.appservice;

import java.lang.reflect.Constructor;
import java.util.Map;

import com.krishagni.catissueplus.bulkoperator.metadata.HookingInformation;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationConstants;
import com.krishagni.catissueplus.bulkoperator.util.BulkOperationException;
import edu.wustl.common.exception.ErrorKey;

public abstract class AbstractBulkOperationAppService
{
	public AbstractBulkOperationAppService(boolean isAuthenticationRequired,
			String userName, String password) throws Exception
	{
		isAuthRequired = isAuthenticationRequired;
		initialize(userName, password);
	}

	protected transient boolean isAuthRequired = true;

	public boolean isAuthenticationRequired()
	{
		return isAuthRequired;
	}

	public static AbstractBulkOperationAppService getInstance(String migrationAppClassName,
	boolean isAuthenticationRequired, String userName, String password) throws BulkOperationException
	{
		if (migrationAppClassName == null)
		{
			migrationAppClassName = BulkOperationConstants.CA_CORE_MIGRATION_APP_SERVICE;
		}
		AbstractBulkOperationAppService appService = null;
		try
		{
			Class migrationServiceTypeClass = Class.forName(migrationAppClassName);
			Class[] constructorParameters = new Class[3];
			constructorParameters[0] = boolean.class;
			constructorParameters[1] = String.class;
			constructorParameters[2] = String.class;
			Constructor constructor = migrationServiceTypeClass
					.getDeclaredConstructor(constructorParameters);
			appService = (AbstractBulkOperationAppService) constructor.newInstance(
					isAuthenticationRequired, userName, password);
		}
		catch (Exception exp)
		{
			ErrorKey errorKey = ErrorKey.getErrorKey("bulk.invalid.username.password");
			throw new BulkOperationException(errorKey, exp, "");
		}
		return appService;
	}

	abstract public void initialize(String userName, String password) throws Exception;

	abstract public void authenticate(String userName, String password) throws BulkOperationException;

	public Object insert(Object obj) throws Exception
	{
		return insertObject(obj);
	}

	public abstract Long insertDEObject(final String entityName,final Map<String, Object> dataValue,final HookingInformation hookInformationObject) throws Exception;


	public Object search(Object obj) throws Exception
	{
		return searchObject(obj);
	}

	public Object update(Object obj) throws Exception
	{
		return updateObject(obj);
	}

	public Long hookStaticDEObject(Object hookingInformationObject) throws Exception
	{
		return hookStaticDynExtObject(hookingInformationObject);
	}

	abstract protected Object insertObject(Object obj) throws Exception;

	abstract public void deleteObject(Object obj) throws Exception;

	abstract protected Object updateObject(Object obj) throws Exception;

	abstract protected Object searchObject(Object obj) throws Exception;


	abstract protected Long hookStaticDynExtObject(Object hookingInformationObject) throws Exception;

}
