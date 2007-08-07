package edu.wustl.catissuecore.conceptcode;

import java.util.Date;
import java.util.List;

import edu.upmc.opi.caBIG.caTIES.server.CaTIES_ExporterPR;
import edu.upmc.opi.caBIG.caTIES.services.caTIES_TiesPipe.TiesPipe;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.DeIdentifiedSurgicalPathologyReportBizLogic;
import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.caties.util.InitUtility;
import edu.wustl.catissuecore.caties.util.StopServer;
import edu.wustl.catissuecore.domain.pathology.DeIdentifiedSurgicalPathologyReport;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;
import gate.Gate;

/**
 * This class is responsible to fetch de-identified reports and to spawn a sepearate thread to generate concept codes for de-identified reports.
 * This class manages the thread pool so that excessive threads will not be spawned. 
 * @author vijay_pande
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
	 */
	public ConceptCodeManager()
	{
		try
		{
			this.initCoder();		
		}
		catch(Exception ex)
		{
			Logger.out.error("Initialization of concept coding process failed or error in main thread",ex);
		}
	}
		
	/**
	 * This method is responsible for creating prerequisite environment that is required for initialization of the concept coding process
	 * @throws Exception throws exception occured in the initialization process.
	 */
	private void initCoder() throws Exception
	{
		InitUtility.init();
		// Configuring CSV logger
		CSVLogger.configure(CaTIESConstants.LOGGER_CONCEPT_CODER);
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
		try 
		{
			this.tiesPipe = new TiesPipe();
			this.tiesPipe.setGateHome(this.gateHome);
			this.tiesPipe.setCreoleUrlName(this.creoleUrlName);
			this.tiesPipe.setCaseInsensitiveGazetteerUrlName(this.caseInsensitiveGazetteerUrlName);
			this.tiesPipe.setCaseSensitiveGazetteerUrlName(this.caseSensitiveGazetteerUrlName);
			this.tiesPipe.setSectionChunkerUrlName(this.sectionChunkerUrlName);
			this.tiesPipe.setConceptFilterUrlName(this.conceptFilterUrlName);
			this.tiesPipe.setNegExUrlName(this.negExUrlName);
			this.tiesPipe.setConceptCategorizerUrlName(this.conceptCategorizerUrlName);
		} 
		catch (Exception x) 
		{
			Logger.out.fatal(x.getMessage());
		}
	}

	/**
	 * Method establishGateInterface.
	 */
	private void establishGateInterface() 
	{
		try 
		{
			System.setProperty("gate.home", this.gateHome);
			Gate.init();
		} 
		catch (Exception x) 
		{
			Logger.out.fatal(x.getMessage());
		}
	}

	/**
	 * This method is responsible for managing the overall process of de-identification
	 * @throws InterruptedException 
	 */
	public void startProcess() throws InterruptedException
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
				Thread.sleep(Integer.parseInt(CaTIESProperties.getValue(CaTIESConstants.CONCEPT_CODER_SLEEPTIME)));
			}
		}
	}
		
	/**
	 * This method is responsible for managing the pool of thread, fetching individual reports by ID and intiating the de-identification rpocess.
	 * @param deidReportIDList deidentified surgical pathology report ID list
	 * @throws Exception generic exception
	 */
	private void processReports(List deidReportIDList) throws  Exception
	{
		BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
		DeIdentifiedSurgicalPathologyReportBizLogic bizLogic =(DeIdentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(DeIdentifiedSurgicalPathologyReport.class.getName());
		if(deidReportIDList.size()<=1)
		{
			Logger.out.info("Concept Coding process finished at "+new Date().toString()+ ". Thread is going to sleep.");
			Thread.sleep(Integer.parseInt(CaTIESProperties.getValue(CaTIESConstants.CONCEPT_CODER_SLEEPTIME)));
		}
		else
		{
			Logger.out.info(deidReportIDList.size()+" reports found for Concept Coding");
			try
			{
				if(deidReportIDList!=null)
				{
					CSVLogger.info(CaTIESConstants.LOGGER_CONCEPT_CODER,CaTIESConstants.CSVLOGGER_DATETIME+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_DEIDENTIFIED_REPORT+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_STATUS+CaTIESConstants.CSVLOGGER_SEPARATOR+CaTIESConstants.CSVLOGGER_MESSAGE);
					String id;
					NameValueBean nb;
					DeIdentifiedSurgicalPathologyReport deidReport=null;	
					for(int i=1;i<deidReportIDList.size();i++)
					{
						nb=(NameValueBean)deidReportIDList.get(i);
						id=nb.getValue();
						deidReport=(DeIdentifiedSurgicalPathologyReport)bizLogic.getReportById(Long.parseLong(id));
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
	 * This method fetche a list of ID's of identified surgical pathology reports pending for de-indetification
	 * @return deidReportIDList De-identified report ID list
	 * @throws Exception generic exception
	 */
	private List getReportIDList() throws Exception
	{
		List deidReportIDList=null;
		try
		{
			String sourceObjectName=DeIdentifiedSurgicalPathologyReport.class.getName();
			String[] displayNameFields=new String[] {Constants.SYSTEM_IDENTIFIER};
			String valueField=new String(Constants.SYSTEM_IDENTIFIER);
			String[] whereColumnName = new String[]{CaTIESConstants.COLUMN_NAME_REPORT_STATUS};
			String[] whereColumnCondition = new String[]{"="};
			Object[] whereColumnValue = new String[]{CaTIESConstants.PENDING_FOR_XML};
			String joinCondition = null;
			String separatorBetweenFields = ", ";	
			
			BizLogicFactory bizLogicFactory = BizLogicFactory.getInstance();
			DeIdentifiedSurgicalPathologyReportBizLogic bizLogic =(DeIdentifiedSurgicalPathologyReportBizLogic) bizLogicFactory.getBizLogic(DeIdentifiedSurgicalPathologyReport.class.getName());
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
	 * Main method for the ConceptCodeManager class
	 * @param args commandline arguments
	 */
	public static void main(String[] args)
	{
		ConceptCodeManager conceptCodeManager=new ConceptCodeManager();
		Thread stopThread=new StopServer(CaTIESConstants.CONCEPT_CODER_PORT);
		stopThread.start();
		try
		{
			conceptCodeManager.startProcess();
		}
		catch(Exception ex)
		{
			Logger.out.error("Concept code manager failed");
		}
	}
}