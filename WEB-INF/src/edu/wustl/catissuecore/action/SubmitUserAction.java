
package edu.wustl.catissuecore.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.dto.UserDTO;
import edu.wustl.catissuecore.multiRepository.bean.SiteUserRolePrivilegeBean;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.beans.AddNewSessionDataBean;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IDomainObjectFactory;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.factory.IForwordToFactory;
import edu.wustl.common.util.AbstractForwardToProcessor;
import edu.wustl.common.util.global.CommonUtilities;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.daofactory.DAOConfigFactory;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.util.HibernateMetaData;
import edu.wustl.simplequery.bizlogic.QueryBizLogic;

/**
 * @author renuka_bajpai
 */
public class SubmitUserAction extends Action
{

	/**
	 * logger.
	 */
	private transient final Logger logger = Logger.getCommonLogger(SubmitUserAction.class);

	/**
	 * target.
	 */
	String target = null;

	/**
	 * abstractDomain.
	 */
	AbstractDomainObject abstractDomain = null;

	/**
	 * messages.
	 */
	ActionMessages messages = null;

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws AssignDataException
	 *             generic exception
	 * @return ActionForward : ActionForward
	 */

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws AssignDataException
	{
		try
		{
			final AbstractActionForm abstractForm = (AbstractActionForm) form;
			if (abstractForm.isAddOperation())
			{

				return this.executeAddUser(mapping, request, abstractForm);
			}
			else
			{
				return this.executeEditUser(mapping, request, abstractForm);
			}
		}
		catch (final BizLogicException excp)
		{
			final ActionErrors errors = new ActionErrors();
			final ActionError error = new ActionError("errors.item", excp.getMessage());
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);
			this.target = new String(Constants.FAILURE);

			this.logger.error(excp.getMessage(), excp);
		}
		catch (final DAOException excp)
		{
			this.target = Constants.FAILURE;
			this.logger.error(excp.getMessage(), excp);
		}
		catch (final ApplicationException excep)
		{
			this.target = Constants.FAILURE;
			this.logger.error(excep.getMessage(), excep);
		}
		return mapping.findForward(this.target);
	}

	/**
	 * @param mapping
	 *            : mapping
	 * @param request
	 *            : request
	 * @param form
	 *            : form
	 * @return ActionForward : ActionForward
	 * @throws ApplicationException
	 *             : ApplicationException
	 */
	private ActionForward executeAddUser(ActionMapping mapping, HttpServletRequest request,
			AbstractActionForm form) throws ApplicationException
	{
		User user = null;
		final IDomainObjectFactory abstractDomainObjectFactory = this.getIDomainObjectFactory();
		final AbstractActionForm form1 = form;
		user = (User) abstractDomainObjectFactory.getDomainObject(form1.getFormId(), form1);
		final AbstractActionForm abstractForm = form;
		final String objectName = abstractDomainObjectFactory.getDomainObjectName(abstractForm
				.getFormId());
		// UserBizLogic bizLogic = (UserBizLogic)
		// BizLogicFactory.getInstance().getBizLogic(Constants.USER_FORM_ID);
		final HttpSession session = request.getSession();
		// SessionDataBean sessionDataBean = (SessionDataBean)
		// session.getAttribute(Constants.SESSION_DATA);
		// bizLogic.insert(user, sessionDataBean, 0);
		this.target = new String(Constants.SUCCESS);
		final AbstractDomainObject abstractDomain = user;
		final UserDTO userDTO = this.getUserDTO(user, session);

		this.insertUser(userDTO, request.getSession(), form);

		// Attributes to decide AddNew action
		final String submittedFor = request.getParameter(Constants.SUBMITTED_FOR);
		request.setAttribute(Constants.SYSTEM_IDENTIFIER, user.getId());
		this.logger.debug("Checking parameter SubmittedFor in CommonAddEditAction--->"
				+ request.getParameter(Constants.SUBMITTED_FOR));
		this.logger.debug("SubmittedFor attribute of Form-Bean received---->"
				+ abstractForm.getSubmittedFor());
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic queryBizLogic = factory
				.getBizLogic(edu.wustl.common.util.global.Constants.QUERY_INTERFACE_ID);
		this.messages = new ActionMessages();

		this.addMessage(this.messages, abstractDomain, "add", (QueryBizLogic) queryBizLogic,
				objectName);

		if ((submittedFor != null) && (submittedFor.equals("AddNew")))
		{
			this.logger.debug("SubmittedFor is AddNew in CommonAddEditAction...................");

			final Stack formBeanStack = (Stack) session.getAttribute(Constants.FORM_BEAN_STACK);

			if (formBeanStack != null)
			{

				final AddNewSessionDataBean addNewSessionDataBean = (AddNewSessionDataBean) formBeanStack
						.pop();

				if (addNewSessionDataBean != null)
				{
					final AbstractActionForm sessionFormBean = addNewSessionDataBean
							.getAbstractActionForm();

					final String forwardTo = addNewSessionDataBean.getForwardTo();
					this.logger.debug("forwardTo in CommonAddEditAction--------->" + forwardTo);
					sessionFormBean.setAddNewObjectIdentifier(addNewSessionDataBean.getAddNewFor(),
							abstractDomain.getId());
					sessionFormBean.setMutable(false);
					if (formBeanStack.isEmpty())
					{
						session.removeAttribute(Constants.FORM_BEAN_STACK);
						request.setAttribute(Constants.SUBMITTED_FOR, "Default");
						this.logger.debug("SubmittedFor set as Default in"
								+ " CommonAddEditAction===========");

						this.logger.debug("cleaning FormBeanStack from session*************");
					}
					else
					{
						request.setAttribute(Constants.SUBMITTED_FOR, "AddNew");
					}
					final String formBeanName = CommonUtilities.getFormBeanName(sessionFormBean);
					request.setAttribute(formBeanName, sessionFormBean);

					this.logger.debug("InitiliazeAction operation=========>"
							+ sessionFormBean.getOperation());

					if (this.messages != null)
					{
						this.saveMessages(request, this.messages);
					}
					final String statusMessageKey
					= String.valueOf(abstractForm.getFormId() + "."
							+ String.valueOf(abstractForm.isAddOperation()));
					request.setAttribute(Constants.STATUS_MESSAGE_KEY, statusMessageKey);
					if ((sessionFormBean.getOperation().equals("edit")))
					{
						this.logger.debug("Edit object Identifier while AddNew "
								+ "is from Edit operation==>" + sessionFormBean.getId());
						final ActionForward editForward = new ActionForward();

						final String addPath = (mapping.findForward(forwardTo)).getPath();
						this.logger.debug("Operation before edit==========>" + addPath);

						final String editPath = addPath.replaceFirst("operation=add",
								"operation=edit");
						this.logger.debug("Operation edited=============>" + editPath);
						editForward.setPath(editPath);

						return editForward;
					}
					return (mapping.findForward(forwardTo));
				}
				else
				{
					this.target = new String(Constants.FAILURE);

					final ActionErrors errors = new ActionErrors();
					final ActionError error = new ActionError("errors.item.unknown",
							AbstractDomainObject.parseClassName(objectName));
					errors.add(ActionErrors.GLOBAL_ERROR, error);
					this.saveErrors(request, errors);
				}
			}
		}
		else if ((submittedFor != null) && (submittedFor.equals("ForwardTo")))
		{
			this.logger
					.debug("SubmittedFor is ForwardTo in CommonAddEditAction...................");
			request.setAttribute(Constants.SUBMITTED_FOR, "Default");
			request.setAttribute("forwardToHashMap", this.generateForwardToHashMap(abstractForm,
					abstractDomain));
		}
		if (abstractForm.getForwardTo() != null && abstractForm.getForwardTo().trim().length() > 0)
		{
			final String forwardTo = abstractForm.getForwardTo();
			this.logger.debug("ForwardTo in Add :-- : " + forwardTo);
			this.target = forwardTo;
		}
		request.setAttribute("forwardToPrintMap", this.generateForwardToPrintMap(abstractForm,
				abstractDomain));
		if (this.messages != null)
		{
			this.saveMessages(request, this.messages);
		}
		final String statusMessageKey = String.valueOf(abstractForm.getFormId() + "."
				+ String.valueOf(abstractForm.isAddOperation()));
		request.setAttribute(Constants.STATUS_MESSAGE_KEY, statusMessageKey);

		return mapping.findForward(this.target);
	}

	/**
	 * @param mapping
	 *            : mapping
	 * @param request
	 *            : request
	 * @param abstractForm
	 *            : abstractForm
	 * @return ActionForward : ActionForward
	 * @throws ApplicationException
	 *             : ApplicationException
	 */
	private ActionForward executeEditUser(ActionMapping mapping, HttpServletRequest request,
			AbstractActionForm abstractForm) throws ApplicationException
	{
		this.target = new String(Constants.SUCCESS);
		DAO dao = null;
		// If operation is edit, update the data in the database.

		// List list = bizLogic.retrieve(objectName,
		// Constants.SYSTEM_IDENTIFIER,
		// new Long(abstractForm.getId()));
		// if (!list.isEmpty())
		// {
		// abstractDomain = (AbstractDomainObject) list.get(0);
		// abstractDomain.setAllValues(abstractForm);
		final DefaultBizLogic defaultBizLogic = new DefaultBizLogic();
		final IDomainObjectFactory abstractDomainObjectFactory = this.getIDomainObjectFactory();
		final String objectName = this.getObjectName(abstractDomainObjectFactory, abstractForm);
		this.abstractDomain = defaultBizLogic.populateDomainObject(objectName, new Long(
				abstractForm.getId()), abstractForm);
		if (this.abstractDomain != null)
		{
			dao = DAOConfigFactory.getInstance().getDAOFactory(Constants.APPLICATION_NAME).getDAO();

			dao.openSession(null);
			AbstractDomainObject abstractDomainOld = null;
			try
			{
				abstractDomainOld = (AbstractDomainObject) dao.retrieveById(objectName, new Long(
						abstractForm.getId()));
			}
			catch (final Exception ex)
			{
				this.logger.debug(ex.getMessage(), ex);
				ex.printStackTrace();
			}
			final HttpSession session = request.getSession();
			final UserDTO userCurrent = this.getUserDTO((User) this.abstractDomain, session);
			final User userOld = (User) abstractDomainOld;

			this.updateUser(userCurrent, userOld, request.getSession(), abstractForm);

			try
			{
				dao.closeSession();
			}
			catch (final Exception ex)
			{
				this.logger.debug(ex.getMessage(), ex);
				ex.printStackTrace();
			}

			if ((abstractForm.getActivityStatus() != null)
					&& (Status.ACTIVITY_STATUS_DISABLED.toString().equals(abstractForm
							.getActivityStatus())))
			{
				final String moveTo = abstractForm.getOnSubmit();
				this.logger.debug("MoveTo in Disabled :-- : " + moveTo);
				final ActionForward reDirectForward = new ActionForward();
				reDirectForward.setPath(moveTo);
				return reDirectForward;
			}

			if (abstractForm.getOnSubmit() != null
					&& abstractForm.getOnSubmit().trim().length() > 0)
			{
				final String forwardTo = abstractForm.getOnSubmit();
				this.logger.debug("OnSubmit :-- : " + forwardTo);
				return (mapping.findForward(forwardTo));
			}

			final String submittedFor = request.getParameter(Constants.SUBMITTED_FOR);
			this.logger.debug("Submitted for in Edit CommonAddEditAction===>" + submittedFor);

			if ((submittedFor != null) && (submittedFor.equals("ForwardTo")))
			{
				this.logger.debug("SubmittedFor is ForwardTo in" + " CommonAddEditAction...");

				request.setAttribute(Constants.SUBMITTED_FOR, "Default");

				request.setAttribute("forwardToHashMap", this.generateForwardToHashMap(
						abstractForm, this.abstractDomain));
			}

			if (abstractForm.getForwardTo() != null
					&& abstractForm.getForwardTo().trim().length() > 0)
			{
				final String forwardTo = abstractForm.getForwardTo();
				this.logger.debug("ForwardTo in Edit :-- : " + forwardTo);

				this.target = forwardTo;
			}

			final String pageOf = request.getParameter(Constants.PAGE_OF);
			this.logger.debug("pageof for query edit==" + pageOf);
			if (pageOf != null)
			{
				if (pageOf.equals(Constants.QUERY))
				{
					this.target = pageOf;
				}
			}
			this.messages = new ActionMessages();
			final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
			final IBizLogic queryBizLogic = factory
					.getBizLogic(edu.wustl.common.util.global.Constants.QUERY_INTERFACE_ID);
			this.addMessage(this.messages, this.abstractDomain, "edit",
					(QueryBizLogic) queryBizLogic, objectName);
		}
		else
		{
			this.target = new String(Constants.FAILURE);

			final ActionErrors errors = new ActionErrors();
			final ActionError error = new ActionError("errors.item.unknown", AbstractDomainObject
					.parseClassName(objectName));
			errors.add(ActionErrors.GLOBAL_ERROR, error);
			this.saveErrors(request, errors);
		}
		request.setAttribute("forwardToPrintMap", this.generateForwardToPrintMap(abstractForm,
				this.abstractDomain));

		if (this.messages != null)
		{
			this.saveMessages(request, this.messages);
		}

		final String statusMessageKey = String.valueOf(abstractForm.getFormId() + "."
				+ String.valueOf(abstractForm.isAddOperation()));
		request.setAttribute(Constants.STATUS_MESSAGE_KEY, statusMessageKey);
		return mapping.findForward(this.target);
	}

	/**
	 * * This method generates HashMap of data required to be forwarded if Form
	 * is submitted for Print request
	 *
	 * @param abstractForm
	 *            : abstractForm
	 * @param abstractDomain
	 *            : abstractDomain
	 * @return HashMap : HashMap
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private HashMap generateForwardToPrintMap(AbstractActionForm abstractForm,
			AbstractDomainObject abstractDomain) throws BizLogicException
	{
		HashMap forwardToPrintMap = null;
		final IForwordToFactory factory = AbstractFactoryConfig.getInstance().getForwToFactory();
		final AbstractForwardToProcessor forwardToProcessor = factory.getForwardToPrintProcessor();
		forwardToPrintMap = (HashMap) forwardToProcessor.populateForwardToData(abstractForm,
				abstractDomain);
		return forwardToPrintMap;
	}

	/**
	 * @param abstractForm
	 *            : abstractForm
	 * @param abstractDomain
	 *            : abstractDomain
	 * @return HashMap : HashMap
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private HashMap generateForwardToHashMap(AbstractActionForm abstractForm,
			AbstractDomainObject abstractDomain) throws BizLogicException
	{
		HashMap forwardToHashMap;
		final IForwordToFactory factory = AbstractFactoryConfig.getInstance().getForwToFactory();
		final AbstractForwardToProcessor forwardToProcessor = factory.getForwardToProcessor();
		forwardToHashMap = (HashMap) forwardToProcessor.populateForwardToData(abstractForm,
				abstractDomain);
		return forwardToHashMap;
	}

	/**
	 * @param abstractDomainObjectFactory
	 *            : abstractDomainObjectFactory
	 * @param abstractForm
	 *            : abstractForm
	 * @return String : String
	 */
	public String getObjectName(IDomainObjectFactory abstractDomainObjectFactory,
			AbstractActionForm abstractForm)
	{
		return abstractDomainObjectFactory.getDomainObjectName(abstractForm.getFormId());
	}

	/**
	 * @param user
	 *            : user
	 * @param session
	 *            : session
	 * @return UserDTO : UserDTO
	 */
	private UserDTO getUserDTO(User user, HttpSession session)
	{
		final UserDTO userDTO = new UserDTO();
		final Map<String, SiteUserRolePrivilegeBean> userRowIdBeanMap = (Map<String, SiteUserRolePrivilegeBean>) session
				.getAttribute("rowIdBeanMapForUserPage");
		userDTO.setUser(user);
		userDTO.setUserRowIdBeanMap(userRowIdBeanMap);
		return userDTO;
	}

	/**
	 * @param userDTO
	 *            : userDTO
	 * @param session
	 *            : session
	 * @param form
	 *            : form
	 * @throws BizLogicException
	 *             : BizLogicException
	 */
	private void insertUser(UserDTO userDTO, HttpSession session, AbstractActionForm form)
			throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(form.getFormId());
		final SessionDataBean sessionDataBean = (SessionDataBean) session
				.getAttribute(Constants.SESSION_DATA);
		bizLogic.insert(userDTO, sessionDataBean, 0);
	}
	/**
	 * @param userCurrent : userCurrent
	 * @param userOld : userOld
	 * @param session : session
	 * @param form : form
	 * @throws BizLogicException : BizLogicException
	 */
	private void updateUser(UserDTO userCurrent, User userOld, HttpSession session,
			AbstractActionForm form) throws BizLogicException
	{
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final IBizLogic bizLogic = factory.getBizLogic(form.getFormId());
		final SessionDataBean sessionDataBean = (SessionDataBean) session
				.getAttribute(Constants.SESSION_DATA);
		bizLogic.update(userCurrent, userOld, 0, sessionDataBean);
	}

	/**
	 * This method will add the success message into ActionMessages object
	 *
	 * @param messages
	 *            ActionMessages
	 * @param abstractDomain
	 *            AbstractDomainObject
	 * @param addoredit
	 *            String
	 * @param queryBizLogic
	 *            QueryBizLogic
	 * @param objectName
	 *            String
	 */
	private void addMessage(ActionMessages messages, AbstractDomainObject abstractDomain,
			String addoredit, QueryBizLogic queryBizLogic, String objectName)
	{
		final String message = abstractDomain.getMessageLabel();
		final Validator validator = new Validator();
		final boolean isEmpty = Validator.isEmpty(message);
		String displayName = null;
		try
		{
			displayName = queryBizLogic.getDisplayNamebyTableName(HibernateMetaData
					.getTableName(abstractDomain.getClass()));
		}
		catch (final Exception excp)
		{
			displayName = AbstractDomainObject.parseClassName(objectName);
			this.logger.error(excp.getMessage(), excp);
		}

		if (!isEmpty)
		{
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("object." + addoredit
					+ ".success", displayName, message));
		}
		else
		{
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("object." + addoredit
					+ ".successOnly", displayName));
		}
	}

	/**
	 * @return IDomainObjectFactory : IDomainObjectFactory
	 * @throws ApplicationException
	 *             : ApplicationException
	 */
	public IDomainObjectFactory getIDomainObjectFactory() throws ApplicationException
	{
		try
		{
			final IDomainObjectFactory factory = AbstractFactoryConfig.getInstance()
					.getDomainObjectFactory();
			return factory;
		}
		catch (final BizLogicException exception)
		{
			// logger.error(
			// "Failed to get QueryBizLogic object from BizLogic Factory");
			throw new ApplicationException(ErrorKey.getErrorKey("errors.item"), exception,
					"Failed to get DomainObjectFactory in base Add/Edit.");
		}
	}
}
