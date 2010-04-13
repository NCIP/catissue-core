alter table CATISSUE_SPECIMEN_PROTOCOL add (GENERATE_LABEL number(1,0) default 1);
alter table CATISSUE_SPECIMEN_PROTOCOL add (LABEL_FORMAT varchar(255) default null);