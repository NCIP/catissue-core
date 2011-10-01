package edu.wustl.catissuecore.domain.dozer;

import edu.wustl.catissuecore.domain.beanfactories.GenericBeanFactory;
import org.dozer.CustomConverter;
import org.dozer.DozerBeanMapperSingletonWrapper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * User: Ion C. Olaru
 * Date: 8/14/11- 10:36 AM
 *
 * This does a collection mapping only.
 * This converter should be applied on polymorphic collections, it will work on non-polymorphic collections as well.
 *
 * it instantiates the destination collection,
 * loop through the source collection and create, map and add destination objects to destination collection
 */
public class CollectionsCustomConverter implements CustomConverter {

    public Object convert(Object dest, Object src, Class<?> destinationClass, Class<?> sourceClass) {

        if (src == null) return null;

        try {

            ArrayList destinationArray = java.util.ArrayList.class.newInstance();

            Collection collection = null;
            if (src.getClass().isArray()) {
                collection = Arrays.asList((Object[]) src);
            } else {
                collection = (Collection)src;
            }

            // this class represents the class of the object in the Array
            Class doClass = Object.class;

            for (Object collectionObject : collection) {

                if (collectionObject != null) {
                    Object destinationObject = new GenericBeanFactory().createBean(collectionObject, collectionObject.getClass(), null);
                    doClass = destinationObject.getClass();
                    DozerBeanMapperSingletonWrapper.getInstance().map(collectionObject, destinationObject);
                    destinationArray.add(destinationObject);
                }
            }


            if (src.getClass().isArray()) {
                dest = destinationArray;
            } else {
                Object[] oa = (Object[]) Array.newInstance(doClass, destinationArray.size());
                dest = destinationArray.toArray(oa);
            }

            return dest;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
