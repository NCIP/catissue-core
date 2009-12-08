SET storage_engine=InnoDB;
SET FOREIGN_KEY_CHECKS=0;
drop table if exists CATISSUE_PERMISSIBLE_VALUE;
drop table if exists CATISSUE_CDE;
drop table if exists CATISSUE_COLL_COORDINATORS;
drop table if exists CATISSUE_CANCER_RESEARCH_GROUP;
drop table if exists CATISSUE_COLLECTION_PROTOCOL;
drop table if exists CATISSUE_EVENT_PARAM;
drop table if exists CATISSUE_TRANSFER_EVENT_PARAM;
drop table if exists CATISSUE_STOR_CONT_SPEC_CLASS;
drop table if exists CATISSUE_COLL_EVENT_PARAM;
drop table if exists CATISSUE_PASSWORD;
drop table if exists CATISSUE_SPECIMEN_BIOHZ_REL;
drop table if exists CATISSUE_MOL_SPE_REVIEW_PARAM;
drop table if exists CATISSUE_STOR_TYPE_HOLDS_TYPE;;
drop table if exists CATISSUE_STORAGE_TYPE;
drop table if exists CATISSUE_CONTAINER;
drop table if exists CATISSUE_DISTRIBUTION_SPEC_REQ;
drop table if exists CATISSUE_SITE;
drop table if exists CATISSUE_EMBEDDED_EVENT_PARAM;
drop table if exists CATISSUE_IN_OUT_EVENT_PARAM;
drop table if exists CATISSUE_COLL_DISTRIBUTION_REL;
drop table if exists CATISSUE_DISPOSAL_EVENT_PARAM;
drop table if exists CATISSUE_SPECIMEN_REQUIREMENT;
drop table if exists CATISSUE_INSTITUTION;
drop table if exists CATISSUE_ST_CONT_ST_TYPE_REL;
drop table if exists CATISSUE_SPECIMEN_PROTOCOL;
drop table if exists CATISSUE_FLUID_SPE_EVENT_PARAM;
drop table if exists CATISSUE_BIOHAZARD;
drop table if exists CATISSUE_QUANTITY;
drop table if exists CATISSUE_SPUN_EVENT_PARAMETERS;
drop table if exists CATISSUE_RECEIVED_EVENT_PARAM;
drop table if exists CATISSUE_RACE;
drop table if exists CATISSUE_SPECI_ARRAY_CONTENT;
drop table if exists CATISSUE_ADDRESS;
drop table if exists CATISSUE_REPORTED_PROBLEM;
drop table if exists CATISSUE_SPECIMEN_ARRAY;
drop table if exists CATISSUE_SPECIMEN_ARRAY_TYPE;
drop table if exists CATISSUE_DISTRIBUTED_ITEM;
drop table if exists CATISSUE_PARTICIPANT;
drop table if exists CATISSUE_SPECIMEN_CHAR;
drop table if exists CATISSUE_SPECIMEN_EVENT_PARAM;
drop table if exists CATISSUE_STOR_TYPE_SPEC_CLASS;
drop table if exists CATISSUE_COLL_PROT_EVENT;
drop table if exists CATISSUE_CONTAINER_TYPE;
drop table if exists CATISSUE_CAPACITY;
drop table if exists CATISSUE_PART_MEDICAL_ID;
drop table if exists CATISSUE_STORAGE_CONTAINER;
drop table if exists CATISSUE_CELL_SPE_REVIEW_PARAM;
drop table if exists CATISSUE_DISTRIBUTION;
drop table if exists CATISSUE_PROCEDURE_EVENT_PARAM;
drop table if exists CATISSUE_DISTRIBUTION_PROTOCOL;
drop table if exists CATISSUE_EXTERNAL_IDENTIFIER;
drop table if exists CATISSUE_DEPARTMENT;
drop table if exists CATISSUE_FIXED_EVENT_PARAM;
drop table if exists CATISSUE_THAW_EVENT_PARAMETERS;
drop table if exists CATISSUE_COLL_PROT_REG;
drop table if exists CATISSUE_FROZEN_EVENT_PARAM;
drop table if exists CATISSUE_USER;
drop table if exists CATISSUE_TIS_SPE_EVENT_PARAM;
drop table if exists CATISSUE_ST_CONT_COLL_PROT_REL;
drop table if exists CATISSUE_STORTY_HOLDS_SPARRTY;
drop table if exists CATISSUE_CONT_HOLDS_SPARRTYPE;
drop table if exists CATISSUE_ABS_SPECI_COLL_GROUP;
drop table if exists CATISSUE_SPECI_COLL_REQ_GROUP;
drop table if exists CATISSUE_SPECIMEN_COLL_GROUP;
drop table if exists CATISSUE_SPECIMEN_TYPE;
drop table if exists CATISSUE_AUDIT_EVENT_QUERY_LOG;
drop table if exists CATISSUE_AUDIT_EVENT;
drop table if exists CATISSUE_AUDIT_EVENT_LOG;
drop table if exists CATISSUE_AUDIT_EVENT_DETAILS;
drop table if exists CATISSUE_SPECIMEN_LABEL_COUNT;

#--caTies
drop table if exists CATISSUE_REPORT_TEXTCONTENT;
drop table if exists CATISSUE_IDENTIFIED_REPORT;
drop table if exists CATISSUE_CONCEPT_REFERENT;
drop table if exists CATISSUE_REPORT_CONTENT;
drop table if exists CATISSUE_REVIEW_PARAMS;
drop table if exists CATISSUE_REPORT_BICONTENT;
drop table if exists CATISSUE_REPORT_SECTION;
drop table if exists CATISSUE_DEIDENTIFIED_REPORT;
drop table if exists CATISSUE_QUARANTINE_PARAMS;
drop table if exists CATISSUE_PATHOLOGY_REPORT;
drop table if exists CATISSUE_REPORT_XMLCONTENT;
drop table if exists CATISSUE_REPORT_QUEUE;
drop table if exists CATISSUE_REPORT_PARTICIP_REL;
drop table if exists CATISSUE_CONCEPT;
drop table if exists CATISSUE_SEMANTIC_TYPE;
drop table if exists CATISSUE_CONCEPT_CLASSIFICATN;


#------ Consent Tracking related drop, create and add foreign key scripts.
drop table if exists CATISSUE_CONSENT_TIER_RESPONSE;
drop table if exists CATISSUE_CONSENT_TIER_STATUS;
drop table if exists CATISSUE_CONSENT_TIER;

#---- Specimen Order
drop table if exists CATISSUE_PATH_CASE_ORDER_ITEM;
drop table if exists CATISSUE_DERIEVED_SP_ORD_ITEM;
drop table if exists CATISSUE_NEW_SPEC_ORD_ITEM;
drop table if exists CATISSUE_EXISTING_SP_ORD_ITEM;
drop table if exists CATISSUE_SPECIMEN_ORDER_ITEM;

#---- Specimen Array Order
drop table if exists CATISSUE_NEW_SP_AR_ORDER_ITEM;
drop table if exists CATISSUE_EXIST_SP_AR_ORD_ITEM;
drop table if exists CATISSUE_SP_ARRAY_ORDER_ITEM;

drop table if exists CATISSUE_ORDER_ITEM;
drop table if exists CATISSUE_ORDER;

#--Specimen Model change
drop table if exists CATISSUE_ABSTRACT_SPECIMEN;
drop table if exists CATISSUE_SPECIMEN;
drop table if exists CATISSUE_CP_REQ_SPECIMEN;
drop table if exists catissue_fluid_req_specimen;
drop table if exists catissue_fluid_specimen;

drop table if exists catissue_cell_specimen;
drop table if exists catissue_cell_req_specimen;

drop table if exists catissue_tissue_specimen;
drop table if exists catissue_tissue_req_specimen;

drop table if exists catissue_mol_req_specimen;
drop table if exists catissue_molecular_specimen;
drop table if exists catissue_catissue_bulk_operation;
drop table if exists catissue_stor_type_spec_type;
drop table if exists catissue_stor_cont_spec_type;

SET FOREIGN_KEY_CHECKS=1;

