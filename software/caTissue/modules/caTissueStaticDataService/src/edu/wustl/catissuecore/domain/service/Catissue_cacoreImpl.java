package edu.wustl.catissuecore.domain.service;

import edu.wustl.catissuecore.domain.util.Service;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

/**
 * @author Ion C. Olaru
 * @created by Introduce Toolkit version 1.4
 */
public class Catissue_cacoreImpl extends Catissue_cacoreImplBase {

	private static Logger log = Logger.getLogger(Catissue_cacoreImpl.class);

	public Catissue_cacoreImpl() throws RemoteException {
		super();
	}

	private QueryProcessingExceptionType getError(Exception e) {
		e.printStackTrace();
		gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType ex = new gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType();
		ex.setStackTrace(new StackTraceElement[0]);
		ex.setFaultString(e.getCause().toString());
		return ex;
	}

	public edu.wustl.common.domain.ws.AbstractDomainObject insert(
			edu.wustl.common.domain.ws.AbstractDomainObject object)
			throws RemoteException, QueryProcessingExceptionType {

		log.debug(">>> INSERTING: " + object.getClass());
		edu.wustl.common.domain.ws.AbstractDomainObject ado = null;
		try {
			ado = getClient().insertWsObject(object);
		} catch (Exception e) {
			throw getError(e);
		}
		log.debug(">>> INSERTED: " + object.getClass());
		return ado;
	}

	public edu.wustl.common.domain.ws.AbstractDomainObject update(
			edu.wustl.common.domain.ws.AbstractDomainObject object)
			throws RemoteException, QueryProcessingExceptionType {
		edu.wustl.common.domain.ws.AbstractDomainObject ado = null;
		try {
			ado = getClient().updateWsObject(object);
		} catch (Exception e) {
			throw getError(e);
		}
		return ado;
	}

	public edu.wustl.common.domain.ws.AbstractDomainObject disable(
			edu.wustl.common.domain.ws.AbstractDomainObject object)
			throws RemoteException, QueryProcessingExceptionType {
		edu.wustl.common.domain.ws.AbstractDomainObject ado = null;
		WAPIClient client = null;
		try {
			client = getClient();
		} catch (Exception e) {
			throw getError(e);
		}
		try {
			ado = client.updateWsObjectStatus(object);
		} catch (Exception e) {
			throw getError(e);
		}
		if (ado == null)
			return object;
		try {
			ado = client.updateWsObject(object);
		} catch (Exception e) {
			throw getError(e);
		}
		return ado;
	}

	private WAPIClient getClient() throws Exception {
//		System.out.println("url: " + getConfiguration().getCql2QueryProcessorConfig_applicationHostName());
		return WAPIUtility.getClient(this.getConfiguration());
	}

}
