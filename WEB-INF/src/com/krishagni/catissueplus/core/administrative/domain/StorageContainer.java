
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.dependency.StorageContainerDependencyChecker;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

@Configurable
public class StorageContainer extends BaseEntity {
	public static final String NUMBER_LABELING_SCHEME = "Numbers";
	
	public static final String UPPER_CASE_ALPHA_LABELING_SCHEME = "Alphabets Upper Case";
	
	public static final String LOWER_CASE_ALPHA_LABELING_SCHEME = "Alphabets Lower Case";
	
	public static final String UPPER_CASE_ROMAN_LABELING_SCHEME = "Roman Upper Case";
	
	public static final String LOWER_CASE_ROMAN_LABELING_SCHEME = "Roman Lower Case";
	
	private String name;

	private String barcode;

	private Double temperature;
	
	private int dimensionOneCapacity;
	
	private int dimensionTwoCapacity;
	
	private String dimensionOneLabelingScheme = NUMBER_LABELING_SCHEME;
	
	private String dimensionTwoLabelingScheme = NUMBER_LABELING_SCHEME;
	
	private Site site;

	private StorageContainer parentContainer;

	private User createdBy;

	private String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
	
	private String comments;
	
	private Set<StorageContainer> childContainers = new HashSet<StorageContainer>();
	
	private Set<StorageContainer> ancestorContainers = new HashSet<StorageContainer>();
	
	private Set<StorageContainer> descendentContainers = new HashSet<StorageContainer>();
	
	//
	// all types of these specimen classes are allowed
	//
	private Set<String> allowedSpecimenClasses = new HashSet<String>(); 
	
	private Set<String> allowedSpecimenTypes = new HashSet<String>();
	
	private Set<CollectionProtocol> allowedCps = new HashSet<CollectionProtocol>();
	
	private boolean storeSpecimenEnabled = false;
			
	private StorageContainerPosition position;
	
	private Set<StorageContainerPosition> occupiedPositions = new HashSet<StorageContainerPosition>();
	
	//
	// query capabilities
	//
	private StorageContainerStats stats;
	
	private Set<String> compAllowedSpecimenClasses = new HashSet<String>();
	
	private Set<String> compAllowedSpecimenTypes = new HashSet<String>();
	
	private Set<CollectionProtocol> compAllowedCps = new HashSet<CollectionProtocol>();
	
	@Autowired
	private StorageContainerDependencyChecker dependencyChecker;
	
