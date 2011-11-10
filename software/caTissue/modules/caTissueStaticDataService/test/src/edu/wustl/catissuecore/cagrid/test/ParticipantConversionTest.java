package edu.wustl.catissuecore.cagrid.test;
import edu.wustl.catissuecore.domain.ws.*;
import edu.wustl.catissuecore.domain.client.Fixtures;
import edu.wustl.catissuecore.domain.service.WAPIUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

/**
 * @author Ion C. Olaru
 * Date: 7/15/11
 * Time: 5:08 PM
 */
public class ParticipantConversionTest extends BaseConversionTest {

    public void testWsToDomainWithZeroIds() {
        Participant ws = new Participant();
        ws.setIdentifier(0);
        ws.setRaceCollection(Fixtures.createRaceCollectionForWs(ws));
        ws.setCollectionProtocolRegistrationCollection(Fixtures.getCollectionProtocolRegistrationWS(ws, null));
        ws.setParticipantMedicalIdentifierCollection(Fixtures.createPMICollectionForWs(ws));
        ws.setParticipantRecordEntryCollection(Fixtures.createPRECollectionForWs(ws));

        edu.wustl.catissuecore.domain.Participant d = (edu.wustl.catissuecore.domain.Participant)WAPIUtility.convertWsToDomain(ws);

        assertNotNull(d);
        assertEquals(null, d.getId());
        assertEquals(3, d.getRaceCollection().size());
        Object[] wsRaces; wsRaces = d.getRaceCollection().toArray();
        Collection<edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier> pmis = d.getParticipantMedicalIdentifierCollection();
        assertEquals(2, pmis.size());
        assertEquals("Asian", ((edu.wustl.catissuecore.domain.Race)wsRaces[2]).getRaceName());

        assertEquals(java.util.HashSet.class, d.getCollectionProtocolRegistrationCollection().getClass());
        assertEquals(java.util.HashSet.class, d.getRaceCollection().getClass());

        assertEquals(1, d.getParticipantRecordEntryCollection().size());
    }

    public void testWsToDomainRaceReferences() {
        Participant ws = new Participant();
        ws.setRaceCollection(Fixtures.createRaceCollectionForWs(ws));
        assertEquals(3, ws.getRaceCollection().getRace().length);

        edu.wustl.catissuecore.domain.Participant d = (edu.wustl.catissuecore.domain.Participant)WAPIUtility.convertWsToDomain(ws);

        assertNotNull(d);
        assertNotNull(d.getRaceCollection());
        assertEquals(3, d.getRaceCollection().size());

        Object[] races = d.getRaceCollection().toArray();

        edu.wustl.catissuecore.domain.Race race = null;

        race = (edu.wustl.catissuecore.domain.Race)races[0];
        assertEquals("White", race.getRaceName());
        assertNotNull(race.getParticipant());
        assertSame(d, race.getParticipant());

        race = (edu.wustl.catissuecore.domain.Race)races[1];
        assertEquals("Unknown", race.getRaceName());
        assertNotNull(race.getParticipant());
        assertSame(d, race.getParticipant());

        race = (edu.wustl.catissuecore.domain.Race)races[2];
        assertEquals("Asian", race.getRaceName());
        assertNotNull(race.getParticipant());
        assertSame(d, race.getParticipant());

    }

    public void testWsToDomain() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 1959);
        c.set(Calendar.MONTH, 3);
        c.set(Calendar.DATE, 17);
        Participant ws = new Participant();
        ws.setActivityStatus("Some Status");
        ws.setLastName("Gorbachev");
        ws.setFirstName("Mikhail");
        ws.setActivityStatus("Active");
        ws.setBirthDate(c);
        ws.setIdentifier(99);
        ws.setRaceCollection(Fixtures.createRaceCollectionForWs(ws));
        ws.setCollectionProtocolRegistrationCollection(Fixtures.getCollectionProtocolRegistrationWS(ws, null));
        ws.setParticipantMedicalIdentifierCollection(Fixtures.createPMICollectionForWs(ws));
        ws.setParticipantRecordEntryCollection(Fixtures.createPRECollectionForWs(ws));

        edu.wustl.catissuecore.domain.Participant d = (edu.wustl.catissuecore.domain.Participant)WAPIUtility.convertWsToDomain(ws);

        assertNotNull(d);
        assertEquals("Active", d.getActivityStatus());
        assertEquals("Gorbachev", d.getLastName());
        assertEquals("Mikhail", d.getFirstName());
        assertEquals(99, d.getId().intValue());
        Calendar controlC = Calendar.getInstance();
        assertNotSame(c, controlC);
        controlC.setTime(d.getBirthDate());
        assertEquals(1959, controlC.get(Calendar.YEAR));
        assertEquals(3, controlC.get(Calendar.MONTH));
        assertEquals(17, controlC.get(Calendar.DATE));
        assertEquals(3, d.getRaceCollection().size());
        Object[] wsRaces; wsRaces = d.getRaceCollection().toArray();
        Collection<edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier> pmis = d.getParticipantMedicalIdentifierCollection();
        assertEquals(2, pmis.size());
        assertEquals("Asian", ((edu.wustl.catissuecore.domain.Race)wsRaces[2]).getRaceName());

        assertEquals(java.util.HashSet.class, d.getCollectionProtocolRegistrationCollection().getClass());
        assertEquals(java.util.HashSet.class, d.getRaceCollection().getClass());
        assertEquals(java.util.HashSet.class, d.getParticipantRecordEntryCollection().getClass());

        assertEquals(1, d.getParticipantRecordEntryCollection().size());
    }

    public void testDomainToWs() {
        edu.wustl.catissuecore.domain.Participant d = new edu.wustl.catissuecore.domain.Participant();
        d.setActivityStatus("Some Status");
        d.setLastName("Gorbachev");
        d.setFirstName("Mikhail");
        d.setId((long)123);
        List<edu.wustl.catissuecore.domain.Race> races = new ArrayList<edu.wustl.catissuecore.domain.Race>();
        edu.wustl.catissuecore.domain.Race race = new edu.wustl.catissuecore.domain.Race();
        race.setRaceName("race A");
        races.add(race);
        race = new edu.wustl.catissuecore.domain.Race();
        race.setRaceName("race B");
        races.add(race);
        d.setRaceCollection(races);
        d.setCollectionProtocolRegistrationCollection(Fixtures.getCollectionProtocolRegistration(d));
        Participant ws = (Participant)WAPIUtility.convertDomainToWs(d);

        assertNotNull(ws);
        assertEquals(123, ws.getIdentifier());
        assertEquals(null, ws.getActivityStatus());
        assertEquals(null, ws.getLastName());
        assertEquals(null, ws.getFirstName());
        assertEquals(null, ws.getRaceCollection());
    }

}
