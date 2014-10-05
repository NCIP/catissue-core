drop procedure if exists insert_specimen_event_de_record //

create procedure insert_specimen_event_de_record(IN event_name CHAR(32), IN specimen_id BIGINT, IN user_id BIGINT, OUT rec_id BIGINT)
begin
 select 
   last_id into @last_id 
 from 
   dyextn_id_seq 
 where
   table_name = 'RECORD_ID_SEQ'
 for update;
 
 select 
   fc.identifier into @ctx_id 
 from 
   dyextn_containers c 
   inner join catissue_form_context fc on fc.container_id = c.identifier
 where 
   c.name = event_name;

 insert into 
   catissue_form_record_entry(identifier, form_ctxt_id, object_id, record_id, updated_by, update_time, activity_status) 
   values(default, @ctx_id, specimen_id, @last_id + 1, user_id, current_timestamp(), 'ACTIVE');

 update dyextn_id_seq set last_id = @last_id + 1 where table_name = 'RECORD_ID_SEQ';

 set rec_id = @last_id + 1;
end
//

drop trigger if exists specimen_coll_event_trg //
create trigger specimen_coll_event_trg before insert
on catissue_coll_event_param
for each row begin
  declare rec_id bigint;
  call insert_specimen_event_de_record('SpecimenCollectionEvent', new.specimen_id, new.user_id, rec_id);
  set new.identifier = rec_id;
end
//

drop trigger if exists specimen_rcvd_event_trg //
create trigger specimen_rcvd_event_trg before insert
on catissue_received_event_param
for each row begin
  declare rec_id bigint;
  call insert_specimen_event_de_record('SpecimenReceivedEvent', new.specimen_id, new.user_id, rec_id);
  set new.identifier = rec_id;
end
//

drop trigger if exists specimen_transfer_event_trg //
create trigger specimen_transfer_event_trg before insert
on catissue_transfer_event_param
for each row begin
  declare rec_id bigint;
  call insert_specimen_event_de_record('SpecimenTransferEvent', new.specimen_id, new.user_id, rec_id);
  set new.identifier = rec_id;
end
//

drop trigger if exists specimen_distri_event_trg //
create trigger specimen_distri_event_trg before insert
on catissue_distri_event_param
for each row begin
  declare rec_id bigint;
  call insert_specimen_event_de_record('SpecimenDistributedEvent', new.specimen_id, new.user_id, rec_id);
  set new.identifier = rec_id;
end
//

drop trigger if exists specimen_disposal_event_trg //
create trigger specimen_disposal_event_trg before insert
on catissue_disposal_event_param
for each row begin
  declare rec_id bigint;
  call insert_specimen_event_de_record('SpecimenDisposalEvent', new.specimen_id, new.user_id, rec_id);
  set new.identifier = rec_id;
end
//
