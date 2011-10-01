-- catissue_institution schema changes
ALTER TABLE catissue_institution
	ADD COLUMN DIRTY_EDIT_FLAG TINYINT NULL AFTER NAME,
	ADD COLUMN REMOTE_IDENTIFIER BIGINT NULL AFTER DIRTY_EDIT_FLAG,
	ADD COLUMN REMOTE_MANAGED_FLAG TINYINT NULL AFTER REMOTE_IDENTIFIER;
	
ALTER TABLE catissue_institution add constraint REMOTE_IDENTIFIER UNIQUE KEY (REMOTE_IDENTIFIER);

-- catissue_user schema changes
ALTER TABLE catissue_user
	ADD COLUMN DIRTY_EDIT_FLAG TINYINT NULL,
	ADD COLUMN REMOTE_IDENTIFIER BIGINT NULL,
	ADD COLUMN REMOTE_MANAGED_FLAG TINYINT NULL;
	
ALTER TABLE catissue_user add constraint REMOTE_IDENTIFIER UNIQUE KEY (REMOTE_IDENTIFIER);

-- catissue_collection_protocol schema changes

ALTER TABLE catissue_collection_protocol
	ADD COLUMN DIRTY_EDIT_FLAG TINYINT NULL,
	ADD COLUMN REMOTE_IDENTIFIER BIGINT NULL,
	ADD COLUMN REMOTE_MANAGED_FLAG TINYINT NULL;
	
ALTER TABLE catissue_collection_protocol add constraint REMOTE_IDENTIFIER UNIQUE KEY (REMOTE_IDENTIFIER);

-- Search Grid changes for User entity
INSERT INTO `catissue_interface_column_data` ( `TABLE_ID`, `COLUMN_NAME`, `ATTRIBUTE_TYPE`) VALUES
 ( (select  cqtd.TABLE_ID from catissue_query_table_data cqtd	 where cqtd.ALIAS_NAME = 'User'), 'REMOTE_MANAGED_FLAG', 'tinyint');

 INSERT INTO `catissue_search_display_data` ( `RELATIONSHIP_ID`, `COL_ID`, `DISPLAY_NAME`, `DEFAULT_VIEW_ATTRIBUTE`, `ATTRIBUTE_ORDER`) 
VALUES (
			( select relationData.RELATIONSHIP_ID from CATISSUE_TABLE_RELATION relationData, CATISSUE_QUERY_TABLE_DATA tableData
				where relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				and tableData.ALIAS_NAME = 'User'
			), 
			(SELECT  columnData.IDENTIFIER
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				 and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				 and tableData.ALIAS_NAME = 'User'
				 and  columnData.COLUMN_NAME = 'REMOTE_MANAGED_FLAG'
			), 
			'RemoteManagedFlag', 
			1, 
			(SELECT  max(displayData.ATTRIBUTE_ORDER)+1
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData,  CATISSUE_SEARCH_DISPLAY_DATA displayData  
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID 
				 and  relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID 
				 and  columnData.IDENTIFIER = displayData.COL_ID 
				 and tableData.ALIAS_NAME = 'User'
			)
		);
	 	 
INSERT INTO `catissue_interface_column_data` ( `TABLE_ID`, `COLUMN_NAME`, `ATTRIBUTE_TYPE`) VALUES
 ( (select  cqtd.TABLE_ID from catissue_query_table_data cqtd	 where cqtd.ALIAS_NAME = 'User'), 'DIRTY_EDIT_FLAG', 'tinyint');

 INSERT INTO `catissue_search_display_data` ( `RELATIONSHIP_ID`, `COL_ID`, `DISPLAY_NAME`, `DEFAULT_VIEW_ATTRIBUTE`, `ATTRIBUTE_ORDER`) 
VALUES (
			( select relationData.RELATIONSHIP_ID from CATISSUE_TABLE_RELATION relationData, CATISSUE_QUERY_TABLE_DATA tableData
				where relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				and tableData.ALIAS_NAME = 'User'
			), 
			(SELECT  columnData.IDENTIFIER
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				 and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				 and tableData.ALIAS_NAME = 'User'
				 and  columnData.COLUMN_NAME = 'DIRTY_EDIT_FLAG'
			), 
			'DirtyEditFlag', 
			1, 
			(SELECT  max(displayData.ATTRIBUTE_ORDER)+1
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData,  CATISSUE_SEARCH_DISPLAY_DATA displayData  
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID 
				 and  relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID 
				 and  columnData.IDENTIFIER = displayData.COL_ID 
				 and tableData.ALIAS_NAME = 'User'
			)
		);
	 	 
-- Search Grid changes for Institution entity		 
INSERT INTO `catissue_interface_column_data` ( `TABLE_ID`, `COLUMN_NAME`, `ATTRIBUTE_TYPE`) VALUES
 ( (select  cqtd.TABLE_ID from catissue_query_table_data cqtd	 where cqtd.ALIAS_NAME = 'Institution'), 'REMOTE_MANAGED_FLAG', 'tinyint');

 INSERT INTO `catissue_search_display_data` ( `RELATIONSHIP_ID`, `COL_ID`, `DISPLAY_NAME`, `DEFAULT_VIEW_ATTRIBUTE`, `ATTRIBUTE_ORDER`) 
