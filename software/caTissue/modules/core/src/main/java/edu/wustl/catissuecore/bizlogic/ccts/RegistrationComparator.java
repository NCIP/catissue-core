/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Denis G. Krylov
 * 
 */
public class RegistrationComparator extends BaseComparator implements
		IDomainObjectComparator<CollectionProtocolRegistration> {
	private static final Logger logger = Logger
			.getCommonLogger(RegistrationComparator.class);
	private static final List<Field> fields = new ArrayList<Field>();
	static {
		fields.add(new Field("Collection Protocol",
				"collectionProtocol.shortTitle"));
		fields.add(new Field("Participant Protocol ID",
				"protocolParticipantIdentifier"));
		fields.add(new Field("Registration Date", "registrationDate"));
		fields.add(new Field("Activity Status", "activityStatus"));
	}

	@Override
	public boolean supports(Class<CollectionProtocolRegistration> cls) {
		return CollectionProtocolRegistration.class.equals(cls);
	}

	@Override
	public List<IDomainObjectComparisonResult> compare(CollectionProtocolRegistration oldObj,
			CollectionProtocolRegistration newObj) {
		List<IDomainObjectComparisonResult> results = new ArrayList<IDomainObjectComparisonResult>();
		compare(oldObj, newObj, results);
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.wustl.catissuecore.bizlogic.ccts.BaseComparator#getFields()
	 */
	@Override
	public List<Field> getFields() {
		return fields;
	}

}
