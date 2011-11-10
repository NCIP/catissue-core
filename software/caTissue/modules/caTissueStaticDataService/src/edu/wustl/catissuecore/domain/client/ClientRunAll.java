package edu.wustl.catissuecore.domain.client;

import edu.wustl.catissuecore.domain.ws.*;
import edu.wustl.catissuecore.domain.util.UniqueKeyGenerator;
import edu.wustl.common.domain.ws.AbstractDomainObject;
import org.apache.axis.types.URI;
import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Map;

/**
 * @author Ion C. Olaru
 *         Date: 8/11/11 - 4:47 PM
 */
public class ClientRunAll {

    private static Logger log = Logger.getLogger(ClientRunAll.class);

    public static Catissue_cacoreClient client;


    public static void testCreateParticipant() throws RemoteException {
        Participant p = new Participant();
        p.setActivityStatus("Active");
        p.setFirstName("Alexander");
        p.setLastName("Nevsky");

        Object pResult = client.insert(p);
        System.out.println(pResult.getClass());
/*
        System.out.println(String.format("--> Participant inserted: %d", pResult.getIdentifier()));
        p.setIdentifier(pResult.getIdentifier());
*/

    }

    /**
     * This code creates a Collection Protocol with an Event, a Participant and associate them to Create the
     * Collection Protocol Registration, it also creates a Storage Container associated with Collection Protocol.
     * This information is needed for the next step.
     * */
    public static void createParticipantAndCPR() throws RemoteException {

        String key = UniqueKeyGenerator.getKey();

        // Create Collection Protocol
        CollectionProtocol cp = Fixtures.createCollectionProtocolWithOneCollectionProtocolEvent();
        cp.setTitle("System Test Site " + key);
        cp.setShortTitle("System Test Site " + key);
        CollectionProtocol cpResult = (CollectionProtocol) client.insert(cp);
        System.out.println(String.format("--> Collection Protocol inserted: %d", cpResult.getIdentifier()));
        cp.setIdentifier(cpResult.getIdentifier());

        // Create Participant and associate with the Collection Protocol
        Participant p = new Participant();
        p.setActivityStatus("Active");
        p.setFirstName("First" + key);
        p.setLastName("Last" + key);

        // Create Participant Collection Protocol Registration
        p.setCollectionProtocolRegistrationCollection(Fixtures.getCollectionProtocolRegistrationWS(p, cp));

        // Create Participant Race
        p.setRaceCollection(Fixtures.createRaceCollectionForWs(p));

        // Create Participant Medical Identifiers
        p.setParticipantMedicalIdentifierCollection(Fixtures.createPMICollectionForWs(p));

        // Create Participant Record Entry Collections
        p.setParticipantRecordEntryCollection(Fixtures.createPRECollectionForWs(p));

        Participant pResult = (Participant)client.insert(p);
        System.out.println(String.format("--> Participant inserted: %d", pResult.getIdentifier()));
        p.setIdentifier(pResult.getIdentifier());

        createStorageContainer(cp);

    }

    private static void createStorageContainer(CollectionProtocol cp) throws RemoteException {
        StorageContainer sc = new StorageContainer();
        sc.setSite(new StorageContainerSite());
        sc.setActivityStatus("Active");
        sc.setName("Container " + UniqueKeyGenerator.getKey());
        sc.getSite().setSite(new Site());
        sc.getSite().getSite().setIdentifier(1);
        sc.setTemperatureInCentigrade(-20);
        sc.setStorageType(new StorageContainerStorageType());
        sc.getStorageType().setStorageType(new StorageType());
        sc.getStorageType().getStorageType().setIdentifier(1);
        sc.setCapacity(new ContainerCapacity());
        sc.getCapacity().setCapacity(new Capacity());
        sc.getCapacity().getCapacity().setOneDimensionCapacity(BigInteger.TEN);
        sc.getCapacity().getCapacity().setTwoDimensionCapacity(BigInteger.TEN);
        sc.setCollectionProtocolCollection(new StorageContainerCollectionProtocolCollection());
        sc.getCollectionProtocolCollection().setCollectionProtocol(new CollectionProtocol[1]);
        sc.getCollectionProtocolCollection().getCollectionProtocol()[0] = cp;

        sc.setHoldsSpecimenClassCollection(new StorageContainerHoldsSpecimenClassCollection());
        sc.getHoldsSpecimenClassCollection().setString(new String[1]);
        sc.getHoldsSpecimenClassCollection().getString()[0] = "Fluid";

        sc.setHoldsSpecimenTypeCollection(new StorageContainerHoldsSpecimenTypeCollection());
        sc.getHoldsSpecimenTypeCollection().setString(new String[1]);
        sc.getHoldsSpecimenTypeCollection().getString()[0] = "Serum";

        StorageContainer scResult = (StorageContainer)client.insert(sc);
        System.out.println(String.format("--> Storage Container inserted: %d", scResult.getIdentifier()));
        sc.setIdentifier(scResult.getIdentifier());

    }

