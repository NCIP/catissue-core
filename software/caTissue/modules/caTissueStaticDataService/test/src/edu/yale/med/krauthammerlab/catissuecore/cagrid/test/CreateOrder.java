package edu.yale.med.krauthammerlab.catissuecore.cagrid.test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.cagrid.cql2.AttributeValue;
import org.cagrid.cql2.BinaryPredicate;
import org.cagrid.cql2.CQLAttribute;
import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql2.CQLTargetObject;
import org.cagrid.cql2.results.CQLQueryResults;
import org.jdom.Element;

import edu.wustl.catissuecore.domain.client.ClientRunAll;
import edu.wustl.catissuecore.domain.client.Fixtures;
import edu.wustl.catissuecore.domain.ws.CellSpecimen;
import edu.wustl.catissuecore.domain.ws.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ws.DistributionProtocol;
import edu.wustl.catissuecore.domain.ws.ExistingSpecimenOrderItem;
import edu.wustl.catissuecore.domain.ws.ExistingSpecimenOrderItemSpecimen;
import edu.wustl.catissuecore.domain.ws.FluidSpecimen;
import edu.wustl.catissuecore.domain.ws.MolecularSpecimen;
import edu.wustl.catissuecore.domain.ws.OrderDetails;
import edu.wustl.catissuecore.domain.ws.OrderDetailsDistributionProtocol;
import edu.wustl.catissuecore.domain.ws.OrderDetailsOrderItemCollection;
import edu.wustl.catissuecore.domain.ws.Specimen;
import edu.wustl.catissuecore.domain.ws.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.ws.TissueSpecimen;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

public class CreateOrder extends TestBase {

    public static OrderDetails createOrder(DistributionProtocol dp) {
        OrderDetails od = new OrderDetails();
        od.setComment("Comments: Grid client test.");
        od.setName("Order name XYZ");
        od.setStatus("New");
        od.setRequestedDate(Calendar.getInstance());
        
        od.setOrderItemCollection(new OrderDetailsOrderItemCollection());

        od.setDistributionProtocol(new OrderDetailsDistributionProtocol());
        od.getDistributionProtocol().setDistributionProtocol(dp);

        return od;
    }

    /**
     * Create an order using a derived specimen order item.
     * @throws Exception
     */
    public void testCreateOrderWithDerivedSpecimenOrderItem() throws Exception {
        OrderDetails od = Fixtures.createOrderWithDerivedSpecimenOrderItem();
        od = (OrderDetails)client.insert(od);
    }
    
    /** 
     * Create an order using a specimen order item.
     * @throws Exception
     */
    public void testCreateOrderDetailsWithSOI() throws Exception {
        OrderDetails od = Fixtures.createOrderDetailsWithSOI();
        od = (OrderDetails)client.insert(od);
    }
    
    public OrderDetails order(List<Specimen> specimens) throws QueryProcessingExceptionType, RemoteException {
        // Insert a new Distribution Protocol
        //DistributionProtocol dp = Fixtures.createDistributionProtocol();
        //dp = (DistributionProtocol)client.insert(dp);
        //System.out.println("--> Distribution Protocol inserted: " + dp.getIdentifier());

        ExistingSpecimenOrderItem[] items = new ExistingSpecimenOrderItem[specimens.size()];
        for (int i=0; i< items.length; ++i) {
            ExistingSpecimenOrderItem esoi = new ExistingSpecimenOrderItem();
            esoi.setSpecimen(new ExistingSpecimenOrderItemSpecimen());
            esoi.getSpecimen().setSpecimen(specimens.get(i));
            esoi.setStatus("New");
            esoi.setRequestedQuantity(0.2);
            items[i] = esoi;
            
        }
        
        // Insert an order
        OrderDetails od = new OrderDetails();
        od.setComment("Comments: Grid client test.");
        od.setName("Order name XYZ");
        od.setStatus("New");
        od.setRequestedDate(Calendar.getInstance());
        
        od.setOrderItemCollection(new OrderDetailsOrderItemCollection());

        //od.setDistributionProtocol(new OrderDetailsDistributionProtocol());
        //od.getDistributionProtocol().setDistributionProtocol(Fixtures.createDistributionProtocol());
        
        od.getOrderItemCollection().setOrderItem(items);

        od = (OrderDetails)client.insert(od);
        System.out.println("--> OrderDetails inserted: " + od.getIdentifier());

        return od;
    }

    protected CQLQuery createOrderQuery(OrderDetails od) {
        CQLQuery q = new CQLQuery();
        CQLTargetObject targetObject = new CQLTargetObject();
        targetObject.setClassName("edu.wustl.catissuecore.domain.OrderDetails");
    
        CQLAttribute att = new CQLAttribute();
        
        att.setBinaryPredicate(BinaryPredicate.EQUAL_TO);
        att.setName("id");
        AttributeValue val = new AttributeValue();
        val.setLongValue(od.getIdentifier());
        att.setAttributeValue(val);
        targetObject.setCQLAttribute(att);
        q.setCQLTargetObject(targetObject);
        //System.out.println(serialize(q));
        return q;
    }

    /** 
      * Create a simple distribution protocol.
      */
    public void testCreateDistributionProtocol() throws Exception {
        DistributionProtocol dp = Fixtures.createDistributionProtocol();
        dp = (DistributionProtocol)client.insert(dp);
        System.out.println("--> Distribution Protocol inserted: " + dp.getIdentifier());

        CQLQuery q = new CQLQuery();
        CQLTargetObject targetObject = new CQLTargetObject();
        targetObject.setClassName("edu.wustl.catissuecore.domain.DistributionProtocol");
    
        CQLAttribute att = new CQLAttribute();
        
        att.setBinaryPredicate(BinaryPredicate.EQUAL_TO);
        att.setName("id");
        AttributeValue val = new AttributeValue();
        val.setLongValue(dp.getIdentifier());
        att.setAttributeValue(val);
        targetObject.setCQLAttribute(att);
        q.setCQLTargetObject(targetObject);

        CQLQueryResults results = client.executeQuery(q);
        assertTrue(results.getObjectResult().length == 1);
    }

    /** 
     * Create an order for one of each class of specimens with a simple distribution protocol.
     */
    public void testCreateOrder() throws Exception {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        List<Specimen> specimens = new ArrayList<Specimen>();
        specimens.add(insertSpecimen(scg,MolecularSpecimen.class,"Molecular","DNA"));
        specimens.add(insertSpecimen(scg,FluidSpecimen.class,"Fluid","Whole Blood"));
        specimens.add(insertSpecimen(scg,CellSpecimen.class,"Cell","Cryopreserved Cells"));
        specimens.add(insertSpecimen(scg,TissueSpecimen.class,"Tissue","Frozen Tissue"));

        OrderDetails od = order(specimens);
        
        CQLQuery q = createOrderQuery(od);
        
        CQLQueryResults results = client.executeQuery(q);
        assertTrue(results.getObjectResult().length == 1);
        Element e = this.getResultsElement(results);
        
        
    }


}
