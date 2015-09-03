
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.bridge.MessageUtil;
import org.springframework.context.MessageSource;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.krishagni.catissueplus.core.biospecimen.ConfigParams;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitFactory;
import com.krishagni.catissueplus.core.biospecimen.events.FileDetail;
import com.krishagni.catissueplus.core.biospecimen.events.FileType;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprLockDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SprRequestDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.DocumentDeIdentifier;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.service.LabelPrinter;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

public class VisitServiceImpl implements VisitService {
	private DaoFactory daoFactory;

	private VisitFactory visitFactory;
	
	private SpecimenService specimenSvc;
	
	private ConfigurationService cfgSvc;
	
	private LabelGenerator visitNameGenerator;
	
	private static String defaultVisitSprDir;
	
	private MessageSource messageSource;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setVisitFactory(VisitFactory visitFactory) {
		this.visitFactory = visitFactory;
	}
	
	public void setSpecimenSvc(SpecimenService specimenSvc) {
		this.specimenSvc = specimenSvc;
	}
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}
	
	public void setVisitNameGenerator(LabelGenerator visitNameGenerator) {
		this.visitNameGenerator = visitNameGenerator;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> getVisit(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();			
			Visit visit = getVisit(crit.getId(), crit.getName());
			AccessCtrlMgr.getInstance().ensureReadVisitRights(visit);
			return ResponseEvent.response(VisitDetail.from(visit));			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}	
	
	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> addVisit(RequestEvent<VisitDetail> req) {
		try {
			VisitDetail respPayload = saveOrUpdateVisit(req.getPayload(), false, false);
			return ResponseEvent.response(respPayload);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> updateVisit(RequestEvent<VisitDetail> req) {
		try {
			VisitDetail respPayload = saveOrUpdateVisit(req.getPayload(), true, false);
			return ResponseEvent.response(respPayload);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> patchVisit(RequestEvent<VisitDetail> req) {
		try {
			VisitDetail respPayload = saveOrUpdateVisit(req.getPayload(), true, true);
			return ResponseEvent.response(respPayload);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			Visit visit = getVisit(crit.getId(), crit.getName());
			AccessCtrlMgr.getInstance().ensureReadVisitRights(visit);
			return ResponseEvent.response(visit.getDependentEntities());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<VisitDetail> deleteVisit(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			Visit visit = getVisit(crit.getId(), crit.getName());
			AccessCtrlMgr.getInstance().ensureDeleteVisitRights(visit);
			visit.delete();
			return ResponseEvent.response(VisitDetail.from(visit));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<VisitSpecimenDetail> collectVisitAndSpecimens(RequestEvent<VisitSpecimenDetail> req) {		
		try {
			VisitDetail inputVisit = req.getPayload().getVisit();
			VisitDetail savedVisit = saveOrUpdateVisit(inputVisit, inputVisit.getId() != null, false);			
			
			List<SpecimenDetail> specimens = req.getPayload().getSpecimens();
			setVisitId(savedVisit.getId(), specimens);
						
			RequestEvent<List<SpecimenDetail>> collectSpecimensReq = new RequestEvent<List<SpecimenDetail>>(specimens);
			ResponseEvent<List<SpecimenDetail>> collectSpecimensResp = specimenSvc.collectSpecimens(collectSpecimensReq);
			collectSpecimensResp.throwErrorIfUnsuccessful();
			
			VisitSpecimenDetail resp = new VisitSpecimenDetail();
			resp.setVisit(savedVisit);
			resp.setSpecimens(collectSpecimensResp.getPayload());
			return ResponseEvent.response(resp);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<FileDetail> getSpr(RequestEvent<SprRequestDetail> req) {
		try {
			SprRequestDetail detail = req.getPayload();
			Visit visit = getVisit(detail.getVisitId(), detail.getVisitName());
			
			AccessCtrlMgr.getInstance().ensureReadSprRights(visit);
		
			String fileName = visit.getSprName();
			if (StringUtils.isBlank(fileName)) {
				return ResponseEvent.userError(VisitErrorCode.NO_SPR_UPLOADED);
			}
			
			File file = getSprFile(visit.getId());
			if (!file.exists()) {
				return ResponseEvent.serverError(VisitErrorCode.UNABLE_TO_LOCATE_SPR);
			}
			
			if (detail.getType() != null && detail.getType().equals(FileType.PDF)) {
				file = generatePdf(file, visit);
				fileName = fileName.split("\\.")[0] + ".pdf";
			}

			FileDetail fileDetail = new FileDetail();
			fileDetail.setFile(file);
			fileDetail.setFileName(fileName);
			return ResponseEvent.response(fileDetail);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<String> uploadSprFile(RequestEvent<SprDetail> req) {
		try {
			SprDetail detail = req.getPayload();
			Visit visit = getVisit(detail.getVisitId(), null);
			ensureUpdateSprRights(visit);
			String sprText = Utility.getString(detail.getInputStream(), detail.getContentType());
			
			DocumentDeIdentifier deIdentifier = getSprDeIdentifier(); 
			if (deIdentifier != null) {
				Map<String, Object> props = Collections.<String, Object>singletonMap("visitId", detail.getVisitId());
				sprText = deIdentifier.deIdentify(sprText, props);
			} 

			File sprFile = getSprFile(visit.getId());
			FileUtils.writeStringToFile(sprFile, sprText, (String) null, false);
			
			String sprName = detail.getName(); 
			sprName = sprName.substring(0, sprName.lastIndexOf(".")) + ".txt";
			visit.updateSprName(sprName);
			return new ResponseEvent<String>(sprName);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<String> updateSprText(RequestEvent<SprDetail> req) {
		try {
			SprDetail detail = req.getPayload();
			Visit visit = getVisit(detail.getVisitId(), null);
			
			ensureUpdateSprRights(visit);
			
			File file = getSprFile(detail.getVisitId());
			if (!file.exists()) {
				return ResponseEvent.serverError(VisitErrorCode.UNABLE_TO_LOCATE_SPR);
			}
			FileUtils.writeStringToFile(file, detail.getSprText(), (String) null, false);
			return ResponseEvent.response(detail.getSprText());
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> deleteSprFile(RequestEvent<EntityQueryCriteria> req) {
		try {
			EntityQueryCriteria crit = req.getPayload();
			Visit visit = getVisit(crit.getId(), crit.getName());
		
			if (visit.isSprLocked()) {
				return ResponseEvent.userError(VisitErrorCode.LOCKED_SPR);
			}
			AccessCtrlMgr.getInstance().ensureDeleteSprRights(visit);
		
			if (StringUtils.isBlank(visit.getSprName())) {
				return ResponseEvent.userError(VisitErrorCode.NO_SPR_UPLOADED);
			}
		
			File file = getSprFile(visit.getId());
			if (!file.exists()) {
				return ResponseEvent.serverError(VisitErrorCode.UNABLE_TO_LOCATE_SPR);
			}
		
			boolean isFileDeleted = file.delete();
			if (isFileDeleted) {
				visit.setSprName(null);
			}
			return ResponseEvent.response(isFileDeleted);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@PlusTransactional
	@Override
	public ResponseEvent<SprLockDetail> updateSprLockStatus(RequestEvent<SprLockDetail> req) {
		SprLockDetail detail = req.getPayload();
		Visit visit = getVisit(detail.getVisitId(), detail.getVisitName());
		if (detail.isLocked()) {
			AccessCtrlMgr.getInstance().ensureLockSprRights(visit);
		} else {
			AccessCtrlMgr.getInstance().ensureUnlockSprRights(visit);
		}
		visit.setSprLocked(detail.isLocked());
		return ResponseEvent.response(detail);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LabelPrinter<Visit> getLabelPrinter() {
		String beanName = cfgSvc.getStrSetting(
				ConfigParams.MODULE, 
				ConfigParams.VISIT_LABEL_PRINTER, 
				"defaultVisitLabelPrinter");
		
		return (LabelPrinter<Visit>)OpenSpecimenAppCtxProvider.getAppCtx().getBean(beanName);
	}
	

	private VisitDetail saveOrUpdateVisit(VisitDetail input, boolean update, boolean partial) {		
		Visit existing = null;
		
		if (update && (input.getId() != null || StringUtils.isNotBlank(input.getName()))) {
			existing = getVisit(input.getId(), input.getName());
			AccessCtrlMgr.getInstance().ensureCreateOrUpdateVisitRights(existing);
		}
		
		if (update && existing == null) {
			throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND);
		}

		Visit visit = null;		
		if (partial) {
			visit = visitFactory.createVisit(existing, input);
		} else {
			visit = visitFactory.createVisit(input);
		}
		
		AccessCtrlMgr.getInstance().ensureCreateOrUpdateVisitRights(visit);
		
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ensureValidAndUniqueVisitName(existing, visit, ose);
		ose.checkAndThrow();
		
		if (existing == null) {
			if (visit.isMissed()) {
				visit.createMissedSpecimens();
			}
			
			existing = visit;
		} else {
			existing.update(visit);
		}
		
		existing.setNameIfEmpty();
		daoFactory.getVisitsDao().saveOrUpdate(existing);
		return VisitDetail.from(existing);		
	}

	private Visit getVisit(Long visitId, String visitName) {
		Visit visit = null;
		
		if (visitId != null) {
			visit = daoFactory.getVisitsDao().getById(visitId);
		} else if (StringUtils.isNotBlank(visitName)) {
			visit = daoFactory.getVisitsDao().getByName(visitName);
		}		
		
		if (visit == null) {
			throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND);
		}
		
		return visit;
	}
	
	private void ensureValidAndUniqueVisitName(Visit existing, Visit visit, OpenSpecimenException ose) {
		if (existing != null && 
			StringUtils.isNotBlank(existing.getName()) && 
			existing.getName().equals(visit.getName())) {
			return;
		}
		
		CollectionProtocol cp = visit.getCollectionProtocol();
		String name = visit.getName();
		
		if (StringUtils.isBlank(name)) {
			if (cp.isManualVisitNameEnabled() && visit.isCompleted()) {
				ose.addError(VisitErrorCode.NAME_REQUIRED);
			}
			
			return;
		}
		
		if (StringUtils.isNotBlank(cp.getVisitNameFormat())) {
			//
			// Visit name format is specified
			//
			
			if (!cp.isManualVisitNameEnabled()) {
				ose.addError(VisitErrorCode.MANUAL_NAME_NOT_ALLOWED);
				return;
			}


			if (!visitNameGenerator.validate(cp.getVisitNameFormat(), visit, name)) {
				ose.addError(VisitErrorCode.INVALID_NAME, name);
				return;
			}
		}
		
		if (daoFactory.getVisitsDao().getByName(name) != null) {
			ose.addError(VisitErrorCode.DUP_NAME, name);
		}		
	}
	
	private void setVisitId(Long visitId, List<SpecimenDetail> specimens) {
		if (CollectionUtils.isEmpty(specimens)) {
			return;
		}
		
		for (SpecimenDetail specimen : specimens) {
			specimen.setVisitId(visitId);
			setVisitId(visitId, specimen.getChildren());
		}
	}
	
	private File getSprFile(Long visitId) {
		String path = cfgSvc.getStrSetting(
				ConfigParams.MODULE, 
				ConfigParams.SPR_DIR, 
				getDefaultVisitSprDir());
		path = path + File.separator + visitId + File.separator + "spr.txt";
		
		return new File(path);
	}

	private DocumentDeIdentifier getSprDeIdentifier() {
		String sprDeidentifierBean = cfgSvc.getStrSetting(ConfigParams.MODULE, ConfigParams.SPR_DEIDENTIFIER);
		if (StringUtils.isBlank(sprDeidentifierBean)) {
			return null;
		}
		
		return (DocumentDeIdentifier) OpenSpecimenAppCtxProvider
				.getAppCtx()
				.getBean(sprDeidentifierBean);
	}
	
	private static String getDefaultVisitSprDir() {
		if (StringUtils.isBlank(defaultVisitSprDir)) {
			defaultVisitSprDir = ConfigUtil.getInstance().getDataDir() + File.separator + "visit-sprs";
		}
		return defaultVisitSprDir;
	}
	
	private void ensureUpdateSprRights(Visit visit) {
		if (visit.isSprLocked()) {
			throw OpenSpecimenException.userError(VisitErrorCode.LOCKED_SPR);
		}
		
		AccessCtrlMgr.getInstance().ensureCreateOrUpdateSprRights(visit);
	}
	
	private File generatePdf(File file, Visit visit) throws Exception {
		FileOutputStream out = null;
		Document document = new Document();
		try {
			File sprReport = File.createTempFile("spr-report", ".pdf");
			out= new FileOutputStream(sprReport);
			
			PdfWriter.getInstance(document, out);
			document.open();
			document.addTitle(getMessage("spr_title"));
			
			Paragraph header = new Paragraph();
			header.setAlignment(Element.ALIGN_CENTER);
			Chunk headerText = getChunk(getMessage("spr_header"), 10, true, true);
			header.add(headerText);
			document.add(header);
			document.add(Chunk.NEWLINE);
			
			Paragraph patientInfo = new Paragraph();
			Chunk patientInfoText = getChunk(getMessage("spr_patient_info"), 10, true, false);
			patientInfo.add(patientInfoText);
			document.add(patientInfo);
			PdfPTable patientInfoTbl = getHeader(visit); 
			document.add(patientInfoTbl);
		
			String fileText = Utility.getFileText(file);
			document.add(new Paragraph(fileText, getFont(8,false)));
			
			
			Chunk endReport = getChunk(getMessage("spr_end_of_report"), 6, false, true);
			Paragraph footer = new Paragraph();
			footer.setAlignment(Element.ALIGN_CENTER);
			footer.add(endReport);
			document.add(footer);
			
			return sprReport;
		} catch(Exception e) {
			throw OpenSpecimenException.serverError(e);
		} finally {
			document.close();
			out.close();
		}
	}
	
	private PdfPTable getHeader(Visit visit) {
		PdfPTable header = new PdfPTable(3);
		header.setSpacingBefore(4f);
		header.setHorizontalAlignment(Element.ALIGN_LEFT);
		header.setWidthPercentage(100);
		header.setSpacingAfter(4f);

		Map<String, String> headerInfo = new LinkedHashMap<String, String>();
		headerInfo.put(getMessage("spr_protocol_name"), visit.getCollectionProtocol().getShortTitle());
		headerInfo.put(getMessage("spr_gender"), visit.getRegistration().getParticipant().getGender());
		headerInfo.put(getMessage("spr_printed_by"),
				AuthUtil.getCurrentUser().getLastName() + ", " + AuthUtil.getCurrentUser().getFirstName());
		headerInfo.put(getMessage("spr_visit_name"), visit.getName());
		headerInfo.put(getMessage("spr_ppid"), visit.getRegistration().getPpid());
		Integer age = Utility.getAge(visit.getRegistration().getParticipant().getBirthDate());
		headerInfo.put(getMessage("spr_age"), (age != null) ? age.toString() : "-");
		headerInfo.put(getMessage("spr_printed_on_date"), Utility.getDateString(new Date()));
		headerInfo.put(getMessage("spr_participant_race"),
				Utility.stringListToCsv(visit.getRegistration().getParticipant().getRaces()));
		headerInfo.put(getMessage("spr_collection_date"), Utility.getDateString(visit.getVisitDate()));
		
		for (Map.Entry<String, String> entry : headerInfo.entrySet()) {
			Paragraph paragraph = new Paragraph();
			paragraph.add(getChunk(entry.getKey(), 8, true, false));
			
			String val = (entry.getValue() != null) ? entry.getValue() : "-";  
			paragraph.add(getChunk(val, 8, false, false));
			
			PdfPCell cell = new PdfPCell(paragraph);
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			header.addCell(cell);
		}
		
		return header;
	}
	
	private Font getFont(int size, boolean bold) {
		int font = bold ? Font.BOLD: Font.NORMAL;
		return new Font(FontFamily.HELVETICA, size, font);
	}
	
	private Chunk getChunk(String message, int fontSize, boolean bold, boolean underline) {
		Chunk chunk = new Chunk(message);
		chunk.setFont(getFont(fontSize, bold));
		chunk.setUnderline(0.1f, -2f);
		return chunk;
	}
	
	private String getMessage(String code) {
		return messageSource.getMessage(code, null, Locale.getDefault());
	}
}
