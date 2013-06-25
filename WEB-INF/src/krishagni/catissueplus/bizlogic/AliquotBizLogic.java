
package krishagni.catissueplus.bizlogic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.dto.AliquotContainerDetailsDTO;
import krishagni.catissueplus.dto.AliquotDetailsDTO;
import krishagni.catissueplus.dto.ContainerInputDetailsDTO;
import krishagni.catissueplus.dto.SingleAliquotDetailsDTO;
import edu.wustl.catissuecore.domain.ExternalIdentifier;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCharacteristics;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenPosition;
import edu.wustl.catissuecore.dto.ExternalIdentifierDTO;
import edu.wustl.catissuecore.dto.SpecimenDTO;
import edu.wustl.catissuecore.printserviceclient.LabelPrinter;
import edu.wustl.catissuecore.printserviceclient.LabelPrinterFactory;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.global.Status;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;
import edu.wustl.security.manager.SecurityManagerFactory;

public class AliquotBizLogic
{

	private SpecimenDTO createAliquotSpecimenDTOFromDTO(AliquotDetailsDTO aliquotDetailsDTO,
			Specimen parentSpecimen, SingleAliquotDetailsDTO singleAliquotDetailsDTO,
			SpecimenPosition spePositionObj)
	{
		SpecimenDTO specimenDto = new SpecimenDTO();
		specimenDto.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.toString());
		specimenDto.setAvailable(Boolean.TRUE);
		specimenDto.setAvailableQuantity(singleAliquotDetailsDTO.getQuantity());
		//		if (!Validator.isEmpty(singleAliquotDetailsDTO.getAliqoutLabel()))
		//			specimenDto.setLabel(singleAliquotDetailsDTO.getAliqoutLabel());
		//		if (!Validator.isEmpty(singleAliquotDetailsDTO.getBarCode()))
		//			specimenDto.setBarcode(singleAliquotDetailsDTO.getBarCode());

