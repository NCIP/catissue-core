angular.module('os.biospecimen.visit.spr', ['os.biospecimen.models'])
  .controller('VisitSprCtrl', function($scope, $sce, visit, DeleteUtil, Alerts, $window) {

    function init() {
      $scope.sprUploader = {};
      $scope.sprUrl = $sce.trustAsResourceUrl(visit.getSprFileUrl(false));
      $scope.sprPdfFileUrl = $sce.trustAsResourceUrl(visit.getSprFileUrl(true));
      $scope.spr = {name: visit.sprName, locked: visit.sprLocked};
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
          $scope.spr.isTextSpr = (sprText == '' ? false : true);
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
            $scope.spr.name = undefined;
          }
        }
      );
    }

    $scope.toggleSprLock = function(lock) {
      visit.updateSprLockStatus(lock).then(function(result) {
        $scope.spr.locked = result.locked;
        visit.sprLocked = result.locked;
        if ($scope.spr.locked) {
          Alerts.success("visits.spr_locked");
        } else {
          Alerts.success("visits.spr_unlocked");
        }
      });
    }
    
    $scope.downloadSpr = function() {
      $window.open($scope.sprUrl, '_blank');
    }

    init();
  });
