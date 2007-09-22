package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.upmc.opi.caBIG.caTIES.database.domain.Specimen;
import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.SpecimenCollectionGroupBizLogic;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.factory.AbstractDomainObjectFactory;
import edu.wustl.common.factory.MasterFactory;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

public class PrintLabelAction extends Action {

	 public ActionForward execute(ActionMapping mapping, ActionForm form,
	            HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException
     {
	 	 try
	 	 {
		 	 SpecimenCollectionGroupForm  specimenCollectionGroupForm = (SpecimenCollectionGroupForm)form;
		 	 AbstractDomainObjectFactory abstractDomainObjectFactory = (AbstractDomainObjectFactory) MasterFactory.getFactory(ApplicationProperties.getValue("app.domainObjectFactory"));
		 	 SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)abstractDomainObjectFactory.getDomainObject(specimenCollectionGroupForm.getFormId(), specimenCollectionGroupForm);
		 	 SessionDataBean sessionDataBean = getSessionData(request);
		 	 printSCGLabels(specimenCollectionGroup,sessionDataBean);
	 	 }
	 	 catch(Exception e)
	 	 {
	 		Logger.out.error(e.getMessage(), e);
	 	 }
	 	 return mapping.findForward("");
     }
	 
	private SessionDataBean getSessionData(HttpServletRequest request) 
    {
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		 /**
		  *  This if loop is specific to Password Security feature.
		  */
		if(obj == null)
		{
			obj = request.getSession().getAttribute(Constants.TEMP_SESSION_DATA);
		}
		if(obj!=null)
		{
			SessionDataBean sessionData = (SessionDataBean) obj;
			return  sessionData;
		} 
		return null;
	}
	 
	 private void printSCGLabels(SpecimenCollectionGroup specimenCollectionGroup , SessionDataBean sessionDataBean) throws DAOException
	 {
		 Collection specimenCollection = getSpecimens(specimenCollectionGroup);
		 Iterator it = specimenCollection.iterator();
		 while(it.hasNext())
		 {
			 Specimen specimen = (Specimen)it.next();
			 String printString = getPrintObject(specimen);
			 print(printString,sessionDataBean);
		 }
	 }
	
	 private Collection getSpecimens(SpecimenCollectionGroup specimenCollectionGroup) throws DAOException
	 {
		 SpecimenCollectionGroupBizLogic specimenCollectionGroupBizLogic = (SpecimenCollectionGroupBizLogic)BizLogicFactory.getInstance().getBizLogic(Constants.SPECIMEN_COLLECTION_GROUP_FORM_ID);
		 Collection specimenCollection = (Collection)specimenCollectionGroupBizLogic.retrieveAttribute(SpecimenCollectionGroup.class.getName(), specimenCollectionGroup.getId(), "elements(specimenCollection)");
		 return specimenCollection;
	 }
	 
	 private String getPrintObject(Specimen specimen)
	 {
		 String printObj ="";
		 if(specimen != null)
		 {
			 
		 }
		 return printObj;
	 }
	 
	 private void print(String printString , SessionDataBean sessionDataBean)
	 {
		 
	 }
}
