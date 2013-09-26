/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */


package edu.wustl.catissuecore.querysuite.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pooja_deshpande
 * Base class for all the meta data related classes
 */
public class BaseMetadata
{

	/**
	 * specify attribute Column Name Map.
	 */
	Map<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();
	/**
	 * specify attribute Column Name Map.
	 */
	Map<String, String> attributeColumnNameMap = new HashMap<String, String>();
	/**
	 * specify attribute Data type Map.
	 */
	Map<String, String> attributeDatatypeMap = new HashMap<String, String>();
	/**
	 * specify attribute Primary key Map.
	 */
	Map<String, String> attributePrimarkeyMap = new HashMap<String, String>();
	/**
	 * specify entity List.
	 */
	List<String> entityList = new ArrayList<String>();
}
