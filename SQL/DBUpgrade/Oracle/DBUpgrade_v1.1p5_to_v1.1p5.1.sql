alter table CATISSUE_SPECIMEN_PROTOCOL add (GENERATE_LABEL number(1,0) default 0);
alter table CATISSUE_SPECIMEN_PROTOCOL add (LABEL_FORMAT varchar(255) default null);
alter table CATISSUE_CP_REQ_SPECIMEN add (GENERATE_LABEL number(1,0) default 0);