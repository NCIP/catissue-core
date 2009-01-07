/* To remove unnecessary protection elements of Storage Container*/
delete
FROM csm_protection_element
WHERE object_id regexp '_[[:digit:]]'
    AND object_id NOT regexp 'Site_[[:digit:]]'
 	AND object_id NOT regexp 'CollectionProtocol_[[:digit:]]'
    AND object_id NOT regexp 'User_[[:digit:]]'
    AND object_id NOT LIKE 'SITE_%';