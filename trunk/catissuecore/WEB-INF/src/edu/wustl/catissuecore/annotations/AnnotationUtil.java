/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author 
 *@version 1.0
 */

package edu.wustl.catissuecore.annotations;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.catissuecore.action.annotations.AnnotationConstants;
import edu.wustl.catissuecore.domain.EntityMap;
import edu.wustl.catissuecore.domain.EntityMapCondition;
import edu.wustl.catissuecore.util.CatissueCoreCacheManager;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.global.Constants;

public class AnnotationUtil

{

    /**
     * This method updates module map by parsing xml file
     * @param xmlFileName file to be parsed
     * @return dataType Map
     * @throws DataTypeFactoryInitializationException on Exception
     */
    public static Map map = new HashMap();

    public final List<NameValueBean> populateStaticEntityList(
            String xmlFileName, String displayNam)
            throws DataTypeFactoryInitializationException
    {
        List list = new ArrayList();

        SAXReader saxReader = new SAXReader();
        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(xmlFileName);

        Document document = null;

        try
        {
            document = saxReader.read(inputStream);
            Element className = null;
            Element displayName = null;
            Element conditionInvoker = null;

            Element primitiveAttributesElement = document.getRootElement();
            Iterator primitiveAttributeElementIterator = primitiveAttributesElement
                    .elementIterator("static-entity");

            Element primitiveAttributeElement = null;

            while (primitiveAttributeElementIterator.hasNext())
            {
                primitiveAttributeElement = (Element) primitiveAttributeElementIterator
                        .next();

                className = primitiveAttributeElement.element("name");
                displayName = primitiveAttributeElement.element("displayName");
                conditionInvoker = primitiveAttributeElement
                        .element("conditionInvoker");
                list.add(new NameValueBean(displayName.getStringValue(),
                        className.getStringValue()));

                if (displayNam != null)
                {
                    if (className.getText().equals(displayNam))
                    {
                        map.put("name", className.getText());
                        map.put("displayName", displayName.getText());
                        map.put("conditionInvoker", conditionInvoker.getText());
                    }
                }

            }
        }
        catch (DocumentException documentException)
        {
            throw new DataTypeFactoryInitializationException(documentException);
        }

        return list;
    }
    
    public Collection getEntityMapConditionsCollection(String[] conditions,
            EntityMap entityMapObj)
    {
        Collection entityMapConditionCollection = new HashSet();
        if(conditions!=null)    
        for (int i = 0; i < conditions.length; i++)
        {
           boolean check = checkForAll(conditions);
           if(!check)
            if (!conditions[i]
                    .equals(new Integer(Constants.SELECT_OPTION_VALUE)
                            .toString())
                    && !conditions[i].equals(Constants.ALL))
            {
                EntityMapCondition entityMapCondition = new EntityMapCondition();
                entityMapCondition.setEntityMap(entityMapObj);
                entityMapCondition.setStaticRecordId(new Long(conditions[i]));
                entityMapCondition.setTypeId(new Long(4));
                entityMapConditionCollection.add(entityMapCondition);
            }
        }
        return entityMapConditionCollection;

    }
    
    private boolean checkForAll(String[] conditions)
    {
        if(conditions!=null)    
            for (int i = 0; i < conditions.length; i++)
            {
                if (conditions[i].equals(Constants.ALL))
                    return true;
            }
        
        return false;
    }
    /**
     * @param annotationForm 
     * @throws DynamicExtensionsApplicationException 
     * @throws DynamicExtensionsSystemException 
     * 
     */
    public static List getSystemEntityList() throws DynamicExtensionsSystemException,
            DynamicExtensionsApplicationException
    {
        List<NameValueBean> systemEntityList = new ArrayList<NameValueBean>();
        AnnotationUtil util = new AnnotationUtil();
        List<NameValueBean> staticEntityInformationList = util.populateStaticEntityList("StaticEntityInformation.xml",null);
        CatissueCoreCacheManager cacheManager = CatissueCoreCacheManager
                .getInstance();
        if (cacheManager
                .getObjectFromCache(AnnotationConstants.STATIC_ENTITY_LIST) == null)
        {
            systemEntityList.add(new NameValueBean(Constants.SELECT_OPTION,
                    Constants.SELECT_OPTION_VALUE));
            if (staticEntityInformationList != null
                    && !staticEntityInformationList.isEmpty())
            {
                Iterator listIterator = staticEntityInformationList.iterator();
                while (listIterator.hasNext())
                {
                    NameValueBean nameValueBean = (NameValueBean) listIterator
                            .next();
                    systemEntityList.add(new NameValueBean(nameValueBean
                            .getName(), Utility.getEntityId(nameValueBean
                            .getValue())));
                }
            }
            cacheManager.addObjectToCache(
                    AnnotationConstants.STATIC_ENTITY_LIST,
                    (Serializable) systemEntityList);
        }
        else
        {
            systemEntityList = (List<NameValueBean>) cacheManager
                    .getObjectFromCache(AnnotationConstants.STATIC_ENTITY_LIST);
        }
        return systemEntityList;
    }

    

}
