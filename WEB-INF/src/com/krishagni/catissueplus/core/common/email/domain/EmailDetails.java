
package com.krishagni.catissueplus.core.common.email.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailDetails {

	private List<String> toAddress;

	private List<String> ccAddress;

	private List<String> bccAddress;

	private String subject;

	private String body;

	private boolean htmlBody = false;

	public List<File> attachments;

	public List<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	public EmailDetails() {
		toAddress = new ArrayList<String>();
	}

	public void addToAddress(String toAddress) {
		this.toAddress.add(toAddress);
	}

	public void removeToAddress(String toAddress) {
		this.toAddress.remove(toAddress);
	}

	public void setToAddress(String[] toAddress) {
		this.toAddress = Arrays.asList(toAddress);
	}

	public Collection<String> getToAddress() {
		return this.toAddress;
	}

	public InternetAddress[] getToInternetAddrArray() throws AddressException {
		return convertToInternetAddrArray(toAddress);
	}

	public void addCcAddress(String ccAddress) {
		if (null == this.ccAddress) {
			this.ccAddress = new ArrayList<String>();
		}
		this.ccAddress.add(ccAddress);
	}

	/**
	* Remove from carbon copy(cc) recipient address list.
	* @param ccAddress -Address to remove from recipient address list.
	*/
	public void removeCcAddress(String ccAddress) {
		this.ccAddress.remove(ccAddress);
	}

	public void setCcAddress(String[] ccAddress) {
		this.ccAddress = Arrays.asList(ccAddress);
	}

	public Collection<String> getCcAddress() {
		return ccAddress;
	}

	public InternetAddress[] getCcInternetAddrArray() throws AddressException {
		return convertToInternetAddrArray(ccAddress);
	}

	public void addBccAddress(String bccAddress) {
		if (null == this.bccAddress) {
			this.bccAddress = new ArrayList<String>();
		}
		this.bccAddress.add(bccAddress);
	}

	public void removeBccAddress(String bccAddress) {
		this.bccAddress.remove(bccAddress);
	}

	public void setBccAddress(String[] bccAddress) {
		this.bccAddress = Arrays.asList(bccAddress);
	}

	public Collection<String> getBccAddress() {
		return bccAddress;
	}

	public InternetAddress[] getBccInternetAddrArray() throws AddressException {
		return convertToInternetAddrArray(bccAddress);
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isHtmlBody() {
		return htmlBody;
	}

	public void setHtmlBody(boolean htmlBody) {
		this.htmlBody = htmlBody;
	}

	private InternetAddress[] convertToInternetAddrArray(List<String> emailAddrList) throws AddressException {
		List<InternetAddress> internetAddrList = new ArrayList<InternetAddress>();
		if (emailAddrList != null) {
			for (String emailAddr : emailAddrList) {
				internetAddrList.add(new InternetAddress(emailAddr));
			}
		}
		return (InternetAddress[]) internetAddrList.toArray(new InternetAddress[internetAddrList.size()]);
	}
}
