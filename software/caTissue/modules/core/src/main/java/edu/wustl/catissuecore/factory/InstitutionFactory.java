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

import edu.wustl.catissuecore.domain.Institution;

public class InstitutionFactory implements InstanceFactory<Institution>
{

	private static InstitutionFactory institutionFactory;

	private InstitutionFactory()
	{
		super();
	}

	public static synchronized InstitutionFactory getInstance()
	{
		if(institutionFactory==null){
			institutionFactory = new InstitutionFactory();
		}
		return institutionFactory;
	}

	public Institution createClone(Institution obj)
	{
		Institution institution = createObject();
		institution.setName(obj.getName());
		return institution;
	}

	public Institution createObject()
	{
		Institution institution = new Institution();
		initDefaultValues(institution);
		return institution;
	}

	public void initDefaultValues(Institution obj)
	{

	}
}
