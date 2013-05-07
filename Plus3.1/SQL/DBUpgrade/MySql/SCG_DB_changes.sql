
/* Altering engine type to Innodb */
ALTER TABLE CatIssue_Address
TYPE = INNODB;

ALTER TABLE CatIssue_Audit_Event
TYPE = INNODB;

ALTER TABLE CatIssue_Audit_Event_Details
TYPE = INNODB;

ALTER TABLE CatIssue_Audit_Event_Log
TYPE = INNODB;

ALTER TABLE CatIssue_Audit_Event_Query_Log
TYPE = INNODB;

ALTER TABLE CatIssue_bioHazard
TYPE = INNODB;

ALTER TABLE CatIssue_Cancer_Research_Group
TYPE = INNODB;

ALTER TABLE CatIssue_Capacity
TYPE = INNODB;

ALTER TABLE CatIssue_cde
TYPE = INNODB;

ALTER TABLE CatIssue_Cell_spe_Review_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Clinical_Report
TYPE = INNODB;

ALTER TABLE CatIssue_coll_CoorDinAtOrs
TYPE = INNODB;

ALTER TABLE CatIssue_coll_Distribution_rel
TYPE = INNODB;

ALTER TABLE CatIssue_coll_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_coll_Prot_Event
TYPE = INNODB;

ALTER TABLE CatIssue_coll_Prot_reg
TYPE = INNODB;

ALTER TABLE CatIssue_coll_Specimen_req
TYPE = INNODB;

ALTER TABLE CatIssue_Collection_Protocol
TYPE = INNODB;

ALTER TABLE CatIssue_Cont_Holds_SparrType
TYPE = INNODB;

ALTER TABLE CatIssue_Container
TYPE = INNODB;

ALTER TABLE CatIssue_Container_Type
TYPE = INNODB;

ALTER TABLE CatIssue_Department
TYPE = INNODB;

ALTER TABLE CatIssue_Disposal_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Distributed_Item
TYPE = INNODB;

ALTER TABLE CatIssue_Distribution
TYPE = INNODB;

ALTER TABLE CatIssue_Distribution_Protocol
TYPE = INNODB;

ALTER TABLE CatIssue_Distribution_spe_req
TYPE = INNODB;

ALTER TABLE CatIssue_Embedded_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_External_IdentIfier
TYPE = INNODB;

ALTER TABLE CatIssue_Fixed_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Fluid_spe_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Frozen_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_In_Out_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Institution
TYPE = INNODB;

ALTER TABLE CatIssue_InterFace_Column_Data
TYPE = INNODB;

ALTER TABLE CatIssue_mol_spe_Review_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Part_Medical_Id
TYPE = INNODB;

ALTER TABLE CatIssue_Participant
TYPE = INNODB;

ALTER TABLE CatIssue_Password
TYPE = INNODB;

ALTER TABLE CatIssue_Permissible_Value
TYPE = INNODB;

ALTER TABLE CatIssue_Procedure_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Quantity
TYPE = INNODB;

ALTER TABLE CatIssue_Query_EditLink_cols
TYPE = INNODB;

ALTER TABLE CatIssue_Query_Table_Data
TYPE = INNODB;

ALTER TABLE CatIssue_Race
TYPE = INNODB;

ALTER TABLE CatIssue_Received_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Related_Tables_Map
TYPE = INNODB;

ALTER TABLE CatIssue_Reported_Problem
TYPE = INNODB;

ALTER TABLE CatIssue_Search_Display_Data
TYPE = INNODB;

ALTER TABLE CatIssue_Site
TYPE = INNODB;

ALTER TABLE CatIssue_speci_Array_Content
TYPE = INNODB;

ALTER TABLE CatIssue_Specimen
TYPE = INNODB;

ALTER TABLE CatIssue_Specimen_Array
TYPE = INNODB;

ALTER TABLE CatIssue_Specimen_Array_Type
TYPE = INNODB;

ALTER TABLE CatIssue_Specimen_biohz_rel
TYPE = INNODB;

ALTER TABLE CatIssue_Specimen_Char
TYPE = INNODB;

ALTER TABLE CatIssue_Specimen_coll_Group
TYPE = INNODB;

ALTER TABLE CatIssue_Specimen_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Specimen_Protocol
TYPE = INNODB;

ALTER TABLE CatIssue_Specimen_Requirement
TYPE = INNODB;

ALTER TABLE CatIssue_Specimen_Type
TYPE = INNODB;

ALTER TABLE CatIssue_Spun_Event_Parameters
TYPE = INNODB;

ALTER TABLE CatIssue_st_Cont_coll_Prot_rel
TYPE = INNODB;

