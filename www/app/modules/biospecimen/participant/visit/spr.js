angular.module('os.biospecimen.visit.spr', ['os.biospecimen.models'])
  .controller('VisitSprCtrl', function($scope, $sce, visit, DeleteUtil, Alerts) {

    function init() {
      $scope.sprUploader = {};
      $scope.sprUrl = $sce.trustAsResourceUrl(visit.getSprFileUrl());
      $scope.sprName = visit.sprName;
      $scope.uploadMode = false;
      $scope.spr = {};

      loadSpr();
    }

    function loadSpr() {
      if (!$scope.sprName) {
        return;
      }
      visit.getSprText().then(
        function(result) {
          $scope.spr.sprText = result.data;
        }
      );
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
          Alerts.success("visits.spr_uploaded", {file:fileName});
          $scope.uploadMode = false;
          $scope.sprName = fileName;
          loadSpr();
        }
      )
    }

    $scope.saveSpr = function(sprEditor, sprText) {
      var data = {sprText: sprText};
      return visit.updateSprText(data);
    }

    $scope.confirmDeleteSpr = function() {
      DeleteUtil.confirmDelete({
        entity: {sprName: $scope.sprName},
        templateUrl: 'modules/biospecimen/participant/visit/confirm-delete-spr-file.html',
        delete: deleteSpr
      });
    }

    function deleteSpr() {
      visit.deleteSprFile().then(
        function(result) {
          if (result.data) {
            $scope.sprName = undefined;
          }
        }
      );
    }

    init();
  });