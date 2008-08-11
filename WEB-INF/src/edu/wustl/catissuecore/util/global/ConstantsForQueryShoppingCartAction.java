package edu.wustl.catissuecore.util.global;

import edu.wustl.catissuecore.domain.CellSpecimen;
import edu.wustl.catissuecore.domain.FluidSpecimen;
import edu.wustl.catissuecore.domain.MolecularSpecimen;
import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.SpecimenArray;
import edu.wustl.catissuecore.domain.TissueSpecimen;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.SurgicalPathologyReport;

public class ConstantsForQueryShoppingCartAction 
{
            
	// constants required for caching mechanism of ParticipantBizLogic
	public static final String ADD = "add";
	public static final String DELETE = "delete";
	public static final String ID = "id";
	public static final String IS_LIST_EMPTY = "isListEmpty";
	public static final String FALSE="false";
	public static final String TRUE="true";
	
	// For Specimen Event Parameters.	
	public static final String SPECIMEN_ARRAY_ID = "specimenArrayIds";
	public static final String PATHALOGICAL_CASE_ID = "pathalogicalCaseIds";
	public static final String DEIDENTIFIED_PATHALOGICAL_CASE_ID = "deidentifiedPathalogicalCaseIds";	
	public static final String SURGICAL_PATHALOGY_CASE_ID ="surgicalPathalogicalCaseIds";
	public static final String SPECIMEN_ID = "specimenId";
	public static final String ADD_TO_ORDER_LIST = "addToOrderList";
    public static final String REQUESTED_FOR_BIOSPECIMENS = "RequestedBioSpecimens";
    public static final String DEFINE_ARRAY_FORM_OBJECTS = "DefineArrayFormObjects";
    public static final String REQUEST_TO_ORDER = "requestToOrder";
    
    // Query Module Interface UI constants       
    public static final String SELECTED_COLUMN_META_DATA = "selectedColumnMetaData";
    public static final String OPERATION = "operation";
    public static final String RESULTS_PER_PAGE = "numResultsPerPage";
    public static final String CHECK_ALL_ACROSS_ALL_PAGES = "isCheckAllAcrossAllChecked";
    
    // Approve User Constants
    public static final String PAGE_NUMBER = "pageNum";
    public static final String TOTAL_RESULTS = "totalResults";
    
    // Shopping Cart
    public static final String QUERY_SHOPPING_CART = "queryShoppingCart";
    
    // SimpleSearchAction    
    public static final String SPREADSHEET_COLUMN_LIST = "spreadsheetColumnList";
    public static final String QUERY_SESSION_DATA = "querySessionData";
        
    // Mandar: Used for Date Validations in Validator Class
    public static final String SHOPPING_CART_ADD = "shoppingCartAdd";
    
    // constant for pagination data list
    public static final String PAGINATION_DATA_LIST = "paginationDataList";
    
    // Frame names in Query Module Results page.
    public static final String PAGEOF_QUERY_MODULE = "pageOfQueryModule";
    
    // -- menu selection related
    public static final String PAGEOF = "pageOf";    
       
    // constants for passwordManager
    public static final String SELECT_OPTION = "-- Select --";
       	
    
    // constants for Data required in query
    public static final String EVENT_PARAMETERS[] = {	ConstantsForQueryShoppingCartAction.SELECT_OPTION,
		"Cell Specimen Review",	"Check In Check Out", "Collection",
		"Disposal", "Embedded", "Fixed", "Fluid Specimen Review",
		"Frozen", "Molecular Specimen Review", "Procedure", "Received",
		"Spun", "Thaw", "Tissue Specimen Review", "Transfer" };
    
    // Constants required for Forgot Password
    public static final String EVENT_PARAMETERS_LIST = "eventParametersList";
    
    // Mandar: Used for Date Validations in Validator Class
    public static final String SHOPPING_CART_DELETE = "shoppingCartDelete";
    public static final String EXPORT = "export";
    public static final String VIEW = "view";
    
    // Query Shopping cart constants
    public static final String VALIDATION_MESSAGE_FOR_ORDERING = "validationMessageForOrdering";
    public static final String SPECIMEN_ARRAY_CLASS_NAME = SpecimenArray.class.getName();
    public static final String IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME = IdentifiedSurgicalPathologyReport.class.getName();
    public static final String DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME = DeidentifiedSurgicalPathologyReport.class.getName();
    public static final String SURGICAL_PATHALOGY_REPORT_CLASS_NAME = SurgicalPathologyReport.class.getName();
    public static final String IS_SPECIMENARRAY_PRESENT = "isSpecimenArrayPresent";
    public static final String IS_SPECIMENID_PRESENT = "isSpecimenIdPresent";
    public static final String DIFFERENT_VIEW_IN_CART = "differentCartView";
    public static final String DOT_CSV = ".csv";
    public static final String DELIMETER = ",";
    public static final String SHOPPING_CART_FILE_NAME = "MyList.csv";
    public static final String APPLICATION_DOWNLOAD = "application/download";
    public static final String UNDERSCORE = "_";
    public static final String SPECIMEN_NAME = Specimen.class.getName();
    
    public static final String[] specimenNameArray = {
        SPECIMEN_NAME,
        FluidSpecimen.class.getName(),
        MolecularSpecimen.class.getName(),
        TissueSpecimen.class.getName(),
        CellSpecimen.class.getName()
    };
    
    public static final String[] entityNameArray = {
        SPECIMEN_NAME,
        FluidSpecimen.class.getName(),
        MolecularSpecimen.class.getName(),
        TissueSpecimen.class.getName(),
        CellSpecimen.class.getName(),
        SPECIMEN_ARRAY_CLASS_NAME,
        IDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
        DEIDENTIFIED_SURGICAL_PATHALOGY_REPORT_CLASS_NAME,
        SURGICAL_PATHALOGY_REPORT_CLASS_NAME
};
    public static final String BULK_TRANSFERS = "bulkTransfers";
    public static final String BULK_DISPOSALS = "bulkDisposals";
    public static final String EDIT_MULTIPLE_SPECIMEN = "editMultipleSp";
            
}
