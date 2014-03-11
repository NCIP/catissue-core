
package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.CreateSpecimenTemplateForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.DeriveSpecimenBean;
import edu.wustl.catissuecore.bean.SpecimenRequirementBean;
import edu.wustl.catissuecore.tree.QueryTreeNodeData;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveSpecimenRequirementAction.
 *
 * @author renuka_bajpai
 */
public class SaveSpecimenRequirementAction extends BaseAction
{

	/** logger. */

	private static final Logger LOGGER = Logger
			.getCommonLogger(SaveSpecimenRequirementAction.class);

	private static String isPersistent = null;
	
	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping object of ActionMapping
	 * @param form object of ActionForm
	 * @param request object of HttpServletRequest
	 * @param response object of HttpServletResponse
	 *
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		String target = Constants.SUCCESS;
//		request.getParameter("isPersistent")
		final CreateSpecimenTemplateForm createSpecimenTemplateForm = (CreateSpecimenTemplateForm) form;
		final HttpSession session = request.getSession();
		final String operation = request.getParameter(Constants.OPERATION);
		String mapKey = request.getParameter(Constants.EVENT_KEY);
		final String parentNodeId = request.getParameter(Constants.PARENT_NODE_ID);
		final String nodeId = request.getParameter(Constants.TREE_NODE_ID);
		final Vector<QueryTreeNodeData> treeData = new Vector<QueryTreeNodeData>();
		String objectName = null;
		String parentId = null;
		//final StringTokenizer st = new StringTokenizer(mapKey, "_");
		String eventSelected = null;
		if(nodeId != null && !nodeId.startsWith(Constants.NEW_SPECIMEN))
			eventSelected = mapKey;
		else	
			eventSelected = parentNodeId.substring(parentNodeId.lastIndexOf('_')+1,parentNodeId.length());

		isPersistent = request.getParameter("isPersistent");
		request.setAttribute("isPersistent", isPersistent);
				
		final Map collectionProtocolEventMap = (Map) session
		.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		final CollectionProtocolEventBean collectionProtocolEventBean = (CollectionProtocolEventBean) collectionProtocolEventMap.get(mapKey);
		
