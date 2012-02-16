CREATE OR replace PROCEDURE Spp_event_migrate 
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
  spp_id               INTEGER; 
  spp_de_id            INTEGER; 
  collection_procedure VARCHAR(200); 
  container            VARCHAR(200); 
  received_quality     VARCHAR(200); 
  rec_entry_id         INTEGER; 
  comments             VARCHAR(2000); 
  event_timestamp      TIMESTAMP; 
  event_user_id        INTEGER; 
  v_code               NUMBER; 
  v_errm               VARCHAR2(64); 
  -------------------------DECLARE CURSOR---------------------------------------------- 
  CURSOR spp_mig_cursor IS 
    SELECT cpe.identifier, 
           specreq.identifier 
    FROM   catissue_coll_prot_event cpe, 
           catissue_cp_req_specimen specreq 
    WHERE  specreq.collection_protocol_event_id = cpe.identifier; 
BEGIN 
  -----------------------------------calling function for collection event---------------------------------------------------------------         
  SELECT Query_formation(coll_event_name) 
  INTO   coll_query_text 
  FROM   dual; 

  -----------------------------------calling function for received event---------------------------------------------------------------         
  SELECT Query_formation(rec_event_name) 
  INTO   rec_query_text 
  FROM   dual; 

  OPEN spp_mig_cursor; 

  LOOP 
      FETCH spp_mig_cursor INTO cpe_id, specreq_id; 

      EXIT WHEN spp_mig_cursor%notfound; 

      ------------- 
      -------Insert entry into caTissue_SPP table 
      SELECT catissue_spp_seq.nextval 
      INTO   spp_id 
      FROM   dual; 

      SELECT Concat('migrated_spp_', spp_id) 
      INTO   spp_name 
      FROM   dual; 

      INSERT INTO catissue_spp 
                  (identifier, 
                   NAME, 
                   barcode) 
      VALUES     (spp_id, 
                  spp_name, 
                  NULL); 

      -------getting the collection events 
      SELECT identifier 
      INTO   coll_event_id 
      FROM   catissue_coll_event_param 
      WHERE  identifier IN (SELECT identifier 
                            FROM   catissue_specimen_event_param 
                            WHERE  specimen_id IN (SELECT identifier 
                                                   FROM 
                                   catissue_cp_req_specimen 
                                                   WHERE 
                                   identifier = specreq_id) 
                           ); 

      ----selecting the container Id for collection event 
      IF ( coll_event_id != 0 ) THEN 
        BEGIN 
            SELECT con.identifier 
            INTO   coll_container_id 
            FROM   dyextn_container con, 
                   dyextn_entity ent 
            WHERE  con.caption LIKE 'CollectionEventParameters' 
                   AND con.abstract_entity_id = ent.identifier 
                   AND ent.entity_group_id IN (SELECT identifier 
                                               FROM   dyextn_entity_group 
                                               WHERE 
                       long_name LIKE 'SpecimenEvents') 
            ; 

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
                        spp_id, 
                        1, 
                        0); 

            SELECT coll.collection_procedure, 
                   coll.container, 
                   event.comments, 
                   event.user_id, 
                   event.event_timestamp 
            INTO   collection_procedure, container, comments, event_user_id, 
                   event_timestamp 
            FROM   catissue_coll_event_param coll, 
                   catissue_specimen_event_param event 
            WHERE  event.specimen_id = specreq_id 
                   AND coll.identifier = event.identifier; 

            SELECT dyextn_abstract_re_seq.currval 
            INTO   rec_entry_id 
            FROM   dual; 

            SELECT catissue_abs_appl_seq.nextval 
            INTO   max_id 
            FROM   dual; 

            coll_query_text_form := 'insert into CATISSUE_ABSTRACT_APPLICATION(identifier,timestamp,user_details,comments)      values(:1, :2, :3, :4)'; 

            EXECUTE IMMEDIATE coll_query_text_form 
            USING max_id, event_timestamp, event_user_id, comments; 

            INSERT INTO catissue_action_application 
                        (identifier, 
                         action_app_record_entry_id) 
            VALUES     (max_id, 
                        rec_entry_id); 

            EXECUTE IMMEDIATE coll_query_text 
            USING collection_procedure, container, max_id, rec_entry_id; 

            NULL; 
        EXCEPTION 
            WHEN OTHERS THEN 
              v_code := SQLCODE; 

              v_errm := Substr(sqlerrm, 1, 1000); 

              dbms_output.Put_line('exception occer''Error code ' 
                                   || v_code 
                                   ||' ' 
                                   ||v_errm 
                                   ||' ' 
                                   ||counter); 
        END; 
      END IF; 

      --getting the recieved events 
      SELECT identifier 
      INTO   rec_event_id 
      FROM   catissue_received_event_param 
      WHERE  identifier IN (SELECT identifier 
                            FROM   catissue_specimen_event_param 
                            WHERE  specimen_id IN (SELECT identifier 
                                                   FROM 
                                   catissue_cp_req_specimen 
                                                   WHERE 
                                   identifier = specreq_id) 
                           ); 

      -------selecting the container Id for Recieved event 
      IF ( rec_event_id != 0 ) THEN 
        BEGIN 
            SELECT con.identifier 
            INTO   rec_container_id 
            FROM   dyextn_container con, 
                   dyextn_entity ent 
            WHERE  con.caption LIKE 'ReceivedEventParameters' 
                   AND con.abstract_entity_id = ent.identifier 
                   AND ent.entity_group_id IN (SELECT identifier 
                                               FROM   dyextn_entity_group 
                                               WHERE 
                       long_name LIKE 'SpecimenEvents') 
            ; 

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
                        1, 
                        rec_entry_id, 
                        spp_id, 
                        2, 
                        0); 

            SELECT rec.received_quality, 
                   event.comments, 
                   event.user_id, 
                   event.event_timestamp 
            INTO   received_quality, comments, event_user_id, event_timestamp 
            FROM   catissue_received_event_param rec, 
                   catissue_specimen_event_param event 
            WHERE  event.specimen_id = specreq_id 
                   AND rec.identifier = event.identifier; 

            SELECT catissue_abs_appl_seq.nextval 
            INTO   max_id 
            FROM   dual; 

            rec_query_text_form := 'insert into CATISSUE_ABSTRACT_APPLICATION(identifier,timestamp,user_details,comments)         values(:1, :2, :3, :4)'; 

            EXECUTE IMMEDIATE rec_query_text_form 
            USING max_id, event_timestamp, event_user_id, comments; 

            INSERT INTO catissue_action_application 
                        (identifier, 
                         action_app_record_entry_id) 
            VALUES     (max_id, 
                        rec_entry_id); 

            UPDATE catissue_cp_req_specimen 
            SET    spp_identifier = spp_id 
            WHERE  identifier = specreq_id; 

            EXECUTE IMMEDIATE rec_query_text 
            USING max_id,received_quality, rec_entry_id; 

            COMMIT; 

            NULL; 
        EXCEPTION 
            WHEN OTHERS THEN 
              v_code := SQLCODE; 

              v_errm := Substr(sqlerrm, 1, 1000); 

              dbms_output.Put_line('exception occer''Error code ' 
                                   || v_code 
                                   ||' ' 
                                   ||v_errm 
                                   ||' ' 
                                   ||counter); 
        END; 
      END IF; 
  END LOOP; 

  CLOSE spp_mig_cursor; 
END spp_event_migrate; 