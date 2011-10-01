package edu.wustl.catissuecore.domain;

import edu.wustl.catissuecore.util.IStringIdentifiable;
import edu.wustl.common.participant.domain.IRace;
import edu.wustl.common.domain.AbstractDomainObject;

import java.io.Serializable;
/**
	*
	**/

public class Race extends AbstractDomainObject implements Serializable, IRace<Participant>, IStringIdentifiable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;


	/**
	* System generated Identfier.
	**/

	private Long id;
	/**
	* Retrieves the value of the id attribute
	* @return id
	**/

	public Long getId(){
		return id;
	}

	/**
	* Sets the value of id attribute
	**/

	public void setId(Long id){
		this.id = id;
	}

	/**
	* raceName.
	**/

	private String raceName;
	/**
	* Retrieves the value of the raceName attribute
	* @return raceName
	**/

	public String getRaceName(){
		return raceName;
	}

	/**
	* Sets the value of raceName attribute
	**/

	public void setRaceName(String raceName){
		this.raceName = raceName;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.Participant object
	**/

	private Participant participant;
	/**
	* Retrieves the value of the participant attribute
	* @return participant
	**/

	public Participant getParticipant(){
		return participant;
	}
	/**
	* Sets the value of participant attribute
	**/

	public void setParticipant(Participant participant){
		this.participant = participant;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof Race)
		{
			Race c =(Race)obj;
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}

	@Override
	public String getAsString() {
		return getRaceName();
	}

	@Override
	public int compareTo(IStringIdentifiable o) {
		return (getAsString()+"").compareTo(o.getAsString()+"");
	}

}