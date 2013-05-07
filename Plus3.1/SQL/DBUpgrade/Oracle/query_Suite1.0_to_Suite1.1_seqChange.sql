-- shifted query_seq off constraint_seq 
declare
    i number :=1;
    sql_stmt varchar2(50);
begin
	select nvl(max(identifier)+1,1) into i from query;
	sql_stmt :='create sequence QUERY_SEQ start with ' || i ;
	execute immediate sql_stmt;
end;
/