		if (operation.equals(Constants.ADD))
		{
			
			final Integer totalNoOfSpecimen = collectionProtocolEventBean
					.getSpecimenRequirementbeanMap().size();
			
			SpecimenRequirementBean specimenRequirementBean = this.createSpecimenBean(
					createSpecimenTemplateForm, collectionProtocolEventBean.getUniqueIdentifier(),
					totalNoOfSpecimen);

			if (specimenRequirementBean != null)
			{
				collectionProtocolEventBean.addSpecimenRequirementBean(specimenRequirementBean);

				//to create CP Tree node
				objectName = collectionProtocolEventBean.getCollectionPointLabel()
				+ Constants.CLASS;
				if(parentNodeId != null && parentNodeId.startsWith("cpName"))
				{
					parentId = collectionProtocolEventBean.getUniqueIdentifier();  
				}
				else{
					parentId = parentNodeId.substring(parentNodeId.lastIndexOf('_')+1);
				}
				AppUtility.createSpecimenNode(objectName, parentId, specimenRequirementBean,
						treeData, operation);
				request.setAttribute("nodeAdded", treeData);
			}
			
		}
		if (operation.equals(Constants.EDIT))
		{
			try
			{
				this.initCreateSpecimenTemplateForm(createSpecimenTemplateForm, request);
				request.getSession().setAttribute(Constants.TREE_NODE_ID, parentNodeId);
				mapKey = parentNodeId.substring(parentNodeId.indexOf('_')+1);
				request.getSession().setAttribute("key",mapKey);
				
				//delete old tree node
				request.setAttribute("deleteNode", nodeId);
				
				//add node
				
				final HttpSession sessionObject = request.getSession();
				final SpecimenRequirementBean currentSpecimenRequirementBean = (SpecimenRequirementBean) sessionObject
						.getAttribute(Constants.EDIT_SPECIMEN_REQUIREMENT_BEAN);
				if(parentNodeId.startsWith("New"))
				{
					objectName = Constants.NEW_SPECIMEN;
					parentId = parentNodeId.substring(parentNodeId.indexOf('_')+1);
				}else{
					objectName  = parentNodeId.substring(0,parentNodeId.indexOf('_')); 
					parentId = parentNodeId.substring(parentNodeId.lastIndexOf('_')+1,parentNodeId.length());
				}
				//Set requirement title to parent requirementTitle if derived/aliquot requirement title is empty
				if((currentSpecimenRequirementBean.getLineage().equals(Constants.DERIVED_SPECIMEN) || currentSpecimenRequirementBean.getLineage().equals(Constants.ALIQUOT))
						&& (currentSpecimenRequirementBean.getSpecimenRequirementLabel()==null || currentSpecimenRequirementBean.getSpecimenRequirementLabel().isEmpty()))
				{
					
					final SpecimenRequirementBean parentSpecimenRequirementBean = CollectionProtocolUtil.getParentSpecimen(
							mapKey, collectionProtocolEventMap);
					currentSpecimenRequirementBean.setSpecimenRequirementLabel(parentSpecimenRequirementBean.getSpecimenRequirementLabel());
				}
				
				AppUtility.createSpecimenNode(objectName, parentId, currentSpecimenRequirementBean,
						treeData, operation);
				request.setAttribute("nodeAdded", treeData);
				
			}
			catch (final Exception e)
			{
				LOGGER.error(e.getMessage(), e);
				final ActionErrors actionErrors = new ActionErrors();
				actionErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionError("errors.item", e
						.getMessage()));
				this.saveErrors(request, actionErrors);
//				return mapping.findForward(Constants.FAILURE);
				target = Constants.FAILURE;
			}
		}
		session.setAttribute("listKey", eventSelected);
		return mapping.findForward(target);
	}

	/**
	 * Creates the specimen bean.
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 * @param totalNoOfSpecimen : totalNoOfSpecimen
	 *
	 * @return SpecimenRequirementBean : SpecimenRequirementBean
	 */
	private SpecimenRequirementBean createSpecimenBean(
			CreateSpecimenTemplateForm createSpecimenTemplateForm, String uniqueIdentifier,
			Integer totalNoOfSpecimen)
	{
		final SpecimenRequirementBean specimenRequirementBean = this.createSpecimen(
				createSpecimenTemplateForm, uniqueIdentifier, totalNoOfSpecimen);
		Map aliquotSpecimenMap = null;
		Collection deriveSpecimenCollection = null;
		if (!Validator.isEmpty(createSpecimenTemplateForm.getNoOfAliquots() ))
				//createSpecimenTemplateForm.getNoOfAliquots() != null
				//&& !createSpecimenTemplateForm.getNoOfAliquots().equals(""))
		{
			aliquotSpecimenMap = this.getAliquots(createSpecimenTemplateForm,
					specimenRequirementBean.getUniqueIdentifier());
		}

		Map deriveSpecimenMap = createSpecimenTemplateForm.deriveSpecimenMap();

		if (createSpecimenTemplateForm.getNoOfDeriveSpecimen() == 0)
		{
			createSpecimenTemplateForm.setDeriveSpecimenValues(null);
			deriveSpecimenMap = null;
		}
		final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.bean");
		try
		{
			if (!isDeriveMapEmpty(deriveSpecimenMap))
			{
				deriveSpecimenCollection = parser.generateData(deriveSpecimenMap);
				deriveSpecimenMap = this.getderiveSpecimen(deriveSpecimenCollection,
						createSpecimenTemplateForm, specimenRequirementBean.getUniqueIdentifier());
			}
		}
		catch (final Exception e)
		{
			LOGGER.error(e.getMessage(), e);
		}

		specimenRequirementBean.setAliquotSpecimenCollection((LinkedHashMap) aliquotSpecimenMap);
		specimenRequirementBean.setDeriveSpecimenCollection((LinkedHashMap) deriveSpecimenMap);
		return specimenRequirementBean;
	}

	/**
	 * Checks if is derive map empty.
	 *
	 * @param deriveSpecimenMap the derive specimen map
	 *
	 * @return true, if checks if is derive map empty
	 */
	private boolean isDeriveMapEmpty(Map deriveSpecimenMap)
	{
		return deriveSpecimenMap == null || deriveSpecimenMap.isEmpty();
	}

	/**
	 * Creates the specimen.
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 * @param totalNoOfSpecimen : totalNoOfSpecimen
	 *
	 * @return SpecimenRequirementBean : SpecimenRequirementBean
	 */
	private SpecimenRequirementBean createSpecimen(
			CreateSpecimenTemplateForm createSpecimenTemplateForm, String uniqueIdentifier,
			Integer totalNoOfSpecimen)
	{
		final SpecimenRequirementBean specimenRequirementBean = new SpecimenRequirementBean();
		specimenRequirementBean.setParentName(Constants.ALIAS_SPECIMEN + "_" + uniqueIdentifier);
		specimenRequirementBean.setUniqueIdentifier(uniqueIdentifier
				+ Constants.UNIQUE_IDENTIFIER_FOR_NEW_SPECIMEN + totalNoOfSpecimen);
		specimenRequirementBean.setDisplayName(Constants.ALIAS_SPECIMEN + "_" + uniqueIdentifier
				+ Constants.UNIQUE_IDENTIFIER_FOR_NEW_SPECIMEN + totalNoOfSpecimen);
		specimenRequirementBean.setLineage(Constants.NEW_SPECIMEN);
		this.setSessionDataBean(createSpecimenTemplateForm, specimenRequirementBean);

		// Aliquot
		specimenRequirementBean.setNoOfAliquots(createSpecimenTemplateForm.getNoOfAliquots());
		specimenRequirementBean.setLabelFormat(createSpecimenTemplateForm.getLabelFormat());

		specimenRequirementBean.setLabelFormatForAliquot(createSpecimenTemplateForm.getLabelFormatForAliquot());

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
		specimenRequirementBean.setSpecimenRequirementLabel(createSpecimenTemplateForm.getSpecimenReqTitle());
    	 return specimenRequirementBean;
	}


	/**
	 * Sets the session data bean.
	 *
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

		specimenRequirementBean.setLabelFormat(createSpecimenTemplateForm.getLabelFormat());
    	specimenRequirementBean.setSpecimenRequirementLabel(createSpecimenTemplateForm.getSpecimenReqTitle());
//		specimenRequirementBean.setLabelFormatForAliquot(createSpecimenTemplateForm.getLabelFormatForAliquot()());
	}

	/**
	 * Gets the aliquots.
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 *
	 * @return Map : Map
	 */
	private Map getAliquots(CreateSpecimenTemplateForm createSpecimenTemplateForm,
			String uniqueIdentifier)
	{
		final Map aliquotMap = new LinkedHashMap();
		final String noOfAliquotes = createSpecimenTemplateForm.getNoOfAliquots();
		final Double aliquotCount = Double.parseDouble(noOfAliquotes);
		final Double aliquotQuantity = this.calculateQuantity(createSpecimenTemplateForm);
		for (int iCount = 1; iCount <= aliquotCount; iCount++)
		{
			final SpecimenRequirementBean specimenRequirementBean = this.createSpecimen(
					createSpecimenTemplateForm, uniqueIdentifier, iCount);
			this.createAliquot(createSpecimenTemplateForm, uniqueIdentifier, aliquotQuantity
					.toString(), iCount, specimenRequirementBean);
			aliquotMap.put(specimenRequirementBean.getUniqueIdentifier(), specimenRequirementBean);
		}
		return aliquotMap;
	}

	/**
	 * Creates the aliquot.
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 * @param quantityPerAliquot : quantityPerAliquot
	 * @param iCount : iCount
	 * @param specimenRequirementBean : specimenRequirementBean
	 *
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


		specimenRequirementBean.setLabelFormat(createSpecimenTemplateForm.getLabelFormatForAliquot());
		specimenRequirementBean.setDeriveSpecimen(null);
		specimenRequirementBean.setNoOfDeriveSpecimen(0);
		return quantityPerAliquot;
	}

	/**
	 * Get derive specimen.
	 *
	 * @param deriveSpecimenCollection : deriveSpecimenCollection
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 *
	 * @return Map : map
	 */
	private Map getderiveSpecimen(Collection deriveSpecimenCollection,
			CreateSpecimenTemplateForm createSpecimenTemplateForm, String uniqueIdentifier)
	{
		final Map deriveSpecimenMap = new LinkedHashMap();
		final Iterator deriveSpecimenCollectionItr = deriveSpecimenCollection.iterator();
		Integer deriveSpecimenCount = 1;
		while (deriveSpecimenCollectionItr.hasNext())
		{
			final DeriveSpecimenBean deriveSpecimenBean = (DeriveSpecimenBean) deriveSpecimenCollectionItr
					.next();
			 
			final SpecimenRequirementBean specimenRequirementBean = new SpecimenRequirementBean();
			specimenRequirementBean.setParentName(Constants.ALIAS_SPECIMEN + "_" + uniqueIdentifier);
			this.setSessionDataBean(createSpecimenTemplateForm, specimenRequirementBean);


			specimenRequirementBean.setLabelFormatForAliquot(createSpecimenTemplateForm.getLabelFormatForAliquot());

 		    specimenRequirementBean.setSpecimenRequirementLabel(deriveSpecimenBean.getRequirementLabel());
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

			specimenRequirementBean.setLabelFormat(deriveSpecimenBean.getLabelFormat());
			if(isPersistent.equals("true"))
			{
				if(deriveSpecimenBean.getId() == null)
				{
//					specimenRequirementBean.setId(-1l);
				}
				else
				{
					specimenRequirementBean.setId(deriveSpecimenBean.getId());
				}
			}
			if(deriveSpecimenBean.getCollectionEventId()!=null && !deriveSpecimenBean.getCollectionEventId().isEmpty())
			{
				specimenRequirementBean.setCollectionEventId(Long.parseLong(deriveSpecimenBean.getCollectionEventId()));
			}
			if(deriveSpecimenBean.getReceivedEventId()!=null && !deriveSpecimenBean.getReceivedEventId().isEmpty())
			{
				specimenRequirementBean.setReceivedEventId(Long.parseLong(deriveSpecimenBean.getReceivedEventId()));
			}
			
			deriveSpecimenMap.put(specimenRequirementBean.getUniqueIdentifier(),
					specimenRequirementBean);
			deriveSpecimenCount = deriveSpecimenCount + 1;
		}

		return deriveSpecimenMap;
	}

	/**
	 * Get derive specimen.
	 *
	 * @param deriveSpecimenCollection : deriveSpecimenCollection
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param uniqueIdentifier : uniqueIdentifier
	 *
	 * @return Map : map
	 */
	private Map updatederiveSpecimen(Collection deriveSpecimenCollection,CreateSpecimenTemplateForm createSpecimenTemplateForm,
			SpecimenRequirementBean parentSpecimenRequirementBean, String uniqueIdentifier)
	{
		final Map deriveSpecimenMap = new LinkedHashMap();
		final Iterator deriveSpecimenCollectionItr = deriveSpecimenCollection.iterator();
		Integer deriveSpecimenCount = 1;
		while (deriveSpecimenCollectionItr.hasNext())
		{
			final DeriveSpecimenBean deriveSpecimenBean = (DeriveSpecimenBean) deriveSpecimenCollectionItr
					.next();
			 
			final SpecimenRequirementBean specimenRequirementBean = new SpecimenRequirementBean();
			specimenRequirementBean.setParentName(Constants.ALIAS_SPECIMEN + "_" + uniqueIdentifier);
			specimenRequirementBean.setClassName(deriveSpecimenBean.getSpecimenClass());
			specimenRequirementBean.setType(deriveSpecimenBean.getSpecimenType());
			specimenRequirementBean.setTissueSide(parentSpecimenRequirementBean.getTissueSide());
			specimenRequirementBean.setTissueSite(parentSpecimenRequirementBean.getTissueSite());
			specimenRequirementBean.setPathologicalStatus(parentSpecimenRequirementBean.getPathologicalStatus());
			specimenRequirementBean.setConcentration(deriveSpecimenBean.getConcentration());
			specimenRequirementBean.setQuantity(deriveSpecimenBean.getQuantity());
			specimenRequirementBean.setStorageContainerForSpecimen(deriveSpecimenBean.getStorageLocation());

			// Collected and received events
			specimenRequirementBean.setCollectionEventUserId(parentSpecimenRequirementBean
					.getCollectionEventUserId());
			specimenRequirementBean.setReceivedEventUserId(parentSpecimenRequirementBean
					.getReceivedEventUserId());
			specimenRequirementBean.setCollectionEventContainer(parentSpecimenRequirementBean
					.getCollectionEventContainer());
			specimenRequirementBean.setReceivedEventReceivedQuality(parentSpecimenRequirementBean
					.getReceivedEventReceivedQuality());
			specimenRequirementBean.setCollectionEventCollectionProcedure(parentSpecimenRequirementBean
					.getCollectionEventCollectionProcedure());

			specimenRequirementBean.setLabelFormat(deriveSpecimenBean.getLabelFormat());
	    	specimenRequirementBean.setSpecimenRequirementLabel(deriveSpecimenBean.getRequirementLabel());
			specimenRequirementBean.setLabelFormatForAliquot(createSpecimenTemplateForm.getLabelFormatForAliquot());

			specimenRequirementBean.setUniqueIdentifier(uniqueIdentifier
					+ Constants.UNIQUE_IDENTIFIER_FOR_DERIVE + deriveSpecimenCount);
			specimenRequirementBean.setLineage(Constants.DERIVED_SPECIMEN);
			specimenRequirementBean.setDisplayName(Constants.ALIAS_SPECIMEN + "_"
					+ uniqueIdentifier + Constants.UNIQUE_IDENTIFIER_FOR_DERIVE
					+ deriveSpecimenCount);
			// Aliquot
			specimenRequirementBean.setNoOfAliquots(null);
			specimenRequirementBean.setQuantityPerAliquot(null);
			specimenRequirementBean.setStorageContainerForAliquotSpecimem("");
			// Derive
			specimenRequirementBean.setDeriveSpecimen(null);
			specimenRequirementBean.setNoOfDeriveSpecimen(0);
			specimenRequirementBean.setId(deriveSpecimenBean.getId());
			if(deriveSpecimenBean.getCollectionEventId()!=null)
			{
				specimenRequirementBean.setCollectionEventId(Long.parseLong(deriveSpecimenBean.getCollectionEventId()));
			}
			if(deriveSpecimenBean.getReceivedEventId()!=null)
			{
				specimenRequirementBean.setReceivedEventId(Long.parseLong(deriveSpecimenBean.getReceivedEventId()));
			}
			
			deriveSpecimenMap.put(specimenRequirementBean.getUniqueIdentifier(),
					specimenRequirementBean);
			deriveSpecimenCount = deriveSpecimenCount + 1;
		}

		return deriveSpecimenMap;
	}

	/**
	 * Inits the create specimen template form.
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 * @param request : request
	 *
	 * @throws Exception : Exception
	 */
	private void initCreateSpecimenTemplateForm(
			CreateSpecimenTemplateForm createSpecimenTemplateForm, HttpServletRequest request)
			throws Exception
	{
		final HttpSession session = request.getSession();
		final SpecimenRequirementBean specimenRequirementBean = (SpecimenRequirementBean) session
				.getAttribute(Constants.EDIT_SPECIMEN_REQUIREMENT_BEAN);
		this.setSessionDataBean(createSpecimenTemplateForm, specimenRequirementBean);

		int noOfAliquots = 0;
		if (createSpecimenTemplateForm.getNoOfAliquots() != null
				&& !createSpecimenTemplateForm.getNoOfAliquots().equals(""))
		{
			noOfAliquots = Integer.valueOf(createSpecimenTemplateForm.getNoOfAliquots());
		}
		int noOfBeanAliquots = 0;
		if (specimenRequirementBean.getNoOfAliquots() != null
				&& !specimenRequirementBean.getNoOfAliquots().equals(""))
		{
			noOfBeanAliquots =Integer.valueOf(specimenRequirementBean.getNoOfAliquots());
		}
		final int totalNewAliquots = noOfAliquots - noOfBeanAliquots;
		if (totalNewAliquots < 0)
		{
			LOGGER.debug("Cannot delete aliquot(s)");
			throw new Exception("Cannot delete aliquot(s)");
		}
		LinkedHashMap newAliquotSpecimenMap = specimenRequirementBean
				.getAliquotSpecimenCollection();

		if (newAliquotSpecimenMap == null)
		{
			newAliquotSpecimenMap = new LinkedHashMap();
		}
		else
		{
			updateChildAliquots(specimenRequirementBean,
					newAliquotSpecimenMap);
		}
		for (int i = 0; i < totalNewAliquots; i++)
		{
			final int iCount = ++noOfBeanAliquots;
			final SpecimenRequirementBean aliquotBean = this.createSpecimen(
					createSpecimenTemplateForm, specimenRequirementBean.getUniqueIdentifier(),
					iCount);
			final Double aliquotQuantity = this.calculateQuantity(createSpecimenTemplateForm);
			this.createAliquot(createSpecimenTemplateForm, specimenRequirementBean
					.getUniqueIdentifier(), aliquotQuantity.toString(), iCount, aliquotBean);
			newAliquotSpecimenMap.put(aliquotBean.getUniqueIdentifier(), aliquotBean);
		}
		Set<String> set = newAliquotSpecimenMap.keySet();

		Iterator<String> itr = set.iterator();
		while(itr.hasNext())
		{
			String key = itr.next();
			SpecimenRequirementBean srb = (SpecimenRequirementBean)newAliquotSpecimenMap.get(key);
			srb.setLabelFormat(createSpecimenTemplateForm.getLabelFormatForAliquot());
		}
		specimenRequirementBean.setNoOfAliquots(createSpecimenTemplateForm.getNoOfAliquots());

		specimenRequirementBean.setQuantityPerAliquot(createSpecimenTemplateForm
				.getQuantityPerAliquot());
		specimenRequirementBean.setStorageContainerForAliquotSpecimem(createSpecimenTemplateForm
				.getStorageLocationForAliquotSpecimen());
		specimenRequirementBean.setAliquotSpecimenCollection(newAliquotSpecimenMap);

		// Derive
		final int noOfDeriveSpecimen = Integer.valueOf(createSpecimenTemplateForm
				.getNoOfDeriveSpecimen());
		final int noOfBeanDerive = Integer.valueOf(specimenRequirementBean.getNoOfDeriveSpecimen());
		final int totalNewDeriveSpecimen = noOfDeriveSpecimen - noOfBeanDerive;
		Collection deriveSpecimenCollection = null;
		Map deriveSpecimenMap = createSpecimenTemplateForm.deriveSpecimenMap();
		final MapDataParser parser = new MapDataParser("edu.wustl.catissuecore.bean");
		if (deriveSpecimenMap != null && !deriveSpecimenMap.isEmpty())
		{
			deriveSpecimenCollection = parser.generateData(deriveSpecimenMap);
			// for ordering
			final IdComparator deriveSpBeanComp = new IdComparator();
			final List deriveSpecimenList = new LinkedList(deriveSpecimenCollection);
			Collections.sort(deriveSpecimenList, deriveSpBeanComp);
			deriveSpecimenMap = this.getderiveSpecimen(deriveSpecimenList,createSpecimenTemplateForm,
									specimenRequirementBean.getUniqueIdentifier());
		}

		LinkedHashMap oldDeriveSpecimenMap = specimenRequirementBean.getDeriveSpecimenCollection();
		final Object[] keyArr = deriveSpecimenMap.keySet().toArray();

			final LinkedHashMap deriveMap = new LinkedHashMap();
			for (final Object element : keyArr)
			{
				
				final SpecimenRequirementBean newBean = (SpecimenRequirementBean) deriveSpecimenMap
						.get(element);
				deriveMap.put(element, deriveSpecimenMap.get(element));

				if(oldDeriveSpecimenMap!=null && oldDeriveSpecimenMap.get(element)!=null)
				{	
					final SpecimenRequirementBean oldBean = (SpecimenRequirementBean) oldDeriveSpecimenMap
							.get(element);
					newBean.setAliquotSpecimenCollection(oldBean.getAliquotSpecimenCollection());
					newBean.setDeriveSpecimenCollection(oldBean.getDeriveSpecimenCollection());
					LinkedHashMap childAliquotSpecimenMap = newBean.getAliquotSpecimenCollection();
					LinkedHashMap childDeriveSpecimenMap = newBean.getDeriveSpecimenCollection();
					updateChildAliquots(newBean, childAliquotSpecimenMap);
					updateChildDerives(newBean,childDeriveSpecimenMap);
				}	
			}
			specimenRequirementBean.setDeriveSpecimenCollection(deriveMap);
			if (oldDeriveSpecimenMap != null)
			{
				oldDeriveSpecimenMap.clear();
			}
		specimenRequirementBean.setNoOfDeriveSpecimen(createSpecimenTemplateForm
				.getNoOfDeriveSpecimen());
	}

	private void updateChildAliquots(
			final SpecimenRequirementBean parent,
			LinkedHashMap newAliquotSpecimenMap)
	{
		Collection<SpecimenRequirementBean> aiquotes=newAliquotSpecimenMap.values();
		for (SpecimenRequirementBean aliquote : aiquotes)
		{
			aliquote.setClassName(parent.getClassName());
			aliquote.setType(parent.getType());
			aliquote.setTissueSide(parent.getTissueSide());
			aliquote.setTissueSite(parent.getTissueSite());
			aliquote.setPathologicalStatus(parent.getPathologicalStatus());
			aliquote.setCollectionEventUserId(parent.getCollectionEventUserId());
			aliquote.setCollectionEventCollectionProcedure(parent.getCollectionEventCollectionProcedure());
			aliquote.setReceivedEventReceivedQuality(parent.getReceivedEventReceivedQuality());
			aliquote.setCollectionEventContainer(parent.getCollectionEventContainer());
			aliquote.setReceivedEventUserId(parent.getReceivedEventUserId());
			aliquote.setSpecimenRequirementLabel(parent.getSpecimenRequirementLabel());
			updateChildAliquots(aliquote, aliquote.getAliquotSpecimenCollection());
			updateChildDerives(aliquote,aliquote.getDeriveSpecimenCollection());
		}
	}

	private void updateChildDerives(
			final SpecimenRequirementBean parent,
			LinkedHashMap deriveMap)
	{
		Collection<SpecimenRequirementBean> derives=deriveMap.values();
		for (SpecimenRequirementBean derive : derives)
		{
			derive.setTissueSide(parent.getTissueSide());
			derive.setTissueSite(parent.getTissueSite());
			derive.setPathologicalStatus(parent.getPathologicalStatus());
			derive.setCollectionEventUserId(parent.getCollectionEventUserId());
			derive.setCollectionEventCollectionProcedure(parent.getCollectionEventCollectionProcedure());
			derive.setReceivedEventReceivedQuality(parent.getReceivedEventReceivedQuality());
			derive.setCollectionEventContainer(parent.getCollectionEventContainer());
			derive.setReceivedEventUserId(parent.getReceivedEventUserId());
			updateChildAliquots(derive, derive.getAliquotSpecimenCollection());
			updateChildAliquots(derive, derive.getAliquotSpecimenCollection());
		}
	}

	/**
	 * Calculate quantity.
	 *
	 * @param createSpecimenTemplateForm : createSpecimenTemplateForm
	 *
	 * @return Double : Double
	 */
	private Double calculateQuantity(CreateSpecimenTemplateForm createSpecimenTemplateForm)
	{
		final String noOfAliquotes = createSpecimenTemplateForm.getNoOfAliquots();
		final String quantityPerAliquot = createSpecimenTemplateForm.getQuantityPerAliquot();
		final Double aliquotCount = Double.parseDouble(noOfAliquotes);
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