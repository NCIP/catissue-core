/**
 * <p>Title: StorageContainerHDAO Class>
 * <p>Description:	StorageContainerHDAO is used to add Storage Container information into the database using Hibernate.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 * Created on Jul 23, 2005
 */

package edu.wustl.catissuecore.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.namegenerator.BarcodeGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.LabelGenException;
import edu.wustl.catissuecore.namegenerator.LabelGenerator;
import edu.wustl.catissuecore.namegenerator.LabelGeneratorFactory;
import edu.wustl.catissuecore.namegenerator.NameGeneratorException;
import edu.wustl.catissuecore.util.ApiSearchUtil;
import edu.wustl.catissuecore.util.Position;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.exception.ErrorKey;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;

/**
 * StorageContainerHDAO is used to add Storage Container information into the
 * database using Hibernate.
 * @author vaishali_khandelwal
 */
/**
 * @author geeta_jaggal
 *
 */
public class StorageContainerBizLogic extends CatissueDefaultBizLogic
{
	/**
	 * Logger object.
	 */
	private static final transient Logger logger = Logger.getCommonLogger(StorageContainerBizLogic.class);
	/**
	 * Saves the storageContainer object in the database.
	 * @param dao - DAo object
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void insert(final Object obj, final DAO dao, final SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		try
		{
			final StorageContainer container = (StorageContainer) obj;
			container.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
			final SiteBizLogic sBiz = new SiteBizLogic();
			final StorageTypeBizLogic stBiz = new StorageTypeBizLogic();
			int posOneCapacity = 1, posTwoCapacity = 1;
			int posDimOne = Constants.STORAGE_CONTAINER_FIRST_ROW, posDimTwo
			= Constants.STORAGE_CONTAINER_FIRST_COLUMN;
			boolean[][] fullStatus = null;
			final int noOfContainers = container.getNoOfContainers().intValue();
			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() != null)
			{
				final Object object = dao.retrieveById(StorageContainer.class.getName(), container
						.getLocatedAtPosition().getParentContainer().getId());
				if (object != null)
				{
					final StorageContainer parentContainer = (StorageContainer) object;
					this.checkStatus(dao, parentContainer, "Parent Container");
					final int totalCapacity = parentContainer.getCapacity()
							.getOneDimensionCapacity().intValue()
							* parentContainer.getCapacity().getTwoDimensionCapacity().intValue();
					final Collection children = this.getChildren(dao,
							parentContainer.getId());
					if ((noOfContainers + children.size()) > totalCapacity)
					{
						throw this.getBizLogicException(null,
						"errors.storageContainer.overflow","");
					}
					else
					{
						validateParentContainer(dao, sessionDataBean,
						container, parentContainer);
						final ContainerPosition cntPos = container.getLocatedAtPosition();
						cntPos.setParentContainer(parentContainer);
						container.setSite(parentContainer.getSite());
						posOneCapacity = parentContainer.getCapacity().
						getOneDimensionCapacity().intValue();
						posTwoCapacity = parentContainer.getCapacity().
						getTwoDimensionCapacity().intValue();
						fullStatus = StorageContainerUtil.
						getStorageContainerFullStatus(dao, parentContainer,children);
						posDimOne = cntPos.getPositionDimensionOne().intValue();
						posDimTwo = cntPos.getPositionDimensionTwo().intValue();
						container.setLocatedAtPosition(cntPos);
					}
				}
				else
				{
					throw this.getBizLogicException(null, "errors.storageContainerExist", "");
				}
			}
			else
			{
				sBiz.loadSite(dao, container);
			}
			stBiz.loadStorageType(dao, container);
			for (int i = 0; i < noOfContainers; i++)
			{
				final StorageContainer cont = setContainerPos(container, posDimOne, posDimTwo);
				logger.debug("Collection protocol size:"
						+ container.getCollectionProtocolCollection().size());
				setLabelAndBarcode(dao, container, cont);
				dao.insert(cont);
				container.setId(cont.getId());
				container.setCapacity(cont.getCapacity());
				if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() != null)
				{
					do
					{
						if (posDimTwo == posTwoCapacity)
						{
							if (posDimOne == posOneCapacity)
							{
								posDimOne = Constants.STORAGE_CONTAINER_FIRST_ROW;
							}
							else
							{
								posDimOne = (posDimOne + 1)
										% (posOneCapacity + 1);
							}
							posDimTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
						}
						else
						{
							posDimTwo = posDimTwo + 1;
						}
					}
					while (fullStatus[posDimOne][posDimTwo] != false);
				}
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		catch (final ApplicationException e)
		{
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}

	/**
	 * @param dao DAO Object
	 * @param sessionDataBean SessionDataBean object
	 * @param container Container Object
	 * @param parentContainer parent Container Object
	 * @throws BizLogicException BizLogicException Exception
	 */
	private void validateParentContainer(final DAO dao, final SessionDataBean sessionDataBean,
			final StorageContainer container, final StorageContainer parentContainer)
			throws BizLogicException
	{
		if (!StorageContainerUtil.validatePosition(parentContainer, container))
		{
			throw this.getBizLogicException(null,
					"errors.storageContainer.dimensionOverflow", "");
		}
		final String contId=container.getLocatedAtPosition().
		getParentContainer().getId().toString();
		final String pos1=container.getLocatedAtPosition()
		.getPositionDimensionOne().toString();
		final String pos2=container.getLocatedAtPosition()
		.getPositionDimensionTwo().toString();
		this.checkContainer(dao,StorageContainerUtil.setparameterList(contId, pos1, pos2, false),
		sessionDataBean,null);
		final boolean parentContValid = StorageContainerUtil.isParentContainerValidToUSe(
				container, parentContainer);
		if (!parentContValid)
		{
			throw this.getBizLogicException(null, "parent.container.not.valid", "");
		}
	}
	/**
	 * @param dao DAO Object
	 * @param container  New Container Object
	 * @param cont New Container Object
	 * @throws BizLogicException BizLogicException Exception
	 * @throws DAOException DAOException Exception
	 */
	private void setLabelAndBarcode(final DAO dao, final StorageContainer container,
			final StorageContainer cont) throws BizLogicException, DAOException
	{
		setLabel(container, cont);
		setBarcode();
		dao.insert(cont.getCapacity());
		if (cont.getFull() == null)
		{
			cont.setFull(false);
		}
	}
	/**
	 * Set Barcode
	 * @throws BizLogicException BizLogicException
	 */
	private void setBarcode() throws BizLogicException
	{
		if (edu.wustl.catissuecore.util.global.Variables.isStorageContainerBarcodeGeneratorAvl)
		{
			try
			{
				BarcodeGeneratorFactory
				.getInstance(Constants.STORAGECONTAINER_BARCODE_GENERATOR_PROPERTY_NAME);
			}
			catch (final NameGeneratorException e)
			{
				logger.error(e.getMessage(), e);
				throw this.getBizLogicException(e, "name.generator.exp", "");
			}
		}
	}
	/**
	 * @param container  New Container Object
	 * @param cont New Container Object
	 * @throws BizLogicException BizLogicException
	 */
	private void setLabel(final StorageContainer container, final StorageContainer cont)
			throws BizLogicException
	{
		if (edu.wustl.catissuecore.util.global.Variables.isStorageContainerLabelGeneratorAvl)
		{
			LabelGenerator scLblGenerator;
			try
			{
				scLblGenerator = LabelGeneratorFactory
						.getInstance(Constants.STORAGECONTAINER_LABEL_GENERATOR_PROPERTY_NAME);
				try
				{
					scLblGenerator.setLabel(cont);
				}
				catch (LabelGenException e)
				{
					logger.error(e.getMessage(), e);
				}
				container.setName(cont.getName());
			}
			catch (final NameGeneratorException e)
			{
				logger.error(e.getMessage(), e);
				e.printStackTrace() ;
				throw this.getBizLogicException(e, "name.generator.exp", "");
			}
		}
	}
	/**
	 * @param container StorageContainer Object
	 * @param posDimOne position dim one
	 * @param posDimTwo position dim two
	 * @return StorageContainer
	 */
	private StorageContainer setContainerPos(final StorageContainer container,
			final int posDimOne, final int posDimTwo)
	{
		final StorageContainer cont = new StorageContainer(container);
		if (cont.getLocatedAtPosition() != null
				&& cont.getLocatedAtPosition().getParentContainer() != null)
		{
			final ContainerPosition cntPos = cont.getLocatedAtPosition();
			cntPos.setPositionDimensionOne(Integer.valueOf(posDimOne));
			cntPos.setPositionDimensionTwo(Integer.valueOf(posDimTwo));
			cntPos.setOccupiedContainer(cont);
			cont.setLocatedAtPosition(cntPos);
		}
		return cont;
	}
	/**
	 * @param obj - AbstractDomainObject.
	 * @return string array of DynamicGroups
	 * @throws SMException - throws SMException
	 */
	protected String[] getDynamicGroups(final AbstractDomainObject obj) throws SMException
	{
		String[] dynamicGroups = null;
		final StorageContainer storageContainer = (StorageContainer) obj;

		if (storageContainer.getLocatedAtPosition() != null
				&& storageContainer.getLocatedAtPosition().getParentContainer() != null)
		{
			dynamicGroups = SecurityManagerFactory.getSecurityManager().getProtectionGroupByName(
					storageContainer.getLocatedAtPosition().getParentContainer());
		}
		else
		{
			dynamicGroups = SecurityManagerFactory.getSecurityManager().getProtectionGroupByName(
					storageContainer.getSite());
		}
		return dynamicGroups;
	}
	/**
	 * @param obj AbstractDomainObject
	 * @param dao DAO object
	 * @param sessionDataBean SessionDataBean
	 */
	public void postInsert(final Object obj, final DAO dao, final SessionDataBean sessionDataBean)
			throws BizLogicException
	{
		super.postInsert(obj, dao, sessionDataBean);
	}

