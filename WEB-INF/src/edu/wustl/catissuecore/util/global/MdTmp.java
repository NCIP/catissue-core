/*
 * Created on Jul 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package edu.wustl.catissuecore.util.global;

/**
 * @author mandar_deshmukh
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
import java.util.*;
import java.lang.reflect.*;
import edu.wustl.catissuecore.domain.*;

public class MdTmp {
	Map map;

	String packName;

	public void createMap() {
		map = new TreeMap();
		map.put("CollectionProtocolEvent:1.SpecimenRequirements:1.tissueType",
				"Fluid Specimen");
		map.put("CollectionProtocolEvent:1.studyCalendarEventPoint",
				new Integer(11));

		map.put("CollectionProtocolEvent:1.SpecimenRequirements:1.subType",
				"Blood");

		map.put("CollectionProtocolEvent:2.clinicalStatus", "Pre-Opt");
		map.put("CollectionProtocolEvent:2.studyCalendarEventPoint",
				new Integer(10));
		map.put("CollectionProtocolEvent:2.SpecimenRequirements:2.tissueType",
				"Cell Specimen");
		map.put("CollectionProtocolEvent:1.clinicalStatus", "Pre-Opt");
		map
				.put(
						"CollectionProtocolEvent:2.SpecimenRequirements:2.tissueSubType",
						"Gel");
		map.put("CollectionProtocolEvent:2.SpecimenRequirements:1.tissueType",
				"Fluid Specimen");
		map
				.put(
						"CollectionProtocolEvent:2.SpecimenRequirements:1.tissueSubType",
						"Blood");
		packName = "edu.wustl.catissuecore.domain.";
	} // createmap

	public Iterator getItr() {
		Set s = map.keySet();
		Iterator i = s.iterator();
		return i;
	} // getItr

	public static void main(String[] args) {

		MdTmp a1 = new MdTmp();
		a1.createMap();
		a1.genObjects();

	} // main

	public void breakString(String str, String delim) {

		StringTokenizer st = new StringTokenizer(str, delim);
		System.out.println(st.countTokens());
		while (st.hasMoreElements()) {
			//			System.out.print("NE: "+st.nextElement());
			System.out.print("\tNT: " + st.nextToken());
		}
		System.out.println();
	}

	public Object createObject(String str)
	{
		try {
			Class cl = Class.forName(packName + str);

			Object o = cl.newInstance();
			return o;
		} // try
		catch (Exception e) {
			System.out.println("Error in CreateObject : " + e);
			return null;
		}
	} // createobject

	public void genObjects() 
	{
		try 
		{
			//			 list for top level class objects
			List outerList = new ArrayList();
			List innerList = new ArrayList();
			// get the keys
			Iterator i = getItr();
			String classCheck="";
			
			Stack objectStack = new Stack();
			
			while (i.hasNext()) {
				// get single key
				Object o = i.next();
				System.out.println(o);
				String str = o.toString();
				StringTokenizer st = new StringTokenizer(str, ".");
				String value = (String) map.get(o);
				System.out.println("Value: " + value);
				
								
				String classToken = st.nextToken();
				
				if(objectStack.isEmpty() )
				{
					objectStack.push(classToken );
					classCheck = classToken; 
				} // stack mt
				else
				{
						int chkCl = objectStack.search(classToken );
						if (chkCl == -1)		// new object
						{
							StringTokenizer st1 = new StringTokenizer(classToken, ":");

							String className = st1.nextToken();
							String classIndex = st1.nextToken();
							
							Object obj1;
							try
							{
								 obj1 = outerList.get(Integer.parseInt(classIndex)-1);
		
								 // to call the set method of the collection
								 
							} //object available at given index
							catch(IndexOutOfBoundsException iobe)
							{
								System.out.println("Object Created Successfully :- " + className);
							}

							
						} //class not found
						
				} // stack not mt
				
				
				if (st.countTokens() == 2)
				{
					String attr = st.nextToken();
					System.out.println("---");
					System.out.print("Classtoken: " + classToken);
					System.out.println("\nAttr: " + attr);
					
					getDataObject(classToken, attr, outerList,value);

				} // token cnt = 2
				else 
				{
					classToken = st.nextToken();
					String attr = st.nextToken();
					System.out.println("---");
					System.out.print("Classtoken: " + classToken);
					System.out.println("\nAttr: " + attr);
					
					getDataObject(classToken, attr, innerList,value);
	

					
				} // token!=2

			} // while itr

			System.out.println("Complete List");
			System.out.println(outerList);

		} // try
		catch (Exception e) {
			System.out.println("Error in GenObjects : " + e);
		} // catch
	} // genobjects

	
	
	public void getDataObject(String classToken,String attr,List aList,String value)
	{
		try
		{
		System.out.println("---");
		System.out.print("Classtoken: " + classToken);
		System.out.println("\nAttr: " + attr);
		// separating the classname from its index
		StringTokenizer st1 = new StringTokenizer(classToken, ":");

		String className = st1.nextToken();
		String classIndex = st1.nextToken();
		
		Object obj;
		try
		{
			 obj = aList.get(Integer.parseInt(classIndex)-1);
			System.out.println("Object Retrieved Successfully :- "
						+ className);
		} //object available at given index
		catch(IndexOutOfBoundsException iobe)
		{
			obj = createObject(className);
			System.out.println("Object Created Successfully :- " + className);
		}


		System.out.println("Object Class : " + obj.getClass());
		Field f[] = obj.getClass().getDeclaredFields();
		System.out.println("\n================================");
		for (int f1 = 0; f1 < f.length; f1++) {
			System.out.println("\n\t" + f[f1].getName() + " : "
					+ f[f1].getType());
		}

		Method m[] = obj.getClass().getDeclaredMethods();
		System.out.println("\n========= Methods =======================");
		Method reqMethod;
		for (int m1 = 0; m1 < m.length; m1++) {
			System.out.print("\t" + m[m1].getName());
			if (m[m1].getName().equalsIgnoreCase("set" + attr)) {
				reqMethod = m[m1];
				Object param[] = new Object[1];
				param[0] = value;
				System.out.println("\nInvoking : "
						+ (m[m1].getName()));
				reqMethod.invoke(obj, param);
				System.out.println("\n\n\t\tMethod Invoked");
			}
		}

		aList.add((Integer.parseInt(classIndex) - 1), obj);
		System.out.println("Object Added to List");
		} // try
		catch (Exception e)
		{
			System.out.println("Error in GenObjects : " + e);
		} // catch
		
	}
	
	
} // class