	public StorageContainer() {
		ancestorContainers.add(this);
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

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
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

	public String getDimensionOneLabelingScheme() {
		return dimensionOneLabelingScheme;
	}

	public void setDimensionOneLabelingScheme(String dimensionOneLabelingScheme) {
		this.dimensionOneLabelingScheme = dimensionOneLabelingScheme;
	}

	public String getDimensionTwoLabelingScheme() {
		return dimensionTwoLabelingScheme;
	}

	public void setDimensionTwoLabelingScheme(String dimensionTwoLabelingScheme) {
		this.dimensionTwoLabelingScheme = dimensionTwoLabelingScheme;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public StorageContainer getParentContainer() {
		return parentContainer;
	}

	public void setParentContainer(StorageContainer parentContainer) {
		if (this.equals(parentContainer)) {
			return;
		}
		
		StorageContainer currParent = getParentContainer();
		if (currParent != null && currParent.equals(parentContainer)) {
			return;
		}
		
		if (currParent != null) {
			getAncestorContainers().remove(currParent);
			getAncestorContainers().removeAll(currParent.getAncestorContainers());
			
			for (StorageContainer descendent : getDescendentContainers()) {
				descendent.getAncestorContainers().remove(currParent);
				descendent.getAncestorContainers().removeAll(currParent.getAncestorContainers());
			}			
		}
		
		this.parentContainer = parentContainer;
		if (parentContainer != null) {
			getAncestorContainers().add(parentContainer);
			getAncestorContainers().addAll(parentContainer.getAncestorContainers());
			for (StorageContainer descendent : getDescendentContainers()) {
				descendent.getAncestorContainers().add(parentContainer);
				descendent.getAncestorContainers().addAll(parentContainer.getAncestorContainers());
			}
		}
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Set<StorageContainer> getChildContainers() {
		return childContainers;
	}

	public void setChildContainers(Set<StorageContainer> childContainers) {
		this.childContainers = childContainers;
	}

	public Set<StorageContainer> getAncestorContainers() {
		return ancestorContainers;
	}

	public void setAncestorContainers(Set<StorageContainer> ancestorContainers) {
		this.ancestorContainers = ancestorContainers;
	}

	public Set<StorageContainer> getDescendentContainers() {
		return descendentContainers;
	}

	public void setDescendentContainers(Set<StorageContainer> descendentContainers) {
		this.descendentContainers = descendentContainers;
	}

	public Set<String> getAllowedSpecimenClasses() {
		return allowedSpecimenClasses;
	}

	public void setAllowedSpecimenClasses(Set<String> allowedSpecimenClasses) {
		this.allowedSpecimenClasses = allowedSpecimenClasses;
	}

	public Set<String> getAllowedSpecimenTypes() {
		return allowedSpecimenTypes;
	}

	public void setAllowedSpecimenTypes(Set<String> allowedSpecimenTypes) {
		this.allowedSpecimenTypes = allowedSpecimenTypes;
	}

	public Set<CollectionProtocol> getAllowedCps() {
		return allowedCps;
	}

	public void setAllowedCps(Set<CollectionProtocol> allowedCps) {
		this.allowedCps = allowedCps;
	}

	public boolean isStoreSpecimenEnabled() {
		return storeSpecimenEnabled;
	}

	public void setStoreSpecimenEnabled(boolean storeSpecimenEnabled) {
		this.storeSpecimenEnabled = storeSpecimenEnabled;
	}

	public StorageContainerPosition getPosition() {
		return position;
	}

	public void setPosition(StorageContainerPosition position) {
		this.position = position;
	}

	public Set<StorageContainerPosition> getOccupiedPositions() {
		return occupiedPositions;
	}

	public void setOccupiedPositions(Set<StorageContainerPosition> occupiedPositions) {
		this.occupiedPositions = occupiedPositions;
	}
	
	public StorageContainerStats getStats() {
		return stats;
	}

	public void setStats(StorageContainerStats stats) {
		this.stats = stats;
	}
	
	public Set<String> getCompAllowedSpecimenClasses() {
		return compAllowedSpecimenClasses;
	}

	public void setCompAllowedSpecimenClasses(Set<String> compAllowedSpecimenClasses) {
		this.compAllowedSpecimenClasses = compAllowedSpecimenClasses;
	}

	public Set<String> getCompAllowedSpecimenTypes() {
		return compAllowedSpecimenTypes;
	}

	public void setCompAllowedSpecimenTypes(Set<String> compAllowedSpecimenTypes) {
		this.compAllowedSpecimenTypes = compAllowedSpecimenTypes;
	}

	public Set<CollectionProtocol> getCompAllowedCps() {
		return compAllowedCps;
	}

	public void setCompAllowedCps(Set<CollectionProtocol> compAllowedCps) {
		this.compAllowedCps = compAllowedCps;
	}

	public void update(StorageContainer other) {
		setName(other.name);
		setBarcode(other.barcode);
		setTemperature(other.temperature);
		updateCapacity(other.dimensionOneCapacity, other.dimensionTwoCapacity);
		updateLabelingScheme(other.dimensionOneLabelingScheme, other.dimensionTwoLabelingScheme);
		updateContainerLocation(other.site, other.parentContainer, other.position);
		updateActivityStatus(other.activityStatus);
		setComments(other.comments);
		updateAllowedSpecimenClassAndTypes(other.allowedSpecimenClasses, other.allowedSpecimenTypes);
		updateAllowedCps(other.allowedCps);
		updateStoreSpecimenEnabled(other.storeSpecimenEnabled);
		
		validateRestrictions();
	}
	
	public int freePositionsCount() {
		return dimensionOneCapacity * dimensionTwoCapacity - occupiedPositions.size();
	}
	
	public Set<Integer> occupiedPositionsOrdinals() {
		Set<Integer> result = new HashSet<Integer>();
				
		for (StorageContainerPosition pos : getOccupiedPositions()) {
			result.add((pos.getPosTwoOrdinal() - 1) * dimensionTwoCapacity + pos.getPosOneOrdinal());
		}
		
		return result;
	}
	
	public boolean areValidPositions(String posOne, String posTwo) {
		int posOneOrdinal = converters.get(getDimensionOneLabelingScheme()).toOrdinal(posOne);
		int posTwoOrdinal = converters.get(getDimensionTwoLabelingScheme()).toOrdinal(posTwo);
			
		return areValidPositions(posOneOrdinal, posTwoOrdinal);
	}
	
	public boolean areValidPositions(int posOne, int posTwo) {
		return posOne >= 1 && posOne <= getDimensionOneCapacity() && 
				posTwo >= 1 && posTwo <= getDimensionTwoCapacity();
	}
	
	public StorageContainerPosition createPosition(String posOne, String posTwo) {
		int posOneOrdinal = converters.get(getDimensionOneLabelingScheme()).toOrdinal(posOne);
		int posTwoOrdinal = converters.get(getDimensionTwoLabelingScheme()).toOrdinal(posTwo);
		return createPosition(posOneOrdinal, posOne, posTwoOrdinal, posTwo);
	}
	
	public void removePosition(StorageContainerPosition position) {
		Iterator<StorageContainerPosition> iter = getOccupiedPositions().iterator();
		while (iter.hasNext()) {
			if (iter.next().getId().equals(position.getId())) {
				iter.remove();
				break;
			}
		}		
	}
	
	public void addPosition(StorageContainerPosition position) {
		position.setContainer(this);
		getOccupiedPositions().add(position);
	}
	
	public StorageContainerPosition nextAvailablePosition() {
		Set<Integer> occupiedPositionOrdinals = occupiedPositionsOrdinals();
		
		for (int y = 1; y <= getDimensionTwoCapacity(); ++y) {
			for (int x = 1; x <= getDimensionOneCapacity(); ++x) {
				int pos = (y - 1) * getDimensionOneCapacity() + x;
				if (!occupiedPositionOrdinals.contains(pos)) {
					String posOne = converters.get(getDimensionOneLabelingScheme()).fromOrdinal(x);
					String posTwo = converters.get(getDimensionTwoLabelingScheme()).fromOrdinal(y);
					return createPosition(x, posOne, y, posTwo);
				}
			}
		}
		
		return null;
	}
		
	public boolean canSpecimenOccupyPosition(Long specimenId, String posOne, String posTwo) {
		return canOccupyPosition(true, specimenId, posOne, posTwo);
	}

	public boolean canContainerOccupyPosition(Long containerId, String posOne, String posTwo) {
		return canOccupyPosition(false, containerId, posOne, posTwo);
	}
	
	public boolean canContain(Specimen specimen) {
		return canContainSpecimen(
				specimen.getVisit().getCollectionProtocol(),
				specimen.getSpecimenClass(),
				specimen.getSpecimenType()); 
	}
	
	public boolean canContain(StorageContainer container) {
		Set<String> allowedClasses = getCompAllowedSpecimenClasses();
		if (!allowedClasses.containsAll(container.getCompAllowedSpecimenClasses())) {
			return false;
		}
		
		Set<String> allowedTypes = getCompAllowedSpecimenTypes();
		if (!allowedTypes.containsAll(container.getCompAllowedSpecimenTypes())) {
			allowedTypes = computeAllAllowedSpecimenTypes();
			if (!allowedTypes.containsAll(container.getCompAllowedSpecimenTypes())) { 
				return false;
			}			
		}
				
		if (!getCompAllowedCps().isEmpty() && 
				!getCompAllowedCps().containsAll(container.getCompAllowedCps())) {
			return false;
		}
		
		return true;
	}
	
	public boolean canContainSpecimen(CollectionProtocol cp, String specimenClass, String specimenType) {
		if (!isStoreSpecimenEnabled()) {
			return false;
		}
		
		if (!getCompAllowedSpecimenClasses().contains(specimenClass) && 
				!getCompAllowedSpecimenTypes().contains(specimenType)) {
			return false;						
		}
		
		if (!getCompAllowedCps().isEmpty() && !getCompAllowedCps().contains(cp)) {
			return false;
		}
		
		return true; 
	}

	public static boolean isValidScheme(String scheme) {
		if (StringUtils.isBlank(scheme)) {
			return false;
		}
		
		return scheme.equals(NUMBER_LABELING_SCHEME) ||
				scheme.equals(UPPER_CASE_ALPHA_LABELING_SCHEME) ||
				scheme.equals(LOWER_CASE_ALPHA_LABELING_SCHEME) ||
				scheme.equals(UPPER_CASE_ROMAN_LABELING_SCHEME) ||
				scheme.equals(LOWER_CASE_ROMAN_LABELING_SCHEME);
	}
	
	
	public void validateRestrictions() {
		StorageContainer parent = getParentContainer();		
		if (parent != null && !parent.canContain(this)) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.VIOLATES_RESTRICTIONS);
		}
		
		for (StorageContainerPosition pos : getOccupiedPositions()) {
			if (pos.getOccupyingContainer() != null) {
				pos.getOccupyingContainer().validateRestrictions();
			} else if (pos.getOccupyingSpecimen() != null) {
				if (!canContain(pos.getOccupyingSpecimen())) {
					throw OpenSpecimenException.userError(StorageContainerErrorCode.VIOLATES_RESTRICTIONS);
				}
			}
		}
	}
	
	public Set<CollectionProtocol> computeAllowedCps() {
		if (CollectionUtils.isNotEmpty(getAllowedCps()) || getParentContainer() == null) {
			return new HashSet<CollectionProtocol>(getAllowedCps());
		}
		
		return getParentContainer().computeAllowedCps();
	}
	
	public Set<String> computeAllowedSpecimenClasses() {
		if (CollectionUtils.isNotEmpty(getAllowedSpecimenTypes()) || 
				CollectionUtils.isNotEmpty(getAllowedSpecimenClasses())) {
			return new HashSet<String>(getAllowedSpecimenClasses());
		}
		
		if (getParentContainer() != null) {
			return getParentContainer().computeAllowedSpecimenClasses();
		}
				
		return new HashSet<String>(getDaoFactory().getPermissibleValueDao().getSpecimenClasses());
	}
	
	public Set<String> computeAllowedSpecimenTypes() {
		Set<String> types = new HashSet<String>();
		if (CollectionUtils.isNotEmpty(getAllowedSpecimenTypes())) {
			types.addAll(getAllowedSpecimenTypes());
		} else if (CollectionUtils.isEmpty(getAllowedSpecimenClasses()) && getParentContainer() != null) {
			types.addAll(getParentContainer().computeAllowedSpecimenTypes());			
		}
		
		return types;
	}
	
	private Set<String> computeAllAllowedSpecimenTypes() {
		Set<String> types = new HashSet<String>();
		
		if (CollectionUtils.isNotEmpty(getAllowedSpecimenTypes())) {
			types.addAll(getAllowedSpecimenTypes());
		} else if (CollectionUtils.isEmpty(getAllowedSpecimenClasses())) {
			if (getParentContainer() != null) {
				return getParentContainer().computeAllAllowedSpecimenTypes();
			}
		}

		Set<String> classes = getCompAllowedSpecimenClasses();
		if (CollectionUtils.isNotEmpty(classes)) {
			types.addAll(getDaoFactory().getPermissibleValueDao().getSpecimenTypes(classes));
		}
				
		return types;
	}
	
	public List<Map<String, Object>> getDependentEntities() {
		return dependencyChecker.getDependentEntities(this);
	}
	
	public void delete() {
		List<Map<String, Object>> dependencies = getDependentEntities();
		if (!dependencies.isEmpty()) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.REF_ENTITY_FOUND);
		}
		
