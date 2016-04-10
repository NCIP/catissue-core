package com.krishagni.openspecimen.rde.services.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig.Workflow;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CprErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.VisitErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;
import com.krishagni.catissueplus.core.biospecimen.events.LabelPrintJobSummary;
import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimenDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolRegistrationService;
import com.krishagni.catissueplus.core.biospecimen.services.SpecimenService;
import com.krishagni.catissueplus.core.biospecimen.services.VisitService;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.domain.LabelPrintJob;
import com.krishagni.catissueplus.core.common.domain.PrintItem;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.errors.ParameterizedError;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.de.domain.DeObject;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail.AttrDetail;
import com.krishagni.openspecimen.rde.events.BarcodeDetail;
import com.krishagni.openspecimen.rde.events.BarcodePartDetail;
import com.krishagni.openspecimen.rde.events.ContainerOccupancyDetail;
import com.krishagni.openspecimen.rde.events.ContainerOccupancyDetail.Position;
import com.krishagni.openspecimen.rde.events.ParticipantRegDetail;
import com.krishagni.openspecimen.rde.events.SpecimenAndFrozenEventDetail;
import com.krishagni.openspecimen.rde.events.SpecimenAndFrozenEventDetail.EventDetail;
import com.krishagni.openspecimen.rde.events.SpecimenPrintDetail;
import com.krishagni.openspecimen.rde.events.VisitRegDetail;
import com.krishagni.openspecimen.rde.domain.RdeError;
import com.krishagni.openspecimen.rde.services.BarcodeParser;
import com.krishagni.openspecimen.rde.services.SpecimenCollectionService;
import com.krishagni.openspecimen.rde.tokens.BarcodePart;

public class SpecimenCollectionServiceImpl implements SpecimenCollectionService {

	private CollectionProtocolRegistrationService cprSvc;
	
	private VisitService visitSvc;
	
	private SpecimenService spmnSvc;
	
	private DaoFactory daoFactory;
	
	private JdbcTemplate jdbcTmpl;

	private EmailService emailSvc;
		
	public void setCprSvc(CollectionProtocolRegistrationService cprSvc) {
		this.cprSvc = cprSvc;
	}

	public void setVisitSvc(VisitService visitSvc) {
		this.visitSvc = visitSvc;
	}
	
