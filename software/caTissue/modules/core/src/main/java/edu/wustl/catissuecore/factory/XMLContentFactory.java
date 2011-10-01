package edu.wustl.catissuecore.factory;

import edu.wustl.catissuecore.domain.pathology.XMLContent;


public class XMLContentFactory implements InstanceFactory<XMLContent>
{
	private static XMLContentFactory xmlContentFactory;

	protected XMLContentFactory() {
		super();
	}

	public static synchronized XMLContentFactory getInstance() {
		if(xmlContentFactory == null) {
			xmlContentFactory = new XMLContentFactory();
		}
		return xmlContentFactory;
	}
	public XMLContent createClone(XMLContent t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public XMLContent createObject()
	{
		XMLContent content = new XMLContent();
		initDefaultValues(content);
		return content;
	}

	public void initDefaultValues(XMLContent t)
	{
		// TODO Auto-generated method stub

	}

}
