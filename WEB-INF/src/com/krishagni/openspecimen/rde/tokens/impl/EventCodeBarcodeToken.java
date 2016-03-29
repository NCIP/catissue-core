package com.krishagni.openspecimen.rde.tokens.impl;

import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.domain.AbstractLabelTmplToken;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;
import com.krishagni.openspecimen.rde.tokens.BarcodeToken;

public class EventCodeBarcodeToken extends AbstractLabelTmplToken implements BarcodeToken {

	@Override
	public String getName() {
		return "EVENT_CODE";
	}

	@Override
	public String getReplacement(Object object) {
		if (object instanceof Visit) {
			return ((Visit)object).getCpEvent().getCode();
		} else if (object instanceof Specimen) {
			return ((Specimen)object).getVisit().getCpEvent().getCode();
		}

		return null;
	}

	@Override
	public BarcodePart parse(Map<String, Object> contextMap, String input, int startIdx, String ... args) {
		String[] parts = input.substring(startIdx).split("-", 2);
		String eventCode = parts[0];
		
		BarcodePart result = new BarcodePart();
		result.setToken(getName());
		result.setStartIdx(startIdx);
		result.setEndIdx(startIdx + eventCode.length());
		result.setCode(eventCode);
				
		CollectionProtocol cp = (CollectionProtocol)contextMap.get("cp");
		for (CollectionProtocolEvent event : cp.getCollectionProtocolEvents()) {
			if (eventCode.equals(event.getCode())) {
				result.setValue(event);

				if (event.getEventPoint() != null) {
					if (event.getEventPoint() < 0) {
						result.setDisplayValue("-T" + (-1 * event.getEventPoint().intValue()) + ": ");
					} else {
						result.setDisplayValue("T" + event.getEventPoint().intValue() + ": ");
					}
				} else {
					result.setDisplayValue("TEOS: ");
				}

				result.setDisplayValue(result.getDisplayValue() + event.getEventLabel());
				break;
			}
		}
		 
		return result;
	}
}
