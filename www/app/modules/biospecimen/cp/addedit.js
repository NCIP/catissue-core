
angular.module('os.biospecimen.cp.addedit', ['os.biospecimen.models', 'os.administrative.models'])
  .controller('CpAddEditCtrl', function(
    $scope, $state, $stateParams, $sce, $timeout,
    cp, extensionCtxt, CollectionProtocol, User, Site, ExtensionsUtil, PvManager) {

    function init() {
      $scope.cp = cp;

      $scope.sopDocUploader = {ctrl: {}};
      $scope.sopDocUploadUrl = $sce.trustAsResourceUrl(CollectionProtocol.getSopDocUploadUrl());

      $scope.deFormCtrl = {};
      $scope.extnOpts = ExtensionsUtil.getExtnOpts(cp, extensionCtxt);
      $scope.coordinators = [];

      loadPvs();

      if (!!cp.id && $stateParams.mode == 'copy') {
        $scope.mode = 'copy';
        $scope.copyFrom = cp.id;
        angular.forEach(cp.cpSites, function(site) {
          delete site.id;
        });
        delete cp.id;
        cp.title = cp.shortTitle = cp.code = cp.sopDocumentName = cp.sopDocumentUrl = "";
      } else {
        cp.$$sopDocumentName = cp.sopDocumentName;
        if (!!cp.sopDocumentName) {
          cp.sopDocumentName = cp.sopDocumentName.substring(cp.sopDocumentName.indexOf("_") + 1);
        }
      }
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
    }

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

      if ($scope.sopDocUploader.ctrl.data) {
        $scope.sopDocUploader.ctrl.submit().then(
          function(filename) {
            cp.sopDocumentName = cp.$$sopDocumentName = filename;
            cp.sopDocumentUrl = undefined;
            saveCp(cp);
          }
        );
      } else {
        if ($scope.cp.sopDocumentUrl) {
          cp.sopDocumentName = undefined; 
        } else {
          cp.sopDocumentName = cp.$$sopDocumentName;
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
