package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.Date;

import edu.wustl.catissuecore.actionForm.ViewSurgicalPathologyReportForm;
import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.PathologyReportReviewParameter;
import edu.wustl.catissuecore.domain.pathology.QuarantineEventParameter;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.domain.AbstractDomainObject;

// TODO
/*
 * Domain object is PathologyReportReviewParameter or QuarantineEventParameter
 * depending on formId = Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID or formId =
 * Constants.PATHOLOGY_REPORT_REVIEW_FORM_ID
 */
/*
 * the two domain objects don't share an interface, hence the ugly
 * AbstractDomainObject here.
 */
@InputUIRepOfDomain(ViewSurgicalPathologyReportForm.class)
public class ViewSurgicalPathologyReportTransformer
implements
UIDomainTransformer<ViewSurgicalPathologyReportForm, AbstractDomainObject> {

	public AbstractDomainObject createDomainObject(ViewSurgicalPathologyReportForm form) {
		AbstractDomainObject domainObj;
		if (form.getSubmittedFor().equalsIgnoreCase(Constants.QUARANTINE)) {
			InstanceFactory<QuarantineEventParameter> instFact = DomainInstanceFactory.getInstanceFactory(QuarantineEventParameter.class);
			domainObj = instFact.createObject();//new QuarantineEventParameter();
		} else
		{
			InstanceFactory<PathologyReportReviewParameter> instFact = DomainInstanceFactory.getInstanceFactory(PathologyReportReviewParameter.class);
			domainObj = instFact.createObject();//new PathologyReportReviewParameter();
		}

		overwriteDomainObject(domainObj, form);
		return domainObj;
	}

	public void overwriteDomainObject(AbstractDomainObject domainObject1, ViewSurgicalPathologyReportForm form) {
		if (domainObject1 instanceof PathologyReportReviewParameter) {
			PathologyReportReviewParameter domainObject = (PathologyReportReviewParameter) domainObject1;
			domainObject.setComment(form.getComments());
			domainObject.setTimestamp(new Date());
			domainObject.setStatus(Constants.COMMENT_STATUS_RENDING);

			if (!form.getIdentifiedReportId().equals("")) {
				InstanceFactory<IdentifiedSurgicalPathologyReport> instFact = DomainInstanceFactory.getInstanceFactory(IdentifiedSurgicalPathologyReport.class);
				final IdentifiedSurgicalPathologyReport report = instFact.createObject();//new IdentifiedSurgicalPathologyReport();
				report.setId(Long.valueOf(form.getIdentifiedReportId()));
				//domainObject.setSurgicalPathologyReport(report);

			} else if (form.getDeIdentifiedReportId() != 0) {
				InstanceFactory<DeidentifiedSurgicalPathologyReport> instFact = DomainInstanceFactory.getInstanceFactory(DeidentifiedSurgicalPathologyReport.class);
				final DeidentifiedSurgicalPathologyReport deidReport = instFact.createObject();//new DeidentifiedSurgicalPathologyReport();
				deidReport.setId(Long.valueOf(form.getDeIdentifiedReportId()));
				//domainObject.setSurgicalPathologyReport(deidReport);
			}
		} else if (domainObject1 instanceof QuarantineEventParameter) {
			QuarantineEventParameter domainObject = (QuarantineEventParameter) domainObject1;
			domainObject.setComment(form.getComments());
			domainObject.setTimestamp(new Date());
			domainObject.setStatus(Constants.COMMENT_STATUS_RENDING);
			if (form.getAcceptReject() == 1) {
				domainObject.setStatus(Constants.COMMENT_STATUS_QUARANTINED);
			}

			if (form.getAcceptReject() == 2) {
				domainObject.setStatus(Constants.COMMENT_STATUS_NOT_QUARANTINED);
			}
			if (form.getDeIdentifiedReportId() != 0) {
				InstanceFactory<DeidentifiedSurgicalPathologyReport> instFact = DomainInstanceFactory.getInstanceFactory(DeidentifiedSurgicalPathologyReport.class);
				final DeidentifiedSurgicalPathologyReport deidReport = instFact.createObject();//new DeidentifiedSurgicalPathologyReport();
				deidReport.setId(Long.valueOf(form.getDeIdentifiedReportId()));
				domainObject.setDeIdentifiedSurgicalPathologyReport(deidReport);
				domainObject.setQuarantineStatus(false);
			}
		} else
			throw new IllegalArgumentException();
	}

}
