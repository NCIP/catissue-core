package krishagni.catissueplus.Exception;

import java.util.HashMap;
import java.util.Map;

public enum SpecimenErrorCodeEnum
{
    NOT_FOUND(1030, "Specimen with given label doesn't exist."),
	NOT_AUTHORIZED(1004,  "User doesn't have specimen processing privileges on this specimen."),
	INVALID_CLASS(1005, "Specimen Class is invalid."),
	INVALID_TYPE(1006, "Specimen Type is invalid."),
	INVALID_TISSUE_SITE(1007, "Tissue Site is invalid.");

    private int code;
    private String description;
 
    /**
     * A mapping between the integer code and its corresponding Status to facilitate lookup by code.
     */
    private static Map<Integer, SpecimenErrorCodeEnum> codeToStatusMapping;
 
    private SpecimenErrorCodeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }
 
    public static SpecimenErrorCodeEnum getStatus(int i) {
        if (codeToStatusMapping == null) {
            initMapping();
        }
        return codeToStatusMapping.get(i);
    }
 
    private static void initMapping() {
        codeToStatusMapping = new HashMap<Integer, SpecimenErrorCodeEnum>();
        for (SpecimenErrorCodeEnum s : values()) {
            codeToStatusMapping.put(s.code, s);
        }
    }
 
    public int getCode() {
        return code;
    }
 
    public String getDescription() {
        return description;
    }
 
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Status");
        sb.append("{code=").append(code);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
