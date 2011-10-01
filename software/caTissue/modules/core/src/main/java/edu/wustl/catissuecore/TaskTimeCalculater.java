
package edu.wustl.catissuecore;

import edu.wustl.common.util.logger.Logger;

/**
 * This class provides functionality to log performance of a given task.
 * @author abhijit_naik
 *
 */
public final class TaskTimeCalculater
{

	private static final int MILLIS_IN_A_SECOND = 1000;
	private static final boolean IS_LOGGING_ON = false;
	private static TaskTimeCalculater nullTaskCalculater = new TaskTimeCalculater();
	private long startTime;
	private long endTime;
	private String taskName;
	private transient Logger logger;

	/**
	 * Constructs new object for a given class object.
	 * @param clazz class object for which task time calculator object to
	 * be created.
	 */
	protected TaskTimeCalculater(Class clazz)
	{
		this.logger = Logger.getCommonLogger(clazz);
	}

	/**
	 * Default private constructor. Used for internal purpose only.
	 */
	private TaskTimeCalculater()
	{

	}

	/**
	 * @return name of the task whose performance is about to calculate.
	 */
	protected String getTaskName()
	{
		return this.taskName;
	}

	/**
	 * @param task name of the task whose performance is about to calculate.
	 */
	protected void setTaskName(String task)
	{
		this.taskName = task;
	}

	/**
	 * @return task start time
	 */
	protected long getStartTime()
	{
		return this.startTime;
	}

	/**
	 * @param timeStamp task start time.
	 */
	protected void setStartTime(long timeStamp)
	{
		this.startTime = timeStamp;
	}

	/**
	 * @return task end time
	 */
	protected long getEndTime()
	{
		return this.endTime;
	}

	/**
	 * @param timeStamp task end time
	 */
	protected void setEndTime(long timeStamp)
	{
		this.endTime = timeStamp;
	}

	/**
	 * @return formatted message with total time taken for the task.
	 */
	public String getTimeTaken()
	{
		final long timeTaken = this.getEndTime() - this.getStartTime();
		final String logString = this.getTaskName() + "," + timeTaken;
		this.logger.info(logString);
		return logString;
	}

	/**
	 * @return formatted message with total time taken in milliseconds
	 *  for the task.
	 */
	public String getTimeTakenInSecs()
	{
		String logString;
		if (!IS_LOGGING_ON)
		{
			logString = "";
		}
		else
		{
			double timeTaken = ((double) this.getEndTime()) - ((double) this.getStartTime());
			timeTaken /= MILLIS_IN_A_SECOND;
			logString = this.getTaskName() + "," + timeTaken;
			this.logger.info(logString);
		}
		return logString;
	}

	/**
	 * A static function to be used for notifying beginning of the task.
	 * @param taskName name of the task.
	 * @return new object of TaskTimeCalculater for the given task.
	 */
	public static TaskTimeCalculater startTask(final String taskName)
	{
		return startTask(taskName, TaskTimeCalculater.class);
	}

	/**
	 * A static function to be used for notifying beginning of the task.
	 * @param taskName name of the task.
	 * @param clazz class object for which logger object to be created.
	 * @return new object of TaskTimeCalculater for the given task.
	 */
	public static TaskTimeCalculater startTask(String taskName, Class clazz)
	{
		TaskTimeCalculater taskTimeCalculater = nullTaskCalculater;
		if (IS_LOGGING_ON)
		{
			taskTimeCalculater = new TaskTimeCalculater(clazz);
			taskTimeCalculater.setTaskName(taskName);
			taskTimeCalculater.setStartTime(System.currentTimeMillis());
		}
		return taskTimeCalculater;
	}

	/**
	 * A static function used to notify the task is finish. This function then
	 * calculates the total time taken and returns the formatted string.
	 * @param taskCalculater taskTimeCalculater object representing a task
	 * which is finished.
	 * @return formatted string of total time taken by the task.
	 */
	public static String endTask(final TaskTimeCalculater taskCalculater)
	{
		String timeTaken;
		if (IS_LOGGING_ON)
		{
			taskCalculater.setEndTime(System.currentTimeMillis());
			timeTaken = taskCalculater.getTimeTaken();
		}
		else
		{
			timeTaken = "";
		}
		return timeTaken;
	}
}