ALTER TABLE CatIssue_st_Cont_st_Type_rel
TYPE = INNODB;

ALTER TABLE CatIssue_sTor_Cont_spec_Class
TYPE = INNODB;

ALTER TABLE CatIssue_sTor_Type_Holds_Type
TYPE = INNODB;

ALTER TABLE CatIssue_sTor_Type_spec_Class
TYPE = INNODB;

ALTER TABLE CatIssue_Storage_Container
TYPE = INNODB;

ALTER TABLE CatIssue_Storage_Type
TYPE = INNODB;

ALTER TABLE CatIssue_sTorty_Holds_Sparrty
TYPE = INNODB;

ALTER TABLE CatIssue_Table_Relation
TYPE = INNODB;

ALTER TABLE CatIssue_Thaw_Event_Parameters
TYPE = INNODB;

ALTER TABLE CatIssue_tIs_spe_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_Transfer_Event_Param
TYPE = INNODB;

ALTER TABLE CatIssue_User
TYPE = INNODB;

ALTER TABLE csm_Application
TYPE = INNODB;

ALTER TABLE csm_Group
TYPE = INNODB;

ALTER TABLE csm_Privilege
TYPE = INNODB;

ALTER TABLE csm_Protection_Element
TYPE = INNODB;

ALTER TABLE csm_Protection_Group
TYPE = INNODB;

ALTER TABLE csm_Role
TYPE = INNODB;

ALTER TABLE csm_Role_Privilege
TYPE = INNODB;

ALTER TABLE csm_User
TYPE = INNODB;

ALTER TABLE csm_User_Group
TYPE = INNODB;

ALTER TABLE csm_User_Group_Role_pg
TYPE = INNODB;

ALTER TABLE csm_User_pe
TYPE = INNODB;
/*------------Alteration in parent entity 'catissue_specimen_coll_group' ------*/
/* Renamed to parent */

ALTER TABLE CatIssue_Specimen_coll_Group RENAME CatIssue_Abs_Speci_coll_Group;

/* Creating catissue_specimen_coll_group a child of Abstract SCG*/

CREATE TABLE `Catissue_Specimen_coll_Group` (
  `IdentIfier`                 BIGINT(20) NOT NULL AUTO_INCREMENT,
  `Name`                       VARCHAR(255) DEFAULT NULL ,
  `Comments`                   TEXT,
  `Collection_Protocol_reg_Id` BIGINT(20) DEFAULT NULL ,
  `Surgical_Pathology_Number`  VARCHAR(50) DEFAULT NULL ,
  PRIMARY KEY( `IdentIfier` ),
  UNIQUE KEY `Name`( `Name` ),
  KEY `fk_scg_coll_prot_reg`( `Collection_Protocol_reg_Id` ),
  CONSTRAINT `fk_scg_coll_prot_reg` FOREIGN KEY( `Collection_Protocol_reg_Id` ) REFERENCES `CatIssue_coll_Prot_reg`( `IdentIfier` ),
  CONSTRAINT fk_scg_parent_spec_coll_Group FOREIGN KEY( IdentIfier ) REFERENCES CatIssue_Abs_Speci_coll_Group( IdentIfier ))
ENGINE = INNODB
DEFAULT CHARSET = utf8;

ALTER TABLE CatIssue_Specimen_coll_Group ADD COLUMN `Collection_Protocol_Event_Id` BIGINT(20) DEFAULT NULL ;

alter table `catissue_specimen_coll_group` ,add column `COLLECTION_STATUS` varchar (50)   NULL ; 

INSERT INTO CatIssue_Specimen_coll_Group
           (IdentIfier,
            Name,
            Comments,
            Collection_Protocol_reg_Id,
            Surgical_Pathology_Number,
	    collection_status,
	    Collection_Protocol_Event_Id)
SELECT a.IdentIfier,
       a.Name,
       a.Comments,
       a.Collection_Protocol_reg_Id,
       b.Surgical_Pathological_Number,
       'Complete',
       a.Collection_Protocol_Event_Id
FROM   CatIssue_Abs_Speci_coll_Group a
       JOIN CatIssue_Clinical_Report b
         ON a.Clinical_Report_Id = b.IdentIfier;


/*------ Creating child entities ----------------------*/

/*TODO*/

ALTER TABLE CatIssue_Specimen_coll_Group ADD CONSTRAINT `fk_coll_Prot_Event_spec_coll_Group` FOREIGN KEY( `Collection_Protocol_Event_Id`) REFERENCES `catissue_coll_prot_event` (`IDENTIFIER`);

