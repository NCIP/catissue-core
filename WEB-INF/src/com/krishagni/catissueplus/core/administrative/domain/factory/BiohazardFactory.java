package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.Biohazard;
import com.krishagni.catissueplus.core.administrative.events.BiohazardDetails;


public interface BiohazardFactory {

	public Biohazard createBiohazard(BiohazardDetails biohazardDetails);

	public Biohazard patchBiohazard(Biohazard oldBiohazard, BiohazardDetails details);
}
