SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS CATISSUE_CCTS_NOTIF;
create table CATISSUE_CCTS_NOTIF (
   IDENTIFIER bigint not null auto_increment,
   DATE_SENT datetime not null, 
   DATE_RECEIVED datetime not null, 
   EVENT_TYPE_ID bigint not null,
   APPLICATION_ID bigint null,
   OBJECT_ID_TYPE bigint not null,
   OBJECT_ID_VALUE varchar(128) not null,
   PROCESSING_STATUS_ID bigint not null,
   primary key (IDENTIFIER)
);

DROP TABLE IF EXISTS CATISSUE_CCTS_NOTIF_LOG;
create table CATISSUE_CCTS_NOTIF_LOG (
   IDENTIFIER bigint not null auto_increment,
   NOTIFICATION_ID bigint not null,
   DATE_TIME datetime not null, 
   PAYLOAD text null,
   ERROR_CODE varchar(255) null,
   PROCESSING_RESULT_ID bigint not null,
   USER_ID bigint null,
   primary key (IDENTIFIER)
);

DROP TABLE IF EXISTS CATISSUE_CCTS_DATA_QUEUE;
create table CATISSUE_CCTS_DATA_QUEUE (
   IDENTIFIER bigint not null auto_increment,
   PAYLOAD text not null,
   DATE_TIME datetime not null,
   NOTIFICATION_ID bigint null,
   PROCESSING_STATUS_ID bigint not null,
   USER_ID bigint null,
   PARTICIPANT_ID bigint null,
   primary key (IDENTIFIER)
);

DROP TABLE IF EXISTS CATISSUE_CCTS_APPLICATION;
create table CATISSUE_CCTS_APPLICATION (
   IDENTIFIER bigint not null,
   NAME varchar(32) not null,
   primary key (IDENTIFIER)
);

DROP TABLE IF EXISTS CATISSUE_CCTS_EVENT_TYPE;
create table CATISSUE_CCTS_EVENT_TYPE (
   IDENTIFIER bigint not null,
   NAME varchar(64) not null,
   primary key (IDENTIFIER)
);

DROP TABLE IF EXISTS CATISSUE_CCTS_OBJ_ID_TYPE;
create table CATISSUE_CCTS_OBJ_ID_TYPE (
   IDENTIFIER bigint not null,
   NAME varchar(64) not null,
   primary key (IDENTIFIER)
);

DROP TABLE IF EXISTS CATISSUE_CCTS_PROC_STATUS;
create table CATISSUE_CCTS_PROC_STATUS (
   IDENTIFIER bigint not null,
   NAME varchar(64) not null,
   primary key (IDENTIFIER)
);

DROP TABLE IF EXISTS CATISSUE_CCTS_PROC_RESULT;
create table CATISSUE_CCTS_PROC_RESULT (
   IDENTIFIER bigint not null,
   NAME varchar(64) not null,
   primary key (IDENTIFIER)
);

SET FOREIGN_KEY_CHECKS=1;

alter table CATISSUE_CCTS_NOTIF add constraint CCTSFK00001 foreign key (EVENT_TYPE_ID) references CATISSUE_CCTS_EVENT_TYPE (IDENTIFIER);
alter table CATISSUE_CCTS_NOTIF add constraint CCTSFK00002 foreign key (APPLICATION_ID) references CATISSUE_CCTS_APPLICATION (IDENTIFIER);
alter table CATISSUE_CCTS_NOTIF add constraint CCTSFK00003 foreign key (OBJECT_ID_TYPE) references CATISSUE_CCTS_OBJ_ID_TYPE (IDENTIFIER);
alter table CATISSUE_CCTS_NOTIF add constraint CCTSFK00004 foreign key (PROCESSING_STATUS_ID) references CATISSUE_CCTS_PROC_STATUS (IDENTIFIER);
alter table CATISSUE_CCTS_NOTIF_LOG add constraint CCTSFK00005 foreign key (NOTIFICATION_ID) references CATISSUE_CCTS_NOTIF (IDENTIFIER);
alter table CATISSUE_CCTS_NOTIF_LOG add constraint CCTSFK00006 foreign key (PROCESSING_RESULT_ID) references CATISSUE_CCTS_PROC_RESULT (IDENTIFIER);
alter table CATISSUE_CCTS_NOTIF_LOG add constraint CCTSFK00007 foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_CCTS_DATA_QUEUE add constraint CCTSFK00008 foreign key (NOTIFICATION_ID) references CATISSUE_CCTS_NOTIF (IDENTIFIER);
alter table CATISSUE_CCTS_DATA_QUEUE add constraint CCTSFK00009 foreign key (PROCESSING_STATUS_ID) references CATISSUE_CCTS_PROC_STATUS (IDENTIFIER);
alter table CATISSUE_CCTS_DATA_QUEUE add constraint CCTSFK00010 foreign key (USER_ID) references CATISSUE_USER (IDENTIFIER);
alter table CATISSUE_CCTS_DATA_QUEUE add constraint CCTSFK00011 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT (IDENTIFIER);

