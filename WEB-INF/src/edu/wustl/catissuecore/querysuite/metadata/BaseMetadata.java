
package edu.wustl.catissuecore.querysuite.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author pooja_deshpande
 * Base class for all the meta data related classes
 */
public class BaseMetadata
{

	/**
	 * specify attribute Column Name Map.
	 */
	HashMap<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();
	/**
	 * specify attribute Column Name Map.
	 */
	HashMap<String, String> attributeColumnNameMap = new HashMap<String, String>();
	/**
	 * specify attribute Data type Map.
	 */
	HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	/**
	 * specify attribute Primary key Map.
	 */
	HashMap<String, String> attributePrimarkeyMap = new HashMap<String, String>();
	/**
	 * specify entity List.
	 */
	List<String> entityList = new ArrayList<String>();
}
