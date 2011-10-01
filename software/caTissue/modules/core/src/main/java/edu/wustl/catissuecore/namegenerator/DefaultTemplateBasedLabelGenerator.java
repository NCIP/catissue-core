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
import edu.wustl.catissuecore.factory.DomainInstanceFactory;
import edu.wustl.catissuecore.factory.InstanceFactory;
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
public class DefaultTemplateBasedLabelGenerator extends DefaultSpecimenLabelGenerator implements TemplateBasedLabelGenerator
{

	/** The logger. */
	private static final Logger LOGGER = Logger.getCommonLogger(DefaultTemplateBasedLabelGenerator.class);


	/**
	 * Instantiates a new custom specimen label generator.
	 *
	 * @throws ApplicationException the application exception
	 */
	public DefaultTemplateBasedLabelGenerator() throws ApplicationException
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

			if (objSpecimen.getChildSpecimenCollection()!=null && objSpecimen.getChildSpecimenCollection().size() > 0)
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
					catch (final NameGeneratorException ex)
					{
						throw new LabelException(ex.getMessage());
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
//		if("SYS_UID".equals(token))
//		{
//			currentLabel = currentLabel+1;
//		}
		String mylable = "";
		try
		{
			mylable = TokenFactory.getInstance(token).getTokenValue(objSpecimen);

		}
		catch(TokenNotFoundException exp)
		{
			mylable = token;
		}
		catch(ApplicationException excep)
		{
			throw new NameGeneratorException(excep.getMsgValues());
		}
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
		boolean isGenLabel = false;

		if(objSpecimen.getSpecimenRequirement() != null && Validator.isEmpty(objSpecimen.getSpecimenRequirement().getLabelFormat()))
		{
			isGenLabel = false;
		}
		else if(objSpecimen.getSpecimenRequirement() != null && !Validator.isEmpty(objSpecimen.getSpecimenRequirement().getLabelFormat()) && !objSpecimen.getSpecimenRequirement().getLabelFormat().contains("%CP_DEFAULT%"))
		{
			isGenLabel = true;
		}
		else if(objSpecimen.getSpecimenRequirement() != null && objSpecimen.getSpecimenRequirement().getLabelFormat().contains("%CP_DEFAULT%"))
		{
			isGenLabel = isLblGenOnForCP(objSpecimen);
		}
		else if(objSpecimen.getSpecimenRequirement() == null)
		{
			isGenLabel = isLblGenOnForCP(objSpecimen);
		}
		return isGenLabel;
	}


	public static boolean isLblGenOnForCP(Specimen objSpecimen)
	{
		String lineage = objSpecimen.getLineage();
		String parentLabelformat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat();
		String deriveLabelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getDerivativeLabelFormat();
		String aliquotLabelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getAliquotLabelFormat();
		boolean isGenLabel = false;
		if(Constants.NEW_SPECIMEN.equals(lineage))
		{
			isGenLabel = !Validator.isEmpty(parentLabelformat);
		}
		else if(Constants.DERIVED_SPECIMEN.equals(lineage))
		{
			isGenLabel = getGenLabelForChildSpecimen(deriveLabelFormat,parentLabelformat);
		}
		else if(Constants.ALIQUOT.equals(lineage))
		{
			isGenLabel = getGenLabelForChildSpecimen(aliquotLabelFormat,parentLabelformat);
		}
		return isGenLabel;
	}


	private static boolean getGenLabelForChildSpecimen(String format,String parentLabelformat)
	{
		boolean isGenLabel = false;

		if(!Validator.isEmpty(format) && !format.contains("%CP_DEFAULT%"))
		{
			isGenLabel = true;
		}
		else if(!Validator.isEmpty(format) && format.contains("%CP_DEFAULT%"))
		{
			isGenLabel = !Validator.isEmpty(parentLabelformat);
		}
		return isGenLabel;
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
		if(objSpecimen.getSpecimenRequirement() != null)
		{
			if(objSpecimen.getSpecimenRequirement().getLabelFormat().contains("%CP_DEFAULT%"))
			{
//				labelFormat=objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat();
				labelFormat = getSpecimenLabelFormat(objSpecimen);
			}
			else if(!objSpecimen.getSpecimenRequirement().getLabelFormat().contains("%CP_DEFAULT%"))
			{
				labelFormat = objSpecimen.getSpecimenRequirement().getLabelFormat();
			}
		}

		else if(objSpecimen.getSpecimenRequirement() == null)
		{
			labelFormat = getSpecimenLabelFormat(objSpecimen);
		}

		return labelFormat;
	}

