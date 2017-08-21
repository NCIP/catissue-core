CREATE OR REPLACE PROCEDURE migrate 
IS currentId NUMBER(20);
   pageSize NUMBER(20);
   pageIterator NUMBER(20);
   pageContent CLOB;
   sqlStatement CLOB;
   ancestors CLOB;
   finalStatement CLOB;
   currentTime CLOB;
BEGIN

 SELECT TO_CHAR(SYSTIMESTAMP) INTO currentTime FROM dual;
 DBMS_OUTPUT.put_line('EXECUTION STARTED AT: ' || currentTime );
  
 pageSize := 100;
 pageContent := '';
 pageIterator := 1;
 ancestors := '';
 finalStatement := '';
 sqlStatement := '';

 FOR nodes IN (SELECT identifier FROM os_storage_containers where parent_container_id IS NULL)
 LOOP
  pageIterator := pageIterator + 1; 

  IF (DBMS_LOB.GETLENGTH(pageContent) IS NOT NULL) THEN
   pageContent := pageContent || ',';
  END IF;

  pageContent := pageContent || nodes.identifier;
  
  IF (pageIterator > pageSize) THEN 
   pageIterator := 1;
   createHierarchy(ancestors, pageContent, sqlStatement);   
  
   IF (DBMS_LOB.GETLENGTH(sqlStatement) IS NOT NULL ) THEN
    finalStatement := 'INSERT ALL ' || sqlStatement ;
    finalStatement := finalStatement || ' select 1 from dual';
    EXECUTE IMMEDIATE TO_CHAR(finalStatement);
    COMMIT;
    sqlStatement := '';
   END IF;
 
    
   pageContent := '';
  END IF;

 END LOOP;

 IF (DBMS_LOB.GETLENGTH(pageContent) IS NOT NULL ) THEN
  createHierarchy(ancestors, pageContent, sqlStatement);
  IF (DBMS_LOB.GETLENGTH(sqlStatement) IS NOT NULL ) THEN
   finalStatement := 'INSERT ALL ' || sqlStatement ;
   finalStatement := finalStatement || ' select 1 from dual';
   EXECUTE IMMEDIATE TO_CHAR(finalStatement);
   COMMIT;
   sqlStatement := '';
  END IF;
 END IF;

 SELECT TO_CHAR(SYSTIMESTAMP) INTO currentTime FROM dual;
 DBMS_OUTPUT.put_line('EXECUTION COMPLETE AT: ' || currentTime );
 COMMIT;
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


CREATE OR REPLACE PROCEDURE createHierarchy(ancestors IN CLOB, currentNodes IN CLOB, sqlStatement IN OUT CLOB)
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
  sqlStatement := buildSql(sqlStatement, currentNodeId, currentNodeId );

  numAncestors := getCountOfIds(ancestors);
  ancestorIterator := 1;

  WHILE ancestorIterator <= numAncestors
  LOOP
   currentAncestorId := getIdByIndex(ancestors,ancestorIterator);
   sqlStatement := buildSql(sqlStatement, currentAncestorId, currentNodeId);
   ancestorIterator := ancestorIterator + 1;
  END LOOP;

  IF ((DBMS_LOB.GETLENGTH(sqlStatement) IS NOT NULL) AND (DBMS_LOB.GETLENGTH(sqlStatement) > 10000)) THEN
   finalStatement := 'INSERT ALL ' || sqlStatement ;
   finalStatement := finalStatement || ' select 1 from dual';
   EXECUTE IMMEDIATE TO_CHAR(finalStatement);
   COMMIT;
   sqlStatement := '';
  END IF;

  currentChildren := '';
  extendedAncestors := '';

  currentChildren := getChildrenOf(currentNodeId);
  
  IF (DBMS_LOB.GETLENGTH(ancestors) IS NULL) THEN
   extendedAncestors := TO_CHAR(currentNodeId);
  ELSE 
   extendedAncestors := ancestors || ',' ;
   extendedAncestors := extendedAncestors || TO_CHAR(currentNodeId);
  END IF;

  createHierarchy(extendedAncestors,currentChildren,sqlStatement);

  IF ((DBMS_LOB.GETLENGTH(sqlStatement) IS NOT NULL) AND (DBMS_LOB.GETLENGTH(sqlStatement) > 10000)) THEN
   finalStatement := 'INSERT ALL ' || sqlStatement ;
   finalStatement := finalStatement || ' select 1 from dual';
   EXECUTE IMMEDIATE TO_CHAR(finalStatement);
   COMMIT;
   sqlStatement := '';
  END IF;

  nodeIterator := nodeIterator  + 1;
 END LOOP;
 
END;
/


CREATE OR REPLACE FUNCTION buildSql(str IN OUT CLOB, ancestorId IN NUMBER, descendentId IN NUMBER) return CLOB
IS 
BEGIN

 str := str || ' into os_containers_hierarchy (ancestor_id, descendent_id) values ';
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

 FOR nodes IN (SELECT identifier FROM os_storage_containers WHERE parent_container_id = ancestor)
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


