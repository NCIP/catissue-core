package edu.wustl.catissuecore.conceptcode;

import java.io.ByteArrayInputStream;
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
import edu.wustl.catissuecore.domain.pathology.BinaryContent;
import edu.wustl.catissuecore.domain.pathology.Concept;
import edu.wustl.catissuecore.domain.pathology.ConceptReferent;
import edu.wustl.catissuecore.domain.pathology.ConceptReferentClassification;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.SemanticType;
import edu.wustl.catissuecore.domain.pathology.XMLContent;
import edu.wustl.catissuecore.reportloader.CSVLogger;
import edu.wustl.catissuecore.reportloader.Parser;
import edu.wustl.catissuecore.reportloader.ReportLoaderUtil;
import edu.wustl.common.util.logger.Logger;

public class ConceptCoder 
{
	public ConceptCoder(DeidentifiedSurgicalPathologyReport deidReport, CaTIES_ExporterPR exporterPR, TiesPipe tiesPipe)
	{
		this.deidPathologyReport=deidReport;
		this.currentReportText= this.deidPathologyReport.getTextContent().getData();
		this.exporterPR=exporterPR;
		this.tiesPipe=tiesPipe;
	}
	
	public void process() throws Exception  // change name to process if this class need not to be a thread, then do not catch exception here, throw it upward 
	{
		try
		{
			Logger.out.info("Inside Concept coder");
			processPathologyReport();
			Logger.out.info("Report is Concept coded by caties");
			Logger.out.info("Updating Report");
			updateReport();
			ReportLoaderUtil.updateObject(this.deidPathologyReport);
		}
		catch (Throwable ex) 
		{
			Logger.out.error("Concept coding process failed for report id:"+this.deidPathologyReport.getId()+" "+ex.getMessage());
			this.deidPathologyReport.setReportStatus(Parser.CC_PROCESS_FAILED);
			CSVLogger.info(Parser.LOGGER_CONCEPT_CODER, new Date().toString()+","+this.deidPathologyReport.getId()+","+Parser.CC_PROCESS_FAILED+","+ex.getMessage());
		}
		if(!this.deidPathologyReport.getReportStatus().equalsIgnoreCase(Parser.CC_PROCESS_FAILED))
		{
			Logger.out.info("Report is updated");
			CSVLogger.info(Parser.LOGGER_CONCEPT_CODER, new Date().toString()+","+this.deidPathologyReport.getId()+","+Parser.CONCEPT_CODED+","+"Report Concept Coded successfully");
		}
		
		Logger.out.info("Report is updated");
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
		Logger.out.info("Got ties respose!");
		Logger.out.debug("Got ties response of length " + tiesResponse.length());
		return tiesResponse;
	}

