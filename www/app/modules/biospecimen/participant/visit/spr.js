angular.module('os.biospecimen.visit.spr', ['os.biospecimen.models'])
  .controller('VisitSprCtrl', function($scope, $sce, visit, Alerts) {

    function init() {
      $scope.sprUploader = {};
      $scope.sprUrl = $sce.trustAsResourceUrl(visit.getSprUrl());
      $scope.sprName = visit.sprName;
      $scope.uploadMode = false;
    }

    $scope.showUploadMode = function() {
      $scope.uploadMode = true;
    }

    $scope.cancel = function() {
      $scope.uploadMode = false;
    }

    $scope.upload = function() {
      $scope.sprUploader.ctrl.submit().then(
        function(fileName) {
          $scope.uploadMode = false;
          $scope.sprName = fileName;
          Alerts.success("visits.spr_uploaded", {file:fileName});
        }
      )
    }

    $scope.confirmDeleteSpr = function() {
      //TODO: Delete Spr feature
    }

    init();
  });