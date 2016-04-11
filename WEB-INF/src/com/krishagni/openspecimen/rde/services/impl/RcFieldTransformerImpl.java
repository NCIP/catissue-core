//package com.krishagni.openspecimen.rde.services.impl;
//
//import java.util.List;
//
//import org.apache.commons.lang.StringUtils;
//
//import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
//import com.krishagni.openspecimen.rde.services.BarcodeParser;
//import com.krishagni.openspecimen.rde.tokens.BarcodePart;
//import com.krishagni.openspecimen.redcap.FieldTransformer;
//import com.krishagni.openspecimen.redcap.domain.LogEvent;
//
//public class RcFieldTransformerImpl implements FieldTransformer {
//
//	@Override
//	public LogEvent transform(LogEvent event) {
//		BarcodeParser parser = new BarcodeParserImpl();
//		String barcode = event.getDataValues().get("clinical_barcode");
//		if (StringUtils.isBlank(barcode)) {
//			return event;
//		}
//
//		List<BarcodePart> parts = parser.parseVisitBarcode(barcode);
//		for (BarcodePart part : parts) {
//			String value = part.getDisplayValue();
//			if (part.getToken().equals("EVENT_CODE")) {
//				value = ((CollectionProtocolEvent)part.getValue()).getEventLabel();
//			}
//
//			event.getDataValues().put("_" + part.getToken(), value);
//		}
//
//		System.err.println("***Map dump***");
//		System.err.println(event.getDataValues());
//		System.err.println("******");
//		return event;
//	}
//
//}
