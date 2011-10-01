package edu.wustl.catissuecore.domain.client;

import edu.wustl.catissuecore.domain.deintegration.ws.*;
import edu.wustl.catissuecore.domain.ws.*;
import edu.wustl.catissuecore.domain.util.UniqueKeyGenerator;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ion C. Olaru
 *         Date: 7/21/11 - 10:27 AM
 */
public class Fixtures {

    public static Set<edu.wustl.catissuecore.domain.CollectionProtocolRegistration> getCollectionProtocolRegistration(edu.wustl.catissuecore.domain.Participant p) {
        Set<edu.wustl.catissuecore.domain.CollectionProtocolRegistration> cprs = new HashSet();

        edu.wustl.catissuecore.domain.CollectionProtocol cp = new edu.wustl.catissuecore.domain.CollectionProtocol();
        cp.setType("CP_TYPE");
        cp.setId((long)12);

        for (byte i=0; i<3; i++) {
            edu.wustl.catissuecore.domain.CollectionProtocolRegistration cpr = new edu.wustl.catissuecore.domain.CollectionProtocolRegistration();
            cpr.setBarcode("BARCODE");
            cpr.setCollectionProtocol(cp);
            cprs.add(cpr);
        }

        return cprs;
    }

    public static ParticipantCollectionProtocolRegistrationCollection getCollectionProtocolRegistrationWS(Participant p, CollectionProtocol cp) {

        if (cp == null) {
            cp = new CollectionProtocol();
            cp.setIdentifier(new Long(1));
        }

        CollectionProtocolRegistrationCollectionProtocol cprcp = new CollectionProtocolRegistrationCollectionProtocol();
        cprcp.setCollectionProtocol(cp);

        CollectionProtocolRegistrationParticipant cprp = new CollectionProtocolRegistrationParticipant();
        cprp.setParticipant(p);

        CollectionProtocolRegistration[] cprArray = new CollectionProtocolRegistration[1];
        cprArray[0] = new CollectionProtocolRegistration();
        cprArray[0].setCollectionProtocol(cprcp);
        cprArray[0].setActivityStatus("Active");
        cprArray[0].setProtocolParticipantIdentifier("WS_PPI-01" + UniqueKeyGenerator.getKey());
        cprArray[0].setRegistrationDate(Calendar.getInstance());
        cprArray[0].setParticipant(null);

        ParticipantCollectionProtocolRegistrationCollection pcprc = new ParticipantCollectionProtocolRegistrationCollection();
        pcprc.setCollectionProtocolRegistration(cprArray);

        return pcprc;

    }

    public static Institution createInstitution() {
        Institution i = new Institution();
        i.setName("Institution name" + UniqueKeyGenerator.getKey());
        i.setRemoteId(Long.parseLong(UniqueKeyGenerator.getKey()));
        return i;
    }