    /**
     * This method creates a Specimen Collection group using the Event and Collection
     * Protocol Registration from previous method.
     *
     * */
    public static void createSpecimenCollectionGroupWithSpecimens(long cpeId, long cprId, long scId, long sCharId) throws RemoteException {

        String key = UniqueKeyGenerator.getKey();

        SpecimenCollectionGroup scg = new SpecimenCollectionGroup();

        // event
        scg.setCollectionProtocolEvent(new SpecimenCollectionGroupCollectionProtocolEvent());
        scg.getCollectionProtocolEvent().setCollectionProtocolEvent(new CollectionProtocolEvent());
        scg.getCollectionProtocolEvent().getCollectionProtocolEvent().setIdentifier(cpeId);

        // site
        scg.setSpecimenCollectionSite(new AbstractSpecimenCollectionGroupSpecimenCollectionSite());
        scg.getSpecimenCollectionSite().setSite(new Site());
        scg.getSpecimenCollectionSite().getSite().setIdentifier(1);

        // protocol registration
        scg.setCollectionProtocolRegistration(new SpecimenCollectionGroupCollectionProtocolRegistration());
        scg.getCollectionProtocolRegistration().setCollectionProtocolRegistration(new CollectionProtocolRegistration());
        scg.getCollectionProtocolRegistration().getCollectionProtocolRegistration().setIdentifier(cprId);

        scg.setClinicalDiagnosis("Not Specified");
        scg.setClinicalStatus("Not Specified");
        scg.setActivityStatus("Active");
        scg.setCollectionStatus("Pending");
        scg.setComment("comment");

        SpecimenCollectionGroup scgResult = (SpecimenCollectionGroup)client.insert(scg);
        System.out.println(String.format("--> Specimen Collection Group inserted: %d", scgResult.getIdentifier()));
        scg.setIdentifier(scgResult.getIdentifier());

        Specimen s1 = insertSpecimen(scg, scId, sCharId);
        Specimen s2 = insertSpecimen(scg, scId, sCharId);
    }

    /**
     * This method inserts specimen using the container's ID created in the edu.wustl.wapi.client.ClientRunAll#createParticipantAndCPR()
     * */
    private static Specimen insertSpecimen(SpecimenCollectionGroup scg, long scId, long sCharId) throws RemoteException {
        String key = UniqueKeyGenerator.getKey();
        // Specimen
        Specimen s = new FluidSpecimen();

        // attributes
        s.setSpecimenClass("Fluid");
        s.setSpecimenType("Serum");
        s.setCollectionStatus("Collected");
        s.setActivityStatus("Active");
        s.setPathologicalStatus("Not Specified");
        s.setBarcode("Bar Code XYZ - " + key);
        s.setLabel("Label XYZ - " + key);
        s.setInitialQuantity(10.0);
        s.setAvailableQuantity(10.0);
        s.setIsAvailable(true);

        SpecimenPosition sp = new SpecimenPosition();
        sp.setStorageContainer(new SpecimenPositionStorageContainer());
        sp.getStorageContainer().setStorageContainer(new StorageContainer());
        sp.getStorageContainer().getStorageContainer().setIdentifier(scId);
        s.setSpecimenPosition(new SpecimenSpecimenPosition());
        s.getSpecimenPosition().setSpecimenPosition(sp);

        // specimen characteristics
        s.setSpecimenCharacteristics(new AbstractSpecimenSpecimenCharacteristics());
        s.getSpecimenCharacteristics().setSpecimenCharacteristics(new SpecimenCharacteristics());
        s.getSpecimenCharacteristics().getSpecimenCharacteristics().setIdentifier(sCharId);
        s.getSpecimenCharacteristics().getSpecimenCharacteristics().setTissueSide("Left");
        s.getSpecimenCharacteristics().getSpecimenCharacteristics().setTissueSite("Not Specified");

        // collections
        s.setSpecimenCollectionGroup(new SpecimenSpecimenCollectionGroup());
        s.getSpecimenCollectionGroup().setSpecimenCollectionGroup(scg);

        Specimen sResult = (Specimen)client.insert(s);
        System.out.println(String.format("--> Specimen inserted: %d", sResult.getIdentifier()));
        s.setIdentifier(sResult.getIdentifier());

        return sResult;
    }

