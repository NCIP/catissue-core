package edu.wustl.catissuecore.domain.service;

import edu.wustl.catissuecore.domain.service.WAPIUtility;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import gov.nih.nci.system.applicationservice.ApplicationException;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;
import java.util.Calendar;

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
        gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType ex = new gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType();
        ex.setStackTrace(new StackTraceElement[0]);
        ex.setFaultString(e.getCause().toString());
        return ex;
    }

  public edu.wustl.common.domain.ws.AbstractDomainObject insert(edu.wustl.common.domain.ws.AbstractDomainObject object) throws RemoteException, QueryProcessingExceptionType {
        log.debug(">>> INSERTING: " + object.getClass());
        edu.wustl.common.domain.ws.AbstractDomainObject ado = null;
        try {
            ado = WAPIUtility.insertWsObject(object);
        } catch (gov.nih.nci.system.applicationservice.ApplicationException e) {
            throw getError(e);
        }
        log.debug(">>> INSERTED: " + object.getClass());
        return ado;
    }

  public edu.wustl.common.domain.ws.AbstractDomainObject update(edu.wustl.common.domain.ws.AbstractDomainObject object) throws RemoteException, QueryProcessingExceptionType {
        edu.wustl.common.domain.ws.AbstractDomainObject ado = null;
        try {
            ado = WAPIUtility.updateWsObject(object);
        } catch (gov.nih.nci.system.applicationservice.ApplicationException e) {
            throw getError(e);
        }
        return ado;
    }

  public edu.wustl.common.domain.ws.AbstractDomainObject disable(edu.wustl.common.domain.ws.AbstractDomainObject object) throws RemoteException, QueryProcessingExceptionType {
        edu.wustl.common.domain.ws.AbstractDomainObject ado = null;
        try {
            ado = WAPIUtility.updateWsObjectStatus(object);
        } catch (ApplicationException e) {
            throw getError(e);
        }
        if (ado == null) return object;
        try {
            ado = WAPIUtility.updateWsObject(object);
        } catch (gov.nih.nci.system.applicationservice.ApplicationException e) {
            e.printStackTrace();
            throw new RemoteException(e.getMessage(), e);
        }
        return ado;
    }

}

