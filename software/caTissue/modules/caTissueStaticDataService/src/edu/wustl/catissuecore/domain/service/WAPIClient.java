/**
 * 
 */
package edu.wustl.catissuecore.domain.service;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import edu.wustl.catissuecore.cacore.CaTissueWritableAppService;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;
import gov.nih.nci.system.query.example.InsertExampleQuery;
import gov.nih.nci.system.query.example.UpdateExampleQuery;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class WAPIClient {

	private static Logger log = Logger.getLogger(WAPIClient.class);

	private CaTissueWritableAppService service;

	public WAPIClient(CaTissueWritableAppService service) {
		this.service = service;
	}

	public CaTissueWritableAppService getService() {
		return service;
	}

	public void setService(CaTissueWritableAppService service) {
		this.service = service;
	}

	/**
	 * 
	 * @param inputWsAdo
	 * @return
	 * @throws ApplicationException
	 */
	public Object updateWsObjectStatus(Object inputWsAdo) throws ApplicationException {
		String setActivityStatusMethodName = "setActivityStatus";
		String disabledValue = "Disabled";
		Class c = inputWsAdo.getClass();
		log.debug(">>> Reflection found class: " + c);
		try {
			Method m = c.getMethod(setActivityStatusMethodName, String.class);
			m.invoke(inputWsAdo, disabledValue);
			return inputWsAdo;
		} catch (NoSuchMethodException e) {
			log.debug(c.getName() + " doesn't have "
					+ setActivityStatusMethodName);
			return null;
		} catch (Exception e) {
			throw new ApplicationException("Couldn't invoke " + c.getName()
					+ "." + setActivityStatusMethodName, e);
		}
	}

	/**
	 * 
	 * @param inputWsAdo
	 * @return
	 * @throws ApplicationException
	 */
	public Object insertWsObject(Object inputWsAdo) throws ApplicationException {
		log.debug(">>> insertWsObject");
		Object ado = WAPIUtility.convertWsToDomain(inputWsAdo);
		WAPIUtility.nullifyFieldValue(ado, "setId", "getId", Long.class, null);
		ado = insert(ado);
		Object outputWsAdo = WAPIUtility.convertDomainToWs(ado);
		return outputWsAdo;
	}

	/**
	 * 
	 * @param inputWsAdo
	 * @return
	 * @throws ApplicationException
	 */
	public Object updateWsObject(Object inputWsAdo)
			throws ApplicationException {
		log.debug(">>> updateWsObject");
		Object ado = WAPIUtility.convertWsToDomain(inputWsAdo);
		ado = update(ado);
		Object outputWsAdo = WAPIUtility.convertDomainToWs(ado);
		return outputWsAdo;
	}

	/**
	 * PRIVATE
	 * */
	private <T> T insert(T object) throws ApplicationException {
		log.debug(">>> insert");
		try {
			SDKQuery query = new InsertExampleQuery(object);
			SDKQueryResult result = getService().executeQuery(query,
					WAPIUtility.getCallerIdentity());
			log.debug(">>> Insert result: " + result.getObjectResult());
			return (T) result.getObjectResult();
		} catch (Exception e) {
			// e.printStackTrace();
			// gov.nih.nci.cagrid.common.FaultUtil.printFault(e);
			throw new ApplicationException(e.getCause());
		}
	}

	private <T> T update(T object) throws ApplicationException {
		log.debug(">>> update");
		try {
			SDKQuery query = new UpdateExampleQuery(object);
			SDKQueryResult result = getService().executeQuery(query,
					WAPIUtility.getCallerIdentity());
			log.debug(">>> Update result: " + result.getObjectResult());
			return (T) result.getObjectResult();
		} catch (Exception e) {
			throw new ApplicationException(e.getCause());
		}
	}

}
