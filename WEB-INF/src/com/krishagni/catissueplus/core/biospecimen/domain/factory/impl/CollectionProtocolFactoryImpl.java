package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.SpecimenLabelAutoPrintMode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol.SpecimenLabelPrePrintMode;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolSite;
import com.krishagni.catissueplus.core.biospecimen.domain.CpSpecimenLabelPrintSetting;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CollectionProtocolFactory;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSiteDetail;
import com.krishagni.catissueplus.core.biospecimen.events.CpSpecimenLabelPrintSettingDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
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
	
	private LabelGenerator specimenLabelGenerator;
	
	private LabelGenerator visitNameGenerator;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setSpecimenLabelGenerator(LabelGenerator specimenLabelGenerator) {
		this.specimenLabelGenerator = specimenLabelGenerator;
	}
	
	public void setVisitNameGenerator(LabelGenerator visitNameGenerator) {
		this.visitNameGenerator = visitNameGenerator;
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
		cp.setStartDate(input.getStartDate());
		cp.setEndDate(input.getEndDate());
		setCoordinators(input, cp, ose);

		cp.setIrbIdentifier(input.getIrbId());
		cp.setPpidFormat(input.getPpidFmt());
		cp.setManualPpidEnabled(input.getManualPpidEnabled());
		cp.setEnrollment(input.getAnticipatedParticipantsCount());
		cp.setDescriptionURL(input.getDescriptionUrl());
		cp.setConsentsWaived(input.getConsentsWaived());

		setVisitNameFmt(input, cp, ose);
		setLabelFormats(input, cp, ose);		
		setSpecimenLabelPrePrintMode(input, cp, ose);
		setSpecimenLabelPrintSettings(input, cp, ose);
		setActivityStatus(input, cp, ose);
		setCollectionProtocolExtension(input, cp, ose);

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
		}
	}

	private void setPrincipalInvestigator(CollectionProtocolDetail input, CollectionProtocol result, OpenSpecimenException ose) {
		UserSummary user = input.getPrincipalInvestigator();		
		User pi = null;
		if (user != null && user.getId() != null) {
			pi = daoFactory.getUserDao().getById(user.getId());
		} else if (user != null && user.getLoginName() != null && user.getDomain() != null) {
			pi = daoFactory.getUserDao().getUser(user.getLoginName(), user.getDomain());
		} else {			
			ose.addError(CpErrorCode.PI_REQUIRED);
			return;
		}
		
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
			User coordinator = null;
			
			if (user.getId() != null) {
				coordinator = daoFactory.getUserDao().getById(user.getId());
			} else if (user.getLoginName() != null && user.getDomain() != null) {
				coordinator = daoFactory.getUserDao().getUser(user.getLoginName(), user.getDomain());
			} 
			
			if (coordinator == null) {
				ose.addError(CpErrorCode.INVALID_COORDINATORS);
				return;
			}
			
			coordinators.add(coordinator);
		}

		result.setCoordinators(coordinators);
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
			ose.addError(error);
		}
		
		return labelFmt;
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
	
	private void setSpecimenLabelPrePrintMode(CollectionProtocolDetail input, CollectionProtocol cp, OpenSpecimenException ose) {
		if (StringUtils.isBlank(input.getSpmnLabelPrePrintMode())) {
			return;
		}
		
		SpecimenLabelPrePrintMode spmnLabelPrePrintMode = null; 
		try {
			spmnLabelPrePrintMode = SpecimenLabelPrePrintMode.valueOf(input.getSpmnLabelPrePrintMode());
		} catch(IllegalArgumentException iae) {
			ose.addError(CpErrorCode.INVALID_SPMN_LABEL_PRE_PRINT_MODE, input.getSpmnLabelPrePrintMode());
			return;
		}

		cp.setSpmnLabelPrePrintMode(spmnLabelPrePrintMode);
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
}
