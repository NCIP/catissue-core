package edu.wustl.catissuecore.domain.service;

import edu.wustl.catissuecore.cacore.CaTissueWritableAppService;
import edu.wustl.catissuecore.domain.converter.GenericConverter;
import edu.wustl.catissuecore.domain.dozer.SpringProxyResolver;
import edu.wustl.catissuecore.domain.util.PropertiesLoader;
import edu.wustl.catissuecore.domain.util.Service;
import edu.wustl.common.domain.ws.AbstractDomainObject;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;
import org.apache.log4j.Logger;
import org.globus.wsrf.security.SecurityManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ion C. Olaru Date: 7/19/11 - 2:16 PM
 */
public class WAPIUtility {

	static Set cache = new HashSet();

	private static Logger log = Logger.getLogger(GenericConverter.class);

	public static CaTissueWritableAppService service() throws Exception {
		CaTissueWritableAppService service = Service.getService(
				PropertiesLoader.getCaTissueSuperUserUsername(),
				PropertiesLoader.getCaTissueSuperUserPassword());
		// CaTissueWritableAppService service =
		// Service.getService("admin@washu.edu", "Aa_111111");
		// service.setGridIdentity();
		return service;
	}

	public static CaTissueWritableAppService service(String url)
			throws Exception {
		CaTissueWritableAppService service = Service.getService(url,
				PropertiesLoader.getCaTissueSuperUserUsername(),
				PropertiesLoader.getCaTissueSuperUserPassword());
		// CaTissueWritableAppService service =
		// Service.getService("admin@washu.edu", "Aa_111111");
		// service.setGridIdentity();
		return service;
	}

	public static String getCallerIdentity() {
		SecurityManager sm = org.globus.wsrf.security.SecurityManager
				.getManager();
		String caller = sm.getCaller();
		if ((caller == null) || (caller.equals("<anonymous>"))) {
			return null;
		} else {
			return caller;
		}
	}

	public static Object convertDomainToWs(Object ado) {
		log.debug(">>> convertDomainToWs: " + ado.getClass().getCanonicalName());
		Object wsAdo = null;

		SpringProxyResolver spr = SpringProxyResolver.getInstance();
		ado = spr.unenhanceObject(ado);

		wsAdo = GenericConverter.convert(ado);

		if (wsAdo == null) {
			log.debug(">>> Object not converted.");
		} else {
			log.debug(">>> Object converted to: " + wsAdo);
			return wsAdo;
		}
		return null;
	}

	public static Object convertWsToDomain(Object wsAdo) {
		log.debug(">>> convertWsToDomain: " + wsAdo.getClass().getName());
		Object ado = null;

		ado = GenericConverter.convert(wsAdo);

		if (ado == null) {
			log.debug(">>> Object not converted.");
		} else {
			log.debug(">>> Object converted to: " + ado);
			return ado;
		}
		return null;
	}

