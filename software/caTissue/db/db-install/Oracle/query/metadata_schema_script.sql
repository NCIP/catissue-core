create table CATEGORIAL_CLASS (
   ID number(19,0) not null,
   DE_ENTITY_ID number(19,0),
   PATH_FROM_PARENT_ID number(19,0),
   PARENT_CATEGORIAL_CLASS_ID number(19,0),
   primary key (ID)
);
create table CATEGORIAL_ATTRIBUTE (
   ID number(19,0) not null,
   CATEGORIAL_CLASS_ID number(19,0),
   DE_CATEGORY_ATTRIBUTE_ID number(19,0),
   DE_SOURCE_CLASS_ATTRIBUTE_ID number(19,0),
   primary key (ID)
);
create table CATEGORY (
   ID number(19,0) not null,
   DE_ENTITY_ID number(19,0),
   PARENT_CATEGORY_ID number(19,0),
   ROOT_CATEGORIAL_CLASS_ID number(19,0) unique,
   primary key (ID)
);
alter table CATEGORIAL_CLASS add constraint FK9651EF32D8D56A33 foreign key (PARENT_CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS;
alter table CATEGORIAL_ATTRIBUTE add constraint FK31F77B56E8CBD948 foreign key (CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS;
alter table CATEGORY add constraint FK31A8ACFE2D0F63E7 foreign key (PARENT_CATEGORY_ID) references CATEGORY;
alter table CATEGORY add constraint FK31A8ACFE211D9A6B foreign key (ROOT_CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS;

create sequence CATEGORIAL_CLASS_SEQ;
create sequence CATEGORY_SEQ;
create sequence CATEGORIAL_ATTRIBUTE_SEQ;