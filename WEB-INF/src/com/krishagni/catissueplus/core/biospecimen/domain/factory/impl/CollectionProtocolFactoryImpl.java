package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.services.ContainerSelectionStrategyFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.SpecimenLabelAutoPrintMode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.SpecimenLabelPrePrintMode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.VisitNamePrintMode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolSite;
import com.krishagni.catissueplus.core.biospecimen.domain.CpSpecimenLabelPrintSetting;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSiteDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpSpecimenLabelPrintSettingDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.impl.CollectionProtocolCopier;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.de.domain.DeObject;

public class CollectionProtocolFactoryImpl implements CollectionProtocolFactory {
	private DaoFactory daoFactory;
	
	private LabelGenerator ppidGenerator;

	private LabelGenerator specimenLabelGenerator;
	
	private LabelGenerator visitNameGenerator;

	private CollectionProtocolCopier cpCopier;

	private ContainerSelectionStrategyFactory containerSelFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setPpidGenerator(LabelGenerator ppidGenerator) {
		this.ppidGenerator = ppidGenerator;
	}

	public void setSpecimenLabelGenerator(LabelGenerator specimenLabelGenerator) {
		this.specimenLabelGenerator = specimenLabelGenerator;
	}
	
	public void setVisitNameGenerator(LabelGenerator visitNameGenerator) {
		this.visitNameGenerator = visitNameGenerator;
	}

	public void setCpCopier(CollectionProtocolCopier cpCopier) {
		this.cpCopier = cpCopier;
	}

	public void setContainerSelFactory(ContainerSelectionStrategyFactory containerSelFactory) {
		this.containerSelFactory = containerSelFactory;
	}

	@Override
	public CollectionProtocol createCollectionProtocol(CollectionProtocolDetail input) {
		CollectionProtocol cp = new CollectionProtocol();

		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		cp.setId(input.getId());
		setSites(input, cp, ose);
		setTitle(input, cp, ose);
		setShortTitle(input, cp, ose);
		setCode(input, cp, ose);
		setPrincipalInvestigator(input, cp, ose);
		setCoordinators(input, cp, ose);
		setDate(input, cp, ose);

		cp.setIrbIdentifier(input.getIrbId());
		cp.setEnrollment(input.getAnticipatedParticipantsCount());
		cp.setSopDocumentUrl(input.getSopDocumentUrl());
		cp.setSopDocumentName(input.getSopDocumentName());
		cp.setExtractSprText(input.getExtractSprText());
		cp.setDescriptionURL(input.getDescriptionUrl());
		cp.setConsentsWaived(input.getConsentsWaived());

		setPpidFormat(input, cp, ose);
		setLabelFormats(input, cp, ose);
		setBarcodeSetting(input, cp, ose);
		setVisitNameFmt(input, cp, ose);
		setContainerSelectionStrategy(input, cp, ose);
		setVisitNamePrintMode(input, cp, ose);
		cp.setVisitNamePrintCopies(input.getVisitNamePrintCopies());
		setSpecimenLabelPrePrintMode(input, cp, ose);
		setSpecimenLabelPrintSettings(input, cp, ose);
		setActivityStatus(input, cp, ose);
		setCollectionProtocolExtension(input, cp, ose);

		ose.checkAndThrow();
		return cp;
	}
	
	@Override
	public CollectionProtocol createCpCopy(CollectionProtocolDetail input, CollectionProtocol existing) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		CollectionProtocol cp = cpCopier.copy(existing);
		setTitle(input, cp, ose);
		setShortTitle(input, cp, ose);
		setCode(input, cp, ose);


		if (CollectionUtils.isNotEmpty(input.getCpSites())) {
			setSites(input, cp, ose);
		}
		
		if (input.getPrincipalInvestigator() != null) {
			setPrincipalInvestigator(input, cp, ose);
		}

		if (CollectionUtils.isNotEmpty(input.getCoordinators())) {
			setCoordinators(input, cp, ose);
		}
		
