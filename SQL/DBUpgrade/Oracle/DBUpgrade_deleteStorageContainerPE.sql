/* To remove unnecessary protection elements of StorageContainer */
delete  
FROM csm_protection_element
WHERE regexp_like(object_id,  '_[[:digit:]]')
   AND NOT regexp_like(object_id,   'Site_[[:digit:]]')
   AND NOT regexp_like(object_id,   'CollectionProtocol_[[:digit:]]')
   AND NOT regexp_like(object_id,   'User_[[:digit:]]');
  
