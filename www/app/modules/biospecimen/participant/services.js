
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
        return $http.get(url() + '/' + cprId + '/visits', {params: {includeStats: !!includeStats}})
          .then(ApiUtil.processResp, ApiUtil.processResp)
          .then(function(result) {
            if (result.status != 'ok') {
              return result;
            }

            angular.forEach(result.data, function(visit) {
              visit.pendingSpecimens = visit.anticipatedSpecimens - (visit.collectedSpecimens + visit.uncollectedSpecimens);
              visit.totalSpecimens = visit.anticipatedSpecimens + visit.unplannedSpecimens;
            });
        
            return result;
          });
      },

      getVisitSpecimensTree: function(cprId, visitDetail) {
        var apiUrl = url() + '/' + cprId + '/specimens/';
        return $http.get(apiUrl, {params: visitDetail}).then(ApiUtil.processResp, ApiUtil.processResp);
      }
    };
  });
