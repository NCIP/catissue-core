drop table PATH;
drop table INTER_MODEL_ASSOCIATION;
drop table INTRA_MODEL_ASSOCIATION;
drop table ASSOCIATION;
drop table ID_TABLE;

/*INTERMEDIATE_PATH contains  ASSOCIATION(ASSOCIATION_ID) connected by underscore */
create table PATH(
     PATH_ID           bigint         not null auto_increment,
     FIRST_ENTITY_ID   bigint         null,
     INTERMEDIATE_PATH varchar(1000)  null,
     LAST_ENTITY_ID    bigint         null,
     primary key (PATH_ID)
);
/* Possible values for ASSOCIATION_TYPE are 1 and 2
ASSOCIATION_TYPE = 1 represents INTER_MODEL_ASSOCIATION.
ASSOCIATION_TYPE = 2 represents INTRA_MODEL_ASSOCIATION.
*/     
create table ASSOCIATION(
    ASSOCIATION_ID    bigint    not null,
    ASSOCIATION_TYPE  INT(8)    not null ,
    primary key (ASSOCIATION_ID)
);

create table INTER_MODEL_ASSOCIATION(
    ASSOCIATION_ID      bigint  not null,
    LEFT_ENTITY_ID      bigint  not null,
    LEFT_ATTRIBUTE_ID   bigint  not null,
    RIGHT_ENTITY_ID     bigint  not null,
    RIGHT_ATTRIBUTE_ID  bigint  not null,
    primary key (ASSOCIATION_ID),
    foreign key (ASSOCIATION_ID) references ASSOCIATION(ASSOCIATION_ID)
);
create table INTRA_MODEL_ASSOCIATION(
    ASSOCIATION_ID    bigint    not null,
    DE_ASSOCIATION_ID bigint    not null,
    primary key (ASSOCIATION_ID),
    foreign key (ASSOCIATION_ID) references ASSOCIATION(ASSOCIATION_ID)
);
create table ID_TABLE(
    NEXT_ASSOCIATION_ID    bigint    not null,
    primary key (NEXT_ASSOCIATION_ID)
);
create index INDEX1 ON PATH (FIRST_ENTITY_ID,LAST_ENTITY_ID);
insert into ID_TABLE(NEXT_ASSOCIATION_ID) value(1);
alter table CATEGORIAL_CLASS drop foreign key FK9651EF32D8D56A33;
alter table CATEGORIAL_ATTRIBUTE drop foreign key FK31F77B56E8CBD948;
alter table CATEGORY drop foreign key FK31A8ACFE2D0F63E7;
alter table CATEGORY drop foreign key FK31A8ACFE211D9A6B;

drop table if exists CATEGORIAL_CLASS;
drop table if exists CATEGORIAL_ATTRIBUTE;
drop table if exists CATEGORY;

create table CATEGORIAL_CLASS (
   ID bigint not null auto_increment,
   DE_ENTITY_ID bigint,
   PATH_FROM_PARENT_ID bigint,
   PARENT_CATEGORIAL_CLASS_ID bigint,
   primary key (ID)
);

create table CATEGORIAL_ATTRIBUTE (
   ID bigint not null auto_increment,
   CATEGORIAL_CLASS_ID bigint not null,
   DE_CATEGORY_ATTRIBUTE_ID bigint not null,
   DE_SOURCE_CLASS_ATTRIBUTE_ID bigint not null,
   primary key (ID)
);

create table CATEGORY (
   ID bigint not null auto_increment,
   DE_ENTITY_ID bigint not null,
   PARENT_CATEGORY_ID bigint,
   ROOT_CATEGORIAL_CLASS_ID bigint unique not null,
   primary key (ID)
);

alter table CATEGORIAL_CLASS add index FK9651EF32D8D56A33 (PARENT_CATEGORIAL_CLASS_ID), add constraint FK9651EF32D8D56A33 foreign key (PARENT_CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS (ID);
alter table CATEGORIAL_ATTRIBUTE add index FK31F77B56E8CBD948 (CATEGORIAL_CLASS_ID), add constraint FK31F77B56E8CBD948 foreign key (CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS (ID);
alter table CATEGORY add index FK31A8ACFE2D0F63E7 (PARENT_CATEGORY_ID), add constraint FK31A8ACFE2D0F63E7 foreign key (PARENT_CATEGORY_ID) references CATEGORY (ID);
alter table CATEGORY add index FK31A8ACFE211D9A6B (ROOT_CATEGORIAL_CLASS_ID), add constraint FK31A8ACFE211D9A6B foreign key (ROOT_CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS (ID);
