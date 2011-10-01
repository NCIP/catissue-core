Declare
w_table varchar2(30);
sql_text varchar2(256); 
Dynamic_Cursor integer; 
dummy integer;
cursor droptable is
select table_name from user_tables where table_name like 'TEMP%';
BEGIN
open droptable;
fetch droptable into w_table;
while droptable%FOUND loop
Dynamic_Cursor := dbms_sql.open_cursor;
sql_text := 'drop table '||w_table;
dbms_sql.parse(Dynamic_Cursor, sql_text, dbms_sql.native);
dummy := dbms_sql.execute(Dynamic_Cursor);
dbms_sql.close_cursor(Dynamic_Cursor);
fetch droptable into w_table;
end loop;
close droptable;
commit;
END;
/
