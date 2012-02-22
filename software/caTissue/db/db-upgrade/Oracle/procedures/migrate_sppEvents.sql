CREATE OR replace PROCEDURE Spp_event_migrate 
AS 
  counter              INTEGER; 
  stme                 INTEGER; 
  -----------------------@using parameter----------------------------- 
  activitystatus       VARCHAR(1000); 
  sp_id                INTEGER; 
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
  coll_container            VARCHAR(200); 
  rec_quality     VARCHAR(200); 
  rec_entry_id         INTEGER; 
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

  OPEN spp_mig_cursor; 

  LOOP 
      FETCH spp_mig_cursor INTO cpe_id, specreq_id; 

      EXIT WHEN spp_mig_cursor%notfound; 


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

             SELECT coll.collection_procedure, coll.container
             into coll_procedure, coll_container
            FROM   catissue_coll_event_param coll, 
                   catissue_specimen_event_param event 
            WHERE  event.specimen_id = specreq_id 
                   AND coll.identifier = event.identifier; 



            SELECT rec.received_quality
            INTO   rec_quality  
            FROM   catissue_received_event_param rec, 
                   catissue_specimen_event_param event 
            WHERE  event.specimen_id = specreq_id 
                   AND rec.identifier = event.identifier; 

			select spp.spp_id into spp_iden from temp_spp_events spp where spp.collection_procedure like COLL_PROCEDURE 
	and spp.container like coll_CONTAINER and spp.received_quality like REC_QUALITY;
	
            UPDATE catissue_cp_req_specimen 
            SET    spp_identifier = spp_iden 
            WHERE  identifier = specreq_id; 


            COMMIT; 

            
  END LOOP; 

  CLOSE spp_mig_cursor; 
END spp_event_migrate; 