CREATE OR replace PROCEDURE Frozen_migrate_call(event_name in varchar2)
IS 
  form_context_id           INTEGER; 
  seqval                    INTEGER; 
  counter                   INTEGER; 
  specimen_event_identifier INTEGER; 
  specimen_id               INTEGER; 
  specimen_event_user_id    INTEGER; 
  specimen_event_param_id   INTEGER; 
  specimen_comments         VARCHAR2(2000); 
  specimen_timestamp        TIMESTAMP; 
  parent_specimen_id        INTEGER; 
  flag                      INTEGER; 
  dispo_reason              VARCHAR2(2000); 
  query_text                VARCHAR2(2000); 
  query_text_form           VARCHAR2(2000); 
  v_code                    INTEGER; 
  v_errm                    VARCHAR2(2000); 
  CURSOR mig_cursor IS 
    SELECT spec.identifier, 
           spec.specimen_id, 
           spec.event_timestamp, 
           spec.user_id, 
           spec.comments, 
           froz.method, 
           absspec.parent_specimen_id 
    FROM   catissue_frozen_event_param froz, 
           catissue_specimen_event_param spec, 
           catissue_specimen se, 
           catissue_abstract_specimen absspec 
    WHERE  froz.identifier = spec.identifier 
           AND spec.specimen_id = se.identifier 
           AND absspec.identifier = se.identifier; 
BEGIN 
  SELECT formcontext.identifier 
  INTO   form_context_id 
  FROM   dyextn_abstract_form_context formcontext 
         join dyextn_container dcontainer 
           ON formcontext.container_id = dcontainer.identifier 
         join dyextn_entity ent 
           ON ent.identifier = dcontainer.abstract_entity_id 
         join dyextn_abstract_metadata meta 
           ON meta.NAME = event_name 
              AND meta.identifier = ent.identifier 
         join dyextn_abstract_metadata meta2 
           ON meta2.NAME = 'SpecimenEvents' 
         join dyextn_entity_group eg 
           ON eg.identifier = meta2.identifier 
              AND eg.identifier = ent.entity_group_id 
              AND formcontext.activity_status = 'Active'; 

  SELECT Query_formation(event_name) 
  INTO   query_text 
  FROM   dual; 

  dbms_output.Put_line(query_text); 

  counter := 0; 

  OPEN mig_cursor; 

  LOOP 
      FETCH mig_cursor INTO specimen_event_identifier, specimen_id, 
      specimen_timestamp 
      , specimen_event_user_id, specimen_comments, dispo_reason, 
      parent_specimen_id; 

      EXIT WHEN mig_cursor%notfound; 

      SELECT COUNT(*) 
      INTO   flag 
      FROM   catissue_received_event_param rec, 
             catissue_specimen_event_param spec 
      WHERE  spec.specimen_id = parent_specimen_id 
             AND spec.event_timestamp = specimen_timestamp 
             AND spec.identifier = rec.identifier; 

      IF flag = 0 THEN 
        BEGIN 
            INSERT INTO dyextn_abstract_record_entry 
                        (identifier, 
                         modified_date, 
                         activity_status, 
                         abstract_form_context_id) 
            VALUES      (dyextn_abstract_re_seq.nextval, 
                         SYSDATE, 
                         'Active', 
                         form_context_id); 

            INSERT INTO catissue_action_app_rcd_entry 
                        (identifier) 
            VALUES     (dyextn_abstract_re_seq.currval); 

            query_text_form := 
'insert  into CATISSUE_ABSTRACT_APPLICATION(identifier,timestamp,user_details,comments) values(:1, :2, :3, :4)'
; 

EXECUTE IMMEDIATE query_text_form 
USING specimen_event_identifier, specimen_timestamp, specimen_event_user_id, 
specimen_comments; 

INSERT INTO catissue_action_application 
            (identifier, 
             specimen_id, 
             action_app_record_entry_id) 
VALUES     (specimen_event_identifier, 
            specimen_id, 
            dyextn_abstract_re_seq.currval); 

SELECT dyextn_abstract_re_seq.currval 
INTO   seqval 
FROM   dual; 

EXECUTE IMMEDIATE query_text 
USING specimen_event_identifier, dispo_reason, seqval; 

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

CLOSE mig_cursor; 
EXCEPTION 
  WHEN dup_val_on_index THEN 
             ROLLBACK; 

             dbms_output.Put_line('Duplicate value on an index'); 
WHEN OTHERS THEN 
             ROLLBACK; 

             v_code := SQLCODE; 

             v_errm := Substr(sqlerrm, 1, 64); 

             dbms_output.Put_line('exception occer''Error code ' 
                                  || v_code 
                                  || ': ' 
                                  || v_errm); 
END frozen_migrate_call; 