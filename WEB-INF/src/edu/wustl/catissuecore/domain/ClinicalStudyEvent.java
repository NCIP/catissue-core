/**
 * 
 */
package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * @author srinivasarao_vassadi
 * @hibernate.class table="CATISSUE_CLINICAL_STUDY_EVENT"
 */
public class ClinicalStudyEvent extends AbstractDomainObject {

    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * System generated unique id.
     */
    protected Long id;
    
    protected String collectionPointLabel;
    
    protected Double studyCalendarEventPoint;
  
    protected ClinicalStudy clinicalStudy;
    
    protected Collection eventEntryCollection = new HashSet();
    /**
     * @return the clinicalStudy
     * @hibernate.many-to-one column="CLINICAL_STUDY_ID" class="edu.wustl.catissuecore.domain.ClinicalStudy"
     * constrained="true" 
     */
    public ClinicalStudy getClinicalStudy() {
        return clinicalStudy;
    }

    /*
     * @param clinicalStudy the clinicalStudy to set
    */ 
    public void setClinicalStudy(ClinicalStudy clinicalStudy) {
        this.clinicalStudy = clinicalStudy;
    }

    /**
     * @return the collectionPointLabel
     * @hibernate.property name="collectionPointLabel" column="COLLECTION_POINT_LABEL" type="string"
     * length="255"
     *  
     */   
    public String getCollectionPointLabel() {
        return collectionPointLabel;
    }

    /**
     * @param collectionPointLabel the collectionPointLabel to set
     */
    public void setCollectionPointLabel(String collectionPointLabel) {
        this.collectionPointLabel = collectionPointLabel;
    }

    /**
     * @return the studyCalendarEventPoint
     * @hibernate.property name="studyCalendarEventPoint" column="EVENT_POINT" type="double"
     * length="10"
     */
    public Double getStudyCalendarEventPoint() {
        return studyCalendarEventPoint;
    }

    /**
     * @param studyCalendarEventPoint the studyCalendarEventPoint to set
     */
    public void setStudyCalendarEventPoint(Double studyCalendarEventPoint) {
        this.studyCalendarEventPoint = studyCalendarEventPoint;
    }



    /**
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_STUDY_EVENT_SEQ"
     */
    public Long getId()
    {
        return id;
    }
    
    
    /**
     * @return the eventEntryCollection
     * @hibernate.set name="eventEntryCollection" table="CATISSUE_CLIN_STUDY_EVNT_NTRY"
     * inverse="true" cascade="save-update" lazy="true"
     * @hibernate.collection-key column="CLINICAL_STUDY_EVENT_ID"
     * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ClinicalStudyEventEntry"
     */
    public Collection getEventEntryCollection()
    {
        return eventEntryCollection;
    }

    
    /**
     * @param eventEntryCollection the eventEntryCollection to set
     */
    public void setEventEntryCollection(Collection eventEntryCollection)
    {
        this.eventEntryCollection = eventEntryCollection;
    }


    /**
     * @param id The id to set.
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    public void setAllValues(IValueObject abstractForm) throws AssignDataException
    {
        

    }

}
