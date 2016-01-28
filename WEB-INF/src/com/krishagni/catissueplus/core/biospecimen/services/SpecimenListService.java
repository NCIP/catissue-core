package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.ListSpecimensDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListOp;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetails;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListSummary;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.ExportedFileDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public interface SpecimenListService {
	public ResponseEvent<List<SpecimenListSummary>> getUserSpecimenLists(RequestEvent<?> req);
	
	public ResponseEvent<SpecimenListDetails> getSpecimenList(RequestEvent<Long> req);
	
	public ResponseEvent<SpecimenListDetails> createSpecimenList(RequestEvent<SpecimenListDetails> req);
	
	public ResponseEvent<SpecimenListDetails> updateSpecimenList(RequestEvent<SpecimenListDetails> req);
	
	public ResponseEvent<ListSpecimensDetail> getListSpecimens(RequestEvent<Long> req);
	
	public ResponseEvent<SpecimenListDetails> patchSpecimenList(RequestEvent<SpecimenListDetails> req);
	
	public ResponseEvent<ListSpecimensDetail>  updateListSpecimens(RequestEvent<UpdateListSpecimensOp> req);
	
	public ResponseEvent<List<UserSummary>> shareSpecimenList(RequestEvent<ShareSpecimenListOp> req);
	
	public ResponseEvent<SpecimenListDetails> deleteSpecimenList(RequestEvent<Long> req);

	public ResponseEvent<ExportedFileDetail> exportSpecimenList(RequestEvent<EntityQueryCriteria> req);
}
