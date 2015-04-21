
angular.module('os.common.import.addctrl', ['os.common.import.importjob'])
  .controller('ImportObjectCtrl', function($scope, $sce, $state, importDetail, Alerts, ImportJob) {
    function init() {
      $scope.importDetail = importDetail;
      $scope.importJobsFileUrl = $sce.trustAsResourceUrl(ImportJob.url() + 'input-file');
      $scope.fileImporter = {};
 
      $scope.importJob = new ImportJob({
        objectType: importDetail.objectTypes[0].type,
        importType: 'CREATE',
        inputFileId: undefined
      });
    }

    function submitJob(fileId) {
      $scope.importJob.inputFileId = fileId;
      $scope.importJob.$saveOrUpdate().then(
        function(resp) {
          Alerts.success('bulk_imports.job_submitted', resp);
          $state.go(importDetail.onSuccessState);
        }
      );
    }

    $scope.import = function() {
      $scope.fileImporter.submit().then(
        function(resp) {
          submitJob(resp.fileId);
        }
      );
    };

    init();
  });
