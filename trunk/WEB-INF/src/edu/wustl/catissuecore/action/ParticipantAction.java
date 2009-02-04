/**
 * <p>Title: ParticipantAction Class>
 * <p>Description:  This class initializes the fields in the Participant Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 7, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.ParticipantForm;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.bizlogic.ParticipantBizLogic;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.SecureAction;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.cde.CDEManager;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * This class initializes the fields in the Participant Add/Edit webpage.
 * @author gautam_shetty
 */
public class ParticipantAction extends SecureAction
{

    /**
     * Overrides the execute method of Action class.
     * Sets the various fields in Participant Add/Edit webpage.
     * */
    protected ActionForward executeSecureAction(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        ParticipantForm participantForm = (ParticipantForm) form;
        //This if condition is for participant lookup. When participant is selected from the list then 
        //that participant gets stored in request as participantform1.
        //After that we have to show the slected participant in o/p
        if (request.getAttribute("participantSelect") != null)
        {
            participantForm = (ParticipantForm) request.getAttribute("participantForm1");
            request.setAttribute("participantForm", participantForm);
        }
        if (participantForm.getOperation().equals(Constants.EDIT) || participantForm.getOperation().equals("viewAnnotations"))
        {
            request.setAttribute("participantId", new Long(participantForm.getId()).toString());
        }
        //Bug- setting the default Gender
//      if (participantForm.getGender() == null)
//      {
//          participantForm.setGender(Constants.UNSPECIFIED);
//      }
        //Bug- setting the default Vital status
//      if (participantForm.getVitalStatus() == null)
//      {
//          participantForm.setVitalStatus(Constants.UNKNOWN);
//      }
        //List of keys used in map of ActionForm
        List key = new ArrayList();
        key.add("ParticipantMedicalIdentifier:i_Site_id");
        key.add("ParticipantMedicalIdentifier:i_medicalRecordNumber");

        //Gets the map from ActionForm
        Map map = participantForm.getValues();

        //Calling DeleteRow of BaseAction class
        MapDataParser.deleteRow(key, map, request.getParameter("status"));
        //Gets the value of the operation parameter.
        String operation = request.getParameter(Constants.OPERATION);

        //Sets the operation attribute to be used in the Add/Edit Participant Page. 
        request.setAttribute(Constants.OPERATION, operation);

        //Sets the pageOf attribute (for Add,Edit or Query Interface)
        String pageOf = request.getParameter(Constants.PAGEOF);

        request.setAttribute(Constants.PAGEOF, pageOf);

        //Sets the genderList attribute to be used in the Add/Edit Participant Page.
        List genderList = CDEManager.getCDEManager().getPermissibleValueList(
                Constants.CDE_NAME_GENDER, null);
        genderList.remove(0);
        request.setAttribute(Constants.GENDER_LIST, genderList);
        if (participantForm.getGender() == null || participantForm.getGender().equals(""))
        {
            Iterator itr = genderList.iterator();
            while (itr.hasNext())
            {
                NameValueBean nvb = (NameValueBean) itr.next();
                participantForm.setGender(nvb.getValue());
                break;
            }

        }

        //Sets the genotypeList attribute to be used in the Add/Edit Participant Page.
        //NameValueBean unknownVal = new NameValueBean(Constants.UNKNOWN,Constants.UNKNOWN);
        List genotypeList = CDEManager.getCDEManager().getPermissibleValueList(
                Constants.CDE_NAME_GENOTYPE, null);
        request.setAttribute(Constants.GENOTYPE_LIST, genotypeList);    
        
        //Bug- setting the default Genotype
//      if(participantForm.getGenotype() == null)
//      {
//          participantForm.setGenotype(Constants.UNKNOWN);
//      }

        //Sets the ethnicityList attribute to be used in the Add/Edit Participant Page.
        List ethnicityList = CDEManager.getCDEManager().getPermissibleValueList(
                Constants.CDE_NAME_ETHNICITY, null);
        request.setAttribute(Constants.ETHNICITY_LIST, ethnicityList);
        //Bug- setting the default ethnicity
//      if (participantForm.getEthnicity() == null)
//      {
//          participantForm.setEthnicity(Constants.NOTSPECIFIED);
//      }

        //Sets the raceList attribute to be used in the Add/Edit Participant Page.
        List raceList = CDEManager.getCDEManager().getPermissibleValueList(Constants.CDE_NAME_RACE,
                null);
        request.setAttribute(Constants.RACELIST, raceList);     
        //Bug- setting the default raceTypes
//      if (participantForm.getRaceTypes() == null || participantForm.getRaceTypes().length == 0)
//      {
//          String[] raceTypes = {Constants.NOTSPECIFIED};
//          participantForm.setRaceTypes(raceTypes);
//      }       

        //Sets the vitalStatus attribute to be used in the Add/Edit Participant Page.
        List vitalStatusList = CDEManager.getCDEManager().getPermissibleValueList(
                Constants.CDE_VITAL_STATUS, null);
        vitalStatusList.remove(0);
        request.setAttribute(Constants.VITAL_STATUS_LIST, vitalStatusList);
        if (participantForm.getVitalStatus() == null || participantForm.getVitalStatus().equals(""))
        {
            Iterator itr = vitalStatusList.iterator();
            while (itr.hasNext())
            {
                NameValueBean nvb = (NameValueBean) itr.next();
                participantForm.setVitalStatus(nvb.getValue());
                break;
            }

        }
        //Sets the activityStatusList attribute to be used in the Site Add/Edit Page.
        request.setAttribute(Constants.ACTIVITYSTATUSLIST, Constants.ACTIVITY_STATUS_VALUES);

        ParticipantBizLogic bizlogic = (ParticipantBizLogic) BizLogicFactory.getInstance()
                .getBizLogic(Constants.PARTICIPANT_FORM_ID);

        //Sets the Site list of corresponding type.
        String sourceObjectName = Site.class.getName();
        String[] displayNameFields = {"name"};
        String valueField = Constants.SYSTEM_IDENTIFIER;

        List siteList = bizlogic.getList(sourceObjectName, displayNameFields, valueField, true);

        request.setAttribute(Constants.SITELIST, siteList);
        Logger.out.debug("pageOf :---------- " + pageOf);

        return mapping.findForward(pageOf);
    }
}