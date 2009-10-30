
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

/**
 * @author janhavi_hasabnis
 *
 */
public class ConceptCoder
{

	private transient final Logger logger = Logger.getCommonLogger(ConceptCoder.class);

	/**
	 * @param deidReport - deidReport
	 * @param exporterPRParam - exporterPRParam
	 * @param tiesPipeParam - tiesPipeParam
	 * @throws SQLException - SQLException
	 */
	public ConceptCoder(DeidentifiedSurgicalPathologyReport deidReport,
			CaTIES_ExporterPR exporterPRParam, TiesPipe tiesPipeParam) throws SQLException
	{
		this.deidPathologyReport = deidReport;
		this.currentReportText = "";
		if (this.deidPathologyReport.getTextContent() != null)
		{
			final TextContent textContent = (TextContent) CaCoreAPIService.getObject(
					TextContent.class, Constants.SYSTEM_IDENTIFIER, this.deidPathologyReport
							.getTextContent().getId());
			this.currentReportText = textContent.getData();
		}
		this.exporterPR = exporterPRParam;
		this.tiesPipe = tiesPipeParam;
	}

	/**
	 * @throws Exception - Exception
	 */
	public void process() throws Exception // change name to process if this class need not to be a thread, then do not catch exception here, throw it upward
	{
		final Long startTime = new Date().getTime();
		try
		{
			this.logger.info("Inside Concept coder");
			this.processPathologyReport();
			this.logger.info("Report is Concept coded by caties");
			this.logger.info("Updating Report");
			this.updateReport();
			this.deidPathologyReport = (DeidentifiedSurgicalPathologyReport) CaCoreAPIService
					.updateObject(this.deidPathologyReport);
		}
		catch (final Exception ex)
		{
			final Long endTime = new Date().getTime();
			this.logger.error("Concept coding process failed for report id:"
					+ this.deidPathologyReport.getId() + " " + ex.getMessage(),ex);
			this.deidPathologyReport.setReportStatus(CaTIESConstants.CC_PROCESS_FAILED);
			this.deidPathologyReport = (DeidentifiedSurgicalPathologyReport) CaCoreAPIService
					.updateObject(this.deidPathologyReport);
			CSVLogger.info(CaTIESConstants.LOGGER_CONCEPT_CODER, new Date().toString() + ","
					+ this.deidPathologyReport.getId() + "," + CaTIESConstants.CC_PROCESS_FAILED
					+ "," + ex.getMessage() + "," + (endTime - startTime));
		}
		if (!this.deidPathologyReport.getReportStatus().equalsIgnoreCase(
				CaTIESConstants.CC_PROCESS_FAILED))
		{
			final Long endTime = new Date().getTime();
			this.logger.info("Report is updated");
			CSVLogger.info(CaTIESConstants.LOGGER_CONCEPT_CODER, new Date().toString() + ","
					+ this.deidPathologyReport.getId() + "," + CaTIESConstants.CONCEPT_CODED + ","
					+ "Report Concept Coded successfully," + (endTime - startTime));
		}
		this.deidPathologyReport = null;
		this.logger.info("Report is updated");
	}

	/**
	 * @throws Exception - Exception
	 */
    private void processPathologyReport() throws Exception
    {
          this.currentReportText=removeIllegalXmlCharacters();
          this.tiesResponse = executeTiesPipeDirect();
          disassembleTiesResponse();
          extractCodesFromChirps();     
    }
    /**
     * 
     * @return String values.
     */
    public String removeIllegalXmlCharacters() 
    {
    	   String sprText="\n\n"+this.currentReportText;
    	   //this.logger.info("ReportText:"+sprText) ;
    	   //System.out.println("ReportText:"+sprText) ;
          // illegal XML character
    	  String result = sprText;
    	  this.logger.info("Before Removing illegal Char:"+result+"\n\n");
          char[] illegalChar={0x1d,0xc,'\\',':'};
          try 
          {
                StringBuffer stringBuffer = new StringBuffer(sprText);
                // loop to check each character
                for (int idx = 0; idx < stringBuffer.length(); idx++) 
                {
                      for(int i=0;i<illegalChar.length;i++)
                      {
                            // check for illegal character
                            if (stringBuffer.charAt(idx) == illegalChar[i]) 
                            {
//                                Logger.out.error("Found bad character.");
                                  stringBuffer.setCharAt(idx, ' ');
                            }
                      }
                }
                result = stringBuffer.toString();
//                result.replaceAll("__", "  ");
                result=result.replaceAll("     ", "    .");
//                result.replaceAll("\\(\\+\\)", "(.)");
                result.replaceAll("\\.br+\\)", ".   ");
//              result.replaceAll("\\", " ");
//              System.out.println(result);
                this.logger.info("After Removing illegal Char:"+result);
//               System.out.println("After Removing illegal Char:"+result);
          }
          catch (Exception ex) 
          {
        	  this.logger.error("Error in removeIllegalXmlCharacters method"+ex.getMessage(),ex);
        	  ex.printStackTrace();
          }
          return result+"\n\n";
    }

