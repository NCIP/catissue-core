package edu.yale.med.krauthammerlab.catissuecore.cagrid.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.UUID;

import java.math.BigInteger;

import org.cagrid.cql2.AttributeValue;
import org.cagrid.cql2.BinaryPredicate;
import org.cagrid.cql2.CQLAttribute;
import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql2.CQLTargetObject;
import org.cagrid.cql2.results.CQLQueryResults;
import org.globus.gsi.GlobusCredential;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import edu.wustl.catissuecore.domain.client.Catissue_cacoreClient;
import edu.wustl.catissuecore.domain.client.ClientRunAll;
import edu.wustl.catissuecore.domain.client.Fixtures;
import edu.wustl.catissuecore.domain.util.UniqueKeyGenerator;
import edu.wustl.catissuecore.domain.ws.*;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;
import junit.framework.TestCase;

public abstract class TestBase extends TestCase {

    String url = "https://catissuesvc.wustl.edu:443/wsrf/services/cagrid/Catissue_cacore";
    //String url = "https://granite.med.yale.edu:8443/wsrf/services/cagrid/Catissue_cacore";
    protected Catissue_cacoreClient client;
    GlobusCredential cred;
    CQLQuery query;
    String cqlFileName = "cqls/Query205.xml";
    private int positionCounter = 1;

    public TestBase() {
        super();
    }

    public TestBase(String name) {
        super(name);
    }

    public SpecimenCollectionGroup createSpecimenCollectionGroup(CollectionProtocolRegistration reg)
            throws RemoteException {

        SpecimenCollectionGroup scg = new SpecimenCollectionGroup();

        // event
        scg.setCollectionProtocolEvent(new SpecimenCollectionGroupCollectionProtocolEvent());
        CollectionProtocolEvent event = reg.getCollectionProtocol().getCollectionProtocol()
                .getCollectionProtocolEventCollection().getCollectionProtocolEvent(0);
        scg.getCollectionProtocolEvent().setCollectionProtocolEvent(event);

        // site
        scg.setSpecimenCollectionSite(new AbstractSpecimenCollectionGroupSpecimenCollectionSite());
        scg.getSpecimenCollectionSite().setSite(new Site());
        scg.getSpecimenCollectionSite().getSite().setIdentifier(1);

        // protocol registration
        scg.setCollectionProtocolRegistration(new SpecimenCollectionGroupCollectionProtocolRegistration());
        scg.getCollectionProtocolRegistration().setCollectionProtocolRegistration(reg);

        scg.setClinicalDiagnosis("Not Specified");
        scg.setClinicalStatus("Not Specified");
        scg.setActivityStatus("Active");
        scg.setCollectionStatus("Pending");
        scg.setComment("comment");

        SpecimenCollectionGroup scgResult = (SpecimenCollectionGroup) client.insert(scg);
        System.out.println(String.format("--> Specimen Collection Group inserted: %d",
                scgResult.getIdentifier()));
        scg.setIdentifier(scgResult.getIdentifier());
        scg.setIdentifier(scgResult.getIdentifier());

        return scg;
    }

    protected StorageContainer makeContainer(long typeId, long siteId) throws RemoteException {
        StorageContainer container = new StorageContainer();
        container.setStorageType(new StorageContainerStorageType());
        container.getStorageType().setStorageType(new StorageType());
        container.getStorageType().getStorageType().setIdentifier(typeId);
        container.setSite(new StorageContainerSite());
        container.getSite().setSite(new Site());
        container.getSite().getSite().setIdentifier(siteId);
        container.setCapacity(new ContainerCapacity());
        container.getCapacity().setCapacity(new Capacity());
        container.getCapacity().getCapacity().setOneDimensionCapacity(BigInteger.ONE);
        container.getCapacity().getCapacity().setTwoDimensionCapacity(BigInteger.ONE);
        container.setActivityStatus("Active");
        container = (StorageContainer) client.insert(container);
        return container;
    }

    protected Specimen insertSpecimen(SpecimenCollectionGroup scg, Class type,
            String specimenClass, String specimenType) throws RemoteException,
            InstantiationException, IllegalAccessException {
        return insertSpecimen(scg, null, type, specimenClass, specimenType);
    }

