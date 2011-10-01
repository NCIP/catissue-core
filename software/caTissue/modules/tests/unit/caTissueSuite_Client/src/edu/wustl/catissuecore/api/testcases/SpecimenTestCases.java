package edu.wustl.catissuecore.api.testcases;

import edu.wustl.catissuecore.domain.*;
import gov.nih.nci.system.applicationservice.ApplicationException;

import java.util.HashSet;

/**
 * @author Ion C. Olaru
 *         Date: 8/8/11 - 3:17 PM
 */
public class SpecimenTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {

    public void testMoveSpecimenToSpecimenCollectionGroup() throws ApplicationException {
        // get Site by Id
        Site site = new Site();
        site.setId((long)1);
        site = searchById(Site.class, site);

        System.out.println(site);

        Specimen s = new Specimen();
        s.setId((long)4);
        s = searchById(Specimen.class, s);

        System.out.println(s);

        SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
        scg.setId((long)23);
        scg = searchById(SpecimenCollectionGroup.class, scg);

        scg.setSpecimenCollectionSite(site);

        System.out.println(scg);

//        scg.getSpecimenCollection().add(s);
        s.setSpecimenCollectionGroup(scg);
        update(s);
//        update(scg);
    }


    public void testAddNewSpecimenToSpecimenCollectionGroup() throws ApplicationException {

        SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
        scg.setId((long)966);
        scg = searchById(SpecimenCollectionGroup.class, scg);

        Site site = new Site();
        site.setId((long)1);
        site = searchById(Site.class, site);

        scg.setSpecimenCollectionSite(site);

        update(scg);

        Specimen parentSpecimen = new Specimen();
        parentSpecimen.setId((long)5);
        parentSpecimen = searchById(Specimen.class, parentSpecimen);


        Specimen s = new TissueSpecimen();
        s.setSpecimenClass("Tissue");
        s.setSpecimenType("Fixed Tissue");
        s.setCollectionStatus("Collected");
        s.setActivityStatus("Active");
        s.setInitialQuantity(10.0);
        s.setAvailableQuantity(9.0);
        s.setIsAvailable(Boolean.TRUE);
        s.setParentSpecimen(parentSpecimen);
        s.setSpecimenCollectionGroup(scg);
        parentSpecimen.setChildSpecimenCollection(new HashSet());
        parentSpecimen.getChildSpecimenCollection().add(s);
        s.setConsentTierStatusCollection(new HashSet());

        insert(s);
    }

    public void testCreateSpecimenCollectionGroup() throws ApplicationException {

        CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
        cpr.setId((long)1);
        cpr = searchById(CollectionProtocolRegistration.class, cpr);

        CollectionProtocolEvent cpe = new CollectionProtocolEvent();
        cpe.setId((long)1);
        cpe = searchById(CollectionProtocolEvent.class, cpe);

        Site site = new Site();
        site.setId((long)1);
        site = searchById(Site.class, site);

        SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
        scg.setSpecimenCollectionSite(site);
        scg.setCollectionProtocolRegistration(cpr);
        scg.setCollectionProtocolEvent(cpe);
        scg.setClinicalDiagnosis("Not Specified");
        scg.setClinicalStatus("Not Specified");
        scg.setActivityStatus("Active");
        scg.setCollectionStatus("Pending");
        scg = insert(scg);

        System.out.println(scg.getId());
    }


}
