#--  Name: Sachin Lale Bug ID: 3052 Patch ID: 3052_4 See also: 1-4 ---
#--  Description : A comment field at the Specimen Collection Group level.---
alter table CATISSUE_SPECIMEN_COLL_GROUP add column COMMENTS varchar2(2000);

#--  Name: Shital Lawhale Bug ID: 3835 
#--  Description : A createdOn date field at the Specimen level.---
alter table CATISSUE_SPECIMEN add  CREATED_ON_DATE date;

#-- Ashish Gupta Bug id 2741---Added Association between SCG and Events
alter table CATISSUE_SPECIMEN_EVENT_PARAM  add column SPECIMEN_COLL_GRP_ID number(19,0);
alter table CATISSUE_SPECIMEN_EVENT_PARAM  add constraint FK753F33AD8CA560D1 foreign key (SPECIMEN_COLL_GRP_ID) references CATISSUE_SPECIMEN_COLL_GROUP;