update catissue_specimen_protocol sp set sp.title=sp.identifier,sp.short_title=sp.identifier where sp.title like '%-P';

update catissue_specimen_coll_group scg set scg.name=scg.identifier;

update catissue_specimen_protocol sp set sp.title=sp.identifier,sp.short_title=sp.identifier where 
sp.identifier in (select dp.IDENTIFIER from catissue_distribution_protocol dp);

update CATISSUE_QUARANTINE_PARAMS qp set qp.COMMENTS='';
update CATISSUE_REVIEW_PARAMS rp set rp.COMMENTS='';
update CATISSUE_REPORTED_PROBLEM rp set rp.COMMENTS='';
update CATISSUE_SPECIMEN sp set sp.COMMENTS='';
update CATISSUE_SPECIMEN_EVENT_PARAM sep set sep.COMMENTS='';
update CATISSUE_CONTAINER con set con.COMMENTS='';
update CATISSUE_CONTAINER_TYPE cp set cp.COMMENTS='';
update CATISSUE_DISTRIBUTION dist set dist.COMMENTS='';
update CATISSUE_SPECIMEN_COLL_GROUP scg set scg.COMMENTS='';
update CATISSUE_ORDER od set od.COMMENTS='';
update CATISSUE_BASE_SHIPMENT bs set bs.SENDER_COMMENTS='', bs.RECEIVER_COMMENTS='';
update CATISSUE_AUDIT_EVENT ae set ae.COMMENTS='';
update CATISSUE_BIOHAZARD bio set bio.COMMENTS='';
commit;