		this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
	
	private StorageContainerPosition createPosition(int posOneOrdinal, String posOne, int posTwoOrdinal, String posTwo) {
		StorageContainerPosition position = new StorageContainerPosition();
		position.setPosOneOrdinal(posOneOrdinal);
		position.setPosOne(posOne);
		position.setPosTwoOrdinal(posTwoOrdinal);
		position.setPosTwo(posTwo);
		position.setContainer(this);
		
		return position;
	}
	
	private StorageContainerPosition getOccupiedPosition(int posOne, int posTwo) {
		StorageContainerPosition result = null;
		
		for (StorageContainerPosition pos : getOccupiedPositions()) {
			if (pos.getPosOneOrdinal() == posOne && pos.getPosTwoOrdinal() == posTwo) {
				result = pos;
				break;
			}
		}
		
		return result;
	}
	
	private boolean canOccupyPosition(boolean isSpecimenEntity, Long entityId, String posOne, String posTwo) {
		int posOneOrdinal = converters.get(getDimensionOneLabelingScheme()).toOrdinal(posOne);
		int posTwoOrdinal = converters.get(getDimensionTwoLabelingScheme()).toOrdinal(posTwo);
		
		if (!areValidPositions(posOneOrdinal, posTwoOrdinal)) {
			return false;
		}

		StorageContainerPosition pos = getOccupiedPosition(posOneOrdinal, posTwoOrdinal);
		if (pos == null) {
			return true; // vacant position
		} else if (entityId == null) { 
			return false; // position is not vacant and entity is new
		} else if (isSpecimenEntity) {
			return pos.getOccupyingSpecimen() != null && pos.getOccupyingSpecimen().getId().equals(entityId); 
		} else {
			return pos.getOccupyingContainer() != null && pos.getOccupyingContainer().getId().equals(entityId);
		}
	}
	
