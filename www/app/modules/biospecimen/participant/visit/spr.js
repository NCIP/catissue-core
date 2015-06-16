angular.module('os.biospecimen.visit.spr', ['os.biospecimen.models'])
  .controller('VisitSprCtrl', function($scope, $sce, visit, DeleteUtil, Alerts) {

    function init() {
      $scope.sprUploader = {};
      $scope.sprUrl = $sce.trustAsResourceUrl(visit.getSprFileUrl());
      $scope.spr = {name: visit.sprName, lock: visit.sprLock};
      $scope.uploadMode = false;

      loadSpr();
    }

    function loadSpr() {
      if (!$scope.spr.name) {
        return;
      }
      visit.getSprText().then(
        function(sprText) {
          $scope.spr.text = sprText;
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
          $scope.spr.name = fileName;
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
        entity: {sprName: $scope.spr.sprName},
        templateUrl: 'modules/biospecimen/participant/visit/confirm-delete-spr-file.html',
        delete: deleteSpr
      });
    }

    function deleteSpr() {
      visit.deleteSprFile().then(
        function(isDeleted) {
          if (isDeleted) {
            $scope.spr = {};
          }
        }
      );
    }

    $scope.lockSpr = function(lock) {
      visit.lockSpr(lock).then(function(result) {
        $scope.spr.lock = lock;
        if (lock) {
          Alerts.success("visits.spr_locked");
        } else {
          Alerts.success("visits.spr_unlocked");
        }
      });
    }

    init();
  });
