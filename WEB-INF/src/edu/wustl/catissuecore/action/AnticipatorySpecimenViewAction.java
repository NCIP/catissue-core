
package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.GenericSpecimenVO;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.IdComparator;
import edu.wustl.catissuecore.util.SpecimenAutoStorageContainer;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author abhijit_naik
 *
 */
public class AnticipatorySpecimenViewAction extends BaseAction
{
	/**
	 * 
	 */
	private static final String SPECIMEN_KEY_PREFIX = "S_";
	Long cpId = null;
	private SpecimenAutoStorageContainer autoStorageContainer;
	
	Set asignedPositonSet = null;
	/* (non-Javadoc)
	 * @see edu.wustl.common.action.BaseAction#executeAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		asignedPositonSet = new HashSet();
		String target = Constants.SUCCESS;
		boolean isFromSpecimenEditPage = false;
		SpecimenCollectionGroupForm specimenCollectionGroupForm = (SpecimenCollectionGroupForm) form;
		HttpSession session = request.getSession();
		Long id = specimenCollectionGroupForm.getId();

		Long specimenId = null;
		

		HashMap forwardToHashMap = (HashMap) request.getAttribute("forwardToHashMap");
		if (forwardToHashMap != null)
		{
			id = (Long) forwardToHashMap.get("specimenCollectionGroupId");

			if (id != null)
				specimenCollectionGroupForm.setId(id);

			specimenId = (Long) forwardToHashMap.get("specimenId");
			if (specimenId != null)
			{
				session.setAttribute(Constants.SPECIMENFORM, specimenId);
				isFromSpecimenEditPage = true;
			}

		}
		SessionDataBean bean = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		SpecimenCollectionGroupBizLogic scgBizLogic = new SpecimenCollectionGroupBizLogic();
		autoStorageContainer = new SpecimenAutoStorageContainer();

		try
		{
			target = Constants.SUCCESS;
			session.setAttribute(Constants.SCGFORM, specimenCollectionGroupForm.getId());
			SpecimenCollectionGroup specimencollectionGroup = scgBizLogic.getSCGFromId(id, bean, true);
			if (specimencollectionGroup.getActivityStatus().equalsIgnoreCase(Constants.ACTIVITY_STATUS_DISABLED))
			{
				target = Constants.ACTIVITY_STATUS_DISABLED;
			}
			cpId = specimencollectionGroup.getCollectionProtocolRegistration().getCollectionProtocol().getId();

			if (!isFromSpecimenEditPage)
			{
				addSCGSpecimensToSession(session, specimencollectionGroup);
			}
			else
			{
				Collection scgSpecimenList = specimencollectionGroup.getSpecimenCollection();
				getSpcimensToShowOnSummary(specimenId, scgSpecimenList, session);
			}
			request.setAttribute("RequestType", ViewSpecimenSummaryForm.REQUEST_TYPE_ANTICIPAT_SPECIMENS);
			autoStorageContainer.setCollectionProtocol(cpId);
			autoStorageContainer.setSpecimenStoragePositions(bean);
			if(request.getParameter("target") != null)
				target = request.getParameter("target");
			
			autoStorageContainer.fillAllocatedPositionSet(asignedPositonSet);
			session.setAttribute("asignedPositonSet",asignedPositonSet);
			return mapping.findForward(target);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			autoStorageContainer = null;
		}
		return null;
	}

	/**
	 * @param session
	 * @param specimencollectionGroup
	 * @throws DAOException
	 */
	private void addSCGSpecimensToSession(HttpSession session, SpecimenCollectionGroup specimencollectionGroup) throws DAOException
	{
		LinkedHashMap<String, CollectionProtocolEventBean> cpEventMap = new LinkedHashMap<String, CollectionProtocolEventBean>();

		CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();

		eventBean.setUniqueIdentifier(String.valueOf(specimencollectionGroup.getId().longValue()));
		// sorting of specimen collection accoding to id 
		LinkedList<Specimen> specimenList = new LinkedList<Specimen>();
		Iterator<Specimen> itr = specimencollectionGroup.getSpecimenCollection().iterator();
		while (itr.hasNext())
		{
			Specimen specimen = (Specimen) itr.next();
			if(!Constants.ACTIVITY_STATUS_DISABLED.equals(specimen.getActivityStatus()))
			{
				specimenList.add(specimen);
			}
		}
		Comparator spIdComp = new IdComparator();
		Collections.sort(specimenList, spIdComp);
		eventBean.setSpecimenRequirementbeanMap(getSpecimensMap(specimenList, cpId));
		//eventBean.setSpecimenRequirementbeanMap(getSpecimensMap(
		//specimencollectionGroup.getSpecimenCollection(),cpId ));
		String globalSpecimenId = "E" + eventBean.getUniqueIdentifier() + "_";
		cpEventMap.put(globalSpecimenId, eventBean);
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP, cpEventMap);
	}

	protected LinkedHashMap<String, GenericSpecimen> getSpecimensMap(Collection<Specimen> specimenCollection, long collectionProtocolId)
			throws DAOException
	{
		LinkedHashMap<String, GenericSpecimen> specimenMap = new LinkedHashMap<String, GenericSpecimen>();

		Iterator<Specimen> specimenIterator = specimenCollection.iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = specimenIterator.next();
			if (specimen.getParentSpecimen() == null)
			{
				GenericSpecimenVO specBean = getSpecimenBean(specimen, null);
				addToSpToAutoCont(specBean);
				setChildren(specimen, specBean);
				specBean.setUniqueIdentifier(SPECIMEN_KEY_PREFIX + specimen.getId());
				specBean.setCollectionProtocolId(collectionProtocolId);
				specimenMap = getOrderedMap(specimenMap, specimen.getId(), specBean, SPECIMEN_KEY_PREFIX);
			}

		}
		return specimenMap;
	}
	protected LinkedHashMap<String, GenericSpecimen> getSpecimensMap(Collection specimenCollection, long specimenId, long collectionProtocolId) throws DAOException
	{
		LinkedHashMap<String, GenericSpecimen> specimenMap = new LinkedHashMap<String, GenericSpecimen>();

		Iterator specimenIterator = specimenCollection.iterator();
		while (specimenIterator.hasNext())
		{
			Specimen specimen = (Specimen) specimenIterator.next();
			if (specimen.getId() != null && specimen.getId() == specimenId)
			{
				GenericSpecimenVO specBean = getSpecimenBean(specimen, null);
				addToSpToAutoCont(specBean);
				setChildren(specimen, specBean);
				specBean.setUniqueIdentifier("S_" + specimen.getId());
				specBean.setCollectionProtocolId(collectionProtocolId);
				specimenMap.put("S_" + specimen.getId(), specBean);
			}

		}
		return specimenMap;
	}
	

	private LinkedHashMap<String, GenericSpecimen> getOrderedMap(LinkedHashMap<String, GenericSpecimen> specimenMap, Long id,
			GenericSpecimenVO specBean, String prefix)
	{
		LinkedHashMap<String, GenericSpecimen> orderedMap = new LinkedHashMap<String, GenericSpecimen>();
		Object[] keyArray = specimenMap.keySet().toArray();
		for (int ctr = 0; ctr < keyArray.length; ctr++)
		{
			String keyVal = (String) keyArray[ctr];
			String keyId = keyVal.substring(prefix.length());
			if (Long.parseLong(keyId) > id)
			{
				orderedMap.put(keyVal, specimenMap.get(keyVal));
				specimenMap.remove(keyVal);
			}
		}
		specimenMap.put(prefix + id, specBean);
		if (!orderedMap.isEmpty())
		{
			specimenMap.putAll(orderedMap);
		}
		return specimenMap;
	}

	protected void setChildren(Specimen specimen, GenericSpecimen parentSpecimenVO) throws DAOException
	{
		Collection<AbstractSpecimen> specimenChildren = specimen.getChildSpecimenCollection();
		List<AbstractSpecimen> specimenChildrenCollection =
			new LinkedList<AbstractSpecimen>(specimenChildren);
		CollectionProtocolUtil.getSortedCPEventList(specimenChildrenCollection);

		if (specimenChildrenCollection == null || specimenChildrenCollection.isEmpty())
		{
			return;
		}

		Iterator<AbstractSpecimen> iterator = specimenChildrenCollection.iterator();

		LinkedHashMap<String, GenericSpecimen> aliquotMap = new LinkedHashMap<String, GenericSpecimen>();
		LinkedHashMap<String, GenericSpecimen> derivedMap = new LinkedHashMap<String, GenericSpecimen>();

		while (iterator.hasNext())
		{
			Specimen childSpecimen = (Specimen) iterator.next();
			String lineage = childSpecimen.getLineage();

			GenericSpecimenVO specimenBean = getSpecimenBean(childSpecimen, specimen.getLabel());
			//addToSpToAutoCont(specimenBean);
			setChildren(childSpecimen, specimenBean);
			String prefix = lineage + specimen.getId() + "_";
			specimenBean.setUniqueIdentifier(prefix + childSpecimen.getId());

			if (Constants.ALIQUOT.equals(childSpecimen.getLineage()))
			{
				aliquotMap = getOrderedMap(aliquotMap, childSpecimen.getId(), specimenBean, prefix);
			}
			else
			{
				derivedMap = getOrderedMap(derivedMap, childSpecimen.getId(), specimenBean, prefix);
			}
			specimenBean.setCollectionProtocolId(cpId);

		}
		if(aliquotMap != null && !aliquotMap.isEmpty())
		{
		
			Iterator aliquotItr = aliquotMap.keySet().iterator();
			while(aliquotItr.hasNext())
			{
				String key = (String) aliquotItr.next();
				GenericSpecimenVO sp = (GenericSpecimenVO) aliquotMap.get(key);
				addToSpToAutoCont((GenericSpecimenVO)sp);
				
			}
		}
		
		if(derivedMap != null && !derivedMap.isEmpty())
		{
			Iterator derivedItr = derivedMap.keySet().iterator();
			while(derivedItr.hasNext())
			{
				String key = (String) derivedItr.next();
				GenericSpecimenVO sp = (GenericSpecimenVO) derivedMap.get(key);
				addToSpToAutoCont((GenericSpecimenVO)sp);
			}
		}
		
		parentSpecimenVO.setAliquotSpecimenCollection(aliquotMap);
		parentSpecimenVO.setDeriveSpecimenCollection(derivedMap);
	}

	protected GenericSpecimenVO getSpecimenBean(Specimen specimen, String parentName) throws DAOException
	{
		GenericSpecimenVO specimenDataBean = new GenericSpecimenVO();
		specimenDataBean.setBarCode(specimen.getBarcode());
		specimenDataBean.setClassName(specimen.getClassName());
		specimenDataBean.setDisplayName(specimen.getLabel());
		specimenDataBean.setPathologicalStatus(specimen.getPathologicalStatus());
		specimenDataBean.setId(specimen.getId().longValue());
		specimenDataBean.setParentName(parentName);
		if (specimen.getInitialQuantity() != null)
		{
			specimenDataBean.setQuantity(specimen.getInitialQuantity().toString());
		}

		specimenDataBean.setCheckedSpecimen(true);
		if (Constants.SPECIMEN_COLLECTED.equals(specimen.getCollectionStatus()))
		{
			specimenDataBean.setReadOnly(true);
		}
		specimenDataBean.setType(specimen.getSpecimenType());
		SpecimenCharacteristics characteristics = specimen.getSpecimenCharacteristics();
		if (characteristics != null)
		{
			specimenDataBean.setTissueSide(characteristics.getTissueSide());
			specimenDataBean.setTissueSite(characteristics.getTissueSite());
		}
		//specimenDataBean.setExternalIdentifierCollection(specimen.getExternalIdentifierCollection());
		//specimenDataBean.setBiohazardCollection(specimen.getBiohazardCollection());
		//specimenDataBean.setSpecimenEventCollection(specimen.getSpecimenEventCollection());

		//		specimenDataBean.setSpecimenCollectionGroup(specimen.getSpecimenCollectionGroup());

		//		specimenDataBean.setStorageContainer(null);
		String concentration = "";
		if ("Molecular".equals(specimen.getClassName()))
		{
			concentration = String.valueOf(((MolecularSpecimen) specimen).getConcentrationInMicrogramPerMicroliter());
		}
		specimenDataBean.setConcentration(concentration);

		String storageType = null;

		if (specimen != null && specimen.getSpecimenPosition() != null)
		{
			StorageContainer container = specimen.getSpecimenPosition().getStorageContainer();
			Logger.out.info("-----------Container while getting from domain--:" + container);
			specimenDataBean.setContainerId(String.valueOf(container.getId()));
			specimenDataBean.setSelectedContainerName(container.getName());
			specimenDataBean.setPositionDimensionOne(String.valueOf(specimen.getSpecimenPosition().getPositionDimensionOne()));
			specimenDataBean.setPositionDimensionTwo(String.valueOf(specimen.getSpecimenPosition().getPositionDimensionTwo()));
			specimenDataBean.setStorageContainerForSpecimen("Auto");
		}
		//Mandar : 18Aug08 to check for virtual specimens which are collected after updating the initial storage types. START
		else if (specimen != null && specimen.getSpecimenPosition() == null && "Collected".equals(specimen.getCollectionStatus()))
		{
			specimenDataBean.setStorageContainerForSpecimen("Virtual");
		} // //Mandar : 18Aug08 to check for virtual specimens which are collected after updating the initial storage types. END
		else
		{
			//TODO:After model change 
			storageType = getStorageType(specimen);
			specimenDataBean.setStorageContainerForSpecimen(storageType);

		}
		/*if ("Auto".equals(storageType))
		{
			autoStorageContainer.addSpecimen(specimenDataBean, specimenDataBean.getClassName());
		}*/
		//setChildren(specimen, specimenDataBean);
		//specimenDataBean.setAliquotSpecimenCollection(getChildren(specimen, Constants.ALIQUOT));
		//specimenDataBean.setDeriveSpecimenCollection(getChildren(specimen, Constants.ALIQUOT));
		return specimenDataBean;
	}

	private void addToSpToAutoCont(GenericSpecimenVO specimenDataBean)
	{
		if ("Auto".equals(specimenDataBean.getStorageContainerForSpecimen()))
		{
			autoStorageContainer.addSpecimen(specimenDataBean, specimenDataBean.getClassName());
		}
		
	}
	private String getStorageType(Specimen specimen)
	{
		String storageType;
		SpecimenRequirement reqSpecimen = specimen.getSpecimenRequirement();
		if (reqSpecimen == null)
		{
			storageType = "Virtual";
		}
		else
		{
			storageType = reqSpecimen.getStorageType();
		}
		return storageType;
	}

	/**
	 * @param session
	 * @param specimencollectionGroup
	 * @throws DAOException
	 */
	private void addSpecimensToSession(HttpSession session, Long specimenId, List specimenList) throws DAOException
	{
		LinkedHashMap<String, CollectionProtocolEventBean> cpEventMap = new LinkedHashMap<String, CollectionProtocolEventBean>();

		CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();

		eventBean.setUniqueIdentifier(String.valueOf(specimenId.longValue()));

		// sorting of specimen collection accoding to id 

		Comparator spIdComp = new IdComparator();
		Collections.sort(specimenList, spIdComp);
		eventBean.setSpecimenRequirementbeanMap(getSpecimensMap(specimenList,specimenId,cpId));

		String globalSpecimenId = "E" + eventBean.getUniqueIdentifier() + "_";
		cpEventMap.put(globalSpecimenId, eventBean);
		session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
		session.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP, cpEventMap);
	}


	private void getSpcimensToShowOnSummary(long specimenId, Collection scgSpecimenList, HttpSession session) throws DAOException
	{
		List<Specimen> specimenList = new ArrayList<Specimen>();
		//Collection scgSpecimenList = specimencollectionGroup.getSpecimenCollection();
		//getSpcimensToShowOnSummary(specimenId,scgSpecimenList);
		if (scgSpecimenList != null)
		{
			Iterator itr = scgSpecimenList.iterator();
			while (itr.hasNext())
			{
				Specimen specimen = (Specimen) itr.next();
				if(!Constants.ACTIVITY_STATUS_DISABLED.equals(specimen.getActivityStatus()))
				{
					if (specimen.getId().equals(specimenId)
							|| (specimen.getParentSpecimen() != null && specimen.getParentSpecimen().getId().equals(specimenId)))
					{
						specimenList.add(specimen);
					}
				}
			}
		}
		addSpecimensToSession(session, specimenId, specimenList);
	}
}
