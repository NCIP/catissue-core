alter table CATISSUE_INTERFACE_COLUMN_DATA drop constraint FK9C900851F947634C;
alter table CATISSUE_COLL_COORDINATORS drop constraint FKE490E33A48304401;
alter table CATISSUE_COLL_COORDINATORS drop constraint FKE490E33A2206F20F;
alter table CATISSUE_PERMISSIBLE_VALUE drop constraint FK57DDCE153B5435E;
alter table CATISSUE_PERMISSIBLE_VALUE drop constraint FK57DDCE1FC56C2B1;
alter table CATISSUE_COLLECTION_PROTOCOL drop constraint FK32DC439DBC7298A9;
alter table CATISSUE_EVENT_PARAM drop constraint FK90C79AECBC7298A9;
alter table CATISSUE_TRANSFER_EVENT_PARAM drop constraint FK71F9AC103C2DAC61;
alter table CATISSUE_TRANSFER_EVENT_PARAM drop constraint FK71F9AC1099DF0A92;
alter table CATISSUE_TRANSFER_EVENT_PARAM drop constraint FK71F9AC10BC7298A9;
alter table CATISSUE_COLL_EVENT_PARAM drop constraint FKF9888F91BC7298A9;
alter table CATISSUE_SPECIMEN_BIOHZ_REL drop constraint FK7A3F5539F398D480;
alter table CATISSUE_SPECIMEN_BIOHZ_REL drop constraint FK7A3F553960773DB2;
alter table CATISSUE_MOL_SPE_REVIEW_PARAM drop constraint FK5280ECEBC7298A9;
alter table CATISSUE_STORAGE_TYPE drop constraint FKE9A0629A5F7CB0FE;
alter table CATISSUE_DISTRIBUTION_SPE_REQ drop constraint FKE34A3688BE10F0CE;
alter table CATISSUE_DISTRIBUTION_SPE_REQ drop constraint FKE34A36886B1F36E7;
alter table CATISSUE_SITE drop constraint FKB024C3436CD94566;
alter table CATISSUE_SITE drop constraint FKB024C3432206F20F;
alter table CATISSUE_EMBEDDED_EVENT_PARAM drop constraint FKD356182FBC7298A9;
alter table CATISSUE_IN_OUT_EVENT_PARAM drop constraint FK4F0FAEB9BC7298A9;
alter table CATISSUE_COLL_DISTRIBUTION_REL drop constraint FK1483BCB56B1F36E7;
alter table CATISSUE_COLL_DISTRIBUTION_REL drop constraint FK1483BCB548304401;
alter table CATISSUE_DISPOSAL_EVENT_PARAM drop constraint FK1BC818D6BC7298A9;
alter table CATISSUE_AUDIT_EVENT drop constraint FKACAF697A2206F20F;
alter table CATISSUE_FLUID_SPE_EVENT_PARAM drop constraint FK70565D20BC7298A9;
alter table CATISSUE_SPECIMEN_PROTOCOL drop constraint FKB8481373870EB740;
alter table CATISSUE_SPUN_EVENT_PARAMETERS drop constraint FK312D77BCBC7298A9;
alter table CATISSUE_RECEIVED_EVENT_PARAM drop constraint FKA7139D06BC7298A9;
alter table CATISSUE_CLINICAL_REPORT drop constraint FK54A4264515246F7;
alter table CATISSUE_COLL_SPECIMEN_REQ drop constraint FK860E6ABEBE10F0CE;
alter table CATISSUE_COLL_SPECIMEN_REQ drop constraint FK860E6ABE53B01F66;
alter table CATISSUE_DISTRIBUTED_ITEM drop constraint FKA7C3ED4B60773DB2;
alter table CATISSUE_DISTRIBUTED_ITEM drop constraint FKA7C3ED4BF8278B6;
alter table CATISSUE_SPECIMEN_EVENT_PARAM drop constraint FK753F33AD60773DB2;
alter table CATISSUE_SPECIMEN_EVENT_PARAM drop constraint FK753F33AD2206F20F;
alter table CATISSUE_AUDIT_EVENT_LOG drop constraint FK8BB672DF77F0B904;
alter table CATISSUE_COLL_PROT_EVENT drop constraint FK7AE7715948304401;
alter table CATISSUE_STORAGE_CONT_DETAILS drop constraint FK3531F575B3DFB11D;
alter table CATISSUE_PART_MEDICAL_ID drop constraint FK349E77F9A7F77D13;
alter table CATISSUE_PART_MEDICAL_ID drop constraint FK349E77F987E5ADC7;
alter table CATISSUE_STORAGE_CONTAINER drop constraint FK28429D015F7CB0FE;
alter table CATISSUE_STORAGE_CONTAINER drop constraint FK28429D01A7F77D13;
alter table CATISSUE_STORAGE_CONTAINER drop constraint FK28429D0159A3CE5C;
alter table CATISSUE_STORAGE_CONTAINER drop constraint FK28429D01DB097B2E;
alter table CATISSUE_CELL_SPE_REVIEW_PARAM drop constraint FK52F40EDEBC7298A9;
alter table CATISSUE_DISTRIBUTION drop constraint FK542766806B1F36E7;
alter table CATISSUE_DISTRIBUTION drop constraint FK542766801DBE834F;
alter table CATISSUE_DISTRIBUTION drop constraint FK54276680BC7298A9;
alter table CATISSUE_DISTRIBUTION drop constraint FK54276680FB9AE97E;
alter table CATISSUE_AUDIT_EVENT_DETAILS drop constraint FK5C07745D34FFD77F;
alter table CATISSUE_PROCEDURE_EVENT_PARAM drop constraint FKEC6B4260BC7298A9;
alter table CATISSUE_DISTRIBUTION_PROTOCOL drop constraint FKC8999977BC7298A9;
alter table CATISSUE_EXTERNAL_IDENTIFIER drop constraint FK5CF2FA2160773DB2;
alter table CATISSUE_FIXED_EVENT_PARAM drop constraint FKE0F1781BC7298A9;
alter table CATISSUE_THAW_EVENT_PARAMETERS drop constraint FKD8890A48BC7298A9;
alter table CATISSUE_COLL_PROT_REG drop constraint FK5EB25F1387E5ADC7;
alter table CATISSUE_COLL_PROT_REG drop constraint FK5EB25F1348304401;
alter table CATISSUE_FROZEN_EVENT_PARAM drop constraint FK52627245BC7298A9;
alter table CATISSUE_SPECIMEN drop constraint FK1674810456906F39;
alter table CATISSUE_SPECIMEN drop constraint FK1674810433BF33C5;
alter table CATISSUE_SPECIMEN drop constraint FK16748104B189E99D;
alter table CATISSUE_SPECIMEN drop constraint FK1674810432B31EAB;
alter table CATISSUE_USER drop constraint FKB025CFC71792AD22;
alter table CATISSUE_USER drop constraint FKB025CFC7FFA96920;
alter table CATISSUE_USER drop constraint FKB025CFC76CD94566;
alter table CATISSUE_USER drop constraint FKB025CFC7F30C2528;
alter table CATISSUE_TIS_SPE_EVENT_PARAM drop constraint FKBB9648F4BC7298A9;
alter table CATISSUE_SPECIMEN_COLL_GROUP drop constraint FKDEBAF167A7F77D13;
alter table CATISSUE_SPECIMEN_COLL_GROUP drop constraint FKDEBAF1674CE21DDA;
alter table CATISSUE_SPECIMEN_COLL_GROUP drop constraint FKDEBAF16753B01F66;
alter table CATISSUE_SPECIMEN_COLL_GROUP drop constraint FKDEBAF1677E07C4AC;
drop table CATISSUE_INTERFACE_COLUMN_DATA cascade constraints;
drop table CATISSUE_COLL_COORDINATORS cascade constraints;
drop table CATISSUE_PERMISSIBLE_VALUE cascade constraints;
drop table CATISSUE_CANCER_RESEARCH_GROUP cascade constraints;
drop table CATISSUE_COLLECTION_PROTOCOL cascade constraints;
drop table CATISSUE_EVENT_PARAM cascade constraints;
drop table CATISSUE_TRANSFER_EVENT_PARAM cascade constraints;
drop table CATISSUE_COLL_EVENT_PARAM cascade constraints;
drop table CATISSUE_SPECIMEN_BIOHZ_REL cascade constraints;
drop table CATISSUE_MOL_SPE_REVIEW_PARAM cascade constraints;
drop table CATISSUE_STORAGE_TYPE cascade constraints;
drop table CATISSUE_DISTRIBUTION_SPE_REQ cascade constraints;
drop table CATISSUE_SITE cascade constraints;
drop table CATISSUE_EMBEDDED_EVENT_PARAM cascade constraints;
drop table CATISSUE_IN_OUT_EVENT_PARAM cascade constraints;
drop table CATISSUE_COLL_DISTRIBUTION_REL cascade constraints;
drop table CATISSUE_DISPOSAL_EVENT_PARAM cascade constraints;
drop table CATISSUE_CDE cascade constraints;
drop table CATISSUE_SPECIMEN_REQUIREMENT cascade constraints;
drop table CATISSUE_INSTITUTION cascade constraints;
drop table CATISSUE_AUDIT_EVENT cascade constraints;
drop table CATISSUE_STORAGE_CONT_CAPACITY cascade constraints;
drop table CATISSUE_FLUID_SPE_EVENT_PARAM cascade constraints;
drop table CATISSUE_SPECIMEN_PROTOCOL cascade constraints;
drop table CATISSUE_BIOHAZARD cascade constraints;
drop table CATISSUE_SPUN_EVENT_PARAMETERS cascade constraints;
drop table CATISSUE_RECEIVED_EVENT_PARAM cascade constraints;
drop table CATISSUE_CLINICAL_REPORT cascade constraints;
drop table CATISSUE_COLL_SPECIMEN_REQ cascade constraints;
drop table CATISSUE_ADDRESS cascade constraints;
drop table CATISSUE_REPORTED_PROBLEM cascade constraints;
drop table CATISSUE_DISTRIBUTED_ITEM cascade constraints;
drop table CATISSUE_PARTICIPANT cascade constraints;
drop table CATISSUE_SPECIMEN_CHAR cascade constraints;
drop table CATISSUE_SPECIMEN_EVENT_PARAM cascade constraints;
drop table CATISSUE_AUDIT_EVENT_LOG cascade constraints;
drop table CATISSUE_COLL_PROT_EVENT cascade constraints;
drop table CATISSUE_STORAGE_CONT_DETAILS cascade constraints;
drop table CATISSUE_QUERY_TABLE_DATA cascade constraints;
drop table CATISSUE_PART_MEDICAL_ID cascade constraints;
drop table CATISSUE_STORAGE_CONTAINER cascade constraints;
drop table CATISSUE_CELL_SPE_REVIEW_PARAM cascade constraints;
drop table CATISSUE_DISTRIBUTION cascade constraints;
drop table CATISSUE_AUDIT_EVENT_DETAILS cascade constraints;
drop table CATISSUE_PROCEDURE_EVENT_PARAM cascade constraints;
drop table CATISSUE_DISTRIBUTION_PROTOCOL cascade constraints;
drop table CATISSUE_DEPARTMENT cascade constraints;
drop table CATISSUE_EXTERNAL_IDENTIFIER cascade constraints;
drop table CATISSUE_FIXED_EVENT_PARAM cascade constraints;
drop table CATISSUE_THAW_EVENT_PARAMETERS cascade constraints;
drop table CATISSUE_COLL_PROT_REG cascade constraints;
drop table CATISSUE_FROZEN_EVENT_PARAM cascade constraints;
drop table CATISSUE_SPECIMEN cascade constraints;
drop table CATISSUE_USER cascade constraints;
drop table CATISSUE_TIS_SPE_EVENT_PARAM cascade constraints;
drop table CATISSUE_SPECIMEN_COLL_GROUP cascade constraints;
drop sequence hibernate_sequence;
create table CATISSUE_INTERFACE_COLUMN_DATA (
   IDENTIFIER number(19,0) not null,
   TABLE_ID number(19,0),
   COLUMN_NAME varchar2(50),
   DISPLAY_NAME varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_COORDINATORS (
   COLLECTION_PROTOCOL_ID number(19,0) not null,
   USER_ID number(19,0) not null,
   primary key (COLLECTION_PROTOCOL_ID, USER_ID)
);
create table CATISSUE_PERMISSIBLE_VALUE (
   IDENTIFIER varchar2(30) not null,
   CONCEPT_CODE varchar2(20),
   DEFINITION varchar2(500),
   EVS_CODE varchar2(500),
   PARENT_IDENTIFIER varchar2(30),
   VALUE varchar2(100),
   PUBLIC_ID varchar2(30),
   primary key (IDENTIFIER)
);
create table CATISSUE_CANCER_RESEARCH_GROUP (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(50) not null unique,
   primary key (IDENTIFIER)
);
create table CATISSUE_COLLECTION_PROTOCOL (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_TRANSFER_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   FROM_POSITION_DIMENSION_ONE number(10,0),
   FROM_POSITION_DIMENSION_TWO number(10,0),
   TO_POSITION_DIMENSION_ONE number(10,0),
   TO_POSITION_DIMENSION_TWO number(10,0),
   TO_STORAGE_CONTAINER_ID number(19,0),
   FROM_STORAGE_CONTAINER_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   COLLECTION_PROCEDURE varchar2(50),
   CONTAINER varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_BIOHZ_REL (
   BIOHAZARD_ID number(19,0) not null,
   SPECIMEN_ID number(19,0) not null,
   primary key (SPECIMEN_ID, BIOHAZARD_ID)
);
create table CATISSUE_MOL_SPE_REVIEW_PARAM (
   IDENTIFIER number(19,0) not null,
   GEL_IMAGE_URL varchar2(200),
   QUALITY_INDEX varchar2(50),
   LANE_NUMBER varchar2(50),
   GEL_NUMBER number(10,0),
   ABSORBANCE_AT_260 double precision,
   ABSORBANCE_AT_280 double precision,
   RATIO_28S_TO_18S double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_STORAGE_TYPE (
   IDENTIFIER number(19,0) not null,
   TYPE varchar2(50) not null unique,
   DEFAULT_TEMP_IN_CENTIGRADE double precision,
   ONE_DIMENSION_LABEL varchar2(50),
   TWO_DIMENSION_LABEL varchar2(50),
   STORAGE_CONTAINER_CAPACITY_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTION_SPE_REQ (
   DISTRIBUTION_PROTOCOL_ID number(19,0) not null,
   SPECIMEN_REQUIREMENT_ID number(19,0) not null,
   primary key (DISTRIBUTION_PROTOCOL_ID, SPECIMEN_REQUIREMENT_ID)
);
create table CATISSUE_SITE (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(50) not null unique,
   TYPE varchar2(50),
   EMAIL_ADDRESS varchar2(150),
   USER_ID number(19,0),
   ACTIVITY_STATUS varchar2(50),
   ADDRESS_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_EMBEDDED_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   EMBEDDING_MEDIUM varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_IN_OUT_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   STORAGE_STATUS varchar2(100) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_DISTRIBUTION_REL (
   COLLECTION_PROTOCOL_ID number(19,0) not null,
   DISTRIBUTION_PROTOCOL_ID number(19,0) not null,
   primary key (COLLECTION_PROTOCOL_ID, DISTRIBUTION_PROTOCOL_ID)
);
create table CATISSUE_DISPOSAL_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   REASON varchar2(50) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_CDE (
   PUBLIC_ID varchar2(30) not null,
   LONG_NAME varchar2(200),
   DEFINITION varchar2(500),
   VERSION varchar2(50),
   primary key (PUBLIC_ID)
);
create table CATISSUE_SPECIMEN_REQUIREMENT (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_CLASS varchar2(255) not null,
   SPECIMEN_TYPE varchar2(50),
   TISSUE_SITE varchar2(150),
   PATHOLOGY_STATUS varchar2(50),
   QUANTITY double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_INSTITUTION (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(50) not null unique,
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT (
   IDENTIFIER number(19,0) not null,
   IP_ADDRESS varchar2(20),
   EVENT_TIMESTAMP date,
   USER_ID number(19,0),
   COMMENTS varchar2(500),
   primary key (IDENTIFIER)
);
create table CATISSUE_STORAGE_CONT_CAPACITY (
   IDENTIFIER number(19,0) not null,
   ONE_DIMENSION_CAPACITY number(10,0),
   TWO_DIMENSION_CAPACITY number(10,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_FLUID_SPE_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   CELL_COUNT double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_PROTOCOL (
   IDENTIFIER number(19,0) not null,
   PRINCIPAL_INVESTIGATOR_ID number(19,0),
   TITLE varchar2(50) not null unique,
   SHORT_TITLE varchar2(50),
   IRB_IDENTIFIER varchar2(50),
   START_DATE date,
   END_DATE date,
   ENROLLMENT number(10,0),
   DESCRIPTION_URL varchar2(200),
   ACTIVITY_STATUS varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_BIOHAZARD (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(50) not null unique,
   COMMENTS varchar2(500),
   TYPE varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPUN_EVENT_PARAMETERS (
   IDENTIFIER number(19,0) not null,
   GFORCE double precision,
   DURATION_IN_MINUTES number(10,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_RECEIVED_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   RECEIVED_QUALITY varchar2(255),
   primary key (IDENTIFIER)
);
create table CATISSUE_CLINICAL_REPORT (
   IDENTIFIER number(19,0) not null,
   SURGICAL_PATHOLOGICAL_NUMBER varchar2(50),
   PARTICIPENT_MEDI_IDENTIFIER_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_SPECIMEN_REQ (
   COLLECTION_PROTOCOL_EVENT_ID number(19,0) not null,
   SPECIMEN_REQUIREMENT_ID number(19,0) not null,
   primary key (COLLECTION_PROTOCOL_EVENT_ID, SPECIMEN_REQUIREMENT_ID)
);
create table CATISSUE_ADDRESS (
   IDENTIFIER number(19,0) not null,
   STREET varchar2(50),
   CITY varchar2(50),
   STATE varchar2(50),
   COUNTRY varchar2(50),
   ZIPCODE varchar2(30),
   PHONE_NUMBER varchar2(50),
   FAX_NUMBER varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_REPORTED_PROBLEM (
   IDENTIFIER number(19,0) not null,
   AFFILIATION varchar2(200) not null,
   NAME_OF_REPORTER varchar2(200) not null,
   REPORTERS_EMAIL_ID varchar2(50) not null,
   MESSAGE_BODY varchar2(200) not null,
   SUBJECT varchar2(100),
   REPORTED_DATE date,
   ACTIVITY_STATUS varchar2(100),
   COMMENTS varchar2(2000),
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTED_ITEM (
   IDENTIFIER number(19,0) not null,
   QUANTITY double precision,
   SPECIMEN_ID number(19,0),
   DISTRIBUTION_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_PARTICIPANT (
   IDENTIFIER number(19,0) not null,
   LAST_NAME varchar2(50),
   FIRST_NAME varchar2(50),
   MIDDLE_NAME varchar2(50),
   BIRTH_DATE date,
   GENDER varchar2(20),
   GENOTYPE varchar2(50),
   RACE varchar2(50),
   ETHNICITY varchar2(50),
   SOCIAL_SECURITY_NUMBER varchar2(50) unique,
   ACTIVITY_STATUS varchar2(20),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_CHAR (
   IDENTIFIER number(19,0) not null,
   TISSUE_SITE varchar2(150),
   TISSUE_SIDE varchar2(50),
   PATHOLOGICAL_STATUS varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_ID number(19,0),
   EVENT_TIMESTAMP date,
   USER_ID number(19,0),
   COMMENTS varchar2(500),
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT_LOG (
   IDENTIFIER number(19,0) not null,
   OBJECT_IDENTIFIER number(19,0),
   OBJECT_NAME varchar2(50),
   EVENT_TYPE varchar2(50),
   AUDIT_EVENT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_PROT_EVENT (
   IDENTIFIER number(19,0) not null,
   CLINICAL_STATUS varchar2(50),
   STUDY_CALENDAR_EVENT_POINT double precision,
   COLLECTION_PROTOCOL_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_STORAGE_CONT_DETAILS (
   IDENTIFIER number(19,0) not null,
   PARAMETER_NAME varchar2(50),
   VALUE varchar2(50),
   STORAGE_CONTAINER_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_QUERY_TABLE_DATA (
   TABLE_ID number(19,0) not null,
   DISPLAY_NAME varchar2(50),
   TABLE_NAME varchar2(50),
   ALIAS_NAME varchar2(50),
   primary key (TABLE_ID)
);
create table CATISSUE_PART_MEDICAL_ID (
   IDENTIFIER number(19,0) not null,
   MEDICAL_RECORD_NUMBER varchar2(50),
   SITE_ID number(19,0),
   PARTICIPANT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_STORAGE_CONTAINER (
   IDENTIFIER number(19,0) not null,
   CONTAINER_NUMBER number(10,0),
   TEMPERATURE double precision,
   IS_CONTAINER_FULL number(1,0),
   BARCODE varchar2(50) unique,
   ACTIVITY_STATUS varchar2(30),
   STORAGE_TYPE_ID number(19,0),
   SITE_ID number(19,0),
   PARENT_CONTAINER_ID number(19,0),
   STORAGE_CONTAINER_CAPACITY_ID number(19,0),
   POSITION_DIMENSION_ONE number(10,0),
   POSITION_DIMENSION_TWO number(10,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_CELL_SPE_REVIEW_PARAM (
   IDENTIFIER number(19,0) not null,
   NEOPLASTIC_CELLULARITY_PER double precision,
   VIABLE_CELL_PERCENTAGE double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTION (
   IDENTIFIER number(19,0) not null,
   TO_SITE_ID number(19,0),
   FROM_SITE_ID number(19,0),
   DISTRIBUTION_PROTOCOL_ID number(19,0),
   ACTIVITY_STATUS varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_AUDIT_EVENT_DETAILS (
   IDENTIFIER number(19,0) not null,
   ELEMENT_NAME varchar2(150),
   PREVIOUS_VALUE varchar2(150),
   CURRENT_VALUE varchar2(150),
   AUDIT_EVENT_LOG_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_PROCEDURE_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   URL varchar2(200) not null,
   NAME varchar2(50) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTION_PROTOCOL (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_DEPARTMENT (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(50) not null unique,
   primary key (IDENTIFIER)
);
create table CATISSUE_EXTERNAL_IDENTIFIER (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(50),
   VALUE varchar2(50),
   SPECIMEN_ID number(19,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_FIXED_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   FIXATION_TYPE varchar2(50) not null,
   DURATION_IN_MINUTES number(10,0),
   primary key (IDENTIFIER)
);
create table CATISSUE_THAW_EVENT_PARAMETERS (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_PROT_REG (
   IDENTIFIER number(19,0) not null,
   PROTOCOL_PARTICIPANT_ID varchar2(50),
   REGISTRATION_DATE date,
   PARTICIPANT_ID number(19,0),
   COLLECTION_PROTOCOL_ID number(19,0),
   ACTIVITY_STATUS varchar2(20),
   primary key (IDENTIFIER)
);
create table CATISSUE_FROZEN_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   METHOD varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_CLASS varchar2(255) not null,
   TYPE varchar2(50),
   AVAILABLE number(1,0),
   POSITION_DIMENSION_ONE number(10,0),
   POSITION_DIMENSION_TWO number(10,0),
   BARCODE varchar2(50) unique,
   COMMENTS varchar2(200),
   ACTIVITY_STATUS varchar2(50),
   PARENT_SPECIMEN_ID number(19,0),
   STORAGE_CONTAINER_IDENTIFIER number(19,0),
   SPECIMEN_COLLECTION_GROUP_ID number(19,0),
   SPECIMEN_CHARACTERISTICS_ID number(19,0),
   QUANTITY double precision,
   AVAILABLE_QUANTITY double precision,
   CONCENTRATION double precision,
   primary key (IDENTIFIER)
);
create table CATISSUE_USER (
   IDENTIFIER number(19,0) not null,
   EMAIL_ADDRESS varchar2(100),
   PASSWORD varchar2(50),
   FIRST_NAME varchar2(50),
   LAST_NAME varchar2(50),
   LOGIN_NAME varchar2(50) not null unique,
   START_DATE date,
   ACTIVITY_STATUS varchar2(50),
   DEPARTMENT_ID number(19,0),
   CANCER_RESEARCH_GROUP_ID number(19,0),
   INSTITUTION_ID number(19,0),
   ADDRESS_ID number(19,0),
   CSM_USER_ID number(19,0),
   STATUS_COMMENT varchar2(2000),
   primary key (IDENTIFIER)
);
create table CATISSUE_TIS_SPE_EVENT_PARAM (
   IDENTIFIER number(19,0) not null,
   NEOPLASTIC_CELLULARITY_PER double precision,
   NECROSIS_PERCENTAGE double precision,
   LYMPHOCYTIC_PERCENTAGE double precision,
   TOTAL_CELLULARITY_PERCENTAGE double precision,
   HISTOLOGICAL_QUALITY varchar2(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_COLL_GROUP (
   IDENTIFIER number(19,0) not null,
   CLINICAL_DIAGNOSIS varchar2(150),
   CLINICAL_STATUS varchar2(50),
   ACTIVITY_STATUS varchar2(50),
   SITE_ID number(19,0),
   COLLECTION_PROTOCOL_EVENT_ID number(19,0),
   CLINICAL_REPORT_ID number(19,0),
   COLLECTION_PROTOCOL_REG_ID number(19,0),
   primary key (IDENTIFIER)
);
alter table CATISSUE_INTERFACE_COLUMN_DATA add constraint FK9C900851F947634C foreign key (TABLE_ID) references CATISSUE_QUERY_TABLE_DATA;
alter table CATISSUE_COLL_COORDINATORS add constraint FKE490E33A48304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL;
alter table CATISSUE_COLL_COORDINATORS add constraint FKE490E33A2206F20F foreign key (USER_ID) references CATISSUE_USER;
alter table CATISSUE_PERMISSIBLE_VALUE add constraint FK57DDCE153B5435E foreign key (PARENT_IDENTIFIER) references CATISSUE_PERMISSIBLE_VALUE;
alter table CATISSUE_PERMISSIBLE_VALUE add constraint FK57DDCE1FC56C2B1 foreign key (PUBLIC_ID) references CATISSUE_CDE;
alter table CATISSUE_COLLECTION_PROTOCOL add constraint FK32DC439DBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_PROTOCOL;
alter table CATISSUE_EVENT_PARAM add constraint FK90C79AECBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_TRANSFER_EVENT_PARAM add constraint FK71F9AC103C2DAC61 foreign key (TO_STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER;
alter table CATISSUE_TRANSFER_EVENT_PARAM add constraint FK71F9AC1099DF0A92 foreign key (FROM_STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER;
alter table CATISSUE_TRANSFER_EVENT_PARAM add constraint FK71F9AC10BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_COLL_EVENT_PARAM add constraint FKF9888F91BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_SPECIMEN_BIOHZ_REL add constraint FK7A3F5539F398D480 foreign key (BIOHAZARD_ID) references CATISSUE_BIOHAZARD;
alter table CATISSUE_SPECIMEN_BIOHZ_REL add constraint FK7A3F553960773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN;
alter table CATISSUE_MOL_SPE_REVIEW_PARAM add constraint FK5280ECEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM;
alter table CATISSUE_STORAGE_TYPE add constraint FKE9A0629A5F7CB0FE foreign key (STORAGE_CONTAINER_CAPACITY_ID) references CATISSUE_STORAGE_CONT_CAPACITY;
alter table CATISSUE_DISTRIBUTION_SPE_REQ add constraint FKE34A3688BE10F0CE foreign key (SPECIMEN_REQUIREMENT_ID) references CATISSUE_SPECIMEN_REQUIREMENT;
alter table CATISSUE_DISTRIBUTION_SPE_REQ add constraint FKE34A36886B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL;
alter table CATISSUE_SITE add constraint FKB024C3436CD94566 foreign key (ADDRESS_ID) references CATISSUE_ADDRESS;
alter table CATISSUE_SITE add constraint FKB024C3432206F20F foreign key (USER_ID) references CATISSUE_USER;
alter table CATISSUE_EMBEDDED_EVENT_PARAM add constraint FKD356182FBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_IN_OUT_EVENT_PARAM add constraint FK4F0FAEB9BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_COLL_DISTRIBUTION_REL add constraint FK1483BCB56B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL;
alter table CATISSUE_COLL_DISTRIBUTION_REL add constraint FK1483BCB548304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL;
alter table CATISSUE_DISPOSAL_EVENT_PARAM add constraint FK1BC818D6BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_AUDIT_EVENT add constraint FKACAF697A2206F20F foreign key (USER_ID) references CATISSUE_USER;
alter table CATISSUE_FLUID_SPE_EVENT_PARAM add constraint FK70565D20BC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM;
alter table CATISSUE_SPECIMEN_PROTOCOL add constraint FKB8481373870EB740 foreign key (PRINCIPAL_INVESTIGATOR_ID) references CATISSUE_USER;
alter table CATISSUE_SPUN_EVENT_PARAMETERS add constraint FK312D77BCBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_RECEIVED_EVENT_PARAM add constraint FKA7139D06BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_CLINICAL_REPORT add constraint FK54A4264515246F7 foreign key (PARTICIPENT_MEDI_IDENTIFIER_ID) references CATISSUE_PART_MEDICAL_ID;
alter table CATISSUE_COLL_SPECIMEN_REQ add constraint FK860E6ABEBE10F0CE foreign key (SPECIMEN_REQUIREMENT_ID) references CATISSUE_SPECIMEN_REQUIREMENT;
alter table CATISSUE_COLL_SPECIMEN_REQ add constraint FK860E6ABE53B01F66 foreign key (COLLECTION_PROTOCOL_EVENT_ID) references CATISSUE_COLL_PROT_EVENT;
alter table CATISSUE_DISTRIBUTED_ITEM add constraint FKA7C3ED4B60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN;
alter table CATISSUE_DISTRIBUTED_ITEM add constraint FKA7C3ED4BF8278B6 foreign key (DISTRIBUTION_ID) references CATISSUE_DISTRIBUTION;
alter table CATISSUE_SPECIMEN_EVENT_PARAM add constraint FK753F33AD60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN;
alter table CATISSUE_SPECIMEN_EVENT_PARAM add constraint FK753F33AD2206F20F foreign key (USER_ID) references CATISSUE_USER;
alter table CATISSUE_AUDIT_EVENT_LOG add constraint FK8BB672DF77F0B904 foreign key (AUDIT_EVENT_ID) references CATISSUE_AUDIT_EVENT;
alter table CATISSUE_COLL_PROT_EVENT add constraint FK7AE7715948304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL;
alter table CATISSUE_STORAGE_CONT_DETAILS add constraint FK3531F575B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER;
alter table CATISSUE_PART_MEDICAL_ID add constraint FK349E77F9A7F77D13 foreign key (SITE_ID) references CATISSUE_SITE;
alter table CATISSUE_PART_MEDICAL_ID add constraint FK349E77F987E5ADC7 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT;
alter table CATISSUE_STORAGE_CONTAINER add constraint FK28429D015F7CB0FE foreign key (STORAGE_CONTAINER_CAPACITY_ID) references CATISSUE_STORAGE_CONT_CAPACITY;
alter table CATISSUE_STORAGE_CONTAINER add constraint FK28429D01A7F77D13 foreign key (SITE_ID) references CATISSUE_SITE;
alter table CATISSUE_STORAGE_CONTAINER add constraint FK28429D0159A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE;
alter table CATISSUE_STORAGE_CONTAINER add constraint FK28429D01DB097B2E foreign key (PARENT_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER;
alter table CATISSUE_CELL_SPE_REVIEW_PARAM add constraint FK52F40EDEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM;
alter table CATISSUE_DISTRIBUTION add constraint FK542766806B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL;
alter table CATISSUE_DISTRIBUTION add constraint FK542766801DBE834F foreign key (TO_SITE_ID) references CATISSUE_SITE;
alter table CATISSUE_DISTRIBUTION add constraint FK54276680BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_DISTRIBUTION add constraint FK54276680FB9AE97E foreign key (FROM_SITE_ID) references CATISSUE_SITE;
alter table CATISSUE_AUDIT_EVENT_DETAILS add constraint FK5C07745D34FFD77F foreign key (AUDIT_EVENT_LOG_ID) references CATISSUE_AUDIT_EVENT_LOG;
alter table CATISSUE_PROCEDURE_EVENT_PARAM add constraint FKEC6B4260BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_DISTRIBUTION_PROTOCOL add constraint FKC8999977BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_PROTOCOL;
alter table CATISSUE_EXTERNAL_IDENTIFIER add constraint FK5CF2FA2160773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN;
alter table CATISSUE_FIXED_EVENT_PARAM add constraint FKE0F1781BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_THAW_EVENT_PARAMETERS add constraint FKD8890A48BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_COLL_PROT_REG add constraint FK5EB25F1387E5ADC7 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT;
alter table CATISSUE_COLL_PROT_REG add constraint FK5EB25F1348304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL;
alter table CATISSUE_FROZEN_EVENT_PARAM add constraint FK52627245BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM;
alter table CATISSUE_SPECIMEN add constraint FK1674810456906F39 foreign key (SPECIMEN_CHARACTERISTICS_ID) references CATISSUE_SPECIMEN_CHAR;
alter table CATISSUE_SPECIMEN add constraint FK1674810433BF33C5 foreign key (SPECIMEN_COLLECTION_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP;
alter table CATISSUE_SPECIMEN add constraint FK16748104B189E99D foreign key (PARENT_SPECIMEN_ID) references CATISSUE_SPECIMEN;
alter table CATISSUE_SPECIMEN add constraint FK1674810432B31EAB foreign key (STORAGE_CONTAINER_IDENTIFIER) references CATISSUE_STORAGE_CONTAINER;
alter table CATISSUE_USER add constraint FKB025CFC71792AD22 foreign key (INSTITUTION_ID) references CATISSUE_INSTITUTION;
alter table CATISSUE_USER add constraint FKB025CFC7FFA96920 foreign key (CANCER_RESEARCH_GROUP_ID) references CATISSUE_CANCER_RESEARCH_GROUP;
alter table CATISSUE_USER add constraint FKB025CFC76CD94566 foreign key (ADDRESS_ID) references CATISSUE_ADDRESS;
alter table CATISSUE_USER add constraint FKB025CFC7F30C2528 foreign key (DEPARTMENT_ID) references CATISSUE_DEPARTMENT;
alter table CATISSUE_TIS_SPE_EVENT_PARAM add constraint FKBB9648F4BC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM;
alter table CATISSUE_SPECIMEN_COLL_GROUP add constraint FKDEBAF167A7F77D13 foreign key (SITE_ID) references CATISSUE_SITE;
alter table CATISSUE_SPECIMEN_COLL_GROUP add constraint FKDEBAF1674CE21DDA foreign key (CLINICAL_REPORT_ID) references CATISSUE_CLINICAL_REPORT;
alter table CATISSUE_SPECIMEN_COLL_GROUP add constraint FKDEBAF16753B01F66 foreign key (COLLECTION_PROTOCOL_EVENT_ID) references CATISSUE_COLL_PROT_EVENT;
alter table CATISSUE_SPECIMEN_COLL_GROUP add constraint FKDEBAF1677E07C4AC foreign key (COLLECTION_PROTOCOL_REG_ID) references CATISSUE_COLL_PROT_REG;
create sequence hibernate_sequence increment by 1 start with 1 NOMAXVALUE minvalue 1 nocycle nocache noorder;

ALTER TABLE CATISSUE_COLL_SPECIMEN_REQ ADD(IDENTIFIER number(20));
ALTER TABLE CATISSUE_DISTRIBUTION_SPE_REQ ADD(IDENTIFIER number(20));