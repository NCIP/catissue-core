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
