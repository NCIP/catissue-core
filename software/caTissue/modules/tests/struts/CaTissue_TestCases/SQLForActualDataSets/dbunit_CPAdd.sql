catissue_clinical_diagnosis;select CLINICAL_DIAGNOSIS from catissue_clinical_diagnosis where COLLECTION_PROTOCOL_ID in(select IDENTIFIER from catissue_specimen_protocol where TITLE='Tissue Collection Protocol')               
catissue_coll_coordinators;select USER_ID from catissue_coll_coordinators where COLLECTION_PROTOCOL_ID in(select IDENTIFIER from catissue_specimen_protocol where TITLE='Tissue Collection Protocol')
catissue_coll_prot_event;select COLLECTION_POINT_LABEL,STUDY_CALENDAR_EVENT_POINT from catissue_coll_prot_event where COLLECTION_PROTOCOL_ID in(select IDENTIFIER from catissue_specimen_protocol where TITLE='Tissue Collection Protocol')    
catissue_collection_protocol;select UNSIGNED_CONSENT_DOC_URL from catissue_collection_protocol where IDENTIFIER in(select IDENTIFIER from catissue_specimen_protocol where TITLE='Tissue Collection Protocol')              
catissue_consent_tier;select STATEMENT from catissue_consent_tier where COLL_PROTOCOL_ID in(select IDENTIFIER from catissue_specimen_protocol where TITLE='Tissue Collection Protocol')
catissue_specimen_protocol;select PRINCIPAL_INVESTIGATOR_ID,SHORT_TITLE,IRB_IDENTIFIER,START_DATE,ENROLLMENT,DESCRIPTION_URL,LABEL_FORMAT,DERIV_LABEL_FORMAT,ALIQUOT_LABEL_FORMAT from catissue_specimen_protocol where TITLE='Tissue Collection Protocol'                   
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='SHORT_TITLE'
catissue_audit_event;select EVENT_TYPE from catissue_audit_event











