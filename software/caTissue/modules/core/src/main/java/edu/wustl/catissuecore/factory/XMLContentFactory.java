/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

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
