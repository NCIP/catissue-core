alter table CATISSUE_COLL_COORDINATORS drop foreign key FKE490E33A48304401;
alter table CATISSUE_COLL_COORDINATORS drop foreign key FKE490E33A2206F20F;
alter table CATISSUE_COLLECTION_PROTOCOL drop foreign key FK32DC439DBC7298A9;
alter table CATISSUE_EVENT_PARAM drop foreign key FK90C79AECBC7298A9;
alter table CATISSUE_TRANSFER_EVENT_PARAM drop foreign key FK71F9AC103C2DAC61;
alter table CATISSUE_TRANSFER_EVENT_PARAM drop foreign key FK71F9AC1099DF0A92;
alter table CATISSUE_TRANSFER_EVENT_PARAM drop foreign key FK71F9AC10BC7298A9;
alter table CATISSUE_STOR_CONT_SPEC_CLASS drop foreign key FKE7F5E8C2B3DFB11D;
alter table CATISSUE_COLL_EVENT_PARAM drop foreign key FKF9888F91BC7298A9;
alter table CATISSUE_PASSWORD drop foreign key FKDE1F38972206F20F;
alter table CATISSUE_SPECIMEN_BIOHZ_REL drop foreign key FK7A3F5539F398D480;
alter table CATISSUE_SPECIMEN_BIOHZ_REL drop foreign key FK7A3F553960773DB2;
alter table CATISSUE_MOL_SPE_REVIEW_PARAM drop foreign key FK5280ECEBC7298A9;
#--alter table CATISSUE_STORAGE_TYPE drop foreign key FKE9A0629A81236791;
alter table CATISSUE_STORAGE_TYPE drop foreign key FKE9A0629ABC7298A9;
alter table CATISSUE_STORAGE_TYPE drop foreign key FK185C50B59A3CE5C;
alter table CATISSUE_STORAGE_TYPE drop foreign key FK185C50B81236791;