INSERT INTO catissue_ccts_application (IDENTIFIER,NAME) VALUES (1,'C3PR');
INSERT INTO catissue_ccts_application (IDENTIFIER,NAME) VALUES (2,'PSC');

INSERT INTO catissue_ccts_event_type (IDENTIFIER,NAME) VALUES (1,'SUBJECT_CREATION');
INSERT INTO catissue_ccts_event_type (IDENTIFIER,NAME) VALUES (2,'STUDY_CREATION');
INSERT INTO catissue_ccts_event_type (IDENTIFIER,NAME) VALUES (3,'SUBJECT_REGISTRATION');
INSERT INTO catissue_ccts_event_type (IDENTIFIER,NAME) VALUES (4,'STUDY_CALENDAR_UPDATE');

INSERT INTO catissue_ccts_obj_id_type (IDENTIFIER,NAME) VALUES (1,'GRID_ID');

INSERT INTO catissue_ccts_proc_result (IDENTIFIER,NAME) VALUES (1,'FAILURE');
INSERT INTO catissue_ccts_proc_result (IDENTIFIER,NAME) VALUES (2,'SUCCESS');

INSERT INTO catissue_ccts_proc_status (IDENTIFIER,NAME) VALUES (1,'CANCELLED');
INSERT INTO catissue_ccts_proc_status (IDENTIFIER,NAME) VALUES (2,'COMPLETED');
INSERT INTO catissue_ccts_proc_status (IDENTIFIER,NAME) VALUES (3,'PENDING');
INSERT INTO catissue_ccts_proc_status (IDENTIFIER,NAME) VALUES (4,'REJECTED');
INSERT INTO catissue_ccts_proc_status (IDENTIFIER,NAME) VALUES (5,'PROCESSING');


ALTER TABLE catissue_participant ADD GRID_ID varchar(128);

ALTER TABLE catissue_site ADD CTEP_ID varchar(50);
ALTER TABLE catissue_site add constraint CTEP_ID UNIQUE KEY (CTEP_ID);

ALTER TABLE catissue_coll_prot_reg ADD GRID_ID varchar(128);
alter table CATISSUE_CCTS_DATA_QUEUE add registration_id bigint null;
alter table CATISSUE_CCTS_DATA_QUEUE add constraint CCTSFK00012 foreign key (registration_id) references catissue_coll_prot_reg (IDENTIFIER);

alter table catissue_specimen_protocol add irb_site_id bigint null;
alter table catissue_specimen_protocol add constraint CCTSFK00013 foreign key (irb_site_id) references catissue_site (IDENTIFIER);
alter table CATISSUE_CCTS_DATA_QUEUE add incoming BOOLEAN NOT NULL DEFAULT true;

ALTER TABLE `csm_migrate_user` ADD COLUMN `IDENTITY` VARCHAR(100) NULL;

INSERT INTO catissue_department (IDENTIFIER, NAME) VALUES (9999, 'Other_Department');
INSERT INTO catissue_institution (IDENTIFIER, NAME) VALUES (9999, 'Other_Institution');
INSERT INTO catissue_cancer_research_group (IDENTIFIER, NAME) VALUES (9999, 'Other_CancerResearchGroup');

ALTER TABLE catissue_cp_grid_prvg ADD COLUMN ROLE_ID VARCHAR(255) NULL DEFAULT NULL;

