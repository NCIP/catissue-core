angular.module('openspecimen')
  .factory('InstituteService', function($http, ApiUrls, ApiUtil) {
    var url = function() {
      return ApiUrls.getUrl('institutions');
    };

  return {
    getInstituteList: function() {
      return $http.get(url()).then(ApiUtil.processResp, ApiUtil.processResp);
    },

    saveOrUpdateInstitute: function(instituteDetail) {
      if(instituteDetail.id) {
        return $http.put(url() + '/' + instituteDetail.id, instituteDetail)
          .then(ApiUtil.processResp, ApiUtil.processResp);
      } else {
        return $http.post(url(), instituteDetail)
          .then(ApiUtil.processResp, ApiUtil.processResp);
      }
    }
  }
})
