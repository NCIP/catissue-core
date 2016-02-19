package com.krishagni.catissueplus.core.administrative.services.impl;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrder.Status;
import com.krishagni.catissueplus.core.administrative.domain.DistributionOrderItem;
import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionOrderFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.DistributionProtocolErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderListCriteria;
import com.krishagni.catissueplus.core.administrative.events.DistributionOrderSummary;
import com.krishagni.catissueplus.core.administrative.events.ReturnedSpecimensDetail;
import com.krishagni.catissueplus.core.administrative.events.SpecimenReturnDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPositionDetail;
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
import com.krishagni.catissueplus.core.common.util.NumUtil;
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
			DistributionOrder order = getOrder(req.getPayload(), null);
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
			
			//
			//  Saved to obtain IDs to make distributed events
			//
			daoFactory.getDistributionOrderDao().saveOrUpdate(order, true);
			
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
			daoFactory.getDistributionOrderDao().saveOrUpdate(existingOrder, true);
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

	@Override
	@PlusTransactional
	public ResponseEvent<DistributionOrderDetail> returnSpecimens(RequestEvent<ReturnedSpecimensDetail> req) {
		try {
			ReturnedSpecimensDetail returnedSpmnsDetail = req.getPayload();

			DistributionOrder order = getOrder(returnedSpmnsDetail.getOrderId(), returnedSpmnsDetail.getOrderName());
			AccessCtrlMgr.getInstance().ensureUpdateDistributionOrderRights(order);
			if (!order.isOrderExecuted()) {
				return ResponseEvent.userError(DistributionOrderErrorCode.NOT_DISTRIBUTED, order.getName());
			}

			Map<Long, DistributionOrderItem> orderItemsMap = order.getOrderItemsMap();
			for (SpecimenReturnDetail detail : returnedSpmnsDetail.getReturnedSpecimens()) {
				DistributionOrderItem item = orderItemsMap.get(detail.getItemId());
				if (item == null) {
					throw OpenSpecimenException.userError(DistributionOrderErrorCode.ITEM_NOT_FOUND, detail.getItemId(), order.getName());
				}

				AccessCtrlMgr.getInstance().ensureCreateOrUpdateSpecimenRights(item.getSpecimen());
				setItemReturnDetail(item, detail);
				item.returnSpecimen();
			}

			daoFactory.getDistributionOrderDao().saveOrUpdate(order, true);
			return ResponseEvent.response(DistributionOrderDetail.from(order));
		} catch (OpenSpecimenException ose) {
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
	
	private List<Specimen> getValidSpecimens(DistributionProtocol dp, List<String> specimenLabels, OpenSpecimenException ose) {
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
		for (Map.Entry<String, Set<Long>> specimenSitesMapEntry : specimenSiteIdsMap.entrySet()) {
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

	private DistributionOrder getOrder(Long orderId, String orderName) {
		DistributionOrder order = null;
		Object key = null;

		if (orderId != null) {
			order = daoFactory.getDistributionOrderDao().getById(orderId);
			key = orderId;
		} else if (StringUtils.isNotBlank(orderName)) {
			order = daoFactory.getDistributionOrderDao().getOrder(orderName);
			key = orderName;
		}

		if (order == null) {
			throw OpenSpecimenException.userError(DistributionOrderErrorCode.NOT_FOUND, key);
		}

		return order;
	}

	private void setItemReturnDetail(DistributionOrderItem item, SpecimenReturnDetail returnDetail) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		ensureItemNotReturned(item, ose);

		setItemReturnQty(item, returnDetail.getQuantity(), ose);
		setItemReturnDate(item, returnDetail.getTime(), ose);
		setItemReturnUser(item, returnDetail.getUserId(), ose);
		setItemReturnPosition(item, returnDetail.getLocation(), ose);
		item.setReturnComments(returnDetail.getComments());

		ose.checkAndThrow();
	}

	private void ensureItemNotReturned(DistributionOrderItem item, OpenSpecimenException ose) {
		if (item.isReturned()) {
			ose.addError(DistributionOrderErrorCode.SPECIMEN_ALREADY_RETURNED, item.getSpecimen().getLabel());
		}
	}

	private void setItemReturnQty(DistributionOrderItem item, BigDecimal returnQty, OpenSpecimenException ose) {
		if (returnQty == null) {
			ose.addError(DistributionOrderErrorCode.QTY_REQ);
			return;
		}

		if (NumUtil.lessThan(item.getQuantity(), returnQty)) {
			ose.addError(DistributionOrderErrorCode.INVALID_RETURN_QUANTITY, item.getSpecimen().getLabel());
			return;
		}

		item.setReturnQuantity(returnQty);
	}

	private void setItemReturnDate(DistributionOrderItem item, Date returnDate, OpenSpecimenException ose) {
		if (returnDate == null) {
			ose.addError(DistributionOrderErrorCode.DATE_REQ);
			return;
		}

		if (item.getOrder().getExecutionDate().after(returnDate)) {
			ose.addError(DistributionOrderErrorCode.INVALID_RETURN_DATE, item.getSpecimen().getLabel());
			return;
		}

		item.setReturnDate(returnDate);
	}

	private void setItemReturnPosition(DistributionOrderItem item, StorageContainerPositionDetail positionDetail, OpenSpecimenException ose) {
		if (positionDetail == null) {
			return;
		}

		if (positionDetail.getContainerId() == null && StringUtils.isBlank(positionDetail.getContainerName())) {
			return;
		}

		StorageContainer container = null;
		Object key = null;
		if (positionDetail.getContainerId() != null) {
			container = daoFactory.getStorageContainerDao().getById(positionDetail.getContainerId());
			key = positionDetail.getContainerId();
		} else {
			container = daoFactory.getStorageContainerDao().getByName(positionDetail.getContainerName());
			key = positionDetail.getContainerName();
		}

		if (container == null) {
			ose.addError(StorageContainerErrorCode.NOT_FOUND, key);
			return;
		}

		if (!container.canContain(item.getSpecimen())) {
			ose.addError(StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN, container.getName(), item.getSpecimen().getLabel());
			return;
		}

		StorageContainerPosition position = container.nextAvailablePosition();
		if (position == null) {
			ose.addError(StorageContainerErrorCode.NO_FREE_SPACE, container.getName());
			return;
		}

		item.setReturnContainer(container);
		item.setReturnRow(position.getPosOne());
		item.setReturnColumn(position.getPosTwo());
	}

	private void setItemReturnUser(DistributionOrderItem item, Long userId, OpenSpecimenException ose) {
		if (userId == null) {
			ose.addError(DistributionOrderErrorCode.USER_REQ);
			return;
		}

		User user = daoFactory.getUserDao().getById(userId);
		if (user == null) {
			ose.addError(UserErrorCode.NOT_FOUND, userId);
			return;
		}

		item.setReturnUser(user);
	}

	private static final String ORDER_DISTRIBUTED_EMAIL_TMPL = "order_distributed";
}
