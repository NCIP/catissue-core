create table CATISSUE_STUDY_FORM_CONTEXT (
   IDENTIFIER bigint not null auto_increment,
   NO_OF_ENTRIES bigint,
   primary key (IDENTIFIER)
);

create table  CATISSUE_CP_STUDYFORMCONTEXT (
   STUDY_FORM_CONTEXT_ID bigint not null,
   COLLECTION_PROTOCOL_ID bigint not null,
   primary key (STUDY_FORM_CONTEXT_ID,COLLECTION_PROTOCOL_ID)
);

create table CATISSUE_PARTICIPANT_REC_NTRY (
   IDENTIFIER bigint not null auto_increment,
   PARTICIPANT_ID bigint,
   primary key (IDENTIFIER)
);
alter table CATISSUE_PARTICIPANT_REC_NTRY add constraint FKB025CF444792AD22 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT (IDENTIFIER);

create table CATISSUE_SPECIMEN_REC_NTRY (
   IDENTIFIER bigint not null auto_increment,
   SPECIMEN_ID bigint,
   primary key (IDENTIFIER)
);
alter table CATISSUE_SPECIMEN_REC_NTRY add constraint FKB025CF555792AD22 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);

create table CATISSUE_SCG_REC_NTRY (
   IDENTIFIER bigint not null auto_increment,
   SPECIMEN_COLLECTION_GROUP_ID bigint,
   primary key (IDENTIFIER)
);

alter table CATISSUE_SCG_REC_NTRY add constraint FKB025CF666792AD22 foreign key (SPECIMEN_COLLECTION_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);

