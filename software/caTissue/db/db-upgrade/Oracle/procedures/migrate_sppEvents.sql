create or replace
PROCEDURE SPP_EVENT_MIGRATE AS
	

	coll_event_name varchar (100) := 'CollectionEventParameters';
	rec_event_name varchar (100) := 'ReceivedEventParameters';
	 coll_query_text varchar(1000);
   coll_query_text_form varchar(1000);
   rec_query_text varchar(1000);
   rec_query_text_form varchar(1000);
    counter integer;
 stme integer;

  -----------------------@using parameter-----------------------------
   activitystatus varchar(1000);
   sp_id integer;
   des_reason varchar(1000);
   s_seq_var integer;
   COLLECTIONl varchar(1000);
   CONT varchar(1000);
  --------------------------------------------------------------------
    specReq_ID integer;
    cpe_ID integer;
    coll_event_id integer :=0;
    rec_event_id integer :=0;
    flag integer ;
    spp_name varchar(100);
    record_not_found integer;
    coll_container_id integer;
    rec_container_id integer;
    max_id integer;
    id_for_spp integer;
    spp_id integer;
    spp_de_id  integer;
    COLLECTION_PROCEDURE varchar(200);
    CONTAINER varchar(200);
    RECEIVED_QUALITY varchar(200);
    rec_entry_id integer;
    comments varchar(2000);
    event_timestamp timestamp;
    event_user_id integer;
    v_code  NUMBER; 
v_errm  VARCHAR2(64);
    -------------------------DECLARE CURSOR----------------------------------------------
      
   cursor spp_mig_cursor IS
    select cpe.identifier, specReq.identifier  
    from catissue_coll_prot_event cpe, catissue_cp_req_specimen specReq
	where specReq.COLLECTION_PROTOCOL_EVENT_ID = cpe.identifier;
	
    BEGIN
	    -----------------------------------calling function for collection event---------------------------------------------------------------        
              
              select  query_formation_excol(coll_event_name) into coll_query_text from dual;
              

-----------------------------------calling function for received event---------------------------------------------------------------        
              
              select   query_formation(rec_event_name) into rec_query_text from dual;
              
               open spp_mig_cursor;
               loop
               
               fetch spp_mig_cursor into cpe_ID, specReq_ID;
                EXIT WHEN  spp_mig_cursor%NOTFOUND;
      -------------
      -------Insert entry into caTissue_SPP table
	
	select CATISSUE_SPP_SEQ.NEXTVAL into spp_id from dual;
        select CONCAT('migrated_spp_',spp_id) into spp_name from dual;
	insert into catissue_spp(IDENTIFIER,NAME,BARCODE) values(spp_id,spp_name,null);
	
	-------getting the collection events
       select identifier into coll_event_id from catissue_coll_event_param where identifier in
	( select identifier from CATISSUE_SPECIMEN_EVENT_PARAM where specimen_id in (
	select identifier from catissue_cp_req_specimen where identifier = specReq_ID));

----selecting the container Id for collection event

       IF (coll_event_id != 0) THEN 
       begin
       select con.identifier into coll_container_id from dyextn_container con, dyextn_entity ent where con.caption like 'CollectionEventParameters' 
       and con.abstract_entity_id=ent.identifier and ent.entity_group_id in 
	    (select identifier from dyextn_entity_group where Long_name like 'SpecimenEvents');
	
----Insert entry in abstract Form context for collecion events
			      
          Insert into dyextn_abstract_form_context(IDENTIFIER,FORM_LABEL,CONTAINER_ID,HIDE_FORM,ACTIVITY_STATUS) 
          values(DYEXTN_ABSTRACT_FRM_CTXT_SEQ.NEXTVAL,null,coll_container_id,0,'Active');
	
