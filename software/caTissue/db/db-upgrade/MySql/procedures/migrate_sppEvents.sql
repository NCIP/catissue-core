drop procedure if exists   SPP_Event_migrate;
//
create procedure   SPP_Event_migrate() 
  
  Begin   
	
   DECLARE counter integer default 0;
DECLARE _stme integer default 0;

  #-----------------------@using parameter-----------------------------
  declare activitystatus Text;
  declare sp_id integer;

  declare s_seq_var long default 1;
  declare COLLECTIONl Text;
  declare CONT Text;
  #--------------------------------------------------------------------
   declare specReq_ID integer;
   declare cpe_ID integer;
   declare coll_event_id integer;
   declare rec_event_id integer;
   declare flag integer ;

   declare record_not_found integer default 0;

   declare max_id integer default 0;
   declare id_for_spp integer default 0;
   declare spp_id integer default 0;
   declare spp_de_id  integer default 0;
   declare COLLECTION_PROCEDURE varchar(200);
   declare CONTAINER varchar(200);
   declare RECEIVED_QUALITY varchar(200);
   declare rec_entry_id integer default 0;

#-----------------------------------------------------------------------
      
   declare spp_mig_cursor cursor   for
    select cpe.identifier, specReq.identifier  
    from catissue_coll_prot_event cpe, catissue_cp_req_specimen specReq
	where specReq.COLLECTION_PROTOCOL_EVENT_ID = cpe.identifier;
  
	declare CONTINUE HANDLER for NOT FOUND SET record_not_found = 1;
  
   set counter :=1;
  #------------------------------------------------------------------------------------------------------------------       
      open spp_mig_cursor;
    select 'open cursor';
     itr: LOOP

     fetch spp_mig_cursor into cpe_ID,
                            specReq_ID;
      if record_not_found then LEAVE itr;
      select 'no record';
      end if;
    SELECT 'Hello World!';
       #-----------------------------------------------------------------
#--Insert entry into caTissue_SPP table
	SELECT 'Hello World3!';
#--getting the collection events
       select identifier into coll_event_id from catissue_coll_event_param where identifier in
	( select identifier from CATISSUE_SPECIMEN_EVENT_PARAM where specimen_id in (
	select identifier from catissue_cp_req_specimen where identifier = specReq_ID));

	
#-- Insert default values for Collection event
	SELECT rec.received_quality
                    
            INTO   received_quality
            FROM   catissue_received_event_param rec, 
                   catissue_specimen_event_param event 
            WHERE  event.specimen_id = specreq_id 
                   AND rec.identifier = event.identifier limit 1;

SELECT coll.collection_procedure, 
                   coll.container 
            INTO   collection_procedure, container 
            FROM   catissue_coll_event_param coll, 
                   catissue_specimen_event_param event 
            WHERE  event.specimen_id = specreq_id 
                   AND coll.identifier = event.identifier limit 1; 


	select spp.spp_id into spp_id from temp_spp_events spp where spp.collection_procedure like COLLECTION_PROCEDURE 
	and spp.container like CONTAINER and spp.received_quality like RECEIVED_QUALITY;

	select spp_id; 
	
	update catissue_cp_req_specimen set spp_identifier = spp_id 
	where identifier = specReq_ID; 
      End if;
     set counter =counter+1;
    set _stme=counter;
    select _stme;
    commit;
END loop;
    close spp_mig_cursor;   
  
end;
//

