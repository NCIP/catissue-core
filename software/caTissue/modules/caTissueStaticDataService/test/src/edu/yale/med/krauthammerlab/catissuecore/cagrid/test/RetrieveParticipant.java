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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;

import edu.wustl.catissuecore.domain.client.Catissue_cacoreClient;
import edu.wustl.catissuecore.domain.client.ClientRunAll;
import junit.framework.TestCase;
import org.globus.gsi.GlobusCredential;
import org.cagrid.cql.utilities.CQL2SerializationUtil;
import org.cagrid.cql2.CQLQuery;
import org.cagrid.cql2.results.CQLQueryResults;

public class RetrieveParticipant extends TestCase {

    String url = "https://sbhost07-vm05.semanticbits.com:9443/wsrf/services/cagrid/Catissue_cacore";
    Catissue_cacoreClient client;
    GlobusCredential cred;
    CQLQuery query;
    String cqlFileName = "cqls/Query205.xml";

    /**
     * Test that a participant, collection protocol, and registration can be created via the WAPI 
     * and the participant can be retrieved.
     * @throws Throwable
     */
    public void testRetrieveParticipant() throws Throwable {

        CQLQueryResults result = client.executeQuery(query);
        
        this.assertTrue("Has at least one result.", result.getObjectResult().length >0);
    }

    @Override
    protected void setUp() throws Exception {
        
        if (url.startsWith("https")) {
            cred = GlobusCredential.getDefaultCredential();

            client = new Catissue_cacoreClient(url, cred);
        } else {
            client = new Catissue_cacoreClient(url);
        }
        ClientRunAll.client = client;
        ClientRunAll.createParticipantAndCPR();
        String strQuery = readQueryFile(cqlFileName);

        query = CQL2SerializationUtil.deserializeCql2Query(strQuery);
    }
    
    private String readQueryFile(String filename) {
        StringBuilder contents = new StringBuilder();

        BufferedReader input;
        try {
            input = new BufferedReader(new FileReader(filename));
            String line = null;

            while ((line = input.readLine()) != null) {
                contents.append(line);
                contents.append(System.getProperty("line.separator"));
            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return contents.toString();

    }
}