		if (StringUtils.isNotBlank(input.getIrbId())) {
			cp.setIrbIdentifier(input.getIrbId());
		}

		if (input.getStartDate() == null) {
			input.setStartDate(cp.getStartDate());
		}

		if (input.getEndDate() == null) {
			input.setEndDate(cp.getEndDate());
		}

		setDate(input, cp, ose);
		cp.setSopDocumentUrl(input.getSopDocumentUrl());
		cp.setSopDocumentName(input.getSopDocumentName());
		ose.checkAndThrow();
		return cp;
	}

	private void setSites(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		 if (CollectionUtils.isEmpty(input.getCpSites())) {
			 ose.addError(CpErrorCode.REPOSITORIES_REQUIRED);
			 return;
		 }
		 
		 Map<String, CollectionProtocolSiteDetail> cpSiteDetails = new HashMap<String, CollectionProtocolSiteDetail>();
		 for (CollectionProtocolSiteDetail detail: input.getCpSites()) {
			 cpSiteDetails.put(detail.getSiteName(), detail);
		 }
		 
		 if (CollectionUtils.isEmpty(cpSiteDetails.keySet())) {
			 ose.addError(CpErrorCode.REPOSITORIES_REQUIRED);
			 return;
		 }
		 
		 List<Site> repositories = daoFactory.getSiteDao().getSitesByNames(cpSiteDetails.keySet());
		 if (repositories.size() != cpSiteDetails.keySet().size()) {
			 ose.addError(CpErrorCode.INVALID_REPOSITORIES);
			 return;
		 }
		 
		 Set<CollectionProtocolSite> sites = new HashSet<CollectionProtocolSite>();
		 for (Site site: repositories) {
			 CollectionProtocolSiteDetail detail = cpSiteDetails.get(site.getName());
			 CollectionProtocolSite cpSite = new CollectionProtocolSite();
			 cpSite.setId(detail.getId());
			 cpSite.setSite(site); 
			 cpSite.setCode(StringUtils.isBlank(detail.getCode()) ? null: detail.getCode().trim());
			 cpSite.setCollectionProtocol(result);
			 
			 sites.add(cpSite);
		 }
		 
		 result.setSites(sites);
 	}

	private void setTitle(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getTitle())) {
			ose.addError(CpErrorCode.TITLE_REQUIRED);
			return;
		}

		result.setTitle(input.getTitle());
	}
	
	private void setShortTitle(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getShortTitle())) {
			ose.addError(CpErrorCode.SHORT_TITLE_REQUIRED);
			return;
		}

		result.setShortTitle(input.getShortTitle());
	}
	
	private void setCode(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		if (StringUtils.isNotBlank(input.getCode())) {
			result.setCode(input.getCode().trim());
		} else {
			result.setCode(null);
		}
	}

	private void setPrincipalInvestigator(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		UserSummary user = input.getPrincipalInvestigator();		
		if (user == null) {
			ose.addError(CpErrorCode.PI_REQUIRED);
			return;
		}
		
		User pi = getUser(user);
		if (pi == null) {
			ose.addError(CpErrorCode.PI_NOT_FOUND);
			return;
		}

		result.setPrincipalInvestigator(pi);
	}
	
	private void setCoordinators(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {		
		List<UserSummary> users = input.getCoordinators();
		if (CollectionUtils.isEmpty(users)) {
			return;
		}
		
		Set<User> coordinators = new HashSet<User>();		
		for (UserSummary user : users) {
			User coordinator = getUser(user);
			if (coordinator == null) {
				ose.addError(CpErrorCode.INVALID_COORDINATORS);
				return;
			}
			
			coordinators.add(coordinator);
		}

		result.setCoordinators(coordinators);
	}
	
	private void setDate(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		Date startDt = input.getStartDate();
		result.setStartDate(startDt);

		Date endDt = input.getEndDate();
		result.setEndDate(endDt);

		if (startDt != null && endDt != null && startDt.after(endDt)) {
			ose.addError(CpErrorCode.START_DT_GT_END_DT, startDt, endDt);
		}
	}

	private void setActivityStatus(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		String status = input.getActivityStatus();
		
		if (StringUtils.isBlank(status)) {
			result.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		} else if (Status.isValidActivityStatus(status)) {
			result.setActivityStatus(status);
		} else {
			ose.addError(ActivityStatusErrorCode.INVALID);
		}
	}
	
	private void setPpidFormat(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		String ppidFmt = ensureValidPpidFormat(input.getPpidFmt(), ose);
		result.setPpidFormat(ppidFmt);
		result.setManualPpidEnabled(input.getManualPpidEnabled());
	}

	private String ensureValidPpidFormat(String ppidFmt, OpenSpecimenException ose) {
		if (StringUtils.isNotBlank(ppidFmt) && !ppidGenerator.isValidLabelTmpl(ppidFmt)) {
			ose.addError(CpErrorCode.INVALID_PPID_FMT, ppidFmt);
		}

		return ppidFmt;
	}

	private void setLabelFormats(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		String labelFmt = ensureValidLabelFmt(input.getSpecimenLabelFmt(), CpErrorCode.INVALID_SPECIMEN_LABEL_FMT, ose);		
		result.setSpecimenLabelFormat(labelFmt);
		
		labelFmt = ensureValidLabelFmt(input.getAliquotLabelFmt(), CpErrorCode.INVALID_ALIQUOT_LABEL_FMT, ose);
		result.setAliquotLabelFormat(labelFmt);
		
		labelFmt = ensureValidLabelFmt(input.getDerivativeLabelFmt(), CpErrorCode.INVALID_DERIVATIVE_LABEL_FMT, ose);
		result.setDerivativeLabelFormat(labelFmt);
		
		result.setManualSpecLabelEnabled(input.getManualSpecLabelEnabled());
	}
	
	private String ensureValidLabelFmt(String labelFmt, ErrorCode error, OpenSpecimenException ose) {
		if (StringUtils.isNotBlank(labelFmt) && !specimenLabelGenerator.isValidLabelTmpl(labelFmt)) {
			ose.addError(error, labelFmt);
		}
		
		return labelFmt;
	}

	private void setBarcodeSetting(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		boolean barcodingEnabled = input.getBarcodingEnabled() != null ? input.getBarcodingEnabled() : false;
		if (barcodingEnabled && !result.areAllSpecimenLabelsAutoGenerated()) {
			ose.addError(CpErrorCode.CANNOT_ENABLE_BARCODING, result.getShortTitle());
		}

		result.setBarcodingEnabled(barcodingEnabled);
	}

	private void setVisitNameFmt(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		String nameFmt = ensureValidVisitNameFmt(input.getVisitNameFmt(), ose);
		result.setVisitNameFormat(nameFmt);
		result.setManualVisitNameEnabled(input.getManualVisitNameEnabled());
	}
	
	private String ensureValidVisitNameFmt(String nameFmt, OpenSpecimenException ose) {
		if (StringUtils.isNotBlank(nameFmt) && !visitNameGenerator.isValidLabelTmpl(nameFmt)) {
			ose.addError(CpErrorCode.INVALID_VISIT_NAME_FMT, nameFmt);
		}
		
		return nameFmt;
	}

	private void setContainerSelectionStrategy(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		String strategy = input.getContainerSelectionStrategy();
		if (StringUtils.isBlank(strategy)) {
			return;
		}

		if (containerSelFactory.getStrategy(strategy) == null) {
			ose.addError(CpErrorCode.INV_CONT_SEL_STRATEGY, strategy);
			return;
		}

		result.setContainerSelectionStrategy(strategy);
		result.setAliquotsInSameContainer(input.getAliquotsInSameContainer());
	}

	private void setVisitNamePrintMode(CollectionProtocolDetail input, CollectionProtocol cp, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getVisitNamePrintMode())) {
			return;
		}

		VisitNamePrintMode printMode = null;
		try {
			printMode = VisitNamePrintMode.valueOf(input.getVisitNamePrintMode());
		} catch (IllegalArgumentException iae) {
			ose.addError(CpErrorCode.INVALID_VISIT_NAME_PRINT_MODE, input.getVisitNamePrintMode());
			return;
		}

		cp.setVisitNamePrintMode(printMode);
	}

	private void setSpecimenLabelPrePrintMode(CollectionProtocolDetail input, CollectionProtocol cp, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getSpmnLabelPrePrintMode())) {
			return;
		}
		
		SpecimenLabelPrePrintMode printMode = null;
		try {
			printMode = SpecimenLabelPrePrintMode.valueOf(input.getSpmnLabelPrePrintMode());
		} catch(IllegalArgumentException iae) {
			ose.addError(CpErrorCode.INVALID_SPMN_LABEL_PRE_PRINT_MODE, input.getSpmnLabelPrePrintMode());
			return;
		}

		cp.setSpmnLabelPrePrintMode(printMode);
	}
	
	private void setSpecimenLabelPrintSettings(CollectionProtocolDetail input, CollectionProtocol cp, OpenSpecimenException ose) {
		if (CollectionUtils.isEmpty(input.getSpmnLabelPrintSettings())) {
			return;
		}

		Map<String, CpSpecimenLabelPrintSetting> settings = new HashMap<>();
		for (CpSpecimenLabelPrintSettingDetail detail : input.getSpmnLabelPrintSettings()) {
			CpSpecimenLabelPrintSetting setting = getSpecimenLabelPrintSetting(detail, cp, ose);
			if (settings.containsKey(setting.getLineage())) {
				ose.addError(CpErrorCode.DUP_PRINT_SETTING, setting.getLineage());
			}

			settings.put(setting.getLineage(), setting);
		}
		
		cp.setSpmnLabelPrintSettings(new HashSet<>(settings.values()));
	}

	private CpSpecimenLabelPrintSetting getSpecimenLabelPrintSetting(CpSpecimenLabelPrintSettingDetail settingDetail, CollectionProtocol cp, OpenSpecimenException ose) {
		CpSpecimenLabelPrintSetting setting = new CpSpecimenLabelPrintSetting();

		//
		// Lineage validation and setting
		//
		if (StringUtils.isBlank(settingDetail.getLineage())) {
			ose.addError(CpErrorCode.SPMN_LINEAGE_REQUIRED);
		} else if (!Specimen.isValidLineage(settingDetail.getLineage())) {
			ose.addError(CpErrorCode.INVALID_SPMN_LINEAGE, settingDetail.getLineage());
		} else {
			setting.setLineage(settingDetail.getLineage());
		}

		//
		// print mode validation and setting
		//
		String printMode = settingDetail.getPrintMode();
		if (StringUtils.isNotBlank(printMode)) {
			try {
				setting.setPrintMode(SpecimenLabelAutoPrintMode.valueOf(printMode));
			} catch (IllegalArgumentException iae) {
				ose.addError(CpErrorCode.INVALID_SPMN_LABEL_PRINT_MODE, printMode);
			}
		}

		setting.setCopies(settingDetail.getCopies());
		setting.setCollectionProtocol(cp);
		return setting;
	}

	private void setCollectionProtocolExtension(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		DeObject extension = DeObject.createExtension(input.getExtensionDetail(), result);
		result.setExtension(extension);
	}
	
	private User getUser(UserSummary userDetail) {
		if (userDetail == null) {
			return null;
		}

		User user = null;
		if (userDetail.getId() != null) {
			user = daoFactory.getUserDao().getById(userDetail.getId());
		} else if (StringUtils.isNotBlank(userDetail.getLoginName()) && StringUtils.isNotBlank(userDetail.getDomain())) {
			user = daoFactory.getUserDao().getUser(userDetail.getLoginName(), userDetail.getDomain());
		} else if (StringUtils.isNotBlank(userDetail.getEmailAddress())) {
			user = daoFactory.getUserDao().getUserByEmailAddress(userDetail.getEmailAddress());
		}
		
		return user;
	}
}
