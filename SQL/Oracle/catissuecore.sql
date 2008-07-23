create table CATISSUE_PERMISSIBLE_VALUE (
   IDENTIFIER number(19,0) not null ,
   CONCEPT_CODE varchar(40),
   DEFINITION varchar2(500),
   PARENT_IDENTIFIER number(19,0),
   VALUE varchar(100),
   PUBLIC_ID varchar(30),
   primary key (IDENTIFIER)
);
create table CATISSUE_CDE (
   PUBLIC_ID varchar(30) not null,
   LONG_NAME varchar(200),
   DEFINITION varchar2(500),
   VERSION varchar(50),
   LAST_UPDATED date,
   primary key (PUBLIC_ID)
);
create table CATISSUE_AUDIT_EVENT (
   IDENTIFIER number(19,0) not null ,
   IP_ADDRESS varchar(20),
   EVENT_TIMESTAMP date,
   USER_ID number(19,0),
   COMMENTS varchar2(500),
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT_LOG (
   IDENTIFIER number(19,0) not null ,
   OBJECT_IDENTIFIER number(19,0),
   OBJECT_NAME varchar(50),
   EVENT_TYPE varchar(50),
   AUDIT_EVENT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT_DETAILS (
   IDENTIFIER number(19,0) not null ,
   ELEMENT_NAME varchar(150),
   PREVIOUS_VALUE varchar(150),
   CURRENT_VALUE varchar(500),
   AUDIT_EVENT_LOG_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_COORDINATORS (
   COLLECTION_PROTOCOL_ID number(19,0) not null,
   USER_ID number(19,0) not null,
   primary key (COLLECTION_PROTOCOL_ID, USER_ID)
);
create table CATISSUE_CANCER_RESEARCH_GROUP (
   IDENTIFIER number(19,0) not null ,
   NAME varchar(255) not null unique,
   primary key (IDENTIFIER)
);
create table CATISSUE_COLLECTION_PROTOCOL (
   IDENTIFIER number(19,0) not null,
   ALIQUOT_IN_SAME_CONTAINER number(1,0),
   UNSIGNED_CONSENT_DOC_URL varchar2(500),
   CONSENTS_WAIVED number(1,0),
   CP_TYPE varchar(50) default NULL,
   PARENT_CP_ID number(19,0) default NULL,
   SEQUENCE_NUMBER integer,	
   STUDY_CALENDAR_EVENT_POINT DOUBLE PRECISION default NULL,
   primary key (IDENTIFIER)
);

create table CATISSUE_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_TRANSFER_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   FROM_POSITION_DIMENSION_ONE integer,
   FROM_POSITION_DIMENSION_TWO integer,
   TO_POSITION_DIMENSION_ONE integer,
   TO_POSITION_DIMENSION_TWO integer,
   TO_STORAGE_CONTAINER_ID number(19,0),
   FROM_STORAGE_CONTAINER_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_STOR_CONT_SPEC_CLASS (
   STORAGE_CONTAINER_ID number(19,0) not null,
   SPECIMEN_CLASS varchar(50)
);
create table CATISSUE_COLL_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   COLLECTION_PROCEDURE varchar(50),
   CONTAINER varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_PASSWORD (
   IDENTIFIER number(19,0) not null ,
   PASSWORD varchar(255),
   UPDATE_DATE date,
   USER_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_BIOHZ_REL (
   BIOHAZARD_ID number(19,0) not null,
   SPECIMEN_ID number(19,0) not null,
   primary key (SPECIMEN_ID, BIOHAZARD_ID)
);
create table CATISSUE_MOL_SPE_REVIEW_PARAM (
   IDENTIFIER number(19,0) not null,
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
   IDENTIFIER number(19,0) not null,
   DEFAULT_TEMPERATURE DOUBLE PRECISION,
   primary key (IDENTIFIER)
);

create table CATISSUE_CONTAINER (
   IDENTIFIER number(19,0) not null ,
   ACTIVITY_STATUS varchar(50),
   BARCODE varchar(255) unique,
   CAPACITY_ID number(19,0),
   COMMENTS varchar2(500),
   FULL number(1,0),
   NAME varchar(255) unique not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_SITE (
   IDENTIFIER number(19,0) not null ,
   NAME varchar(255) not null unique,
   TYPE varchar(50),
   EMAIL_ADDRESS varchar(255),
   USER_ID number(19,0),
   ACTIVITY_STATUS varchar(50),
   ADDRESS_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_EMBEDDED_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   EMBEDDING_MEDIUM varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_IN_OUT_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   STORAGE_STATUS varchar(100) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_DISTRIBUTION_REL (
   COLLECTION_PROTOCOL_ID number(19,0) not null,
   DISTRIBUTION_PROTOCOL_ID number(19,0) not null,
   primary key (COLLECTION_PROTOCOL_ID, DISTRIBUTION_PROTOCOL_ID)
);
create table CATISSUE_DISPOSAL_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   REASON varchar(255),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_REQUIREMENT (
   IDENTIFIER number(19,0) not null ,
   SPECIMEN_CLASS varchar(255) not null,
   SPECIMEN_TYPE varchar(50),
   TISSUE_SITE varchar(150),
   PATHOLOGY_STATUS varchar(50),
   QUANTITY_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_INSTITUTION (
   IDENTIFIER number(19,0) not null ,
   NAME varchar(255) not null unique,
   primary key (IDENTIFIER)
);
create table CATISSUE_ST_CONT_ST_TYPE_REL (
   STORAGE_CONTAINER_ID number(19,0) not null,
   STORAGE_TYPE_ID number(19,0) not null,
   primary key (STORAGE_CONTAINER_ID, STORAGE_TYPE_ID)
);
create table CATISSUE_STOR_TYPE_HOLDS_TYPE (
   STORAGE_TYPE_ID number(19,0) not null,
   HOLDS_STORAGE_TYPE_ID number(19,0) not null,
   primary key (STORAGE_TYPE_ID, HOLDS_STORAGE_TYPE_ID)
);

create table CATISSUE_STORTY_HOLDS_SPARRTY (
   STORAGE_TYPE_ID number(19,0) not null,
   SPECIMEN_ARRAY_TYPE_ID number(19,0) not null,
   primary key (STORAGE_TYPE_ID, SPECIMEN_ARRAY_TYPE_ID)
);

create table CATISSUE_CONT_HOLDS_SPARRTYPE (
   STORAGE_CONTAINER_ID number(19,0) not null,
   SPECIMEN_ARRAY_TYPE_ID number(19,0) not null,
   primary key (STORAGE_CONTAINER_ID, SPECIMEN_ARRAY_TYPE_ID)
);

create table CATISSUE_SPECIMEN_PROTOCOL (
   IDENTIFIER number(19,0) not null ,
   PRINCIPAL_INVESTIGATOR_ID number(19,0),
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
   IDENTIFIER number(19,0) not null,
   CELL_COUNT double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_BIOHAZARD (
   IDENTIFIER number(19,0) not null ,
   NAME varchar(255) not null unique,
   COMMENTS varchar2(500),
   TYPE varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_QUANTITY (
   IDENTIFIER number(19,0) not null ,
   QUANTITY double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_SPUN_EVENT_PARAMETERS (
   IDENTIFIER number(19,0) not null,
   GFORCE double precision,
   DURATION_IN_MINUTES integer,
   primary key (IDENTIFIER)
);
create table CATISSUE_RECEIVED_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   RECEIVED_QUALITY varchar(255),
   primary key (IDENTIFIER)
);
create table CATISSUE_RACE (
   IDENTIFIER number(19,0) not null,
   PARTICIPANT_ID number(19,0) not null,
   RACE_NAME varchar(50),
   primary key (IDENTIFIER)
);
/*create table CATISSUE_COLL_SPECIMEN_REQ (
   COLLECTION_PROTOCOL_EVENT_ID number(19,0) not null,
   SPECIMEN_REQUIREMENT_ID number(19,0) not null,
   primary key (COLLECTION_PROTOCOL_EVENT_ID, SPECIMEN_REQUIREMENT_ID)
);*/
create table CATISSUE_SPECI_ARRAY_CONTENT (
   IDENTIFIER number(19,0) not null ,
   CONC_IN_MICROGM_PER_MICROLTR double precision,
   INITIAL_QUANTITY double precision,
   POSITION_DIMENSION_ONE integer,
   POSITION_DIMENSION_TWO integer,
   SPECIMEN_ID number(19,0),
   SPECIMEN_ARRAY_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_ADDRESS (
   IDENTIFIER number(19,0) not null ,
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
   IDENTIFIER number(19,0) not null ,
   AFFILIATION varchar(255) not null,
   NAME_OF_REPORTER varchar(255) not null,
   REPORTERS_EMAIL_ID varchar(255) not null,
   MESSAGE_BODY varchar(500) not null,
   SUBJECT varchar(255),
   REPORTED_DATE date,
   ACTIVITY_STATUS varchar(100),
   COMMENTS varchar2(500),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_ARRAY (
   IDENTIFIER number(19,0) not null,
   CREATED_BY_ID number(19,0),
   SPECIMEN_ARRAY_TYPE_ID number(19,0),
   DISTRIBUTION_ID number(19,0),
   AVAILABLE number(1,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_ARRAY_TYPE (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_CLASS varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTED_ITEM (
   IDENTIFIER number(19,0) not null ,
   QUANTITY double precision,
   SPECIMEN_ID number(19,0),
   DISTRIBUTION_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_PARTICIPANT (
   IDENTIFIER number(19,0) not null ,
   LAST_NAME varchar(255),
   FIRST_NAME varchar(255),
   MIDDLE_NAME varchar(255),
   BIRTH_DATE date,
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
   IDENTIFIER number(19,0) not null ,
   TISSUE_SITE varchar(150),
   TISSUE_SIDE varchar(50),
   primary key (IDENTIFIER)
);
/*Ashish Gupta Bug id 2741---Added Association between SCG and Events*/
create table CATISSUE_SPECIMEN_EVENT_PARAM (
   IDENTIFIER number(19,0) not null ,
   SPECIMEN_COLL_GRP_ID number(19,0),
   SPECIMEN_ID number(19,0),
   EVENT_TIMESTAMP date,
   USER_ID number(19,0),
   COMMENTS varchar2(500),
   primary key (IDENTIFIER)
);
create table CATISSUE_STOR_TYPE_SPEC_CLASS (
   STORAGE_TYPE_ID number(19,0) not null,
   SPECIMEN_CLASS varchar(50)
);

create table CATISSUE_CONTAINER_TYPE (
   IDENTIFIER number(19,0) not null ,
   CAPACITY_ID number(19,0),
   NAME varchar(255) unique,
   ONE_DIMENSION_LABEL varchar(255),
   TWO_DIMENSION_LABEL varchar(255),
   COMMENTS varchar2(500),
   ACTIVITY_STATUS VARCHAR(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_CAPACITY (
   IDENTIFIER number(19,0) not null ,
   ONE_DIMENSION_CAPACITY integer,
   TWO_DIMENSION_CAPACITY integer,
   primary key (IDENTIFIER)
);
create table CATISSUE_PART_MEDICAL_ID (
   IDENTIFIER number(19,0) not null ,
   MEDICAL_RECORD_NUMBER varchar(255),
   SITE_ID number(19,0),
   PARTICIPANT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_STORAGE_CONTAINER (
   IDENTIFIER number(19,0) not null,
   SITE_ID number(19,0),
   TEMPERATURE double precision,
   STORAGE_TYPE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_CELL_SPE_REVIEW_PARAM (
   IDENTIFIER number(19,0) not null,
   NEOPLASTIC_CELLULARITY_PER double precision,
   VIABLE_CELL_PERCENTAGE double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTION (
   IDENTIFIER number(19,0) not null ,
   TO_SITE_ID number(19,0),
   DISTRIBUTION_PROTOCOL_ID number(19,0),
   ACTIVITY_STATUS varchar(50),
   SPECIMEN_ID number(19,0),
   EVENT_TIMESTAMP date,
   USER_ID number(19,0),
   COMMENTS varchar2(500),
   primary key (IDENTIFIER)
);
create table CATISSUE_PROCEDURE_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   URL varchar(255) not null,
   NAME varchar(255) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTION_PROTOCOL (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTION_SPEC_REQ (
   IDENTIFIER number(19,0) not null ,
   SPECIMEN_CLASS varchar(255) not null,
   SPECIMEN_TYPE varchar(50),
   TISSUE_SITE varchar(150),
   PATHOLOGY_STATUS varchar(50),
   QUANTITY double precision,
   DISTRIBUTION_PROTOCOL_ID,
   primary key (IDENTIFIER),
   CONSTRAINT FK_DISTRIBUTION_PROTOCOL FOREIGN KEY (DISTRIBUTION_PROTOCOL_ID) REFERENCES CATISSUE_DISTRIBUTION_PROTOCOL
);
create table CATISSUE_EXTERNAL_IDENTIFIER (
   IDENTIFIER number(19,0) not null ,
   NAME varchar(255),
   VALUE varchar(255),
   SPECIMEN_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_DEPARTMENT (
   IDENTIFIER number(19,0) not null ,
   NAME varchar(255) not null unique,
   primary key (IDENTIFIER)
);
create table CATISSUE_FIXED_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   FIXATION_TYPE varchar(50) not null,
   DURATION_IN_MINUTES integer,
   primary key (IDENTIFIER)
);
create table CATISSUE_THAW_EVENT_PARAMETERS (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_PROT_REG (
   IDENTIFIER number(19,0) not null ,
   PROTOCOL_PARTICIPANT_ID varchar(255),
   REGISTRATION_DATE date,
   PARTICIPANT_ID number(19,0),
   COLLECTION_PROTOCOL_ID number(19,0),
   ACTIVITY_STATUS varchar(50),
   CONSENT_SIGN_DATE date,
   CONSENT_DOC_URL varchar2(500),
   CONSENT_WITNESS number(19,0), 
   DATE_OFFSET integer,	
   primary key (IDENTIFIER)
);
create table CATISSUE_FROZEN_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   METHOD varchar(50),
   primary key (IDENTIFIER)
);

create table CATISSUE_USER (
   IDENTIFIER number(19,0) not null ,
   EMAIL_ADDRESS varchar(255),
   FIRST_NAME varchar(255),
   LAST_NAME varchar(255),
   LOGIN_NAME varchar(255) not null unique,
   START_DATE date,
   ACTIVITY_STATUS varchar(50),
   DEPARTMENT_ID number(19,0),
   CANCER_RESEARCH_GROUP_ID number(19,0),
   INSTITUTION_ID number(19,0),
   ADDRESS_ID number(19,0),
   CSM_USER_ID number(19,0),
   STATUS_COMMENT varchar2(500),
   FIRST_TIME_LOGIN number(1,0) default 1,
   primary key (IDENTIFIER)
);
create table CATISSUE_TIS_SPE_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   NEOPLASTIC_CELLULARITY_PER double precision,
   NECROSIS_PERCENTAGE double precision,
   LYMPHOCYTIC_PERCENTAGE double precision,
   TOTAL_CELLULARITY_PERCENTAGE double precision,
   HISTOLOGICAL_QUALITY varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_ST_CONT_COLL_PROT_REL (
   STORAGE_CONTAINER_ID number(19,0) not null,
   COLLECTION_PROTOCOL_ID number(19,0) not null,
   primary key (STORAGE_CONTAINER_ID, COLLECTION_PROTOCOL_ID)
);

create table CATISSUE_ABS_SPECI_COLL_GROUP (
   IDENTIFIER number(19,0) not null ,
   CLINICAL_DIAGNOSIS varchar(150),
   CLINICAL_STATUS varchar(50),
   ACTIVITY_STATUS varchar(50),
   SITE_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_COLL_PROT_EVENT (
   IDENTIFIER number(19,0) not null ,
   CLINICAL_STATUS varchar(50),
   COLLECTION_POINT_LABEL varchar(255),
   STUDY_CALENDAR_EVENT_POINT double precision,
   COLLECTION_PROTOCOL_ID number(19,0),
   primary key (IDENTIFIER),
   unique (COLLECTION_PROTOCOL_ID,COLLECTION_POINT_LABEL),
   CONSTRAINT FK7AE7715948304401 FOREIGN KEY (COLLECTION_PROTOCOL_ID) REFERENCES catissue_collection_protocol,
   CONSTRAINT FK_PARENT_COLL_PROT_EVENT FOREIGN KEY (IDENTIFIER) REFERENCES catissue_abs_speci_coll_group
);

create table CATISSUE_SPECIMEN_COLL_GROUP (
   IDENTIFIER number(19,0) not null ,
   NAME varchar(255) unique,  
   COMMENTS varchar2(2000),
   COLLECTION_PROTOCOL_REG_ID number(19,0),
   SURGICAL_PATHOLOGY_NUMBER varchar(50),
   COLLECTION_PROTOCOL_EVENT_ID number(19,0),
   COLLECTION_STATUS varchar2(50),
   DATE_OFFSET integer, 	
   primary key (IDENTIFIER),
   CONSTRAINT fkDebaf1677e07c4ac FOREIGN KEY( Collection_Protocol_reg_Id ) REFERENCES CATISSUE_COLL_PROT_REG( IdentIfier ),
   CONSTRAINT fk_Parent_spec_coll_Group FOREIGN KEY( IdentIfier ) REFERENCES CATISSUE_ABS_SPECI_COLL_GROUP( IdentIfier )
);


CREATE TABLE CATISSUE_ABSTRACT_SPECIMEN
(                                                                                                                 
      IDENTIFIER NUMBER(19,0) NOT NULL,    
	  SPECIMEN_CLASS varchar(50) NOT NULL,         
      SPECIMEN_TYPE varchar(50),                                                                                                                
      LINEAGE varchar(50),                                                                                                              
      PATHOLOGICAL_STATUS varchar(50),                                                                                                  
      PARENT_SPECIMEN_ID number(19,0),                                                                                                    
      SPECIMEN_CHARACTERISTICS_ID number(19,0),                                                                                      
      INITIAL_QUANTITY double precision,                                                                                                        
      PRIMARY KEY  (IDENTIFIER),                                                                                                                     
      CONSTRAINT FK1674810456906F39 FOREIGN KEY (SPECIMEN_CHARACTERISTICS_ID) REFERENCES catissue_specimen_char                 
); 

CREATE TABLE CATISSUE_CP_REQ_SPECIMEN
 (                                                                                                                 
     IDENTIFIER NUMBER(19,0) NOT NULL,         
     STORAGE_TYPE varchar(255) NOT NULL,      
	 COLLECTION_PROTOCOL_EVENT_ID number(19,0),
     PRIMARY KEY  (IDENTIFIER),                                                                                                                     
     CONSTRAINT FK111110456906F39 FOREIGN KEY (COLLECTION_PROTOCOL_EVENT_ID) REFERENCES catissue_coll_prot_event,
	 CONSTRAINT FK_PARENT_CP_REQ_SPECIMEN FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_ABSTRACT_SPECIMEN
 ); 
 
 
 CREATE TABLE CATISSUE_MOL_REQ_SPECIMEN
 (                                                                                                                 
     IDENTIFIER NUMBER(19,0) NOT NULL,   
	 CONCENTRATION double precision,
     PRIMARY KEY  (IDENTIFIER),                                                                                                                     
     CONSTRAINT FK_MOL_REQ_SPECIMEN FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_CP_REQ_SPECIMEN                
 );
CREATE TABLE CATISSUE_FLUID_REQ_SPECIMEN
(                                                                                                                 
     IDENTIFIER NUMBER(19,0) NOT NULL,       
     PRIMARY KEY  (IDENTIFIER),                                                                                                                     
     CONSTRAINT FK_FLUID_REQ_SPECIMEN FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_CP_REQ_SPECIMEN
);
CREATE TABLE CATISSUE_CELL_REQ_SPECIMEN
(                                                                                                                 
     IDENTIFIER NUMBER(19,0) NOT NULL,       
      PRIMARY KEY  (IDENTIFIER),                                                                                                                     
      CONSTRAINT FK_CELL_REQ_SPECIMEN FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_CP_REQ_SPECIMEN
                
);
CREATE TABLE CATISSUE_TISSUE_REQ_SPECIMEN
(                                                                                                                 
     IDENTIFIER NUMBER(19,0) NOT NULL,       
      PRIMARY KEY  (IDENTIFIER),                                                                                                                     
      CONSTRAINT FK_TISSUE_REQ_SPECIMEN FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_CP_REQ_SPECIMEN               
);

CREATE TABLE CATISSUE_SPECIMEN
(                                                                                                                 
     IDENTIFIER NUMBER(19,0) NOT NULL,
     LABEL varchar(255) unique,                                                                                                               
     AVAILABLE number(1,0),                                                                                                         
     BARCODE varchar(255) unique,
     COMMENTS varchar2(500),
     ACTIVITY_STATUS varchar(50),                                                                                              
     SPECIMEN_COLLECTION_GROUP_ID number(19,0),     
     REQ_SPECIMEN_ID number(19,0),     
     AVAILABLE_QUANTITY double precision,                                                                                                        
     CREATED_ON_DATE date ,                                                                                                             
     COLLECTION_STATUS varchar(50),                                                                                                    
     PRIMARY KEY  (IDENTIFIER),                                                                                                                     
     CONSTRAINT FK1674810433BF33C5 FOREIGN KEY (SPECIMEN_COLLECTION_GROUP_ID) REFERENCES catissue_specimen_coll_group,
     CONSTRAINT FK_REQ_SPECIMEN_ID FOREIGN KEY (REQ_SPECIMEN_ID) REFERENCES CATISSUE_CP_REQ_SPECIMEN,
 	 CONSTRAINT FK_PARENT_SPECIMEN FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_ABSTRACT_SPECIMEN
   ); 
   
   CREATE TABLE CATISSUE_MOLECULAR_SPECIMEN
   (                                                                                                                 
     IDENTIFIER NUMBER(19,0) NOT NULL,  
     CONCENTRATION double precision,    
     PRIMARY KEY  (IDENTIFIER),                                                                                                                     
     CONSTRAINT FK_MOLECULAR_SPECIMEN FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_SPECIMEN             
   );

CREATE TABLE CATISSUE_FLUID_SPECIMEN
(                                                                                                                 
     IDENTIFIER NUMBER(19,0) NOT NULL ,       
      PRIMARY KEY  (IDENTIFIER),                                                                                                                     
      CONSTRAINT FK_FLUID_SPECIMEN FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_SPECIMEN                
);
CREATE TABLE CATISSUE_CELL_SPECIMEN
(                                                                                                                 
     IDENTIFIER NUMBER(19,0) NOT NULL ,       
      PRIMARY KEY  (IDENTIFIER),                                                                                                                     
      CONSTRAINT FK_CELL_SPECIMEN FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_SPECIMEN
);
CREATE TABLE CATISSUE_TISSUE_SPECIMEN
(                                                                                                                 
     IDENTIFIER NUMBER(19,0) NOT NULL ,       
      PRIMARY KEY  (IDENTIFIER),                                                                                                                     
      CONSTRAINT FK_TISSUE_SPECIMEN FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_SPECIMEN
);
create table CATISSUE_SPECIMEN_TYPE (
   SPECIMEN_ARRAY_TYPE_ID number(19,0) not null,
   SPECIMEN_TYPE varchar(50)
);
create table CATISSUE_AUDIT_EVENT_QUERY_LOG (
   IDENTIFIER number(19,0) not null,
   QUERY_DETAILS clob,  
   AUDIT_EVENT_ID number(19,0),
   primary key (IDENTIFIER)
);

alter table CATISSUE_AUDIT_EVENT_QUERY_LOG add constraint FK62DC439DBC7298A9 foreign key (AUDIT_EVENT_ID) references CATISSUE_AUDIT_EVENT ;
alter table CATISSUE_COLL_COORDINATORS add constraint FKE490E33A48304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL ;
alter table CATISSUE_COLL_COORDINATORS  add constraint FKE490E33A2206F20F foreign key (USER_ID) references CATISSUE_USER ;
alter table CATISSUE_COLLECTION_PROTOCOL  add constraint FK32DC439DBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_PROTOCOL  ;
alter table CATISSUE_COLLECTION_PROTOCOL  add constraint FK32DC439DBC7298B9 foreign key (PARENT_CP_ID) references CATISSUE_COLLECTION_PROTOCOL;
alter table CATISSUE_EVENT_PARAM  add constraint FK90C79AECBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_TRANSFER_EVENT_PARAM  add constraint FK71F9AC103C2DAC61 foreign key (TO_STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER  ;
alter table CATISSUE_TRANSFER_EVENT_PARAM  add constraint FK71F9AC1099DF0A92 foreign key (FROM_STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER  ;
alter table CATISSUE_TRANSFER_EVENT_PARAM  add constraint FK71F9AC10BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_STOR_CONT_SPEC_CLASS  add constraint FKE7F5E8C2B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER  ;
alter table CATISSUE_COLL_EVENT_PARAM  add constraint FKF9888F91BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_PASSWORD  add constraint FKDE1F38972206F20F foreign key (USER_ID) references CATISSUE_USER  ;
alter table CATISSUE_SPECIMEN_BIOHZ_REL  add constraint FK7A3F5539F398D480 foreign key (BIOHAZARD_ID) references CATISSUE_BIOHAZARD  ;
alter table CATISSUE_SPECIMEN_BIOHZ_REL  add constraint FK7A3F553960773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN  ;
alter table CATISSUE_MOL_SPE_REVIEW_PARAM  add constraint FK5280ECEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM  ;
alter table CATISSUE_STORAGE_TYPE  add constraint FKE9A0629ABC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER_TYPE  ;
alter table CATISSUE_CONTAINER  add constraint FK49B8DE5DAC76C0 foreign key (CAPACITY_ID) references CATISSUE_CAPACITY  ;
alter table CATISSUE_SITE  add constraint FKB024C3436CD94566 foreign key (ADDRESS_ID) references CATISSUE_ADDRESS  ;
alter table CATISSUE_SITE  add constraint FKB024C3432206F20F foreign key (USER_ID) references CATISSUE_USER  ;
alter table CATISSUE_EMBEDDED_EVENT_PARAM  add constraint FKD356182FBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_IN_OUT_EVENT_PARAM  add constraint FK4F0FAEB9BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_COLL_DISTRIBUTION_REL  add constraint FK1483BCB56B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL  ;
alter table CATISSUE_COLL_DISTRIBUTION_REL  add constraint FK1483BCB548304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL  ;
alter table CATISSUE_DISPOSAL_EVENT_PARAM  add constraint FK1BC818D6BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_ST_CONT_ST_TYPE_REL  add constraint FK703B902159A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE  ;
alter table CATISSUE_ST_CONT_ST_TYPE_REL  add constraint FK703B9021B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER  ;
alter table CATISSUE_STOR_TYPE_HOLDS_TYPE  add constraint FK185C50B59A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE  ;
alter table CATISSUE_STOR_TYPE_HOLDS_TYPE  add constraint FK185C50B81236791 foreign key (HOLDS_STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE  ;
alter table CATISSUE_STORTY_HOLDS_SPARRTY  add constraint FK70F57E4459A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE  ;
alter table CATISSUE_STORTY_HOLDS_SPARRTY  add constraint FK70F57E44ECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE  ;
alter table CATISSUE_CONT_HOLDS_SPARRTYPE  add constraint FKDC7E31E2ECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE  ;
alter table CATISSUE_CONT_HOLDS_SPARRTYPE  add constraint FKDC7E31E2B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER  ;
alter table CATISSUE_SPECIMEN_PROTOCOL add constraint FKB8481373870EB740 foreign key (PRINCIPAL_INVESTIGATOR_ID) references CATISSUE_USER  ;
alter table CATISSUE_FLUID_SPE_EVENT_PARAM  add constraint FK70565D20BC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM  ;
alter table CATISSUE_SPUN_EVENT_PARAMETERS  add constraint FK312D77BCBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_RECEIVED_EVENT_PARAM  add constraint FKA7139D06BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_RACE  add constraint FKB0242ECD87E5ADC7 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT  ;
/*alter table CATISSUE_COLL_SPECIMEN_REQ  add constraint FK860E6ABEBE10F0CE foreign key (SPECIMEN_REQUIREMENT_ID) references CATISSUE_SPECIMEN_REQUIREMENT  ;*/
/*alter table CATISSUE_COLL_SPECIMEN_REQ  add constraint FK860E6ABE53B01F66 foreign key (COLLECTION_PROTOCOL_EVENT_ID) references CATISSUE_COLL_PROT_EVENT  ;*/
alter table CATISSUE_SPECI_ARRAY_CONTENT  add constraint FKBEA9D458C4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY  ;
alter table CATISSUE_SPECI_ARRAY_CONTENT  add constraint FKBEA9D45860773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN  ;
alter table CATISSUE_SPECIMEN_ARRAY  add constraint FKECBF8B3E64B129CC foreign key (CREATED_BY_ID) references CATISSUE_USER  ;
alter table CATISSUE_SPECIMEN_ARRAY  add constraint FKECBF8B3EF8278B6 foreign key (DISTRIBUTION_ID) references CATISSUE_DISTRIBUTION  ;
alter table CATISSUE_SPECIMEN_ARRAY  add constraint FKECBF8B3EBC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER  ;
alter table CATISSUE_SPECIMEN_ARRAY  add constraint FKECBF8B3EECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE  ;
alter table CATISSUE_SPECIMEN_ARRAY_TYPE  add constraint FKD36E0B9BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER_TYPE  ;
alter table CATISSUE_DISTRIBUTED_ITEM  add constraint FKA7C3ED4B60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN  ;
alter table CATISSUE_DISTRIBUTED_ITEM  add constraint FKA7C3ED4BF8278B6 foreign key (DISTRIBUTION_ID) references CATISSUE_DISTRIBUTION  ;
alter table CATISSUE_SPECIMEN_EVENT_PARAM  add constraint FK753F33AD8CA560D1 foreign key (SPECIMEN_COLL_GRP_ID) references CATISSUE_SPECIMEN_COLL_GROUP;
alter table CATISSUE_SPECIMEN_EVENT_PARAM  add constraint FK753F33AD60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_ABSTRACT_SPECIMEN;
alter table CATISSUE_SPECIMEN_EVENT_PARAM  add constraint FK753F33AD2206F20F foreign key (USER_ID) references CATISSUE_USER  ;
alter table CATISSUE_STOR_TYPE_SPEC_CLASS  add constraint FK1BCF33BA59A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE  ;
alter table CATISSUE_CONTAINER_TYPE  add constraint FKCBBC9954DAC76C0 foreign key (CAPACITY_ID) references CATISSUE_CAPACITY  ;
alter table CATISSUE_PART_MEDICAL_ID  add constraint FK349E77F9A7F77D13 foreign key (SITE_ID) references CATISSUE_SITE  ;
alter table CATISSUE_PART_MEDICAL_ID  add constraint FK349E77F987E5ADC7 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT  ;
alter table CATISSUE_STORAGE_CONTAINER  add constraint FK28429D01BC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER  ;
alter table CATISSUE_STORAGE_CONTAINER  add constraint FK28429D01A7F77D13 foreign key (SITE_ID) references CATISSUE_SITE  ;
alter table CATISSUE_STORAGE_CONTAINER  add constraint FK28429D0159A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE  ;
alter table CATISSUE_CELL_SPE_REVIEW_PARAM  add constraint FK52F40EDEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM  ;
alter table CATISSUE_DISTRIBUTION  add constraint FK542766802206F20F foreign key (USER_ID) references CATISSUE_USER  ;
alter table CATISSUE_DISTRIBUTION  add constraint FK542766806B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL  ;
alter table CATISSUE_DISTRIBUTION  add constraint FK542766801DBE834F foreign key (TO_SITE_ID) references CATISSUE_SITE  ;
alter table CATISSUE_DISTRIBUTION  add constraint FK5427668060773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN  ;
alter table CATISSUE_PROCEDURE_EVENT_PARAM  add constraint FKEC6B4260BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_DISTRIBUTION_PROTOCOL  add constraint FKC8999977BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_PROTOCOL  ;
alter table CATISSUE_EXTERNAL_IDENTIFIER  add constraint FK5CF2FA2160773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN  ;
alter table CATISSUE_FIXED_EVENT_PARAM  add constraint FKE0F1781BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_THAW_EVENT_PARAMETERS  add constraint FKD8890A48BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_COLL_PROT_REG  add constraint FK5EB25F1387E5ADC7 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT  ;
alter table CATISSUE_COLL_PROT_REG  add constraint FK5EB25F1348304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL  ;
alter table CATISSUE_FROZEN_EVENT_PARAM  add constraint FK52627245BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM  ;
alter table CATISSUE_USER  add constraint FKB025CFC71792AD22 foreign key (INSTITUTION_ID) references CATISSUE_INSTITUTION  ;
alter table CATISSUE_USER  add constraint FKB025CFC7FFA96920 foreign key (CANCER_RESEARCH_GROUP_ID) references CATISSUE_CANCER_RESEARCH_GROUP  ;
alter table CATISSUE_USER  add constraint FKB025CFC76CD94566 foreign key (ADDRESS_ID) references CATISSUE_ADDRESS  ;
alter table CATISSUE_USER  add constraint FKB025CFC7F30C2528 foreign key (DEPARTMENT_ID) references CATISSUE_DEPARTMENT  ;
alter table CATISSUE_TIS_SPE_EVENT_PARAM  add constraint FKBB9648F4BC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM  ;
alter table CATISSUE_ST_CONT_COLL_PROT_REL  add constraint FK3AE9FCA7B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER  ;
alter table CATISSUE_ST_CONT_COLL_PROT_REL  add constraint FK3AE9FCA748304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL  ;
alter table CATISSUE_ABS_SPECI_COLL_GROUP add constraint FKDEBAF167A7F77D13 foreign key (SITE_ID) references CATISSUE_SITE  ;
alter table CATISSUE_SPECIMEN_COLL_GROUP  add constraint FKDEBAF16753B01F66 foreign key (COLLECTION_PROTOCOL_EVENT_ID) references CATISSUE_COLL_PROT_EVENT  ;
alter table CATISSUE_SPECIMEN_TYPE  add constraint FKFF69C195ECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE  ;
alter table CATISSUE_PERMISSIBLE_VALUE  add constraint FK57DDCE153B5435E foreign key (PARENT_IDENTIFIER) references CATISSUE_PERMISSIBLE_VALUE  ;
alter table CATISSUE_PERMISSIBLE_VALUE  add constraint FK57DDCE1FC56C2B1 foreign key (PUBLIC_ID) references CATISSUE_CDE ;
alter table CATISSUE_AUDIT_EVENT  add constraint FKACAF697A2206F20F foreign key (USER_ID) references CATISSUE_USER  ;
alter table CATISSUE_AUDIT_EVENT_LOG  add constraint FK8BB672DF77F0B904 foreign key (AUDIT_EVENT_ID) references CATISSUE_AUDIT_EVENT  ;
alter table CATISSUE_AUDIT_EVENT_DETAILS  add constraint FK5C07745D34FFD77F foreign key (AUDIT_EVENT_LOG_ID) references CATISSUE_AUDIT_EVENT_LOG  ;

alter table CATISSUE_COLL_PROT_REG add constraint FK5EB25F13A0FF79D4 foreign key (CONSENT_WITNESS) references CATISSUE_USER;


create sequence CATISSUE_CANCER_RES_GRP_SEQ;
create sequence CATISSUE_USER_SEQ;
create sequence CATISSUE_SPECIMEN_PROTOCOL_SEQ;
create sequence CATISSUE_CAPACITY_SEQ;
create sequence CATISSUE_COLL_PROT_REG_SEQ;
create sequence CATISSUE_SPEC_EVENT_PARAM_SEQ;
create sequence CATISSUE_SITE_SEQ;
create sequence CATISSUE_ADDRESS_SEQ;
create sequence CATISSUE_CONTAINER_TYPE_SEQ;
create sequence CATISSUE_AUDIT_EVENT_LOG_SEQ;
create sequence CATISSUE_INTF_COLUMN_DATA_SEQ;
create sequence CATISSUE_DISTRIBUTED_ITEM_SEQ;
create sequence CATISSUE_DEPARTMENT_SEQ;
create sequence CATISSUE_QUERY_TABLE_DATA_SEQ;
create sequence CATISSUE_AUDIT_EVENT_DET_SEQ;
create sequence CATISSUE_EXTERNAL_ID_SEQ;
create sequence CATISSUE_REPORTED_PROBLEM_SEQ;
create sequence CATISSUE_SPECIMEN_CHAR_SEQ;
create sequence CATISSUE_AUDIT_EVENT_PARAM_SEQ;
create sequence CATISSUE_PARTICIPANT_SEQ;
create sequence CATISSUE_STORAGE_CONTAINER_SEQ;
create sequence CATISSUE_PART_MEDICAL_ID_SEQ;
create sequence CATISSUE_COLL_PROT_EVENT_SEQ;
create sequence CATISSUE_BIOHAZARD_SEQ;
create sequence CATISSUE_INSTITUTION_SEQ;
create sequence CATISSUE_SPECIMEN_REQ_SEQ;
create sequence CATISSUE_SPECIMEN_COLL_GRP_SEQ;
create sequence CATISSUE_SPECIMEN_SEQ;
create sequence CATISSUE_PERMISSIBLE_VALUE_SEQ;
create sequence CATISSUE_QUANTITY_SEQ;
create sequence CATISSUE_AUDIT_EVENT_SEQ;
create sequence CATISSUE_PASSWORD_SEQ;
create sequence CATISSUE_CONTAINER_SEQ;
create sequence CATISSUE_SPECI_ARRAY_CNTNT_SEQ;
create sequence CATISSUE_DISTRIBUTION_SEQ;
create sequence CATISSUE_AUDIT_EVENT_QUERY_SEQ;
create sequence CATISSUE_DIST_SPECIMEN_REQ_SEQ;
create sequence CATISSUE_RACE_SEQ;

/* Consent Tracking related drop, create and add foreign key scripts. */

create table CATISSUE_CONSENT_TIER_RESPONSE (
   IDENTIFIER number(19,0) not null,
   RESPONSE varchar(255),
   CONSENT_TIER_ID number(19,0),
   COLL_PROT_REG_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_CONSENT_TIER_STATUS (
   IDENTIFIER number(19,0) not null,
   CONSENT_TIER_ID number(19,0),
   STATUS varchar(255),
   SPECIMEN_ID number(19,0),
   SPECIMEN_COLL_GROUP_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_CONSENT_TIER (
   IDENTIFIER number(19,0) not null,
   STATEMENT varchar2(500),
   COLL_PROTOCOL_ID number(19,0),
   primary key (IDENTIFIER)
);

alter table CATISSUE_CONSENT_TIER_RESPONSE  add constraint FKFB1995FD4AD77FCB foreign key (COLL_PROT_REG_ID) references CATISSUE_COLL_PROT_REG (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_RESPONSE  add constraint FKFB1995FD17B9953 foreign key (CONSENT_TIER_ID) references CATISSUE_CONSENT_TIER (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS  add constraint FKF74E94AEF69249F7 foreign key (SPECIMEN_COLL_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS  add constraint FKF74E94AE60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER_STATUS  add constraint FKF74E94AE17B9953 foreign key (CONSENT_TIER_ID) references CATISSUE_CONSENT_TIER (IDENTIFIER);
alter table CATISSUE_CONSENT_TIER  add constraint FK51725303E36A4B4F foreign key (COLL_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);

create sequence CATISSUE_CONSENT_TIER_RES_SEQ;
create sequence CATISSUE_CONSENT_TIER_STAT_SEQ;
create sequence CATISSUE_CONSENT_TIER_SEQ;
 /* ordering */
create sequence CATISSUE_ORDER_SEQ;
create sequence CATISSUE_ORDER_ITEM_SEQ;

/* Specimen Order */

create table CATISSUE_PATH_CASE_ORDER_ITEM (
   IDENTIFIER number(19,0) not null,
   PATHOLOGICAL_STATUS varchar(255),
   TISSUE_SITE varchar(255),
   SPECIMEN_COLL_GROUP_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_DERIEVED_SP_ORD_ITEM (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_ID number(19,0),
   primary key (IDENTIFIER)
);


create table CATISSUE_NEW_SPEC_ORD_ITEM (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_CLASS varchar(255),
   SPECIMEN_TYPE varchar(255),
   primary key (IDENTIFIER)
);

create table CATISSUE_EXISTING_SP_ORD_ITEM (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_SPECIMEN_ORDER_ITEM (
   IDENTIFIER number(19,0) not null,
   ARRAY_ORDER_ITEM_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_EXIST_SP_AR_ORD_ITEM (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_ARRAY_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_NEW_SP_AR_ORDER_ITEM (
   IDENTIFIER number(19,0) not null,
   ARRAY_TYPE_ID number(19,0),
   NAME varchar(255),
   SPECIMEN_ARRAY_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_SP_ARRAY_ORDER_ITEM (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);

create table CATISSUE_ORDER_ITEM (
   IDENTIFIER number(19,0) not null,
   DESCRIPTION varchar2(500),
   DISTRIBUTED_ITEM_ID number(19,0),
   STATUS varchar(50),
   REQUESTED_QUANTITY double precision,
   ORDER_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_ORDER (
   IDENTIFIER number(19,0) not null,
   COMMENTS varchar2(500),
   DISTRIBUTION_PROTOCOL_ID number(19,0),
   NAME varchar2(500),
   REQUESTED_DATE date,
   STATUS varchar(50),
   primary key (IDENTIFIER)
);


/* extra for catissue_distribution */
 alter table catissue_distribution add ORDER_ID number(19,0);
 alter table catissue_distributed_item add  SPECIMEN_ARRAY_ID number(19,0);    
/* extra finished */

alter table CATISSUE_PATH_CASE_ORDER_ITEM add constraint FKBD5029D5F69249F7 foreign key (SPECIMEN_COLL_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_PATH_CASE_ORDER_ITEM add constraint FKBD5029D5BC7298A9 foreign key (IDENTIFIER) references CATISSUE_NEW_SPEC_ORD_ITEM (IDENTIFIER);

alter table CATISSUE_DERIEVED_SP_ORD_ITEM add constraint FK3742152BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_NEW_SPEC_ORD_ITEM (IDENTIFIER);
alter table CATISSUE_DERIEVED_SP_ORD_ITEM add constraint FK3742152B60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);

alter table CATISSUE_NEW_SPEC_ORD_ITEM add constraint FK_NW_SPEC_ODR_ITM foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_ORDER_ITEM (IDENTIFIER);

alter table CATISSUE_EXISTING_SP_ORD_ITEM add constraint FKF8B855EEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_EXISTING_SP_ORD_ITEM add constraint FKF8B855EE60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);

alter table CATISSUE_SPECIMEN_ORDER_ITEM add constraint FK48C3B39FBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ORDER_ITEM add constraint FK48C3B39F83505A30 foreign key (ARRAY_ORDER_ITEM_ID) references CATISSUE_NEW_SP_AR_ORDER_ITEM (IDENTIFIER);


alter table CATISSUE_EXIST_SP_AR_ORD_ITEM add constraint FKE3823170BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SP_ARRAY_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_EXIST_SP_AR_ORD_ITEM add constraint FKE3823170C4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);

alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add constraint FKC5C92CCBCE5FBC3A foreign key (ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add constraint FKC5C92CCBBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SP_ARRAY_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add constraint FKC5C92CCBC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);

alter table CATISSUE_SP_ARRAY_ORDER_ITEM add constraint FK_SP_ARAY_ODR_ITM foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);

alter table CATISSUE_ORDER_ITEM add constraint FKB501E88060975C0B foreign key (DISTRIBUTED_ITEM_ID) references CATISSUE_DISTRIBUTED_ITEM (IDENTIFIER);
alter table CATISSUE_ORDER_ITEM add constraint FKB501E880783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_ORDER add constraint FK543F22B26B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL (IDENTIFIER);

alter table CATISSUE_DISTRIBUTION add constraint FK54276680783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_DISTRIBUTED_ITEM add constraint FKA7C3ED4BC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY;


create table CATISSUE_SPECIMEN_LABEL_COUNT (
   LABEL_COUNT number(19,0) not null,
   primary key (LABEL_COUNT)
);
INSERT INTO CATISSUE_SPECIMEN_LABEL_COUNT (LABEL_COUNT) VALUES ('0');

/****caTIES Realated Tables - start**********/

create table CATISSUE_REPORT_TEXTCONTENT (
   IDENTIFIER number(19,0) not null,
   REPORT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_IDENTIFIED_REPORT (
   IDENTIFIER number(19,0) not null,
   DEID_REPORT number(19,0),
   SCG_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_CONCEPT_REFERENT (
   IDENTIFIER number(19,0) not null,
   CONCEPT_ID number(19,0),
   CONCEPT_CLASSIFICATION_ID number(19,0),
   DEIDENTIFIED_REPORT_ID number(19,0),
   END_OFFSET number(19,0),
   IS_MODIFIER number(1,0),
   IS_NEGATED number(1,0),
   START_OFFSET number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_CONTENT (
   IDENTIFIER number(19,0) not null,
   REPORT_DATA clob,
   primary key (IDENTIFIER)
);
create table CATISSUE_REVIEW_PARAMS (
   IDENTIFIER number(19,0) not null,
   REVIEWER_ROLE varchar(100),
   REPORT_ID number(19,0),
   EVENT_TIMESTAMP timestamp,
   USER_ID number(19,0),
   COMMENTS varchar2(4000),
   STATUS varchar(100),
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_BICONTENT (
   IDENTIFIER number(19,0) not null,
   REPORT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_SECTION (
   IDENTIFIER number(19,0) not null,
   DOCUMENT_FRAGMENT varchar2(4000),
   END_OFFSET integer,
   NAME varchar(100),
   START_OFFSET integer,
   TEXT_CONTENT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_DEIDENTIFIED_REPORT (
   IDENTIFIER number(19,0) not null,
   STATUS varchar(100),
   SCG_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_QUARANTINE_PARAMS (
   IDENTIFIER number(19,0) not null,
   DEID_REPORT_ID number(19,0),
   IS_QUARANTINED number(1),
   EVENT_TIMESTAMP timestamp,
   USER_ID number(19,0),
   COMMENTS varchar2(4000),
   STATUS varchar(100),
   primary key (IDENTIFIER)
);
create table CATISSUE_PATHOLOGY_REPORT (
   IDENTIFIER number(19,0) not null,
   ACTIVITY_STATUS varchar(100),
   REVIEW_FLAG number(1),
   SOURCE_ID number(19,0),
   REPORT_STATUS varchar(100),
   COLLECTION_DATE_TIME date,
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_XMLCONTENT (
   IDENTIFIER number(19,0) not null,
   REPORT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_QUEUE (
   IDENTIFIER number(19,0) not null,
   STATUS varchar(50),
   SURGICAL_PATHOLOGY_NUMBER varchar(50),
   PARTICIPANT_NAME varchar(255),
   SITE_NAME varchar(255),
   REPORT_LOADED_DATE date,
   REPORT_TEXT clob,
   SPECIMEN_COLL_GRP_ID number(19,0),
   REPORT_COLLECTION_DATE date,
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORT_PARTICIP_REL(
   PARTICIPANT_ID number(19,0),
   REPORT_ID number(19,0)
);
create table CATISSUE_CONCEPT (
   IDENTIFIER number(19,0) not null,
   CONCEPT_UNIQUE_ID varchar(30),
   NAME varchar2(500),
   SEMANTIC_TYPE_ID number(19,0),
   primary key (IDENTIFIER)
);

create table CATISSUE_SEMANTIC_TYPE (
   IDENTIFIER number(19,0) not null,
   LABEL varchar2(500),
   primary key (IDENTIFIER)
);

create table CATISSUE_CONCEPT_CLASSIFICATN (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(500),
   primary key (IDENTIFIER)
);


alter table CATISSUE_REPORT_TEXTCONTENT add constraint FKD74882FD91092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_TEXTCONTENT add constraint FKD74882FDBC7298A9 foreign key (IDENTIFIER) references CATISSUE_REPORT_CONTENT (IDENTIFIER);
alter table CATISSUE_IDENTIFIED_REPORT add constraint FK6A2246DCBC7298A9 foreign key (IDENTIFIER) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_IDENTIFIED_REPORT add constraint FK6A2246DC752DD177 foreign key (DEID_REPORT) references CATISSUE_DEIDENTIFIED_REPORT (IDENTIFIER);
alter table CATISSUE_IDENTIFIED_REPORT add constraint FK6A2246DC91741663 foreign key (SCG_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add constraint FK799CCA7E9F96B363 foreign key (DEIDENTIFIED_REPORT_ID) references CATISSUE_DEIDENTIFIED_REPORT (IDENTIFIER);
alter table CATISSUE_REVIEW_PARAMS add constraint FK5311FFF62206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_REVIEW_PARAMS add constraint FK5311FFF691092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_BICONTENT add constraint FK8A9A4EE391092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_BICONTENT add constraint FK8A9A4EE3BC7298A9 foreign key (IDENTIFIER) references CATISSUE_REPORT_CONTENT (IDENTIFIER);
alter table CATISSUE_DEIDENTIFIED_REPORT add constraint FKCDD0DF7BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_DEIDENTIFIED_REPORT add constraint FKCDD0DF7B91741663 foreign key (SCG_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_QUARANTINE_PARAMS add constraint FK3C12AE3B2206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_QUARANTINE_PARAMS add constraint FK3C12AE3B3EEC14E3 foreign key (DEID_REPORT_ID) references CATISSUE_DEIDENTIFIED_REPORT (IDENTIFIER);
alter table CATISSUE_PATHOLOGY_REPORT add constraint FK904EC9F040DCD7BF foreign key (SOURCE_ID) references CATISSUE_SITE (IDENTIFIER);
alter table CATISSUE_REPORT_XMLCONTENT add constraint FK4597C9F1BC7298A9 foreign key (IDENTIFIER) references CATISSUE_REPORT_CONTENT (IDENTIFIER);
alter table CATISSUE_REPORT_XMLCONTENT add constraint FK4597C9F191092806 foreign key (REPORT_ID) references CATISSUE_PATHOLOGY_REPORT (IDENTIFIER);
alter table CATISSUE_REPORT_QUEUE add constraint FK214246228CA560D1 foreign key (SPECIMEN_COLL_GRP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_CONCEPT add constraint FKC1A3C8CC7F0C2C7 foreign key (SEMANTIC_TYPE_ID) references CATISSUE_SEMANTIC_TYPE (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add constraint FK799CCA7EA9816272 foreign key (CONCEPT_ID) references CATISSUE_CONCEPT (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add constraint FK799CCA7E72C371DD foreign key (CONCEPT_CLASSIFICATION_ID) references CATISSUE_CONCEPT_CLASSIFICATN (IDENTIFIER);

create sequence CATISSUE_CONCEPT_REFERENT_SEQ;
create sequence CATISSUE_PATHOLOGY_REPORT_SEQ;
create sequence CATISSUE_QUARANTINE_PARAMS_SEQ;
create sequence CATISSUE_REPORT_SECTION_SEQ;
create sequence CATISSUE_REVIEW_PARAMS_SEQ;
create sequence CATISSUE_REPORT_CONTENT_SEQ;
create sequence CATISSUE_REPORT_QUEUE_SEQ;
create sequence CATISSUE_SEMANTIC_TYPE_SEQ;
create sequence CATISSUE_CONCEPT_SEQ;
create sequence CATISSUE_CONCEPT_CLASSFCTN_SEQ;

/****caTIES Realated Tables - end**********/

/* Suite 1.1 model changes */
create table CATISSUE_ABSTRACT_POSITION (
	IDENTIFIER number(19,0) NOT NULL,
	POSITION_DIMENSION_ONE INTEGER,
	POSITION_DIMENSION_TWO INTEGER,
	primary key (IDENTIFIER)
);

create table CATISSUE_SPECIMEN_POSITION(
	IDENTIFIER number(19,0) NOT NULL,
	SPECIMEN_ID number(19,0),
	CONTAINER_ID number(19,0),
	primary key (IDENTIFIER),
	CONSTRAINT FK_SPECIMEN_POSITION FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_ABSTRACT_POSITION,
	CONSTRAINT FK_SPECIMEN FOREIGN KEY (SPECIMEN_ID) REFERENCES CATISSUE_SPECIMEN,
	CONSTRAINT FK_STORAGE_CONTAINER FOREIGN KEY (CONTAINER_ID) REFERENCES CATISSUE_STORAGE_CONTAINER
);

create table CATISSUE_CONTAINER_POSITION(
	IDENTIFIER number(19,0) NOT NULL,
	PARENT_CONTAINER_ID number(19,0),
	CONTAINER_ID number(19,0),
	primary key (IDENTIFIER),
	CONSTRAINT FK_CONTAINER_POSITION FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_ABSTRACT_POSITION,
	CONSTRAINT FK_CONTAINER FOREIGN KEY (CONTAINER_ID) REFERENCES CATISSUE_CONTAINER,
	CONSTRAINT FK_OCCUPIED_CONTAINER FOREIGN KEY (PARENT_CONTAINER_ID) REFERENCES CATISSUE_CONTAINER
);

create sequence CATISSUE_ABS_POSITION_SEQ;

/**Added a workaround so that edit of Container,specimen should work. NEED TO REMOVE **/
ALTER TABLE CATISSUE_SPECIMEN ADD POSITION_DIMENSION_ONE integer;
ALTER TABLE CATISSUE_SPECIMEN ADD POSITION_DIMENSION_TWO integer;
ALTER TABLE CATISSUE_SPECIMEN ADD STORAGE_CONTAINER_IDENTIFIER number(19,0);
ALTER TABLE CATISSUE_CONTAINER ADD POSITION_DIMENSION_ONE integer;
ALTER TABLE CATISSUE_CONTAINER ADD POSITION_DIMENSION_TWO integer;
ALTER TABLE CATISSUE_CONTAINER ADD PARENT_CONTAINER_ID number(19,0);
ALTER TABLE CATISSUE_SPECIMEN_ARRAY ADD STORAGE_CONTAINER_ID number(19,0);

// For specimen Model change

create sequence CATISSUE_ABSTRACT_SPECIMEN_SEQ;

/** MSR changes : Start **/

CREATE TABLE CATISSUE_SITE_USERS (
   SITE_ID NUMBER(19,0) ,
   USER_ID NUMBER(19,0) ,
   PRIMARY KEY (SITE_ID, USER_ID)
);

CREATE TABLE CATISSUE_SITE_CP(
   SITE_ID NUMBER(19,0) ,
   COLLECTION_PROTOCOL_ID NUMBER(19,0) ,
   PRIMARY KEY (SITE_ID,COLLECTION_PROTOCOL_ID)
 );

CREATE TABLE CATISSUE_USER_CP(
  USER_ID NUMBER(19,0) ,
  COLLECTION_PROTOCOL_ID NUMBER(19,0) ,
  PRIMARY KEY (USER_ID,COLLECTION_PROTOCOL_ID)
 );

ALTER TABLE CATISSUE_SITE_USERS ADD CONSTRAINT FK1 FOREIGN KEY (USER_ID) REFERENCES CATISSUE_USER (IDENTIFIER);
ALTER TABLE CATISSUE_SITE_USERS ADD CONSTRAINT FK2 FOREIGN KEY (SITE_ID) REFERENCES CATISSUE_SITE (IDENTIFIER);

ALTER TABLE CATISSUE_SITE_CP ADD CONSTRAINT FK3 FOREIGN KEY (SITE_ID) REFERENCES CATISSUE_SITE (IDENTIFIER);
ALTER TABLE CATISSUE_SITE_CP ADD CONSTRAINT FK4 FOREIGN KEY (COLLECTION_PROTOCOL_ID) REFERENCES CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);

ALTER TABLE CATISSUE_USER_CP ADD CONSTRAINT FK5 FOREIGN KEY (USER_ID) REFERENCES CATISSUE_USER (IDENTIFIER);
ALTER TABLE CATISSUE_USER_CP ADD CONSTRAINT FK6 FOREIGN KEY (COLLECTION_PROTOCOL_ID) REFERENCES CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);

/** MSR changes : End **/

commit;

