--SET storage_engine=InnoDB;
create table DYEXTN_ABSTR_CONTAIN_CTR (
   IDENTIFIER bigint not null,
   CONTAINER_ID numeric(20),
   primary key (IDENTIFIER)
);
create table DYEXTN_ABSTRACT_ENTITY (
   id numeric(20) not null,
   primary key (id)
);
create table DYEXTN_BASE_ABSTRACT_ATTRIBUTE (
   IDENTIFIER numeric(20) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_CAT_ASSO_CTL (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY (
   IDENTIFIER numeric(20) not null,
   ROOT_CATEGORY_ELEMENT numeric(20),
   CATEGORY_ENTITY_ID numeric(20),
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY_ASSOCIATION (
   IDENTIFIER numeric(20) not null,
   CATEGORY_ENTIY_ID numeric(20),
   CATEGORY_ENTITY_ID numeric(20),
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY_ATTRIBUTE (
   IDENTIFIER numeric(20) not null,
   ABSTRACT_ATTRIBUTE_ID numeric(20),
   CATEGORY_ENTIY_ID numeric(20),
   CATEGORY_ENTITY_ID numeric(20),
   IS_VISIBLE bigint,
   IS_RELATTRIBUTE bit,
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY_ENTITY (
   IDENTIFIER numeric(20) not null,
   NUMBER_OF_ENTRIES integer,
   ENTITY_ID numeric(20),
   OWN_PARENT_CATEGORY_ENTITY_ID numeric(20),
   TREE_PARENT_CATEGORY_ENTITY_ID numeric(20),
   CATEGORY_ASSOCIATION_ID numeric(20),
   PARENT_CATEGORY_ENTITY_ID numeric(20),
   REL_ATTR_CAT_ENTITY_ID numeric(20),
   IS_CREATETABLE bit,
   primary key (IDENTIFIER)
);
create table DYEXTN_ID_GENERATOR (
   ID bigint not null,
   NEXT_AVAILABLE_ID bigint,
   primary key (ID)
);
create table DYEXTN_PATH (
   IDENTIFIER bigint not null identity,
   CATEGORY_ENTITY_ID numeric(20),
   primary key (IDENTIFIER)
);
create table DYEXTN_PATH_ASSO_REL (
   IDENTIFIER bigint not null identity,
   PATH_ID bigint,
   ASSOCIATION_ID numeric(20),
   PATH_SEQUENCE_NUMBER integer,
   SRC_INSTANCE_ID integer,
   TGT_INSTANCE_ID integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_SQL_AUDIT (
   IDENTIFIER bigint not null identity,
   AUDIT_DATE datetime,
   QUERY_EXECUTED varchar(4000),
   USER_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_USERDEF_DE_VALUE_REL (
   USER_DEF_DE_ID bigint not null,
   PERMISSIBLE_VALUE_ID bigint not null,
   primary key (USER_DEF_DE_ID, PERMISSIBLE_VALUE_ID)
);
CREATE TABLE DYEXTN_FORM_CTRL_NOTES (
   IDENTIFIER bigint not null, 
   NOTE varchar(255), 
   FORM_CONTROL_ID bigint, 
   INSERTION_ORDER numeric(20), 
   primary key (IDENTIFIER)
);