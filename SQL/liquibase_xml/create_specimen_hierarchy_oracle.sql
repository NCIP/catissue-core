CREATE OR REPLACE PROCEDURE migrate 
IS currentId NUMBER(20);
   pageSize NUMBER(20);
   pageIterator NUMBER(20);
   pageContent CLOB;
   sqlValues CLOB;
   sqlStatement CLOB;
   ancestors CLOB;
   finalStatement CLOB;
   currentTime CLOB;
BEGIN

 SELECT TO_CHAR(SYSTIMESTAMP) INTO currentTime FROM dual;
 DBMS_OUTPUT.put_line('EXECUTION STARTED AT: ' || currentTime );
  
 pageSize := 100;
 pageContent := '';
 sqlValues := '';
 pageIterator := 1;
 ancestors := '';
 finalStatement := '';
 sqlStatement := '';

 safeDropTable('CATISSUE_SPECIMEN_HIERARCHY');
 EXECUTE IMMEDIATE 'CREATE TABLE CATISSUE_SPECIMEN_HIERARCHY (ANCESTOR_ID NUMBER(20) , DESCENDENT_ID NUMBER(20))';

 FOR nodes IN (SELECT identifier FROM catissue_specimen where PARENT_SPECIMEN_ID IS NULL)
 LOOP
  pageIterator := pageIterator + 1; 

  IF (DBMS_LOB.GETLENGTH(pageContent) IS NOT NULL) THEN
   pageContent := pageContent || ',';
  END IF;

  pageContent := pageContent || nodes.identifier;
  
  IF (pageIterator > pageSize) THEN 
   pageIterator := 1;
   createHierarchy(ancestors, pageContent, sqlValues , sqlStatement);   
  
   IF (DBMS_LOB.GETLENGTH(sqlStatement) IS NOT NULL ) THEN
    finalStatement := 'INSERT ALL ' || sqlStatement ;
    finalStatement := finalStatement || ' SELECT 1 FROM DUAL';
    EXECUTE IMMEDIATE finalStatement;
    COMMIT;
    sqlStatement := '';
   END IF;
 
    
   pageContent := '';
  END IF;

 END LOOP;

 IF (DBMS_LOB.GETLENGTH(pageContent) IS NOT NULL ) THEN
  createHierarchy(ancestors, pageContent, sqlValues , sqlStatement);
  IF (DBMS_LOB.GETLENGTH(sqlStatement) IS NOT NULL ) THEN
   finalStatement := 'INSERT ALL ' || sqlStatement ;
   finalStatement := finalStatement || ' SELECT 1 FROM DUAL';
   EXECUTE IMMEDIATE finalStatement;
   COMMIT;
   sqlStatement := '';
  END IF;
 END IF;

 SELECT TO_CHAR(SYSTIMESTAMP) INTO currentTime FROM dual;
 DBMS_OUTPUT.put_line('EXECUTION COMPLETE AT: ' || currentTime );
 COMMIT;
END;
/



CREATE OR REPLACE PROCEDURE safeDropTable(tableName IN varchar2)
IS
  tableDoesntExists exception;
  pragma exception_init(tableDoesntExists, -942);
  tab varchar2(32);
BEGIN
  
  tab := dbms_assert.simple_sql_name(tableName);

  EXECUTE IMMEDIATE 'DROP TABLE ' || tab;
  DBMS_OUTPUT.put_line('THE TABLE ' || tab || ' HAS BEEN DROPPED SUCCESSFULLY!' );

EXCEPTION
  WHEN tableDoesntExists
  THEN DBMS_OUTPUT.put_line('TABLE ' || tab || ' DOESNT EXIST, DROPPING NOT REQUIRED!');
END;
/


CREATE OR REPLACE FUNCTION getCountOfIds(str IN CLOB) RETURN INTEGER 
IS 
BEGIN 
 RETURN DBMS_LOB.GETLENGTH(str) - DBMS_LOB.GETLENGTH(REPLACE(str , ',' , NULL)) + 1;
END;
/


CREATE OR REPLACE FUNCTION getIdByIndex(str IN CLOB, pos number) RETURN NUMBER
IS result CLOB;
   startPos NUMBER(20);
   len NUMBER(20);
   endPos NUMBER(20);
