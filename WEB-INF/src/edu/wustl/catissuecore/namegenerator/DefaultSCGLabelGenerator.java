package edu.wustl.catissuecore.namegenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.SpecimenCollectionGroup;
import edu.wustl.catissuecore.util.global.Constants;


/**
 * DefaultSpecimenLabelGenerator is a class which contains the default 
 * implementations AbstractSpecimenGenerator classe.
 * @author virender_mehta
 */
public class DefaultSCGLabelGenerator implements LabelGenerator
{
	/**
	 * Current label 
	 */
	protected Long currentLabel;
	/**
	 * Datasource Name
	 */
	String DATASOURCE_JNDI_NAME = "java:/catissuecore";
	/**
	 * Default Constructor
	 */
	public DefaultSCGLabelGenerator()
	{
		super();
		init();
	}
	
	/**
	 * This is a init() function it is called from the default constructor of Base class.When getInstance of base class
	 * called then this init function will be called.
	 * This method will first check the Datatbase Name and then set function name that will convert
	 * lable from int to String
	 */
	protected void init()
	{
		currentLabel = getLastAvailableSCGLabel(null);
	}
	
	/**
	 * This method will retrive unique specimen Lable.
	 * @return Total No of Specimen
	 * @throws ClassNotFoundException
	 * @throws DAOException 
	 */
	private Long getLastAvailableSCGLabel(String databaseConstant)  
	{
		Long noOfRecords = new Long("0");
		String sql = "Select max(IDENTIFIER) as MAX_IDENTIFIER from CATISSUE_ABS_SPECI_COLL_GROUP";
 		Connection conn = null;
		try
		{
        	InitialContext ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup(DATASOURCE_JNDI_NAME);
        	conn = ds.getConnection();
        	ResultSet resultSet= conn.createStatement().executeQuery(sql);
        	
        	if(resultSet.next())
        	{
        		return new Long (resultSet.getLong(1));
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
 
		return noOfRecords;
	}

	
	public void setLabel(Object obj) 
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)obj;
		CollectionProtocolRegistration collectionProtocolRegistration = specimenCollectionGroup.getCollectionProtocolRegistration();
		CollectionProtocol collectionProtocol = collectionProtocolRegistration.getCollectionProtocol();
		long participantId = collectionProtocolRegistration.getParticipant().getId();
		String collectionProtocolTitle=collectionProtocol.getTitle();
		String maxCollTitle = collectionProtocolTitle;
		if(collectionProtocolTitle.length()>Constants.COLLECTION_PROTOCOL_TITLE_LENGTH)
		{
			maxCollTitle = collectionProtocolTitle.substring(0,Constants.COLLECTION_PROTOCOL_TITLE_LENGTH-1);
		}
		currentLabel++;
		specimenCollectionGroup.setName(maxCollTitle+"_"+participantId+"_"+currentLabel);
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.namegenerator.LabelGenerator#setLabel(java.util.List)
	 */
	public void setLabel(List objSpecimenList) {

		
	}
	
	/**
	 * Returns label for the given domain object
	 */
	public String getLabel(Object obj) 
	{
		SpecimenCollectionGroup specimenCollectionGroup = (SpecimenCollectionGroup)obj;
		setLabel(specimenCollectionGroup);
		
		return (specimenCollectionGroup.getName());
	}	
}
