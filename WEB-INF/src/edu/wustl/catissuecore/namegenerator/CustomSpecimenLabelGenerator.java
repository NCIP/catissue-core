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
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.util.global.Validator;
import edu.wustl.common.util.logger.Logger;


// TODO: Auto-generated Javadoc
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
	private static final Logger LOGGER = Logger.getCommonLogger(CustomSpecimenLabelGenerator.class);


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
		if(isCollStatusPending(objSpecimen))
		{
			throw new LabelGenException("Specimen status is not "+Constants.COLLECTION_STATUS_COLLECTED);
		}
		if (objSpecimen.getLabel() != null)
		{
			throw new LabelGenException("Label already assigned");
		}

			this.setSpecimenReq(objSpecimen);
			this.setScgData(objSpecimen);

			setLabelForSpecimen(objSpecimen);

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
	 * Checks if is coll status pending.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @return true, if checks if is coll status pending
	 */
	private boolean isCollStatusPending(final Specimen objSpecimen)
	{
		return objSpecimen.getCollectionStatus() == null || !Constants.COLLECTION_STATUS_COLLECTED.equals(objSpecimen.getCollectionStatus());
	}

	/**
	 * Sets the label for specimen.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @throws LabelGenException the label gen exception
	 */
	private void setLabelForSpecimen(final Specimen objSpecimen) throws LabelGenException
	{
		StringBuffer buffer = new StringBuffer();

		if(isGenLabel(objSpecimen))
		{
			String labelFormat = getLabelFormat(objSpecimen, buffer);

			if (!Validator.isEmpty(labelFormat))
			{
				StringTokenizer tokenizer = new StringTokenizer(labelFormat, "%");
				while (tokenizer.hasMoreTokens())
				{
					String token = tokenizer.nextToken();
					try
					{
						buffer.append(getTokenValue(objSpecimen, token));

					}
					catch (final Exception ex)
					{
						LOGGER.info(ex);
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
	}

	/**
	 * Gets the token value.
	 *
	 * @param objSpecimen the obj specimen
	 * @param token the token
	 *
	 * @return the token value
	 *
	 * @throws NameGeneratorException the name generator exception
	 */
	private String getTokenValue(final Specimen objSpecimen, String token)
			throws NameGeneratorException
	{
		if("SYS_UID".equals(token))
		{
			currentLabel = currentLabel+1;
		}
		String mylable = TokenFactory.getInstance(token).getTokenValue(objSpecimen,
				token, currentLabel);
		if (mylable == null)
		{
			mylable = "";
		}
		return mylable;
	}


	/**
	 * Checks if is gen label.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @return true, if checks if is gen label
	 */
	private boolean isGenLabel(final Specimen objSpecimen)
	{
		return (objSpecimen.getSpecimenRequirement() != null && objSpecimen.getSpecimenRequirement().getGenLabel()) || objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel();
	}


	/**
	 * Gets the label format.
	 *
	 * @param objSpecimen the obj specimen
	 * @param buffer the buffer
	 *
	 * @return the label format
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
			else if(isCPLabelformatAvl(objSpecimen))
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
		else if(isCPLabelformatAvl(objSpecimen))
		{
			labelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat();
		}
		return labelFormat;
	}

	/**
	 * Checks if is cp labelformat avl.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @return true, if checks if is cp labelformat avl
	 */
	private boolean isCPLabelformatAvl(final Specimen objSpecimen)
	{
		return objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel() && !Validator.isEmpty(objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat());
	}


	/**
	 * Checks if is cp default.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @return true, if checks if is cp default
	 */
	private boolean isCPDefault(final Specimen objSpecimen)
	{
		return objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel() && Validator.isEmpty(objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat());
	}


	/**
	 * Sets the scg data.
	 *
	 * @param objSpecimen the obj specimen
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
	 * Sets the specimen req.
	 *
	 * @param objSpecimen the obj specimen
	 */
	private void setSpecimenReq(final Specimen objSpecimen)
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
				LOGGER.info(exp);
			}
		}
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



	/**
	 * Gets the sCG data.
	 *
	 * @param spec the spec
	 *
	 * @return the sCG data
	 */
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
		CollectionProtocol protocol = new CollectionProtocol();
		List list;
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
			Long cprId = Long.valueOf(obje[2].toString());
			String PPI= obje[3].toString();
			Long scgId = Long.valueOf(obje[4].toString());
			Long cpId = Long.valueOf(obje[5].toString());
			protocol.setId(cpId);
			protocol.setSpecimenLabelFormat(labelFormat);
			protocol.setGenerateLabel(generateLabel);

			cpr.setId(cprId);
			cpr.setProtocolParticipantIdentifier(PPI);
			cpr.setCollectionProtocol(protocol);

			scg.setId(scgId);
			scg.setCollectionProtocolRegistration(cpr);

		}
		catch (ApplicationException e)
		{
			LOGGER.info(e);
		}
		return scg;
	}


}
