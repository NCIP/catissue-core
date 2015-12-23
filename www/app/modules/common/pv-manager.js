
/**
 * TODO: The PvManager will actually do following
 * 1. make REST API calls to get PVs for the input attribute
 * 2. Cache the PVs so that frequent calls are not needed
 */
angular.module('openspecimen')
  .factory('PvManager', function($http, $q, $translate, ApiUrls, ApiUtil, Site, Util) {
    var url = ApiUrls.getBaseUrl() + 'permissible-values';

    var anatomicSites = [
      'DIGESTIVE ORGANS',
      'SKIN',
      'MALE GENITAL ORGANS',
      'UNKNOWN PRIMARY SITE',
      'PERIPHERAL NERVES AND AUTONOMIC NERVOUS SYSTEM',
      'FEMALE GENITAL ORGANS',                       
      'OTHER AND ILL-DEFINED SITES',
      'HEMATOPOIETIC AND RETICULOENDOTHELIAL SYSTEMS',
      'RETROPERITONEUM AND PERITONEUM',
      'RESPIRATORY SYSTEM AND INTRATHORACIC ORGANS',
      'BONES, JOINTS AND ARTICULAR CARTILAGE',
      'THYROID AND OTHER ENDOCRINE GLANDS',
      'MENINGES',
      'CONNECTIVE, SUBCUTANEOUS AND OTHER SOFT TISSUES',
      'BREAST',
      'LIP, ORAL CAVITY AND PHARYNX',
      'LYMPH NODES',
      'URINARY TRACT',
      'BRAIN',
      'SPINAL CORD, CRANIAL NERVES, AND OTHER PARTS OF CENTRAL NERVOUS SYSTEM',
      'EYE, BRAIN AND OTHER PARTS OF CENTRAL NERVOUS SYSTEM',
      'Not Specified'
    ];
    
    var positionLabelingSchemes = [
      'Numbers',
      'Alphabets Upper Case',
      'Alphabets Lower Case',
      'Roman Upper Case',
      'Roman Lower Case'
    ];

    /** We need to have i18n keys for these as well **/
    var storageTypes = [
      'Auto',
      'Manual',
      'Virtual'
    ];

    var visitStatuses = [
      'Complete',
      'Pending',
      'Missed Collection'
    ];

    var specimenStatuses = [
      'Collected',
      'Missed Collection',
      'Pending'
    ];

    var activityStatuses = [
      'Active',
      'Pending',
      'Disabled',
      'Closed'
    ];

    var qualityStatuses = [
      'Acceptable',
      'Unacceptable'
    ];

    var spmnLabelPrePrintModes = [
      {name: 'ON_REGISTRATION', displayKey:'cp.spmn_label_pre_print_modes.ON_REGISTRATION'},
      {name: 'ON_VISIT_COMPLETION', displayKey:'cp.spmn_label_pre_print_modes.ON_VISIT_COMPLETION'},
      {name: 'NONE', displayKey:'cp.spmn_label_pre_print_modes.NONE'}
    ];


    var spmnLabelAutoPrintModes = [
      {name: 'PRE_PRINT', displayKey:'srs.spmn_label_auto_print_modes.PRE_PRINT'},
      {name: 'ON_COLLECTION', displayKey:'srs.spmn_label_auto_print_modes.ON_COLLECTION'},
      {name: 'NONE', displayKey:'srs.spmn_label_auto_print_modes.NONE'}
    ];

    var pvMap = {
      anatomicSite: anatomicSites,
      'storage-type': storageTypes,
      'visit-status': visitStatuses,
      'specimen-status': specimenStatuses,
      'container-position-labeling-schemes': positionLabelingSchemes,
      'activity-status': activityStatuses,
      'quality-status': qualityStatuses,
      'specimen-label-pre-print-modes': spmnLabelPrePrintModes,
      'specimen-label-auto-print-modes': spmnLabelAutoPrintModes
    };

    var pvIdMap = {
      'clinical-status'     : '2003988',
      'gender'              : '2003989',
      'genotype'            : '2003990',
      'specimen-class'      : '2003991',
      'laterality'          : '2003992',
      'pathology-status'    : '2003993',
      'collection-procedure': '2003996',
      'collection-container': '2003997',
      'vital-status'        : '2004001',
      'received-quality'    : '2003994',

      'ethnicity'           : 'Ethnicity_PID',
      'race'                : 'Race_PID',
      'anatomic-site'       : 'Tissue_Site_PID',
      'site-type'           : 'Site_Type_PID',
      'clinical-diagnosis'  : 'Clinical_Diagnosis_PID',
      'specimen-biohazard'  : 'specimen_biohazard',
      'consent_response'    : 'consent_response',
      'missed-visit-reason' : 'missed_visit_reason',
      'cohort'              : 'cohort'
    };

    function valueOf(input) {
      return input.value;
    };

    function parentAndValue(input) {
      return {parent: input.parentValue, value: input.value};
    };

    function transform(pvs, transformfn, incParentVal, result) {
      transformfn = transformfn || (incParentVal ? parentAndValue : valueOf);
      return pvs.map(function(pv) { return transformfn(pv); });
    };

    function loadPvs(attr, srchTerm, transformFn, incOnlyLeaf) {
      var pvId = pvIdMap[attr];
      if (!pvId && pvMap[attr]) {
        return _getPvs(attr);
      } else if (!pvId) {
        pvId = attr;
      }

      var params = {
        attribute: pvId,
        searchString: srchTerm,
        includeOnlyLeafValue: incOnlyLeaf
      };

      return $http.get(url, {params: params}).then(
        function(result) {
          return transform(result.data, transformFn, null);
        }
      );
    };

    function loadPvsByParent(parentAttr, parentVal, incParentVal, transformFn) {
      var pvId = pvIdMap[parentAttr];
      if (!pvId) {
        pvId = parentAttr;
      }

      var params = {
        parentAttribute: pvId, 
        parentValue: parentVal,  
        includeParentValue: incParentVal
      };

      return $http.get(url, {params: params}).then(
        function(result) {
          return transform(result.data, transformFn, incParentVal);
        }
      );
    };

    function  _getPvs(attr) {
      var deferred = $q.defer();
      var result = undefined;
      if (pvMap[attr]) {
        result = pvMap[attr];
        if (hasI18nKey(result) && !isDisplayNameInitialized(result)) {
          initDisplayNames(result);
        }
      } else {
        result = [];
      }
      deferred.resolve(result);
      return deferred.promise;
    };

    function hasI18nKey(pvs) {
      return !!pvs[0].displayKey;
    }

    function isDisplayNameInitialized(pvs) {
      return !!pvs[0].displayName;
    }

    function initDisplayNames(pvs) {
      $translate('pvs.not_specified').then(
        function() {
          angular.forEach(pvs, function(pv) {
            pv.displayName = $translate.instant(pv.displayKey);
          });
        }
      );
    }

    return {
      getPvs: function(attr, srchTerm, transformFn) {
        var pvs = [];
        loadPvs(attr, srchTerm, transformFn).then(
          function(result) {
            Util.unshiftAll(pvs, result);
          }
        );    

        return pvs;
      },

      getLeafPvs: function(attr, srchTerm, transformFn) {
        var pvs = [];
        loadPvs(attr, srchTerm, transformFn, true).then(
          function(result) {
            Util.unshiftAll(pvs, result);
          }
        );

        return pvs;
      },

      loadPvs: loadPvs,

      getPvsByParent: function(parentAttr, parentVal, incParentVal, transformFn) {
        var pvs = [];

        loadPvsByParent(parentAttr, parentVal, incParentVal, transformFn).then(
          function(result) {
            Util.unshiftAll(pvs, result);
          }
        );

        return pvs;
      },

      loadPvsByParent: loadPvsByParent,

      getSites: function(opts) {
        var sites = [];
        Site.query(opts).then(
          function(siteList) {
            angular.forEach(siteList, function(site) {
              sites.push(site.name);
            });
          }
        );
        return sites;
      },

      notSpecified: function() {
        return $translate.instant('pvs.not_specified');
      }
    };
  });
