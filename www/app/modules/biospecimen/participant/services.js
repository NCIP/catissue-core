
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
  });
