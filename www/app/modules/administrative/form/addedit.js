angular.module('os.administrative.form.addedit', ['os.administrative.models'])
  .controller('FormAddEditCtrl', function($scope, $state, $sce, $stateParams) {
    
    function init() {
      $scope.url = "http://localhost:8180/openspecimen/csd_web/pages/csdUI.html#loadCachedForm/";
     // $scope.url = "/csd_web/pages/csdUI.html#loadCachedForm/";
      
      if ($stateParams.formId) {
         $scope.url += $stateParams.formId + "/true ?_reqTime=" +  new Date().getTime();
      }

      $scope.url = $sce.trustAsResourceUrl($scope.url);
    }

    init(); 
  });
