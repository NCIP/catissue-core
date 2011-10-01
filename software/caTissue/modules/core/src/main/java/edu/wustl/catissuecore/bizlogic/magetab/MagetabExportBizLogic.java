package edu.wustl.catissuecore.bizlogic.magetab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Level;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.MAGETABInvestigation;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.SDRF;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.ExtractNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.HybridizationNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SampleNode;
import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.SourceNode;
import uk.ac.ebi.arrayexpress2.magetab.exception.ParseException;
import uk.ac.ebi.arrayexpress2.magetab.parser.MAGETABParser;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.wustl.catissuecore.GSID.GSIDConstant;
import edu.wustl.catissuecore.bizlogic.CatissueDefaultBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.exception.DAOException;
import gov.nih.nci.logging.api.util.StringUtils;

/**
 * @author Alexander Zgursky
 * @author srikalyan
 */

public class MagetabExportBizLogic extends CatissueDefaultBizLogic {   
    

	private static final String IDF_FILE_LOCATION;
	private static final String OUTPUT_FOLDER_LOCATION;
	private static transient final org.apache.log4j.Logger logger = Logger
			.getLogger(MagetabExportBizLogic.class);
	static {		
		
		logger.getParent().setLevel((Level) Level.DEBUG);
		Properties defaultProps = new Properties();
		InputStream in = null;
		in = MagetabExportBizLogic.class.getClassLoader().getResourceAsStream(
				"magetab.properties");
		if (in != null) {
			try {
				defaultProps.load(in);
			} catch (IOException e) {
				logger.error(GSIDConstant.GSID_PROPERTIES_LOAD_ERROR, e);
				defaultProps = null;
			}
			if (defaultProps != null) {
				IDF_FILE_LOCATION = defaultProps
						.getProperty("magetab.idfFileLocation");
				OUTPUT_FOLDER_LOCATION = defaultProps
						.getProperty("magetab.outputFolderLocation");
			} else {
				IDF_FILE_LOCATION = "NOT_FOUND";
				OUTPUT_FOLDER_LOCATION = "NOT_FOUND";
			}
		} else {
			IDF_FILE_LOCATION = "NOT_FOUND";
			OUTPUT_FOLDER_LOCATION = "NOT_FOUND";
		}

	}

	public MagetabExportBizLogic() {
	}

	public void export(List<List<Specimen>> specimenChains,
			MagetabExportConfig config, String zipFileName) {
		List<List<String>> sdrfTable = buildSdrfTable(specimenChains, config);
		Set<Integer> chainSelections = getFullSelection(sdrfTable);
		prepareMagetabZip(sdrfTable, chainSelections, zipFileName);
	}

	public void export(List<Long> specimenIds, String zipFileName) {
		List<List<Specimen>> specimens = querySpecimenChains(specimenIds);
		MagetabExportConfig config = getDefaultConfig();
		List<List<String>> sdrfTable = buildSdrfTable(specimens, config);
		Set<Integer> chainSelections = getFullSelection(sdrfTable);
		prepareMagetabZip(sdrfTable, chainSelections, zipFileName);
	}

	public List<List<String>> buildSdrfTable(List<List<Specimen>> specimens,
			MagetabExportConfig config) {
		SDRF sdrf = buildSdrfGraph(specimens, config);
		return sdrfGraphToTable(sdrf);
	}

	private List<List<String>> sdrfGraphToTable(SDRF sdrf) {
		String sdrfStr = sdrf.toString();
		String[] lines = sdrfStr.split("\n", 0);
		List<List<String>> sdrfTable = new ArrayList<List<String>>(
				lines.length - 1);
		for (String line : lines) {
			String[] row = line.split("\t", -1);
			if (!("!!!source".equals(row[0]))) { // <-- check for dummy row
				sdrfTable.add(Arrays.asList(row));
			}
		}
		return sdrfTable;
	}

