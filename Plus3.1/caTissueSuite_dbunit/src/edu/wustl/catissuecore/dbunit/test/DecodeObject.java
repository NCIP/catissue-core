/**
 * @Class DecodeObject.java
 * @Author abhijit_naik
 * @Created on Aug 6, 2008
 */
package edu.wustl.catissuecore.dbunit.test;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.HashSet;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.Specimen;


/**
 * @author abhijit_naik
 *
 */
public class DecodeObject
{
	public static String encodeObject(Object object)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(baos);
		encoder.writeObject(object);
		encoder.close();
		return baos.toString();
		
	}
	
	public static Object decodeObject(String xml)
	{
		
		XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(xml.getBytes()));
		return decoder.readObject();
	}
	public static void main (String[] args)
	{
		Specimen sp=new Specimen();
		sp.setId(Long.valueOf(1));
		sp.setLabel("label");
		sp.setBarcode("bar");
		Specimen childSpecimen = new CellSpecimen();
		childSpecimen.setLabel("child");
		childSpecimen.setParentSpecimen(sp);
		sp.getChildSpecimenCollection().add(childSpecimen);
		Specimen sp1=new Specimen();
		sp1.setId(Long.valueOf(10));
		sp1.setLabel("LABEL_10");
		sp1.setBarcode("BAR_22");
		HashSet<Specimen> s = new HashSet<Specimen>();
		s.add(sp);
		s.add(sp1);
		String xml = encodeObject(s);
		System.out.println(xml);
		Collection cl= (Collection) decodeObject(xml);
	}
}
