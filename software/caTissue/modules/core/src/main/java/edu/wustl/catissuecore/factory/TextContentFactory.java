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

import edu.wustl.catissuecore.domain.pathology.TextContent;


public class TextContentFactory implements InstanceFactory<TextContent>
{
	private static TextContentFactory textContentFactory;

	protected TextContentFactory() {
		super();
	}

	public static synchronized TextContentFactory getInstance() {
		if(textContentFactory == null) {
			textContentFactory = new TextContentFactory();
		}
		return textContentFactory;
	}

	public TextContent createClone(TextContent t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public TextContent createObject()
	{
		TextContent content = new TextContent();
		initDefaultValues(content);
		return content;
	}

	public void initDefaultValues(TextContent t)
	{
		// TODO Auto-generated method stub

	}

}