	/**
	 * 
	 * @param specimens
	 *            Specimens to export. <li>May contain nulls as specimens<br>
	 *            <li>Sublists must contain at least two elements<br> <li>First
	 *            element defines source, last element defines extract,
	 *            everything in between - samples.
	 * 
	 */
	private SDRF buildSdrfGraph(List<List<Specimen>> specimens,
			MagetabExportConfig config) {
		MAGETABInvestigation investigation = new MAGETABInvestigation();
		SDRF sdrf = investigation.SDRF;

		// TODO:this ugly hack was introduced due to the bugs in limpopo parser
		// -
		// the renderer is failing if try to add a node chain having more nodes
		// than the previous chains.
		// What's make it worse is that source nodes get sorted alphabetically
		// during rendering..
		// The following line of code adds a fake node chain with the source
		// name starting with "!!!",
		// which hopefully should be rendered first. This fake chain would hold
		// as many as the maximum nodes
		// among all the chains.
//		addFakeChain(sdrf, specimens);
		DAO dao = null;
		try {
			dao = this.openDAOSession(null);
			for (List<Specimen> row : specimens) {

				ListIterator<Specimen> iterator = row.listIterator();
				Specimen specimen = iterator.next();

				// Populating source node
				SourceNode sourceNode = sdrf.lookupNode(getNodeName(specimen),
						SourceNode.class);
				if (sourceNode == null) {
					sourceNode = new SourceNode();
					sourceNode.setNodeType("sourcename");
					sourceNode.setNodeName(getNodeName(specimen));
					for (String transformerName : config
							.getSourceTransformers()) {						
						Transformer transformer = config.getTransformers().get(
								transformerName);
						transformer.transform(specimen, sourceNode);						
					}
				}
				AbstractSDRFNode parent = sourceNode;
				// Populating sample nodes
				if (iterator.hasNext()) {
					specimen = iterator.next();
				} else {
					// we have only source - store it and continue
				//	sdrf.storeNode(sourceNode);
					logger.error("continuing");
					continue;
				}
				int sampleCounter=0;
				if (config.getSampleColumnSelected().equals("true")) {
					while (iterator.hasNext()) {
						sampleCounter++;
						SampleNode sampleNode = sdrf.lookupNode(getNodeName(specimen), SampleNode.class);					
						if (sampleNode == null) {
							sampleNode = new SampleNode();
							sampleNode.setNodeType("sample1");	
							sampleNode.setNodeName(getNodeName(specimen));
							logger.debug("the sample node name is"+sampleNode.getNodeName());
							for (String transformerName : config
									.getSampleTransformers()) {
								logger.debug("The Sample transformer name is "+transformerName +" and the specimen id is "+specimen.getId()+" the type is "+specimen.getSpecimenType());
								Transformer transformer = config.getTransformers()
										.get(transformerName);
								transformer.transform(specimen, sampleNode);							
							}
						}					
						try
						{
						parent.addChildNode(sampleNode);
						sampleNode.addParentNode(parent);
						sdrf.storeNode(parent);
						parent = sampleNode;					
						}catch(Exception e){
							logger.debug("Error occured during sample convertion");
						}
	
						specimen = iterator.next();
					}
				}
				if(sampleCounter==0)
					logger.error("no sample data");
				// We have an extract node left
				ExtractNode extractNode = sdrf.lookupNode(getNodeName(specimen),
						ExtractNode.class);
				if (extractNode == null) {
					extractNode = new ExtractNode();
					extractNode.setNodeType("extractname");
					extractNode.setNodeName(getNodeName(specimen));
					for (String transformerName : config
							.getExtractTransformers()) {
						//logger.error("The extract transformer name is "+transformerName);
						Transformer transformer = config.getTransformers().get(
								transformerName);
						transformer.transform(specimen, extractNode);						
					}
				}
				parent.addChildNode(extractNode);
				extractNode.addParentNode(parent);
				sdrf.storeNode(parent);
				sdrf.storeNode(extractNode);

			}
			return sdrf;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (BizLogicException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		} finally {
			if (dao != null) {
				try {
					this.closeDAOSession(dao);
				} catch (BizLogicException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getNodeName(Specimen specimen)
	{
		String nodeName= StringUtils.isBlank(specimen.getLabel()) ? StringUtils.isBlank(specimen.getBarcode()) ? specimen.getId()+"_ID" : specimen.getBarcode(): specimen.getLabel();
		logger.debug("the node name '" +nodeName+"'");
		return nodeName;
	}

	private Set<Integer> getFullSelection(List<List<String>> sdrfTable) {
		Set<Integer> chainSelections = new HashSet<Integer>();
		for (int i = 1; i < sdrfTable.size(); i++) {
			chainSelections.add(i);
		}
		return chainSelections;
	}

	public MagetabExportConfig getDefaultConfig() {
		return new MagetabExportConfig();
	}

	public List<List<Specimen>> querySpecimenChains(List<Long> specimenIds) {
		DAO dao = null;
		try {
			dao = this.openDAOSession(null);
			Set<AbstractSpecimen> extracts = new LinkedHashSet<AbstractSpecimen>();
			for (Long specimenId : specimenIds) {
				Specimen specimen = (Specimen) dao.retrieveById(
						Specimen.class.getName(), specimenId);
				findExtracts(dao, specimen, extracts);
			}

			List<List<Specimen>> specimenChains = new LinkedList<List<Specimen>>();
			for (AbstractSpecimen extract : extracts) {
				LinkedList<Specimen> chain = new LinkedList<Specimen>();
				AbstractSpecimen head = extract;
				while (head != null) {
					if (head instanceof Specimen) {
						chain.addFirst((Specimen) head);
					}

					head = head.getParentSpecimen();
				}
				specimenChains.add(chain);
			}

			return specimenChains;

		} catch (BizLogicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			if (dao != null) {
				try {
					this.closeDAOSession(dao);
				} catch (BizLogicException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void findExtracts(DAO dao, AbstractSpecimen specimen,
			Set<AbstractSpecimen> extracts) {
		if ("molecular".equalsIgnoreCase(specimen.getSpecimenClass())) {
			extracts.add(specimen);
		} else {
			for (AbstractSpecimen child : /* specimen.getChildSpecimenCollection() */getChildSpecimenCollection(
					dao, specimen)) {
				findExtracts(dao, child, extracts);
			}
		}
	}

	private Collection<AbstractSpecimen> getChildSpecimenCollection(DAO dao,
			AbstractSpecimen specimen) {
		String hql = "select child "
				+ "from edu.wustl.catissuecore.domain.AbstractSpecimen as parent, "
				+ "edu.wustl.catissuecore.domain.AbstractSpecimen as child "
				+ "where child.parentSpecimen = parent and " + "parent.id="
				+ specimen.getId();
		try {
			List<?> children = dao.executeQuery(hql);
			return (Collection<AbstractSpecimen>) children;
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	// public List<List<Specimen>> querySpecimenChains(List<Long> specimenIds,
	// String dnaOrRna) {
	// DAO dao = null;
	//
	// try {
	// dao = this.openDAOSession(null);
	// List<List<Specimen>> specimens = new LinkedList<List<Specimen>>();
	// for (Long specimenId : specimenIds) {
	// Specimen specimen = (Specimen)dao.retrieveById(Specimen.class.getName(),
	// specimenId);
	// LinkedList<Specimen> row = new LinkedList<Specimen>();
	//
	// AbstractSpecimen head = null;
	// FindExtractResult result = findExtract(specimen, dnaOrRna);
	// if (result == null) {
	// continue;
	// // row.add(null);
	// // head = specimen;
	// } else {
	// head = result.extract;
	// }
	//
	// while (head != null) {
	// if (head instanceof Specimen) {
	// row.addFirst((Specimen) head);
	// }
	//
	// head = head.getParentSpecimen();
	// }
	//
	// specimens.add(row);
	//
	// }
	//
	// return specimens;
	//
	// } catch (BizLogicException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return null;
	// } catch (DAOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return null;
	// } finally {
	// if (dao != null) {
	// try {
	// this.closeDAOSession(dao);
	// } catch (BizLogicException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }
	// }
	//
	// private FindExtractResult findExtract(AbstractSpecimen specimen, String
	// dnaOrRna) {
	// if (!"molecular".equalsIgnoreCase(specimen.getSpecimenClass())) {
	// Collection<AbstractSpecimen> children =
	// specimen.getChildSpecimenCollection();
	// FindExtractResult nonExactMatch = null;
	// for (AbstractSpecimen child : children) {
	// FindExtractResult result = findExtract(child, dnaOrRna);
	// if (result == null) {
	// //keep searching
	// } else if (result.exactMatch) {
	// return result;
	// } else if (nonExactMatch == null) { // keep first non-exact match
	// nonExactMatch = result;
	// }
	// }
	//
	// return nonExactMatch;
	//
	// // } else if (specimen.getSpecimenType() == null) {
	// // return new FindExtractResult(specimen, false);
	// // } else if
	// (specimen.getSpecimenType().toUpperCase().contains(dnaOrRna)) {
	// // return new FindExtractResult(specimen, true);
	// } else {
	// return new FindExtractResult(specimen, true);
	// // return new FindExtractResult(specimen, false);
	// }
	// }
	// private class FindExtractResult {
	// public AbstractSpecimen extract;
	// public boolean exactMatch;
	// public FindExtractResult(AbstractSpecimen extract, boolean exactMatch) {
	// this.extract = extract;
	// this.exactMatch = exactMatch;
	// }
	// }
	//

//	private void addFakeChain(SDRF sdrf, List<List<Specimen>> specimens) {
//		int maxLength = 0;
//		for (List<Specimen> row : specimens) {
//			if (row.size() > maxLength) {
//				maxLength = row.size();
//			}
//		}
//		try {
//			SourceNode sourceNode = new SourceNode();
//			sourceNode.setNodeType("sourcename");
//			sourceNode.setNodeName("!!!source");
//			AbstractSDRFNode parent = sourceNode;
//			for (int i = 0; i < maxLength - 2; i++) {
//				SampleNode sampleNode = new SampleNode();
//				sampleNode.setNodeType("samplename");
//				sampleNode.setNodeName("!!!sample" + (i + 1));
//				parent.addChildNode(sampleNode);
//				sampleNode.addParentNode(parent);
//				sdrf.storeNode(parent);
//				parent = sampleNode;
//			}
//
//			if (maxLength > 1) {
//				ExtractNode extractNode = new ExtractNode();
//				extractNode.setNodeType("extractname");
//				extractNode.setNodeName("!!!extract");
//
//				parent.addChildNode(extractNode);
//				extractNode.addParentNode(parent);
//				sdrf.storeNode(parent);
//				parent = extractNode;
//			}
//
//			HybridizationNode hybNode = new HybridizationNode();
//			hybNode.setNodeType("hybridizationname");
//			hybNode.setNodeName("!!!hybridization");
//
//			parent.addChildNode(hybNode);
//			hybNode.addParentNode(parent);
//			sdrf.storeNode(parent);
//			parent = hybNode;
//
//			sdrf.storeNode(parent);
//
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

	private MAGETABInvestigation parseTestInvestigation() {
		MAGETABParser parser = new MAGETABParser();
		URL idfURL;
		try {
			idfURL = URI.create("file://" + IDF_FILE_LOCATION)
			// .create("http://www.ebi.ac.uk/arrayexpress/files/E-GEOD-18858/E-GEOD-18858.idf.txt")
					.toURL();
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		// parser.addErrorItemListener(new MyErrorItemListener());
		try {
			return parser.parse(idfURL);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private void copy(String src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst.getAbsolutePath()
				+ File.separatorChar + "magetab.idf");
		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	private void prepareMagetabZip(List<List<String>> sdrfTable,
			Set<Integer> selections, String zipFileName) {
		MAGETABInvestigation investigation = new MAGETABInvestigation();
		investigation.IDF.sdrfFile = Arrays
				.asList(new String[] { "magetab.sdrf" });
		MagetabExportConfig config = getDefaultConfig();
		investigation.IDF.termSourceFile = new LinkedList<String>();
		investigation.IDF.termSourceName = new LinkedList<String>();
		investigation.IDF.termSourceVersion = new LinkedList<String>();
		for (TermSource termSource : config.getTermSources().values()) {
			investigation.IDF.termSourceFile.add(termSource.getFile());
			investigation.IDF.termSourceName.add(termSource.getName());
			investigation.IDF.termSourceVersion.add(termSource.getVersion());
		}
		try {
			// write out
			File output = new File(OUTPUT_FOLDER_LOCATION);
			if (!output.exists()) {
				output.mkdirs();
			}
			System.out.println("Starting writing round 1...");
			File outputDir1 = new File(output, "temp"
					+ System.currentTimeMillis()
					+ java.util.UUID.randomUUID().toString());
			if (!outputDir1.exists()) {
				outputDir1.mkdirs();
			}
			// copy the IDF file.
			copy(IDF_FILE_LOCATION, outputDir1);

			// File idfOut1 = new File(outputDir1, investigation.accession
			// + ".idf.txt");
			// File sdrfOut1 = new File(outputDir1,
			// investigation.IDF.sdrfFile.get(0));

			File idfOut1 = new File(outputDir1, "magetab.idf");
			File sdrfOut1 = new File(outputDir1, "magetab.sdrf");

			// TODO:using template file - need to take it from resources
			// writeString2File(investigation.IDF.toString(), idfOut1);

			// Writer writer = new StringWriter();
			// SDRFWriter sdrfWriter = new SDRFWriter(writer);
			// sdrfWriter.write(investigation.SDRF);
			String sdrfAsString = renderSdrf(sdrfTable, selections);
			writeString2File(sdrfAsString, sdrfOut1);

			createZip(Arrays.asList(new File[] { idfOut1, sdrfOut1 }),
					zipFileName);

		} catch (Exception ex) {
			logger.error("test failed", ex);
		}

	}

	private String renderSdrf(List<List<String>> sdrfTable,
			Set<Integer> selections) {
		StringBuilder sb = new StringBuilder();
		renderRow(sb, sdrfTable.get(0));
		for (int j = 1; j < sdrfTable.size(); j++) {
			if (selections.contains(j)) {
				sb.append("\n");
				renderRow(sb, sdrfTable.get(j));
			}
		}

		return sb.toString();
	}

	private void renderRow(StringBuilder sb, List<String> row) {
		Iterator<String> iter = row.iterator();
		for (sb.append(iter.next()); iter.hasNext(); sb.append("\t").append(
				iter.next()))
			;
	}

	private void writeString2File(String str, File file) throws IOException {
		Writer idfWriter = new OutputStreamWriter(new FileOutputStream(file),
				Charset.forName("UTF-8"));
		idfWriter.write(str);
		idfWriter.close();
	}

	private void createZip(Collection<File> files, String zipFileName) {
		byte[] buf = new byte[1024];
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipFileName));

			for (Iterator<File> iterator = files.iterator(); iterator.hasNext();) {
				File f = iterator.next();
				FileInputStream in = new FileInputStream(f);
				out.putNextEntry(new ZipEntry(f.getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
				//
				// f.delete();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// private static class MyErrorItemListener implements ErrorItemListener {
	// public void errorOccurred(ErrorItem item) {
	// ErrorCode code = null;
	// for (ErrorCode ec : ErrorCode.values()) {
	// if (item.getErrorCode() == ec.getIntegerValue()) {
	// code = ec;
	// break;
	// }
	// }
	//
	// if (code != null) {
	// System.out.println("Listener reported error...");
	// System.out.println("\tError Code: " + item.getErrorCode()
	// + " [" + code.getErrorMessage() + "]");
	// System.out.println("\tType: " + item.getErrorType());
	// System.out.println("\tFile: " + item.getParsedFile());
	// System.out.println("\tLine: "
	// + (item.getLine() != -1 ? item.getLine() : "n/a"));
	// System.out.println("\tColumn: "
	// + (item.getCol() != -1 ? item.getCol() : "n/a"));
	// System.out
	// .println("\tAdditional comment: " + item.getComment());
	// } else {
	// System.out.println("Listener reported error...");
	// System.out.println("\tError Code: " + item.getErrorCode());
	// System.out.println("\tFile: " + item.getParsedFile());
	// System.out.println("\tLine: "
	// + (item.getLine() != -1 ? item.getLine() : "n/a"));
	// System.out.println("\tColumn: "
	// + (item.getCol() != -1 ? item.getCol() : "n/a"));
	// System.out
	// .println("\tAdditional comment: " + item.getComment());
	// }
	// }
	// }
}