    public static OrderDetails placeAnOrder(long specimenId) throws RemoteException {
        // Insert a new Distribution Protocol
        DistributionProtocol dp = Fixtures.createDistributionProtocol();
        dp = (DistributionProtocol)client.insert(dp);
        System.out.println("--> Distribution Protocol inserted: " + dp.getIdentifier());

        // Insert an order
        OrderDetails od = Fixtures.createOrderWithExistingSpecimenOrderItem(specimenId, dp.getIdentifier());
        od = (OrderDetails)client.insert(od);
        System.out.println("--> OrderDetails inserted: " + od.getIdentifier());

        return od;
    }

    public static void updateOrderToPending(long spId, long dpId, long oId, long oiId) throws RemoteException {
        OrderDetails od = Fixtures.createOrderWithExistingSpecimenOrderItem(spId, dpId);
        od.setIdentifier(oId);
        od.setStatus("Pending");
        od.getOrderItemCollection().getOrderItem()[0].setIdentifier(oiId);
        od.getOrderItemCollection().getOrderItem()[0].setStatus("Pending - Protocol Review");
        od = (OrderDetails)client.update(od);
        System.out.println("->> OrderDetails updated: " + od.getIdentifier());
    }

    public static void distributeOrder(long spId, long dpId, long oId, long oiId) throws RemoteException {
        OrderDetails od = Fixtures.createOrderWithExistingSpecimenOrderItem(spId, dpId);
        od.setIdentifier(oId);
        od.setStatus("Completed");
        od.getOrderItemCollection().getOrderItem()[0].setIdentifier(oiId);
        od.getOrderItemCollection().getOrderItem()[0].setStatus("Pending");

        // Create a Distribution
        Distribution d = Fixtures.createDistribution(od.getIdentifier(), dpId, spId);
        d = (Distribution)client.insert(d);
        System.out.println("-->> Distribution created: " + d.getIdentifier());

        // od = (OrderDetails)client.update(od);
        // System.out.println("->> OrderDetails updated: " + od.getIdentifier());
    }

    public static void distributeOrder(long spId, long dpId, long oId, long oiId, long diId) throws RemoteException {
        OrderDetails od = Fixtures.createOrderWithExistingSpecimenOrderItem(spId, dpId);
        Distribution d = Fixtures.createDistribution(oId, dpId, spId);
        DistributedItem di = d.getDistributedItemCollection().getDistributedItem(0);
        di.setIdentifier(diId);

        od.setIdentifier(oId);
        od.setStatus("Completed");
        od.getOrderItemCollection().getOrderItem()[0].setIdentifier(oiId);
        od.getOrderItemCollection().getOrderItem()[0].setStatus("Distributed");
        od.getOrderItemCollection().getOrderItem()[0].setDistributedItem(new OrderItemDistributedItem());
        od.getOrderItemCollection().getOrderItem()[0].getDistributedItem().setDistributedItem(di);

        od = (OrderDetails)client.update(od);
        System.out.println("->> OrderDetails updated: " + od.getIdentifier());
    }

    public static GlobusCredential getGlobusCredential() throws GlobusCredentialException {
        return GlobusCredential.getDefaultCredential();
    }

    public static void main(String[] args) throws RemoteException, GlobusCredentialException {
        createParticipantAndCPR();
//        createSpecimenCollectionGroupWithSpecimens();
    }
}
