import edu.wustl.catissuecore.domain.ws.OrderDetails;
import edu.wustl.catissuecore.domain.client.Fixtures;
import edu.wustl.catissuecore.domain.service.WAPIUtility;

import java.util.HashSet;

/**
 * @author Ion C. Olaru
 *         Date: 7/27/11 - 4:58 PM
 */
public class OrderDetailsConversionTest extends BaseConversionTest {

    @Override
    public void testWsToDomain() {
        OrderDetails ws = Fixtures.createOrderDetailsWithSOI();
        edu.wustl.catissuecore.domain.OrderDetails d = (edu.wustl.catissuecore.domain.OrderDetails)WAPIUtility.convertWsToDomain(ws);
        assertEquals("Comments: WS Java client.", d.getComment());
        assertEquals(3, d.getOrderItemCollection().size());
        assertEquals(HashSet.class, d.getOrderItemCollection().getClass());
    }

    public void testWsToDomainOrderWithExistingSOI() {
        OrderDetails ws = Fixtures.createOrderWithExistingSpecimenOrderItem(2, 2);
        edu.wustl.catissuecore.domain.OrderDetails d = (edu.wustl.catissuecore.domain.OrderDetails)WAPIUtility.convertWsToDomain(ws);
        assertEquals("Comments: WS Java client.", d.getComment());
        assertEquals(1, d.getOrderItemCollection().size());
        assertEquals(HashSet.class, d.getOrderItemCollection().getClass());
        Object[] array = d.getOrderItemCollection().toArray();
        assertEquals(edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem.class, array[0].getClass());
        edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem oi = (edu.wustl.catissuecore.domain.ExistingSpecimenOrderItem)array[0];
    }

    public void testWsToDomainOrderWithDerivedSOI() {
        OrderDetails ws = Fixtures.createOrderWithDerivedSpecimenOrderItem();
        edu.wustl.catissuecore.domain.OrderDetails d = (edu.wustl.catissuecore.domain.OrderDetails)WAPIUtility.convertWsToDomain(ws);
        assertEquals("Comments: WS Java client.", d.getComment());
        assertEquals(1, d.getOrderItemCollection().size());
        assertEquals(HashSet.class, d.getOrderItemCollection().getClass());
        Object[] array = d.getOrderItemCollection().toArray();
        assertEquals(edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem.class, array[0].getClass());
        edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem oi = (edu.wustl.catissuecore.domain.DerivedSpecimenOrderItem)array[0];
        assertEquals("Tissue", oi.getSpecimenClass());
        assertEquals("DNA", oi.getSpecimenType());
        assertNotNull(oi.getParentSpecimen());
        assertEquals("Specimen Array OI Name", oi.getNewSpecimenArrayOrderItem().getName());
    }

    @Override
    public void testDomainToWs() {
        edu.wustl.catissuecore.domain.SpecimenOrderItem o1 = new edu.wustl.catissuecore.domain.SpecimenOrderItem();
        edu.wustl.catissuecore.domain.SpecimenOrderItem o2 = new edu.wustl.catissuecore.domain.SpecimenOrderItem();
    }
}
