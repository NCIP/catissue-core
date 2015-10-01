
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Audited
public class StorageContainer extends BaseEntity {
	private static final String ENTITY_NAME = "storage_container";
	
	public static final String NUMBER_LABELING_SCHEME = "Numbers";
	
	public static final String UPPER_CASE_ALPHA_LABELING_SCHEME = "Alphabets Upper Case";
	
	public static final String LOWER_CASE_ALPHA_LABELING_SCHEME = "Alphabets Lower Case";
	
	public static final String UPPER_CASE_ROMAN_LABELING_SCHEME = "Roman Upper Case";
	
	public static final String LOWER_CASE_ROMAN_LABELING_SCHEME = "Roman Lower Case";
	
	private String name;

	private String barcode;

	private Double temperature;
	
	private int noOfColumns;
	
	private int noOfRows;
	
	private String columnLabelingScheme = NUMBER_LABELING_SCHEME;
	
	private String rowLabelingScheme = NUMBER_LABELING_SCHEME;
	
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

	private transient StorageContainerPosition lastAssignedPos;


	public StorageContainer() {
		ancestorContainers.add(this);
	}
	
	public static String getEntityName() {
		return ENTITY_NAME;
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

	public int getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public String getColumnLabelingScheme() {
		return columnLabelingScheme;
	}

	public void setColumnLabelingScheme(String columnLabelingScheme) {
		this.columnLabelingScheme = columnLabelingScheme;
	}

	public String getRowLabelingScheme() {
		return rowLabelingScheme;
	}

	public void setRowLabelingScheme(String rowLabelingScheme) {
		this.rowLabelingScheme = rowLabelingScheme;
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

	public StorageContainerPosition getLastAssignedPos() {
		return lastAssignedPos;
	}

	public void setLastAssignedPos(StorageContainerPosition lastAssignedPos) {
		this.lastAssignedPos = lastAssignedPos;
	}

	@NotAudited
	public Set<StorageContainer> getChildContainers() {
		return childContainers;
	}

	public void setChildContainers(Set<StorageContainer> childContainers) {
		this.childContainers = childContainers;
	}

	@NotAudited
	public Set<StorageContainer> getAncestorContainers() {
		return ancestorContainers;
	}

	public void setAncestorContainers(Set<StorageContainer> ancestorContainers) {
		this.ancestorContainers = ancestorContainers;
	}

	@NotAudited
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

	@NotAudited
	public Set<StorageContainerPosition> getOccupiedPositions() {
		return occupiedPositions;
	}

	public void setOccupiedPositions(Set<StorageContainerPosition> occupiedPositions) {
		this.occupiedPositions = occupiedPositions;
	}
	
	@NotAudited
	public StorageContainerStats getStats() {
		return stats;
	}

	public void setStats(StorageContainerStats stats) {
		this.stats = stats;
	}
	
	@NotAudited
	public Set<String> getCompAllowedSpecimenClasses() {
		return compAllowedSpecimenClasses;
	}

	public void setCompAllowedSpecimenClasses(Set<String> compAllowedSpecimenClasses) {
		this.compAllowedSpecimenClasses = compAllowedSpecimenClasses;
	}

	@NotAudited
	public Set<String> getCompAllowedSpecimenTypes() {
		return compAllowedSpecimenTypes;
	}

	public void setCompAllowedSpecimenTypes(Set<String> compAllowedSpecimenTypes) {
		this.compAllowedSpecimenTypes = compAllowedSpecimenTypes;
	}

	@NotAudited
	public Set<CollectionProtocol> getCompAllowedCps() {
		return compAllowedCps;
	}

	public void setCompAllowedCps(Set<CollectionProtocol> compAllowedCps) {
		this.compAllowedCps = compAllowedCps;
	}

	public void update(StorageContainer other) {
		boolean hasParentChanged = false;
		if (getParentContainer() == null && other.getParentContainer() != null) {
			hasParentChanged = true;
		} else if (getParentContainer() != null && !getParentContainer().equals(other.getParentContainer())) {
			hasParentChanged = true;
		}
		
		setName(other.name);
		setBarcode(other.barcode);
		setTemperature(other.temperature);
		updateCapacity(other.noOfColumns, other.noOfRows);
		updateLabelingScheme(other.columnLabelingScheme, other.rowLabelingScheme);
		updateContainerLocation(other.getSite(), other.getParentContainer(), other.getPosition());
		updateActivityStatus(other.activityStatus);
		setComments(other.comments);
		updateAllowedSpecimenClassAndTypes(other.getAllowedSpecimenClasses(), other.getAllowedSpecimenTypes(), hasParentChanged);
		updateAllowedCps(other.getAllowedCps(), hasParentChanged);
		updateStoreSpecimenEnabled(other.storeSpecimenEnabled);
		
		validateRestrictions();
	}
	
	public int freePositionsCount() {
		return noOfColumns * noOfRows - occupiedPositions.size();
	}
	
	public Set<Integer> occupiedPositionsOrdinals() {
		Set<Integer> result = new HashSet<Integer>();
				
		for (StorageContainerPosition pos : getOccupiedPositions()) {
			result.add((pos.getPosTwoOrdinal() - 1) * getNoOfColumns() + pos.getPosOneOrdinal());
		}
		
		return result;
	}
	
	public String toColumnLabelingScheme(int ordinal) {
		return converters.get(getColumnLabelingScheme()).fromOrdinal(ordinal);
	}
	
	public String toRowLabelingScheme(int ordinal) {
		return converters.get(getRowLabelingScheme()).fromOrdinal(ordinal);
	}
	
	public boolean areValidPositions(String posOne, String posTwo) {
		int posOneOrdinal = converters.get(getColumnLabelingScheme()).toOrdinal(posOne);
		int posTwoOrdinal = converters.get(getRowLabelingScheme()).toOrdinal(posTwo);
			
		return areValidPositions(posOneOrdinal, posTwoOrdinal);
	}
	
	public boolean areValidPositions(int posOne, int posTwo) {
		return posOne >= 1 && posOne <= getNoOfColumns() && 
				posTwo >= 1 && posTwo <= getNoOfRows();
	}
	
	public StorageContainerPosition createPosition(String posOne, String posTwo) {
		int posOneOrdinal = converters.get(getColumnLabelingScheme()).toOrdinal(posOne);
		int posTwoOrdinal = converters.get(getRowLabelingScheme()).toOrdinal(posTwo);
		return createPosition(posOneOrdinal, posOne, posTwoOrdinal, posTwo);
	}
	
	public StorageContainerPosition createVirtualPosition() {
		return createPosition(0, null, 0, null);
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
		return nextAvailablePosition(null, null);
	}

	public StorageContainerPosition nextAvailablePosition(boolean fromLastAssignedPos) {
		String row = null, col = null;
		if (fromLastAssignedPos && lastAssignedPos != null) {
			row = lastAssignedPos.getPosTwo();
			col = lastAssignedPos.getPosOne();
		}

		return nextAvailablePosition(row, col);
	}

	public StorageContainerPosition nextAvailablePosition(String row, String col) {
		int startRow = 1, startCol = 1;
		boolean startPosSpecified = StringUtils.isNotBlank(row) && StringUtils.isNotBlank(col);
		Set<Integer> occupiedPositionOrdinals = occupiedPositionsOrdinals();

		if (startPosSpecified) {
			startRow = converters.get(getRowLabelingScheme()).toOrdinal(row);
			startCol = converters.get(getColumnLabelingScheme()).toOrdinal(col);
		}

		for (int y = startRow; y <= getNoOfRows(); ++y) {
			for (int x = startCol; x <= getNoOfColumns(); ++x) {
				int pos = (y - 1) * getNoOfColumns() + x;
				if (!occupiedPositionOrdinals.contains(pos)) {
					String posOne = converters.get(getColumnLabelingScheme()).fromOrdinal(x);
					String posTwo = converters.get(getRowLabelingScheme()).fromOrdinal(y);

					return (lastAssignedPos = createPosition(x, posOne, y, posTwo));
				}
			}

			startCol = 1;
		}

		if (startPosSpecified) {
			return nextAvailablePosition(null, null);
		}

		return null;
	}

	public boolean isPositionOccupied(String posOne, String posTwo) {
		int posOneOrdinal = converters.get(getColumnLabelingScheme()).toOrdinal(posOne);
		int posTwoOrdinal = converters.get(getRowLabelingScheme()).toOrdinal(posTwo);

		return getOccupiedPosition(posOneOrdinal, posTwoOrdinal) != null;
	}
	
	public boolean canSpecimenOccupyPosition(Long specimenId, String posOne, String posTwo) {
		return canOccupyPosition(true, specimenId, posOne, posTwo, false);
	}

	public boolean canSpecimenOccupyPosition(Long specimenId, String posOne, String posTwo, boolean vacateOccupant) {
		return canOccupyPosition(true, specimenId, posOne, posTwo, vacateOccupant);
	}

	public boolean canContainerOccupyPosition(Long containerId, String posOne, String posTwo) {
		return canOccupyPosition(false, containerId, posOne, posTwo, false);
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
				
		if (!getCompAllowedCps().isEmpty()) {
			return getCompAllowedCps().containsAll(container.getCompAllowedCps());
		} else if (!container.getCompAllowedCps().isEmpty()) {
			for (CollectionProtocol cp : container.getCompAllowedCps()) {
				if (!cp.getRepositories().contains(getSite())) {
					return false;
				}
			}
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
		
		if (!getCompAllowedCps().isEmpty()) {
			return getCompAllowedCps().contains(cp);
		} else {
			return cp.getRepositories().contains(getSite());
		}
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
			throw OpenSpecimenException.userError(StorageContainerErrorCode.CANNOT_HOLD_CONTAINER, parent.getName(), getName());
		}
		
		for (StorageContainerPosition pos : getOccupiedPositions()) {
			if (pos.getOccupyingContainer() != null) {
				pos.getOccupyingContainer().validateRestrictions();
			} else if (pos.getOccupyingSpecimen() != null) {
				if (!canContain(pos.getOccupyingSpecimen())) {
					throw OpenSpecimenException.userError(
							StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN, 
							getName(), 
							pos.getOccupyingSpecimen().getLabelOrDesc());
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

	public List<DependentEntityDetail> getDependentEntities() {
		return DependentEntityDetail.singletonList(
				Specimen.getEntityName(), getSpecimenCount());
	}
	
	public void delete() {
		int specimenCnt = getSpecimenCount();
		if (specimenCnt > 0) {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.REF_ENTITY_FOUND);
		}
		
		deleteWithoutCheck();
	}
	
	public String getStringifiedAncestors() {
		StringBuilder names = new StringBuilder();
		getStringifiedAncestors(names);
		names.delete(names.length() - 2, names.length());
		return names.toString();
	}

	//
	// Assign unoccupied positions in container
	//
	public void assignPositions(Collection<StorageContainerPosition> positions) {
		assignPositions(positions, false);
	}

	//
	// Two cases:
	// case #1: vacateOccupant: true - Assign unoccupied positions
	// case #2: Otherwise - Vacate occupant before assigning position to new occupant
	//
	public void assignPositions(Collection<StorageContainerPosition> positions, boolean vacateOccupant) {
		Set<Long> specimenIds = Collections.emptySet();
		if (vacateOccupant) {
			specimenIds = new HashSet<Long>();
			for (StorageContainerPosition position : positions) {
				if (position.getOccupyingSpecimen() != null) {
					specimenIds.add(position.getOccupyingSpecimen().getId());
				}
			}
		}

		for (StorageContainerPosition position : positions) {
			StorageContainerPosition existing = null;
			if (position.getPosOneOrdinal() != 0 && position.getPosTwoOrdinal() != 0) {
				existing = getOccupiedPosition(position.getPosOneOrdinal(), position.getPosTwoOrdinal());
			}

			if (existing != null && !vacateOccupant) {
				continue; 
			}
						
			if (position.getOccupyingSpecimen() != null) {
				if (existing != null && !specimenIds.contains(existing.getOccupyingSpecimen().getId())) {
					//
					// The occupant that is being vacated is not assigned any new position
					// in this transaction. Therefore virtualise it.
					//
					existing.getOccupyingSpecimen().updatePosition(null);
				}

				position.getOccupyingSpecimen().updatePosition(position);
			} else {
				StorageContainer childContainer = position.getOccupyingContainer();
				boolean hasParentChanged = !this.equals(childContainer.getParentContainer());
				childContainer.updateContainerLocation(getSite(), this, position);
				
				if (hasParentChanged) {
					childContainer.updateComputedClassAndTypes();
					childContainer.updateComputedCps();
				}
			}
		}
	}
	
	private void deleteWithoutCheck() {
		for (StorageContainer child: getChildContainers()) {
			child.deleteWithoutCheck();
		}
		
		setName(Utility.getDisabledValue(getName(), 64));
		if (getBarcode() != null) {
			setBarcode(Utility.getDisabledValue(getBarcode(), 64));
		}
		
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
		if (getParentContainer() != null) {
			getParentContainer().removePosition(getPosition());
			setPosition(null);
		}
	}
	
	private int getSpecimenCount() {
		int specimenCnt = 0;
		for (StorageContainer descendant: descendentContainers) {
			specimenCnt += descendant.getSelfSpecimenCount();
		}
		
		return specimenCnt;
	}
	
	private int getSelfSpecimenCount() {
		return getOccupiedPositions().size() - getChildContainers().size();
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
	
	private boolean canOccupyPosition(
			boolean isSpecimenEntity,
			Long entityId, 
			String posOne, 
			String posTwo, 
			boolean vacateOccupant) {
		
		int posOneOrdinal = converters.get(getColumnLabelingScheme()).toOrdinal(posOne);
		int posTwoOrdinal = converters.get(getRowLabelingScheme()).toOrdinal(posTwo);
		
		if (!areValidPositions(posOneOrdinal, posTwoOrdinal)) {
			return false;
		}

		StorageContainerPosition pos = getOccupiedPosition(posOneOrdinal, posTwoOrdinal);
		if (pos == null) {
			return true; // vacant position
		} else if (entityId == null) { 
			return false; // position is not vacant and entity is new
		} else if (isSpecimenEntity) {
			return (vacateOccupant && pos.getOccupyingContainer() == null) ||
					pos.getOccupyingSpecimen() != null && pos.getOccupyingSpecimen().getId().equals(entityId);
		} else {
			return pos.getOccupyingContainer() != null && pos.getOccupyingContainer().getId().equals(entityId);
		}
	}
	
	private void updateCapacity(int newNoOfColumns, int newNoOfRows) {
		if (newNoOfColumns < getNoOfColumns() || 
				newNoOfRows < getNoOfRows()) {
			if (arePositionsOccupiedBeyondCapacity(newNoOfColumns, newNoOfRows)) {
				throw OpenSpecimenException.userError(StorageContainerErrorCode.CANNOT_SHRINK_CONTAINER);
			}
		}
		
		setNoOfColumns(newNoOfColumns);
		setNoOfRows(newNoOfRows); 
	}
	
	private void updateLabelingScheme(String newColumnLabelingScheme, String newRowLabelingScheme) {
		boolean colSchemeChanged = !getColumnLabelingScheme().equals(newColumnLabelingScheme);
		boolean rowSchemeChanged = !getRowLabelingScheme().equals(newRowLabelingScheme);
		
		if (!colSchemeChanged && !rowSchemeChanged) {
			return;
		}
		
		for (StorageContainerPosition pos : getOccupiedPositions()) {
			if (colSchemeChanged) {
				pos.setPosOne(converters.get(newColumnLabelingScheme).fromOrdinal(pos.getPosOneOrdinal()));
			}
			
			if (rowSchemeChanged) {
				pos.setPosTwo(converters.get(newRowLabelingScheme).fromOrdinal(pos.getPosTwoOrdinal()));
			}
		}
		
		setColumnLabelingScheme(newColumnLabelingScheme);
		setRowLabelingScheme(newRowLabelingScheme);
	}
	
	private void updateContainerLocation(Site otherSite, StorageContainer otherParentContainer, StorageContainerPosition otherPos) {
		Site existing = site;
		
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
		
		if (!site.equals(existing)) { // has site changed? if yes, ensure all child containers beneath it are updated
			updateSite(site);
		}
	}
	
	private void updateSite(Site site) {
		this.site = site;
		for (StorageContainer container : getChildContainers()) {
			container.updateSite(site);
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

	private void updateAllowedSpecimenClassAndTypes(
			Set<String> newSpecimenClasses, 
			Set<String> newSpecimenTypes, 
			boolean updateComputedTypes) {
		
		boolean computeTypes = updateComputedTypes;
		
		if (!CollectionUtils.isEqualCollection(getAllowedSpecimenClasses(), newSpecimenClasses)) {
			getAllowedSpecimenClasses().clear();
			getAllowedSpecimenClasses().addAll(newSpecimenClasses);
			computeTypes = true;			
		}
		
		if (!CollectionUtils.isEqualCollection(getAllowedSpecimenTypes(), newSpecimenTypes)) {
			getAllowedSpecimenTypes().clear();
			getAllowedSpecimenTypes().addAll(newSpecimenTypes);
			computeTypes = true;			
		}
		
		if (computeTypes) {
			updateComputedClassAndTypes();
		}
				
	}
	
	private void updateComputedClassAndTypes() {
		getCompAllowedSpecimenClasses().clear();
		getCompAllowedSpecimenClasses().addAll(computeAllowedSpecimenClasses());

		getCompAllowedSpecimenTypes().clear();
		getCompAllowedSpecimenTypes().addAll(computeAllowedSpecimenTypes());				
		
		for (StorageContainer childContainer : getChildContainers()) {
			childContainer.updateComputedClassAndTypes();
		}		
	}
	
	private void updateAllowedCps(Set<CollectionProtocol> newCps, boolean updateComputedCps) {
		boolean computeCps = updateComputedCps;		
		if (!CollectionUtils.isEqualCollection(getAllowedCps(), newCps)) {
			getAllowedCps().clear();
			getAllowedCps().addAll(newCps);			
			computeCps = true;
		}
		
		if (computeCps) {
			updateComputedCps();			
		}
	}
	
	private void updateComputedCps() {
		getCompAllowedCps().clear();
		getCompAllowedCps().addAll(computeAllowedCps());
		
		for (StorageContainer childContainer : getChildContainers()) {
			childContainer.updateComputedCps();
		}		
	}
	
	private void updateStoreSpecimenEnabled(boolean newStoreSpecimenEnabled) {
		this.storeSpecimenEnabled = newStoreSpecimenEnabled;
	}
		
	private boolean arePositionsOccupiedBeyondCapacity(int noOfCols, int noOfRows) {
		boolean result = false;
		for (StorageContainerPosition pos : getOccupiedPositions()) {
			if (pos.getPosOneOrdinal() > noOfCols) {
				result = true;
				break;
			}
			
			if (pos.getPosTwoOrdinal() > noOfRows) {
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
	
	private void getStringifiedAncestors(StringBuilder names) {
		if (getParentContainer() != null) {
			getParentContainer().getStringifiedAncestors(names);
		}
		
		names.append(getName()).append(", ");
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
			
			try {
				return Integer.parseInt(pos);
			} catch (NumberFormatException nfe) {
				throw OpenSpecimenException.userError(StorageContainerErrorCode.INVALID_NUMBER_POSITION);
			}			
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
