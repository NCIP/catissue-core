
package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.CreateImageEvent;
import com.krishagni.catissueplus.core.administrative.events.DeleteImageEvent;
import com.krishagni.catissueplus.core.administrative.events.GetImageEvent;
import com.krishagni.catissueplus.core.administrative.events.GotImageEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageCreatedEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.ImageUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.PatchImageEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateImageEvent;

public interface ImageService {

	public ImageCreatedEvent createImage(CreateImageEvent reqEvent);

	public ImageUpdatedEvent updateImage(UpdateImageEvent event);

	public ImageUpdatedEvent patchImage(PatchImageEvent event);

	public ImageDeletedEvent deleteImage(DeleteImageEvent reqEvent);

	public GotImageEvent getImage(GetImageEvent event);

}
