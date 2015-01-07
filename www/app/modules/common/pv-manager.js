
/**
 * TODO: The PvManager will actually do following
 * 1. make REST API calls to get PVs for the input attribute
 * 2. Cache the PVs so that frequent calls are not needed
 */
angular.module('openspecimen')
  .factory('PvManager', function($http, $q, ApiUrls, ApiUtil, Site) {
    var url = ApiUrls.getBaseUrl() + 'permissible-values/attribute=';

    var genders = [
      'Female Gender', 
      'Male Gender', 
      'Unknown', 
      'Unspecified'
    ];

    var ethnicity = [
      'Hispanic or Latino', 
      'Not Hispanic or Latino', 
      'Not Reported', 
      'Unknown'
    ];

    var vitalStatuses = [
      'Alive', 
      'Dead', 
      'Unknown', 
      'Unspecified'
    ];

    var races = [
      'White', 
      'Black or African American', 
      'American Indian or Alaska Native',
      'Asian', 
      'Native Hawaiian or Other Pacific Islander', 
      'Not Reported',
      'Unknown'
    ];

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

    /** We need to have i18n keys for these as well **/
    var storageTypes = [
      'Auto',
      'Manual',
      'Virtual'
    ];

    var visitStatuses = [
      'Complete',
      'Incomplete',
       'Pending'
    ];

    var pvMap = {
      gender: genders, 
      ethnicity: ethnicity, 
      vitalStatus: vitalStatuses, 
      race: races,
      anatomicSite: anatomicSites,

      'storage-type': storageTypes,
      'visit-status': visitStatuses
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

      'ethnicity'           : 'Ethnicity_PID',
      'race'                : 'Race_PID',
      'anatomic-site'       : 'Tissue_Site_PID',
      'site-type'           : 'Site_Type_PID'
    };

    return {
      _getPvs: function(attr) {
        var deferred = $q.defer();
        var result = undefined;
        if (pvMap[attr]) {
          result = {status: 'ok', data: pvMap[attr]};
        } else {
          result = {status: 'error'};
        }

        deferred.resolve(result);
        return deferred.promise;
      },

      getPvs: function(attr, srchTerm) {
        var pvId = pvIdMap[attr];
        if (!pvId) {
          return pvMap[attr] || [];
        }

        var pvs = [];
        $http.get(url + pvId, {params: {searchString: srchTerm}}).then(
          function(result) {
            angular.forEach(result.data, function(pv) {
              pvs.push(pv.value);
            });
          }
        );

        return pvs;
      },

      getPvsByParent: function(attr, parentVal) {
        var pvId = pvIdMap[attr];
        if (!pvId) {
          return [];
        }

        var pvs = [];
        $http.get(url + pvId, {params: {parentValue: parentVal}}).then(
          function(result) {
            angular.forEach(result.data, function(pv) {
              pvs.push(pv.value);
            });
          }
        );

        return pvs;
      },

      /** loadPvs will be deprecated soon **/
      loadPvs: function(scope, attr) {
        this._getPvs(attr).then(
          function(result) {
            if (result.status != 'ok') {
              alert("Failed to load PVs of attribute: " + attr);
              return;
            }

            scope[attr + '_pvs'] = result.data;
          }
        );
      },

      getSites: function() {
        var sites = [];
        Site.list().then(
          function(siteList) {
            angular.forEach(siteList, function(site) {
              sites.push(site.name);
            });
          }
        );

        return sites;
      },

      getClinicalDiagnoses: function(params, cb) {
        var url = ApiUrls.getBaseUrl() + '/clinical-diagnoses';
        var diagnoses = [];
        $http.get(url, {params: params}).then(
          function(result) {
            angular.forEach(result.data, function(diagnosis) {
              diagnoses.push(diagnosis);
            });

            if (typeof cb == 'function') {
              cb(result.data);
            }
          }
        );

        return diagnoses;
      }
    };
  });
