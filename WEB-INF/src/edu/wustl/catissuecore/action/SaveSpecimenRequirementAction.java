
package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.CreateSpecimenTemplateForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.DeriveSpecimenBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * @author renuka_bajpai
 */
public class SaveSpecimenRequirementAction extends BaseAction
{

	/**
	 * logger.
	 */

	private transient Logger logger = Logger.getCommonLogger(SaveSpecimenRequirementAction.class);
	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @return ActionForward : ActionForward
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{

		CreateSpecimenTemplateForm createSpecimenTemplateForm = (CreateSpecimenTemplateForm) form;
		HttpSession session = request.getSession();
		String operation = (String) request.getParameter(Constants.OPERATION);
		String eventKey = (String) request.getParameter(Constants.EVENT_KEY);
		String nodeId = "New_" + eventKey;
		session.setAttribute(Constants.TREE_NODE_ID, nodeId);
		if (operation.equals(Constants.ADD))
		{
			Map collectionProtocolEventMap = (Map) session
					.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
			CollectionProtocolEventBean collectionProtocolEventBean =
				(CollectionProtocolEventBean) collectionProtocolEventMap
					.get(eventKey);
			Integer totalNoOfSpecimen = collectionProtocolEventBean.getSpecimenRequirementbeanMap()
					.size();
			SpecimenRequirementBean specimenRequirementBean = createSpecimenBean(
					createSpecimenTemplateForm,
					collectionProtocolEventBean.getUniqueIdentifier(),
					totalNoOfSpecimen);
			if (specimenRequirementBean != null)
			{
				collectionProtocolEventBean.addSpecimenRequirementBean(specimenRequirementBean);
			}
		}
		if (operation.equals(Constants.EDIT))
		{
			try
			{
				initCreateSpecimenTemplateForm(createSpecimenTemplateForm, request);
			}
			catch (Exception e)
			{
				logger.debug(e.getMessage(), e);
				ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError("errors.item", e
						.getMessage()));
				saveErrors(request, actionErrors);
				return (mapping.findForward(Constants.FAILURE));
			}
		}
		return (mapping.findForward(Constants.SUCCESS));
	}
	/**
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 * @param totalNoOfSpecimen : totalNoOfSpecimen
	 * @return SpecimenRequirementBean : SpecimenRequirementBean
	 */
	private SpecimenRequirementBean createSpecimenBean(
			CreateSpecimenTemplateForm createSpecimenTemplateForm, String uniqueIdentifier,
			Integer totalNoOfSpecimen)
	{
		SpecimenRequirementBean specimenRequirementBean = createSpecimen(
				createSpecimenTemplateForm, uniqueIdentifier, totalNoOfSpecimen);
		Map aliquotSpecimenMap = null;
		Collection deriveSpecimenCollection = null;
		if (createSpecimenTemplateForm.getNoOfAliquots() != null
				&& !createSpecimenTemplateForm.getNoOfAliquots().equals(""))
		{
			aliquotSpecimenMap = (Map) getAliquots(createSpecimenTemplateForm,
					specimenRequirementBean.getUniqueIdentifier());
		}

		Map deriveSpecimenMap = createSpecimenTemplateForm.deriveSpecimenMap();

		if (createSpecimenTemplateForm.getNoOfDeriveSpecimen() == 0)
		{
			createSpecimenTemplateForm.setDeriveSpecimenValues(null);
			deriveSpecimenMap = null;
		}
		MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.bean");
		try
		{
			if (deriveSpecimenMap != null && deriveSpecimenMap.size() != 0)
			{
				deriveSpecimenCollection = parser.generateData(deriveSpecimenMap);
				deriveSpecimenMap = getderiveSpecimen(deriveSpecimenCollection,
						createSpecimenTemplateForm,
						specimenRequirementBean.getUniqueIdentifier());
			}
		}
		catch (Exception e)
		{
			logger.debug(e.getMessage(), e);
			e.printStackTrace();
		}

		specimenRequirementBean.setAliquotSpecimenCollection((LinkedHashMap) aliquotSpecimenMap);
		specimenRequirementBean.setDeriveSpecimenCollection((LinkedHashMap) deriveSpecimenMap);
		return specimenRequirementBean;
	}
	/**
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 * @param totalNoOfSpecimen : totalNoOfSpecimen
	 * @return SpecimenRequirementBean : SpecimenRequirementBean
	 */
	private SpecimenRequirementBean createSpecimen(
			CreateSpecimenTemplateForm createSpecimenTemplateForm, String uniqueIdentifier,
			Integer totalNoOfSpecimen)
	{
		SpecimenRequirementBean specimenRequirementBean = new SpecimenRequirementBean();
		specimenRequirementBean.setParentName(Constants.ALIAS_SPECIMEN + "_" + uniqueIdentifier);
		specimenRequirementBean.setUniqueIdentifier(uniqueIdentifier
				+ Constants.UNIQUE_IDENTIFIER_FOR_NEW_SPECIMEN + totalNoOfSpecimen);
		specimenRequirementBean.setDisplayName(Constants.ALIAS_SPECIMEN + "_" + uniqueIdentifier
				+ Constants.UNIQUE_IDENTIFIER_FOR_NEW_SPECIMEN + totalNoOfSpecimen);
		specimenRequirementBean.setLineage(Constants.NEW_SPECIMEN);
		setSessionDataBean(createSpecimenTemplateForm, specimenRequirementBean);

		// Aliquot
		specimenRequirementBean.setNoOfAliquots(createSpecimenTemplateForm.getNoOfAliquots());
		specimenRequirementBean.setQuantityPerAliquot(createSpecimenTemplateForm
				.getQuantityPerAliquot());
		specimenRequirementBean.setStorageContainerForAliquotSpecimem(createSpecimenTemplateForm
				.getStorageLocationForAliquotSpecimen());

		// Derive
		if (createSpecimenTemplateForm.getNoOfDeriveSpecimen() == 0)
		{
			createSpecimenTemplateForm.setDeriveSpecimenValues(null);
		}
		specimenRequirementBean.setNoOfDeriveSpecimen(createSpecimenTemplateForm
				.getNoOfDeriveSpecimen());
		specimenRequirementBean.setDeriveSpecimen(createSpecimenTemplateForm.deriveSpecimenMap());
		return specimenRequirementBean;
	}

	/**
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param specimenRequirementBean : specimenRequirementBean
	 */
	private void setSessionDataBean(CreateSpecimenTemplateForm createSpecimenTemplateForm,
			SpecimenRequirementBean specimenRequirementBean)
	{
		specimenRequirementBean.setClassName(createSpecimenTemplateForm.getClassName());
		specimenRequirementBean.setType(createSpecimenTemplateForm.getType());
		specimenRequirementBean.setTissueSide(createSpecimenTemplateForm.getTissueSide());
		specimenRequirementBean.setTissueSite(createSpecimenTemplateForm.getTissueSite());
		specimenRequirementBean.setPathologicalStatus(createSpecimenTemplateForm
				.getPathologicalStatus());
		specimenRequirementBean.setConcentration(createSpecimenTemplateForm.getConcentration());
		specimenRequirementBean.setQuantity(createSpecimenTemplateForm.getQuantity());
		specimenRequirementBean.setStorageContainerForSpecimen(createSpecimenTemplateForm
				.getStorageLocationForSpecimen());

		// Collected and received events
		specimenRequirementBean.setCollectionEventUserId(createSpecimenTemplateForm
				.getCollectionEventUserId());
		specimenRequirementBean.setReceivedEventUserId(createSpecimenTemplateForm
				.getReceivedEventUserId());
		specimenRequirementBean.setCollectionEventContainer(createSpecimenTemplateForm
				.getCollectionEventContainer());
		specimenRequirementBean.setReceivedEventReceivedQuality(createSpecimenTemplateForm
				.getReceivedEventReceivedQuality());
		specimenRequirementBean.setCollectionEventCollectionProcedure(createSpecimenTemplateForm
				.getCollectionEventCollectionProcedure());
	}
	/**
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 * @return Map : Map
	 */
	private Map getAliquots(CreateSpecimenTemplateForm createSpecimenTemplateForm,
			String uniqueIdentifier)
	{
		Map aliquotMap = new LinkedHashMap();
		String noOfAliquotes = createSpecimenTemplateForm.getNoOfAliquots();
		Double aliquotCount = Double.parseDouble(noOfAliquotes);
		Double aliquotQuantity = calculateQuantity(createSpecimenTemplateForm);
		for (int iCount = 1; iCount <= aliquotCount; iCount++)
		{
			SpecimenRequirementBean specimenRequirementBean = createSpecimen(
					createSpecimenTemplateForm, uniqueIdentifier, iCount);
			createAliquot(createSpecimenTemplateForm, uniqueIdentifier, aliquotQuantity.toString(),
					iCount, specimenRequirementBean);
			aliquotMap.put(specimenRequirementBean.getUniqueIdentifier(), specimenRequirementBean);
		}
		return aliquotMap;
	}
	/**
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 * @param quantityPerAliquot : quantityPerAliquot
	 * @param iCount : iCount
	 * @param specimenRequirementBean : specimenRequirementBean
	 * @return String : String
	 */
	private String createAliquot(CreateSpecimenTemplateForm createSpecimenTemplateForm,
			String uniqueIdentifier, String quantityPerAliquot, int iCount,
			SpecimenRequirementBean specimenRequirementBean)
	{
		specimenRequirementBean.setUniqueIdentifier(uniqueIdentifier
				+ Constants.UNIQUE_IDENTIFIER_FOR_ALIQUOT + iCount);
		specimenRequirementBean.setDisplayName(Constants.ALIAS_SPECIMEN + "_" + uniqueIdentifier
				+ Constants.UNIQUE_IDENTIFIER_FOR_ALIQUOT + iCount);
		specimenRequirementBean.setLineage(Constants.ALIQUOT);
		if (quantityPerAliquot == null || quantityPerAliquot.equals(""))
		{
			quantityPerAliquot = "0";
		}
		specimenRequirementBean.setQuantity(quantityPerAliquot);
		specimenRequirementBean.setNoOfAliquots(null);
		specimenRequirementBean.setQuantityPerAliquot(null);
		specimenRequirementBean.setStorageContainerForAliquotSpecimem(null);
		specimenRequirementBean.setStorageContainerForSpecimen(createSpecimenTemplateForm
				.getStorageLocationForAliquotSpecimen());
		specimenRequirementBean.setDeriveSpecimen(null);
		specimenRequirementBean.setNoOfDeriveSpecimen(0);
		return quantityPerAliquot;
	}
	/**
	 *
	 * @param deriveSpecimenCollection : deriveSpecimenCollection
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 * @return Map : map
	 */
	private Map getderiveSpecimen(Collection deriveSpecimenCollection,
			CreateSpecimenTemplateForm createSpecimenTemplateForm, String uniqueIdentifier)
	{
		Map deriveSpecimenMap = new LinkedHashMap();
		Iterator deriveSpecimenCollectionItr = deriveSpecimenCollection.iterator();
		Integer deriveSpecimenCount = 1;
		while (deriveSpecimenCollectionItr.hasNext())
		{
			DeriveSpecimenBean deriveSpecimenBean = (DeriveSpecimenBean) deriveSpecimenCollectionItr
					.next();
			SpecimenRequirementBean specimenRequirementBean = createSpecimen(
					createSpecimenTemplateForm, uniqueIdentifier, deriveSpecimenCount);
			specimenRequirementBean.setUniqueIdentifier(uniqueIdentifier
					+ Constants.UNIQUE_IDENTIFIER_FOR_DERIVE + deriveSpecimenCount);
			specimenRequirementBean.setLineage(Constants.DERIVED_SPECIMEN);
			specimenRequirementBean.setDisplayName(Constants.ALIAS_SPECIMEN + "_"
					+ uniqueIdentifier + Constants.UNIQUE_IDENTIFIER_FOR_DERIVE
					+ deriveSpecimenCount);

			specimenRequirementBean.setQuantity(deriveSpecimenBean.getQuantity());
			specimenRequirementBean.setConcentration(deriveSpecimenBean.getConcentration());
			specimenRequirementBean.setClassName(deriveSpecimenBean.getSpecimenClass());
			specimenRequirementBean.setType(deriveSpecimenBean.getSpecimenType());
			specimenRequirementBean.setStorageContainerForSpecimen(deriveSpecimenBean
					.getStorageLocation());
			// Aliquot
			specimenRequirementBean.setNoOfAliquots(null);
			specimenRequirementBean.setQuantityPerAliquot(null);
			specimenRequirementBean.setStorageContainerForAliquotSpecimem("");
			// Derive
			specimenRequirementBean.setDeriveSpecimen(null);
			specimenRequirementBean.setNoOfDeriveSpecimen(0);
			deriveSpecimenMap.put(specimenRequirementBean.getUniqueIdentifier(),
					specimenRequirementBean);
			deriveSpecimenCount = deriveSpecimenCount + 1;
		}

		return deriveSpecimenMap;
	}
	/**
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param request : request
	 * @throws Exception : Exception
	 */
	private void initCreateSpecimenTemplateForm(
			CreateSpecimenTemplateForm createSpecimenTemplateForm, HttpServletRequest request)
			throws Exception
	{
		HttpSession session = request.getSession();
		SpecimenRequirementBean specimenRequirementBean = (SpecimenRequirementBean) session
				.getAttribute(Constants.EDIT_SPECIMEN_REQUIREMENT_BEAN);
		setSessionDataBean(createSpecimenTemplateForm, specimenRequirementBean);

		int noOfAliquots = 0;
		if (createSpecimenTemplateForm.getNoOfAliquots() != null
				&& !createSpecimenTemplateForm.getNoOfAliquots().equals(""))
		{
			noOfAliquots = new Integer(createSpecimenTemplateForm.getNoOfAliquots());
		}
		int noOfBeanAliquots = 0;
		if (specimenRequirementBean.getNoOfAliquots() != null
				&& !specimenRequirementBean.getNoOfAliquots().equals(""))
		{
			noOfBeanAliquots = new Integer(specimenRequirementBean.getNoOfAliquots());
		}
		int totalNewAliquots = noOfAliquots - noOfBeanAliquots;
		if (totalNewAliquots < 0)
		{
			logger.debug("Cannot delete aliquot(s)");
			throw new Exception("Cannot delete aliquot(s)");
		}
		LinkedHashMap newAliquotSpecimenMap = specimenRequirementBean
				.getAliquotSpecimenCollection();
		if (newAliquotSpecimenMap == null)
		{
			newAliquotSpecimenMap = new LinkedHashMap();
		}
		for (int i = 0; i < totalNewAliquots; i++)
		{
			int iCount = ++noOfBeanAliquots;
			SpecimenRequirementBean aliquotBean = createSpecimen(createSpecimenTemplateForm,
					specimenRequirementBean.getUniqueIdentifier(), iCount);
			Double aliquotQuantity = calculateQuantity(createSpecimenTemplateForm);
			createAliquot(createSpecimenTemplateForm,
					specimenRequirementBean.getUniqueIdentifier(), aliquotQuantity.toString(),
					iCount, aliquotBean);
			newAliquotSpecimenMap.put(aliquotBean.getUniqueIdentifier(), aliquotBean);
		}
		specimenRequirementBean.setNoOfAliquots(createSpecimenTemplateForm.getNoOfAliquots());
		specimenRequirementBean.setQuantityPerAliquot(createSpecimenTemplateForm
				.getQuantityPerAliquot());
		specimenRequirementBean.setStorageContainerForAliquotSpecimem(createSpecimenTemplateForm
				.getStorageLocationForAliquotSpecimen());
		specimenRequirementBean.setAliquotSpecimenCollection(newAliquotSpecimenMap);

		// Derive
		int noOfDeriveSpecimen = new Integer(createSpecimenTemplateForm.getNoOfDeriveSpecimen());
		int noOfBeanDerive = new Integer(specimenRequirementBean.getNoOfDeriveSpecimen());
		int totalNewDeriveSpecimen = noOfDeriveSpecimen - noOfBeanDerive;
		Collection deriveSpecimenCollection = null;
		Map deriveSpecimenMap = createSpecimenTemplateForm.deriveSpecimenMap();
		MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.bean");
		if (deriveSpecimenMap != null && deriveSpecimenMap.size() != 0)
		{
			deriveSpecimenCollection = parser.generateData(deriveSpecimenMap);
			// for ordering
			IdComparator deriveSpBeanComp = new IdComparator();
			List deriveSpecimenList = new LinkedList(deriveSpecimenCollection);
			Collections.sort(deriveSpecimenList, deriveSpBeanComp);
			deriveSpecimenMap = getderiveSpecimen(deriveSpecimenList, createSpecimenTemplateForm,
					specimenRequirementBean.getUniqueIdentifier());
		}

		LinkedHashMap oldDeriveSpecimenMap = specimenRequirementBean.getDeriveSpecimenCollection();
		Object[] keyArr = deriveSpecimenMap.keySet().toArray();

		if (totalNewDeriveSpecimen > 0)
		{
			for (int iCount = 0; iCount < noOfBeanDerive; iCount++)
			{
				deriveSpecimenMap.remove(keyArr[iCount]);
			}
			if (oldDeriveSpecimenMap == null)
			{
				oldDeriveSpecimenMap = new LinkedHashMap();
			}
			oldDeriveSpecimenMap.putAll(deriveSpecimenMap);
			specimenRequirementBean.setDeriveSpecimenCollection(oldDeriveSpecimenMap);
		}
		else
		{
			LinkedHashMap deriveMap = new LinkedHashMap();
			for (int iCount = 0; iCount < keyArr.length; iCount++)
			{
				SpecimenRequirementBean newBean = (SpecimenRequirementBean) deriveSpecimenMap
						.get(keyArr[iCount]);
				SpecimenRequirementBean oldBean = (SpecimenRequirementBean) oldDeriveSpecimenMap
						.get(keyArr[iCount]);
				deriveMap.put(keyArr[iCount], deriveSpecimenMap.get(keyArr[iCount]));
				newBean.setAliquotSpecimenCollection(oldBean.getAliquotSpecimenCollection());
				newBean.setDeriveSpecimenCollection(oldBean.getDeriveSpecimenCollection());
			}
			specimenRequirementBean.setDeriveSpecimenCollection((LinkedHashMap) deriveMap);
			if (oldDeriveSpecimenMap != null)
			{
				oldDeriveSpecimenMap.clear();
			}
		}
		specimenRequirementBean.setNoOfDeriveSpecimen(createSpecimenTemplateForm
				.getNoOfDeriveSpecimen());
	}
	/**
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @return Double : Double
	 */
	private Double calculateQuantity(CreateSpecimenTemplateForm createSpecimenTemplateForm)
	{
		String noOfAliquotes = createSpecimenTemplateForm.getNoOfAliquots();
		String quantityPerAliquot = createSpecimenTemplateForm.getQuantityPerAliquot();
		Double aliquotCount = Double.parseDouble(noOfAliquotes);
		Double parentQuantity = Double.parseDouble(createSpecimenTemplateForm.getQuantity());
		Double aliquotQuantity = 0D;
		if (quantityPerAliquot == null || quantityPerAliquot.equals(""))
		{
			aliquotQuantity = parentQuantity / aliquotCount;
			parentQuantity = parentQuantity - (aliquotQuantity * aliquotCount);
		}
		else
		{
			aliquotQuantity = Double.parseDouble(quantityPerAliquot);
			parentQuantity = parentQuantity - (aliquotQuantity * aliquotCount);
		}
		return aliquotQuantity;
	}
}