	private void updateCapacity(int newDimensionOneCapacity, int newDimensionTwoCapacity) {
		if (newDimensionOneCapacity < getDimensionOneCapacity() || 
				newDimensionTwoCapacity < getDimensionTwoCapacity()) {
			if (arePositionsOccupiedBeyondCapacity(newDimensionOneCapacity, newDimensionTwoCapacity)) {
				throw OpenSpecimenException.userError(StorageContainerErrorCode.CANNOT_SHRINK_CONTAINER);
			}
		}
		
		setDimensionOneCapacity(newDimensionOneCapacity);
		setDimensionTwoCapacity(newDimensionTwoCapacity); 
	}
	
	private void updateLabelingScheme(String newDimOneScheme, String newDimTwoScheme) {
		boolean dimOneSchemeChanged = !getDimensionOneLabelingScheme().equals(newDimOneScheme);
		boolean dimTwoSchemeChanged = !getDimensionTwoLabelingScheme().equals(newDimTwoScheme);
		
		if (!dimOneSchemeChanged && !dimTwoSchemeChanged) {
			return;
		}
		
		for (StorageContainerPosition pos : getOccupiedPositions()) {
			if (dimOneSchemeChanged) {
				pos.setPosOne(converters.get(newDimOneScheme).fromOrdinal(pos.getPosOneOrdinal()));
			}
			
			if (dimTwoSchemeChanged) {
				pos.setPosTwo(converters.get(newDimTwoScheme).fromOrdinal(pos.getPosTwoOrdinal()));
			}
		}
		
		setDimensionOneLabelingScheme(newDimOneScheme);
		setDimensionTwoLabelingScheme(newDimTwoScheme);
	}
	
