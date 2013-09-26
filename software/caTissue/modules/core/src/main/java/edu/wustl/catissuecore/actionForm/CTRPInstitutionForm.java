/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.actionForm;

import java.io.Serializable;

import org.apache.struts.action.ActionForm;

/**
 * @author Ravi.Batchu
 * 
 */
public class CTRPInstitutionForm extends ActionForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -47400133364001038L;
	private String coppaSelection;
	private String operation;
	private String entityName;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCoppaSelection() {
		return coppaSelection;
	}

	public void setCoppaSelection(String coppaSelection) {
		this.coppaSelection = coppaSelection;
	}

}
