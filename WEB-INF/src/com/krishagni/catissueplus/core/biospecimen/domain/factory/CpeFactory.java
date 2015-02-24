package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolEventDetail;

public interface CpeFactory {
	public CollectionProtocolEvent createCpe(CollectionProtocolEventDetail cpe);
	
	public CollectionProtocolEvent createCpeCopy(CollectionProtocolEventDetail cpe, CollectionProtocolEvent existing);
}
