package com.util;

import org.apache.log4j.Logger;

public class TaskTimeCalculater {

	private long startTime;
	private long endTime;
	private String taskName;
	Logger logger;
	protected TaskTimeCalculater(Class clazz){
		logger = Logger.getLogger(clazz);
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
		double timeTaken = ((double)getEndTime()) - ((double)getStartTime());
		timeTaken/=1000;
		String logString = getTaskName() +"," + timeTaken;
		logger.info(logString);
		return logString;
	}
	public static TaskTimeCalculater startTask(String taskName, Class clazz){
		TaskTimeCalculater taskTimeCalculater = new TaskTimeCalculater(clazz);
		taskTimeCalculater.setTaskName(taskName);
		taskTimeCalculater.setStartTime(System.currentTimeMillis());
		return taskTimeCalculater;
	}
	public static String endTask(TaskTimeCalculater taskTimeCalculater){
		taskTimeCalculater.setEndTime(System.currentTimeMillis());
		return taskTimeCalculater.getTimeTaken();
	}
}
