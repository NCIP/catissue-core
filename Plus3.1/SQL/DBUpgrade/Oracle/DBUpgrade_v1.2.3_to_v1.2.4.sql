alter table catissue_collection_protocol add IS_EMPI_ENABLE  number(1,0);

update catissue_collection_protocol set IS_EMPI_ENABLE ='0';

alter table CATISSUE_PARTICIPANT add EMPI_ID_STATUS varchar2(50);

ALTER TABLE CATISSUE_SITE ADD FACILITY_ID varchar2(50);

alter table CATISSUE_PERMISSIBLE_VALUE add SORTORDER number(11,0);

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 1 where VALUE='White' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 2 where VALUE='Black or African American' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 3 where VALUE='American Indian or Alaska Native' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 4 where VALUE='Asian' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 5 where VALUE='Native Hawaiian or Other Pacific Islander' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 6 where VALUE='Not Reported' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 7 where VALUE='Other' and PUBLIC_ID='Race_PID';

update CATISSUE_PERMISSIBLE_VALUE set SORTORDER = 8 where VALUE='Unknown' and PUBLIC_ID='Race_PID';

   
commit;