
package edu.wustl.catissuecore.util.metadata;

import java.io.InputStream;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * @author deepali_ahirrao
 * This class is called to convert the given XML into its POJO.
 * The constructor takes XML and its XSD as input parameter. The method getJavaObject is called to get
 * POJO of the XML.
 */
public class XMLToObjectConverter
{

	/** Unmarshaller. */
	private final Unmarshaller unmarshaller;
	private JAXBContext jaxbContext=null;
	/**
	 * This method creates the unmarshaller object for converting XML to its POJO.
	 * @param packageName name of package.
	 * @throws JAXBException exception.
	 * @throws SAXException
	 */
	public XMLToObjectConverter(String packageName, URL xsdFileUrl) throws JAXBException,
			SAXException
	{
		jaxbContext = JAXBContext.newInstance(packageName, Thread.currentThread()
				.getContextClassLoader());
		unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setEventHandler(new XMLEventHandler());

		/* This part is for validating given XML against a XSD. If the XSD is not specified
		 * then Jaxb will not validate the XML and convert the XML to its corresponding POJO.
		 */
		if (xsdFileUrl != null)
		{
			SchemaFactory schemafactory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");

			Schema schema = schemafactory.newSchema(xsdFileUrl);
			unmarshaller.setSchema(schema);
		}
	}

    /**
     * Object xml converter.
     *
     * @param outputPVVersion the output pv version
     * @param fileName the file name
     * @param baseDir the base dir
     * @throws JAXBException the jAXB exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
//    public void objectXMLConverter(final StaticMetaDataType outputPVVersion,
//            final String fileName, final String baseDir) throws JAXBException,
//            IOException
//    {
//        Marshaller marshaller = jaxbContext.createMarshaller();
//        FileWriter writer = new FileWriter(baseDir + File.separator + fileName
//                + ".xml");
//        marshaller.marshal(outputPVVersion, writer);
//    }

	/**
	 * @param fileInputStream File Input Stream
	 * @return java object
	 * @throws JAXBException exception
	 */
	public Object getJavaObject(InputStream fileInputStream) throws JAXBException
	{

		return unmarshaller.unmarshal(fileInputStream);
	}
}
