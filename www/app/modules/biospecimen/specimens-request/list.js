
angular.module('os.biospecimen.specimensrequest.list', ['os.biospecimen.models'])
  .controller('SpecimensRequestListCtrl', function($scope, $state, cpView, specimensRequestList) {
    function init() {
      $scope.cpView = cpView;
      $scope.spmnsReqList = specimensRequestList;
    }

    $scope.showRequest = function(req) {
      $state.go('specimens-request-detail.overview', {requestId: req.id});
    }

    init();
  });
