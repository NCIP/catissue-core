DROP PROCEDURE IF EXISTS migrate //
create PROCEDURE migrate()
begin
 
 DECLARE currentId, iterator, pageSize BIGINT;  
 DECLARE endOfTable BOOLEAN;
 DECLARE ancestors, nextGen, sqlValues TEXT;
 
 DECLARE curs CURSOR FOR SELECT IDENTIFIER FROM os_storage_containers WHERE PARENT_container_ID IS NULL;
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET endOfTable := TRUE;
 
 SET endOfTable = FALSE;
 SET ancestors = '';
 SET nextGen = '';
 SET sqlValues = '';
 SET pageSize = 150;
 SET @sql_store = ''; 
 SET @@SESSION.max_sp_recursion_depth=1024;
 
 DELETE FROM os_containers_hierarchy;

 SELECT 'Migration in progress..' as Message; 
 
 OPEN curs;
 WHILE NOT endOfTable DO
  SET iterator = 0;
 
  WHILE (iterator < pageSize) AND (NOT endOfTable) DO 
   SET iterator = iterator + 1;
   FETCH curs into currentId;

   IF NOT endOfTable THEN
    IF LENGTH(ancestors) > 0 THEN
     SET ancestors = CONCAT( ancestors , ',');
    END IF;
  
    SET ancestors = CONCAT( ancestors , CONVERT(currentId , CHAR(50)));
   END IF;
  END WHILE;
 
  CALL createHierarchy(nextGen,ancestors,sqlValues);
 
  IF LENGTH(@sql_store) > 0 THEN
   SET @sql_stmt = CONCAT( 'insert into os_containers_hierarchy(ancestor_id, descendent_id) values ' , @sql_store );
   PREPARE stmt3 from @sql_stmt;
   EXECUTE stmt3;
   DEALLOCATE PREPARE stmt3;
   SET @sql_store = '';
  END IF; 
 
  SET ancestors = '';

 END WHILE;
 CLOSE curs;

END
//


DROP PROCEDURE IF EXISTS getChildrenOf //
create PROCEDURE getChildrenOf(IN nodeId TEXT, OUT children TEXT)
begin

 DECLARE CURRENT_ID BIGINT;
 DECLARE end_of_table BOOLEAN;
 DECLARE temp TEXT;

 DECLARE curs CURSOR FOR SELECT identifier from os_storage_containers where parent_container_ID = CONVERT(nodeId, UNSIGNED INTEGER);
 DECLARE CONTINUE HANDLER FOR NOT FOUND SET end_of_table := TRUE;

 SET end_of_table = FALSE;
 SET children = '';

 OPEN curs;
 WHILE NOT end_of_table DO
  FETCH curs into CURRENT_ID;

  IF NOT end_of_table THEN
   SET temp = CONVERT(CURRENT_ID, CHAR(50));
   SET children = CONCAT(children, temp);
   SET children = CONCAT(children, ',' );
  END IF;

 END WHILE;
 CLOSE curs;

 IF LENGTH(children) > 1 THEN
  SET children = SUBSTRING(children, 1, LENGTH(children)-1);
 END IF;

END
//



DROP PROCEDURE IF EXISTS getIdCount //
create PROCEDURE getIdCount(IN input TEXT, OUT idCount INT)
begin
 DECLARE str TEXT;

 SET str = input;
 SET idCount = LENGTH(str) - LENGTH(REPLACE(str, ',' , '')) + 1;
 
 IF LENGTH(str) = 0 THEN
  SET idCount = 0;
 END IF;

END
//


DROP PROCEDURE IF EXISTS getIdByIndex //
create PROCEDURE getIdByIndex(IN str TEXT, IN pos INT, OUT id BIGINT)
begin

 DECLARE result TEXT;
 SET result = REPLACE(SUBSTRING(SUBSTRING_INDEX(str, ',', pos),LENGTH(SUBSTRING_INDEX(str, ',', pos -1)) + 1),',', '');
 SET id = CONVERT(result, UNSIGNED INTEGER);

END
//



DROP PROCEDURE IF EXISTS createHierarchy //
create PROCEDURE createHierarchy(IN ancestors TEXT, IN currentNodes TEXT, INOUT sqlValues TEXT)
begin

 DECLARE nodeIterator, ancestorIterator, numNodes, numAncestors, currentAncestorId ,currentNodeId BIGINT;
 DECLARE currentChildren,extendedAncestors TEXT;
 SET nodeIterator = 1;
 CALL getIdCount(currentNodes,numNodes);

 WHILE nodeIterator <= numNodes DO
  CALL getIdByIndex(currentNodes, nodeIterator, currentNodeId );
  CALL buildSql(sqlValues, currentNodeId, currentNodeId );

  CALL getIdCount(ancestors,numAncestors);
  SET ancestorIterator = 1;
  
  WHILE ancestorIterator <= numAncestors DO
   CALL getIdByIndex(ancestors,ancestorIterator,currentAncestorId);
   CALL buildSql(sqlValues, currentAncestorId, currentNodeId );
   SET ancestorIterator = ancestorIterator + 1;
  END WHILE;
 
  SET currentChildren = '';
  SET extendedAncestors = '';
  CALL getChildrenOf(currentNodeId,currentChildren);  

  IF LENGTH(ancestors) = 0 THEN 
   SET extendedAncestors = CONVERT(currentNodeId, CHAR(50));
  ELSE
   SET extendedAncestors = CONCAT(ancestors, ',' );
   SET extendedAncestors = CONCAT(extendedAncestors, CONVERT(currentNodeId, CHAR(50)));
  END IF;

  CALL createHierarchy(extendedAncestors,currentChildren,sqlValues);

  IF LENGTH(ancestors) = 0 THEN

   IF LENGTH(@sql_store) > 0 THEN 
    SET @sql_store = CONCAT(@sql_store , ',');
   END IF;
  
   SET @sql_store = CONCAT(@sql_store , sqlValues);

   IF LENGTH(@sql_store) > 3700 THEN
    SET @sql_stmt = CONCAT( 'insert into os_containers_hierarchy(ancestor_id, descendent_id) values ' , @sql_store );
    PREPARE stmt3 from @sql_stmt;
    EXECUTE stmt3;
    DEALLOCATE PREPARE stmt3;
    SET @sql_store = '';
   END IF;
   
   SET sqlValues = '';
 
  END IF;

  SET nodeIterator = nodeIterator+1;
 END WHILE; 

END
//



DROP PROCEDURE IF EXISTS buildSql //
create PROCEDURE buildSql(INOUT buildString TEXT, IN ancestor_id BIGINT, IN descendent_id BIGINT)
begin

 IF LENGTH(buildString) > 0 THEN
  SET buildString = CONCAT( buildString , ',' );
 END IF;
 
 SET buildString = CONCAT( buildString , '(' );
 SET buildString = CONCAT( buildString , CONVERT(ancestor_id , CHAR(50)));
 SET buildString = CONCAT( buildString , ',' );
 SET buildString = CONCAT( buildString , CONVERT(descendent_id , CHAR(50)));
 SET buildString = CONCAT( buildString , ')' );

end
//


set autocommit=1//
CALL migrate()//


