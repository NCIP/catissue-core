package edu.wustl.catissuecore.conceptcode;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.JDOMParseException;
import org.jdom.input.SAXBuilder;

import edu.upmc.opi.caBIG.caTIES.common.CaTIES_JDomUtils;
import edu.upmc.opi.caBIG.caTIES.server.CaTIES_ConceptAccumulator;
import edu.upmc.opi.caBIG.caTIES.server.CaTIES_ExporterPR;
import edu.upmc.opi.caBIG.caTIES.services.caTIES_TiesPipe.TiesPipe;
import edu.wustl.catissuecore.caties.util.CSVLogger;
import edu.wustl.catissuecore.caties.util.CaCoreAPIService;
import edu.wustl.catissuecore.caties.util.CaTIESConstants;
import edu.wustl.catissuecore.caties.util.CaTIESProperties;
import edu.wustl.catissuecore.domain.pathology.BinaryContent;
import edu.wustl.catissuecore.domain.pathology.Concept;
import edu.wustl.catissuecore.domain.pathology.ConceptReferent;
import edu.wustl.catissuecore.domain.pathology.ConceptReferentClassification;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.SemanticType;
import edu.wustl.catissuecore.domain.pathology.TextContent;
import edu.wustl.catissuecore.domain.pathology.XMLContent;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class ConceptCoder 
{
	private transient Logger logger = Logger.getCommonLogger(ConceptCoder.class);
	public ConceptCoder(DeidentifiedSurgicalPathologyReport deidReport, CaTIES_ExporterPR exporterPR, TiesPipe tiesPipe)throws SQLException
	{
		this.deidPathologyReport=deidReport;
		this.currentReportText="";
		if(this.deidPathologyReport.getTextContent()!=null)
		{
			TextContent textContent=(TextContent)CaCoreAPIService.getObject(TextContent.class, Constants.SYSTEM_IDENTIFIER,this.deidPathologyReport.getTextContent().getId());
			this.currentReportText= textContent.getData();
		}
		this.exporterPR=exporterPR;
		this.tiesPipe=tiesPipe;
	}
	
	public void process() throws Exception  // change name to process if this class need not to be a thread, then do not catch exception here, throw it upward 
	{
		Long startTime=new Date().getTime();
		try
		{
			logger.info("Inside Concept coder");
			processPathologyReport();
			logger.info("Report is Concept coded by caties");
			logger.info("Updating Report");
			updateReport();
			this.deidPathologyReport=(DeidentifiedSurgicalPathologyReport)CaCoreAPIService.updateObject(this.deidPathologyReport);
		}
		catch (Exception ex) 
		{
			Long endTime=new Date().getTime();
			logger.error("Concept coding process failed for report id:"+this.deidPathologyReport.getId()+" "+ex.getMessage());
			this.deidPathologyReport.setReportStatus(CaTIESConstants.CC_PROCESS_FAILED);
			this.deidPathologyReport=(DeidentifiedSurgicalPathologyReport)CaCoreAPIService.updateObject(this.deidPathologyReport);
			CSVLogger.info(CaTIESConstants.LOGGER_CONCEPT_CODER, new Date().toString()+","+this.deidPathologyReport.getId()+","+CaTIESConstants.CC_PROCESS_FAILED+","+ex.getMessage()+","+(endTime-startTime));
		}
		if(!this.deidPathologyReport.getReportStatus().equalsIgnoreCase(CaTIESConstants.CC_PROCESS_FAILED))
		{
			Long endTime=new Date().getTime();
			logger.info("Report is updated");
			CSVLogger.info(CaTIESConstants.LOGGER_CONCEPT_CODER, new Date().toString()+","+this.deidPathologyReport.getId()+","+CaTIESConstants.CONCEPT_CODED+","+"Report Concept Coded successfully,"+(endTime-startTime));
		}
		this.deidPathologyReport=null;
		logger.info("Report is updated");
	}
	
	private void processPathologyReport() throws Exception
	{
		this.tiesResponse = executeTiesPipeDirect();
		disassembleTiesResponse();
		extractCodesFromChirps();	
	}

	/**
	 * Method executeTiesPipeDirect.
	 * 
	 * @return String
	 * @throws Exception
	 */
	private String executeTiesPipeDirect() throws Exception 
	{
		org.jdom.Document requestDocument = new org.jdom.Document(new Element("Report"));
		requestDocument.getRootElement().setAttribute("name", this.minOID);
		CDATA cdata = new CDATA(this.currentReportText);
		Element bodyElement = new Element("Body");
		bodyElement.addContent(cdata);
		requestDocument.getRootElement().addContent(bodyElement);
		String requestAsString = CaTIES_JDomUtils.convertDocumentToString(requestDocument, null);
		String tiesResponse = this.tiesPipe.processMessage(requestAsString);
		logger.info("Got ties respose!");
		logger.debug("Got ties response of length " + tiesResponse.length());
		return tiesResponse;
	}

	 /**
	 * Method updateReport
	 */
	 private void updateReport() throws Exception
	 {
		logger.info("*********************Inside update report***************");
		 try 
		 {
			 if((CaTIESProperties.getValue(CaTIESConstants.CATIES_SAVE_BI_CONTENT)).equalsIgnoreCase("true"))
			 {
				 BinaryContent binaryContent=new BinaryContent();
				 binaryContent.setData(this.gateXML);
				 binaryContent.setSurgicalPathologyReport(this.deidPathologyReport);
				 this.deidPathologyReport.setBinaryContent(binaryContent);
			 }
			 if((CaTIESProperties.getValue(CaTIESConstants.CATIES_SAVE_XML_CONTENT)).equalsIgnoreCase("true"))
			 {
				 XMLContent xmlContent=new XMLContent();
				 xmlContent.setData(this.chirpsXML);
				 xmlContent.setSurgicalPathologyReport(this.deidPathologyReport);
				 this.deidPathologyReport.setXmlContent(xmlContent);
			 }
			 this.deidPathologyReport.setReportStatus(CaTIESConstants.CONCEPT_CODED);
		}
		 catch (Exception ex) 
		 {
			 logger.error("Error occured while updating deidentified pathology report");
			 throw ex;
		 }
	 }

	/**
	 * Method extractGateXmlAndChirps.
	 */
	private void disassembleTiesResponse() throws Exception
	{
		//
		// Extract Gate XML and CHIRPS
		//
		try 
		{
			logger.info("Extracting GATE XML and chirps");
			SAXBuilder builder = new SAXBuilder();
			byte[] byteArray = this.tiesResponse.getBytes();
			if(this.tiesResponse.equalsIgnoreCase(CaTIESConstants.ERROR_GATE))
			{
				throw new Exception("Error with GATE library! Unable to connect");
			}
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			org.jdom.Document responseDocument = builder.build(byteArrayInputStream);
			Element responseRootElement = responseDocument.getRootElement();

			Element reportCodesElement = responseRootElement.getChild(CaTIESConstants.TAG_REPORTCODES); //"ReportCodes");
			this.reportCodesAccumulator = new CaTIES_ConceptAccumulator();
			this.reportCodesAccumulator.xmlDeSerialize(reportCodesElement);

			Collection<ConceptReferent> conceptReferentCollection=xmlDeSerializeLocal(reportCodesElement);
			this.deidPathologyReport.setConceptReferentCollection(conceptReferentCollection);
			
			Element gateXMLElement = responseRootElement.getChild(CaTIESConstants.TAG_GATEXML);  //"GateXML");
			gateXMLElement = gateXMLElement.getChild(CaTIESConstants.TAG_GATEDOCUMENT);  //"GateDocument");
			Element chirpsXMLElement = (Element) responseRootElement.getChild(CaTIESConstants.TAG_CHIRPSXML);  //"ChirpsXML");
			chirpsXMLElement = chirpsXMLElement.getChild(CaTIESConstants.TAG_ENVELOPE);  //"Envelope");
			Document gateXMLDocument = new Document((Element) gateXMLElement.clone());
			Document chirpsXMLDocument = new Document((Element) chirpsXMLElement.clone());
			this.gateXML = CaTIES_JDomUtils.convertDocumentToString(gateXMLDocument, null);
			this.chirpsXML = CaTIES_JDomUtils.convertDocumentToString(chirpsXMLDocument, null);
			logger.debug("Got gateXML of length " + this.gateXML.length());
			logger.debug("Got chirpsXML of length " + this.chirpsXML.length());
		}
		catch(JDOMParseException ex)
		{
			logger.error("Error in disassembleTiesResponse()");
			logger.error("Error in parsing TIES response. Not in XML format"+ex);
			throw new Exception("Error in parsing TIES response. Not in XML format");
		}
		catch (Exception ex) 
		{
			logger.error("Error in disassembleTiesResponse()");
			logger.error("Failed to parse the pay load XML."+ex);
			this.gateXML = "";
			this.chirpsXML = "";
			throw new Exception("Failed to parse the pay load XML");
		}
	}

	/**
	 * Method extractCodesFromChirps.
	 */
	private void extractCodesFromChirps() 
	{
		//
		// Extract the codes from the CHIRPS
		//
		this.exporterPR.setCHIRPsDocument(chirpsXML);
		this.exporterPR.execute();
		this.theCodes = this.exporterPR.getCodesAsString();
		logger.debug("Got codes of length " + this.theCodes.length());
	}
	
	public Collection<ConceptReferent> xmlDeSerializeLocal(Element conceptSetElement) throws Exception
	{
		Collection indexedConceptElements = conceptSetElement.getChildren(CaTIESConstants.TAG_INDEXED_CONCEPT);  //"IndexedConcept") ;
		Map<String, Concept> conceptCodeMap=new HashMap<String, Concept>();
		Map<String, ConceptReferentClassification> conceptReferrentClassicificationMap=new HashMap<String, ConceptReferentClassification>();
		Collection<ConceptReferent> conceptReferentSet=new HashSet<ConceptReferent>();
		try
		{
			for (Iterator indexedConceptIterator = indexedConceptElements.iterator(); indexedConceptIterator.hasNext();) 
			{
				// IndexedConcept
				Element indexedConceptElement = (Element)indexedConceptIterator.next();
				
				// ConceptReferent
				Element conceptReferentElement = indexedConceptElement.getChild(CaTIESConstants.TAG_CONCEPT_REFERENT);  //"ConceptReferent") ;
				ConceptReferent conceptReferentToAdd = new ConceptReferent() ;
				conceptReferentToAdd.setEndOffset(new Long(conceptReferentElement.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_END_OFFSET)));  //"endOffset")));
				conceptReferentToAdd.setStartOffset(new Long(conceptReferentElement.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_START_OFFSET))); //"startOffset")));
	//			conceptReferentToAdd.setDocumentFragment(conceptReferentElement.getAttributeValue("documentFragment"));
			    conceptReferentToAdd.setIsModifier(new Boolean(conceptReferentElement.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_ISMODIFIER)));  //"isModifier")));
			    conceptReferentToAdd.setIsNegated(new Boolean(conceptReferentElement.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_ISNEGATED)));  //"isNegated")));
			    conceptReferentToAdd.setDeIdentifiedSurgicalPathologyReport(this.deidPathologyReport);
			    
			    // Concept
			    Element conceptElement = (Element) indexedConceptElement.getChild(CaTIESConstants.TAG_CONCEPT);  //"Concept");
			    Concept conceptToAdd = new Concept();
				conceptToAdd.setConceptUniqueIdentifier(conceptElement.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_CUI));  //"cui"));
				conceptToAdd.setName(conceptElement.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_NAME));  //"name")) ;
				SemanticType semanticType=new SemanticType();
				semanticType.setLabel(conceptElement.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_SEMANTICTYPE));  //"semanticType"));
				conceptToAdd.setSemanticType(semanticType);
				conceptToAdd.setConceptReferentCollection(new HashSet());
				if(conceptCodeMap.get(conceptToAdd.getConceptUniqueIdentifier())!=null)
				{
					conceptToAdd=conceptCodeMap.get(conceptToAdd.getConceptUniqueIdentifier());		
				}	
				(conceptToAdd.getConceptReferentCollection()).add(conceptReferentToAdd);
				conceptCodeMap.put(conceptToAdd.getConceptUniqueIdentifier().toUpperCase(),conceptToAdd);
				
				// ConceptClassification
				Element conceptClassificationElement = (Element) indexedConceptElement.getChild(CaTIESConstants.TAG_CONCEPT_CLASSIFICATION);  //"ConceptClassification");
				ConceptReferentClassification conceptClassificationToAdd = new ConceptReferentClassification();
			    conceptClassificationToAdd.setName(conceptClassificationElement.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_NAME));  //"name"));
			    conceptClassificationToAdd.setConceptReferentCollection(new HashSet());
			    if(conceptReferrentClassicificationMap.get(conceptClassificationToAdd.getName())!=null)
				{
			    	conceptClassificationToAdd=conceptReferrentClassicificationMap.get(conceptClassificationToAdd.getName());		
				}	
				(conceptClassificationToAdd.getConceptReferentCollection()).add(conceptReferentToAdd);
				conceptReferrentClassicificationMap.put(conceptClassificationToAdd.getName().toUpperCase(),conceptClassificationToAdd);
				
				conceptReferentToAdd.setConcept(conceptToAdd);
				conceptReferentToAdd.setConceptReferentClassification(conceptClassificationToAdd);
				conceptReferentSet.add(conceptReferentToAdd);
			}
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			logger.error("Exception in deserialize"+ex);
			throw ex;
		}
		return conceptReferentSet;
	}
	
	/**
	 * Field tiesPipe.
	 */
	private TiesPipe tiesPipe = null;
	/**
	 * Field tiesResponse.
	 */
	private String tiesResponse = "";

	private CaTIES_ConceptAccumulator reportCodesAccumulator = null;

	/**
	 * Field gateXML.
	 */
	private String gateXML = "";

	/**
	 * Field chirpsXML.
	 */
	private String chirpsXML = "";

	/**
	 * Field theCodes.
	 */
	private String theCodes = "";
	/**
	 * Field minOID.
	 */
	private String minOID = "";

	/**
	 * Field currentReportText.
	 */
	private String currentReportText = "";
	/**
	 * Field exporterPR.
	 */
	private CaTIES_ExporterPR exporterPR = null;

	private DeidentifiedSurgicalPathologyReport deidPathologyReport;
}
