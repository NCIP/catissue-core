package edu.wustl.catissuecore.conceptcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import org.apache.log4j.PropertyConfigurator;

import edu.upmc.opi.caBIG.caTIES.server.CaTIES_ExporterPR;
import edu.upmc.opi.caBIG.caTIES.services.caTIES_TiesPipe.TiesPipe;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DeidentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.reportloader.CSVLogger;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.catissuecore.reportloader.SiteInfoHandler;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.global.Variables;
import edu.wustl.common.util.logger.Logger;
import gate.Gate;

/**
 * @author vijay_pande
 * This class is responsible to fetch de-identified reports and to spawn a sepearate thread to generate concept codes for de-identified reports.
 * This class manages the thread pool so that excessive threads will not be spawned. 
 */
public class ConceptCodeManager
{	

	/**
	 * Field coderVersion.
	 */
	private String coderVersion = null;
	/**
	 * Field gateHome.
	 */
	private String gateHome = null;

	/**
	 * Field creoleUrlName.
	 */
	private String creoleUrlName = null;

	/**
	 * Field caseInsensitiveGazetteerUrlName.
	 */
	private String caseInsensitiveGazetteerUrlName = null;

	/**
	 * Field caseSensitiveGazetteerUrlName.
	 */
	private String caseSensitiveGazetteerUrlName = null;

	/**
	 * Field sectionChunkerUrlName.
	 */
	private String sectionChunkerUrlName = null;

	/**
	 * Field conceptFilterUrlName.
	 */
	private String conceptFilterUrlName = null;

	/**
	 * Field negExUrlName.
	 */
	private String negExUrlName = null;

	/**
	 * Field conceptCategorizerUrlName.
	 */
	private String conceptCategorizerUrlName = null;
	/**
	 * Field tiesPipe.
	 */
	private TiesPipe tiesPipe = null;
	/**
	 * Field exporterPR.
	 */
	private CaTIES_ExporterPR exporterPR = null;
	/**
	 * Default constructor of the class
	 * 
	 */
	public ConceptCodeManager()
	{
		try
		{
			this.init();
			
		}
		catch(Exception ex)
		{
			Logger.out.error("Initialization of concept coding process failed or error in main thread",ex);
		}
	}
		
	
	/**
	 * @throws Exception throws exception occured in the initialization process.
	 * This method is responsible for creating prerequisite environment that is required for initialization of the concept coding process
	 */
	private void init() throws Exception
	{
		// Initialization methods
		Variables.applicationHome = System.getProperty("user.dir");
		//Logger.out = org.apache.log4j.Logger.getLogger("");
		//Configuring common logger
		Logger.configure(Parser.LOGGER_GENERAL);
		// Configuring CSV logger
		CSVLogger.configure(Parser.LOGGER_CONCEPT_CODER);
		//Configuring logger properties
		PropertyConfigurator.configure(Variables.applicationHome + File.separator+"logger.properties");
		// Setting properties for UseImplManager
		System.setProperty("gov.nih.nci.security.configFile","./catissuecore-properties"+File.separator+"ApplicationSecurityConfig.xml");
		// initializing cache manager
		CDEManager.init();
		//initializing XMLPropertyHandler to read properties from caTissueCore_Properties.xml file
		XMLPropertyHandler.init("./catissuecore-properties"+File.separator+"caTissueCore_Properties.xml");
		// initializing SiteInfoHandler to read site names from site configuration file
		SiteInfoHandler.init(XMLPropertyHandler.getValue("site.info.filename"));
		// Intialization to retrieve values of keys from ApplicationResources.properties file
		ApplicationProperties.initBundle("ApplicationResources");
		CaTIESProperties.initBundle("caTIES");
		
		setAll();
		initialize();
	}
	
	/**
	 * Methos to set initial values for various processors like gate, gazeteer, chunker, etc.
	 */
	public void setAll() 
	{
		this.coderVersion=CaTIESProperties.getValue("caties.coder.version");
		this.gateHome=CaTIESProperties.getValue("caties.gate.home");
		this.creoleUrlName=CaTIESProperties.getValue("caties.creole.url.name");
		this.caseInsensitiveGazetteerUrlName=CaTIESProperties.getValue("caties.case.insensitive.gazetteer.url.name");
		this.caseSensitiveGazetteerUrlName=CaTIESProperties.getValue("caties.case.sensitive.gazetteer.url.name");
		this.sectionChunkerUrlName=CaTIESProperties.getValue("caties.section.chunker.url.name");
		this.conceptFilterUrlName=CaTIESProperties.getValue("caties.concept.filter.url.name");
		this.negExUrlName=CaTIESProperties.getValue("caties.neg.ex.url.name");
		this.conceptCategorizerUrlName=CaTIESProperties.getValue("caties.concept.categorizer.url.name");

	}
	/**
	 * Method initialize.
	 */
	public void initialize() 
	{
		establishCaTIESExporterPR();
		Logger.out.debug("Established Exporter PR.");
		establishTiesPipeForDirectAccess();
		Logger.out.debug("Established Ties Pipe.");
		establishGateInterface();
		Logger.out.debug("Established Gate Interface.");
	}
	/**
	 * Method establishCaTIESExporterPR.
	 */
	private void establishCaTIESExporterPR() 
	{
		this.exporterPR = new CaTIES_ExporterPR();
	}

