package edu.wustl.catissuecore.domain.dozer;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Ion C. Olaru
 *         Date: 8/1/11 - 2:30 PM
 */
public class GenericCollectionConverter {

    private GenericCollectionConverter() {
    }
    
    
    private static final String setsFile = "edu/wustl/catissuecore/domain/genericCustomConverterSets.properties";
    private static final String parentRefFile = "edu/wustl/catissuecore/domain/genericCustomConverterParentRef.properties";
    private static Map<String, Map> collections = new HashMap<String, Map>();
    private static Map<String, Map> references = new HashMap<String, Map>();

    private static Logger log = Logger.getLogger(GenericCollectionConverter.class);

    static {
        Properties pSets = new Properties();
        Properties pRefs = new Properties();
        try {
            pSets.load(GenericCollectionConverter.class.getClassLoader().getResourceAsStream(setsFile));
            pRefs.load(GenericCollectionConverter.class.getClassLoader().getResourceAsStream(parentRefFile));

            // process sets
            Enumeration en = pSets.propertyNames();
            while (en.hasMoreElements()) {
                String elName = (String)en.nextElement();
                String elValue = (String)pSets.get(elName);

                String rootClassName = elName.substring(0, elName.lastIndexOf("."));
                String rootClassMethodName = elName.substring(elName.lastIndexOf(".") + 1, elName.length());

                if (collections.get(rootClassName) == null) {
                    collections.put(rootClassName, new HashMap<String, String>());
                }
                collections.get(rootClassName).put(rootClassMethodName, elValue);
            }

            // process references
            en = pRefs.propertyNames();
            while (en.hasMoreElements()) {
                String elName = (String)en.nextElement();
                String elValue = (String)pRefs.get(elName);

                String rootClassName = elName.substring(0, elName.lastIndexOf("."));
                String rootClassMethodName = elName.substring(elName.lastIndexOf(".") + 1, elName.length());

                if (references.get(rootClassName) == null) {
                    references.put(rootClassName, new HashMap<String, String>());
                }
                references.get(rootClassName).put(rootClassMethodName, elValue);
            }

        } catch (IOException e) {
            log.error(String.format(">>> Check these files to be in the class path: 1) %s, 2) %s ", setsFile, parentRefFile));
            e.printStackTrace();
        }
    }

    public static void convertCollectionTypes(Object o) {
        String className = o.getClass().getCanonicalName();
        log.debug(">>> Convert collections types for class: " + className);

        Map<String, String> items = collections.get(className);
        if (items != null) {
            for (String e : items.keySet()) {
                String methodName = e;
                String collectionType = items.get(e);

                Class klass = null;
                Class newCollectionClass = null;
                Collection newCollection = null;

                try {
                    klass = Class.forName(className);
                    newCollectionClass = Class.forName(collectionType);
                    newCollection = (Collection)newCollectionClass.newInstance();

                    Method method = klass.getMethod(methodName);
                    log.debug(">>> Found collection to be converted: " + method);

                    Collection coll = (Collection)method.invoke(o);

                    if (coll != null) {
                        for (Object collectionObject : coll) {
                            newCollection.add(collectionObject);
                        }
                        String setterMethodName = "s" + methodName.substring(1);
                        Method setterMethod = klass.getMethod(setterMethodName, Collection.class);
                        setterMethod.invoke(o, newCollection);                        
                    }


                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                }

            }
        }
    }

    public static void adjustReference(Object o) {
        String className = o.getClass().getCanonicalName();
        log.debug(">>> Adjust references for class: " + className);

        Map<String, String> items = references.get(className);
        if (items != null) {
            for (String e : items.keySet()) {
                String methodName = e;
                Boolean enabled = Boolean.parseBoolean(items.get(e));

                if (!enabled) {
                    log.debug(">>> Method skipped: " + methodName);
                    continue;
                }

                Class klass = null;

                try {
                    klass = Class.forName(className);

                    Method method = klass.getMethod(methodName);
                    log.debug(">>> Found collection to be processed: " + method);

                    Collection coll = (Collection)method.invoke(o);

                    if (coll != null) {
                        for (Object collectionObject : coll) {                            
                            Method setter = findSetter(collectionObject.getClass(), o, collectionObject);
                            if (setter==null) {
                            	log.error("Unable to find a setter for "+o.getClass().getCanonicalName()+" in "+collectionObject.getClass());
                            } else {
                            	setter.invoke(collectionObject, o);
                                log.debug(String.format(">>> %s injected into %s", o.getClass(), collectionObject.getClass().getCanonicalName()));	
                            }                            
                        }
                    }

                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                } catch (NoSuchMethodException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                    throw new RuntimeException(e1);
                }

            }
        }

    }
    
	private static Method findSetter(Class clazz, Object o, Object collectionObject) {
		String simpleClassName = o.getClass().getSimpleName();
		String setterName = "set" + simpleClassName;
		Method method = ReflectionUtils.findMethod(clazz, setterName,
				new Class[] { o.getClass() });
		if (method == null) {
			log.warn(setterName + " was not found in " + clazz
					+ ". Looking further...");
			setterName = setterName + "_" + clazz.getSimpleName();
			method = ReflectionUtils.findMethod(clazz, setterName,
					new Class[] { o.getClass() });
			if (method == null) {
				log.warn(setterName
						+ " was not found in "
						+ clazz
						+ " either. Looking for any 'set' method that takes a single argument assignable to "
						+ simpleClassName + "...");
				for (Method m : ReflectionUtils.getAllDeclaredMethods(clazz)) {
					if (m.getName().startsWith("set")
							&& m.getParameterTypes().length == 1
							&& void.class.equals(m.getReturnType())) {
						Class paramType = m.getParameterTypes()[0];
						if (paramType.isAssignableFrom(o.getClass())) {
							log.warn("Found our setter: "+m.getName());
							method = m;
							break;
						}
					}
				}
			}
		}
		return method;

	}

	public static void setBackReference(Object value, Object destination) {
		try {
			final String name = destination.getClass().getSimpleName();
			BeanUtils.setProperty(value, name.substring(0, 1).toLowerCase()+name.substring(1), destination);
		} catch (Exception e) {
			log.warn("Unable to set property "+destination.getClass().getSimpleName()+" on "+value.getClass().getSimpleName());
		} 
	}

}
