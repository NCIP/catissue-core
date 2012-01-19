package edu.wustl.catissuecore.domain.util;

import org.apache.log4j.Logger;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Ion C. Olaru
 * Date: 1/18/12
 * This class is resetting the collections that are throwing org.hibernate.LazyInitializationException due to a
 * caTissue side hibernate approach. Also, circle references are removed to make AXIS capable of successful marshaling.
 */
public class CollectionsHandler {

    private static Logger log = Logger.getLogger(PropertiesLoader.class);

    /**
     * Get all the field declared in a class including the ones declared in all superclasses.
     * @param c Class to get the fields from
     * @return The list of class fields
     */
    private static List<Field> getFields(Class c) {
        List<Field> fields = new ArrayList<Field>();
        Field[] localFields = c.getDeclaredFields();

        if (localFields != null) {
            fields.addAll(Arrays.asList(localFields));
        }

        if (c.getSuperclass() != null) {
            List superFields = getFields(c.getSuperclass());
            if (superFields != null && superFields.size() > 0) {
                fields.addAll(superFields);
            }
        }

        return fields;
    }

    /**
     * Determines whether the given type is a collection
     * @param c Class type
     * @return true if "c" is a collection
     */
    public static boolean isCollection(Class c) {
        if (c == java.util.Collection.class || c == java.util.Set.class || c == java.util.List.class) return true;
        Class[] is = c.getInterfaces();
        for (Class _i : is) {
            if (_i == java.util.Collection.class || _i == java.util.Set.class || _i == java.util.List.class) return true;
        }
        return false;
    }

    /**
     * Determines whether the given type is a java.util.Set
     * @param c Class type
     * @return true if "c" is a collection
     */
    public static boolean isSet(Class c) {
        return (c == java.util.Set.class);
    }

    public static boolean isJavaType(Class c) {
        if (c.isPrimitive()) return true;
        if (c.getPackage().getName().equals("java.lang")) return true;
        if (c.getPackage().getName().equals("java.util")) return true;
        return false;
    }

    /**
     * Does the processing described in the class header for a particular object
     * First it check the collections them moves to the other types which may have their own collections.
     * IT IS IMPORTANT TO PROCESS COLLECTIONS FIRST SINCE THEY ARE PART OF java.util PACKAGE WHICH IS
     * EXCLUDED IN THE else BRANCH
     * @param o Object to be processed
     * @param objectCache cache of object, to avoid circular references
     */
    public static void handleObject(Object o, Set objectCache) {

        if (o == null) return;
        if (objectCache.contains(o)) {
            return;
        }
        objectCache.add(o);

        List<Field> fields = getFields(o.getClass());

        for (Field f : fields) {
            if (isCollection(f.getType())) {
                Collection c = (Collection)doInvokeGetter(f, o, true);
                log.debug(">>> COLLECTION RECEIVED: " + f.getName());
                handleCollection(c, objectCache);
            } else {
                if (!isJavaType(f.getType())) {
                    log.debug(">>> NOW PROCESSING: " + f.getName() + " OF TYPE " + f.getType());
                    // Handle the other fields that may have their own Collections fields
                    handleObject(doInvokeGetter(f, o, false), objectCache);
                }
            }
        }
    }

    /**
     * Invokes the getter, if it exists, of the given field for a particular class and suppress any thrown exceptions.
     * Fields for which getter throws an org.hibernate.LazyInitializationException are reset to an empty java.util.ArrayList
     * @param o Object which class which contains the field
     * @param f Field to invoke the setter for
     * @param invokeSize whether to invoke the method on the collection to handle Lazy Init
     * @return the object returned by the getter method
     */
    public static Object doInvokeGetter(Field f, Object o, boolean invokeSize) {
        PropertyDescriptor pd = null;
        try {
            pd = new PropertyDescriptor(f.getName(), o.getClass());
            Method readMethod = pd.getReadMethod();
            log.debug(">>> GETTER: " + readMethod);
            Object getterResult = readMethod.invoke(o);
            if (invokeSize) {
                Method sizeMethod = getterResult.getClass().getMethod("size");
                sizeMethod.invoke(getterResult);
            }
            return getterResult;
        } catch (Throwable e) {
            Method writeMethod = pd.getWriteMethod();
            try {
                log.debug(">>> SETTER: " + writeMethod);
                if (isSet(f.getType())) writeMethod.invoke(o, new java.util.HashSet());
                else writeMethod.invoke(o, new java.util.ArrayList());
            } catch (Exception e1) {
                throw new RuntimeException(String.format("Exception on invoking method '%s' on class '%s'.", f.getName(), o.getClass()), e1);
            }
        }
        return null;
    }

    /**
     * Does the processing described in the class header for a particular collection of objects
     * @param c Collection to be processed
     */
    public static void handleCollection(Collection c, Set objectCache) {
        if (c == null || c.size() == 0) return;
        log.debug(">>> COLLECTION: " + c.getClass().getName());
        for (Object o : c) {
            handleObject(o, objectCache);
        }
    }

}
