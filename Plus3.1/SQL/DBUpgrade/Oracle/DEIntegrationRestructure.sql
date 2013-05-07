create table CATISSUE_STUDY_FORM_CONTEXT (
   IDENTIFIER number(19,0) not null,
   NO_OF_ENTRIES integer,
   primary key (IDENTIFIER)
);

create table  CATISSUE_CP_STUDYFORMCONTEXT (
   STUDY_FORM_CONTEXT_ID number(19,0) not null,
   COLLECTION_PROTOCOL_ID number(19,0) not null,
   primary key (STUDY_FORM_CONTEXT_ID,COLLECTION_PROTOCOL_ID)
);

create table CATISSUE_PARTICIPANT_REC_NTRY (
   IDENTIFIER number(19,0) not null,
   PARTICIPANT_ID number(19,0) not null,
   primary key (IDENTIFIER)
);
alter table CATISSUE_PARTICIPANT_REC_NTRY add constraint FKB025CF444792AD22 foreign key (PARTICIPANT_ID) references CATISSUE_PARTICIPANT (IDENTIFIER);

create table CATISSUE_SPECIMEN_REC_NTRY (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_ID number(19,0) not null,
   primary key (IDENTIFIER)
);
alter table CATISSUE_SPECIMEN_REC_NTRY add constraint FKB025CF555792AD22 foreign key (SPECIMEN_ID) references CATISSUE_SPECIMEN (IDENTIFIER);

create table CATISSUE_SCG_REC_NTRY (
   IDENTIFIER number(19,0) not null,
   SPECIMEN_COLLECTION_GROUP_ID number(19,0) not null,
   primary key (IDENTIFIER)
);

alter table CATISSUE_SCG_REC_NTRY add constraint FKB025CF666792AD22 foreign key (SPECIMEN_COLLECTION_GROUP_ID) references CATISSUE_SPECIMEN_COLL_GROUP (IDENTIFIER);

