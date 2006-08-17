
package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author gautam_shetty
 * @author Ashwin Gupta 
 * @hibernate.joined-subclass table="CATISSUE_SPECIMEN_ARRAY_TYPE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class SpecimenArrayType extends ContainerType
{
    
    protected String specimenClass;
    
    protected Collection specimenTypeCollection = new HashSet();
    
    public SpecimenArrayType()
    {
    }
    
    /**
     * @return Returns the specimenClass.
     * @hibernate.property name="specimenClass" type="string" column="SPECIMEN_CLASS" length="50"
     */
    public String getSpecimenClass()
    {
        return specimenClass;
    }
    
    /**
     * @param specimenClass The specimenClass to set.
     */
    public void setSpecimenClass(String specimenClass)
    {
        this.specimenClass = specimenClass;
    }
    
//    /**
//     * @return Returns the holdsSpecimenClassCollection.
//     * @hibernate.set name="holdsSpecimenClassCollection" table="CATISSUE_SPEC_ARY_TYPE_SPEC_CLASS"
//	 * cascade="save-update" inverse="false" lazy="false"
//	 * @hibernate.collection-key column="SPECIMEN_ARRAY_TYPE_ID"
//	 * @hibernate.element type="string" column="NAME" length="30"
//     */
//    public Collection getHoldsSpecimenClassCollection()
//    {
//        return holdsSpecimenClassCollection;
//    }
//    
//    /**
//     * @param holdsSpecimenClassCollection The holdsSpecimenClassCollection to set.
//     */
//    public void setHoldsSpecimenClassCollection(
//            Collection holdsSpecimenClassCollection)
//    {
//        this.holdsSpecimenClassCollection = holdsSpecimenClassCollection;
//    }
    
//    /**
//     * @return Returns the specimenClass.
//     * @hibernate.many-to-one column="SPECIMEN_CLASS_ID" class="edu.wustl.catissuecore.domain.SpecimenClass"
//     * constrained="true"
//     */
//    public SpecimenClass getSpecimenClass()
//    {
//        return specimenClass;
//    }
//
//    /**
//     * @param specimenClass The specimenClass to set.
//     */
//    public void setSpecimenClass(SpecimenClass specimenClass)
//    {
//        this.specimenClass = specimenClass;
//    }

	/**
     * @return Returns the specimenTypeCollection.
     * @hibernate.set name="specimenTypeCollection" table="CATISSUE_SPECIMEN_TYPE"
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_ARRAY_TYPE_ID"
	 * @hibernate.element type="string" column="SPECIMEN_TYPE" length="50"
     */
    public Collection getSpecimenTypeCollection()
    {
        return specimenTypeCollection;
    }

    /**
     * @param specimenTypeCollection The specimenTypeCollection to set.
     */
    public void setSpecimenTypeCollection(Collection specimenTypeCollection)
    {
        this.specimenTypeCollection = specimenTypeCollection;
    }
}