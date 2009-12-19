
package edu.wustl.catissuecore.client;

import edu.wustl.bulkoperator.BulkOperator;
import edu.wustl.bulkoperator.DataList;
import edu.wustl.common.jobmanager.Job;
import edu.wustl.common.jobmanager.JobStatusListener;

public class BulkOperatorJob extends Job
{
	private BulkOperator bulkOperator = null;
	private String loginName = null;
	private String password = null;
	private String className = null;
	private DataList dataList = null;

	public BulkOperatorJob(String operationName, String loginName, String password, String userId,
			BulkOperator bulkOperator, DataList dataList, String className,
			JobStatusListener jobStatusListener)
	{
		super(operationName, userId, jobStatusListener);
		this.bulkOperator = bulkOperator;
		this.loginName = loginName;
		this.password = password;
		this.className = className;
		this.dataList = dataList;
	}

	@Override
	public void doJob()
	{
		try
		{
			bulkOperator.startProcess(getJobName(), this.loginName, this.password, getJobStartedBy(),
					this.dataList, this.className, this.getJobData());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}