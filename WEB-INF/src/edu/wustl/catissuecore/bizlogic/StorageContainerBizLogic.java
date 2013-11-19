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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.wustl.catissuecore.domain.AbstractPosition;
import edu.wustl.catissuecore.domain.Capacity;
import edu.wustl.catissuecore.domain.Container;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
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
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.EqualClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;
import edu.wustl.security.exception.SMException;
import edu.wustl.security.manager.SecurityManagerFactory;

import edu.wustl.dao.query.generator.ColumnValueBean;

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
	private static final transient Logger logger = Logger
			.getCommonLogger(StorageContainerBizLogic.class);

	/**
	 * Saves the storageContainer object in the database.
	 * @param dao - DAo object
	 * @param obj
	 *            The storageType object to be saved.
	 * @param sessionDataBean
	 *            The session in which the object is saved.
	 * @throws BizLogicException throws BizLogicException
	 */
	protected void insert(final Object obj, final DAO dao,
			final SessionDataBean sessionDataBean) throws BizLogicException
	{
		try
		{
			final StorageContainer container = (StorageContainer) obj;
			container.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE
					.toString());
			final SiteBizLogic sBiz = new SiteBizLogic();
			final StorageTypeBizLogic stBiz = new StorageTypeBizLogic();
			int posOneCapacity = 1, posTwoCapacity = 1;
			int posDimOne = Constants.STORAGE_CONTAINER_FIRST_ROW, posDimTwo = Constants.STORAGE_CONTAINER_FIRST_COLUMN;
			String posDimOneString = null, posDimTwoString = null;
			boolean[][] fullStatus = null;
			final int noOfContainers = container.getNoOfContainers().intValue();
			if (container.getLocatedAtPosition() != null
					&& container.getLocatedAtPosition().getParentContainer() != null)
			{
				final Object object = dao.retrieveById(
						StorageContainer.class.getName(), container
								.getLocatedAtPosition().getParentContainer()
								.getId());
				if (object != null)
				{
					final StorageContainer parentContainer = (StorageContainer) object;
					this.checkStatus(dao, parentContainer, "Parent Container");
					final int totalCapacity = parentContainer.getCapacity()
							.getOneDimensionCapacity().intValue()
							* parentContainer.getCapacity()
									.getTwoDimensionCapacity().intValue();
					final Collection children = this.getChildren(dao,
							parentContainer.getId());
					if ((noOfContainers + children.size()) > totalCapacity)
					{
						throw this.getBizLogicException(null,
								"errors.storageContainer.overflow", "");
					}
					else
					{
						validateParentContainer(dao, sessionDataBean,
								container, parentContainer);
						final ContainerPosition cntPos = container
								.getLocatedAtPosition();
						cntPos.setParentContainer(parentContainer);
						container.setSite(parentContainer.getSite());
						posOneCapacity = parentContainer.getCapacity()
								.getOneDimensionCapacity().intValue();
						posTwoCapacity = parentContainer.getCapacity()
								.getTwoDimensionCapacity().intValue();
						fullStatus = StorageContainerUtil
								.getStorageContainerFullStatus(dao,
										parentContainer, children);
						posDimOne = cntPos.getPositionDimensionOne().intValue();
						posDimTwo = cntPos.getPositionDimensionTwo().intValue();
						if (Validator.isEmpty(cntPos
								.getPositionDimensionOneString()))
						{
							posDimOneString = StorageContainerUtil
									.convertSpecimenPositionsToString(
											parentContainer.getName(), 1,
											cntPos.getPositionDimensionOne());
						}
						else
						{
							posDimOneString = cntPos
									.getPositionDimensionOneString();
						}
						if (Validator.isEmpty(cntPos
								.getPositionDimensionTwoString()))
						{
							posDimTwoString = StorageContainerUtil
									.convertSpecimenPositionsToString(
											parentContainer.getName(), 1,
											cntPos.getPositionDimensionTwo());
						}
						else
						{
							posDimTwoString = cntPos
									.getPositionDimensionTwoString();
						}

						container.setLocatedAtPosition(cntPos);
					}
				}
				else
				{
					throw this.getBizLogicException(null,
							"errors.storageContainerExist", "");
				}
			}
			else
			{
				sBiz.loadSite(dao, container);
			}
			stBiz.loadStorageType(dao, container);
			for (int i = 0; i < noOfContainers; i++)
			{
				final StorageContainer cont = setContainerPos(container,
						posDimOne, posDimTwo, posDimOneString, posDimTwoString);
				logger.debug("Collection protocol size:"
						+ container.getCollectionProtocolCollection().size());
				setLabelAndBarcode(dao, container, cont);
				setLabellingSchemes(cont);
				setCanHoldStorageType(container);
				dao.insert(cont);
				container.setId(cont.getId());
				container.setCapacity(cont.getCapacity());
				if (container.getLocatedAtPosition() != null
						&& container.getLocatedAtPosition()
								.getParentContainer() != null)
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
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
		}
		catch (final ApplicationException e)
		{
			logger.error(e.getMessage(), e);
			throw this.getBizLogicException(e, e.getErrorKeyName(),
					e.getMsgValues());
		}
	}

	private void setLabellingSchemes(StorageContainer cont)
	{
		if (cont.getOneDimensionLabellingScheme() == null)
		{
			cont.setOneDimensionLabellingScheme(Constants.LABELLING_SCHEME_NUMBERS);
		}
		if (cont.getTwoDimensionLabellingScheme() == null)
		{
			cont.setTwoDimensionLabellingScheme(Constants.LABELLING_SCHEME_NUMBERS);
		}

	}

	/**
	 * @param dao DAO Object
	 * @param sessionDataBean SessionDataBean object
	 * @param container Container Object
	 * @param parentContainer parent Container Object
	 * @throws BizLogicException BizLogicException Exception
	 */
	private void validateParentContainer(final DAO dao,
			final SessionDataBean sessionDataBean,
			final StorageContainer container,
			final StorageContainer parentContainer) throws BizLogicException
	{
		if (!StorageContainerUtil.validatePosition(parentContainer, container))
		{
			throw this.getBizLogicException(null,
					"errors.storageContainer.dimensionOverflow", "");
		}
		final String contId = container.getLocatedAtPosition()
				.getParentContainer().getId().toString();
		final String pos1 = container.getLocatedAtPosition()
				.getPositionDimensionOne().toString();
		final String pos2 = container.getLocatedAtPosition()
				.getPositionDimensionTwo().toString();
		this.checkContainer(dao, StorageContainerUtil.setparameterList(contId,
				pos1, pos2, false), sessionDataBean, null);
		final boolean parentContValid = StorageContainerUtil
				.isParentContainerValidToUSe(container, parentContainer);
		if (!parentContValid)
		{
			throw this.getBizLogicException(null, "parent.container.not.valid",
					"");
		}
	}

	/**
	 * @param dao DAO Object
	 * @param container  New Container Object
	 * @param cont New Container Object
	 * @throws BizLogicException BizLogicException Exception
	 * @throws DAOException DAOException Exception
	 */
	private void setLabelAndBarcode(final DAO dao,
			final StorageContainer container, final StorageContainer cont)
			throws BizLogicException, DAOException
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
	private void setLabel(final StorageContainer container,
			final StorageContainer cont) throws BizLogicException
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
			final int posDimOne, final int posDimTwo, String posDimOneString,
			String posDimTwoString)
	{
		final StorageContainer cont = new StorageContainer(container);
		if (cont.getLocatedAtPosition() != null
				&& cont.getLocatedAtPosition().getParentContainer() != null)
		{
			final ContainerPosition cntPos = cont.getLocatedAtPosition();
			cntPos.setPositionDimensionOne(Integer.valueOf(posDimOne));
			cntPos.setPositionDimensionOneString(posDimOneString);
			cntPos.setPositionDimensionTwo(Integer.valueOf(posDimTwo));
			cntPos.setPositionDimensionTwoString(posDimTwoString);
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
	protected String[] getDynamicGroups(final AbstractDomainObject obj)
			throws SMException
	{
		String[] dynamicGroups = null;
		final StorageContainer storageContainer = (StorageContainer) obj;

		if (storageContainer.getLocatedAtPosition() != null
				&& storageContainer.getLocatedAtPosition().getParentContainer() != null)
		{
			dynamicGroups = SecurityManagerFactory.getSecurityManager()
					.getProtectionGroupByName(
							storageContainer.getLocatedAtPosition()
									.getParentContainer());
		}
		else
		{
			dynamicGroups = SecurityManagerFactory.getSecurityManager()
					.getProtectionGroupByName(storageContainer.getSite());
		}
		return dynamicGroups;
	}

	/**
	 * @param obj AbstractDomainObject
	 * @param dao DAO object
	 * @param sessionDataBean SessionDataBean
	 */
	public void postInsert(final Object obj, final DAO dao,
			final SessionDataBean sessionDataBean) throws BizLogicException
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
			final Object object = dao.retrieveById(
					StorageContainer.class.getName(), oldContainer.getId());
			StorageContainer oldContForChange = (StorageContainer) object;
			setSiteTocontainer((HibernateDAO) dao, container);
			validateContainer(dao, sessionDataBean, container, oldContainer);
			final Collection<SpecimenPosition> specimenPosColl = this
					.getSpecimenPositionCollForContainer(dao, container.getId());
			container.setSpecimenPositionCollection(specimenPosColl);
			this.setValuesinPersistentObject(oldContForChange, container, dao);
			dao.update(oldContForChange, oldContainer);
			dao.update(oldContForChange.getCapacity(),
					oldContainer.getCapacity());
			if (container.getActivityStatus().equals(
					Status.ACTIVITY_STATUS_DISABLED.toString()))
			{
				final Long[] containerIDArr = {container.getId()};
				if (StorageContainerUtil.isContainerAvailableForDisabled(dao,
						containerIDArr))
				{
					final List<Map<String, Object>> disabledConts = new ArrayList<Map<String, Object>>();
					final List<StorageContainer> disabledContList = new ArrayList<StorageContainer>();
					disabledContList.add(oldContForChange);
					this.addEntriesInDisabledMap(oldContForChange,
							disabledConts);
					this.setDisableToSubContainer(oldContForChange,
							disabledConts, dao, disabledContList);
					oldContForChange.getOccupiedPositions().clear();
					this.disableSubStorageContainer(dao, disabledContList);
					updateConPOs(dao, oldContainer, oldContForChange);
				}
				else
				{
					throw this.getBizLogicException(null,
							"errors.container.contains.specimen", "");
				}
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
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
	private void validateContainer(final DAO dao,
			final SessionDataBean sessionDataBean,
			final StorageContainer container,
			final StorageContainer oldContainer) throws DAOException,
			BizLogicException
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
	private void updateConPOs(final DAO dao,
			final StorageContainer oldContainer, final StorageContainer oldCont)
			throws DAOException
	{
		final ContainerPosition prevPosition = oldCont.getLocatedAtPosition();
		oldCont.setLocatedAtPosition(null);
		dao.update(oldCont, oldContainer);
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
	private void checkContainer(final DAO dao,
			final StorageContainer container,
			final StorageContainer oldContainer) throws DAOException,
			BizLogicException
	{
		if (container.getLocatedAtPosition() != null)
		{
			final StorageContainer parentStrgCont = (StorageContainer) dao
					.retrieveById(StorageContainer.class.getName(), container
							.getLocatedAtPosition().getParentContainer()
							.getId());
			container.getLocatedAtPosition().setParentContainer(parentStrgCont);
		}
		logger.debug("container.isParentChanged() : "
				+ container.isParentChanged());
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
		final SiteBizLogic site = new SiteBizLogic();
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
			final StorageContainer oldContainer) throws DAOException,
			BizLogicException
	{
		final Integer oldContDimOne = oldContainer.getCapacity()
				.getOneDimensionCapacity();
		final Integer oldContDimTwo = oldContainer.getCapacity()
				.getTwoDimensionCapacity();
		final Integer newContDimOne = container.getCapacity()
				.getOneDimensionCapacity();
		final Integer newContDimTwo = container.getCapacity()
				.getTwoDimensionCapacity();
		if (oldContDimOne.intValue() > newContDimOne.intValue()
				|| oldContDimTwo.intValue() > newContDimTwo.intValue())
		{
			final boolean canReduceDim = StorageContainerUtil
					.canReduceDimension(dao, oldContainer.getId(),
							newContDimOne, newContDimTwo);
			if (!canReduceDim)
			{
				throw this.getBizLogicException(null,
						"errors.storageContainer.cannotReduce", "");
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
	private void checkContainerPos(final DAO dao,
			final SessionDataBean sessionDataBean,
			final StorageContainer container,
			final StorageContainer oldContainer) throws BizLogicException
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
		if (container.getLocatedAtPosition().getParentContainer().getId()
				.longValue() == oldContainer.getLocatedAtPosition()
				.getParentContainer().getId().longValue()
				&& container.getLocatedAtPosition().getPositionDimensionOne()
						.longValue() == oldContainer.getLocatedAtPosition()
						.getPositionDimensionOne().longValue()
				&& container.getLocatedAtPosition().getPositionDimensionTwo()
						.longValue() == oldContainer.getLocatedAtPosition()
						.getPositionDimensionTwo().longValue())
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
			final StorageContainer container, final boolean flag)
			throws BizLogicException
	{
		if (flag
				&& container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null)
		{
			final String contId = container.getLocatedAtPosition()
					.getParentContainer().getId().toString();
			final String pos1 = container.getLocatedAtPosition()
					.getPositionDimensionOne().toString();
			final String pos2 = container.getLocatedAtPosition()
					.getPositionDimensionTwo().toString();
			this.checkContainer(dao, StorageContainerUtil.setparameterList(
					contId, pos1, pos2, false), sessionDataBean, null);
		}
	}

	/**
	 * @param dao DAO object
	 * @param container StorageContainer Object
	 * @param oldContainer Old StorageContainer Object
	 * @throws BizLogicException BizLogicException
	 * @throws DAOException DAOException
	 */
	private void checkPosChanged(final DAO dao,
			final StorageContainer container,
			final StorageContainer oldContainer) throws DAOException,
			BizLogicException
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
			final StorageContainer oldContainer) throws DAOException,
			BizLogicException
	{
		checkDimentionOverflow(dao, container);
		if (oldContainer.getLocatedAtPosition() != null
				&& oldContainer.getLocatedAtPosition()
						.getPositionDimensionOne() != null
				&& oldContainer.getLocatedAtPosition()
						.getPositionDimensionOne().intValue() != container
						.getLocatedAtPosition().getPositionDimensionOne()
						.intValue()
				|| oldContainer.getLocatedAtPosition()
						.getPositionDimensionTwo().intValue() != container
						.getLocatedAtPosition().getPositionDimensionTwo()
						.intValue())
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
	private void checkDimentionOverflow(final DAO dao,
			final StorageContainer container) throws DAOException,
			BizLogicException
	{
		final String sourceObjectName = StorageContainer.class.getName();
		final String[] selectColumnName = {"id",
				"capacity.oneDimensionCapacity",
				"capacity.twoDimensionCapacity"};
		final QueryWhereClause queryWhereClause = new QueryWhereClause(
				sourceObjectName);
		queryWhereClause.addCondition(new EqualClause("id", container
				.getLocatedAtPosition().getParentContainer().getId()));
		final List list = dao.retrieve(sourceObjectName, selectColumnName,
				queryWhereClause);
		if (!list.isEmpty())
		{
			final Object[] obj1 = (Object[]) list.get(0);
			final Integer pcCapacityOne = (Integer) obj1[1];
			final Integer pcCapacityTwo = (Integer) obj1[2];
			if (!StorageContainerUtil.validatePosition(
					pcCapacityOne.intValue(), pcCapacityTwo.intValue(),
					container))
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
			if (StorageContainerUtil.isUnderSubContainer(container, container
					.getLocatedAtPosition().getParentContainer().getId(), dao))
			{
				throw this.getBizLogicException(null,
						"errors.container.under.subcontainer", "");
			}
			logger.debug("Loading ParentContainer: "
					+ container.getLocatedAtPosition().getParentContainer()
							.getId());
			if (!StorageContainerUtil.validatePosition(dao, container))
			{
				throw this.getBizLogicException(null,
						"errors.storageContainer.dimensionOverflow", "");
			}
			caUseCont(dao, container);
			this.checkStatus(dao, container.getLocatedAtPosition()
					.getParentContainer(), "Parent Container");
			final Site site = new SiteBizLogic().getSite(dao, container
					.getLocatedAtPosition().getParentContainer().getId());
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
		final boolean canUse = StorageContainerUtil
				.isContainerAvailableForPositions(dao, container);
		logger.debug("canUse : " + canUse);
		if (!canUse)
		{
			throw this.getBizLogicException(null,
					"errors.storageContainer.inUse", "");
		}
	}

	/**
	 * @param persistentobject - StorageContainer persistent object.
	 * @param newObject - StorageContainer newObject
	 * @param dao - DAO object
	 * @throws BizLogicException throws BizLogicException
	 */
	private void setValuesinPersistentObject(StorageContainer persistentobject,
			final StorageContainer newObject, final DAO dao)
			throws BizLogicException
	{
		try
		{
			persistentobject.setActivityStatus(newObject.getActivityStatus());
			persistentobject.setBarcode(newObject.getBarcode());
			final Capacity persistCapacity = persistentobject.getCapacity();
			final Capacity newCapacity = newObject.getCapacity();
			persistCapacity.setOneDimensionCapacity(newCapacity
					.getOneDimensionCapacity());
			persistCapacity.setTwoDimensionCapacity(newCapacity
					.getTwoDimensionCapacity());
			final Collection children = this
					.getChildren(dao, newObject.getId());
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
			persistentobject.setHoldsSpecimenTypeCollection(newObject
					.getHoldsSpecimenTypeCollection());
			persistentobject.setName(newObject.getName());
			persistentobject.setNoOfContainers(newObject.getNoOfContainers());
			persistentobject.setParentChanged(newObject.isParentChanged());
			persistentobject.setPositionChanged(newObject.isPositionChanged());
			setContPos(persistentobject, newObject);
			persistentobject.setSimilarContainerMap(newObject
					.getSimilarContainerMap());
			persistentobject.setSite(newObject.getSite());
			persistentobject.setStartNo(newObject.getStartNo());
			persistentobject.setStorageType(newObject.getStorageType());
			persistentobject.setTempratureInCentigrade(newObject
					.getTempratureInCentigrade());
			if (!persistentobject.getOneDimensionLabellingScheme().equals(
					newObject.getOneDimensionLabellingScheme()))
			{
				updateSpecimenPositionValues(persistentobject,
						newObject.getOneDimensionLabellingScheme(), 1);
				updateContainerPositionValues(persistentobject,
						newObject.getOneDimensionLabellingScheme(), 1);
			}
			if (!persistentobject.getTwoDimensionLabellingScheme().equals(
					newObject.getTwoDimensionLabellingScheme()))
			{
				updateSpecimenPositionValues(persistentobject,
						newObject.getTwoDimensionLabellingScheme(), 2);
				updateContainerPositionValues(persistentobject,
						newObject.getTwoDimensionLabellingScheme(), 2);
			}
			persistentobject.setOneDimensionLabellingScheme(newObject
					.getOneDimensionLabellingScheme());
			persistentobject.setTwoDimensionLabellingScheme(newObject
					.getTwoDimensionLabellingScheme());
		}
		catch (final ApplicationException exp)
		{
			logger.error(exp.getMessage(), exp);
			throw this.getBizLogicException(exp, exp.getErrorKeyName(),
					exp.getMsgValues());
		}
	}

	private void updateSpecimenPositionValues(
			StorageContainer persistentobject, String newLabellingScheme,
			Integer positionDimension) throws BizLogicException
	{
		String oldLabellingScheme = null;
		if (positionDimension.equals(1))
		{
			oldLabellingScheme = persistentobject
					.getOneDimensionLabellingScheme();
		}
		else if (positionDimension.equals(2))
		{
			oldLabellingScheme = persistentobject
					.getTwoDimensionLabellingScheme();
		}
		Collection<SpecimenPosition> specimenPositionCollection = persistentobject
				.getSpecimenPositionCollection();
		if (specimenPositionCollection != null
				&& !specimenPositionCollection.isEmpty())
		{
			Iterator<SpecimenPosition> iter = specimenPositionCollection
					.iterator();
			Class parameterType = String.class;
			Boolean toUpperCase = false, toLowerCase = false;
			String fromFunction = null, toFunction = null;

			fromFunction = getFromFunctionName(oldLabellingScheme, fromFunction);

			toFunction = getToFunctionName(newLabellingScheme, toFunction);
			//get complete method Name
			String methodName = fromFunction + "To" + toFunction;
			if (newLabellingScheme.contains("Upper"))
			{
				toUpperCase = true;
			}
			if (newLabellingScheme.contains("Lower"))
			{
				toLowerCase = true;
			}
			if (Constants.LABELLING_SCHEME_NUMBERS.equals(oldLabellingScheme))
			{
				parameterType = Integer.TYPE;
			}

			Method method = null;
			try
			{
				if (!fromFunction.toLowerCase()
						.equals(toFunction.toLowerCase()))
				{
					method = AppUtility.class.getDeclaredMethod(methodName,
							parameterType);
					method.setAccessible(true); //if security settings allow this
					while (iter.hasNext())
					{
						AbstractPosition pos = (AbstractPosition) iter.next();
						updatePosition(positionDimension, pos, toUpperCase,
								fromFunction, method);
					}
				}
				else
				{
					while (iter.hasNext())
					{
						AbstractPosition pos = (AbstractPosition) iter.next();
						updatePositionCase(positionDimension, pos, toUpperCase,
								toLowerCase);
					}
				}
			}
			catch (IllegalArgumentException exp)
			{
				logger.error(exp.getMessage(), exp);
				throw this.getBizLogicException(exp, null, exp.getMessage());
			}
			catch (IllegalAccessException exp)
			{
				logger.error(exp.getMessage(), exp);
				throw this.getBizLogicException(exp, null, exp.getMessage());
			}
			catch (InvocationTargetException exp)
			{
				logger.error(exp.getMessage(), exp);
				throw this.getBizLogicException(exp, null, exp.getMessage());
			} //use null if the method is static
			catch (SecurityException exp)
			{
				logger.error(exp.getMessage(), exp);
				throw this.getBizLogicException(exp, null, exp.getMessage());
			}
			catch (NoSuchMethodException exp)
			{
				logger.error(exp.getMessage(), exp);
				throw this.getBizLogicException(exp, null, exp.getMessage());
			}
		}
	}

	private void updateContainerPositionValues(
			StorageContainer persistentobject, String newLabellingScheme,
			Integer positionDimension) throws BizLogicException
	{
		String oldLabellingScheme = null;
		if (positionDimension.equals(1))
		{
			oldLabellingScheme = persistentobject
					.getOneDimensionLabellingScheme();
		}
		else if (positionDimension.equals(2))
		{
			oldLabellingScheme = persistentobject
					.getTwoDimensionLabellingScheme();
		}
		Collection<ContainerPosition> containerPositionCollection = persistentobject
				.getOccupiedPositions();
		if (containerPositionCollection != null
				&& !containerPositionCollection.isEmpty())
		{
			Iterator<ContainerPosition> iter = containerPositionCollection
					.iterator();
			Class parameterType = String.class;
			Boolean toUpperCase = false, toLowerCase = false;
			String fromFunction = null, toFunction = null;

			fromFunction = getFromFunctionName(oldLabellingScheme, fromFunction);

			toFunction = getToFunctionName(newLabellingScheme, toFunction);
			//get complete method Name
			String methodName = fromFunction + "To" + toFunction;
			if (newLabellingScheme.contains("Upper"))
			{
				toUpperCase = true;
			}
			else if (newLabellingScheme.contains("Lower"))
			{
				toLowerCase = true;
			}
			if (Constants.LABELLING_SCHEME_NUMBERS.equals(oldLabellingScheme))
			{
				parameterType = Integer.TYPE;
			}

			Method method = null;
			try
			{
				if (!fromFunction.toLowerCase()
						.equals(toFunction.toLowerCase()))
				{
					method = AppUtility.class.getDeclaredMethod(methodName,
							parameterType);
					method.setAccessible(true); //if security settings allow this
					while (iter.hasNext())
					{
						AbstractPosition pos = (AbstractPosition) iter.next();
						updatePosition(positionDimension, pos, toUpperCase,
								fromFunction, method);
					}
				}
				else
				{
					while (iter.hasNext())
					{
						AbstractPosition pos = (AbstractPosition) iter.next();
						updatePositionCase(positionDimension, pos, toUpperCase,
								toLowerCase);
					}
				}
			}
			catch (IllegalArgumentException exp)
			{
				logger.error(exp.getMessage(), exp);
				throw this.getBizLogicException(exp, null, exp.getMessage());
			}
			catch (IllegalAccessException exp)
			{
				logger.error(exp.getMessage(), exp);
				throw this.getBizLogicException(exp, null, exp.getMessage());
			}
			catch (InvocationTargetException exp)
			{
				logger.error(exp.getMessage(), exp);
				throw this.getBizLogicException(exp, null, exp.getMessage());
			} //use null if the method is static
			catch (SecurityException exp)
			{
				logger.error(exp.getMessage(), exp);
				throw this.getBizLogicException(exp, null, exp.getMessage());
			}
			catch (NoSuchMethodException exp)
			{
				logger.error(exp.getMessage(), exp);
				throw this.getBizLogicException(exp, null, exp.getMessage());
			}
		}
	}

	private void updatePositionCase(Integer positionDimension,
			AbstractPosition pos, Boolean toUpperCase, Boolean toLowerCase)
	{
		String dimensionValue = null;
		if (positionDimension.equals(1))
		{
			dimensionValue = pos.getPositionDimensionOneString();
			if (toUpperCase)
			{
				dimensionValue = dimensionValue.toUpperCase();
			}
			else if (toLowerCase)
			{
				dimensionValue = dimensionValue.toLowerCase();
			}
			pos.setPositionDimensionOneString(dimensionValue);
		}
		if (positionDimension.equals(2))
		{
			dimensionValue = pos.getPositionDimensionTwoString();
			if (toUpperCase)
			{
				dimensionValue = dimensionValue.toUpperCase();
			}
			else if (toLowerCase)
			{
				dimensionValue = dimensionValue.toLowerCase();
			}
			pos.setPositionDimensionTwoString(dimensionValue);
		}
	}

	private void updatePosition(Integer positionDimension,
			AbstractPosition pos, Boolean toUpperCase, String fromFunction,
			Method method) throws IllegalAccessException,
			InvocationTargetException
	{
		String dimensionValue = null;
		if (positionDimension.equals(1))
		{
			dimensionValue = pos.getPositionDimensionOneString();
			String computedValue = (String) method.invoke(
					null,
					fromFunction.equals("integer") ? Integer
							.valueOf(dimensionValue) : dimensionValue)
					.toString();
			if (toUpperCase)
			{
				computedValue = computedValue.toUpperCase();
			}
			pos.setPositionDimensionOneString(computedValue);
		}
		if (positionDimension.equals(2))
		{
			dimensionValue = pos.getPositionDimensionTwoString();
			String computedValue = (String) method.invoke(
					null,
					fromFunction.equals("integer") ? Integer
							.valueOf(dimensionValue) : dimensionValue)
					.toString();
			if (toUpperCase)
			{
				computedValue = computedValue.toUpperCase();
			}
			pos.setPositionDimensionTwoString(computedValue);
		}
	}

	private String getToFunctionName(String newLabellingScheme,
			String toFunction)
	{
		//get New Labelling scheme
		if (Constants.LABELLING_SCHEME_ALPHABETS_LOWER_CASE
				.equals(newLabellingScheme))
		{
			toFunction = "String";
		}
		else if (Constants.LABELLING_SCHEME_ALPHABETS_UPPER_CASE
				.equals(newLabellingScheme))
		{
			toFunction = "String";
		}
		else if (Constants.LABELLING_SCHEME_ROMAN_LOWER_CASE
				.equals(newLabellingScheme))
		{
			toFunction = "Roman";
		}
		else if (Constants.LABELLING_SCHEME_ROMAN_UPPER_CASE
				.equals(newLabellingScheme))
		{
			toFunction = "Roman";
		}
		else if (Constants.LABELLING_SCHEME_NUMBERS.equals(newLabellingScheme))
		{
			toFunction = "Integer";
		}
		return toFunction;
	}

	private String getFromFunctionName(String oldLabellingScheme,
			String fromFunction)
	{
		if (Constants.LABELLING_SCHEME_ALPHABETS_LOWER_CASE
				.equals(oldLabellingScheme))
		{
			fromFunction = "string";
		}
		else if (Constants.LABELLING_SCHEME_ALPHABETS_UPPER_CASE
				.equals(oldLabellingScheme))
		{
			fromFunction = "string";
		}
		else if (Constants.LABELLING_SCHEME_ROMAN_LOWER_CASE
				.equals(oldLabellingScheme))
		{
			fromFunction = "roman";
		}
		else if (Constants.LABELLING_SCHEME_ROMAN_UPPER_CASE
				.equals(oldLabellingScheme))
		{
			fromFunction = "roman";
		}
		else if (Constants.LABELLING_SCHEME_NUMBERS.equals(oldLabellingScheme))
		{
			fromFunction = "integer";
		}
		return fromFunction;
	}

	/**
	 * Set container position.
	 * @param persistentobject StorageContainer object
	 * @param newObject StorageContainer object
	 */
	private void setContPos(StorageContainer persistentobject,
			StorageContainer newObject)
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
			cntPos.setPositionDimensionOneString(newObject
					.getLocatedAtPosition().getPositionDimensionOneString());
			cntPos.setPositionDimensionTwo(newObject.getLocatedAtPosition()
					.getPositionDimensionTwo());
			cntPos.setPositionDimensionTwoString(newObject
					.getLocatedAtPosition().getPositionDimensionTwoString());
			cntPos.setParentContainer(newObject.getLocatedAtPosition()
					.getParentContainer());
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
	public Collection getChildren(DAO dao, Long containerId)
			throws ApplicationException
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
			throw this.getBizLogicException(e, e.getErrorKeyName(),
					e.getMsgValues());
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
			SessionDataBean sessionDataBean, Specimen specimen)
			throws BizLogicException
	{
		final String scID = (String) parameterList.get(0);
		final String posOne = (String) parameterList.get(1);
		final String posTwo = (String) parameterList.get(2);
		try
		{
			final List list = reteriveSCObject(dao, scID);
			if (!list.isEmpty())
			{
				final StorageContainer strCont = populateSCObject(list);
				final boolean hasAccess = StorageContainerUtil
						.validateContainerAccess(dao, strCont, sessionDataBean);
				logger.debug("hasAccess..............." + hasAccess);
				if (!hasAccess)
				{
					throw this.getBizLogicException(null,
							"access.use.object.denied", "");
				}
				this.checkStatus(dao, strCont, "Storage Container");
				new SiteBizLogic().checkClosedSite(dao, strCont.getId(),
						"Container Site");
				final boolean isValidPosition = StorageContainerUtil
						.validatePosition(strCont, posOne, posTwo);
				logger.debug("isValidPosition : " + isValidPosition);
				if (isValidPosition)
				{
					canUsePosition(dao, specimen, parameterList, strCont);
				}
				else
				{
					throw this.getBizLogicException(null,
							"errors.storageContainer.dimensionOverflow", "");
				}
			}
			else
			{
				throw this.getBizLogicException(null,
						"errors.storageContainerExist", "");
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
		}
	}

	/**
	 * @param dao - DAO object.
	 * @param container - StorageContainer object
	 * @param sessionDataBean - SessionDataBean object
	 * @return boolean value
	 * @throws BizLogicException throws BizLogicException
	 */
	public boolean validateContainerAccess(DAO dao, StorageContainer container,
			SessionDataBean sessionDataBean) throws BizLogicException
	{
		boolean flag = true;
		logger.debug("validateContainerAccess..................");
		if (sessionDataBean != null && sessionDataBean.isAdmin())
		{
			return flag;
		}
		final Long userId = sessionDataBean.getUserId();
		Site site = null;
		Set<Long> loggedInUserSiteIdSet = null;
		site = new SiteBizLogic().getSite(dao, container.getId());
		loggedInUserSiteIdSet = new UserBizLogic().getRelatedSiteIds(userId);
		if (loggedInUserSiteIdSet != null
				&& loggedInUserSiteIdSet.contains(Long.valueOf(site.getId())))
		{
			return flag;
		}
		else
		{
			throw this.getBizLogicException(null, "access.use.object.denied",
					"");
		}
	}

	/**
	 * @param dao DAO object
	 * @param specimen Specimen Object
	 * @param parameterList Parameter List includes SC identifier, pos1, pos2
	 * @param stcont StorageContainer object
	 * @throws BizLogicException BizLogicException
	 */
	private void canUsePosition(DAO dao, Specimen specimen,
			final List<Object> parameterList, final StorageContainer stcont)
			throws BizLogicException
	{
		final String posOne = (String) parameterList.get(1);
		final String posTwo = (String) parameterList.get(2);
		final boolean multipleSpecimen = (Boolean) parameterList.get(3);

		final boolean canUsePosition = StorageContainerUtil
				.isPositionAvailable(dao, stcont, posOne, posTwo, specimen);
		logger.debug("canUsePosition : " + canUsePosition);
		if (!canUsePosition)
		{
			if (multipleSpecimen)
			{

				throw this.getBizLogicException(null,
						"errors.storageContainer.inUse", "");
			}
			else
			{
				throw this.getBizLogicException(null,
						"errors.storageContainer.inUse", "");
			}
		}
	}

	/**
	 * @param dao DAO object
	 * @param strgContID StorageContainer Id
	 * @return List of Storage Container object
	 * @throws DAOException DAOException
	 */
	private List reteriveSCObject(DAO dao, String strgContID)
			throws DAOException
	{
		final String sourceObjectName = StorageContainer.class.getName();
		final String[] selectColumnName = {Constants.SYSTEM_IDENTIFIER,
				"capacity.oneDimensionCapacity",
				"capacity.twoDimensionCapacity", "name"};
		final QueryWhereClause queryWhereClause = new QueryWhereClause(
				sourceObjectName);
		queryWhereClause.addCondition(new EqualClause(
				Constants.SYSTEM_IDENTIFIER, Long.valueOf(strgContID)));
		return dao.retrieve(sourceObjectName, selectColumnName,
				queryWhereClause);
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
	protected boolean validate(Object obj, DAO dao, String operation)
			throws BizLogicException
	{
		try
		{
			if (obj == null)
			{
				throw this.getBizLogicException(null,
						"domain.object.null.err.msg", "");
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
			validateContainerName(container);
			//validateLabellingSchemes(dao,container);
			StorageContainerUtil.populateSpecimenType(container);
			return true;
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
		}

	}

	/*private void validateLabellingSchemes(DAO dao, StorageContainer container) throws BizLogicException
	{
		if(container.getId()!=null)
		{
			boolean isContainerEmpty=StorageContainerUtil.isContainerEmpty(dao, container);
			if(!isContainerEmpty)
			{
				List labellingList=StorageContainerUtil.getLabellingSchemeByContainerId(container.getId().toString());
				String oneDimensionLabellingScheme=(String) labellingList.get(0);// ((ArrayList)labellingList.get(0)).get(0);
				String twoDimensionLabellingScheme=(String) labellingList.get(1);//((ArrayList)labellingList.get(0)).get(1);

				if(!oneDimensionLabellingScheme.equals(container.getOneDimensionLabellingScheme()) 
						|| !twoDimensionLabellingScheme.equals(container.getTwoDimensionLabellingScheme()))
				{
					throw this.getBizLogicException(null, "storageContainer.labellingScheme.errMsg", container.getName());
				}
			}
		}
	}*/

	private void validateContainerName(StorageContainer container)
			throws BizLogicException
	{
		if (container.getName() != null
				&& (container.getName().contains("'") || container.getName()
						.contains("\"")))
		{
			throw this.getBizLogicException(null, "error.quotes.stcont.name",
					"");
		}

	}

	/**
	 * @param operation Operation
	 * @param container Storage Container
	 * @throws BizLogicException BizLogicException
	 */
	private void validateStatus(String operation,
			final StorageContainer container) throws BizLogicException
	{
		if (operation.equals(Constants.ADD))
		{
			if (!Status.ACTIVITY_STATUS_ACTIVE.toString().equals(
					container.getActivityStatus()))
			{
				throw this.getBizLogicException(null,
						"activityStatus.active.errMsg", "");
			}
			if (container.getFull().booleanValue())
			{
				throw this.getBizLogicException(null,
						"storageContainer.isContainerFull.errMsg", "");
			}
		}
		else
		{
			if (!Validator.isEnumeratedValue(Constants.ACTIVITY_STATUS_VALUES,
					container.getActivityStatus()))
			{
				throw this.getBizLogicException(null, "activityStatus.errMsg",
						"");
			}
		}
	}

	/**
	 * @param container Storage Container object
	 * @param validator Validator Object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateStTypeAndTemp(final StorageContainer container,
			final Validator validator) throws BizLogicException
	{
		String message;
		if (container.getStorageType() == null)
		{
			message = ApplicationProperties.getValue("storageContainer.type");
			throw this.getBizLogicException(null, "errors.item.required",
					message);
		}
		else
		{
			if (container.getStorageType().getId() == null
					&& container.getStorageType().getName() != null)
			{
				StorageTypeBizLogic storageTypeBizLogic = new StorageTypeBizLogic();
				Long storageTypeId = storageTypeBizLogic
						.getContaierTypeId(container.getStorageType().getName());
				if (storageTypeId == null)
				{
					throw this.getBizLogicException(null, "errors.valid.data",
							ApplicationProperties
									.getValue("storageContainer.type"));
				}
				StorageType storageType = new StorageType();
				storageType.setId(storageTypeId);
				container.setStorageType(storageType);
			}
		}
		if (container.getTempratureInCentigrade() != null
				&& !Validator.isEmpty(container.getTempratureInCentigrade()
						.toString())
				&& (!validator.isDouble(container.getTempratureInCentigrade()
						.toString(), false)))
		{
			message = ApplicationProperties
					.getValue("storageContainer.temperature");
			throw this
					.getBizLogicException(null, "errors.item.format", message);
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
			validateContPos(dao, container);
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
	private void validateDimOne(final StorageContainer container,
			final Validator validator) throws BizLogicException
	{
		String message;
		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getPositionDimensionOne() != null
				&& Validator.isEmpty(String.valueOf(container
						.getLocatedAtPosition().getPositionDimensionOne())))
		{
			message = ApplicationProperties
					.getValue("storageContainer.oneDimension");
			throw this.getBizLogicException(null, "errors.item.required",
					message);
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
	private void validateIsNumeric(final StorageContainer container,
			final Validator validator) throws BizLogicException
	{
		String message;
		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getPositionDimensionOne() != null
				&& !validator.isNumeric(String.valueOf(container
						.getLocatedAtPosition().getPositionDimensionOne())))
		{
			message = ApplicationProperties
					.getValue("storageContainer.oneDimension");
			throw this
					.getBizLogicException(null, "errors.item.format", message);
		}
	}

	/**
	 * @param container Storage Container object
	 * @param validator Validator Object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateDimTwo(final StorageContainer container,
			final Validator validator) throws BizLogicException
	{
		String message;
		if (container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getPositionDimensionTwo() != null
				&& !Validator.isEmpty(String.valueOf(container
						.getLocatedAtPosition().getPositionDimensionTwo()))
				&& (!validator.isNumeric(String.valueOf(container
						.getLocatedAtPosition().getPositionDimensionTwo()))))
		{
			message = ApplicationProperties
					.getValue("storageContainer.twoDimension");
			throw this
					.getBizLogicException(null, "errors.item.format", message);

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
		try
		{
			final Integer xPos = container.getLocatedAtPosition()
					.getPositionDimensionOne();
			final Integer yPos = container.getLocatedAtPosition()
					.getPositionDimensionTwo();
			/**
			 * Following code is added to set the x and y dimension in case only
			 * storage container is given and x and y positions are not given
			 */
			if (xPos == null || yPos == null)
			{
				final Container cont = container.getLocatedAtPosition()
						.getParentContainer();
				final Position position = StorageContainerUtil
						.getFirstAvailablePositionInContainer(cont, dao);
				if (position != null)
				{
					final ContainerPosition cntPos = container
							.getLocatedAtPosition();
					cntPos.setPositionDimensionOne(position.getXPos());
					cntPos.setPositionDimensionTwo(position.getYPos());
					cntPos.setPositionDimensionOneString(StorageContainerUtil
							.convertSpecimenPositionsToString(cont.getName(),
									1, position.getXPos()));
					cntPos.setPositionDimensionTwoString(StorageContainerUtil
							.convertSpecimenPositionsToString(cont.getName(),
									2, position.getYPos()));
					cntPos.setOccupiedContainer(container);
				}
				else
				{
					throw this.getBizLogicException(null,
							"storage.specified.full", "");
				}
			}
		}
		catch (ApplicationException exception)
		{
			final ErrorKey errorkey = ErrorKey
					.getErrorKey("invalid.container.name");
			throw new BizLogicException(errorkey, exception,
					exception.getMsgValues());
		}
	}

	/**
	 * @param dao DAo object
	 * @param container Storage Container object
	 * @throws DAOException DAOException
	 * @throws BizLogicException BizLogicException
	 */
	private void validateContPos(DAO dao, final StorageContainer container)
			throws DAOException, BizLogicException
	{
		HibernateDAO hibernateDAO = (HibernateDAO) dao;
		if (container.getLocatedAtPosition().getParentContainer().getName() != null)
		{
			// Create a map of substitution parameters.
			Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
			substParams.put("0", new NamedQueryParam(DBTypes.STRING, container
					.getLocatedAtPosition().getParentContainer().getName()));

			final List list = hibernateDAO.executeNamedQuery(
					"getStorageContainerIdByContainerName", substParams);
			if (!list.isEmpty())
			{
				container.getLocatedAtPosition().getParentContainer()
						.setId((Long) list.get(0));
			}
			else
			{
				final String message1 = ApplicationProperties
						.getValue("specimen.storageContainer");
				throw this.getBizLogicException(null, "errors.invalid",
						message1);
			}
		}
	}

	/**
	 * @param container Storage Container object
	 * @param validator Validator Object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateContainer(final StorageContainer container,
			final Validator validator) throws BizLogicException
	{
		String message;
		if (container.getNoOfContainers() == null)
		{
			container.setNoOfContainers(Integer.valueOf(1));
		}
		if (Validator.isEmpty(container.getNoOfContainers().toString()))
		{
			message = ApplicationProperties
					.getValue("storageContainer.noOfContainers");
			throw this.getBizLogicException(null, "errors.item.required",
					message);
		}
		if (!validator.isNumeric(container.getNoOfContainers().toString(), 1))
		{
			message = ApplicationProperties
					.getValue("storageContainer.noOfContainers");
			throw this
					.getBizLogicException(null, "errors.item.format", message);
		}
		validateContParent(container);
	}

	/**
	 * @param container StorageContainer object
	 * @throws BizLogicException BizLogicException
	 */
	private void validateContParent(final StorageContainer container)
			throws BizLogicException
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
	private void validateContSite(final StorageContainer container)
			throws BizLogicException
	{
		String message;
		if (container.getSite() == null
				|| ((container.getSite().getId() == null || container.getSite()
						.getId() <= 0) && (container.getSite().getName() == null || ""
						.equals(container.getSite().getName()))))
		{
			message = ApplicationProperties.getValue("storageContainer.site");
			throw this.getBizLogicException(null, "errors.item.invalid",
					message);
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
		return this.getList(sourceObjectName, displayNameFields, valueField,
				true);
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
	public long[] getDefaultHoldCollectionProtocolList(
			StorageContainer container) throws BizLogicException
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
	private Collection<SpecimenPosition> getSpecimenPositionCollForContainer(
			DAO dao, Long containerId) throws BizLogicException
	{
		List<SpecimenPosition> specimenPosColl = null;
		try
		{
			if (containerId != null)
			{
				specimenPosColl = dao.retrieve(
						SpecimenPosition.class.getName(),
						"storageContainer.id", containerId);
			}
		}
		catch (final DAOException daoExp)
		{
			logger.error(daoExp.getMessage(), daoExp);
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
		}
		return specimenPosColl;
	}

	/**
	 * Called from DefaultBizLogic to get ObjectId for authorization check.
	 * @see edu.wustl.common.bizlogic.DefaultBizLogic#getObjectId(edu.wustl.common.dao.DAO, java.lang.Object)
	 * @param dao DAO object
	 * @param domainObject Abstract Domain object
	 */
	public String getObjectId(DAO dao, Object domainObject)
			throws BizLogicException
	{
		String objId = null;
		if (domainObject instanceof StorageContainer)
		{
			final StorageContainer storageContainer = (StorageContainer) domainObject;
			Site site = getSite(dao, storageContainer);
			if (site != null)
			{
				final StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(Site.class.getName()).append("_")
						.append(site.getId().toString());
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
				final String[] selectColumnName = {};
				final String sourceObjectName = StorageContainer.class
						.getName();
				final QueryWhereClause queryWhereClause = new QueryWhereClause(
						sourceObjectName);
				String errMsg = Constants.DOUBLE_QUOTES;
				if (storageContainer.getLocatedAtPosition()
						.getParentContainer().getName() != null
						&& storageContainer.getLocatedAtPosition()
								.getParentContainer().getName() != "")
				{
					errMsg = "Storage Container Name";
					queryWhereClause.addCondition(new EqualClause("name",
							storageContainer.getLocatedAtPosition()
									.getParentContainer().getName()));
				}
				else
				{
					errMsg = "Storage Container Identifier";
					queryWhereClause.addCondition(new EqualClause("id",
							storageContainer.getLocatedAtPosition()
									.getParentContainer().getId()));
				}
				final List list = dao.retrieve(sourceObjectName,
						selectColumnName, queryWhereClause);

				if (!list.isEmpty())
				{
					final StorageContainer parentContainer = (StorageContainer) list
							.get(0);
					site = parentContainer.getSite();
				}
				else
				{
					this.logger.debug("Storage Container id :"
							+ storageContainer.getLocatedAtPosition()
									.getParentContainer().getId()
							+ " or Storage Container name : "
							+ storageContainer.getLocatedAtPosition()
									.getParentContainer().getName()
							+ " is invalid");
					throw this.getBizLogicException(null, "errors.item.format",
							errMsg);
				}
			}
			catch (final DAOException e)
			{
				logger.error(e.getMessage(), e);
				throw this.getBizLogicException(e, e.getErrorKeyName(),
						e.getMsgValues());
			}
		}
		else
		{
			if (storageContainer.getSite().getId() == null)
			{
				final SiteBizLogic sBiz = new SiteBizLogic();
				Long siteId = sBiz.retriveSiteIdByName(dao, storageContainer
						.getSite().getName());
				storageContainer.getSite().setId(siteId);
			}
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
	private void disableSubStorageContainer(DAO dao,
			List<StorageContainer> disableContList) throws BizLogicException
	{
		try
		{
			final int count = disableContList.size();
			final List<Long> containerIdList = new ArrayList<Long>();
			for (int i = 0; i < count; i++)
			{
				final StorageContainer container = disableContList.get(i);
				containerIdList.add(container.getId());
			}
			final List listOfSpecimenIDs = this.getRelatedObjects(dao,
					Specimen.class, "specimenPosition.storageContainer",
					edu.wustl.common.util.Utility.toLongArray(containerIdList));
			if (!listOfSpecimenIDs.isEmpty())
			{

				throw this.getBizLogicException(null,
						"errors.container.contains.specimen", "");
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
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
		}
	}

	/**
	 * @param storageContainer - StorageContainer object.
	 * @param disabledConts -List of disabledConts
	 * @param dao - DAO object
	 * @param disableContList - list of disabledContainers
	 * @throws BizLogicException throws BizLogicException
	 */
	private void setDisableToSubContainer(StorageContainer storageContainer,
			List<Map<String, Object>> disabledConts, DAO dao,
			List disableContList) throws BizLogicException
	{

		try
		{
			if (storageContainer != null)
			{
				final Collection childrenColl = new StorageContainerBizLogic()
						.getChildren(dao, storageContainer.getId());
				final Iterator iterator = childrenColl.iterator();
				while (iterator.hasNext())
				{
					final StorageContainer container = (StorageContainer) iterator
							.next();
					container.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED
							.toString());
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
			final ErrorKey errorKey = ErrorKey.getErrorKey(exp
					.getErrorKeyName());
			throw new BizLogicException(errorKey, exp, exp.getMsgValues());
		}

	}

	/**
	 * @param container - StorageContainer object.
	 * @param disabledConts - List of disabledConts
	 */
	private void addEntriesInDisabledMap(StorageContainer container,
			List<Map<String, Object>> disabledConts)
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
		if (container != null
				&& container.getLocatedAtPosition() != null
				&& container.getLocatedAtPosition().getParentContainer() != null)
		{
			containerDetails.put(parentContNameKey, container
					.getLocatedAtPosition().getParentContainer().getName());
			containerDetails.put(parentContIdKey, container
					.getLocatedAtPosition().getParentContainer().getId());
			containerDetails.put(pos1Key, container.getLocatedAtPosition()
					.getPositionDimensionOne());
			containerDetails.put(pos2Key, container.getLocatedAtPosition()
					.getPositionDimensionTwo());
		}
		disabledConts.add(containerDetails);
	}

	public StorageContainer getStorageContainerFromName(DAO dao, String label)
			throws BizLogicException
	{
		StorageContainer storageContainer = null;
		try
		{
			String sourceObjectName = StorageContainer.class.getName();
			String column = "name";
			List<StorageContainer> storageContainerList = dao.retrieve(
					sourceObjectName, column, label);
			if (storageContainerList != null && !storageContainerList.isEmpty())
			{
				storageContainer = storageContainerList.get(0);
			}

		}
		catch (final ApplicationException exp)
		{
			logger.error(exp.getMessage(), exp);
			final ErrorKey errorKey = ErrorKey.getErrorKey(exp
					.getErrorKeyName());
			throw new BizLogicException(errorKey, exp, exp.getMsgValues());
		}

		return storageContainer;
	}

	/**
	 * @param identifier
	 *            Identifier of the container or site node.
	 * @param nodeName
	 *            Name of the site or container
	 * @param parentId
	 *            parent identifier of the selected node
	 * @return conNodeList This List contains all the containers
	 * @throws ApplicationException
	 * @Description This method will retrieve all the containers under the
	 *              selected node
	 */
	public Map<Long, String> getStorageContainers() throws ApplicationException
	{
		JDBCDAO dao = null;
		Map<Long, String> map = new HashMap<Long, String>();
		try
		{
			dao = AppUtility.openJDBCSession();
			List resultList = new ArrayList();
			final String sql = this.createSql();
			resultList = dao.executeQuery(sql);

			final Iterator iterator = resultList.iterator();

			while (iterator.hasNext())
			{
				final List rowList = (List) iterator.next();
				map.put(Long.valueOf((String) rowList.get(0)),
						(String) rowList.get(1));

			}

		}
		catch (final DAOException daoExp)
		{
			this.logger.error(daoExp.getMessage(), daoExp);
			daoExp.printStackTrace();
			throw this.getBizLogicException(daoExp, daoExp.getErrorKeyName(),
					daoExp.getMsgValues());
		}
		finally
		{
			try
			{
				dao.closeSession();
			}
			catch (final DAOException e)
			{
				this.logger.error(e.getMessage(), e);
				e.printStackTrace();
			}
		}
		return map;
	}

	private String createSql()
	{
		final String sql = "SELECT cn.IDENTIFIER, cn.name, cn.activity_status "
				+ "FROM CATISSUE_CONTAINER cn join CATISSUE_STORAGE_CONTAINER sc ON sc.IDENTIFIER=cn.IDENTIFIER "
				+ " where cn.ACTIVITY_STATUS!='Disabled'"
				+ " ORDER BY cn.IDENTIFIER ";

		return sql;
	}

	public void updateStorageContainerPosition(DAO dao, String containerName,
			String parentContainerName, String pos1, String pos2)
			throws ApplicationException
	{
		StorageContainer sc = getStorageContainerFromName(dao, containerName);
		ContainerPosition pos = null;

		StorageContainer parentContainer = getStorageContainerFromName(dao,
				parentContainerName);

		List labellingList = StorageContainerUtil
				.getLabellingSchemeByContainerId(parentContainer.getId()
						.toString(), dao);
		String oneDimensionLabellingScheme = (String) labellingList.get(0);// ((ArrayList)labellingList.get(0)).get(0);
		String twoDimensionLabellingScheme = (String) labellingList.get(1);// ((ArrayList)labellingList.get(0)).get(1);
		Integer pos1Integer = AppUtility.getPositionValueInInteger(
				oneDimensionLabellingScheme, pos1);
		Integer pos2Integer = AppUtility.getPositionValueInInteger(
				twoDimensionLabellingScheme, pos2);
		pos = (ContainerPosition) sc.getLocatedAtPosition();
		pos.setPositionDimensionOne(pos1Integer);
		pos.setPositionDimensionTwo(pos2Integer);
		pos.setPositionDimensionOneString(pos1);
		pos.setPositionDimensionTwoString(pos2);
		dao.update(pos);

	}

	public void updateStorageContainerPosition(String containerName,
			String parentContainerName, HibernateDAO dao)
			throws ApplicationException
	{

		StorageContainer sc = getStorageContainerFromName(dao, containerName);
		//Storage container is null when user trying to move site node from tree.
		//When container is null we are throwing site can not be transfered exception.
		if (sc != null)
		{
			ContainerPosition pos = null;
			StorageContainer parentSc = getStorageContainerFromName(dao,
					parentContainerName);
			//parentSc is null when user trying to move container from one site to another site.
			//and it is not null when ueser trying to move container from one container to another.
			if (parentSc != null)
			{
				Long typeId = sc.getStorageType().getId();
				StorageTypeBizLogic stBiz = new StorageTypeBizLogic();
				boolean canHold = stBiz.canHoldContainerType(typeId.intValue(),
						parentSc);
				//Container type check is added to check restriction.
				if (!canHold)
				{
					throw new BizLogicException(null, null, parentContainerName
							+ Constants.CONTAINER_INVALID_MESSAGE
							+ sc.getStorageType().getName());
				}
				List labellingList = StorageContainerUtil
						.getLabellingSchemeByContainerId(parentSc.getId()
								.toString(), dao);
				String oneDimensionLabellingScheme = (String) labellingList
						.get(0);// ((ArrayList)labellingList.get(0)).get(0);
				String twoDimensionLabellingScheme = (String) labellingList
						.get(1);// ((ArrayList)labellingList.get(0)).get(1);
				Position position = StorageContainerUtil
						.getFirstAvailablePositionsInContainer(parentSc,
								new HashSet<String>(), dao, null, null);
				//Container Located position is null when container is moved from site to another container.
				//In above case we create new ContainerPosition object with new position details.
				//If its not null we update existing ContainerPosition object.
				if (sc.getLocatedAtPosition() != null)
				{
					pos = (ContainerPosition) sc.getLocatedAtPosition();
				}
				else
				{
					pos = new ContainerPosition();
				}
				pos.setParentContainer(parentSc);
				pos.setPositionDimensionOne(position.getXPos());
				pos.setPositionDimensionTwo(position.getYPos());
				pos.setPositionDimensionOneString(AppUtility.getPositionValue(
						oneDimensionLabellingScheme, position.getXPos()));
				pos.setPositionDimensionTwoString(AppUtility.getPositionValue(
						twoDimensionLabellingScheme, position.getYPos()));
				pos.setOccupiedContainer(sc);
				sc.setLocatedAtPosition(pos);
				sc.setSite(parentSc.getSite());

			}
			else
			{
				//This else block is getting executed when container is moved to site.
				SiteBizLogic sitBizLogic = new SiteBizLogic();
				Long id = sitBizLogic.retriveSiteIdByName(dao,
						parentContainerName);
				Site site = new Site();
				site.setId(id);
				sc.setSite(site);
				pos = (ContainerPosition) sc.getLocatedAtPosition();
				if (pos != null)
				{
					pos.setParentContainer(null);
					pos.setOccupiedContainer(null);
					sc.setLocatedAtPosition(null);
				}

			}
			dao.update(sc);

			//updateChildContainerSite is called to update child container under transfered container.
			updateChildContainerSite(sc.getName(), dao, sc.getSite());

		}
		else
		{
			throw new BizLogicException(null, null,
					Constants.SITE_NOT_TRANSFRED_MESSAGE);

		}

	}

	//This function gets called recursively to update site for all child container of transfered container
	public static void updateChildContainerSite(String parentName,
			HibernateDAO dao, Site site) throws ApplicationException
	{
		Collection<String> childrenCollection = getContainerChildren(
				parentName, dao);
		Iterator<String> childContIterator = childrenCollection.iterator();
		Map<String, NamedQueryParam> params;

		while (childContIterator.hasNext())
		{
			String childName = childContIterator.next();
			params = new HashMap<String, NamedQueryParam>();
			params.put("0", new NamedQueryParam(DBTypes.LONG, site.getId()));
			params.put("1", new NamedQueryParam(DBTypes.STRING, childName));
			dao.executeUpdateWithNamedQuery("updateSiteOfChildContainer",
					params);
			updateChildContainerSite(childName, dao, site);
		}

	}

	/**
	 * @param containerId - containerId.
	 * @return collection of container childrens
	 * @throws BizLogicException
	 */
	public static Collection getContainerChildren(String containerName, DAO dao)
			throws ApplicationException
	{
		Collection<String> children = null;
		try
		{
			children = new StorageContainerBizLogic().getChildrenName(dao,
					containerName);
		}
		catch (final ApplicationException e)
		{
			logger.error(e.getMessage(), e);
			ErrorKey errorKey = ErrorKey.getErrorKey(e.getErrorKeyName());
			throw new ApplicationException(errorKey, e, e.getMsgValues());
		}
		return children;
	}

	/**
	* @param children Children Collection
	* @param dao DAO object
	* @param containerId container identifier
	* @throws ApplicationException ApplicationException
	* @return childrenColl
	*/
	public Collection getChildrenName(DAO dao, String containerName)
			throws ApplicationException
	{
		final String hql = "select cntPos.occupiedContainer.name from ContainerPosition cntPos, StorageContainer container where cntPos.occupiedContainer.id=container.id and cntPos.parentContainer.name ="
				+ "'" + containerName + "'";
		List childrenColl = new ArrayList();
		childrenColl = dao.executeQuery(hql);
		return childrenColl;
	}

	private void setSiteTocontainer(HibernateDAO hibernateDao,
			StorageContainer container) throws BizLogicException
	{
		if (container.getSite() != null &&  container.getSite().getId()==null
				&& container.getSite().getName() != null)
		{
			SiteBizLogic siteBizLogic = new SiteBizLogic();
			Long siteId = siteBizLogic.retriveSiteIdByName(hibernateDao,
					container.getSite().getName());
			Site site = new Site();
			site.setId(siteId);
			container.setSite(site);
		}
	}

	private void setCanHoldStorageType(StorageContainer container)
			throws BizLogicException
	{
		Long storageTypeId = null;

		Collection<StorageType> canHoldStorageTypes = container
				.getHoldsStorageTypeCollection();
		for (StorageType storageType : canHoldStorageTypes)
		{
			if (storageType.getId() == null && storageType.getName() != null)
			{
				StorageTypeBizLogic storageTypeBizLogic = new StorageTypeBizLogic();
				storageTypeId = storageTypeBizLogic
						.getContaierTypeId(storageType.getName());
				if (storageTypeId == null)
				{
					throw this.getBizLogicException(null, "errors.valid.data",
							ApplicationProperties
									.getValue("storageType.holdsStorageType"));
				}
				storageType.setId(storageTypeId);
			}
		}
	}
}