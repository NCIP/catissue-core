package edu.wustl.catissuecore;

import org.apache.log4j.Logger;

public class TaskTimeCalculater {

	private static boolean logPerformance = true;
	private static TaskTimeCalculater nullTaskTimeCalculater = new TaskTimeCalculater();
	private long startTime;
	private long endTime;
	private String taskName;
	Logger logger;
	protected TaskTimeCalculater(Class clazz){
		logger = Logger.getLogger(clazz);
	}
	private TaskTimeCalculater(){

	}

	protected String getTaskName(){
		return taskName;
	}
	protected void setTaskName(String task){
		this.taskName = task;
	}
	protected long getStartTime()
	{
		return startTime;
	}
	protected void setStartTime(long timeStamp){
		this.startTime = timeStamp;
	}
	protected long getEndTime()
	{
		return endTime;
	}
	protected void setEndTime(long timeStamp){
		this.endTime = timeStamp;
	}
	public String getTimeTaken(){
		long timeTaken = getEndTime() - getStartTime();
		String logString = getTaskName() +"," + timeTaken;
		logger.info(logString);
		return logString;
	}
	public String getTimeTakenInSecs(){
		if (!logPerformance)
		{
			return "";
		}
		double timeTaken = ((double)getEndTime()) - ((double)getStartTime());
		timeTaken/=1000;
		String logString = getTaskName() +"," + timeTaken;
		logger.info(logString);
		return logString;
	}
	
	public static TaskTimeCalculater startTask(String taskName){
		return startTask(taskName, TaskTimeCalculater.class);
	}
	public static TaskTimeCalculater startTask(String taskName, Class clazz){
		if (!logPerformance)
		{
			return nullTaskTimeCalculater;
		}

		TaskTimeCalculater taskTimeCalculater = new TaskTimeCalculater(clazz);
		taskTimeCalculater.setTaskName(taskName);
		taskTimeCalculater.setStartTime(System.currentTimeMillis());
		return taskTimeCalculater;
	}
	public static String endTask(TaskTimeCalculater taskTimeCalculater){
		if (!logPerformance)
		{
			return "";
		}

		taskTimeCalculater.setEndTime(System.currentTimeMillis());
		return taskTimeCalculater.getTimeTaken();
	}
}
