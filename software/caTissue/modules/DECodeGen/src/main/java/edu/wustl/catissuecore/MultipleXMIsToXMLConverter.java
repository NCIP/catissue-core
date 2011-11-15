/**
 * 
 */
package edu.wustl.catissuecore;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;
import gov.nih.nci.cagrid.metadata.xmi.XMIParser;
import gov.nih.nci.cagrid.metadata.xmi.XmiFileType;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Take multiple XMIs and produce a single caGrid domain model XML out of them.
 * 
 * @author Denis G. Krylov
 * 
 */
public class MultipleXMIsToXMLConverter {

	private static final Log LOG = LogFactory
			.getLog(MultipleXMIsToXMLConverter.class);

	private List<File> xmiFiles = new ArrayList<File>();
	private File domainModelFile;
	private DomainModel mergedModel;
	private Map<String, String> idToIdMap = new HashMap<String, String>();

	/**
	 * @param xmiFiles
	 * @param domainModelFile
	 */
	public MultipleXMIsToXMLConverter(List<File> xmiFiles, File domainModelFile) {
		if (xmiFiles == null || xmiFiles.isEmpty()) {
			throw new RuntimeException("Please specify one or more XMI files.");
		}
		if (domainModelFile == null) {
			throw new RuntimeException(
					"Please specify an output caGrid domain model XML file.");
		}
		this.xmiFiles = xmiFiles;
		this.domainModelFile = domainModelFile;
	}

	/**
	 * 
	 */
	public void convert() {
		FileWriter writer = null;
		try {
			for (File xmiFile : xmiFiles) {
				XMIParser parser = new XMIParser("catissue", "2.0");
				DomainModel model = null;
				model = parser.parse(xmiFile, XmiFileType.SDK_40_EA);
				updateIDs(model);
				if (mergedModel == null) {
					mergedModel = model;
				} else {
					mergeIntoModel(model);
				}
			}

			writer = new FileWriter(domainModelFile);
			MetadataUtils.serializeDomainModel(mergedModel, writer);
			writer.flush();
		} catch (Exception e) {
			LOG.error(e, e);
			throw new RuntimeException(e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	private void updateIDs(DomainModel model) {
		if (model.getExposedUMLAssociationCollection() != null) {
			for (DomainModelExposedUMLAssociationCollection ass : Arrays
					.asList(model.getExposedUMLAssociationCollection())) {
				if (ass.getUMLAssociation() != null) {
					for (UMLAssociation umlAss : ass.getUMLAssociation()) {
						if (umlAss.getSourceUMLAssociationEdge() != null) {
							updateIDs(umlAss.getSourceUMLAssociationEdge()
									.getUMLAssociationEdge());
						}
						if (umlAss.getTargetUMLAssociationEdge() != null) {
							updateIDs(umlAss.getTargetUMLAssociationEdge()
									.getUMLAssociationEdge());
						}
					}
				}
			}
		}
		if (model.getExposedUMLClassCollection() != null
				&& model.getExposedUMLClassCollection().getUMLClass() != null) {
			for (UMLClass uml : model.getExposedUMLClassCollection()
					.getUMLClass()) {
				if (uml.getId() != null)
					uml.setId(mapToUUID(uml.getId()));
			}
		}
		if (model.getUmlGeneralizationCollection() != null
				&& model.getUmlGeneralizationCollection()
						.getUMLGeneralization() != null) {
			for (UMLGeneralization gen : model.getUmlGeneralizationCollection()
					.getUMLGeneralization()) {
				if (gen.getSubClassReference() != null)
					updateIDs(gen.getSubClassReference());
				if (gen.getSuperClassReference() != null)
					updateIDs(gen.getSuperClassReference());
			}
		}
	}

	private void updateIDs(UMLAssociationEdge edge) {
		if (edge.getUMLClassReference() != null) {
			updateIDs(edge.getUMLClassReference());
		}
	}

	private void updateIDs(UMLClassReference ref) {
		if (ref.getRefid() != null) {
			ref.setRefid(mapToUUID(ref.getRefid()));
		}
	}

	private String mapToUUID(String refid) {
		if (idToIdMap.containsKey(refid))
			return idToIdMap.get(refid);
		String uuid = UUID.randomUUID().toString();
		idToIdMap.put(refid, uuid);
		return uuid;
	}

	private void mergeIntoModel(DomainModel model) {
		mergeExposedUMLAssociationCollection(
				mergedModel.getExposedUMLAssociationCollection(),
				model.getExposedUMLAssociationCollection());
		mergeExposedUMLClassCollection(
				mergedModel.getExposedUMLClassCollection(),
				model.getExposedUMLClassCollection());
		mergeUmlGeneralizationCollection(
				mergedModel.getUmlGeneralizationCollection(),
				model.getUmlGeneralizationCollection());
	}

	private void mergeUmlGeneralizationCollection(
			DomainModelUmlGeneralizationCollection root,
			DomainModelUmlGeneralizationCollection branch) {
		List<UMLGeneralization> list = root.getUMLGeneralization() != null ? new ArrayList(
				Arrays.asList(root.getUMLGeneralization())) : new ArrayList();
		if (branch.getUMLGeneralization() != null) {
			list.addAll(Arrays.asList(branch.getUMLGeneralization()));
		}
		root.setUMLGeneralization(list.toArray(new UMLGeneralization[0]));
	}

	private void mergeExposedUMLClassCollection(
			DomainModelExposedUMLClassCollection root,
			DomainModelExposedUMLClassCollection branch) {
		List<UMLClass> list = root.getUMLClass() != null ? new ArrayList(
				Arrays.asList(root.getUMLClass())) : new ArrayList();
		if (branch.getUMLClass() != null) {
			list.addAll(Arrays.asList(branch.getUMLClass()));
		}
		root.setUMLClass(list.toArray(new UMLClass[0]));

	}

	private void mergeExposedUMLAssociationCollection(
			DomainModelExposedUMLAssociationCollection root,
			DomainModelExposedUMLAssociationCollection branch) {
		List<UMLAssociation> list = root.getUMLAssociation() != null ? new ArrayList(
				Arrays.asList(root.getUMLAssociation())) : new ArrayList();
		if (branch.getUMLAssociation() != null) {
			list.addAll(Arrays.asList(branch.getUMLAssociation()));
		}
		root.setUMLAssociation(list.toArray(new UMLAssociation[0]));

	}

	public static void main(String[] args) {
		System.out.println("Example usage: -i src\\test\\resources\\caTissueCore_v2.0.xmi -i src\\test\\resources\\org_specimen_model.xmi -o src\\test\\resources\\merged.xml");
		List<File> xmiFiles = new ArrayList<File>();
		File domainModelFile = new File("merged_domain_model.xml");
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			if ("-i".equals(arg))
				xmiFiles.add(new File(args[i + 1]));
			if ("-o".equals(arg))
				domainModelFile = new File(args[i + 1]);
		}
		MultipleXMIsToXMLConverter converter = new MultipleXMIsToXMLConverter(
				xmiFiles, domainModelFile);
		converter.convert();
	}

}
