create table DYEXTN_ABSTR_CONTAIN_CTR (
   IDENTIFIER bigint not null,
   CONTAINER_ID bigint,
   primary key (IDENTIFIER)
);

create table DYEXTN_ABSTRACT_ENTITY (
   id bigint not null,
   primary key (id)
);

create table DYEXTN_BASE_ABSTRACT_ATTRIBUTE (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);


create table DYEXTN_CAT_ASSO_CTL (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);


create table DYEXTN_CATEGORY (
   IDENTIFIER bigint not null,
   ROOT_CATEGORY_ELEMENT bigint,
   CATEGORY_ENTITY_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY_ASSOCIATION (
   IDENTIFIER bigint not null,
   CATEGORY_ENTIY_ID bigint,
   CATEGORY_ENTITY_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY_ATTRIBUTE (
   IDENTIFIER bigint not null,
   ATTRIBUTE_ID bigint,
   CATEGORY_ENTIY_ID bigint,
   CATEGORY_ENTITY_ID bigint,
   IS_VISIBLE bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_CATEGORY_ENTITY (
   IDENTIFIER bigint not null,
   NUMBER_OF_ENTRIES integer,
   ENTITY_ID bigint,
   OWN_PARENT_CATEGORY_ENTITY_ID bigint,
   TREE_PARENT_CATEGORY_ENTITY_ID bigint,
   CATEGORY_ASSOCIATION_ID bigint,
   PARENT_CATEGORY_ENTITY_ID bigint,
   REL_ATTR_CAT_ENTITY_ID bigint,
   IS_CREATETABLE tinyint(1),
   primary key (IDENTIFIER)
);


create table DYEXTN_ID_GENERATOR (
   ID bigint not null,
   NEXT_AVAILABLE_ID bigint,
   primary key (ID)
);


create table DYEXTN_PATH (
   IDENTIFIER bigint not null auto_increment,
   CATEGORY_ENTITY_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_PATH_ASSO_REL (
   IDENTIFIER bigint not null auto_increment,
   PATH_ID bigint,
   ASSOCIATION_ID bigint,
   PATH_SEQUENCE_NUMBER integer,
   SRC_INSTANCE_ID integer,
   TGT_INSTANCE_ID integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_SQL_AUDIT (
   IDENTIFIER bigint not null auto_increment,
   AUDIT_DATE date,
   QUERY_EXECUTED varchar(4000),
   USER_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_USERDEF_DE_VALUE_REL (
   USER_DEF_DE_ID bigint not null,
   PERMISSIBLE_VALUE_ID bigint not null,
   primary key (USER_DEF_DE_ID, PERMISSIBLE_VALUE_ID)
);