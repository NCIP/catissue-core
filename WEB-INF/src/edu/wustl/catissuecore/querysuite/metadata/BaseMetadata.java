package edu.wustl.catissuecore.querysuite.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author pooja_deshpande
 * Base class for all the metadata related classes
 */
public class BaseMetadata 
{
	HashMap<String, List<String>> entityNameAttributeNameMap = new HashMap<String, List<String>>();
	HashMap<String, String> attributeColumnNameMap = new HashMap<String, String>();
	HashMap<String, String> attributeDatatypeMap = new HashMap<String, String>();
	HashMap<String, String> attributePrimarkeyMap = new HashMap<String, String>();
	List<String> entityList = new ArrayList<String>();
}
