declare
    i number :=1; j number :=1;k number :=1;l number :=1;
    sql_stmt varchar2(50);
begin
    select nvl(max(identifier)+1,1) into i from dyextn_permissible_value;
    sql_stmt :='create sequence DYEXTN_PERMISSIBLEVAL_SEQ start with ' || i ;
    execute immediate sql_stmt;
	
	 select nvl(max(identifier)+1,1) into j from DYEXTN_DATABASE_PROPERTIES ;
    execute immediate 'create sequence DYEXTN_DATABASE_PROPERTIES_SEQ  start with ' || j ; 
	
	 select nvl(max(identifier)+1,1) into k from DYEXTN_ATTRIBUTE_TYPE_INFO;
    execute immediate 'create sequence DYEXTN_ATTRIBUTE_TYPE_INFO_SEQ start with ' || k ; 
	
	 select nvl(max(identifier)+1,1) into l  from DYEXTN_ABSTRACT_METADATA;
    execute immediate 'create sequence DYEXTN_ABSTRACT_METADATA_SEQ start with ' || l ; 
	
	 select nvl(max(identifier)+1,1) into i from DYEXTN_SEMANTIC_PROPERTY;
    execute immediate 'create sequence DYEXTN_SEMANTIC_PROPERTY_SEQ start with ' || i ;
	
	select nvl(max(identifier)+1,1) into j from DYEXTN_ROLE;
    execute immediate 'create sequence DYEXTN_ROLE_SEQ start with ' || j ;    
   
	select nvl(max(identifier)+1,1) into k from DYEXTN_DATA_ELEMENT;
    execute immediate 'create sequence DYEXTN_DATA_ELEMENT_SEQ start with ' || k ;    
   
	select nvl(max(identifier)+1,1) into l from DYEXTN_TAGGED_VALUE;
    execute immediate 'create sequence DYEXTN_TAGGED_VALUE_SEQ start with ' || l ;
   
	end;