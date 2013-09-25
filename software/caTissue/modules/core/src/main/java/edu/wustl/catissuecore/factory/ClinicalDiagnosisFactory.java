/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.ClinicalDiagnosis;

public class ClinicalDiagnosisFactory implements InstanceFactory<ClinicalDiagnosis>
{
	private static ClinicalDiagnosisFactory clinicalDiagnosisFactory;

	private ClinicalDiagnosisFactory()
	{
		super();
	}

	public static synchronized ClinicalDiagnosisFactory getInstance()
	{
		if(clinicalDiagnosisFactory==null){
			clinicalDiagnosisFactory = new ClinicalDiagnosisFactory();
		}
		return clinicalDiagnosisFactory;
	}

	public ClinicalDiagnosis createClone(ClinicalDiagnosis obj)
	{
		ClinicalDiagnosis clinicalDiagnosis = createObject();
		clinicalDiagnosis.setName(obj.getName());
		clinicalDiagnosis.setCollectionProtocol(obj.getCollectionProtocol());
		return clinicalDiagnosis;
	}


	public ClinicalDiagnosis createObject()
	{
		ClinicalDiagnosis clinicalDiagnosis = new ClinicalDiagnosis();
		initDefaultValues(clinicalDiagnosis);
		return clinicalDiagnosis;
	}

	public void initDefaultValues(ClinicalDiagnosis obj)
	{

	}


}
