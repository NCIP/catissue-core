create or replace procedure 
  insert_spec_event_de_record(event_name IN VARCHAR2, specimen_id IN NUMBER, user_id IN NUMBER, rec_id OUT NUMBER)
IS
  last_id NUMBER;
  ctx_id NUMBER;
begin
 select 
   last_id into last_id 
 from 
   dyextn_id_seq 
 where
   table_name = 'RECORD_ID_SEQ'
 for update;
 
 select 
   fc.identifier into ctx_id 
 from 
   dyextn_containers c 
   inner join catissue_form_context fc on fc.container_id = c.identifier
 where 
   c.name = event_name;

 insert into 
   catissue_form_record_entry(identifier, form_ctxt_id, object_id, record_id, updated_by, update_time, activity_status) 
   values(catissue_form_rec_entry_seq.nextval, ctx_id, specimen_id, last_id + 1, user_id, sysdate, 'ACTIVE');

 update dyextn_id_seq set last_id = last_id + 1 where table_name = 'RECORD_ID_SEQ';

 rec_id := last_id + 1;
end;
//

create or replace trigger specimen_coll_event_trg before insert
on catissue_coll_event_param
for each row 
  declare rec_id number;  
begin
  insert_spec_event_de_record('SpecimenCollectionEvent', :new.specimen_id, :new.user_id, rec_id);
  :new.identifier := rec_id;
end;
//

create or replace trigger specimen_rcvd_event_trg before insert
on catissue_received_event_param
for each row
  declare rec_id number;
begin
  insert_spec_event_de_record('SpecimenReceivedEvent', :new.specimen_id, :new.user_id, rec_id);
  :new.identifier := rec_id;
end;
//

create or replace trigger specimen_transfer_event_trg before insert
on catissue_transfer_event_param
for each row 
  declare rec_id number;
begin
  insert_spec_event_de_record('SpecimenTransferEvent', :new.specimen_id, :new.user_id, rec_id);
  :new.identifier := rec_id;
end;
//

create or replace trigger specimen_distri_event_trg before insert
on catissue_distri_event_param
for each row 
  declare rec_id number;
begin
  insert_spec_event_de_record('SpecimenDistributedEvent', :new.specimen_id, :new.user_id, rec_id);
  :new.identifier := rec_id;
end;
//

create or replace trigger specimen_disposal_event_trg before insert
on catissue_disposal_event_param
for each row 
  declare rec_id number;
begin
  insert_spec_event_de_record('SpecimenDisposalEvent', :new.specimen_id, :new.user_id, rec_id);
  :new.identifier := rec_id;
end;
//
