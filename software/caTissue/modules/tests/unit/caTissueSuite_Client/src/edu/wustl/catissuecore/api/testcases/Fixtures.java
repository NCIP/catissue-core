package edu.wustl.catissuecore.api.testcases;

import edu.wustl.catissuecore.domain.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Ion C. Olaru
 *         Date: 7/29/11 - 11:14 AM
 */
public class Fixtures {

    public static Specimen createSpecimen() {
        String key = UniqueKeyGeneratorUtil.getUniqueKey();
        Specimen s = new Specimen();
        s.setActivityStatus("Active");
        s.setAvailableQuantity(900.0);
        s.setBarcode("BarCode " + key);
        s.setIsAvailable(true);
        s.setLabel("Label " + key);
        s.setSpecimenType("DNA");
        s.setSpecimenClass("Tissue");
        return s;
    }

    public static SpecimenCollectionGroup createSpecimenCollectionGroup() {
        String key = UniqueKeyGeneratorUtil.getUniqueKey();
        SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
        scg.setSpecimenCollection(new ArrayList<Specimen>());
        scg.getSpecimenCollection().add(createSpecimen());
        scg.setName("Some Collection group name" + key);
        Iterator<Specimen> it = scg.getSpecimenCollection().iterator();

        while (it.hasNext()) {
            Specimen s = it.next();
            s.setSpecimenCollectionGroup(scg);
        }

        return scg;
    }

    public static CollectionProtocolRegistration createCollectionProtocolRegistration() {
        CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
        cpr.setProtocolParticipantIdentifier("I-" + UniqueKeyGeneratorUtil.getUniqueKey());
        return cpr;
    }


}
