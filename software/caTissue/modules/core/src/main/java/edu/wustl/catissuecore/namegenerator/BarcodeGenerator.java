
package edu.wustl.catissuecore.namegenerator;

import java.util.List;

/**
 * This is the base interface for  Bar code generation.
 * @author Falguni_Sachde
 *
 */
public interface BarcodeGenerator
{

	/**
	 * Set Bar code for given Object.
	 * @param object Object for which bar code will be generated.
	 */
	void setBarcode(Object object);

	/**
	 * Set Bar code for given Object.
	 * @param object Object for which bar code will be generated.
	 */
	void setBarcode(List<Object> object);

}