package com.krishagni.catissueplus.core.biospecimen.label.cpr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class CpUniqueIdPpidToken extends AbstractPpidToken {

	@Autowired
	private DaoFactory daoFactory;
	
	public CpUniqueIdPpidToken() {
		this.name = "CP_UID";
	}

	@Override
	public String getLabel(CollectionProtocolRegistration cpr, String... args) {
		String format = "%d";
		if (args != null && args.length > 0) {
			int digit = Integer.parseInt(args[0]);
			format = digit == 0 ? format : "%0" + digit + "d";
		}
		
		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId("PPID", cpr.getCollectionProtocol().getId().toString());
		return String.format(format, uniqueId);
	}

	@Override
	public int validate(Object object, String input, int startIdx, String... args) {
		String regx = "\\d+";
		if (args != null && args.length > 0) {
			int digit = Integer.parseInt(args[0]);
			regx = digit == 0 ? regx : "\\d{" + digit + "}";
		}
		
		Pattern pattern = Pattern.compile(regx);
		Matcher matcher = pattern.matcher(input.substring(startIdx));
		if (matcher.find() && matcher.start() == 0) {
			return startIdx + matcher.group(0).length();
		}
		
		return startIdx;
	}
}
