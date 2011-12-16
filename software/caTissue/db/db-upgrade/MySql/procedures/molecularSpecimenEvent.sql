drop procedure if exists   mole_Event_migrate;
//
CREATE  PROCEDURE   mole_Event_migrate()
Begin
  DECLARE counter integer default 0;
   DECLARE _stme TEXT;
  DECLARE _output2 TEXT default 'success' ;
  declare record_not_found integer default 0;
  declare form_context_id integer default 1;
  declare seq_ver long ;
  
  declare specimen_event_identifier integer ;
  declare specimen_id integer ;
  declare specimen_event_user_id integer ; 
  declare specimen_event_param_id integer ;
  declare specimen_comments varchar(100);
  declare specimen_timestamp DATE;
  declare Dyn_qi varchar(255);
  declare Dyn_img_Url varchar(255);
  declare dyn_LANE_NUMBER Text;
  declare dyn_GEL_NUMBER double;
  declare dyn_ABSORBANCE_AT_260 double;
  declare dyn_ABSORBANCE_AT_280 double;
  declare dyn_RATIO_28S_TO_18S double;
  declare query_text Text;
  declare query_text_form Text;
  declare event_name varchar (100) default 'MolecularSpecimenReviewParameters';
  #-----------------------@using parameter-----------------------------
  declare activitystatus Text;
  declare sp_id integer;
  declare des_reason Text default '';
  declare s_seq_var long default 1;
  declare qi Text;
  declare img_url Text;
  declare LANE_NUMBER Text;
  declare GEL_NUMBER  double;
  declare Ab260 double;
  declare Ab280 double;
  declare ratio double;
  #--------------------------------------------------------------------
  
  
 #-------------------------------------------------------------------
   declare mig_cursor cursor   for
    select spec.identifier,
           spec.specimen_id,
           spec.event_timestamp,
           spec.user_id,
           spec.comments,
           mol.GEL_IMAGE_URL,
           mol.QUALITY_INDEX,
           mol.LANE_NUMBER,
           mol.GEL_NUMBER,
           mol.ABSORBANCE_AT_260,
           mol.ABSORBANCE_AT_280,
           mol.RATIO_28S_TO_18S
      from  catissue_mol_spe_review_param mol,
           catissue_specimen_event_param spec,
         catissue_specimen se
	where
      mol.identifier = spec.identifier and spec.specimen_id=se.identifier;

     
    
      
  declare CONTINUE HANDLER for NOT FOUND SET record_not_found = 1;
  

  
  #-----------------------------query for form contex id--------------------------------------
  SELECT formContext.identifier into form_context_id FROM dyextn_abstract_form_context formContext 
              join dyextn_container dcontainer
              on formcontext.container_id = dcontainer.identifier
              join dyextn_entity ent
              on ent.identifier = dcontainer.abstract_entity_id
              join dyextn_abstract_metadata  meta
              on meta.name= event_name and meta.identifier = ent.identifier
              join dyextn_abstract_metadata  meta2
              on meta2.name = 'SpecimenEvents'
              join dyextn_entity_group eg
              on eg.identifier=meta2.identifier
              and eg.identifier  =  ent.ENTITY_GROUP_ID and formContext.Activity_Status='Active';
              
  #-----------------------------------calling function---------------------------------------------------------------        
              
              select   query_formation_mol(event_name) into query_text;
              select query_text;
              set @query_text_form := query_text;
              select @query_text_form;
              prepare stmt from @query_text_form;
              
  #------------------------------------------------------------------------------------------------------------------       
      open mig_cursor;

     
          
      itr: loop
      
      fetch mig_cursor into specimen_event_identifier,
                            specimen_id,
                            specimen_timestamp,
                            specimen_event_user_id,
                            specimen_comments,
                            Dyn_img_Url,
                            Dyn_qi,
                            dyn_LANE_NUMBER,
                            dyn_GEL_NUMBER,
                            dyn_ABSORBANCE_AT_260,
                            dyn_ABSORBANCE_AT_280,
                            dyn_RATIO_28S_TO_18S;
      if record_not_found then LEAVE itr;
      end if;
      
      
                       
      #-------------------------------------------------------------------
      INSERT IGNORE into    dyextn_abstract_record_entry
      (modified_date,activity_status,abstract_form_context_id)
      values (sysdate(),'Active',form_context_id);  
      #-------------------------------------------------------------------   
      select _output2;
      select max(identifier) into seq_ver from  dyextn_abstract_record_entry;
      select seq_ver;
      #-------------------------------------------------------------------     
      
      INSERT IGNORE into    catissue_action_app_rcd_entry(identifier)values(seq_ver);
      #select _output2;
      #-------------------------------------------------------------------
  
      INSERT IGNORE into    catissue_abstract_application
          (identifier,timestamp,user_details,comments)
      values(specimen_event_identifier,specimen_timestamp,specimen_event_user_id,specimen_comments);
      select _output2;
      #----------------------print tha all values ---------------------------------------------
       
        select  specimen_event_identifier,
                            specimen_id,
                            specimen_timestamp,
                            specimen_event_user_id,
                            specimen_comments;
       #-------------------------------------------------------------------
       
      INSERT IGNORE into    catissue_action_application
      (identifier,specimen_id,action_app_record_entry_id)
      values(specimen_event_identifier,specimen_id,seq_ver);
      #-------------------------------------------------------------------
      
   set @sp_id := specimen_event_identifier;
   set @activitystatus :='Active';

   set @s_seq_var :=seq_ver;
   set @qi:=Dyn_qi;
   set @img_url:=Dyn_img_Url;
   set @LANE_NUMBER :=dyn_LANE_NUMBER;
   set @GEL_NUMBER :=dyn_GEL_NUMBER;
   set @Ab260 :=dyn_ABSORBANCE_AT_260;
   set @Ab280 :=dyn_ABSORBANCE_AT_280;
   set @ratio :=dyn_RATIO_28S_TO_18S;
   execute stmt using @Ab260,@Ab280,@img_url,@GEL_NUMBER,@sp_id,@LANE_NUMBER,@qi,@ratio,@s_seq_var;
     
      
    set counter =counter+1;
    set _stme=counter;
    select _stme;

                           
    end loop;          
    close mig_cursor; 
    #------------------------------------------------------------------
    
    
end;
//
