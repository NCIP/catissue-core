package edu.yale.med.krauthammerlab.catissuecore.cagrid.test;

import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import edu.wustl.catissuecore.domain.ws.*;
import org.cagrid.cql2.AttributeValue;
import org.cagrid.cql2.BinaryPredicate;
import org.cagrid.cql2.CQLAttribute;
import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql2.CQLTargetObject;
import org.cagrid.cql2.results.CQLQueryResults;
import org.jdom.Element;

import edu.wustl.catissuecore.domain.client.ClientRunAll;
import edu.wustl.catissuecore.domain.client.Fixtures;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

public class CreateOrder extends TestBase {
    
    //TODO: Order based on a list of SCG IDs

    public static OrderDetails createOrder(DistributionProtocol dp) {
        OrderDetails od = new OrderDetails();
        od.setComment("Comments: Grid client test.");
        od.setName("Order name "+UUID.randomUUID().toString());
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
        OrderDetails od = Fixtures.createOrderWithDerivedSpecimenOrderItem(client);
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
        od.setName("Order name "+UUID.randomUUID().toString());
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

    public StorageContainer createStorageContainer(CollectionProtocolRegistration reg, SpecimenCollectionGroup scg) throws QueryProcessingExceptionType, RemoteException {
            StorageContainer storageContainer = new StorageContainer();
            storageContainer.setActivityStatus("Active");
            final String uuid = UUID.randomUUID().toString();
            storageContainer.setBarcode(uuid);

            ContainerCapacity capacity = new ContainerCapacity();
            Capacity capacityValue = new Capacity();
            capacityValue.setOneDimensionCapacity(BigInteger.valueOf(1000));
            capacityValue.setTwoDimensionCapacity(BigInteger.valueOf(1000));
            capacity.setCapacity(capacityValue );
            storageContainer.setCapacity(capacity );

            StorageContainerCollectionProtocolCollection collectionProtocolCollection = new StorageContainerCollectionProtocolCollection();
            collectionProtocolCollection.setCollectionProtocol(new CollectionProtocol[]{reg.getCollectionProtocol().getCollectionProtocol()});
            storageContainer.setCollectionProtocolCollection(collectionProtocolCollection);

            storageContainer.setComment(uuid);
            storageContainer.setName(uuid);

            StorageContainerStorageType storageType = new StorageContainerStorageType();
            StorageType type = new StorageType();
            type.setIdentifier(3);
            storageType.setStorageType(type );
            storageContainer.setStorageType(storageType );

            StorageContainerSite scSite = new StorageContainerSite();
            Site site = new Site();
            site.setIdentifier(1);
            scSite.setSite(site );
            storageContainer.setSite(scSite );

            storageContainer.setIdentifier(((StorageContainer)client.insert(storageContainer)).getIdentifier());
            storageContainer.setName("In Transit_Shipment Container_"+storageContainer.getIdentifier());
            System.out.println("---> Storage Container inserted:" + storageContainer.getIdentifier());

            return storageContainer;
        }
    /**
     * Create an order for one of each class of specimens with a simple distribution protocol.
     */
    public void testCreateOrder() throws Exception {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);
        StorageContainer container = createStorageContainer(reg, scg);
        
        List<Specimen> specimens = new ArrayList<Specimen>();
        specimens.add(insertSpecimen(scg,container,MolecularSpecimen.class,"Molecular","DNA"));
        specimens.add(insertSpecimen(scg,container,FluidSpecimen.class,"Fluid","Whole Blood"));
        specimens.add(insertSpecimen(scg,container,CellSpecimen.class,"Cell","Cryopreserved Cells"));
        specimens.add(insertSpecimen(scg,container,TissueSpecimen.class,"Tissue","Frozen Tissue"));

        OrderDetails od = order(specimens);
        
        CQLQuery q = createOrderQuery(od);
        
        CQLQueryResults results = client.executeQuery(q);
        assertTrue(results.getObjectResult().length == 1);
        Element e = this.getResultsElement(results);
        
        
    }


}
