alter table CATISSUE_SPECIMEN_PROTOCOL add column GENERATE_LABEL tinyint(1) default 1;
alter table CATISSUE_SPECIMEN_PROTOCOL add column LABEL_FORMAT varchar(255) default null;