	 /**
	 * Method updateReport
	 */
	 private void updateReport() throws Exception
	 {
		Logger.out.info("*********************Inside update report***************");
		 try 
		 {
			 BinaryContent binaryContent=new BinaryContent();
			 binaryContent.setData(this.gateXML);
			 binaryContent.setSurgicalPathologyReport(this.deidPathologyReport);
			 this.deidPathologyReport.setBinaryContent(binaryContent);
			 
			 XMLContent xmlContent=new XMLContent();
			 xmlContent.setData(this.chirpsXML);
			 xmlContent.setSurgicalPathologyReport(this.deidPathologyReport);
			 this.deidPathologyReport.setXmlContent(xmlContent);
			 
			 this.deidPathologyReport.setReportStatus(Parser.CONCEPT_CODED);
		 }
		 catch (Exception ex) 
		 {
			 Logger.out.error("Error occured while updating deidentified pathology report");
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
			Logger.out.info("Extracting GATE XML and chirps");
			SAXBuilder builder = new SAXBuilder();
			byte[] byteArray = this.tiesResponse.getBytes();
			if(this.tiesResponse.equalsIgnoreCase(Parser.ERROR_GATE))
			{
				throw new Exception("Error with GATE library! Unable to connect");
			}
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			org.jdom.Document responseDocument = builder.build(byteArrayInputStream);
			Element responseRootElement = responseDocument.getRootElement();

			Element reportCodesElement = responseRootElement.getChild(Parser.TAG_REPORTCODES); //"ReportCodes");
			this.reportCodesAccumulator = new CaTIES_ConceptAccumulator();
			this.reportCodesAccumulator.xmlDeSerialize(reportCodesElement);

			Collection<ConceptReferent> conceptReferentCollection=xmlDeSerializeLocal(reportCodesElement);
			this.deidPathologyReport.setConceptReferentCollection(conceptReferentCollection);
			
			Element gateXMLElement = responseRootElement.getChild(Parser.TAG_GATEXML);  //"GateXML");
			gateXMLElement = gateXMLElement.getChild(Parser.TAG_GATEDOCUMENT);  //"GateDocument");
			Element chirpsXMLElement = (Element) responseRootElement.getChild(Parser.TAG_CHIRPSXML);  //"ChirpsXML");
			chirpsXMLElement = chirpsXMLElement.getChild(Parser.TAG_ENVELOPE);  //"Envelope");
			Document gateXMLDocument = new Document((Element) gateXMLElement.clone());
			Document chirpsXMLDocument = new Document((Element) chirpsXMLElement.clone());
			this.gateXML = CaTIES_JDomUtils.convertDocumentToString(gateXMLDocument, null);
			this.chirpsXML = CaTIES_JDomUtils.convertDocumentToString(chirpsXMLDocument, null);
			Logger.out.debug("Got gateXML of length " + this.gateXML.length());
			Logger.out.debug("Got chirpsXML of length " + this.chirpsXML.length());
		}
		catch(JDOMParseException ex)
		{
			Logger.out.error("Error in disassembleTiesResponse()");
			Logger.out.error("Error in parsing TIES response. Not in XML format"+ex);
			throw new Exception("Error in parsing TIES response. Not in XML format");
		}
		catch (Exception ex) 
		{
			Logger.out.error("Error in disassembleTiesResponse()");
			Logger.out.error("Failed to parse the pay load XML."+ex);
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
		Logger.out.debug("Got codes of length " + this.theCodes.length());
	}
	
	public Collection<ConceptReferent> xmlDeSerializeLocal(Element conceptSetElement) throws Exception
	{
		Collection indexedConceptElements = conceptSetElement.getChildren(Parser.TAG_INDEXED_CONCEPT);  //"IndexedConcept") ;
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
				Element conceptReferentElement = indexedConceptElement.getChild(Parser.TAG_CONCEPT_REFERENT);  //"ConceptReferent") ;
				ConceptReferent conceptReferentToAdd = new ConceptReferent() ;
				conceptReferentToAdd.setEndOffset(new Long(conceptReferentElement.getAttributeValue(Parser.TAG_ATTRIBUTE_END_OFFSET)));  //"endOffset")));
				conceptReferentToAdd.setStartOffset(new Long(conceptReferentElement.getAttributeValue(Parser.TAG_ATTRIBUTE_START_OFFSET))); //"startOffset")));
	//			conceptReferentToAdd.setDocumentFragment(conceptReferentElement.getAttributeValue("documentFragment"));
			    conceptReferentToAdd.setIsModifier(new Boolean(conceptReferentElement.getAttributeValue(Parser.TAG_ATTRIBUTE_ISMODIFIER)));  //"isModifier")));
			    conceptReferentToAdd.setIsNegated(new Boolean(conceptReferentElement.getAttributeValue(Parser.TAG_ATTRIBUTE_ISNEGATED)));  //"isNegated")));
			    conceptReferentToAdd.setDeidentifiedSurgicalPathologyReport(this.deidPathologyReport);
			    
			    // Concept
			    Element conceptElement = (Element) indexedConceptElement.getChild(Parser.TAG_CONCEPT);  //"Concept");
			    Concept conceptToAdd = new Concept();
				conceptToAdd.setConceptUniqueIdentifier(conceptElement.getAttributeValue(Parser.TAG_ATTRIBUTE_CUI));  //"cui"));
				conceptToAdd.setName(conceptElement.getAttributeValue(Parser.TAG_ATTRIBUTE_NAME));  //"name")) ;
				SemanticType semanticType=new SemanticType();
				semanticType.setLabel(conceptElement.getAttributeValue(Parser.TAG_ATTRIBUTE_SEMANTICTYPE));  //"semanticType"));
				conceptToAdd.setSemanticType(semanticType);
				conceptToAdd.setConceptReferentCollection(new HashSet());
				if(conceptCodeMap.get(conceptToAdd.getConceptUniqueIdentifier())!=null)
				{
					conceptToAdd=conceptCodeMap.get(conceptToAdd.getConceptUniqueIdentifier());		
				}	
				(conceptToAdd.getConceptReferentCollection()).add(conceptReferentToAdd);
				conceptCodeMap.put(conceptToAdd.getConceptUniqueIdentifier().toUpperCase(),conceptToAdd);
				
				// ConceptClassification
				Element conceptClassificationElement = (Element) indexedConceptElement.getChild(Parser.TAG_CONCEPT_CLASSIFICATION);  //"ConceptClassification");
				ConceptReferentClassification conceptClassificationToAdd = new ConceptReferentClassification();
			    conceptClassificationToAdd.setName(conceptClassificationElement.getAttributeValue(Parser.TAG_ATTRIBUTE_NAME));  //"name"));
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
			Logger.out.error("Exception in deserialize"+ex);
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
