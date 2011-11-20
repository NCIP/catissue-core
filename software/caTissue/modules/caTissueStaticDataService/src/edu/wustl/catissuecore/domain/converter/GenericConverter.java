package edu.wustl.catissuecore.domain.converter;

import edu.wustl.common.domain.ws.AbstractDomainObject;
import edu.wustl.catissuecore.domain.dozer.GenericEventListener;
import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.DozerEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ion C. Olaru
 *         Date: 7/21/11 - 4:37 PM
 */
public class GenericConverter {

    private static Logger log = Logger.getLogger(GenericConverter.class);

    private GenericConverter() {
    }

    public static Object convert(Object object) {
/*
        if (object instanceof AbstractDomainObject) {
            return convert((AbstractDomainObject)object);
        }
        if (object instanceof edu.wustl.common.domain.AbstractDomainObject) {
            return convert((edu.wustl.common.domain.AbstractDomainObject)object);
        }
*/

        String pName = object.getClass().getPackage().getName();

        if (pName.endsWith(".ws")) {
            return convertWsToDomain(object, pName);
        } else {
            return convertDomainToWs(object, pName);
        }
//        throw new RuntimeException(String.format("Can't convert the object of class %s.", object.getClass()));
    }

    private static Object convertWsToDomain(Object wsAdo, String pName) {
        String className = wsAdo.getClass().getSimpleName();
        Class toClass;

        if (pName.endsWith(".ws")) {
            pName = pName.substring(0, pName.indexOf(".ws"));

            try {
                toClass = Class.forName(pName + "." + className);
                log.debug(String.format(">>> Converting [%s] into [%s]", wsAdo.getClass().getCanonicalName(), toClass));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("Wrong package for the provided class. It should be part of *.ws package.");
        }

        DozerBeanMapper dbm = ((DozerBeanMapper)DozerBeanMapperSingletonWrapper.getInstance());
        if (dbm.getEventListeners().size() == 0) {
            List<DozerEventListener> del = new ArrayList<DozerEventListener>();
            del.add(new GenericEventListener());
            dbm.setEventListeners(del);
        }
        return DozerBeanMapperSingletonWrapper.getInstance().map(wsAdo, toClass);
    }

    private static Object convertDomainToWs(Object ado, String pName) {

        Object wsAdo;
        Long id;

        try {
            Method m = ado.getClass().getMethod("getId");
            Object objectId = m.invoke(ado);
            if (objectId == null)
                throw new RuntimeException("Object was NOT saved on caTissue/caCore.");

            id = (Long)objectId;

            pName += ".ws";
            String className = ado.getClass().getSimpleName();
            Class toClass;

            toClass = Class.forName(pName + "." + className);
            wsAdo = toClass.newInstance();

            // set the ID
            String methodName = "setIdentifier";

            m = toClass.getMethod(methodName, java.lang.Long.TYPE);
            m.invoke(wsAdo, id.longValue());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return wsAdo;
    }

/*
    public static edu.wustl.common.domain.AbstractDomainObject convert(AbstractDomainObject wsAdo) {

        String pName = wsAdo.getClass().getPackage().getName();
        String className = wsAdo.getClass().getSimpleName();
        Class toClass;

        if (pName.endsWith(".ws")) {
            pName = pName.substring(0, pName.indexOf(".ws"));

            try {
                toClass = Class.forName(pName + "." + className);
                log.debug(String.format(">>> Converting [%s] into [%s]", wsAdo.getClass().getCanonicalName(), toClass));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("Wrong package for the provided class. It should be part of *.ws package.");
        }

        DozerBeanMapper dbm = ((DozerBeanMapper)DozerBeanMapperSingletonWrapper.getInstance());
        if (dbm.getEventListeners().size() == 0) {
            List<DozerEventListener> del = new ArrayList<DozerEventListener>();
            del.add(new GenericEventListener());
            dbm.setEventListeners(del);
        }
        return (edu.wustl.common.domain.AbstractDomainObject)DozerBeanMapperSingletonWrapper.getInstance().map(wsAdo, toClass);
    }

    public static AbstractDomainObject convert(edu.wustl.common.domain.AbstractDomainObject ado) {

        if (ado.getId() == null) {
            throw new RuntimeException("Object was NOT saved on caTissue/caCore.");
        }

        // Only the object and it's ID needed.
        AbstractDomainObject wsAdo = null;
        String pName = ado.getClass().getPackage().getName() + ".ws";
        String className = ado.getClass().getSimpleName();
        Class toClass;
        try {
            toClass = Class.forName(pName + "." + className);
            wsAdo = (AbstractDomainObject)toClass.newInstance();
            // set the ID
            String methodName = "setIdentifier";

            Method m = toClass.getMethod(methodName, java.lang.Long.TYPE);
            m.invoke(wsAdo, ado.getId().longValue());

                //
            // log.debug(String.format(">>> Converting [%s] into [%s]", ado.getClass().getCanonicalName(), toClass));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        // return (AbstractDomainObject)DozerBeanMapperSingletonWrapper.getInstance().map(ado, toClass);
        return wsAdo;
    }
*/
}
