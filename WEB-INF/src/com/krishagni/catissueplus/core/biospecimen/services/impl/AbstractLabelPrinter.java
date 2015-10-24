package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.common.domain.LabelPrintJobItem;
import com.krishagni.catissueplus.core.common.domain.LabelPrintJobItem.Status;
import com.krishagni.catissueplus.core.common.domain.LabelPrintRule;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.LabelPrinter;

public abstract class AbstractLabelPrinter<T> implements LabelPrinter<T> {

	protected Map<String, Object> makeLabelData(LabelPrintJobItem item, LabelPrintRule rule, Map<String, String> dataItems) {
		Map<String, Object> labelData = new HashMap<String, Object>();
		labelData.put("jobItem", item);
		labelData.put("rule", rule);
		labelData.put("dataItems", dataItems);
		return labelData;
	}

	@SuppressWarnings("unchecked")
	protected void generateCmdFiles(List<Map<String, Object>> labelDataList) {
		for (Map<String, Object> labelData : labelDataList) {
			generateCmdFile(
				(LabelPrintJobItem)labelData.get("jobItem"),
				(SpecimenLabelPrintRule)labelData.get("rule"),
				(Map<String, String>)labelData.get("dataItems"));
		}
	}

	private void generateCmdFile(LabelPrintJobItem jobItem, LabelPrintRule rule, Map<String, String> dataItems) {
		if (StringUtils.isBlank(rule.getCmdFilesDir()) || rule.getCmdFilesDir().trim().equals("*")) {
			return;
		}

		PrintWriter writer = null;
		try {
			String file = rule.getCmdFilesDir() + File.separator + jobItem.getItemLabel() + ".cmd";
			writer = new PrintWriter(new FileWriter(file));

			switch (rule.getCmdFileFmt()) {
				case CSV:
					writer.println(getCommaSeparatedValueFields(dataItems));
					break;

				case KEY_VALUE:
					writer.println(getKeyValueFields(dataItems));
					break;
			}

			jobItem.setStatus(Status.QUEUED);
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	private String getCommaSeparatedValueFields(Map<String, String> dataItems) {
		StringBuilder content = new StringBuilder();
		for (String dataItem : dataItems.values()) {
			content.append("\"").append(dataItem).append("\",");
		}

		if (!dataItems.isEmpty()) {
			content.deleteCharAt(content.length() - 1);
		}

		return content.toString();
	}

	private String getKeyValueFields(Map<String, String> dataItems) {
		StringBuilder content = new StringBuilder();
		for (Map.Entry<String, String> dataItem : dataItems.entrySet()) {
			content.append(String.format("%s=%s\n", dataItem.getKey(), dataItem.getValue()));
		}

		if (!dataItems.isEmpty()) {
			content.deleteCharAt(content.length() - 1);
		}

		return content.toString();
	}
}
