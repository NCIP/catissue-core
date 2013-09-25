/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.yale.med.krauthammerlab.catissuecore.cagrid.test;

import java.util.Iterator;

import org.cagrid.cql.utilities.iterator.CQL2QueryResultsIterator;
import org.cagrid.cql2.AttributeValue;
import org.cagrid.cql2.BinaryPredicate;
import org.cagrid.cql2.CQLAttribute;
import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql2.CQLTargetObject;
import org.cagrid.cql2.results.CQLQueryResults;
import org.globus.gsi.GlobusCredential;
import org.globus.wsrf.encoding.DeserializationException;

import edu.wustl.catissuecore.domain.client.Catissue_cacoreClient;
import edu.wustl.catissuecore.domain.client.ClientRunAll;
import edu.wustl.catissuecore.domain.ws.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.ws.FluidSpecimen;
import edu.wustl.catissuecore.domain.ws.Specimen;
import edu.wustl.catissuecore.domain.ws.SpecimenCollectionGroup;

public class DeserializationTest extends TestBase {

    /**
     * Test that a query from a caTissue grid service can be deserialized
     * using the CQL query results iterator.
     */
    public void testCreateAndRetrieveSpecimen() throws Throwable {

        CollectionProtocolRegistration reg = createParticipantAndCPR();
        SpecimenCollectionGroup scg = createSpecimenCollectionGroup(reg);

        Specimen specimen = insertSpecimen(scg, FluidSpecimen.class, "Fluid","Serum");
        System.out.println(String.format("--> Specimen inserted: %d", specimen.getIdentifier()));
        
        CQLQuery q = new CQLQuery();
        CQLTargetObject targetObject = new CQLTargetObject();
        targetObject.setClassName("edu.wustl.catissuecore.domain.FluidSpecimen");

        CQLAttribute att = new CQLAttribute();
        
        att.setBinaryPredicate(BinaryPredicate.EQUAL_TO);
        att.setName("id");
        AttributeValue val = new AttributeValue();
        val.setLongValue(specimen.getIdentifier());
        att.setAttributeValue(val);
        targetObject.setCQLAttribute(att);
        q.setCQLTargetObject(targetObject);
        
        CQLQueryResults results = client.executeQuery(q);
        assertTrue("Has one result.", results.getObjectResult().length == 1);
        try {
            Iterator iter = new CQL2QueryResultsIterator(results, 
                    getClass().getResourceAsStream("client-config.wsdd"));
            while (iter.hasNext()) {
                edu.wustl.catissuecore.domain.FluidSpecimen s 
                    = (edu.wustl.catissuecore.domain.FluidSpecimen) iter.next();
                assertTrue(s.getId() == specimen.getIdentifier());
                assertTrue("\""+s.getBarcode()+"\" == \""+specimen.getBarcode()+"\"",
                        s.getBarcode().equals(specimen.getBarcode()));
                assertTrue(s.getBarcode().equals(specimen.getLabel()));
                assertTrue("\""+s.getGlobalSpecimenIdentifier()+"\" == \""+specimen.getGlobalSpecimenIdentifier()+"\"",
                        s.getGlobalSpecimenIdentifier().equals(specimen.getGlobalSpecimenIdentifier()));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        
    }
    
}
