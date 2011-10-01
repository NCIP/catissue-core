/* Name: Sachin Lale Bug ID: 3052 Patch ID: 3052_4 See also: 1-4*/
/*Description : A comment field at the Specimen Collection Group level.*/
alter table CATISSUE_SPECIMEN_COLL_GROUP add COMMENTS varchar2(2000);

/*  Name: Shital Lawhale Bug ID: 3835 */
/*  Description : A createdOn date field at the Specimen level.*/
alter table CATISSUE_SPECIMEN add CREATED_ON_DATE date;

/* Ashish Gupta Bug id 2741---Added Association between SCG and Events */
alter table CATISSUE_SPECIMEN_EVENT_PARAM  add SPECIMEN_COLL_GRP_ID number(19,0);
alter table CATISSUE_SPECIMEN_EVENT_PARAM  add constraint FK753F33AD8CA560D1 foreign key (SPECIMEN_COLL_GRP_ID) references CATISSUE_SPECIMEN_COLL_GROUP;
/* Deepti Shelar Bug -----In CP based view addFuture SCGs */
alter table CATISSUE_COLL_PROT_EVENT add COLLECTION_POINT_LABEL varchar(255);
alter table CATISSUE_COLL_PROT_EVENT add constraint CPID_LABEL_KEY unique (COLLECTION_PROTOCOL_ID,COLLECTION_POINT_LABEL);

/*  Name: Shital Lawhale Bug ID: 3549 */
/*  Description : A ATTRIBUTE ORDER field in CATISSUE_SEARCH_DISPLAY_DATA .*/
alter table CATISSUE_SEARCH_DISPLAY_DATA add ATTRIBUTE_ORDER number(5,0);

/*Patch ID: SimpleSearchEdit_17*/
/*Adding Metadata table required for the Simple search edit feature.*/
/*This table contains information about the columns to be hyperlinked for the given table.*/
drop table CATISSUE_QUERY_EDITLINK_COLS;
CREATE TABLE CATISSUE_QUERY_EDITLINK_COLS
(
      TABLE_ID number(30), 
      COL_ID number(30)
);

drop table if exists CATISSUE_SPECIMEN_LABEL_COUNT;
create table CATISSUE_SPECIMEN_LABEL_COUNT (
   LABEL_COUNT number(19,0) not null,
   primary key (LABEL_COUNT)
);
INSERT INTO CATISSUE_SPECIMEN_LABEL_COUNT (LABEL_COUNT) VALUES ('0');

#-- Aarti Assigning technician role to technicians on public data group
INSERT INTO CSM_USER_GROUP_ROLE_PG (USER_GROUP_ROLE_PG_ID,GROUP_ID,ROLE_ID,PROTECTION_GROUP_ID,UPDATE_DATE) VALUES (CSM_USER_GROU_USER_GROUP_R_SEQ.NEXTVAL,3,3,20,TO_DATE('2005-08-24','yyyy-mm-dd'));

/* Bug id :4278 Deepti technicians can not access cp based view*/
update CSM_PG_PE
set PROTECTION_GROUP_ID = '18'
where PROTECTION_ELEMENT_ID = '292';
 
/*associate new events to SCG start*/
insert all into catissue_specimen_event_param (identifier, specimen_coll_grp_id, event_timestamp, user_id )
values (catissue_spec_event_param_seq.nextval, specimen_collection_group_id,
        least_timestamp, user_id
        )
into catissue_coll_event_param (
    identifier, collection_procedure, container
    )
values (catissue_spec_event_param_seq.currval, collection_procedure, container
    )
select specimen_collection_group_id, least_timestamp, user_id, collection_procedure, container from ( select  specimen_collection_group_id , user_id, event_timestamp,
        collection_procedure, container,
        min(event_timestamp) over(partition by specimen_collection_group_id order by specimen_id) least_timestamp,
        row_number() over(partition by specimen_collection_group_id order by
specimen_id) rn
from    catissue_specimen join
        (select     specimen_id, event_timestamp, user_id, 
collection_procedure, container
         from       catissue_specimen_event_param
                    join catissue_coll_event_param  using(identifier)
        )
        on (identifier=specimen_id and parent_specimen_id is null)
)
where rn = 1;

insert all
into catissue_specimen_event_param (
    identifier, specimen_coll_grp_id, event_timestamp, user_id
)
values (catissue_spec_event_param_seq.nextval, specimen_collection_group_id,
        least_timestamp, user_id
        )
into catissue_received_event_param (
    identifier, received_quality
    )
values (catissue_spec_event_param_seq.currval, received_quality
    )
select specimen_collection_group_id, least_timestamp, user_id, received_quality from ( select  specimen_collection_group_id , user_id, event_timestamp, received_quality,
        min(event_timestamp) over(partition by specimen_collection_group_id order by specimen_id) least_timestamp,
        row_number() over(partition by specimen_collection_group_id order by
specimen_id) rn
from    catissue_specimen join
        (select     specimen_id, event_timestamp, user_id, received_quality
         from       catissue_specimen_event_param
                    join catissue_received_event_param  using(identifier)
        )
        on (identifier=specimen_id and parent_specimen_id is null)
)
where rn = 1;
/*associate new events to SCG start end*/