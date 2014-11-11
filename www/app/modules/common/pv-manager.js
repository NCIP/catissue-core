
angular.module('openspecimen')
  .factory('PvManager', function($http, $q, ApiUrls, ApiUtil) {
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

    var pvMap = {
      gender: genders, 
      ethnicity: ethnicity, 
      vitalStatus: vitalStatuses, 
      race: races
    };

    return {
      getPvs: function(attr) {
        var deferred = $q.defer();
        if (pvMap[attr]) {
          result = {status: 'ok', data: pvMap[attr]};
        } else {
          result = {status: 'error'};
        }

        deferred.resolve(result);
        return deferred.promise;
      },

      loadPvs: function(scope, attr) {
        this.getPvs(attr).then(
          function(result) {
            if (result.status != 'ok') {
              alert("Failed to load PVs of attribute: " + attr);
              return;
            }

            scope[attr + '_pvs'] = result.data;
          }
        );
      }
    };
  });
