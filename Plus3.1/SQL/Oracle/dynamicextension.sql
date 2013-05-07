create table DYEXTN_OBJECT_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_COLUMN_PROPERTIES (
   IDENTIFIER number(19,0) not null,
   PRIMITIVE_ATTRIBUTE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_USERDEFINED_DE (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_CONSTRAINT_PROPERTIES (
   IDENTIFIER number(19,0) not null,
   SOURCE_ENTITY_KEY varchar2(255),
   TARGET_ENTITY_KEY varchar2(255),
   ASSOCIATION_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_PERMISSIBLE_VALUE (
   IDENTIFIER number(19,0) not null,
   DESCRIPTION varchar2(255),
   ATTRIBUTE_TYPE_INFO_ID number(19,0),
   USER_DEF_DE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_CHECK_BOX (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_TABLE_PROPERTIES (
   IDENTIFIER number(19,0) not null,
   ENTITY_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_MAP_CONDNS (
   IDENTIFIER number(19,0) not null,
   STATIC_RECORD_ID number(19,0),
   TYPE_ID number(19,0),
   FORM_CONTEXT_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_INTEGER_CONCEPT_VALUE (
   IDENTIFIER number(19,0) not null,
   VALUE number(10,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_STRING_CONCEPT_VALUE (
   IDENTIFIER number(19,0) not null,
   VALUE varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_BOOLEAN_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_BOOLEAN_CONCEPT_VALUE (
   IDENTIFIER number(19,0) not null,
   VALUE number(1,0),
   primary key (IDENTIFIER)
);
create table DE_FILE_ATTR_RECORD_VALUES (
   IDENTIFIER number(19,0) not null,
   CONTENT_TYPE varchar2(255),
   FILE_CONTENT blob,
   FILE_NAME varchar2(255),
   RECORD_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ABSTRACT_METADATA (
   IDENTIFIER number(19,0) not null,
   CREATED_DATE date,
   DESCRIPTION varchar2(1000),
   LAST_UPDATED date,
   NAME varchar2(1000),
   PUBLIC_ID varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_STRING_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   MAX_SIZE number(10,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_SHORT_CONCEPT_VALUE (
   IDENTIFIER number(19,0) not null,
   VALUE number(5,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_FLOAT_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_FILE_UPLOAD (
   IDENTIFIER number(19,0) not null,
   NO_OF_COLUMNS number(10,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_BARR_CONCEPT_VALUE (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY (
   IDENTIFIER number(19,0) not null,
   DATA_TABLE_STATE number(10,0),
   IS_ABSTRACT number(1,0),
   PARENT_ENTITY_ID number(19,0),
   INHERITANCE_STRATEGY number(10,0),
   DISCRIMINATOR_COLUMN_NAME varchar2(255),
   DISCRIMINATOR_VALUE varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_INTEGER_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_TAGGED_VALUE (
   IDENTIFIER number(19,0) not null,
   T_KEY varchar2(255),
   T_VALUE varchar2(255),
   ABSTRACT_METADATA_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DE_OBJECT_ATTR_RECORD_VALUES (
   IDENTIFIER number(19,0) not null,
   CLASS_NAME varchar2(255),
   OBJECT_CONTENT blob,
   RECORD_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_COMBOBOX (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_CADSR_VALUE_DOMAIN_INFO (
   IDENTIFIER number(19,0) not null,
   DATATYPE varchar2(255),
   NAME varchar2(255),
   TYPE varchar2(255),
   PRIMITIVE_ATTRIBUTE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_PRIMITIVE_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   IS_COLLECTION number(1,0),
   IS_IDENTIFIED number(1,0),
   IS_PRIMARY_KEY number(1,0),
   IS_NULLABLE number(1,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ASSOCIATION (
   IDENTIFIER number(19,0) not null,
   DIRECTION varchar2(255),
   TARGET_ENTITY_ID number(19,0),
   SOURCE_ROLE_ID number(19,0),
   TARGET_ROLE_ID number(19,0),
   IS_SYSTEM_GENERATED number(1,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_MAP (
   IDENTIFIER number(19,0) not null,
   CONTAINER_ID number(19,0),
   STATUS varchar2(10),
   STATIC_ENTITY_ID number(19,0),
   CREATED_DATE date,
   CREATED_BY varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_CADSRDE (
   IDENTIFIER number(19,0) not null,
   PUBLIC_ID varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_DOUBLE_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_SELECT_CONTROL (
   IDENTIFIER number(19,0) not null,
   SEPARATOR_STRING varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_GROUP_REL (
   ENTITY_GROUP_ID number(19,0) not null,
   ENTITY_ID number(19,0) not null,
   primary key (ENTITY_ID, ENTITY_GROUP_ID)
);
create table DYEXTN_DATABASE_PROPERTIES (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_FILE_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   MAX_FILE_SIZE float,
   primary key (IDENTIFIER)
);
create table DYEXTN_FILE_EXTENSIONS (
   IDENTIFIER number(19,0) not null,
   FILE_EXTENSION varchar2(255),
   ATTRIBUTE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_LONG_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_FLOAT_CONCEPT_VALUE (
   IDENTIFIER number(19,0) not null,
   VALUE float,
   primary key (IDENTIFIER)
);
create table DYEXTN_TEXTFIELD (
   IDENTIFIER number(19,0) not null,
   NO_OF_COLUMNS number(10,0),
   IS_PASSWORD number(1,0),
   IS_URL number(1,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ROLE (
   IDENTIFIER number(19,0) not null,
   ASSOCIATION_TYPE varchar2(255),
   MAX_CARDINALITY number(10,0),
   MIN_CARDINALITY number(10,0),
   NAME varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_RULE (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(255),
   ATTRIBUTE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_DATE_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   FORMAT varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_ATTRIBUTE_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   PRIMITIVE_ATTRIBUTE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_MAP_RECORD (
   IDENTIFIER number(19,0) not null,
   FORM_CONTEXT_ID number(19,0),
   STATIC_ENTITY_RECORD_ID number(19,0),
   STATUS varchar2(10),
   DYNAMIC_ENTITY_RECORD_ID number(19,0),
   MODIFIED_DATE date,
   CREATED_DATE date,
   CREATED_BY varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_ASSO_DISPLAY_ATTR (
   IDENTIFIER number(19,0) not null,
   SEQUENCE_NUMBER number(10,0),
   DISPLAY_ATTRIBUTE_ID number(19,0),
   SELECT_CONTROL_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_TEXTAREA (
   IDENTIFIER number(19,0) not null,
   TEXTAREA_COLUMNS number(10,0),
   TEXTAREA_ROWS number(10,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ENTITY_GROUP (
   IDENTIFIER number(19,0) not null,
   LONG_NAME varchar2(255),
   SHORT_NAME varchar2(255),
   VERSION varchar2(255),
   IS_SYSTEM_GENERATED number(1,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_DATA_ELEMENT (
   IDENTIFIER number(19,0) not null,
   ATTRIBUTE_TYPE_INFO_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_DOUBLE_CONCEPT_VALUE (
   IDENTIFIER number(19,0) not null,
   VALUE double precision,
   primary key (IDENTIFIER)
);
create table DYEXTN_LIST_BOX (
   IDENTIFIER number(19,0) not null,
   MULTISELECT number(1,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTAINMENT_CONTROL (
   IDENTIFIER number(19,0) not null,
   DISPLAY_CONTAINER_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTROL (
   IDENTIFIER number(19,0) not null,
   CAPTION varchar2(800),
   CSS_CLASS varchar2(255),
   HIDDEN number(1,0),
   NAME varchar2(255),
   SEQUENCE_NUMBER number(10,0),
   TOOLTIP varchar2(255),
   ABSTRACT_ATTRIBUTE_ID number(19,0),
   CONTAINER_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_LONG_CONCEPT_VALUE (
   IDENTIFIER number(19,0) not null,
   VALUE number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_FORM_CONTEXT (
   IDENTIFIER number(19,0) not null,
   IS_INFINITE_ENTRY number(1,0),
   ENTITY_MAP_ID number(19,0),
   STUDY_FORM_LABEL varchar2(255),
   NO_OF_ENTRIES number(10,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ATTRIBUTE (
   IDENTIFIER number(19,0) not null,
   ENTIY_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_RULE_PARAMETER (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(255),
   VALUE varchar2(255),
   RULE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_DATE_CONCEPT_VALUE (
   IDENTIFIER number(19,0) not null,
   VALUE date,
   primary key (IDENTIFIER)
);
create table DYEXTN_VIEW (
   IDENTIFIER number(19,0) not null,
   NAME varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_BYTE_ARRAY_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   CONTENT_TYPE varchar2(255),
   primary key (IDENTIFIER)
);
create table DYEXTN_SEMANTIC_PROPERTY (
   IDENTIFIER number(19,0) not null,
   CONCEPT_CODE varchar2(255),
   TERM varchar2(255),
   THESAURAS_NAME varchar2(255),
   SEQUENCE_NUMBER number(10,0),
   CONCEPT_DEFINITION varchar2(255),
   ABSTRACT_METADATA_ID number(19,0),
   ABSTRACT_VALUE_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_ATTRIBUTE_RECORD (
   IDENTIFIER number(19,0) not null,
   ENTITY_ID number(19,0),
   ATTRIBUTE_ID number(19,0),
   RECORD_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_DATA_GRID (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DE_COLL_ATTR_RECORD_VALUES (
   IDENTIFIER number(19,0) not null,
   RECORD_VALUE varchar2(4000),
   COLLECTION_ATTR_RECORD_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_NUMERIC_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   MEASUREMENT_UNITS varchar2(255),
   DECIMAL_PLACES number(10,0),
   NO_DIGITS number(10,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_SHORT_TYPE_INFO (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_CONTAINER (
   IDENTIFIER number(19,0) not null,
   BUTTON_CSS varchar2(255),
   CAPTION varchar2(800),
   ENTITY_ID number(19,0),
   MAIN_TABLE_CSS varchar2(255),
   REQUIRED_FIELD_INDICATOR varchar2(255),
   REQUIRED_FIELD_WARNING_MESSAGE varchar2(255),
   TITLE_CSS varchar2(255),
   BASE_CONTAINER_ID number(19,0),
   ENTITY_GROUP_ID number(19,0),
   VIEW_ID number(19,0),
   primary key (IDENTIFIER)
);
create table DYEXTN_RADIOBUTTON (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);
create table DYEXTN_DATEPICKER (
   IDENTIFIER number(19,0) not null,
   primary key (IDENTIFIER)
);

alter table DYEXTN_OBJECT_TYPE_INFO add constraint FK74819FB0BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO;
alter table DYEXTN_COLUMN_PROPERTIES add constraint FK8FCE2B3FBC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES;
alter table DYEXTN_COLUMN_PROPERTIES add constraint FK8FCE2B3FB4C15A36 foreign key (PRIMITIVE_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE;
alter table DYEXTN_USERDEFINED_DE add constraint FK630761FFBC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATA_ELEMENT;
alter table DYEXTN_CONSTRAINT_PROPERTIES add constraint FK82886CD8BC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES;
alter table DYEXTN_CONSTRAINT_PROPERTIES add constraint FK82886CD8927B15B9 foreign key (ASSOCIATION_ID) references DYEXTN_ASSOCIATION;
alter table DYEXTN_PERMISSIBLE_VALUE add constraint FK136264E08C8D972A foreign key (ATTRIBUTE_TYPE_INFO_ID) references DYEXTN_ATTRIBUTE_TYPE_INFO;
alter table DYEXTN_PERMISSIBLE_VALUE add constraint FK136264E03D51114B foreign key (USER_DEF_DE_ID) references DYEXTN_USERDEFINED_DE;
alter table DYEXTN_CHECK_BOX add constraint FK4EFF9257BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_TABLE_PROPERTIES add constraint FKE608E08179F466F7 foreign key (ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_TABLE_PROPERTIES add constraint FKE608E081BC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATABASE_PROPERTIES;
alter table DYEXTN_ENTITY_MAP_CONDNS add constraint FK2A9D602969F17C26 foreign key (FORM_CONTEXT_ID) references DYEXTN_FORM_CONTEXT;
alter table DYEXTN_INTEGER_CONCEPT_VALUE add constraint FKFBA33B3CBC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE;
alter table DYEXTN_STRING_CONCEPT_VALUE add constraint FKADE7D889BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE;
alter table DYEXTN_BOOLEAN_TYPE_INFO add constraint FK28F1809FBC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO;
alter table DYEXTN_BOOLEAN_CONCEPT_VALUE add constraint FK57B6C4A6BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE;
alter table DE_FILE_ATTR_RECORD_VALUES add constraint FKE68334E7E150DFC9 foreign key (RECORD_ID) references DYEXTN_ATTRIBUTE_RECORD;
alter table DYEXTN_STRING_TYPE_INFO add constraint FKDA35FE02BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO;
alter table DYEXTN_SHORT_CONCEPT_VALUE add constraint FKC1945ABABC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE;
alter table DYEXTN_FLOAT_TYPE_INFO add constraint FK7E1C0693BC7298A9 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO;
alter table DYEXTN_FILE_UPLOAD add constraint FK2FAD41E7BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_BARR_CONCEPT_VALUE add constraint FK89D27DF7BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE;
alter table DYEXTN_ENTITY add constraint FK8B243640450711A2 foreign key (PARENT_ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_ENTITY add constraint FK8B243640BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA;
alter table DYEXTN_INTEGER_TYPE_INFO add constraint FK5F9CB235BC7298A9 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO;
alter table DYEXTN_TAGGED_VALUE add constraint FKF79D055B7D7A9B8E foreign key (ABSTRACT_METADATA_ID) references DYEXTN_ABSTRACT_METADATA;
alter table DE_OBJECT_ATTR_RECORD_VALUES add constraint FK504EADC4E150DFC9 foreign key (RECORD_ID) references DYEXTN_ATTRIBUTE_RECORD;
alter table DYEXTN_COMBOBOX add constraint FKABBC649ABC7298A9 foreign key (IDENTIFIER) references DYEXTN_SELECT_CONTROL;
alter table DYEXTN_CADSR_VALUE_DOMAIN_INFO add constraint FK1C9AA364B4C15A36 foreign key (PRIMITIVE_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE;
alter table DYEXTN_PRIMITIVE_ATTRIBUTE add constraint FKA9F765C7BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE;
alter table DYEXTN_ASSOCIATION add constraint FK104684243AC5160 foreign key (SOURCE_ROLE_ID) references DYEXTN_ROLE;
alter table DYEXTN_ASSOCIATION add constraint FK10468424F60C84D6 foreign key (TARGET_ROLE_ID) references DYEXTN_ROLE;
alter table DYEXTN_ASSOCIATION add constraint FK104684246315C5C9 foreign key (TARGET_ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_ASSOCIATION add constraint FK10468424BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE;
alter table DYEXTN_CADSRDE add constraint FK588A2509BC7298A9 foreign key (IDENTIFIER) references DYEXTN_DATA_ELEMENT;
alter table DYEXTN_DOUBLE_TYPE_INFO add constraint FKC83869C2BC7298A9 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO;
alter table DYEXTN_SELECT_CONTROL add constraint FKDFEBB657BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_ENTITY_GROUP_REL add constraint FK5A0D835A992A67D7 foreign key (ENTITY_GROUP_ID) references DYEXTN_ENTITY_GROUP;
alter table DYEXTN_ENTITY_GROUP_REL add constraint FK5A0D835A79F466F7 foreign key (ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_FILE_TYPE_INFO add constraint FKA00F0EDBC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO;
alter table DYEXTN_FILE_EXTENSIONS add constraint FKD49834FA4D87D1BE foreign key (ATTRIBUTE_ID) references DYEXTN_FILE_TYPE_INFO;
alter table DYEXTN_LONG_TYPE_INFO add constraint FK257281EDBC7298A9 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO;
alter table DYEXTN_FLOAT_CONCEPT_VALUE add constraint FK6785309ABC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE;
alter table DYEXTN_TEXTFIELD add constraint FKF9AFC850BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_RULE add constraint FKC27E0994D87D1BE foreign key (ATTRIBUTE_ID) references DYEXTN_ATTRIBUTE;
alter table DYEXTN_DATE_TYPE_INFO add constraint FKFBA549FBC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO;
alter table DYEXTN_ATTRIBUTE_TYPE_INFO add constraint FK62596D53B4C15A36 foreign key (PRIMITIVE_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE;
alter table DYEXTN_ENTITY_MAP_RECORD add constraint FK43A4501369F17C26 foreign key (FORM_CONTEXT_ID) references DYEXTN_FORM_CONTEXT;
alter table DYEXTN_ASSO_DISPLAY_ATTR add constraint FKD12FD3823B3AAE3B foreign key (DISPLAY_ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE;
alter table DYEXTN_ASSO_DISPLAY_ATTR add constraint FKD12FD382F7AA8E80 foreign key (SELECT_CONTROL_ID) references DYEXTN_SELECT_CONTROL;
alter table DYEXTN_TEXTAREA add constraint FK946EE257BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_ENTITY_GROUP add constraint FK105DE7A0BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA;
alter table DYEXTN_DATA_ELEMENT add constraint FKB1153E48C8D972A foreign key (ATTRIBUTE_TYPE_INFO_ID) references DYEXTN_ATTRIBUTE_TYPE_INFO;
alter table DYEXTN_DOUBLE_CONCEPT_VALUE add constraint FKB94E6449BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE;
alter table DYEXTN_LIST_BOX add constraint FK208395A7BC7298A9 foreign key (IDENTIFIER) references DYEXTN_SELECT_CONTROL;
alter table DYEXTN_CONTAINMENT_CONTROL add constraint FK3F9D4AD3F7798636 foreign key (DISPLAY_CONTAINER_ID) references DYEXTN_CONTAINER;
alter table DYEXTN_CONTAINMENT_CONTROL add constraint FK3F9D4AD3BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_CONTROL add constraint FK70FB5E80A67822BB foreign key (ABSTRACT_ATTRIBUTE_ID) references DYEXTN_ATTRIBUTE;
alter table DYEXTN_CONTROL add constraint FK70FB5E809C6A9B9 foreign key (CONTAINER_ID) references DYEXTN_CONTAINER;
alter table DYEXTN_LONG_CONCEPT_VALUE add constraint FK3E1A6EF4BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE;
alter table DYEXTN_FORM_CONTEXT add constraint FKE56CCDB111B8FADA foreign key (ENTITY_MAP_ID) references DYEXTN_ENTITY_MAP;
alter table DYEXTN_ATTRIBUTE add constraint FK37F1E2FFB15CD09F foreign key (ENTIY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_ATTRIBUTE add constraint FK37F1E2FFBC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_METADATA;
alter table DYEXTN_RULE_PARAMETER add constraint FK22567363871AAD3E foreign key (RULE_ID) references DYEXTN_RULE;
alter table DYEXTN_DATE_CONCEPT_VALUE add constraint FK45F598A6BC7298A9 foreign key (IDENTIFIER) references DYEXTN_PERMISSIBLE_VALUE;
alter table DYEXTN_BYTE_ARRAY_TYPE_INFO add constraint FK18BDA73BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO;
alter table DYEXTN_SEMANTIC_PROPERTY add constraint FKD2A0B5B13BAB5E46 foreign key (ABSTRACT_VALUE_ID) references DYEXTN_PERMISSIBLE_VALUE;
alter table DYEXTN_SEMANTIC_PROPERTY add constraint FKD2A0B5B17D7A9B8E foreign key (ABSTRACT_METADATA_ID) references DYEXTN_ABSTRACT_METADATA;
alter table DYEXTN_ATTRIBUTE_RECORD add constraint FK9B20ED9179F466F7 foreign key (ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_ATTRIBUTE_RECORD add constraint FK9B20ED914D87D1BE foreign key (ATTRIBUTE_ID) references DYEXTN_PRIMITIVE_ATTRIBUTE;
alter table DYEXTN_DATA_GRID add constraint FK233EB73EBC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DE_COLL_ATTR_RECORD_VALUES add constraint FK847DA577355836BC foreign key (COLLECTION_ATTR_RECORD_ID) references DYEXTN_ATTRIBUTE_RECORD;
alter table DYEXTN_NUMERIC_TYPE_INFO add constraint FK4DEC9544BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ATTRIBUTE_TYPE_INFO;
alter table DYEXTN_SHORT_TYPE_INFO add constraint FK99540B3BC7298A9 foreign key (IDENTIFIER) references DYEXTN_NUMERIC_TYPE_INFO;
alter table DYEXTN_CONTAINER add constraint FK1EAB84E4A1257067 foreign key (BASE_CONTAINER_ID) references DYEXTN_CONTAINER;
alter table DYEXTN_CONTAINER add constraint FK1EAB84E479F466F7 foreign key (ENTITY_ID) references DYEXTN_ENTITY;
alter table DYEXTN_CONTAINER add constraint FK1EAB84E4992A67D7 foreign key (ENTITY_GROUP_ID) references DYEXTN_ENTITY_GROUP;
alter table DYEXTN_CONTAINER add constraint FK1EAB84E445DEFCF5 foreign key (VIEW_ID) references DYEXTN_VIEW;
alter table DYEXTN_RADIOBUTTON add constraint FK16F5BA90BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
alter table DYEXTN_DATEPICKER add constraint FKFEADD199BC7298A9 foreign key (IDENTIFIER) references DYEXTN_CONTROL;
create sequence DYEXTN_PERMISSIBLEVAL_SEQ;
create sequence DYEXTN_SEMANTIC_PROPERTY_SEQ;
create sequence DYEXTN_ATTRIBUTE_TYPE_INFO_SEQ;
create sequence DYEXTN_RULE_PARAMETER_SEQ;
create sequence DYEXTN_ABSTRACT_METADATA_SEQ;
create sequence DE_FILE_ATTR_REC_VALUES_SEQ;
create sequence DE_OBJECT_ATTR_REC_VALUES_SEQ;
create sequence DYEXTN_VIEW_SEQ;
create sequence DYEXTN_CONTROL_SEQ;
create sequence DYEXTN_RULE_SEQ;
create sequence DYEXTN_FORM_CONTEXT_SEQ;
create sequence DYEXTN_DATA_ELEMENT_SEQ;
create sequence DYEXTN_VALUE_DOMAIN_SEQ;
create sequence DYEXTN_ENTITY_MAP_SEQ;
create sequence DE_ATTR_REC_SEQ;
create sequence DYEXTN_ENTITY_MAP_CONDN_SEQ;
create sequence DYEXTN_CONTAINER_SEQ;
create sequence DYEXTN_FILE_EXTN_SEQ;
create sequence DYEXTN_ASSO_DISPLAY_ATTR_SEQ;
create sequence DYEXTN_DATABASE_PROPERTIES_SEQ;
create sequence DYEXTN_ROLE_SEQ;
create sequence DYEXTN_TAGGED_VALUE_SEQ;
create sequence DYEXTN_ENTITY_RECORD_SEQ;
create sequence DE_COLL_ATTR_REC_VALUES_SEQ;
