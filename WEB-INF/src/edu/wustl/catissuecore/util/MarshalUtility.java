/**
 *
 */

package edu.wustl.catissuecore.util;

import java.io.IOException;
import java.io.Writer;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This class contains utility methods to marshal object.
 * @author shrishail_kalshetty
 *
 */
public final class MarshalUtility
{

	/**
	 * Private constructor.
	 */
	private MarshalUtility()
	{
		//private constructor.
	}

	/**
	 * This method marshals object into an XML file.
	 * @param mappingXML mapping XML file.
	 * @param object Object to marshal in XML format.
	 * @param writer Writer to marshal object.
	 * @throws DynamicExtensionsSystemException throw DESystemException.
	 */
	public static void marshalObject(String mappingXML, final Object object, Writer writer)
			throws DynamicExtensionsSystemException
	{
		try
		{
			Mapping mapping = new Mapping();
			mapping.loadMapping(mappingXML);
			Marshaller marshaller = new Marshaller(writer);
			//marshaller.setProperty("org.exolab.castor.xml.naming", "mixed");
			marshaller.setMapping(mapping);
			marshaller.marshal(object);
		}
		catch (MappingException exception)
		{
			throw new DynamicExtensionsSystemException("Error while reading mapping file.",
					exception);
		}
		catch (IOException exception)
		{
			throw new DynamicExtensionsSystemException("Error while marshalling object.", exception);
		}
		catch (MarshalException exception)
		{
			throw new DynamicExtensionsSystemException("Error while marshalling object.", exception);
		}
		catch (ValidationException exception)
		{
			throw new DynamicExtensionsSystemException(
					"Error while validating XML template for marshal operation.", exception);
		}
	}
}
