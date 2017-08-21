
angular.module('os.biospecimen.cp.addedit', ['os.biospecimen.models', 'os.administrative.models'])
  .controller('CpAddEditCtrl', function(
    $scope, $state, $stateParams, $sce, $timeout,
    cp, extensionCtxt, CollectionProtocol, User, Site, ExtensionsUtil, PvManager) {

    function init() {
      $scope.cp = cp;
      $scope.op = !cp.id ? 'Create' : 'Update';
      $scope.cp.repositoryNames = cp.getRepositoryNames();

      $scope.sopDocUploader = {ctrl: {}};
      $scope.sopDocUploadUrl = $sce.trustAsResourceUrl(CollectionProtocol.getSopDocUploadUrl());

      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(cp, extensionCtxt);
      $scope.coordinators = [];

      if (!!cp.id && $stateParams.mode == 'copy') {
        $scope.mode = 'copy';
        $scope.copyFrom = cp.id;
        angular.forEach(cp.cpSites, function(site) {
          delete site.id;
        });
        delete cp.id;
        cp.title = cp.shortTitle = cp.code = cp.sopDocumentName = cp.sopDocumentUrl = "";
      } else {
        if (!!cp.sopDocumentName) {
          cp.$$sopDocDispName = cp.sopDocumentName.substring(cp.sopDocumentName.indexOf("_") + 1);
        }
      }
    };

    function saveCp(cp) {
      var q;
      if ($scope.mode == 'copy') {
        q = cp.copy($scope.copyFrom);
      } else {
        q = cp.$saveOrUpdate();
      }

      q.then(
        function(savedCp) {
          $state.go('cp-detail.overview', {cpId: savedCp.id});
        }
      );
    }

    $scope.createCp = function() {
      var formCtrl = $scope.deFormCtrl.ctrl;
      if (formCtrl && !formCtrl.validate()) {
        return;
      }

      var cp = angular.copy($scope.cp);
      delete cp.repositoryNames;

      if (formCtrl) {
        cp.extensionDetail = formCtrl.getFormData();
      }

      if ($scope.sopDocUploader.ctrl.isFileSelected()) {
        $scope.sopDocUploader.ctrl.submit().then(
          function(filename) {
            cp.sopDocumentName = cp.$$sopDocDispName = filename;
            cp.sopDocumentUrl = undefined;
            saveCp(cp);
          }
        );
      } else {
        if ($scope.cp.sopDocumentUrl) {
          cp.sopDocumentName = undefined; 
        }

        saveCp(cp);
      }
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

    $scope.removeSopDocument = function() {
      cp.$$sopDocumentName = cp.sopDocumentName = undefined;
    }

    init();
  });
