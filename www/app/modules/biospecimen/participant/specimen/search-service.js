angular.module('os.biospecimen.specimen.search', ['os.biospecimen.models'])
  .factory('SpecimenSearchSvc', function($state, Specimen, Alerts) {
    var matchingSpecimens = [];
    var searchKey = undefined;

    function search(searchData) {
      Specimen.listByLabels(searchData.label).then(
        function(specimens) {
          if (specimens == undefined || specimens.length == 0) {
            Alerts.error('search.error', {entity: 'Specimen', key: searchData.label});
            return;
          } else if (specimens.length > 1) {
            matchingSpecimens = specimens;
            searchKey = searchData.label;
            $state.go('specimen-search', {}, {reload: true});
          } else {
            showSpecimenDetail(specimens[0]);
          }
        }
      );
    }

    function getSpecimens() {
      return matchingSpecimens;
    }

    function getSearchKey() {
      return searchKey;
    }

    function showSpecimenDetail(specimen) {
      var params = {
        cpId: specimen.cpId,
        cprId: specimen.cprId,
        visitId: specimen.visitId,
        specimenId: specimen.id
      };
      $state.go('specimen-detail.overview', params);
    }

    return {
      getSpecimens: getSpecimens,

      getSearchKey: getSearchKey,

      search: search,

      showSpecimenDetail: showSpecimenDetail
    };
  })
  .controller('SpecimenResultsView', function($scope, specimens, searchKey, SpecimenSearchSvc) {
    function init() {
      $scope.specimens = specimens;
      $scope.searchKey = searchKey;
    }

    $scope.showSpecimenDetail = SpecimenSearchSvc.showSpecimenDetail

    init();
  });