	/**
	 * Method establishTiesPipeForDirectAccess.
	 */
	private void establishTiesPipeForDirectAccess() 
	{
		try {
			this.tiesPipe = new TiesPipe();
			this.tiesPipe.setGateHome(this.gateHome);
			this.tiesPipe.setCreoleUrlName(this.creoleUrlName);
			this.tiesPipe.setCaseInsensitiveGazetteerUrlName(this.caseInsensitiveGazetteerUrlName);
			this.tiesPipe.setCaseSensitiveGazetteerUrlName(this.caseSensitiveGazetteerUrlName);
			this.tiesPipe.setSectionChunkerUrlName(this.sectionChunkerUrlName);
			this.tiesPipe.setConceptFilterUrlName(this.conceptFilterUrlName);
			this.tiesPipe.setNegExUrlName(this.negExUrlName);
			this.tiesPipe.setConceptCategorizerUrlName(this.conceptCategorizerUrlName);
		} catch (Exception x) 
		{
//			CaTIES_ExceptionLogger.out.logException(Logger.out, x);
			Logger.out.fatal(x.getMessage());
		}
	}

	/**
	 * Method establishGateInterface.
	 */
	private void establishGateInterface() 
	{
		try {
			System.setProperty("gate.home", this.gateHome);
			Gate.init();
		} catch (Exception x) {
//			CaTIES_ExceptionLogger.out.logException(Logger.out, x);
			Logger.out.fatal(x.getMessage());
		}
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
				Logger.out.info("Concept Coding process started at "+new Date().toString());		
				List deidReportIDList=getReportIDList();
				processReports(deidReportIDList);
			}
			catch(Exception ex)
			{
				Logger.out.error("Unexpected Exception in Concept Code Pipeline ",ex);
				Logger.out.info("Concept Coding process finished at "+new Date().toString()+ ". Thread is going to sleep.");
				Thread.sleep(Integer.parseInt(XMLPropertyHandler.getValue("deid.sleepsize")));
			}
		}
	}
		
	/**
	 * @param deidReportIDList deidentified surgical pathology report ID list
	 * @throws Exception generic exception
	 * This method is responsible for managing the pool of thread, fetching individual reports by ID and intiating the de-identification rpocess.
	 */
	private void processReports(List deidReportIDList) throws  Exception
	{
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		DeidentifiedSurgicalPathologyReportBizLogic bizLogic =(DeidentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(DeidentifiedSurgicalPathologyReport.class.getName());
		if(deidReportIDList.size()<=1)
		{
			Logger.out.info("Concept Coding process finished at "+new Date().toString()+ ". Thread is going to sleep.");
			Thread.sleep(Integer.parseInt(XMLPropertyHandler.getValue("deid.sleepsize")));
		}
		else
		{
			Logger.out.info(deidReportIDList.size()+" reports found for Concept Coding");
			try
			{
				if(deidReportIDList!=null)
				{
					CSVLogger.info(Parser.LOGGER_CONCEPT_CODER, "Date/Time, De-Identified report ID, Status, Message");
					String id;
					NameValueBean nb;
					DeidentifiedSurgicalPathologyReport deidReport=null;	
					for(int i=1;i<deidReportIDList.size();i++)
					{
						nb=(NameValueBean)deidReportIDList.get(i);
						id=nb.getValue();
						deidReport=(DeidentifiedSurgicalPathologyReport)bizLogic.getReportById(Long.parseLong(id));
						ConceptCoder cc=new ConceptCoder(deidReport,exporterPR, tiesPipe);
						Logger.out.info("Concept coding of report serial no "+i+" started....");
						cc.process();
						Logger.out.info("Concept coding of report serial no "+i+" finished.");
					}
				}
			}
			catch(Exception ex)
			{
				Logger.out.error("Concept Coding pipeline failed:",ex);			
			}
		}
	}
			
	/**
	 * @return deidReportIDList De-identified report ID list
	 * @throws Exception generic exception
	 * This method fetche a list of ID's of identified surgical pathology reports pending for de-indetification
	 */
	private List getReportIDList() throws Exception
	{
		List deidReportIDList=null;
		try
		{
			String sourceObjectName=DeidentifiedSurgicalPathologyReport.class.getName();
			String[] displayNameFields=new String[] {Constants.SYSTEM_IDENTIFIER};
			String valueField=new String(Constants.SYSTEM_IDENTIFIER);
			String[] whereColumnName = new String[]{Parser.COLUMN_NAME_REPORT_STATUS};
			String[] whereColumnCondition = new String[]{"="};
			Object[] whereColumnValue = new String[]{Parser.PENDING_FOR_XML};
			String joinCondition = null;
			String separatorBetweenFields = ", ";	
			
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			DeidentifiedSurgicalPathologyReportBizLogic bizLogic =(DeidentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(DeidentifiedSurgicalPathologyReport.class.getName());
			deidReportIDList = bizLogic.getList(sourceObjectName, displayNameFields, valueField, whereColumnName, whereColumnCondition, whereColumnValue, joinCondition, separatorBetweenFields, false);
		}
		catch(DAOException ex)
		{
			Logger.out.error("Error in fetching de-identified reports for concept coding:",ex);
			throw ex;
		}	
		return deidReportIDList;
	}
	/**
	 * @param args commandline arguments
	 * main method for the DeIDPipeline class
	 */
	public static void main(String[] args)
	{
		ConceptCodeManager ccManager=new ConceptCodeManager();
		Thread th=new Thread(){
			public void	run()
			{
				try
				{
					int port=Integer.parseInt(XMLPropertyHandler.getValue("concept.coder.port"));
					ServerSocket serv = new ServerSocket(port);
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
				ccManager.processManager();
			}
			catch(Exception ex)
			{
				Logger.out.error("Concept code manager failed");
			}
	}
}