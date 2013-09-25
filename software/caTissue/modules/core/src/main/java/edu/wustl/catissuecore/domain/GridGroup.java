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

public class GridGroup extends AbstractDomainObject implements Serializable{
	
	private static final long serialVersionUID = 1234567890L;
	
	private Long id;
	private String gridGroupName;
	private String csmGroupName;
	private Set<User> users;
	
	
	public Set<User> getUsers() {
		return users;
	}


	public void setUsers(Set<User> users) {
		this.users = users;
	}


	public String getGridGroupName() {
		return gridGroupName;
	}


	public void setGridGroupName(String gridGroupName) {
		this.gridGroupName = gridGroupName;
	}


	public String getCsmGroupName() {
		return csmGroupName;
	}


	public void setCsmGroupName(String csmGroupName) {
		this.csmGroupName = csmGroupName;
	}


	public Long getId() {
		return id;
	}

	
	public void setId(Long id) {
		this.id = id;
		
	}

}
