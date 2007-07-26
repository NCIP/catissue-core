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

#-- Aarti Assigning technician role to technicians on public data group
INSERT INTO `CSM_USER_GROUP_ROLE_PG` (`GROUP_ID`,`ROLE_ID`,`PROTECTION_GROUP_ID`,`UPDATE_DATE`) VALUES (3,3,20,'2005-08-24');

/*Patch ID: SimpleSearchEdit_16*/
/*Adding Metadata table required for the Simple search edit feature.*/
/*This table contains information about the columns to be hyperlinked for the given table.*/
drop table if exists CATISSUE_QUERY_EDITLINK_COLS;
CREATE TABLE CATISSUE_QUERY_EDITLINK_COLS
(
      TABLE_ID bigint not null, 
      COL_ID bigint not null
);

/* Bug id :4278 Deepti technicians can not access cp based view*/
update CSM_PG_PE
set PROTECTION_GROUP_ID = '18'
where PROTECTION_ELEMENT_ID = '292';

/*associate new events to SCG start*/

create table CATISSUE_SCG_EVENT_PARAM (
   SPECIMEN_EVENT_IDENTIFIER bigint,
   SCG_EVENT_IDENTIFIER bigint,
   SPECIMEN_COLL_GRP_ID bigint,
   EVENT_TIMESTAMP datetime,
   USER_ID bigint,
   COMMENTS text,
   COLLECTION_PROCEDURE varchar(50),
   CONTAINER varchar(50),
   RECEIVED_QUALITY varchar(255),
   TYPE text,
   primary key(SPECIMEN_EVENT_IDENTIFIER)
);

drop table if exists CATISSUE_SPECIMEN_LABEL_COUNT;

create table CATISSUE_SPECIMEN_LABEL_COUNT (
   LABEL_COUNT bigint not null,
   primary key (LABEL_COUNT)
);
INSERT INTO CATISSUE_SPECIMEN_LABEL_COUNT (LABEL_COUNT) VALUES ('0');

insert into CATISSUE_SCG_EVENT_PARAM (SPECIMEN_EVENT_IDENTIFIER,SPECIMEN_COLL_GRP_ID,USER_ID,EVENT_TIMESTAMP) select b.identifier, specimen_collection_group_id, user_id, min(event_timestamp) from catissue_specimen a join catissue_specimen_event_param b on (a.identifier = specimen_id) join catissue_coll_event_param c on (b.identifier = c.identifier) group by specimen_collection_group_id;
update CATISSUE_SCG_EVENT_PARAM set type = 'COLL' where type is null;
insert into catissue_specimen_event_param(specimen_coll_grp_id,user_id,event_timestamp) select specimen_collection_group_id, user_id, min(event_timestamp) from catissue_specimen a join catissue_specimen_event_param b on (a.identifier = specimen_id) join catissue_coll_event_param c on (b.identifier = c.identifier) group by specimen_collection_group_id;
update CATISSUE_SCG_EVENT_PARAM a,catissue_specimen_event_param b set a.SCG_EVENT_IDENTIFIER = b.IDENTIFIER where b.SPECIMEN_COLL_GRP_ID=a.SPECIMEN_COLL_GRP_ID and a.type='COLL';
update CATISSUE_SCG_EVENT_PARAM a,catissue_coll_event_param b set a.COLLECTION_PROCEDURE = b.COLLECTION_PROCEDURE where a.SPECIMEN_EVENT_IDENTIFIER=b.identifier and a.type='COLL';
update CATISSUE_SCG_EVENT_PARAM a,catissue_coll_event_param b set a.CONTAINER = b.CONTAINER where a.SPECIMEN_EVENT_IDENTIFIER=b.identifier and a.type='COLL';
insert into catissue_coll_event_param (IDENTIFIER,COLLECTION_PROCEDURE,CONTAINER) select SCG_EVENT_IDENTIFIER,COLLECTION_PROCEDURE,CONTAINER from CATISSUE_SCG_EVENT_PARAM  where type='COLL';

insert into CATISSUE_SCG_EVENT_PARAM (SPECIMEN_EVENT_IDENTIFIER,SPECIMEN_COLL_GRP_ID,USER_ID,EVENT_TIMESTAMP) select b.identifier, specimen_collection_group_id, user_id, min(event_timestamp) from catissue_specimen a join catissue_specimen_event_param b on (a.identifier = specimen_id) join catissue_received_event_param c on (b.identifier = c.identifier) group by specimen_collection_group_id;
update CATISSUE_SCG_EVENT_PARAM set type = 'REC' where type is null;
insert into catissue_specimen_event_param(specimen_coll_grp_id,user_id,event_timestamp) select specimen_collection_group_id, user_id, min(event_timestamp) from catissue_specimen a join catissue_specimen_event_param b on (a.identifier = specimen_id) join catissue_received_event_param c on (b.identifier = c.identifier) group by specimen_collection_group_id;
update CATISSUE_SCG_EVENT_PARAM a,catissue_specimen_event_param b set a.SCG_EVENT_IDENTIFIER =b.IDENTIFIER where b.SPECIMEN_COLL_GRP_ID=a.SPECIMEN_COLL_GRP_ID and b.identifier not in (select identifier from catissue_coll_event_param) and a.type='REC';
update CATISSUE_SCG_EVENT_PARAM a,catissue_received_event_param b set a.RECEIVED_QUALITY= b.RECEIVED_QUALITY where a.SPECIMEN_EVENT_IDENTIFIER=b.identifier and a.type='REC';
insert into catissue_received_event_param (IDENTIFIER,RECEIVED_QUALITY) select SCG_EVENT_IDENTIFIER,RECEIVED_QUALITY from CATISSUE_SCG_EVENT_PARAM  where type='REC';
drop table CATISSUE_SCG_EVENT_PARAM;
/*associate new events to SCG start end*/

