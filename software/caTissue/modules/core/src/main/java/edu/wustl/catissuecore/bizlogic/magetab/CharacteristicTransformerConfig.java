/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.bizlogic.magetab;

public class CharacteristicTransformerConfig {
	private TermSource termSource;
	
	public CharacteristicTransformerConfig(TermSource termSource) {
		this.termSource = termSource;
	}

	public TermSource getTermSource() {
		return termSource;
	}

	public void setTermSource(TermSource termSource) {
		this.termSource = termSource;
	}
	
}
