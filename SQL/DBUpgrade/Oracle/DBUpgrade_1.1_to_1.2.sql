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