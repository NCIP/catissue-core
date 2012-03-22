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

public class PopulatedAssociation extends TestBase {
    
    /**
     * Test that a specimen collection group retrieved from a grid service contains populated 
     * relations to the cells that are part of the collection group.
     * 
     * INCOMPLETE
     * 
     * @throws Exception
     */
    public void testSpecimenCollectionGroupPopulated() throws Exception {
        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        Specimen specimen = insertSpecimen(scg,CellSpecimen.class,"Cell","Cryopreserved Cells");
        System.out.println(String.format("--> Specimen inserted: %d", specimen.getIdentifier()));
        
        CQLQuery q = createSpecimenQuery(specimen,"edu.wustl.catissuecore.domain.CellSpecimen");
        CQLQueryResults results = client.executeQuery(q);
        assertTrue("Has one result.", results.getObjectResult().length == 1);
        System.out.println(serialize(results));
        Element objectResult = getResultsElement(results);
        assertTrue(objectResult.getChildren().size() == 1);
        Element specElement = (Element) objectResult.getChildren().get(0);
        for (Element e : (List<Element>)objectResult.getChildren()) {
            System.out.println(e.getName());
        }
        assertTrue("Specimen IDs match",Long.parseLong(specElement.getAttributeValue("id")) == specimen.getIdentifier());
        Element scgElement = objectResult.getChild("specimenCollectionGroup",specElement.getNamespace());
        assertTrue("Has SCG association",scgElement != null);
        scgElement = scgElement.getChild("SpecimenCollectionGroup",specElement.getNamespace());
        assertTrue("Has Specimen Collection Group",scgElement != null);
        assertTrue("SCG IDs match",Long.parseLong(scgElement.getAttributeValue("id")) == scg.getIdentifier());
    }
}
