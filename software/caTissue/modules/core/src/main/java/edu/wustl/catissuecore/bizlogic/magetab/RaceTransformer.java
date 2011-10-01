package edu.wustl.catissuecore.bizlogic.magetab;

import java.util.Collection;

import uk.ac.ebi.arrayexpress2.magetab.datamodel.sdrf.node.AbstractSDRFNode;
import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Specimen;

public class RaceTransformer extends AbstractCharacteristicTransformer {

	public RaceTransformer() {
		super("Race", "Race", "Race");
	}

	@Override
	public String getCharacteristicValue(Specimen specimen) {
		Collection<Race> raceCollection = 
			specimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getParticipant().getRaceCollection();
		
		String value = "";
		for (Race race : raceCollection) {
			if (value.isEmpty()) {
				value = race.getRaceName();
			} else {
				value = value + ", " + race.getRaceName();
			}
		}
		
		return value;
	}
	
}
