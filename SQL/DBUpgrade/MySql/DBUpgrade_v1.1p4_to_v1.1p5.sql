/* Adding WUSTLKEY column in catissue_user table*/
ALTER TABLE CATISSUE_USER ADD COLUMN WUSTLKEY varchar(100) UNIQUE;

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

/**
 * Container Type Req
 */
CREATE TABLE catissue_stor_type_spec_type 
(                                                                       
  STORAGE_TYPE_ID bigint(20) NOT NULL,                                                                          
  SPECIMEN_TYPE varchar(50)
);     
alter table catissue_stor_type_spec_type add index STORAGE_TYPE_ID_FK (STORAGE_TYPE_ID), add constraint STORAGE_TYPE_ID_FK foreign key (STORAGE_TYPE_ID) references CATISSUE_STORAGE_TYPE (IDENTIFIER);
/**
 * Container Type Req
 */
CREATE TABLE catissue_stor_cont_spec_type
(                                                                                 
  STORAGE_CONTAINER_ID bigint(20) NOT NULL,                                                                                  
  SPECIMEN_TYPE varchar(50) 
);      
alter table catissue_stor_cont_spec_type add index SPECIMEN_TYPE_ST_ID_FK (STORAGE_CONTAINER_ID), add constraint SPECIMEN_TYPE_ST_ID_FK foreign key (STORAGE_CONTAINER_ID) references CATISSUE_STORAGE_CONTAINER (IDENTIFIER);

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
 * Upgrade scripts for audit.
 */
ALTER TABLE CATISSUE_AUDIT_EVENT ADD COLUMN EVENT_TYPE varchar(20) ;

INSERT INTO CATISSUE_AUDIT_EVENT(IDENTIFIER,EVENT_TYPE)
SELECT AUDIT_EVENT_ID,EVENT_TYPE FROM CATISSUE_AUDIT_EVENT_LOG cl
ON DUPLICATE KEY UPDATE EVENT_TYPE = cl.EVENT_TYPE ;

CREATE TABLE CATISSUE_DATA_AUDIT_EVENT_LOG
(
	IDENTIFIER bigint NOT NULL auto_increment,
	OBJECT_IDENTIFIER bigint,
	OBJECT_NAME VARCHAR(50),
	PARENT_LOG_ID bigint,
	PRIMARY KEY (`IDENTIFIER`)
);

CREATE TABLE CATISSUE_LOGIN_AUDIT_EVENT_LOG
(
	IDENTIFIER bigint NOT NULL auto_increment,
	LOGIN_TIMESTAMP DATETIME,
	USER_LOGIN_ID VARCHAR(20),
	LOGIN_SOURCE_ID BIGINT,
	LOGIN_IP_ADDRESS VARCHAR(20),
	IS_LOGIN_SUCCESSFUL VARCHAR(10),
	PRIMARY KEY (`IDENTIFIER`)
) ;

INSERT INTO CATISSUE_DATA_AUDIT_EVENT_LOG(IDENTIFIER,OBJECT_IDENTIFIER,OBJECT_NAME)
SELECT IDENTIFIER,OBJECT_IDENTIFIER,OBJECT_NAME FROM CATISSUE_AUDIT_EVENT_LOG ;

ALTER TABLE CATISSUE_DATA_AUDIT_EVENT_LOG ADD INDEX FK5C07745DC62F96A411 (IDENTIFIER),
ADD CONSTRAINT FK5C07745DC62F96A411 FOREIGN KEY (IDENTIFIER)
REFERENCES CATISSUE_AUDIT_EVENT_LOG (IDENTIFIER);

ALTER TABLE CATISSUE_DATA_AUDIT_EVENT_LOG ADD INDEX FK5C07745DC62F96A412 (PARENT_LOG_ID),
ADD CONSTRAINT FK5C07745DC62F96A412 FOREIGN KEY (PARENT_LOG_ID)
REFERENCES CATISSUE_DATA_AUDIT_EVENT_LOG (IDENTIFIER);

ALTER TABLE CATISSUE_AUDIT_EVENT_LOG DROP COLUMN OBJECT_IDENTIFIER ;
ALTER TABLE CATISSUE_AUDIT_EVENT_LOG DROP COLUMN OBJECT_NAME ;
ALTER TABLE CATISSUE_AUDIT_EVENT_LOG DROP COLUMN EVENT_TYPE ;

ALTER TABLE catissue_audit_event_details, change CURRENT_VALUE CURRENT_VALUE varchar(4000) default NULL;
ALTER TABLE catissue_audit_event_details, change PREVIOUS_VALUE PREVIOUS_VALUE varchar(4000) default NULL;
