package edu.wustl.catissuecore.bizlogic.uidomain;

import java.util.Date;

import edu.wustl.catissuecore.actionForm.ReportedProblemForm;
import edu.wustl.catissuecore.domain.ReportedProblem;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.bizlogic.InputUIRepOfDomain;
import edu.wustl.common.bizlogic.UIDomainTransformer;

@InputUIRepOfDomain(ReportedProblemForm.class)
public class ReportedProblemTransformer implements UIDomainTransformer<ReportedProblemForm, ReportedProblem> {

    public ReportedProblem createDomainObject(ReportedProblemForm uiRepOfDomain) {
    	InstanceFactory<ReportedProblem> instFact = DomainInstanceFactory.getInstanceFactory(ReportedProblem.class);
        ReportedProblem reportedProblem =instFact.createObject();// new ReportedProblem();
        overwriteDomainObject(reportedProblem, uiRepOfDomain);
        return reportedProblem;
    }

    public void overwriteDomainObject(ReportedProblem domainObject, ReportedProblemForm uiRepOfDomain) {
        // Change for API Search --- Ashwin 04/10/2006
        if (SearchUtil.isNullobject(domainObject.getReportedDate())) {
            domainObject.setReportedDate(new Date());
        }
        domainObject.setId(Long.valueOf(uiRepOfDomain.getId()));
        domainObject.setSubject(uiRepOfDomain.getSubject());
        domainObject.setFrom(uiRepOfDomain.getFrom());
        domainObject.setMessageBody(uiRepOfDomain.getMessageBody());
        domainObject.setActivityStatus(uiRepOfDomain.getActivityStatus());
        domainObject.setComments(uiRepOfDomain.getComments());
        domainObject.setNameOfReporter(uiRepOfDomain.getNameOfReporter());
        domainObject.setAffiliation(uiRepOfDomain.getAffiliation());
    }

}
