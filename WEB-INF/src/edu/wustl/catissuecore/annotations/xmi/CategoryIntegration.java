
package edu.wustl.catissuecore.annotations.xmi;

import java.util.HashMap;
import java.util.List;

import edu.common.dynamicextensions.util.CategoryCreator;

public class CategoryIntegration
{

	public static void main(String[] args)
	{

		List<HashMap> categoryList = CategoryCreator.createCategory(args);
		DECategoryIntegration.categoryIntegration(categoryList);

	}

}
