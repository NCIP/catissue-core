create or replace FUNCTION sf_compSchemaTab_Diffcols
(

   pvchTablename        IN VARCHAR2,
   pvchSchema1      in varchar2,
   pvchSchema2      in varchar2
)
return varchar2
IS
asd varchar2(4000);
begin
  FOR c IN (select COLUMN_NAME as col1, DATA_TYPE, DATA_LENGTH, DATA_PRECISION,data_SCALE,  nullable
  fROM (
      select TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION , data_SCALE, nullable
          from DBA_TAB_COLS
       where table_name = pvchTablename
         and owner = pvchSchema1
  minus
     select TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION , data_SCALE, nullable
        from DBA_TAB_COLS
      where table_name = pvchTablename
      and owner =pvchSchema2
   )
 )
        LOOP


case
	 when c.DATA_TYPE  like '%CHAR%' then
	 	  asd:= asd || ' '||c.col1||' '|| lower(c.DATA_TYPE) ||'('||c.DATA_LENGTH  ||'),';
  	 when c.DATA_TYPE   in ('NUMBER', 'FLOAT')   then
	 	  case when c.data_SCALE >0 then
		     	asd:= asd || ' '||c.col1||' '|| lower(c.DATA_TYPE) ||'('||c.DATA_PRECISION || ','|| to_char(c.data_SCALE)  ||'),';
		  else
		     	asd:= asd || ' '||c.col1||' '|| lower(c.DATA_TYPE) ||'('||c.DATA_PRECISION || '),';
		  end case;
   	 when (c.DATA_TYPE   in ('DATE') or   c.DATA_TYPE    like '%TIME%'  ) then
	    	 	  asd:= asd || ' '||c.col1||' '|| lower(c.DATA_TYPE)||',' ;

	when c.DATA_TYPE    like '%LOB%' then
		    	 	  asd:= asd || ' '||c.col1||' '|| lower(c.DATA_TYPE) ||',';
		else
					asd:= asd || ' '||c.col1||' '|| lower(c.DATA_TYPE) ||',' ;

end 	case 		  ;

            DBMS_OUTPUT.put_line (asd);
        END LOOP;

            DBMS_OUTPUT.put_line (asd);
   return substr(asd, 0, length(asd)-1);



end;


CREATE OR REPLACE FUNCTION sf_compTabDataRowsDiffStruct
(

   pvchTablename        IN VARCHAR2,
   pvchSchema1      in varchar2, -- OLD
   pvchSchema2      in varchar2 -- new
)
return varchar2
IS
asd int :=0;
/* check if they have blob or clob fields*/

mycols varchar2(4000) := sf_compTabInschemasWDiffStruct(upper(pvchTablename), pvchSchema1, pvchSchema2);


lsql varchar(1000) :='Select Count(*) From (select ' || mycols ||' from '|| pvchSchema1||'.' ||pvchTablename ||' INTERSECT select '|| mycols ||' from ' || pvchSchema2 ||'.' || pvchTablename ||') A';
cols varchar2(10000);
lreturn varchar(200):= '';
begin
/*DBMS_OUTPUT.ENABLE(10000);*/

 DBMS_OUTPUT.PUT_LINE( 'vchSchemfgegwergwergwergis   -->'||lsql);
  DBMS_OUTPUT.PUT_LINE( 'vchSchemafrom is   -->'||ASD);
select  count(*) into asd
  From DBA_TAB_cols
  where TABLE_NAME = pvchTablename
     and owner in (pvchSchema1,pvchSchema2 )
     and DATA_TYPE  in ('CLOB', 'BLOB');

  DBMS_OUTPUT.PUT_LINE( 'vchSchemafrom is   -->'||ASD);

  DBMS_OUTPUT.PUT_LINE( 'vchSchemafrom is   -->'||lsql);
  if (asd = 0) or (ASD is null) then
    cols:= sf_compTabInschemasWDiffStruct(upper(pvchTablename), pvchSchema1, pvchSchema2);
   lsql :='Select Count(*) From (select '||mycols ||' from '|| pvchSchema1||'.' ||pvchTablename ||' INTERSECT select ' || mycols ||' from ' || pvchSchema2 ||'.' || pvchTablename ||') A';

     execute immediate lsql into asd;  -- 19373
  else
  	 lreturn:= 'CLOB/BLOB DATA Cannot compare ';
	 asd:=-1;
  end if ;

if ((asd= 0) or (asd is null  ) )   then
      return to_char(asd);
  else
      return lreturn || to_char(asd);
end if    ;
end ;
/


CREATE OR REPLACE FUNCTION sf_compTabInschemasWDiffStruct
(

   pvchTablename        IN VARCHAR2,
   pvchSchema1      in varchar2,
   pvchSchema2      in varchar2
)
return varchar2
IS
asd varchar(4000);
begin
  FOR c IN (select COLUMN_NAME as col1
  fROM (
      select TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION , data_SCALE, nullable
          from DBA_TAB_COLS
       where table_name = pvchTablename
         and owner = pvchSchema1
  intersect
     select TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION , data_SCALE, nullable
        from DBA_TAB_COLS
      where table_name = pvchTablename
      and owner =pvchSchema2
   )
 )
        LOOP
            DBMS_OUTPUT.put_line ('c.col1 in sf_compTabInschemasWDiffStruct '||c.col1);
   asd:= asd || c.col1||',';

        END LOOP;

            DBMS_OUTPUT.put_line ('asd in sf_compTabInschemasWDiffStruct last '||asd);
            DBMS_OUTPUT.put_line ('return  in sf_compTabInschemasWDiffStruct last '||substr(asd, 0, length(asd)-1));
   return substr(asd, 0, length(asd)-1);