VALUES (
			( select relationData.RELATIONSHIP_ID from CATISSUE_TABLE_RELATION relationData, CATISSUE_QUERY_TABLE_DATA tableData
				where relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				and tableData.ALIAS_NAME = 'Institution'
			), 
			(SELECT  columnData.IDENTIFIER
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				 and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				 and tableData.ALIAS_NAME = 'Institution'
				 and  columnData.COLUMN_NAME = 'REMOTE_MANAGED_FLAG'
			), 
			'RemoteManagedFlag', 
			1, 
			(SELECT  max(displayData.ATTRIBUTE_ORDER)+1
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData,  CATISSUE_SEARCH_DISPLAY_DATA displayData  
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID 
				 and  relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID 
				 and  columnData.IDENTIFIER = displayData.COL_ID 
				 and tableData.ALIAS_NAME = 'Institution'
			)
		);
	 	 
INSERT INTO `catissue_interface_column_data` ( `TABLE_ID`, `COLUMN_NAME`, `ATTRIBUTE_TYPE`) VALUES
 ( (select  cqtd.TABLE_ID from catissue_query_table_data cqtd	 where cqtd.ALIAS_NAME = 'Institution'), 'DIRTY_EDIT_FLAG', 'tinyint');

 INSERT INTO `catissue_search_display_data` ( `RELATIONSHIP_ID`, `COL_ID`, `DISPLAY_NAME`, `DEFAULT_VIEW_ATTRIBUTE`, `ATTRIBUTE_ORDER`) 
VALUES (
			( select relationData.RELATIONSHIP_ID from CATISSUE_TABLE_RELATION relationData, CATISSUE_QUERY_TABLE_DATA tableData
				where relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				and tableData.ALIAS_NAME = 'Institution'
			), 
			(SELECT  columnData.IDENTIFIER
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				 and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				 and tableData.ALIAS_NAME = 'Institution'
				 and  columnData.COLUMN_NAME = 'DIRTY_EDIT_FLAG'
			), 
			'DirtyEditFlag', 
			1, 
			(SELECT  max(displayData.ATTRIBUTE_ORDER)+1
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData,  CATISSUE_SEARCH_DISPLAY_DATA displayData  
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID 
				 and  relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID 
				 and  columnData.IDENTIFIER = displayData.COL_ID 
				 and tableData.ALIAS_NAME = 'Institution'
			)
		);

-- Search Grid changes for CollectionProtocol entity
INSERT INTO `catissue_interface_column_data` ( `TABLE_ID`, `COLUMN_NAME`, `ATTRIBUTE_TYPE`) VALUES
 ( (select  cqtd.TABLE_ID from catissue_query_table_data cqtd	 where cqtd.ALIAS_NAME = 'CollectionProtocol'), 'REMOTE_MANAGED_FLAG', 'tinyint');

 INSERT INTO `catissue_search_display_data` ( `RELATIONSHIP_ID`, `COL_ID`, `DISPLAY_NAME`, `DEFAULT_VIEW_ATTRIBUTE`, `ATTRIBUTE_ORDER`) 
VALUES (
			( select relationData.RELATIONSHIP_ID from CATISSUE_TABLE_RELATION relationData, CATISSUE_QUERY_TABLE_DATA tableData
				where relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				and tableData.ALIAS_NAME = 'CollectionProtocol'
			), 
			(SELECT  columnData.IDENTIFIER
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				 and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				 and tableData.ALIAS_NAME = 'CollectionProtocol'
				 and  columnData.COLUMN_NAME = 'REMOTE_MANAGED_FLAG'
			), 
			'RemoteManagedFlag', 
			1, 
			(SELECT  max(displayData.ATTRIBUTE_ORDER)+1
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData,  CATISSUE_SEARCH_DISPLAY_DATA displayData  
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID 
				 and  relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID 
				 and  columnData.IDENTIFIER = displayData.COL_ID 
				 and tableData.ALIAS_NAME = 'CollectionProtocol'
			)
		);
	 	 
INSERT INTO `catissue_interface_column_data` ( `TABLE_ID`, `COLUMN_NAME`, `ATTRIBUTE_TYPE`) VALUES
 ( (select  cqtd.TABLE_ID from catissue_query_table_data cqtd	 where cqtd.ALIAS_NAME = 'CollectionProtocol'), 'DIRTY_EDIT_FLAG', 'tinyint');

 INSERT INTO `catissue_search_display_data` ( `RELATIONSHIP_ID`, `COL_ID`, `DISPLAY_NAME`, `DEFAULT_VIEW_ATTRIBUTE`, `ATTRIBUTE_ORDER`) 
VALUES (
			( select relationData.RELATIONSHIP_ID from CATISSUE_TABLE_RELATION relationData, CATISSUE_QUERY_TABLE_DATA tableData
				where relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				and tableData.ALIAS_NAME = 'CollectionProtocol'
			), 
			(SELECT  columnData.IDENTIFIER
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID
				 and relationData.CHILD_TABLE_ID =  tableData.TABLE_ID
				 and tableData.ALIAS_NAME = 'CollectionProtocol'
				 and  columnData.COLUMN_NAME = 'DIRTY_EDIT_FLAG'
			), 
			'DirtyEditFlag', 
			1, 
			(SELECT  max(displayData.ATTRIBUTE_ORDER)+1
				 FROM CATISSUE_INTERFACE_COLUMN_DATA columnData,  CATISSUE_TABLE_RELATION relationData,  CATISSUE_QUERY_TABLE_DATA tableData,  CATISSUE_SEARCH_DISPLAY_DATA displayData  
				 where relationData.CHILD_TABLE_ID = columnData.TABLE_ID 
				 and  relationData.PARENT_TABLE_ID = tableData.TABLE_ID 
				 and  relationData.RELATIONSHIP_ID = displayData.RELATIONSHIP_ID 
				 and  columnData.IDENTIFIER = displayData.COL_ID 
				 and tableData.ALIAS_NAME = 'CollectionProtocol'
			)
		);
	 	 		