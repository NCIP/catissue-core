
angular.module('os.common.import.addctrl', ['os.common.import.importjob'])
  .controller('ImportObjectCtrl', function(
    $scope, $sce, $state, importDetail, 
    Form, Alerts, ImportJob) {

    function init() {
      $scope.importDetail = importDetail;
      $scope.importJobsFileUrl = $sce.trustAsResourceUrl(ImportJob.url() + 'input-file');
      $scope.inputFileTmplUrl  = $sce.trustAsResourceUrl(
        ImportJob.url() + 'input-file-template?schema=' + importDetail.objectType);
      $scope.fileImporter = {};
 
      $scope.importJob = new ImportJob({
        objectType: importDetail.objectType,
        importType: importDetail.importType || 'CREATE',
        csvType: importDetail.csvType || 'SINGLE_ROW_PER_OBJ',
        inputFileId: undefined,
        objectParams: {
          entityType: importDetail.entityType,
          formName: undefined
        }
      });

      $scope.extn = {
        selectedForm: [],
        formsList: []
      }

      if (importDetail.objectType == 'extensions') {
        Form.listForms(importDetail.entityType).then(
          function(forms) {
            forms = forms.filter(function(form){ return !form.sysForm; });
            $scope.extn.formsList = forms;
            if (forms.length > 0) {
              $scope.extn.selectedForm = forms[0];
              $scope.onFormSelect();
            }
          }
        );
      }
    }

    function submitJob(fileId) {
      $scope.importJob.inputFileId = fileId;
      $scope.importJob.$saveOrUpdate().then(
        function(resp) {
          Alerts.success('bulk_imports.job_submitted', resp);
          $state.go(importDetail.onSuccess.state, importDetail.onSuccess.params);
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

    $scope.onFormSelect = function() {
      var formName = $scope.extn.selectedForm.name;
      $scope.inputFileTmplUrl  = $sce.trustAsResourceUrl(
        ImportJob.url() + 'input-file-template?' +
        'schema=' + importDetail.objectType +
        '&formName=' + formName +
        '&entityType=' + importDetail.entityType
      );
      $scope.importJob.objectParams.formName = formName;
    };

    init();
  });