	/**
	 * Exceptions cauth inside this method can be skipped since there are
	 * objects which just won't have the getId(), setId() methods.
	 * */
	public static void nullifyFieldValue(Object o, String setterName,
			String getterName, Class type, Object value) {

		Class c = o.getClass();
		log.debug(">>> Nullifying:" + c.getCanonicalName());
		log.debug(">>> Reflection found class: " + c);

		if (cache.contains(o))
			return;

		try {
			Method setterMethod = c.getMethod(setterName, type);
			Method getterMethod = c.getMethod(getterName);
			Class getterMethodType = getterMethod.getReturnType();
			Object getterValue = getterMethod.invoke(o);

			if (getterValue != null) {
				long getterLongValue = 1;
				if (getterMethodType == Long.class)
					getterLongValue = ((Long) getterValue).longValue();
				if (getterMethodType == Integer.class)
					getterLongValue = ((Integer) getterValue).longValue();
				if (getterLongValue == 0) {
					setterMethod.invoke(o, value);
				}
			}
			cache.add(o);

			for (Method method : c.getDeclaredMethods()) {

				boolean isCollection = false;
				Class returnType = method.getReturnType();

				if (returnType == java.util.Collection.class)
					isCollection = true;
				else {
					Class[] is = returnType.getInterfaces();
					for (Class _interface : is) {
						if (_interface == java.util.Collection.class) {
							isCollection = true;
						}
					}
				}

				// go deeeper do the same to all collections
				if (isCollection) {
					log.debug(">>> " + method.getName() + " is a Collection");

					// get Collection elements
					Collection coll = (Collection) method.invoke(o);
					if (coll != null) {
						for (Object collectionObject : coll) {
							log.debug(">>> Processing element in the collection ...");
							nullifyFieldValue(collectionObject, setterName,
									getterName, type, value);
						}
					}
				}

			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param inputWsAdo
	 * @return
	 * @throws ApplicationException
	 */
	public static AbstractDomainObject updateWsObjectStatus(AbstractDomainObject inputWsAdo) throws ApplicationException {
		String setActivityStatusMethodName = "setActivityStatus";
		String disabledValue = "Disabled";
		Class c = inputWsAdo.getClass();
		log.debug(">>> Reflection found class: " + c);
		try {
			Method m = c.getMethod(setActivityStatusMethodName, String.class);
			m.invoke(inputWsAdo, disabledValue);
			return inputWsAdo;
		} catch (NoSuchMethodException e) {
			log.debug(c.getName() + " doesn't have " + setActivityStatusMethodName);
			return null;
		} catch (Exception e) {
			throw new ApplicationException("Couldn't invoke " + c.getName() + "." + setActivityStatusMethodName, e);
		}
	}

	/**
	 * 
	 * @param inputWsAdo
	 * @return
	 * @throws ApplicationException
	 */
	public static Object insertWsObject(AbstractDomainObject inputWsAdo) throws ApplicationException {
		log.debug(">>> insertWsObject");
		Object ado = convertWsToDomain(inputWsAdo);
		WAPIUtility.nullifyFieldValue(ado, "setId", "getId", Long.class, null);
		ado = insert(ado);
		Object outputWsAdo = convertDomainToWs(ado);
		return outputWsAdo;
	}

	public static Object updateWsObject(
			AbstractDomainObject inputWsAdo) throws ApplicationException {
		log.debug(">>> updateWsObject");
		Object ado = convertWsToDomain(inputWsAdo);
		ado = update(ado);
		Object outputWsAdo = convertDomainToWs(ado);
		return outputWsAdo;
	}

	/**
	 * PRIVATE
	 * */
	private static <T> T insert(T object) throws ApplicationException {
		log.debug(">>> insert");
		try {
			SDKQuery query = new InsertExampleQuery(object);
			SDKQueryResult result = service().executeQuery(query,
					getCallerIdentity());
			log.debug(">>> Insert result: " + result.getObjectResult());
			return (T) result.getObjectResult();
		} catch (Exception e) {
			// e.printStackTrace();
			// gov.nih.nci.cagrid.common.FaultUtil.printFault(e);
			throw new ApplicationException(e.getCause());
		}
	}

	private static <T> T update(T object) throws ApplicationException {
		log.debug(">>> update");
		try {
			SDKQuery query = new UpdateExampleQuery(object);
			SDKQueryResult result = service().executeQuery(query,
					getCallerIdentity());
			log.debug(">>> Update result: " + result.getObjectResult());
			return (T) result.getObjectResult();
		} catch (Exception e) {
			throw new ApplicationException(e.getCause());
		}
	}

	public static WAPIClient getClient(
			Catissue_cacoreConfiguration configuration) throws Exception {
		return getClient(
				configuration.getCql2QueryProcessorConfig_staticLoginUser(),
				configuration.getCql2QueryProcessorConfig_staticLoginPass());

	}

	public static WAPIClient getClient(String username, String password)
			throws Exception {
		return new WAPIClient(Service.getService(username, password));

	}
	public static WAPIClient getClient(String url, String username, String password) throws Exception {
		return new WAPIClient(Service.getService(url, username, password));

	}

}
