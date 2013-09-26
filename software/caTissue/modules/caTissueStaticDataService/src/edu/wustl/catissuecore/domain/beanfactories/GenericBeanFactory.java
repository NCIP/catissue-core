/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.domain.beanfactories;

import org.dozer.BeanFactory;

/**
 * @author Ion C. Olaru
 *         Date: 8/2/11 - 11:47 AM
 */
public class GenericBeanFactory implements BeanFactory {

    public Object createBean(Object source, Class<?> sourceClass, String targetBeanId) {

        String sourcePackage = sourceClass.getPackage().getName();
        String destinationPackage;
        String className = sourceClass.getSimpleName();

        if (sourcePackage.endsWith(".ws")) {
            destinationPackage = sourcePackage.substring(0, sourcePackage.length() - 3);
        } else {
            destinationPackage = sourcePackage + ".ws";
        }

        try {
            Class c = Class.forName(destinationPackage + "." + className);
            source = c.newInstance();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        // System.out.println(String.format(">>> srcPkg: %s, dstPkg: %s, cls: %s", sourcePackage, destinationPackage, className));


        return source;
    }

}
