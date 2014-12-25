
angular.module('os.biospecimen.cp.addedit', ['os.biospecimen.models', 'os.administrative.models'])
  .controller('CpAddEditCtrl', function($scope, $state, CollectionProtocol, PvManager, User) {
  
    function init() {
      $scope.cp = new CollectionProtocol();
      $scope.ppidFmt = {};
      $scope.coordinators = [];
      $scope.clinicalDiagnoses = [];

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

    function getPpidFmt() {
      var result = $scope.ppidFmt.prefix || '';
      if ($scope.ppidFmt.digitsWidth) {
        result += '%0' + $scope.ppidFmt.digitsWidth + 'd';
      }

      result += ($scope.ppidFmt.suffix || '');
      return result;
    };

    $scope.searchClinicalDiagnoses = function(searchTerm) {
      $scope.clinicalDiagnoses = PvManager.getClinicalDiagnoses({searchTerm: searchTerm});
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
