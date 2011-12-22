package edu.wustl.catissuecore.GSID;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.utils.UserUtility;
import edu.wustl.catissuecore.util.EmailHandler;
import edu.wustl.common.util.global.CommonServiceLocator;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;

public class GSIDServiceStatusNotifier {
	
	public static String JOB_NAME = "GSIID_SERVICE_JOB";
	public static String TRIGGER_NAME = "GSIDServiceStatusNotifier";
	//private static final Log LOG = LogFactory.getLog(GSIDClient.class);
	private static Logger LOG = Logger.getLogger(GSIDClient.class);
	
	public static volatile boolean SERVICE_DOWN_FLAG;

	/**
	 * send service down message 
	 */
	public void sendServiceStatusFailureEmail() {
		if (!SERVICE_DOWN_FLAG) {
			List<String> adminEmails = getAllAdminEmails();
			EmailHandler emailHandler = new EmailHandler();
			GSIDClient client = new GSIDClient();
			String subject = "caTissue GSID Service is Down ";
			String body = " caTissue is configured to fetch GSIDs for new biospecimens from "
					+ client.getUrl()
					+ ".  The service is currently down.  Please contact the administrator of the GSID service.  You will be notified when the service is again available.  At that time, you will need to utilize the GSID batch fetching tool in caTissue to assign GSIDs to all biospecimens that were created while the service was down.";
			emailHandler.sendGsIdServiceStatusEmail(
					(String[]) adminEmails.toArray(new String[0]), subject,
					body);
		}
		SERVICE_DOWN_FLAG = true;
		// enable job to run only once ...
	}
	
	/**
	 * send service back up message
	 */
	public void sendServiceStatusSuccessEmail() {
		List<String> adminEmails = getAllAdminEmails();
		EmailHandler emailHandler = new EmailHandler();
		GSIDClient client = new GSIDClient();
		String subject = "caTissue GSID Service is Back Up  ";
		String body = " caTissue is configured to fetch GSIDs for new biospecimens from "+client.getUrl()+".  The service is now back up.  Please utilize the GSID batch fetching tool in caTissue to assign GSIDs to all biospecimens that were created while the service was down. ";
		emailHandler.sendGsIdServiceStatusEmail((String[]) adminEmails.toArray(new String[0]), subject , body );
		SERVICE_DOWN_FLAG = false;
	}
	
	/**
	 * get all admin emails 
	 * @return
	 */
	private List<String> getAllAdminEmails() {
		List<String> adminEmails = new ArrayList<String>();
		final String applicationName = CommonServiceLocator.getInstance()
		.getAppName();
		HibernateDAO hibernateDao = null;
		try 
		{
			hibernateDao = (HibernateDAO) DAOConfigFactory.getInstance()
			.getDAOFactory(applicationName).getDAO();
			hibernateDao.openSession(null);		
			
			List<User> users = hibernateDao.retrieve(User.class.getName());
			for (User user:users) {
				String id = UserUtility.getRoleId(user);
				if (id != null && id.equals("1")) {
					adminEmails.add(user.getEmailAddress());
					//System.out.println(user.getEmailAddress());
				}
			}
			//System.out.println(users.size());
			hibernateDao.commit();
		} catch (final Exception e) {
			try {
				hibernateDao.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} finally {
			try {
				hibernateDao.closeSession();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}
		return adminEmails;
	}
	public  boolean checkServiceStatus() {
		GSIDClient gsidClient = new GSIDClient();
		//System.out.println("Checking service - " + gsidClient.getUrl());
		boolean serviceStatus = false;
		try {
			// some random id 
			String identifier = "5784d1dd-9373-533e-8086-fd479fbd564e";
			gsidClient.validateIdentifier(identifier);
			serviceStatus = true;
			SERVICE_DOWN_FLAG = false;
		} catch (RemoteException e) {			
			LOG.error(GSIDConstant.GSID_REGISTER_REMOTE_ERROR, e);			
		}
		return serviceStatus;
	}
	

	public  void unScheduleJob()  {
		SchedulerFactory schedFact2 =  new org.quartz.impl.StdSchedulerFactory();
		Scheduler sched2;
		try {
			sched2 = schedFact2.getScheduler();
			sched2.unscheduleJob(TRIGGER_NAME, sched2.DEFAULT_GROUP);
			LOG.debug("UNSCHEDULED " + this.JOB_NAME);
		} catch (SchedulerException e) {
			LOG.fatal("UNABLE TO SCHEDULE " + this.JOB_NAME);
			e.printStackTrace();
		}
	}
	
	public  void scheduleJob()  throws Exception {
		 
		 /*
		  if (checkServiceStatus(parentIdentifiers)) {
			// if the gsid service is up , and schedule job is called some how (this might not happen) , 
				 // will send an email to admins and and unschedule if the job exists and return  ...
			sendServiceStatusSuccessEmail();
			unScheduleJob();
			return; 
		  }*/
		  // schedule new job ..
	      SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
	      Scheduler sched  = schedFact.getScheduler();
	      sched.start();
	      JobDetail jobDetail = new JobDetail(JOB_NAME,sched.DEFAULT_GROUP,GSIDServiceStatusJob.class);
	     // jobDetail.getJobDataMap().put("type","FULL");
	      // IDS to pass to the service , store in job context
	     // jobDetail.getJobDataMap().put(GSIDServiceStatusJob.CTX_ARRY, parentIdentifiers);
	      // run every 30 minutes .
	      int repeatIntervalinMinutes = 60; 
	      SimpleTrigger strigger  =  new SimpleTrigger(TRIGGER_NAME, sched.DEFAULT_GROUP, new Date(), null, SimpleTrigger.REPEAT_INDEFINITELY, repeatIntervalinMinutes*60000);
	      try {
	    	
			sched.scheduleJob(jobDetail, strigger);
	      } catch (SchedulerException se) {
	    	  // we are calling this method when ever the user tries to assign gsid and service is down , 
	    	  // if it happens to the second user and job has been already scheduled , we dont need to schedule the job again .
	    	  if (se instanceof org.quartz.ObjectAlreadyExistsException) {
	    		  LOG.info("JOB Already exists .. no need to schedule  " +this.JOB_NAME);
	    		  return ;
	    	  } else {
	    		  throw se;
	    	  }
	      }  
	}
	
	public static void main(String[] args) {
	    try {
	    	System.out.println(" Service is down , send emails to folks and schedule job ..");
	    	GSIDServiceStatusNotifier n = new GSIDServiceStatusNotifier();
	    	
	    	String[] parentIdentifiers = { "a" , "b"};
	    	
	    	n.scheduleJob();
	      	//Thread.sleep(35000);
	      	//n.scheduleJob(parentIdentifiers);
	      	
	      	//GSIDServiceStatusNotifier.unScheduleJob();
		      
	    
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	  }
}
