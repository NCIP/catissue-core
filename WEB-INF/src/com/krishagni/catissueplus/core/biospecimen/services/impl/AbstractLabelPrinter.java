package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.common.domain.LabelPrintJobItem;
import com.krishagni.catissueplus.core.common.domain.LabelPrintJobItem.Status;
import com.krishagni.catissueplus.core.common.domain.LabelPrintRule;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.service.LabelPrinter;

public abstract class AbstractLabelPrinter<T> implements LabelPrinter<T> {
	//
	// format: <item_label>_<yyyyMMddHHmm>_<unique_os_run_num>_<copy>.txt
	// E.g. TCP-0003-S-01-P1_201604040807_1_1.txt, TCP-0003-S-01-P1_201604040807_1_2.txt etc
	//
	private static final String LABEL_FILENAME_FMT = "%s_%s_%d_%d.txt";

	private static final String TSTAMP_FMT = "yyyyMMddHHmm";

	private AtomicInteger uniqueNum = new AtomicInteger();

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
				(LabelPrintRule)labelData.get("rule"),
				(Map<String, String>)labelData.get("dataItems"));
		}
	}

	private void generateCmdFile(LabelPrintJobItem jobItem, LabelPrintRule rule, Map<String, String> dataItems) {
		if (StringUtils.isBlank(rule.getCmdFilesDir()) || rule.getCmdFilesDir().trim().equals("*")) {
			return;
		}

		try {
			String content = null;
			switch (rule.getCmdFileFmt()) {
				case CSV:
					content = getCommaSeparatedValueFields(dataItems);
					break;

				case KEY_VALUE:
					content = getKeyValueFields(dataItems);
					break;
			}

			writeToFile(jobItem, rule, content);
			jobItem.setStatus(Status.QUEUED);
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
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

	private void writeToFile(LabelPrintJobItem item, LabelPrintRule rule, String content)
	throws IOException {
		String tstamp = new SimpleDateFormat(TSTAMP_FMT).format(item.getJob().getSubmissionDate());
		int labelCount = uniqueNum.incrementAndGet();

		for (int i = 0; i < item.getCopies(); ++i) {
			String filename = String.format(LABEL_FILENAME_FMT, item.getItemLabel(), tstamp, labelCount, (i + 1));
			FileUtils.write(new File(rule.getCmdFilesDir(), filename), content);
		}
	}

}