	private String  getSpecimenLabelFormat(Specimen objSpecimen)
	{
		String labelFormat = "";
		if(Constants.NEW_SPECIMEN.equals(objSpecimen.getLineage()))
		{
			labelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat();
		}
		else if(Constants.DERIVED_SPECIMEN.equals(objSpecimen.getLineage()))
		{
			labelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getDerivativeLabelFormat();
		}
		else if(Constants.ALIQUOT.equals(objSpecimen.getLineage()))
		{
			labelFormat = objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getAliquotLabelFormat();
		}
		if(labelFormat.contains("%CP_DEFAULT%"))
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
//	private boolean isCPLabelformatAvl(final Specimen objSpecimen)
//	{
//		return objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel() && !Validator.isEmpty(objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat());
//	}


	/**
	 * Checks if is cp default.
	 *
	 * @param objSpecimen the obj specimen
	 *
	 * @return true, if checks if is cp default
	 */
//	private boolean isCPDefault(final Specimen objSpecimen)
//	{
//		return objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getGenerateLabel() && Validator.isEmpty(objSpecimen.getSpecimenCollectionGroup().getCollectionProtocolRegistration().getCollectionProtocol().getSpecimenLabelFormat());
//	}


	/**
	 * Sets the scg data.
	 *
	 * @param objSpecimen the obj specimen
	 * @throws LabelException
	 */
	private void setScgData(final Specimen objSpecimen) throws LabelException
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
	 * @throws LabelException
	 */
	private void setSpecimenReq(final Specimen objSpecimen) throws LabelException
	{
		if(objSpecimen.getSpecimenRequirement() == null && objSpecimen.getId() != null)
		{
			String hql = "select specimen.specimenRequirement from edu.wustl.catissuecore.domain.Specimen as specimen"
				+" where specimen.id="+ objSpecimen.getId();

			List<Object[]> list=null;
			try
			{
				list=AppUtility.executeQuery(hql);
				if(list!=null && !list.isEmpty())
				{
					Object object = list.get(0);
					objSpecimen.setSpecimenRequirement((SpecimenRequirement)object);
				}
			}
			catch(ApplicationException exp)
			{
				LOGGER.info(exp);
				throw new LabelException(exp.getMessage());
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
	 * @throws LabelException
	 */
	private SpecimenCollectionGroup getSCGData(Specimen spec) throws LabelException
	{
		String hql = "select scg.collectionProtocolRegistration.collectionProtocol.specimenLabelFormat," +
		"scg.collectionProtocolRegistration.collectionProtocol.derivativeLabelFormat, "+
		"scg.collectionProtocolRegistration.collectionProtocol.aliquotLabelFormat, "+
		"scg.collectionProtocolRegistration.id, "+
		" scg.collectionProtocolRegistration.protocolParticipantIdentifier, " +
		"scg.id, "  +
		"scg.collectionProtocolRegistration.collectionProtocol.id "+
		"from edu.wustl.catissuecore.domain.SpecimenCollectionGroup as scg where scg.id ="+ spec.getSpecimenCollectionGroup().getId();

		SpecimenCollectionGroup scg = new SpecimenCollectionGroup();
		InstanceFactory<CollectionProtocolRegistration> cprInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocolRegistration.class);
		CollectionProtocolRegistration cpr = cprInstFact.createObject();
		InstanceFactory<CollectionProtocol> cpInstFact = DomainInstanceFactory.getInstanceFactory(CollectionProtocol.class);
		CollectionProtocol protocol = cpInstFact.createObject();
		List list;
		try
		{
			list = AppUtility.executeQuery(hql);

			Object[] obje = (Object[])list.get(0);
			String labelFormat ="";
			if(obje[0] != null)
			{
				labelFormat = obje[0].toString();
			}

			String derivativeLabelForma ="";
			if(obje[1] != null)
			{
				derivativeLabelForma = obje[1].toString();
			}

			String aliquotLabelFormat ="";
			if(obje[2] != null)
			{
				aliquotLabelFormat = obje[2].toString();
			}

			Long cprId = Long.valueOf(obje[3].toString());
			String PPI = null;
			if(obje[4] != null)
			PPI= obje[4].toString();

			Long scgId = Long.valueOf(obje[5].toString());
			Long cpId = Long.valueOf(obje[6].toString());
			protocol.setId(cpId);
			protocol.setSpecimenLabelFormat(labelFormat);
			protocol.setDerivativeLabelFormat(derivativeLabelForma);
			protocol.setAliquotLabelFormat(aliquotLabelFormat);
			cpr.setId(cprId);
			cpr.setProtocolParticipantIdentifier(PPI);
			cpr.setCollectionProtocol(protocol);

			scg.setId(scgId);
			scg.setCollectionProtocolRegistration(cpr);

		}
		catch (ApplicationException exp)
		{
			LOGGER.info(exp);
			throw new LabelException(exp.getMessage());
		}
		return scg;
	}
	/**
	 * Set Label.
	 * @param obj Object
	 * @param ignoreCollectedStatus boolean
	 * @throws LabelGenException LabelGenException
	 */
	public synchronized void setLabel(Object obj, boolean ignoreCollectedStatus) throws LabelGenException
	{
		final Specimen objSpecimen = (Specimen) obj;
		if (objSpecimen.getLabel() != null)
		{
			throw new LabelGenException("Label already assigned");
		}
		this.setSpecimenReq(objSpecimen);
		this.setScgData(objSpecimen);
		setLabelForSpecimen(objSpecimen);
		if (objSpecimen.getChildSpecimenCollection()!=null && objSpecimen.getChildSpecimenCollection().size() > 0)
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
}
