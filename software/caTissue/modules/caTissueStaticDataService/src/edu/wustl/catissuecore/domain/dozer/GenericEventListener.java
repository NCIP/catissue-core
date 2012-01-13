package edu.wustl.catissuecore.domain.dozer;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dozer.DozerEventListener;
import org.dozer.event.DozerEvent;
import org.dozer.fieldmap.FieldMap;

import edu.wustl.catissuecore.domain.service.WAPIUtility;

/**
 * @author Ion C. Olaru Date: 8/2/11 - 3:34 PM
 */
public class GenericEventListener implements DozerEventListener {

	static Set cache = new HashSet();

	private static Logger log = Logger.getLogger(GenericEventListener.class);

	public void mappingStarted(DozerEvent event) {
	}

	public void preWritingDestinationValue(DozerEvent event) {
	}

	public void postWritingDestinationValue(DozerEvent event) {
		Object destination = event.getDestinationObject();
		Object value = event.getDestinationValue();
		FieldMap map = event.getFieldMap();
		if (value instanceof List) {
			WAPIUtility.nullifyFieldValue("setId", "getId", Long.class, null,
					(Collection)value);
			GenericCollectionConverter.convertCollectionTypes(destination);
			GenericCollectionConverter.adjustReference(destination);
			
			map.writeDestValue(destination, new HashSet((List)value));
			
		} else 
		if (value instanceof Serializable && !(value instanceof Number) && !(value instanceof Boolean) && !(value instanceof String)) {
			WAPIUtility.nullifyFieldValue(value, "setId", "getId",
					Long.class, null);
			GenericCollectionConverter.convertCollectionTypes(value);
			GenericCollectionConverter.setBackReference(value, destination)		;
		}

		/*
		 * if (!cache.contains(destination)) { cache.add(destination);
		 * log.debug(">>>" + destination.getClass().getCanonicalName());
		 * 
		 * WAPIUtility.nullifyFieldValue(destination, "setId", "getId",
		 * Long.class, null);
		 * GenericCollectionConverter.convertCollectionTypes(destination);
		 * GenericCollectionConverter.adjustReference(destination);
		 * 
		 * 
		 * }
		 */
	}

	public void mappingFinished(DozerEvent event) {
		Object destination = event.getDestinationObject();
		WAPIUtility.nullifyFieldValue(destination, "setId", "getId",
				Long.class, null);
		GenericCollectionConverter.convertCollectionTypes(destination);
		GenericCollectionConverter.adjustReference(destination);
	}
}
