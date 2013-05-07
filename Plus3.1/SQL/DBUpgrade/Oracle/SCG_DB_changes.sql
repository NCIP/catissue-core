
/*Alteration in parent entity 'catissue_specimen_coll_group' */
/* Renamed to parent */

ALTER TABLE CATISSUE_SPECIMEN_COLL_GROUP RENAME TO CATISSUE_ABS_SPECI_COLL_GROUP;


ALTER TABLE CATISSUE_ABS_SPECI_COLL_GROUP DROP CONSTRAINT FKDEBAF1677E07C4AC;

 

ALTER TABLE CATISSUE_ABS_SPECI_COLL_GROUP DROP CONSTRAINT FKDEBAF16753B01F66;

/* Creating catissue_specimen_coll_group a child of Abstract SCG*/

CREATE TABLE CATISSUE_SPECIMEN_COLL_GROUP (
  IdentIfier                 NUMBER(19,0) NOT NULL ,
  Name                       VARCHAR2(255)  ,
  Comments                   VARCHAR2(2000),
  Collection_Protocol_reg_Id NUMBER(19,0)  ,
  Surgical_Pathology_Number  VARCHAR2(50)  ,
  PRIMARY KEY( IdentIfier ),
  UNIQUE ( Name ),
  CONSTRAINT fkDebaf1677e07c4ac FOREIGN KEY( Collection_Protocol_reg_Id ) REFERENCES CATISSUE_COLL_PROT_REG( IdentIfier ),
  CONSTRAINT fk_Parent_spec_coll_Group FOREIGN KEY( IdentIfier ) REFERENCES CATISSUE_ABS_SPECI_COLL_GROUP( IdentIfier ))
;


ALTER TABLE CATISSUE_SPECIMEN_COLL_GROUP ADD Collection_Protocol_Event_Id NUMBER(19,0);

ALTER TABLE CATISSUE_SPECIMEN_COLL_GROUP ADD COLLECTION_STATUS VARCHAR2 (50); 

INSERT INTO CATISSUE_SPECIMEN_COLL_GROUP
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
FROM   CATISSUE_ABS_SPECI_COLL_GROUP a
       JOIN CATISSUE_CLINICAL_REPORT b
         ON a.Clinical_Report_Id = b.IdentIfier;


/* Creating child entities */

/*TODO*/

ALTER TABLE CATISSUE_SPECIMEN_COLL_GROUP ADD CONSTRAINT fk_CATISSUE_COLL_PROT_EVENT FOREIGN KEY( Collection_Protocol_Event_Id) REFERENCES CATISSUE_COLL_PROT_EVENT (IDENTIFIER);

CREATE TABLE CATISSUE_SPECI_COLL_REQ_GROUP (
  IdentIfier NUMBER(19,0) NOT NULL ,
  PRIMARY KEY( IdentIfier ),
  CONSTRAINT fk_Abs_Speci_coll_Group FOREIGN KEY( IdentIfier ) REFERENCES CATISSUE_ABS_SPECI_COLL_GROUP( IdentIfier ))
;
/* Alteration in catissue_coll_prot_event */

ALTER TABLE CATISSUE_COLL_PROT_EVENT ADD  Specimen_Coll_req_Group_Id NUMBER(19,0);

ALTER TABLE CATISSUE_COLL_PROT_EVENT ADD CONSTRAINT fk_coll_Event_req_Group FOREIGN KEY( Specimen_Coll_req_Group_Id) REFERENCES CATISSUE_SPECI_COLL_REQ_GROUP(IDENTIFIER);
/* Alter SPECIMEN adding col IS_COLL_PROT_REQ,COLLECTION_STATUS  */

ALTER TABLE CATISSUE_SPECIMEN ADD  (IS_COLL_PROT_REQ NUMBER(1,0)  ,COLLECTION_STATUS VARCHAR (50)  );

			/* Populating table */
/* Inserting into catissue_abstract_specimen_coll_group */

INSERT INTO CATISSUE_ABS_SPECI_COLL_GROUP
           (identifier,
		   Clinical_Diagnosis,
            Clinical_Status,
            Activity_Status,
            Site_Id,
            Collection_Protocol_Event_Id)
SELECT CATISSUE_SPECIMEN_COLL_GRP_SEQ.NEXTVAL ,
       'Not Specified',
       Clinical_Status,
       'Active',
       NULL,
       IdentIfier
