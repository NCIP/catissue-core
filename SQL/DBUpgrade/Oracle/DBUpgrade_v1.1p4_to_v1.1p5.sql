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

CREATE sequence LOGIN_EVENT_PARAM_SEQ;

 CREATE TABLE CATISSUE_LOGIN_AUDIT_EVENT_LOG
(
	IDENTIFIER number(19,0) not null ,
	LOGIN_TIMESTAMP date,
	USER_LOGIN_ID number(19,0),
	LOGIN_SOURCE_ID number(19,0),
	LOGIN_IP_ADDRESS varchar2(200),
	IS_LOGIN_SUCCESSFUL number(1,0),
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

/**
 * For converting the CURRENT_VALUE,PREVIOUS_VALUE columns from Varchar to CLOB
 */
ALTER TABLE catissue_audit_event_details ADD NEW_CURRENT_VALUE CLOB default NULL;
ALTER TABLE catissue_audit_event_details ADD NEW_PREVIOUS_VALUE CLOB default NULL;

UPDATE catissue_audit_event_details set NEW_CURRENT_VALUE=CURRENT_VALUE;
UPDATE catissue_audit_event_details set NEW_PREVIOUS_VALUE=PREVIOUS_VALUE;

ALTER TABLE catissue_audit_event_details DROP COLUMN CURRENT_VALUE ;
ALTER TABLE catissue_audit_event_details DROP COLUMN PREVIOUS_VALUE ;

ALTER TABLE catissue_audit_event_details RENAME COLUMN NEW_CURRENT_VALUE to CURRENT_VALUE;
ALTER TABLE catissue_audit_event_details RENAME COLUMN NEW_PREVIOUS_VALUE to PREVIOUS_VALUE;

/*For bug fix #15764*/
UPDATE CATISSUE_QUERY_TABLE_DATA set TABLE_NAME='CATISSUE_CP_REQ_SPECIMEN' where TABLE_NAME='CATISSUE_COLL_SPECIMEN_REQ';

/*Adding index on AUDIT_EVENT_ID to solve the performance issue*/
CREATE INDEX INDX_CAT_AUDIT_QUERY_AUDITID ON CATISSUE_AUDIT_EVENT_QUERY_LOG (AUDIT_EVENT_ID);

commit;