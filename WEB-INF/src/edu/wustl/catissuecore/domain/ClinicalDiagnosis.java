package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
/**
 * @author kalpana_thakur
 */
public class ClinicalDiagnosis extends AbstractDomainObject implements Serializable, Comparable
{

	/**
	 * Serial id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * System generated unique id.
	 */
	private Long id;
	/**
	 * Clinical Diagnosis value.
	 */
	private String clinicalDiagnosis;

	/**
	 * Collection Protocol instance.
	 */
	private CollectionProtocol collectionProtocol;

	/**
	 * The collection protocol.
	 * @return the collection protocol.
	 */
	public CollectionProtocol getCollectionProtocol()
	{
		return collectionProtocol;
	}

	/**
	 * Set the collection protocol.
	 * @param collectionProtocol collectionProtocol
	 */
	public void setCollectionProtocol(CollectionProtocol collectionProtocol)
	{
		this.collectionProtocol = collectionProtocol;
	}

	/**
	 * This method is called to get the clinical Diagnosis.
	 * @return clinicalDiagnosis clinicalDiagnosis.
	 */
	public String getClinicalDiagnosis()
	{
		return clinicalDiagnosis;
	}

	/**
	 * set clinicalDiagnosis.
	 * @param clinicalDiagnosis clinicalDiagnosis
	 */
	public void setClinicalDiagnosis(String clinicalDiagnosis)
	{
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

    /**
     * This method called to get the Identifier.
     * @return id identifier.
     */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param clinDiagId clinical Diagnosis Identifier.
	 * set identifier.
	 */
	public void setId(Long clinDiagId)
	{
		id = clinDiagId;
	}



	/**
	 * Set all values.
	 * @param arg0  IValueObject
	 * @throws AssignDataException AssignDataException
	 */
	public void setAllValues(IValueObject arg0) throws AssignDataException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * To compare objects.
	 * @param object object.
	 * @return object.
	 */
	public int compareTo(Object object)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