BEGIN 

 IF pos = 1 THEN
  startPos := 1;
 ELSE
  startPos := instr(str,',',1,pos-1) + 1;
 END IF;

 endPos := instr(str,',',1,pos);
 len := endPos - startPos;

 IF (endPos < startPos) THEN    
  len := DBMS_LOB.GETLENGTH(str) - startPos + 1;
 END IF;
 
 return TO_NUMBER(substr(str, startPos, len));
END;
/


CREATE OR REPLACE PROCEDURE createHierarchy(ancestors IN CLOB, currentNodes IN CLOB, sqlValues IN OUT CLOB, sqlStatement IN OUT CLOB)
IS startPos NUMBER(20);
   nodeIterator NUMBER(20);
   ancestorIterator NUMBER(20);
   numNodes NUMBER(20);
   numAncestors NUMBER(20);
   currentAncestorId NUMBER(20);
   currentNodeId NUMBER(20);
   currentChildren CLOB;
   extendedAncestors CLOB;
   finalStatement CLOB;
BEGIN
 nodeIterator := 1;
 numNodes := getCountOfIds(currentNodes);

 WHILE nodeIterator <= numNodes 
 LOOP
  
  currentNodeId := getIdByIndex(currentNodes, nodeIterator );
  sqlValues := buildSql(sqlValues, currentNodeId, currentNodeId );

  numAncestors := getCountOfIds(ancestors);
  ancestorIterator := 1;

  WHILE ancestorIterator <= numAncestors
  LOOP
   currentAncestorId := getIdByIndex(ancestors,ancestorIterator);
   sqlValues := buildSql(sqlValues, currentAncestorId, currentNodeId);
   ancestorIterator := ancestorIterator + 1;
  END LOOP;

  currentChildren := '';
  extendedAncestors := '';

  currentChildren := getChildrenOf(currentNodeId);
  
  IF (DBMS_LOB.GETLENGTH(ancestors) IS NULL) THEN
   extendedAncestors := TO_CHAR(currentNodeId);
  ELSE 
   extendedAncestors := ancestors || ',' ;
   extendedAncestors := extendedAncestors || TO_CHAR(currentNodeId);
  END IF;

  createHierarchy(extendedAncestors,currentChildren,sqlValues,sqlStatement);

  IF (DBMS_LOB.GETLENGTH(ancestors) IS NULL) THEN
   
   sqlStatement := sqlStatement || sqlValues; 
   
   IF (DBMS_LOB.GETLENGTH(sqlStatement)>10000) THEN
    finalStatement := 'INSERT ALL ' || sqlStatement ;
    finalStatement := finalStatement || ' SELECT 1 FROM DUAL';
    EXECUTE IMMEDIATE finalStatement;
    COMMIT;
    sqlStatement := '';
   END IF;

   sqlValues := '';
  END IF;

  nodeIterator := nodeIterator  + 1;
 END LOOP;
 
END;
/


CREATE OR REPLACE FUNCTION buildSql(str IN OUT CLOB, ancestorId IN NUMBER, descendentId IN NUMBER) return CLOB
IS 
BEGIN

 str := str || ' INTO CATISSUE_SPECIMEN_HIERARCHY (ANCESTOR_ID, DESCENDENT_ID) VALUES ';
 str := str || '(';
 str := str || TO_CHAR(ancestorId);
 str := str || ',';
 str := str || TO_CHAR(descendentId);
 str := str || ')';

 return str;
END;
/


CREATE OR REPLACE FUNCTION getChildrenOf(ancestor IN NUMBER) return CLOB
IS 
   children CLOB;
BEGIN
 children := '';

 FOR nodes IN (SELECT identifier FROM CATISSUE_SPECIMEN WHERE PARENT_SPECIMEN_ID = ancestor)
 LOOP
  children := children || nodes.identifier;
  children := children || ',';
 END LOOP;
 
 IF (DBMS_LOB.GETLENGTH(children)>1) THEN
  children := substr(children , 1 ,  DBMS_LOB.GETLENGTH(children)-1);
 END IF;

 return children;
END; 
/


