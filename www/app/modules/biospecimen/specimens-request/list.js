
angular.module('os.biospecimen.specimensrequest.list', ['os.biospecimen.models'])
  .controller('SpecimensRequestListCtrl', function($scope, $state, specimensRequestList) {
    function init() {
      $scope.spmnsReqList = specimensRequestList;
    }

    $scope.showRequest = function(req) {
      $state.go('specimens-request-detail.overview', {requestId: req.id});
    }

    init();
  });
