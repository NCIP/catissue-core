/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


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

	boolean isGenerateLabel();

	void setGenerateLabel(boolean generateLabel);
}