create or replace
function query_formation_excol_mol(event_name in varchar2)
  RETURN varchar2 AS 
  
  
          qur varchar2(4000);
           attrib_name varchar2(200);
          tbl_name varchar (200);
          col_name varchar2 (400);

           
          accpt_col_name varchar2(1000);
          cnct_col_name varchar2(1000);
          accpt_attrib_name varchar2(1000);
          cnct_attrib_name varchar2(100);
          dyn_col_name_prt1 varchar2(1000);
          dyn_col_name_prt2 varchar2(1000);
          dyn_col_name varchar2(2000);
  
  
  
  
 
 
  
  
  
  
  cursor Event_migrate IS  Select db1.name tabelName,db2.name columnName,meta3.name as AttributeName 
  from dyextn_abstract_metadata meta1 join dyextn_entity_group eg1 
  on eg1.identifier = meta1.identifier and meta1.name like 'SpecimenEvents'  
  join dyextn_entity ent1 on ent1.entity_group_id =eg1.identifier  
  join dyextn_abstract_metadata meta2 on meta2.identifier = ent1.identifier 
  and meta2.name like event_name
  join DYEXTN_TABLE_PROPERTIES tab 
  on tab.abstract_entity_id = ent1.identifier  join dyextn_database_properties db1 
  on tab.identifier =db1.identifier  join dyextn_attribute abAttr1 on abAttr1.ENTIY_ID =  ent1.identifier  
  join dyextn_abstract_metadata meta3 on meta3.identifier = abAttr1.identifier  
  join DYEXTN_PRIMITIVE_ATTRIBUTE attr1 on attr1.identifier =  abAttr1.identifier 
  join dyextn_column_properties col1 on col1.PRIMITIVE_ATTRIBUTE_ID= attr1.identifier  
  join dyextn_database_properties db2 on db2.identifier = col1.identifier;
 
  begin
  
   open Event_migrate;
   
           LOOP
  

             FETCH Event_migrate INTO tbl_name,col_name,attrib_name;
              
               EXIT WHEN Event_migrate%NOTFOUND;
              cnct_col_name :=accpt_col_name||','|| col_name;
              accpt_col_name := cnct_col_name;
             
          
  
  end LOOP;
          
          CLOSE  Event_migrate;
          
  
          select SUBSTR(cnct_col_name,2)into accpt_col_name from dual;
          
           Select meta2.identifier into dyn_col_name_prt1
          from dyextn_abstract_metadata meta1 join dyextn_entity_group eg1 
          on eg1.identifier = meta1.identifier and meta1.name like 'Catissue Suite'  
          join dyextn_entity ent1 on ent1.entity_group_id =eg1.identifier  
          join dyextn_abstract_metadata meta2 on meta2.identifier = ent1.identifier 
          and meta2.name like 'edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry';
        
        
           Select meta2.identifier into dyn_col_name_prt2
          from dyextn_abstract_metadata meta1 join dyextn_entity_group eg1 
          on eg1.identifier = meta1.identifier and meta1.name like 'SpecimenEvents'  
          join dyextn_entity ent1 on ent1.entity_group_id =eg1.identifier  
          join dyextn_abstract_metadata meta2 on meta2.identifier = ent1.identifier 
          and meta2.name like event_name;
           dyn_col_name :='DYEXTN_AS_'||dyn_col_name_prt1||'_'||dyn_col_name_prt2;
           accpt_col_name :=accpt_col_name ||','|| dyn_col_name;
        
        
         
         qur :='insert into '||tbl_name||'('||accpt_col_name||')'||'values(:1, :2, :3, :4, :5, :6, :7, :8, :9)';
        
         
          
        
        return qur;
     end query_formation_excol_mol;   
  