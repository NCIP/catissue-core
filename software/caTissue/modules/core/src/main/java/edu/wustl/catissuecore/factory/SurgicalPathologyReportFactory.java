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

import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;


public class SurgicalPathologyReportFactory implements InstanceFactory<SurgicalPathologyReport>
{
	private static SurgicalPathologyReportFactory sprFactory;

	private SurgicalPathologyReportFactory()
	{
		super();
	}

	public static synchronized SurgicalPathologyReportFactory getInstance()
	{
		if(sprFactory==null)
		{
			sprFactory = new SurgicalPathologyReportFactory();
		}
		return sprFactory;
	}

	public SurgicalPathologyReport createClone(SurgicalPathologyReport t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public SurgicalPathologyReport createObject()
	{
		SurgicalPathologyReport spr=new SurgicalPathologyReport();
		initDefaultValues(spr);
		return spr;
	}

	public void initDefaultValues(SurgicalPathologyReport t)
	{
		// TODO Auto-generated method stub

	}

}
