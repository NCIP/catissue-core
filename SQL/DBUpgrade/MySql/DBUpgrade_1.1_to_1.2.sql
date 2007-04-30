#--  Name: Sachin Lale Bug ID: 3052 Patch ID: 3052_3 See also: 1-4 ---
#--  Description : A comment field at the Specimen Collection Group level.---
alter table CATISSUE_SPECIMEN_COLL_GROUP add column COMMENTS text;

#--  Name: Shital Lawhale Bug ID: 3835 
#--  Description : A createdOn date field at Specimen level.---
alter table CATISSUE_SPECIMEN add column CREATED_ON_DATE date;

#-- Ashish Gupta Bug id 2741---Added Association between SCG and Events
alter table CATISSUE_SPECIMEN_EVENT_PARAM add column SPECIMEN_COLL_GRP_ID bigint;
alter table CATISSUE_SPECIMEN_EVENT_PARAM add index FK753F33AD8CA560D1 (SPECIMEN_COLL_GRP_ID), add constraint FK753F33AD8CA560D1 foreign key (SPECIMEN_COLL_GRP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);

#-- Deepti Shelar Bug -----In CP based view addFuture SCGs
alter table CATISSUE_COLL_PROT_EVENT add column COLLECTION_POINT_LABEL VARCHAR(255);
alter table CATISSUE_COLL_PROT_EVENT add constraint CPID_LABEL_KEY unique (COLLECTION_PROTOCOL_ID,COLLECTION_POINT_LABEL);

#--  Name: Shital Lawhale Bug ID: 3549 
#--  Description : A ATTRIBUTE ORDER field in CATISSUE_SEARCH_DISPLAY_DATA .---
alter table CATISSUE_SEARCH_DISPLAY_DATA add column ATTRIBUTE_ORDER int(5);

#-- Aarti Sharma : updating all dates with value '0000-00-00' to '2005-01-01'
update csm_group
set update_date = '2005-01-01'
where update_date = '0000-00-00';

update csm_protection_element
set update_date = '2005-01-01'
where update_date = '0000-00-00';

update CSM_PROTECTION_GROUP
set update_date = '2005-01-01'
where update_date = '0000-00-00';

update CSM_ROLE
set update_date = '2005-01-01'
where update_date = '0000-00-00';

update CSM_PG_PE
set update_date = '2005-01-01'
where update_date = '0000-00-00';

update CSM_ROLE_PRIVILEGE
set update_date = '2005-01-01'
where update_date = '0000-00-00';

update CSM_USER_GROUP_ROLE_PG
set update_date = '2005-01-01'
where update_date = '0000-00-00';

/*Patch ID: SimpleSearchEdit_16*/
/*Adding Metadata table required for the Simple search edit feature.*/
/*This table contains information about the columns to be hyperlinked for the given table.*/
drop table if exists CATISSUE_QUERY_EDITLINK_COLS;
CREATE TABLE CATISSUE_QUERY_EDITLINK_COLS
(
      TABLE_ID bigint not null, 
      COL_ID bigint not null
);