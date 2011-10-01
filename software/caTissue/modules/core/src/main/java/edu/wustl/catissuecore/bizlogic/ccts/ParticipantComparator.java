/**
 * 
 */
package edu.wustl.catissuecore.bizlogic.ccts;

import java.util.ArrayList;
import java.util.List;

import edu.wustl.catissuecore.domain.Participant;
import edu.wustl.common.util.logger.Logger;

/**
 * @author Denis G. Krylov
 * 
 */
public class ParticipantComparator extends BaseComparator implements
		IDomainObjectComparator<Participant> {
	private static final Logger logger = Logger
			.getCommonLogger(ParticipantComparator.class);
	private static final List<Field> fields = new ArrayList<Field>();
	static {
		fields.add(new Field("First Name", "firstName"));
		fields.add(new Field("Last Name", "lastName"));
		fields.add(new Field("Middle Name", "middleName"));
		fields.add(new Field("Birth Date", "birthDate"));
		fields.add(new Field("Death Date", "deathDate"));
		fields.add(new Field("Ethnicity", "ethnicity"));
		fields.add(new Field("Gender", "gender"));
		fields.add(new Field("Race", "raceCollection"));
		fields.add(new Field("Activity Status", "activityStatus"));
		fields.add(new Field("Vital Status", "vitalStatus"));
		fields.add(new Field("MRN", "participantMedicalIdentifierCollection"));
	}

	@Override
	public boolean supports(Class<Participant> cls) {
		return Participant.class.equals(cls);
	}

	@Override
	public List<IDomainObjectComparisonResult> compare(Participant oldObj,
			Participant newObj) {
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
