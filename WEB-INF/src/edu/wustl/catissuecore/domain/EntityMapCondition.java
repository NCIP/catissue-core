package edu.wustl.catissuecore.domain;



import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;


/**
 * @author shital_lawhale
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * @hibernate.class table="CATISSUE_ENTITY_MAP_CONDITIONS"
 */

public class EntityMapCondition extends AbstractDomainObject implements java.io.Serializable
{
    
    /**Unique id of the object.*/
    private Long id;      
    private Long typeId;    
    private Long staticRecordId;
    
    private EntityMap entityMap ;
    
       
    public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
    {
        // TODO Auto-generated method stub
        
    }
   
    public Long getSystemIdentifier()
    {
        // TODO Auto-generated method stub
        return id;
    }
    
    public void setSystemIdentifier(Long systemIdentifier)
    {
        this.id = systemIdentifier;
    }

    /**
     * Returns the systemIdentifier assigned to user.
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_ENTITY_MAP_CONDITION_SEQ"
     * @return Returns the systemIdentifier.
     */
    public Long getId()
    {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(Long id)
    {
        this.id = id;
    }
    
 
  
    /**
     * @return
     * @hibernate.property name="staticRecordId" column="STATIC_RECORD_ID" type="long" length="30"
     * 
     */
    public Long getStaticRecordId()
    {
        return staticRecordId;
    }
    /**
     * @param collectionProtocol The collectionProtocol to set.
     */
    public void setStaticRecordId(
            Long collectionProtocol)
    {
        this.staticRecordId = collectionProtocol;
    }
    /**
     * @return Returns the typeId.
     *  @hibernate.property name="typeId" column="TYPE_ID" type="long" length="30"
     */
    public Long getTypeId()
    {
        return typeId;
    }
    /**
     * @param typeId The typeId to set.
     */
    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    /**
     * @return 
     * @hibernate.many-to-one column="ENTITY_MAP_ID" class="edu.wustl.catissuecore.domain.EntityMap" constrained="true"
     */
    
    public EntityMap getEntityMap()
    {
        return entityMap;
    }

    
    public void setEntityMap(EntityMap entityMap)
    {
        this.entityMap = entityMap;
    }
 
}
