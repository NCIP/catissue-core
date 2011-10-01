/**
 * <p>Title: ReturnEventParameters Class>
 * <p>Description:  Attributes associated with return event of a specimen. </p>
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on Jan 18, 2007
 */

package edu.wustl.catissuecore.domain;

/**
 * Attributes associated with return event of a specimen.
 * @hibernate.joined-subclass table="CATISSUE_RETURN_EVENT_PARAM"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author Mandar Deshmukh
 */
public class ReturnEventParameters extends SpecimenEventParameters implements java.io.Serializable
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = 1234567890L;

	/**
	 * NOTE: Do not delete this constructor. Hibernet uses this by reflection API.
	 */
	public ReturnEventParameters()
	{
		super();
	}
}