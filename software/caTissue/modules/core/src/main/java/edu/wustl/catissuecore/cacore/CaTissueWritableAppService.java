package edu.wustl.catissuecore.cacore;

import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.WritableApplicationService;
import gov.nih.nci.system.query.SDKQuery;
import gov.nih.nci.system.query.SDKQueryResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CaTissueWritableAppService extends WritableApplicationService {

	String getCaTissueServerProperty(String key) throws ApplicationException;
	String getVisitInformationURL(Object urlInformationObject) throws ApplicationException;
	String getSpecimenCollectionGroupURL(Object urlInformationObject) throws ApplicationException;
	String getSpecimenCollectionGroupLabel(Object scg) throws ApplicationException;
	Object registerParticipant(Object object, Long cpid) throws ApplicationException;
	Object getVisitRelatedEncounteredDate(Map<String, Long> map) throws ApplicationException;
	List<Object> getCaTissueLocalParticipantMatchingObects(Object object, Set<Long> cpIdSet) throws ApplicationException;
	List<Object> getParticipantMatchingObects(Object object) throws ApplicationException;
    public SDKQueryResult executeQuery(SDKQuery query, String gridId) throws ApplicationException;
}
