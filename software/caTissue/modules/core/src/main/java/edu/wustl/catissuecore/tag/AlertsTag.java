package edu.wustl.catissuecore.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.context.support.WebApplicationContextUtils;

import edu.wustl.catissuecore.bizlogic.ccts.ICctsIntegrationBizLogic;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

/**
 * Populates a {@link Collection} of alerts: items that require administrator's
 * attention. Makes the collection available as a scoped variable (request
 * scope).
 * 
 * @author Denis G. Krylov
 * 
 */
public class AlertsTag extends SimpleTagSupport {

	private static final Logger logger = Logger
			.getCommonLogger(AlertsTag.class);

	private String var = "alerts";

	@Override
	public void doTag() throws JspException, IOException {
		try {
			final PageContext jspCtx = (PageContext) getJspContext();
			ApplicationContext appCtx = WebApplicationContextUtils
					.getRequiredWebApplicationContext(jspCtx
							.getServletContext());
			Collection<String> alerts = new ArrayList<String>();
			final SessionDataBean sessionDataBean = (SessionDataBean) jspCtx
					.getSession().getAttribute(Constants.SESSION_DATA);

			if (sessionDataBean != null
					&& isAuthorized(sessionDataBean, appCtx)) {
				populateAlerts(alerts, appCtx);
			}

			jspCtx.setAttribute(var, alerts, PageContext.REQUEST_SCOPE);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
	}

	/**
	 * @param sessionDataBean
	 * @param appCtx
	 * @return
	 * @throws BizLogicException 
	 * @throws BeansException 
	 */
	private boolean isAuthorized(final SessionDataBean sessionDataBean,
			ApplicationContext appCtx) throws BeansException, BizLogicException {
		return sessionDataBean.isAdmin()
				|| ((ICctsIntegrationBizLogic) appCtx
						.getBean("cctsIntegrationBizLogic"))
						.isAuthorized(sessionDataBean);
	}

	private void populateAlerts(Collection<String> alerts,
			ApplicationContext ctx) {
		populateDataQueueAlerts(alerts, ctx);
	}

	/**
	 * @param alerts
	 * @param ctx
	 * @throws BeansException
	 * @throws NoSuchMessageException
	 */
	private void populateDataQueueAlerts(Collection<String> alerts,
			ApplicationContext ctx) throws BeansException,
			NoSuchMessageException {
		ICctsIntegrationBizLogic bizLogic = (ICctsIntegrationBizLogic) ctx
				.getBean("cctsIntegrationBizLogic");
		MessageSource messageSource = (MessageSource) ctx
				.getBean("messageSource");
		int count = bizLogic.getPendingDataQueueItemsCount();
		if (count > 0) {
			String msg;
			if (count == 1) {
				msg = messageSource.getMessage("Alerts.msg.dataQueue.singular",
						new Object[0], Locale.getDefault());
			} else {
				msg = messageSource.getMessage("Alerts.msg.dataQueue.plural",
						new Object[] { count }, Locale.getDefault());
			}
			alerts.add(msg);
		}
	}

	/**
	 * @return the var
	 */
	public final String getVar() {
		return var;
	}

	/**
	 * @param var
	 *            the var to set
	 */
	public final void setVar(String var) {
		this.var = var;
	}

}
