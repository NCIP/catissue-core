/**
 * <p>Title: ApproveUserAction Class>
 * <p>Description:	ApproveUserAction is used to Approve/Reject a user's registration request.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Apr 26, 2005
 */

package edu.wustl.catissuecore.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.actionForm.DomainObjectListForm;
import edu.wustl.catissuecore.dao.AbstractBizLogic;
import edu.wustl.catissuecore.dao.BizLogicFactory;
import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.GeneratePassword;
import edu.wustl.catissuecore.util.global.SendEmail;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.util.logger.Logger;

/**
 * ApproveUserAction is used to Approve/Reject a user's registration request.
 * @author gautam_shetty
 */
public class ApproveUserAction extends Action
{

    /**
     * Overrides the execute method in Action.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        String target = null;
        DomainObjectListForm approveUserForm = (DomainObjectListForm) form;

        //Gets the collection of users to be approved/rejected.
        Collection col = approveUserForm.getAllValues();
        Iterator iterator = col.iterator();

        AbstractBizLogic bizLogic = BizLogicFactory.getBizLogic(Constants.USER_FORM_ID);

        List list = null;
        String activityStatus = null;

        while (iterator.hasNext())
        {
            String identifier = (String) iterator.next();
            String objName = AbstractDomainObject
                    .getDomainObjectName(Constants.USER_FORM_ID);
            list = bizLogic.retrieve(objName, Constants.IDENTIFIER, new Long(
                    identifier));

            if (list.size() != 0)
            {
                User user = (User) list.get(0);
                String emailBody = null;

                //Set the time of approval.
                //                user.setMemberSince(Calendar.getInstance().getTime());

                //Sets the comments given by the approver.
                if (approveUserForm.getComments() != null)
                {
                    //                    StringWriter str = new StringWriter(approveUserForm.getComments().length());
                    //                    str.write(approveUserForm.getComments());
                    //                    Clob newClob = new SerialClob(approveUserForm.getComments().toCharArray() ); 
                    //                    user.setComments(newClob);
                    //                    DatabaseMetaData dbMetaData = st.getConnection().getMetaData();
                    //Clob newClob =  new ClobImpl(approveUserForm.getComments());
                    user.setComments(approveUserForm.getComments());
                    //                    System.out.println("CLOB VALUE..................."+newClob.toString());
                    //                    user.getComments().getAsciiStream().read(approveUserForm.getComments().getBytes());
                }

                if (approveUserForm.getOperation().equals(
                        Constants.ACTIVITY_STATUS_APPROVE))
                {
                    //If operation is equal to Approve, generate a password for the user.
                    String password = GeneratePassword.getPassword(user.getUser().getLoginName());
                    user.getUser().setPassword(password);

                    //Change the Activity Status to approve status.
                    //                    list = dao.retrieve(AbstractDomainObject.getDomainObjectName(Constants.ACTIVITY_STATUS_FORM_ID),
                    //                                    "status", Constants.ACTIVITY_STATUS_ACTIVE);
                    activityStatus = Constants.ACTIVITY_STATUS_ACTIVE;

                    emailBody = "Your membership has been approved.\n\nLogin Name : "
                            + user.getUser().getLoginName()
                            + "\nPassword : "
                            + password
                            + "\n\nRegards,\n-catissuecore Administrator.";
                }
                else
                {
                    //Change the Activity Status for reject status.
                    //                    list = dao.retrieve(AbstractDomainObject.getDomainObjectName(Constants.ACTIVITY_STATUS_FORM_ID),
                    //                                    "status", Constants.ACTIVITY_STATUS_REJECT);
                    activityStatus = Constants.ACTIVITY_STATUS_REJECT;

                    emailBody = "Your membership has been rejected."
                            + "\n\nRegards,\n-catissuecore Administrator.";
                }

                //Sets the activity status as active.
                user.setActivityStatus(activityStatus);

                //Updates user's information in the database.
                bizLogic.update(user);

                SendEmail email = new SendEmail();

                //Sends the membership status email to the user.
                boolean emailStatus = email.sendmail(user.getAddress()
                        .getEmailAddress(), Variables.toAddress,
                        Variables.mailServer,
                        Constants.APPROVE_USER_EMAIL_SUBJECT, emailBody);

                if (emailStatus == true)
                {
                    Logger.out.debug("Password successfully sent to "
                            + user.getUser().getLoginName() + " at "
                            + user.getAddress().getEmailAddress());
                }
                else
                {
                    Logger.out.error("Sending Password Failed to "
                            + user.getUser().getLoginName() + " at "
                            + user.getAddress().getEmailAddress());
                    target = new String(Constants.FAILURE);
                }
                target = new String(Constants.SUCCESS);
            }
        }
        return mapping.findForward(target);
    }
}