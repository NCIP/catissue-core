angular.module('openspecimen')
  .controller('sgh.CpBulkPrintingCtrl', function(
    $scope, $http, $state, ApiUrls, Alerts) {

    function init() {
      $scope.regReq = {
        tridCount: 0
      }
    };

    $scope.bulkPrint = function() {
      $http.post(ApiUrls.getBaseUrl() + 'sgh/trids', $scope.regReq).then(
        function(result) {
          Alerts.success("custom_sgh.trid_printed");
          $state.go('home');  
        }
      );
    }

    init();
  });
