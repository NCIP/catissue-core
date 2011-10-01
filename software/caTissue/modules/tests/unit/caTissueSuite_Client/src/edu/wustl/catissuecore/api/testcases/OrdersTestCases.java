package edu.wustl.catissuecore.api.testcases;

import edu.wustl.catissuecore.domain.*;
import gov.nih.nci.system.applicationservice.ApplicationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * @author Ion C. Olaru
 *         Date: 7/27/11 - 12:03 PM
 */
public class OrdersTestCases extends AbstractCaCoreApiTestCasesWithRegularAuthentication {

    public void testNewOrderWithSpecimenOrderItem() throws ApplicationException {
        OrderDetails order = new OrderDetails();
        Collection items = new HashSet();
        SpecimenOrderItem oi = new SpecimenOrderItem();

        oi.setDescription("OI Description " + UniqueKeyGeneratorUtil.getUniqueKey());
        oi.setRequestedQuantity(2.9);
        oi.setOrderDetails(order);
        oi.setStatus("New");

        items.add(oi);

        order.setComment("Order Comments " + UniqueKeyGeneratorUtil.getUniqueKey());
        order.setName("Order name " + UniqueKeyGeneratorUtil.getUniqueKey());
        order.setOrderItemCollection(items);
        order.setStatus("New");

        DistributionProtocol dp = getDistributionProtocolById((long)2);
        order.setDistributionProtocol(dp);

        OrderDetails o = insert(order);

        assertTrue(o.getId() > 0);
        log.debug(">>> INSERTED ORDER: " + o.getId());
        System.out.println(">>> INSERTED ORDER: " + o.getId());
    }

    public void testNewOrderWithNewSpecimenOrderItem() throws ApplicationException {
        OrderDetails order = new OrderDetails();
        Collection items = new HashSet();
        NewSpecimenOrderItem oi = new NewSpecimenOrderItem();

        oi.setDescription("OI Description " + UniqueKeyGeneratorUtil.getUniqueKey());
        oi.setRequestedQuantity(2.9);
        oi.setOrderDetails(order);
        oi.setStatus("New");
        oi.setSpecimenClass("DNA");
        oi.setSpecimenType("Fluid");

        items.add(oi);

        order.setComment("Order Comments " + UniqueKeyGeneratorUtil.getUniqueKey());
        order.setName("Order name " + UniqueKeyGeneratorUtil.getUniqueKey());
        order.setOrderItemCollection(items);
        order.setStatus("New");

        DistributionProtocol dp = getDistributionProtocolById((long)2);
        order.setDistributionProtocol(dp);

        OrderDetails o = insert(order);

        assertTrue(o.getId() > 0);
        log.debug(">>> INSERTED ORDER: " + o.getId());
        System.out.println(">>> INSERTED ORDER: " + o.getId());
    }

    public void testNewOrderWithDerivedSpecimenOrderItem() throws ApplicationException {
        OrderDetails order = new OrderDetails();
        Collection items = new HashSet();
        DerivedSpecimenOrderItem oi = new DerivedSpecimenOrderItem();

        Specimen s = new Specimen();
        s.setId((long)3720);

        oi.setDescription("OI Description " + UniqueKeyGeneratorUtil.getUniqueKey());
        oi.setRequestedQuantity(2.9);
        oi.setOrderDetails(order);
        oi.setStatus("New");
        oi.setSpecimenClass("DNA");
        oi.setSpecimenType("Fluid");
        oi.setParentSpecimen(s);

        items.add(oi);

        order.setComment("Order Comments " + UniqueKeyGeneratorUtil.getUniqueKey());
        order.setName("Order name " + UniqueKeyGeneratorUtil.getUniqueKey());
        order.setOrderItemCollection(items);
        order.setStatus("New");

        DistributionProtocol dp = getDistributionProtocolById((long)2);
        order.setDistributionProtocol(dp);

        OrderDetails o = insert(order);

        assertTrue(o.getId() > 0);
        log.debug(">>> INSERTED ORDER: " + o.getId());
        System.out.println(">>> INSERTED ORDER: " + o.getId());
    }

    public void testNewEmptyOrder() throws ApplicationException {

        OrderDetails order = new OrderDetails();
        order.setComment("Order Comments " + UniqueKeyGeneratorUtil.getUniqueKey());
        order.setName("Order name " + UniqueKeyGeneratorUtil.getUniqueKey());
        order.setStatus("New");

        DistributionProtocol dp = getDistributionProtocolById((long)2);
        order.setDistributionProtocol(dp);

        OrderDetails o = insert(order);

        assertTrue(o.getId() > 0);
        log.debug(">>> INSERTED ORDER: " + o.getId());
        System.out.println(">>> INSERTED ORDER: " + o.getId());
    }

    public void testInsertDistributionProtocol() throws ApplicationException {
        DistributionProtocol dp = new DistributionProtocol();

        User u = new User();
        u.setId((long)1);

        dp.setDistributionSpecimenRequirementCollection(new HashSet());
        dp.setPrincipalInvestigator(u);
        dp.setActivityStatus("Active");
        dp.setTitle("DP Title" + UniqueKeyGeneratorUtil.getUniqueKey());
        dp.setShortTitle("DP Short Title");
        dp.setStartDate(new Date());
        dp = insert(dp);
        assertTrue(dp.getId() > 0);
        System.out.println(dp.getId());
    }

}
