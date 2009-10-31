/**
 * 
 */

package edu.wustl.catissuecore.bean;

import java.util.Collection;

/**
 * @author mandar_deshmukh
 *
 */
public class SummarySpDetails
{

	/**
	 * Empty constructor 
	 */
	public SummarySpDetails()
	{
		super();
	}

	private String tissueCount = "0";
	private String cellCount = "0";
	private String moleculeCount = "0";
	private String fluidCount = "0";
	private Collection<Object> tissueTypeDetails;
	private Collection<Object> cellTypeDetails;
	private Collection<Object> molTypeDetails;
	private Collection<Object> fluidTypeDetails;
	private String tissueQuantity = "0";
	private String cellQuantity = "0";
	private String moleculeQuantity = "0";
	private String fluidQuantity = "0";
	private Collection<Object> patStDetails;
	private Collection<Object> tSiteDetails;

	/**
	 * Returns the Cell Specimen Count
	 * @return String
	 */
	public String getCellCount()
	{
		return this.cellCount;
	}

	/**
	 * Set the Cell Specimen Count
	 * @param String
	 * @return void
	 */
	public void setCellCount(final String cellCount)
	{
		this.cellCount = cellCount;
	}

	/**
	 * Returns the Fluid Specimen Count
	 * @return String
	 */
	public String getFluidCount()
	{
		return this.fluidCount;
	}

	/**
	 * Set the Fluid Specimen Count
	 * @param String
	 * @return void
	 */
	public void setFluidCount(final String fluidCount)
	{
		this.fluidCount = fluidCount;
	}

	/**
	 * Returns the Molecular Specimen Count
	 * @return String
	 */
	public String getMoleculeCount()
	{
		return this.moleculeCount;
	}

	/**
	 * Set the Molecular Specimen Count
	 * @param String
	 * @return void
	 */
	public void setMoleculeCount(final String moleculeCount)
	{
		this.moleculeCount = moleculeCount;
	}

	/**
	 * Returns the Tissue Specimen Count
	 * @return String
	 */
	public String getTissueCount()
	{
		return this.tissueCount;
	}

	/**
	 * Set Tissue Specimen Count
	 * @param String
	 * @return void
	 */
	public void setTissueCount(final String tissueCount)
	{
		this.tissueCount = tissueCount;
	}

	/**
	 * Returns the Cell Specimen Quantity
	 * @return String
	 */
	public String getCellQuantity()
	{
		return this.cellQuantity;
	}

	/**
	 * Set the Cell Specimen Quantity
	 * @param String
	 * @return void
	 */
	public void setCellQuantity(final String cellQuantity)
	{
		this.cellQuantity = cellQuantity;
	}

	/**
	 * Returns the cell sub-type name and available quantity of it
	 * @return Collection
	 */
	public Collection getCellTypeDetails()
	{
		return this.cellTypeDetails;
	}

	/**
	 * Return the available FLuid quantity
	 * @return String
	 */
	public String getFluidQuantity()
	{
		return this.fluidQuantity;
	}

	/**
	 * Set the available Fluid quantity
	 * @param String
	 * @return void
	 */
	public void setFluidQuantity(final String fluidQuantity)
	{
		this.fluidQuantity = fluidQuantity;
	}

	/**
	 * Returns the Fluid sub-type name and available quantity of it
	 * @return Collection<Object>
	 */
	public Collection getFluidTypeDetails()
	{
		return this.fluidTypeDetails;
	}

	/**
	 * Return the available Molecular quantity
	 * @return String
	 */
	public String getMoleculeQuantity()
	{
		return this.moleculeQuantity;
	}

	/**
	 * Set the available Molecular quantity
	 * @param String
	 * @return void
	 */
	public void setMoleculeQuantity(final String moleculeQuantity)
	{
		this.moleculeQuantity = moleculeQuantity;
	}

	/**
	 * Returns the Molecular sub-type name and available quantity of it
	 * @return Collection<Object>
	 */
	public Collection getMolTypeDetails()
	{
		return this.molTypeDetails;
	}

	/**
	 * Return the available Tissue quantity
	 * @return String
	 */
	public String getTissueQuantity()
	{
		return this.tissueQuantity;
	}

	/**
	 * Set the available Tissue quantity
	 * @param String
	 * @return void
	 */
	public void setTissueQuantity(final String tissueQuantity)
	{
		this.tissueQuantity = tissueQuantity;
	}

	/**
	 * Returns the Tissue sub-type name and available quantity of it
	 * @return Collection<Object>
	 */
	public Collection getTissueTypeDetails()
	{
		return this.tissueTypeDetails;
	}

	/**
	 * Set the Cell sub-type name and available quantity of it
	 * @param Collection<Object>
	 * @return void
	 */
	public void setCellTypeDetails(final Collection<Object> cellTypeDetails)
	{
		this.cellTypeDetails = cellTypeDetails;
	}

	/**
	 * Set the Fluid sub-type name and available quantity of it
	 * @param Collection<Object>
	 * @return void
	 */
	public void setFluidTypeDetails(final Collection<Object> fluidTypeDetails)
	{
		this.fluidTypeDetails = fluidTypeDetails;
	}

	/**
	 * Set the Molecular sub-type name and available quantity of it
	 * @param Collection<Object>
	 * @return void
	 */
	public void setMolTypeDetails(final Collection<Object> moleTypeDetails)
	{
		this.molTypeDetails = moleTypeDetails;
	}

	/**
	 * Set the Tissue sub-type name and available quantity of it
	 * @param Collection<Object>
	 * @return void
	 */
	public void setTissueTypeDetails(final Collection<Object> tissueTypeDetails)
	{
		this.tissueTypeDetails = tissueTypeDetails;
	}

	/**
	 * Get PatStDetails.
	 * @return Collection ob Object
	 */
	public Collection<Object> getPatStDetails()
	{
		return this.patStDetails;
	}

	/**
	 * Set PatStDetails.
	 * @param patStDetails Collection of Object.
	 */
	public void setPatStDetails(final Collection<Object> patStDetails)
	{
		this.patStDetails = patStDetails;
	}

	/**
	 * Get TSiteDetails.
	 * @return Collection of Object
	 */
	public Collection<Object> getTSiteDetails()
	{
		return this.tSiteDetails;
	}

	/**
	 * Set TSiteDetails.
	 * @param siteDetails Collection of Object
	 */
	public void setTSiteDetails(final Collection<Object> siteDetails)
	{
		this.tSiteDetails = siteDetails;
	}
}