
angular.module('os.biospecimen.cp')
  .controller('CpLabelSettingsCtrl', function($scope, $translate, cp, PvManager) {

    function init() {
      $scope.settingCtx = {
        spmnLabelPrePrintModes: PvManager.getPvs('specimen-label-pre-print-modes'),
        visitNamePrintModes: PvManager.getPvs('visit-name-print-modes')
      };
      
      if (!cp.spmnLabelPrintSettings || cp.spmnLabelPrintSettings.length == 0) {
        cp.spmnLabelPrintSettings = [
          {"lineage": "New"},
          {"lineage": "Derived"},
          {"lineage": "Aliquot"}
        ];
      }
      setViewCtx();
    };

    function setViewCtx() {
      angular.extend($scope.settingCtx, {view: 'view_setting', cp: cp});
      setUserInputLabels(cp);
      onPrePrintModeChange();
    }

    function setEditCtx() {
      angular.extend($scope.settingCtx, {view: 'edit_setting', cp: angular.copy(cp)});
      onPrePrintModeChange();
    }

    function setUserInputLabels(cp) {
      $scope.settingCtx.userInputLabels = '';
      $translate('cp.label_format.ppid').then(
        function() {
          var result = [];

          if (cp.manualPpidEnabled) {
            result.push($translate.instant('cp.label_format.ppid'));
          }

          if (cp.manualVisitNameEnabled) {
            result.push($translate.instant('cp.label_format.visit'));
          }

          if (cp.manualSpecLabelEnabled) {
            result.push($translate.instant('cp.label_format.specimen'));
          }

          $scope.settingCtx.userInputLabels = result.join(", ");
        }
      );
    }

    function onPrePrintModeChange() {
      $scope.settingCtx.prePrintDisabled = ($scope.settingCtx.cp.spmnLabelPrePrintMode == 'NONE');

      loadLabelAutoPrintModes();
      if ($scope.settingCtx.prePrintDisabled) {
        angular.forEach($scope.settingCtx.cp.spmnLabelPrintSettings,
          function(setting) {
            if (setting.printMode == 'PRE_PRINT') {
              setting.printMode = '';
            }
          }
        );
      }
    }
    
    function loadLabelAutoPrintModes() {
      $scope.settingCtx.spmnLabelAutoPrintModes = [];

      PvManager.loadPvs('specimen-label-auto-print-modes').then(
        function(pvs) {
          if ($scope.settingCtx.cp.spmnLabelPrePrintMode != 'NONE') {
            $scope.settingCtx.spmnLabelAutoPrintModes = pvs;
          } else {
            $scope.settingCtx.spmnLabelAutoPrintModes = pvs.filter(
              function(pv) {
                return pv.name != 'PRE_PRINT';
              }
            );
          }
        }
      );
    }

    $scope.showEditForm = function() {
      setEditCtx();
    }

    $scope.onPrePrintModeChange = onPrePrintModeChange;

    $scope.save = function() {
      delete $scope.settingCtx.cp.repositoryNames;
      delete $scope.settingCtx.cp.extensionDetail;
      delete $scope.settingCtx.cp.catalogSetting;

      $scope.settingCtx.cp.$saveOrUpdate().then(
        function(savedcp) {
          angular.extend(cp, savedcp);
          setViewCtx();
        }
      );
    };

    $scope.revertEdit = function() {
      setViewCtx();
    }

    init();
  });