    /**
     * This method inserts specimen using the container's ID created in the
     * edu.wustl.wapi.client.ClientRunAll#createParticipantAndCPR()
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     * */
    protected Specimen insertSpecimen(SpecimenCollectionGroup scg, StorageContainer container, Class type,
            String specimenClass, String specimenType) throws RemoteException,
            InstantiationException, IllegalAccessException {
        String key = UniqueKeyGenerator.getKey();
        // Specimen
        Specimen s = (Specimen) type.newInstance();

        // attributes
        s.setSpecimenClass(specimenClass);
        s.setSpecimenType(specimenType);
        s.setCollectionStatus("Collected");
        s.setActivityStatus("Active");
        s.setPathologicalStatus("Not Specified");
        s.setBarcode("Bar Code XYZ - " + key);
        s.setLabel("Label XYZ - " + key);
        s.setInitialQuantity(10.0);
        s.setAvailableQuantity(10.0);
        s.setIsAvailable(true);
        s.setActivityStatus("Active");

        // s.setSpecimenPosition(new SpecimenSpecimenPosition());
        // s.getSpecimenPosition().setSpecimenPosition(new SpecimenPosition());
        // s.getSpecimenPosition().getSpecimenPosition().setStorageContainer(new
        // SpecimenPositionStorageContainer());
        // s.getSpecimenPosition().getSpecimenPosition().getStorageContainer().setStorageContainer(makeContainer(7,1));
        // s.getSpecimenPosition().getSpecimenPosition().setPositionDimensionOne(BigInteger.valueOf(1));
        // s.getSpecimenPosition().getSpecimenPosition().setPositionDimensionTwo(BigInteger.valueOf(1));

        // specimen characteristics
        s.setSpecimenCharacteristics(new AbstractSpecimenSpecimenCharacteristics());
        s.getSpecimenCharacteristics().setSpecimenCharacteristics(new SpecimenCharacteristics());
        s.getSpecimenCharacteristics().getSpecimenCharacteristics().setTissueSide("Left");
        s.getSpecimenCharacteristics().getSpecimenCharacteristics().setTissueSite("Not Specified");

        // collections
        s.setSpecimenCollectionGroup(new SpecimenSpecimenCollectionGroup());
        s.getSpecimenCollectionGroup().setSpecimenCollectionGroup(scg);
        s.setActivityStatus("Active");
        
        if (container != null) {
            // position
            SpecimenPosition pos = new SpecimenPosition();
            pos.setPositionDimensionOne(BigInteger.valueOf(positionCounter++));
            pos.setPositionDimensionTwo(BigInteger.valueOf(1));
            SpecimenPositionStorageContainer storageContainer = new SpecimenPositionStorageContainer();

            storageContainer.setStorageContainer(container);
            
            pos.setStorageContainer(storageContainer);
            final SpecimenPositionSpecimen specimen = new SpecimenPositionSpecimen();                   
            pos.setSpecimen(specimen);
            
            SpecimenSpecimenPosition specimenPosition = new SpecimenSpecimenPosition();   
            specimenPosition.setSpecimenPosition(pos);
            s.setSpecimenPosition(specimenPosition); 
        }
        Specimen sResult = (Specimen) client.insert(s);
        s.setIdentifier(sResult.getIdentifier());
        s.setGlobalSpecimenIdentifier(sResult.getGlobalSpecimenIdentifier());

        return s;
    }

    /**
     * This code creates a Collection Protocol with an Event, a Participant and
     * associate them to Create the Collection Protocol Registration, it also
     * creates a Storage Container associated with Collection Protocol. This
     * information is needed for the next step.
     * 
     * @return CollectionProtocolRegistration
     * */
    public CollectionProtocolRegistration createParticipantAndCPR() throws RemoteException {

        String key = UniqueKeyGenerator.getKey();

        // Create Collection Protocol
        CollectionProtocol cp = Fixtures.createCollectionProtocolWithOneCollectionProtocolEvent();
        cp.setTitle("Test CP " + key);
        cp.setShortTitle("Test CP " + key);
        CollectionProtocol cpResult = (CollectionProtocol) client.insert(cp);
        System.out.println(String.format("--> Collection Protocol inserted: %d",
                cpResult.getIdentifier()));
        cp.setIdentifier(cpResult.getIdentifier());

        // Create Participant and associate with the Collection Protocol
        Participant p = new Participant();
        p.setActivityStatus("Active");
        p.setFirstName("First" + key);
        p.setLastName("Last" + key);

        ParticipantCollectionProtocolRegistrationCollection reg = Fixtures
                .getCollectionProtocolRegistrationWS(p, cp);
        // Create Participant Collection Protocol Registration
        p.setCollectionProtocolRegistrationCollection(reg);

        // Create Participant Race
        p.setRaceCollection(Fixtures.createRaceCollectionForWs(p));

        // Create Participant Medical Identifiers
        p.setParticipantMedicalIdentifierCollection(Fixtures.createPMICollectionForWs(p));

        // Create Participant Record Entry Collections
        p.setParticipantRecordEntryCollection(Fixtures.createPRECollectionForWs(p));

        Participant pResult = (Participant) client.insert(p);
        System.out.println(String.format("--> Participant inserted: %d", pResult.getIdentifier()));
        p.setIdentifier(pResult.getIdentifier());
        return reg.getCollectionProtocolRegistration(0);
    }

