
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.repository.ContainerRestrictionsCriteria;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.util.SchemeOrdinalConverterUtil;
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

	public enum PositionLabelingMode {
		LINEAR,
		TWO_D
	}

	private String name;

	private String barcode;

	private ContainerType type;
	
	private Double temperature;
	
	private int noOfColumns;
	
	private int noOfRows;

	private PositionLabelingMode positionLabelingMode = PositionLabelingMode.TWO_D;
	
	private String columnLabelingScheme = NUMBER_LABELING_SCHEME;
	
	private String rowLabelingScheme = NUMBER_LABELING_SCHEME;
	
	private Site site;

	private StorageContainer parentContainer;

	private User createdBy;

	private String activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
	
	private String comments;
	
	private Set<StorageContainer> childContainers = new LinkedHashSet<>();
	
	private Set<StorageContainer> ancestorContainers = new HashSet<>();
	
	private Set<StorageContainer> descendentContainers = new HashSet<>();
	
	//
	// all types of these specimen classes are allowed
	//
	private Set<String> allowedSpecimenClasses = new HashSet<>();
	
	private Set<String> allowedSpecimenTypes = new HashSet<>();
	
	private Set<CollectionProtocol> allowedCps = new HashSet<>();
	
	private boolean storeSpecimenEnabled = false;
			
	private StorageContainerPosition position;
	
	private Set<StorageContainerPosition> occupiedPositions = new HashSet<>();

	//
	// query capabilities
	//
	private StorageContainerStats stats;
	
	private Set<String> compAllowedSpecimenClasses = new HashSet<>();
	
	private Set<String> compAllowedSpecimenTypes = new HashSet<>();
	
	private Set<CollectionProtocol> compAllowedCps = new HashSet<>();

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

	public ContainerType getType() {
		return type;
	}

	public void setType(ContainerType type) {
		this.type = type;
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

	public PositionLabelingMode getPositionLabelingMode() {
		return positionLabelingMode;
	}

	public void setPositionLabelingMode(PositionLabelingMode positionLabelingMode) {
		this.positionLabelingMode = positionLabelingMode;
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
	
	public void addChildContainer(StorageContainer container) {
		container.setParentContainer(this);
		childContainers.add(container);
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
		
		setName(other.getName());
		setBarcode(other.getBarcode());
		setType(other.getType());
		setTemperature(other.getTemperature());
		updateCapacity(other.getNoOfColumns(), other.getNoOfRows());
		setPositionLabelingMode(other.getPositionLabelingMode());
		updateLabelingScheme(other.getColumnLabelingScheme(), other.getRowLabelingScheme());
		updateContainerLocation(other.getSite(), other.getParentContainer(), other.getPosition());
		updateActivityStatus(other.getActivityStatus());
		setComments(other.getComments());
		updateAllowedSpecimenClassAndTypes(other.getAllowedSpecimenClasses(), other.getAllowedSpecimenTypes(), hasParentChanged);
		updateAllowedCps(other.getAllowedCps(), hasParentChanged);
		updateStoreSpecimenEnabled(other.isStoreSpecimenEnabled());
		
		validateRestrictions();
	}
	
	public int freePositionsCount() {
		return noOfColumns * noOfRows - occupiedPositions.size();
	}

	public boolean hasFreePositionsForReservation() {
		return hasFreePositionsForReservation(1);
	}

	public boolean hasFreePositionsForReservation(int freePositions) {
		return (getNoOfColumns() * getNoOfRows() - getOccupiedPositions().size()) > (freePositions - 1);
	}

	public List<StorageContainer> getChildContainersSortedByPosition() {
		return getChildContainers().stream()
			.sorted((c1, c2) -> c1.getPosition().getPosition() - c2.getPosition().getPosition())
			.collect(Collectors.toList());
	}

	public Set<Integer> occupiedPositionsOrdinals() {
		return getOccupiedPositions().stream()
			.map(pos -> (pos.getPosTwoOrdinal() - 1) * getNoOfColumns() + pos.getPosOneOrdinal())
			.collect(Collectors.toSet());
	}
	
	public String toColumnLabelingScheme(int ordinal) {
		return fromOrdinal(getColumnLabelingScheme(), ordinal);
	}
	
	public String toRowLabelingScheme(int ordinal) {
		return fromOrdinal(getRowLabelingScheme(), ordinal);
	}
	
	public boolean areValidPositions(String posOne, String posTwo) {
		int posOneOrdinal = toOrdinal(getColumnLabelingScheme(), posOne);
		int posTwoOrdinal = toOrdinal(getRowLabelingScheme(), posTwo);
			
		return areValidPositions(posOneOrdinal, posTwoOrdinal);
	}
	
	public boolean areValidPositions(int posOne, int posTwo) {
		return posOne >= 1 && posOne <= getNoOfColumns() && 
				posTwo >= 1 && posTwo <= getNoOfRows();
	}
	
	public StorageContainerPosition createPosition(String posOne, String posTwo) {
		int posOneOrdinal = toOrdinal(getColumnLabelingScheme(), posOne);
		int posTwoOrdinal = toOrdinal(getRowLabelingScheme(), posTwo);
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
			int startRow = lastAssignedPos.getPosTwoOrdinal();
			int startCol = lastAssignedPos.getPosOneOrdinal() + 1;
			if (startCol > getNoOfColumns()) {
				++startRow;
				startCol = 1;
			}
			
			row = fromOrdinal(getRowLabelingScheme(), startRow);
			col = fromOrdinal(getColumnLabelingScheme(), startCol);
		}

		return nextAvailablePosition(row, col);
	}

	public StorageContainerPosition nextAvailablePosition(int position) {
		String row = null, column = null;
		if (position > 0) {
			row    = fromOrdinal(getRowLabelingScheme(),    (position - 1) / getNoOfColumns() + 1);
			column = fromOrdinal(getColumnLabelingScheme(), (position - 1) % getNoOfColumns() + 1);
		}

		return nextAvailablePosition(row, column);
	}

	public StorageContainerPosition nextAvailablePosition(String row, String col) {
		int startRow = 1, startCol = 1;
		boolean startPosSpecified = StringUtils.isNotBlank(row) && StringUtils.isNotBlank(col);
		Set<Integer> occupiedPositionOrdinals = occupiedPositionsOrdinals();

		if (startPosSpecified) {
			startRow = toOrdinal(getRowLabelingScheme(), row);
			startCol = toOrdinal(getColumnLabelingScheme(), col);
		}

		for (int y = startRow; y <= getNoOfRows(); ++y) {
			for (int x = startCol; x <= getNoOfColumns(); ++x) {
				int pos = (y - 1) * getNoOfColumns() + x;
				if (!occupiedPositionOrdinals.contains(pos)) {
					String posOne = fromOrdinal(getColumnLabelingScheme(), x);
					String posTwo = fromOrdinal(getRowLabelingScheme(), y);

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
		int posOneOrdinal = toOrdinal(getColumnLabelingScheme(), posOne);
		int posTwoOrdinal = toOrdinal(getRowLabelingScheme(), posTwo);

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

	public StorageContainerPosition getReservedPosition(String row, String column, String reservationId) {
		StorageContainerPosition reservedPos = getOccupiedPositions().stream()
			.filter(pos -> pos.equals(row, column, reservationId))
			.findFirst().orElse(null);
		if (reservedPos == null) {
			return null;
		}

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -5);
		if (reservedPos.getReservationTime().before(cal.getTime())) {
			getOccupiedPositions().remove(reservedPos);
			return null;
		}

		return reservedPos;
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

		ContainerRestrictionsCriteria crit = new ContainerRestrictionsCriteria()
			.containerId(getId())
			.specimenClasses(getCompAllowedSpecimenClasses())
			.specimenTypes(getCompAllowedSpecimenTypes())
			.collectionProtocols(getCompAllowedCps())
			.site(getSite());

		StorageContainerDao containerDao = getDaoFactory().getStorageContainerDao();
		List<String> nonCompliantContainers = containerDao.getNonCompliantContainers(crit);
		if (CollectionUtils.isNotEmpty(nonCompliantContainers)) {
			// Show first non compliant container in error message
			throw OpenSpecimenException.userError(
				StorageContainerErrorCode.CANNOT_HOLD_CONTAINER,
				getName(),
				nonCompliantContainers.get(0));
		}

		List<String> nonCompliantSpecimens = containerDao.getNonCompliantSpecimens(crit);
		if (CollectionUtils.isNotEmpty(nonCompliantSpecimens)) {
			// Show first non compliant specimen in error message
			throw OpenSpecimenException.userError(
				StorageContainerErrorCode.CANNOT_HOLD_SPECIMEN,
				getName(),
				nonCompliantSpecimens.get(0));
		}
	}
	
	public Set<CollectionProtocol> computeAllowedCps() {
		if (CollectionUtils.isNotEmpty(getAllowedCps()) || getParentContainer() == null) {
			return new HashSet<>(getAllowedCps());
		}
		
		return getParentContainer().computeAllowedCps();
	}
	
	public Set<String> computeAllowedSpecimenClasses() {
		if (CollectionUtils.isNotEmpty(getAllowedSpecimenTypes()) || 
				CollectionUtils.isNotEmpty(getAllowedSpecimenClasses())) {
			return new HashSet<>(getAllowedSpecimenClasses());
		}
		
		if (getParentContainer() != null) {
			return getParentContainer().computeAllowedSpecimenClasses();
		}
				
		return new HashSet<>(getDaoFactory().getPermissibleValueDao().getSpecimenClasses());
	}
	
	public Set<String> computeAllowedSpecimenTypes() {
		Set<String> types = new HashSet<>();
		if (CollectionUtils.isNotEmpty(getAllowedSpecimenTypes())) {
			types.addAll(getAllowedSpecimenTypes());
		} else if (CollectionUtils.isEmpty(getAllowedSpecimenClasses()) && getParentContainer() != null) {
			types.addAll(getParentContainer().computeAllowedSpecimenTypes());			
		}
		
		return types;
	}

	public List<DependentEntityDetail> getDependentEntities() {
		return DependentEntityDetail.singletonList(
				Specimen.getEntityName(), getSpecimensCount());
	}
	
	public void delete() {
		int specimensCnt = getSpecimensCount();
		if (specimensCnt > 0) {
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
	
	public StorageContainer copy() {
		StorageContainer copy = new StorageContainer();
		copy.setType(getType());
		copy.setSite(getSite());
		copy.setParentContainer(getParentContainer());
		copy.setNoOfColumns(getNoOfColumns());
		copy.setNoOfRows(getNoOfRows());
		copy.setPositionLabelingMode(getPositionLabelingMode());
		copy.setColumnLabelingScheme(getColumnLabelingScheme());
		copy.setRowLabelingScheme(getRowLabelingScheme());
		copy.setTemperature(getTemperature());
		copy.setStoreSpecimenEnabled(isStoreSpecimenEnabled());
		copy.setComments(getComments());
		copy.setCreatedBy(getCreatedBy());
		copy.setAllowedSpecimenClasses(new HashSet<>(getAllowedSpecimenClasses()));
		copy.setAllowedSpecimenTypes(new HashSet<>(getAllowedSpecimenTypes()));
		copy.setAllowedCps(new HashSet<>(getAllowedCps()));
		copy.setCompAllowedSpecimenClasses(computeAllowedSpecimenClasses());
		copy.setCompAllowedSpecimenTypes(computeAllowedSpecimenTypes());
		copy.setCompAllowedCps(computeAllowedCps());
		return copy;
	}
	
	public void removeCpRestriction(CollectionProtocol cp) {
		getAllowedCps().remove(cp);
		updateComputedCps();
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

	private int getSpecimensCount() {
		return getDaoFactory().getStorageContainerDao().getSpecimensCount(getId());
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

		int posOneOrdinal = toOrdinal(getColumnLabelingScheme(), posOne);
		int posTwoOrdinal = toOrdinal(getRowLabelingScheme(), posTwo);
		
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
		if (newNoOfColumns < getNoOfColumns() || newNoOfRows < getNoOfRows()) {
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
				pos.setPosOne(fromOrdinal(newColumnLabelingScheme, pos.getPosOneOrdinal()));
			}
			
			if (rowSchemeChanged) {
				pos.setPosTwo(fromOrdinal(newRowLabelingScheme, pos.getPosTwoOrdinal()));
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
		setSite(site);
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

	private String fromOrdinal(String scheme, Integer pos) {
		try {
			return SchemeOrdinalConverterUtil.fromOrdinal(scheme, pos);
		} catch (IllegalArgumentException iae) {
			throw OpenSpecimenException.userError(getSchemeOrdinalErrorCode(scheme));
		}
	}

	private Integer toOrdinal(String scheme, String pos) {
		try {
			return SchemeOrdinalConverterUtil.toOrdinal(scheme, pos);
		} catch (IllegalArgumentException iae) {
			throw OpenSpecimenException.userError(getSchemeOrdinalErrorCode(scheme));
		}
	}

	private StorageContainerErrorCode getSchemeOrdinalErrorCode(String scheme) {
		StorageContainerErrorCode code = null;
		if (scheme.equals(NUMBER_LABELING_SCHEME)) {
			code = StorageContainerErrorCode.INVALID_NUMBER_POSITION;
		} else if (scheme.equals(UPPER_CASE_ALPHA_LABELING_SCHEME) || scheme.equals(LOWER_CASE_ALPHA_LABELING_SCHEME)) {
			code = StorageContainerErrorCode.INVALID_ALPHA_POSITION;
		} else if (scheme.equals(UPPER_CASE_ROMAN_LABELING_SCHEME) || scheme.equals(LOWER_CASE_ROMAN_LABELING_SCHEME)) {
			code = StorageContainerErrorCode.INVALID_ROMAN_POSITION;
		}

		return code;
	}
}
