package edu.wustl.catissuecore.domain.pathology;

import edu.wustl.catissuecore.domain.EventParameters;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;

/**
 * @version 1.0
 * @created 26-Sep-2006 4:07:06 PM
 * Represents clinical procedure parameters
 */
public class ClinicalProcedureEventParameterSet extends EventParameters
{

	/**
	 * procedure name
	 */
	protected String procedureName;
	
	/**
	 * Specimen collection group 
	 */
	protected SpecimenCollectionGroup specimenCollectionGroup;

	/**
	 * Constructor
	 */
	public ClinicalProcedureEventParameterSet()
	{

	}

	

}