
angular.module('openspecimen')
  .factory('ParticipantService', function($http, ApiUrls, ApiUtil) {
    var url = function() { 
      return ApiUrls.getUrl('participants');
    };

    return {
      getMatchingParticipants: function(criteria) {
        return $http.post(
          url() + '/match', 
          criteria).then(ApiUtil.processResp, ApiUtil.processResp);
      }
    };
  })

  .factory('CprService', function($http, $q, ApiUrls, ApiUtil) {
    return {
      getRegistration: function(cprId) {
        var deferred = $q.defer();
        deferred.resolve({
          data: {
            id: 7,
            participant: {
              lastName: 'Dravid',
              firstName: 'Rahul',
              birthDate: new Date(Date.parse('01/01/1970')),
              gender: 'Male Gender',
              race: ['Asian'],
              vitalStatus: 'Alive',
              ethnicity: null,
              ssn: '111-22-3333',
              pmis: [
                {siteName: 'AKU', mrn: '1'},
                {siteName: 'JHU', mrn: '2'}
              ]
            },
            ppid: '111-22-3333-p',
            barcode: null,
            registrationDate: new Date(Date.parse('11/24/2014')),
            activityStatus: 'ACTIVE'
          }
        });
        return deferred.promise;
      },

      getVisits: function(cprId) {
        var deferred = $q.defer();
        deferred.resolve({
          data: [
            {
              name: 'Pre-Surgery',
              eventPoint: 10,
              collectionDate: new Date(Date.parse('03/09/2014')),
              anticipatedSpecimens: 5,
              collectedSpecimens: 3,
              status: 'Completed'
            },
            {
              name: 'Surgery',
              eventPoint: 30,
              collectionDate: new Date(Date.parse('04/01/2014')),
              anticipatedSpecimens: 10,
              collectedSpecimens: 10,
              status: 'Completed'
            },
            {
              name: 'Post-Surgery',
              eventPoint: 45,
              collectionDate: undefined,
              anticipatedCollectionDate: new Date(Date.parse('04/15/2014')),
              anticipatedSpecimens: 20,
              collectedSpecimens: 0,
              status: 'Pending'
            },
            {
              name: 'Relapse',
              eventPoint: 60,
              collectionDate: undefined,
              anticipatedCollectionDate: new Date(Date.parse('05/01/2014')),
              anticipatedSpecimens: 5,
              collectedSpecimens: 0,
              status: 'Pending'
            }
          ]
        });
        return deferred.promise;
      }
    };
  });
