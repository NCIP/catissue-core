SET storage_engine=InnoDB;
SET FOREIGN_KEY_CHECKS=0;

drop table if exists QUERY_PARAMETERIZED_QUERY;
drop table if exists CATEGORIAL_CLASS;
drop table if exists QUERY_INTRA_MODEL_ASSOCIATION;
drop table if exists QUERY_CONSTRAINTS;
drop table if exists QUERY_PARAMETERIZED_CONDITION;
drop table if exists QUERY_QUERY_ENTITY;
drop table if exists QUERY_CONDITION;
drop table if exists QUERY_RULE;
drop table if exists QUERY;
drop table if exists QUERY_LOGICAL_CONNECTOR;
drop table if exists QUERY_EXPRESSION;
drop table if exists QUERY_MODEL_ASSOCIATION;
drop table if exists QUERY_CONDITION_VALUES;
drop table if exists QUERY_JOIN_GRAPH;
drop table if exists CATEGORIAL_ATTRIBUTE;
drop table if exists QUERY_OUTPUT_ATTRIBUTE;
drop table if exists QUERY_INTER_MODEL_ASSOCIATION;
drop table if exists QUERY_EXPRESSION_OPERAND;
drop table if exists QUERY_GRAPH_ENTRY;
drop table if exists CATEGORY;
drop table if exists QUERY_EXPRESSIONID;

