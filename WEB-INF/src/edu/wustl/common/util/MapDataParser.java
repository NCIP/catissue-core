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

		map.put("ParticipantMedicalIdentifier:1_medicalRecordNumber","AAA");
		map.put("ParticipantMedicalIdentifier:1_Site_systemIdentifier","1");
		
		
//		map.put("SpecimenRequirement#FluidSpecimenRequirement:1_pathologyStatus","Primary Tumor");
//		map.put("SpecimenRequirement#FluidSpecimenRequirement:1_specimenType","Blood");
//		map.put("SpecimenRequirement#FluidSpecimenRequirement:1_tissueSite","Adrenal-Cortex");
//		map.put("SpecimenRequirement#FluidSpecimenRequirement:1_quantityInMiliLiter","20");
		
//		map.put("CollectionProtocolEvent:1_clinicalStatus", "Pre-Opt");                       
//		map.put("CollectionProtocolEvent:1_studyCalendarEventPoint", "11.0");                 
//		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_tissueSite", "Lung");        
//		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_specimenType", "Blood");     
//		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_specimenClass", "Tissue");
//		map.put("CollectionProtocolEvent:1_SpecimenRequirement:1_quantityIn", "6");
//		                                                                                      
//		map.put("CollectionProtocolEvent:2_studyCalendarEventPoint", "10.0");                 
//		map.put("CollectionProtocolEvent:2_clinicalStatus", "Pre-Opt");                       
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_specimenType", "Blood");     
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_tissueSite", "Kidney");      
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_specimenClass", "Fluid");    
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:1_quantityIn", "7");
//		
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_tissueSite", "Brain");       
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_specimenType", "Gel");       
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_specimenClass", "Cell");     
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:2_quantityIn", "8");
//		
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_tissueSite", "Lever");       
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_specimenType", "Cell");      
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_specimenClass", "Molecular");
//		map.put("CollectionProtocolEvent:2_SpecimenRequirement:3_quantityIn", "9");
		
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
				String methodName =  Utility.createAccessorMethodName(attrName,true);
				
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
			{
				collection = dataList;
				
				StringTokenizer st = new StringTokenizer(className,"#");
				if(st.countTokens()>1)
				{
					st.nextToken();
					className = st.nextToken();
				}
			}
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
			return Utility.getValueFor(parentObj,className);
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
		return (Collection) Utility.getValueFor(parentObj, attrName);
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
//		map = aMapDataParser.fixMap(map);
		System.out.println(map);
		Collection dataCollection = aMapDataParser.generateData(map);
		System.out.println("Data: "+dataCollection);
	}
}