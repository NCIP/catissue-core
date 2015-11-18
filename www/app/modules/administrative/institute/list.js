angular.module('os.administrative.institute.list', ['os.administrative.models'])
  .controller('InstituteListCtrl', function($scope, $state, Institute, Util) {

    function init() {
      $scope.instituteFilterOpts = {includeStats: true};
      loadInstitutes($scope.instituteFilterOpts);
      Util.filter($scope, 'instituteFilterOpts', loadInstitutes);
    }

    function loadInstitutes(filterOpts) {
      Institute.query(filterOpts).then(
        function(instituteList) {
          $scope.instituteList = instituteList;
        }
      );
    }

    $scope.showInstituteOverview = function(institute) {
      $state.go('institute-detail.overview', {instituteId: institute.id});
    };

    init();
  });
