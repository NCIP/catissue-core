create table INTERCEPTOR_ERROR_OBJ (IDENTIFIER bigint(30) NOT NULL,ERROR_TIMESTAMP datetime, OBJECT_TYPE varchar(400),ERROR_CODE varchar(200),OBJECT_ID bigint(30),RECOVERY_DONE boolean,EVENT_CODE bigint(30),NUMBER_OF_TRY int,PROCESSOR_CLASS varchar(400),PRIMARY KEY  (IDENTIFIER));
create table CATISSUE_SPECIMEN_CIDER_MESSAGE (IDENTIFIER bigint(20) auto_increment primary key ,SPECIMEN_ID bigint(20),SENT_DATE datetime,EVENT_TYPE varchar(100));


create table PARTICIPANT_MERGE_MESSAGES(
IDENTIFIER bigint(20) not null auto_increment ,
MESSAGE_TYPE varchar(50),
MESSAGE_DATE date,
HL7_MESSAGE varchar(1500),
MESSAGE_STATUS varchar(200),
primary key (IDENTIFIER)
);

alter table catissue_collection_protocol add column IS_EMPI_ENABLE boolean;

update catissue_collection_protocol set IS_EMPI_ENABLE = false; 
alter table catissue_participant add column EMPI_ID varchar(50);
alter table catissue_participant add column EMPI_ID_STATUS varchar(50);

CREATE TABLE MATCHED_PARTICIPANT_MAPPING(
   SEARCHED_PARTICIPANT_ID bigint NOT NULL,
   NO_OF_MATCHED_PARTICIPANTS bigint,
   CREATION_DATE DATE,
   PRIMARY KEY (SEARCHED_PARTICIPANT_ID),
   FOREIGN KEY (SEARCHED_PARTICIPANT_ID) REFERENCES CATISSUE_PARTICIPANT(IDENTIFIER)
);

CREATE TABLE EMPI_PARTICIPANT_USER_MAPPING(
	PARTICIPANT_ID bigint NOT NULL,
	USER_ID bigint,
	FOREIGN KEY (USER_ID) REFERENCES CATISSUE_USER(IDENTIFIER),
	FOREIGN KEY (PARTICIPANT_ID) REFERENCES MATCHED_PARTICIPANT_MAPPING(SEARCHED_PARTICIPANT_ID)  on delete cascade
);

create table CATISSUE_MATCHED_PARTICIPANT (
   PARTICIPANT_ID bigint,
   EMPI_ID VARCHAR(255),
   LAST_NAME varchar(255),
   FIRST_NAME varchar(255),
   MIDDLE_NAME varchar(255),
   BIRTH_DATE date,
   GENDER varchar(20),
   SOCIAL_SECURITY_NUMBER varchar(50),
   ACTIVITY_STATUS varchar(50),
   DEATH_DATE date,
   VITAL_STATUS varchar(50),
   PARTICIPANT_MRN  VARCHAR(3000),
   PARTICIPANT_RACE  VARCHAR(1000),
   IS_FROM_EMPI VARCHAR(20),
   SEARCHED_PARTICIPANT_ID bigint,
   FOREIGN KEY (SEARCHED_PARTICIPANT_ID) REFERENCES MATCHED_PARTICIPANT_MAPPING(SEARCHED_PARTICIPANT_ID)  on delete cascade
);

alter table catissue_site add column FACILITY_ID varchar(50);

create table PARTICIPANT_EMPI_ID_MAPPING (
   PERMANENT_PARTICIPANT_ID varchar(255),
   TEMPARARY_PARTICIPANT_ID varchar(255),
   OLD_EMPI_ID varchar(255)
);

alter table participant_empi_id_mapping add column TEMPMRNDATE datetime;