	private void updateContainerLocation(Site otherSite, StorageContainer otherParentContainer, StorageContainerPosition otherPos) {
		if (otherParentContainer == null) {
			if (getParentContainer() != null) {
				getParentContainer().removePosition(position);
			}
			
			parentContainer = null;
			position = null;
			site = otherSite;
		} else {
			parentContainer = otherParentContainer;
			site = otherParentContainer.getSite();
			if (cycleExistsInHierarchy(otherParentContainer)) {
				throw OpenSpecimenException.userError(StorageContainerErrorCode.HIERARCHY_CONTAINS_CYCLE);
			}
			
			if (position != null) {
				position.update(otherPos);
			} else {
				position = otherPos;
			}			
		}
	}
	
	private void updateActivityStatus(String newActivityStatus) {
		if (activityStatus.equals(newActivityStatus)) { 
			// activity status has not changed
			return;
		}
		
		if (activityStatus.equals(Status.ACTIVITY_STATUS_ACTIVE.getStatus()) && hasSpecimen(this)) {
			//
			// existing status was active and container has some specimens stored in hierarchy
			//
			throw OpenSpecimenException.userError(StorageContainerErrorCode.REF_ENTITY_FOUND);
		}

		List<StorageContainer> containers = new ArrayList<StorageContainer>();
		containers.add(this);
		while (!containers.isEmpty()) {
			StorageContainer container = containers.remove(0);
			container.setActivityStatus(newActivityStatus);
			
			containers.addAll(container.getChildContainers());
		}
	}

