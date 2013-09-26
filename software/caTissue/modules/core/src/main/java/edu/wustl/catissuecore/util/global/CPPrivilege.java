/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

/**
 * 
 */

package edu.wustl.catissuecore.util.global;

/**
 * @author juberahamad_patel
 *
 */
public enum CPPrivilege {
	REGISTRATION(18), SPECIMEN_ACCESSION(20), SPECIMEN_PROCESSING(26), PHI_ACCESS(23);

	private int id;

	CPPrivilege(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return this.id;
	}
}
