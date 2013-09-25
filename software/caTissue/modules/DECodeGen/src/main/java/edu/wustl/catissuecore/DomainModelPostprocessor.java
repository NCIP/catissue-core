/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore;

import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;

/**
 * 
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DomainModelPostprocessor {

	private Map<String, String> hookEntities = new TreeMap<String, String>();
	private Set<String> abstractEntities = new HashSet<String>();
	private Set<String> staticPackages = new HashSet<String>();
	private String projectLongName;
	private String projectShortName;
	private String projectVersion;
	private String projectDescription;

	public DomainModelPostprocessor(String projectLongName,
			String projectShortName, String projectVersion,
			String projectDescription, Map<String, String> hookEntities,
			Set<String> abstractEntities, Set<String> staticPackages) {
		this.projectLongName = projectLongName;
		this.projectShortName = projectShortName;
		this.projectVersion = projectVersion;
		this.projectDescription = projectDescription;
		this.hookEntities = hookEntities;
		this.abstractEntities = abstractEntities;
		this.staticPackages = staticPackages;
	}

	void process(Element root) throws Exception {

		XPathFactory xFact = XPathFactory.newInstance();
		XPathExpression getUmlClasses = xFact.newXPath().compile(
				"//*[local-name()='UMLClass']");
		XPathExpression getUmlAtts = xFact
				.newXPath()
				.compile(
						"*[local-name()='umlAttributeCollection']/*[local-name()='UMLAttribute']");
		XPathExpression hasNonIdUmlAtts = xFact
				.newXPath()
				.compile(
						"count(*[local-name()='umlAttributeCollection']/*[local-name()='UMLAttribute' and not(@id)]) > 1");
		XPathExpression getUmlAttsCollEl = xFact.newXPath().compile(
				"*[local-name()='umlAttributeCollection']");
		XPathExpression getInvalidAttEls = xFact.newXPath().compile(
				"//*[local-name()='UMLAttribute' and not(@dataTypeName)]");

		// Find duplicate class ids and duplicate classes

		Map<String, Element> classIdsMap = new TreeMap<String, Element>();
		Map<String, Element> fqnSetMap = new TreeMap<String, Element>();
		Map<String, List<Element>> dupHookFqnMap = new TreeMap<String, List<Element>>();
		Map<String, List<Element>> dupDEFqnMap = new TreeMap<String, List<Element>>();
		List<Element> abstractEls = new ArrayList<Element>();

		// AbstractRecordEntry to keep
		Element areToKeep = null;

		NodeList nodes = (NodeList) getUmlClasses.evaluate(root,
				XPathConstants.NODESET);
		for (int i = 0; i < nodes.getLength(); i++) {
			Element el = (Element) nodes.item(i);
			String elFqn = getFullyQualifiedName(el);

			if (fqnSetMap.containsKey(elFqn)) {
				Element dupEl = fqnSetMap.get(elFqn);
				if (isHookEntity(elFqn)) {
					List<Element> dupEls = dupHookFqnMap.get(elFqn);
					if (dupEls == null) {
						dupEls = new ArrayList<Element>();
						dupHookFqnMap.put(elFqn, dupEls);
						dupEls.add(dupEl);
					}
					dupEls.add(el);
				} else if (this.abstractEntities.contains(elFqn)) {
					abstractEls.add(el);
				} else {
					// It must be a duplicated DE class
					// In this case, we'll want to delete the one that has only
					// a single id attribute
					List<Element> dupEls = dupDEFqnMap.get(elFqn);
					if (dupEls == null) {
						dupEls = new ArrayList<Element>();
						dupDEFqnMap.put(elFqn, dupEls);
						dupEls.add(dupEl);
					}
					dupEls.add(el);
				}
			}

			String id = el.getAttribute("id");
			if (classIdsMap.containsKey(id)) {
				Element otherEl = classIdsMap.get(id);
				String otherElFqn = getFullyQualifiedName(otherEl);
				throw new Exception(elFqn + " shares and id with " + otherElFqn
						+ ": " + id);
			}

			if (areToKeep == null
					&& abstractEntities.contains(elFqn)
					&& (Boolean) hasNonIdUmlAtts.evaluate(el,
							XPathConstants.BOOLEAN)) {
				areToKeep = el;
			}

			fqnSetMap.put(elFqn, el);
			classIdsMap.put(id, el);
		}

		// Delete duplicate, non-hook, DE model entities
		for (Entry<String, List<Element>> entry : dupDEFqnMap.entrySet()) {
			for (Element dupEl : entry.getValue()) {
				if (!(Boolean) hasNonIdUmlAtts.evaluate(dupEl,
						XPathConstants.BOOLEAN)) { // i.e. it has only an id
													// attribute

					// then, delete it and everything that references it
					String umlClassId = dupEl.getAttribute("id");
					deleteElements(getAssocEls(xFact, umlClassId, root));
					detach(dupEl);
				}
			}
		}

		// Process the hook entities
		for (Entry<String, String> entry : this.hookEntities.entrySet()) {

			String hookFqn = entry.getKey();

			// Get valid static entity
			String packageName = hookFqn.substring(0, hookFqn.lastIndexOf("."));
			String className = hookFqn.substring(hookFqn.lastIndexOf(".") + 1);
			Element staticUmlClassEl = getElement(
					xFact,
					"//*[local-name()='UMLClass' and @packageName='"
							+ packageName
							+ "' and @className='"
							+ className
							+ "' and count(*[local-name()='umlAttributeCollection']/"
							+ "*[local-name()='UMLAttribute' and not(@id)]) > 1]",
					root);
			if (staticUmlClassEl == null) {
				throw new Exception("Couldn't local static hook entity: "
						+ hookFqn);
			}

			for (Element umlClassEl : dupHookFqnMap.get(hookFqn)) {
				if (!(Boolean) hasNonIdUmlAtts.evaluate(umlClassEl,
						XPathConstants.BOOLEAN)) {
					// Merge the associations of static entities from DE models
					// with associations of static entities from static model
					mergeUMLAssociations(xFact, root,
							staticUmlClassEl.getAttribute("id"),
							umlClassEl.getAttribute("id"));
					// Delete static entities from DE models
					detach(umlClassEl);
				}
			}

			String hookREFqn = entry.getValue();

			// Merge elements
			Set<String> attSet = new HashSet<String>();
			Element targetUmlClassEl = null;
			List<Element> umlClassEls = dupHookFqnMap.get(hookREFqn);

			for (Element umlClassEl : umlClassEls) {

				if (targetUmlClassEl == null) {
					targetUmlClassEl = umlClassEl;
					continue;
				}

				// Merge UMLAttribute element
				NodeList attEls = (NodeList) getUmlAtts.evaluate(umlClassEl,
						XPathConstants.NODESET);
				for (int i = 0; i < attEls.getLength(); i++) {
					Element attEl = (Element) attEls.item(i);
					String attName = attEl.getAttribute("name");
					if (attSet.contains(attName)) {
						// We can ignore this attribute.
						continue;
					}
					attSet.add(attName);

					Element umlAttsCollEl = (Element) getUmlAttsCollEl
							.evaluate(targetUmlClassEl, XPathConstants.NODE);

					umlAttsCollEl.appendChild(detach(attEl));
				}

				// Merge UMLAssociation elements
				mergeUMLAssociations(xFact, root,
						targetUmlClassEl.getAttribute("id"),
						umlClassEl.getAttribute("id"));

				// Remove generalizations
				String genElsStr = "//*[local-name()='UMLGeneralization' and "
						+ "./*[local-name()='subClassReference' and @refid='"
						+ umlClassEl.getAttribute("id") + "']]";
				XPathExpression getGenElsExp = xFact.newXPath().compile(
						genElsStr);
				NodeList genEls = (NodeList) getGenElsExp.evaluate(root,
						XPathConstants.NODESET);
				for (int i = 0; i < genEls.getLength(); i++) {
					Element genEl = (Element) genEls.item(i);
					detach(genEl);
				}

				// Remove empty class
				detach(umlClassEl);
			}
		}

		// Keep only 1 AbstractRecordEntry node
		// and reuse its id for all other elements referring to instances of
		// other AbstractRecordEntry nodes
		for (Element abstractEl : abstractEls) {
			String nodesWithRefId = String.format("//*[@refid='%s']",
					abstractEl.getAttribute("id"));
			XPathExpression xpathExp = xFact.newXPath().compile(
					nodesWithRefId);
			NodeList refs = (NodeList) xpathExp.evaluate(root,
					XPathConstants.NODESET);

			// Replace the 'refid' attribute of the found Nodes
			// with the 'id' attribute value of the one of the preserved Element
			// stored in areToKeep var
			for (int i = 0; i < refs.getLength(); i++) {
				Node n = refs.item(i);
				NamedNodeMap attrMap = n.getAttributes();
				attrMap.getNamedItem("refid").setNodeValue(
						areToKeep.getAttribute("id"));
			}
		}

		// Remove superclasses with no subclasses
		for (Element abstractEl : abstractEls) {
			String checkGenElsStr = "count(//*[local-name()='UMLGeneralization' and "
					+ "./*[local-name()='superClassReference' and @refid='"
					+ abstractEl.getAttribute("id") + "']]) > 0";
			XPathExpression checkGenElsExp = xFact.newXPath().compile(
					checkGenElsStr);
			if (!(Boolean) checkGenElsExp
					.evaluate(root, XPathConstants.BOOLEAN)) {
				detach(abstractEl);
			}
		}

		// Remove invalid attributes elements
		NodeList invalidAttEls = (NodeList) getInvalidAttEls.evaluate(root,
				XPathConstants.NODESET);
		for (int i = 0; i < invalidAttEls.getLength(); i++) {
			detach((Element) invalidAttEls.item(i));
		}

		// Set allowableAsTarget="false" on all non-static classes
		StringBuilder sb = new StringBuilder();
		sb.append("//*[local-name()='UMLClass' and ");
		for (Iterator<String> i = this.staticPackages.iterator(); i.hasNext();) {
			String staticPackageName = i.next();
			sb.append(" not(@packageName='").append(staticPackageName)
					.append("')");
			if (i.hasNext()) {
				sb.append(" and");
			}
		}
		sb.append("]");
		XPathExpression getNonStaticModelUmlClassEls = xFact.newXPath()
				.compile(sb.toString());
		NodeList umlClassEls = (NodeList) getNonStaticModelUmlClassEls
				.evaluate(root, XPathConstants.NODESET);
		for (int i = 0; i < umlClassEls.getLength(); i++) {
			Element umlClassEl = (Element) umlClassEls.item(i);
			umlClassEl.setAttribute("allowableAsTarget", "false");
		}

		// Remove associations where both sides have no role name
		deleteElements(getElements(
				xFact,
				"//*[local-name()='UMLAssociation' and ./"
						+ "*[local-name()='sourceUMLAssociationEdge']/"
						+ "*[local-name()='UMLAssociationEdge' and not(@roleName)] and"
						+ "*[local-name()='targetUMLAssociationEdge']/"
						+ "*[local-name()='UMLAssociationEdge' and not(@roleName)]"
						+ "]", root));

		// Set project info
		root.setAttribute("projectLongName", this.projectLongName);
		root.setAttribute("projectShortName", this.projectShortName);
		root.setAttribute("projectVersion", this.projectVersion);
		root.setAttribute("projectDescription", this.projectDescription);

		// Add missing roleName attributes on UMLAssociation elements
		setAttribute(xFact,
				"//*[local-name()='UMLAssociationEdge' and not(@roleName)]",
				"roleName", "", root);

		// Add missing description attribute to UMLClass and UMLAttribute
		// elements
		setAttribute(xFact, "//*[(local-name()='UMLClass' or "
				+ "local-name()='UMLAttribute') and " + "not(@description)]",
				"description", "", root);

		// Add missing conceptDefition, conceptName, and conceptCode attributes
		// to SemanticMetadata elements
		setAttribute(
				xFact,
				"//*[local-name()='SemanticMetadata' and not(@conceptDefinition)]",
				"conceptDefinition", "", root);
		setAttribute(xFact,
				"//*[local-name()='SemanticMetadata' and not(@conceptName)]",
				"conceptName", "", root);
		setAttribute(xFact,
				"//*[local-name()='SemanticMetadata' and not(@conceptCode)]",
				"conceptCode", "", root);

		// Check for invalid references
		NodeList refEls = getElements(xFact,
				"//*[local-name()='UMLClassReference' "
						+ "or local-name()='superClassReference' "
						+ "or local-name()='subClassReference']", root);
		for (int i = 0; i < refEls.getLength(); i++) {
			Element refEl = (Element) refEls.item(i);
			String refid = refEl.getAttribute("refid");
			NodeList classEls = getElements(xFact,
					"//*[local-name()='UMLClass' and @id='" + refid + "']",
					root);
			if (classEls.getLength() == 0) {
				throw new Exception("No UMLClass for refid " + refid);
			}
		}
	}

	private void deleteElements(NodeList els) {
		for (int i = 0; i < els.getLength(); i++) {
			detach((Element) els.item(i));
		}
	}

	private Element getElement(XPathFactory xFact, String exp, Element ctx)
			throws Exception {
		XPathExpression xpath = xFact.newXPath().compile(exp);
		return (Element) xpath.evaluate(ctx, XPathConstants.NODE);
	}

	private void mergeUMLAssociations(XPathFactory xFact, Element root,
			String targetUmlClassId, String umlClassId) throws Exception {
		NodeList assocRefEls = getAssocEls(xFact, umlClassId, root);
		for (int i = 0; i < assocRefEls.getLength(); i++) {
			Element refEl = (Element) assocRefEls.item(i);
			refEl.setAttribute("refid", targetUmlClassId);
		}
	}

	private NodeList getAssocEls(XPathFactory xFact, String umlClassId,
			Element ctx) throws Exception {
		String assocRefElsStr = "//*[local-name()='UMLAssociation']/"
				+ "*[local-name()='sourceUMLAssociationEdge' or "
				+ "local-name()='targetUMLAssociationEdge']/"
				+ "*[local-name()='UMLAssociationEdge']/"
				+ "*[local-name()='UMLClassReference' and @refid='"
				+ umlClassId + "']";
		return getElements(xFact, assocRefElsStr, ctx);
	}

	private NodeList getElements(XPathFactory xFact, String exp, Element ctx)
			throws Exception {
		XPathExpression xpath = xFact.newXPath().compile(exp);
		return (NodeList) xpath.evaluate(ctx, XPathConstants.NODESET);
	}

	private void setAttribute(XPathFactory xFact, String exp, String attName,
			String attValue, Element ctx) throws Exception {
		XPathExpression xpath = xFact.newXPath().compile(exp);
		NodeList assocEls = (NodeList) xpath.evaluate(ctx,
				XPathConstants.NODESET);
		for (int i = 0; i < assocEls.getLength(); i++) {
			Element assocEl = (Element) assocEls.item(i);
			assocEl.setAttribute(attName, attValue);
		}
	}

	private Element detach(Element childEl) {
		Node parentNode = childEl.getParentNode();
		return (Element) parentNode.removeChild(childEl);
	}

	private boolean isHookEntity(String elFqn) {
		return this.hookEntities.containsKey(elFqn)
				|| this.hookEntities.containsValue(elFqn);
	}

	private String getFullyQualifiedName(Element el) {
		return el.getAttribute("packageName") + "."
				+ el.getAttribute("className");
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: input_path output_path");
			System.exit(1);
		}
		String inPath = args[0];
		String outPath = args[1];
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new File(inPath));

			Map<String, String> hookEntities = new TreeMap<String, String>();
			hookEntities
					.put("edu.wustl.catissuecore.domain.Participant",
							"edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry");
			hookEntities
					.put("edu.wustl.catissuecore.domain.Specimen",
							"edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry");
			hookEntities
					.put("edu.wustl.catissuecore.domain.SpecimenCollectionGroup",
							"edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry");
			hookEntities
					.put("edu.wustl.catissuecore.domain.processingprocedure.ActionApplication",
							"edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry");

			Set<String> abstractEntities = new HashSet<String>();
			abstractEntities
					.add("edu.common.dynamicextensions.domain.integration.AbstractRecordEntry");

			Set<String> staticPackages = new HashSet<String>();
			staticPackages.add("edu.wustl.catissuecore.domain");
			staticPackages
					.add("edu.wustl.catissuecore.domain.processingprocedure");

			DomainModelPostprocessor dmp = new DomainModelPostprocessor(
					"catissue", "catissue", "2.0", "catissue", hookEntities,
					abstractEntities, staticPackages);
			dmp.process(doc.getDocumentElement());

			Source source = new DOMSource(doc);
			Result result = new StreamResult(outPath);
			Transformer xformer = TransformerFactory.newInstance()
					.newTransformer();
			xformer.transform(source, result);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
