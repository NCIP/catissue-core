package edu.wustl.catissuecore.cagrid.test;
import edu.wustl.catissuecore.domain.ws.*;
import edu.wustl.catissuecore.domain.ws.Participant;
import edu.wustl.catissuecore.domain.service.WAPIUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ion C. Olaru
 * Date: 7/15/11
 * Time: 5:08 PM
 */
public class CollectionProtocolRegistrationConversionTest extends BaseConversionTest {

    public void testWsToDomain() {
        CollectionProtocolRegistration ws = new CollectionProtocolRegistration();

        CollectionProtocolRegistrationCollectionProtocol cprcp = new CollectionProtocolRegistrationCollectionProtocol();
        cprcp.setCollectionProtocol(new CollectionProtocol());
        cprcp.getCollectionProtocol().setIdentifier(22);

        CollectionProtocolRegistrationParticipant cprp = new CollectionProtocolRegistrationParticipant();
        cprp.setParticipant(new Participant());
        cprp.getParticipant().setIdentifier(11);

        ws.setCollectionProtocol(cprcp);
        ws.setParticipant(cprp);

        ws.setIdentifier(99);
        edu.wustl.catissuecore.domain.CollectionProtocolRegistration d = (edu.wustl.catissuecore.domain.CollectionProtocolRegistration) WAPIUtility.convertWsToDomain(ws);

        assertNotNull(d);
        assertEquals(99, d.getId().intValue());
        assertEquals(11, d.getParticipant().getId().intValue());
        assertEquals(22, d.getCollectionProtocol().getId().intValue());
    }

    public void testDomainToWs() {
        edu.wustl.catissuecore.domain.CollectionProtocolRegistration d = new edu.wustl.catissuecore.domain.CollectionProtocolRegistration();
        d.setId(new Long(1001));
        d.setBarcode("89ABC");
        d.setParticipant(new edu.wustl.catissuecore.domain.Participant()); d.getParticipant().setId(new Long(101));
        d.setCollectionProtocol(new edu.wustl.catissuecore.domain.CollectionProtocol()); d.getCollectionProtocol().setId(new Long(201));
        CollectionProtocolRegistration ws = (CollectionProtocolRegistration)WAPIUtility.convertDomainToWs(d);
        assertNotNull(ws);
        assertEquals(1001, ws.getIdentifier());
        assertEquals(null, ws.getBarcode());
        assertEquals(null, ws.getParticipant());
        assertEquals(null, ws.getCollectionProtocol());
    }

}
