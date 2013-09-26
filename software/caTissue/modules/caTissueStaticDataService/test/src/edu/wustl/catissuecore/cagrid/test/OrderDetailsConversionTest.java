/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.cagrid.test;
import edu.wustl.catissuecore.domain.ws.OrderDetails;
import edu.wustl.catissuecore.domain.client.Fixtures;
import edu.wustl.catissuecore.domain.service.WAPIUtility;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

import java.rmi.RemoteException;
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

    public void testWsToDomainOrderWithDerivedSOI() throws QueryProcessingExceptionType, RemoteException {
        OrderDetails ws = Fixtures.createOrderWithDerivedSpecimenOrderItem(null);
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
        assertTrue(oi.getNewSpecimenArrayOrderItem().getName().startsWith("Specimen Array OI Name"));
    }

    @Override
    public void testDomainToWs() {
        edu.wustl.catissuecore.domain.SpecimenOrderItem o1 = new edu.wustl.catissuecore.domain.SpecimenOrderItem();
        edu.wustl.catissuecore.domain.SpecimenOrderItem o2 = new edu.wustl.catissuecore.domain.SpecimenOrderItem();
    }
}
