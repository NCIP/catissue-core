/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;

/**
 * This interface is for getting all Specimen Type Array.
 * @author virender_mehta
 * @created-on Dec 31, 2009
 */
public interface ISpecimenTypeDomain
{
	/**
	 * @param holdsSpecimenClassCollection The holdsSpecimenClassCollection to set.
	 */
	void setHoldsSpecimenClassCollection(Collection<String> holdsSpecimenClassCollection);
	/**
	 * Holds SpecimenType Collection.
	 * @param holdsSpecimenTypeCollection Specimen Type collection
	 */
	void setHoldsSpecimenTypeCollection(Collection<String> holdsSpecimenTypeCollection);
	/**
	 * Holds Specimen Class Collection
	 * @return getHoldsSpecimenClassCollection
	 */
	public Collection<String> getHoldsSpecimenClassCollection();
}
