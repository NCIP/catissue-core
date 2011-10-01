import edu.wustl.catissuecore.domain.ws.*;
import edu.wustl.common.domain.ws.AbstractDomainObject;
import edu.wustl.catissuecore.domain.client.Fixtures;
import edu.wustl.catissuecore.domain.service.WAPIUtility;
import gov.nih.nci.system.applicationservice.ApplicationException;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ion C. Olaru
 *         Date: 7/25/11 - 11:10 AM
 */
public class WAPIUtilityTest extends TestCase {

    public void testDisableField() throws ApplicationException {
        AbstractDomainObject ado = Fixtures.createParticipant();
        assertEquals("Active", ((Participant)ado).getActivityStatus());
        WAPIUtility.updateWsObjectStatus(ado);
        assertEquals("Disabled", ((Participant)ado).getActivityStatus());
    }

    public void testNullifyWithValuesGTZero() throws Exception {
        edu.wustl.catissuecore.domain.Participant p = new edu.wustl.catissuecore.domain.Participant();
        p.setRaceCollection(new ArrayList<edu.wustl.catissuecore.domain.Race>());
        edu.wustl.catissuecore.domain.Race r;

        r = new edu.wustl.catissuecore.domain.Race();
        r.setParticipant(p);
        r.setId((long)10);
        p.getRaceCollection().add(r);

        r = new edu.wustl.catissuecore.domain.Race();
        r.setId((long)11);
        p.getRaceCollection().add(r);

        p.setId((long)12);
        assertNotNull(p.getId());
        WAPIUtility.nullifyFieldValue(p, "setId", "getId",  Long.class, null);
        assertNotNull(p.getId());

        Iterator<edu.wustl.catissuecore.domain.Race> it = p.getRaceCollection().iterator();
        while (it.hasNext()) {
            edu.wustl.catissuecore.domain.Race race = it.next();
            assertNotNull(race.getId());
        }
    }

    public void testNullifyWithValuesEqToZero() throws Exception {
        edu.wustl.catissuecore.domain.Participant p = new edu.wustl.catissuecore.domain.Participant();
        p.setRaceCollection(new ArrayList<edu.wustl.catissuecore.domain.Race>());
        edu.wustl.catissuecore.domain.Race r;

        r = new edu.wustl.catissuecore.domain.Race();
        r.setParticipant(p);
        r.setId((long)0);
        p.getRaceCollection().add(r);

        r = new edu.wustl.catissuecore.domain.Race();
        r.setId((long)0);
        p.getRaceCollection().add(r);

        p.setId((long)0);
        assertNotNull(p.getId());
        WAPIUtility.nullifyFieldValue(p, "setId", "getId",  Long.class, null);

        assertNull(p.getId());

        Iterator<edu.wustl.catissuecore.domain.Race> it = p.getRaceCollection().iterator();
        while (it.hasNext()) {
            edu.wustl.catissuecore.domain.Race race = it.next();
            assertNull(race.getId());
        }
    }
}
