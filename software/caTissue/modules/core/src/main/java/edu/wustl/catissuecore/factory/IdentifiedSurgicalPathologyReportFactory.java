/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;


public class IdentifiedSurgicalPathologyReportFactory
		implements
			InstanceFactory<IdentifiedSurgicalPathologyReport>
{
	private static IdentifiedSurgicalPathologyReportFactory identifiedSPRFactory;

	private IdentifiedSurgicalPathologyReportFactory()
	{
		super();
	}

	public static synchronized IdentifiedSurgicalPathologyReportFactory getInstance()
	{
		if(identifiedSPRFactory==null){
			identifiedSPRFactory = new IdentifiedSurgicalPathologyReportFactory();
		}
		return identifiedSPRFactory;
	}

	public IdentifiedSurgicalPathologyReport createClone(IdentifiedSurgicalPathologyReport t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public IdentifiedSurgicalPathologyReport createObject()
	{
		IdentifiedSurgicalPathologyReport pathologyReport=new IdentifiedSurgicalPathologyReport();
		initDefaultValues(pathologyReport);
		return pathologyReport;
	}

	public void initDefaultValues(IdentifiedSurgicalPathologyReport t)
	{
		// TODO Auto-generated method stub

	}

}
