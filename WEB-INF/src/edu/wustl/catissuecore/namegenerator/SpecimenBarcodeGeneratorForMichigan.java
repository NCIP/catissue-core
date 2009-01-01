package edu.wustl.catissuecore.namegenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.wustl.catissuecore.domain.AbstractSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.util.global.Constants;

/**
 * This is the Specimen Barcode Generator for Michigan University.
 * @author falguni_sachde
 *
 */
public class SpecimenBarcodeGeneratorForMichigan extends DefaultSpecimenBarcodeGenerator {

	/**
	 * 
	 */
	public SpecimenBarcodeGeneratorForMichigan() {
		//init();//TODO :Commented by Falguni because we are not using separate table for Michigan ,as not able to persist barcode count. 
		super();
	}
	/**
	 * Datasource Name
	 */
	String DATASOURCE_JNDI_NAME = "java:/catissuecore";
	/**
	 * This is a init() function it is called from the default constructor of
	 * Base class. When getInstance of base class called then this init function
	 * will be called. This method will first check the Datatbase Name and then
	 * set function name that will convert lable from int to String
	 */
	protected void init() 
	{
		String sql = "select MAX(LABEL_COUNT) from CATISSUE_SPECIMEN_LABEL_COUNT";
		Connection conn = null;
		currentBarcode = new Long(0);
		try {
        	conn = getConnection();
        	ResultSet resultSet= conn.createStatement().executeQuery(sql);
        	
        	if(resultSet.next())
        	{
        		currentBarcode = new Long (resultSet.getLong(1));
        	}	        
		}
        catch(NamingException e){
        	e.printStackTrace();
        }
        catch(SQLException ex)
        {
        	ex.printStackTrace();
        }
        finally
        {
        	if (conn!=null)
        	{
        		try {
					conn.close();
				} catch (SQLException exception) {
					// TODO Auto-generated catch block
					exception.printStackTrace();
				}
        	}
        }

	}

	/**
	 * @return conn
	 * @throws NamingException
	 * @throws SQLException
	 */
	private Connection getConnection() throws NamingException, SQLException {
		Connection conn;
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup(DATASOURCE_JNDI_NAME);
		conn = ds.getConnection();
		return conn;
	}

	
	/**
	 * @param input
	 * @param pattern
	 * @return String
	 */
	private String format(long input, String pattern) 
	{
		DecimalFormat df = new DecimalFormat(pattern);
		return df.format(input);
	}

	
	/**
	 * 
	 */
//	private void persistLabelCount() 
//	{
//		String sql = "update CATISSUE_SPECIMEN_LABEL_COUNT SET LABEL_COUNT='"
//				+ currentBarcode + "'";
//
//		try {
//			Connection conn = getConnection();
//			conn.createStatement().executeUpdate(sql);
//		} catch (Exception daoexception) {
//			daoexception.printStackTrace();
//		}
//
//	}
	/**
	 * This function is overridden as per Michgam requirement. 
	 */
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.DefaultSpecimenBarcodeGenerator#setBarcode(edu.wustl.common.domain.AbstractDomainObject)
	 */
	public void setBarcode(Object obj) {
		
		Specimen objSpecimen = (Specimen)obj;

		if(objSpecimen.getLineage().equals(Constants.NEW_SPECIMEN))				
		{
			String siteName = objSpecimen.getSpecimenCollectionGroup().getGroupName();
			currentBarcode = currentBarcode + 1;

			String year = new SimpleDateFormat("yy").format(new Date());
			String day = format(Calendar.getInstance().get(Calendar.DAY_OF_YEAR),
					"000");
			String nextNumber = format(currentBarcode, "0000");

			//TODO :Commented by Falguni because hibernate session is getting closed by calling this method. 
			//persistLabelCount();
			//String label = siteName + "-" + year + "-" + day + "-" + nextNumber;
			//Modification suggested for Michigan only -as per catissuecore 1.2.0.1
			String barcode = siteName + "_" + nextNumber;
			objSpecimen.setBarcode(barcode);
		}
	
	
		else if(objSpecimen.getLineage().equals(Constants.ALIQUOT))				
		{
			setNextAvailableAliquotSpecimenBarcode(objSpecimen.getParentSpecimen(),objSpecimen);
		}
	
	
		else if(objSpecimen.getLineage().equals(Constants.DERIVED_SPECIMEN))				
		{
			setNextAvailableDeriveSpecimenBarcode(objSpecimen.getParentSpecimen(),objSpecimen);
		}
		
		if(objSpecimen.getChildSpecimenCollection().size()>0)
		{
			Collection specimenCollection = objSpecimen.getChildSpecimenCollection();
			Iterator it = specimenCollection.iterator();
			while(it.hasNext())
			{
				Specimen objChildSpecimen = (Specimen)it.next();
				setBarcode(objChildSpecimen);
			}
			
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.DefaultSpecimenBarcodeGenerator#setNextAvailableDeriveSpecimenBarcode(edu.wustl.catissuecore.domain.Specimen, edu.wustl.catissuecore.domain.Specimen)
	 */
	synchronized void setNextAvailableDeriveSpecimenBarcode(AbstractSpecimen parentObject,Specimen specimenObject)
	{
		String parentSpecimenBarcode = (String)((Specimen)parentObject).getBarcode();				
		long aliquotCount = parentObject.getChildSpecimenCollection().size();
		specimenObject.setBarcode(parentSpecimenBarcode + "_" + (format((aliquotCount + 1), "00")));
	}

	/**
	 * This function is overridden as per Michigan requirement. 
	 */
	synchronized void setNextAvailableAliquotSpecimenBarcode(AbstractSpecimen parentObject, Specimen specimenObject) 
	{
		String parentSpecimenBarcode = (String)((Specimen)parentObject).getBarcode();
		long aliquotCount =  0;
		aliquotCount = parentObject.getChildSpecimenCollection().size();	
		Iterator itr = parentObject.getChildSpecimenCollection().iterator();
		while(itr.hasNext())
		{
			Specimen spec = (Specimen)itr.next();
			if(spec.getLabel()==null)
			{
				aliquotCount--;
			}
		}
		aliquotCount++;
		specimenObject.setBarcode( parentSpecimenBarcode + "_"+ format((aliquotCount), "00"));
	}
	
}
