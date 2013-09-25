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

import edu.wustl.catissuecore.domain.pathology.ReportSection;


public class ReportSectionFactory implements InstanceFactory<ReportSection>
{
	private static ReportSectionFactory reportSectionFactory;

	private ReportSectionFactory()
	{
		super();
	}

	public static synchronized ReportSectionFactory getInstance()
	{
		if(reportSectionFactory==null)
		{
			reportSectionFactory = new ReportSectionFactory();
		}
		return reportSectionFactory;
	}

	public ReportSection createClone(ReportSection t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ReportSection createObject()
	{
		ReportSection reportSection= new ReportSection();
		initDefaultValues(reportSection);
		return reportSection;
	}

	public void initDefaultValues(ReportSection t)
	{
		// TODO Auto-generated method stub

	}

}
