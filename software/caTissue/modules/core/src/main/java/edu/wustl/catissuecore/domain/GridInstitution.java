/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Set;

import edu.wustl.common.domain.AbstractDomainObject;

public class GridInstitution extends AbstractDomainObject implements Serializable{
	
	private static final long serialVersionUID = 1234567890L;
	
	private Long id;
	private String gridInstitutionName;
	private Long localInstitutionIdentifier;
	private Set<GridGroup> gridGroups;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGridInstitutionName() {
		return gridInstitutionName;
	}
	public void setGridInstitutionName(String gridInstitutionName) {
		this.gridInstitutionName = gridInstitutionName;
	}
	public Long getLocalInstitutionIdentifier() {
		return localInstitutionIdentifier;
	}
	public void setLocalInstitutionIdentifier(Long localInstitutionIdentifier) {
		this.localInstitutionIdentifier = localInstitutionIdentifier;
	}
	public Set<GridGroup> getGridGroups() {
		return gridGroups;
	}
	public void setGridGroups(Set<GridGroup> gridGroups) {
		this.gridGroups = gridGroups;
	}
}
