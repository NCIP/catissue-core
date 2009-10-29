
package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.logger.Logger;

/**
 * An abstract base class for specimen collection group
 * and requirement group.
 * @hibernate.class table="CATISSUE_ABS_SPECI_COLL_GROUP"
 * @author abhijit_naik
 */
public abstract class AbstractSpecimenCollectionGroup extends AbstractDomainObject
		implements
			Serializable,
			IActivityStatus
{

	/**
	 * It is serial version ID for the class.
	 */
	private static final long serialVersionUID = -8771880333931001152L;

	/**
	 * logger Logger - Generic logger.
	 */
	private static Logger logger = Logger.getCommonLogger(AbstractSpecimenCollectionGroup.class);

	/**
	 * System generated unique id.
	 */
	protected Long id;
	/**
	 * Participant's clinical diagnosis at
	 * this collection event (e.g. Prostate Adenocarcinoma).
	 */
	protected String clinicalDiagnosis;
	/**
	 * The clinical status of the participant at the time of specimen collection.
	 * (e.g. New DX, pre-RX, pre-OP, post-OP, remission, relapse)
	 */
	protected String clinicalStatus;
	/**
	 * Defines whether this  record can be queried (Active)
	 * or not queried (Inactive) by any actor.
	 */
	protected String activityStatus;
	/**
	 * A physical location associated with biospecimen collection,
	 * storage, processing, or utilization.
	 */
	protected Site specimenCollectionSite;

	/**
	 * Default Constructor.
	 */
	public AbstractSpecimenCollectionGroup()
	{
		super();
	}

	/**
	 * An abstract function should be overridden by the child
	 * classes.
	 * @return name of the Specimen collection group
	 */
	public abstract String getGroupName();

	/**
	 * Get Collection Protocol Registration.
	 * @return CollectionProtocolRegistration object.
	 */
	public abstract CollectionProtocolRegistration getCollectionProtocolRegistration();

	/**
	 * An abstract function should be overridden by the child
	 * classes.
	 * @param name Specimen collection group.
	 * @throws BizLogicException bizLogicException.
	 */
	protected abstract void setGroupName(String name) throws BizLogicException;

	/**
	 * Returns the system generated unique id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_SPECIMEN_COLL_GRP_SEQ"
	 * @return the system generated unique id.
	 * @see #setId(Long)
	 */
	@Override
	public Long getId()
	{
		return this.id;
	}

	/**
	 * Set the identifer.
	 * @param identifier as long.
	 */
	@Override
	public void setId(Long identifier)
	{
		this.id = identifier;
	}

	/**
	 * Returns the participant's clinical diagnosis at
	 * this collection event (e.g. Prostate Adenocarcinoma).
	 * @hibernate.property name="clinicalDiagnosis" type="string"
	 * column="CLINICAL_DIAGNOSIS" length="150"
	 * @return the participant's clinical diagnosis at
	 * this collection event (e.g. Prostate Adenocarcinoma).
	 * @see #setClinicalDiagnosis(String)
	 */
	public String getClinicalDiagnosis()
	{
		return this.clinicalDiagnosis;
	}

	/**
	 * Sets the participant's clinical diagnosis at
	 * this collection event (e.g. Prostate Adenocarcinoma).
	 * @param clinicalDiagnosis the participant's clinical diagnosis at
	 * this collection event (e.g. Prostate Adenocarcinoma).
	 * @see #getClinicalDiagnosis()
	 */
	public void setClinicalDiagnosis(String clinicalDiagnosis)
	{
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	/**
	 * Returns the clinical status of the participant at the time of specimen collection.
	 * (e.g. New DX, pre-RX, pre-OP, post-OP, remission, relapse)
	 * @hibernate.property name="clinicalStatus" type="string"
	 * column="CLINICAL_STATUS" length="50"
	 * @return clinical status of the participant at the time of specimen collection.
	 * @see #setClinicalStatus(String)
	 */
	public String getClinicalStatus()
	{
		return this.clinicalStatus;
	}

	/**
	 * Sets the clinical status of the participant at the time of specimen collection.
	 * (e.g. New DX, pre-RX, pre-OP, post-OP, remission, relapse)
	 * @param clinicalStatus the clinical status of the participant at the time of specimen collection.
	 * @see #getClinicalStatus()
	 */
	public void setClinicalStatus(String clinicalStatus)
	{
		this.clinicalStatus = clinicalStatus;
	}

	/**
	 * Returns whether this  record can be queried (Active)
	 * or not queried (Inactive) by any actor.
	 * @hibernate.property name="activityStatus" type="string"
	 * column="ACTIVITY_STATUS" length="50"
	 * @return Active if this record can be queried else returns InActive.
	 * @see #setActivityStatus(String)
	 */
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * Sets whether this  record can be queried (Active)
	 * or not queried (Inactive) by any actor.
	 * @param activityStatus Active if this record can be queried else returns InActive.
	 * @see #getActivityStatus()
	 */
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	 * Returns the physical location associated with biospecimen collection,
	 * storage, processing, or utilization.
	 * @hibernate.many-to-one column="SITE_ID"
	 * class="edu.wustl.catissuecore.domain.Site" constrained="true"
	 * @return the physical location associated with biospecimen collection,
	 * storage, processing, or utilization.
	 * @see #setSpecimenCollectionSite(Site)
	 */
	public Site getSpecimenCollectionSite()
	{
		return this.specimenCollectionSite;
	}

	/**
	 * Sets the physical location associated with biospecimen collection,
	 * storage, processing, or utilization.
	 * @param specimenCollectionSite Site physical location associated with
	 * biospecimen collection, storage, processing, or utilization.
	 * @see #getSpecimenCollectionSite()
	 */
	public void setSpecimenCollectionSite(Site specimenCollectionSite)
	{
		this.specimenCollectionSite = specimenCollectionSite;
	}

	/**
	 * Method overidden from its extended class.
	 * @param valueObject IValueObject.
	 * @throws AssignDataException assignDataException.
	 */
	@Override
	public void setAllValues(IValueObject valueObject) throws AssignDataException
	{
		final AbstractActionForm abstractForm = (AbstractActionForm) valueObject;
		final SpecimenCollectionGroupForm form = (SpecimenCollectionGroupForm) abstractForm;
		this.setClinicalDiagnosis(form.getClinicalDiagnosis());
		this.setClinicalStatus(form.getClinicalStatus());
		this.setActivityStatus(form.getActivityStatus());
		this.specimenCollectionSite = new Site();
		this.specimenCollectionSite.setId(Long.valueOf(form.getSiteId()));

	}
}