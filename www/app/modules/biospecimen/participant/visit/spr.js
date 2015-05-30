angular.module('os.biospecimen.visit.spr', ['os.biospecimen.models'])
  .controller('VisitSprCtrl', function($scope, $sce, visit, Alerts) {
    function init() {
      $scope.sprUploader = {};
      $scope.sprUrl = $sce.trustAsResourceUrl(visit.getSprUrl());
    }

    $scope.upload = function() {
      $scope.sprUploader.submit().then(
        function(fileName) {
          Alerts.success("visits.spr_uploaded", {file:fileName});
        }
      )
    }

    init();
  });