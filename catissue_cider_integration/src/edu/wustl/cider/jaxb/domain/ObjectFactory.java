//
// This file was edu.wustl.cider.jaxb.domain by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v1.0.4-b18-fcs
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// edu.wustl.cider.jaxb.domain on: 2011.06.16 at 07:41:18 GMT
//


package edu.wustl.cider.jaxb.domain;

import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * edu.wustl.cider.jaxb.domain in the edu.wustl.cider.jaxb.domain package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
public class ObjectFactory
    extends edu.wustl.cider.jaxb.domain.impl.runtime.DefaultJAXBContextImpl
{

    private static java.util.HashMap defaultImplementations = new java.util.HashMap(23, 0.75F);
    private static java.util.HashMap rootTagMap = new java.util.HashMap();
    public final static edu.wustl.cider.jaxb.domain.impl.runtime.GrammarInfo grammarInfo = new edu.wustl.cider.jaxb.domain.impl.runtime.GrammarInfoImpl(rootTagMap, defaultImplementations, (edu.wustl.cider.jaxb.domain.ObjectFactory.class));
    public final static java.lang.Class version = (edu.wustl.cider.jaxb.domain.impl.JAXBVersion.class);

    static {
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.SpecimenCharacteristicsType.class), "edu.wustl.cider.jaxb.domain.impl.SpecimenCharacteristicsTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.SpecimenType.class), "edu.wustl.cider.jaxb.domain.impl.SpecimenTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.Participant.class), "edu.wustl.cider.jaxb.domain.impl.ParticipantImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.CoordinatorType.class), "edu.wustl.cider.jaxb.domain.impl.CoordinatorTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.ParticipantType.class), "edu.wustl.cider.jaxb.domain.impl.ParticipantTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.SiteType.class), "edu.wustl.cider.jaxb.domain.impl.SiteTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.SCGCollectionType.class), "edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.SpecimenCollectionType.class), "edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.PrincipalInvestigatorType.class), "edu.wustl.cider.jaxb.domain.impl.PrincipalInvestigatorTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.StorageContainerType.class), "edu.wustl.cider.jaxb.domain.impl.StorageContainerTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.SpecimenPositionType.class), "edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.SpecimenCollectionGroupType.class), "edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionGroupTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.CollectionProtocolRegistrationType.class), "edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.CollectionProtocolType.class), "edu.wustl.cider.jaxb.domain.impl.CollectionProtocolTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.CollectionEventType.class), "edu.wustl.cider.jaxb.domain.impl.CollectionEventTypeImpl");
        defaultImplementations.put((edu.wustl.cider.jaxb.domain.SpecimenEventsType.class), "edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl");
        rootTagMap.put(new javax.xml.namespace.QName("", "Participant"), (edu.wustl.cider.jaxb.domain.Participant.class));
    }

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.wustl.cider.jaxb.domain
     *
     */
    public ObjectFactory() {
        super(grammarInfo);
    }

    /**
     * Create an instance of the specified Java content interface.
     *
     * @param javaContentInterface
     *     the Class object of the javacontent interface to instantiate
     * @return
     *     a new instance
     * @throws JAXBException
     *     if an error occurs
     */
    public java.lang.Object newInstance(java.lang.Class javaContentInterface)
        throws javax.xml.bind.JAXBException
    {
        return super.newInstance(javaContentInterface);
    }

    /**
     * Get the specified property. This method can only be
     * used to get provider specific properties.
     * Attempting to get an undefined property will result
     * in a PropertyException being thrown.
     *
     * @param name
     *     the name of the property to retrieve
     * @return
     *     the value of the requested property
     * @throws PropertyException
     *     when there is an error retrieving the given property or value
     */
    public java.lang.Object getProperty(java.lang.String name)
        throws javax.xml.bind.PropertyException
    {
        return super.getProperty(name);
    }

    /**
     * Set the specified property. This method can only be
     * used to set provider specific properties.
     * Attempting to set an undefined property will result
     * in a PropertyException being thrown.
     *
     * @param value
     *     the value of the property to be set
     * @param name
     *     the name of the property to retrieve
     * @throws PropertyException
     *     when there is an error processing the given property or value
     */
    public void setProperty(java.lang.String name, java.lang.Object value)
        throws javax.xml.bind.PropertyException
    {
        super.setProperty(name, value);
    }

    /**
     * Create an instance of SpecimenCharacteristicsType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.SpecimenCharacteristicsType createSpecimenCharacteristicsType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.SpecimenCharacteristicsTypeImpl();
    }

    /**
     * Create an instance of SpecimenType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.SpecimenType createSpecimenType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.SpecimenTypeImpl();
    }

    /**
     * Create an instance of Participant
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.Participant createParticipant()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.ParticipantImpl();
    }

    /**
     * Create an instance of CoordinatorType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.CoordinatorType createCoordinatorType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.CoordinatorTypeImpl();
    }

    /**
     * Create an instance of ParticipantType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.ParticipantType createParticipantType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.ParticipantTypeImpl();
    }

    /**
     * Create an instance of SiteType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.SiteType createSiteType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.SiteTypeImpl();
    }

    /**
     * Create an instance of SCGCollectionType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.SCGCollectionType createSCGCollectionType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.SCGCollectionTypeImpl();
    }

    /**
     * Create an instance of SpecimenCollectionType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.SpecimenCollectionType createSpecimenCollectionType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionTypeImpl();
    }

    /**
     * Create an instance of PrincipalInvestigatorType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.PrincipalInvestigatorType createPrincipalInvestigatorType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.PrincipalInvestigatorTypeImpl();
    }

    /**
     * Create an instance of StorageContainerType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.StorageContainerType createStorageContainerType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.StorageContainerTypeImpl();
    }

    /**
     * Create an instance of SpecimenPositionType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.SpecimenPositionType createSpecimenPositionType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.SpecimenPositionTypeImpl();
    }

    /**
     * Create an instance of SpecimenCollectionGroupType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.SpecimenCollectionGroupType createSpecimenCollectionGroupType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.SpecimenCollectionGroupTypeImpl();
    }

    /**
     * Create an instance of CollectionProtocolRegistrationType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.CollectionProtocolRegistrationType createCollectionProtocolRegistrationType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.CollectionProtocolRegistrationTypeImpl();
    }

    /**
     * Create an instance of CollectionProtocolType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.CollectionProtocolType createCollectionProtocolType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.CollectionProtocolTypeImpl();
    }

    /**
     * Create an instance of CollectionEventType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.CollectionEventType createCollectionEventType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.CollectionEventTypeImpl();
    }

    /**
     * Create an instance of SpecimenEventsType
     *
     * @throws JAXBException
     *     if an error occurs
     */
    public edu.wustl.cider.jaxb.domain.SpecimenEventsType createSpecimenEventsType()
        throws javax.xml.bind.JAXBException
    {
        return new edu.wustl.cider.jaxb.domain.impl.SpecimenEventsTypeImpl();
    }

}
