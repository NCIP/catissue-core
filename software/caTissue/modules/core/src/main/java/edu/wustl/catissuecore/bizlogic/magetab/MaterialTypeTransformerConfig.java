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
