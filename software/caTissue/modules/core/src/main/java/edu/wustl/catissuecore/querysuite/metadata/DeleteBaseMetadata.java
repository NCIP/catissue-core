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

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * @author pooja_deshpande
 * Base class for the classes related to deletion of metadata
 */

public class DeleteBaseMetadata
{

	/**
	 * Specify entity ID Attribute List Map.
	 */
	HashMap<Long, List<AttributeInterface>> entityIDAttributeListMap = new HashMap<Long, List<AttributeInterface>>();
	/**
	 * Specify entity Attributes To Delete.
	 */
	HashMap<String, List<String>> entityAttributesToDelete = new HashMap<String, List<String>>();
	/**
	 * Specify attribute Data type Map.
	 */
	HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	/**
	 * Specify entity Name List.
	 */
	List<String> entityNameList = new ArrayList<String>();
	/**
	 * Specify entity ID Map.
	 */
	Map<String, Long> entityIDMap = new HashMap<String, Long>();
}
