
angular.module('openspecimen')
  .factory('CollectionProtocolService', function($http, ApiUrls, ApiUtil) {
    var url = function() {
      return ApiUrls.getUrl('collection-protocols');
    };

    return {   
      getCpList: function(detailed) {
        var params = {};
        if (detailed) {
          params = {detailedList: detailed};
        }

        return $http.get(url(), {params: params}).then(ApiUtil.processResp);
      },

      getRegisteredParticipants: function(cpId, detailed) {
        var params = {includeStats: !detailed ? false : true};
        return $http.get(
          url() + '/' + cpId + '/registered-participants', 
          {params: params}).then(ApiUtil.processResp);
      }
    }
  });
