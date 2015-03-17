angular.module('os.administrative.institute.list', ['os.administrative.models'])
  .controller('InstituteListCtrl', function($scope, $state, Institute, Util) {

    function init() {
      $scope.instituteFilterOpts = {};
      loadInstitutes();
      Util.filter($scope, 'instituteFilterOpts', filter);
    }

    var loadInstitutes = function(filterOpts) {
      Institute.query(filterOpts).then(
        function(instituteList) {
          $scope.instituteList = instituteList;
        }
      )
    }

    $scope.showInstituteOverview = function(institute) {
      $state.go('institute-detail.overview', {instituteId: institute.id});
    };

    function filter(filterOpts) {
      loadInstitutes(filterOpts);
    }

    init();
  });
