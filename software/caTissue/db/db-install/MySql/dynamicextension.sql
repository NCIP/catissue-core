SET storage_engine=InnoDB;
SET FOREIGN_KEY_CHECKS=0;
drop table if exists DYEXTN_OBJECT_TYPE_INFO;
drop table if exists DYEXTN_COLUMN_PROPERTIES;
drop table if exists DYEXTN_USERDEFINED_DE;
drop table if exists DYEXTN_CONSTRAINT_PROPERTIES;
drop table if exists DYEXTN_PERMISSIBLE_VALUE;
drop table if exists DYEXTN_CHECK_BOX;
drop table if exists DYEXTN_TABLE_PROPERTIES;
drop table if exists DYEXTN_ENTITY_MAP_CONDNS;
drop table if exists DYEXTN_INTEGER_CONCEPT_VALUE;
drop table if exists DYEXTN_STRING_CONCEPT_VALUE;
drop table if exists DYEXTN_BOOLEAN_TYPE_INFO;
drop table if exists DYEXTN_BOOLEAN_CONCEPT_VALUE;
drop table if exists DE_FILE_ATTR_RECORD_VALUES;
drop table if exists DYEXTN_ABSTRACT_METADATA;
drop table if exists DYEXTN_STRING_TYPE_INFO;
drop table if exists DYEXTN_SHORT_CONCEPT_VALUE;
drop table if exists DYEXTN_FLOAT_TYPE_INFO;
drop table if exists DYEXTN_FILE_UPLOAD;
drop table if exists DYEXTN_BARR_CONCEPT_VALUE;
drop table if exists DYEXTN_ENTITY;
drop table if exists DYEXTN_INTEGER_TYPE_INFO;
drop table if exists DYEXTN_TAGGED_VALUE;
drop table if exists DE_OBJECT_ATTR_RECORD_VALUES;
drop table if exists DYEXTN_COMBOBOX;
drop table if exists DYEXTN_CADSR_VALUE_DOMAIN_INFO;
drop table if exists DYEXTN_PRIMITIVE_ATTRIBUTE;
drop table if exists DYEXTN_ASSOCIATION;
drop table if exists DYEXTN_ENTITY_MAP;
drop table if exists DYEXTN_CADSRDE;
drop table if exists DYEXTN_DOUBLE_TYPE_INFO;
drop table if exists DYEXTN_SELECT_CONTROL;
drop table if exists DYEXTN_ENTITY_GROUP_REL;
drop table if exists DYEXTN_DATABASE_PROPERTIES;
drop table if exists DYEXTN_FILE_TYPE_INFO;
drop table if exists DYEXTN_FILE_EXTENSIONS;
drop table if exists DYEXTN_LONG_TYPE_INFO;
drop table if exists DYEXTN_FLOAT_CONCEPT_VALUE;
drop table if exists DYEXTN_TEXTFIELD;
drop table if exists DYEXTN_ROLE;
drop table if exists DYEXTN_RULE;
drop table if exists DYEXTN_DATE_TYPE_INFO;
drop table if exists DYEXTN_ATTRIBUTE_TYPE_INFO;
drop table if exists DYEXTN_ENTITY_MAP_RECORD;
drop table if exists DYEXTN_ASSO_DISPLAY_ATTR;
drop table if exists DYEXTN_TEXTAREA;
drop table if exists DYEXTN_ENTITY_GROUP;
drop table if exists DYEXTN_DATA_ELEMENT;
drop table if exists DYEXTN_DOUBLE_CONCEPT_VALUE;
drop table if exists DYEXTN_LIST_BOX;
drop table if exists DYEXTN_CONTAINMENT_CONTROL;
drop table if exists DYEXTN_CONTROL;
drop table if exists DYEXTN_LONG_CONCEPT_VALUE;
drop table if exists DYEXTN_FORM_CONTEXT;
drop table if exists DYEXTN_ATTRIBUTE;
drop table if exists DYEXTN_RULE_PARAMETER;
drop table if exists DYEXTN_DATE_CONCEPT_VALUE;
drop table if exists DYEXTN_VIEW;
drop table if exists DYEXTN_BYTE_ARRAY_TYPE_INFO;
drop table if exists DYEXTN_SEMANTIC_PROPERTY;
drop table if exists DYEXTN_ATTRIBUTE_RECORD;
drop table if exists DYEXTN_DATA_GRID;
drop table if exists DE_COLL_ATTR_RECORD_VALUES;
drop table if exists DYEXTN_NUMERIC_TYPE_INFO;
drop table if exists DYEXTN_SHORT_TYPE_INFO;
drop table if exists DYEXTN_CONTAINER;
drop table if exists DYEXTN_RADIOBUTTON;
drop table if exists DYEXTN_DATEPICKER;

