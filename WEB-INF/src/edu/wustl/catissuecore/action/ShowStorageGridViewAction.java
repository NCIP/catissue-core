/**
 * <p>
 * Title: ShowStorageGridViewAction Class>
 * <p>
 * Description: ShowStorageGridViewAction shows the grid view of the map
 * according to the storage container selected from the tree view.
 * </p>
 * Copyright: Copyright (c) year Company: Washington University, School of
 * Medicine, St. Louis.
 *
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import krishagni.catissueplus.bizlogic.StorageContainerBizlogic;
import krishagni.catissueplus.bizlogic.StorageContainerGraphBizlogic;
import krishagni.catissueplus.dao.StorageContainerDAO;
import krishagni.catissueplus.dao.StorageContainerGraphDAO;
import krishagni.catissueplus.dto.StorageContainerStoredSpecimenDetailsDTO;
import krishagni.catissueplus.dto.StorageContainerUtilizationDTO;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.bizlogic.CollectionProtocolBizLogic;
import edu.wustl.catissuecore.bizlogic.NewSpecimenBizLogic;
import edu.wustl.catissuecore.bizlogic.SpecimenArrayBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageContainerBizLogic;
import edu.wustl.catissuecore.bizlogic.StorageTypeBizLogic;
import edu.wustl.catissuecore.domain.ContainerPosition;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.StorageContainer;
import edu.wustl.catissuecore.domain.StorageType;
import edu.wustl.catissuecore.storage.StorageContainerGridObject;
import edu.wustl.catissuecore.util.CollectionProtocolUtil;
import edu.wustl.catissuecore.util.SpecimenUtil;
import edu.wustl.catissuecore.util.StorageContainerUtil;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.action.BaseAction;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.bizlogic.IBizLogic;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.factory.AbstractFactoryConfig;
import edu.wustl.common.factory.IFactory;
import edu.wustl.common.util.global.Status;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.DAO;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.query.generator.ColumnValueBean;
import edu.wustl.dao.util.HibernateMetaData;

/**
 * ShowStorageGridViewAction shows the grid view of the map according to the
 * storage container selected from the tree view.
 *
 * @author gautam_shetty
 */
public class ShowStorageGridViewAction extends BaseAction
{

	/**
	 * logger.
	 */

	private transient static final Logger logger = Logger
			.getCommonLogger(ShowStorageGridViewAction.class);

	/**
	 * Overrides the executeSecureAction method of SecureAction class.
	 *
	 * @param mapping
	 *            object of ActionMapping
	 * @param form
	 *            object of ActionForm
	 * @param request
	 *            object of HttpServletRequest
	 * @param response
	 *            object of HttpServletResponse
	 * @throws Exception : generic exception
	 * @return ActionForward : ActionForward
	 */
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String target = request.getParameter(Constants.FORWARD_TO);
		if (target == null)
		{
			target = Constants.SUCCESS;
		}
		String id = request.getParameter(Constants.SYSTEM_IDENTIFIER);
		if (id == null)
		{
			id = (String) request.getAttribute(Constants.SYSTEM_IDENTIFIER);
			if (id == null)
			{
				id = "0";
			}
		}

		if (id.equalsIgnoreCase("0"))
		{
			String scName = request.getParameter("storageContainerName");
			ColumnValueBean cvb = new ColumnValueBean(Constants.NAME, scName);
			DefaultBizLogic bizLogic = new DefaultBizLogic();
			List contList = bizLogic.retrieve(StorageContainer.class.getName(), cvb);
			StorageContainer sc = (StorageContainer) contList.get(0);
			id = sc.getId().toString();
		}
		request.setAttribute("storageContainerIdentifier", id);

		HibernateDAO dao = null;
		
		 request.setAttribute(Constants.SHOW_UTILIZATION_ALERT, false);
		 request.setAttribute("showUtilization", false);
		
		try {
		    Long containerId = Long.valueOf(id);
		    dao = (HibernateDAO) AppUtility.openDAOSession(null);
		    
		 
		     StorageContainerGraphDAO graphDao =  new StorageContainerGraphDAO();
		     StorageContainerStoredSpecimenDetailsDTO storageContainerStoredSpecimenDetailsDTO =  graphDao.getStrotedSpecimenDetailsDTOByContainerId(dao, containerId);
		     Long specimenCount = storageContainerStoredSpecimenDetailsDTO.getSpecimenCount();
		     request.setAttribute(Constants.SPEC_COUNT, specimenCount==null?0:specimenCount);
		     Long storedCapacity = storageContainerStoredSpecimenDetailsDTO.getCapacity();
		     request.setAttribute(Constants.CAPACITY,storedCapacity==null?0:storedCapacity);
		     Long utilizationPercentage = storageContainerStoredSpecimenDetailsDTO.getPercentUtilization()==null?0:storageContainerStoredSpecimenDetailsDTO.getPercentUtilization();
             request.setAttribute(Constants.PERCENTAGE, utilizationPercentage);
		     if (utilizationPercentage >= 90)
		        {
		            request.setAttribute(Constants.SHOW_UTILIZATION_ALERT, true);
		        }
		     request.setAttribute("showUtilization", true);
		   }finally{
		       AppUtility.closeDAOSession(dao);
		   }
              

