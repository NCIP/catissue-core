
angular.module('openspecimen')
  .directive('osFileUpload', function($parse) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        element.bind('change', function() {
          $parse(attrs.files).assign(scope, element[0].files)
          scope.$apply();
        })
      }
    };
  })
  .service('FileSvc', function($http, ApiUtil) {
    return {
      upload: function(url, files) {
        var fd = new FormData();
        angular.forEach(files, function(file) {
          fd.append('file', file);
        });

        return $http.post(url, fd, 
          {
           transformRequest: angular.identity, 
           headers: {'Content-Type': undefined}
          }
        ).then(ApiUtil.processResp);
      }
    };
  });
