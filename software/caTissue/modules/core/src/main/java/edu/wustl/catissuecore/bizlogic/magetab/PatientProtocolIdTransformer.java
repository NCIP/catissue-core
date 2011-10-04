package edu.wustl.catissuecore.bizlogic.magetab;

import edu.wustl.catissuecore.domain.Specimen;

public class PatientProtocolIdTransformer extends AbstractCharacteristicTransformer {

	public PatientProtocolIdTransformer() {
		super("Protocol Participant ID", "Protocol Participant ID", "Protocol Participant ID");
	}
	
	@Override
	public String getCharacteristicValue(Specimen specimen) {
		// TODO Auto-generated method stub
		return specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getProtocolParticipantIdentifier();
	}
	
	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return false;
	}

}
