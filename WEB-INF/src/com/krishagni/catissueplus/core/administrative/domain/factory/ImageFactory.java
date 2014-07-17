
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.Image;
import com.krishagni.catissueplus.core.administrative.events.ImageDetails;
import com.krishagni.catissueplus.core.administrative.events.ImagePatchDetails;

public interface ImageFactory {

	public Image create(ImageDetails details);

	public Image patch(Image oldImage, ImagePatchDetails details);

}