CREATE TABLE `CatIssue_Speci_coll_Req_Group` (
  `IdentIfier` BIGINT(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY( `IdentIfier` ),
  CONSTRAINT fk_Parent_spec_coll_Group_req_Group FOREIGN KEY( IdentIfier ) REFERENCES CatIssue_Abs_Speci_coll_Group( IdentIfier ))
ENGINE = INNODB
DEFAULT CHARSET = utf8;
/* ----------------------Alteration in 'catissue_coll_prot_event' ------*/

ALTER TABLE CatIssue_coll_Prot_Event ADD COLUMN Specimen_Coll_req_Group_Id BIGINT(20);

ALTER TABLE CatIssue_coll_Prot_Event ADD CONSTRAINT fk_coll_Event_req_Group FOREIGN KEY( Specimen_Coll_req_Group_Id) REFERENCES CatIssue_Speci_coll_Req_Group(IDENTIFIER);
/*Alter SPECIMEN adding col IS_COLL_PROT_REQ,COLLECTION_STATUS  */

alter table `catissue_specimen` ,add column `IS_COLL_PROT_REQ` tinyint (1)   NULL  after `CREATED_ON_DATE`, add column `COLLECTION_STATUS` varchar (50)   NULL  after `IS_COLL_PROT_REQ`;

			/* Populating table */
/* Inserting into CatIssue_Abs_Speci_coll_Group */

INSERT INTO CatIssue_Abs_Speci_coll_Group
           (Clinical_Diagnosis,
            Clinical_Status,
            Activity_Status,
            Site_Id,
            Collection_Protocol_Event_Id)
SELECT 'Not Specified',
       Clinical_Status,
       'Active',
       NULL,
       IdentIfier
FROM   CatIssue_coll_Prot_Event;
/* Inserting into CatIssue_Speci_coll_Req_Group  */

INSERT INTO CatIssue_Speci_coll_Req_Group
           (IdentIfier)
SELECT IdentIfier
FROM   CatIssue_Abs_Speci_coll_Group
WHERE  Name IS NULL ;
/* Updating catissue_coll_prot_event*/

UPDATE CatIssue_coll_Prot_Event a,CatIssue_Abs_Speci_coll_Group b
SET    a.Specimen_Coll_req_Group_Id = b.IdentIfier
WHERE  a.IdentIfier = b.Collection_Protocol_Event_Id
       AND b.Name IS NULL ;
/* Inserting catissue_specimen_char( REF#9999)  */


INSERT INTO CatIssue_Specimen_Char
           (Tissue_Site,
            Tissue_Side)
SELECT a.Tissue_Site,
       a.IdentIfier
FROM   CatIssue_Specimen_Requirement a
       JOIN CatIssue_Quantity b
         ON a.Quantity_Id = b.IdentIfier
       JOIN CatIssue_coll_Specimen_req c
         ON a.IdentIfier = c.Specimen_Requirement_Id
       LEFT JOIN CatIssue_coll_Prot_Event d
         ON c.Collection_Protocol_Event_Id = d.IdentIfier;
/* insert into specimen from specimen_requirement all related to CatIssue_coll_Specimen_req*/

INSERT INTO CatIssue_Specimen
           (Specimen_Class,
            TYPE,
            Label,
            Lineage,
            Pathological_Status,
            Available,
            Position_Dimension_One,
            Position_Dimension_Two,
            BarCode,
            Comments,
            Activity_Status,
            Parent_Specimen_Id,
            Storage_Container_IdentIfier,
            Specimen_Collection_Group_Id,
            Specimen_Characteristics_Id,
            Available_Quantity,
            Quantity,
            Concentration,
            Created_On_Date,
            Is_coll_Prot_req,
            Collection_Status)
SELECT a.Specimen_Class,
       a.Specimen_Type,
       NULL,
       'New',
       a.Pathology_Status,
       0,/*False for dummy*/
       2,
       NULL,
       NULL,
       NULL,
       'Active',
       NULL,
       NULL,
       e.IdentIfier,
       f.IdentIfier,
       b.Quantity,
       b.Quantity,
       NULL,
       NULL,
       1,/* Confirmed 1=true 0=false*/
       NULL
FROM   CatIssue_Specimen_Requirement a
       JOIN CatIssue_Quantity b
         ON a.Quantity_Id = b.IdentIfier
       JOIN CatIssue_coll_Specimen_req c
         ON a.IdentIfier = c.Specimen_Requirement_Id
       LEFT JOIN CatIssue_coll_Prot_Event d
         ON c.Collection_Protocol_Event_Id = d.IdentIfier
       LEFT JOIN CatIssue_Speci_coll_Req_Group e
         ON d.Specimen_Coll_req_Group_Id = e.IdentIfier
       LEFT JOIN CatIssue_Specimen_Char f
         ON a.IdentIfier = f.Tissue_Side;


/*Updating old actual specimen*/

update catissue_specimen set IS_COLL_PROT_REQ =0,COLLECTION_STATUS='Collected'  where IS_COLL_PROT_REQ is NULL;

/* Updating catissue_specimen_char with "Not Specified".See REF#9999 */
UPDATE CatIssue_Specimen_Char
SET    Tissue_Side = 'Not Specified'
WHERE  Tissue_Side > 0;



/*Entering into CSM table */

INSERT INTO `CSM_PROTECTION_ELEMENT` (PROTECTION_ELEMENT_ID,PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,
ATTRIBUTE,PROTECTION_ELEMENT_TYPE,APPLICATION_ID,UPDATE_DATE) select max(PROTECTION_ELEMENT_ID)+1,'edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup','edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup','edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup',NULL,NULL,1,'2007-01-17' from CSM_PROTECTION_ELEMENT;

INSERT INTO `CSM_PG_PE` SELECT MAX(PG_PE_ID) + 1,18,(SELECT PROTECTION_ELEMENT_ID
        FROM   CSM_PROTECTION_ELEMENT
        WHERE  PROTECTION_ELEMENT_NAME = 'edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup' limit 1),'2007-01-17' from CSM_PG_PE;


/* Droping obsolate tables catissue_clinical_report... */




/*Droping unwanted columns from CatIssue_Abs_Speci_coll_Group which comes from catissue_specimen_coll_group */

alter table CatIssue_Abs_Speci_coll_Group DROP FOREIGN KEY FKDEBAF1674CE21DDA;
alter table CatIssue_Abs_Speci_coll_Group DROP FOREIGN KEY FKDEBAF16753B01F66;
alter table CatIssue_Abs_Speci_coll_Group DROP FOREIGN KEY FKDEBAF1677E07C4AC;

alter table CatIssue_Abs_Speci_coll_Group drop column name;

alter table CatIssue_Abs_Speci_coll_Group drop column comments;
/*TODO*/

/*alter table CatIssue_Abs_Speci_coll_Group drop column SURGICAL_PATHOLOGY_NUMBER */

alter table Catissue_Abs_Speci_coll_Group drop column COLLECTION_PROTOCOL_EVENT_ID;

alter table CatIssue_Abs_Speci_coll_Group drop column COLLECTION_PROTOCOL_REG_ID;

alter table `CatIssue_Abs_Speci_coll_Group` drop column `CLINICAL_REPORT_ID`;
/*alter table `CatIssue_Abs_Speci_coll_Group` drop column `SURGICAL_PATHOLOGY_NUMBER`;*/

UPDATE CATISSUE_SPECIMEN_PROTOCOL SET ENROLLMENT=0 WHERE ENROLLMENT IS NULL;

/* For cp Enhancements */
alter table `catissue_collection_protocol` ,add column `CP_TYPE` varchar (50),add column `PARENT_CP_ID` bigint (20), add column `SEQUENCE_NUMBER` integer,add column `STUDY_CALENDAR_EVENT_POINT` double;
alter table CATISSUE_COLLECTION_PROTOCOL add index FK32DC439DBC7298B9 (PARENT_CP_ID), add constraint FK32DC439DBC7298B9 foreign key (PARENT_CP_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);

alter table `CATISSUE_COLL_PROT_REG` ,add column `DATE_OFFSET` integer;
alter table `catissue_specimen_coll_group` ,add column `DATE_OFFSET` integer;

INSERT INTO CSM_PROTECTION_ELEMENT
           (PROTECTION_ELEMENT_NAME,
            PROTECTION_ELEMENT_DESCRIPTION,
            OBJECT_ID,
            APPLICATION_ID)
VALUES     ('edu.wustl.catissuecore.action.SubCollectionProtocolRegistrationAction',
            'edu.wustl.catissuecore.action.SubCollectionProtocolRegistrationAction',
            'edu.wustl.catissuecore.action.SubCollectionProtocolRegistrationAction',
            1);

INSERT INTO CSM_PG_PE
           (PROTECTION_GROUP_ID,
            PROTECTION_ELEMENT_ID)
VALUES     (18,
            (SELECT PROTECTION_ELEMENT_ID
             FROM   CSM_PROTECTION_ELEMENT
             WHERE  OBJECT_ID = 'edu.wustl.catissuecore.action.SubCollectionProtocolRegistrationAction' limit 1));



/*CP Enhacements finish */

alter table CatIssue_Clinical_Report DROP FOREIGN KEY FK54A4264515246F7;
DROP TABLE CatIssue_coll_Specimen_req;

commit;



