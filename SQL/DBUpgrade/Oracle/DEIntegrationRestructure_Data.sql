alter table CATISSUE_STUDY_FORM_CONTEXT add constraint FKC6723622BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_FORM_CONTEXT (IDENTIFIER);
alter table CATISSUE_PARTICIPANT_REC_NTRY add constraint FKC6723656BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_RECORD_ENTRY (IDENTIFIER);
alter table CATISSUE_SPECIMEN_REC_NTRY add constraint FKC6727856BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_RECORD_ENTRY (IDENTIFIER);
alter table CATISSUE_SCG_REC_NTRY add constraint FKC6723689BC7298A9 foreign key (IDENTIFIER) references DYEXTN_ABSTRACT_RECORD_ENTRY (IDENTIFIER);

delete from DYEXTN_ENTITY_MAP_CONDNS where form_context_id is null;

delete from DYEXTN_ENTITY_MAP_CONDNS where identifier not in
(select min(identifier) from DYEXTN_ENTITY_MAP_CONDNS T2
where T2.form_context_id=DYEXTN_ENTITY_MAP_CONDNS.form_context_id);

insert into dyextn_abstract_form_context (identifier,form_label,container_id,hide_form)
 (select DYEXTN_ABSTRACT_FRM_CTXT_SEQ.NEXTVAL,fc.study_form_label,em.container_id,1 from dyextn_form_context fc,
 dyextn_entity_map em, DYEXTN_ENTITY_MAP_CONDNS cond
 where fc.entity_map_id=em.identifier and cond.form_context_id=fc.identifier
 and cond.STATIC_RECORD_ID=0);

insert into dyextn_abstract_form_context (identifier,form_label,container_id,hide_form)
 (select DYEXTN_ABSTRACT_FRM_CTXT_SEQ.NEXTVAL,fc.study_form_label,em.container_id,0 from dyextn_form_context fc,
 dyextn_entity_map em, DYEXTN_ENTITY_MAP_CONDNS cond
 where fc.entity_map_id=em.identifier and cond.form_context_id=fc.identifier
 and cond.STATIC_RECORD_ID<>0);

insert into dyextn_abstract_form_context (identifier,form_label,container_id,hide_form)
(select DYEXTN_ABSTRACT_FRM_CTXT_SEQ.NEXTVAL,fc.study_form_label,em.container_id,0 from dyextn_entity_map em,dyextn_form_context fc
left join DYEXTN_ENTITY_MAP_CONDNS cond on cond.form_context_id=fc.identifier
where fc.entity_map_id=em.identifier and fc.identifier not in
(select fc1.identifier from dyextn_form_context fc1 join DYEXTN_ENTITY_MAP_CONDNS cond1 on
cond1.form_context_id=fc1.identifier));

insert into catissue_study_form_context (identifier)
(select identifier from dyextn_abstract_form_context);

insert into CATISSUE_CP_STUDYFORMCONTEXT(STUDY_FORM_CONTEXT_ID,COLLECTION_PROTOCOL_ID)
(select afc.identifier, cond.STATIC_RECORD_ID from dyextn_abstract_form_context afc,
DYEXTN_ENTITY_MAP_CONDNS cond, DYEXTN_ENTITY_MAP em, DYEXTN_FORM_CONTEXT fc
where afc.hide_form=0 and afc.container_id=em.container_id
and cond.FORM_CONTEXT_ID = fc.identifier and fc.ENTITY_MAP_ID = em.identifier);

-- Change Participant associations to ParticipantRecordEntry
update dyextn_attribute da set da.entiy_id = (select identifier from dyextn_abstract_metadata where name like 'edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry')
where da.identifier in (select identifier from dyextn_association where target_entity_id in (select ABSTRACT_ENTITY_ID from DYEXTN_CONTAINER where IDENTIFIER
in (select container_id from dyextn_entity_map where static_entity_id=844))) and da.entiy_id=844;

-- SCG
update dyextn_attribute da set da.entiy_id = (select identifier from dyextn_abstract_metadata where name like 'edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry')
where da.identifier in (select identifier from dyextn_association where target_entity_id in (select ABSTRACT_ENTITY_ID from DYEXTN_CONTAINER where IDENTIFIER
in (select container_id from dyextn_entity_map where static_entity_id=379))) and da.entiy_id=379;

-- Specimen
update dyextn_attribute da set da.entiy_id = (select identifier from dyextn_abstract_metadata where name like 'edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry')
where da.identifier in (select identifier from dyextn_association where target_entity_id in (select ABSTRACT_ENTITY_ID from DYEXTN_CONTAINER where IDENTIFIER
in (select container_id from dyextn_entity_map where static_entity_id=4))) and da.entiy_id=4;

-- Add path between DE table-ParticipantRecordEntry and remove path DE table-Participant
update path set first_entity_id=(select identifier from dyextn_abstract_metadata where
name like 'edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry') where first_entity_id=844 and last_entity_id in
(select ABSTRACT_ENTITY_ID from DYEXTN_CONTAINER
where IDENTIFIER in (select container_id from dyextn_entity_map where static_entity_id=844));

-- Add path between DE table-SCGRecordEntry and remove path DE table-SCG
update path set first_entity_id=(select identifier from dyextn_abstract_metadata where
name like 'edu.wustl.catissuecore.domain.deintegration.SCGRecordEntry') where first_entity_id=379 and last_entity_id in
(select ABSTRACT_ENTITY_ID from DYEXTN_CONTAINER
where IDENTIFIER in (select container_id from dyextn_entity_map where static_entity_id=379));

-- Add path between DE table-SpecimenRecordEntry and remove path DE table-Specimen
update path set first_entity_id=(select identifier from dyextn_abstract_metadata where
name like 'edu.wustl.catissuecore.domain.deintegration.SpecimenRecordEntry') where first_entity_id=4 and last_entity_id in
(select ABSTRACT_ENTITY_ID from DYEXTN_CONTAINER
where IDENTIFIER in (select container_id from dyextn_entity_map where static_entity_id=4));


--drop table dyextn_entity_map_record
--drop table dyextn_entity_map_condns
--drop table dyextn_form_context
--drop table dyextn_entity_map