
package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.BiohazardCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.BiohazardDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.BiohazardUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.CreateBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchBiohazardEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateBiohazardEvent;

public interface BiohazardService {

	public BiohazardCreatedEvent createBiohazard(CreateBiohazardEvent reqEvent);

	public BiohazardUpdatedEvent updateBiohazard(UpdateBiohazardEvent reqEvent);

	public BiohazardUpdatedEvent patchBiohazard(PatchBiohazardEvent event);

	public BiohazardDeletedEvent deteteBiohazard(DeleteBiohazardEvent reqEvent);

}
