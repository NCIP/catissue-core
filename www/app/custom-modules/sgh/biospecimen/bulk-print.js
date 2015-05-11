angular.module('os.biospecimen.cp.bulkprint', ['os.biospecimen.models'])
  .controller('CpBulkPrintCtrl', function(
     $scope, $state, $stateParams, $modal,
     cp, CollectionProtocolEvent, PvManager) {

    var pvsLoaded = false;
    var copyFrom = undefined;
    
    function init() {
      $scope.cp = cp;
      $scope.mode = undefined;
      $scope.selected = {};
    };

    
    init();
  });
