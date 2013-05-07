DECLARE
   CNT NUMBER;
   ID NUMBER;
   CP_ID NUMBER;

BEGIN
DECLARE CURSOR  INV_CUR IS
		 SELECT COUNT(*),p.identifier, cpr.collection_protocol_id 
 							   	FROM CATISSUE_SPECIMEN c, CATISSUE_SPECIMEN_COLL_GROUP cc, CATISSUE_COLL_PROT_REG cpr,CATISSUE_PERMISSIBLE_VALUE p,CATISSUE_ABSTRACT_SPECIMEN a 
WHERE cc.IDENTIFIER=c.specimen_collection_group_id  AND cpr.IDENTIFIER=cc.collection_protocol_reg_id
AND c.identifier = a.identifier AND p.value = a.SPECIMEN_TYPE AND parent_identifier IN (1,2,3,4) 
GROUP BY SPECIMEN_TYPE, collection_protocol_id, p.identifier;
BEGIN
   OPEN INV_CUR;
   FETCH INV_CUR INTO CNT,ID,CP_ID;
   LOOP
     IF INV_CUR%NOTFOUND THEN
	 	EXIT;
	 END IF;
	 INSERT INTO @@databasename@@.specimen_inventory VALUES(@@databasename@@.specimen_inventory_SEQ.NEXTVAL,CNT,ID,CP_ID);
	 FETCH INV_CUR INTO CNT,ID,CP_ID;
   END LOOP;
   CLOSE INV_CUR;
   commit;
END;
END; 
/

DECLARE
   NUM_DISTRIBUTED NUMBER;
   SP_TYPE_ID NUMBER;
   COLL_PROT_ID NUMBER;
   REPORT_ID NUMBER;	
BEGIN
DECLARE CURSOR  REPORT_CUR IS
SELECT count(*) as number_distributed,st.id as specimen_type_id, dp.identifier as protocol_id,
concat(EXTRACT(YEAR from event_timestamp),ceil(EXTRACT(MONTH from event_timestamp)/3)) as report_period_id
FROM CATISSUE_DISTRIBUTION d,catissue_specimen_protocol dp,catissue_abstract_specimen a,
CATISSUE_DISTRIBUTED_ITEM di,catissue_specimen s , @@databasename@@.specimen_type st WHERE d.distribution_protocol_id = dp.identifier 
and di.distribution_id = d.identifier and s.identifier = di.specimen_id and a.SPECIMEN_TYPE= st.name and s.identifier = a.identifier 
group by st.id,concat(EXTRACT(YEAR from event_timestamp),ceil(EXTRACT(MONTH from event_timestamp)/3)),dp.identifier;
BEGIN
   OPEN REPORT_CUR;
   FETCH REPORT_CUR INTO NUM_DISTRIBUTED,SP_TYPE_ID,COLL_PROT_ID,REPORT_ID;
   LOOP
     IF REPORT_CUR%NOTFOUND THEN
	 	EXIT;
	 END IF;
	 INSERT INTO @@databasename@@.number_distributed VALUES(@@databasename@@.REPORTING_PERIOD_SEQ.NEXTVAL, NUM_DISTRIBUTED,SP_TYPE_ID,COLL_PROT_ID,REPORT_ID);
	 FETCH REPORT_CUR INTO NUM_DISTRIBUTED,SP_TYPE_ID,COLL_PROT_ID,REPORT_ID;
   END LOOP;
   CLOSE REPORT_CUR;
   commit;
END;
END; 
/