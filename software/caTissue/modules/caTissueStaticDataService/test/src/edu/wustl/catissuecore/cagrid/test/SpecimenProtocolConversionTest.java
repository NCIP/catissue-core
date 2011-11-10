package edu.wustl.catissuecore.cagrid.test;
import edu.wustl.catissuecore.domain.SpecimenProtocol;
import edu.wustl.catissuecore.domain.ws.DistributionProtocol;
import edu.wustl.catissuecore.domain.service.WAPIUtility;

/**
 * @author Ion C. Olaru
 *         Date: 8/2/11 - 3:20 PM
 */
public class SpecimenProtocolConversionTest extends BaseConversionTest {

    @Override
    public void testWsToDomain() {
        DistributionProtocol ws = new DistributionProtocol();
        ws.setIdentifier(19);
        ws.setActivityStatus("Active");

        edu.wustl.catissuecore.domain.DistributionProtocol d = (edu.wustl.catissuecore.domain.DistributionProtocol) WAPIUtility.convertWsToDomain(ws);

        assertEquals(19, d.getId().longValue());

    }

    public void testWsToDomainZeroId() {
        DistributionProtocol ws = new DistributionProtocol();
        ws.setActivityStatus("Active");

        edu.wustl.catissuecore.domain.DistributionProtocol d = (edu.wustl.catissuecore.domain.DistributionProtocol) WAPIUtility.convertWsToDomain(ws);

        assertEquals(null, d.getId());

    }

    @Override
    public void testDomainToWs() {

    }
}
