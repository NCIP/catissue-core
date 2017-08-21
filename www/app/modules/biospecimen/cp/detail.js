
angular.module('os.biospecimen.cp.detail', ['os.biospecimen.models'])
  .controller('CpDetailCtrl', function($scope, $q, $translate, cp, CollectionProtocol, PvManager, DeleteUtil, CpSettingsReg) {

    function init() {
      //
      // SOP document names are of form: <cp id>_<actual filename>
      //
      if (!!cp.sopDocumentName) {
        cp.$$sopDocDispName = cp.sopDocumentName.substring(cp.sopDocumentName.indexOf("_") + 1);
      }

      $scope.cp = cp;
      $scope.cp.repositoryNames = cp.getRepositoryNames();
      $scope.downloadUri = CollectionProtocol.url() + cp.id + '/definition';
      $scope.sites = PvManager.getSites();

      var opts = {sites: cp.repositoryNames, cp: cp.shortTitle};
      angular.extend($scope.cpResource.updateOpts, opts);
      angular.extend($scope.cpResource.deleteOpts, opts);

      CpSettingsReg.getSettings().then(
        function(settings) {
          $scope.settings = settings;
        }
      );
    }

    $scope.editCp = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteCp = function() {
      DeleteUtil.delete($scope.cp, {onDeleteState: 'cp-list', forceDelete: true});
    }

    init();
  });