FROM   CATISSUE_COLL_PROT_EVENT;
/* Inserting into catissue_specimen_coll_requirement_group */

INSERT INTO CATISSUE_SPECI_COLL_REQ_GROUP
           (IdentIfier)
SELECT IdentIfier
FROM   CATISSUE_ABS_SPECI_COLL_GROUP
WHERE  Name IS NULL ;
/* Updating catissue_coll_prot_event */

UPDATE CATISSUE_COLL_PROT_EVENT a SET Specimen_Coll_req_Group_Id=(SELECT IdentIfier FROM CATISSUE_ABS_SPECI_COLL_GROUP b WHERE  a.IdentIfier = b.Collection_Protocol_Event_Id
       AND b.Name IS NULL);

/* Inserting catissue_specimen_char( REF#9999)  */


INSERT INTO CATISSUE_SPECIMEN_CHAR
           (identifier,
		   Tissue_Site,
            Tissue_Side)
SELECT CATISSUE_SPECIMEN_CHAR_SEQ.NEXTVAL,
      a.Tissue_Site,
       'S.'||a.IdentIfier
FROM   CATISSUE_SPECIMEN_REQUIREMENT a
       JOIN CATISSUE_QUANTITY b
         ON a.Quantity_Id = b.IdentIfier
       JOIN CATISSUE_COLL_SPECIMEN_REQ c
         ON a.IdentIfier = c.Specimen_Requirement_Id
       LEFT JOIN CATISSUE_COLL_PROT_EVENT d
         ON c.Collection_Protocol_Event_Id = d.IdentIfier;
/* insert into specimen from specimen_requirement all related to CatIssue_coll_Specimen_req*/

