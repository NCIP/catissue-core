/*Adding index to solve performance issue while loading page for query dashboard bug :14379*/
CREATE INDEX INDX_CAT_AUDIT_QUERY_QUERYID ON CATISSUE_AUDIT_EVENT_QUERY_LOG (QUERY_ID);
/*Adding index on columns which are queried frequently - bug : 14603 */
CREATE INDEX INDX_CAT_SPEC_ARRAY_AVA ON CATISSUE_SPECIMEN_ARRAY (AVAILABLE);
CREATE INDEX INDX_CAT_SITE_TYPE ON CATISSUE_SITE (TYPE);
