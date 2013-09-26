/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.domain.utils;

import edu.wustl.catissuecore.domain.util.CQLWriter;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import junit.framework.TestCase;

/**
 * @author Ion C. Olaru
 *         Date: 5/1/12 -1:41 PM
 */
public class CQLWriterTest extends TestCase {

    public void testSerializeToAFile() {
        CQLWriter.writeCQL(new CQLQuery());
    }

}
