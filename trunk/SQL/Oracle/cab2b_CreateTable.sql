drop table PATH;
drop table INTER_MODEL_ASSOCIATION;
drop table INTRA_MODEL_ASSOCIATION;
drop table ASSOCIATION;
drop table ID_TABLE;

drop sequence CATEGORIAL_CLASS_SEQ;
drop sequence CATEGORIAL_ATTRIBUTE_SEQ;
drop sequence CATEGORY_SEQ;


/*INTERMEDIATE_PATH contains  ASSOCIATION(ASSOCIATION_ID) connected by underscore */

create table PATH(

     PATH_ID          number(38,0) not null,

     FIRST_ENTITY_ID  number(38,0) null,

     INTERMEDIATE_PATH varchar2(1000)  null,

     LAST_ENTITY_ID   number(38,0)        null,

     primary key (PATH_ID)

);

/* Possible values for ASSOCIATION_TYPE are 1 and 2

ASSOCIATION_TYPE = 1 represents INTER_MODEL_ASSOCIATION.

ASSOCIATION_TYPE = 2 represents INTRA_MODEL_ASSOCIATION.

*/     

create table ASSOCIATION(

    ASSOCIATION_ID     number(38,0)    not null,

    ASSOCIATION_TYPE  number(8,0)    not null ,

    primary key (ASSOCIATION_ID)

);

create table INTER_MODEL_ASSOCIATION(

    ASSOCIATION_ID      number(38,0)  not null,

    LEFT_ENTITY_ID      number(38,0)  not null,

    LEFT_ATTRIBUTE_ID   number(38,0)  not null,

    RIGHT_ENTITY_ID     number(38,0)  not null,

    RIGHT_ATTRIBUTE_ID  number(38,0)  not null,

    primary key (ASSOCIATION_ID),

    foreign key (ASSOCIATION_ID) references ASSOCIATION(ASSOCIATION_ID)

);

create table INTRA_MODEL_ASSOCIATION(

    ASSOCIATION_ID    number(38,0)    not null,

    DE_ASSOCIATION_ID number(38,0)    not null,

    primary key (ASSOCIATION_ID),

    foreign key (ASSOCIATION_ID) references ASSOCIATION(ASSOCIATION_ID)

);

 
create table ID_TABLE(

    NEXT_ASSOCIATION_ID    number(38,0)    not null,

    primary key (NEXT_ASSOCIATION_ID)

);



alter table CATEGORIAL_CLASS drop constraint FK9651EF32D8D56A33;
alter table CATEGORIAL_ATTRIBUTE drop constraint FK31F77B56E8CBD948;
alter table CATEGORY drop constraint FK31A8ACFE2D0F63E7;
alter table CATEGORY drop constraint FK31A8ACFE211D9A6B;

 
drop table  CATEGORIAL_CLASS;
drop table CATEGORIAL_ATTRIBUTE;
drop table  CATEGORY;


create table CATEGORIAL_CLASS (

   ID number(38,0) not null ,

   DE_ENTITY_ID number(38,0),

   PATH_FROM_PARENT_ID number(38,0),

   PARENT_CATEGORIAL_CLASS_ID number(38,0),

   primary key (ID)

);

 
create table CATEGORIAL_ATTRIBUTE (

   ID number(38,0) not null,

   CATEGORIAL_CLASS_ID number(38,0) not null,

   DE_CATEGORY_ATTRIBUTE_ID number(38,0) not null,

   DE_SOURCE_CLASS_ATTRIBUTE_ID number(38,0) not null,

   primary key (ID)

);

 
create table CATEGORY (

   ID number(38,0) not null ,

   DE_ENTITY_ID number(38,0) not null,

   PARENT_CATEGORY_ID number(38,0),

   ROOT_CATEGORIAL_CLASS_ID number(38,0) unique not null,

   primary key (ID)

); 
