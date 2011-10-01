import edu.wustl.catissuecore.domain.ws.CollectionProtocol;
import edu.wustl.catissuecore.domain.ws.Participant;
import edu.wustl.catissuecore.domain.ws.ParticipantRaceCollection;
import edu.wustl.catissuecore.domain.ws.Race;
import edu.wustl.catissuecore.domain.service.WAPIUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Ion C. Olaru
 * Date: 7/15/11
 * Time: 5:08 PM
 */
public class CollectionProtocolConversionTest extends BaseConversionTest {

    public void testWsToDomain() {
        CollectionProtocol ws = new CollectionProtocol();
        ws.setShortTitle("Short Title");
        ws.setIdentifier(99);
        edu.wustl.catissuecore.domain.CollectionProtocol d = (edu.wustl.catissuecore.domain.CollectionProtocol) WAPIUtility.convertWsToDomain(ws);
        assertNotNull(d);
        assertEquals("Short Title", d.getShortTitle());
        assertEquals(99, d.getId().intValue());
    }

    public void testDomainToWs() {
        edu.wustl.catissuecore.domain.CollectionProtocol d = new edu.wustl.catissuecore.domain.CollectionProtocol();
        d.setType("T1");
        d.setId((long)223);
        CollectionProtocol ws = (CollectionProtocol)WAPIUtility.convertDomainToWs(d);
        assertNotNull(ws);
        assertEquals(223, ws.getIdentifier());
        assertEquals(null, ws.getType());
    }

}
