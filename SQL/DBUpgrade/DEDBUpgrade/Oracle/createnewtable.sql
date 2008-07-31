create table DYEXTN_ABSTR_CONTAIN_CTR (
   IDENTIFIER number(19,0) not null,
   CONTAINER_ID number(19,0),
   primary key (IDENTIFIER)
);


create table DYEXTN_ABSTRACT_ENTITY (
   id number(19,0) not null,
   primary key (id)
);


create table DYEXTN_BASE_ABSTRACT_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);

create table DYEXTN_CAT_ASSO_CTL (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY (
   IDENTIFIER number(19,0) not null,
   ROOT_CATEGORY_ELEMENT number(19,0),
   CATEGORY_ENTITY_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY_ASSOCIATION (
   IDENTIFIER number(19,0) not null,
   CATEGORY_ENTIY_ID number(19,0),
   CATEGORY_ENTITY_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   ATTRIBUTE_ID number(19,0),
   CATEGORY_ENTIY_ID number(19,0),
   CATEGORY_ENTITY_ID number(19,0),
   IS_VISIBLE number(1,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY_ENTITY (
   IDENTIFIER number(19,0) not null,
   NUMBER_OF_ENTRIES number(10,0),
   ENTITY_ID number(19,0),
   OWN_PARENT_CATEGORY_ENTITY_ID number(19,0),
   TREE_PARENT_CATEGORY_ENTITY_ID number(19,0),
   CATEGORY_ASSOCIATION_ID number(19,0),
   PARENT_CATEGORY_ENTITY_ID number(19,0),
   REL_ATTR_CAT_ENTITY_ID number(19,0),
   IS_CREATETABLE NUMBER(1,0),	
   primary key (IDENTIFIER)
);
create table DYEXTN_ID_GENERATOR (
   ID number(19,0) not null,
   NEXT_AVAILABLE_ID number(19,0),
   primary key (ID)
);
create table DYEXTN_PATH (
   IDENTIFIER number(19,0) not null,
   CATEGORY_ENTITY_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_PATH_ASSO_REL (
   IDENTIFIER number(19,0) not null,
   PATH_ID number(19,0),
   ASSOCIATION_ID number(19,0),
   PATH_SEQUENCE_NUMBER number(10,0),
   SRC_INSTANCE_ID number(10,0),
   TGT_INSTANCE_ID number(10,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_SQL_AUDIT (
   IDENTIFIER number(19,0) not null,
   AUDIT_DATE date,
   QUERY_EXECUTED varchar2(4000),
   USER_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_USERDEF_DE_VALUE_REL (
   USER_DEF_DE_ID number(19,0) not null,
   PERMISSIBLE_VALUE_ID number(19,0) not null,
   primary key (USER_DEF_DE_ID, PERMISSIBLE_VALUE_ID)
);