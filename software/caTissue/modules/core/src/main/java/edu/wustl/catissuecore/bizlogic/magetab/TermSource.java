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