alter table CATISSUE_CONTAINER drop foreign key FK49B8DE5DB097B2E;
alter table CATISSUE_CONTAINER drop foreign key FK49B8DE5DAC76C0;
alter table CATISSUE_DISTRIBUTION_SPE_REQ drop foreign key FKE34A3688BE10F0CE;
alter table CATISSUE_DISTRIBUTION_SPE_REQ drop foreign key FKE34A36886B1F36E7;
alter table CATISSUE_SITE drop foreign key FKB024C3436CD94566;
alter table CATISSUE_SITE drop foreign key FKB024C3432206F20F;
alter table CATISSUE_EMBEDDED_EVENT_PARAM drop foreign key FKD356182FBC7298A9;
alter table CATISSUE_IN_OUT_EVENT_PARAM drop foreign key FK4F0FAEB9BC7298A9;
alter table CATISSUE_COLL_DISTRIBUTION_REL drop foreign key FK1483BCB56B1F36E7;
alter table CATISSUE_COLL_DISTRIBUTION_REL drop foreign key FK1483BCB548304401;
alter table CATISSUE_DISPOSAL_EVENT_PARAM drop foreign key FK1BC818D6BC7298A9;
alter table CATISSUE_SPECIMEN_REQUIREMENT drop foreign key FK39AFE96861A1C94F;
alter table CATISSUE_ST_CONT_ST_TYPE_REL drop foreign key FK703B902159A3CE5C;
alter table CATISSUE_ST_CONT_ST_TYPE_REL drop foreign key FK703B9021B3DFB11D;
alter table CATISSUE_SPECIMEN_PROTOCOL drop foreign key FKB8481373870EB740;
alter table CATISSUE_FLUID_SPE_EVENT_PARAM drop foreign key FK70565D20BC7298A9;
alter table CATISSUE_SPUN_EVENT_PARAMETERS drop foreign key FK312D77BCBC7298A9;
alter table CATISSUE_RECEIVED_EVENT_PARAM drop foreign key FKA7139D06BC7298A9;
alter table CATISSUE_RACE drop foreign key FKB0242ECD87E5ADC7;
alter table CATISSUE_CLINICAL_REPORT drop foreign key FK54A4264515246F7;
alter table CATISSUE_COLL_SPECIMEN_REQ drop foreign key FK860E6ABEBE10F0CE;
alter table CATISSUE_COLL_SPECIMEN_REQ drop foreign key FK860E6ABE53B01F66;
alter table CATISSUE_SPECI_ARRAY_CONTENT drop foreign key FKBEA9D458C4A3C438;
alter table CATISSUE_SPECI_ARRAY_CONTENT drop foreign key FKBEA9D45860773DB2;
alter table CATISSUE_SPECI_ARRAY_CONTENT drop foreign key FKBEA9D45892AB74B4;
alter table CATISSUE_SPECIMEN_ARRAY drop foreign key FKECBF8B3E64B129CC;
alter table CATISSUE_SPECIMEN_ARRAY drop foreign key FKECBF8B3EF8278B6;
alter table CATISSUE_SPECIMEN_ARRAY drop foreign key FKECBF8B3EBC7298A9;
alter table CATISSUE_SPECIMEN_ARRAY drop foreign key FKECBF8B3EB3DFB11D;
alter table CATISSUE_SPECIMEN_ARRAY drop foreign key FKECBF8B3EECE89343;
alter table CATISSUE_SPECIMEN_ARRAY_TYPE drop foreign key FKD36E0B9BBC7298A9;
alter table CATISSUE_DISTRIBUTED_ITEM drop foreign key FKA7C3ED4B60773DB2;
alter table CATISSUE_DISTRIBUTED_ITEM drop foreign key FKA7C3ED4BF8278B6;
alter table CATISSUE_SPECIMEN_EVENT_PARAM drop foreign key FK753F33AD60773DB2;
alter table CATISSUE_SPECIMEN_EVENT_PARAM drop foreign key FK753F33AD2206F20F;
alter table CATISSUE_STOR_TYPE_SPEC_CLASS drop foreign key FK1BCF33BA59A3CE5C;
alter table CATISSUE_COLL_PROT_EVENT drop foreign key FK7AE7715948304401;
alter table CATISSUE_CONTAINER_TYPE drop foreign key FKCBBC9954DAC76C0;
alter table CATISSUE_PART_MEDICAL_ID drop foreign key FK349E77F9A7F77D13;
alter table CATISSUE_PART_MEDICAL_ID drop foreign key FK349E77F987E5ADC7;
alter table CATISSUE_STORAGE_CONTAINER drop foreign key FK28429D01BC7298A9;
alter table CATISSUE_STORAGE_CONTAINER drop foreign key FK28429D01A7F77D13;
alter table CATISSUE_STORAGE_CONTAINER drop foreign key FK28429D0159A3CE5C;
alter table CATISSUE_CELL_SPE_REVIEW_PARAM drop foreign key FK52F40EDEBC7298A9;
alter table CATISSUE_DISTRIBUTION drop foreign key FK542766802206F20F;
alter table CATISSUE_DISTRIBUTION drop foreign key FK542766806B1F36E7;
alter table CATISSUE_DISTRIBUTION drop foreign key FK542766801DBE834F;
alter table CATISSUE_DISTRIBUTION drop foreign key FK5427668060773DB2;
alter table CATISSUE_PROCEDURE_EVENT_PARAM drop foreign key FKEC6B4260BC7298A9;
alter table CATISSUE_DISTRIBUTION_PROTOCOL drop foreign key FKC8999977BC7298A9;
alter table CATISSUE_EXTERNAL_IDENTIFIER drop foreign key FK5CF2FA2160773DB2;
alter table CATISSUE_FIXED_EVENT_PARAM drop foreign key FKE0F1781BC7298A9;
alter table CATISSUE_THAW_EVENT_PARAMETERS drop foreign key FKD8890A48BC7298A9;
alter table CATISSUE_COLL_PROT_REG drop foreign key FK5EB25F1387E5ADC7;
alter table CATISSUE_COLL_PROT_REG drop foreign key FK5EB25F13A0FF79D4;
alter table CATISSUE_COLL_PROT_REG drop foreign key FK5EB25F1348304401;
alter table CATISSUE_FROZEN_EVENT_PARAM drop foreign key FK52627245BC7298A9;
alter table CATISSUE_SPECIMEN drop foreign key FK1674810456906F39;
alter table CATISSUE_SPECIMEN drop foreign key FK1674810433BF33C5;
alter table CATISSUE_SPECIMEN drop foreign key FK16748104B189E99D;
alter table CATISSUE_SPECIMEN drop foreign key FK1674810432B31EAB;
alter table CATISSUE_USER drop foreign key FKB025CFC71792AD22;
alter table CATISSUE_USER drop foreign key FKB025CFC7FFA96920;
alter table CATISSUE_USER drop foreign key FKB025CFC76CD94566;
alter table CATISSUE_USER drop foreign key FKB025CFC7F30C2528;
alter table CATISSUE_TIS_SPE_EVENT_PARAM drop foreign key FKBB9648F4BC7298A9;
alter table CATISSUE_ST_CONT_COLL_PROT_REL drop foreign key FK3AE9FCA7B3DFB11D;
alter table CATISSUE_ST_CONT_COLL_PROT_REL drop foreign key FK3AE9FCA748304401;
alter table CATISSUE_SPECIMEN_COLL_GROUP drop foreign key FKDEBAF167A7F77D13;
alter table CATISSUE_SPECIMEN_COLL_GROUP drop foreign key FKDEBAF1674CE21DDA;
alter table CATISSUE_SPECIMEN_COLL_GROUP drop foreign key FKDEBAF16753B01F66;
alter table CATISSUE_SPECIMEN_COLL_GROUP drop foreign key FKDEBAF1677E07C4AC;
alter table CATISSUE_SPECIMEN_TYPE drop foreign key FKFF69C195ECE89343;
alter table CATISSUE_PERMISSIBLE_VALUE drop foreign key FK57DDCE153B5435E;
alter table CATISSUE_PERMISSIBLE_VALUE drop foreign key FK57DDCE1FC56C2B1;
alter table CATISSUE_AUDIT_EVENT drop foreign key FKACAF697A2206F20F;
alter table CATISSUE_AUDIT_EVENT_LOG drop foreign key FK8BB672DF77F0B904;
alter table CATISSUE_AUDIT_EVENT_DETAILS drop foreign key FK5C07745D34FFD77F;
alter table CATISSUE_ENTITY_MAP_CONDITIONS drop foreign key FKDE84F0FB11B8FADA;
alter table CATISSUE_AUDIT_EVENT_QUERY_LOG drop foreign key FK62DC439DBC7298A9;
alter table CATISSUE_RETURN_EVENT_PARAM drop foreign key FKD8890A48BC7298A91;


#---- caTIES  related alter table script

