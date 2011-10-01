package edu.wustl.catissuecore.bizlogic.magetab;

public abstract class AbstractTransformer implements Transformer {
	private String name;
	private String userFriendlyName;
	private String localName;

	public AbstractTransformer(String name, String userFriendlyName, String localName) {
		this.name = name;
		this.userFriendlyName = userFriendlyName;
		this.localName=localName;
	}
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getUserFriendlyName() {
		return userFriendlyName;
	}
	
	@Override
	public String getLocalName(){
		return localName;
	}
}
