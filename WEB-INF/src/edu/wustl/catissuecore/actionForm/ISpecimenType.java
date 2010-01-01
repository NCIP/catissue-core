package edu.wustl.catissuecore.actionForm;

/**
 * This interface is for getting all Specimen Type Array.
 * @author virender_mehta
 * @created-on Dec 31, 2009
 */
public interface ISpecimenType
{
	/**
	 * @return getHoldsFluidSpType
	 */
	String[] getHoldsFluidSpType();
	/**
	 * @return getHoldsCellSpType
	 */
	String[] getHoldsCellSpType();
	/**
	 * @return getHoldsMolSpType
	 */
	String[] getHoldsMolSpType();
	/**
	 * @return getHoldsTissueSpType
	 */
	String[] getHoldsTissueSpType();
	/**
	 * @param holdsTissueSpType Tissue Specimen Types
	 */
	void setHoldsTissueSpType(String[] holdsTissueSpType);
	/**
	 * @param holdsFluidSpType Fluid Specimen Types
	 */
	void setHoldsFluidSpType(String[] holdsFluidSpType);
	/**
	 * @param holdsCellSpType Cell Specimen Types
	 */
	void setHoldsCellSpType(String[] holdsCellSpType);
	/**
	 * @param holdsMolSpType Molecular Specimen Types
	 */
	void setHoldsMolSpType(String[] holdsMolSpType);
	/**
	 * @param specimenOrArrayType Setting specimenOrArrayType
	 */
	void setSpecimenOrArrayType(String specimenOrArrayType);
}
