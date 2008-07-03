declare
    i number :=1; j number :=1;k number :=1;l number :=1;
    sql_stmt varchar2(150);
begin

    select nvl(max(identifier),0)+1 into i from DYEXTN_ATTRIBUTE_RECORD;
    sql_stmt :='create sequence DE_ATTR_REC_SEQ start with ' || i ;
    execute immediate sql_stmt;
    
    select nvl(max(identifier),0)+1 into i from DE_COLL_ATTR_RECORD_VALUES;
    sql_stmt :='create sequence DE_COLL_ATTR_REC_VALUES_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DE_FILE_ATTR_RECORD_VALUES;
    sql_stmt :='create sequence DE_FILE_ATTR_REC_VALUES_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DE_OBJECT_ATTR_RECORD_VALUES;
    sql_stmt :='create sequence DE_OBJECT_ATTR_REC_VALUES_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_ABSTRACT_METADATA;
    sql_stmt :='create sequence DYEXTN_ABSTRACT_METADATA_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_ASSO_DISPLAY_ATTR;
    sql_stmt :='create sequence DYEXTN_ASSO_DISPLAY_ATTR_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_ATTRIBUTE_TYPE_INFO;
    sql_stmt :='create sequence DYEXTN_ATTRIBUTE_TYPE_INFO_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_CONTAINER;
    sql_stmt :='create sequence DYEXTN_CONTAINER_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_CONTROL;
    sql_stmt :='create sequence DYEXTN_CONTROL_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_DATABASE_PROPERTIES;
    sql_stmt :='create sequence DYEXTN_DATABASE_PROPERTIES_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_DATA_ELEMENT;
    sql_stmt :='create sequence DYEXTN_DATA_ELEMENT_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_ENTITY_MAP_CONDNS;
    sql_stmt :='create sequence DYEXTN_ENTITY_MAP_CONDN_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_ENTITY_MAP;
    sql_stmt :='create sequence DYEXTN_ENTITY_MAP_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_ENTITY_MAP_RECORD;
    sql_stmt :='create sequence DYEXTN_ENTITY_RECORD_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_FILE_EXTENSIONS;
    sql_stmt :='create sequence DYEXTN_FILE_EXTN_SEQ start with ' || i ;
    execute immediate sql_stmt;
	
	select nvl(max(identifier),0)+1 into i from DYEXTN_FORM_CONTEXT;
    sql_stmt :='create sequence DYEXTN_FORM_CONTEXT_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_PERMISSIBLE_VALUE;
    sql_stmt :='create sequence DYEXTN_PERMISSIBLEVAL_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_ROLE;
    sql_stmt :='create sequence DYEXTN_ROLE_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_RULE_PARAMETER;
    sql_stmt :='create sequence DYEXTN_RULE_PARAMETER_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_RULE;
    sql_stmt :='create sequence DYEXTN_RULE_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_SEMANTIC_PROPERTY;
    sql_stmt :='create sequence DYEXTN_SEMANTIC_PROPERTY_SEQ start with ' || i ;
    execute immediate sql_stmt;
	
	select nvl(max(identifier),0)+1 into i from DYEXTN_TAGGED_VALUE;
    sql_stmt :='create sequence DYEXTN_TAGGED_VALUE_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_CADSR_VALUE_DOMAIN_INFO;
    sql_stmt :='create sequence DYEXTN_VALUE_DOMAIN_SEQ start with ' || i ;
    execute immediate sql_stmt;

	select nvl(max(identifier),0)+1 into i from DYEXTN_VIEW;
    sql_stmt :='create sequence DYEXTN_VIEW_SEQ start with ' || i ;
    execute immediate sql_stmt;
    
    select nvl(max(identifier),0)+1 into i from DYEXTN_ATTRIBUTE_TYPE_INFO;
    sql_stmt :='create sequence DYEX_ATTR_TYP_INFO_SEQ start with ' || i ;
    execute immediate sql_stmt;
 end;