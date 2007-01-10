package edu.wustl.catissuecore.bizlogic.querysuite;
/*
*//**
 * @author Mandar Shidhore
 * @version 1.0
 *//*

package edu.wustl.catissuecore.processor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.wustl.cab2b.common.beans.Attribute;
import edu.wustl.cab2b.common.beans.Entity;
import edu.wustl.cab2b.common.beans.IAttribute;
import edu.wustl.cab2b.common.beans.IEntity;
import edu.wustl.cab2b.common.beans.ISemanticProperty;
import edu.wustl.cab2b.common.beans.MatchedClass;
import edu.wustl.cab2b.common.beans.SemanticProperty;
import edu.wustl.cab2b.common.util.Constants;
import edu.wustl.cab2b.server.cache.EntityCache;



public class LoadEntitiesFromCacheProcessor
{
private static final long serialVersionUID = 1234567890L;
        
   
    private static EntityCache entityCache;

    public MatchedClass search(int[] searchTarget, String[] searchString, int basedOn)
    { 
        MatchedClass resultMatchedClass = new MatchedClass();
        MatchedClass matchedClass = null;
        for (int i = 0; i < searchTarget.length; i++)
        {
            matchedClass = search(searchTarget[i], searchString, basedOn);
            if (matchedClass.getEntityCollection().isEmpty() == false)
            {
                resultMatchedClass.getEntityCollection().addAll(
                        matchedClass.getEntityCollection());
            }

            if (matchedClass.getMatchedAttributeCollection().isEmpty() == false)
            {
                resultMatchedClass.getMatchedAttributeCollection().addAll(
                        matchedClass.getMatchedAttributeCollection());
            }
        }
        return resultMatchedClass;
    }

    public MatchedClass search(int searchTarget, String[] searchString, int basedOn)
    {
        MatchedClass matchedClass = null;

        switch (searchTarget)
        {
            case Constants.CLASS :
                matchedClass = searchEntity(searchString, basedOn);
                break;
            case Constants.ATTRIBUTE :
                matchedClass = searchAttribute(searchString, basedOn);
                break;
            case Constants.PV :
                break;
        }
        return matchedClass;
        
    }
 
    private Collection createSearchEntity(String[] searchString, int basedOn)
    {
        Collection entityCollection = new HashSet();

        for (int i = 0; i < searchString.length; i++)
        {
            IEntity entity = new Entity();
            switch (basedOn)
            {
                case Constants.BASED_ON_TEXT :
                    entity.setName(searchString[i]);
                    entity.setDescription(searchString[i]);
                    break;
                case Constants.BASED_ON_CONCEPT_CODE :
                    ISemanticProperty semanticProperty = new SemanticProperty();
                    semanticProperty.setConceptCode(searchString[i]);
                    Set semanticPropCollection = new HashSet();
                    semanticPropCollection.add(semanticProperty);
                    entity.setSemanticPropertyCollection(semanticPropCollection);
                    break;
            }
            entityCollection.add(entity);
        }
        return entityCollection;
    }

    private Collection createSearchAttribute(String[] searchString, int basedOn)
    {
        Collection attributeCollection = new HashSet();
        
        for (int i = 0; i < searchString.length; i++)
        {
            IAttribute attribute = new Attribute();
            switch (basedOn)
            {
                case Constants.BASED_ON_TEXT :
                    attribute.setName(searchString[i]);
                    attribute.setDescription(searchString[i]);
                    break;
                case Constants.BASED_ON_CONCEPT_CODE :
                    ISemanticProperty semanticProperty = new SemanticProperty();
                    semanticProperty.setConceptCode(searchString[i]);
                    Collection semanticPropCollection = new HashSet();
                    semanticPropCollection.add(semanticProperty);
                    attribute.setSemanticPropertyCollection(semanticPropCollection);
                    break;
            }
            attributeCollection.add(attribute);
        }
        return attributeCollection;
    }

    private MatchedClass searchEntity(String[] searchString, int basedOn)
    {
        //        IEntity entity = createSearchEntity(searchString, basedOn);
        Collection entityCollection = createSearchEntity(searchString, basedOn);
        MatchedClass matchedClass = entityCache.getEntityOnEntityParameters(entityCollection);

        return matchedClass;
    }

    private MatchedClass searchAttribute(String[] searchString, int basedOn)
    {
        Collection attributeCollection = createSearchAttribute(searchString, basedOn);
        return entityCache.getEntityOnAttributeParameters(attributeCollection);
    }
    
}

*/