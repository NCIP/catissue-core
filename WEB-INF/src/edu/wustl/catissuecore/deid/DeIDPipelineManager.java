package edu.wustl.catissuecore.deid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;

import com.deid.JniDeID;

import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.IdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * @author vijay_pande
 * This class is responsible to fetch identified reports and to spawn a sepearate thread to convert identified reports into deidentiied reports.
 * This class manages the thread pool so that excessive threads will not be spawned. 
 */
public class DeIDPipelineManager
{
	protected static List<String> sectionPriority;
	protected static HashMap<String,String> abbrToHeader;
	protected static String configFileName;
	private static String pathToConfigFiles;
	protected static JniDeID deid;
	private RejectedExecutionHandler rejectedExecutionHandler; 
	
	private int corePoolSize;
	private int maxPoolSize;
	private int keepAliveSeconds;

	
	/**
	 * Default constructor of the class
	 * 
	 */
	public DeIDPipelineManager()
	{
		try
		{
			this.init();
			
		}
		catch(Exception ex)
		{
			Logger.out.error("Initialization of deidentification process failed or error in main thread",ex);
		}
	}
		
	
	/**
	 * @throws Exception throws exception occured in the initialization process.
	 * This method is responsible for creating prerequisite environment that is required for initialization of the DeID process
	 */
	private void init() throws Exception
	{
		sectionPriority = new ArrayList<String>();
		abbrToHeader = new HashMap <String,String>();

		Variables.applicationHome = System.getProperty("user.dir");
		Logger.out = org.apache.log4j.Logger.getLogger("");
		Logger.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
		PropertyConfigurator.configure(Variables.applicationHome + File.separator+"ApplicationResources.properties");
		System.setProperty("gov.nih.nci.security.configFile",
				"./catissuecore-properties"+File.separator+"ApplicationSecurityConfig.xml");
		CDEManager.init();
		XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
		ApplicationProperties.initBundle("ApplicationResources");

		corePoolSize=4;
		maxPoolSize=Integer.parseInt(XMLPropertyHandler.getValue("threadPool.maxPoolSize"));
		keepAliveSeconds=100;
				
		pathToConfigFiles=new String(Variables.applicationHome + System.getProperty("file.separator")+"catissuecore-properties"+System.getProperty("file.separator"));
		cacheConfigFileName();
		setUpSectionHeaderPriorities();

		deid=new JniDeID(); 

		deid.setDictionaryLocation(XMLPropertyHandler.getValue("deid.home"));	
	}
		
	/**
	 * 
	 * @throws InterruptedException 
	 * This method is responsible for managing the overall process of de-identification
	 */
	public void processManager() throws InterruptedException
	{
		while(true)
		{
			try
			{
				Logger.out.info("Deidentification process started at "+new Date().toString());		
				List isprIDList=getReportIDList();
				processReports(isprIDList);
			}
			catch(Exception ex)
			{
				Logger.out.error("Unexpected Exception in deid Pipeline ",ex);
				Logger.out.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
				Thread.sleep(Integer.parseInt(XMLPropertyHandler.getValue("deid.sleepsize")));
			}
			catch (Throwable th)
		    {
				Logger.out.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
				Thread.sleep(Integer.parseInt(XMLPropertyHandler.getValue("deid.sleepsize")));
		    }
		}
	}
		
