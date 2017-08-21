package com.krishagni.catissueplus.core.query;

import java.util.Collection;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public interface ListGenerator {
	ListDetail getList(ListConfig cfg, List<Column> searchCriteria);

	int getListSize(ListConfig cfg, List<Column> searchCriteria);

	List<Column> getFilters(ListConfig cfg);

	Collection<Object> getExpressionValues(ListConfig cfg, String expr, String searchTerm);
}
