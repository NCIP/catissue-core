catissue_order;select NAME,STATUS from catissue_order where IDENTIFIER=201
catissue_distribution;select TO_SITE_ID,DISTRIBUTION_PROTOCOL_ID from catissue_distribution where ORDER_ID IN(select IDENTIFIER from catissue_order where NAME='Order_201')
catissue_audit_event_details;select ELEMENT_NAME,CURRENT_VALUE from catissue_audit_event_details where ELEMENT_NAME='NAME'
catissue_audit_event;SELECT EVENT_TYPE FROM catissue_audit_event 