SET FOREIGN_KEY_CHECKS=1;
create table QUERY_PARAMETERIZED_QUERY (
   IDENTIFIER bigint not null,
   QUERY_NAME varchar(255) unique,
   DESCRIPTION text,
   primary key (IDENTIFIER)
);
create table CATEGORIAL_CLASS (
   ID bigint not null auto_increment,
   DE_ENTITY_ID bigint,
   PATH_FROM_PARENT_ID bigint,
   PARENT_CATEGORIAL_CLASS_ID bigint,
   primary key (ID)
);
create table QUERY_INTRA_MODEL_ASSOCIATION (
   IDENTIFIER bigint not null,
   DE_ASSOCIATION_ID bigint not null,
   primary key (IDENTIFIER)
);
create table QUERY_CONSTRAINTS (
   IDENTIFIER bigint not null auto_increment,
   QUERY_JOIN_GRAPH_ID bigint unique,
   primary key (IDENTIFIER)
);
create table QUERY_PARAMETERIZED_CONDITION (
   IDENTIFIER bigint not null,
   CONDITION_INDEX integer,
   CONDITION_NAME varchar(255),
   primary key (IDENTIFIER)
);
create table QUERY_QUERY_ENTITY (
   IDENTIFIER bigint not null auto_increment,
   ENTITY_ID bigint not null,
   primary key (IDENTIFIER)
);
create table QUERY_CONDITION (
   IDENTIFIER bigint not null auto_increment,
   ATTRIBUTE_ID bigint not null,
   RELATIONAL_OPERATOR varchar(255),
   QUERY_RULE_ID bigint,
   POSITION integer,
   primary key (IDENTIFIER)
);
create table QUERY_RULE (
   IDENTIFIER bigint not null,
   QUERY_EXPRESSION_ID bigint not null,
   primary key (IDENTIFIER)
);
create table QUERY (
   IDENTIFIER bigint not null auto_increment,
   QUERY_CONSTRAINTS_ID bigint unique,
   primary key (IDENTIFIER)
);
create table QUERY_LOGICAL_CONNECTOR (
   IDENTIFIER bigint not null auto_increment,
   LOGICAL_OPERATOR varchar(255),
   NESTING_NUMBER integer,
   QUERY_EXPRESSION_ID bigint,
   POSITION integer,
   primary key (IDENTIFIER)
);
create table QUERY_EXPRESSION (
   IDENTIFIER bigint not null auto_increment,
   QUERY_EXPRESSIONID_ID bigint unique,
   QUERY_QUERY_ENTITY_ID bigint not null,
   IS_IN_VIEW bit,
   IS_VISIBLE bit,
   QUERY_CONSTRAINT_ID bigint,
   primary key (IDENTIFIER)
);
create table QUERY_MODEL_ASSOCIATION (
   IDENTIFIER bigint not null auto_increment,
   primary key (IDENTIFIER)
);
create table QUERY_CONDITION_VALUES (
   QUERY_CONDITION_ID bigint not null,
   VALUE_LIST varchar(255),
   POSITION integer not null,
   primary key (QUERY_CONDITION_ID, POSITION)
);
create table QUERY_JOIN_GRAPH (
   IDENTIFIER bigint not null auto_increment,
   primary key (IDENTIFIER)
);
create table CATEGORIAL_ATTRIBUTE (
   ID bigint not null auto_increment,
   CATEGORIAL_CLASS_ID bigint,
   DE_CATEGORY_ATTRIBUTE_ID bigint,
   DE_SOURCE_CLASS_ATTRIBUTE_ID bigint,
   primary key (ID)
);
create table QUERY_OUTPUT_ATTRIBUTE (
   IDENTIFIER bigint not null auto_increment,
   EXPRESSIONID_ID bigint,
   ATTRIBUTE_ID bigint not null,
   PARAMETERIZED_QUERY_ID bigint,
   POSITION integer,
   primary key (IDENTIFIER)
);
create table QUERY_INTER_MODEL_ASSOCIATION (
   IDENTIFIER bigint not null,
   SOURCE_SERVICE_URL text not null,
   TARGET_SERVICE_URL text not null,
   SOURCE_ATTRIBUTE_ID bigint not null,
   TARGET_ATTRIBUTE_ID bigint not null,
   primary key (IDENTIFIER)
);
create table QUERY_EXPRESSION_OPERAND (
   IDENTIFIER bigint not null auto_increment,
   QUERY_EXPRESSION_ID bigint,
   POSITION integer,
   primary key (IDENTIFIER)
);
create table QUERY_GRAPH_ENTRY (
   IDENTIFIER bigint not null auto_increment,
   QUERY_MODEL_ASSOCIATION_ID bigint,
   SOURCE_EXPRESSIONID_ID bigint,
   TARGET_EXPRESSIONID_ID bigint,
   QUERY_JOIN_GRAPH_ID bigint,
   primary key (IDENTIFIER)
);
create table CATEGORY (
   ID bigint not null auto_increment,
   DE_ENTITY_ID bigint,
   PARENT_CATEGORY_ID bigint,
   ROOT_CATEGORIAL_CLASS_ID bigint unique,
   primary key (ID)
);
create table QUERY_EXPRESSIONID (
   IDENTIFIER bigint not null,
   SUB_EXPRESSION_ID integer not null,
   primary key (IDENTIFIER)
);
alter table QUERY_PARAMETERIZED_QUERY add index FKA272176BBC7298A9 (IDENTIFIER), add constraint FKA272176BBC7298A9 foreign key (IDENTIFIER) references QUERY (IDENTIFIER);
alter table CATEGORIAL_CLASS add index FK9651EF32D8D56A33 (PARENT_CATEGORIAL_CLASS_ID), add constraint FK9651EF32D8D56A33 foreign key (PARENT_CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS (ID);
alter table QUERY_INTRA_MODEL_ASSOCIATION add index FKF1EDBDD3BC7298A9 (IDENTIFIER), add constraint FKF1EDBDD3BC7298A9 foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION (IDENTIFIER);
alter table QUERY_CONSTRAINTS add index FKE364FCFFD3C625EA (QUERY_JOIN_GRAPH_ID), add constraint FKE364FCFFD3C625EA foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH (IDENTIFIER);
alter table QUERY_PARAMETERIZED_CONDITION add index FK9BE75A3EBC7298A9 (IDENTIFIER), add constraint FK9BE75A3EBC7298A9 foreign key (IDENTIFIER) references QUERY_CONDITION (IDENTIFIER);
alter table QUERY_CONDITION add index FKACCE6246104CA7 (QUERY_RULE_ID), add constraint FKACCE6246104CA7 foreign key (QUERY_RULE_ID) references QUERY_RULE (IDENTIFIER);
alter table QUERY_RULE add index FK14A6503365F8F4CB (QUERY_EXPRESSION_ID), add constraint FK14A6503365F8F4CB foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION (IDENTIFIER);
alter table QUERY_RULE add index FK14A65033BC7298A9 (IDENTIFIER), add constraint FK14A65033BC7298A9 foreign key (IDENTIFIER) references QUERY_EXPRESSION_OPERAND (IDENTIFIER);
alter table QUERY add index FK49D20A8251EDC5B (QUERY_CONSTRAINTS_ID), add constraint FK49D20A8251EDC5B foreign key (QUERY_CONSTRAINTS_ID) references QUERY_CONSTRAINTS (IDENTIFIER);
alter table QUERY_LOGICAL_CONNECTOR add index FKCF30478065F8F4CB (QUERY_EXPRESSION_ID), add constraint FKCF30478065F8F4CB foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION (IDENTIFIER);
alter table QUERY_EXPRESSION add index FK1B473A8FCA571190 (QUERY_EXPRESSIONID_ID), add constraint FK1B473A8FCA571190 foreign key (QUERY_EXPRESSIONID_ID) references QUERY_EXPRESSIONID (IDENTIFIER);
alter table QUERY_EXPRESSION add index FK1B473A8F9E19EF66 (QUERY_CONSTRAINT_ID), add constraint FK1B473A8F9E19EF66 foreign key (QUERY_CONSTRAINT_ID) references QUERY_CONSTRAINTS (IDENTIFIER);
alter table QUERY_EXPRESSION add index FK1B473A8F1CF7F689 (QUERY_QUERY_ENTITY_ID), add constraint FK1B473A8F1CF7F689 foreign key (QUERY_QUERY_ENTITY_ID) references QUERY_QUERY_ENTITY (IDENTIFIER);
alter table QUERY_CONDITION_VALUES add index FK9997379DDA532516 (QUERY_CONDITION_ID), add constraint FK9997379DDA532516 foreign key (QUERY_CONDITION_ID) references QUERY_CONDITION (IDENTIFIER);
alter table CATEGORIAL_ATTRIBUTE add index FK31F77B56E8CBD948 (CATEGORIAL_CLASS_ID), add constraint FK31F77B56E8CBD948 foreign key (CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS (ID);
alter table QUERY_OUTPUT_ATTRIBUTE add index FK22C9DB75BF5EEB27 (EXPRESSIONID_ID), add constraint FK22C9DB75BF5EEB27 foreign key (EXPRESSIONID_ID) references QUERY_EXPRESSIONID (IDENTIFIER);
alter table QUERY_OUTPUT_ATTRIBUTE add index FK22C9DB75C4E818F8 (PARAMETERIZED_QUERY_ID), add constraint FK22C9DB75C4E818F8 foreign key (PARAMETERIZED_QUERY_ID) references QUERY_PARAMETERIZED_QUERY (IDENTIFIER);
alter table QUERY_INTER_MODEL_ASSOCIATION add index FKD70658D1BC7298A9 (IDENTIFIER), add constraint FKD70658D1BC7298A9 foreign key (IDENTIFIER) references QUERY_MODEL_ASSOCIATION (IDENTIFIER);
alter table QUERY_EXPRESSION_OPERAND add index FKA3B976F965F8F4CB (QUERY_EXPRESSION_ID), add constraint FKA3B976F965F8F4CB foreign key (QUERY_EXPRESSION_ID) references QUERY_EXPRESSION (IDENTIFIER);
alter table QUERY_GRAPH_ENTRY add index FKF055E4EA955C60E6 (QUERY_MODEL_ASSOCIATION_ID), add constraint FKF055E4EA955C60E6 foreign key (QUERY_MODEL_ASSOCIATION_ID) references QUERY_MODEL_ASSOCIATION (IDENTIFIER);
alter table QUERY_GRAPH_ENTRY add index FKF055E4EAD3C625EA (QUERY_JOIN_GRAPH_ID), add constraint FKF055E4EAD3C625EA foreign key (QUERY_JOIN_GRAPH_ID) references QUERY_JOIN_GRAPH (IDENTIFIER);
alter table QUERY_GRAPH_ENTRY add index FKF055E4EA7A5E6479 (TARGET_EXPRESSIONID_ID), add constraint FKF055E4EA7A5E6479 foreign key (TARGET_EXPRESSIONID_ID) references QUERY_EXPRESSIONID (IDENTIFIER);
alter table QUERY_GRAPH_ENTRY add index FKF055E4EAEE560703 (SOURCE_EXPRESSIONID_ID), add constraint FKF055E4EAEE560703 foreign key (SOURCE_EXPRESSIONID_ID) references QUERY_EXPRESSIONID (IDENTIFIER);
alter table CATEGORY add index FK31A8ACFE2D0F63E7 (PARENT_CATEGORY_ID), add constraint FK31A8ACFE2D0F63E7 foreign key (PARENT_CATEGORY_ID) references CATEGORY (ID);
alter table CATEGORY add index FK31A8ACFE211D9A6B (ROOT_CATEGORIAL_CLASS_ID), add constraint FK31A8ACFE211D9A6B foreign key (ROOT_CATEGORIAL_CLASS_ID) references CATEGORIAL_CLASS (ID);
alter table QUERY_EXPRESSIONID add index FK6662DBEABC7298A9 (IDENTIFIER), add constraint FK6662DBEABC7298A9 foreign key (IDENTIFIER) references QUERY_EXPRESSION_OPERAND (IDENTIFIER);
