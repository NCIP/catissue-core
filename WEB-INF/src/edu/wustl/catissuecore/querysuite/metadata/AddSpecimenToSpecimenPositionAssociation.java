package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddSpecimenToSpecimenPositionAssociation
{
	private Connection connection = null;

	private static Statement stmt = null;
	
	/*public static void main(String args[])throws Exception
	{
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(true);
		
		AddSpecimenToSpecimenPositionAssociation addSpecimenToSpecimenPositionAssociation = new AddSpecimenToSpecimenPositionAssociation(connection);
		addSpecimenToSpecimenPositionAssociation.addAssociation();	
	}*/
	
	public void addAssociation() throws SQLException, IOException
	{
		String entityName = "edu.wustl.catissuecore.domain.Specimen";
		String targetEntityName = "edu.wustl.catissuecore.domain.SpecimenPosition";
		AddAssociations addAssociations = new AddAssociations(connection);
		addAssociations.addAssociation(entityName,targetEntityName,"specimen_specimenPosition","ASSOCIATION","specimen",true,"specimenPosition","SPECIMEN_ID",1,1);
		addAssociations.addAssociation(targetEntityName,entityName,"specimenPosition_specimen","ASSOCIATION","specimenPosition",false,"","SPECIMEN_ID",1,0);
	}

	public AddSpecimenToSpecimenPositionAssociation(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
