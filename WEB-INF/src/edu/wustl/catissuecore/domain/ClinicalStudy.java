/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author shital lawhale
 *@version 1.0
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.ClinicalStudyForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.util.logger.Logger;

/**
 * @hibernate.joined-subclass table="CATISSUE_CLINICAL_STUDY"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */

public class ClinicalStudy extends SpecimenProtocol implements java.io.Serializable
{

	/**
	 * logger Logger - Generic logger.
	 */
	private static org.apache.log4j.Logger logger = Logger.getLogger(ClinicalStudy.class);

	/**
	 * Serial Version Id of the class.
	 */
	private static final long serialVersionUID = 3426153460514363640L;

	/**
	 * Default Constructor.
	 */
	public ClinicalStudy()
	{
		super();
	}

	/**
	 * Parameterized Constructor.
	 * @param form AbstractActionForm.
	 */
	public ClinicalStudy(AbstractActionForm form)
	{
		super();
		this.setAllValues(form);
	}

	/**
	 * Collection of users associated with the ClinicalStudy.
	 */
	protected Collection coordinatorCollection = new HashSet();

	/**
	 * Collection of clinicalStudyEvent with Clinical Study.
	 */
	protected Collection clinicalStudyEventCollection = new HashSet();

	/**
	 * Collectio of clinicalStudyRegistration with Clinical Study.
	 */
	protected Collection clinicalStudyRegistrationCollection = new HashSet();

	/**
	  * @return the clinicalStudyEventCollection
	  * @hibernate.set name="clinicalStudyEventCollection" table="CATISSUE_CLINICAL_STUDY_EVENT"
	  * inverse="true" cascade="save-update" lazy="true"
	  * @hibernate.collection-key column="CLINICAL_STUDY_ID"
	  * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ClinicalStudyEvent"
	  */
	public Collection getClinicalStudyEventCollection()
	{
		return this.clinicalStudyEventCollection;
	}

	/**
	 * @param clinicalStudyEventCollection the clinicalStudyEventCollection to set
	 */
	public void setClinicalStudyEventCollection(Collection clinicalStudyEventCollection)
	{
		this.clinicalStudyEventCollection = clinicalStudyEventCollection;
	}

	/**
	 * Returns the collection of Users(ProtocolCoordinators) for this ClinicalStudy.
	 * @hibernate.set name="userCollection" table="CATISSUE_CLINICAL_STUDY_COORDINATORS"
	 * cascade="none" inverse="false" lazy="true"
	 * @hibernate.collection-key column="CLINICAL_STUDY_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.User" column="USER_ID"
	 * @return The collection of Users.
	 */
	public Collection getCoordinatorCollection()
	{
		return this.coordinatorCollection;
	}

	/**
	 * Set the Co-ordinator collection.
	 * @param coordinatorCollection Co-ordinatorCollection.
	 */
	public void setCoordinatorCollection(Collection coordinatorCollection)
	{
		this.coordinatorCollection = coordinatorCollection;
	}

	/**
	 * Returns collection of clinicalStudy registrations of this clinicalStudy.
	 * @return collection of clinicalStudy registrations of this clinicalStudy.
	 * @hibernate.set name="clinicalStudyRegistrationCollection" table="CATISSUE_CLINICAL_STUDY_REG"
	 * inverse="true" cascade="save-update" lazy="true"
	 * @hibernate.collection-key column="CLINICAL_STUDY_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ClinicalStudyRegistration"
	 * @see setCollectionProtocolRegistrationCollection(Collection)
	 */
	public Collection getClinicalStudyRegistrationCollection()
	{
		return this.clinicalStudyRegistrationCollection;
	}

	/**
	 * Sets the collection protocol registrations of this participant.
	 * @param clinicalStudyRegistrationCollection - protocolRegistrationCollection
	 * collection of collection protocol registrations of this participant.
	 * @see #getCollectionProtocolRegistrationCollection()
	 */
	public void setClinicalStudyRegistrationCollection(
			Collection clinicalStudyRegistrationCollection)
	{
		this.clinicalStudyRegistrationCollection = clinicalStudyRegistrationCollection;
	}

	/**
	 * Set all values in the form.
	 * @param abstractForm IValueObject.
	 */
	@Override
	public void setAllValues(IValueObject abstractForm)
	{
		User coordinator = null;
		try
		{
			super.setAllValues(abstractForm);

			final ClinicalStudyForm cpForm = (ClinicalStudyForm) abstractForm;

			this.coordinatorCollection.clear();
			final long[] coordinatorsArr = cpForm.getProtocolCoordinatorIds();
			if (coordinatorsArr != null)
			{
				for (final long element : coordinatorsArr)
				{
					if (element != -1)
					{
						coordinator = new User();
						coordinator.setId(Long.valueOf(element));
						this.coordinatorCollection.add(coordinator);
					}
				}
			}
		}
		catch (final Exception excp)
		{
			logger.error(excp.getMessage(), excp);
		}
	}

	/**
	 * Returns message label to display on success add or edit.
	 * @return String message label.
	 */
	@Override
	public String getMessageLabel()
	{
		return this.title;
	}
}