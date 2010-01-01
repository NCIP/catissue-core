/* Adding WUSTLKey column in CATISSUE_USER table */
ALTER TABLE CATISSUE_USER ADD WUSTLKEY varchar2(100) UNIQUE;

/**  Bug 13225 -  Clinical Diagnosis subset at CP definition level 
 */
create table CATISSUE_CLINICAL_DIAGNOSIS (
   IDENTIFIER number(19,0) not null ,
   CLINICAL_DIAGNOSIS varchar(255),
   COLLECTION_PROTOCOL_ID number(19,0),
   primary key (IDENTIFIER),
   CONSTRAINT FK_CD_COLPROT FOREIGN KEY (COLLECTION_PROTOCOL_ID) REFERENCES CATISSUE_COLLECTION_PROTOCOL
);
create sequence CATISSUE_CLINICAL_DIAG_SEQ;

create table CATISSUE_STOR_CONT_SPEC_TYPE
(
   STORAGE_CONTAINER_ID number(19,0) not null,
   SPECIMEN_TYPE varchar(50)
);
alter table CATISSUE_STOR_CONT_SPEC_TYPE  add constraint FK_SPECIMEN_TYPE_ST_ID foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER;

create table CATISSUE_STOR_TYPE_SPEC_TYPE
(
   STORAGE_TYPE_ID number(19,0) not null,
   SPECIMEN_TYPE varchar(50)
);
alter table CATISSUE_STOR_TYPE_SPEC_TYPE add constraint FK_STORAGE_TYPE_ID foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE;

CREATE INDEX INDX_STOR_TYPE_SPEC ON catissue_stor_type_spec_type(STORAGE_TYPE_ID);

CREATE INDEX INDX_STOR_CONT_SPEC ON catissue_stor_cont_spec_type (STORAGE_CONTAINER_ID);

/**
 * Populate catissue_stor_type_spec_type
 */
insert into catissue_stor_type_spec_type
select STORAGE_TYPE_ID, t.typevalue from catissue_stor_type_spec_class sp,(select p.value, c.value typevalue from catissue_permissible_value p,catissue_permissible_value c
where c.parent_identifier = p.identifier and p.value in ('Molecular','Cell','Fluid','Tissue')) t
where t.value = sp.SPECIMEN_CLASS;

/**
 * Populate catissue_stor_cont_spec_type
 */
insert into catissue_stor_cont_spec_type
select STORAGE_CONTAINER_ID, t.typevalue from catissue_stor_cont_spec_class sp,(select p.value, c.value typevalue from catissue_permissible_value p,catissue_permissible_value c
where c.parent_identifier = p.identifier and p.value in ('Molecular','Cell','Fluid','Tissue')) t
where t.value = sp.SPECIMEN_CLASS;

/**
 * Audit changes.
 */
CREATE TABLE CATISSUE_DATA_AUDIT_EVENT_LOG
(
	IDENTIFIER NUMBER NOT NULL,
	OBJECT_IDENTIFIER NUMBER,
	OBJECT_NAME VARCHAR(50),
	PARENT_LOG_ID NUMBER,
	PRIMARY KEY (IDENTIFIER)
);

CREATE TABLE CATISSUE_LOGIN_AUDIT_EVENT_LOG
(
	IDENTIFIER NUMBER NOT NULL,
	LOGIN_TIMESTAMP DATE,
	USER_LOGIN_ID VARCHAR(20),
	LOGIN_SOURCE_ID NUMBER,
	LOGIN_IP_ADDRESS VARCHAR(20),
	IS_LOGIN_SUCCESSFUL VARCHAR(10),
	PRIMARY KEY (IDENTIFIER)
) ;

MERGE INTO CATISSUE_DATA_AUDIT_EVENT_LOG C1
USING (SELECT IDENTIFIER,OBJECT_IDENTIFIER,OBJECT_NAME FROM CATISSUE_AUDIT_EVENT_LOG) C2
ON (C1.IDENTIFIER = C2.IDENTIFIER)
WHEN NOT MATCHED THEN INSERT (C1.IDENTIFIER,C1.OBJECT_IDENTIFIER,C1.OBJECT_NAME)
VALUES (C2.IDENTIFIER,C2.OBJECT_IDENTIFIER,C2.OBJECT_NAME) ;

ALTER TABLE CATISSUE_DATA_AUDIT_EVENT_LOG ADD CONSTRAINT FK5C07745DC62F96A411
FOREIGN KEY (IDENTIFIER) REFERENCES CATISSUE_AUDIT_EVENT_LOG (IDENTIFIER);

ALTER TABLE CATISSUE_DATA_AUDIT_EVENT_LOG ADD CONSTRAINT FK5C07745DC62F96A412 
FOREIGN KEY (PARENT_LOG_ID) REFERENCES CATISSUE_DATA_AUDIT_EVENT_LOG (IDENTIFIER);

ALTER TABLE CATISSUE_AUDIT_EVENT_LOG DROP COLUMN OBJECT_IDENTIFIER ;
ALTER TABLE CATISSUE_AUDIT_EVENT_LOG DROP COLUMN OBJECT_NAME ;
ALTER TABLE CATISSUE_AUDIT_EVENT_LOG DROP COLUMN EVENT_TYPE ;

ALTER TABLE catissue_audit_event_details MODIFY CURRENT_VALUE varchar(4000) default NULL;
ALTER TABLE catissue_audit_event_details MODIFY PREVIOUS_VALUE varchar(4000) default NULL;

commit;