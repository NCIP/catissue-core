package edu.wustl.catissuecore.bizlogic.magetab;

public class TermSource {
	private String name;
	private String file;
	private String version;
	
	public TermSource(String name, String file, String version) {
		this.name = name;
		this.file = file;
		this.version = version;
	}
	
	public String getName() {
		return name;
	}
	
	public String getFile() {
		return file;
	}
	
	public String getVersion() {
		return version;
	}
}
