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

import edu.wustl.catissuecore.domain.SpecimenArrayContent;


public class SpecimenArrayContentFactory implements InstanceFactory<SpecimenArrayContent>
{

	private static SpecimenArrayContentFactory spArrayContentFactory;

	private SpecimenArrayContentFactory() {
		super();
	}

	public static synchronized SpecimenArrayContentFactory getInstance() {
		if(spArrayContentFactory == null) {
			spArrayContentFactory = new SpecimenArrayContentFactory();
		}
		return spArrayContentFactory;
	}

	public SpecimenArrayContent createClone(SpecimenArrayContent obj)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public SpecimenArrayContent createObject()
	{
		SpecimenArrayContent spArrayContent=new SpecimenArrayContent();
		initDefaultValues(spArrayContent);
		return spArrayContent;
	}

	public void initDefaultValues(SpecimenArrayContent obj)
	{}

}
