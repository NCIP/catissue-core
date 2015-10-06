angular.module('os.administrative.models.support', ['os.common.models'])
  .factory('Support', function(osModel, $http, ApiUtil) {
    var Support = osModel('support');
    
    Support.prototype.getType = function() {
      return 'support';
    }
    
    Support.sendFeedback = function(feedback) {
      return $http.post(Support.url() + 'user-feedback', feedback).then(ApiUtil.processResp);
    }
    
    return Support;
  });
