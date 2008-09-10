package edu.wustl.catissuecore.querysuite.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;

/**
 * 
 * @author pooja_deshpande
 * Base class for the classes related to deletion of metadata
 */

public class DeleteBaseMetadata 
{
	HashMap<Long, List<AttributeInterface>> entityIDAttributeListMap = new HashMap<Long, List<AttributeInterface>>();
	HashMap<String, List<String>> entityAttributesToDelete = new HashMap<String, List<String>>();
	HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	List<String> entityNameList = new ArrayList<String>();
	Map<String, Long> entityIDMap = new HashMap<String, Long>();
}
