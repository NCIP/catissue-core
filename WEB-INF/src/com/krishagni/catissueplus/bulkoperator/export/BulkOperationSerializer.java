package com.krishagni.catissueplus.bulkoperator.export;

import static com.krishagni.catissueplus.bulkoperator.export.XmlUtil.writeElement;
import static com.krishagni.catissueplus.bulkoperator.export.XmlUtil.writeElementEnd;
import static com.krishagni.catissueplus.bulkoperator.export.XmlUtil.writeElementStart;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.krishagni.catissueplus.bulkoperator.metadata.Attribute;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationClass;
import com.krishagni.catissueplus.bulkoperator.metadata.BulkOperationMetadata;


public class BulkOperationSerializer {
	
	private BulkOperationMetadata metaData = null;
	
	BulkOperationClass boClass = null;
	
	private StringBuilder xmlBuilder; 

	public BulkOperationSerializer(BulkOperationMetadata metaData) throws IOException {
		this.metaData = metaData;
		this.boClass = metaData.getBulkOperationClass().iterator().next();
		xmlBuilder = new StringBuilder();
	}
	
	
	public String serialize() {
		emitBOStart();
		
		serializeRecMapper(boClass);
		
		emitIntegrationProps();
		
		emitBOEnd();
		
		String xmlTemplate = format(xmlBuilder.toString());

        return xmlTemplate;
	}

   	private void emitBOStart() {
		Map<String, String> elementStartDetails = new HashMap<String, String>();
		elementStartDetails.put("templateName", boClass.getTemplateName());
		elementStartDetails.put("batchSize", boClass.getBatchSize().toString());
        writeElementStart(xmlBuilder,"BulkOperationMetadata", elementStartDetails);

        elementStartDetails.clear();
		elementStartDetails.put("className", boClass.getClassName());
		elementStartDetails.put("type", "DEEntity");
		
		writeElementStart(xmlBuilder,"BulkOperationClass", elementStartDetails);	
	}
	

	private void serializeRecMapper(BulkOperationClass boClass) {

		for (BulkOperationClass association : boClass.getContainmentAssociationCollection()) {
			Map<String, String> attrs = new HashMap<String, String>();
			attrs.put("className", association.getClassName());
			writeElementStart(xmlBuilder, "containmentAssociation", attrs);
			
			serializeRecMapper(association);
			writeElementEnd(xmlBuilder, "containmentAssociation");
		}
		
		for (BulkOperationClass collection : boClass.getReferenceAssociationCollection()) {
			Map<String, String> attrs = new HashMap<String, String>();
			
			attrs.put("className", collection.getClassName());
			writeElementStart(xmlBuilder, "containmentAssociation", attrs);
			
			serializeRecMapper(collection);
			writeElementEnd(xmlBuilder, "containmentAssociation");
		}
		serializeRecordFields(boClass.getAttributeCollection());
	}
	
	private void emitIntegrationProps() {
		writeElementStart(xmlBuilder, "hookingInformation");
		serializeRecordFields(boClass.getHookingInformation().getAttributeCollection());
		writeElementEnd(xmlBuilder, "hookingInformation");
	}

	private void emitBOEnd() {
		writeElementEnd(xmlBuilder, "BulkOperationClass");
		writeElementEnd(xmlBuilder, "BulkOperationMetadata");
	}

    public String format(String unformattedXml) {
        try {
            final Document document = parseXmlFile(unformattedXml);

            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);

            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	private void serializeRecordFields(Collection<Attribute> fields) {
		for (Attribute recField : fields) {
			Map<String, String> field = new HashMap<String, String>();
			field.put("csvColumnName", recField.getCsvColumnName());
            field.put("name", recField.getName());

            if (recField.getFormat() != null && !recField.getFormat().isEmpty()) {
				field.put("format", recField.getFormat());
			}

			writeElement(xmlBuilder, "attribute", null, field);
		}		
	}
}
