alter table CATISSUE_PERMISSIBLE_VALUE drop constraint FK57DDCE153B5435E;
alter table CATISSUE_PERMISSIBLE_VALUE drop constraint FK57DDCE1FC56C2B1;
drop sequence CATISSUE_PERMISSIBLE_VALUE_SEQ;

drop table CATISSUE_PERMISSIBLE_VALUE cascade constraints;
drop table CATISSUE_CDE cascade constraints;

create table CATISSUE_PERMISSIBLE_VALUE (
   IDENTIFIER number(19,0) not null,
   CONCEPT_CODE varchar2(40),
   DEFINITION varchar2(500),
   PARENT_IDENTIFIER number(19,0),
   VALUE varchar2(100),
   PUBLIC_ID varchar2(30),
   primary key (IDENTIFIER)
);

create table CATISSUE_CDE (
   PUBLIC_ID varchar2(30) not null,
   LONG_NAME varchar2(200),
   DEFINITION varchar2(500),
   VERSION varchar2(50),
   LAST_UPDATED date,
   primary key (PUBLIC_ID)
);

alter table CATISSUE_PERMISSIBLE_VALUE add constraint FK57DDCE153B5435E foreign key (PARENT_IDENTIFIER) references CATISSUE_PERMISSIBLE_VALUE;
alter table CATISSUE_PERMISSIBLE_VALUE add constraint FK57DDCE1FC56C2B1 foreign key (PUBLIC_ID) references CATISSUE_CDE;
create sequence CATISSUE_PERMISSIBLE_VALUE_SEQ;