    public static Participant createParticipant() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1959);
        c.set(Calendar.MONTH, 3);
        c.set(Calendar.DATE, 17);
        Participant ws = new Participant();
        ws.setActivityStatus("Active");
        ws.setLastName("Gorbachev");
        ws.setFirstName("Mikhail");
        ws.setBirthDate(c);
        ws.setIdentifier(99);
        return ws;
    }

    public static ParticipantRaceCollection createRaceCollectionForWs(Participant p) {
        // Participant newP = new Participant();
        // newP.setIdentifier(p.getIdentifier());

        ParticipantRaceCollection prc = new ParticipantRaceCollection();
        Race[] races = new Race[3];
        races[0] = new Race();
        races[0].setRaceName("Asian");
        // races[0].setParticipant(new RaceParticipant()); races[0].getParticipant().setParticipant(p);
        races[1] = new Race();
        races[1].setRaceName("Unknown");
        // races[1].setParticipant(new RaceParticipant()); races[1].getParticipant().setParticipant(p);
        races[2] = new Race();
        races[2].setRaceName("White");
        // races[2].setParticipant(new RaceParticipant()); races[2].getParticipant().setParticipant(p);
        prc.setRace(races);
        return prc;
    }

    public static ParticipantParticipantRecordEntryCollection createPRECollectionForWs(Participant p) {
        ParticipantParticipantRecordEntryCollection prec = new ParticipantParticipantRecordEntryCollection();

        ParticipantRecordEntry[] preArray = new ParticipantRecordEntry[1];
        prec.setParticipantRecordEntry(preArray);
        ParticipantRecordEntry pre = new ParticipantRecordEntry();
        preArray[0] = pre;

        return prec;
    }

    public static ParticipantParticipantMedicalIdentifierCollection createPMICollectionForWs(Participant p) {
        ParticipantParticipantMedicalIdentifierCollection pmic = new ParticipantParticipantMedicalIdentifierCollection();
        ParticipantMedicalIdentifier[] pmcArray = new ParticipantMedicalIdentifier[2];

        Site site1 = new Site();
        site1.setIdentifier(1);
        site1.setName("In Transit");

        Site site2 = new Site();
        site2.setIdentifier(1);
        site2.setName("In Transit");

        pmcArray[0] = new ParticipantMedicalIdentifier();
        pmcArray[0].setMedicalRecordNumber("MRN-01-" + UniqueKeyGenerator.getKey());
        pmcArray[0].setSite(new ParticipantMedicalIdentifierSite());
        pmcArray[0].getSite().setSite(site1);

        pmcArray[1] = new ParticipantMedicalIdentifier();
        pmcArray[1].setMedicalRecordNumber("MRN-02-" + UniqueKeyGenerator.getKey());
        pmcArray[1].setSite(new ParticipantMedicalIdentifierSite());
        pmcArray[1].getSite().setSite(site2);

        pmic.setParticipantMedicalIdentifier(pmcArray);
        return pmic;
    }

    private static OrderDetails createOrderDetails() {
        OrderDetails od = new OrderDetails();
        od.setComment("Comments: WS Java client.");
        od.setName("Order name 55");
        od.setStatus("New");
        od.setRequestedDate(Calendar.getInstance());
        return od;
    }

    public static OrderDetails createOrderWithDerivedSpecimenOrderItem() {

        Specimen s = new Specimen();
        s.setIdentifier(3941);

        OrderDetails od = createOrderDetails();
        od.setOrderItemCollection(new OrderDetailsOrderItemCollection());

        DerivedSpecimenOrderItem[] items = new DerivedSpecimenOrderItem[1];
        od.getOrderItemCollection().setOrderItem(items);

        od.setDistributionProtocol(new OrderDetailsDistributionProtocol());
        od.getDistributionProtocol().setDistributionProtocol(new DistributionProtocol());
        od.getDistributionProtocol().getDistributionProtocol().setIdentifier(2);

        DerivedSpecimenOrderItem item0 = new DerivedSpecimenOrderItem();
        item0.setSpecimenClass("Tissue");
        item0.setSpecimenType("DNA");
        item0.setStatus("New");
        item0.setDescription("Desc OrderItem 0");
        item0.setRequestedQuantity(0.1);
        item0.setNewSpecimenArrayOrderItem(new SpecimenOrderItemNewSpecimenArrayOrderItem());
        item0.getNewSpecimenArrayOrderItem().setNewSpecimenArrayOrderItem(new NewSpecimenArrayOrderItem());
        item0.getNewSpecimenArrayOrderItem().getNewSpecimenArrayOrderItem().setName("Specimen Array OI Name");
        item0.setParentSpecimen(new DerivedSpecimenOrderItemParentSpecimen());
        item0.getParentSpecimen().setSpecimen(s);
        items[0] = item0;

        return od;
    }

    public static OrderDetails createOrderDetailsWithSOI() {

        OrderDetails od = createOrderDetails();
        od.setOrderItemCollection(new OrderDetailsOrderItemCollection());
        OrderItem[] items = new OrderItem[3];
        od.getOrderItemCollection().setOrderItem(items);

        od.setDistributionProtocol(new OrderDetailsDistributionProtocol());
        od.getDistributionProtocol().setDistributionProtocol(new DistributionProtocol());
        od.getDistributionProtocol().getDistributionProtocol().setIdentifier(2);

        OrderItem item0 = new OrderItem();
        items[0] = item0;
        //items[0].setIdentifier(1);
        item0.setStatus("New");
        item0.setDescription("Desc OrderItem 0");
        item0.setRequestedQuantity(0.1);

        SpecimenOrderItem item1 = new SpecimenOrderItem();
        items[1] = item1;
        //items[1].setIdentifier(2);
        item1.setStatus("New");
        item1.setDescription("Desc SpecimenOrderItem 1");
        item1.setRequestedQuantity(0.2);

        SpecimenOrderItem item2 = new SpecimenOrderItem();
        items[2] = item2;
        // items[2].setIdentifier(3);
        item2.setStatus("New");
        item2.setDescription("Desc SpecimenOrderItem 2");
        item2.setRequestedQuantity(0.5);

        return od;
    }

    public static DistributionProtocol createDistributionProtocol() {

        DistributionProtocol dp = new DistributionProtocol();
        dp.setStartDate(Calendar.getInstance());
        dp.setActivityStatus("Active");
        SpecimenProtocolPrincipalInvestigator user = new SpecimenProtocolPrincipalInvestigator();
        user.setUser(new User());
        user.getUser().setIdentifier(1);
        dp.setPrincipalInvestigator(user);
        dp.setTitle("Some Title " + UniqueKeyGenerator.getKey());
        dp.setShortTitle("Some Title" + UniqueKeyGenerator.getKey());

        return dp;
    }

    public static SpecimenCollectionGroup createSpecimenCollectionGroup() {
        SpecimenCollectionGroup scg = new SpecimenCollectionGroup();

        // event
        scg.setCollectionProtocolEvent(new SpecimenCollectionGroupCollectionProtocolEvent());
        scg.getCollectionProtocolEvent().setCollectionProtocolEvent(new CollectionProtocolEvent());
        scg.getCollectionProtocolEvent().getCollectionProtocolEvent().setIdentifier(1);

        // site
        scg.setSpecimenCollectionSite(new AbstractSpecimenCollectionGroupSpecimenCollectionSite());
        scg.getSpecimenCollectionSite().setSite(new Site());
        scg.getSpecimenCollectionSite().getSite().setIdentifier(1);

        // protocol registration
        scg.setCollectionProtocolRegistration(new SpecimenCollectionGroupCollectionProtocolRegistration());
        scg.getCollectionProtocolRegistration().setCollectionProtocolRegistration(new CollectionProtocolRegistration());
        scg.getCollectionProtocolRegistration().getCollectionProtocolRegistration().setIdentifier(1);

        scg.setClinicalDiagnosis("Not Specified");
        scg.setClinicalStatus("Not Specified");
        scg.setActivityStatus("Active");
        scg.setCollectionStatus("Pending");
        scg.setComment("comment");

        return scg;
    }

    public static CollectionProtocol createCollectionProtocolWithOneCollectionProtocolEvent() {
        String key = UniqueKeyGenerator.getKey();

        CollectionProtocol cp = new CollectionProtocol();
        cp.setActivityStatus("Active");
        cp.setTitle("CP Title - " + key);
        cp.setShortTitle("CP Short Title - " + key);
        cp.setStartDate(Calendar.getInstance());
        cp.setRemoteId(new Date().getTime());

        User pi = new User();
        pi.setIdentifier(1);

        User user = new User();
        user.setIdentifier(2);

        cp.setPrincipalInvestigator(new SpecimenProtocolPrincipalInvestigator());
        cp.getPrincipalInvestigator().setUser(pi);
        cp.setCoordinatorCollection(new CollectionProtocolCoordinatorCollection());
        cp.getCoordinatorCollection().setUser(new User[1]);
        cp.getCoordinatorCollection().getUser()[0] = user;

        // CPE
        CollectionProtocolEvent cpe = new CollectionProtocolEvent();

        // site
        cpe.setSpecimenCollectionSite(new AbstractSpecimenCollectionGroupSpecimenCollectionSite());
        cpe.getSpecimenCollectionSite().setSite(new Site());
        cpe.getSpecimenCollectionSite().getSite().setIdentifier(1);

        cpe.setClinicalDiagnosis("Not Specified");
        cpe.setClinicalStatus("Not Specified");
        cpe.setActivityStatus("Active");
        cpe.setStudyCalendarEventPoint(5.0);
        cpe.setCollectionPointLabel("CPE - point label");
        cpe.setLabelFormat("%CP_DEFAULT%");

        cp.setCollectionProtocolEventCollection(new CollectionProtocolCollectionProtocolEventCollection());
        cp.getCollectionProtocolEventCollection().setCollectionProtocolEvent(new CollectionProtocolEvent[1]);
        cp.getCollectionProtocolEventCollection().getCollectionProtocolEvent()[0] = cpe;

        return cp;
    }

    public static OrderDetails createOrderWithExistingSpecimenOrderItem(long specimenId, long dpId) {

        OrderDetails od = createOrderDetails();

        od.setOrderItemCollection(new OrderDetailsOrderItemCollection());

        ExistingSpecimenOrderItem[] items = new ExistingSpecimenOrderItem[1];
        od.getOrderItemCollection().setOrderItem(items);

        od.setDistributionProtocol(new OrderDetailsDistributionProtocol());
        od.getDistributionProtocol().setDistributionProtocol(new DistributionProtocol());
        od.getDistributionProtocol().getDistributionProtocol().setIdentifier(dpId);

        ExistingSpecimenOrderItem esoi0 = new ExistingSpecimenOrderItem();
        esoi0.setSpecimen(new ExistingSpecimenOrderItemSpecimen());
        esoi0.getSpecimen().setSpecimen(new FluidSpecimen());
        esoi0.getSpecimen().getSpecimen().setIdentifier(specimenId);
//        esoi0.getSpecimen().getSpecimen().setSpecimenClass("Fixed Tissue");
        esoi0.setStatus("New");
        esoi0.setRequestedQuantity(0.2);

/*
        SpecimenSpecimenCollectionGroup sscg = new SpecimenSpecimenCollectionGroup();
        sscg.setSpecimenCollectionGroup(new SpecimenCollectionGroup());
        sscg.getSpecimenCollectionGroup().setIdentifier(1064);
        esoi0.getSpecimen().getSpecimen().setSpecimenCollectionGroup(sscg);
*/

        items[0] = esoi0;

        return od;
    }

    public static Distribution createDistribution(long odId, long dpId, long spcId) {
        Distribution d = new Distribution();

        d.setDistributionProtocol(new DistributionDistributionProtocol());
        d.getDistributionProtocol().setDistributionProtocol(new DistributionProtocol());
        d.getDistributionProtocol().getDistributionProtocol().setIdentifier(dpId);

        d.setToSite(new DistributionToSite());
        d.getToSite().setSite(new Site());
        d.getToSite().getSite().setIdentifier(1);

        d.setActivityStatus("Active");
        d.setOrderDetails(new DistributionOrderDetails());
        d.getOrderDetails().setOrderDetails(new OrderDetails());
        d.getOrderDetails().getOrderDetails().setIdentifier(odId);
        d.setComment("COMM");

        d.setDistributedBy(new DistributionDistributedBy());
        d.getDistributedBy().setUser(new User());
        d.getDistributedBy().getUser().setIdentifier(1);

        d.setDistributedItemCollection(new DistributionDistributedItemCollection());
        d.getDistributedItemCollection().setDistributedItem(new DistributedItem[1]);
        d.getDistributedItemCollection().getDistributedItem()[0] = new DistributedItem();
        d.getDistributedItemCollection().getDistributedItem()[0].setQuantity(0.2);

        DistributedItem di = d.getDistributedItemCollection().getDistributedItem()[0];
        di.setSpecimen(new DistributedItemSpecimen());
        di.getSpecimen().setSpecimen(new FluidSpecimen());
        di.getSpecimen().getSpecimen().setIdentifier(spcId);

        return d;
    }


}
