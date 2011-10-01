create table CATISSUE_CCTS_NOTIF (
   IDENTIFIER number(19,0) not null,
   DATE_SENT date not null,
   DATE_RECEIVED date not null,
   EVENT_TYPE_ID number(19,0) not null,
   APPLICATION_ID number(19,0) null,
   OBJECT_ID_TYPE number(19,0) not null,
   OBJECT_ID_VALUE varchar(128) not null,
   PROCESSING_STATUS_ID number(19,0) not null,
   primary key (IDENTIFIER)
);
CREATE sequence CATISSUE_CCTS_NOTIF_SEQ NOCACHE;


create table CATISSUE_CCTS_NOTIF_LOG (
   IDENTIFIER number(19,0) not null,
   NOTIFICATION_ID number(19,0) not null,
   DATE_TIME date not null, 
   PAYLOAD clob null,
   ERROR_CODE varchar(255) null,
   PROCESSING_RESULT_ID number(19,0) not null,
   USER_ID number(19,0) null,
   primary key (IDENTIFIER)
);
CREATE sequence CATISSUE_CCTS_NOTIF_LOG_SEQ NOCACHE;


create table CATISSUE_CCTS_DATA_QUEUE (
   IDENTIFIER number(19,0) not null,
   PAYLOAD clob not null,
   DATE_TIME date not null,
   NOTIFICATION_ID number(19,0) null,
   PROCESSING_STATUS_ID number(19,0) not null,
   USER_ID number(19,0) null,
   PARTICIPANT_ID number(19,0) null,
   primary key (IDENTIFIER)
);
CREATE sequence CATISSUE_CCTS_DATA_QUEUE_SEQ NOCACHE;


create table CATISSUE_CCTS_APPLICATION (
   IDENTIFIER number(19,0) not null,
   NAME varchar(32) not null,
   primary key (IDENTIFIER)
);


create table CATISSUE_CCTS_EVENT_TYPE (
   IDENTIFIER number(19,0) not null,
   NAME varchar(64) not null,
   primary key (IDENTIFIER)
);


create table CATISSUE_CCTS_OBJ_ID_TYPE (
   IDENTIFIER number(19,0) not null,
   NAME varchar(64) not null,
   primary key (IDENTIFIER)
);


create table CATISSUE_CCTS_PROC_STATUS (
   IDENTIFIER number(19,0) not null,
   NAME varchar(64) not null,
   primary key (IDENTIFIER)
);


create table CATISSUE_CCTS_PROC_RESULT (
   IDENTIFIER number(19,0) not null,
   NAME varchar(64) not null,
   primary key (IDENTIFIER)
);


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

ALTER TABLE catissue_participant ADD ( GRID_ID VARCHAR2(128) );

ALTER TABLE catissue_site ADD CTEP_ID varchar2(50);
ALTER TABLE catissue_site add constraint CTEP_ID UNIQUE (CTEP_ID);

ALTER TABLE catissue_coll_prot_reg ADD GRID_ID varchar2(128);
alter table CATISSUE_CCTS_DATA_QUEUE add registration_id number(19,0) null;
alter table CATISSUE_CCTS_DATA_QUEUE add constraint CCTSFK00012 foreign key (registration_id) references catissue_coll_prot_reg (IDENTIFIER);

alter table catissue_specimen_protocol add irb_site_id number(19,0) null;
alter table catissue_specimen_protocol add constraint CCTSFK00013 foreign key (irb_site_id) references catissue_site (IDENTIFIER);
alter table CATISSUE_CCTS_DATA_QUEUE add incoming NUMBER(1) DEFAULT 1 NOT NULL;


ALTER TABLE csm_migrate_user ADD IDENTITY varchar2(100 Byte);

INSERT INTO catissue_department (IDENTIFIER, NAME) VALUES (9999, 'Other_Department');
INSERT INTO catissue_institution (IDENTIFIER, NAME) VALUES (9999, 'Other_Institution');
INSERT INTO catissue_cancer_research_group (IDENTIFIER, NAME) VALUES (9999, 'Other_CancerResearchGroup');

ALTER TABLE catissue_cp_grid_prvg ADD ROLE_ID varchar2(255 Byte) NULL;
