package edu.wustl.catissuecore.print;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.rpc.ParameterMode;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.w3c.dom.Document;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.common.domain.AbstractDomainObject;
import gov.nih.nci.security.authorization.domainobjects.User;
/**
 * This Class is used to define method for label printing
 * @author falguni_sachde
 */
public class LabelPrinterImpl implements LabelPrinter {


	public boolean printLabel(AbstractDomainObject abstractDomainObject, String ipAddress, User userObj) {
		
		LinkedHashMap dataMap = createObjectMap(abstractDomainObject);
		ArrayList listMap = new ArrayList ();
		listMap.add(dataMap);
		Document doc = new PrintXMLGenerator().generateXMLDoc(listMap);
		String strXMLData = getStringFromDocument(doc);
		try {
			callPrintWebService(strXMLData);
		} catch (Exception e) {
		 return false;
		}
		
		return true;
	}
	
	

	private void callPrintWebService(String strXMLData) throws Exception {
		String endpoint = "http://localhost:8080/ws4ee/services/PrintWebService";
		String method = "print";

		// Make the call
		Service service = new Service();
		Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new java.net.URL(endpoint));
		call.setOperationName(method);
		call.addParameter("op1", XMLType.XSD_STRING, ParameterMode.IN);
		call.setReturnType(XMLType.XSD_STRING);
		String ret = (String) call.invoke(new Object[] {strXMLData});
		System.out.println("LabelPrinterImpl.callPrintWebService()"+ret);
	}



	public boolean printLabel(List<AbstractDomainObject> abstractDomainObjectList, String ipAddress, User userObj) {
		//Iterate through all objects in List ,crate map of each object.
		ArrayList listMap = new ArrayList ();
		for(int cnt=0;cnt < abstractDomainObjectList.size();cnt++)
		{
			AbstractDomainObject abstractDomainObject = abstractDomainObjectList.get(cnt); 
			LinkedHashMap dataMap = createObjectMap(abstractDomainObject);
			listMap.add(dataMap);
		}
		Document doc = new PrintXMLGenerator().generateXMLDoc(listMap);
		String strXMLData = getStringFromDocument(doc);
		try {
			callPrintWebService(strXMLData);
		} catch (Exception e) {
		 return false;
		}
		
		return true;
		
	}
	public String getStringFromDocument(Document doc) {
		try {
			DOMSource domSource = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.transform(domSource, result);
			System.out.println(writer.toString());
			return writer.toString();
		} catch (TransformerException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	LinkedHashMap createObjectMap(AbstractDomainObject abstractDomainObject)
	{
		LinkedHashMap dataMap = new LinkedHashMap();
		if(abstractDomainObject instanceof Specimen)
		{
			
			Specimen objSpecimen = (Specimen)abstractDomainObject;
			String label= objSpecimen.getLabel();
			String barcode = objSpecimen.getBarcode();
		
			dataMap.put("class", objSpecimen.getClassName());
			dataMap.put("id",objSpecimen.getId().toString());
			dataMap.put("label", label);
			dataMap.put("barcode",barcode);
		}
		return dataMap;
	}
}
