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

import java.util.List;
import java.util.Map;

public class MaterialTypeTransformerConfig {
	private TermSource termSource;
	Map<String, String> materialTypesMap;
	List<String> knownMolecularTypes;
	
	public MaterialTypeTransformerConfig(TermSource termSource, Map<String, String> materialTypesMap,List<String> knownMolecularTypes ) {
		this.termSource = termSource;
		this.materialTypesMap = materialTypesMap;
		this.knownMolecularTypes=knownMolecularTypes;
	}
	
	public TermSource getTermSource() {
		return termSource;
	}
	
	public Map<String, String> getMaterialTypesMap() {
		return materialTypesMap;
	}

	public List<String> getKnownMolecularTypes() {
		return knownMolecularTypes;
	}
	
}
