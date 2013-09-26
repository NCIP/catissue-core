/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.Race;


public class RaceFactory implements InstanceFactory<Race>
{
	private static RaceFactory raceFactory;

	private RaceFactory() {
		super();
	}

	public static synchronized RaceFactory getInstance() {
		if(raceFactory == null) {
			raceFactory = new RaceFactory();
		}
		return raceFactory;
	}

	public Race createClone(Race t)
	{
		Race race=createObject();
		race.setId(Long.valueOf(t.getId()));
		race.setRaceName(t.getRaceName());
		return race;
	}

	public Race createObject()
	{
		Race race=new Race();
		initDefaultValues(race);
		return race;
	}

	public void initDefaultValues(Race t){}

}
