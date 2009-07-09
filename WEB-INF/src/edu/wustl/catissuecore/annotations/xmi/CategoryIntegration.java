
package edu.wustl.catissuecore.annotations.xmi;

import java.util.HashMap;
import java.util.List;

import edu.common.dynamicextensions.util.CategoryCreator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
/**
 * @author janhavi_hasabnis
 *
 */
public class CategoryIntegration
{

	/**
	 * logger Logger - Generic logger.
	 */
	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}
	private static Logger logger = Logger.getCommonLogger(CategoryIntegration.class);
/**
 * @param args - args
 */
	public static void main(String[] args)
	{

		final List<HashMap> categoryList = CategoryCreator.createCategory(args);
		DECategoryIntegration.categoryIntegration(categoryList);

	}

}
