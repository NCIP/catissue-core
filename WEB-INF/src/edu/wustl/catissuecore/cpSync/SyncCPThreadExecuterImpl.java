package edu.wustl.catissuecore.cpSync;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;




public class SyncCPThreadExecuterImpl
{
	private BlockingQueue<Runnable> tasksQueue;
	private RejectedExecutionHandler executionHandler;
	private Map<String, Future<String>> taskMap;
	 
	// Create the ThreadPoolExecutor
	private ThreadPoolExecutor executor = null;
	private static SyncCPThreadExecuterImpl syncCP = null;
	
	private SyncCPThreadExecuterImpl()
	{
		super();
		tasksQueue = new ArrayBlockingQueue<Runnable>(50);
		executionHandler = new MyRejectedExecutionHandelerImpl();
		taskMap = new HashMap<String, Future<String>>();
	}
	
	public static SyncCPThreadExecuterImpl getInstance()
	{
		if (syncCP == null)
		{
			syncCP = new SyncCPThreadExecuterImpl();
		}
		return syncCP;
	}
	public void init()
	{
		executor = new ThreadPoolExecutor(10, 15, 10,
		        TimeUnit.SECONDS, tasksQueue, executionHandler){
			protected void beforeExecute(Thread t, Runnable r) { 
		         t.setName(r.toString());
		    }

//		    protected void afterExecute(Runnable r, Throwable t) { 
//		         Thread.currentThread().setName("no time taken");
//		    } 
		};
		
		executor.allowCoreThreadTimeOut(true);
	}
	
	public  void shutdown()
	{
		executor.shutdownNow();
	}
	
	
	public void startSync(String jobName)
	{
		Future<String> ft = executor.submit(new MyWork(jobName),new String());
		taskMap.put(jobName, ft);
		updateMap();
	}
	
	private void updateMap() 
	{
		for (String jobName : taskMap.keySet())
		{
			Future<String> ft = taskMap.get(jobName);
			if(ft.isDone())
				taskMap.remove(jobName);
		}
		
	}
	public boolean isSyncOn(String jobName)
	{
		boolean isSyncOn = Boolean.FALSE;
		Future<String> ft = taskMap.get(jobName);
		if(ft != null)
			isSyncOn = !taskMap.get(jobName).isDone();
		return isSyncOn; 
//		return getJob(jobName)!=null?true:false;
	}
	// Starting the monitor thread as a daemon
//	Thread monitor = new Thread(new MyMonitorThread(executor));
//	monitor.setDaemon(true);
//	monitor.start();

	public void stopSync(String jobName)
	{
		Future<String> ft = taskMap.get(jobName);
		if(ft != null)
			ft.cancel(true);
//		Thread job = getJob(jobName);
//		if(job!=null)
//			job.interrupt();
	}
	
	
	
}
