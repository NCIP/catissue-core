
package edu.wustl.catissuecore.actionForm;
import java.util.Collection;

import org.apache.struts.action.ActionForm;

/**
 * This form holds the data of the summary page of caTissue 
 * @author sagar_baldwa
 */
public class SummaryForm extends ActionForm
{
	static final long serialVersionUID = 12345; 
	
	private String totalSpecimenCount="0";
	private String tissueCount="0";
	private String cellCount="0";
	private String moleculeCount="0";
	private String fluidCount="0";
	private Collection<Object> tissueTypeDetails;
	private Collection<Object> cellTypeDetails;
	private Collection<Object> moleculeTypeDetails;
	private Collection<Object> fluidTypeDetails;
	private String tissueQuantity="0";
	private String cellQuantity="0";
	private String moleculeQuantity="0";
	private String fluidQuantity="0";
	
	/**
	 * Default Constructor
	 *
	 */public SummaryForm()
	{}

	/**
	 * Returns the Cell Specimen Count
	 * @return String
	 */
	public String getCellCount() {
		return cellCount;
	}

	/**
	 * Set the Cell Specimen Count
	 * @param String
	 * @return void
	 */
	public void setCellCount(String cellCount) {
		this.cellCount = cellCount;
	}

	/**
	 * Returns the Fluid Specimen Count
	 * @return String
	 */
	public String getFluidCount() {
		return fluidCount;
	}

	/**
	 * Set the Fluid Specimen Count
	 * @param String
	 * @return void
	 */
	public void setFluidCount(String fluidCount) {
		this.fluidCount = fluidCount;
	}

	/**
	 * Returns the Molecular Specimen Count
	 * @return String
	 */
	public String getMoleculeCount() {
		return moleculeCount;
	}

	/**
	 * Set the Molecular Specimen Count
	 * @param String
	 * @return void
	 */
	public void setMoleculeCount(String moleculeCount) {
		this.moleculeCount = moleculeCount;
	}

	/**
	 * Returns the Tissue Specimen Count
	 * @return String
	 */
	public String getTissueCount() {
		return tissueCount;
	}

	/**
	 * Set Tissue Specimen Count
	 * @param String
	 * @return void
	 */
	public void setTissueCount(String tissueCount) {
		this.tissueCount = tissueCount;
	}

	/**
	 * Returns the Total Specimen Count of caTissue
	 * @return String
	 */
	public String getTotalSpecimenCount() {
		return totalSpecimenCount;
	}

	/**
	 * Set the Total Specimen Count of caTissue
	 * @param String
	 * @return void
	 */
	public void setTotalSpecimenCount(String totalSpecimenCount) {
		this.totalSpecimenCount = totalSpecimenCount;
	}

	/**
	 * Returns the Cell Specimen Quantity
	 * @return String
	 */
	public String getCellQuantity() {
		return cellQuantity;
	}

	/**
	 * Set the Cell Specimen Quantity
	 * @param String
	 * @return void
	 */
	public void setCellQuantity(String cellQuantity) {
		this.cellQuantity = cellQuantity;
	}

	/**
	 * Returns the cell sub-type name and available quantity of it
	 * @return Collection
	 */
	public Collection getCellTypeDetails() {
		return cellTypeDetails;
	}

	/**
	 * Return the available FLuid quantity
	 * @return String
	 */
	public String getFluidQuantity() {
		return fluidQuantity;
	}

	/**
	 * Set the available Fluid quantity
	 * @param String
	 * @return void
	 */
	public void setFluidQuantity(String fluidQuantity) {
		this.fluidQuantity = fluidQuantity;
	}

	/**
	 * Returns the Fluid sub-type name and available quantity of it
	 * @return Collection<Object>
	 */
	public Collection getFluidTypeDetails() {
		return fluidTypeDetails;
	}

	/**
	 * Return the available Molecular quantity
	 * @return String
	 */
	public String getMoleculeQuantity() {
		return moleculeQuantity;
	}

	/**
	 * Set the available Molecular quantity
	 * @param String
	 * @return void
	 */
	public void setMoleculeQuantity(String moleculeQuantity) {
		this.moleculeQuantity = moleculeQuantity;
	}

	/**
	 * Returns the Molecular sub-type name and available quantity of it
	 * @return Collection<Object>
	 */
	public Collection getMoleculeTypeDetails() {
		return moleculeTypeDetails;
	}

	/**
	 * Return the available Tissue quantity
	 * @return String
	 */
	public String getTissueQuantity() {
		return tissueQuantity;
	}

	/**
	 * Set the available Tissue quantity
	 * @param String
	 * @return void
	 */
	public void setTissueQuantity(String tissueQuantity) {
		this.tissueQuantity = tissueQuantity;
	}

	/**
	 * Returns the Tissue sub-type name and available quantity of it
	 * @return Collection<Object>
	 */
	public Collection getTissueTypeDetails() {
		return tissueTypeDetails;
	}	

	/**
	 * Set the Cell sub-type name and available quantity of it
	 * @param Collection<Object>
	 * @return void
	 */
	public void setCellTypeDetails(Collection<Object> cellTypeDetails) {
		this.cellTypeDetails = cellTypeDetails;
	}

	/**
	 * Set the Fluid sub-type name and available quantity of it
	 * @param Collection<Object>
	 * @return void
	 */
	public void setFluidTypeDetails(Collection<Object> fluidTypeDetails) {
		this.fluidTypeDetails = fluidTypeDetails;
	}

	/**
	 * Set the Molecular sub-type name and available quantity of it
	 * @param Collection<Object>
	 * @return void
	 */
	public void setMoleculeTypeDetails(Collection<Object> moleculeTypeDetails) {
		this.moleculeTypeDetails = moleculeTypeDetails;
	}

	/**
	 * Set the Tissue sub-type name and available quantity of it
	 * @param Collection<Object>
	 * @return void
	 */
	public void setTissueTypeDetails(Collection<Object> tissueTypeDetails) {
		this.tissueTypeDetails = tissueTypeDetails;
	}

}
