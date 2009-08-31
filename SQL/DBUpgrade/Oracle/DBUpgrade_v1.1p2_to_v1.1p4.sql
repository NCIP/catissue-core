
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

-- This query is specific to p2 production dump and for NACC neuropathology form.
-- On other P2/rc4 dump, this query mey get fail. 
update DYEXTN_CATEGORY_ENTITY set REL_ATTR_CAT_ENTITY_ID = 3651 where identifier = 3699;
-- Inserting a PUBLIC group for supporting Sharing queries to ALL
INSERT INTO CSM_PROTECTION_GROUP (PROTECTION_GROUP_ID,PROTECTION_GROUP_NAME,PROTECTION_GROUP_DESCRIPTION,APPLICATION_ID,LARGE_ELEMENT_COUNT_FLAG,UPDATE_DATE,PARENT_PROTECTION_GROUP_ID) VALUES (CSM_PROTECTIO_PROTECTION_G_SEQ.NEXTVAL,' PUBLIC_QUERY_PROTECTION_GROUP',NULL,1,0,to_date('2009-08-06','yyyy-mm-dd'),NULL);
