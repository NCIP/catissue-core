angular.module('os.administrative.form.addedit', ['os.administrative.models'])
  .controller('FormAddEditCtrl', function($scope, $state, $sce, $stateParams, form) {
    
    function init() {
      $scope.form = form;
      $scope.form.id = $stateParams.formId;
      $scope.url = "csd_web/pages/csdUI.html#loadCachedForm/";
      
      if ($stateParams.formId) {
         $scope.url += $stateParams.formId + "/true ?_reqTime=" +  new Date().getTime();
      }

      $scope.url = $sce.trustAsResourceUrl($scope.url);
    }

    init(); 
  });
