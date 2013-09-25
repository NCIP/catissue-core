/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.domain.util;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.data.DataServiceConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ion C. Olaru
 *         Date: 5/1/12 -1:29 PM
 */
public class CQLWriter {

    public static void writeCQL(CQLQuery q) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS");
            String fileName = "CQL_Query_" + formatter.format(new Date()) + ".xml";
            StringWriter writer = new StringWriter();
            File toFile = new File(System.getProperty("java.io.tmpdir"), fileName);
            FileWriter fw = new FileWriter(toFile);
            Utils.serializeObject(q, DataServiceConstants.CQL_QUERY_QNAME, fw);
            System.out.println("Writing a file to: " + toFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