create table CATISSUE_PERMISSIBLE_VALUE (
   IDENTIFIER bigint not null auto_increment,
   CONCEPT_CODE varchar(40),
   DEFINITION text,
   PARENT_IDENTIFIER bigint,
   VALUE varchar(225),
   PUBLIC_ID varchar(30),
   primary key (IDENTIFIER)
);
create table CATISSUE_CDE (
   PUBLIC_ID varchar(30) not null,
   LONG_NAME varchar(200),
   DEFINITION text,
   VERSION varchar(50),
   LAST_UPDATED date,
   primary key (PUBLIC_ID)
);
create table CATISSUE_AUDIT_EVENT (
   IDENTIFIER bigint not null auto_increment,
   IP_ADDRESS varchar(20),
   EVENT_TIMESTAMP datetime,
   USER_ID bigint,
   COMMENTS text,
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT_LOG (
   IDENTIFIER bigint not null auto_increment,
   OBJECT_IDENTIFIER bigint,
   OBJECT_NAME varchar(50),
   EVENT_TYPE varchar(50),
   AUDIT_EVENT_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT_DETAILS (
   IDENTIFIER bigint not null auto_increment,
   ELEMENT_NAME varchar(150),
   PREVIOUS_VALUE varchar(150),
   CURRENT_VALUE varchar(500),
   AUDIT_EVENT_LOG_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_COORDINATORS (
   COLLECTION_PROTOCOL_ID bigint not null,
   USER_ID bigint not null,
   primary key (COLLECTION_PROTOCOL_ID, USER_ID)
);
create table CATISSUE_CANCER_RESEARCH_GROUP (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255) not null unique,
   primary key (IDENTIFIER)
);
create table CATISSUE_COLLECTION_PROTOCOL (
   IDENTIFIER bigint not null,
   UNSIGNED_CONSENT_DOC_URL text,
   ALIQUOT_IN_SAME_CONTAINER bit,
   CONSENTS_WAIVED bit,
   CP_TYPE varchar(50) default NULL,
   PARENT_CP_ID bigint(20) default NULL,
   SEQUENCE_NUMBER integer,	
   STUDY_CALENDAR_EVENT_POINT double default NULL,
   primary key (IDENTIFIER)
);

create table CATISSUE_EVENT_PARAM (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_TRANSFER_EVENT_PARAM (
   IDENTIFIER bigint not null,
   FROM_POSITION_DIMENSION_ONE integer,
   FROM_POSITION_DIMENSION_TWO integer,
   TO_POSITION_DIMENSION_ONE integer,
   TO_POSITION_DIMENSION_TWO integer,
   TO_STORAGE_CONTAINER_ID bigint,
   FROM_STORAGE_CONTAINER_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_STOR_CONT_SPEC_CLASS (
   STORAGE_CONTAINER_ID bigint not null,
   SPECIMEN_CLASS varchar(50)
);
create table CATISSUE_COLL_EVENT_PARAM (
   IDENTIFIER bigint not null,
   COLLECTION_PROCEDURE varchar(50),
   CONTAINER varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_PASSWORD (
   IDENTIFIER bigint not null auto_increment,
   PASSWORD varchar(255),
   UPDATE_DATE date,
   USER_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_BIOHZ_REL (
   BIOHAZARD_ID bigint not null,
   SPECIMEN_ID bigint not null,
   primary key (SPECIMEN_ID, BIOHAZARD_ID)
);
create table CATISSUE_MOL_SPE_REVIEW_PARAM (
   IDENTIFIER bigint not null,
   GEL_IMAGE_URL varchar(255),
   QUALITY_INDEX varchar(50),
   LANE_NUMBER varchar(50),
   GEL_NUMBER integer,
   ABSORBANCE_AT_260 double precision,
   ABSORBANCE_AT_280 double precision,
   RATIO_28S_TO_18S double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_STORAGE_TYPE (
   IDENTIFIER BIGINT not null,
   DEFAULT_TEMPERATURE DOUBLE PRECISION,
   primary key (IDENTIFIER)
);

create table CATISSUE_CONTAINER (
   IDENTIFIER bigint not null auto_increment,
   ACTIVITY_STATUS varchar(50),
   BARCODE varchar(255) unique,
   CAPACITY_ID bigint,
   COMMENTS text,
   CONT_FULL bit,
   NAME varchar(255) unique not null, 
   primary key (IDENTIFIER)
);
create table CATISSUE_SITE (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255) not null unique,
   TYPE varchar(50),
   EMAIL_ADDRESS varchar(255),
   USER_ID bigint,
   ACTIVITY_STATUS varchar(50),
   ADDRESS_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_EMBEDDED_EVENT_PARAM (
   IDENTIFIER bigint not null,
   EMBEDDING_MEDIUM varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_IN_OUT_EVENT_PARAM (
   IDENTIFIER bigint not null,
   STORAGE_STATUS varchar(100) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_DISTRIBUTION_REL (
   COLLECTION_PROTOCOL_ID bigint not null,
   DISTRIBUTION_PROTOCOL_ID bigint not null,
   primary key (COLLECTION_PROTOCOL_ID, DISTRIBUTION_PROTOCOL_ID)
);
create table CATISSUE_DISPOSAL_EVENT_PARAM (
   IDENTIFIER bigint not null,
   REASON varchar(255),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_REQUIREMENT (
   IDENTIFIER bigint not null auto_increment,
   SPECIMEN_CLASS varchar(255) not null,
   SPECIMEN_TYPE varchar(50),
   TISSUE_SITE varchar(150),
   PATHOLOGY_STATUS varchar(50),
   QUANTITY_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_INSTITUTION (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255) not null unique,
   primary key (IDENTIFIER)
);
create table CATISSUE_ST_CONT_ST_TYPE_REL (
   STORAGE_CONTAINER_ID bigint not null,
   STORAGE_TYPE_ID bigint not null,
   primary key (STORAGE_CONTAINER_ID, STORAGE_TYPE_ID)
);
create table CATISSUE_STOR_TYPE_HOLDS_TYPE (
   STORAGE_TYPE_ID BIGINT not null,
   HOLDS_STORAGE_TYPE_ID BIGINT not null,
   primary key (STORAGE_TYPE_ID, HOLDS_STORAGE_TYPE_ID)
);

create table CATISSUE_STORTY_HOLDS_SPARRTY (
   STORAGE_TYPE_ID BIGINT not null,
   SPECIMEN_ARRAY_TYPE_ID BIGINT not null,
   primary key (STORAGE_TYPE_ID, SPECIMEN_ARRAY_TYPE_ID)
);

create table CATISSUE_CONT_HOLDS_SPARRTYPE (
   STORAGE_CONTAINER_ID BIGINT not null,
   SPECIMEN_ARRAY_TYPE_ID BIGINT not null,
   primary key (STORAGE_CONTAINER_ID, SPECIMEN_ARRAY_TYPE_ID)
);

create table CATISSUE_SPECIMEN_PROTOCOL (
   IDENTIFIER bigint not null auto_increment,
   PRINCIPAL_INVESTIGATOR_ID bigint,
   TITLE varchar(255) not null unique,
   SHORT_TITLE varchar(255),
   IRB_IDENTIFIER varchar(255),
   START_DATE date,
   END_DATE date,
   ENROLLMENT integer,
   DESCRIPTION_URL varchar(255),
   ACTIVITY_STATUS varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_FLUID_SPE_EVENT_PARAM (
   IDENTIFIER bigint not null,
   CELL_COUNT double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_BIOHAZARD (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255) not null unique,
   COMMENTS text,
   TYPE varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_QUANTITY (
   IDENTIFIER bigint not null auto_increment,
   QUANTITY double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_SPUN_EVENT_PARAMETERS (
   IDENTIFIER bigint not null,
   GFORCE double precision,
   DURATION_IN_MINUTES integer,
   primary key (IDENTIFIER)
);
create table CATISSUE_RECEIVED_EVENT_PARAM (
   IDENTIFIER bigint not null,
   RECEIVED_QUALITY varchar(255),
   primary key (IDENTIFIER)
);

create table CATISSUE_RACE (
   IDENTIFIER bigint not null auto_increment,
   RACE_NAME varchar(50),
   PARTICIPANT_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_SPECI_ARRAY_CONTENT (
   IDENTIFIER bigint not null auto_increment,
   CONC_IN_MICROGM_PER_MICROLTR double precision,
   INITIAL_QUANTITY double precision,
   POSITION_DIMENSION_ONE integer,
   POSITION_DIMENSION_TWO integer,
   SPECIMEN_ID bigint,
   SPECIMEN_ARRAY_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_ADDRESS (
   IDENTIFIER bigint not null auto_increment,
   STREET varchar(255),
   CITY varchar(50),
   STATE varchar(50),
   COUNTRY varchar(50),
   ZIPCODE varchar(30),
   PHONE_NUMBER varchar(50),
   FAX_NUMBER varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORTED_PROBLEM (
   IDENTIFIER bigint not null auto_increment,
   AFFILIATION varchar(255) not null,
   NAME_OF_REPORTER varchar(255) not null,
   REPORTERS_EMAIL_ID varchar(255) not null,
   MESSAGE_BODY varchar(500) not null,
   SUBJECT varchar(255),
   REPORTED_DATE date,
   ACTIVITY_STATUS varchar(100),
   COMMENTS text,
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_ARRAY (
   IDENTIFIER bigint not null,
   CREATED_BY_ID bigint,
   SPECIMEN_ARRAY_TYPE_ID bigint,
   DISTRIBUTION_ID bigint,
   AVAILABLE bit,
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_ARRAY_TYPE (
   IDENTIFIER bigint not null,
   SPECIMEN_CLASS varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTED_ITEM (
   IDENTIFIER bigint not null auto_increment,
   QUANTITY double precision,
   SPECIMEN_ID bigint,
   DISTRIBUTION_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_PARTICIPANT (
   IDENTIFIER bigint not null auto_increment,
   LAST_NAME varchar(255),
   FIRST_NAME varchar(255),
   MIDDLE_NAME varchar(255),
   BIRTH_DATE date,
   EMPI_ID varchar(50),
   GENDER varchar(20),
   GENOTYPE varchar(50),
   ETHNICITY varchar(50),
   SOCIAL_SECURITY_NUMBER varchar(50) unique,
   ACTIVITY_STATUS varchar(50),
   DEATH_DATE date,
   VITAL_STATUS varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_CHAR (
   IDENTIFIER bigint not null auto_increment,
   TISSUE_SITE varchar(150),
   TISSUE_SIDE varchar(50),
   primary key (IDENTIFIER)
);
#-- Ashish Gupta Bug id 2741---Added Association between SCG and Events
create table CATISSUE_SPECIMEN_EVENT_PARAM (
   IDENTIFIER bigint not null auto_increment,
   SPECIMEN_ID bigint,
   SPECIMEN_COLL_GRP_ID bigint,
   EVENT_TIMESTAMP datetime,
   USER_ID bigint,
   COMMENTS text,
   primary key (IDENTIFIER)
);
create table CATISSUE_STOR_TYPE_SPEC_CLASS (
   STORAGE_TYPE_ID bigint not null,
   SPECIMEN_CLASS varchar(50)
);



create table CATISSUE_COLL_PROT_REG (
   IDENTIFIER bigint not null auto_increment,
   PROTOCOL_PARTICIPANT_ID varchar(255),
   REGISTRATION_DATE date,
   PARTICIPANT_ID bigint,
   COLLECTION_PROTOCOL_ID bigint,
   ACTIVITY_STATUS varchar(50),
   CONSENT_SIGN_DATE datetime,
   CONSENT_DOC_URL text,
   CONSENT_WITNESS bigint,
   BARCODE varchar(255) unique,
   DATE_OFFSET integer,	
   primary key (IDENTIFIER)
);

CREATE TABLE `catissue_abs_speci_coll_group` 
(                                               
	 `IDENTIFIER` bigint(20) NOT NULL auto_increment,   
	 `CLINICAL_DIAGNOSIS` varchar(150) default NULL,                                                    
	 `CLINICAL_STATUS` varchar(50) default NULL,                                                        
	 `ACTIVITY_STATUS` varchar(50) default NULL,                                                        
	 `SITE_ID` bigint(20) default NULL,                                                                 
	 PRIMARY KEY  (`IDENTIFIER`),
	 KEY `FKDEBAF167A7F77D13` (`SITE_ID`),                                                              
	 CONSTRAINT `FKDEBAF167A7F77D13` FOREIGN KEY (`SITE_ID`) REFERENCES `catissue_site` (`IDENTIFIER`)  
);

CREATE TABLE `catissue_coll_prot_event`
(                                                                                                                     
    `IDENTIFIER` bigint(20) NOT NULL auto_increment,                                                                                                            
    `CLINICAL_STATUS` varchar(50) default NULL,                                                                                                                 
    `COLLECTION_POINT_LABEL` varchar(255) default NULL,                                                                                                         
    `STUDY_CALENDAR_EVENT_POINT` double default NULL,                                                                                                           
    `COLLECTION_PROTOCOL_ID` bigint(20) default NULL,
    `LABELFORMAT` varchar(255) default NULL,                                                                                                           
    PRIMARY KEY  (`IDENTIFIER`),                                                                                                                                
    UNIQUE KEY `COLLECTION_PROTOCOL_ID` (`COLLECTION_PROTOCOL_ID`,`COLLECTION_POINT_LABEL`),                                                                    
    KEY `FK7AE7715948304401` (`COLLECTION_PROTOCOL_ID`),                                                                                                        
    CONSTRAINT `FK7AE7715948304401` FOREIGN KEY (`COLLECTION_PROTOCOL_ID`) REFERENCES `catissue_collection_protocol` (`IDENTIFIER`),
    CONSTRAINT `FK_PARENT_COLL_PROT_EVENT` FOREIGN KEY (`IDENTIFIER`) REFERENCES `catissue_abs_speci_coll_group` (`IDENTIFIER`)
);

CREATE TABLE `catissue_specimen_coll_group` 
(                                                                                                         
	`IDENTIFIER` bigint(20) NOT NULL auto_increment,        
	`NAME` varchar(255) default NULL, 
	`BARCODE` varchar(255) default NULL, 
	`COMMENTS` text,
	`ENCOUNTER_TIMESTAMP` datetime,
	`COLLECTION_PROTOCOL_REG_ID` bigint(20) default NULL,                                                                                               
	`SURGICAL_PATHOLOGY_NUMBER` varchar(50) default NULL,                                                                                               
	`COLLECTION_PROTOCOL_EVENT_ID` bigint(20) default NULL,
	`COLLECTION_STATUS` varchar(50),
    `DATE_OFFSET` integer,                                                                                           
	PRIMARY KEY  (`IDENTIFIER`),
    UNIQUE KEY `NAME` (`NAME`),
	UNIQUE KEY `BARCODE` (`BARCODE`),
	KEY `FKDEBAF1677E07C4AC` (`COLLECTION_PROTOCOL_REG_ID`),                                                                                            
	KEY `FK_COLL_PROT_EVENT_SPEC_COLL_GROUP` (`COLLECTION_PROTOCOL_EVENT_ID`),                                                                          
	CONSTRAINT `FK_COLL_PROT_EVENT_SPEC_COLL_GROUP` FOREIGN KEY (`COLLECTION_PROTOCOL_EVENT_ID`) REFERENCES `catissue_coll_prot_event` (`IDENTIFIER`),  
	CONSTRAINT `FKDEBAF1677E07C4AC` FOREIGN KEY (`COLLECTION_PROTOCOL_REG_ID`) REFERENCES `catissue_coll_prot_reg` (`IDENTIFIER`),                      
	CONSTRAINT `FK_PARENT_SPEC_COLL_GROUP` FOREIGN KEY (`IDENTIFIER`) REFERENCES `catissue_abs_speci_coll_group` (`IDENTIFIER`)                 
);

create table CATISSUE_CONTAINER_TYPE (
   IDENTIFIER bigint not null auto_increment,
   CAPACITY_ID bigint,
   NAME varchar(255) unique,
   ONE_DIMENSION_LABEL varchar(255),
   TWO_DIMENSION_LABEL varchar(255),
   COMMENTS text,
   ACTIVITY_STATUS VARCHAR(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_CAPACITY (
   IDENTIFIER bigint not null auto_increment,
   ONE_DIMENSION_CAPACITY integer,
   TWO_DIMENSION_CAPACITY integer,
   primary key (IDENTIFIER)
);
create table CATISSUE_PART_MEDICAL_ID (
   IDENTIFIER bigint not null auto_increment,
   MEDICAL_RECORD_NUMBER varchar(255),
   SITE_ID bigint,
   PARTICIPANT_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_STORAGE_CONTAINER (
   IDENTIFIER bigint not null,
   SITE_ID bigint,
   TEMPERATURE double precision,
   STORAGE_TYPE_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_CELL_SPE_REVIEW_PARAM (
   IDENTIFIER bigint not null,
   NEOPLASTIC_CELLULARITY_PER double precision,
   VIABLE_CELL_PERCENTAGE double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTION (
   IDENTIFIER bigint not null auto_increment,
   TO_SITE_ID bigint,
   DISTRIBUTION_PROTOCOL_ID bigint,
   ACTIVITY_STATUS varchar(50),
   SPECIMEN_ID bigint,
   EVENT_TIMESTAMP datetime,
   USER_ID bigint,
   COMMENTS text,
   primary key (IDENTIFIER)
);
create table CATISSUE_PROCEDURE_EVENT_PARAM (
   IDENTIFIER bigint not null,
   URL varchar(255) not null,
   NAME varchar(255) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTION_PROTOCOL (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTION_SPEC_REQ (
   IDENTIFIER bigint not null auto_increment,
   SPECIMEN_CLASS varchar(255) not null,
   SPECIMEN_TYPE varchar(50),
   TISSUE_SITE varchar(150),
   PATHOLOGY_STATUS varchar(50),
   QUANTITY double precision,
   DISTRIBUTION_PROTOCOL_ID bigint,
   CONSTRAINT `fk_distribution_protocol` FOREIGN KEY (`DISTRIBUTION_PROTOCOL_ID`) REFERENCES `CATISSUE_DISTRIBUTION_PROTOCOL` (`IDENTIFIER`),
   primary key (IDENTIFIER)
);
create table CATISSUE_EXTERNAL_IDENTIFIER (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255),
   VALUE varchar(255),
   SPECIMEN_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_DEPARTMENT (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255) not null unique,
   primary key (IDENTIFIER)
);
create table CATISSUE_FIXED_EVENT_PARAM (
   IDENTIFIER bigint not null,
   FIXATION_TYPE varchar(50) not null,
   DURATION_IN_MINUTES integer,
   primary key (IDENTIFIER)
);
create table CATISSUE_THAW_EVENT_PARAMETERS (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);

create table CATISSUE_FROZEN_EVENT_PARAM (
   IDENTIFIER bigint not null,
   METHOD varchar(50),
   primary key (IDENTIFIER)
);

#-- Specimen Model Change

CREATE TABLE `CATISSUE_ABSTRACT_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,       
	 `SPECIMEN_CLASS` varchar(50) NOT NULL default '',
     `SPECIMEN_TYPE` varchar(50) default NULL,                                                                                                                
     `LINEAGE` varchar(50) default NULL,                                                                                                              
     `PATHOLOGICAL_STATUS` varchar(50) default NULL,                                                                                                  
     `PARENT_SPECIMEN_ID` bigint(20) default NULL,                                                                                                    
     `SPECIMEN_CHARACTERISTICS_ID` bigint(20) default NULL,                                                                                           
     `INITIAL_QUANTITY` double default NULL,                                                                                                        
     PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
     KEY `FK1674810456906F39` (`SPECIMEN_CHARACTERISTICS_ID`),                                                                                        
     CONSTRAINT `FK1674810456906F39` FOREIGN KEY (`SPECIMEN_CHARACTERISTICS_ID`) REFERENCES `catissue_specimen_char` (`IDENTIFIER`)                 
); 

CREATE TABLE `CATISSUE_CP_REQ_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,      
     `STORAGE_TYPE` varchar(255) NOT NULL default '',      
	 `COLLECTION_PROTOCOL_EVENT_ID` bigint(20) default NULL,
	 `LABELFORMAT` varchar(255) default NULL,
     PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
     KEY `FK111110456906F39` (`COLLECTION_PROTOCOL_EVENT_ID`),                                                                                        
     CONSTRAINT `FK111110456906F39` FOREIGN KEY (`COLLECTION_PROTOCOL_EVENT_ID`) REFERENCES `catissue_coll_prot_event` (`IDENTIFIER`),
	 CONSTRAINT `FK_PARENT_CP_REQ_SPECIMEN` FOREIGN KEY (`IDENTIFIER`) REFERENCES `CATISSUE_ABSTRACT_SPECIMEN` (`IDENTIFIER`)
);

CREATE TABLE `CATISSUE_MOL_REQ_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,   
	 `CONCENTRATION` double default NULL,
      PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
      CONSTRAINT `FK_MOL_REQ_SPECIMEN` FOREIGN KEY (`IDENTIFIER`) REFERENCES `CATISSUE_CP_REQ_SPECIMEN` (`IDENTIFIER`)                 
);
CREATE TABLE `CATISSUE_FLUID_REQ_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,       
      PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
      CONSTRAINT `FK_FLUID_REQ_SPECIMEN` FOREIGN KEY (`IDENTIFIER`) REFERENCES `CATISSUE_CP_REQ_SPECIMEN` (`IDENTIFIER`)                 
);
CREATE TABLE `CATISSUE_CELL_REQ_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,       
      PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
      CONSTRAINT `FK_CELL_REQ_SPECIMEN` FOREIGN KEY (`IDENTIFIER`) REFERENCES `CATISSUE_CP_REQ_SPECIMEN` (`IDENTIFIER`)                 
);
CREATE TABLE `CATISSUE_TISSUE_REQ_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,       
      PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
      CONSTRAINT `FK_TISSUE_REQ_SPECIMEN` FOREIGN KEY (`IDENTIFIER`) REFERENCES `CATISSUE_CP_REQ_SPECIMEN` (`IDENTIFIER`)                 
);

CREATE TABLE `CATISSUE_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,   
     `LABEL` varchar(255) default NULL,                                                                                                               
     `AVAILABLE` tinyint(1) default NULL,                                                                                                             
     `BARCODE` varchar(255) default NULL,
     `COMMENTS` text,                   
     `ACTIVITY_STATUS` varchar(50) default NULL,                                                                                                      
	 `SPECIMEN_COLLECTION_GROUP_ID` bigint(20) default NULL,  
	 `REQ_SPECIMEN_ID` bigint(20) default NULL,
     `AVAILABLE_QUANTITY` double default NULL,                                                                                                        
     `CREATED_ON_DATE` date default NULL,                                                                                                             
     `COLLECTION_STATUS` varchar(50) default NULL,                                                                                                    
     PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
     UNIQUE KEY `LABEL` (`LABEL`),                                                                                                                    
     UNIQUE KEY `BARCODE` (`BARCODE`),                                                                                                                
     KEY `FK1674810433BF33C5` (`SPECIMEN_COLLECTION_GROUP_ID`),                                                                                       
     KEY `FK_REQ_SPECIMEN_ID` (`REQ_SPECIMEN_ID`),
     CONSTRAINT `FK1674810433BF33C5` FOREIGN KEY (`SPECIMEN_COLLECTION_GROUP_ID`) REFERENCES `catissue_specimen_coll_group` (`IDENTIFIER`),
     CONSTRAINT `FK_REQ_SPECIMEN_ID` FOREIGN KEY (`REQ_SPECIMEN_ID`) REFERENCES `CATISSUE_CP_REQ_SPECIMEN` (`IDENTIFIER`),
 	 CONSTRAINT `FK_PARENT_SPECIMEN` FOREIGN KEY (`IDENTIFIER`) REFERENCES `CATISSUE_ABSTRACT_SPECIMEN` (`IDENTIFIER`)
   ); 

CREATE TABLE `CATISSUE_MOLECULAR_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,  
     `CONCENTRATION` double default NULL,
      PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
      CONSTRAINT `FK_MOLECULAR_SPECIMEN` FOREIGN KEY (`IDENTIFIER`) REFERENCES `CATISSUE_SPECIMEN` (`IDENTIFIER`)                 
);
CREATE TABLE `CATISSUE_FLUID_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,       
      PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
      CONSTRAINT `FK_FLUID_SPECIMEN` FOREIGN KEY (`IDENTIFIER`) REFERENCES `CATISSUE_SPECIMEN` (`IDENTIFIER`)                 
);
CREATE TABLE `CATISSUE_CELL_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,       
      PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
      CONSTRAINT `FK_CELL_SPECIMEN` FOREIGN KEY (`IDENTIFIER`) REFERENCES `CATISSUE_SPECIMEN` (`IDENTIFIER`)                 
);
CREATE TABLE `CATISSUE_TISSUE_SPECIMEN`
(                                                                                                                 
     `IDENTIFIER` bigint(20) NOT NULL auto_increment,       
      PRIMARY KEY  (`IDENTIFIER`),                                                                                                                     
      CONSTRAINT `FK_TISSUE_SPECIMEN` FOREIGN KEY (`IDENTIFIER`) REFERENCES `CATISSUE_SPECIMEN` (`IDENTIFIER`)                 
); 

create table CATISSUE_USER (
   IDENTIFIER bigint not null auto_increment,
   EMAIL_ADDRESS varchar(255),
   FIRST_NAME varchar(255),
   LAST_NAME varchar(255),
   LOGIN_NAME varchar(255) not null unique,
   START_DATE date,
   ACTIVITY_STATUS varchar(50),
   DEPARTMENT_ID bigint,
   CANCER_RESEARCH_GROUP_ID bigint,
   INSTITUTION_ID bigint,
   ADDRESS_ID bigint,
   CSM_USER_ID bigint,
   STATUS_COMMENT text,
   FIRST_TIME_LOGIN bit default 1,
   WUSTLKEY varchar(100) UNIQUE,
   primary key (IDENTIFIER)
);
create table CATISSUE_TIS_SPE_EVENT_PARAM (
   IDENTIFIER bigint not null,
   NEOPLASTIC_CELLULARITY_PER double precision,
   NECROSIS_PERCENTAGE double precision,
   LYMPHOCYTIC_PERCENTAGE double precision,
   TOTAL_CELLULARITY_PERCENTAGE double precision,
   HISTOLOGICAL_QUALITY varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_ST_CONT_COLL_PROT_REL (
   STORAGE_CONTAINER_ID bigint not null,
   COLLECTION_PROTOCOL_ID bigint not null,
   primary key (STORAGE_CONTAINER_ID, COLLECTION_PROTOCOL_ID)
);

create table CATISSUE_SPECIMEN_TYPE (
   SPECIMEN_ARRAY_TYPE_ID bigint not null,
   SPECIMEN_TYPE varchar(50)
);

create table CATISSUE_AUDIT_EVENT_QUERY_LOG (
   IDENTIFIER bigint not null auto_increment,
   QUERY_DETAILS longtext,  
   AUDIT_EVENT_ID bigint,
   primary key (IDENTIFIER)
);

alter table CATISSUE_AUDIT_EVENT_QUERY_LOG add index FK62DC439DBC7298A9 (AUDIT_EVENT_ID), add constraint FK62DC439DBC7298A9 foreign key (AUDIT_EVENT_ID) references CATISSUE_AUDIT_EVENT (IDENTIFIER);

alter table CATISSUE_COLL_COORDINATORS add index FKE490E33A48304401 (COLLECTION_PROTOCOL_ID), add constraint FKE490E33A48304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_COLL_COORDINATORS add index FKE490E33A2206F20F (USER_ID), add constraint FKE490E33A2206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_COLLECTION_PROTOCOL add index FK32DC439DBC7298A9 (IDENTIFIER), add constraint FK32DC439DBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_PROTOCOL (IDENTIFIER);
alter table CATISSUE_COLLECTION_PROTOCOL add index FK32DC439DBC7298B9 (PARENT_CP_ID), add constraint FK32DC439DBC7298B9 foreign key (PARENT_CP_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_EVENT_PARAM add index FK90C79AECBC7298A9 (IDENTIFIER), add constraint FK90C79AECBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_TRANSFER_EVENT_PARAM add index FK71F9AC103C2DAC61 (TO_STORAGE_CONTAINER_ID), add constraint FK71F9AC103C2DAC61 foreign key (TO_STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_TRANSFER_EVENT_PARAM add index FK71F9AC1099DF0A92 (FROM_STORAGE_CONTAINER_ID), add constraint FK71F9AC1099DF0A92 foreign key (FROM_STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_TRANSFER_EVENT_PARAM add index FK71F9AC10BC7298A9 (IDENTIFIER), add constraint FK71F9AC10BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_STOR_CONT_SPEC_CLASS add index FKE7F5E8C2B3DFB11D (STORAGE_CONTAINER_ID), add constraint FKE7F5E8C2B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_COLL_EVENT_PARAM add index FKF9888F91BC7298A9 (IDENTIFIER), add constraint FKF9888F91BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_PASSWORD add index FKDE1F38972206F20F (USER_ID), add constraint FKDE1F38972206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_SPECIMEN_BIOHZ_REL add index FK7A3F5539F398D480 (BIOHAZARD_ID), add constraint FK7A3F5539F398D480 foreign key (BIOHAZARD_ID) references CATISSUE_BIOHAZARD (IDENTIFIER);
alter table CATISSUE_SPECIMEN_BIOHZ_REL add index FK7A3F553960773DB2 (SPECIMEN_ID), add constraint FK7A3F553960773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_MOL_SPE_REVIEW_PARAM add index FK5280ECEBC7298A9 (IDENTIFIER), add constraint FK5280ECEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM (IDENTIFIER);
#--alter table CATISSUE_STORAGE_TYPE add index FKE9A0629A81236791 (HOLDS_STORAGE_TYPE_ID), add constraint FKE9A0629A81236791 foreign key (HOLDS_STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_STORAGE_TYPE add index FKE9A0629ABC7298A9 (IDENTIFIER), add constraint FKE9A0629ABC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER_TYPE (IDENTIFIER);
alter table CATISSUE_CONTAINER add index FK49B8DE5DAC76C0 (CAPACITY_ID), add constraint FK49B8DE5DAC76C0 foreign key (CAPACITY_ID) references CATISSUE_CAPACITY (IDENTIFIER);
alter table CATISSUE_SITE add index FKB024C3436CD94566 (ADDRESS_ID), add constraint FKB024C3436CD94566 foreign key (ADDRESS_ID) references CATISSUE_ADDRESS (IDENTIFIER);
alter table CATISSUE_SITE add index FKB024C3432206F20F (USER_ID), add constraint FKB024C3432206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_EMBEDDED_EVENT_PARAM add index FKD356182FBC7298A9 (IDENTIFIER), add constraint FKD356182FBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_IN_OUT_EVENT_PARAM add index FK4F0FAEB9BC7298A9 (IDENTIFIER), add constraint FK4F0FAEB9BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_COLL_DISTRIBUTION_REL add index FK1483BCB56B1F36E7 (DISTRIBUTION_PROTOCOL_ID), add constraint FK1483BCB56B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_COLL_DISTRIBUTION_REL add index FK1483BCB548304401 (COLLECTION_PROTOCOL_ID), add constraint FK1483BCB548304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_DISPOSAL_EVENT_PARAM add index FK1BC818D6BC7298A9 (IDENTIFIER), add constraint FK1BC818D6BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_ST_CONT_ST_TYPE_REL add index FK703B902159A3CE5C (STORAGE_TYPE_ID), add constraint FK703B902159A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_ST_CONT_ST_TYPE_REL add index FK703B9021B3DFB11D (STORAGE_CONTAINER_ID), add constraint FK703B9021B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_STOR_TYPE_HOLDS_TYPE add index (STORAGE_TYPE_ID), add constraint FK185C50B59A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_STOR_TYPE_HOLDS_TYPE add index (HOLDS_STORAGE_TYPE_ID), add constraint FK185C50B81236791 foreign key (HOLDS_STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_STORTY_HOLDS_SPARRTY add index (STORAGE_TYPE_ID), add constraint FK70F57E4459A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_STORTY_HOLDS_SPARRTY add index (SPECIMEN_ARRAY_TYPE_ID), add constraint FK70F57E44ECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_CONT_HOLDS_SPARRTYPE add index (SPECIMEN_ARRAY_TYPE_ID), add constraint FKDC7E31E2ECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_CONT_HOLDS_SPARRTYPE add index (STORAGE_CONTAINER_ID), add constraint FKDC7E31E2B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_SPECIMEN_PROTOCOL add index FKB8481373870EB740 (PRINCIPAL_INVESTIGATOR_ID), add constraint FKB8481373870EB740 foreign key (PRINCIPAL_INVESTIGATOR_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_FLUID_SPE_EVENT_PARAM add index FK70565D20BC7298A9 (IDENTIFIER), add constraint FK70565D20BC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_SPUN_EVENT_PARAMETERS add index FK312D77BCBC7298A9 (IDENTIFIER), add constraint FK312D77BCBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_RECEIVED_EVENT_PARAM add index FKA7139D06BC7298A9 (IDENTIFIER), add constraint FKA7139D06BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_RACE add index FKB0242ECD87E5ADC7 (PARTICIPANT_ID), add constraint FKB0242ECD87E5ADC7 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT (IDENTIFIER);
alter table CATISSUE_SPECI_ARRAY_CONTENT add index FKBEA9D458C4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKBEA9D458C4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);
alter table CATISSUE_SPECI_ARRAY_CONTENT add index FKBEA9D45860773DB2 (SPECIMEN_ID), add constraint FKBEA9D45860773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3E64B129CC (CREATED_BY_ID), add constraint FKECBF8B3E64B129CC foreign key (CREATED_BY_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EF8278B6 (DISTRIBUTION_ID), add constraint FKECBF8B3EF8278B6 foreign key (DISTRIBUTION_ID) references CATISSUE_DISTRIBUTION (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EBC7298A9 (IDENTIFIER), add constraint FKECBF8B3EBC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EECE89343 (SPECIMEN_ARRAY_TYPE_ID), add constraint FKECBF8B3EECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY_TYPE add index FKD36E0B9BBC7298A9 (IDENTIFIER), add constraint FKD36E0B9BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER_TYPE (IDENTIFIER);
alter table CATISSUE_DISTRIBUTED_ITEM add index FKA7C3ED4B60773DB2 (SPECIMEN_ID), add constraint FKA7C3ED4B60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_DISTRIBUTED_ITEM add index FKA7C3ED4BF8278B6 (DISTRIBUTION_ID), add constraint FKA7C3ED4BF8278B6 foreign key (DISTRIBUTION_ID) references CATISSUE_DISTRIBUTION (IDENTIFIER);
alter table CATISSUE_SPECIMEN_EVENT_PARAM add index FK753F33AD60773DB2 (SPECIMEN_ID), add constraint FK753F33AD60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_ABSTRACT_SPECIMEN (IDENTIFIER);
alter table CATISSUE_SPECIMEN_EVENT_PARAM add index FK753F33AD2206F20F (USER_ID), add constraint FK753F33AD2206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_SPECIMEN_EVENT_PARAM add index FK753F33AD8CA560D1 (SPECIMEN_COLL_GRP_ID), add constraint FK753F33AD8CA560D1 foreign key (SPECIMEN_COLL_GRP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_STOR_TYPE_SPEC_CLASS add index FK1BCF33BA59A3CE5C (STORAGE_TYPE_ID), add constraint FK1BCF33BA59A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_CONTAINER_TYPE add index FKCBBC9954DAC76C0 (CAPACITY_ID), add constraint FKCBBC9954DAC76C0 foreign key (CAPACITY_ID) references CATISSUE_CAPACITY (IDENTIFIER);
alter table CATISSUE_PART_MEDICAL_ID add index FK349E77F9A7F77D13 (SITE_ID), add constraint FK349E77F9A7F77D13 foreign key (SITE_ID) references CATISSUE_SITE (IDENTIFIER);
alter table CATISSUE_PART_MEDICAL_ID add index FK349E77F987E5ADC7 (PARTICIPANT_ID), add constraint FK349E77F987E5ADC7 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT (IDENTIFIER);
alter table CATISSUE_STORAGE_CONTAINER add index FK28429D01BC7298A9 (IDENTIFIER), add constraint FK28429D01BC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER (IDENTIFIER);
alter table CATISSUE_STORAGE_CONTAINER add index FK28429D01A7F77D13 (SITE_ID), add constraint FK28429D01A7F77D13 foreign key (SITE_ID) references CATISSUE_SITE (IDENTIFIER);
alter table CATISSUE_STORAGE_CONTAINER add index FK28429D0159A3CE5C (STORAGE_TYPE_ID), add constraint FK28429D0159A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_CELL_SPE_REVIEW_PARAM add index FK52F40EDEBC7298A9 (IDENTIFIER), add constraint FK52F40EDEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION add index FK542766802206F20F (USER_ID), add constraint FK542766802206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION add index FK542766806B1F36E7 (DISTRIBUTION_PROTOCOL_ID), add constraint FK542766806B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION add index FK542766801DBE834F (TO_SITE_ID), add constraint FK542766801DBE834F foreign key (TO_SITE_ID) references CATISSUE_SITE (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION add index FK5427668060773DB2 (SPECIMEN_ID), add constraint FK5427668060773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_PROCEDURE_EVENT_PARAM add index FKEC6B4260BC7298A9 (IDENTIFIER), add constraint FKEC6B4260BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION_PROTOCOL add index FKC8999977BC7298A9 (IDENTIFIER), add constraint FKC8999977BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_PROTOCOL (IDENTIFIER);
alter table CATISSUE_EXTERNAL_IDENTIFIER add index FK5CF2FA2160773DB2 (SPECIMEN_ID), add constraint FK5CF2FA2160773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_FIXED_EVENT_PARAM add index FKE0F1781BC7298A9 (IDENTIFIER), add constraint FKE0F1781BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_THAW_EVENT_PARAMETERS add index FKD8890A48BC7298A9 (IDENTIFIER), add constraint FKD8890A48BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_COLL_PROT_REG add index FK5EB25F1387E5ADC7 (PARTICIPANT_ID), add constraint FK5EB25F1387E5ADC7 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT (IDENTIFIER);
alter table CATISSUE_COLL_PROT_REG add index FK5EB25F13A0FF79D4 (CONSENT_WITNESS), add constraint FK5EB25F13A0FF79D4 foreign key (CONSENT_WITNESS) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_COLL_PROT_REG add index FK5EB25F1348304401 (COLLECTION_PROTOCOL_ID), add constraint FK5EB25F1348304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_FROZEN_EVENT_PARAM add index FK52627245BC7298A9 (IDENTIFIER), add constraint FK52627245BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_USER add index FKB025CFC71792AD22 (INSTITUTION_ID), add constraint FKB025CFC71792AD22 foreign key (INSTITUTION_ID) references CATISSUE_INSTITUTION (IDENTIFIER);
alter table CATISSUE_USER add index FKB025CFC7FFA96920 (CANCER_RESEARCH_GROUP_ID), add constraint FKB025CFC7FFA96920 foreign key (CANCER_RESEARCH_GROUP_ID) references CATISSUE_CANCER_RESEARCH_GROUP (IDENTIFIER);
alter table CATISSUE_USER add index FKB025CFC76CD94566 (ADDRESS_ID), add constraint FKB025CFC76CD94566 foreign key (ADDRESS_ID) references CATISSUE_ADDRESS (IDENTIFIER);
alter table CATISSUE_USER add index FKB025CFC7F30C2528 (DEPARTMENT_ID), add constraint FKB025CFC7F30C2528 foreign key (DEPARTMENT_ID) references CATISSUE_DEPARTMENT (IDENTIFIER);
alter table CATISSUE_TIS_SPE_EVENT_PARAM add index FKBB9648F4BC7298A9 (IDENTIFIER), add constraint FKBB9648F4BC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_ST_CONT_COLL_PROT_REL add index FK3AE9FCA7B3DFB11D (STORAGE_CONTAINER_ID), add constraint FK3AE9FCA7B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_ST_CONT_COLL_PROT_REL add index FK3AE9FCA748304401 (COLLECTION_PROTOCOL_ID), add constraint FK3AE9FCA748304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_SPECIMEN_COLL_GROUP add index FKDEBAF16753B01F66 (COLLECTION_PROTOCOL_EVENT_ID), add constraint FKDEBAF16753B01F66 foreign key (COLLECTION_PROTOCOL_EVENT_ID) references CATISSUE_COLL_PROT_EVENT (IDENTIFIER);
alter table CATISSUE_SPECIMEN_TYPE add index FKFF69C195ECE89343 (SPECIMEN_ARRAY_TYPE_ID), add constraint FKFF69C195ECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_PERMISSIBLE_VALUE add index FK57DDCE153B5435E (PARENT_IDENTIFIER), add constraint FK57DDCE153B5435E foreign key (PARENT_IDENTIFIER) references CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER);
alter table CATISSUE_PERMISSIBLE_VALUE add index FK57DDCE1FC56C2B1 (PUBLIC_ID), add constraint FK57DDCE1FC56C2B1 foreign key (PUBLIC_ID) references CATISSUE_CDE (PUBLIC_ID);
alter table CATISSUE_AUDIT_EVENT add index FKACAF697A2206F20F (USER_ID), add constraint FKACAF697A2206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_AUDIT_EVENT_LOG add index FK8BB672DF77F0B904 (AUDIT_EVENT_ID), add constraint FK8BB672DF77F0B904 foreign key (AUDIT_EVENT_ID) references CATISSUE_AUDIT_EVENT (IDENTIFIER);
alter table CATISSUE_AUDIT_EVENT_DETAILS add index FK5C07745D34FFD77F (AUDIT_EVENT_LOG_ID), add constraint FK5C07745D34FFD77F foreign key (AUDIT_EVENT_LOG_ID) references CATISSUE_AUDIT_EVENT_LOG (IDENTIFIER);

					 
#------ Consent Tracking related drop, create and add foreign key scripts.

create table CATISSUE_CONSENT_TIER_RESPONSE (
   IDENTIFIER bigint not null auto_increment,
   RESPONSE varchar(255),
   CONSENT_TIER_ID bigint,
   COLL_PROT_REG_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_CONSENT_TIER_STATUS (
   IDENTIFIER bigint not null auto_increment,
   CONSENT_TIER_ID bigint,
   STATUS varchar(255),
   SPECIMEN_ID bigint,
   SPECIMEN_COLL_GROUP_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_CONSENT_TIER (
   IDENTIFIER bigint not null auto_increment,
   STATEMENT text,
   COLL_PROTOCOL_ID bigint,
   primary key (IDENTIFIER)
);


alter table CATISSUE_CONSENT_TIER_RESPONSE add index FKFB1995FD4AD77FCB (COLL_PROT_REG_ID), add constraint FKFB1995FD4AD77FCB foreign key (COLL_PROT_REG_ID) references CATISSUE_COLL_PROT_REG (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_RESPONSE add index FKFB1995FD17B9953 (CONSENT_TIER_ID), add constraint FKFB1995FD17B9953 foreign key (CONSENT_TIER_ID) references CATISSUE_CONSENT_TIER (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS add index FKF74E94AEF69249F7 (SPECIMEN_COLL_GROUP_ID), add constraint FKF74E94AEF69249F7 foreign key (SPECIMEN_COLL_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS add index FKF74E94AE60773DB2 (SPECIMEN_ID), add constraint FKF74E94AE60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS add index FKF74E94AE17B9953 (CONSENT_TIER_ID), add constraint FKF74E94AE17B9953 foreign key (CONSENT_TIER_ID) references CATISSUE_CONSENT_TIER (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER add index FK51725303E36A4B4F (COLL_PROTOCOL_ID), add constraint FK51725303E36A4B4F foreign key (COLL_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);


#---- Ordering system  related alter table script


create table CATISSUE_PATH_CASE_ORDER_ITEM (
   IDENTIFIER bigint not null,
   PATHOLOGICAL_STATUS varchar(255),
   TISSUE_SITE varchar(255),
   SPECIMEN_COLL_GROUP_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_DERIEVED_SP_ORD_ITEM (
   IDENTIFIER bigint not null,
   SPECIMEN_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_NEW_SPEC_ORD_ITEM (
   IDENTIFIER bigint not null,
   SPECIMEN_CLASS varchar(255),
   SPECIMEN_TYPE varchar(255),
   primary key (IDENTIFIER)
);

create table CATISSUE_EXISTING_SP_ORD_ITEM (
   IDENTIFIER bigint not null,
   SPECIMEN_ID bigint,
   primary key (IDENTIFIER)
);


create table CATISSUE_SPECIMEN_ORDER_ITEM (
   IDENTIFIER bigint not null,
   ARRAY_ORDER_ITEM_ID bigint,
   primary key (IDENTIFIER)
);

#---- Specimen Array Order


create table CATISSUE_NEW_SP_AR_ORDER_ITEM (
   IDENTIFIER bigint not null,
   ARRAY_TYPE_ID bigint,
   NAME varchar(255),
   SPECIMEN_ARRAY_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_EXIST_SP_AR_ORD_ITEM (
   IDENTIFIER bigint not null,
   SPECIMEN_ARRAY_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_SP_ARRAY_ORDER_ITEM (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);


create table CATISSUE_ORDER_ITEM (
   IDENTIFIER bigint not null auto_increment,
   DESCRIPTION text,
   DISTRIBUTED_ITEM_ID bigint,
   STATUS varchar(50),
   REQUESTED_QUANTITY double precision,
   ORDER_ID bigint,
   primary key (IDENTIFIER)
);


create table CATISSUE_ORDER (
   IDENTIFIER bigint not null auto_increment,
   COMMENTS text,
   DISTRIBUTION_PROTOCOL_ID bigint,
   NAME text,
   REQUESTED_DATE datetime,
   STATUS varchar(50),
   primary key (IDENTIFIER)
);



/* extra for catissue_distribution */
 alter table catissue_distribution add column `ORDER_ID` bigint   NULL;
 alter table catissue_distributed_item add column `SPECIMEN_ARRAY_ID` bigint   NULL;  
/* extra finished */
 
alter table CATISSUE_PATH_CASE_ORDER_ITEM add index FKBD5029D5F69249F7 (SPECIMEN_COLL_GROUP_ID), add constraint FKBD5029D5F69249F7 foreign key (SPECIMEN_COLL_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_PATH_CASE_ORDER_ITEM add index FKBD5029D5BC7298A9 (IDENTIFIER), add constraint FKBD5029D5BC7298A9 foreign key (IDENTIFIER) references CATISSUE_NEW_SPEC_ORD_ITEM (IDENTIFIER);

alter table CATISSUE_DERIEVED_SP_ORD_ITEM add index FK3742152BBC7298A9 (IDENTIFIER), add constraint FK3742152BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_NEW_SPEC_ORD_ITEM (IDENTIFIER);
alter table CATISSUE_DERIEVED_SP_ORD_ITEM add index FK3742152B60773DB2 (SPECIMEN_ID), add constraint FK3742152B60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);

alter table CATISSUE_NEW_SPEC_ORD_ITEM add index FK_NEW_SPECIMEN_ORDER_ITEM (IDENTIFIER), add constraint FK_NEW_SPECIMEN_ORDER_ITEM foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_ORDER_ITEM (IDENTIFIER);

alter table CATISSUE_EXISTING_SP_ORD_ITEM add index FKF8B855EEBC7298A9 (IDENTIFIER), add constraint FKF8B855EEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_EXISTING_SP_ORD_ITEM add index FKF8B855EE60773DB2 (SPECIMEN_ID), add constraint FKF8B855EE60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);

alter table CATISSUE_SPECIMEN_ORDER_ITEM add index FK48C3B39FBC7298A9 (IDENTIFIER), add constraint FK48C3B39FBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ORDER_ITEM add index FK48C3B39F83505A30 (ARRAY_ORDER_ITEM_ID), add constraint FK48C3B39F83505A30 foreign key (ARRAY_ORDER_ITEM_ID) references CATISSUE_NEW_SP_AR_ORDER_ITEM (IDENTIFIER);

alter table CATISSUE_EXIST_SP_AR_ORD_ITEM add index FKE3823170BC7298A9 (IDENTIFIER), add constraint FKE3823170BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SP_ARRAY_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_EXIST_SP_AR_ORD_ITEM add index FKE3823170C4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKE3823170C4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);

alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBCE5FBC3A (ARRAY_TYPE_ID), add constraint FKC5C92CCBCE5FBC3A foreign key (ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBBC7298A9 (IDENTIFIER), add constraint FKC5C92CCBBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SP_ARRAY_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBC4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKC5C92CCBC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);

alter table CATISSUE_SP_ARRAY_ORDER_ITEM add index FK_SP_ARRAY_ORDER_ITEM (IDENTIFIER), add constraint FK_SP_ARRAY_ORDER_ITEM foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);

alter table CATISSUE_ORDER_ITEM add index FKB501E88060975C0B (DISTRIBUTED_ITEM_ID), add constraint FKB501E88060975C0B foreign key (DISTRIBUTED_ITEM_ID) references CATISSUE_DISTRIBUTED_ITEM (IDENTIFIER);
alter table CATISSUE_ORDER_ITEM add index FKB501E880783867CC (ORDER_ID), add constraint FKB501E880783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);

alter table CATISSUE_ORDER add index FK543F22B26B1F36E7 (DISTRIBUTION_PROTOCOL_ID), add constraint FK543F22B26B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL (IDENTIFIER);

alter table CATISSUE_DISTRIBUTION add index FK54276680783867CC (ORDER_ID), add constraint FK54276680783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_DISTRIBUTED_ITEM add index FKA7C3ED4BC4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKA7C3ED4BC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);


create table CATISSUE_SPECIMEN_LABEL_COUNT (
   LABEL_COUNT bigint not null,
   primary key (LABEL_COUNT)
);
INSERT INTO CATISSUE_SPECIMEN_LABEL_COUNT (LABEL_COUNT) VALUES ('0');


/****caTIES Realated Tables - start**********/

create table CATISSUE_REPORT_TEXTCONTENT (
   IDENTIFIER bigint not null,
   REPORT_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_IDENTIFIED_REPORT (
   IDENTIFIER bigint not null,
   DEID_REPORT bigint,
   SCG_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_CONCEPT_REFERENT (
   IDENTIFIER bigint not null auto_increment,
   CONCEPT_ID bigint,
   CONCEPT_CLASSIFICATION_ID bigint,
   DEIDENTIFIED_REPORT_ID bigint,
   END_OFFSET bigint,
   IS_MODIFIER bit,
   IS_NEGATED bit,
   START_OFFSET bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_CONTENT (
   IDENTIFIER bigint not null auto_increment,
   REPORT_DATA text,
   primary key (IDENTIFIER)
);
create table CATISSUE_REVIEW_PARAMS (
   IDENTIFIER bigint not null auto_increment,
   REVIEWER_ROLE varchar(100),
   REPORT_ID bigint,
   EVENT_TIMESTAMP datetime,
   USER_ID bigint,
   COMMENTS text,
   STATUS varchar(100),
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_BICONTENT (
   IDENTIFIER bigint not null,
   REPORT_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_SECTION (
   IDENTIFIER bigint not null auto_increment,
   DOCUMENT_FRAGMENT text,
   END_OFFSET integer,
   NAME varchar(100),
   START_OFFSET integer,
   TEXT_CONTENT_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_DEIDENTIFIED_REPORT (
   IDENTIFIER bigint not null,
   STATUS varchar(100),
   SCG_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_QUARANTINE_PARAMS (
   IDENTIFIER bigint not null auto_increment,
   DEID_REPORT_ID bigint,
   IS_QUARANTINED bit,
   EVENT_TIMESTAMP datetime,
   USER_ID bigint,
   COMMENTS text,
   STATUS varchar(100),
   primary key (IDENTIFIER)
);
create table CATISSUE_PATHOLOGY_REPORT (
   IDENTIFIER bigint not null auto_increment,
   ACTIVITY_STATUS varchar(100),
   REVIEW_FLAG bit,
   SOURCE_ID bigint,
   REPORT_STATUS varchar(100),
   COLLECTION_DATE_TIME date,
   primary key (IDENTIFIER)
);

create table CATISSUE_REPORT_XMLCONTENT (
   IDENTIFIER bigint not null,
   REPORT_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_QUEUE (
   IDENTIFIER bigint not null auto_increment,
   STATUS varchar(50),
   SURGICAL_PATHOLOGY_NUMBER varchar(50),
   PARTICIPANT_NAME varchar(255),
   SITE_NAME varchar(255),
   REPORT_LOADED_DATE date,
   REPORT_TEXT text,
   SPECIMEN_COLL_GRP_ID bigint,
   REPORT_COLLECTION_DATE date,
   primary key (IDENTIFIER)
);

create table CATISSUE_REPORT_PARTICIP_REL(
   PARTICIPANT_ID bigint,
   REPORT_ID bigint
);
create table CATISSUE_CONCEPT (
   IDENTIFIER bigint not null auto_increment,
   CONCEPT_UNIQUE_ID varchar(30),
   NAME text,
   SEMANTIC_TYPE_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_SEMANTIC_TYPE (
   IDENTIFIER bigint not null auto_increment,
   LABEL text,
   primary key (IDENTIFIER)
);

create table CATISSUE_CONCEPT_CLASSIFICATN (
   IDENTIFIER bigint not null auto_increment,
   NAME text,
   primary key (IDENTIFIER)
);



alter table CATISSUE_REPORT_TEXTCONTENT add index FKD74882FD91092806 (REPORT_ID), add constraint FKD74882FD91092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_TEXTCONTENT add index FKD74882FDBC7298A9 (IDENTIFIER), add constraint FKD74882FDBC7298A9 foreign key (IDENTIFIER) references CATISSUE_REPORT_CONTENT (IDENTIFIER);
alter table CATISSUE_IDENTIFIED_REPORT add index FK6A2246DCBC7298A9 (IDENTIFIER), add constraint FK6A2246DCBC7298A9 foreign key (IDENTIFIER) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_IDENTIFIED_REPORT add index FK6A2246DC752DD177 (DEID_REPORT), add constraint FK6A2246DC752DD177 foreign key (DEID_REPORT) references CATISSUE_DEIDENTIFIED_REPORT (IDENTIFIER);
alter table CATISSUE_IDENTIFIED_REPORT add index FK6A2246DC91741663 (SCG_ID), add constraint FK6A2246DC91741663 foreign key (SCG_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add index FK799CCA7E9F96B363 (DEIDENTIFIED_REPORT_ID), add constraint FK799CCA7E9F96B363 foreign key (DEIDENTIFIED_REPORT_ID) references CATISSUE_DEIDENTIFIED_REPORT (IDENTIFIER);
alter table CATISSUE_REVIEW_PARAMS add index FK5311FFF62206F20F (USER_ID), add constraint FK5311FFF62206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_REVIEW_PARAMS add index FK5311FFF691092806 (REPORT_ID), add constraint FK5311FFF691092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_BICONTENT add index FK8A9A4EE391092806 (REPORT_ID), add constraint FK8A9A4EE391092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_BICONTENT add index FK8A9A4EE3BC7298A9 (IDENTIFIER), add constraint FK8A9A4EE3BC7298A9 foreign key (IDENTIFIER) references CATISSUE_REPORT_CONTENT (IDENTIFIER);
alter table CATISSUE_DEIDENTIFIED_REPORT add index FKCDD0DF7BBC7298A9 (IDENTIFIER), add constraint FKCDD0DF7BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_DEIDENTIFIED_REPORT add index FKCDD0DF7B91741663 (SCG_ID), add constraint FKCDD0DF7B91741663 foreign key (SCG_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_QUARANTINE_PARAMS add index FK3C12AE3B2206F20F (USER_ID), add constraint FK3C12AE3B2206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_QUARANTINE_PARAMS add index FK3C12AE3B3EEC14E3 (DEID_REPORT_ID), add constraint FK3C12AE3B3EEC14E3 foreign key (DEID_REPORT_ID) references CATISSUE_DEIDENTIFIED_REPORT (IDENTIFIER);
alter table CATISSUE_PATHOLOGY_REPORT add index FK904EC9F040DCD7BF (SOURCE_ID), add constraint FK904EC9F040DCD7BF foreign key (SOURCE_ID) references CATISSUE_SITE (IDENTIFIER);
alter table CATISSUE_REPORT_XMLCONTENT add index FK4597C9F1BC7298A9 (IDENTIFIER), add constraint FK4597C9F1BC7298A9 foreign key (IDENTIFIER) references CATISSUE_REPORT_CONTENT (IDENTIFIER);
alter table CATISSUE_REPORT_XMLCONTENT add index FK4597C9F191092806 (REPORT_ID), add constraint FK4597C9F191092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_QUEUE add index FK214246228CA560D1 (SPECIMEN_COLL_GRP_ID), add constraint FK214246228CA560D1 foreign key (SPECIMEN_COLL_GRP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_CONCEPT add index FKC1A3C8CC7F0C2C7 (SEMANTIC_TYPE_ID), add constraint FKC1A3C8CC7F0C2C7 foreign key (SEMANTIC_TYPE_ID) references CATISSUE_SEMANTIC_TYPE (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add index FK799CCA7EA9816272 (CONCEPT_ID), add constraint FK799CCA7EA9816272 foreign key (CONCEPT_ID) references CATISSUE_CONCEPT (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add index FK799CCA7E72C371DD (CONCEPT_CLASSIFICATION_ID), add constraint FK799CCA7E72C371DD foreign key (CONCEPT_CLASSIFICATION_ID) references CATISSUE_CONCEPT_CLASSIFICATN (IDENTIFIER);

/****caTIES Realated Tables - end**********/

alter table catissue_consent_tier_status drop foreign key FKF74E94AEF69249F7;
alter table catissue_consent_tier_status add CONSTRAINT `FKF74E94AEF69249F7` FOREIGN KEY (`SPECIMEN_COLL_GROUP_ID`) REFERENCES `catissue_specimen_coll_group` (`IDENTIFIER`);
alter table catissue_specimen_event_param drop foreign key FK753F33AD8CA560D1;
alter table catissue_specimen_event_param add CONSTRAINT `FK753F33AD8CA560D1` FOREIGN KEY (`SPECIMEN_COLL_GRP_ID`) REFERENCES `catissue_specimen_coll_group` (`IDENTIFIER`);  
alter table catissue_identified_report drop foreign key FK6A2246DC91741663;
alter table catissue_identified_report add CONSTRAINT `FK6A2246DC91741663` FOREIGN KEY (`SCG_ID`) REFERENCES `catissue_specimen_coll_group` (`IDENTIFIER`);  
alter table catissue_deidentified_report drop foreign key FKCDD0DF7B91741663;
alter table catissue_deidentified_report add CONSTRAINT `FKCDD0DF7B91741663` FOREIGN KEY (`SCG_ID`) REFERENCES `catissue_specimen_coll_group` (`IDENTIFIER`);  



/* Suite 1.1 model changes */
drop table if exists CATISSUE_ABSTRACT_POSITION;
drop table if exists CATISSUE_SPECIMEN_POSITION;
drop table if exists CATISSUE_CONTAINER_POSITION;

create table CATISSUE_ABSTRACT_POSITION (
	IDENTIFIER BIGINT not null auto_increment,
	POSITION_DIMENSION_ONE INTEGER,
	POSITION_DIMENSION_TWO INTEGER,
	primary key (IDENTIFIER)
)ENGINE = INNODB DEFAULT CHARSET = utf8;


create table CATISSUE_SPECIMEN_POSITION(
	IDENTIFIER BIGINT not null auto_increment,
	SPECIMEN_ID BIGINT,
	CONTAINER_ID BIGINT,
	primary key (IDENTIFIER),
	index FK_SPECIMEN_POSITION (IDENTIFIER),
	constraint FK_SPECIMEN_POSITION foreign key (IDENTIFIER) references CATISSUE_ABSTRACT_POSITION (IDENTIFIER),
	index FK_SPECIMEN (SPECIMEN_ID),
	constraint FK_SPECIMEN foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER),
	index FK_STORAGE_CONTAINER (CONTAINER_ID),
	constraint FK_STORAGE_CONTAINER foreign key (CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER)
)ENGINE = INNODB DEFAULT CHARSET = utf8;


create table CATISSUE_CONTAINER_POSITION(
	IDENTIFIER BIGINT not null auto_increment,
	PARENT_CONTAINER_ID bigint,
	CONTAINER_ID BIGINT,
	primary key (IDENTIFIER),
	index FK_CONTAINER_POSITION (IDENTIFIER), 
	constraint FK_CONTAINER_POSITION foreign key (IDENTIFIER) references CATISSUE_ABSTRACT_POSITION (IDENTIFIER),
	index FK_CONTAINER (CONTAINER_ID),
	constraint FK_CONTAINER foreign key (CONTAINER_ID) references CATISSUE_CONTAINER (IDENTIFIER),
	index FK_OCCUPIED_CONTAINER (PARENT_CONTAINER_ID),
	constraint FK_OCCUPIED_CONTAINER foreign key (PARENT_CONTAINER_ID) references CATISSUE_CONTAINER (IDENTIFIER)
)ENGINE = INNODB DEFAULT CHARSET = utf8;

//Msr changes
CREATE TABLE CATISSUE_SITE_USERS (
   SITE_ID BIGINT ,
   USER_ID BIGINT ,
   PRIMARY KEY (SITE_ID, USER_ID)
);

CREATE TABLE CATISSUE_SITE_CP(
   SITE_ID BIGINT ,
   COLLECTION_PROTOCOL_ID BIGINT ,
   PRIMARY KEY (SITE_ID,COLLECTION_PROTOCOL_ID)
 );

CREATE TABLE CATISSUE_USER_CP(
  USER_ID BIGINT ,
  COLLECTION_PROTOCOL_ID BIGINT ,
  PRIMARY KEY (USER_ID,COLLECTION_PROTOCOL_ID)
 );

ALTER TABLE CATISSUE_SITE_USERS ADD INDEX FK1 (USER_ID), ADD CONSTRAINT FK1 FOREIGN KEY (USER_ID) REFERENCES CATISSUE_USER (IDENTIFIER);
ALTER TABLE CATISSUE_SITE_USERS ADD INDEX FK2 (SITE_ID), ADD CONSTRAINT FK2 FOREIGN KEY (SITE_ID) REFERENCES CATISSUE_SITE (IDENTIFIER);

ALTER TABLE CATISSUE_SITE_CP ADD INDEX FK3 (SITE_ID), ADD CONSTRAINT FK3 FOREIGN KEY (SITE_ID) REFERENCES CATISSUE_SITE (IDENTIFIER);
ALTER TABLE CATISSUE_SITE_CP ADD INDEX FK4 (COLLECTION_PROTOCOL_ID), ADD CONSTRAINT FK4 FOREIGN KEY (COLLECTION_PROTOCOL_ID) REFERENCES CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);

ALTER TABLE CATISSUE_USER_CP ADD INDEX FK5 (USER_ID), ADD CONSTRAINT FK5 FOREIGN KEY (USER_ID) REFERENCES CATISSUE_USER (IDENTIFIER);
ALTER TABLE CATISSUE_USER_CP ADD INDEX FK6 (COLLECTION_PROTOCOL_ID), ADD CONSTRAINT FK6 FOREIGN KEY (COLLECTION_PROTOCOL_ID) REFERENCES CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);

ALTER TABLE `CATISSUE_SPECIMEN` change `BARCODE` `BARCODE` varchar (255) BINARY NULL;
ALTER TABLE `CATISSUE_SPECIMEN_COLL_GROUP` change `BARCODE` `BARCODE` varchar (255) BINARY NULL ;
ALTER TABLE `CATISSUE_CONTAINER` change `BARCODE` `BARCODE` varchar (255) BINARY NULL;
ALTER TABLE `CATISSUE_COLL_PROT_REG` change `BARCODE` `BARCODE` varchar (255) BINARY NULL;

/* for adding the mataPhone for participant last name*/ 
ALTER TABLE CATISSUE_PARTICIPANT ADD LNAME_METAPHONE varchar(50); 

/**  Bug 13225 -  Clinical Diagnosis subset at CP definition level 
 */
drop table if exists CATISSUE_CLINICAL_DIAGNOSIS;
create table CATISSUE_CLINICAL_DIAGNOSIS (
   IDENTIFIER BIGINT not null auto_increment,
   CLINICAL_DIAGNOSIS varchar(255),
   COLLECTION_PROTOCOL_ID BIGINT,
   primary key (IDENTIFIER),
   CONSTRAINT FK_CD_COLPROT FOREIGN KEY (COLLECTION_PROTOCOL_ID) REFERENCES CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER)
);

/*Query audit changes.*/

alter table catissue_audit_event_query_log add column QUERY_ID bigint(20) default null; 
alter table catissue_audit_event_query_log add column TEMP_TABLE_NAME varchar(150) default null;
alter table catissue_audit_event_query_log add column IF_TEMP_TABLE_DELETED tinyint(1) default false;
alter table catissue_audit_event_query_log add column ROOT_ENTITY_NAME varchar(150) default null;
alter table catissue_audit_event_query_log add column COUNT_OF_ROOT_RECORDS bigint(20) default null;

/*Bulk Operations from UI*/
create table catissue_bulk_operation (
	IDENTIFIER BIGINT(20) not null auto_increment,
	OPERATION VARCHAR(100) not null,
	CSV_TEMPLATE VARCHAR(5000) not null,
	XML_TEMPALTE VARCHAR(15000) not null,
	DROPDOWN_NAME VARCHAR(100) not null,
	PRIMARY KEY  (`IDENTIFIER`), UNIQUE KEY OPERATION (`OPERATION`), UNIQUE KEY DROPDOWN_NAME (`DROPDOWN_NAME`)
);

/**
 * Container Type Req
 */
CREATE TABLE catissue_stor_type_spec_type 
(                                                                       
  STORAGE_TYPE_ID bigint(20) NOT NULL,                                                                          
  SPECIMEN_TYPE varchar(50) default NULL,
  primary key (STORAGE_TYPE_ID),                                                             
  CONSTRAINT STORAGE_TYPE_ID_FK FOREIGN KEY (STORAGE_TYPE_ID) REFERENCES catissue_storage_type (IDENTIFIER)  
);     
/**
 * Container Type Req
 */
CREATE TABLE catissue_stor_cont_spec_type
(                                                                                 
  STORAGE_CONTAINER_ID bigint(20) NOT NULL,                                                                                  
  SPECIMEN_TYPE varchar(50) default NULL,                                                                                   
  primary key (STORAGE_CONTAINER_ID),                                                                           
  CONSTRAINT SPECIMEN_TYPE_ST_ID_FK FOREIGN KEY (STORAGE_CONTAINER_ID) REFERENCES catissue_storage_container (IDENTIFIER)  
);      