    public String serialize(Object o) throws Exception {
        StringWriter writer = new StringWriter();
        Utils.serializeObject(o, DataServiceConstants.CQL_RESULT_SET_QNAME, writer);
        return writer.getBuffer().toString();
    }

    protected CQLQuery createSpecimenQuery(Specimen specimen, String className) {
        CQLQuery q = new CQLQuery();
        CQLTargetObject targetObject = new CQLTargetObject();
        targetObject.setClassName(className);
        // targetObject.set_instanceof("edu.wustl.catissuecore.domain.FluidSpecimen");

        CQLAttribute att = new CQLAttribute();

        att.setBinaryPredicate(BinaryPredicate.EQUAL_TO);
        att.setName("id");
        AttributeValue val = new AttributeValue();
        val.setLongValue(specimen.getIdentifier());
        att.setAttributeValue(val);
        targetObject.setCQLAttribute(att);
        q.setCQLTargetObject(targetObject);
        // System.out.println(serialize(q));
        return q;
    }

    protected Element getResultsElement(CQLQueryResults results) throws Exception {
        String resultString = serialize(results);
        System.out.println(resultString);
        SAXBuilder builder = new SAXBuilder();

        Document result = builder.build(new StringReader(resultString));
        Element objectResult = (Element) result.getRootElement().getChildren().get(0);
        return objectResult;
    }

    @Override
    protected void setUp() throws Exception {

        if (url.startsWith("https")) {
            cred = GlobusCredential.getDefaultCredential();

            client = new Catissue_cacoreClient(url, cred);
        } else {
            client = new Catissue_cacoreClient(url);
        }
        ClientRunAll.client = client;
    }

    public StorageContainer createStorageContainer(CollectionProtocolRegistration reg,
            SpecimenCollectionGroup scg) throws QueryProcessingExceptionType, RemoteException {
        StorageContainer storageContainer = new StorageContainer();
        storageContainer.setActivityStatus("Active");
        final String uuid = UUID.randomUUID().toString();
        storageContainer.setBarcode(uuid);

        ContainerCapacity capacity = new ContainerCapacity();
        Capacity capacityValue = new Capacity();
        capacityValue.setOneDimensionCapacity(BigInteger.valueOf(1000));
        capacityValue.setTwoDimensionCapacity(BigInteger.valueOf(1000));
        capacity.setCapacity(capacityValue);
        storageContainer.setCapacity(capacity);

        StorageContainerCollectionProtocolCollection collectionProtocolCollection = new StorageContainerCollectionProtocolCollection();
        collectionProtocolCollection.setCollectionProtocol(new CollectionProtocol[] { reg
                .getCollectionProtocol().getCollectionProtocol() });
        storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);

        storageContainer.setComment(uuid);
        storageContainer.setName(uuid);

        StorageContainerStorageType storageType = new StorageContainerStorageType();
        StorageType type = new StorageType();
        type.setIdentifier(3);
        storageType.setStorageType(type);
        storageContainer.setStorageType(storageType);

        StorageContainerSite scSite = new StorageContainerSite();
        Site site = new Site();
        site.setIdentifier(1);
        scSite.setSite(site);
        storageContainer.setSite(scSite);

        storageContainer.setIdentifier(((StorageContainer) client.insert(storageContainer))
                .getIdentifier());
        storageContainer.setName("In Transit_Shipment Container_"
                + storageContainer.getIdentifier());

        return storageContainer;
    }

}