drop procedure if exists   Create_spp_events;
//
create procedure   Create_spp_events() 
  
  Begin   
	
declare coll_event_name varchar (100) default 'CollectionEventParameters';
declare rec_event_name varchar (100) default 'ReceivedEventParameters';
declare coll_query_text Text;
  declare coll_query_text_form Text;
  declare rec_query_text Text;
  declare rec_query_text_form Text;
   DECLARE counter integer default 0;
DECLARE _stme integer default 0;

  #-----------------------@using parameter-----------------------------
  declare activitystatus Text;
  declare sp_id integer;
  declare des_reason Text default '';
  declare s_seq_var long default 1;
  declare COLLECTIONl Text;
  declare CONT Text;
  #--------------------------------------------------------------------
   declare specReq_ID integer;
   declare cpe_ID integer;
   declare coll_event_id integer;
   declare rec_event_id integer;
   declare flag integer ;
   declare spp_name varchar(100);
   declare record_not_found integer default 0;
   declare coll_container_id integer default 0;
   declare rec_container_id integer default 0;
   declare max_id integer default 0;
   declare id_for_spp integer default 0;
   declare spp_id integer default 0;
   declare spp_de_id  integer default 0;
   declare COLLECTION_PROCEDURE varchar(200);
   declare CONTAINER varchar(200);
   declare RECEIVED_QUALITY varchar(200);
   declare rec_entry_id integer default 0;
   declare comments varchar(2000);
   declare event_timestamp varchar(200);
   declare event_user_id integer default 0;
#--------Inserting the event data into temp table--------


    
   
#-----------------------------------------------------------------------
      
   declare spp_create_cursor cursor   for
    select spp.identifier, spp.collection_procedure, spp.container, spp.received_quality
    from temp_spp_events spp where identifier is not null;
  
	declare CONTINUE HANDLER for NOT FOUND SET record_not_found = 1;
  #-----------------------------------calling function for collection event---------------------------------------------------------------        
              
              select  query_formation(coll_event_name) into coll_query_text;
              select coll_query_text;
              set @coll_query_text_form := coll_query_text;
              select @coll_query_text_form;
              prepare coll_stmt from @coll_query_text_form;

#-----------------------------------calling function for received event---------------------------------------------------------------        
              
              select   query_formation(rec_event_name) into rec_query_text;
              select rec_query_text;
              set @rec_query_text_form := rec_query_text;
              select @rec_query_text_form;
              prepare rec_stmt from @rec_query_text_form;
  
   set counter :=1;
  #------------------------------------------------------------------------------------------------------------------       
      open spp_create_cursor;
    select 'open cursor';
     itr: LOOP

     fetch spp_create_cursor into cpe_ID,COLLECTION_PROCEDURE,CONTAINER,RECEIVED_QUALITY;

     if record_not_found then LEAVE itr;
      select 'no record';
      end if;
      select COLLECTION_PROCEDURE;
      select CONTAINER;
      select 'test';
    SELECT 'Hello World!';
       #-----------------------------------------------------------------
#--Insert entry into caTissue_SPP table
	SELECT 'Hello World3!';
	select IFNULL(max(cast(identifier as SIGNED)),0)+1 into max_id from catissue_spp;
	select max_id;
	select CONCAT('migrated_spp_',max_id) into spp_name from dual;
	insert into catissue_spp(IDENTIFIER,NAME,BARCODE) values(max_id,spp_name,null);
	

#-- selecting the container Id for collection event

       SELECT 'Hello World2!';
       select con.identifier into coll_container_id from dyextn_container con, dyextn_entity ent where con.caption like 'CollectionEventParameters' 
	and con.abstract_entity_id=ent.identifier and ent.entity_group_id in 
	(select identifier from dyextn_entity_group where Long_name like 'SpecimenEvents');
	