end ;
/


CREATE OR REPLACE FUNCTION     sf_compTablesDataRows
(

   pvchTablename        IN VARCHAR2,
   pvchSchema1      in varchar2,
   pvchSchema2      in varchar2
)
return varchar2
IS
asd int :=0;
/* check if they have blob or clob fields*/





lsql varchar(1000) :='Select Count(*) From (select * from '|| pvchSchema1||'.' ||pvchTablename ||' INTERSECT select * from ' || pvchSchema2 ||'.' || pvchTablename ||') A';
cols varchar2(10000);
begin
/*DBMS_OUTPUT.ENABLE(10000);*/
	DBMS_OUTPUT.PUT_LINE( 'lsql is  sf_compTablesDataRows 1  -->'||lsql);
  DBMS_OUTPUT.PUT_LINE( 'vchSchemafrom is sf_compTablesDataRows 1   -->'||ASD);
select  count(*) into asd
  From DBA_TAB_cols
  where TABLE_NAME = pvchTablename
     and owner in (pvchSchema1,pvchSchema2 )
     and DATA_TYPE  in ('CLOB', 'BLOB');

  DBMS_OUTPUT.PUT_LINE( 'vchSchemafrom is sf_compTablesDataRows 2  -->'||ASD);

  DBMS_OUTPUT.PUT_LINE( 'asd is sf_compTablesDataRows -->'||asd);

  if (asd = 0) or (ASD is null) then
    cols:= sf_getcolumns(pvchTablename, pvchSchema1);
    DBMS_OUTPUT.PUT_LINE( 'cols: is sf_compTablesDataRows -->'||cols);
   lsql :='Select Count(*) From (select '||cols ||' from '|| pvchSchema1||'.' ||pvchTablename ||' INTERSECT select ' || cols ||' from ' || pvchSchema2 ||'.' || pvchTablename ||') A';
DBMS_OUTPUT.PUT_LINE( 'lsql is  sf_compTablesDataRows after last -->'||lsql);
     execute immediate lsql into asd;  -- 19373
  end if ;


return asd;
end ;
/


CREATE OR REPLACE FUNCTION     sf_compTablesInschemas
(

   pvchTablename        IN VARCHAR2,
   pvchSchema1      in varchar2,
   pvchSchema2      in varchar2
)
return varchar2
IS
asd int;
begin
select Count(*) into asd
FROM (

(select TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION , data_SCALE, nullable
          from DBA_TAB_COLS
       where table_name = pvchTablename
         and owner = pvchSchema1
  minus
     select TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION , data_SCALE, nullable
        from DBA_TAB_COLS
      where table_name = pvchTablename
      and owner =pvchSchema2)

      union all

       (select TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION , data_SCALE, nullable
          from DBA_TAB_COLS
       where table_name = pvchTablename
         and owner = pvchSchema2
  minus
     select TABLE_NAME, COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION , data_SCALE, nullable
        from DBA_TAB_COLS
      where table_name = pvchTablename
      and owner =pvchSchema1 )

    );

if asd > 0 then
return 'false';
else
return 'true';
end if ;



end ;
/


CREATE OR REPLACE FUNCTION     sf_getcolumns
(

   pvchTablename        IN VARCHAR2,
   pvchSchema1      in varchar2
)
return varchar2
is
cols varchar(2000):='';
begin

  FOR c IN (select COLUMN_NAME as col1  from DBA_TAB_COLS where table_name =pvchTablename  and owner = pvchSchema1)
  LOOP

    cols:= cols|| c.col1 || ',';


     END LOOP;
  cols:=substr(cols, 0, length(cols)-1);
  DBMS_OUTPUT.PUT_LINE(cols);
return cols;

end ;
/

create or replace FUNCTION     sf_compDataInTables
(

   pvchTablename        IN VARCHAR2,
   pvchSchema1      in varchar2,
   colSchema1       in varchar2,
   pvchSchema2      in varchar2,
   colSchema2       in varchar2
)
return varchar2
IS
asd int;
lsql varchar(1000) := 'Select Count(*) From ((select '||colSchema2 ||' from '|| pvchSchema2||'.' ||pvchTablename ||' minus select ' || colSchema1 ||' from '|| pvchSchema1 ||'.' || pvchTablename ||') union all '||
'(select '||colSchema1 ||' from '|| pvchSchema1||'.' ||pvchTablename ||' minus select ' || colSchema2 ||' from ' || pvchSchema2 ||'.' || pvchTablename ||'))A';
begin
execute immediate lsql into asd;
if asd > 0 then
return 'Data in the columns are different';
else
return 'Data in the columns are identical';
end if;
end;
/


