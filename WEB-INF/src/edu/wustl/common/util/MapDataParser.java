package edu.wustl.common.util;

/**
 * @author Kapil Kaveeshwar
 */
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class MapDataParser 
{
	private String packageName = "";
	private Map bigMap = new HashMap();
	private Collection dataList = new HashSet();	

	public MapDataParser(String packageName)
	{
		this.packageName = packageName;
	}
	
	private Map createMap()
	{
		Map map = new TreeMap();

		map.put("CollectionProtocolEvent:1_clinicalStatus", "Pre-Opt");                       
		map.put("CollectionProtocolEvent:1_studyCalendarEventPoint", "11.0");                 
		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_tissueSite", "Lung");        
		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_specimenType", "Blood");     
		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_specimenClass", "Tissue");
		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_quantityIn", "6");
		                                                                                      
		map.put("CollectionProtocolEvent:2_studyCalendarEventPoint", "10.0");                 
		map.put("CollectionProtocolEvent:2_clinicalStatus", "Pre-Opt");                       
		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_specimenType", "Blood");     
		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_tissueSite", "Kidney");      
		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_specimenClass", "Fluid");    
		                                                                                      
		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_tissueSite", "Brain");       
		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_specimenType", "Gel");       
		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_specimenClass", "Cell");     
		                                                                                      
		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_tissueSite", "Lever");       
		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_specimenType", "Cell");      
		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_specimenClass", "Molecular");

		return map; 
	} // createmap
	
	private Object toObject(String str,Class type) throws Exception
	{
		if(type.equals(String.class))
			return str;
		else if(type.equals(Long.class))
			return new Long(str);
		else if(type.equals(Double.class))
			return new Double(str);
		else if(type.equals(Float.class))
			return new Float(str);
		else if(type.equals(Integer.class))
			return new Integer(str);
		else if(type.equals(Byte.class))
			return new Integer(str);
		else if(type.equals(Short.class))
			return new Integer(str);
		return str;
	}
	
	private Method findMethod(Class objClass, String methodName) throws Exception
	{
		Method method[] = objClass.getMethods();
		for (int i = 0; i < method.length; i++) 
		{
			if(method[i].getName().equals(methodName))
				return method[i]; 
		}
		return null;
	}
	
	//CollectionProtocolEvent:1.studyCalendarEventPoint, new Double(11)
	private void parstData(Object parentObj, String str,String value,String parentKey) throws Exception
	{
		StringTokenizer st = new StringTokenizer(str, "_");
		
		int tokenCount = st.countTokens();
		if(tokenCount>1)
		{
			String className = st.nextToken();
			String mapKey = parentKey+"-"+str.substring(0,str.indexOf("_"));
			Object obj = parseClassAndGetInstance(parentObj,className,mapKey);
			
			if(tokenCount==2)
			{
				String attrName = st.nextToken();
				String methodName =  createAccessorMethodName(attrName,true);
				
				Class objClass = obj.getClass();

				Method method = findMethod(objClass,methodName);
				Object objArr[] = {toObject(value,method.getParameterTypes()[0])};
				
				method.invoke(obj,objArr);
			}
			else
			{
				int firstIndex = str.indexOf("_");
				className = str.substring(firstIndex+1);
				parstData(obj,className,value,mapKey);
			}
		}
		
	}
	
	private String createAccessorMethodName(String attr,boolean isSetter)
	{
		String firstChar = attr.substring(0,1);
		String str = "get"; 
		if(isSetter)
			str = "set"; 
		return str + firstChar.toUpperCase() + attr.substring(1);
	}
	
	private Object parseClassAndGetInstance(Object parentObj,String str,String mapKey) throws Exception
	{
		//map.put("CollectionProtocolEvent:1.SpecimenRequirement:1.specimenType", "Blood");
		StringTokenizer innerST = new StringTokenizer(str, ":");
		String className = ""; 
		
		int count = innerST.countTokens();
		
		if(count==2) //Case obj is a collection
		{
			className = innerST.nextToken();
			String index = innerST.nextToken();
			
			Collection collection = null;
			
			if(parentObj == null)
				collection = dataList;
			else//SpecimenRequirement:1.specimenType", "Blood");
			{
				String collectionName = className;
				StringTokenizer st = new StringTokenizer(className,"#");
				if(st.countTokens()>1)
				{
					collectionName = st.nextToken();
					className = st.nextToken();
				}
				collection = getCollectionObj(parentObj,collectionName);
			}
			
			return getObjFromList(collection, index, className, mapKey);
		}
		else //case map.put("CollectionProtocolEvent.studyCalendarEventPoint", new Double(11));
		{
			className = str;
			return getObj(parentObj,className);
		}
	}
	
	private Object getObjFromList(Collection coll,String index,String className,String mapKey) throws Exception
	{
		Object obj = bigMap.get(mapKey);
		if(obj!=null)
		{
			return obj;
		}
		else
		{
			String fullyQualifiedClassName = packageName + "." +className;
			Class aClass = Class.forName(fullyQualifiedClassName);
			obj = aClass.newInstance();
			coll.add(obj);
			bigMap.put(mapKey,obj);
			return obj;
		}
	}
	
	private Collection getCollectionObj(Object parentObj, String str) throws Exception
	{
		String attrName = str+"Collection";
		return (Collection)getObj(parentObj, attrName);
	}
	
	private Object getObj(Object parentObj, String attrName) throws Exception
	{
		//Create the getter method of attribute
		String methodName =  createAccessorMethodName(attrName,false);
		Class objClass = parentObj.getClass();
		Method method = objClass.getMethod(methodName, new Class[0]);

		return method.invoke(parentObj,new Object[0]);
	}
	

	public Collection generateData(Map map)  throws Exception
	{
		Iterator it = map.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			String value = (String)map.get(key);
			parstData(null, key, value,"KEY");
		}
		return dataList;
	}
	
	public static void main(String[] args) throws Exception
	{
		MapDataParser aMapDataParser = new MapDataParser("edu.wustl.catissuecore.domain");
		Map map = aMapDataParser.createMap();
		map = aMapDataParser.fixMap(map);
		System.out.println(map);
		Collection dataCollection = aMapDataParser.generateData(map);
		System.out.println("Data: "+dataCollection);
	}
	
	//SpecimenRequirement#FluidSpecimenRequirement:1.specimenType", "Blood");
	private Map fixMap(Map orgMap)
	{
		Map replaceMap = new HashMap();
		Map unitMap = new HashMap();
		unitMap.put("Cell","CellCount");
		unitMap.put("Fluid","MiliLiter");
		unitMap.put("Tissue","Gram");
		unitMap.put("Molecular","MicroGram");
		
		Iterator it = orgMap.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			if(key.indexOf("specimenClass")!=-1)
			{
				String value = (String)orgMap.get(key);
				String replaceWith = "SpecimenRequirement"+"#"+value+"SpecimenRequirement";
				
				key = key.substring(0,key.lastIndexOf("_"));
				String newKey = key.replaceFirst("SpecimenRequirement",replaceWith);
				
				replaceMap.put(key,newKey);
			}
		}
		
		Map newMap = new HashMap();
		it = orgMap.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String)it.next();
			String value = (String)orgMap.get(key);
			if(key.indexOf("SpecimenRequirement")==-1)
			{
				newMap.put(key,value);
			}
			else
			{
				if(key.indexOf("specimenClass")==-1)
				{
					if(key.indexOf("quantityIn")!=-1)
					{
						String keyPart = "quantityIn";
						
						String searchKey = key.substring(0,key.lastIndexOf("_"))+"_specimenClass";
						String specimenClass = (String)orgMap.get(searchKey);
						String unit = (String)unitMap.get(specimenClass);
						String newKeyPart = keyPart+unit;
						
						key = key.replaceFirst(keyPart,newKeyPart);
						
						//Rplace # and class name
						keyPart = key.substring(0,key.lastIndexOf("_"));
						newKeyPart = (String)replaceMap.get(keyPart);
						key = key.replaceFirst(keyPart,newKeyPart);
						newMap.put(key,value);
						
						//newMap.put(key,value);
					}
					else
					{
						String keyPart = key.substring(0,key.lastIndexOf("_"));
						String newKeyPart = (String)replaceMap.get(keyPart);
						key = key.replaceFirst(keyPart,newKeyPart);
						newMap.put(key,value);
					}
				}
			}
		}		
		return newMap;
	}
} // class