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

import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;


public class PathologyReportReviewParameterFactory
		implements
			InstanceFactory<PathologyReportReviewParameter>
{
	private static PathologyReportReviewParameterFactory pathReportReviewEventParamFactory;

	private PathologyReportReviewParameterFactory()
	{
		super();
	}

	public static synchronized PathologyReportReviewParameterFactory getInstance()
	{
		if(pathReportReviewEventParamFactory==null){
			pathReportReviewEventParamFactory = new PathologyReportReviewParameterFactory();
		}
		return pathReportReviewEventParamFactory;
	}


	public PathologyReportReviewParameter createClone(PathologyReportReviewParameter t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public PathologyReportReviewParameter createObject()
	{
		PathologyReportReviewParameter parameter= new PathologyReportReviewParameter();
		initDefaultValues(parameter);
		return parameter;
	}

	public void initDefaultValues(PathologyReportReviewParameter t)
	{
		// TODO Auto-generated method stub

	}

}
