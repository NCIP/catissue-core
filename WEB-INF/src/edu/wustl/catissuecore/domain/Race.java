
package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
  * @author sagar_baldwa
 */
public class Race extends AbstractDomainObject implements java.io.Serializable
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated Identfier.
	 */
	protected Long id;

	/**
	 * raceName.
	 */
	protected String raceName;

	/**
	 * participant.
	 */
	protected Participant participant;

	/**
	 * Default Constructor.
	 */
	public Race()
	{
		super();
	}

	/**
	 * Copy Constructor.
	 * @param race Race Object
	 */
	public Race(Race race)
	{
		super();
		this.id = Long.valueOf(race.getId().longValue());
		this.raceName = race.getRaceName();
	}

	/**
	 * Get Identifier.
	 * @return Long.
	 */
	@Override
	public Long getId()
	{
		// TODO Auto-generated method stub
		return this.id;
	}

	/**
	 * Set All Values.
	 * @param arg0 IValueObject.
	 * @throws AssignDataException AssignDataException.
	 */
	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Set identifier.
	 * @param identifier Long.
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;

	}

	/**
	 * Get RaceName.
	 * @return String.
	 */
	public String getRaceName()
	{
		return this.raceName;
	}

	/**
	 * Set RaceName.
	 * @param raceName String.
	 */
	public void setRaceName(String raceName)
	{
		this.raceName = raceName;
	}

	/**
	 * Get Participant.
	 * @return Participant.
	 */
	public Participant getParticipant()
	{
		return this.participant;
	}

	/**
	 * Set Participant.
	 * @param participant Participant.
	 */
	public void setParticipant(Participant participant)
	{
		this.participant = participant;
	}
}