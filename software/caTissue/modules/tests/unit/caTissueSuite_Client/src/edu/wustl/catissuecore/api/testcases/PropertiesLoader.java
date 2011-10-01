package edu.wustl.catissuecore.api.testcases;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PropertiesLoader {

	private static Properties properties = new Properties();

	static {
		try {
			URL url = PropertiesLoader.class.getClassLoader().getResource("api_testcases.properties");
			properties.load(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static String getAdminUsername() {
		return properties.getProperty("admin.username");
	}

	public static String getAdminPassword() {
		return properties.getProperty("admin.password");
	}

	public static String getCPShortTitleForAddParticipantWithCPR() {
		return properties.getProperty("admin.add.participant.with.cpr.cp.short.title");
	}

	public static String getGenderForSearchParticipantByExample() {
		return properties
				.getProperty("admin.search.participant.by.example.gender");
	}

	public static String getFirstNameForMatchingParticipant() {
		return properties.getProperty("admin.matching.participant.firstname");
	}

	public static String getCPTitleForUpdateParticipantWithBarcodeinCPR() {
		return properties
				.getProperty("admin.update.participant.cpr.with.barcode.cp.title");
	}

	public static String getCPTitleForUpdateParticipant() {
		return properties.getProperty("admin.update.participant.cp.title");
	}

	public static String getNonPIPCScientistUsername() {
		return properties.getProperty("non.pipc.scientist.username");
	}

	public static String getNonPIPCScientistPassword() {
		return properties.getProperty("non.pipc.scientist.password");
	}

	public static String getCPTitleOfParticipantForNonPIPCScientist() {
		return properties.getProperty("non.pipc.scientist.cp.title");
	}

	public static String getPPIForSpecimen() {
		return properties.getProperty("non.pipc.scientist.specimen.male.ppi");
	}

	public static String getPCScientistUsername() {
		return properties.getProperty("pc.scientist.username");
	}

	public static String getPCScientistPassword() {
		return properties.getProperty("pc.scientist.password");
	}

	public static String getPCScientistCPTitle() {
		return properties.getProperty("pc.scientist.cp.title");
	}

	public static String getPIScientistUsername() {
		return properties.getProperty("pi.scientist.username");
	}

	public static String getPIScientistPassword() {
		return properties.getProperty("pi.scientist.password");
	}

	public static String getCPTitleForPIScientistForParticipant() {
		return properties.getProperty("pi.scientist.participant.cp.title");
	}

	public static String getScientistReadDeniedUsername() {
		return properties.getProperty("scientist.read.denied.username");
	}

	public static String getScientistReadDeniedPassword() {
		return properties.getProperty("scientist.read.denied.username");
	}

	public static String getCPTitleForScientistReadDeniedForParticipant() {
		return properties
				.getProperty("scientist.read.denied.participant.cp.title");
	}

	public static String getCPTitleForScientistReadDeniedForFluidSpecimen() {
		return properties
				.getProperty("scientist.read.denied.fluidspecimen.cp.title");
	}

	public static String getScientistReadUsername() {
		return properties.getProperty("scientist.read.username");
	}

	public static String getScientistReadPassword() {
		return properties.getProperty("scientist.read.username");
	}

	public static String getCPTitleForScientistReadForTissueSpecimen() {
		return properties.getProperty("scientist.read.tissuespecimen.cp.title");
	}

	public static String getFilterScientistUsername() {
		return properties.getProperty("filter.scientist.username");
	}

	public static String getFilterScientistPassword() {
		return properties.getProperty("filter.scientist.password");
	}

    public static String getGridDorianUrl() {
        return properties.getProperty("grid.dorian.url");
    }

    public static String getGridAuthenticationServiceUrl() {
        return properties.getProperty("grid.authentication.service.url");
    }

    public static String getGridUsername() {
        return properties.getProperty("grid.username");
    }

    public static String getGridPassword() {
        return properties.getProperty("grid.password");
    }

}
