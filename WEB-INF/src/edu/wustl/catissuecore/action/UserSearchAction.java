/*
 * Created on Sep 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.UserForm;
import edu.wustl.catissuecore.bizlogic.AbstractBizLogic;
import edu.wustl.catissuecore.bizlogic.BizLogicFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.security.SecurityManager;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;
import gov.nih.nci.security.authorization.domainobjects.Role;
import gov.nih.nci.security.authorization.domainobjects.User;


/**
 * @author gautam_shetty
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserSearchAction extends SecureAction
{
    
    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.action.SecureAction#executeSecureAction(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward executeSecureAction(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        UserForm userForm = (UserForm)form;
        String target = userForm.getPageOf();
        
        try
        {
            User user = SecurityManager.getInstance(UserSearchAction.class).getUserById(String.valueOf(userForm.getSystemIdentifier()));
            AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);
            List userList = bizLogic.retrieve(edu.wustl.catissuecore.domain.User.class.getName(),"systemIdentifier",new Long(userForm.getSystemIdentifier()));
            Role role = SecurityManager.getInstance(UserSearchAction.class).getUserRole(userForm.getSystemIdentifier());
            edu.wustl.catissuecore.domain.User appUser = null;
            if (!userList.isEmpty())
            {
                appUser = (edu.wustl.catissuecore.domain.User)userList.get(0);
                appUser.setLoginName(user.getLoginName());
                appUser.setLastName(user.getLastName());
                appUser.setFirstName(user.getFirstName());
                appUser.setEmailAddress(user.getEmailId());
                appUser.setRoleId(role.getId().toString());
            }
            
            userForm.setAllValues(appUser);
        }
        catch(DAOException daoExp)
        {
            daoExp.printStackTrace();
            Logger.out.debug(daoExp.getMessage(), daoExp);
            target = Constants.FAILURE;
        }
        
        
        return mapping.findForward(target);
    }

}
 