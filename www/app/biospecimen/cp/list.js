
angular.module('openspecimen')
  .controller('CpListCtrl', function($scope, CollectionProtocolService) {
    CollectionProtocolService.getCpList(true).then(
      function(result) {
        if (result.status == "ok") {
          $scope.cpList = result.data;
        } else {
          alert("Failed to load cp list");
        }
      }
    );
  });
