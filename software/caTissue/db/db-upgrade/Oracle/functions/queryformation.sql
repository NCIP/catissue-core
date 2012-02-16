create or replace
FUNCTION Query_formation(event_name IN VARCHAR2)
RETURN VARCHAR2 
AS 
  tbl_name          VARCHAR2(2000); 
  col_name          VARCHAR2(4000); 
  attrib_name       VARCHAR2(2000); 
  qur               VARCHAR2(1000); 
  accpt_col_name    VARCHAR2(200); 
  cnct_col_name     VARCHAR2(200); 
  accpt_attrib_name VARCHAR2(200); 
  cnct_attrib_name  VARCHAR2(200); 
  dyn_col_name_prt1 VARCHAR2(200); 
  dyn_col_name_prt2 VARCHAR2(200); 
  dyn_col_name      VARCHAR2(255);
  attrib_bind       VARCHAR2(200);
  attrib_counter    INTEGER;
  
  CURSOR event_migrate IS 
 
    SELECT db1.NAME   tabelname, 
           db2.NAME   columnname, 
           meta3.NAME AS attributename 
    FROM   dyextn_abstract_metadata meta1 
           join dyextn_entity_group eg1 
             ON eg1.identifier = meta1.identifier 
                AND meta1.NAME LIKE 'SpecimenEvents' 
           join dyextn_entity ent1 
             ON ent1.entity_group_id = eg1.identifier 
           join dyextn_abstract_metadata meta2 
             ON meta2.identifier = ent1.identifier 
                AND meta2.NAME LIKE event_name 
           join dyextn_table_properties tab 
             ON tab.abstract_entity_id = ent1.identifier 
           join dyextn_database_properties db1 
             ON tab.identifier = db1.identifier 
           join dyextn_attribute abattr1 
             ON abattr1.entiy_id = ent1.identifier 
           join dyextn_abstract_metadata meta3 
             ON meta3.identifier = abattr1.identifier 
           join dyextn_primitive_attribute attr1 
             ON attr1.identifier = abattr1.identifier 
           join dyextn_column_properties col1 
             ON col1.primitive_attribute_id = attr1.identifier 
           join dyextn_database_properties db2 
             ON db2.identifier = col1.identifier
             order by meta3.NAME; 
BEGIN 
  OPEN event_migrate; 
attrib_counter :=0;
  LOOP 
      FETCH event_migrate INTO tbl_name, col_name, attrib_name; 

      EXIT WHEN event_migrate%notfound; 

      cnct_col_name := accpt_col_name 
                       ||',' 
                       || col_name; 

      accpt_col_name := cnct_col_name; 
      
      attrib_counter :=attrib_counter+1;
      cnct_attrib_name := attrib_bind
                          ||',:'
                         ||attrib_counter;
                         
      attrib_bind := cnct_attrib_name;
  END LOOP; 

  CLOSE event_migrate; 

SELECT Substr(attrib_bind, 2) 
  INTO   attrib_bind 
  FROM   dual; 
  
  SELECT Substr(cnct_col_name, 2) 
  INTO   accpt_col_name 
  FROM   dual; 

  SELECT meta2.identifier 
  INTO   dyn_col_name_prt1 
  FROM   dyextn_abstract_metadata meta1 
         join dyextn_entity_group eg1 
           ON eg1.identifier = meta1.identifier 
              AND meta1.NAME LIKE 'Catissue Suite' 
         join dyextn_entity ent1 
           ON ent1.entity_group_id = eg1.identifier 
         join dyextn_abstract_metadata meta2 
           ON meta2.identifier = ent1.identifier 
              AND meta2.NAME LIKE 
  'edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry'; 

  SELECT meta2.identifier 
  INTO   dyn_col_name_prt2 
  FROM   dyextn_abstract_metadata meta1 
         join dyextn_entity_group eg1 
           ON eg1.identifier = meta1.identifier 
              AND meta1.NAME LIKE 'SpecimenEvents' 
         join dyextn_entity ent1 
           ON ent1.entity_group_id = eg1.identifier 
         join dyextn_abstract_metadata meta2 
           ON meta2.identifier = ent1.identifier 
              AND meta2.NAME LIKE event_name; 

  dyn_col_name := 'DYEXTN_AS_' 
                  ||dyn_col_name_prt1 
                  ||'_' 
                  ||dyn_col_name_prt2; 

  accpt_col_name := accpt_col_name 
                    ||',' 
                    || dyn_col_name; 
  
   attrib_counter :=attrib_counter+1;
   
  attrib_bind := attrib_bind
                   ||',:'
                  ||attrib_counter;
                  
  qur := 'insert into ' 
         ||tbl_name 
         ||'(' 
         ||accpt_col_name 
         ||')' 
         ||'values('
         ||attrib_bind
         ||')'; 

  RETURN qur; 
END Query_formation;