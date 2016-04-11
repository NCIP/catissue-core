
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.DpRequirement;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.DpRequirementErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DpRequirementFactory;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderStat;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderStatListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolDetail;
import com.krishagni.catissueplus.core.administrative.events.DpRequirementDetail;
import com.krishagni.catissueplus.core.administrative.events.DprStat;
import com.krishagni.catissueplus.core.administrative.repository.DpListCriteria;
import com.krishagni.catissueplus.core.administrative.repository.DpRequirementDao;
import com.krishagni.catissueplus.core.administrative.services.DistributionProtocolService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.ObjectStateParamsResolver;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.CsvFileWriter;
import com.krishagni.catissueplus.core.common.util.CsvWriter;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.events.FormCtxtSummary;
import com.krishagni.catissueplus.core.de.events.ListEntityFormsOp;
import com.krishagni.catissueplus.core.de.repository.FormDao;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class DistributionProtocolServiceImpl implements DistributionProtocolService, ObjectStateParamsResolver {
	
	private static final Map<String, String> attrDisplayKeys = new HashMap<String, String>() {
		{
			put("specimenType", "dist_specimen_type");
			put("anatomicSite", "dist_anatomic_site");
			put("pathologyStatus", "dist_pathology_status");
		}
	};
	
	private DaoFactory daoFactory;

	private DistributionProtocolFactory distributionProtocolFactory;
	
	private DpRequirementFactory dprFactory;

	private FormDao formDao;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDistributionProtocolFactory(DistributionProtocolFactory distributionProtocolFactory) {
		this.distributionProtocolFactory = distributionProtocolFactory;
	}
	
	public void setDprFactory(DpRequirementFactory dprFactory) {
		this.dprFactory = dprFactory;
	}

	public void setFormDao(FormDao formDao) {
		this.formDao = formDao;
	}

	private DpRequirementDao getDprDao() {
		return daoFactory.getDistributionProtocolRequirementDao();
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionProtocolDetail>> getDistributionProtocols(RequestEvent<DpListCriteria> req) {
		try {
			DpListCriteria crit = req.getPayload();
			Set<Long> siteIds = AccessCtrlMgr.getInstance().getReadAccessDistributionOrderSites();
			if (siteIds != null && CollectionUtils.isEmpty(siteIds)) {
				return ResponseEvent.userError(RbacErrorCode.ACCESS_DENIED);
			}
			
			if (siteIds != null) {
				crit.siteIds(siteIds);
			}
			
			List<DistributionProtocol> dps = daoFactory.getDistributionProtocolDao().getDistributionProtocols(crit);
			List<DistributionProtocolDetail> result = DistributionProtocolDetail.from(dps);
			
			if (crit.includeStat()) {
				addDpStats(result);
			}
						
			return ResponseEvent.response(result);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> getDistributionProtocol(RequestEvent<Long> req) {
		try {
			Long protocolId = req.getPayload();
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(protocolId);
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadDPRights(existing);
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> createDistributionProtocol(RequestEvent<DistributionProtocolDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			DistributionProtocol dp = distributionProtocolFactory.createDistributionProtocol(req.getPayload());
			ensureUniqueConstraints(dp, null);
			
			daoFactory.getDistributionProtocolDao().saveOrUpdate(dp);
			dp.addOrUpdateExtension();
			return ResponseEvent.response(DistributionProtocolDetail.from(dp));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> updateDistributionProtocol(RequestEvent<DistributionProtocolDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			Long protocolId = req.getPayload().getId();
			String title = req.getPayload().getTitle();
			
			DistributionProtocol existing = null;
			if (protocolId != null) {
				existing = daoFactory.getDistributionProtocolDao().getById(protocolId);
			} else {
				existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(title);
			}
			
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
		
			DistributionProtocol distributionProtocol = distributionProtocolFactory.createDistributionProtocol(req.getPayload());
			ensureUniqueConstraints(distributionProtocol, existing);
			
			existing.update(distributionProtocol);
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			existing.addOrUpdateExtension();
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception ex) {
			return ResponseEvent.serverError(ex);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DependentEntityDetail>> getDependentEntities(RequestEvent<Long> req) {
		try {
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(existing.getDependentEntities());
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> deleteDistributionProtocol(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}

			existing.delete();
			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionProtocolDetail> updateActivityStatus(RequestEvent<DistributionProtocolDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			
			Long dpId = req.getPayload().getId();
			String status = req.getPayload().getActivityStatus();
			if (StringUtils.isBlank(status) || !Status.isValidActivityStatus(status)) {
				return ResponseEvent.userError(ActivityStatusErrorCode.INVALID);
			}
			
			DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getById(dpId);
			if (existing == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			if (existing.getActivityStatus().equals(status)) {
				return ResponseEvent.response(DistributionProtocolDetail.from(existing));
			}
			
			if (status.equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
				existing.delete();
			} else {
				existing.setActivityStatus(status);
			}

			daoFactory.getDistributionProtocolDao().saveOrUpdate(existing);
			return ResponseEvent.response(DistributionProtocolDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
		
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionOrderStat>> getOrderStats(
			RequestEvent<DistributionOrderStatListCriteria> req) {
		try {
			DistributionOrderStatListCriteria crit = req.getPayload();
			List<DistributionOrderStat> stats = getOrderStats(crit);
			
			return ResponseEvent.response(stats);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<File> exportOrderStats(RequestEvent<DistributionOrderStatListCriteria> req) {
		File tempFile = null;
		CsvWriter csvWriter = null;
		try {
			DistributionOrderStatListCriteria crit = req.getPayload();
			List<DistributionOrderStat> orderStats = getOrderStats(crit);
			
			tempFile = File.createTempFile("dp-order-stats", null);
			csvWriter = CsvFileWriter.createCsvFileWriter(tempFile);
			
			if (crit.dpId() != null && !orderStats.isEmpty()) {
				DistributionOrderStat orderStat = orderStats.get(0);
				csvWriter.writeNext(new String[] {
					MessageUtil.getInstance().getMessage("dist_dp_title"),
					orderStat.getDistributionProtocol().getTitle()
				});
			}
			
			csvWriter.writeNext(new String[] {
				MessageUtil.getInstance().getMessage("dist_exported_by"),
				AuthUtil.getCurrentUser().formattedName()
			});
			csvWriter.writeNext(new String[] {
				MessageUtil.getInstance().getMessage("dist_exported_on"),
				Utility.getDateString(Calendar.getInstance().getTime())
			});
			csvWriter.writeNext(new String[1]);
			
			String[] headers = getOrderStatsReportHeaders(crit);
			csvWriter.writeNext(headers);
			for (DistributionOrderStat stat: orderStats) {
				csvWriter.writeNext(getOrderStatsReportData(stat, crit));
			}
			
			return ResponseEvent.response(tempFile);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		} finally {
			IOUtils.closeQuietly(csvWriter);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<FormCtxtSummary> getExtensionForm() {
		try {
			List<FormCtxtSummary> forms = formDao.getFormContexts(-1L, "DistributionProtocolExtension");
			return ResponseEvent.response(CollectionUtils.isNotEmpty(forms) ? forms.get(0) : null);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<DpRequirementDetail>> getRequirements(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			Long dpId = req.getPayload();
			DistributionProtocol dp = daoFactory.getDistributionProtocolDao().getById(dpId);
			if (dp == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			List<DpRequirementDetail> reqDetails = DpRequirementDetail.from(dp.getRequirements());
			Map<Long, DprStat> distributionStat = getDprDao().getDistributionStatByDp(dpId);
			for (DpRequirementDetail reqDetail : reqDetails) {
				DprStat stat = distributionStat.get(reqDetail.getId());
				if (stat != null) {
					reqDetail.setDistributedCnt(stat.getDistributedCnt());
					reqDetail.setDistributedQty(stat.getDistributedQty());
				} else {
					reqDetail.setDistributedCnt(new Long(0));
					reqDetail.setDistributedQty(BigDecimal.ZERO);
				}
			}
			
			return ResponseEvent.response(reqDetails);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DpRequirementDetail> getRequirement(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			DpRequirement existing = getDprDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DpRequirementErrorCode.NOT_FOUND);
			}
			
			return ResponseEvent.response(DpRequirementDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DpRequirementDetail> createRequirement(RequestEvent<DpRequirementDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();

			DpRequirement dpr = dprFactory.createDistributionProtocolRequirement(req.getPayload());	

			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureSpecimenPropertyPresent(dpr, ose);
			ensureUniqueReqConstraints(null, dpr, ose);
			ose.checkAndThrow();

			getDprDao().saveOrUpdate(dpr);
			return ResponseEvent.response(DpRequirementDetail.from(dpr));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DpRequirementDetail> updateRequirement(RequestEvent<DpRequirementDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();

			Long dpReqId = req.getPayload().getId();
			DpRequirement existing = getDprDao().getById(dpReqId);
			if (existing == null) {
				return ResponseEvent.userError(DpRequirementErrorCode.NOT_FOUND);
			}

			DpRequirement newDpr = dprFactory.createDistributionProtocolRequirement(req.getPayload());
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureSpecimenPropertyPresent(newDpr, ose);
			ensureUniqueReqConstraints(existing, newDpr, ose);
			ose.checkAndThrow();

			existing.update(newDpr);
			getDprDao().saveOrUpdate(existing);
			return ResponseEvent.response(DpRequirementDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DpRequirementDetail> deleteRequirement(RequestEvent<Long> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();

			DpRequirement existing = getDprDao().getById(req.getPayload());
			if (existing == null) {
				return ResponseEvent.userError(DpRequirementErrorCode.NOT_FOUND);
			}
			
			existing.delete();
			getDprDao().saveOrUpdate(existing);
			return ResponseEvent.response(DpRequirementDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	public String getObjectName() {
		return "distributionProtocol";
	}

	@Override
	@PlusTransactional
	public Map<String, Object> resolve(String key, Object value) {
		if (key.equals("id")) {
			value = Long.valueOf(value.toString());
		}

		return daoFactory.getDistributionProtocolDao().getDpIds(key, value);
	}

	private void ensureSpecimenPropertyPresent(DpRequirement dpr, OpenSpecimenException ose) {
		if (StringUtils.isBlank(dpr.getSpecimenType()) && StringUtils.isBlank(dpr.getAnatomicSite()) &&
			CollectionUtils.isEmpty(dpr.getPathologyStatuses()) && StringUtils.isBlank(dpr.getClinicalDiagnosis())) {
			ose.addError(DpRequirementErrorCode.SPEC_PROPERTY_REQUIRED);
		}
	}

	private void ensureUniqueReqConstraints(DpRequirement oldDpr, DpRequirement newDpr, OpenSpecimenException ose) {
		if (oldDpr != null && oldDpr.equalsSpecimenGroup(newDpr)) {
			return;
		}
		
		DistributionProtocol dp = newDpr.getDistributionProtocol();
		if (dp.hasRequirement(newDpr.getSpecimenType(), newDpr.getAnatomicSite(), newDpr.getPathologyStatuses(),
			newDpr.getClinicalDiagnosis())) {
			ose.addError(DpRequirementErrorCode.ALREADY_EXISTS);
		}
	}
	
	private void addDpStats(List<DistributionProtocolDetail> dps) {
		if (CollectionUtils.isEmpty(dps)) {
			return;
		}
		
		Map<Long, DistributionProtocolDetail> dpMap = new HashMap<Long, DistributionProtocolDetail>();
		for (DistributionProtocolDetail dp : dps) {
			dpMap.put(dp.getId(), dp);
		}
				
		Map<Long, Integer> countMap = daoFactory.getDistributionProtocolDao().getSpecimensCountByDpIds(dpMap.keySet());		
		for (Map.Entry<Long, Integer> count : countMap.entrySet()) {
			dpMap.get(count.getKey()).setDistributedSpecimensCount(count.getValue());
		}		
	}
	
	private void ensureUniqueConstraints(DistributionProtocol newDp, DistributionProtocol existingDp) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		if (!isUniqueTitle(newDp, existingDp)) {
			ose.addError(DistributionProtocolErrorCode.DUP_TITLE, newDp.getTitle());
		}
		
		if (!isUniqueShortTitle(newDp, existingDp)) {
			ose.addError(DistributionProtocolErrorCode.DUP_SHORT_TITLE, newDp.getShortTitle());
		}
		
		ose.checkAndThrow();
	}
	
	private boolean isUniqueTitle(DistributionProtocol newDp, DistributionProtocol existingDp) {
		if (existingDp != null && newDp.getTitle().equals(existingDp.getTitle())) {
			return true;
		}
		
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getDistributionProtocol(newDp.getTitle());
		if (existing != null) {
			return false;
		}
		
		return true;
	}

	private boolean isUniqueShortTitle(DistributionProtocol newDp, DistributionProtocol existingDp) {
		if (existingDp != null && newDp.getShortTitle().equals(existingDp.getShortTitle())) {
			return true;
		}
		
		DistributionProtocol existing = daoFactory.getDistributionProtocolDao().getByShortTitle(newDp.getShortTitle());
		if (existing != null) {
			return false;
		}
		
		return true;
	}

	private List<DistributionOrderStat> getOrderStats(DistributionOrderStatListCriteria crit) {
		if (crit.dpId() != null) {
			DistributionProtocol dp = daoFactory.getDistributionProtocolDao().getById(crit.dpId());
			if (dp == null) {
				throw OpenSpecimenException.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadDPRights(dp);
		} else {
			Set<Long> siteIds = AccessCtrlMgr.getInstance().getCreateUpdateAccessDistributionOrderSiteIds();
			if (siteIds != null && CollectionUtils.isEmpty(siteIds)) {
				throw OpenSpecimenException.userError(RbacErrorCode.ACCESS_DENIED);
			}
			
			if (siteIds != null) {
				crit.siteIds(siteIds);
			}
		}
		
		return daoFactory.getDistributionProtocolDao().getOrderStats(crit);
	}
	
	private String[] getOrderStatsReportHeaders(DistributionOrderStatListCriteria crit) {
		List<String> headers = new ArrayList<String>();
		if (crit.dpId() == null) {
			headers.add(MessageUtil.getInstance().getMessage("dist_dp_title"));
		}
		
		headers.add(MessageUtil.getInstance().getMessage("dist_order_name"));
		headers.add(MessageUtil.getInstance().getMessage("dist_distribution_date"));
		for (String attr: crit.groupByAttrs()) {
			headers.add(MessageUtil.getInstance().getMessage(attrDisplayKeys.get(attr)));
		}
		
		headers.add(MessageUtil.getInstance().getMessage("dist_specimen_distributed"));
		return headers.toArray(new String[0]);
	}
	
	private String [] getOrderStatsReportData(DistributionOrderStat stat, DistributionOrderStatListCriteria crit) {
		List<String> data = new ArrayList<String>();
		if (crit.dpId() == null) {
			data.add(stat.getDistributionProtocol().getShortTitle());
		}
		
		data.add(stat.getName());
		data.add(Utility.getDateString(stat.getExecutionDate()));
		for (String attr: crit.groupByAttrs()) {
			data.add(stat.getGroupByAttrVals().get(attr).toString());
		}
		
		data.add(stat.getDistributedSpecimenCount().toString());
		
		return data.toArray(new String[0]);
	}
}
