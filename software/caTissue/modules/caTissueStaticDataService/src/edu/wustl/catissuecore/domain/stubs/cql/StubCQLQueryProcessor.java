/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.domain.stubs.cql;

import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.MalformedQueryException;
import gov.nih.nci.cagrid.data.QueryProcessingException;
import gov.nih.nci.cagrid.data.cql.CQLQueryProcessor;

import java.util.Properties;

/** 
 *  StubCQLQueryProcessor
 *  This CQL Query Processor is provided as a stub to begin implementing CQL against your
 *  backend data source.  If another CQL query processor implementation is used, 
 *  this file may be safely ignored.
 *  If this class is deleted, the introduce service synchronization process
 *  will recreate it.
 *  
 * @deprecated The stub CQL query processor is a placeholder to provide a starting point for
 * implementation of CQL against a backend data source.
 */
public class StubCQLQueryProcessor extends CQLQueryProcessor {

	public CQLQueryResults processQuery(CQLQuery cqlQuery) throws MalformedQueryException, QueryProcessingException {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("processQuery() is not yet implemented");
	}


	public Properties getRequiredParameters() {
		// TODO Auto-generated method stub
		return new Properties();
	}
}
