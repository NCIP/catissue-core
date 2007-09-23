package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.GenericSpecimenVO;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAO;
import edu.wustl.common.dao.DAOFactory;

public class AnticipatorySpecimenViewAction extends Action {

	private String globalSpecimenId = null;  
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		SpecimenCollectionGroupForm specimenCollectionGroupForm=
			(SpecimenCollectionGroupForm)form;
		HttpSession session = request.getSession();
		Long id = specimenCollectionGroupForm.getId();
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(Constants.DEFAULT_BIZ_LOGIC);
		DAO dao = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		SessionDataBean bean = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		((AbstractDAO)dao).openSession(bean);
		try{
			session.setAttribute("SCGFORM", specimenCollectionGroupForm.getId());
			List cpList = dao.retrieve(SpecimenCollectionGroup.class.getName(), "id", id);
			if(cpList != null && !cpList.isEmpty())
			{
				SpecimenCollectionGroup specimencollectionGroup = (SpecimenCollectionGroup) cpList.get(0);			
				LinkedHashMap<String, CollectionProtocolEventBean> cpEventMap = new LinkedHashMap<String, CollectionProtocolEventBean> ();
				
				CollectionProtocolEventBean eventBean = new CollectionProtocolEventBean();
	
				eventBean.setUniqueIdentifier(String.valueOf(specimencollectionGroup.getId().longValue()));
	//			eventBean.setUniqueIdentifier("E"+eventBean.getUniqueIdentifier() + "_");
				eventBean.setSpecimenRequirementbeanMap(getSpecimensMap(
						specimencollectionGroup.getSpecimenCollection() ));
				globalSpecimenId = "E"+eventBean.getUniqueIdentifier() + "_";
				cpEventMap.put(globalSpecimenId, eventBean);			
				session.removeAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);
				session
				.setAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP, cpEventMap);			
			
				request.setAttribute("RequestType",ViewSpecimenSummaryForm.REQUEST_TYPE_ANTICIPAT_SPECIMENS);
				return mapping.findForward(Constants.SUCCESS);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			((AbstractDAO)dao).closeSession();
		}
		return null;
	}

	private LinkedHashMap<String, GenericSpecimen> getSpecimensMap(Collection specimenCollection)
	{
		LinkedHashMap<String, GenericSpecimen> specimenMap = new LinkedHashMap<String, GenericSpecimen>();
		
		Iterator specimenIterator = specimenCollection.iterator();
		while(specimenIterator.hasNext())
		{
			Specimen specimen = (Specimen)specimenIterator.next();
			if (specimen.getParentSpecimen() == null)
			{
				GenericSpecimenVO specBean =getSpecimenBean(specimen, null);
				specBean.setUniqueIdentifier("S_"+specimen.getId());
				specimenMap.put("S_"+specimen.getId(), specBean);				
			}
			
		}
		return specimenMap;
	}
	
	private LinkedHashMap<String, GenericSpecimen> getChildAliquots(Specimen specimen)
	{
		Collection specimenChildren = specimen.getChildrenSpecimen();
		Iterator iterator = specimenChildren.iterator();
		LinkedHashMap<String, GenericSpecimen>  aliquotMap = new
			LinkedHashMap<String, GenericSpecimen> ();
		while(iterator.hasNext())
		{
			Specimen childSpecimen = (Specimen) iterator.next();
			if(Constants.ALIQUOT.equals(childSpecimen.getLineage()))
			{
				GenericSpecimenVO specimenBean = getSpecimenBean(childSpecimen, specimen.getLabel());
				specimenBean.setUniqueIdentifier("al_" + specimen.getId() +"_"+ childSpecimen.getId());
				aliquotMap.put("al_" + specimen.getId() +"_"+ childSpecimen.getId(), specimenBean);
			}
		}
		return aliquotMap;
	}

	private LinkedHashMap<String, GenericSpecimen> getChildDerived(Specimen specimen)
	{
		Collection specimenChildren = specimen.getChildrenSpecimen();
		Iterator iterator = specimenChildren.iterator();
		LinkedHashMap<String, GenericSpecimen>  derivedMap = new
			LinkedHashMap<String, GenericSpecimen> ();
		while(iterator.hasNext())
		{
			Specimen childSpecimen = (Specimen) iterator.next();
			if(Constants.DERIVED_SPECIMEN.equals(childSpecimen.getLineage()))
			{
				GenericSpecimenVO specimenBean = getSpecimenBean(childSpecimen, specimen.getLabel());
				specimenBean.setUniqueIdentifier("dr_" + specimen.getId() +"_"+ childSpecimen.getId());
				derivedMap.put("dr_" + specimen.getId() +"_"+ childSpecimen.getId(), specimenBean);
			}
		}
		return derivedMap;
	}	
	
	private GenericSpecimenVO getSpecimenBean(Specimen specimen, String parentName)
	{
		GenericSpecimenVO specimenDataBean = new GenericSpecimenVO();
		//specimenDataBean.setBarcode(specimen.getBarcode());
		specimenDataBean.setClassName(specimen.getClassName());
//		specimenDataBean.setCreatedOn(specimen.getCreatedOn());
		specimenDataBean.setDisplayName(specimen.getLabel());
		specimenDataBean.setPathologicalStatus(specimen.getPathologicalStatus());

		specimenDataBean.setParentName(parentName);
		specimenDataBean.setQuantity(specimen.getInitialQuantity().getValue().toString());
		
//		specimenDataBean.setAvailable(Boolean.TRUE);
//		specimenDataBean.setAvailableQuantity(availableQuantity);
//		specimenDataBean.setInitialQuantity(availableQuantity);
		//specimenDataBean.setConcentration(specimen.get);
		if (Constants.SPECIMEN_COLLECTED.equals(specimen.getCollectionStatus()))
		{
			specimenDataBean.setCheckedSpecimen(true);
			specimenDataBean.setReadOnly(true);
		}
		specimenDataBean.setType(specimen.getType());
		specimenDataBean.setStorageContainerForSpecimen("Virtual");
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
		specimenDataBean.setAliquotSpecimenCollection(getChildAliquots(specimen));
		specimenDataBean.setDeriveSpecimenCollection(getChildDerived(specimen));
		return specimenDataBean;
	}
}
