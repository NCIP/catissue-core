package krishagni.catissueplus.Exception;

import java.util.HashMap;
import java.util.Map;

public enum SpecimenErrorCodeEnum
{
    NOT_FOUND(1003, "Specimen with given label doesn't exist."),
	NOT_AUTHORIZED(1004,  "User doesn't have specimen processing privileges on this specimen."),
	INVALID_CLASS(1005, "Specimen Class is invalid."),
	INVALID_TYPE(1006, "Specimen Type is invalid."),
	INVALID_TISSUE_SITE(1007, "Tissue Site is invalid."),
	
	
    INVALID_ALIQUOT_COUNT(1008,"Please enter valid aliquot count or quantity per aliquot."),
    INSUFFICIENT_AVAILABLE_QUANTITY(1009,"Total quantity of all aliquots cannot be greater than available quantity of parent specimen."),
    INSUFFICIEN_STORAGE_LOCATION (1010,"The system cannot store all the aliquots in the same container due to insufficient number of storage locations."),
    INVALID_LABEL_BARCODE(1011, "Invalid specimen label or barcode."),
    POSITION_NOT_AVAILABLE(1012,"Aliquot cannot be placed in occupied position."),
    INVALID_STORAGE_CONTAINER_NAME(1013, "Please enter valid container name."),
    AlIQUOT_QUANTITY(1014,"Please enter valid aliquot quantity."),
    PARSE_ERROR (1015,"Error occured while parsing aliquot details."),
    INVALID_STORAGE_POSITION(1016,"Please enter valid storage position."),
    DUPLICATE_LABEL_BARCODE(1017, "Label/Barcode already exists."),
    LABEL_REQUIRED(1018, "Label cannot be empty."),
    INVALID_CONTAINER(1019,"This Storage Container cannot hold this Specimen."),

	POSITION_ALREADY_OCCUPIED(1040,"Storage position is already occupied by other specimen."),
	INVALID_USER(1041,"Please provide valid user information"),
	SPECIMEN_NOT_AVAILABLE(1042,"Cannot create child specimen(s) since specimen is not available."),
    INTERNAL_SERVER_ERROR(1050,"Error while performing update operation");
    
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
