
/*Upgrade caTissue : Container column name changed to CONT_FULL */

ALTER TABLE CATISSUE_CONTAINER ADD (CONT_FULL number(1,0));
UPDATE CATISSUE_CONTAINER SET CONT_FULL = FULL;
ALTER TABLE CATISSUE_CONTAINER DROP COLUMN FULL;

UPDATE CATISSUE_INTERFACE_COLUMN_DATA set COLUMN_NAME = 'CONT_FULL' where IDENTIFIER = 242;


/*Query audit changes.*/

alter table catissue_audit_event_query_log ADD QUERY_ID number(20) default NULL; 
alter table catissue_audit_event_query_log add TEMP_TABLE_NAME varchar(150) default null;
alter table catissue_audit_event_query_log add IF_TEMP_TABLE_DELETED number(1) default 0;
alter table catissue_audit_event_query_log add ROOT_ENTITY_NAME varchar(150) default null;
alter table catissue_audit_event_query_log add COUNT_OF_ROOT_RECORDS number(20) default null;


/*MetaPhone data for participant*/

ALTER TABLE CATISSUE_PARTICIPANT ADD LNAME_METAPHONE varchar2(50);

/*EMPI Alter table changes.(Shital)*/

ALTER TABLE catissue_participant ADD EMPI_ID varchar2(50);