	/**
	 * Method executeTiesPipeDirect.
	 * @return String
	 * @throws Exception - Exception
	 */
	private String executeTiesPipeDirect() throws Exception
	{
		final org.jdom.Document requestDocument = new org.jdom.Document(new Element("Report"));
		requestDocument.getRootElement().setAttribute("name", this.minOID);
		final CDATA cdata = new CDATA(this.currentReportText);
		final Element bodyElement = new Element("Body");
		bodyElement.addContent(cdata);
		requestDocument.getRootElement().addContent(bodyElement);
		final String requestAsString = CaTIES_JDomUtils.convertDocumentToString(requestDocument,
				null);
		final String tiesResponse = this.tiesPipe.processMessage(requestAsString);
		this.logger.info("Got ties respose!");
		this.logger.debug("Got ties response of length " + tiesResponse.length());
		return tiesResponse;
	}

	/**
	* Method updateReport
	* @throws Exception - Exception
	*/
	private void updateReport() throws Exception
	{
		this.logger.info("*********************Inside update report***************");
		try
		{
			if ((CaTIESProperties.getValue(CaTIESConstants.CATIES_SAVE_BI_CONTENT))
					.equalsIgnoreCase("true"))
			{
				final BinaryContent binaryContent = new BinaryContent();
				binaryContent.setData(this.gateXML);
				binaryContent.setSurgicalPathologyReport(this.deidPathologyReport);
				this.deidPathologyReport.setBinaryContent(binaryContent);
			}
			if ((CaTIESProperties.getValue(CaTIESConstants.CATIES_SAVE_XML_CONTENT))
					.equalsIgnoreCase("true"))
			{
				final XMLContent xmlContent = new XMLContent();
				xmlContent.setData(this.chirpsXML);
				xmlContent.setSurgicalPathologyReport(this.deidPathologyReport);
				this.deidPathologyReport.setXmlContent(xmlContent);
			}
			this.deidPathologyReport.setReportStatus(CaTIESConstants.CONCEPT_CODED);
		}
		catch (final Exception ex)
		{
			this.logger.error("Error occured while updating deidentified" +
					" pathology report"+ex.getMessage(),ex);
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Method extractGateXmlAndChirps.
	 * @throws Exception - Exception
	 */
	private void disassembleTiesResponse() throws Exception
	{
		//
		// Extract Gate XML and CHIRPS
		//
		try
		{
			this.logger.info("Extracting GATE XML and chirps");
			final SAXBuilder builder = new SAXBuilder();
			final byte[] byteArray = this.tiesResponse.getBytes();
			if (this.tiesResponse.equalsIgnoreCase(CaTIESConstants.ERROR_GATE))
			{
				throw new Exception("Error with GATE library! Unable to connect");
			}
			final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			final org.jdom.Document responseDocument = builder.build(byteArrayInputStream);
			final Element responseRootElement = responseDocument.getRootElement();

			final Element reportCodesElement = responseRootElement
					.getChild(CaTIESConstants.TAG_REPORTCODES); //"ReportCodes");
			this.reportCodesAccumulator = new CaTIES_ConceptAccumulator();
			this.reportCodesAccumulator.xmlDeSerialize(reportCodesElement);

			final Collection<ConceptReferent> conceptReferentCollection = this
					.xmlDeSerializeLocal(reportCodesElement);
			this.deidPathologyReport.setConceptReferentCollection(conceptReferentCollection);

			Element gateXMLElement = responseRootElement.getChild(CaTIESConstants.TAG_GATEXML); //"GateXML");
			gateXMLElement = gateXMLElement.getChild(CaTIESConstants.TAG_GATEDOCUMENT); //"GateDocument");
			Element chirpsXMLElement = responseRootElement.getChild(CaTIESConstants.TAG_CHIRPSXML); //"ChirpsXML");
			chirpsXMLElement = chirpsXMLElement.getChild(CaTIESConstants.TAG_ENVELOPE); //"Envelope");
			final Document gateXMLDocument = new Document((Element) gateXMLElement.clone());
			final Document chirpsXMLDocument = new Document((Element) chirpsXMLElement.clone());
			this.gateXML = CaTIES_JDomUtils.convertDocumentToString(gateXMLDocument, null);
			this.chirpsXML = CaTIES_JDomUtils.convertDocumentToString(chirpsXMLDocument, null);
			this.logger.debug("Got gateXML of length " + this.gateXML.length());
			this.logger.debug("Got chirpsXML of length " + this.chirpsXML.length());
		}
		catch (final JDOMParseException ex)
		{
			this.logger.error("Error in disassembleTiesResponse()");
			this.logger.error("Error in parsing TIES response. Not in XML format" + ex.getMessage(),ex);
			ex.printStackTrace();
			throw new Exception("Error in parsing TIES response. Not in XML format");
		}
		catch (final Exception ex)
		{
			this.logger.error("Error in disassembleTiesResponse()");
			this.logger.error("Failed to parse the pay load XML." + ex.getMessage(),ex);
			this.gateXML = "";
			this.chirpsXML = "";
			ex.printStackTrace();
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
		this.exporterPR.setCHIRPsDocument(this.chirpsXML);
		this.exporterPR.execute();
		this.theCodes = this.exporterPR.getCodesAsString();
		this.logger.debug("Got codes of length " + this.theCodes.length());
	}

	/**
	 * @param conceptSetElement - conceptSetElement
	 * @return Collection of ConceptReferent
	 * @throws Exception - Exception
	 */
	public Collection<ConceptReferent> xmlDeSerializeLocal(Element conceptSetElement)
			throws Exception
	{
		final Collection indexedConceptElements = conceptSetElement
				.getChildren(CaTIESConstants.TAG_INDEXED_CONCEPT); //"IndexedConcept") ;
		final Map<String, Concept> conceptCodeMap = new HashMap<String, Concept>();
		final Map<String, ConceptReferentClassification> conceptReferrentClassicificationMap = new HashMap<String, ConceptReferentClassification>();
		final Collection<ConceptReferent> conceptReferentSet = new HashSet<ConceptReferent>();
		try
		{
			for (final Iterator indexedConceptIterator = indexedConceptElements.iterator(); indexedConceptIterator
					.hasNext();)
			{
				// IndexedConcept
				final Element indexedConceptElement = (Element) indexedConceptIterator.next();

				// ConceptReferent
				final Element conceptReferentElement = indexedConceptElement
						.getChild(CaTIESConstants.TAG_CONCEPT_REFERENT); //"ConceptReferent") ;
				final ConceptReferent conceptReferentToAdd = new ConceptReferent();
				conceptReferentToAdd.setEndOffset(new Long(conceptReferentElement
						.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_END_OFFSET))); //"endOffset")));
				conceptReferentToAdd.setStartOffset(new Long(conceptReferentElement
						.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_START_OFFSET))); //"startOffset")));
				//			conceptReferentToAdd.setDocumentFragment(conceptReferentElement.getAttributeValue("documentFragment"));
				conceptReferentToAdd.setIsModifier(new Boolean(conceptReferentElement
						.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_ISMODIFIER))); //"isModifier")));
				conceptReferentToAdd.setIsNegated(new Boolean(conceptReferentElement
						.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_ISNEGATED))); //"isNegated")));
				conceptReferentToAdd
						.setDeIdentifiedSurgicalPathologyReport(this.deidPathologyReport);

				// Concept
				final Element conceptElement = indexedConceptElement
						.getChild(CaTIESConstants.TAG_CONCEPT); //"Concept");
				Concept conceptToAdd = new Concept();
				conceptToAdd.setConceptUniqueIdentifier(conceptElement
						.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_CUI)); //"cui"));
				conceptToAdd.setName(conceptElement
						.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_NAME)); //"name")) ;
				final SemanticType semanticType = new SemanticType();
				semanticType.setLabel(conceptElement
						.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_SEMANTICTYPE)); //"semanticType"));
				conceptToAdd.setSemanticType(semanticType);
				conceptToAdd.setConceptReferentCollection(new HashSet());
				if (conceptCodeMap.get(conceptToAdd.getConceptUniqueIdentifier()) != null)
				{
					conceptToAdd = conceptCodeMap.get(conceptToAdd.getConceptUniqueIdentifier());
				}
				(conceptToAdd.getConceptReferentCollection()).add(conceptReferentToAdd);
				conceptCodeMap.put(conceptToAdd.getConceptUniqueIdentifier().toUpperCase(),
						conceptToAdd);

				// ConceptClassification
				final Element conceptClassificationElement = indexedConceptElement
						.getChild(CaTIESConstants.TAG_CONCEPT_CLASSIFICATION); //"ConceptClassification");
				ConceptReferentClassification conceptClassificationToAdd = new ConceptReferentClassification();
				conceptClassificationToAdd.setName(conceptClassificationElement
						.getAttributeValue(CaTIESConstants.TAG_ATTRIBUTE_NAME)); //"name"));
				conceptClassificationToAdd.setConceptReferentCollection(new HashSet());
				if (conceptReferrentClassicificationMap.get(conceptClassificationToAdd.getName()) != null)
				{
					conceptClassificationToAdd = conceptReferrentClassicificationMap
							.get(conceptClassificationToAdd.getName());
				}
				(conceptClassificationToAdd.getConceptReferentCollection())
						.add(conceptReferentToAdd);
				conceptReferrentClassicificationMap.put(conceptClassificationToAdd.getName()
						.toUpperCase(), conceptClassificationToAdd);

				conceptReferentToAdd.setConcept(conceptToAdd);
				conceptReferentToAdd.setConceptReferentClassification(conceptClassificationToAdd);
				conceptReferentSet.add(conceptReferentToAdd);
			}
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			this.logger.error("Exception in deserialize" + ex.getMessage(),ex);
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
	private final String minOID = "";

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
