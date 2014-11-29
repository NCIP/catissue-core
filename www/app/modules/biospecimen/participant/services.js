
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
    var url = function() {
      return ApiUrls.getUrl('cprs');
    }

    return {
      registerParticipant: function(cpr) {
        return $http.post(url() + '/', cpr).then(ApiUtil.processResp, ApiUtil.processResp);
      },

      getRegistration: function(cprId) {
        return $http.get(url() + '/' + cprId).then(ApiUtil.processResp, ApiUtil.processResp);
      },

      getVisits: function(cprId, includeStats) {
        return $http.get(
          url() + '/' + cprId + '/visits', 
          {params: {includeStats: !!includeStats}})
          .then(ApiUtil.processResp, ApiUtil.processResp);
      }
    };
  });