SET FOREIGN_KEY_CHECKS=1;

create table DYEXTN_OBJECT_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_COLUMN_PROPERTIES (
   IDENTIFIER bigint not null,
   PRIMITIVE_ATTRIBUTE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_USERDEFINED_DE (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_CONSTRAINT_PROPERTIES (
   IDENTIFIER bigint not null,
   SOURCE_ENTITY_KEY varchar(255),
   TARGET_ENTITY_KEY varchar(255),
   ASSOCIATION_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_PERMISSIBLE_VALUE (
   IDENTIFIER bigint not null auto_increment,
   DESCRIPTION varchar(255),
   ATTRIBUTE_TYPE_INFO_ID bigint,
   USER_DEF_DE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_CHECK_BOX (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_TABLE_PROPERTIES (
   IDENTIFIER bigint not null,
   ENTITY_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_MAP_CONDNS (
   IDENTIFIER bigint not null auto_increment,
   STATIC_RECORD_ID bigint,
   TYPE_ID bigint,
   FORM_CONTEXT_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_INTEGER_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_STRING_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_BOOLEAN_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_BOOLEAN_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE tinyint(1) default NULL,
   primary key (IDENTIFIER)
);
create table DE_FILE_ATTR_RECORD_VALUES (
   IDENTIFIER bigint not null auto_increment,
   CONTENT_TYPE varchar(255),
   FILE_CONTENT blob,
   FILE_NAME varchar(255),
   RECORD_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_ABSTRACT_METADATA (
   IDENTIFIER bigint not null auto_increment,
   CREATED_DATE date,
   DESCRIPTION text,
   LAST_UPDATED date,
   NAME text,
   PUBLIC_ID varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_STRING_TYPE_INFO (
   IDENTIFIER bigint not null,
   MAX_SIZE integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_SHORT_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE smallint,
   primary key (IDENTIFIER)
);
create table DYEXTN_FLOAT_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_FILE_UPLOAD (
   IDENTIFIER bigint not null,
   NO_OF_COLUMNS integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_BARR_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY (
   IDENTIFIER bigint not null,
   DATA_TABLE_STATE integer,
   IS_ABSTRACT tinyint(1) default NULL,
   PARENT_ENTITY_ID bigint,
   INHERITANCE_STRATEGY integer,
   DISCRIMINATOR_COLUMN_NAME varchar(255),
   DISCRIMINATOR_VALUE varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_INTEGER_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_TAGGED_VALUE (
   IDENTIFIER bigint not null auto_increment,
   T_KEY varchar(255),
   T_VALUE varchar(255),
   ABSTRACT_METADATA_ID bigint,
   primary key (IDENTIFIER)
);
create table DE_OBJECT_ATTR_RECORD_VALUES (
   IDENTIFIER bigint not null auto_increment,
   CLASS_NAME varchar(255),
   OBJECT_CONTENT blob,
   RECORD_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_COMBOBOX (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_CADSR_VALUE_DOMAIN_INFO (
   IDENTIFIER bigint not null auto_increment,
   DATATYPE varchar(255),
   NAME varchar(255),
   TYPE varchar(255),
   PRIMITIVE_ATTRIBUTE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_PRIMITIVE_ATTRIBUTE (
   IDENTIFIER bigint not null,
   IS_COLLECTION tinyint(1) default NULL,
   IS_IDENTIFIED tinyint(1) default NULL,
   IS_PRIMARY_KEY tinyint(1) default NULL,
   IS_NULLABLE tinyint(1) default NULL,
   primary key (IDENTIFIER)
);
create table DYEXTN_ASSOCIATION (
   IDENTIFIER bigint not null,
   DIRECTION varchar(255),
   TARGET_ENTITY_ID bigint,
   SOURCE_ROLE_ID bigint,
   TARGET_ROLE_ID bigint,
   IS_SYSTEM_GENERATED tinyint(1) default NULL,
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_MAP (
   IDENTIFIER bigint not null auto_increment,
   CONTAINER_ID bigint,
   STATUS varchar(10),
   STATIC_ENTITY_ID bigint,
   CREATED_DATE date,
   CREATED_BY varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_CADSRDE (
   IDENTIFIER bigint not null,
   PUBLIC_ID varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_DOUBLE_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_SELECT_CONTROL (
   IDENTIFIER bigint not null,
   SEPARATOR_STRING varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_GROUP_REL (
   ENTITY_GROUP_ID bigint not null,
   ENTITY_ID bigint not null,
   primary key (ENTITY_ID, ENTITY_GROUP_ID)
);
create table DYEXTN_DATABASE_PROPERTIES (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_FILE_TYPE_INFO (
   IDENTIFIER bigint not null,
   MAX_FILE_SIZE float,
   primary key (IDENTIFIER)
);
create table DYEXTN_FILE_EXTENSIONS (
   IDENTIFIER bigint not null auto_increment,
   FILE_EXTENSION varchar(255),
   ATTRIBUTE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_LONG_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_FLOAT_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE float,
   primary key (IDENTIFIER)
);
create table DYEXTN_TEXTFIELD (
   IDENTIFIER bigint not null,
   NO_OF_COLUMNS integer,
   IS_PASSWORD tinyint(1) default NULL,
   IS_URL tinyint(1) default NULL,
   primary key (IDENTIFIER)
);
create table DYEXTN_ROLE (
   IDENTIFIER bigint not null auto_increment,
   ASSOCIATION_TYPE varchar(255),
   MAX_CARDINALITY integer,
   MIN_CARDINALITY integer,
   NAME varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_RULE (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255),
   ATTRIBUTE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATE_TYPE_INFO (
   IDENTIFIER bigint not null,
   FORMAT varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_ATTRIBUTE_TYPE_INFO (
   IDENTIFIER bigint not null auto_increment,
   PRIMITIVE_ATTRIBUTE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_MAP_RECORD (
   IDENTIFIER bigint not null auto_increment,
   FORM_CONTEXT_ID bigint,
   STATIC_ENTITY_RECORD_ID bigint,
   STATUS varchar(10),
   DYNAMIC_ENTITY_RECORD_ID bigint,
   MODIFIED_DATE date,
   CREATED_DATE date,
   CREATED_BY varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_ASSO_DISPLAY_ATTR (
   IDENTIFIER bigint not null auto_increment,
   SEQUENCE_NUMBER integer,
   DISPLAY_ATTRIBUTE_ID bigint,
   SELECT_CONTROL_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_TEXTAREA (
   IDENTIFIER bigint not null,
   TEXTAREA_COLUMNS integer,
   TEXTAREA_ROWS integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_GROUP (
   IDENTIFIER bigint not null,
   LONG_NAME varchar(255),
   SHORT_NAME varchar(255),
   VERSION varchar(255),
   IS_SYSTEM_GENERATED tinyint(1) default NULL,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATA_ELEMENT (
   IDENTIFIER bigint not null auto_increment,
   ATTRIBUTE_TYPE_INFO_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_DOUBLE_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE double precision,
   primary key (IDENTIFIER)
);
create table DYEXTN_LIST_BOX (
   IDENTIFIER bigint not null,
   MULTISELECT tinyint(1) default NULL,
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTAINMENT_CONTROL (
   IDENTIFIER bigint not null,
   DISPLAY_CONTAINER_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTROL (
   IDENTIFIER bigint not null auto_increment,
   CAPTION varchar(800),
   CSS_CLASS varchar(255),
   HIDDEN tinyint(1) default NULL,
   NAME varchar(255),
   SEQUENCE_NUMBER integer,
   TOOLTIP varchar(255),
   ABSTRACT_ATTRIBUTE_ID bigint,
   CONTAINER_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_LONG_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_FORM_CONTEXT (
   IDENTIFIER bigint not null auto_increment,
   IS_INFINITE_ENTRY tinyint(1) default NULL,
   ENTITY_MAP_ID bigint,
   STUDY_FORM_LABEL varchar(255),
   NO_OF_ENTRIES integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_ATTRIBUTE (
   IDENTIFIER bigint not null,
   ENTIY_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_RULE_PARAMETER (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255),
   VALUE varchar(255),
   RULE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATE_CONCEPT_VALUE (
   IDENTIFIER bigint not null,
   VALUE datetime,
   primary key (IDENTIFIER)
);
create table DYEXTN_VIEW (
   IDENTIFIER bigint not null auto_increment,
   NAME varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_BYTE_ARRAY_TYPE_INFO (
   IDENTIFIER bigint not null,
   CONTENT_TYPE varchar(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_SEMANTIC_PROPERTY (
   IDENTIFIER bigint not null auto_increment,
   CONCEPT_CODE varchar(255),
   TERM varchar(255),
   THESAURAS_NAME varchar(255),
   SEQUENCE_NUMBER integer,
   CONCEPT_DEFINITION varchar(255),
   ABSTRACT_METADATA_ID bigint,
   ABSTRACT_VALUE_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_ATTRIBUTE_RECORD (
   IDENTIFIER bigint not null auto_increment,
   ENTITY_ID bigint,
   ATTRIBUTE_ID bigint,
   RECORD_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATA_GRID (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DE_COLL_ATTR_RECORD_VALUES (
   IDENTIFIER bigint not null auto_increment,
   RECORD_VALUE text,
   COLLECTION_ATTR_RECORD_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_NUMERIC_TYPE_INFO (
   IDENTIFIER bigint not null,
   MEASUREMENT_UNITS varchar(255),
   DECIMAL_PLACES integer,
   NO_DIGITS integer,
   primary key (IDENTIFIER)
);
create table DYEXTN_SHORT_TYPE_INFO (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTAINER (
   IDENTIFIER bigint not null auto_increment,
   BUTTON_CSS varchar(255),
   CAPTION varchar(800),
   ENTITY_ID bigint,
   MAIN_TABLE_CSS varchar(255),
   REQUIRED_FIELD_INDICATOR varchar(255),
   REQUIRED_FIELD_WARNING_MESSAGE varchar(255),
   TITLE_CSS varchar(255),
   BASE_CONTAINER_ID bigint,
   ENTITY_GROUP_ID bigint,
   VIEW_ID bigint,
   primary key (IDENTIFIER)
);
create table DYEXTN_RADIOBUTTON (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATEPICKER (
   IDENTIFIER bigint not null,
   primary key (IDENTIFIER)
);

alter table DYEXTN_OBJECT_TYPE_INFO add index FK74819FB0BC7298A9 (IDENTIFIER), add constraint FK74819FB0BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_COLUMN_PROPERTIES add index FK8FCE2B3FBC7298A9 (IDENTIFIER), add constraint FK8FCE2B3FBC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES (IDENTIFIER);
alter table DYEXTN_COLUMN_PROPERTIES add index FK8FCE2B3FB4C15A36 (PRIMITIVE_ATTRIBUTE_ID), add constraint FK8FCE2B3FB4C15A36 foreign key (PRIMITIVE_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_USERDEFINED_DE add index FK630761FFBC7298A9 (IDENTIFIER), add constraint FK630761FFBC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATA_ELEMENT (IDENTIFIER);
alter table DYEXTN_CONSTRAINT_PROPERTIES add index FK82886CD8BC7298A9 (IDENTIFIER), add constraint FK82886CD8BC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES (IDENTIFIER);
alter table DYEXTN_CONSTRAINT_PROPERTIES add index FK82886CD8927B15B9 (ASSOCIATION_ID), add constraint FK82886CD8927B15B9 foreign key (ASSOCIATION_ID) references DYEXTN_ASSOCIATION (IDENTIFIER);
alter table DYEXTN_PERMISSIBLE_VALUE add index FK136264E08C8D972A (ATTRIBUTE_TYPE_INFO_ID), add constraint FK136264E08C8D972A foreign key (ATTRIBUTE_TYPE_INFO_ID) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_PERMISSIBLE_VALUE add index FK136264E03D51114B (USER_DEF_DE_ID), add constraint FK136264E03D51114B foreign key (USER_DEF_DE_ID) references DYEXTN_USERDEFINED_DE (IDENTIFIER);
alter table DYEXTN_CHECK_BOX add index FK4EFF9257BC7298A9 (IDENTIFIER), add constraint FK4EFF9257BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_TABLE_PROPERTIES add index FKE608E08179F466F7 (ENTITY_ID), add constraint FKE608E08179F466F7 foreign key (ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_TABLE_PROPERTIES add index FKE608E081BC7298A9 (IDENTIFIER), add constraint FKE608E081BC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES (IDENTIFIER);
alter table DYEXTN_ENTITY_MAP_CONDNS add index FK2A9D602969F17C26 (FORM_CONTEXT_ID), add constraint FK2A9D602969F17C26 foreign key (FORM_CONTEXT_ID) references DYEXTN_FORM_CONTEXT (IDENTIFIER);
alter table DYEXTN_INTEGER_CONCEPT_VALUE add index FKFBA33B3CBC7298A9 (IDENTIFIER), add constraint FKFBA33B3CBC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_STRING_CONCEPT_VALUE add index FKADE7D889BC7298A9 (IDENTIFIER), add constraint FKADE7D889BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_BOOLEAN_TYPE_INFO add index FK28F1809FBC7298A9 (IDENTIFIER), add constraint FK28F1809FBC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_BOOLEAN_CONCEPT_VALUE add index FK57B6C4A6BC7298A9 (IDENTIFIER), add constraint FK57B6C4A6BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DE_FILE_ATTR_RECORD_VALUES add index FKE68334E7E150DFC9 (RECORD_ID), add constraint FKE68334E7E150DFC9 foreign key (RECORD_ID) references DYEXTN_ATTRIBUTE_RECORD (IDENTIFIER);
alter table DYEXTN_STRING_TYPE_INFO add index FKDA35FE02BC7298A9 (IDENTIFIER), add constraint FKDA35FE02BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_SHORT_CONCEPT_VALUE add index FKC1945ABABC7298A9 (IDENTIFIER), add constraint FKC1945ABABC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_FLOAT_TYPE_INFO add index FK7E1C0693BC7298A9 (IDENTIFIER), add constraint FK7E1C0693BC7298A9 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_FILE_UPLOAD add index FK2FAD41E7BC7298A9 (IDENTIFIER), add constraint FK2FAD41E7BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_BARR_CONCEPT_VALUE add index FK89D27DF7BC7298A9 (IDENTIFIER), add constraint FK89D27DF7BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_ENTITY add index FK8B243640450711A2 (PARENT_ENTITY_ID), add constraint FK8B243640450711A2 foreign key (PARENT_ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_ENTITY add index FK8B243640BC7298A9 (IDENTIFIER), add constraint FK8B243640BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);
alter table DYEXTN_INTEGER_TYPE_INFO add index FK5F9CB235BC7298A9 (IDENTIFIER), add constraint FK5F9CB235BC7298A9 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_TAGGED_VALUE add index FKF79D055B7D7A9B8E (ABSTRACT_METADATA_ID), add constraint FKF79D055B7D7A9B8E foreign key (ABSTRACT_METADATA_ID) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);
alter table DE_OBJECT_ATTR_RECORD_VALUES add index FK504EADC4E150DFC9 (RECORD_ID), add constraint FK504EADC4E150DFC9 foreign key (RECORD_ID) references DYEXTN_ATTRIBUTE_RECORD (IDENTIFIER);
alter table DYEXTN_COMBOBOX add index FKABBC649ABC7298A9 (IDENTIFIER), add constraint FKABBC649ABC7298A9 foreign key (IDENTIFIER) references DYEXTN_SELECT_CONTROL (IDENTIFIER);
alter table DYEXTN_CADSR_VALUE_DOMAIN_INFO add index FK1C9AA364B4C15A36 (PRIMITIVE_ATTRIBUTE_ID), add constraint FK1C9AA364B4C15A36 foreign key (PRIMITIVE_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_PRIMITIVE_ATTRIBUTE add index FKA9F765C7BC7298A9 (IDENTIFIER), add constraint FKA9F765C7BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_ASSOCIATION add index FK104684243AC5160 (SOURCE_ROLE_ID), add constraint FK104684243AC5160 foreign key (SOURCE_ROLE_ID) references DYEXTN_ROLE (IDENTIFIER);
alter table DYEXTN_ASSOCIATION add index FK10468424F60C84D6 (TARGET_ROLE_ID), add constraint FK10468424F60C84D6 foreign key (TARGET_ROLE_ID) references DYEXTN_ROLE (IDENTIFIER);
alter table DYEXTN_ASSOCIATION add index FK104684246315C5C9 (TARGET_ENTITY_ID), add constraint FK104684246315C5C9 foreign key (TARGET_ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_ASSOCIATION add index FK10468424BC7298A9 (IDENTIFIER), add constraint FK10468424BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_CADSRDE add index FK588A2509BC7298A9 (IDENTIFIER), add constraint FK588A2509BC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATA_ELEMENT (IDENTIFIER);
alter table DYEXTN_DOUBLE_TYPE_INFO add index FKC83869C2BC7298A9 (IDENTIFIER), add constraint FKC83869C2BC7298A9 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_SELECT_CONTROL add index FKDFEBB657BC7298A9 (IDENTIFIER), add constraint FKDFEBB657BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_ENTITY_GROUP_REL add index FK5A0D835A992A67D7 (ENTITY_GROUP_ID), add constraint FK5A0D835A992A67D7 foreign key (ENTITY_GROUP_ID) references DYEXTN_ENTITY_GROUP (IDENTIFIER);
alter table DYEXTN_ENTITY_GROUP_REL add index FK5A0D835A79F466F7 (ENTITY_ID), add constraint FK5A0D835A79F466F7 foreign key (ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_FILE_TYPE_INFO add index FKA00F0EDBC7298A9 (IDENTIFIER), add constraint FKA00F0EDBC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_FILE_EXTENSIONS add index FKD49834FA4D87D1BE (ATTRIBUTE_ID), add constraint FKD49834FA4D87D1BE foreign key (ATTRIBUTE_ID) references DYEXTN_FILE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_LONG_TYPE_INFO add index FK257281EDBC7298A9 (IDENTIFIER), add constraint FK257281EDBC7298A9 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_FLOAT_CONCEPT_VALUE add index FK6785309ABC7298A9 (IDENTIFIER), add constraint FK6785309ABC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_TEXTFIELD add index FKF9AFC850BC7298A9 (IDENTIFIER), add constraint FKF9AFC850BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_RULE add index FKC27E0994D87D1BE (ATTRIBUTE_ID), add constraint FKC27E0994D87D1BE foreign key (ATTRIBUTE_ID) references DYEXTN_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_DATE_TYPE_INFO add index FKFBA549FBC7298A9 (IDENTIFIER), add constraint FKFBA549FBC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_ATTRIBUTE_TYPE_INFO add index FK62596D53B4C15A36 (PRIMITIVE_ATTRIBUTE_ID), add constraint FK62596D53B4C15A36 foreign key (PRIMITIVE_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_ENTITY_MAP_RECORD add index FK43A4501369F17C26 (FORM_CONTEXT_ID), add constraint FK43A4501369F17C26 foreign key (FORM_CONTEXT_ID) references DYEXTN_FORM_CONTEXT (IDENTIFIER);
alter table DYEXTN_ASSO_DISPLAY_ATTR add index FKD12FD3823B3AAE3B (DISPLAY_ATTRIBUTE_ID), add constraint FKD12FD3823B3AAE3B foreign key (DISPLAY_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_ASSO_DISPLAY_ATTR add index FKD12FD382F7AA8E80 (SELECT_CONTROL_ID), add constraint FKD12FD382F7AA8E80 foreign key (SELECT_CONTROL_ID) references DYEXTN_SELECT_CONTROL (IDENTIFIER);
alter table DYEXTN_TEXTAREA add index FK946EE257BC7298A9 (IDENTIFIER), add constraint FK946EE257BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_ENTITY_GROUP add index FK105DE7A0BC7298A9 (IDENTIFIER), add constraint FK105DE7A0BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);
alter table DYEXTN_DATA_ELEMENT add index FKB1153E48C8D972A (ATTRIBUTE_TYPE_INFO_ID), add constraint FKB1153E48C8D972A foreign key (ATTRIBUTE_TYPE_INFO_ID) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_DOUBLE_CONCEPT_VALUE add index FKB94E6449BC7298A9 (IDENTIFIER), add constraint FKB94E6449BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_LIST_BOX add index FK208395A7BC7298A9 (IDENTIFIER), add constraint FK208395A7BC7298A9 foreign key (IDENTIFIER) references DYEXTN_SELECT_CONTROL (IDENTIFIER);
alter table DYEXTN_CONTAINMENT_CONTROL add index FK3F9D4AD3F7798636 (DISPLAY_CONTAINER_ID), add constraint FK3F9D4AD3F7798636 foreign key (DISPLAY_CONTAINER_ID) references DYEXTN_CONTAINER (IDENTIFIER);
alter table DYEXTN_CONTAINMENT_CONTROL add index FK3F9D4AD3BC7298A9 (IDENTIFIER), add constraint FK3F9D4AD3BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_CONTROL add index FK70FB5E80A67822BB (ABSTRACT_ATTRIBUTE_ID), add constraint FK70FB5E80A67822BB foreign key (ABSTRACT_ATTRIBUTE_ID) references DYEXTN_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_CONTROL add index FK70FB5E809C6A9B9 (CONTAINER_ID), add constraint FK70FB5E809C6A9B9 foreign key (CONTAINER_ID) references DYEXTN_CONTAINER (IDENTIFIER);
alter table DYEXTN_LONG_CONCEPT_VALUE add index FK3E1A6EF4BC7298A9 (IDENTIFIER), add constraint FK3E1A6EF4BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_FORM_CONTEXT add index FKE56CCDB111B8FADA (ENTITY_MAP_ID), add constraint FKE56CCDB111B8FADA foreign key (ENTITY_MAP_ID) references DYEXTN_ENTITY_MAP (IDENTIFIER);
alter table DYEXTN_ATTRIBUTE add index FK37F1E2FFB15CD09F (ENTIY_ID), add constraint FK37F1E2FFB15CD09F foreign key (ENTIY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_ATTRIBUTE add index FK37F1E2FFBC7298A9 (IDENTIFIER), add constraint FK37F1E2FFBC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);
alter table DYEXTN_RULE_PARAMETER add index FK22567363871AAD3E (RULE_ID), add constraint FK22567363871AAD3E foreign key (RULE_ID) references DYEXTN_RULE (IDENTIFIER);
alter table DYEXTN_DATE_CONCEPT_VALUE add index FK45F598A6BC7298A9 (IDENTIFIER), add constraint FK45F598A6BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_BYTE_ARRAY_TYPE_INFO add index FK18BDA73BC7298A9 (IDENTIFIER), add constraint FK18BDA73BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_SEMANTIC_PROPERTY add index FKD2A0B5B13BAB5E46 (ABSTRACT_VALUE_ID), add constraint FKD2A0B5B13BAB5E46 foreign key (ABSTRACT_VALUE_ID) references DYEXTN_PERMISSIBLE_VALUE (IDENTIFIER);
alter table DYEXTN_SEMANTIC_PROPERTY add index FKD2A0B5B17D7A9B8E (ABSTRACT_METADATA_ID), add constraint FKD2A0B5B17D7A9B8E foreign key (ABSTRACT_METADATA_ID) references DYEXTN_ABSTRACT_METADATA (IDENTIFIER);
alter table DYEXTN_ATTRIBUTE_RECORD add index FK9B20ED9179F466F7 (ENTITY_ID), add constraint FK9B20ED9179F466F7 foreign key (ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_ATTRIBUTE_RECORD add index FK9B20ED914D87D1BE (ATTRIBUTE_ID), add constraint FK9B20ED914D87D1BE foreign key (ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE (IDENTIFIER);
alter table DYEXTN_DATA_GRID add index FK233EB73EBC7298A9 (IDENTIFIER), add constraint FK233EB73EBC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DE_COLL_ATTR_RECORD_VALUES add index FK847DA577355836BC (COLLECTION_ATTR_RECORD_ID), add constraint FK847DA577355836BC foreign key (COLLECTION_ATTR_RECORD_ID) references DYEXTN_ATTRIBUTE_RECORD (IDENTIFIER);
alter table DYEXTN_NUMERIC_TYPE_INFO add index FK4DEC9544BC7298A9 (IDENTIFIER), add constraint FK4DEC9544BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_SHORT_TYPE_INFO add index FK99540B3BC7298A9 (IDENTIFIER), add constraint FK99540B3BC7298A9 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO (IDENTIFIER);
alter table DYEXTN_CONTAINER add index FK1EAB84E4A1257067 (BASE_CONTAINER_ID), add constraint FK1EAB84E4A1257067 foreign key (BASE_CONTAINER_ID) references DYEXTN_CONTAINER (IDENTIFIER);
alter table DYEXTN_CONTAINER add index FK1EAB84E479F466F7 (ENTITY_ID), add constraint FK1EAB84E479F466F7 foreign key (ENTITY_ID) references DYEXTN_ENTITY (IDENTIFIER);
alter table DYEXTN_CONTAINER add index FK1EAB84E4992A67D7 (ENTITY_GROUP_ID), add constraint FK1EAB84E4992A67D7 foreign key (ENTITY_GROUP_ID) references DYEXTN_ENTITY_GROUP (IDENTIFIER);
alter table DYEXTN_CONTAINER add index FK1EAB84E445DEFCF5 (VIEW_ID), add constraint FK1EAB84E445DEFCF5 foreign key (VIEW_ID) references DYEXTN_VIEW (IDENTIFIER);
alter table DYEXTN_RADIOBUTTON add index FK16F5BA90BC7298A9 (IDENTIFIER), add constraint FK16F5BA90BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
alter table DYEXTN_DATEPICKER add index FKFEADD199BC7298A9 (IDENTIFIER), add constraint FKFEADD199BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL (IDENTIFIER);
