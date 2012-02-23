CREATE OR replace PROCEDURE Create_spp_events 
AS 
  coll_event_name      VARCHAR (100) := 'CollectionEventParameters'; 
  rec_event_name       VARCHAR (100) := 'ReceivedEventParameters'; 
  coll_query_text      VARCHAR(1000); 
  coll_query_text_form VARCHAR(1000); 
  rec_query_text       VARCHAR(1000); 
  rec_query_text_form  VARCHAR(1000); 
  counter              INTEGER; 
  stme                 INTEGER; 
  -----------------------@using parameter----------------------------- 
  activitystatus       VARCHAR(1000); 
  sp_id                INTEGER; 
  des_reason           VARCHAR(1000); 
  s_seq_var            INTEGER; 
  collectionl          VARCHAR(1000); 
  cont                 VARCHAR(1000); 
  -------------------------------------------------------------------- 
  specreq_id           INTEGER; 
  cpe_id               INTEGER; 
  coll_event_id        INTEGER := 0; 
  rec_event_id         INTEGER := 0; 
  flag                 INTEGER; 
  spp_name             VARCHAR(100); 
  record_not_found     INTEGER; 
  coll_container_id    INTEGER; 
  rec_container_id     INTEGER; 
  max_id               INTEGER; 
  id_for_spp           INTEGER; 
  spp_iden               INTEGER; 
  spp_de_id            INTEGER; 
  coll_procedure VARCHAR(200); 
  coll_CONTAINER            VARCHAR(200); 
  rec_quality     VARCHAR(200); 
  rec_entry_id         INTEGER; 
  v_code               NUMBER; 
  v_errm               VARCHAR2(64); 
  -------------------------DECLARE CURSOR---------------------------------------------- 
  CURSOR spp_create_cursor IS 
  select spp.collection_procedure, spp.container, spp.received_quality
    from temp_spp_events spp where spp.spp_id is null;
BEGIN 
  -----------------------------------calling function for collection event---------------------------------------------------------------         
  SELECT Query_formation(coll_event_name) 
  INTO   coll_query_text 
  FROM   dual; 

  -----------------------------------calling function for received event---------------------------------------------------------------         
  SELECT Query_formation(rec_event_name) 
  INTO   rec_query_text 
  FROM   dual; 

  OPEN spp_create_cursor; 

  LOOP 
      FETCH spp_create_cursor INTO COLL_PROCEDURE,coll_CONTAINER,REC_QUALITY; 

      EXIT WHEN spp_create_cursor%notfound; 

      ------------- 
      -------Insert entry into caTissue_SPP table 
      SELECT catissue_spp_seq.nextval 
      INTO   spp_iden 
      FROM   dual; 
	  SELECT COLL_PROCEDURE||'_'||coll_CONTAINER||'_'||REC_QUALITY||'_'||spp_iden
      INTO   spp_name 
      FROM   dual; 

      INSERT INTO catissue_spp 
                  (identifier, 
                   NAME, 
                   barcode) 
      VALUES     (spp_iden, 
                  spp_name, 
                  NULL); 


      ----selecting the container Id for collection event 
            SELECT con.identifier 
            INTO   coll_container_id 
            FROM   dyextn_container con, 
                   dyextn_entity ent 
            WHERE  con.caption LIKE 'CollectionEventParameters' 
                   AND con.abstract_entity_id = ent.identifier 
                   AND ent.entity_group_id IN (SELECT identifier 
                                               FROM   dyextn_entity_group 
                                               WHERE 
                       long_name LIKE 'SpecimenEvents'); 

            ----Insert entry in abstract Form context for collecion events 
            INSERT INTO dyextn_abstract_form_context 
                        (identifier, 
                         form_label, 
                         container_id, 
                         hide_form, 
                         activity_status) 
            VALUES     (dyextn_abstract_frm_ctxt_seq.nextval, 
                        NULL, 
                        coll_container_id, 
                        0, 
                        'Active'); 

            -----Insert default values for Collection event 
            INSERT INTO dyextn_abstract_record_entry 
                        (identifier, 
                         modified_date, 
                         activity_status, 
                         abstract_form_context_id) 
            VALUES      (dyextn_abstract_re_seq.nextval, 
                         SYSDATE(), 
                         'Active', 
                         dyextn_abstract_frm_ctxt_seq.currval); 

            INSERT INTO catissue_action_app_rcd_entry 
                        (identifier) 
            VALUES     (dyextn_abstract_re_seq.currval); 

            INSERT INTO catissue_action 
                        (identifier, 
                         barcode, 
                         action_order, 
                         action_app_record_entry_id, 
                         spp_identifier, 
                         unique_id, 
                         is_skipped) 
            VALUES     (dyextn_abstract_frm_ctxt_seq.currval, 
                        NULL, 
                        1, 
                        dyextn_abstract_re_seq.currval, 
                        spp_iden, 
                        1, 
                        0); 

			select CATISSUE_SPEC_EVENT_PARAM_SEQ.nextval into max_id from dual;
            EXECUTE IMMEDIATE coll_query_text 
            USING coll_procedure, coll_CONTAINER, max_id, rec_entry_id; 



      -------selecting the container Id for Recieved event 
            SELECT con.identifier 
            INTO   rec_container_id 
            FROM   dyextn_container con, 
                   dyextn_entity ent 
            WHERE  con.caption LIKE 'ReceivedEventParameters' 
                   AND con.abstract_entity_id = ent.identifier 
                   AND ent.entity_group_id IN (SELECT identifier 
                                               FROM   dyextn_entity_group 
                                               WHERE 
                       long_name LIKE 'SpecimenEvents'); 

            ------Insert entry in abstract Form context for Recieved event 
            INSERT INTO dyextn_abstract_form_context 
                        (identifier, 
                         form_label, 
                         container_id, 
                         hide_form, 
                         activity_status) 
            VALUES     (dyextn_abstract_frm_ctxt_seq.nextval, 
                        NULL, 
                        rec_container_id, 
                        0, 
                        'Active'); 

            SELECT dyextn_abstract_frm_ctxt_seq.currval 
            INTO   spp_de_id 
            FROM   dual; 

            -------Insert default values for Received event 
            INSERT INTO dyextn_abstract_record_entry 
                        (identifier, 
                         modified_date, 
                         activity_status, 
                         abstract_form_context_id) 
            VALUES      (dyextn_abstract_re_seq.nextval, 
                         SYSDATE(), 
                         'Active', 
                         spp_de_id); 

            SELECT dyextn_abstract_re_seq.currval 
            INTO   rec_entry_id 
            FROM   dual; 

            INSERT INTO catissue_action_app_rcd_entry 
                        (identifier) 
            VALUES     (rec_entry_id); 

            INSERT INTO catissue_action 
                        (identifier, 
                         barcode, 
                         action_order, 
                         action_app_record_entry_id, 
                         spp_identifier, 
                         unique_id, 
                         is_skipped) 
            VALUES     (spp_de_id, 
                        NULL, 
                        2, 
                        rec_entry_id, 
                        spp_iden, 
                        2, 
                        0); 


            SELECT CATISSUE_SPEC_EVENT_PARAM_SEQ.nextval 
            INTO   max_id 
            FROM   dual; 


            EXECUTE IMMEDIATE rec_query_text 
            USING max_id,rec_quality, rec_entry_id; 
            
            update temp_spp_events spp set spp.spp_id = spp_iden where spp.collection_procedure like COLL_PROCEDURE 
	and spp.container like coll_CONTAINER and spp.received_quality like REC_QUALITY;

            COMMIT; 

  END LOOP; 

  CLOSE spp_create_cursor; 
END Create_spp_events; 