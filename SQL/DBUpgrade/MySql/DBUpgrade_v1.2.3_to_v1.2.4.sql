alter table catissue_collection_protocol add column IS_EMPI_ENABLE boolean;

update catissue_collection_protocol set IS_EMPI_ENABLE = false; 
alter table catissue_participant add column EMPI_ID_STATUS varchar(50);

alter table catissue_site add column FACILITY_ID varchar(50);

create table PARTICIPANT_EMPI_ID_MAPPING (
   PERMANENT_PARTICIPANT_ID varchar(255),
   TEMPARARY_PARTICIPANT_ID varchar(255),
   OLD_EMPI_ID varchar(255)
);

alter table CATISSUE_PERMISSIBLE_VALUE add SORTORDER bigint(20);

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 1 where VALUE='White' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 2 where VALUE='Black or African American' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 3 where VALUE='American Indian or Alaska Native' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 4 where VALUE='Asian' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 5 where VALUE='Native Hawaiian or Other Pacific Islander' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 6 where VALUE='Not Reported' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 7 where VALUE='Other' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 8 where VALUE='Unknown' and PUBLIC_ID='Race_PID';

commit;

