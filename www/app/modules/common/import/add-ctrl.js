
angular.module('os.common.import.addctrl', ['os.common.import.importjob'])
  .controller('ImportObjectCtrl', function(
    $rootScope, $scope, $sce, $state, importDetail,
    Form, ImportJob, Alerts, Util, SettingUtil) {

    var maxTxnSize = undefined;

    function init() {
      $scope.importDetail = importDetail;
      $scope.importJob = new ImportJob({
        objectType: importDetail.objectType,
        importType: importDetail.importType || 'CREATE',
        csvType: importDetail.csvType || 'SINGLE_ROW_PER_OBJ',
        dateFormat: $rootScope.global.shortDateFmt,
        timeFormat: $rootScope.global.timeFmt,
        inputFileId: undefined,
        objectParams: importDetail.objectParams || {}
      });

      $scope.importJobsFileUrl = $sce.trustAsResourceUrl(ImportJob.url() + 'input-file');
      $scope.inputFileTmplUrl  = getInputTmplUrl($scope.importJob);
      $scope.fileImporter = {};

      if (importDetail.types && importDetail.types.length > 0) {
        $scope.onTypeSelect(importDetail.types[0]);
      }
    }

    function getInputTmplUrl(importJob) {
      var url = ImportJob.url() + 'input-file-template?' + getQueryParams(importJob);
      return $sce.trustAsResourceUrl(url);
    }

    function getQueryParams(importJob) {
      var qs = 'schema=' + importJob.objectType;
      angular.forEach(importJob.objectParams,
        function(value, key) {
          qs += '&' + key + '=' + value;
        }
      );

      return qs;
    }

    function submitJob(fileId, atomic) {
      $scope.importJob.inputFileId = fileId;
      $scope.importJob.atomic = atomic;
      $scope.importJob.$saveOrUpdate().then(
        function(resp) {
          if (resp.status == 'TXN_SIZE_EXCEEDED') {
            Util.showConfirm({
              ok: function () {
                submitJob(fileId, false);
              },

              title: "common.warning",
              isWarning: true,
              confirmMsg: "bulk_imports.txn_size_exceeded",
              input: { maxTxnSize: maxTxnSize, inputRecsCount: resp.totalRecords }
            });
          } else {
            Alerts.success('bulk_imports.job_submitted', resp);
            $state.go(importDetail.onSuccess.state, importDetail.onSuccess.params);
          }
        }
      );
    }

    $scope.import = function() {
      $scope.fileImporter.submit().then(
        function(resp) {
          submitJob(resp.fileId, true);
        }
      );
    };

    $scope.onTypeSelect = function(objectType) {
      $scope.importDetail.objectType = objectType.type;
      $scope.importDetail.showImportType = objectType.showImportType;
      $scope.importDetail.importType = objectType.importType;

      var importJob          = $scope.importJob;
      importJob.objectType   = objectType.type;
      importJob.importType   = objectType.importType || 'CREATE',
      importJob.csvType      = objectType.csvType || 'SINGLE_ROW_PER_OBJ',
      importJob.objectParams = objectType.params;

      $scope.inputFileTmplUrl  = getInputTmplUrl(importJob);
    };

    //
    // Initialise after the setting value is resolved
    //
    SettingUtil.getSetting('common', 'import_max_records_per_txn').then(
      function(setting) {
        maxTxnSize = setting.value;
        init();
      }
    );
  });
