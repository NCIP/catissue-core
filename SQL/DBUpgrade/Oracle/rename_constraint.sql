-- Rename Constraint

DECLARE
 isConstraintPresent PLS_INTEGER;
 sqlStmt VARCHAR2(200);
BEGIN
  SELECT COUNT(*)
  INTO isConstraintPresent
  FROM  all_constraints
  WHERE constraint_name = 'FK_CATISSUE_COLL_PROT_EVENT'
	AND table_name='CATISSUE_SPECIMEN_COLL_GROUP' AND owner = USER;

  IF isConstraintPresent != 0 THEN
     sqlStmt := 'ALTER TABLE CATISSUE_SPECIMEN_COLL_GROUP DROP constraint FK_CATISSUE_COLL_PROT_EVENT';
    EXECUTE IMMEDIATE sqlStmt;
	
	 sqlStmt := 'ALTER TABLE CATISSUE_SPECIMEN_COLL_GROUP  ADD constraint FKDEBAF16753B01F66 foreign key 
		    (COLLECTION_PROTOCOL_EVENT_ID) references CATISSUE_COLL_PROT_EVENT';
    EXECUTE IMMEDIATE sqlStmt;
	
  END IF;
END;
/