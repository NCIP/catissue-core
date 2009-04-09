
package edu.wustl.catissuecore.namegenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.dao.daofactory.DAOConfigFactory;

/**
 * This  class which contains the default  implementation for Specimen label generation.
 * @author falguni_sachde
 *
 */
public class DefaultSpecimenLabelGenerator implements LabelGenerator
{

	/**
	 * Current label.
	 */
	protected Long currentLabel;
	/**
	 * Datasource Name.
	 */
	String DATASOURCE_JNDI_NAME = "java:/catissuecore";

	/**
	 * Default Constructor.
	 */
	public DefaultSpecimenLabelGenerator()
	{
		init();
	}

	/**
	 * This is a init() function it is called from the
	 * default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * lable from int to String
	 */
	protected void init()
	{
		try
		{
			if (Constants.ORACLE_DATABASE.equals(DAOConfigFactory.getInstance().getDAOFactory(Constants.APPLICATION_NAME).getDataBaseType()))
			{
				currentLabel = getLastAvailableSpecimenLabel
				(Constants.ORACLE_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION);
			}
			else if (Constants.MSSQLSERVER_DATABASE.equals(DAOConfigFactory.getInstance().getDAOFactory(Constants.APPLICATION_NAME).getDataBaseType()))
			{
				currentLabel = getLastAvailableSpecimenLabel(Constants.MSSQLSERVER_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION);
			}
			else
			{
				currentLabel = getLastAvailableSpecimenLabel
				(Constants.MYSQL_NUM_TO_STR_FUNCTION_NAME_FOR_LABEL_GENRATION);
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * This method will retrieve unique specimen Label.
	 * @param databaseConstant constant
	 * @return noOfRecords
	 */
	private Long getLastAvailableSpecimenLabel(String databaseConstant)
	{
		StringBuffer sql = new StringBuffer("select MAX(" + databaseConstant + ") from CATISSUE_SPECIMEN");
		
		// Modify query for mssqlserver DB.
		if (Constants.MSSQLSERVER_DATABASE.equals(DAOConfigFactory.getInstance().getDAOFactory(Constants.APPLICATION_NAME).getDataBaseType())) {
			sql.append(Constants.MSSQLSERVER_QRY_DT_CONVERSION_FOR_LABEL_APPEND_STR);
		}
		Connection conn = null;
		Long noOfRecords = new Long("0");
		try
		{
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(DATASOURCE_JNDI_NAME);
			conn = ds.getConnection();
			ResultSet resultSet = conn.createStatement().executeQuery(sql.toString());

			if (resultSet.next())
			{
				return new Long(resultSet.getLong(1));
			}
		}
		catch (NamingException e)
		{
			e.printStackTrace();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			if (conn != null)
			{
				try
				{
					conn.close();
				}
				catch (SQLException exception)
				{
					// TODO Auto-generated catch block
					exception.printStackTrace();
				}
			}
		}
		return noOfRecords;
	}

	/**
	 * @param parentObject parent object
	 * @param specimenObject specimen obj
	 */
	synchronized void setNextAvailableAliquotSpecimenlabel(Specimen parentObject,
			Specimen specimenObject)
	{

		String parentSpecimenLabel = (String) parentObject.getLabel();
		long aliquotCount = parentObject.getChildSpecimenCollection().size();
		aliquotCount = aliquotCount(parentObject, aliquotCount);
		specimenObject.setLabel(parentSpecimenLabel + "_" + (aliquotCount));
	}

	/**
	 * @param parentObject parent object
	 * @param aliquotCount aliquot count
	 * @return aliquotCount
	 */
	protected long aliquotCount(AbstractSpecimen parentObject, long aliquotCount)
	{
		Iterator<AbstractSpecimen> itr = parentObject.getChildSpecimenCollection().iterator();
		while (itr.hasNext())
		{
			Specimen spec = (Specimen) itr.next();
			if (spec.getLineage().equals(Constants.DERIVED_SPECIMEN) || spec.getLabel() == null)
			{
				aliquotCount--;
			}
		}
		aliquotCount++;
		return aliquotCount;
	}

	/**
	 * @param parentObject parent obj
	 * @param specimenObject specimen obj
	 */
	synchronized void setNextAvailableDeriveSpecimenlabel(Specimen parentObject,
			Specimen specimenObject)
	{
		currentLabel = currentLabel + 1;
		specimenObject.setLabel(currentLabel.toString());
	}

	/**
	 * Setting label.
	 * @param obj Specimen object
	 */
	public synchronized void setLabel(Object obj)
	{

		Specimen objSpecimen = (Specimen) obj;
		Specimen parentSpecimen = (Specimen) objSpecimen.getParentSpecimen();
		if (objSpecimen.getLabel() != null)
		{
			return;
		}

		if (objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))
		{
			currentLabel++;
			objSpecimen.setLabel(currentLabel.toString());
		}
		else if (objSpecimen.getLineage().equals(Constants.ALIQUOT))
		{
			this.setNextAvailableAliquotSpecimenlabel(parentSpecimen, objSpecimen);
		}
		else if (objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))
		{
			setNextAvailableDeriveSpecimenlabel(parentSpecimen, objSpecimen);
		}

		if (objSpecimen.getChildSpecimenCollection().size() > 0)
		{
			Collection<AbstractSpecimen> specimenCollection = objSpecimen.getChildSpecimenCollection();
			Iterator<AbstractSpecimen> it = specimenCollection.iterator();
			while (it.hasNext())
			{
				Specimen objChildSpecimen = (Specimen) it.next();
				setLabel(objChildSpecimen);
			}
		}
	}

	/**
	 * Setting Label.
	 * @param objSpecimenList Specimen object list
	 */
	public synchronized void setLabel(List objSpecimenList)
	{

		List specimenList = objSpecimenList;
		for (int index = 0; index < specimenList.size(); index++)
		{
			Specimen objSpecimen = (Specimen) specimenList.get(index);
			setLabel(objSpecimen);
		}

	}

	/**
	 * Returns label for the given domain object.
	 * @param obj Specimen obj
	 * @return label
	 */
	public String getLabel(Object obj)
	{
		Specimen objSpecimen = (Specimen) obj;
		setLabel(objSpecimen);
		return (objSpecimen.getLabel());
	}
}