declare
    i number :=1;
    sql_stmt varchar2(150);
begin
    select nvl(max(identifier),0)+1 into i from DYEXTN_ATTRIBUTE_TYPE_INFO;
    sql_stmt :='create sequence DYEX_ATTR_TYP_INFO_SEQ start with ' || i ;
    execute immediate sql_stmt;
end;