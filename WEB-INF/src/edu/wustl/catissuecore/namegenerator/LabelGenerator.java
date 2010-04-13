
package edu.wustl.catissuecore.namegenerator;

import java.util.List;

/**
 * This is the base interface for  Label generation.
 * @author Falguni_Sachde
 *
 */
public interface LabelGenerator
{

	/**
	 * Set Label for given Object.
	 * @param object Object for which label will be generated
	 */
	void setLabel(Object object) throws LabelGenException;

	/**
	 * Set Label for given Object.
	 * @param object Object for which label will be generated
	 */
	void setLabel(List object) throws LabelGenException;

	/**
	 * Returns Label for given Object.
	 * @param object -Object for which label will be returned
	 * @return label- Label of object
	 */
	String getLabel(Object object) throws LabelGenException;
}