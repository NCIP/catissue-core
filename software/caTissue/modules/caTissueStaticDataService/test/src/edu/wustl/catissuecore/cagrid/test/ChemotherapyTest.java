package edu.wustl.catissuecore.cagrid.test;

//import clinical_annotation.ws.*;
import edu.wustl.catissuecore.domain.service.WAPIUtility;

/**
 * @author Ion C. Olaru
 * Date: 11/19/11 -10:11 AM
 */
public class ChemotherapyTest extends BaseConversionTest {
    @Override
    public void testWsToDomain() {
/*
        Chemotherapy ws = new Chemotherapy();
        ws.setIdentifier((long) 7);
        ws.setDose(5.6);
        clinical_annotation.Chemotherapy d = (clinical_annotation.Chemotherapy)WAPIUtility.convertWsToDomain(ws);
        assertEquals(7, d.getId().longValue());
        assertEquals(5.6, d.getDose());
*/
    }

    @Override
    public void testDomainToWs() {
/*
        clinical_annotation.Chemotherapy d = new clinical_annotation.Chemotherapy();
        d.setId(99L);
        d.setDose(1.2);
        Chemotherapy ws = (Chemotherapy)WAPIUtility.convertWsToDomain(d);
        assertEquals(99, ws.getIdentifier());
        assertEquals(0.0, ws.getDose());
*/
    }
}
