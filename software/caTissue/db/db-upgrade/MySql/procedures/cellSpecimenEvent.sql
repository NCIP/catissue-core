DROP PROCEDURE IF EXISTS   cell_call_parameter;
//
CREATE  PROCEDURE   cell_call_parameter()
BEGIN
  DECLARE counter INTEGER DEFAULT 0;
   DECLARE _stme TEXT;
  DECLARE _output2 TEXT DEFAULT 'success' ;
  DECLARE record_not_found INTEGER DEFAULT 0;
  DECLARE form_context_id INTEGER DEFAULT 1;
  DECLARE seq_ver LONG ;
  
  DECLARE specimen_event_identifier INTEGER ;
  DECLARE specimen_id INTEGER ;
  DECLARE specimen_event_user_id INTEGER ; 
  DECLARE specimen_event_param_id INTEGER ;
  DECLARE specimen_comments VARCHAR(100);
  DECLARE specimen_timestamp DATE;
  DECLARE Dyn_col_veriable DOUBLE;
  DECLARE Dyn_col_veriable1 DOUBLE;
  DECLARE query_text TEXT;
  DECLARE query_text_form TEXT;
  DECLARE event_name VARCHAR (100); 
  #-----------------------@using parameter-----------------------------
  DECLARE activitystatus TEXT;
  DECLARE sp_id INTEGER;

  DECLARE s_seq_var LONG DEFAULT 1;
  DECLARE neo DOUBLE;
  DECLARE via DOUBLE;
  #--------------------------------------------------------------------
  
  
 #-------------------------------------------------------------------
  DECLARE mig_cursor  CURSOR FOR 
    SELECT spec.identifier,
           spec.specimen_id,
           spec.event_timestamp,
           spec.user_id,
           spec.comments,
           cell.NEOPLASTIC_CELLULARITY_PER,
           cell.VIABLE_CELL_PERCENTAGE
      FROM   catissue_cell_spe_review_param cell,
        catissue_specimen_event_param spec
	WHERE
      cell.identifier = spec.identifier;
     
     
    
      
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET record_not_found = 1;
  
  Set event_name :='CellSpecimenReviewParameters';
  
  #-----------------------------query for form contex id--------------------------------------
  SELECT formContext.identifier INTO form_context_id FROM dyextn_abstract_form_context formContext 
              JOIN dyextn_container dcontainer
              ON formcontext.container_id = dcontainer.identifier
              JOIN dyextn_entity ent
              ON ent.identifier = dcontainer.abstract_entity_id
              JOIN dyextn_abstract_metadata  meta
              ON meta.name= event_name AND meta.identifier = ent.identifier
              JOIN dyextn_abstract_metadata  meta2
              ON meta2.name = 'SpecimenEvents'
              JOIN dyextn_entity_group eg
              ON eg.identifier=meta2.identifier
              AND eg.identifier  =  ent.ENTITY_GROUP_ID AND formContext.Activity_Status='Active';
              
  #-----------------------------------calling function---------------------------------------------------------------        
              
              SELECT   query_formation(event_name) INTO query_text;
              SELECT query_text;
              SET @query_text_form := query_text;
              SELECT @query_text_form;
              PREPARE stmt FROM @query_text_form;
              
  #------------------------------------------------------------------------------------------------------------------       
      OPEN mig_cursor;

     
          
      itr: LOOP
      
      FETCH mig_cursor INTO specimen_event_identifier,
                            specimen_id,
                            specimen_timestamp,
                            specimen_event_user_id,
                            specimen_comments,
                            Dyn_col_veriable,
                            Dyn_col_veriable1;
      IF record_not_found THEN LEAVE itr;
      END IF;
      
      
                       
      #-------------------------------------------------------------------
      INSERT IGNORE INTO   dyextn_abstract_record_entry
      (modified_date,activity_status,abstract_form_context_id)
      VALUES (SYSDATE(),'Active',form_context_id);  
      #-------------------------------------------------------------------   
      SELECT _output2;
      SELECT MAX(identifier) INTO seq_ver FROM   dyextn_abstract_record_entry;
      SELECT seq_ver;
      #-------------------------------------------------------------------     
      
      INSERT IGNORE INTO   catissue_action_app_rcd_entry(identifier)VALUES(seq_ver);
      #select _output2;
      #-------------------------------------------------------------------
  
      INSERT IGNORE INTO   catissue_abstract_application
          (identifier,TIMESTAMP,user_details,comments)
      VALUES(specimen_event_identifier,specimen_timestamp,specimen_event_user_id,specimen_comments);
      SELECT _output2;
      #----------------------print tha all values ---------------------------------------------
       
        SELECT  specimen_event_identifier,
                            specimen_id,
                            specimen_timestamp,
                            specimen_event_user_id,
                            specimen_comments;
       #-------------------------------------------------------------------
       
      INSERT IGNORE INTO   catissue_action_application
      (identifier,specimen_id,action_app_record_entry_id)
      VALUES(specimen_event_identifier,specimen_id,seq_ver);
      #-------------------------------------------------------------------
      
   SET @sp_id := specimen_event_identifier;
   SET @activitystatus :='Active';
   SET @via := Dyn_col_veriable;
   SET @neo :=Dyn_col_veriable1;
   SET @s_seq_var :=seq_ver;
   EXECUTE stmt USING @neo,@via,@sp_id,@s_seq_var;     
     
      
    SET counter =counter+1;
    SET _stme=counter;
    SELECT _stme;

                           
    END LOOP;          
    CLOSE mig_cursor; 
    #------------------------------------------------------------------
    
    
END
//