package com.krishagni.catissueplus.core.biospecimen.label.visit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class EventUniqueIdLabelToken extends AbstractVisitLabelToken {

	@Autowired
	private DaoFactory daoFactory;

	public EventUniqueIdLabelToken() {
		this.name = "EVENT_UID";
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		String format = "%d";
		if (args != null && args.length > 0) {
			int digit = Integer.parseInt(args[0]);
			format = digit == 0 ? format : "%0" + digit + "d";
		}

		String key = visit.getRegistration().getPpid() + "_" + visit.getCpEvent().getId();
		Long uniqueId = daoFactory.getUniqueIdGenerator().getUniqueId(name, key);
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
