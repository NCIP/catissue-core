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
