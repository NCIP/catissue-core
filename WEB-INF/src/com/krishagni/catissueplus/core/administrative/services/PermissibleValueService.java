
package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.AddPvEvent;
import com.krishagni.catissueplus.core.administrative.events.AllPvsEvent;
import com.krishagni.catissueplus.core.administrative.events.DeletePvEvent;
import com.krishagni.catissueplus.core.administrative.events.EditPvEvent;
import com.krishagni.catissueplus.core.administrative.events.GetAllPVsEvent;
import com.krishagni.catissueplus.core.administrative.events.PvAddedEvent;
import com.krishagni.catissueplus.core.administrative.events.PvDeletedEvent;
import com.krishagni.catissueplus.core.administrative.events.PvEditedEvent;
import com.krishagni.catissueplus.core.administrative.events.ValidatePvEvent;

public interface PermissibleValueService {

	PvAddedEvent createPermissibleValue(AddPvEvent event);

	PvEditedEvent updatePermissibleValue(EditPvEvent event);

	PvDeletedEvent deletePermissibleValue(DeletePvEvent event);
	
	AllPvsEvent getPermissibleValues(GetAllPVsEvent event);
	
	Boolean validate(ValidatePvEvent event);

	/*AllPvsEvent updatePvs(UploadFileEvent event);

	AllPvsEvent insertPvs(UploadFileEvent event);
*/
}
