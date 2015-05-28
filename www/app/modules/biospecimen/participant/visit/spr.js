angular.module('os.biospecimen.visit.spr', ['os.biospecimen.models'])
  .controller('SprController', function($scope, $sce, visit, Alerts) {
    $scope.sprUploader = {};
    $scope.sprUrl = $sce.trustAsResourceUrl(visit.getSprUrl());
    $scope.upload = function() {
      $scope.sprUploader.submit().then(
        function(fileName) {
          Alerts.success("visits.report_uploaded", {file:fileName});
        }
      )
    }
  });