	/**
	 * @param isprIDList Identified surgical pathology report ID list
	 * @throws Exception generic exception
	 * This method is responsible for managing the pool of thread, fetching individual reports by ID and intiating the de-identification rpocess.
	 */
	private void processReports(List isprIDList) throws  Exception
	{
		ThreadPoolExecutor deidExecutor=new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveSeconds, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), this.rejectedExecutionHandler);
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		IdentifiedSurgicalPathologyReportBizLogic bizLogic =(IdentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
		if(isprIDList.size()<=1)
		{
			Logger.out.info("Deidentification process finished at "+new Date().toString()+ ". Thread is going to sleep.");
			Thread.sleep(Integer.parseInt(XMLPropertyHandler.getValue("deid.sleepsize")));
		}
		else
		{
			try
			{
				JniDeID.loadDeidLibrary();	
				try
				{
					if(isprIDList!=null)
					{
						String id;
						NameValueBean nb;
						IdentifiedSurgicalPathologyReport ispr=null;	
						for(int i=1;i<isprIDList.size();i++)
						{
							nb=(NameValueBean)isprIDList.get(i);
							id=nb.getValue();
							ispr=(IdentifiedSurgicalPathologyReport)bizLogic.getReportById(Long.parseLong(id));
							Thread th = new DeidReport(ispr);
							deidExecutor.execute(th);
						}
					}
				}
				catch(Exception ex)
				{
					Logger.out.error("Deidentification pipeline is failed:",ex);			
				}
				deidExecutor.shutdown();
				while(deidExecutor.getActiveCount()>0)
				{
					Thread.sleep(100000);
				}
				JniDeID.unloadDeidLibrary();
			}
			catch(UnsatisfiedLinkError ex)
			{
				Logger.out.error("Either Loading or unLoading of DeID library is failed:",ex);
				throw ex;
			}
		}
	}
			
	/**
	 * @return List ISPR ID list
	 * @throws Exception generic exception
	 * This method fetche a list of ID's of identified surgical pathology reports pending for de-indetification
	 */
	private List getReportIDList() throws Exception
	{
		List isprIDList=null;
		try
		{
			String sourceObjectName=IdentifiedSurgicalPathologyReport.class.getName();
			String[] displayNameFields=new String[] {"id"};
			String valueField=new String("id");
			String[] whereColumnName = new String[]{"reportStatus"};
			String[] whereColumnCondition = new String[]{"="};
			Object[] whereColumnValue = new String[]{Parser.PENDING_FOR_DEID};
			String joinCondition = null;
			String separatorBetweenFields = ", ";	
			rejectedExecutionHandler=new ThreadPoolExecutor.AbortPolicy();
			
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			IdentifiedSurgicalPathologyReportBizLogic bizLogic =(IdentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(IdentifiedSurgicalPathologyReport.class.getName());
			isprIDList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields, false);
		}
		catch(DAOException ex)
		{
			Logger.out.error("Error in fetching identified reports for deidentification:",ex);
		}	
		return isprIDList;
	}
		 
	/**
	 * @throws Exception
	 * This method temporarily saves the name of deid config  file name. 
	 * This deid config file conatins the keywords which will be the input for deidentification
	 */
	protected void cacheConfigFileName() throws Exception	
	{
		String cfgFileName=new String(pathToConfigFiles+XMLPropertyHandler.getValue("deid.config.filename"));
        File cfgFile = new File(cfgFileName);
        DeIDPipelineManager.configFileName = cfgFile.getAbsolutePath();
        Logger.out.info("Config file name is "+DeIDPipelineManager.configFileName);
    }
	
	/**
	 * @throws Exception 
	 * This nethod sets the priority order and full name of the abrreviated section name
	 * which is used by the synthesizeSPRText method
	 */
	private void setUpSectionHeaderPriorities() throws Exception
	{
		try 
		{
			String configName = new String(pathToConfigFiles+XMLPropertyHandler.getValue("deid.sectionheaderpriority.filename"));
			BufferedReader br = new BufferedReader(new FileReader(configName));

			String line = "";
			StringTokenizer st;
			String name;
			String abbr;
			String prty;
			while ((line = br.readLine()) != null) 
			{
				st = new StringTokenizer(line, "|");
				name = st.nextToken().trim();
				abbr = st.nextToken().trim();
				prty = st.nextToken().trim();

				sectionPriority.add(abbr);
				abbrToHeader.put(abbr, name);
			}
		}
		catch (IOException ex) 
		{
			Logger.out.error("Error in setting Section header Priorities",ex);
			throw ex;
		}
	}
	
	/**
	 * @param args commandline arguments
	 * main method for the DeIDPipeline class
	 */
	public static void main(String[] args)
	{
		DeIDPipelineManager de=new DeIDPipelineManager();
		Thread th=new Thread(){
			public void	run()
			{
				try
				{
					int PORT=Integer.parseInt(XMLPropertyHandler.getValue("deid.port"));
					ServerSocket serv = new ServerSocket(PORT);
				  	BufferedReader r;
			    	Socket sock = serv.accept();
			    	r =new BufferedReader (new InputStreamReader (sock.getInputStream()) );
			    	PrintWriter out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()),true);
			    	String str=r.readLine();
			    	
			    	System.out.println("Stopping server");
			    	r.close();
			    	sock.close(); 
				    serv.close();
				    
				    System.exit(0);
					}
					catch(Exception e)
					{
						Logger.out.error("Error stopping server ",e);
					}
				}
			};
			th.start();
			try
			{
				de.processManager();
			}
			catch(Exception ex)
			{
				Logger.out.error("Deidentification process manager failed");
			}
	}
	
}