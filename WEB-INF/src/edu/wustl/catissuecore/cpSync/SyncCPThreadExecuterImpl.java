package edu.wustl.catissuecore.cpSync;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.wustl.common.beans.SessionDataBean;




public class SyncCPThreadExecuterImpl
{
	private BlockingQueue<Runnable> tasksQueue;
	private RejectedExecutionHandler executionHandler;
	private Map<String,Map<SessionDataBean, Future<String>>> taskMap;
	private ThreadPoolExecutor executor = null;
	private static SyncCPThreadExecuterImpl syncCP = null;
	
	private SyncCPThreadExecuterImpl()
	{
		super();
		tasksQueue = new ArrayBlockingQueue<Runnable>(50);
		executionHandler = new MyRejectedExecutionHandelerImpl();
		taskMap = new HashMap<String,Map<SessionDataBean, Future<String>>>();
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

		};
		
		executor.allowCoreThreadTimeOut(true);
	}
	
	public  void shutdown()
	{
                if (executor != null) {
                	executor.shutdownNow();
                }
	}
	
	
	public void startSync(String jobName,SessionDataBean sessionDataBean)
	{
		Map<SessionDataBean,Future<String>> valueMap=new HashMap<SessionDataBean,Future<String>>();
		Future<String> ft = executor.submit(new MyWork(jobName,sessionDataBean),new String());
		valueMap.put(sessionDataBean, ft);
		taskMap.put(jobName, valueMap);
		updateMap();
	}
	
	private void updateMap() 
	{
		for (String jobName : taskMap.keySet())
		{
			Future<String> ft = taskMap.get(jobName).values().iterator().next();
			if(ft.isDone())
				taskMap.remove(jobName);
		}
		
	}
	public boolean isSyncOn(String jobName)
	{
		boolean isSyncOn = Boolean.FALSE;
		Future<String> ft =null;
		if(!taskMap.isEmpty() && taskMap.get(jobName)!=null)
		{
			ft=taskMap.get(jobName).values().iterator().next();
		}
		if(ft != null)
			isSyncOn = !taskMap.get(jobName).values().iterator().next().isDone();
		return isSyncOn; 
	}

	public void stopSync(String jobName)
	{
		Future<String> ft = taskMap.get(jobName).values().iterator().next();
		if(ft != null)
			ft.cancel(true);
	}
	
	
	
}
