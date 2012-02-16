drop function if exists query_formation;
//
CREATE FUNCTION query_formation(event_name VARCHAR (255)) 
RETURNS TEXT 
BEGIN 
  DECLARE qur TEXT; 

  DECLARE attrib_name TEXT; 

  DECLARE tbl_name TEXT; 

  DECLARE col_name TEXT; 

  DECLARE accpt_col_name TEXT DEFAULT ''; 

  DECLARE cnct_col_name TEXT; 

  DECLARE accpt_attrib_name TEXT DEFAULT ''; 

  DECLARE attrib_bind TEXT DEFAULT ''; 

  DECLARE cnct_attrib_name TEXT; 

  DECLARE record_not_found INTEGER DEFAULT 0; 

  DECLARE dyn_col_name_prt1 TEXT; 

  DECLARE dyn_col_name_prt2 TEXT; 

  DECLARE dyn_col_name TEXT; 

  DECLARE event_migrate CURSOR FOR 
    SELECT db1.name   tabelname, 
           db2.name   columnname, 
           meta3.name AS attributename 
    FROM   dyextn_abstract_metadata meta1 
           JOIN dyextn_entity_group eg1 
             ON eg1.identifier = meta1.identifier 
                AND meta1.name LIKE 'SpecimenEvents' 
           JOIN dyextn_entity ent1 
             ON ent1.entity_group_id = eg1.identifier 
           JOIN dyextn_abstract_metadata meta2 
             ON meta2.identifier = ent1.identifier 
                AND meta2.name LIKE event_name 
           JOIN dyextn_table_properties tab 
             ON tab.abstract_entity_id = ent1.identifier 
           JOIN dyextn_database_properties db1 
             ON tab.identifier = db1.identifier 
           JOIN dyextn_attribute abattr1 
             ON abattr1.entiy_id = ent1.identifier 
           JOIN dyextn_abstract_metadata meta3 
             ON meta3.identifier = abattr1.identifier 
           JOIN dyextn_primitive_attribute attr1 
             ON attr1.identifier = abattr1.identifier 
           JOIN dyextn_column_properties col1 
             ON col1.primitive_attribute_id = attr1.identifier 
           JOIN dyextn_database_properties db2 
             ON db2.identifier = col1.identifier 
    ORDER  BY attributename; 

  DECLARE CONTINUE HANDLER 
  FOR NOT FOUND 
    SET record_not_found = 1; 

  OPEN event_migrate; 

  READ_LOOP: 
LOOP 
    FETCH event_migrate INTO tbl_name, col_name, attrib_name; 
    IF record_not_found THEN 
      LEAVE read_loop; 
    END IF; 
    SET cnct_col_name=concat(accpt_col_name, ',', col_name); 
    SET accpt_col_name=cnct_col_name; 
    SET cnct_attrib_name=concat(accpt_attrib_name, ',', attrib_name); 
    SET accpt_attrib_name=cnct_attrib_name; 
    SET attrib_bind=concat(attrib_bind, ',', '?'); 
  END LOOP; 

  CLOSE event_migrate; 

  SELECT SUBSTRING(cnct_col_name, 2) 
  INTO   accpt_col_name; 

  SELECT SUBSTRING(attrib_bind, 2) 
  INTO   attrib_bind; 

  SELECT meta2.identifier 
  INTO   dyn_col_name_prt1 
  FROM   dyextn_abstract_metadata meta1 
         JOIN dyextn_entity_group eg1 
           ON eg1.identifier = meta1.identifier 
              AND meta1.name LIKE 'Catissue Suite' 
         JOIN dyextn_entity ent1 
           ON ent1.entity_group_id = eg1.identifier 
         JOIN dyextn_abstract_metadata meta2 
           ON meta2.identifier = ent1.identifier 
              AND meta2.name LIKE 
  'edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry'; 

  SELECT meta2.identifier 
  INTO   dyn_col_name_prt2 
  FROM   dyextn_abstract_metadata meta1 
         JOIN dyextn_entity_group eg1 
           ON eg1.identifier = meta1.identifier 
              AND meta1.name LIKE 'SpecimenEvents' 
         JOIN dyextn_entity ent1 
           ON ent1.entity_group_id = eg1.identifier 
         JOIN dyextn_abstract_metadata meta2 
           ON meta2.identifier = ent1.identifier 
              AND meta2.name LIKE event_name; 

  SET dyn_col_name :=concat('DYEXTN_AS_', dyn_col_name_prt1, '_', 
  dyn_col_name_prt2); 

  SET accpt_col_name =concat(accpt_col_name, ',', dyn_col_name); 

  SET attrib_bind=concat(attrib_bind, ',', '?'); 

  SET @qur :=concat('insert into ', tbl_name, '(', accpt_col_name, ')', 
  'values(', attrib_bind, ')'); 

  RETURN ( @qur ); 
END  
//