alter table CATISSUE_REPORT_TEXTCONTENT drop foreign key FKD74882FD91092806;
alter table CATISSUE_REPORT_TEXTCONTENT drop foreign key FKD74882FDBC7298A9;
alter table CATISSUE_IDENTIFIED_REPORT drop foreign key FK6A2246DCBC7298A9;
alter table CATISSUE_IDENTIFIED_REPORT drop foreign key FK6A2246DC752DD177;
alter table CATISSUE_IDENTIFIED_REPORT drop foreign key FK6A2246DC91741663;
alter table CATISSUE_CONCEPT_REFERENT drop foreign key FK799CCA7E9F96B363;
alter table CATISSUE_REVIEW_PARAMS drop foreign key FK5311FFF62206F20F;
alter table CATISSUE_REVIEW_PARAMS drop foreign key FK5311FFF691092806;
alter table CATISSUE_REPORT_BICONTENT drop foreign key FK8A9A4EE391092806;
alter table CATISSUE_REPORT_BICONTENT drop foreign key FK8A9A4EE3BC7298A9;
alter table CATISSUE_DEIDENTIFIED_REPORT drop foreign key FKCDD0DF7BBC7298A9;
alter table CATISSUE_DEIDENTIFIED_REPORT drop foreign key FKCDD0DF7B91741663;
alter table CATISSUE_QUARANTINE_PARAMS drop foreign key FK3C12AE3B2206F20F;
alter table CATISSUE_QUARANTINE_PARAMS drop foreign key FK3C12AE3B3EEC14E3;
alter table CATISSUE_PATHOLOGY_REPORT drop foreign key FK904EC9F040DCD7BF;
alter table CATISSUE_REPORT_XMLCONTENT drop foreign key FK4597C9F1BC7298A9;
alter table CATISSUE_REPORT_XMLCONTENT drop foreign key FK4597C9F191092806;


#---- Ordering system  related alter table script

alter table CATISSUE_EXISTING_SP_ORD_ITEM drop foreign key FKF8B855EEBC7298A9;
alter table CATISSUE_EXISTING_SP_ORD_ITEM drop foreign key FKF8B855EE60773DB2;
alter table CATISSUE_PATH_CASE_ORDER_ITEM drop foreign key FKBD5029D5F69249F7;
alter table CATISSUE_PATH_CASE_ORDER_ITEM drop foreign key FKBD5029D5BC7298A9;
alter table CATISSUE_ORDER_ITEM drop foreign key FKB501E88060975C0B;
alter table CATISSUE_ORDER_ITEM drop foreign key FKB501E880783867CC;
alter table CATISSUE_DERIEVED_SP_ORD_ITEM drop foreign key FK3742152BBC7298A9;
alter table CATISSUE_DERIEVED_SP_ORD_ITEM drop foreign key FK3742152B60773DB2;
alter table CATISSUE_ORDER drop foreign key FK543F22B26B1F36E7;
alter table CATISSUE_DISTRIBUTION drop foreign key FK54276680783867CC;
alter table CATISSUE_DISTRIBUTED_ITEM drop foreign key FKA7C3ED4BC4A3C438;
alter table CATISSUE_SP_ARRAY_ORDER_ITEM drop foreign key FKE3823170BC7298A9;
alter table CATISSUE_SP_ARRAY_ORDER_ITEM drop foreign key FKE3823170C4A3C438;
alter table CATISSUE_SPECIMEN_ORDER_ITEM drop foreign key FK48C3B39FBC7298A9;
alter table CATISSUE_SPECIMEN_ORDER_ITEM drop foreign key FK48C3B39F83505A30;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop foreign key FKC5C92CCBCE5FBC3A;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop foreign key FKC5C92CCBBC7298A9;
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM drop foreign key FKC5C92CCBC4A3C438;

#-------- Consent Tracking alter scripts
alter table CATISSUE_CONSENT_TIER_RESPONSE drop foreign key FKFB1995FD4AD77FCB;
alter table CATISSUE_CONSENT_TIER_RESPONSE drop foreign key FKFB1995FD17B9953;
alter table CATISSUE_CONSENT_TIER_STATUS drop foreign key FKF74E94AEF69249F7;
alter table CATISSUE_CONSENT_TIER_STATUS drop foreign key FKF74E94AE60773DB2;
alter table CATISSUE_CONSENT_TIER_STATUS drop foreign key FKF74E94AE17B9953;
alter table CATISSUE_CONSENT_TIER drop foreign key FK51725303E36A4B4F;

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
drop table if exists CATISSUE_STOR_TYPE_HOLDS_TYPE;
drop table if exists CATISSUE_STORAGE_TYPE;
drop table if exists CATISSUE_CONTAINER;
drop table if exists CATISSUE_DISTRIBUTION_SPE_REQ;
drop table if exists CATISSUE_SITE;
drop table if exists CATISSUE_EMBEDDED_EVENT_PARAM;
drop table if exists CATISSUE_IN_OUT_EVENT_PARAM;
drop table if exists CATISSUE_ENTITY_MAP;
drop table if exists CATISSUE_ENTITY_MAP_RECORD;
drop table if exists CATISSUE_ENTITY_MAP_CONDITIONS;
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
drop table if exists CATISSUE_CLINICAL_REPORT;
drop table if exists CATISSUE_COLL_SPECIMEN_REQ;
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
drop table if exists CATISSUE_SPECIMEN;
drop table if exists CATISSUE_USER;
drop table if exists CATISSUE_TIS_SPE_EVENT_PARAM;
drop table if exists CATISSUE_ST_CONT_COLL_PROT_REL;
drop table if exists CATISSUE_STORTY_HOLDS_SPARRTY;
drop table if exists CATISSUE_CONT_HOLDS_SPARRTYPE;
drop table if exists CATISSUE_SPECIMEN_COLL_GROUP;
drop table if exists CATISSUE_SPECIMEN_TYPE;
drop table if exists CATISSUE_AUDIT_EVENT_QUERY_LOG;
drop table if exists CATISSUE_AUDIT_EVENT;
drop table if exists CATISSUE_AUDIT_EVENT_LOG;
drop table if exists CATISSUE_AUDIT_EVENT_DETAILS;
drop table if exists CATISSUE_RETURN_EVENT_PARAM;




