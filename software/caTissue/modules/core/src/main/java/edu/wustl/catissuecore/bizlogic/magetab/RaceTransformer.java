/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.bizlogic.magetab;

import java.util.Collection;

import edu.wustl.catissuecore.domain.Race;
import edu.wustl.catissuecore.domain.Specimen;

public class RaceTransformer extends AbstractCharacteristicTransformer {

	public RaceTransformer(CharacteristicTransformerConfig characteristicTransformerConfig) {
		super("Race", "Race", "Race",characteristicTransformerConfig);
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
	
	@Override
	public boolean isMageTabSpec() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
