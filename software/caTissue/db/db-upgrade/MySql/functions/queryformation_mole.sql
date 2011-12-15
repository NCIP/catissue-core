drop function if exists query_formation_mol;
//
create function query_formation_mol(event_name varchar (255))
  RETURNS Text
  
  begin
  
  declare qur Text;
  declare attrib_name Text;
  declare tbl_name Text;
  declare col_name Text;
  declare accpt_col_name Text default '';
  declare cnct_col_name Text;
  declare accpt_attrib_name Text default '';
  declare cnct_attrib_name Text;
  declare record_not_found integer default 0;
  declare dyn_col_name_prt1 Text;
  declare dyn_col_name_prt2 Text;
  declare dyn_col_name Text;
  
  
  
   declare Event_migrate cursor for  Select db1.name tabelName,db2.name columnName,meta3.name as AttributeName 
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
  join dyextn_database_properties db2 on db2.identifier = col1.identifier  order by AttributeName;
  
  
  declare CONTINUE HANDLER for NOT FOUND SET record_not_found = 1;
  
  
  
   open Event_migrate;
   
            read_loop: loop
  

             fetch Event_migrate into tbl_name,col_name,attrib_name;
   
 
             if record_not_found then LEAVE read_loop;
             end if;
    
             set cnct_col_name=concat(accpt_col_name,',',col_name);
             set accpt_col_name=cnct_col_name;
             
             set cnct_attrib_name=concat(accpt_attrib_name,',',attrib_name);
             set accpt_attrib_name=cnct_attrib_name;
             
           
  
  end loop;
          
          close  Event_migrate;
  
          select SUBSTRING(cnct_col_name,2)into accpt_col_name;
          #-------------------------part 1 output 1966--------------------------
          Select meta2.identifier into dyn_col_name_prt1
          from dyextn_abstract_metadata meta1 join dyextn_entity_group eg1 
          on eg1.identifier = meta1.identifier and meta1.name like 'Catissue Suite'  
          join dyextn_entity ent1 on ent1.entity_group_id =eg1.identifier  
          join dyextn_abstract_metadata meta2 on meta2.identifier = ent1.identifier 
          and meta2.name like 'edu.wustl.catissuecore.domain.deintegration.ActionApplicationRecordEntry';
          
          
          #-------------------------part 1 output passing event name --------------------------
          Select meta2.identifier into dyn_col_name_prt2
          from dyextn_abstract_metadata meta1 join dyextn_entity_group eg1 
          on eg1.identifier = meta1.identifier and meta1.name like 'SpecimenEvents'  
          join dyextn_entity ent1 on ent1.entity_group_id =eg1.identifier  
          join dyextn_abstract_metadata meta2 on meta2.identifier = ent1.identifier 
          and meta2.name like event_name ;
          set dyn_col_name :=concat('DYEXTN_AS_',dyn_col_name_prt1,'_',dyn_col_name_prt2);
          set accpt_col_name =concat(accpt_col_name,',',dyn_col_name);
    
          
         set @qur :=concat('insert into ',tbl_name,'(',accpt_col_name,')','values(?,?,?,?,?,?,?,?,?)');
         
        
        
        return (@qur);
     end;
//