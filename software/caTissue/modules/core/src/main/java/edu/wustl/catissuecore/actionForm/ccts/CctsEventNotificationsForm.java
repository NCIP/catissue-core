package edu.wustl.catissuecore.actionForm.ccts;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.util.XMLPropertyHandler;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Denis G. Krylov
 * 
 */
public class CctsEventNotificationsForm extends AbstractActionForm {

	private static final long serialVersionUID = 1L;

	private int pageNum;
	private int numResultsPerPage;

	/**
	 * logger Logger - Generic logger.
	 */
	private static final Logger logger = Logger
			.getCommonLogger(CctsEventNotificationsForm.class);

	/**
	 * Resets all the fields.
	 */
	@Override
	protected void reset() {
		pageNum = 0;
		numResultsPerPage = Integer
				.parseInt(XMLPropertyHandler
						.getValue(edu.wustl.common.util.global.Constants.RECORDS_PER_PAGE_PROPERTY_NAME));
	}

	/**
	 * Returns the form id.
	 * 
	 * @return the form id.
	 * @see AbstractActionForm#getFormId()
	 */
	@Override
	public int getFormId() {
		return Constants.EVENT_NOTIFICATIONS_FORM_ID;
	}

	/**
	 * Overrides the validate method of ActionForm.
	 * 
	 * @return error ActionErrors instance
	 * @param mapping
	 *            Actionmapping instance
	 * @param request
	 *            HttpServletRequest instance
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		final ActionErrors errors = new ActionErrors();
		return errors;
	}

	@Override
	public void setAddNewObjectIdentifier(String arg0, Long arg1) {
	}

	public void setAllValues(AbstractDomainObject abstractDomain) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the pageNum
	 */
	public final int getPageNum() {
		return pageNum;
	}

	/**
	 * @param pageNum
	 *            the pageNum to set
	 */
	public final void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	/**
	 * @return the numResultsPerPage
	 */
	public final int getNumResultsPerPage() {
		return numResultsPerPage;
	}

	/**
	 * @param numResultsPerPage the numResultsPerPage to set
	 */
	public final void setNumResultsPerPage(int numResultsPerPage) {
		this.numResultsPerPage = numResultsPerPage;
	}

}