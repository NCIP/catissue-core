
package edu.wustl.catissuecore.domain.pathology;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * Represents the contents of surgical pathology reports.
 * @hibernate.class table="CATISSUE_REPORT_CONTENT"
 */
public class ReportContent extends AbstractDomainObject
{

	/**
	 *
	 */
	private static final long serialVersionUID = -7892552429390772191L;

	/**
	 * Data content of surgical pathology report.
	 */
	protected String data;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * @return system generated id
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 *               unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence"
	 *                            value="CATISSUE_REPORT_CONTENT_SEQ"
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * @param identifier
	 *            sets system generated id
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * @return report content data.
	 * @hibernate.property name="data" type="String" column="REPORT_DATA"
	 *                     length="4000"
	 */
	public String getData()
	{
		return this.data;
	}

	/**
	 * @param data
	 *            Report Data to set.
	 */
	public void setData(String data)
	{
		this.data = data;
	}

	/**
	 * Constructor.
	 */
	public ReportContent()
	{
		super();
	}

	/**
	 * @param arg0 : arg0
	 * @throws AssignDataException : AssignDataException
	 */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @param valueObject : valueObject
	 * @see
	 * edu.wustl.common.domain.AbstractDomainObject#setAllValues(edu.wustl.common
	 * .actionForm.IValueObject)
	 * @throws AssignDataException : AssignDataException
	 */
	@Override
	public void setAllValues(IValueObject valueObject) throws AssignDataException
	{
		// TODO Auto-generated method stub

	}

}