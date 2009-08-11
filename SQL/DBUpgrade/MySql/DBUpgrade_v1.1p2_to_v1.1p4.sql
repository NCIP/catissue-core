
/*Upgrade caTissue : Container column name changed to CONT_FULL */

ALTER TABLE CATISSUE_CONTAINER ADD (CONT_FULL bit);
UPDATE CATISSUE_CONTAINER SET CONT_FULL = FULL;
ALTER TABLE CATISSUE_CONTAINER DROP COLUMN FULL;

UPDATE CATISSUE_INTERFACE_COLUMN_DATA set COLUMN_NAME = 'CONT_FULL' where IDENTIFIER = 242;

/*Query audit changes.*/

alter table catissue_audit_event_query_log add column QUERY_ID bigint(20) default null; 
alter table catissue_audit_event_query_log add column TEMP_TABLE_NAME varchar(150) default null;
alter table catissue_audit_event_query_log add column IF_TEMP_TABLE_DELETED tinyint(1) default false;
alter table catissue_audit_event_query_log add column ROOT_ENTITY_NAME varchar(150) default null;
alter table catissue_audit_event_query_log add column COUNT_OF_ROOT_RECORDS bigint(20) default null;

/*MetaPhone data for participant*/

ALTER TABLE CATISSUE_PARTICIPANT ADD LNAME_METAPHONE varchar(50);


/*EMPI Alter table changes.(Shital)*/

ALTER TABLE catissue_participant ADD EMPI_ID varchar(50);

