angular.module('openspecimen')
  .factory('InstituteService', function($http, ApiUrls, ApiUtil) {
    var url = function() {
      return ApiUrls.getUrl('institutes');
    };

    return {
      getInstituteList: function() {
        return $http.get(url()).then(ApiUtil.processResp, ApiUtil.processResp);
      },

      saveOrUpdateInstitute: function(institute) {
        var apiMethod = institute.id ? $http.put : $http.post;
        var resource = url() + '/' + (institute.id || '');
        return apiMethod(resource, institute)
          .then(ApiUtil.processResp, ApiUtil.processResp);
      },

      deleteInstitute : function(id) {
        return $http.delete(
          url() + '/' + id).then(ApiUtil.processResp, ApiUtil.processResp);
      }
    }
  });
