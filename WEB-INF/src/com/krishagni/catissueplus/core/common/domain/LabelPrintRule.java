package com.krishagni.catissueplus.core.common.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class LabelPrintRule {
	public enum CmdFileFmt {
		CSV("csv"),
		KEY_VALUE("key-value");

		private String fmt;

		private CmdFileFmt(String fmt) {
			this.fmt = fmt;
		}

		public static CmdFileFmt get(String input) {
			for (CmdFileFmt cfFmt : values()) {
				if (cfFmt.fmt.equals(input)) {
					return cfFmt;
				}
			}

			return null;
		}
	};

	private String labelType;
	
	private IpAddressMatcher ipAddressMatcher;
	
	private String userLogin;
	
	private String printerName;
	
	private String cmdFilesDir;

	private String labelDesign;

	private List<LabelTmplToken> dataTokens = new ArrayList<LabelTmplToken>();
	
	private MessageSource messageSource;

	private CmdFileFmt cmdFileFmt = CmdFileFmt.KEY_VALUE;

	public String getLabelType() {
		return labelType;
	}

	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}

	public IpAddressMatcher getIpAddressMatcher() {
		return ipAddressMatcher;
	}

	public void setIpAddressMatcher(IpAddressMatcher ipAddressMatcher) {
		this.ipAddressMatcher = ipAddressMatcher;
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public String getCmdFilesDir() {
		return cmdFilesDir;
	}

	public void setCmdFilesDir(String cmdFilesDir) {
		this.cmdFilesDir = cmdFilesDir;
	}

	public String getLabelDesign() {
		return labelDesign;
	}

	public void setLabelDesign(String labelDesign) {
		this.labelDesign = labelDesign;
	}

	public List<LabelTmplToken> getDataTokens() {
		return dataTokens;
	}

	public void setDataTokens(List<LabelTmplToken> dataTokens) {
		this.dataTokens = dataTokens;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public CmdFileFmt getCmdFileFmt() {
		return cmdFileFmt;
	}

	public void setCmdFileFmt(CmdFileFmt cmdFileFmt) {
		this.cmdFileFmt = cmdFileFmt;
	}

	public void setCmdFileFmt(String fmt) {
		this.cmdFileFmt = CmdFileFmt.get(fmt);
		if (this.cmdFileFmt == null) {
			throw new IllegalArgumentException("Invalid command file format: " + fmt);
		}
	}

	public boolean isApplicableFor(User user, String ipAddr) {
		if (!isWildCard(userLogin) && !user.getLoginName().equals(userLogin)) {
			return false;
		}
		
		if (ipAddressMatcher != null && !ipAddressMatcher.matches(ipAddr)) {
			return false;
		}
		
		return true;
	}
	
	public Map<String, String> getDataItems(PrintItem<?> printItem) {
		try {
			Map<String, String> dataItems = new LinkedHashMap<String, String>();
			
			if (!isWildCard(labelDesign)) {
				dataItems.put(getMessageStr("LABELDESIGN"), labelDesign);
			}

			if (!isWildCard(labelType)) {
				dataItems.put(getMessageStr("LABELTYPE"), labelType);
			}

			if (!isWildCard(printerName)) {
				dataItems.put(getMessageStr("PRINTER"), printerName);
			}
			
			if (printItem.getCopies() > 1) {
				dataItems.put(getMessageStr("COPIES"), String.valueOf(printItem.getCopies()));
			}
			
			for (LabelTmplToken token : dataTokens) {
				dataItems.put(getMessageStr(token.getName()), token.getReplacement(printItem.getObject()));
			}

			return dataItems;
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
		}
	}

	protected boolean isWildCard(String str) {
		return StringUtils.isNotBlank(str) && str.trim().equals("*");
	}

	private String getMessageStr(String name) {
		return messageSource.getMessage("print_" + name, null, Locale.getDefault());
	}
}