		String contentOfContainer = null;
		final String pageOf = request.getParameter(Constants.PAGE_OF);
		if (Constants.PAGE_OF_STORAGE_CONTAINER.equals(pageOf))
		{
			target = Constants.PAGE_OF_STORAGE_CONTAINER;
		}
		final String position = request.getParameter(Constants.STORAGE_CONTAINER_POSITION);
		if ((null != position) && ("" != position))
		{
			Long positionOne;
			Long positionTwo;
			try
			{
				// The two positions are separated by :
				final StringTokenizer strToken = new StringTokenizer(position, ":");
				positionOne = Long.valueOf(strToken.nextToken());
				positionTwo = Long.valueOf(strToken.nextToken());
			}
			catch (final Exception ex)
			{
				logger.error(ex.getMessage(), ex);
				ex.printStackTrace();
				positionOne = null;
				positionTwo = null;
			}
			request.setAttribute(Constants.POS_ONE, positionOne);
			request.setAttribute(Constants.POS_TWO, positionTwo);

		}
		final IFactory factory = AbstractFactoryConfig.getInstance().getBizLogicFactory();
		final StorageContainerBizLogic bizLogic = (StorageContainerBizLogic) factory
				.getBizLogic(Constants.STORAGE_CONTAINER_FORM_ID);

		final Object containerObject = bizLogic.retrieve(StorageContainer.class.getName(),
				Long.valueOf(id));
		StorageContainerGridObject storageContainerGridObject = null;
		int[][] fullStatus = null;
		int[][] childContainerIds = null;
		String[][] childContainerType = null;
		String[][] childContainerName = null;
		if (containerObject != null)
		{
			storageContainerGridObject = new StorageContainerGridObject();
			final StorageContainer storageContainer = (StorageContainer) containerObject;

			boolean enablePage = this.setEnablePageAttributeIfRequired(request, storageContainer);

			final Site site = (Site) bizLogic.retrieveAttribute(StorageContainer.class.getName(),
					storageContainer.getId(), "site");// container.getSite();
			request.setAttribute("siteName", site.getName());

			request.setAttribute("hierarchy", getHierarchy(storageContainer));
			final StorageType storageType = (StorageType) bizLogic.retrieveAttribute(
					StorageContainer.class.getName(), storageContainer.getId(), "storageType");// storageContainer
			request.setAttribute("storageTypeName", storageType.getName());
			request.setAttribute("containerName", storageContainer.getName());
			request.getAttribute("containerName");
			String oneDimLabel = storageType.getOneDimensionLabel();
			String twoDimLabel = storageType.getTwoDimensionLabel();
			if (oneDimLabel == null)
			{
				oneDimLabel = " ";
			}
			if (twoDimLabel == null)
			{
				twoDimLabel = " ";
			}
			String oneDimLabellingScheme = storageContainer.getOneDimensionLabellingScheme();
			String twoDimLabellingScheme = storageContainer.getTwoDimensionLabellingScheme();
			request.setAttribute(Constants.STORAGE_CONTAINER_DIM_ONE_LABEL, oneDimLabel);
			request.setAttribute(Constants.STORAGE_CONTAINER_DIM_TWO_LABEL, twoDimLabel);

			request.setAttribute("oneDimLabellingScheme", oneDimLabellingScheme);
			request.setAttribute("twoDimLabellingScheme", twoDimLabellingScheme);

			storageContainerGridObject.setId(storageContainer.getId().longValue());
			storageContainerGridObject.setType(storageType.getName());
			storageContainerGridObject.setName(storageContainer.getName());
			final Integer oneDimensionCapacity = storageContainer.getCapacity()
					.getOneDimensionCapacity();
			final Integer twoDimensionCapacity = storageContainer.getCapacity()
					.getTwoDimensionCapacity();
			String oneDimensionLabellingScheme = storageContainer.getOneDimensionLabellingScheme();
			String twoDimensionLabellingScheme = storageContainer.getTwoDimensionLabellingScheme();
			childContainerIds = new int[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];
			storageContainerGridObject.setOneDimensionCapacity(oneDimensionCapacity);
			storageContainerGridObject.setTwoDimensionCapacity(storageContainer.getCapacity()
					.getTwoDimensionCapacity());
			storageContainerGridObject.setOneDimensionLabellingScheme(storageContainer
					.getOneDimensionLabellingScheme());
			storageContainerGridObject.setTwoDimensionLabellingScheme(storageContainer
					.getTwoDimensionLabellingScheme());

			fullStatus = new int[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];
			childContainerType = new String[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];
			childContainerName = new String[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];
			final Collection children = StorageContainerUtil.getContainerChildren(storageContainer
					.getId());
			if (children != null)
			{
				final Iterator iterator = children.iterator();
				while (iterator.hasNext())
				{
					final Object object = iterator.next();
					final StorageContainer childStorageContainer = (StorageContainer) HibernateMetaData
							.getProxyObjectImpl(object);
					if (childStorageContainer != null
							&& childStorageContainer.getLocatedAtPosition() != null)
					{
						final Integer positionDimensionOne = childStorageContainer
								.getLocatedAtPosition().getPositionDimensionOne();
						final Integer positionDimensionTwo = childStorageContainer
								.getLocatedAtPosition().getPositionDimensionTwo();

						fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 1;
						childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo
								.intValue()] = childStorageContainer.getId().intValue();
						childContainerType[positionDimensionOne.intValue()][positionDimensionTwo
								.intValue()] = Constants.CONTAINER_LABEL_CONTAINER_MAP
								+ childStorageContainer.getName();
						childContainerName[positionDimensionOne.intValue()][positionDimensionTwo
								.intValue()] = childStorageContainer.getName();
					}
				}
			}
			final IBizLogic specimenBizLogic = factory.getBizLogic(Constants.NEW_SPECIMEN_FORM_ID);
			String sourceObjectName = Specimen.class.getName();	
			final String[] selectColumnName = {"id", "specimenPosition.positionDimensionOne",
					"specimenPosition.positionDimensionTwo", "label"};
			final String[] whereColumnName = {"specimenPosition.storageContainer.id"};
			final String[] whereColumnCondition = {"="};
			final Object[] whereColumnValue = {Long.valueOf(id)};
			final String joinCondition = Constants.AND_JOIN_CONDITION;

