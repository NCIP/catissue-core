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
