package edu.wustl.catissuecore.cacore;

import gov.nih.nci.system.applicationservice.ApplicationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class CaTissueCoreDelegatorInvoker {

	public Object callDelegator(String methodName, Object domainObject) throws ApplicationException {
		return callDelegator(methodName, domainObject, null, null);
	}

	public Object callDelegator(String methodName, Object domainObject,
			Object protocolId) throws ApplicationException {
		return callDelegator(methodName, domainObject, protocolId, null);
	}

	public Object callDelegator(String methodName, Object domainObject,
			String userName) throws ApplicationException {
		return callDelegator(methodName, domainObject, null, userName);
	}

	public Object callDelegator(String methodName, Object domainObject,
			Set<Long> protocolIds, String userName) throws ApplicationException {
		return callDelegator(methodName, domainObject, (Object) protocolIds,
				userName);
	}

	public Object callDelegator(String methodName, Object domainObject,
			Object protocolId, String userName) throws ApplicationException {
		Object result = null;
		try {
			Class<?> klass = Class
					.forName("edu.wustl.catissuecore.client.CaCoreAppServicesDelegator");
			Object delegator = klass.newInstance();

			Object[] args = null;
			if (userName == null && protocolId == null) {
				args = new Object[1];
				args[0] = domainObject;
			} else if (protocolId == null && userName != null) {
				args = new Object[2];
				args[0] = domainObject;
				args[1] = userName;
			} else {
				args = new Object[3];
				args[0] = domainObject;
				args[1] = protocolId;
				args[2] = userName;
			}

			Method method = getMethod(klass, methodName);
			result = method.invoke(delegator, args);
		} catch (ClassNotFoundException e) {
			throw handleException(e);
		} catch (IllegalAccessException e) {
			throw handleException(e);
		} catch (IllegalArgumentException e) {
			throw handleException(e);
		} catch (InstantiationException e) {
			throw handleException(e);
		} catch (InvocationTargetException e) {
			throw handleException(e);
		}

		return result;
	}

	/**
	 * Gets the specified method from a Class's methods.
	 *
	 * @param objClass
	 *            object class
	 * @param methodName
	 *            method name
	 *
	 * @return method
	 */
	private Method getMethod(Class<?> klass, String methodName) {
		Method methods[] = klass.getMethods();
		Method method = null;
		for (Method m : methods) {
			if (m.getName().equals(methodName)) {
				method = m;
				break;
			}
		}
		return method;
	}

	/**
	 * Handles exception & returns wrapped application exception from specified
	 * exception.
	 *
	 * @param throwable
	 *            throwable
	 *
	 * @return application exception object wrapped with message.
	 *
	 * @see ApplicationException
	 */
	private ApplicationException handleException(Throwable throwable) {
		throwable.printStackTrace();
		String message = throwable.getMessage();
		if (message == null) {
			message = throwable.getCause().getMessage();
		}
		return new ApplicationException(message);
	}

}