	/**
	 * Updates the persistent object in the database.
	 * @param dao DAO object
	 * @param obj
	 *            The object to be updated.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void update(final DAO dao, final Object obj, final Object oldObj,
			final SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			final StorageContainer container = (StorageContainer) obj;
			final StorageContainer oldContainer = (StorageContainer) oldObj;
			final Object object = dao.retrieveById(StorageContainer.class.getName(), oldContainer
					.getId());
			StorageContainer oldContForChange = (StorageContainer) object;
			validateContainer(dao, sessionDataBean, container, oldContainer);
			final Collection<SpecimenPosition> specimenPosColl = this
					.getSpecimenPositionCollForContainer(dao, container.getId());
			container.setSpecimenPositionCollection(specimenPosColl);
			this.setValuesinPersistentObject(oldContForChange, container, dao);
			dao.update(oldContForChange,oldContainer);
			dao.update(oldContForChange.getCapacity(),oldContainer.getCapacity());
			if (container.getActivityStatus().equals(Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				final Long[] containerIDArr = {container.getId()};
				if (StorageContainerUtil.isContainerAvailableForDisabled(dao, containerIDArr))
				{
					final List disabledConts = new ArrayList();
					final List<StorageContainer> disabledContList = new ArrayList<StorageContainer>();
					disabledContList.add(oldContForChange);
					this.addEntriesInDisabledMap(oldContForChange, disabledConts);
					this.setDisableToSubContainer(oldContForChange, disabledConts,
							dao, disabledContList);
					oldContForChange.getOccupiedPositions().clear();
					this.disableSubStorageContainer(dao, sessionDataBean, disabledContList);
					updateConPOs(dao, oldContainer, oldContForChange);
				}
				else
				{
					throw this.getBizLogicException(null, "errors.container.contains.specimen", "");
				}
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}
	/**
	 * @param dao DAO object
	 * @param sessionDataBean SessionDataBean object
	 * @param container New Container
	 * @param oldContainer Old container
	 * @throws DAOException DAOException Exception
	 * @throws BizLogicException BizLogicException Exception
	 */
	private void validateContainer(final DAO dao, final SessionDataBean sessionDataBean,
			final StorageContainer container, final StorageContainer oldContainer)
			throws DAOException, BizLogicException
	{
		checkContainer(dao, container, oldContainer);
		checkContainerPos(dao, sessionDataBean, container, oldContainer);
		checkCapacity(dao, container, oldContainer);
		checkSite(dao, container);
	}
	/**
	 * @param dao DAO object
	 * @param oldContainer oldContainer Object
	 * @param oldCont Persistent container
	 * @throws DAOException DAOException
	 */
	private void updateConPOs(final DAO dao, final StorageContainer oldContainer,
			final StorageContainer oldCont) throws DAOException
	{
		final ContainerPosition prevPosition = oldCont
				.getLocatedAtPosition();
		oldCont.setLocatedAtPosition(null);
		dao.update(oldCont,oldContainer);
		if (prevPosition != null)
		{
			dao.delete(prevPosition);
		}
	}
	/**
	 * @param dao DAO object
	 * @param container container Object
	 * @param oldContainer oldContainer Object
	 * @throws DAOException DAOException
	 * @throws BizLogicException BizLogicException
	 */
	private void checkContainer(final DAO dao, final StorageContainer container,
			final StorageContainer oldContainer) throws DAOException, BizLogicException
	{
		if (container.getLocatedAtPosition() != null)
		{
			final StorageContainer parentStrgCont = (StorageContainer) dao
					.retrieveById(StorageContainer.class.getName(), container
							.getLocatedAtPosition().getParentContainer().getId());
			container.getLocatedAtPosition().setParentContainer(parentStrgCont);
		}
		logger.debug("container.isParentChanged() : " + container.isParentChanged());
		if (container.isParentChanged())
		{
			checkParent(dao, container);
		}
		else
		{
			checkPosChanged(dao, container, oldContainer);
		}
	}
	/**
	 * @param dao DAO object
	 * @param container StorageContainer object
	 * @throws BizLogicException BizLogicException
	 */
	private void checkSite(final DAO dao, final StorageContainer container)
			throws BizLogicException
	{
		final SiteBizLogic site= new SiteBizLogic();
		if (container.getId() != null)
		{
			site.checkClosedSite(dao, container.getId(), "Container site");
		}
		site.setSiteForSubContainers(container, container.getSite(), dao);
	}
	/**
	 * @param dao DAO object
	 * @param container StorageContainer Object
	 * @param oldContainer Old StorageContainer Object
	 * @throws BizLogicException BizLogicException
	 * @throws DAOException DAOException
	 */
	private void checkCapacity(final DAO dao, final StorageContainer container,
			final StorageContainer oldContainer) throws DAOException, BizLogicException
	{
		final Integer oldContDimOne = oldContainer.getCapacity().getOneDimensionCapacity();
		final Integer oldContDimTwo = oldContainer.getCapacity().getTwoDimensionCapacity();
		final Integer newContDimOne = container.getCapacity().getOneDimensionCapacity();
		final Integer newContDimTwo = container.getCapacity().getTwoDimensionCapacity();
		if (oldContDimOne.intValue() > newContDimOne.intValue()
				|| oldContDimTwo.intValue() > newContDimTwo.intValue())
		{
			final boolean canReduceDim = StorageContainerUtil.canReduceDimension(dao, oldContainer
					.getId(), newContDimOne, newContDimTwo);
			if (!canReduceDim)
			{
				throw this.getBizLogicException(null, "errors.storageContainer.cannotReduce",
						"");
			}
		}
	}
	/**
	 * @param dao DAO object
	 * @param container StorageContainer Object
	 * @param sessionDataBean SessionDataBean object
	 * @param oldContainer Old StorageContainer Object
	 * @throws BizLogicException BizLogicException
	 */
	private void checkContainerPos(final DAO dao, final SessionDataBean sessionDataBean,
			final StorageContainer container, final StorageContainer oldContainer)
			throws BizLogicException
	{
		boolean flag = true;
		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null
				&& oldContainer.getLocatedAtPosition() != null)
		{
			flag = compareOldNewObjPos(container, oldContainer);
		}
		checkPos(dao, sessionDataBean, container, flag);
	}
	/**
	 * @param container StorageContainer Object
	 * @param oldContainer Old StorageContainer Object
	 * @return boolean
	 */
	private boolean compareOldNewObjPos(final StorageContainer container,
			final StorageContainer oldContainer)
	{
		boolean flag = true;
		if(container.getLocatedAtPosition().getParentContainer().getId().longValue() == oldContainer
					.getLocatedAtPosition().getParentContainer().getId().longValue()
			&& container.getLocatedAtPosition().getPositionDimensionOne().longValue() == oldContainer
					.getLocatedAtPosition().getPositionDimensionOne().longValue()
			&& container.getLocatedAtPosition().getPositionDimensionTwo().longValue() == oldContainer
					.getLocatedAtPosition().getPositionDimensionTwo().longValue())
			{
				flag = false;
			}
		return flag;
	}
	/**
	 * @param dao DAO Object
	 * @param sessionDataBean SessionDataBean Object
	 * @param container StorageContainer object
	 * @param flag Boolean flag to check position
	 * @throws BizLogicException BizLogicException
	 */
	private void checkPos(final DAO dao, final SessionDataBean sessionDataBean,
			final StorageContainer container, final boolean flag) throws BizLogicException
	{
		if (flag && container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null)
		{
			final String contId=container.getLocatedAtPosition().
			getParentContainer().getId().toString();
			final String pos1=container.getLocatedAtPosition()
			.getPositionDimensionOne().toString();
			final String pos2=container.getLocatedAtPosition()
			.getPositionDimensionTwo().toString();
			this.checkContainer(dao,StorageContainerUtil.setparameterList(contId, pos1, pos2, false),
			sessionDataBean,null);
		}
	}
	/**
	 * @param dao DAO object
	 * @param container StorageContainer Object
	 * @param oldContainer Old StorageContainer Object
	 * @throws BizLogicException BizLogicException
	 * @throws DAOException DAOException
	 */
	private void checkPosChanged(final DAO dao, final StorageContainer container,
			final StorageContainer oldContainer) throws DAOException, BizLogicException
	{
		if (container.isPositionChanged())
		{
			comparePos(dao, container, oldContainer);
		}
	}
	/**
	 * @param dao DAO object
	 * @param container StorageContainer Object
	 * @param oldContainer Old StorageContainer Object
	 * @throws BizLogicException BizLogicException
	 * @throws DAOException DAOException
	 */
	private void comparePos(final DAO dao, final StorageContainer container,
			final StorageContainer oldContainer) throws DAOException, BizLogicException
	{
		checkDimentionOverflow(dao, container);
		if (oldContainer.getLocatedAtPosition() != null
				&& oldContainer.getLocatedAtPosition().getPositionDimensionOne() != null
				&& oldContainer.getLocatedAtPosition().getPositionDimensionOne()
						.intValue() != container.getLocatedAtPosition()
						.getPositionDimensionOne().intValue()
				|| oldContainer.getLocatedAtPosition().getPositionDimensionTwo()
						.intValue() != container.getLocatedAtPosition()
						.getPositionDimensionTwo().intValue())
		{
			caUseCont(dao, container);
		}
	}
	/**
	 * @param dao DAO object
	 * @param container StorageContainer Object
	 * @throws BizLogicException BizLogicException
	 * @throws DAOException DAOException
	 */
	private void checkDimentionOverflow(final DAO dao, final StorageContainer container)
			throws DAOException, BizLogicException
	{
		final String sourceObjectName = StorageContainer.class.getName();
		final String[] selectColumnName = {"id", "capacity.oneDimensionCapacity",
				"capacity.twoDimensionCapacity"};
		final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause("id", container
				.getLocatedAtPosition().getParentContainer().getId()));
		final List list = dao.retrieve(sourceObjectName, selectColumnName,
				queryWhereClause);
		if (!list.isEmpty())
		{
			final Object[] obj1 = (Object[]) list.get(0);
			final Integer pcCapacityOne = (Integer) obj1[1];
			final Integer pcCapacityTwo = (Integer) obj1[2];
			if (!StorageContainerUtil.validatePosition(pcCapacityOne.intValue(), pcCapacityTwo
					.intValue(), container))
			{
				throw this.getBizLogicException(null,
						"errors.storageContainer.dimensionOverflow", "");
			}
		}
	}
	/**
	 * @param dao DAO object
	 * @param container StorageContainer Object
	 * @throws BizLogicException BizLogicException
	 */
	private void checkParent(final DAO dao, final StorageContainer container)
			throws BizLogicException
	{
		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null)
		{
			if (StorageContainerUtil.isUnderSubContainer(container, container.getLocatedAtPosition()
					.getParentContainer().getId(), dao))
			{
				throw this.getBizLogicException(null,
						"errors.container.under.subcontainer", "");
			}
			logger.debug("Loading ParentContainer: "
					+ container.getLocatedAtPosition().getParentContainer().getId());
			if (!StorageContainerUtil.validatePosition(dao, container))
			{
				throw this.getBizLogicException(null,
						"errors.storageContainer.dimensionOverflow", "");
			}
			caUseCont(dao, container);
			this.checkStatus(dao, container.getLocatedAtPosition().getParentContainer(),
					"Parent Container");
			final Site site = new SiteBizLogic().getSite(dao, container.getLocatedAtPosition()
					.getParentContainer().getId());
			this.checkStatus(dao, site, "Parent Container Site");
			container.setSite(site);
		}
	}
	/**
	 * @param dao DAO object
	 * @param container StorageContainer Object
	 * @throws BizLogicException BizLogicException
	 */
	private void caUseCont(final DAO dao, final StorageContainer container)
			throws BizLogicException
	{
		final boolean canUse = StorageContainerUtil.isContainerAvailableForPositions(dao, container);
		logger.debug("canUse : " + canUse);
		if (!canUse)
		{
			throw this.getBizLogicException(null, "errors.storageContainer.inUse", "");
		}
	}
	/**
	 * @param persistentobject - StorageContainer persistent object.
	 * @param newObject - StorageContainer newObject
	 * @param dao - DAO object
	 * @throws BizLogicException throws BizLogicException
	 */
	private void setValuesinPersistentObject(StorageContainer persistentobject,
			final StorageContainer newObject, final DAO dao) throws BizLogicException
	{
		try
		{
			persistentobject.setActivityStatus(newObject.getActivityStatus());
			persistentobject.setBarcode(newObject.getBarcode());
			final Capacity persistCapacity = persistentobject.getCapacity();
			final Capacity newCapacity = newObject.getCapacity();
			persistCapacity.setOneDimensionCapacity(newCapacity.getOneDimensionCapacity());
			persistCapacity.setTwoDimensionCapacity(newCapacity.getTwoDimensionCapacity());
			final Collection children = this.getChildren(dao, newObject.getId());
			this.setChildren(children, dao, persistentobject.getId());
			persistentobject.setCollectionProtocolCollection(newObject
					.getCollectionProtocolCollection());
			persistentobject.setComment(newObject.getComment());
			persistentobject.setFull(newObject.getFull());
			persistentobject.setHoldsSpecimenArrayTypeCollection(newObject
					.getHoldsSpecimenArrayTypeCollection());
			persistentobject.setHoldsSpecimenClassCollection(newObject
					.getHoldsSpecimenClassCollection());
			persistentobject.setHoldsStorageTypeCollection(newObject
					.getHoldsStorageTypeCollection());
			persistentobject.setHoldsSpecimenTypeCollection(
					newObject.getHoldsSpecimenTypeCollection());
			persistentobject.setName(newObject.getName());
			persistentobject.setNoOfContainers(newObject.getNoOfContainers());
			persistentobject.setParentChanged(newObject.isParentChanged());
			persistentobject.setPositionChanged(newObject.isPositionChanged());
			setContPos(persistentobject, newObject);
			persistentobject.setSimilarContainerMap(newObject.getSimilarContainerMap());
			persistentobject.setSite(newObject.getSite());
			persistentobject.setStartNo(newObject.getStartNo());
			persistentobject.setStorageType(newObject.getStorageType());
			persistentobject.setTempratureInCentigrade(newObject.getTempratureInCentigrade());
		}
		catch (final ApplicationException exp)
		{
			logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
			throw this.getBizLogicException(exp, exp.getErrorKeyName(), exp.getMsgValues());
		}
	}
	/**
	 * Set container position.
	 * @param persistentobject StorageContainer object
	 * @param newObject StorageContainer object
	 */
	private void setContPos(StorageContainer persistentobject, StorageContainer newObject)
	{
		if (newObject.getLocatedAtPosition() != null)
		{
			ContainerPosition cntPos = persistentobject.getLocatedAtPosition();
			if (cntPos == null)
			{
				cntPos = new ContainerPosition();
				persistentobject.setLocatedAtPosition(cntPos);
			}
			cntPos.setPositionDimensionOne(newObject.getLocatedAtPosition()
					.getPositionDimensionOne());
			cntPos.setPositionDimensionTwo(newObject.getLocatedAtPosition()
					.getPositionDimensionTwo());
			cntPos.setParentContainer(newObject.getLocatedAtPosition().getParentContainer());
			cntPos.setOccupiedContainer(persistentobject);
		}
		if (newObject.getSpecimenPositionCollection() != null)
		{
			final Collection<SpecimenPosition> specPosColl = persistentobject
					.getSpecimenPositionCollection();
			specPosColl.addAll(newObject.getSpecimenPositionCollection());
		}
	}
	/**
	* @param children Children Collection
	* @param dao DAO object
	* @param containerId container identifier
	* @throws ApplicationException ApplicationException
	*/
	private void setChildren(Collection children, DAO dao, Long containerId)
			throws ApplicationException
	{
		if (children != null)
		{
			getChildren(dao, containerId).addAll(children);
		}
	}
	/**
	* @param children Children Collection
	* @param dao DAO object
	* @param containerId container identifier
	* @throws ApplicationException ApplicationException
	* @return childrenColl
	*/
	public Collection getChildren(DAO dao, Long containerId) throws ApplicationException
	{
		final String hql = "select cntPos.occupiedContainer from ContainerPosition cntPos, StorageContainer container where cntPos.occupiedContainer.id=container.id and cntPos.parentContainer.id ="
				+ containerId;
		List childrenColl = new ArrayList();
		childrenColl = dao.executeQuery(hql);
		return childrenColl;
	}

	/**
	 * @param dao DAO object
	 * @param currentObj Transient object
	 * @param oldObj Persistent object
	 * @param sessionDataBean SessionDataBean object
	 *
	 */
	public void postUpdate(DAO dao, Object currentObj, Object oldObj,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			super.postUpdate(dao, currentObj, oldObj, sessionDataBean);
		}
		catch (final ApplicationException e)
		{
			logger.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
		}
	}
	/**
	 * @param dao - DAO object.
	 * @param sessionDataBean - SessionDataBean object
 	 * @param parameterList Parameter list includes SC Id, pos1, pos2 and boolean multiplespecimen
	 * @param specimen -Specimen object
	 * @throws BizLogicException throws BizLogicException
	 */
	public void checkContainer(DAO dao, final List<Object> parameterList,
			SessionDataBean sessionDataBean,Specimen specimen)
			throws BizLogicException
	{
		final String scID = (String)parameterList.get(0);
		final String posOne = (String)parameterList.get(1);
		final String posTwo = (String)parameterList.get(2);
		try
		{
			final List list = reteriveSCObject(dao, scID);
			if (!list.isEmpty())
			{
				final StorageContainer strCont = populateSCObject(list);
				final boolean hasAccess = StorageContainerUtil.validateContainerAccess(dao, strCont, sessionDataBean);
				logger.debug("hasAccess..............." + hasAccess);
				if (!hasAccess)
				{
					throw this.getBizLogicException(null, "access.use.object.denied", "");
				}
				this.checkStatus(dao, strCont, "Storage Container");
				new SiteBizLogic().checkClosedSite(dao, strCont.getId(), "Container Site");
				final boolean isValidPosition = StorageContainerUtil.validatePosition(strCont, posOne, posTwo);
				logger.debug("isValidPosition : " + isValidPosition);
				if (isValidPosition)
				{
					canUsePosition(dao, specimen,parameterList, strCont);
				}
				else
				{
					throw this.getBizLogicException(null,
							"errors.storageContainer.dimensionOverflow", "");
				}
			}
			else
			{
				throw this.getBizLogicException(null, "errors.storageContainerExist", "");
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}
	/**
	 * @param dao DAO object
	 * @param specimen Specimen Object
	 * @param parameterList Parameter List includes SC identifier, pos1, pos2
	 * @param stcont StorageContainer object
	 * @throws BizLogicException BizLogicException
	 */
	private void canUsePosition(DAO dao, Specimen specimen,final List<Object> parameterList,
			final StorageContainer stcont) throws BizLogicException
	{
		final String posOne = (String)parameterList.get(1);
		final String posTwo = (String)parameterList.get(2);
		final boolean multipleSpecimen = (Boolean)parameterList.get(3);

	    final boolean canUsePosition = StorageContainerUtil.isPositionAvailable(dao, stcont, posOne, posTwo,
				specimen);
		logger.debug("canUsePosition : " + canUsePosition);
		if (!canUsePosition)
		{
			if (multipleSpecimen)
			{

				throw this.getBizLogicException(null,
						"errors.storageContainer.Multiple.inUse", "");
			}
			else
			{
				throw this.getBizLogicException(null, "errors.storageContainer.inUse",
						"");
			}
		}
	}
	/**
	 * @param dao DAO object
	 * @param strgContID StorageContainer Id
	 * @return List of Storage Container object
	 * @throws DAOException DAOException
	 */
	private List reteriveSCObject(DAO dao, String strgContID) throws DAOException
	{
		final String sourceObjectName = StorageContainer.class.getName();
		final String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER,
				"capacity.oneDimensionCapacity", "capacity.twoDimensionCapacity", "name"};
		final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause(Constants.SYSTEM_IDENTIFIER, Long
				.valueOf(strgContID)));
		return dao.retrieve(sourceObjectName, selectColumnName, queryWhereClause);
	}
	/**
	 * @param list List of SC object
	 * @return StorageContainer
	 */
	private StorageContainer populateSCObject(final List list)
	{
		final Object[] obj = (Object[]) list.get(0);
		final StorageContainer stCont = new StorageContainer();
		stCont.setId((Long) obj[0]);
		stCont.setName((String) obj[3]);
		final Capacity cntPos = new Capacity();
		if (obj[1] != null && obj[2] != null)
		{
			cntPos.setOneDimensionCapacity((Integer) obj[1]);
			cntPos.setTwoDimensionCapacity((Integer) obj[2]);
			stCont.setCapacity(cntPos);
		}
		return stCont;
	}
	/**
	 * Overriding the parent class's method to validate the enumerated attribute
	 * values.
	 * @param obj Domain Object
	 * @param dao DAO object
	 * @param operation Add/Edit
	 * @return boolean
	 */
	protected boolean validate(Object obj, DAO dao, String operation) throws BizLogicException
	{
		try
		{
			if (obj == null)
			{
				throw this.getBizLogicException(null, "domain.object.null.err.msg", "");
			}
			final StorageContainer container = (StorageContainer) obj;
			/**
			 * Start: Change for API Search --- Jitendra 06/10/2006 In Case of Api
			 * Search, default values will not get set for the object since
			 * setAllValues() method of domainObject will not get called. To avoid
			 * null pointer exception, we are setting the default values same we
			 * were setting in setAllValues() method of domainObject.
			 */
			ApiSearchUtil.setContainerDefault(container);
			final Validator validator = new Validator();
			validateContainer(container, validator);
			validateStTypeAndTemp(container, validator);
			validatePosition(dao, container, validator);
			validateStatus(operation, container);

			StorageContainerUtil.populateSpecimenType(container);
			return true;
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}

	}
	/**
	 * @param operation Operation
	 * @param container Storage Container
	 * @throws BizLogicException BizLogicException
	 */
	private void validateStatus(String operation, final StorageContainer container)
			throws BizLogicException
	{
		if (operation.equals(Constants.ADD))
		{
			if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(container.getActivityStatus()))
			{
				throw this.getBizLogicException(null, "activityStatus.active.errMsg", "");
			}
			if (container.getFull().booleanValue())
			{
				throw this.getBizLogicException(null,
						"storageContainer.isContainerFull.errMsg", "");
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES, container
					.getActivityStatus()))
			{
				throw this.getBizLogicException(null, "activityStatus.errMsg", "");
			}
		}
	}
	/**
	 * @param container Storage Container object
	 * @param validator Validator Object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateStTypeAndTemp(final StorageContainer container, final Validator validator)
			throws BizLogicException
	{
		String message;
		if (container.getStorageType() == null)
		{
			message = ApplicationProperties.getValue("storageContainer.type");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		if (container.getTempratureInCentigrade() != null
				&& !Validator.isEmpty(container.getTempratureInCentigrade().toString())
				&& (!validator
						.isDouble(container.getTempratureInCentigrade().toString(), false)))
		{
			message = ApplicationProperties.getValue("storageContainer.temperature");
			throw this.getBizLogicException(null, "errors.item.format", message);
		}
	}
	/**
	 * @param dao DAo object
	 * @param container Storage Container object
	 * @param validator Validator Object
	 * @throws DAOException DAOException
	 * @throws BizLogicException BizLogicException
	 */
	private void validatePosition(DAO dao, final StorageContainer container,
			final Validator validator) throws DAOException, BizLogicException
	{
		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null)
		{
			if (container.getLocatedAtPosition().getParentContainer().getId() == null)
			{
				validateContPos(dao, container);
			}
			validateForContFull(dao, container);
			validateDimOne(container, validator);
			validateDimTwo(container, validator);
		}
	}
	/**
	 * @param container Storage Container object
	 * @param validator Validator Object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateDimOne(final StorageContainer container, final Validator validator)
			throws BizLogicException
	{
		String message;
		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getPositionDimensionOne() != null
				&& Validator.isEmpty(String.valueOf(container.getLocatedAtPosition()
						.getPositionDimensionOne())))
		{
			message = ApplicationProperties.getValue("storageContainer.oneDimension");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		else
		{
			validateIsNumeric(container, validator);
		}
	}
	/**
	 * @param container Storage Container object
	 * @param validator Validator Object
	 * @throws BizLogicException BizLogicException
	 */
	 private void validateIsNumeric(final StorageContainer container, final Validator validator)
			throws BizLogicException
	{
		String message;
		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getPositionDimensionOne() != null
				&& !validator.isNumeric(String.valueOf(container.getLocatedAtPosition()
						.getPositionDimensionOne())))
		{
			message = ApplicationProperties.getValue("storageContainer.oneDimension");
			throw this.getBizLogicException(null, "errors.item.format", message);
		}
	}
	/**
	 * @param container Storage Container object
	 * @param validator Validator Object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateDimTwo(final StorageContainer container, final Validator validator)
			throws BizLogicException
	{
		String message;
		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getPositionDimensionTwo() != null
				&& !Validator.isEmpty(String.valueOf(container.getLocatedAtPosition()
						.getPositionDimensionTwo()))
				&& (!validator.isNumeric(String.valueOf(container.getLocatedAtPosition()
						.getPositionDimensionTwo()))))
		{
			message = ApplicationProperties.getValue("storageContainer.twoDimension");
			throw this.getBizLogicException(null, "errors.item.format", message);

		}
	}
	/**
	 * @param dao DAO Object
	 * @param container StorageContainer object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateForContFull(DAO dao, final StorageContainer container)
			throws BizLogicException
	{
		final Integer xPos = container.getLocatedAtPosition().getPositionDimensionOne();
		final Integer yPos = container.getLocatedAtPosition().getPositionDimensionTwo();
		/**
		 * Following code is added to set the x and y dimension in case only
		 * storage container is given and x and y positions are not given
		 */
		if (xPos == null || yPos == null)
		{
			final Container cont = container.getLocatedAtPosition().getParentContainer();
			final Position position = StorageContainerUtil.getFirstAvailablePositionInContainer(cont, dao);
			if (position != null)
			{
				final ContainerPosition cntPos = container.getLocatedAtPosition();
				cntPos.setPositionDimensionOne(position.getXPos());
				cntPos.setPositionDimensionTwo(position.getYPos());
				cntPos.setOccupiedContainer(container);
			}
			else
			{
				throw this.getBizLogicException(null, "storage.specified.full", "");
			}
		}
	}
	/**
	 * @param dao DAo object
	 * @param container Storage Container object
	 * @throws DAOException DAOException
	 * @throws BizLogicException BizLogicException
	 */
	private void validateContPos(DAO dao, final StorageContainer container) throws DAOException,
			BizLogicException
	{
		final String sourceObjectName = StorageContainer.class.getName();
		final String[] selectColumnName = {"id"};
		final QueryWhereClause queryWhereClause = new QueryWhereClause(sourceObjectName);
		queryWhereClause.addCondition(new EqualClause("name", container
				.getLocatedAtPosition().getParentContainer().getName()));
		final List list = dao.retrieve(sourceObjectName, selectColumnName,
				queryWhereClause);
		if (!list.isEmpty())
		{
			container.getLocatedAtPosition().getParentContainer().setId(
					(Long) list.get(0));
		}
		else
		{
			final String message1 = ApplicationProperties
					.getValue("specimen.storageContainer");
			throw this.getBizLogicException(null, "errors.invalid", message1);
		}
	}
	/**
	 * @param container Storage Container object
	 * @param validator Validator Object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateContainer(final StorageContainer container, final Validator validator)
			throws BizLogicException
	{
		String message;
		if (container.getNoOfContainers() == null)
		{
			container.setNoOfContainers(Integer.valueOf(1));
		}
		if (Validator.isEmpty(container.getNoOfContainers().toString()))
		{
			message = ApplicationProperties.getValue("storageContainer.noOfContainers");
			throw this.getBizLogicException(null, "errors.item.required", message);
		}
		if (!validator.isNumeric(container.getNoOfContainers().toString(), 1))
		{
			message = ApplicationProperties.getValue("storageContainer.noOfContainers");
			throw this.getBizLogicException(null, "errors.item.format", message);
		}
		validateContParent(container);
	}
	/**
	 * @param container StorageContainer object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateContParent(final StorageContainer container) throws BizLogicException
	{
		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() == null)
		{
			validateContSite(container);
		}
	}
	/**
	 * @param container StorageContainer object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateContSite(final StorageContainer container) throws BizLogicException
	{
		String message;
		if (container.getSite() == null || container.getSite().getId() == null
				|| container.getSite().getId() <= 0)
		{
			message = ApplicationProperties.getValue("storageContainer.site");
			throw this.getBizLogicException(null, "errors.item.invalid", message);
		}
	}

	/**
	 * @return list of storage containers
	 * @throws BizLogicException throws BizLogicException
	 */
	public List getStorageContainerList() throws BizLogicException
	{
		final String sourceObjectName = StorageContainer.class.getName();
		final String[] displayNameFields = {Constants.SYSTEM_IDENTIFIER};
		final String valueField = Constants.SYSTEM_IDENTIFIER;
		return this.getList(sourceObjectName, displayNameFields, valueField, true);
	}

	/**
	 * To get the ids of the CollectionProtocol that the given StorageContainer
	 * can hold.
	 * @param type
	 *            The reference to StorageType object.
	 * @return The array of ids of CollectionProtocol that the given
	 *         StorageContainer can hold.
	 * @throws BizLogicException throws BizLogicException
	 */
	public long[] getDefaultHoldCollectionProtocolList(StorageContainer container)
			throws BizLogicException
	{
		final Collection spArrayTypeColl = (Collection) this.retrieveAttribute(
				StorageContainer.class.getName(), container.getId(),
				"elements(collectionProtocolCollection)");
		long[] cpList = null;
		if (spArrayTypeColl.isEmpty())
		{
			cpList = new long[]{-1};
		}
		else
		{
			cpList = AppUtility.getobjectIds(spArrayTypeColl);
		}
		return cpList;
	}

	/**
	 * @param dao - DAO object.
	 * @param containerId - Long
	 * @return  collection of SpecimenPosition
	 * @throws BizLogicException throws BizLogicException
	 */
	private Collection<SpecimenPosition> getSpecimenPositionCollForContainer(DAO dao,
			Long containerId) throws BizLogicException
	{
		List<SpecimenPosition> specimenPosColl = null;
		try
		{
			if (containerId != null)
			{
				specimenPosColl	= dao.retrieve(SpecimenPosition.class.getName(),
						"storageContainer.id", containerId);
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
		return specimenPosColl;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check.
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 * @param dao DAO object
	 * @param domainObject Abstract Domain object
	 */
	public String getObjectId(DAO dao, Object domainObject) throws BizLogicException
	{
		String objId = null;
		if (domainObject instanceof StorageContainer)
		{
			final StorageContainer storageContainer = (StorageContainer) domainObject;
			Site site = getSite(dao, storageContainer);
			if (site != null)
			{
				final StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(Site.class.getName()).append("_").append(site.getId().toString());
				objId = stringBuffer.toString();
			}
		}
		return objId;
	}

	private Site getSite(DAO dao, final StorageContainer storageContainer)
			throws BizLogicException
	{
		Site site = null;
		if (storageContainer.getLocatedAtPosition() != null
				&& storageContainer.getLocatedAtPosition().getParentContainer() != null)
		{
			try
			{
				final Object object = dao.retrieveById(StorageContainer.class.getName(),
						storageContainer.getLocatedAtPosition().getParentContainer().getId());
				if (object != null)
				{
					final StorageContainer parentContainer = (StorageContainer) object;
					site = parentContainer.getSite();
				}
			}
			catch (final DAOException e)
			{
				logger.error(e.getMessage(), e);
				e.printStackTrace();
				throw this
				.getBizLogicException(e, e.getErrorKeyName(), e.getMsgValues());
			}
		}
		else
		{
			site = storageContainer.getSite();
		}
		return site;
	}

	/**
	 * To get PrivilegeName for authorization check from 'PermissionMapDetails.xml'.
	 * (non-Javadoc)
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getPrivilegeName(java.lang.Object)
	 * @param domainObject AbstractDomain object
	 */
	protected String getPrivilegeKey(Object domainObject)
	{
		return Constants.ADD_EDIT_STORAGE_CONTAINER;
	}
	/**
	 * @param dao - DAO object.
	 * @param sessionDataBean - SessionDataBean object
	 * @param disableContList - list of StorageContainers
	 * @throws BizLogicException throws BizLogicException
	 */
	private void disableSubStorageContainer(DAO dao, SessionDataBean sessionDataBean,
			List<StorageContainer> disableContList) throws BizLogicException
	{
		try
		{
			final int count = disableContList.size();
			final List containerIdList = new ArrayList();
			for (int i = 0; i < count; i++)
			{
				final StorageContainer container = disableContList.get(i);
				containerIdList.add(container.getId());
			}
			final List listOfSpecimenIDs = this.getRelatedObjects(dao, Specimen.class,
					"specimenPosition.storageContainer", edu.wustl.common.util.Utility
							.toLongArray(containerIdList));
			if (!listOfSpecimenIDs.isEmpty())
			{

				throw this.getBizLogicException(null, "errors.container.contains.specimen", "");
			}
			for (int i = 0; i < count; i++)
			{
				final StorageContainer container = disableContList.get(i);
				dao.update(container);
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			throw this
					.getBizLogicException(daoExp, daoExp.getErrorKeyName(), daoExp.getMsgValues());
		}
	}
	/**
	 * @param storageContainer - StorageContainer object.
	 * @param disabledConts -List of disabledConts
	 * @param dao - DAO object
	 * @param disableContList - list of disabledContainers
	 * @throws BizLogicException throws BizLogicException
	 */
	private void setDisableToSubContainer(StorageContainer storageContainer, List disabledConts,
			DAO dao, List disableContList) throws BizLogicException
	{

		try
		{
			if (storageContainer != null)
			{
				final Collection childrenColl = new StorageContainerBizLogic().getChildren(dao,
						storageContainer.getId());
				final Iterator iterator = childrenColl.iterator();
				while (iterator.hasNext())
				{
					final StorageContainer container = (StorageContainer) iterator.next();
					container.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.toString());
					this.addEntriesInDisabledMap(container, disabledConts);
					container.setLocatedAtPosition(null);
					disableContList.add(container);
					setDisableToSubContainer(container, disabledConts, dao,
							disableContList);
				}
			}
			storageContainer.getOccupiedPositions().clear();

		}
		catch (final ApplicationException exp)
		{
			logger.error(exp.getMessage(), exp);
			exp.printStackTrace();
			final ErrorKey errorKey = ErrorKey.getErrorKey(exp.getErrorKeyName());
			throw new BizLogicException(errorKey, exp, exp.getMsgValues());
		}

	}
	/**
	 * @param container - StorageContainer object.
	 * @param disabledConts - List of disabledConts
	 */
	private void addEntriesInDisabledMap(StorageContainer container, List disabledConts)
	{
		final String contNameKey = "StorageContName";
		final String contIdKey = "StorageContIdKey";
		final String parentContNameKey = "ParentContName";
		final String parentContIdKey = "ParentContId";
		final String pos1Key = "pos1";
		final String pos2Key = "pos2";
		final Map<String, Object> containerDetails = new TreeMap<String, Object>();
		containerDetails.put(contNameKey, container.getName());
		containerDetails.put(contIdKey, container.getId());
		if (container != null && container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null)
		{
			containerDetails.put(parentContNameKey, container.getLocatedAtPosition()
					.getParentContainer().getName());
			containerDetails.put(parentContIdKey, container.getLocatedAtPosition()
					.getParentContainer().getId());
			containerDetails.put(pos1Key, container.getLocatedAtPosition()
					.getPositionDimensionOne());
			containerDetails.put(pos2Key, container.getLocatedAtPosition()
					.getPositionDimensionTwo());
		}
		disabledConts.add(containerDetails);
	}
}