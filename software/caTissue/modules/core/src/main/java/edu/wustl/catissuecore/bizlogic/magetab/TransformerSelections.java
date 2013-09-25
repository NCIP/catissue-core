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

public class TransformerSelections {
	private String name;
	private String userFriendlyName;
	private String localName;
	private boolean selectedForSource;
	private boolean selectedForSample;
	private boolean selectedForExtract;
	private boolean mageTabSpec;
	
	public TransformerSelections() {
		
	}
	
	public TransformerSelections(String name, String userFriendlyName, String localName,
			boolean selectedForSource, boolean selectedForSample, boolean selectedForExtract , boolean mageTabSpec) {
		this.name = name;
		this.userFriendlyName = userFriendlyName;
		this.localName=localName;
		this.selectedForSource = selectedForSource;
		this.selectedForSample = selectedForSample;
		this.selectedForExtract = selectedForExtract;
		this.mageTabSpec = mageTabSpec;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserFriendlyName() {
		return userFriendlyName;
	}
	public void setUserFriendlyName(String userFriendlyName) {
		this.userFriendlyName = userFriendlyName;
	}
	public boolean isSelectedForSource() {
		return selectedForSource;
	}
	public void setSelectedForSource(boolean selectedForSource) {
		this.selectedForSource = selectedForSource;
	}
	public boolean isSelectedForSample() {
		return selectedForSample;
	}
	public void setSelectedForSample(boolean selectedForSample) {
		this.selectedForSample = selectedForSample;
	}
	public boolean isSelectedForExtract() {
		return selectedForExtract;
	}
	public void setSelectedForExtract(boolean selectedForExtract) {
		this.selectedForExtract = selectedForExtract;
	}
	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public boolean isMageTabSpec() {
		return mageTabSpec;
	}

	public void setMageTabSpec(boolean mageTabSpec) {
		this.mageTabSpec = mageTabSpec;
	}	
}
