
angular.module('os.biospecimen.specimensrequest.list', ['os.biospecimen.models'])
  .controller('SpecimensRequestListCtrl', function($scope, specimensRequestList) {
    function init() {
      $scope.spmnsReqList = specimensRequestList;
    }

    init();
  });
