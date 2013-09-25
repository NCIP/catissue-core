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

import java.util.Calendar;

import edu.wustl.catissuecore.domain.ReportedProblem;


public class ReportedProblemFactory implements InstanceFactory<ReportedProblem>
{
	private static ReportedProblemFactory rpFactory;

	private ReportedProblemFactory() {
		super();
	}

	public static synchronized ReportedProblemFactory getInstance()
	{
		if(rpFactory == null) {
			rpFactory = new ReportedProblemFactory();
		}
		return rpFactory;
	}
	public ReportedProblem createClone(ReportedProblem t)
	{

		return null;
	}

	public ReportedProblem createObject()
	{
		ReportedProblem reportedProblem=new ReportedProblem();
		initDefaultValues(reportedProblem);
		return reportedProblem;
	}

	public void initDefaultValues(ReportedProblem t)
	{
		t.setReportedDate(Calendar.getInstance().getTime());
	}

}
