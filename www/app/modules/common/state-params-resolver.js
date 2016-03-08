angular.module('openspecimen')
  .factory('ObjectStateParamsResolver', function($http, ApiUrls) {
    return {
      getParams: function(objectName, key, value) {
        var params = { objectName: objectName, key: key, value: value };
        return $http.get(ApiUrls.getBaseUrl() + '/object-state-params', {params: params}).then(
          function(resp) {
            return resp.data;
          }
        );
      }
    }
  })
  .config(function($stateProvider) {
    $stateProvider.state('object-state-params-resolver', {
      url: '/object-state-params-resolver?:stateName&:objectName&:key&:value',
      controller: function($state, $stateParams, params) {
        $state.go($stateParams.stateName, params, {location: 'replace'});
      },
      resolve: {
        params: function($stateParams, ObjectStateParamsResolver) {
          return ObjectStateParamsResolver.getParams($stateParams.objectName, $stateParams.key, $stateParams.value);
        }
      },
      parent: 'signed-in'
    });
  });