create table CATISSUE_PERMISSIBLE_VALUE (
   IDENTIFIER bigint not null auto_increment,
   CONCEPT_CODE varchar(40),
   DEFINITION text,
   PARENT_IDENTIFIER bigint,
   VALUE varchar(100),
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
   PARENT_CONTAINER_ID bigint,
   COMMENTS text,
   FULL bit,
   NAME varchar(255) unique not null,
   POSITION_DIMENSION_ONE integer,
   POSITION_DIMENSION_TWO integer,
   primary key (IDENTIFIER)
);
create table CATISSUE_DISTRIBUTION_SPE_REQ (
   DISTRIBUTION_PROTOCOL_ID bigint not null,
   SPECIMEN_REQUIREMENT_ID bigint not null,
   primary key (DISTRIBUTION_PROTOCOL_ID, SPECIMEN_REQUIREMENT_ID)
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
create table CATISSUE_ENTITY_MAP (
   IDENTIFIER bigint not null auto_increment,
   CONTAINER_ID bigint,
   STATIC_RECORD_ID bigint,
   STATIC_ENTITY_ID bigint,
   CREATED_DATE date,
   CREATED_BY varchar(255),
   STATUS varchar(10),
   TYPE_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_ENTITY_MAP_RECORD (
   IDENTIFIER bigint not null auto_increment,
   ENTITY_MAP_ID bigint,
   STATIC_ENTITY_RECORD_ID bigint,
   DYNAMIC_ENTITY_RECORD_ID bigint,
   CREATED_DATE date,
   MODIFIED_DATE date,
   CREATED_BY varchar(255),
   STATUS varchar(10),
   primary key (IDENTIFIER)
);
create table CATISSUE_ENTITY_MAP_CONDITIONS (
   IDENTIFIER bigint not null auto_increment,
   STATIC_RECORD_ID bigint,
   TYPE_ID bigint,
   ENTITY_MAP_ID bigint,
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
   PARTICIPANT_ID bigint not null,
   RACE_NAME varchar(50)
);
create table CATISSUE_CLINICAL_REPORT (
   IDENTIFIER bigint not null auto_increment,
   SURGICAL_PATHOLOGICAL_NUMBER varchar(50),
   PARTICIPENT_MEDI_IDENTIFIER_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_COLL_SPECIMEN_REQ (
   COLLECTION_PROTOCOL_EVENT_ID bigint not null,
   SPECIMEN_REQUIREMENT_ID bigint not null,
   primary key (COLLECTION_PROTOCOL_EVENT_ID, SPECIMEN_REQUIREMENT_ID)
);
create table CATISSUE_SPECI_ARRAY_CONTENT (
   IDENTIFIER bigint not null auto_increment,
   CONC_IN_MICROGM_PER_MICROLTR double precision,
   INITIAL_QUANTITY_ID bigint,
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
   STORAGE_CONTAINER_ID bigint,
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
   SPECIMEN_ARRAY_ID bigint,
   DISTRIBUTION_ID bigint,
   primary key (IDENTIFIER)
);
create table CATISSUE_PARTICIPANT (
   IDENTIFIER bigint not null auto_increment,
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
   MARITAL_STATUS varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_CHAR (
   IDENTIFIER bigint not null auto_increment,
   TISSUE_SITE varchar(150),
   TISSUE_SIDE varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN_EVENT_PARAM (
   IDENTIFIER bigint not null auto_increment,
   SPECIMEN_ID bigint,
   EVENT_TIMESTAMP datetime,
   USER_ID bigint,
   COMMENTS text,
   primary key (IDENTIFIER)
);
create table CATISSUE_STOR_TYPE_SPEC_CLASS (
   STORAGE_TYPE_ID bigint not null,
   SPECIMEN_CLASS varchar(50)
);
create table CATISSUE_COLL_PROT_EVENT (
   IDENTIFIER bigint not null auto_increment,
   CLINICAL_STATUS varchar(50),
   STUDY_CALENDAR_EVENT_POINT double precision,
   COLLECTION_PROTOCOL_ID bigint,
   primary key (IDENTIFIER)
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
   ORDER_ID bigint,
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
   primary key (IDENTIFIER)
);
create table CATISSUE_FROZEN_EVENT_PARAM (
   IDENTIFIER bigint not null,
   METHOD varchar(50),
   primary key (IDENTIFIER)
);
create table CATISSUE_SPECIMEN (
   IDENTIFIER bigint not null auto_increment,
   SPECIMEN_CLASS varchar(255) not null,
   TYPE varchar(50),
   LABEL varchar(255) unique,
   LINEAGE varchar(50),
   PATHOLOGICAL_STATUS varchar(50),
   AVAILABLE bit,
   POSITION_DIMENSION_ONE integer,
   POSITION_DIMENSION_TWO integer,
   BARCODE varchar(255) unique,
   COMMENTS text,
   ACTIVITY_STATUS varchar(50),
   PARENT_SPECIMEN_ID bigint,
   STORAGE_CONTAINER_IDENTIFIER bigint,
   SPECIMEN_COLLECTION_GROUP_ID bigint,
   SPECIMEN_CHARACTERISTICS_ID bigint,
   AVAILABLE_QUANTITY double precision,
   QUANTITY double precision,
   CONCENTRATION double precision,
   primary key (IDENTIFIER)
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
create table CATISSUE_SPECIMEN_COLL_GROUP (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255) unique,
   CLINICAL_DIAGNOSIS varchar(150),
   CLINICAL_STATUS varchar(50),
   ACTIVITY_STATUS varchar(50),
   SITE_ID bigint,
   COLLECTION_PROTOCOL_EVENT_ID bigint,
   CLINICAL_REPORT_ID bigint,
   COLLECTION_PROTOCOL_REG_ID bigint,
   SURGICAL_PATHOLOGY_NUMBER varchar(50),
   primary key (IDENTIFIER)
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

create table CATISSUE_RETURN_EVENT_PARAM (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);

alter table CATISSUE_AUDIT_EVENT_QUERY_LOG add index FK62DC439DBC7298A9 (AUDIT_EVENT_ID), add constraint FK62DC439DBC7298A9 foreign key (AUDIT_EVENT_ID) references CATISSUE_AUDIT_EVENT (IDENTIFIER);

alter table CATISSUE_COLL_COORDINATORS add index FKE490E33A48304401 (COLLECTION_PROTOCOL_ID), add constraint FKE490E33A48304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_COLL_COORDINATORS add index FKE490E33A2206F20F (USER_ID), add constraint FKE490E33A2206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_COLLECTION_PROTOCOL add index FK32DC439DBC7298A9 (IDENTIFIER), add constraint FK32DC439DBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_PROTOCOL (IDENTIFIER);
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
alter table CATISSUE_CONTAINER add index FK49B8DE5DB097B2E (PARENT_CONTAINER_ID), add constraint FK49B8DE5DB097B2E foreign key (PARENT_CONTAINER_ID) references CATISSUE_CONTAINER (IDENTIFIER);
alter table CATISSUE_CONTAINER add index FK49B8DE5DAC76C0 (CAPACITY_ID), add constraint FK49B8DE5DAC76C0 foreign key (CAPACITY_ID) references CATISSUE_CAPACITY (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION_SPE_REQ add index FKE34A3688BE10F0CE (SPECIMEN_REQUIREMENT_ID), add constraint FKE34A3688BE10F0CE foreign key (SPECIMEN_REQUIREMENT_ID) references CATISSUE_SPECIMEN_REQUIREMENT (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION_SPE_REQ add index FKE34A36886B1F36E7 (DISTRIBUTION_PROTOCOL_ID), add constraint FKE34A36886B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_SITE add index FKB024C3436CD94566 (ADDRESS_ID), add constraint FKB024C3436CD94566 foreign key (ADDRESS_ID) references CATISSUE_ADDRESS (IDENTIFIER);
alter table CATISSUE_SITE add index FKB024C3432206F20F (USER_ID), add constraint FKB024C3432206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_EMBEDDED_EVENT_PARAM add index FKD356182FBC7298A9 (IDENTIFIER), add constraint FKD356182FBC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_IN_OUT_EVENT_PARAM add index FK4F0FAEB9BC7298A9 (IDENTIFIER), add constraint FK4F0FAEB9BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_COLL_DISTRIBUTION_REL add index FK1483BCB56B1F36E7 (DISTRIBUTION_PROTOCOL_ID), add constraint FK1483BCB56B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_COLL_DISTRIBUTION_REL add index FK1483BCB548304401 (COLLECTION_PROTOCOL_ID), add constraint FK1483BCB548304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_DISPOSAL_EVENT_PARAM add index FK1BC818D6BC7298A9 (IDENTIFIER), add constraint FK1BC818D6BC7298A9 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_SPECIMEN_REQUIREMENT add index FK39AFE96861A1C94F (QUANTITY_ID), add constraint FK39AFE96861A1C94F foreign key (QUANTITY_ID) references CATISSUE_QUANTITY (IDENTIFIER);
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
alter table CATISSUE_CLINICAL_REPORT add index FK54A4264515246F7 (PARTICIPENT_MEDI_IDENTIFIER_ID), add constraint FK54A4264515246F7 foreign key (PARTICIPENT_MEDI_IDENTIFIER_ID) references CATISSUE_PART_MEDICAL_ID (IDENTIFIER);
alter table CATISSUE_COLL_SPECIMEN_REQ add index FK860E6ABEBE10F0CE (SPECIMEN_REQUIREMENT_ID), add constraint FK860E6ABEBE10F0CE foreign key (SPECIMEN_REQUIREMENT_ID) references CATISSUE_SPECIMEN_REQUIREMENT (IDENTIFIER);
alter table CATISSUE_COLL_SPECIMEN_REQ add index FK860E6ABE53B01F66 (COLLECTION_PROTOCOL_EVENT_ID), add constraint FK860E6ABE53B01F66 foreign key (COLLECTION_PROTOCOL_EVENT_ID) references CATISSUE_COLL_PROT_EVENT (IDENTIFIER);
alter table CATISSUE_SPECI_ARRAY_CONTENT add index FKBEA9D458C4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKBEA9D458C4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);
alter table CATISSUE_SPECI_ARRAY_CONTENT add index FKBEA9D45860773DB2 (SPECIMEN_ID), add constraint FKBEA9D45860773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_SPECI_ARRAY_CONTENT add index FKBEA9D45892AB74B4 (INITIAL_QUANTITY_ID), add constraint FKBEA9D45892AB74B4 foreign key (INITIAL_QUANTITY_ID) references CATISSUE_QUANTITY (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3E64B129CC (CREATED_BY_ID), add constraint FKECBF8B3E64B129CC foreign key (CREATED_BY_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EF8278B6 (DISTRIBUTION_ID), add constraint FKECBF8B3EF8278B6 foreign key (DISTRIBUTION_ID) references CATISSUE_DISTRIBUTION (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EBC7298A9 (IDENTIFIER), add constraint FKECBF8B3EBC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EB3DFB11D (STORAGE_CONTAINER_ID), add constraint FKECBF8B3EB3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY add index FKECBF8B3EECE89343 (SPECIMEN_ARRAY_TYPE_ID), add constraint FKECBF8B3EECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ARRAY_TYPE add index FKD36E0B9BBC7298A9 (IDENTIFIER), add constraint FKD36E0B9BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_CONTAINER_TYPE (IDENTIFIER);
alter table CATISSUE_DISTRIBUTED_ITEM add index FKA7C3ED4B60773DB2 (SPECIMEN_ID), add constraint FKA7C3ED4B60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_DISTRIBUTED_ITEM add index FKA7C3ED4BF8278B6 (DISTRIBUTION_ID), add constraint FKA7C3ED4BF8278B6 foreign key (DISTRIBUTION_ID) references CATISSUE_DISTRIBUTION (IDENTIFIER);
alter table CATISSUE_SPECIMEN_EVENT_PARAM add index FK753F33AD60773DB2 (SPECIMEN_ID), add constraint FK753F33AD60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_SPECIMEN_EVENT_PARAM add index FK753F33AD2206F20F (USER_ID), add constraint FK753F33AD2206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_STOR_TYPE_SPEC_CLASS add index FK1BCF33BA59A3CE5C (STORAGE_TYPE_ID), add constraint FK1BCF33BA59A3CE5C foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
alter table CATISSUE_COLL_PROT_EVENT add index FK7AE7715948304401 (COLLECTION_PROTOCOL_ID), add constraint FK7AE7715948304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);
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
alter table CATISSUE_SPECIMEN add index FK1674810456906F39 (SPECIMEN_CHARACTERISTICS_ID), add constraint FK1674810456906F39 foreign key (SPECIMEN_CHARACTERISTICS_ID) references CATISSUE_SPECIMEN_CHAR (IDENTIFIER);
alter table CATISSUE_SPECIMEN add index FK1674810433BF33C5 (SPECIMEN_COLLECTION_GROUP_ID), add constraint FK1674810433BF33C5 foreign key (SPECIMEN_COLLECTION_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_SPECIMEN add index FK16748104B189E99D (PARENT_SPECIMEN_ID), add constraint FK16748104B189E99D foreign key (PARENT_SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_SPECIMEN add index FK1674810432B31EAB (STORAGE_CONTAINER_IDENTIFIER), add constraint FK1674810432B31EAB foreign key (STORAGE_CONTAINER_IDENTIFIER) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_USER add index FKB025CFC71792AD22 (INSTITUTION_ID), add constraint FKB025CFC71792AD22 foreign key (INSTITUTION_ID) references CATISSUE_INSTITUTION (IDENTIFIER);
alter table CATISSUE_USER add index FKB025CFC7FFA96920 (CANCER_RESEARCH_GROUP_ID), add constraint FKB025CFC7FFA96920 foreign key (CANCER_RESEARCH_GROUP_ID) references CATISSUE_CANCER_RESEARCH_GROUP (IDENTIFIER);
alter table CATISSUE_USER add index FKB025CFC76CD94566 (ADDRESS_ID), add constraint FKB025CFC76CD94566 foreign key (ADDRESS_ID) references CATISSUE_ADDRESS (IDENTIFIER);
alter table CATISSUE_USER add index FKB025CFC7F30C2528 (DEPARTMENT_ID), add constraint FKB025CFC7F30C2528 foreign key (DEPARTMENT_ID) references CATISSUE_DEPARTMENT (IDENTIFIER);
alter table CATISSUE_TIS_SPE_EVENT_PARAM add index FKBB9648F4BC7298A9 (IDENTIFIER), add constraint FKBB9648F4BC7298A9 foreign key (IDENTIFIER) references CATISSUE_EVENT_PARAM (IDENTIFIER);
alter table CATISSUE_ST_CONT_COLL_PROT_REL add index FK3AE9FCA7B3DFB11D (STORAGE_CONTAINER_ID), add constraint FK3AE9FCA7B3DFB11D foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);
alter table CATISSUE_ST_CONT_COLL_PROT_REL add index FK3AE9FCA748304401 (COLLECTION_PROTOCOL_ID), add constraint FK3AE9FCA748304401 foreign key (COLLECTION_PROTOCOL_ID) references CATISSUE_COLLECTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_SPECIMEN_COLL_GROUP add index FKDEBAF167A7F77D13 (SITE_ID), add constraint FKDEBAF167A7F77D13 foreign key (SITE_ID) references CATISSUE_SITE (IDENTIFIER);
alter table CATISSUE_SPECIMEN_COLL_GROUP add index FKDEBAF1674CE21DDA (CLINICAL_REPORT_ID), add constraint FKDEBAF1674CE21DDA foreign key (CLINICAL_REPORT_ID) references CATISSUE_CLINICAL_REPORT (IDENTIFIER);
alter table CATISSUE_SPECIMEN_COLL_GROUP add index FKDEBAF16753B01F66 (COLLECTION_PROTOCOL_EVENT_ID), add constraint FKDEBAF16753B01F66 foreign key (COLLECTION_PROTOCOL_EVENT_ID) references CATISSUE_COLL_PROT_EVENT (IDENTIFIER);
alter table CATISSUE_SPECIMEN_COLL_GROUP add index FKDEBAF1677E07C4AC (COLLECTION_PROTOCOL_REG_ID), add constraint FKDEBAF1677E07C4AC foreign key (COLLECTION_PROTOCOL_REG_ID) references CATISSUE_COLL_PROT_REG (IDENTIFIER);
alter table CATISSUE_SPECIMEN_TYPE add index FKFF69C195ECE89343 (SPECIMEN_ARRAY_TYPE_ID), add constraint FKFF69C195ECE89343 foreign key (SPECIMEN_ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_PERMISSIBLE_VALUE add index FK57DDCE153B5435E (PARENT_IDENTIFIER), add constraint FK57DDCE153B5435E foreign key (PARENT_IDENTIFIER) references CATISSUE_PERMISSIBLE_VALUE (IDENTIFIER);
alter table CATISSUE_PERMISSIBLE_VALUE add index FK57DDCE1FC56C2B1 (PUBLIC_ID), add constraint FK57DDCE1FC56C2B1 foreign key (PUBLIC_ID) references CATISSUE_CDE (PUBLIC_ID);
alter table CATISSUE_AUDIT_EVENT add index FKACAF697A2206F20F (USER_ID), add constraint FKACAF697A2206F20F foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_AUDIT_EVENT_LOG add index FK8BB672DF77F0B904 (AUDIT_EVENT_ID), add constraint FK8BB672DF77F0B904 foreign key (AUDIT_EVENT_ID) references CATISSUE_AUDIT_EVENT (IDENTIFIER);
alter table CATISSUE_AUDIT_EVENT_DETAILS add index FK5C07745D34FFD77F (AUDIT_EVENT_LOG_ID), add constraint FK5C07745D34FFD77F foreign key (AUDIT_EVENT_LOG_ID) references CATISSUE_AUDIT_EVENT_LOG (IDENTIFIER);
alter table CATISSUE_RETURN_EVENT_PARAM add index FKD8890A48BC7298A91 (IDENTIFIER), add constraint FKD8890A48BC7298A91 foreign key (IDENTIFIER) references CATISSUE_SPECIMEN_EVENT_PARAM (IDENTIFIER);

/****caTIES Realated Tables - start**********/

alter table CATISSUE_REPORT_TEXTCONTENT drop foreign key FKD74882FD91092806;
alter table CATISSUE_REPORT_TEXTCONTENT drop foreign key FKD74882FDBC7298A9;
alter table CATISSUE_IDENTIFIED_REPORT drop foreign key FK6A2246DCBC7298A9;
alter table CATISSUE_IDENTIFIED_REPORT drop foreign key FK6A2246DC752DD177;
alter table CATISSUE_IDENTIFIED_REPORT drop foreign key FK6A2246DC91741663;
alter table CATISSUE_CONCEPT_REFERENT drop foreign key FK799CCA7E9F96B363;
alter table CATISSUE_REVIEW_PARAMS drop foreign key FK5311FFF62206F20F;
alter table CATISSUE_REVIEW_PARAMS drop foreign key FK5311FFF691092806;
alter table CATISSUE_REPORT_BICONTENT drop foreign key FK8A9A4EE391092806;
alter table CATISSUE_REPORT_BICONTENT drop foreign key FK8A9A4EE3BC7298A9;
alter table CATISSUE_DEIDENTIFIED_REPORT drop foreign key FKCDD0DF7BBC7298A9;
alter table CATISSUE_DEIDENTIFIED_REPORT drop foreign key FKCDD0DF7B91741663;
alter table CATISSUE_QUARANTINE_PARAMS drop foreign key FK3C12AE3B2206F20F;
alter table CATISSUE_QUARANTINE_PARAMS drop foreign key FK3C12AE3B3EEC14E3;
alter table CATISSUE_PATHOLOGY_REPORT drop foreign key FK904EC9F040DCD7BF;
alter table CATISSUE_REPORT_XMLCONTENT drop foreign key FK4597C9F1BC7298A9;
alter table CATISSUE_REPORT_XMLCONTENT drop foreign key FK4597C9F191092806;
/*Ashish --8 march 07 */
alter table CATISSUE_REPORT_QUEUE drop foreign key FK214246228CA560D1;
alter table CATISSUE_CONCEPT_REFERENT drop foreign key FK799CCA7EA9816272;
alter table CATISSUE_CONCEPT_REFERENT drop foreign key FK799CCA7E72C371DD;

drop table if exists CATISSUE_REPORT_TEXTCONTENT;
drop table if exists CATISSUE_IDENTIFIED_REPORT;
drop table if exists CATISSUE_CONCEPT_REFERENT;
drop table if exists CATISSUE_REPORT_CONTENT;
drop table if exists CATISSUE_REVIEW_PARAMS;
drop table if exists CATISSUE_REPORT_BICONTENT;
drop table if exists CATISSUE_REPORT_SECTION;
drop table if exists CATISSUE_DEIDENTIFIED_REPORT;
drop table if exists CATISSUE_PATHOLOGY_REPORT;
drop table if exists CATISSUE_REPORT_XMLCONTENT;
drop table if exists CATISSUE_REPORT_QUEUE;
drop table if exists CATISSUE_REPORT_PARTICIP_REL;


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
/* Ashish 8/3/07*/
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
/* Ashish 8/3/07*/
create table CATISSUE_REPORT_QUEUE (
   IDENTIFIER bigint not null auto_increment,
   STATUS varchar(50),
   REPORT_TEXT text,
   SPECIMEN_COLL_GRP_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_REPORT_PARTICIP_REL(
   PARTICIPANT_ID bigint,
   REPORT_ID bigint
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

/*Ashish 8/3/07*/
alter table CATISSUE_REPORT_QUEUE add index FK214246228CA560D1 (SPECIMEN_COLL_GRP_ID), add constraint FK214246228CA560D1 foreign key (SPECIMEN_COLL_GRP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);

/* caTies tables for Concept HighLighting  -- Ashish -- 8 March,07 */

alter table CATISSUE_CONCEPT drop foreign key FKC1A3C8CC7F0C2C7;

drop table if exists CATISSUE_CONCEPT;
drop table if exists CATISSUE_SEMANTIC_TYPE;
drop table if exists CATISSUE_CONCEPT_CLASSIFICATN;

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

alter table CATISSUE_CONCEPT add index FKC1A3C8CC7F0C2C7 (SEMANTIC_TYPE_ID), add constraint FKC1A3C8CC7F0C2C7 foreign key (SEMANTIC_TYPE_ID) references CATISSUE_SEMANTIC_TYPE (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add index FK799CCA7EA9816272 (CONCEPT_ID), add constraint FK799CCA7EA9816272 foreign key (CONCEPT_ID) references CATISSUE_CONCEPT (IDENTIFIER);
alter table CATISSUE_CONCEPT_REFERENT add index FK799CCA7E72C371DD (CONCEPT_CLASSIFICATION_ID), add constraint FK799CCA7E72C371DD foreign key (CONCEPT_CLASSIFICATION_ID) references CATISSUE_CONCEPT_CLASSIFICATN (IDENTIFIER);


/****caTIES Realated Tables - end**********/

#------- ordering system relate tables----


 
drop table if exists CATISSUE_EXISTING_SP_ORD_ITEM;
drop table if exists CATISSUE_PATH_CASE_ORDER_ITEM;
drop table if exists CATISSUE_ORDER_ITEM;
drop table if exists CATISSUE_DERIEVED_SP_ORD_ITEM;
drop table if exists CATISSUE_ORDER;
drop table if exists CATISSUE_SP_ARRAY_ORDER_ITEM;
drop table if exists CATISSUE_SPECIMEN_ORDER_ITEM;
drop table if exists CATISSUE_NEW_SP_AR_ORDER_ITEM;

create table CATISSUE_EXISTING_SP_ORD_ITEM (
   IDENTIFIER bigint not null,
   SPECIMEN_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_PATH_CASE_ORDER_ITEM (
   IDENTIFIER bigint not null,
   PATHOLOGICAL_STATUS varchar(255),
   TISSUE_SITE varchar(255),
   SPECIMEN_CLASS varchar(255),
   SPECIMEN_TYPE varchar(255),
   SPECIMEN_COLL_GROUP_ID bigint,
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

create table CATISSUE_DERIEVED_SP_ORD_ITEM (
   IDENTIFIER bigint not null,
   SPECIMEN_CLASS varchar(255),
   SPECIMEN_TYPE varchar(255),
   SPECIMEN_ID bigint,
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

create table CATISSUE_SPECIMEN_ORDER_ITEM (
   IDENTIFIER bigint not null,
   ARRAY_ORDER_ITEM_ID bigint,
   primary key (IDENTIFIER)
);

#-----------------Array order item tables.
create table CATISSUE_SP_ARRAY_ORDER_ITEM (
   IDENTIFIER bigint not null,
   SPECIMEN_ARRAY_ID bigint,
   primary key (IDENTIFIER)
);

create table CATISSUE_NEW_SP_AR_ORDER_ITEM (
   IDENTIFIER bigint not null,
   ARRAY_TYPE_ID bigint,
   NAME varchar(255),
   SPECIMEN_ARRAY_ID bigint,
   primary key (IDENTIFIER)
);

 
alter table CATISSUE_EXISTING_SP_ORD_ITEM add index FKF8B855EEBC7298A9 (IDENTIFIER), add constraint FKF8B855EEBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_EXISTING_SP_ORD_ITEM add index FKF8B855EE60773DB2 (SPECIMEN_ID), add constraint FKF8B855EE60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_PATH_CASE_ORDER_ITEM add index FKBD5029D5F69249F7 (SPECIMEN_COLL_GROUP_ID), add constraint FKBD5029D5F69249F7 foreign key (SPECIMEN_COLL_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);
alter table CATISSUE_PATH_CASE_ORDER_ITEM add index FKBD5029D5BC7298A9 (IDENTIFIER), add constraint FKBD5029D5BC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_ORDER_ITEM add index FKB501E88060975C0B (DISTRIBUTED_ITEM_ID), add constraint FKB501E88060975C0B foreign key (DISTRIBUTED_ITEM_ID) references CATISSUE_DISTRIBUTED_ITEM (IDENTIFIER);
alter table CATISSUE_ORDER_ITEM add index FKB501E880783867CC (ORDER_ID), add constraint FKB501E880783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_DISTRIBUTION add index FK54276680783867CC (ORDER_ID), add constraint FK54276680783867CC foreign key (ORDER_ID) references CATISSUE_ORDER (IDENTIFIER);
alter table CATISSUE_DISTRIBUTED_ITEM add index FKA7C3ED4BC4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKA7C3ED4BC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);
alter table CATISSUE_DERIEVED_SP_ORD_ITEM add index FK3742152BBC7298A9 (IDENTIFIER), add constraint FK3742152BBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_DERIEVED_SP_ORD_ITEM add index FK3742152B60773DB2 (SPECIMEN_ID), add constraint FK3742152B60773DB2 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);
alter table CATISSUE_ORDER add index FK543F22B26B1F36E7 (DISTRIBUTION_PROTOCOL_ID), add constraint FK543F22B26B1F36E7 foreign key (DISTRIBUTION_PROTOCOL_ID) references CATISSUE_DISTRIBUTION_PROTOCOL (IDENTIFIER);
alter table CATISSUE_SP_ARRAY_ORDER_ITEM add index FKE3823170BC7298A9 (IDENTIFIER), add constraint FKE3823170BC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_SP_ARRAY_ORDER_ITEM add index FKE3823170C4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKE3823170C4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ORDER_ITEM add index FK48C3B39FBC7298A9 (IDENTIFIER), add constraint FK48C3B39FBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_SPECIMEN_ORDER_ITEM add index FK48C3B39F83505A30 (ARRAY_ORDER_ITEM_ID), add constraint FK48C3B39F83505A30 foreign key (ARRAY_ORDER_ITEM_ID) references CATISSUE_NEW_SP_AR_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBCE5FBC3A (ARRAY_TYPE_ID), add constraint FKC5C92CCBCE5FBC3A foreign key (ARRAY_TYPE_ID) references CATISSUE_SPECIMEN_ARRAY_TYPE (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBBC7298A9 (IDENTIFIER), add constraint FKC5C92CCBBC7298A9 foreign key (IDENTIFIER) references CATISSUE_ORDER_ITEM (IDENTIFIER);
alter table CATISSUE_NEW_SP_AR_ORDER_ITEM add index FKC5C92CCBC4A3C438 (SPECIMEN_ARRAY_ID), add constraint FKC5C92CCBC4A3C438 foreign key (SPECIMEN_ARRAY_ID) references CATISSUE_SPECIMEN_ARRAY (IDENTIFIER)

#------ Consent Tracking related drop, create and add foreign key scripts.

drop table if exists CATISSUE_CONSENT_TIER_RESPONSE;
drop table if exists CATISSUE_CONSENT_TIER_STATUS;
drop table if exists CATISSUE_CONSENT_TIER;

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


