package edu.wustl.catissuecore.namegenerator;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.util.logger.Logger;



/**
 * This class is added to support new token BARCODE.This class will provide barcode of specimen to replace the token.
 * @author pathik_sheth
 *
 */
public class LabelTokenForSpecimenBarcode implements LabelTokens
{

	private static final Logger LOGGER = Logger.getCommonLogger(LabelTokenForSpecimenBarcode.class);

	/**
	 * This will return the barcode for the given specimen object.
	 */
	public String getTokenValue(Object object)
	{
		String valToReplace="";
		Specimen objSpecimen = (Specimen) object;
		try
		{
			if(objSpecimen.getBarcode()!=null)
			{
				valToReplace = objSpecimen.getBarcode();
			}
		}
		catch (Exception e)
		{
			LOGGER.info(e.getMessage());
		}
		return valToReplace;
	}

}
