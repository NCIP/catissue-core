angular.module('os.biospecimen.visit.spr', ['os.biospecimen.models'])
  .controller('VisitSprCtrl', function($scope, $sce, visit, Alerts) {

    function init() {
      $scope.sprUploader = {};
      $scope.sprUrl = $sce.trustAsResourceUrl(visit.getSprUrl());
      $scope.sprName = visit.sprName;
      $scope.uploadMode = false;
      $scope.editMode = false;
      $scope.spr = {};

      loadSpr();
    }

    function loadSpr() {
     visit.getSpr().then(
        function(result) {
          $scope.spr.sprContent = result.data;
        }
      );
    }

    $scope.showUploadMode = function() {
      $scope.uploadMode = true;
    }

    $scope.cancel = function() {
      $scope.uploadMode = false;
    }

    $scope.showEditMode = function() {
      $scope.editMode = true;
    }

    $scope.cancelSprEdit = function() {
       $scope.editMode = false;
    }

    $scope.upload = function() {
      $scope.sprUploader.ctrl.submit().then(
        function(fileName) {
          Alerts.success("visits.spr_uploaded", {file:fileName});
          $scope.uploadMode = false;
          $scope.sprName = fileName;
          loadSpr();
        }
      )
    }

    $scope.saveSpr = function() {
      var data = {sprContent: $scope.spr.sprContent};
      visit.updateSpr(data).then(
        function(result) {
          Alerts.success("visits.spr_saved");
          $scope.editMode = false;
        }
      );
    }

    $scope.confirmDeleteSpr = function() {
      //TODO: Delete Spr feature
    }

    init();
  });