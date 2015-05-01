
angular.module('os.biospecimen.cp.addedit', ['os.biospecimen.models', 'os.administrative.models'])
  .controller('CpAddEditCtrl', function($scope, $state, cp, User, Site) {

    var repositoryNames = null;

    function init() {
      $scope.cp = cp;

      /**
       * Some how the ui-select's multiple option is removing pre-selected items
       * when site list is being loaded or not yet loaded...
       * Therefore we copy pre-selected repositoryNames and then use it when all Sites are loaded
       */
      repositoryNames = angular.copy(cp.repositoryNames);

      $scope.ppidFmt = {};
      $scope.coordinators = [];

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
         
         $scope.cp.repositoryNames = repositoryNames;
      });
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
      var cp = angular.copy($scope.cp);
      cp.ppidFmt = getPpidFmt();
      cp.$saveOrUpdate().then(
        function(savedCp) {
          $state.go('cp-detail.overview', {cpId: savedCp.id});
        }
      );
    };

    init();
  });
