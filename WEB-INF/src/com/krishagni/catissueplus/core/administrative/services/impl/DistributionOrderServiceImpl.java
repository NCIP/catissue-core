package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.MessageSource;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder.Status;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSummary;
import com.krishagni.catissueplus.core.administrative.services.DistributionOrderService;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.VisitSpecimensQueryCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.catissueplus.core.de.domain.Filter;
import com.krishagni.catissueplus.core.de.domain.Filter.Op;
import com.krishagni.catissueplus.core.de.domain.SavedQuery;
import com.krishagni.catissueplus.core.de.events.ExecuteQueryEventOp;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;
import com.krishagni.catissueplus.core.de.services.QueryService;
import com.krishagni.rbac.common.errors.RbacErrorCode;

import edu.common.dynamicextensions.query.WideRowMode;

public class DistributionOrderServiceImpl implements DistributionOrderService {
	private DaoFactory daoFactory;

	private DistributionOrderFactory distributionFactory;
	
	private QueryService querySvc;
	
	private MessageSource messageSource;
	
	private EmailService emailService;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setDistributionFactory(DistributionOrderFactory distributionFactory) {
		this.distributionFactory = distributionFactory;
	}
	
	public void setQuerySvc(QueryService querySvc) {
		this.querySvc = querySvc;
	}
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<DistributionOrderSummary>> getOrders(RequestEvent<DistributionOrderListCriteria> req) {
		try {
			Set<Long> siteIds = AccessCtrlMgr.getInstance().getReadAccessDistributionOrderSites();
			
			if (siteIds != null && siteIds.isEmpty()) {
				return ResponseEvent.userError(RbacErrorCode.ACCESS_DENIED);
			}
			
			DistributionOrderListCriteria crit = req.getPayload();
			if (siteIds != null) {
				crit.siteIds(siteIds);
			}
						
			return ResponseEvent.response(daoFactory.getDistributionOrderDao().getOrders(crit));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> getOrder(RequestEvent<Long> req) {
		try {
			Long distributionId = req.getPayload();
			DistributionOrder order = daoFactory.getDistributionOrderDao().getById(distributionId);
			if (order == null) {
				return ResponseEvent.userError(DistributionOrderErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadDistributionOrderRights(order);
			return ResponseEvent.response(DistributionOrderDetail.from(order));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> createOrder(RequestEvent<DistributionOrderDetail> req) {
		try {
			DistributionOrderDetail detail = req.getPayload();
			DistributionOrder order = distributionFactory.createDistributionOrder(detail, Status.PENDING);
			
			AccessCtrlMgr.getInstance().ensureCreateDistributionOrderRights(order);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraints(null, order, ose);
			
			List<String> specimenLabels = Utility.<List<String>>collect(order.getOrderItems(), "specimen.label");
			getValidSpecimens(order.getDistributionProtocol(), specimenLabels, ose);
			
			ose.checkAndThrow();
			
			Status inputStatus = Status.valueOf(detail.getStatus());
			if (inputStatus == Status.EXECUTED) {
				order.distribute();
			}
			
			daoFactory.getDistributionOrderDao().saveOrUpdate(order);
			sendOrderProcessedEmail(order, null);
			return ResponseEvent.response(DistributionOrderDetail.from(order));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> updateOrder(RequestEvent<DistributionOrderDetail> req) {
		try {
			DistributionOrderDetail detail = req.getPayload();
			Long orderId = detail.getId();
			DistributionOrder existingOrder = daoFactory.getDistributionOrderDao().getById(orderId);
			if (existingOrder == null) {
				return ResponseEvent.userError(DistributionOrderErrorCode.NOT_FOUND);
			}			
			
			AccessCtrlMgr.getInstance().ensureUpdateDistributionOrderRights(existingOrder);
			DistributionOrder newOrder = distributionFactory.createDistributionOrder(detail, null);
			AccessCtrlMgr.getInstance().ensureUpdateDistributionOrderRights(newOrder);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConstraints(existingOrder, newOrder, ose);
			
			List<String> specimenLabels = Utility.<List<String>>collect(newOrder.getOrderItems(), "specimen.label");
			getValidSpecimens(newOrder.getDistributionProtocol(), specimenLabels, ose);
			
			ose.checkAndThrow();
			
			Status oldStatus = existingOrder.getStatus();
			existingOrder.update(newOrder);
			daoFactory.getDistributionOrderDao().saveOrUpdate(existingOrder);
			sendOrderProcessedEmail(existingOrder, oldStatus);
			return ResponseEvent.response(DistributionOrderDetail.from(existingOrder));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<QueryDataExportResult> exportReport(RequestEvent<Long> req) {
		try {
			Long orderId = req.getPayload();
			DistributionOrder order = daoFactory.getDistributionOrderDao().getById(orderId);
			if (order == null) {
				return ResponseEvent.userError(DistributionOrderErrorCode.NOT_FOUND);
			}
			
			AccessCtrlMgr.getInstance().ensureReadDistributionOrderRights(order);
			if (order.getDistributionProtocol().getReport() == null) {
				return ResponseEvent.userError(DistributionOrderErrorCode.RPT_TMPL_NOT_CONFIGURED);
			}
			
			return new ResponseEvent<QueryDataExportResult>(exportOrderReport(order));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<SpecimenInfo>> getSpecimens(RequestEvent<VisitSpecimensQueryCriteria> req) {
		try {
			Long dpId = req.getPayload().getDpId();
			List<String> specimenLabels = req.getPayload().getLabels();
			DistributionProtocol dp = daoFactory.getDistributionProtocolDao().getById(dpId);
			if (dp == null) {
				return ResponseEvent.userError(DistributionProtocolErrorCode.NOT_FOUND);
			}
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			List<Specimen> specimens = getValidSpecimens(dp, specimenLabels, ose);
			
			ose.checkAndThrow();
			
			return ResponseEvent.response(SpecimenInfo.from(Specimen.sortByLabels(specimens, specimenLabels)));
		} catch(OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueConstraints(DistributionOrder existingOrder, DistributionOrder newOrder, OpenSpecimenException ose) {
		if (existingOrder == null || !newOrder.getName().equals(existingOrder.getName())) {
			DistributionOrder order = daoFactory.getDistributionOrderDao().getOrder(newOrder.getName());
			if (order != null) {
				ose.addError(DistributionOrderErrorCode.DUP_NAME, newOrder.getName());
			}
		}
	}
	
	private List<Specimen> getValidSpecimens(DistributionProtocol dp, List<String> specimenLabels,
			OpenSpecimenException ose) {
		List<Pair<Long, Long>> siteCpPairs = AccessCtrlMgr.getInstance().getReadAccessSpecimenSiteCps();
		if (siteCpPairs != null && siteCpPairs.isEmpty()) {
			ose.addError(DistributionOrderErrorCode.INVALID_SPECIMENS_FOR_DP);
			return null;
		}
		
		SpecimenListCriteria crit = new SpecimenListCriteria()
			.labels(specimenLabels)
			.siteCps(siteCpPairs);
		
		List<Specimen> specimens = daoFactory.getSpecimenDao().getSpecimens(crit);
		if (specimens.size() != specimenLabels.size()) {
			ose.addError(DistributionOrderErrorCode.SPECIMEN_DOES_NOT_EXIST);
			return null;
		}
		
		Set<Long> specimenIds = Utility.<Set<Long>>collect(specimens, "id", true);
		Map<String, Set<Long>> specimenSiteIdsMap = daoFactory.getSpecimenDao().getSpecimenSites(specimenIds);
		Set<Long> orderAllowedIds = AccessCtrlMgr.getInstance().getDistributionOrderAllowedSites(dp);
		for (Map.Entry<String, Set<Long>> specimenSitesMapEntry: specimenSiteIdsMap.entrySet()) {
			if (CollectionUtils.intersection(specimenSitesMapEntry.getValue(), orderAllowedIds).isEmpty()) {
				ose.addError(DistributionOrderErrorCode.INVALID_SPECIMENS_FOR_DP);
				return null;
			}
		}
		
		return specimens;
	}
	
	private QueryDataExportResult exportOrderReport(final DistributionOrder order) {
		SavedQuery report = order.getDistributionProtocol().getReport();
		Filter filter = new Filter();
		filter.setField("Order.id");
		filter.setOp(Op.EQ);
		filter.setValues(new String[] { order.getId().toString() });
		
		ExecuteQueryEventOp execReportOp = new ExecuteQueryEventOp();
		execReportOp.setDrivingForm("Participant");
		execReportOp.setAql(report.getAql(new Filter[] { filter }));			
		execReportOp.setWideRowMode(WideRowMode.DEEP.name());
		execReportOp.setRunType("Export");
		return querySvc.exportQueryData(execReportOp, new QueryService.ExportProcessor() {			
			@Override
			public void headers(OutputStream out) {
				PrintWriter writer = new PrintWriter(out);
				writer.println(getMessage("dist_order_name")       + ", " + order.getName());
				writer.println(getMessage("dist_dp_title")         + ", " + order.getDistributionProtocol().getTitle());
				writer.println(getMessage("dist_requestor_name")   + ", " + order.getRequester().formattedName());
				writer.println(getMessage("dist_requested_date")   + ", " + Utility.getDateString(order.getExecutionDate()));
				writer.println(getMessage("dist_receiving_site")   + ", " + order.getSite().getName());
				writer.println(getMessage("dist_exported_by")      + ", " + AuthUtil.getCurrentUser().formattedName());
				writer.println(getMessage("dist_exported_on")      + ", " + Utility.getDateString(Calendar.getInstance().getTime()));
				writer.println();
				writer.flush();
			}
		});
	}
	
	private String getMessage(String code) {
		return messageSource.getMessage(code, null, Locale.getDefault());
	}
	
	private void sendOrderProcessedEmail(DistributionOrder order, Status oldStatus) {
		Status newStatus = order.getStatus();
		if (!newStatus.equals(Status.EXECUTED) || newStatus.equals(oldStatus)) {
			return;
		}

		String[] rcpts = {
			order.getRequester().getEmailAddress(),
			order.getDistributor().getEmailAddress()
		};
		String[] subjectParams = {order.getName()};

		Map<String, Object> props = new HashMap<String, Object>();
		props.put("$subject", subjectParams);
		props.put("order", order);
		emailService.sendEmail(ORDER_DISTRIBUTED_EMAIL_TMPL, rcpts, props);
	}
	
	private static final String ORDER_DISTRIBUTED_EMAIL_TMPL = "order_distributed";
}
