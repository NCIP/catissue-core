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

import edu.wustl.catissuecore.domain.pathology.ReportLoaderQueue;


public class ReportLoaderQueueFactory implements InstanceFactory<ReportLoaderQueue>
{
	private static ReportLoaderQueueFactory reportLoaderQueueFactory;

	private ReportLoaderQueueFactory()
	{
		super();
	}

	public static synchronized ReportLoaderQueueFactory getInstance()
	{
		if(reportLoaderQueueFactory==null){
			reportLoaderQueueFactory = new ReportLoaderQueueFactory();
		}
		return reportLoaderQueueFactory;
	}
	public ReportLoaderQueue createClone(ReportLoaderQueue t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public ReportLoaderQueue createObject()
	{
		ReportLoaderQueue loaderQueue= new ReportLoaderQueue();
		initDefaultValues(loaderQueue);
		return loaderQueue;
	}

	public void initDefaultValues(ReportLoaderQueue t)
	{
		// TODO Auto-generated method stub

	}

}
