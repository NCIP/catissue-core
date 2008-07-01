drop table if exists catissue_participant_empi;

create table catissue_participant_empi (
   Id int(11) NOT NULL default '0',
   EMPI_ID int(11) NOT NULL default '0',
   primary key (Id)
);

INSERT INTO CSM_PROTECTION_ELEMENT 
(PROTECTION_ELEMENT_ID,PROTECTION_ELEMENT_NAME,PROTECTION_ELEMENT_DESCRIPTION,OBJECT_ID,ATTRIBUTE,PROTECTION_ELEMENT_TYPE,APPLICATION_ID,UPDATE_DATE) 
VALUES (336,'Participant Empi','Participant Empi Class','edu.wustl.catissuecore.domain.ParticipantEmpi',
'Participant Empi comments',NULL,1,'0000-00-00');

INSERT INTO csm_pg_pe ( PG_PE_ID ,PROTECTION_GROUP_ID, PROTECTION_ELEMENT_ID
,UPDATE_DATE) VALUES (409,1,336,'0000-00-00');

commit;