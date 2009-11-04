package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

public class ClinicalDiagnosis extends AbstractDomainObject implements Serializable, Comparable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * System generated unique id.
	 */
	protected Long id;
	
	/**
	 * Clinical Diagnosis value.
	 */
	protected String clinicalDiagnosis;
	
	/**
	 * Collection Protocol instance.
	 */
	protected CollectionProtocol collectionProtocol;
	
	/**
	 * The collection protocol.
	 * @return the collection protocol.
	 */
	public CollectionProtocol getCollectionProtocol()
	{
		return collectionProtocol;
	}

	/**
	 * Set the collection protocol
	 * @param collectionProtocol
	 */
	public void setCollectionProtocol(CollectionProtocol collectionProtocol)
	{
		this.collectionProtocol = collectionProtocol;
	}

	/**
	 * 
	 * @return clinicalDiagnosis.
	 */
	public String getClinicalDiagnosis()
	{
		return clinicalDiagnosis;
	}

	/**
	 * set clinicalDiagnosis.
	 * @param clinicalDiagnosis
	 */
	public void setClinicalDiagnosis(String clinicalDiagnosis)
	{
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	
	
    /**
     * @return id. 
     */
	public Long getId()
	{
		
		return id;
	}

	/**
	 * set identifier.
	 */
	public void setId(Long clinDiagId)
	{
		id = clinDiagId;
	}
	
	

	@Override
	public void setAllValues(IValueObject arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