		specimenDto.setClassName(parentSpecimen.getSpecimenClass());
		specimenDto.setCollectionStatus(Constants.COLLECTION_STATUS_COLLECTED);
		specimenDto.setComments("");
		specimenDto.setConcentration((parentSpecimen).getConcentrationInMicrogramPerMicroliter());
		if (spePositionObj != null)
		{
			specimenDto.setContainerId(spePositionObj.getStorageContainer().getId());
			specimenDto.setPos1(spePositionObj.getPositionDimensionOneString());
			specimenDto.setPos2(spePositionObj.getPositionDimensionTwoString());
			specimenDto.setContainerName(singleAliquotDetailsDTO.getStoragecontainer());
		}
		specimenDto.setCreatedDate(aliquotDetailsDTO.getCreationDate());
		//		specimenDto.setGenerateLabel(generateLabel)
		specimenDto.setLineage(Constants.ALIQUOT);
		//		specimenDto.setOperation(operation);
		specimenDto.setParentSpecimenId(parentSpecimen.getId());
		specimenDto.setParentSpecimenName(parentSpecimen.getLabel());
		specimenDto.setPathologicalStatus(parentSpecimen.getPathologicalStatus());
		specimenDto.setQuantity(singleAliquotDetailsDTO.getQuantity());
		specimenDto.setSpecimenCollectionGroupId(parentSpecimen.getSpecimenCollectionGroup()
				.getId());
		specimenDto.setTissueSide(parentSpecimen.getSpecimenCharacteristics().getTissueSide());
		specimenDto.setTissueSite(parentSpecimen.getSpecimenCharacteristics().getTissueSite());
		specimenDto.setExternalIdentifiers(new ArrayList<ExternalIdentifierDTO>());
		specimenDto.setType(parentSpecimen.getSpecimenType());
		if (!Variables.isTemplateBasedLblGeneratorAvl && !Variables.isSpecimenLabelGeneratorAvl)
		{
			specimenDto.setLabel(singleAliquotDetailsDTO.getAliqoutLabel());
		}
		if (!Variables.isSpecimenBarcodeGeneratorAvl && !Variables.isTemplateBasedLblGeneratorAvl)
		{
			specimenDto.setBarcode(singleAliquotDetailsDTO.getBarCode());
		}
		return specimenDto;
	}

	/**
	 * Validate availableQuantity against quantity per aliquot 
	 * @param aliquotDetailObj
	 * @param availableQuantity
	 * @throws ApplicationException
	 */
	private double getTotalDistributedQuantity(
			Collection<SingleAliquotDetailsDTO> aliquotDetailsColl, double availableQuantity)
			throws ApplicationException
	{
		double totalDistributedQuantity = 0;
		Iterator<SingleAliquotDetailsDTO> aliquotDetailsIterator = aliquotDetailsColl.iterator();
		while (aliquotDetailsIterator.hasNext())
		{
			SingleAliquotDetailsDTO aliquotDetailsDTO = aliquotDetailsIterator.next();
			totalDistributedQuantity = totalDistributedQuantity + aliquotDetailsDTO.getQuantity();
		}
		if (totalDistributedQuantity > availableQuantity)
		{
			throw new ApplicationException(null, null, Constants.INSUFFICIENT_AVAILABLE_QUANTITY);
		}
		return totalDistributedQuantity;
	}

	/**
	 * This api create aliquots
	 * @param aliquotDetailObj
	 * @param sessionDataBean
	 * @throws Exception 
	 */
	public void createAliquotSpecimen(AliquotDetailsDTO aliquotDetailsDTO,
			HibernateDAO hibernateDao, SessionDataBean sessionDataBean) throws ApplicationException
	{
		Collection<AbstractDomainObject> specimenCollection = new LinkedHashSet<AbstractDomainObject>();

		try
		{
			Specimen parentSpecimen = getParentSpecimenDetailsForAliquots(hibernateDao,
					aliquotDetailsDTO.getParentLabel());
			aliquotDetailsDTO.setParentId(parentSpecimen.getId());
			List<SingleAliquotDetailsDTO> aliquotDetailList = aliquotDetailsDTO
					.getPerAliquotDetailsCollection();
			StorageContainerBizlogic storageContainerBizlogic = new StorageContainerBizlogic();
			double totalDistributedQuantity = getTotalDistributedQuantity(aliquotDetailList,
					parentSpecimen.getAvailableQuantity());
			SpecimenBizLogic specimenBizLogic = new SpecimenBizLogic();
			List<SpecimenDTO> specimenDtoList = new ArrayList<SpecimenDTO>();
			for (int cnt = 0; cnt < aliquotDetailList.size(); cnt++)
			{
				SingleAliquotDetailsDTO singleAliquotDetailsDTO = aliquotDetailList.get(cnt);
				SpecimenPosition specimenPosition = null;
				if (!singleAliquotDetailsDTO.getStoragecontainer().equals(
						Constants.STORAGE_TYPE_POSITION_VIRTUAL))
				{
					specimenPosition = storageContainerBizlogic
							.getPositionIfAvailableFromContainer(
									singleAliquotDetailsDTO.getStoragecontainer(),
									singleAliquotDetailsDTO.getPos1(),
									singleAliquotDetailsDTO.getPos2(), hibernateDao);
					singleAliquotDetailsDTO.setPos1(specimenPosition
							.getPositionDimensionOneString());
					singleAliquotDetailsDTO.setPos2(specimenPosition
							.getPositionDimensionTwoString());
				}

				SpecimenDTO aliquotSpecimen = createAliquotSpecimenDTOFromDTO(aliquotDetailsDTO,
						parentSpecimen, singleAliquotDetailsDTO, specimenPosition);
				specimenDtoList.add(aliquotSpecimen);
				//				aliquotSpecimen = specimenBizLogic.insert(aliquotSpecimen, hibernateDao,
				//						sessionDataBean);

			}
			specimenDtoList = specimenBizLogic.insert(specimenDtoList, hibernateDao,
					sessionDataBean);
			for (int i = 0; i < specimenDtoList.size(); i++)
			{

				aliquotDetailList.get(i).setAliqoutLabel(specimenDtoList.get(i).getLabel());
				aliquotDetailList.get(i).setBarCode(specimenDtoList.get(i).getBarcode());
				aliquotDetailList.get(i).setAliquotId(specimenDtoList.get(i).getId());
			}

			if (aliquotDetailsDTO.isDisposeParentSpecimen())
			{

				specimenBizLogic.disposeSpecimen(hibernateDao, sessionDataBean,
						parentSpecimen, Constants.SPECIMEN_DISPOSAL_REASON);
			}
			specimenBizLogic.reduceQuantity(totalDistributedQuantity, parentSpecimen.getId(),
					hibernateDao);
			aliquotDetailsDTO.setCurrentAvailableQuantity(parentSpecimen.getAvailableQuantity()
					- totalDistributedQuantity);
			if (aliquotDetailsDTO.isPrintLabel())
			{
				List<AbstractDomainObject> aliquotList = new ArrayList<AbstractDomainObject>(
						specimenCollection);
				printLabel(aliquotList, sessionDataBean);
			}
		}

		catch (Exception exp)
		{
			throw new ApplicationException(null, null, exp.getMessage());
		}

	}

	public void printLabel(List specimenList, SessionDataBean sessionDataBean) throws Exception
	{
		final LabelPrinter labelPrinter = LabelPrinterFactory.getInstance("specimen");

		final boolean printStauts = labelPrinter.printLabel(
				specimenList,
				sessionDataBean.getIpAddress(),
				SecurityManagerFactory.getSecurityManager().getUserById(
						sessionDataBean.getCsmUserId().toString()), "", "");

	}

	public Specimen getParentSpecimenDetailsForAliquots(HibernateDAO hibernateDao, String label)
			throws ApplicationException
	{
		Specimen specimen = null;
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();

		Collection<?> specimenDetailColl;

		params.put("0", new NamedQueryParam(DBTypes.STRING, label));

		specimenDetailColl = hibernateDao.executeNamedQuery(
				"selectParentSpecimenDetailsForAliquot", params);
		if (specimenDetailColl == null || specimenDetailColl.isEmpty())
		{

			specimenDetailColl = hibernateDao.executeNamedQuery(
					"selectParentSpecimenDetailsForAliquotByBarcode", params);
		}

		if (specimenDetailColl == null || specimenDetailColl.isEmpty())
		{
			throw new BizLogicException(null, null, Constants.INVALID_LABEL_BARCODE);
		}
		Iterator specimenDetailIterator = specimenDetailColl.iterator();
		if (specimenDetailIterator.hasNext())
		{

			final Object[] valArr = (Object[]) specimenDetailIterator.next();
			if (valArr != null)
			{
				//specimenDTO = getSpecimenDTOObject(valArr);
				specimen = new Specimen();
				SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
				scg.setId(Long.parseLong(valArr[1].toString()));
				specimen.setSpecimenCollectionGroup(scg);
				specimen.setId(Long.parseLong(valArr[2].toString()));
				specimen.setLabel(valArr[3].toString());
				specimen.setBarcode(valArr[4].toString());
				specimen.setSpecimenClass(valArr[5].toString());
				specimen.setSpecimenType(valArr[6].toString());
				specimen.setPathologicalStatus(valArr[7].toString());
				SpecimenCharacteristics specimenChar = new SpecimenCharacteristics();
				specimenChar.setTissueSite(valArr[8].toString());
				specimenChar.setTissueSide(valArr[9].toString());
				specimen.setSpecimenCharacteristics(specimenChar);
				specimen.setAvailableQuantity(Double.parseDouble(valArr[10].toString()));
				if (valArr[11] != null)
					specimen.setConcentrationInMicrogramPerMicroliter(Double.parseDouble(valArr[11]
							.toString()));
				specimen.setInitialQuantity(Double.parseDouble(valArr[12].toString()));

			}

		}
		return specimen;

	}

	public AliquotDetailsDTO getAliquotDetailsDTO(String parentSpecimentLabel, int aliquotCount,
			Double quantityPerAliquot, HibernateDAO hibernateDao, SessionDataBean sessionDataBean)
			throws ApplicationException
	{
		AliquotDetailsDTO aliquotDetailsDTO = new AliquotDetailsDTO();
		Specimen parentSpecimen = getParentSpecimenDetailsForAliquots(hibernateDao,
				parentSpecimentLabel);
		if (quantityPerAliquot != null
				&& ((quantityPerAliquot * aliquotCount) > parentSpecimen.getAvailableQuantity()))
		{
			throw new ApplicationException(null, null, Constants.INSUFFICIENT_AVAILABLE_QUANTITY);
		}
		aliquotDetailsDTO.setConcentration(parentSpecimen
				.getConcentrationInMicrogramPerMicroliter());
		aliquotDetailsDTO.setCreationDate(new Date());

		aliquotDetailsDTO.setCurrentAvailableQuantity(parentSpecimen.getAvailableQuantity());
		aliquotDetailsDTO.setInitialAvailableQuantity(parentSpecimen.getInitialQuantity());
		aliquotDetailsDTO.setParentId(parentSpecimen.getId());
		aliquotDetailsDTO.setParentLabel(parentSpecimen.getLabel());
		aliquotDetailsDTO.setPathologicalStatus(parentSpecimen.getPathologicalStatus());
		aliquotDetailsDTO.setSpecimenClass(parentSpecimen.getSpecimenClass());
		aliquotDetailsDTO.setType(parentSpecimen.getSpecimenType());
		aliquotDetailsDTO
				.setTissueSide(parentSpecimen.getSpecimenCharacteristics().getTissueSide());
		aliquotDetailsDTO
				.setTissueSite(parentSpecimen.getSpecimenCharacteristics().getTissueSite());
		aliquotDetailsDTO.setConcentration(parentSpecimen
				.getConcentrationInMicrogramPerMicroliter());
		List<SingleAliquotDetailsDTO> singleAliquotDetailsDTOList = new ArrayList<SingleAliquotDetailsDTO>();

		if (quantityPerAliquot == null)
		{
			quantityPerAliquot = parentSpecimen.getAvailableQuantity() / aliquotCount;
		}

		Long cpId = getCpIdFromSpecimenLabel(aliquotDetailsDTO.getParentLabel(), hibernateDao);
		ContainerInputDetailsDTO containerInputDetail = new ContainerInputDetailsDTO();
		containerInputDetail.aliquotCount = aliquotCount;
		containerInputDetail.cpId = cpId;
		containerInputDetail.isAdmin = sessionDataBean.isAdmin();
		containerInputDetail.userId = sessionDataBean.getUserId();
		containerInputDetail.specimenClass = aliquotDetailsDTO.getSpecimenClass();
		containerInputDetail.specimenType = aliquotDetailsDTO.getType();
		AliquotContainerDetailsDTO aliquotContainerDetails = getAliquotContainerDetails(
				containerInputDetail, hibernateDao);
		for (int i = 0; i < aliquotCount; i++)
		{
			SingleAliquotDetailsDTO singleAliquotDetailsDTO = new SingleAliquotDetailsDTO();
			singleAliquotDetailsDTO.setQuantity(quantityPerAliquot);
			singleAliquotDetailsDTO.setAliqoutLabel("");
			singleAliquotDetailsDTO.setBarCode("");
			singleAliquotDetailsDTO.setStoragecontainer(aliquotContainerDetails.containerName);

			singleAliquotDetailsDTO.setPos1(aliquotContainerDetails.position1.get(i));
			singleAliquotDetailsDTO.setPos2(aliquotContainerDetails.position2.get(i));
			singleAliquotDetailsDTOList.add(singleAliquotDetailsDTO);

		}

		aliquotDetailsDTO.setPerAliquotDetailsCollection(singleAliquotDetailsDTOList);
		return aliquotDetailsDTO;
	}

	public AliquotContainerDetailsDTO getAliquotContainerDetails(
			ContainerInputDetailsDTO containerInputDetails, HibernateDAO hibernateDao)
			throws ApplicationException
	{

		StorageContainerBizlogic storageContainerBizlogic = new StorageContainerBizlogic();

		AliquotContainerDetailsDTO aliquotContainerDetails = new AliquotContainerDetailsDTO();
		List<AliquotContainerDetailsDTO> aliquotContainerDetailsDTOList = storageContainerBizlogic
				.getStorageContainerList(containerInputDetails, null, hibernateDao, 1);

		if (aliquotContainerDetailsDTOList == null || aliquotContainerDetailsDTOList.isEmpty())
		{
			throw new ApplicationException(null, null, Constants.INSUFFICIEN_STORAGE_LOCATION);
		}
		AliquotContainerDetailsDTO aliquotContainerDetailsDTO = aliquotContainerDetailsDTOList
				.get(0);
		storageContainerBizlogic.setAvailablePositionsForContainer(aliquotContainerDetailsDTO, "",
				"", containerInputDetails.aliquotCount, hibernateDao);

		return aliquotContainerDetailsDTO;
	}

	public Long getCpIdFromSpecimenId(Long specimenId, HibernateDAO hibernateDao)
			throws DAOException
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.LONG, specimenId));
		List specimenDetailColl = hibernateDao.executeNamedQuery("getCpIdFromSpecimenId", params);
		return (Long) specimenDetailColl.get(0);
	}

	public Long getCpIdFromSpecimenLabel(String specimenLable, HibernateDAO hibernateDao)
			throws DAOException
	{
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.STRING, specimenLable));
		List specimenDetailColl = hibernateDao
				.executeNamedQuery("getCpIdFromSpecimenLabel", params);
		return (Long) specimenDetailColl.get(0);
	}

	public List<SpecimenDTO> getAvailabelSpecimenList(String specimenLable,
			HibernateDAO hibernateDao) throws ApplicationException
	{
		List<SpecimenDTO> specimenDaoList = new ArrayList<SpecimenDTO>();
		Map<String, NamedQueryParam> params = new HashMap<String, NamedQueryParam>();
		params.put("0", new NamedQueryParam(DBTypes.STRING, specimenLable));
		params.put("1", new NamedQueryParam(DBTypes.STRING, specimenLable));

		List specimenDetailColl = hibernateDao.executeNamedQuery("getAvailabelSpecimenLabel",
				params);
		for (int i = 0; i < specimenDetailColl.size(); i++)
		{
			Object[] objectArr = (Object[]) specimenDetailColl.get(i);
			SpecimenDTO specimenDTO = new SpecimenDTO();
			specimenDTO.setLabel(objectArr[0]!=null ? objectArr[0].toString():"");
			specimenDTO.setBarcode(objectArr[1]!=null ? objectArr[1].toString():"");
			specimenDaoList.add(specimenDTO);
		}

		return specimenDaoList;
	}

}
