package edu.wustl.catissuecore.namegenerator;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.domain.SpecimenRequirement;
import edu.wustl.catissuecore.util.global.AppUtility;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


/**
 * The Class CustomSpecimenLabelGenerator.
 */
/**
 * @author nitesh_marwaha
 *
 */
public class CustomSpecimenLabelGenerator extends DefaultSpecimenLabelGenerator
{

	/** The logger. */
	private static Logger logger = Logger.getCommonLogger(CustomSpecimenLabelGenerator.class);


	/**
	 * Instantiates a new custom specimen label generator.
	 *
	 * @throws ApplicationException the application exception
	 */
	public CustomSpecimenLabelGenerator() throws ApplicationException
	{
		super();

	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.DefaultSpecimenLabelGenerator#setLabel(java.lang.Object)
	 */
	@Override
	/**
	 * Setting label.
	 * @param obj Specimen object
	 */
	public synchronized void setLabel(Object obj) throws LabelGenException
	{
		final Specimen objSpecimen = (Specimen) obj;
		if(objSpecimen.getCollectionStatus() == null || !Constants.COLLECTION_STATUS_COLLECTED.equals(objSpecimen.getCollectionStatus()))
		{
			throw new LabelGenException("Specimen status is not "+Constants.COLLECTION_STATUS_COLLECTED);
		}
		if (objSpecimen.getLabel() != null)
		{
			throw new LabelGenException("Label already assigned");
		}
			final Specimen parentSpecimen = (Specimen) objSpecimen.getParentSpecimen();

			this.getSpecimenReq(objSpecimen);
			this.setScgData(objSpecimen);

			StringBuffer buffer = new StringBuffer();
			String labelFormat="";

			if(isGenLabel(objSpecimen))
			{
					labelFormat = getLabelFormat(objSpecimen, buffer);

				if (!Validator.isEmpty(labelFormat))
				{
					StringTokenizer st = new StringTokenizer(labelFormat.toString(), "%");
					while (st.hasMoreTokens())
					{
						String token = st.nextToken();
						try
						{
							String mylable = TokenFactory.getInstance(token).getTokenValue(objSpecimen,
									token, currentLabel);
							if (mylable == null)
							{
								mylable = "";
							}
							buffer.append(mylable);

						}
						catch (final Exception ex)
						{
							logger.info(ex);
						}
					}
//						buffer.append("_");
//						buffer.append(getSpecimenCount(objSpecimen));
				}
				objSpecimen.setLabel(buffer.toString());
			}
			else
			{
				throw new LabelGenException("Manual Label Generation is selected for this specimen");
			}

			if (objSpecimen.getChildSpecimenCollection().size() > 0)
			{
				final Collection<AbstractSpecimen> specimenCollection = objSpecimen
						.getChildSpecimenCollection();


				final Iterator<AbstractSpecimen> specCollItr = specimenCollection.iterator();

				while (specCollItr.hasNext())
				{
					final Specimen objChildSpecimen = (Specimen) specCollItr.next();
					this.setLabel(objChildSpecimen);
				}
			}
	}


	/**
	 * @param objSpecimen
	 * @return
	 */
	private boolean isGenLabel(final Specimen objSpecimen)
	{
		return (objSpecimen.getSpecimenRequirement() != null && objSpecimen.getSpecimenRequirement().getGenLabel()) || objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel();
	}


	/**
	 * @param objSpecimen
	 * @param buffer
	 * @param labelFormat
	 * @return
	 */
	private String getLabelFormat(final Specimen objSpecimen, StringBuffer buffer)
	{
		String labelFormat = "";
		if(objSpecimen.getSpecimenRequirement() != null && objSpecimen.getSpecimenRequirement().getGenLabel() && Validator.isEmpty(objSpecimen.getSpecimenRequirement().getLabelFormat()))
		{
			if(isCPDefault(objSpecimen))
			{
				currentLabel = currentLabel+1;
				buffer.append(currentLabel);
			}
			else if(objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel() && !Validator.isEmpty(objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat()))
			{
				labelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat();
			}
		}

		else if(objSpecimen.getSpecimenRequirement() != null && objSpecimen.getSpecimenRequirement().getGenLabel() && !Validator.isEmpty(objSpecimen.getSpecimenRequirement().getLabelFormat()))
		{
			labelFormat=objSpecimen.getSpecimenRequirement().getLabelFormat();
		}
		else if(isCPDefault(objSpecimen))
		{
			currentLabel = currentLabel+1;
			buffer.append(currentLabel);
		}
		else if(objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel() && !Validator.isEmpty(objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat()))
		{
			labelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat();
		}
		return labelFormat;
	}


	/**
	 * @param objSpecimen
	 * @return
	 */
	private boolean isCPDefault(final Specimen objSpecimen)
	{
		return objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel() && Validator.isEmpty(objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat());
	}


	/**
	 * @param objSpecimen
	 */
	private void setScgData(final Specimen objSpecimen)
	{
		if(objSpecimen.getSpecimenCollectionGroup() == null || objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration() == null)
		{
			SpecimenCollectionGroup group = getSCGData(objSpecimen);
			if(group != null)
			{
				objSpecimen.setSpecimenCollectionGroup(group);
			}

		}
	}


	/**
	 * @param objSpecimen
	 */
	private void getSpecimenReq(final Specimen objSpecimen)
	{
		if(objSpecimen.getSpecimenRequirement() == null && objSpecimen.getId() != null)
		{
			String hql = "select specimen.specimenRequirement from edu.wustl.catissuecore.domain.Specimen as specimen"
				+" where specimen.id="+ objSpecimen.getId();

			List<Object[]> list=null;
			try
			{
				list=AppUtility.executeQuery(hql);
				if(list!=null)
				{
					Object object = list.get(0);
					objSpecimen.setSpecimenRequirement((SpecimenRequirement)object);
				}
			}
			catch(ApplicationException exp)
			{
				logger.info(exp);
			}
		}
	}



	/**
	 * Checks if is aliquot.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @return true, if checks if is aliquot
	 */
	private boolean isAliquot(final Specimen objSpecimen)
	{
		if(objSpecimen.getLineage() == null)
		{
			objSpecimen.setLineage(Constants.NEW_SPECIMEN);
		}
		return objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN) ||objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN);
	}


	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.DefaultSpecimenLabelGenerator#setNextAvailableDeriveSpecimenlabel(edu.wustl.catissuecore.domain.Specimen, edu.wustl.catissuecore.domain.Specimen)
	 */
	@Override
	/**
	 * @param parentObject parent obj
	 * @param specimenObject specimen obj
	 */
	synchronized void setNextAvailableDeriveSpecimenlabel(Specimen parentObject,
			Specimen specimenObject)
	{
		this.currentLabel = this.currentLabel + 1;
		specimenObject.setLabel(this.currentLabel.toString());
	}

	public synchronized void setLabel(Collection<AbstractDomainObject> object) throws LabelGenException
	{
		Iterator<AbstractDomainObject> iterator = object.iterator();
			SpecimenCollectionGroup scg = null;
			while (iterator.hasNext())
			{
				final Specimen newSpecimen = (Specimen) iterator.next();
				if(scg == null)
				{
					if(newSpecimen.getSpecimenCollectionGroup() != null && newSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration() != null)
					{
						scg = newSpecimen.getSpecimenCollectionGroup();
					}
					else
					{
						scg = getSCGData(newSpecimen);
					}
				}
				getSpecimenReq(newSpecimen);
			}
	}


	private SpecimenCollectionGroup getSCGData(Specimen spec)
	{
		String hql = "select scg.collectionProtocolRegistration.collectionProtocol.generateLabel," +
		"scg.collectionProtocolRegistration.collectionProtocol.specimenLabelFormat, "+
		"scg.collectionProtocolRegistration.id, "+
		" scg.collectionProtocolRegistration.protocolParticipantIdentifier, " +
		"scg.id, "  +
		"scg.collectionProtocolRegistration.collectionProtocol.id "+
		"from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg where scg.id ="+ spec.getSpecimenCollectionGroup().getId();

		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		CollectionProtocolRegistration cpr = new CollectionProtocolRegistration();
		CollectionProtocol cp = new CollectionProtocol();
		List list = null;
		try
		{
			list = AppUtility.executeQuery(hql);

			Object[] obje = (Object[])list.get(0);
			boolean generateLabel = (Boolean)obje[0];
			String labelFormat ="";
			if(obje[1] != null)
			{
				labelFormat = obje[1].toString();
			}
			Long cprId = Long.valueOf((obje[2].toString()));
			String PPI= obje[3].toString();
			Long scgId = Long.valueOf(obje[4].toString());
			Long cpId = Long.valueOf(obje[5].toString());
			cp.setId(cpId);
			cp.setSpecimenLabelFormat(labelFormat);
			cp.setGenerateLabel(generateLabel);

			cpr.setId(cprId);
			cpr.setProtocolParticipantIdentifier(PPI);
			cpr.setCollectionProtocol(cp);

			scg.setId(scgId);
			scg.setCollectionProtocolRegistration(cpr);

		}
		catch (ApplicationException e)
		{
			logger.info(e);
		}
		return scg;
	}


}