	private void updateAllowedSpecimenClassAndTypes(Set<String> newSpecimenClasses, Set<String> newSpecimenTypes) {
		boolean modified = false;
		
		if (!CollectionUtils.isEqualCollection(getAllowedSpecimenClasses(), newSpecimenClasses)) {
			getAllowedSpecimenClasses().clear();
			getAllowedSpecimenClasses().addAll(newSpecimenClasses);
			modified = true;			
		}
		
		if (!CollectionUtils.isEqualCollection(getAllowedSpecimenTypes(), newSpecimenTypes)) {
			getAllowedSpecimenTypes().clear();
			getAllowedSpecimenTypes().addAll(newSpecimenTypes);
			modified = true;			
		}
		
		if (modified) {
			for (StorageContainer desc : getDescendentContainers()) {
				desc.getCompAllowedSpecimenClasses().clear();
				desc.getCompAllowedSpecimenClasses().addAll(desc.computeAllowedSpecimenClasses());
				
				desc.getCompAllowedSpecimenTypes().clear();
				desc.getCompAllowedSpecimenTypes().addAll(desc.computeAllowedSpecimenTypes());				
			}
		}
				
	}
	
	private void updateAllowedCps(Set<CollectionProtocol> newCps) {
		if (CollectionUtils.isEqualCollection(getAllowedCps(), newCps)) {
			return;
		}
		
		getAllowedCps().clear();
		getAllowedCps().addAll(newCps);
		
		for (StorageContainer desc : getDescendentContainers()) {
			desc.getCompAllowedCps().clear();
			desc.getCompAllowedCps().addAll(desc.computeAllowedCps());
		}
	}
	
	private void updateStoreSpecimenEnabled(boolean newStoreSpecimenEnabled) {
		this.storeSpecimenEnabled = newStoreSpecimenEnabled;
	}
		
	private boolean arePositionsOccupiedBeyondCapacity(int dimensionOneCapacity, int dimensionTwoCapacity) {
		boolean result = false;
		for (StorageContainerPosition pos : getOccupiedPositions()) {
			if (pos.getPosOneOrdinal() > dimensionOneCapacity) {
				result = true;
				break;
			}
			
			if (pos.getPosTwoOrdinal() > dimensionTwoCapacity) {
				result = true;
				break;
			}
		}
		
		return result;
	}

	private boolean hasSpecimen(StorageContainer container) {
		for (StorageContainerPosition pos : getOccupiedPositions()) {
			if (pos.getOccupyingSpecimen() != null) {
				return true;
			}
		}
		
		for (StorageContainer child : getChildContainers()) {
			if (hasSpecimen(child)) {
				return true;
			}
		}
		
		return false;
	}
		