			List list = null;
			list = specimenBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition);

			String[][] childContainerLable = new String[oneDimensionCapacity.intValue() + 1][twoDimensionCapacity
					.intValue() + 1];
			if (list != null)
			{
				final Iterator iterator = list.iterator();
				while (iterator.hasNext())
				{
					// Specimen specimen = (Specimen)iterator.next();
					final Object[] obj = (Object[]) iterator.next();

					final Long specimenID = (Long) obj[0];
					final Integer positionDimensionOne = (Integer) obj[1];
					final Integer positionDimensionTwo = (Integer) obj[2];
					final String specimenLable = (String) obj[3];

					fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 2;
					childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = specimenID.intValue();
					childContainerType[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = Constants.SPECIMEN_LABEL_CONTAINER_MAP + specimenLable;
					contentOfContainer = Constants.ALIAS_SPECIMEN;
					childContainerLable[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = specimenLable;
				}
			}

			// Showing Specimen Arrays in the Container map.
			sourceObjectName = SpecimenArray.class.getName();

			selectColumnName[1] = "locatedAtPosition.positionDimensionOne";
			selectColumnName[2] = "locatedAtPosition.positionDimensionTwo";
			selectColumnName[3] = "name";
			whereColumnName[0] = "locatedAtPosition.parentContainer.id";
			list = specimenBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition);

			if (list != null)
			{
				final Iterator iterator = list.iterator();
				while (iterator.hasNext())
				{
					final Object[] obj = (Object[]) iterator.next();

					final Long specimenID = (Long) obj[0];
					final Integer positionDimensionOne = (Integer) obj[1];
					final Integer positionDimensionTwo = (Integer) obj[2];
					final String specimenArrayLable = obj[3].toString();

					fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 2;
					childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = specimenID.intValue();
					childContainerType[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = Constants.SPECIMEN_ARRAY_LABEL_CONTAINER_MAP
							+ specimenArrayLable;
					contentOfContainer = Constants.ALIAS_SPECIMEN_ARRAY;

				}
			}

			StringBuffer jsonMidleString = new StringBuffer();
			String headerString = " ,";
			for (int i = 1; i < oneDimensionCapacity + 1; i++)
			{
				jsonMidleString.append("{id:" + i + ",data:[");
				for (int j = 0; j < twoDimensionCapacity + 1; j++)
				{
					if (i == 0 && j == 0)
					{
						jsonMidleString.append("\"\"");
					}
					else if (i == 0)
					{

						jsonMidleString.append("\"" + twoDimLabel + "_" + j + "\"");
					}
					else if (j == 0)
					{
						jsonMidleString.append("\""
								+ AppUtility.getPositionValue(oneDimensionLabellingScheme, i)
								+ "\"");

					}
					else
					{
						String value = "";
						if (fullStatus[i][j] != 0)
						{
							String openStorageContainer = null;
							if (pageOf.equals(Constants.PAGE_OF_STORAGE_CONTAINER))
							{
								openStorageContainer = Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION
										+ "?"
										+ Constants.SYSTEM_IDENTIFIER
										+ "="
										+ childContainerIds[i][j]
										+ "&"
										+ Constants.PAGE_OF
										+ "="
										+ pageOf;
							}
							else
							{
								String storageContainerType1 = "";
								if (pageOf.equals(Constants.PAGE_OF_STORAGE_LOCATION))
								{
									storageContainerType1 = request
											.getParameter(Constants.STORAGE_CONTAINER_TYPE);
								}
								openStorageContainer = Constants.SHOW_STORAGE_CONTAINER_GRID_VIEW_ACTION
										+ "?"
										+ Constants.SYSTEM_IDENTIFIER
										+ "="
										+ childContainerIds[i][j]
										+ "&"
										+ Constants.STORAGE_CONTAINER_TYPE
										+ "="
										+ storageContainerType1
										+ "&"
										+ Constants.PAGE_OF
										+ "="
										+ pageOf;
							}
							if (fullStatus[i][j] == 1)
							{
								String containerName = childContainerType[i][j];
								int containerNameSize = childContainerType[i][j].length();
								if (containerNameSize >= 20)
									containerName = containerName.substring(11, containerNameSize);
								else
									containerName = containerName.substring(11, containerNameSize);
								value = "<a href=\\\\\""
										+ openStorageContainer
										+ "\\\\\" onclick=\\\\\"containerChanged()\\\\\" class=\\\\\"view\\\\\" onmouseover=\\\\\"Tip(\\\' "
										+ childContainerType[i][j] + "\\\')\\\\\">" + containerName
										+ "</a>";
								//value = "<a href=\\\""+openStorageContainer+"\\\" onclick=\\\"containerChanged()\\\" class=\\\"view\\\" onmouseover=\\\"Tip(\\\' "+childContainerType[i][j]+"\\\')\"><img src=\\\"images/uIEnhancementImages/used_container.gif\\\" alt=\\\"Unused\\\" width=\\\"32\\\" height=\\\"32\\\" border=\\\"0\\\" ><br>"+containerName+"</a>";
								// value = containerName;
							}
							else
							{

								String containerName = childContainerType[i][j];
								int containerNameSize = childContainerType[i][j].length();
								if (contentOfContainer != null
										&& contentOfContainer.equals(Constants.ALIAS_SPECIMEN))
								{
									if (containerNameSize >= 20)
										containerName = containerName.substring(11,
												containerNameSize);
									else
										containerName = containerName.substring(11,
												containerNameSize);
								}
								if (contentOfContainer != null
										&& contentOfContainer
												.equals(Constants.ALIAS_SPECIMEN_ARRAY))
								{
									if (containerNameSize >= 18)
										containerName = containerName.substring(8, 18) + "...";
									else
										containerName = containerName.substring(8,
												containerNameSize);
								}

								if (contentOfContainer != null
										&& contentOfContainer.equals(Constants.ALIAS_SPECIMEN))
								{

									value = "<a  class=\\\\\"view\\\\\" href=\\\\\"javascript:forwardToPage(\\\'QuerySpecimenSearch.do?"
											+ Constants.PAGE_OF
											+ "=pageOfNewSpecimenCPQuery&"
											+ Constants.SYSTEM_IDENTIFIER
											+ "="
											+ childContainerIds[i][j]
											+ "\\\')\\\\\" onmouseover=\\\\\"Tip(\\\'"
											+ childContainerType[i][j]
											+ "\\\')\\\\\" >"
											+ containerName + "	</a>";
									//value = "<a  class=\\\\\"view\\\\\" href=\\\\\"QuerySpecimenSearch.do?"+Constants.PAGE_OF+"=pageOfNewSpecimenCPQuery&"+Constants.SYSTEM_IDENTIFIER+"="+childContainerIds[i][j]+"\\\\\" onmouseover=\\\\\"Tip(\\\'"+childContainerType[i][j]+"\\\')\\\\\" ><img src=\\\\\"images/uIEnhancementImages/specimen.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\"  border=\\\\\"0\\\\\"><br>"+containerName+"	</a>";
									//value = containerName;
								}
								if (contentOfContainer != null
										&& contentOfContainer
												.equals(Constants.ALIAS_SPECIMEN_ARRAY))
								{
									value = "<a class=\\\\\"view\\\\\" href=\\\\\"javascript:forwardToPage(\\\'QuerySpecimenArraySearch.do?"
											+ Constants.PAGE_OF
											+ "=pageOfSpecimenArray&"
											+ Constants.SYSTEM_IDENTIFIER
											+ "="
											+ childContainerIds[i][j]
											+ "\\\')\\\\\" onmouseover=\\\\\"Tip(\\\'"
											+ childContainerType[i][j]
											+ "\\\')\\\\\" >"
											+ containerName + " </a>";
									//value = "<a class=\\\\\"view\\\\\" href=\\\\\"QuerySpecimenArraySearch.do?"+Constants.PAGE_OF+"=pageOfSpecimenArray&"+Constants.SYSTEM_IDENTIFIER+"="+childContainerIds[i][j]+" \\\\\" onmouseover=\\\\\"Tip(\\\'"+childContainerType[i][j]+"\\\')\\\\\" ><img src=\\\\\"images/uIEnhancementImages/specimen_array.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\"  border=\\\\\"0\\\\\"><br>"+containerName+" </a>";
									//value = containerName;
								}
							}
						}
						else
						{
							HttpSession session = request.getSession();
							String containerStyle = (String) session
									.getAttribute(Constants.CONTAINER_STYLE);
							String xDimStyleId = (String) session
									.getAttribute(Constants.XDIM_STYLEID);
							String yDimStyleId = (String) session
									.getAttribute(Constants.YDIM_STYLEID);
							String specimenMapKey = (String) session
									.getAttribute(Constants.SPECIMEN_ATTRIBUTE_KEY);
							String specimenCallBackFunction = (String) session
									.getAttribute(Constants.SPECIMEN_CALL_BACK_FUNCTION);
							String selectedContainerName = (String) session
									.getAttribute(Constants.SELECTED_CONTAINER_NAME);
							String pos1 = (String) session.getAttribute(Constants.POS1);
							String pos2 = (String) session.getAttribute(Constants.POS2);
							String storageContainerIdFromMap = (String) session
									.getAttribute("StorageContainerIdFromMap");
							String hyperLinkTag = "<a href=\\\\\"#\\\\\">";
							String onClickEvent = "";
							String addContainer = (String) session.getAttribute("storageContainer");
							if (Constants.PAGE_OF_MULTIPLE_SPECIMEN.equals(pageOf))
							{
								if (enablePage)
								{
									value = hyperLinkTag;
									onClickEvent = "onclick=\\\\\""
											+ specimenCallBackFunction
											+ "(\\\'"
											+ specimenMapKey
											+ "\\\',\\\'"
											+ storageContainerGridObject.getId()
											+ "\\\',\\\'"
											+ storageContainerGridObject.getName()
											+ "\\\' "
											+ ",\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getOneDimensionLabellingScheme(), i)
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getTwoDimensionLabellingScheme(), j)
											+ "\\\');\\ " + "closeFramedWindow()\\\\\"";
								}
								value = value
										+ "<img  "
										+ onClickEvent
										+ "src=\\\\\"images/uIEnhancementImages/empty_container.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\" align=\\\\\"middle\\\\\" border=\\\\\"0\\\\\" onmouseover=\\\\\"Tip(\\\'Unused\\\')\\\\\"></td></td>";
							}
							else if (Constants.PAGE_OF_SPECIMEN_SUMMARY_PAGE.equals(pageOf))
							{								if (enablePage)
							{
								value = hyperLinkTag;
								String methodName = "setTextBoxValueForContainer";
								if (addContainer != null && "true".equals(addContainer))
								{
									methodName = "setTextBoxValueForContainerPage";
								}
								onClickEvent = "onclick=\\\\\""
										+ methodName
										+ "(\\\'"
										+ selectedContainerName
										+ "\\\',\\\'"
										+ storageContainerGridObject.getName()
										+ "\\\');\\ "
										+ "setTextBoxValue(\\\'"
										+ pos1
										+ "\\\',\\\'"
										+ AppUtility.getPositionValue(
												storageContainerGridObject
														.getOneDimensionLabellingScheme(), i)
										+ "\\\');\\ "
										+ "setTextBoxValue(\\\'"
										+ pos2
										+ "\\\',\\\'"
										+ AppUtility.getPositionValue(
												storageContainerGridObject
														.getTwoDimensionLabellingScheme(), j)
										+ "\\\');\\";
								

							}
							value = value + "<img " + onClickEvent;
							/*if(storageContainerIdFromMap!=null)
							{
								value=value +"setTextBoxValue(\\\'"+storageContainerIdFromMap+"\\\',\\\'"+id+"\\\');\\ ";
							}*/
							value = value
									+ "closeFramedWindow()\\\\\" "
									+ "src=\\\\\"images/uIEnhancementImages/empty_container.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\" align=\\\\\"middle\\\\\" border=\\\\\"0\\\\\" onmouseover=\\\\\"Tip(\\\'Unused\\\')\\\\\"></td></td>";
							
							}
							else if (Constants.PAGE_OF_SPECIMEN.equals(pageOf))
							{
								if (enablePage)
								{
									value = hyperLinkTag;
									String methodName = "setTextBoxValueForContainer";
									if (addContainer != null && "true".equals(addContainer))
									{
										methodName = "setTextBoxValueForContainerPage";
									}
									onClickEvent = "onclick=\\\\\""
											+ methodName
											+ "(\\\'"
											+ selectedContainerName
											+ "\\\',\\\'"
											+ storageContainerGridObject.getName()
											+ "\\\');\\ "
											+ "setTextBoxValue(\\\'"
											+ pos1
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getOneDimensionLabellingScheme(), i)
											+ "\\\');\\ "
											+ "setTextBoxValue(\\\'"
											+ pos2
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getTwoDimensionLabellingScheme(), j)
											+ "\\\');\\ "
									+"addTransferEvent(\\\'"+pageOf+"\\\',\\\'"+storageContainerGridObject.getId()+"\\\',\\\'"+storageContainerGridObject.getName()+"\\\',\\\'"+AppUtility.getPositionValue(storageContainerGridObject.getOneDimensionLabellingScheme(),i)+"\\\',\\\'"+AppUtility.getPositionValue(storageContainerGridObject.getTwoDimensionLabellingScheme(),j)+"\\\');\\";

								}
								value = value + "<img " + onClickEvent;
								/*if(storageContainerIdFromMap!=null)
								{
									value=value +"setTextBoxValue(\\\'"+storageContainerIdFromMap+"\\\',\\\'"+id+"\\\');\\ ";
								}*/
								value = value
										+ "closeFramedWindow()\\\\\" "
										+ "src=\\\\\"images/uIEnhancementImages/empty_container.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\" align=\\\\\"middle\\\\\" border=\\\\\"0\\\\\" onmouseover=\\\\\"Tip(\\\'Unused\\\')\\\\\"></td></td>";
							}
							else if (Constants.PAGE_OF_EDIT_SPECIMEN.equals(pageOf))
							{
								if(enablePage)
								{
									value=hyperLinkTag;
									String methodName="setTextBoxValueForContainer";
									if(addContainer!=null && "true".equals(addContainer))
									{
										methodName="setTextBoxValueForContainerPage";
									}
									onClickEvent="onclick=\\\\\""+methodName+"(\\\'"+selectedContainerName+"\\\',\\\'"+storageContainerGridObject.getName()+"\\\');\\ " 
											+"setTextBoxValue(\\\'"+pos1+"\\\',\\\'"+AppUtility.getPositionValue(storageContainerGridObject.getOneDimensionLabellingScheme(),i)+"\\\');\\ "
											+"setTextBoxValue(\\\'"+pos2+"\\\',\\\'"+AppUtility.getPositionValue(storageContainerGridObject.getTwoDimensionLabellingScheme(),j)+"\\\');\\ "
											+"addTransferEvent(\\\'"+pageOf+"\\\',\\\'"+storageContainerGridObject.getId()+"\\\',\\\'"+storageContainerGridObject.getName()+"\\\',\\\'"+AppUtility.getPositionValue(storageContainerGridObject.getOneDimensionLabellingScheme(),i)+"\\\',\\\'"+AppUtility.getPositionValue(storageContainerGridObject.getTwoDimensionLabellingScheme(),j)+"\\\');\\";
									
								}
								value = value+"<img " +onClickEvent;
								/*if(storageContainerIdFromMap!=null)
								{
									value=value +"setTextBoxValue(\\\'"+storageContainerIdFromMap+"\\\',\\\'"+id+"\\\');\\ ";
								}*/
								value=value+"closeFramedWindow()\\\\\" "
									  +"src=\\\\\"images/uIEnhancementImages/empty_container.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\" border=\\\\\"0\\\\\" onmouseover=\\\\\"Tip(\\\'Unused\\\')\\\\\"></td></td>";
							}
							else if (Constants.PAGE_OF_ALIQUOT.equals(pageOf))
							{
								if (enablePage)
								{
									value = hyperLinkTag;
									onClickEvent = "onclick=\\\\\"setTextBoxValueForContainer(\\\'"
											+ containerStyle
											+ "\\\',\\\'"
											+ java.net.URLEncoder.encode(
													storageContainerGridObject.getName(), "UTF-8")
											+ "\\\');\\ "
											+ "setTextBoxValue(\\\'"
											+ xDimStyleId
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getOneDimensionLabellingScheme(), i)
											+ "\\\');\\ "
											+ "setTextBoxValue(\\\'"
											+ yDimStyleId
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getTwoDimensionLabellingScheme(), j)
											+ "\\\');\\ " + "closeFramedWindow()\\\\\"";
								}
								value = value
										+ "<img  "
										+ onClickEvent
										+ "src=\\\\\"images/uIEnhancementImages/empty_container.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\" align=\\\\\"middle\\\\\" border=\\\\\"0\\\\\" onmouseover=\\\\\"Tip(\\\'Unused\\\')\\\\\"></td></td>";

							}
							else if (Constants.PAGE_OF_NEW_ALIQUOT.equals(pageOf))
							{
								if (enablePage)
								{
									value = hyperLinkTag;
									onClickEvent = "onclick=\\\\\"setTextBoxValueForNewAliquot(\\\'"
											+ containerStyle
											+ "\\\',\\\'"
											+ storageContainerGridObject.getName()
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getOneDimensionLabellingScheme(), i)
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getTwoDimensionLabellingScheme(), j)
											+ "\\\');\\ " + "closeFramedWindow()\\\\\"";
								}
								value = value
										+ "<img  "
										+ onClickEvent
										+ "src=\\\\\"images/uIEnhancementImages/empty_container.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\" border=\\\\\"0\\\\\" onmouseover=\\\\\"Tip(\\\'Unused\\\')\\\\\"></td></td>";

							}
							else if (Constants.PAGE_OF_NEW_ALIQUOT.equals(pageOf))
							{
								if (enablePage)
								{
									value = hyperLinkTag;
									onClickEvent = "onclick=\\\\\"setTextBoxValueForNewAliquot(\\\'"
											+ containerStyle
											+ "\\\',\\\'"
											+ java.net.URLEncoder.encode(
													storageContainerGridObject.getName(), "UTF-8")
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getOneDimensionLabellingScheme(), i)
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getTwoDimensionLabellingScheme(), j)
											+ "\\\');\\ " + "closeFramedWindow()\\\\\"";
								}
								value = value
										+ "<img  "
										+ onClickEvent
										+ "src=\\\\\"images/uIEnhancementImages/empty_container.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\" border=\\\\\"0\\\\\" onmouseover=\\\\\"Tip(\\\'Unused\\\')\\\\\"></td></td>";

							}
							else if (Constants.PAGE_OF_NEW_ALIQUOT.equals(pageOf))
							{
								if (enablePage)
								{
									value = hyperLinkTag;
									onClickEvent = "onclick=\\\\\"setTextBoxValueForNewAliquot(\\\'"
											+ containerStyle
											+ "\\\',\\\'"
											+ java.net.URLEncoder.encode(
													storageContainerGridObject.getName(), "UTF-8")
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getOneDimensionLabellingScheme(), i)
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getTwoDimensionLabellingScheme(), j)
											+ "\\\');\\ " + "closeFramedWindow()\\\\\"";
								}
								value = value
										+ "<img  "
										+ onClickEvent
										+ "src=\\\\\"images/uIEnhancementImages/empty_container.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\" border=\\\\\"0\\\\\" onmouseover=\\\\\"Tip(\\\'Unused\\\')\\\\\"></td></td>";

							}
							else if (Constants.PAGE_OF_NEW_ALIQUOT.equals(pageOf))
							{
								if (enablePage)
								{
									value = hyperLinkTag;
									onClickEvent = "onclick=\\\\\"setTextBoxValueForNewAliquot(\\\'"
											+ containerStyle
											+ "\\\',\\\'"
											+ java.net.URLEncoder.encode(
													storageContainerGridObject.getName(), "UTF-8")
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getOneDimensionLabellingScheme(), i)
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getTwoDimensionLabellingScheme(), j)
											+ "\\\');\\ " + "closeFramedWindow()\\\\\"";
								}
								value = value
										+ "<img  "
										+ onClickEvent
										+ "src=\\\\\"images/uIEnhancementImages/empty_container.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\" border=\\\\\"0\\\\\" onmouseover=\\\\\"Tip(\\\'Unused\\\')\\\\\"></td></td>";

							}
							else
							{
								if (enablePage)
								{
									value = hyperLinkTag;
									onClickEvent = "onclick=\\\\\"setTextBoxValue(\\\'"
											+ containerStyle
											+ "\\\',\\\'"
											+ storageContainerGridObject.getId()
											+ "\\\');\\ "
											+ "setTextBoxValue(\\\'"
											+ xDimStyleId
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getOneDimensionLabellingScheme(), i)
											+ "\\\');\\ "
											+ "setTextBoxValue(\\\'"
											+ yDimStyleId
											+ "\\\',\\\'"
											+ AppUtility.getPositionValue(
													storageContainerGridObject
															.getTwoDimensionLabellingScheme(), j)
											+ "\\\');\\ " + "closeFramedWindow()\\\\\" ";
								}
								value = "<img "
										+ onClickEvent
										+ "src=\\\\\"images/uIEnhancementImages/empty_container.gif\\\\\" alt=\\\\\"Unused\\\\\" width=\\\\\"32\\\\\" height=\\\\\"32\\\\\" align=\\\\\"middle\\\\\" border=\\\\\"0\\\\\" onmouseover=\\\\\"Tip(\\\'Unused\\\')\\\\\"></td></td>";
							}

						}

						jsonMidleString.append("\"" + value + "\"");

					}
					if (j < twoDimensionCapacity)
					{
						jsonMidleString.append(",");

					}
				}
				jsonMidleString.append("]}");
				if (i < oneDimensionCapacity)
				{
					jsonMidleString.append(",");
				}

			}

			for (int i = 0; i < twoDimensionCapacity; i++)
			{
				headerString += (AppUtility.getPositionValue(twoDimensionLabellingScheme, i + 1));;
				if (i < (twoDimensionCapacity - 1))
				{
					headerString += ",";
				}

			}

			StringBuffer jsonStringBuffer = new StringBuffer();

			String jsonStart = "{rows:[";
			String jsonEnd = "]}";
			jsonStringBuffer.append(jsonStart);
			jsonStringBuffer.append(jsonMidleString.toString());
			jsonStringBuffer.append(jsonEnd);
			request.setAttribute("gridJson", jsonStringBuffer);
			request.setAttribute("gridHeader", headerString);

			// Showing Specimen Arrays in the Container map.
			sourceObjectName = SpecimenArray.class.getName();

			selectColumnName[1] = "locatedAtPosition.positionDimensionOne";
			selectColumnName[2] = "locatedAtPosition.positionDimensionTwo";
			selectColumnName[3] = "name";
			whereColumnName[0] = "locatedAtPosition.parentContainer.id";
			list = specimenBizLogic.retrieve(sourceObjectName, selectColumnName, whereColumnName,
					whereColumnCondition, whereColumnValue, joinCondition);

			if (list != null)
			{
				final Iterator iterator = list.iterator();
				while (iterator.hasNext())
				{
					final Object[] obj = (Object[]) iterator.next();

					final Long specimenID = (Long) obj[0];
					final Integer positionDimensionOne = (Integer) obj[1];
					final Integer positionDimensionTwo = (Integer) obj[2];
					final String specimenArrayLable = obj[3].toString();

					fullStatus[positionDimensionOne.intValue()][positionDimensionTwo.intValue()] = 2;
					childContainerIds[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = specimenID.intValue();
					childContainerType[positionDimensionOne.intValue()][positionDimensionTwo
							.intValue()] = Constants.SPECIMEN_ARRAY_LABEL_CONTAINER_MAP
							+ specimenArrayLable;
					contentOfContainer = Constants.ALIAS_SPECIMEN_ARRAY;

				}
			}
		}

		request.setAttribute(Constants.CONTENT_OF_CONTAINNER, contentOfContainer);
		if (Constants.PAGE_OF_STORAGE_LOCATION.equals(pageOf))
		{
			final String storageContainerType = request
					.getParameter(Constants.STORAGE_CONTAINER_TYPE);
			this.logger.info("Id-----------------" + id);
			this.logger.info("storageContainerType:" + storageContainerType);
			final int startNumber = StorageContainerUtil.getNextContainerNumber(Long.parseLong(id),
					Long.parseLong(storageContainerType), false);
			request.setAttribute(Constants.STORAGE_CONTAINER_TYPE, storageContainerType);
			request.setAttribute(Constants.START_NUMBER, Integer.valueOf(startNumber));
		}

		request.setAttribute(Constants.PAGE_OF, pageOf);
		request.setAttribute(Constants.CHILD_CONTAINER_SYSTEM_IDENTIFIERS, childContainerIds);
		request.setAttribute(Constants.CHILD_CONTAINER_TYPE, childContainerType);
		request.setAttribute(Constants.CHILD_CONTAINER_NAME, childContainerName);
		request.setAttribute(Constants.STORAGE_CONTAINER_CHILDREN_STATUS, fullStatus);
		request.setAttribute(Constants.STORAGE_CONTAINER_GRID_OBJECT, storageContainerGridObject);

		// Mandar : 29aug06 : to set collectionprotocol titles

		final List collectionProtocolList = CollectionProtocolBizLogic
				.getCollectionProtocolList(id);
		request.setAttribute(Constants.MAP_COLLECTION_PROTOCOL_LIST, collectionProtocolList);

		// Mandar : 29aug06 : to set specimenclass
		List<String> spClassList = new ArrayList<String>();
		final List<String> specimenTypeClassList = AppUtility.getClassAndTypeList(id);
		final List<String> specimenTypeList = new ArrayList<String>();
		Iterator<String> itr = specimenTypeClassList.iterator();
		while (itr.hasNext())
		{
			String className = (String) itr.next();
			String type = "None";
			if (!"None".equals(className))
			{
				type = (String) itr.next();
			}
			if (!spClassList.contains(className))
			{
				spClassList.add(className);
			}
			specimenTypeList.add(type);
		}
		request.setAttribute(Constants.MAP_SPECIMEN_CLASS_LIST, spClassList);
		request.setAttribute(Constants.MAP_SPECIMEN_TYPE_LIST, specimenTypeList);
		return mapping.findForward(target);
	}

	private String getHierarchy(StorageContainer storageContainer)
	{
		StorageContainer st1 = storageContainer;
		List<String> strList = new ArrayList<String>();
		ContainerPosition parentPosition;
		while (st1.getLocatedAtPosition() != null)
		{
			parentPosition = st1.getLocatedAtPosition();
			st1 = (StorageContainer) st1.getLocatedAtPosition().getParentContainer();
			strList.add(st1.getName() + " (" + parentPosition.getPositionDimensionOne() + ","
					+ parentPosition.getPositionDimensionTwo() + ")");
		}
		strList.add(st1.getSite().getName() + "(site)");
		String hierarchy = "";
		for (int j = strList.size() - 1; j >= 0; j--)
		{
			hierarchy += strList.get(j);
			if (j != 0)
			{
				hierarchy += ",";
			}
		}
		hierarchy += "";
		return hierarchy;
	}

	/**
	 * To enable or disable the Storage container links on the page depending on
	 * restriction criteria on Container.
	 *
	 * @param request
	 *            The HttpServletRequest object reference.
	 * @param storageContainer
	 *            The Storage container object reference.
	 * @throws ApplicationException 
	 */
	private boolean setEnablePageAttributeIfRequired(HttpServletRequest request,
			StorageContainer storageContainer) throws ApplicationException
	{
		boolean enablePage = true;
		String activityStatus = request.getParameter(Status.ACTIVITY_STATUS.toString());
		if (activityStatus == null)
		{
			activityStatus = (String) request.getAttribute(Status.ACTIVITY_STATUS.toString());
		}

		if (activityStatus != null
				&& activityStatus.equals(Status.ACTIVITY_STATUS_CLOSED.toString()))
		{
			enablePage = false;
			final ActionErrors errors = new ActionErrors();
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("errors.container.closed"));
			this.saveErrors(request, errors);
		}

		final HttpSession session = request.getSession();
		final SessionDataBean sessionData = (SessionDataBean) request.getSession().getAttribute(
				Constants.SESSION_DATA);
		// checking for container type.
		final String holdContainerType = (String) session
				.getAttribute(Constants.CAN_HOLD_CONTAINER_TYPE);
		final String holdCollectionProtocol = (String) session
				.getAttribute(Constants.CAN_HOLD_COLLECTION_PROTOCOL);
		final String specimenId = (String) session.getAttribute(Constants.SPECIMEN_ID);
		final String parentSpecimenLabel = (String) session
				.getAttribute(Constants.PARENT_SPECIMEN_LABEL_KEY);
		final String parentSpecimenBarcode = (String) session.getAttribute("parentSpecimenBarcode");
		if (enablePage && holdContainerType != null && !holdContainerType.equals(""))
		{
			final int typeId = Integer.parseInt(holdContainerType);
			StorageTypeBizLogic stBiz = new StorageTypeBizLogic();
			enablePage = stBiz.canHoldContainerType(typeId, storageContainer);
		}
		else if (enablePage
				&& (specimenId != null || parentSpecimenLabel != null
						|| holdCollectionProtocol != null || parentSpecimenBarcode != null))
		{
			CollectionProtocolBizLogic cpBiz = new CollectionProtocolBizLogic();
			if (enablePage)
			{
				if (holdCollectionProtocol != null && !holdCollectionProtocol.equals(""))
				{
					final int collectionProtocolId = Integer.parseInt(holdCollectionProtocol);
					enablePage = cpBiz.canHoldCollectionProtocol(collectionProtocolId,
							storageContainer);
				}
				else
				{
					if ((specimenId == null || "".equals(specimenId))
							&& (parentSpecimenLabel == null || "".equals(parentSpecimenLabel))
							&& (parentSpecimenBarcode == null || "".equals(parentSpecimenBarcode)))
					{
						enablePage = false;
					}
					else
					{
						Integer collectionProtocolId = null;
						if (specimenId != null && !"".equals(specimenId))
						{
							collectionProtocolId = Integer.parseInt(CollectionProtocolUtil
									.getCPIdFromSpecimen(specimenId, sessionData));
						}
						else if (parentSpecimenLabel != null && !"".equals(parentSpecimenLabel))
						{
							collectionProtocolId = Integer.parseInt(CollectionProtocolUtil
									.getCPIdFromSpecimenLabel(parentSpecimenLabel, sessionData));
						}
						else if (parentSpecimenBarcode != null && !"".equals(parentSpecimenBarcode))
						{
							collectionProtocolId = Integer
									.parseInt(CollectionProtocolUtil.getCPIdFromSpecimenBarcode(
											parentSpecimenBarcode, sessionData));
						}
						enablePage = cpBiz.canHoldCollectionProtocol(collectionProtocolId,
								storageContainer);
					}
				}
			}

			NewSpecimenBizLogic nspBiz = new NewSpecimenBizLogic();
			final String holdspecimenClass = (String) session
					.getAttribute(Constants.CAN_HOLD_SPECIMEN_CLASS);
			if (enablePage && holdspecimenClass != null)
			{
				if (!holdspecimenClass.equals(""))
				{
					enablePage = nspBiz.canHoldSpecimenClass(holdspecimenClass, storageContainer);
				}
				else
				{
					enablePage = false;
				}
			}

			String holdspType = (String) session.getAttribute(Constants.CAN_HOLD_SPECIMEN_TYPE);
			if (enablePage)
			{
				if (holdspType != null && !holdspType.equals(""))
				{
					enablePage = nspBiz.canHoldSpecimenType(holdspType, storageContainer);
				}
				else
				{
					if (specimenId == null || "".equals(specimenId))
					{
						enablePage = false;
					}
					else
					{
						holdspType = SpecimenUtil.getSpecimenTypeBySpecimenId(
								Long.valueOf(specimenId), sessionData);
						enablePage = nspBiz.canHoldSpecimenType(holdspType, storageContainer);
					}
				}
			}
		}

		SpecimenArrayBizLogic spArraybiz = new SpecimenArrayBizLogic();
		final String holdspecimenArrayType = (String) session
				.getAttribute(Constants.CAN_HOLD_SPECIMEN_ARRAY_TYPE);
		if (enablePage && holdspecimenArrayType != null)
		{
			if (!holdspecimenArrayType.equals(""))
			{
				final int specimenArrayTypeId = Integer.parseInt(holdspecimenArrayType);
				enablePage = spArraybiz.canHoldSpecimenArrayType(specimenArrayTypeId,
						storageContainer);
			}
			else
			{
				enablePage = false;
			}
		}
		return enablePage;
		/*if (enablePage)
		{
			request.setAttribute(Constants.ENABLE_STORAGE_CONTAINER_GRID_PAGE, Constants.TRUE);
		}*/
	}
}