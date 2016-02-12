
angular.module('os.biospecimen.cp.addedit', ['os.biospecimen.models', 'os.administrative.models'])
  .controller('CpAddEditCtrl', function($scope, $state, cp, extensionCtxt, User, Site, Util, PvManager) {

    function init() {
      $scope.cp = cp;
      $scope.deFormCtrl = {};
      $scope.extnOpts = Util.getExtnOpts(cp, extensionCtxt);
      $scope.ppidFmt = cp.getUiPpidFmt();
      $scope.coordinators = [];

      if (!cp.spmnLabelPrintSettings || cp.spmnLabelPrintSettings.length == 0) {
        $scope.cp.spmnLabelPrintSettings = [
          {"lineage": "New"},
          {"lineage": "Derived"},
          {"lineage": "Aliquot"}
        ];
      }

      loadPvs();

      $scope.$watch('ppidFmt', function(newVal) {
        var sample = newVal.prefix || '';

        if (newVal.digitsWidth && newVal.digitsWidth > 0) {
          for (var i = 0; i < newVal.digitsWidth - 1; ++i) {
            sample += '0';
          }

          sample += '7';  
        }

        sample += (newVal.suffix || '');
        $scope.ppidFmt.samplePpid = sample;
      }, true);
    };

    function loadPvs() {
      $scope.sites = [];
      var op = !$scope.cp.id ? 'Create' : 'Update';
      var opts = {resource:'CollectionProtocol', operation: op};
      Site.query(opts).then(function(sites) {
         angular.forEach(sites, function(site) {
           $scope.sites.push(site.name);
         })
         
         $scope.cp.repositoryNames = cp.getRepositoryNames();
      });

      $scope.spmnLabelPrePrintModes = PvManager.getPvs('specimen-label-pre-print-modes');
      loadLabelAutoPrintModes();
    }

    function loadLabelAutoPrintModes() {
      $scope.spmnLabelAutoPrintModes = [];

      PvManager.loadPvs('specimen-label-auto-print-modes').then(
        function(pvs) {
          if ($scope.cp.spmnLabelPrePrintMode != 'NONE') {
            $scope.spmnLabelAutoPrintModes = pvs;
          } else {
            $scope.spmnLabelAutoPrintModes = pvs.filter(
      	      function(pv) {
      	        return pv.name != 'PRE_PRINT';
      	      }
      	    );
          }
        }
      );
    }

    function getPpidFmt() {
      var result = $scope.ppidFmt.prefix || '';
      if ($scope.ppidFmt.digitsWidth) {
        result += '%0' + $scope.ppidFmt.digitsWidth + 'd';
      }

      result += ($scope.ppidFmt.suffix || '');
      return result;
    };

    $scope.createCp = function() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      var cp = angular.copy($scope.cp);
      delete cp.repositoryNames;
      cp.ppidFmt = getPpidFmt();

      if (formCtrl) {
        cp.extensionDetail = formCtrl.getFormData();
      }

      cp.$saveOrUpdate().then(
        function(savedCp) {
          $state.go('cp-detail.overview', {cpId: savedCp.id});
        }
      );
    };

    $scope.onRepositorySelect = function(repositoryName) {
      if (!$scope.cp.cpSites) {
        $scope.cp.cpSites = [];
      }
      $scope.cp.cpSites.push({siteName: repositoryName, code: undefined});
    }

    $scope.onRepositoryRemove = function(repositoryName) {
      var sites = $scope.cp.cpSites;
      for (var i = 0; i < sites.length; i++) {
        if (sites[i].siteName == repositoryName) {
          $scope.cp.cpSites.splice(i, 1);
          break;
        }
      }
    }

    $scope.onPrePrintModeChange = function() {
      $scope.prePrintDisabled = !!$scope.cp.id && $scope.cp.spmnLabelPrePrintMode == 'NONE';

      loadLabelAutoPrintModes();
      if ($scope.prePrintDisabled) {
        angular.forEach($scope.cp.spmnLabelPrintSettings, function(setting) {
          if (setting.printMode == 'PRE_PRINT') {
            setting.printMode = '';
          }
        });
      }
    }

    init();
  });
