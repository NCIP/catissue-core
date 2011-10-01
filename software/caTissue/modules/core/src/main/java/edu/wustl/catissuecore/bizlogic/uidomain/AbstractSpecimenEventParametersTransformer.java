package edu.wustl.catissuecore.bizlogic.uidomain;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import edu.wustl.catissuecore.actionForm.EventParametersForm;
import edu.wustl.catissuecore.actionForm.SpecimenEventParametersForm;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenEventParameters;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.common.bizlogic.UIDomainTransformer;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.global.CommonUtilities;

public abstract class AbstractSpecimenEventParametersTransformer<U extends SpecimenEventParametersForm, D extends SpecimenEventParameters>
        implements UIDomainTransformer<U, D> {

    public void overwriteDomainObject(D domainObject, U uiRepOfDomain) {
        if (SearchUtil.isNullobject(domainObject.getUser())) {
        	InstanceFactory<User> instFact = DomainInstanceFactory.getInstanceFactory(User.class);
            domainObject.setUser(instFact.createObject());
        }
        if (SearchUtil.isNullobject(domainObject.getTimestamp())) {
            domainObject.setTimestamp(Calendar.getInstance().getTime());
        }
        domainObject.setComment(uiRepOfDomain.getComments());
        domainObject.getUser().setId(Long.valueOf(uiRepOfDomain.getUserId()));
        if (uiRepOfDomain.getDateOfEvent() != null && uiRepOfDomain.getDateOfEvent().trim().length() != 0) {
            setDateTimeFromCalender(domainObject, uiRepOfDomain);
        }
        if (uiRepOfDomain.isAddOperation()) {
            domainObject.setSpecimen(new Specimen());
        }
        // logger.debug("uiRepOfDomain.getSpecimenId()" +
        // "............................." + uiRepOfDomain.getSpecimenId());
        if (domainObject.getSpecimen() != null) {
            domainObject.getSpecimen().setId(Long.valueOf(uiRepOfDomain.getSpecimenId()));
        }
    }

    /**
     * @param uiRepOfDomain EventParametersForm.
     * @throws AssignDataException AssignDataException.
     */
    private void setDateTimeFromCalender(D domainObject, EventParametersForm uiRepOfDomain) {
        final Calendar calendar = Calendar.getInstance();
        Date date;
        try {
            date = CommonUtilities.parseDate(uiRepOfDomain.getDateOfEvent(), CommonUtilities.datePattern(uiRepOfDomain
                    .getDateOfEvent()));
            calendar.setTime(date);
            domainObject.setTimestamp(calendar.getTime());
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(uiRepOfDomain.getTimeInHours()));
            calendar.set(Calendar.MINUTE, Integer.parseInt(uiRepOfDomain.getTimeInMinutes()));
            domainObject.setTimestamp(calendar.getTime());
        } catch (final ParseException excp) {
            // TODO
            // SpecimenEventParameters.logger.error(excp.getMessage(), excp);
            excp.printStackTrace();
            // final ErrorKey errorKey =
            // ErrorKey.getErrorKey("assign.data.error");
            // throw new AssignDataException(errorKey, null,
            // "SpecimenEventParameters.java :");
        }
    }
}