-----Insert default values for Collection event

             INSERT into dyextn_abstract_record_entry (IDENTIFIER,modified_date,activity_status,abstract_form_context_id)
             values (DYEXTN_ABSTRACT_RE_SEQ.NEXTVAL,sysdate(),'Active',DYEXTN_ABSTRACT_FRM_CTXT_SEQ.CURRVAL);  
      
            INSERT into catissue_action_app_rcd_entry(identifier)values(DYEXTN_ABSTRACT_RE_SEQ.CURRVAL);
      
            INSERT into catissue_action(IDENTIFIER,BARCODE,ACTION_ORDER,ACTION_APP_RECORD_ENTRY_ID,SPP_IDENTIFIER,UNIQUE_ID,IS_SKIPPED) 
            values(DYEXTN_ABSTRACT_FRM_CTXT_SEQ.CURRVAL,null,1,DYEXTN_ABSTRACT_RE_SEQ.CURRVAL,spp_id,1,0);
	
           select coll.COLLECTION_PROCEDURE,coll.CONTAINER,event.comments,event.user_id, event.event_timestamp into 
           COLLECTION_PROCEDURE,CONTAINER,comments,event_user_id,event_timestamp 
            from catissue_coll_event_param coll, CATISSUE_SPECIMEN_EVENT_PARAM event 
            where event.specimen_id = specReq_ID and coll.identifier =event.identifier;
	
        select DYEXTN_ABSTRACT_RE_SEQ.CURRVAL into rec_entry_id from dual;
      	select CATISSUE_ABS_APPL_SEQ.NEXTVAL into max_id from dual;
      
      coll_query_text_form :='insert into CATISSUE_ABSTRACT_APPLICATION(identifier,timestamp,user_details,comments)
     values(:1, :2, :3, :4)';
     execute immediate coll_query_text_form using 
     max_id,event_timestamp,event_user_id,comments;
      
      INSERT into catissue_action_application (identifier,action_app_record_entry_id)
      values(max_id,rec_entry_id);
      
      
      EXECUTE IMMEDIATE coll_query_text using 
      COLLECTION_PROCEDURE, CONTAINER,max_id, rec_entry_id;
      NULL; 
        EXCEPTION WHEN OTHERS THEN 
        v_code := SQLCODE; 
        v_errm := SUBSTR(SQLERRM, 1, 1000);
        DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm||' '||counter ); 
        end; 
      End if;
      
      --getting the recieved events
              select identifier into rec_event_id from catissue_received_event_param where identifier in
              ( select identifier from CATISSUE_SPECIMEN_EVENT_PARAM where specimen_id in (
            	select identifier from catissue_cp_req_specimen where identifier = specReq_ID));
	
-------selecting the container Id for Recieved event
	       IF (rec_event_id != 0) THEN 
         begin
             select con.identifier into rec_container_id from dyextn_container con, dyextn_entity ent where con.caption like 'ReceivedEventParameters' 
             and con.abstract_entity_id=ent.identifier and ent.entity_group_id in 
             (select identifier from dyextn_entity_group where Long_name like 'SpecimenEvents');
		
------Insert entry in abstract Form context for Recieved event
			      
      Insert into dyextn_abstract_form_context(IDENTIFIER,FORM_LABEL,CONTAINER_ID,HIDE_FORM,ACTIVITY_STATUS) 
      values(DYEXTN_ABSTRACT_FRM_CTXT_SEQ.NEXTVAL,null,rec_container_id,0,'Active');
      select DYEXTN_ABSTRACT_FRM_CTXT_SEQ.CURRVAL into spp_de_id from dual;
	
-------Insert default values for Received event
	    INSERT into dyextn_abstract_record_entry (IDENTIFIER,modified_date,activity_status,abstract_form_context_id)
      values (DYEXTN_ABSTRACT_RE_SEQ.NEXTVAL,sysdate(),'Active',spp_de_id);
      
      select DYEXTN_ABSTRACT_RE_SEQ.CURRVAL into rec_entry_id from dual;
      
      
      INSERT into   catissue_action_app_rcd_entry(identifier)values(rec_entry_id);
      INSERT into catissue_action(IDENTIFIER,BARCODE,ACTION_ORDER,ACTION_APP_RECORD_ENTRY_ID,SPP_IDENTIFIER,UNIQUE_ID,IS_SKIPPED) 
	    values(spp_de_id,null,1,rec_entry_id,spp_id,2,0);
      
      select rec.RECEIVED_QUALITY,event.comments,event.user_id, event.event_timestamp into 
      RECEIVED_QUALITY,comments,event_user_id,event_timestamp 
      from catissue_received_event_param rec, CATISSUE_SPECIMEN_EVENT_PARAM event 
      where event.specimen_id = specReq_ID and rec.identifier =event.identifier;
	
        select CATISSUE_ABS_APPL_SEQ.NEXTVAL into max_id from dual;
        rec_query_text_form :='insert into CATISSUE_ABSTRACT_APPLICATION(identifier,timestamp,user_details,comments)
        values(:1, :2, :3, :4)';
        execute immediate rec_query_text_form using 
        max_id,event_timestamp,event_user_id,comments;
      
       INSERT  into   catissue_action_application
       (identifier,action_app_record_entry_id)
       values(max_id,rec_entry_id);

	update catissue_cp_req_specimen set spp_identifier = spp_id where identifier = specReq_ID;
      
	EXECUTE IMMEDIATE rec_query_text using RECEIVED_QUALITY,max_id,rec_entry_id;
	commit;
   NULL; 
        EXCEPTION WHEN OTHERS THEN 
        v_code := SQLCODE; 
        v_errm := SUBSTR(SQLERRM, 1, 1000);
        DBMS_OUTPUT.PUT_LINE('exception occer''Error code ' || v_code ||' '||v_errm||' '||counter ); 
        end;
        END IF;
  
  end loop;
close spp_mig_cursor;
      
      
END SPP_EVENT_MIGRATE;