/* Name: Sachin Lale Bug ID: 3052 Patch ID: 3052_4 See also: 1-4*/
/*Description : A comment field at the Specimen Collection Group level.*/
alter table CATISSUE_SPECIMEN_COLL_GROUP add column COMMENTS varchar2(2000);

/*  Name: Shital Lawhale Bug ID: 3835 */
/*  Description : A createdOn date field at the Specimen level.*/
alter table CATISSUE_SPECIMEN add column CREATED_ON_DATE date;

/* Ashish Gupta Bug id 2741---Added Association between SCG and Events */
alter table CATISSUE_SPECIMEN_EVENT_PARAM  add column SPECIMEN_COLL_GRP_ID number(19,0);
alter table CATISSUE_SPECIMEN_EVENT_PARAM  add constraint FK753F33AD8CA560D1 foreign key (SPECIMEN_COLL_GRP_ID) references CATISSUE_SPECIMEN_COLL_GROUP;
/* Deepti Shelar Bug -----In CP based view addFuture SCGs */
alter table CATISSUE_COLL_PROT_EVENT add column COLLECTION_POINT_LABEL varchar(255);
alter table CATISSUE_COLL_PROT_EVENT add constraint CPID_LABEL_KEY unique (COLLECTION_PROTOCOL_ID,COLLECTION_POINT_LABEL);

/*  Name: Shital Lawhale Bug ID: 3549 */
/*  Description : A ATTRIBUTE ORDER field in CATISSUE_SEARCH_DISPLAY_DATA .*/
alter table CATISSUE_SEARCH_DISPLAY_DATA add column ATTRIBUTE_ORDER number(5,0);

#-- Aarti Assigning technician role to technicians on public data group
INSERT INTO CSM_USER_GROUP_ROLE_PG (GROUP_ID,ROLE_ID,PROTECTION_GROUP_ID,UPDATE_DATE) VALUES (3,3,20,to_date('2005-08-24','yyyy-mm-dd'));