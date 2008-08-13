package edu.wustl.catissuecore.querysuite.metadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddAssociation
{
	private Connection connection = null;
	private static Statement stmt = null;
	
	public void addAssociation() throws SQLException, IOException
	{
		AddAssociations addAssociations = new AddAssociations(connection);
		String entityName = "edu.wustl.catissuecore.domain.Specimen";
		String targetEntityName = "edu.wustl.catissuecore.domain.SpecimenPosition";
		addAssociations.addAssociation(entityName,targetEntityName,"specimen_specimenPosition","ASSOCIATION","specimen",true,"specimenPosition","SPECIMEN_ID",null,1,1,"BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName,entityName,"specimenPosition_specimen","ASSOCIATION","specimenPosition",false,"","SPECIMEN_ID",null,1,0,"BI_DIRECTIONAL");
		
		entityName = "edu.wustl.catissuecore.domain.User";
		targetEntityName = "edu.wustl.catissuecore.domain.CollectionProtocol";
		addAssociations.addAssociation(entityName,targetEntityName,"CATISSUE_USER_CP","ASSOCIATION","assignedProtocolCollection",true,"assignedProtocolUserCollection","COLLECTION_PROTOCOL_ID","USER_ID",2,1,"BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName,entityName,"CATISSUE_USER_CP","ASSOCIATION","assignedProtocolUserCollection",false,"assignedProtocolCollection","COLLECTION_PROTOCOL_ID","USER_ID",2,0,"BI_DIRECTIONAL");
		
		entityName = "edu.wustl.catissuecore.domain.CollectionProtocol";
	    targetEntityName = "edu.wustl.catissuecore.domain.Site";
		addAssociations.addAssociation(entityName,targetEntityName,"CATISSUE_SITE_CP","ASSOCIATION","siteCollection",true,"collectionProtocolCollection","SITE_ID","COLLECTION_PROTOCOL_ID",2,1,"BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName,entityName,"CATISSUE_SITE_CP","ASSOCIATION","collectionProtocolCollection",false,"siteCollection","SITE_ID","COLLECTION_PROTOCOL_ID",2,0,"BI_DIRECTIONAL");
		
		entityName = "edu.wustl.catissuecore.domain.Site";
	    targetEntityName = "edu.wustl.catissuecore.domain.User";
		addAssociations.addAssociation(entityName,targetEntityName,"CATISSUE_SITE_USERS","ASSOCIATION","siteCollection",true,"assignedSiteUserCollection","USER_ID","SITE_ID",2,1,"BI_DIRECTIONAL");
		addAssociations.addAssociation(targetEntityName,entityName,"CATISSUE_SITE_USERS","ASSOCIATION","assignedSiteUserCollection",false,"siteCollection","USER_ID","SITE_ID",2,0,"BI_DIRECTIONAL");
		
		entityName = "edu.wustl.catissuecore.domain.Site";
	    targetEntityName = "edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup";
	    addAssociations.addAssociation(entityName,targetEntityName,"site_abstractSCG","ASSOCIATION","abstractSpecimenCollectionGroupCollection",false,null,null,"SITE_ID",2,1,"BI_DIRECTIONAL");
	}

	public AddAssociation(Connection connection) throws SQLException
	{
		super();
		this.connection = connection;
		this.stmt = connection.createStatement();
	}
}
