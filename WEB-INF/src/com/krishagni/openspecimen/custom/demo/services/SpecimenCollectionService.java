package com.krishagni.openspecimen.custom.demo.services;

import com.krishagni.openspecimen.custom.demo.events.CollectSpecimensEvent;
import com.krishagni.openspecimen.custom.demo.events.SpecimensCollectedEvent;

public interface SpecimenCollectionService {
	public SpecimensCollectedEvent collect(CollectSpecimensEvent req);
}