	private boolean cycleExistsInHierarchy(StorageContainer parentContainer) {
		if (parentContainer != null && getId().equals(parentContainer.getId())) {
			return true;
		}
		
		for (StorageContainer child : getChildContainers()) {
			if (parentContainer.isDescendentOf(child)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isDescendentOf(StorageContainer other) {
		if (getId() == null || other == null || other.getId() == null) {
			return false;
		}
		
		StorageContainer container = this;
		while (container != null) {
			if (other.getId().equals(container.getId())) {
				return true;
			}
			
			container = container.getParentContainer();
		}
		
		return false;
	}
	
	//
	// Unfortunately @Configurable is not working for objects created by 
	// hibernate and java assist
	//
	private DaoFactory getDaoFactory() {
		return (DaoFactory)OpenSpecimenAppCtxProvider.getAppCtx().getBean("biospecimenDaoFactory");
	}
	
	private Map<String, SchemeOrdinalConverter> converters = new HashMap<String, SchemeOrdinalConverter>() {
		private static final long serialVersionUID = -1198152629671796530L;

		{
			put("Numbers", new NumberSchemeOrdinalConverter());
			put("Alphabets Upper Case", new AlphabetSchemeOrdinalConverter(true));
			put("Alphabets Lower Case", new AlphabetSchemeOrdinalConverter(false));
			put("Roman Upper Case", new RomanSchemeOrdinalConverter(true));
			put("Roman Lower Case", new RomanSchemeOrdinalConverter(false));
		}
	};
	
	private interface SchemeOrdinalConverter {
		public Integer toOrdinal(String pos);
		
		public String fromOrdinal(Integer pos);
	}
	
	private class NumberSchemeOrdinalConverter implements SchemeOrdinalConverter {
		@Override
		public Integer toOrdinal(String pos) {
			if (pos == null) {
				return null;
			}
			
			return Integer.parseInt(pos);
		}

		@Override
		public String fromOrdinal(Integer pos) {
			if (pos == null || pos < 0) {
				throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_NUMBER_POSITION);
			}
			
			return pos.toString();
		}		
	}
	
	
	private class RomanSchemeOrdinalConverter implements SchemeOrdinalConverter {
		private final Map<String, Integer> romanLiterals = new LinkedHashMap<String, Integer>() {
			private static final long serialVersionUID = 685666506457371647L;

			{
				put("m", 1000);
				put("cm", 900);
				put("d",  500);
				put("cd", 400);
				put("c",  100);
				put("xc",  90);
				put("l",   50);
				put("xl",  40);
				put("x",   10);
				put("ix",   9);
				put("v",    5);
				put("iv",   4);
				put("i",    1);				
			}			
		};
		
		private boolean upper;
		
		public RomanSchemeOrdinalConverter(boolean upper) {
			this.upper = upper;
		}

		@Override
		public Integer toOrdinal(String pos) {
			pos = pos.toLowerCase();
			
			int result = 0;
			int len = pos.length(), idx = len;
			while (idx > 0) {
				--idx;
				if (idx == len - 1) {
					Integer val = romanLiterals.get(pos.substring(idx, idx + 1));
					if (val == null) {
						throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_ROMAN_POSITION);
					}
					result += val;
				} else {
					Integer current = romanLiterals.get(pos.substring(idx, idx + 1));
					Integer ahead = romanLiterals.get(pos.substring(idx + 1, idx + 2));
					if (current == null || ahead == null) {
						throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_ROMAN_POSITION);
					}
					
					if (current < ahead) {
						result -= current;
					} else {
						result += current;
					}
				}
			}
			
			return result;
		}

		@Override
		public String fromOrdinal(Integer pos) {
			if (pos == null || pos <= 0) {
				throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_NUMBER_POSITION);
			}
			
			StringBuilder result = new StringBuilder();			
			int num = pos;
			for (Map.Entry<String, Integer> literal : romanLiterals.entrySet()) {
				while (num >= literal.getValue()) {
					result.append(literal.getKey());
					num -= literal.getValue();
				}
			}

			return upper ? result.toString().toUpperCase() : result.toString();
		}
		
	}
	
	private class AlphabetSchemeOrdinalConverter implements SchemeOrdinalConverter {
		private final char[] alphabets = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
		};
				
		private boolean upper;
		
		public AlphabetSchemeOrdinalConverter(boolean upper) {
			this.upper = upper;			
		}

		@Override
		public Integer toOrdinal(String pos) {
			pos = pos.toLowerCase();
			
			if (!StringUtils.isAlpha(pos)) {
				throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_ALPHA_POSITION);
			}
			
			int len = pos.length();
			int base = 1, result = 0;
			while (len > 0) {
				len--;
				
				int charAt = pos.charAt(len);
				result = result + (charAt - 'a' + 1) * base;
				base *= 26;
			}

			return result;
		}

		@Override
		public String fromOrdinal(Integer pos) {
			if (pos == null || pos <= 0) {
				throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_NUMBER_POSITION);
			}
			
			StringBuilder result = new StringBuilder();			
			int num = pos;
		    while (num > 0) {
		    	result.insert(0, alphabets[(num - 1) % 26]);
		    	num = (num - 1) / 26;		      
		    }

		    return upper ? result.toString().toUpperCase() : result.toString();
		}		
	}
}
