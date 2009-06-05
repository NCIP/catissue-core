package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.ViewSpecimenSummaryForm;
import edu.wustl.catissuecore.bean.CollectionProtocolEventBean;
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenDataBean;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.util.SpecimenDetailsTagUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.UserNotAuthorizedException;

public class UpdateBulkSpecimensAction extends UpdateSpecimenStatusAction
{
	private transient Logger logger = Logger.getCommonLogger(UpdateBulkSpecimensAction.class);
	private SpecimenCollectionGroup specimenCollectionGroup = null;
	private ViewSpecimenSummaryForm specimenSummaryForm = null;
	public ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		IBizLogic bizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
		SessionDataBean sessionDataBean = (SessionDataBean) session.getAttribute(Constants.SESSION_DATA);
		try{
			specimenSummaryForm =(ViewSpecimenSummaryForm)form;
			String eventId = specimenSummaryForm.getEventId();
			session = request.getSession();
			LinkedHashSet specimenDomainCollection = getSpecimensToSave(eventId, session);
			if (ViewSpecimenSummaryForm.ADD_USER_ACTION.equals(specimenSummaryForm.getUserAction()))
			{
				//Abhishek Mehta : Performance related Changes
				Collection<AbstractDomainObject> specimenCollection = new LinkedHashSet<AbstractDomainObject>();
				specimenCollection.addAll(specimenDomainCollection);
				new NewSpecimenBizLogic().insert(specimenCollection,sessionDataBean, 0,false);
				setLabelBarCodesToSessionData(eventId, request, specimenDomainCollection);
				
				updateWithNewStorageLocation(session, sessionDataBean, eventId,
						specimenDomainCollection);
			}
			else
			{
				((NewSpecimenBizLogic)bizLogic).bulkUpdateSpecimens(specimenDomainCollection, sessionDataBean);
				setLabelBarCodesToSessionData(eventId, request, specimenDomainCollection);
				
				//11479 S
				updateWithNewStorageLocation(session, sessionDataBean, eventId,
						specimenDomainCollection);
				//11479 E
				
				/*Iterator iter=specimenDomainCollection.iterator();
				List specimenIdList=new ArrayList();
				while( iter.hasNext())
				{
					specimenIdList.add(((Specimen)iter.next()).getId());
				}
				request.setAttribute("specimenIdList", specimenIdList);*/
			}
			ActionMessages actionMessages = new ActionMessages();
			actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
					"object.add.successOnly","Specimens"));
			specimenSummaryForm.setShowbarCode(true);
			specimenSummaryForm.setShowLabel(true);
			saveMessages(request, actionMessages);
			specimenSummaryForm.setReadOnly(true);
				
			//if(request.getParameter("pageOf") != null)
			//	return mapping.findForward(request.getParameter("pageOf"));
			
			//19May08 : Mandar : For GenericSpecimen
			SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request, specimenSummaryForm, true);
			if(request.getAttribute("printflag")!=null && request.getAttribute("printflag").equals("1"))
			{
				HashMap forwardToPrintMap = new HashMap();
				forwardToPrintMap.put("printMultipleSpecimen",specimenDomainCollection );
				request.setAttribute("forwardToPrintMap",forwardToPrintMap);
				request.setAttribute("printMultiple","1");
				if(request.getParameter("pageOf") != null)
					request.setAttribute("pageOf",request.getParameter("pageOf"));
				//bug 12656 start
				if(specimenSummaryForm.getForwardTo() != null && !specimenSummaryForm.getForwardTo().equals(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART))
				{
					return mapping.findForward("printMultiple");
				}
				//bug 12656 end
				
			}
			if(specimenSummaryForm.getForwardTo()!=null&&specimenSummaryForm.getForwardTo().equals(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART))
			{
				Iterator iter=specimenDomainCollection.iterator();
				List specimenIdList=new ArrayList();
				while( iter.hasNext())
				{
					specimenIdList.add(((Specimen)iter.next()).getId());
				}
				request.setAttribute("specimenIdList", specimenIdList);
				if(request.getAttribute("pageOf")== null)
				{
				   request.setAttribute("pageOf", Constants.SUCCESS);
				}
				//bug 12656 start
				if (request.getAttribute("printflag")!=null && request.getAttribute("printflag").equals("1"))
				{
				   request.setAttribute("pageOf", Constants.SUCCESS);
				   return mapping.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART_AND_PRINT);//"printAnticipatorySpecimens";
				}
				else
				{
					if(request.getAttribute("pageOf")!=null && request.getAttribute("pageOf").equals(Constants.PAGE_OF_MULTIPLE_SPECIMEN_WITHOUT_MENU))
					{
						return mapping.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART_WITHOUT_MENU);//
					}
					else
					{
					    return mapping.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART);
					}
				}
				//bug 12656 end
				
			}
			if(request.getParameter("pageOf") != null)
				return mapping.findForward(request.getParameter("pageOf"));
			
			return mapping.findForward(Constants.SUCCESS);
		}
		catch(Exception exception)
		{
			logger.debug(exception.getMessage(), exception);
			//11July08 : Mandar : For GenericSpecimen
			SpecimenDetailsTagUtil.setAnticipatorySpecimenDetails(request, specimenSummaryForm, true);
		   
			ActionErrors actionErrors = new ActionErrors();
			
			// To delegate UserNotAuthorizedException forward
			if(exception instanceof UserNotAuthorizedException)
			{
	            UserNotAuthorizedException ex = (UserNotAuthorizedException) exception;
	            String userName = "";
	        	
	            if(sessionDataBean != null)
	        	{
	        	    userName = sessionDataBean.getUserName();
	        	}
	            String className = Constants.SPECIMEN;
	            String decoratedPrivilegeName = AppUtility.getDisplayLabelForUnderscore(ex.getPrivilegeName());
	            String baseObject = "";
	            if (ex.getBaseObject() != null && ex.getBaseObject().trim().length() != 0)
	            {
	                baseObject = ex.getBaseObject();
	            } else 
	            {
	                baseObject = className;
	            }
	                
	            ActionError error = new ActionError("access.addedit.object.denied", userName, className,decoratedPrivilegeName,baseObject);
	            actionErrors.add(ActionErrors.GLOBAL_ERROR, error);
	        	saveErrors(request, actionErrors);
	        	return (mapping.findForward("multipleSpWithMenuFaliure"));
			}
			exception.printStackTrace();
			actionErrors.add(actionErrors.GLOBAL_MESSAGE, new ActionError(
					"errors.item",exception.getMessage()));
			saveErrors(request, actionErrors);
			saveToken(request);
			if(request.getParameter("pageOf") != null)
				return mapping.findForward("multipleSpWithMenuFaliure");
			return mapping.findForward(Constants.FAILURE);
		}
	}
	private void updateWithNewStorageLocation(HttpSession session,
			SessionDataBean sessionDataBean, String eventId,
			LinkedHashSet specimenDomainCollection) throws DAOException{
		Iterator<Specimen> specimensItr = specimenDomainCollection.iterator();
		Map collectionProtocolEventMap = (Map) session.getAttribute(Constants.COLLECTION_PROTOCOL_EVENT_SESSION_MAP);

		CollectionProtocolEventBean eventBean = (CollectionProtocolEventBean) collectionProtocolEventMap.get(eventId);

		LinkedHashMap<String,GenericSpecimen> specimenMap = (LinkedHashMap) eventBean.getSpecimenRequirementbeanMap();
		Collection<GenericSpecimen> specCollection = specimenMap.values();
		Iterator<GenericSpecimen> iterator = specCollection.iterator();
		HibernateDAO dao = (HibernateDAO)  DAOConfigFactory.getInstance().getDAOFactory(Constants.APPLICATION_NAME).getDAO();
		dao.openSession(sessionDataBean);
		while (iterator.hasNext())
		{
			SpecimenDataBean specimenDataBean = (SpecimenDataBean) iterator.next();
			Specimen specimen =specimenDataBean.getCorresSpecimen();
			if((specimen.getSpecimenPosition()!=null)&&(specimen.getSpecimenPosition().getPositionDimensionOne()!=null)&&(specimen.getSpecimenPosition().getPositionDimensionTwo()!=null))
			{
				specimenDataBean.setPositionDimensionOne(String.valueOf(specimen.getSpecimenPosition().getPositionDimensionOne()));
				specimenDataBean.setPositionDimensionTwo(String.valueOf(specimen.getSpecimenPosition().getPositionDimensionTwo()));
			}
			LinkedHashMap<String,GenericSpecimen> derivesMap=specimenDataBean.getDeriveSpecimenCollection();
			Collection derivesCollection=derivesMap.values();
			Iterator deriveItr = derivesCollection.iterator();
			while(deriveItr.hasNext())
			{
				SpecimenDataBean deriveSpecimenDataBean =(SpecimenDataBean)deriveItr.next();
				Specimen deriveSpec= deriveSpecimenDataBean.getCorresSpecimen();
				if((deriveSpec.getSpecimenPosition()!=null)&&(deriveSpec.getSpecimenPosition().getPositionDimensionOne()!=null)&&(deriveSpec.getSpecimenPosition().getPositionDimensionTwo()!=null))
				{
					deriveSpecimenDataBean.setPositionDimensionOne(String.valueOf(deriveSpec.getSpecimenPosition().getPositionDimensionOne()));
					deriveSpecimenDataBean.setPositionDimensionTwo(String.valueOf(deriveSpec.getSpecimenPosition().getPositionDimensionTwo()));
				}
			}
		}
		dao.closeSession();
		
		ViewSpecimenSummaryAction viewSpecimenSummaryAction= new ViewSpecimenSummaryAction();
		viewSpecimenSummaryAction.populateSpecimenSummaryForm(specimenSummaryForm, specimenMap);
	}
	/**
	 * @param specimenVO
	 * @return
	 * @throws ApplicationException 
	 */
	protected Specimen createSpecimenDomainObject(GenericSpecimen specimenVO) throws ApplicationException{

		specimenVO = (SpecimenDataBean) specimenVO;
		specimenVO.setCheckedSpecimen(true);
		specimenVO.setPrintSpecimen(specimenVO.getPrintSpecimen());//Bug 12631
		Specimen specimen = super.createSpecimenDomainObject(specimenVO);
		setValuesForSpecimen(specimen,specimenVO);
		if (ViewSpecimenSummaryForm.ADD_USER_ACTION
				.equals(specimenSummaryForm.getUserAction()))
		{
			setValuesForNewSpecimen(specimen,specimenVO);
		}
			
		return specimen;
	}
	protected void setValuesForNewSpecimen(Specimen specimen, GenericSpecimen genericSpecimen)
	{
		SpecimenDataBean specimenDataBean = (SpecimenDataBean) genericSpecimen;

		specimen.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		specimen.setComment(specimenDataBean.getComment());
		if(specimen.getCreatedOn()==null)
		{
			specimen.setCreatedOn(new Date());
		}
		specimen.setCollectionStatus(Constants.SPECIMEN_COLLECTED);
		genericSpecimen.setCheckedSpecimen(true);
		genericSpecimen.setPrintSpecimen(specimenDataBean.getPrintSpecimen());//Bug 12631
		specimenDataBean.setCorresSpecimen(specimen);

		specimen.setSpecimenEventCollection(specimenDataBean.getSpecimenEventCollection());
		specimen.setAvailableQuantity(specimen.getInitialQuantity());
		if(specimenDataBean.getSpecimenEventCollection()!=null && !specimenDataBean.getSpecimenEventCollection().isEmpty())
		{
			Iterator iterator = specimenDataBean.getSpecimenEventCollection().iterator();
			while(iterator.hasNext())
			{
				SpecimenEventParameters specimenEventParameters =
					(SpecimenEventParameters) iterator.next();
				specimenEventParameters.setSpecimen(specimen);
				
			}
		} 
		 
	}
	protected void setValuesForSpecimen(Specimen specimen, GenericSpecimen genericSpecimen)
	{
		SpecimenDataBean specimenDataBean = (SpecimenDataBean) genericSpecimen;
		specimen.setPathologicalStatus(specimenDataBean.getPathologicalStatus());
		specimen.setLineage(specimenDataBean.getLineage());
		SpecimenCharacteristics specimenCharacteristics = new SpecimenCharacteristics();
		specimenCharacteristics.setTissueSide(specimenDataBean.getTissueSide());
		specimenCharacteristics.setTissueSite(specimenDataBean.getTissueSite());
		specimen.setSpecimenCharacteristics(specimenCharacteristics);		
		specimen.setLineage(specimenDataBean.getLineage());
		specimen.setIsAvailable(Boolean.TRUE);
		specimen.setPathologicalStatus(
				specimenDataBean.getPathologicalStatus());
		specimen.setSpecimenType(specimenDataBean.getType());
		specimen.setParentSpecimen(specimenDataBean.getParentSpecimen());
		specimen.setComment(specimenDataBean.getComment());
		specimen.setExternalIdentifierCollection(specimenDataBean.getExternalIdentifierCollection());
		specimen.setBiohazardCollection(specimenDataBean.getBiohazardCollection());
		specimen.setSpecimenCollectionGroup(specimenDataBean.getSpecimenCollectionGroup());
		if(specimenDataBean.getCorresSpecimen()!=null)
		{
			specimen.setCreatedOn(specimenDataBean.getCorresSpecimen().getCreatedOn());
		}
		
		specimenDataBean.setCorresSpecimen(specimen);
	}

	private void setLabelBarCodesToSessionData(String EventId, 
						HttpServletRequest request, Collection SpecimenCollection)
	{
		HttpSession session = request.getSession();
		LinkedHashMap specimenMap = (LinkedHashMap) session
		.getAttribute(Constants.SPECIMEN_LIST_SESSION_MAP);
		setLabelBarCodeToSpecimens(specimenMap);
		
	}

	/**
	 * @param specimenMap
	 */
	private void setLabelBarCodeToSpecimens(LinkedHashMap specimenMap)
	{
		Collection specimenCollection = specimenMap.values();
		Iterator iterator = specimenCollection.iterator();
		while(iterator.hasNext())
		{
			SpecimenDataBean specimenDataBean = (SpecimenDataBean) iterator.next();
			Specimen specimen = specimenDataBean.getCorresSpecimen();
			GenericSpecimen formSpecimen = specimenDataBean.getFormSpecimenVo();

			if (specimen == null || formSpecimen == null)
			{
				continue;
			}
			formSpecimen.setDisplayName(specimen.getLabel());
			formSpecimen.setBarCode(specimen.getBarcode());
			setParentLabelToFormSpecimen(
					specimen, formSpecimen);
			if (specimenDataBean.getDeriveSpecimenCollection()!= null)
			{
				setLabelBarCodeToSpecimens(
						specimenDataBean.getDeriveSpecimenCollection());
			}
		}
	}

	/**
	 * @param specimen
	 * @param formSpecimen
	 * @return
	 */
	private void setParentLabelToFormSpecimen(Specimen specimen,
			GenericSpecimen formSpecimen)
	{
		Specimen parentSpecimen = (Specimen)specimen.getParentSpecimen();
		if (parentSpecimen != null)
		{
			formSpecimen.setParentName(parentSpecimen.getLabel());
		}
	}
}
