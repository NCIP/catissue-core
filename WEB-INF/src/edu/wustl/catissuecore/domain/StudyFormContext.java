/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.common.dynamicextensions.domain.integration.AbstractFormContext;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;

/**
 *
 * @author shital_lawhale
 * @hibernate.class table="CATISSUE_STUDY_FORM_CONTEXT"
 */
public class StudyFormContext extends AbstractFormContext
{

	private static final long serialVersionUID = 1L;

	protected Integer noOfEntries;

	protected Collection<CollectionProtocol> collectionProtocolCollection = new HashSet<CollectionProtocol>();

	public Collection<CollectionProtocol> getCollectionProtocolCollection()
	{
		return collectionProtocolCollection;
	}

	public void setCollectionProtocolCollection(
			Collection<CollectionProtocol> collectionProtocolCollection)
	{
		this.collectionProtocolCollection = collectionProtocolCollection;
	}

	public Integer getNoOfEntries()
	{
		return noOfEntries;
	}

	public void setNoOfEntries(Integer noOfEntries)
	{
		this.noOfEntries = noOfEntries;
	}

	/**
	 *
	 */
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{

	}

}