#--Insert entry in abstract Form context for collecion events
	select IFNULL(max(cast(identifier as SIGNED)),0)+1 into max_id from dyextn_abstract_form_context;
			      
	Insert into dyextn_abstract_form_context(IDENTIFIER,FORM_LABEL,CONTAINER_ID,HIDE_FORM,ACTIVITY_STATUS) values(max_id,null,coll_container_id,0,'Active');
	select IFNULL(max(cast(identifier as SIGNED)),0) into spp_id from catissue_spp;
	select IFNULL(max(cast(identifier as SIGNED)),0) into spp_de_id from dyextn_abstract_form_context;
	select spp_id;
	
#-- Insert default values for Collection event

	INSERT IGNORE into   dyextn_abstract_record_entry
      (modified_date,activity_status,abstract_form_context_id)
      values (sysdate(),'Active',spp_de_id);  
      
      select max(identifier) into rec_entry_id from dyextn_abstract_record_entry;
      
      INSERT IGNORE into   catissue_action_app_rcd_entry(identifier)values(rec_entry_id);
      Insert IGNORE into catissue_action(IDENTIFIER,BARCODE,ACTION_ORDER,ACTION_APP_RECORD_ENTRY_ID,SPP_IDENTIFIER,UNIQUE_ID,IS_SKIPPED) 
	values(spp_de_id,null,1,rec_entry_id,spp_id,1,0);
      
	
	select IFNULL(max(cast(identifier as SIGNED)),0)+1 into max_id from catissue_specimen_event_param;
	
     set @sp_id := max_id+counter;
     set @activitystatus :='Active';
    
     set @s_seq_var :=rec_entry_id;
     set @COLLECTIONl:= COLLECTION_PROCEDURE;
     set @CONT:=CONTAINER;
     
    
      execute coll_stmt using @COLLECTIONl,@CONT,@sp_id,@s_seq_var;
      
      
      

#-- selecting the container Id for Recieved event

 
	       SELECT 'Hello World2!';
	       select con.identifier into rec_container_id from dyextn_container con, dyextn_entity ent where con.caption like 'ReceivedEventParameters' 
	and con.abstract_entity_id=ent.identifier and ent.entity_group_id in 
	(select identifier from dyextn_entity_group where Long_name like 'SpecimenEvents');
		
#--Insert entry in abstract Form context for Recieved event
	select IFNULL(max(cast(identifier as SIGNED)),0)+1 into max_id from dyextn_abstract_form_context;
			      
	Insert into dyextn_abstract_form_context(IDENTIFIER,FORM_LABEL,CONTAINER_ID,HIDE_FORM,ACTIVITY_STATUS) values(max_id,null,rec_container_id,0,'Active');
	select IFNULL(max(cast(identifier as SIGNED)),0) into spp_id from catissue_spp;
	select IFNULL(max(cast(identifier as SIGNED)),0) into spp_de_id from dyextn_abstract_form_context;
	select spp_id;
	
	
#-- Insert default values for Received event
	INSERT IGNORE into   dyextn_abstract_record_entry
      (modified_date,activity_status,abstract_form_context_id)
      values (sysdate(),'Active',spp_de_id);
      
      select max(identifier) into rec_entry_id from dyextn_abstract_record_entry;
      
      INSERT IGNORE into   catissue_action_app_rcd_entry(identifier)values(rec_entry_id);
      Insert IGNORE into catissue_action(IDENTIFIER,BARCODE,ACTION_ORDER,ACTION_APP_RECORD_ENTRY_ID,SPP_IDENTIFIER,UNIQUE_ID,IS_SKIPPED) 
	values(spp_de_id,null,2,rec_entry_id,spp_id,2,0);
      
      
	
	select IFNULL(max(cast(identifier as SIGNED)),0)+1 into max_id from catissue_abstract_application;
      
set counter =counter+1;
   set @sp_id := max_id+counter;
   set @activitystatus :='Active';
   set @des_reason := RECEIVED_QUALITY;
   set @s_seq_var :=rec_entry_id;
  execute rec_stmt using @sp_id,@des_reason,@s_seq_var;  
   
  select max(identifier) into max_id from catissue_spp;
  update temp_spp_events set spp_id = max_id where identifier = cpe_ID;
                               
     set counter =counter+1;
    set _stme=counter;
    select _stme;
    commit;
END loop;
    close spp_create_cursor;  
  
end;
//

