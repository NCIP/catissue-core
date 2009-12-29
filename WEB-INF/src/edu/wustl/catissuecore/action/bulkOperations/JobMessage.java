package edu.wustl.catissuecore.action.bulkOperations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.wustl.bulkoperator.jobmanager.JobDetails;

/**
 *This class will carry all the messages. 
 * @author kalpana_thakur
 *
 */
public class JobMessage implements Serializable
{
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * messages.
	 */
	private List<String >messages = new ArrayList<String>();
	/**
	 * is operation successfull.
	 */
	private boolean isOperationSuccessfull;
	/**
	 * operationCalled which operation called.
	 */
	private String operationCalled;
	/**
	 * Job id.
	 */
	private Long jobId;
	/**
	 * Job data.
	 */
	private JobDetails jobData;

	/**
	 * @return the jobId
	 */
	public Long getJobId()
	{
		return jobId;
	}
	/**
	 * @return the jobData
	 */
	public JobDetails getJobData()
	{
		return jobData;
	}
	/**
	 * @param jobData the jobData to set
	 */
	public void setJobData(JobDetails jobData)
	{
		this.jobData = jobData;
	}
	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(Long jobId)
	{
		this.jobId = jobId;
	}
	/**
	 * @return the messages
	 */
	public String getMessages()
	{
		StringBuffer strBuffer = new StringBuffer();
		Iterator<String> itr = messages.iterator();

		while(itr.hasNext())
		{
			if(!isOperationSuccessfull)
			{
				strBuffer.append("Error :: ");
			}
			strBuffer.append(itr.next().toString()).append("\n");
		}

		return strBuffer.toString();
	}
	/**
	 * @return the isOperationSuccessfull
	 */
	public boolean isOperationSuccessfull()
	{
		return isOperationSuccessfull;
	}
	/**
	 * @return the operationCalled
	 */
	public String getOperationCalled()
	{
		return operationCalled;
	}

	/**
	 * @param messages the messages to set
	 */
	public void setMessages(List<String> messages)
	{
		this.messages = messages;
	}
	/**
	 * @param isOperationSuccessfull the isOperationSuccessfull to set
	 */
	public void setOperationSuccessfull(boolean isOperationSuccessfull)
	{
		this.isOperationSuccessfull = isOperationSuccessfull;
	}
	/**
	 * @param operationCalled the operationCalled to set
	 */
	public void setOperationCalled(String operationCalled)
	{
		if(operationCalled == null)
		{
			this.operationCalled = "";
		}
		else
		{
			this.operationCalled = operationCalled.toUpperCase();
		}
	}

	/**
	 * @param  message the messages.
	 */
	public void addMessage(String message)
	{
		messages.add(message);
	}

}
