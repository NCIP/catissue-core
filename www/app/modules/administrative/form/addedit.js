angular.module('os.administrative.form.addedit', ['os.administrative.models'])
  .controller('FormAddEditCtrl', function($scope, $state, $sce, form) {
    
    function init() {
      $scope.form = form;
      $scope.url = "csd_web/pages/csdUI.html#loadCachedForm/";
      
      if (form.id) {
         $scope.url += form.id + "/true ?_reqTime=" +  new Date().getTime();
      }

      $scope.url = $sce.trustAsResourceUrl($scope.url);
    }

    init(); 
  });
