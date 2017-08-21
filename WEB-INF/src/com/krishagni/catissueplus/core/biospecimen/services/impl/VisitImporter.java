package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.SprDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class VisitImporter implements ObjectImporter<VisitDetail, VisitDetail> {

	private VisitService visitSvc;
	
	public void setVisitSvc(VisitService visitSvc) {
		this.visitSvc = visitSvc;
	}
	
	@Override
	public ResponseEvent<VisitDetail> importObject(RequestEvent<ImportObjectDetail<VisitDetail>> req) {
		try {
			ImportObjectDetail<VisitDetail> detail = req.getPayload();
			VisitDetail visitDetail = detail.getObject();
			visitDetail.setForceDelete(true);

			RequestEvent<VisitDetail> visitReq = new RequestEvent<>(visitDetail);
			ResponseEvent<VisitDetail> resp;
			if (detail.isCreate()) {
				resp = visitSvc.addVisit(visitReq);
			} else {
				resp = visitSvc.patchVisit(visitReq);
			}

			if (resp.isSuccessful()) {
				uploadSprfile(detail.getUploadedFilesDir(), resp.getPayload().getId(), visitDetail.getSprName());
			}

			return resp;
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private void uploadSprfile(String uploadedFilesDir, Long id, String sprFilename) {
		if (StringUtils.isBlank(sprFilename)) {
			return;
		}

		FileInputStream in = null;
		try {
			File file = new File(uploadedFilesDir + File.separator + sprFilename);
			in = new FileInputStream(file);

			SprDetail fileDetail = new SprDetail();
			fileDetail.setId(id);
			fileDetail.setFileIn(in);
			fileDetail.setFilename(sprFilename);
			fileDetail.setContentType(Utility.getContentType(file));

			ResponseEvent<String> resp = visitSvc.uploadSprFile(new RequestEvent<>(fileDetail));
			resp.throwErrorIfUnsuccessful();
		} catch (FileNotFoundException fnfe) {
			throw OpenSpecimenException.userError(VisitErrorCode.UNABLE_TO_LOCATE_SPR, sprFilename);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
}
