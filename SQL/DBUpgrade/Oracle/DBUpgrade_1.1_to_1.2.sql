#--  Name: Sachin Lale Bug ID: 3052 Patch ID: 3052_4 See also: 1-4 ---
#--  Description : A comment field at the Specimen Collection Group level.---
alter table CATISSUE_SPECIMEN_COLL_GROUP add column COMMENTS varchar2(2000);

#--  Name: Shital Lawhale Bug ID: 3835 
#--  Description : A createdOn date field at the Specimen level.---
alter table CATISSUE_SPECIMEN add  CREATED_ON_DATE date;
