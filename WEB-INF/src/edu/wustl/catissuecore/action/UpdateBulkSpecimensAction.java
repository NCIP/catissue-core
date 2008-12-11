package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

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
import edu.wustl.catissuecore.bean.GenericSpecimen;
import edu.wustl.catissuecore.bean.SpecimenDataBean;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.util.SpecimenDetailsTagUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;

public class UpdateBulkSpecimensAction extends UpdateSpecimenStatusAction
{
	private SpecimenCollectionGroup specimenCollectionGroup = null;
	private ViewSpecimenSummaryForm specimenSummaryForm = null;
	public ActionForward executeAction(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		IBizLogic bizLogic = BizLogicFactory.getInstance().getBizLogic(
				Constants.NEW_SPECIMEN_FORM_ID);
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
				new NewSpecimenBizLogic().insert(specimenCollection,sessionDataBean, Constants.HIBERNATE_DAO,false);
				setLabelBarCodesToSessionData(eventId, request, specimenDomainCollection);				
			}
			else
			{
				((NewSpecimenBizLogic)bizLogic).bulkUpdateSpecimens(specimenDomainCollection, sessionDataBean);
				setLabelBarCodesToSessionData(eventId, request, specimenDomainCollection);
				
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
				return mapping.findForward("printMultiple");
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
				request.setAttribute("pageOf", Constants.SUCCESS);
				return mapping.findForward(Constants.ADD_MULTIPLE_SPECIMEN_TO_CART);
			}
			if(request.getParameter("pageOf") != null)
				return mapping.findForward(request.getParameter("pageOf"));
			
			return mapping.findForward(Constants.SUCCESS);
		}
		catch(Exception exception)
		{
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
	            String decoratedPrivilegeName = Utility.getDisplayLabelForUnderscore(ex.getPrivilegeName());
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
	/**
	 * @param specimenVO
	 * @return
	 */
	protected Specimen createSpecimenDomainObject(GenericSpecimen specimenVO) throws BizLogicException {

		specimenVO = (SpecimenDataBean) specimenVO;
		specimenVO.setCheckedSpecimen(true);
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

		specimen.setActivityStatus(Constants.ACTIVITY_STATUS_ACTIVE);
		specimen.setComment(specimenDataBean.getComment());
		if(specimen.getCreatedOn()==null)
		{
			specimen.setCreatedOn(new Date());
		}
		specimen.setCollectionStatus(Constants.SPECIMEN_COLLECTED);
		genericSpecimen.setCheckedSpecimen(true);
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
		specimen.setCreatedOn(specimenDataBean.getCorresSpecimen().getCreatedOn());
		
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