INSERT INTO CATISSUE_SPECIMEN
           (identifier,
		   Specimen_Class,
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
SELECT CATISSUE_SPECIMEN_SEQ.NEXTVAL,
      a.Specimen_Class,
       a.Specimen_Type,
       NULL,
       'New',
       a.Pathology_Status,
       1,/*BUG 5745*/
       1,/*Bug related to auto*/
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
FROM   CATISSUE_SPECIMEN_REQUIREMENT a
       JOIN CATISSUE_QUANTITY b
         ON a.Quantity_Id = b.IdentIfier
       JOIN CATISSUE_COLL_SPECIMEN_REQ c
         ON a.IdentIfier = c.Specimen_Requirement_Id
       LEFT JOIN CATISSUE_COLL_PROT_EVENT d
         ON c.Collection_Protocol_Event_Id = d.IdentIfier
       LEFT JOIN CATISSUE_SPECI_COLL_REQ_GROUP e
         ON d.Specimen_Coll_req_Group_Id = e.IdentIfier
       LEFT JOIN CATISSUE_SPECIMEN_CHAR f
         ON 'S.'||a.IdentIfier = f.Tissue_Side;


/*Updating old actual specimen*/

UPDATE CATISSUE_SPECIMEN SET IS_COLL_PROT_REQ =0,COLLECTION_STATUS='Collected'  WHERE IS_COLL_PROT_REQ IS NULL;
/*TODO Verified above*/
/* Updating catissue_specimen_char with "Not Specified".See REF#9999 */
UPDATE CATISSUE_SPECIMEN_CHAR
SET    Tissue_Side = 'Not Specified'
WHERE  Tissue_Side LIKE 'S.%';

/*SELECT count(*) from CATISSUE_SPECIMEN_PROTOCOL where ENROLLMENT IS NULL;*/
UPDATE CATISSUE_SPECIMEN_PROTOCOL SET ENROLLMENT = 0 WHERE ENROLLMENT IS NULL;

/*Deleting catissue_specimen_requirement rec those inserted in specimen table TODO*/
ALTER TABLE CATISSUE_SPECIMEN_REQUIREMENT DROP constraint FK39AFE96861A1C94F;
ALTER TABLE CATISSUE_COLL_SPECIMEN_REQ DROP constraint FK860E6ABEBE10F0CE;
/*TODO*/
DELETE FROM CATISSUE_QUANTITY WHERE identifier IN (SELECT quantity_id FROM CATISSUE_SPECIMEN_REQUIREMENT a join CATISSUE_COLL_SPECIMEN_REQ b ON a.IDENTIFIER=b.specimen_requirement_id);
 

DELETE FROM  CATISSUE_SPECIMEN_REQUIREMENT WHERE identifier IN (SELECT identifier FROM CATISSUE_SPECIMEN_REQUIREMENT a join CATISSUE_COLL_SPECIMEN_REQ b ON a.IDENTIFIER=b.specimen_requirement_id);

ALTER TABLE CATISSUE_SPECIMEN_REQUIREMENT ADD constraint FK39AFE96861A1C94F foreign key (QUANTITY_ID) REFERENCES CATISSUE_QUANTITY (IDENTIFIER)  ;

/*Entering into CSM table */

INSERT INTO CSM_PROTECTION_ELEMENT
SELECT CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup','edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup','edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup',NULL,NULL,1,TO_DATE('2007-01-17','yyyy-mm-dd') FROM dual;

INSERT INTO CSM_PG_PE SELECT CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,18,(SELECT PROTECTION_ELEMENT_ID
        FROM   CSM_PROTECTION_ELEMENT
        WHERE  PROTECTION_ELEMENT_NAME = 'edu.wustl.catissuecore.domain.SpecimenCollectionRequirementGroup' AND ROWNUM<2),TO_DATE('2007-01-17','yyyy-mm-dd') FROM dual;


/* Droping obsolate tables catissue_clinical_report... */
ALTER TABLE CATISSUE_CLINICAL_REPORT DROP constraint FK54A4264515246F7;
ALTER TABLE CATISSUE_ABS_SPECI_COLL_GROUP DROP constraint FKDEBAF1674CE21DDA;
DROP TABLE CATISSUE_CLINICAL_REPORT;

DROP TABLE CATISSUE_COLL_SPECIMEN_REQ;

/*Droping unwanted columns from catissue_abstract_specimen_coll_group which comes from catissue_specimen_coll_group */
ALTER TABLE CATISSUE_ABS_SPECI_COLL_GROUP DROP COLUMN name;

ALTER TABLE CATISSUE_ABS_SPECI_COLL_GROUP DROP COLUMN comments;
/*TODO*/

/*alter table catissue_abstract_specimen_coll_group drop column SURGICAL_PATHOLOGY_NUMBER */

ALTER TABLE CATISSUE_ABS_SPECI_COLL_GROUP DROP COLUMN COLLECTION_PROTOCOL_EVENT_ID;

ALTER TABLE CATISSUE_ABS_SPECI_COLL_GROUP DROP COLUMN COLLECTION_PROTOCOL_REG_ID;

ALTER TABLE CATISSUE_ABS_SPECI_COLL_GROUP DROP COLUMN CLINICAL_REPORT_ID;

/*ALTER TABLE CATISSUE_ABS_SPECI_COLL_GROUP DROP COLUMN SURGICAL_PATHOLOGY_NUMBER; */

/* for CP Enhancements */
ALTER TABLE catissue_collection_protocol 
ADD ( CP_TYPE varchar(50),PARENT_CP_ID number(19,0) default NULL,SEQUENCE_NUMBER integer, STUDY_CALENDAR_EVENT_POINT DOUBLE PRECISION default NULL);

alter table CATISSUE_COLLECTION_PROTOCOL  add constraint FK32DC439DBC7298B9 foreign key (PARENT_CP_ID) references CATISSUE_COLLECTION_PROTOCOL;

ALTER TABLE CATISSUE_COLL_PROT_REG 
ADD ( DATE_OFFSET integer);

ALTER TABLE CATISSUE_SPECIMEN_COLL_GROUP 
ADD ( DATE_OFFSET integer);

INSERT into CSM_PROTECTION_ELEMENT 
values(CSM_PROTECTIO_PROTECTION_E_SEQ.NEXTVAL,'edu.wustl.catissuecore.action.SubCollectionProtocolRegistrationAction','edu.wustl.catissuecore.action.SubCollectionProtocolRegistrationAction','edu.wustl.catissuecore.action.SubCollectionProtocolRegistrationAction',NULL,NULL,1,to_date('2007-01-04','yyyy-mm-dd'));
INSERT INTO CSM_PG_PE select CSM_PG_PE_PG_PE_ID_SEQ.NEXTVAL,18,(select PROTECTION_ELEMENT_ID from csm_protection_element where PROTECTION_ELEMENT_NAME='edu.wustl.catissuecore.action.SubCollectionProtocolRegistrationAction'),to_date('2007-01-04','yyyy-mm-dd') from dual;


/* CP Enhancements end */

commit;
