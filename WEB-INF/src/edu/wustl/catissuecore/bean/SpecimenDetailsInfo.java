
package edu.wustl.catissuecore.bean;

import java.util.List;

/**
 * @author mandar_deshmukh
 *
 */
public interface SpecimenDetailsInfo
{

	String getSelectedSpecimenId();

	List<GenericSpecimen> getSpecimenList();

	List<GenericSpecimen> getAliquotList();

	List<GenericSpecimen> getDerivedList();

	boolean getShowCheckBoxes();

	boolean getShowbarCode();

	boolean getShowLabel();

	boolean getShowParentStorage();

	boolean isMultipleSpEditMode();

}