	public void setSpmnSvc(SpecimenService spmnSvc) {
		this.spmnSvc = spmnSvc;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setJdbcTmpl(JdbcTemplate jdbcTmpl) {
		this.jdbcTmpl = jdbcTmpl;
	}

	public void setEmailSvc(EmailService emailSvc) {
		this.emailSvc = emailSvc;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<ParticipantRegDetail>> registerParticipants(RequestEvent<List<ParticipantRegDetail>> req) {
		try {
			Map<String, List<CollectionProtocolEvent>> cpEventsMap = new HashMap<>();

			boolean error = false;
			List<ParticipantRegDetail> result = new ArrayList<>();
			for (ParticipantRegDetail regDetail : req.getPayload()) {
				result.add(regDetail = lookupOrRegisterParticipant(regDetail, cpEventsMap));
				if (StringUtils.isNotBlank(regDetail.getErrorMessage())) {
					error = true;
				}
			}

			ResponseEvent<List<ParticipantRegDetail>> resp = ResponseEvent.response(result);
			if (error) {
				resp.setError(new OpenSpecimenException(ErrorType.USER_ERROR));
			}

			return resp;
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<VisitSpecimenDetail>> getVisitsByNames(RequestEvent<List<String>> req) {
		List<Visit> visits = visitSvc.getVisitsByName(req.getPayload());
		return ResponseEvent.response(getVisitsSpecimens(visits));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<VisitSpecimenDetail>> getVisitsBySpecimens(RequestEvent<List<String>> req) {
		List<Visit> visits = visitSvc.getSpecimenVisits(req.getPayload());
		return ResponseEvent.response(getVisitsSpecimens(visits));
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<BarcodeDetail>> validateVisitNames(RequestEvent<List<String>> req) {
		try {			
			List<BarcodeDetail> result = new ArrayList<BarcodeDetail>();
			Map<String, BarcodeDetail> barcodeMap = new HashMap<String, BarcodeDetail>();
			
			BarcodeParser parser = new BarcodeParserImpl();
			for (String barcode : req.getPayload()) {
				BarcodeDetail bcDetail = new BarcodeDetail();
				bcDetail.setBarcode(barcode);
				bcDetail.setParts(BarcodePartDetail.from(parser.parseVisitBarcode(barcode)));				
				result.add(bcDetail);
				
				if (!bcDetail.isErroneous()) {
					barcodeMap.put(barcode, bcDetail);
				}
			}

			addTokenCaptions(result);

			if (barcodeMap.isEmpty()) {
				return ResponseEvent.response(result);
			}
			
			List<Visit> visits = daoFactory.getVisitsDao().getByName(barcodeMap.keySet());
			for (Visit visit : visits) {
				if (!hasCollectedSpecimens(visit)) { // TODO: Do we need this check?
					continue;
				}
				
				BarcodeDetail bcDetail = barcodeMap.get(visit.getName());
				bcDetail.setErroneous(true);
				for (BarcodePartDetail part : bcDetail.getParts()) {
					if (part.getToken().equals("EVENT_CODE")) {
						part.setErrorCode(RdeError.SPECIMENS_ALREADY_COLLECTED.name());
						break;
					}
				}
			}

			return ResponseEvent.response(result);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<VisitSpecimenDetail>> registerVisitNames(RequestEvent<List<VisitRegDetail>> req) {
		try {
			Map<String, List<BarcodePart>> barcodeParts = new LinkedHashMap<>();
			Map<String, VisitRegDetail> visitsMap = new LinkedHashMap<>();
			BarcodeParser parser = new BarcodeParserImpl();
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			for (VisitRegDetail visitReg : req.getPayload()) {
				String barcode = visitReg.getBarcode();
				List<BarcodePart> parts = parser.parseVisitBarcode(barcode);
				if (isErroneous(parts)) {
					ose.addError(RdeError.INVALID, barcode);
					continue;
				}
				
				barcodeParts.put(barcode, parts);
				visitsMap.put(barcode, visitReg);
			}
			
			ose.checkAndThrow();

			List<VisitSpecimenDetail> visits = barcodeParts
				.entrySet().stream()
				.map(e -> registerVisit(visitsMap.get(e.getKey()), e.getValue()))
				.collect(Collectors.toList());
			return ResponseEvent.response(visits);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<VisitSpecimenDetail>> registerVisits(RequestEvent<List<VisitDetail>> req) {
		try {
			List<VisitSpecimenDetail> visits = req.getPayload()
				.stream().map(v -> {
					v.setStatus(Visit.VISIT_STATUS_COMPLETED);
					return saveOrUpdateVisit(null, v);
				})
				.collect(Collectors.toList());
			return ResponseEvent.response(visits);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenDetail>> collectPrimarySpecimens(RequestEvent<List<SpecimenDetail>> req) {
		try {
			ResponseEvent<List<SpecimenDetail>> resp = spmnSvc.collectSpecimens(req);
			if (resp.isSuccessful()) {
				sendEmailNotifs(resp.getPayload());
			}

			return resp;
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ContainerOccupancyDetail> validateOccupancyEligibility(RequestEvent<ContainerOccupancyDetail> req) {
		try {
			ContainerOccupancyDetail occupancy = req.getPayload();
			String containerName = occupancy.getContainerName();
			
			StorageContainer container = daoFactory.getStorageContainerDao().getByName(containerName);
			if (container == null) {
				return ResponseEvent.userError(StorageContainerErrorCode.NOT_FOUND, containerName);
			}
			
			Map<String, CollectionProtocol> cpMap = new HashMap<>();
			for (Position pos : occupancy.getPositions()) {
				try {
					String posX = pos.getPositionX(), posY = pos.getPositionY();
					if (!isValidPosition(container, pos)) {
						setError(pos, RdeError.INVALID_CONT_POS, posX, posY);
						continue;
					}
					
					if (container.isPositionOccupied(posX, posY)) {
						setError(pos, RdeError.CONT_POS_OCCUPIED, posX, posY);
						continue;
					}
					
					String cpShortTitle = pos.getCpShortTitle();
					CollectionProtocol cp = cpMap.get(cpShortTitle);
					if (cp == null) {
						cp = daoFactory.getCollectionProtocolDao().getCpByShortTitle(cpShortTitle);
						if (cp == null) {
							setError(pos, RdeError.INVALID_CP, cpShortTitle);
							continue;
						}
						
						cpMap.put(pos.getCpShortTitle(), cp);
					} 
					
					String spmnClass = pos.getSpecimenClass();
					String spmnType = pos.getType();
					if (!container.canContainSpecimen(cp, spmnClass, spmnType)) {
						setError(pos, RdeError.CANNOT_CONTAIN_SPECIMEN, cpShortTitle, spmnClass, spmnType);
					}
				} catch (OpenSpecimenException ose) {
					ParameterizedError pe = ose.getErrors().iterator().next();
					setError(pos, pe.error(), pe.params());
				}
			}
			
			return ResponseEvent.response(occupancy);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<SpecimenAndFrozenEventDetail> collectChildSpecimens(RequestEvent<SpecimenAndFrozenEventDetail> req) {
		try {		
			SpecimenAndFrozenEventDetail detail = req.getPayload();
			
			//
			// Step 1: collect child specimens
			//
			List<SpecimenDetail> specimens = response(spmnSvc.collectSpecimens(request(detail.getSpecimens())));

			//
			// Step 2: add frozen events
			//
			addFrozenEvents(getReqSpecimenIdMap(specimens), detail.getEvents());
			
			//
			// Step 3: construct response
			//			
			SpecimenAndFrozenEventDetail respDetail = new SpecimenAndFrozenEventDetail();
			respDetail.setSpecimens(specimens);
			respDetail.setEvents(detail.getEvents());
			return ResponseEvent.response(respDetail);			
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@PlusTransactional
	@Override
	public ResponseEvent<LabelPrintJobSummary> printSpecimenLabels(RequestEvent<List<SpecimenPrintDetail>> req) {
		try {
			List<PrintItem<Specimen>> printItems = getSpecimenPrintItems(req.getPayload());
			LabelPrintJob printJob = spmnSvc.getLabelPrinter().print(printItems);
			return ResponseEvent.response(LabelPrintJobSummary.from(printJob));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	private ParticipantRegDetail lookupOrRegisterParticipant(ParticipantRegDetail detail, Map<String, List<CollectionProtocolEvent>> cpEventsMap) {
		try {
			String cpShortTitle = detail.getCpShortTitle();
			if (StringUtils.isBlank(cpShortTitle)) {
				throw OpenSpecimenException.userError(CprErrorCode.CP_REQUIRED);
			}

			CollectionProtocolRegistration cpr = null;
			if (StringUtils.isNotBlank(detail.getPpid())) {
				cpr = daoFactory.getCprDao().getCprByCpShortTitleAndPpid(cpShortTitle, detail.getPpid());
			}

			if (cpr == null && StringUtils.isNotBlank(detail.getEmpi())) {
				cpr = daoFactory.getCprDao().getCprByCpShortTitleAndEmpi(cpShortTitle, detail.getEmpi());
			}

			if (cpr == null) {
				cpr = registerParticipant(detail);
			}

			detail.setCprId(cpr.getId());
			detail.setCpId(cpr.getCollectionProtocol().getId());
			detail.setCpShortTitle(cpShortTitle);
			detail.setEmpi(cpr.getParticipant().getEmpi());
			detail.setPpid(cpr.getPpid());
			detail.setNextEventLabel(getNextVisitEvent(cpShortTitle, cpr, cpEventsMap));
		} catch (OpenSpecimenException ose) {
			detail.setErrorMessage(ose.getMessage());
		}

		return detail;
	}

	private CollectionProtocolRegistration registerParticipant(ParticipantRegDetail detail) {
		CollectionProtocolRegistrationDetail cprDetail = new CollectionProtocolRegistrationDetail();
		cprDetail.setCpShortTitle(detail.getCpShortTitle());
		cprDetail.setPpid(detail.getPpid());
		cprDetail.setRegistrationDate(detail.getRegDate());

		ParticipantDetail participant = new ParticipantDetail();
		participant.setEmpi(detail.getEmpi());
		cprDetail.setParticipant(participant);

		cprDetail = response(cprSvc.createRegistration(request(cprDetail)));
		return daoFactory.getCprDao().getById(cprDetail.getId());
	}

	private String getNextVisitEvent(String cpShortTitle, CollectionProtocolRegistration cpr, Map<String, List<CollectionProtocolEvent>> cpEventsMap) {
		List<CollectionProtocolEvent> cpEvents = cpEventsMap.get(cpShortTitle);
		if (cpEvents == null) {
			cpEvents = cpr.getCollectionProtocol().getOrderedCpeList();
			cpEventsMap.put(cpShortTitle, cpEvents);
		}

		if (cpEvents.isEmpty()) {
			return null;
		}

		Visit lastVisit = cpr.getLatestVisit();
		CollectionProtocolEvent nextVisitEvent = null;
		if (lastVisit == null) {
			nextVisitEvent = cpEvents.iterator().next();
		} else {
			boolean breakNext = false;
			for (CollectionProtocolEvent cpe : cpEvents) {
				if (breakNext) {
					nextVisitEvent = cpe;
					break;
				}

				if (cpe.equals(lastVisit.getCpEvent())) {
					breakNext = true;
				}
			}
		}

		return nextVisitEvent == null ? null : nextVisitEvent.getEventLabel();
	}

	private List<VisitSpecimenDetail> getVisitsSpecimens(List<Visit> visits) {
		return visits.stream().map(visit -> new VisitSpecimenDetail(visit)).collect(Collectors.toList());
	}

	private void addTokenCaptions(Collection<BarcodeDetail> barcodes) {
		Map<String, List<BarcodePartDetail>> attrBcParts = new HashMap<>();

		for (BarcodeDetail barcode : barcodes) {
			for (BarcodePartDetail part : barcode.getParts()) {
				if (!part.getToken().startsWith("$extn.")) {
					part.setCaption(getTokenCaption(part.getToken()));
				} else {
					String attr = part.getToken().substring("$extn.".length());
					List<BarcodePartDetail> bcParts = attrBcParts.get(attr);
					if (bcParts == null) {
						bcParts = new ArrayList<>();
						attrBcParts.put(attr, bcParts);
					}

					bcParts.add(part);
				}
			}
		}

		Map<String, String> attrCaptions = getAttrCaptions(attrBcParts.keySet());
		for (Map.Entry<String, String> attrCaption : attrCaptions.entrySet()) {
			List<BarcodePartDetail> bcParts = attrBcParts.get(attrCaption.getKey());
			for (BarcodePartDetail bcPart : bcParts) {
				bcPart.setCaption(attrCaption.getValue());
			}
		}
	}

	private String getTokenCaption(String token) {
		return MessageUtil.getInstance().getMessage("rde_token_" + token.toLowerCase());
	}

	private Map<String, String> getAttrCaptions(Collection<String> attrs) {
		if (CollectionUtils.isEmpty(attrs)) {
			return Collections.emptyMap();
		}

		String sql = String.format(GET_PV_ATTR_CAPTIONS_SQL, getSqlParamPlaceholders(attrs.size()));
		return jdbcTmpl.query(sql, attrs.toArray(new String[0]), new ResultSetExtractor<Map<String, String>>() {
			@Override
			public Map<String, String> extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				Map<String, String> result = new HashMap<String, String>();
				while (rs.next()) {
					result.put(rs.getString("attr"), rs.getString("caption"));
				}

				return result;
			}
		});
	}

	private String getSqlParamPlaceholders(int count) {
		StringBuilder placeholders = new StringBuilder();
		for (int i = 0; i < count; ++i) {
			placeholders.append("?, ");
		}

		return placeholders.delete(placeholders.length() - 2, placeholders.length()).toString();
	}

	private boolean hasCollectedSpecimens(Visit visit) {
		boolean hasCollectedSpecimens = false;
		for (Specimen specimen : visit.getTopLevelSpecimens()) {
			if (specimen.isCollected() || specimen.isMissed()) {
				hasCollectedSpecimens = true;
				break;
			}
		}

		return hasCollectedSpecimens;
	}

	private VisitSpecimenDetail registerVisit(VisitRegDetail regDetail, List<BarcodePart> parts) {
		CollectionProtocol cp = getCp(parts);
		String ppid = getPpid(parts);
		String siteName = getCpSiteName(parts);
				
		VisitDetail visitDetail = new VisitDetail();		
		visitDetail.setCpShortTitle(cp.getShortTitle());
		visitDetail.setPpid(ppid);
		visitDetail.setCohort(getCohort(parts));
		
		CollectionProtocolRegistration cpr = daoFactory.getCprDao().getCprByPpid(cp.getId(), ppid);
		if (cpr == null) {
			Long cprId = registerParticipant(cp.getId(), ppid, regDetail.getVisitDate(), siteName);
			visitDetail.setCprId(cprId);
			cpr = daoFactory.getCprDao().getById(cprId);
		} else {
			visitDetail.setCprId(cpr.getId());
		}

		String visitName = regDetail.getBarcode();
		Visit existingVisit = cpr.getVisitByName(visitName);
		if (existingVisit != null) {
			visitDetail = VisitDetail.from(existingVisit);
		}

		CollectionProtocolEvent cpe = getCpe(parts); 
		visitDetail.setEventId(cpe.getId());
		visitDetail.setName(visitName);
		visitDetail.setSite(siteName);
		visitDetail.setStatus(Visit.VISIT_STATUS_COMPLETED);
		visitDetail.setVisitDate(regDetail.getVisitDate());
		setExtensionAttrs(visitDetail, parts);
		return saveOrUpdateVisit(cpe, visitDetail);		
	}

	private CollectionProtocol getCp(List<BarcodePart> parts) {
		BarcodePart cpPart = getBarcodePart(parts, "CP_CODE");
		return (CollectionProtocol)cpPart.getValue();
	}

	private String getPpid(List<BarcodePart> parts) {
		BarcodePart ppidPart = getBarcodePart(parts, "PPI");
		return (String)ppidPart.getValue();

	}

	private String getCohort(List<BarcodePart> parts) {
		BarcodePart cohortPart = getBarcodePart(parts, "COHORT");
		if (cohortPart == null) {
			return null;
		}

		return (String)cohortPart.getDisplayValue();
	}

	private String getCpSiteName(List<BarcodePart> parts) {
		BarcodePart sitePart = getBarcodePart(parts, "CP_SITE_CODE");

		String siteName = null;
		if (sitePart != null) {
			siteName = ((Site)sitePart.getValue()).getName();
		}

		return siteName;
	}

	private CollectionProtocolEvent getCpe(List<BarcodePart> parts) {
		BarcodePart cpePart = getBarcodePart(parts, "EVENT_CODE");
		if (cpePart == null) {
			return null;
		}

		return (CollectionProtocolEvent)cpePart.getValue();
	}

	private BarcodePart getBarcodePart(List<BarcodePart> parts, String key) {
		for (BarcodePart part : parts) {
			if (part.getToken().equals(key)) {
				return part;
			}
		}

		return null;
	}

	private Long registerParticipant(Long cpId, String ppid, Date regDate, String siteName) {
		ParticipantDetail participant = new ParticipantDetail();
		if (StringUtils.isNotBlank(siteName)) {
			PmiDetail pmi = new PmiDetail();
			pmi.setSiteName(siteName);
			pmi.setMrn(ppid);
			participant.setPmis(Collections.singletonList(pmi));
		}

		CollectionProtocolRegistrationDetail cprDetail = new CollectionProtocolRegistrationDetail();
		cprDetail.setCpId(cpId);
		cprDetail.setPpid(ppid);
		cprDetail.setRegistrationDate(regDate);
		cprDetail.setParticipant(participant);
		return response(cprSvc.createRegistration(request(cprDetail))).getId();
	}

	private void setExtensionAttrs(VisitDetail visitDetail, List<BarcodePart> parts) {
		ExtensionDetail extnDetail = visitDetail.getExtensionDetail();
		if (extnDetail == null) {
			extnDetail = new ExtensionDetail();
		}

		boolean hasExtnAttrs = false;
		for (BarcodePart part : parts) {
			if (!part.getToken().startsWith("$extn.")) {
				continue;
			}

			String fqn = part.getToken().substring("$extn.".length());
			AttrDetail attr = new AttrDetail();
			attr.setName(StringUtils.split(fqn, ".")[1]);
			attr.setValue(part.getDisplayValue());

			extnDetail.getAttrs().add(attr);
			hasExtnAttrs = true;
		}

		if (hasExtnAttrs) {
			visitDetail.setExtensionDetail(extnDetail);
		}
	}

	private VisitSpecimenDetail saveOrUpdateVisit(CollectionProtocolEvent event, VisitDetail input) {
		VisitDetail visitDetail = null;
		if (input.getId() != null) {
			visitDetail = response(visitSvc.updateVisit(request(input)));
		} else {
			visitDetail = response(visitSvc.addVisit(request(input)));
		}

		Visit visit = daoFactory.getVisitsDao().getById(visitDetail.getId());
		if (!visit.isPrePrintEnabled()) {
			//
			// If CP level pre-print is not enabled, create all pending specimens with their labels
			// so that we can scan anticipated specimen labels to collect
			//
			visit.createPendingSpecimens();
		}

		return new VisitSpecimenDetail(visit);
	}

	@PlusTransactional
	private void sendEmailNotifs(List<SpecimenDetail> specimens) {
		Set<Long> cpIds = new HashSet<>();
		Set<Long> visitIds = new HashSet<>();
		for (SpecimenDetail spmn : specimens) {
			cpIds.add(spmn.getCpId());
			visitIds.add(spmn.getVisitId());
		}

		//
		// Map of cp site id -> list of recipient email IDs
		//
		Map<Long, List<String>> siteEmailRcpts = getSiteEmailRcpts(cpIds);

		//
		// Map of visit id -> {cp site id, site name}
		//
		Map<Long, Pair<Long, String>> visitSiteIdsMap = getVisitCpSitesMap(visitIds);

		//
		// Building map of {cp site id, site name} => {specimens}
		//
		Map<Pair<Long, String>, List<SpecimenDetail>> siteSpmnsMap = new HashMap<>();
		for (SpecimenDetail spmn : specimens) {
			Pair<Long, String> cpSite = visitSiteIdsMap.get(spmn.getVisitId());
			if (cpSite == null) {
				continue;
			}

			List<SpecimenDetail> siteSpmns = siteSpmnsMap.get(cpSite);
			if (siteSpmns == null) {
				siteSpmns = new ArrayList<>();
				siteSpmnsMap.put(cpSite, siteSpmns);
			}

			siteSpmns.add(spmn);
		}

		for (Map.Entry<Pair<Long, String>, List<SpecimenDetail>> siteSpmns : siteSpmnsMap.entrySet()) {
			sendEmailNotif(
					siteEmailRcpts.get(siteSpmns.getKey().first()),
					siteSpmns.getKey().second(),
					siteSpmns.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	private Map<Long, List<String>> getSiteEmailRcpts(Set<Long> cpIds) {
		Map<Long, List<String>> siteEmailRcpts = new HashMap<>();

		for (CpWorkflowConfig cpWf : getCpWorkflowsCfg(cpIds)) {
			Workflow wf = cpWf.getWorkflows().get("bde");
			Map<String, List<String>> emailNotifs = (Map<String, List<String>>)wf.getData().get("emailNotifs");
			if (emailNotifs == null) {
				continue;
			}

			for (Map.Entry<String, List<String>> notif : emailNotifs.entrySet()) {
				siteEmailRcpts.put(Long.parseLong(notif.getKey()), notif.getValue());
			}
		}

		return siteEmailRcpts;
	}

	private List<CpWorkflowConfig> getCpWorkflowsCfg(Set<Long> cpIds) {
		return daoFactory.getCollectionProtocolDao().getCpWorkflows(cpIds);
	}

	private Map<Long, Pair<Long, String>> getVisitCpSitesMap(Set<Long> visitIds) {
		String sql = String.format(GET_VISIT_CP_SITES_SQL, getSqlParamPlaceholders(visitIds.size()));
		return jdbcTmpl.query(sql, visitIds.toArray(new Long[0]), new ResultSetExtractor<Map<Long, Pair<Long, String>>>() {
			@Override
			public Map<Long, Pair<Long, String>> extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				Map<Long, Pair<Long, String>> visitSites = new HashMap<Long, Pair<Long, String>>();
				while (rs.next()) {
					Pair<Long, String> site = Pair.make(rs.getLong("siteId"), rs.getString("siteName"));
					visitSites.put(rs.getLong("visitId"), site);
				}

				return visitSites;
			}
		});
	}

	private void sendEmailNotif(List<String> rcpts, String siteName, List<SpecimenDetail> spmns) {
		if (CollectionUtils.isEmpty(rcpts)) {
			return;
		}

		Map<String, Object> props = new HashMap<>();
		props.put("cpShortTitle", spmns.iterator().next().getCpShortTitle());
		props.put("siteName", siteName);
		props.put("spmns", spmns);

		emailSvc.sendEmail(PRIMARY_SPMN_COLL_EMAIL_TMPL, rcpts.toArray(new String[0]), props);
	}

	private boolean isValidPosition(StorageContainer container, Position pos) {
		try {
			if (container.areValidPositions(pos.getPositionX(), pos.getPositionY())) {
				return true;
			}
		} catch (Exception e) {
			// invalid position
		}

		return false;
	}

	private void setError(Position pos, ErrorCode code, Object ... params) {
		pos.setErrorCode(code.code());
		pos.setErrorMsg(getMessage(code, params));
	}

	private String getMessage(ErrorCode code, Object ... params) {
		return MessageUtil.getInstance().getMessage(code.code().toLowerCase(), params);
	}

	private Map<String, Long> getReqSpecimenIdMap(List<SpecimenDetail> specimens) {
		Map<String, Long> result = new HashMap<>();

		if (specimens == null) {
			return result;
		}

		for (SpecimenDetail specimen : specimens) {
			result.put(specimen.getVisitId() + "-" + specimen.getReqId(), specimen.getId());
			result.putAll(getReqSpecimenIdMap(specimen.getChildren()));
		}

		return result;
	}

	private void addFrozenEvents(Map<String, Long> reqSpecimenIdMap, List<EventDetail> events) {
		for (EventDetail event : events) {
			Long specimenId = event.getSpecimenId();
			if (specimenId == null && event.getVisitId() != null && event.getReqId() != null) {
				specimenId = reqSpecimenIdMap.get(event.getVisitId() + "-" + event.getReqId());
				event.setSpecimenId(specimenId);
			}

			if (specimenId == null) {
				continue;
			}

			Long userId = AuthUtil.getCurrentUser().getId();
			if (event.getUser() != null && event.getUser().getId() != null) {
				userId = event.getUser().getId();
			}

			Date time = Calendar.getInstance().getTime();
			if (event.getTime() != null) {
				time = event.getTime();
			}

			event.setId(addFrozenEvent(specimenId, userId, time));
		}
	}

	private Long addFrozenEvent(Long specimenId, Long userId, Date time) {
		Map<String, Object> values = new HashMap<>();
		values.put("user", userId);
		values.put("time", time != null ? time.getTime() : null);
		return DeObject.saveFormData("SpecimenFrozenEvent", "SpecimenEvent", -1L, specimenId, values);
	}

	private boolean isErroneous(List<BarcodePart> parts) {
		for (BarcodePart part : parts) {
			if (part.getValue() == null) {
				return true;
			}
		}

		return false;
	}

	private List<PrintItem<Specimen>> getSpecimenPrintItems(List<SpecimenPrintDetail> spmnsPrintDetail) {
		List<PrintItem<Specimen>> result = new ArrayList<>();
		for (SpecimenPrintDetail spmnPrintDetail : spmnsPrintDetail) {
			result.addAll(getSpecimenPrintItems(spmnPrintDetail));
		}

		return result;
	}

	private List<PrintItem<Specimen>> getSpecimenPrintItems(SpecimenPrintDetail detail) {
		List<PrintItem<Specimen>> printItems = new ArrayList<>();

		Specimen specimen = daoFactory.getSpecimenDao().getByLabel(detail.getLabel());
		if (specimen == null) {
			throw OpenSpecimenException.userError(SpecimenErrorCode.NOT_FOUND, detail.getLabel());
		}

		if (detail.getPrint() != null && detail.getPrint()) {
			printItems.add(PrintItem.make(specimen, specimen.getSpecimenRequirement().getLabelPrintCopiesToUse()));
		}

		for (SpecimenPrintDetail poolSpmnPrintDetail : detail.getSpecimensPool()) {
			printItems.addAll(getSpecimenPrintItems(poolSpmnPrintDetail));
		}

		for (SpecimenPrintDetail childSpmnPrintDetail : detail.getChildren()) {
			printItems.addAll(getSpecimenPrintItems(childSpmnPrintDetail));
		}

		return printItems;
	}

//	private List<PrintItem<Specimen>> getSpecimenPrintItems(SpecimenPrintDetail detail, Specimen parent, Specimen pooledSpecimen) {
//		List<PrintItem<Specimen>> printItems = new ArrayList<>();
//
//		Specimen specimen = getSpecimenForPrint(detail);
//		if (specimen.getId() == null) {
//			if (parent != null) {
//				specimen.setParentSpecimen(parent);
//				specimen.setVisit(parent.getVisit());
//			} else if (pooledSpecimen != null) {
//				specimen.setPooledSpecimen(pooledSpecimen);
//				specimen.setVisit(pooledSpecimen.getVisit());
//			} else if (StringUtils.isNotBlank(detail.getVisitName())) {
//				Visit visit = daoFactory.getVisitsDao().getByName(detail.getVisitName());
//				if (visit == null) {
//					throw OpenSpecimenException.userError(VisitErrorCode.NOT_FOUND, detail.getVisitName());
//				}
//
//				specimen.setVisit(visit);
//			} else {
//				throw OpenSpecimenException.userError(VisitErrorCode.NAME_REQUIRED);
//			}
//		}
//
//		if (detail.getPrint() != null && detail.getPrint()) {
//			printItems.add(PrintItem.make(specimen, specimen.getSpecimenRequirement().getLabelPrintCopiesToUse()));
//		}
//
//		for (SpecimenPrintDetail poolSpmnPrintDetail : detail.getSpecimensPool()) {
//			printItems.addAll(getSpecimenPrintItems(poolSpmnPrintDetail, null, specimen));
//		}
//
//		for (SpecimenPrintDetail childSpmnPrintDetail : detail.getChildren()) {
//			printItems.addAll(getSpecimenPrintItems(childSpmnPrintDetail, specimen, null));
//		}
//
//		return printItems;
//	}
//
//	private Specimen getSpecimenForPrint(SpecimenPrintDetail detail) {
//		Specimen specimen = null;
//
//		if (StringUtils.isNotBlank(detail.getLabel())) {
//			specimen = daoFactory.getSpecimenDao().getByLabel(detail.getLabel());
//			if (specimen != null) {
//				return specimen;
//			}
//		}
//
//		if (detail.getReqId() != null) {
//			SpecimenRequirement sr = daoFactory.getSpecimenRequirementDao().getById(detail.getReqId());
//			if (sr == null) {
//				throw OpenSpecimenException.userError(SrErrorCode.NOT_FOUND, detail.getReqId());
//			}
//
//			specimen = sr.getSpecimen();
//			specimen.setLabel(detail.getLabel());
//		} else {
//			throw OpenSpecimenException.userError(SrErrorCode.NOT_FOUND); // TODO: change not found
//		}
//
//		return specimen;
//	}
	
	private <T> RequestEvent<T> request(T payload) {
		return new RequestEvent<>(payload);
	}

	private <T> T response(ResponseEvent<T> resp) {
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	private static final String GET_VISIT_CP_SITES_SQL =
		"select " +
		"  visit.identifier as visitId, cpSite.identifier as siteId, site.name as siteName " +
		"from " +
		"  catissue_specimen_coll_group visit " +
		"  inner join catissue_coll_prot_reg cpr on cpr.identifier = visit.collection_protocol_reg_id " +
		"  inner join catissue_collection_protocol cp on cp.identifier = cpr.collection_protocol_id " +
		"  inner join catissue_site_cp cpSite on cpSite.collection_protocol_id = cp.identifier and cpSite.site_id = visit.site_id " +
		"  inner join catissue_site site on site.identifier = cpSite.site_id " +
		"where " +
		"  visit.identifier in (%s)";

	private static final String GET_PV_ATTR_CAPTIONS_SQL =
		"select " +
		"  cde.public_id as attr, cde.long_name as caption " +
		"from "  +
		"  catissue_cde cde " +
		"where " +
		"  cde.public_id in (%s)";

	private static final String PRIMARY_SPMN_COLL_EMAIL_TMPL = "rde_primary_spmn_coll";
}
