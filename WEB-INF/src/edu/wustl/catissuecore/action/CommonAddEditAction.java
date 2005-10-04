/**
 * <p>Title: CommonAddEditAction Class>
 * <p>Description:	This Class is used to Add/Edit the data in the database.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 21, 2005
 */

package edu.wustl.catissuecore.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.DistributionForm;
import edu.wustl.catissuecore.actionForm.SpecimenEventParametersForm;
import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Distribution;
import edu.wustl.catissuecore.domain.DomainObjectFactory;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.exception.AssignDataException;
import edu.wustl.catissuecore.exception.BizLogicException;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * This Class is used to Add/Edit data in the database.
 * @author gautam_shetty
 */
public class CommonAddEditAction extends Action
{

    /**
     * Overrides the execute method of Action class.
     * Adds / Updates the data in the database.
     * */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        String target = null;
        AbstractDomainObject abstractDomain = null;

        try
        {
            AbstractActionForm abstractForm = (AbstractActionForm) form;
            AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(abstractForm.getFormId());
            
            if(abstractForm instanceof SpecimenEventParametersForm)
            {
            	String specimenId = String.valueOf(((SpecimenEventParametersForm)abstractForm).getSpecimenId());
            	request.setAttribute(Constants.SPECIMEN_ID,specimenId);
            }
            if(abstractForm instanceof DistributionForm)
            {
            	//Setting Distribution ID as request parameter
            	Long distributionId =new Long(((DistributionForm)abstractForm).getSystemIdentifier());
            	Logger.out.debug("distributionId "+distributionId);
            	request.setAttribute(Constants.DISTRIBUTION_ID,distributionId);
            }

            //The object name which is to be added. 
            String objectName = AbstractDomainObject.getDomainObjectName(abstractForm.getFormId());
            
            ActionMessages messages = null;
            if (abstractForm.isAddOperation())
            {
                //If operation is add, add the data in the database.
                abstractDomain = DomainObjectFactory.getDomainObject(
                        abstractForm.getFormId(), abstractForm);
                
                bizLogic.insert(abstractDomain, getSessionData(request), Constants.HIBERNATE_DAO);
            	
                if(abstractDomain instanceof Specimen)
            		request.setAttribute(Constants.SPECIMEN_ID,String.valueOf(abstractDomain.getSystemIdentifier()));
                
            	if(abstractDomain instanceof Distribution)
                {
                	//Setting Distribution ID as request parameter
                	request.setAttribute(Constants.DISTRIBUTION_ID,abstractDomain.getSystemIdentifier());
                }
            	
                if(abstractDomain instanceof SpecimenCollectionGroup)
                {
                	request.setAttribute(Constants.SPECIMEN_COLLECTION_GROUP_ID,abstractDomain.getSystemIdentifier().toString());
            		target = new String(Constants.REDIRECT_TO_SPECIMEN);
            		return (mapping.findForward(target));
                }	
            	else
            		target = new String(Constants.SUCCESS);

            target = new String(Constants.SUCCESS);
                
                //The successful add messages.
                messages = new ActionMessages();
                messages.add(ActionErrors.GLOBAL_MESSAGE,new ActionMessage("object.add.success",
                        	 AbstractDomainObject.parseClassName(objectName), abstractDomain.getSystemIdentifier()));
                
                if (abstractDomain.getSystemIdentifier() != null)
                {
                    //Setting the system identifier after inserting the object in the DB.
                    abstractForm.setSystemIdentifier(abstractDomain.getSystemIdentifier().longValue());
                    abstractForm.setMutable(false);
                }
            
               
	               Logger.out.debug("CAE :------  " +abstractForm.getRedirectTo());
	               if (abstractForm.getRedirectTo()!=null && abstractForm.getRedirectTo().trim().length() >0 )
	               {
	               		String reDirectUrl = abstractForm.getRedirectTo();
	               		Logger.out.debug("redirecturl -- :  : " + reDirectUrl);
	               		
	               		String tmpreDirectUrl = null;
	               		
	               		
	               		if(reDirectUrl.lastIndexOf('|') != -1)
	               		{
	               			tmpreDirectUrl = reDirectUrl.substring(reDirectUrl.lastIndexOf('|')+1);;
	               			String remainingURL = reDirectUrl.substring(0,reDirectUrl.lastIndexOf('|'));
	               			Logger.out.debug("remaurl -- :  : " + remainingURL);
	               			tmpreDirectUrl = tmpreDirectUrl.replaceAll("_","&" );
	               			tmpreDirectUrl = tmpreDirectUrl + "&"+Constants.REQ_PATH + "="+ remainingURL;  
	               		}
	               		else
	               		{
	               			tmpreDirectUrl = reDirectUrl; 
	               			reDirectUrl =null; 
	               			tmpreDirectUrl = tmpreDirectUrl.replaceAll("_","&" );
	               		}
	               		
	               		Logger.out.debug("tmpurl -- :  : " + tmpreDirectUrl);
	               		
	               		ActionForward reDirectForward = new ActionForward();
	//               		reDirectForward.setName("reDirectTo");
	               		reDirectForward.setPath(tmpreDirectUrl);
	               		return reDirectForward;
	               }
	               Logger.out.debug("CAE ---TARGET ----- "+ target); 
//	               return (mapping.findForward(target)); 
            }
            else
            {
                //If operation is edit, update the data in the database.
                List list = bizLogic.retrieve(objectName, Constants.IDENTIFIER,
										  new Long(abstractForm.getSystemIdentifier()));
                if (!list.isEmpty())
                {
                	abstractDomain = (AbstractDomainObject) list.get(0);
                    abstractDomain.setAllValues(abstractForm);
                    bizLogic.update(abstractDomain,Constants.HIBERNATE_DAO, getSessionData(request));
                    target = new String(Constants.SUCCESS);
                    
                    //The successful edit message.
                    messages = new ActionMessages();
                    messages.add(ActionErrors.GLOBAL_MESSAGE,new ActionMessage("object.edit.success",
                       	 AbstractDomainObject.parseClassName(objectName), abstractDomain.getSystemIdentifier()));
                }
                else
                {
                    target = new String(Constants.FAILURE);
                    
                    ActionErrors errors = new ActionErrors();
                	ActionError error = new ActionError("errors.item.unknown",
                	        				AbstractDomainObject.parseClassName(objectName));
                	errors.add(ActionErrors.GLOBAL_ERROR,error);
                	saveErrors(request,errors);
                }

                if (abstractForm.getRedirectTo()!=null)
                {
                		String reDirectUrl = abstractForm.getRedirectTo();
                		reDirectUrl = reDirectUrl.replaceAll("_","&");
                		
                		ActionForward reDirectForward = new ActionForward();
                		reDirectForward.setName("reDirectTo");
                		reDirectForward.setPath(reDirectUrl);
                		return reDirectForward;
                }
                
            }
            
            if (messages != null)
            {
                saveMessages(request,messages);
            }
            
            //Status message key.
            String statusMessageKey = String.valueOf(abstractForm.getFormId() +
					"."+String.valueOf(abstractForm.isAddOperation()));
            
            request.setAttribute(Constants.STATUS_MESSAGE_KEY,statusMessageKey);
        }
        catch (BizLogicException excp)
        {
        	ActionErrors errors = new ActionErrors();
        	ActionError error = new ActionError("errors.item",excp.getMessage());
        	errors.add(ActionErrors.GLOBAL_ERROR,error);
        	saveErrors(request,errors);
            target = new String(Constants.FAILURE);
            
            Logger.out.error(excp.getMessage(), excp);
        }
        catch (DAOException excp)
        {
            target = new String(Constants.FAILURE);
            Logger.out.debug("excp "+excp.getMessage());
            Logger.out.error(excp.getMessage(), excp);
        }
        catch (UserNotAuthorizedException excp)
        {
            
            ActionErrors errors = new ActionErrors();
            SessionDataBean sessionDataBean =getSessionData(request);
            String userName;
        	if(sessionDataBean == null)
        	{
        	    userName = "";
        	}
        	else
        	{
        	    userName = sessionDataBean.getUserName();
        	}
        	ActionError error = new ActionError("access.addedit.object.denied",userName,abstractDomain.getClass().getName());
        	errors.add(ActionErrors.GLOBAL_ERROR,error);
        	saveErrors(request,errors);
        	target = new String(Constants.FAILURE);
            Logger.out.debug("excp "+excp.getMessage());
            Logger.out.error(excp.getMessage(), excp);
        }
        catch (AssignDataException excp)
        {
            target = new String(Constants.FAILURE);
            Logger.out.debug("excp "+excp.getMessage());
            Logger.out.error(excp.getMessage(), excp);
        }
        
        Logger.out.debug("target....................."+target); 
        return (mapping.findForward(target));
    }
    
    protected SessionDataBean getSessionData(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if(obj!=null)
		{
			SessionDataBean sessionData = (SessionDataBean) obj;
			return  sessionData;
		}
		return null;
		//return (String) request.getSession().getAttribute(Constants.SESSION_DATA);
	}
}