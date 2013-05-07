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
