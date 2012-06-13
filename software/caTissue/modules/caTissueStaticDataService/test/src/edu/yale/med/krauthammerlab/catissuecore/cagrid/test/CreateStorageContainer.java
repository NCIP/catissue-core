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
import org.springframework.util.Assert;

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
import edu.wustl.catissuecore.domain.ws.StorageContainer;
import edu.wustl.catissuecore.domain.ws.TissueSpecimen;
import gov.nih.nci.cagrid.data.faults.QueryProcessingExceptionType;

public class CreateStorageContainer extends TestBase {
    /**
     * Create a storage container.
     * @throws Exception
     */
    public void testCreateStorageContainer() throws Exception {
        Assert.notNull(makeContainer(3, 1));
    }
    

}
