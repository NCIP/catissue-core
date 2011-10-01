import edu.wustl.catissuecore.domain.ws.*;
import edu.wustl.catissuecore.domain.service.WAPIUtility;

/**
 * @author Ion C. Olaru
 *         Date: 8/4/11 - 5:01 PM
 */
public class SpecimenOrderItemTest extends BaseConversionTest {

    @Override
    public void testWsToDomain() {
        NewSpecimenOrderItem ws = new NewSpecimenOrderItem();
        ws.setIdentifier(12);
        ws.setSpecimenClass("Tissue");
        ws.setSpecimenType("DNA");
        ws.setNewSpecimenArrayOrderItem(new SpecimenOrderItemNewSpecimenArrayOrderItem());
        ws.getNewSpecimenArrayOrderItem().setNewSpecimenArrayOrderItem(new NewSpecimenArrayOrderItem());
        ws.getNewSpecimenArrayOrderItem().getNewSpecimenArrayOrderItem().setName("Specimen Array OI Name");

        edu.wustl.catissuecore.domain.NewSpecimenOrderItem d = (edu.wustl.catissuecore.domain.NewSpecimenOrderItem)WAPIUtility.convertWsToDomain(ws);

        assertEquals("Tissue", d.getSpecimenClass());
        assertEquals("DNA", d.getSpecimenType());
        assertEquals(12, d.getId().longValue());
        assertEquals("Specimen Array OI Name", d.getNewSpecimenArrayOrderItem().getName());
    }

    public void testWsToDomainWithDerivedSpecimenOrderItem() {
        Specimen s = new Specimen();
        s.setIdentifier(3941);

        DerivedSpecimenOrderItem ws = new DerivedSpecimenOrderItem();
        ws.setIdentifier(12);
        ws.setSpecimenClass("Tissue");
        ws.setSpecimenType("DNA");
        ws.setParentSpecimen(new DerivedSpecimenOrderItemParentSpecimen());
        ws.getParentSpecimen().setSpecimen(s);

        ws.setNewSpecimenArrayOrderItem(new SpecimenOrderItemNewSpecimenArrayOrderItem());
        ws.getNewSpecimenArrayOrderItem().setNewSpecimenArrayOrderItem(new NewSpecimenArrayOrderItem());
        ws.getNewSpecimenArrayOrderItem().getNewSpecimenArrayOrderItem().setName("Specimen Array OI Name");

        edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem d = (edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem)WAPIUtility.convertWsToDomain(ws);

        assertEquals("Tissue", d.getSpecimenClass());
        assertEquals("DNA", d.getSpecimenType());
        assertEquals(12, d.getId().longValue());
        assertEquals("Specimen Array OI Name", d.getNewSpecimenArrayOrderItem().getName());
        assertEquals(3941, d.getParentSpecimen().getId().longValue());
    }

    @Override
    public void testDomainToWs() {

    }
}
