package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class StorageContainerSummary {
	private Long id;

	private String name;

	private String barcode;

	private String activityStatus;

	private String siteName;

	private String parentContainerName;

	private UserSummary createdBy;
	
	private int dimensionOneCapacity;
	
	private int dimensionTwoCapacity;
	
	private int freePositions;
	
	private List<StorageContainerSummary> childContainers;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getParentContainerName() {
		return parentContainerName;
	}

	public void setParentContainerName(String parentContainerName) {
		this.parentContainerName = parentContainerName;
	}

	public UserSummary getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserSummary createdBy) {
		this.createdBy = createdBy;
	}

	public int getDimensionOneCapacity() {
		return dimensionOneCapacity;
	}

	public void setDimensionOneCapacity(int dimensionOneCapacity) {
		this.dimensionOneCapacity = dimensionOneCapacity;
	}

	public int getDimensionTwoCapacity() {
		return dimensionTwoCapacity;
	}

	public void setDimensionTwoCapacity(int dimensionTwoCapacity) {
		this.dimensionTwoCapacity = dimensionTwoCapacity;
	}

	public int getFreePositions() {
		return freePositions;
	}

	public void setFreePositions(int freePositions) {
		this.freePositions = freePositions;
	}

	public List<StorageContainerSummary> getChildContainers() {
		return childContainers;
	}

	public void setChildContainers(List<StorageContainerSummary> childContainers) {
		this.childContainers = childContainers;
	}

	protected static void transform(StorageContainer container, StorageContainerSummary result) {
		result.setId(container.getId());
		result.setName(container.getName());
		result.setBarcode(container.getBarcode());		
		result.setActivityStatus(container.getActivityStatus());
		result.setCreatedBy(UserSummary.from(container.getCreatedBy()));
		
		result.setSiteName(container.getSite().getName());
		if (container.getParentContainer() != null) {
			result.setParentContainerName(container.getParentContainer().getName());
		}
		
		result.setDimensionOneCapacity(container.getDimensionOneCapacity());
		result.setDimensionTwoCapacity(container.getDimensionTwoCapacity());
		result.setFreePositions(container.freePositionsCount());		
	}
	
	public static StorageContainerSummary from(StorageContainer container) {
		return from(container, false);
	}
	
	public static StorageContainerSummary from(StorageContainer container, boolean includeChildren) {
		StorageContainerSummary result = new StorageContainerSummary();
		transform(container, result);
		if (includeChildren) {
			result.setChildContainers(from(container.getChildContainers(), includeChildren));
		}
		return result;
	}
	
	public static List<StorageContainerSummary> from(Collection<StorageContainer> containers) {
		return from(containers, false);
	}
	
	public static List<StorageContainerSummary> from(Collection<StorageContainer> containers, boolean includeChildren) {
		List<StorageContainerSummary> result = new ArrayList<StorageContainerSummary>();
		
		if (CollectionUtils.isEmpty(containers)) {
			return result;
		}
		
		for (StorageContainer container : containers) {
			result.add(from(container, includeChildren));
		}
		
		return result;
	}

	private static UserInfo getUserInfo(User user) {
		UserInfo userInfo = new UserInfo();
		userInfo.setLoginName(user.getLoginName());
		if (user.getAuthDomain() != null) {
			userInfo.setDomainName(user.getAuthDomain().getName());
		}
		return userInfo;
	}
}
