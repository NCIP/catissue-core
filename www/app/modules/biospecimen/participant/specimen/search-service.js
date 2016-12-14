angular.module('os.biospecimen.specimen.search', ['os.biospecimen.models'])
  .factory('SpecimenSearchSvc', function($state, $q, Specimen, Alerts, CollectionProtocol) {
    var matchingSpecimens = [];
    var searchKey = undefined;
    var barcodingEnabled = undefined;

    function search(searchData) {
      var opts = {label: searchData.label, barcode: searchData.label};
      Specimen.query(opts).then(
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

    function isBarcodingEnabled() {
      if (barcodingEnabled != undefined) {
        var deferred = $q.defer();
        deferred.resolve(barcodingEnabled);
        return deferred.promise;
      }

      return CollectionProtocol.getBarcodingEnabled().then(
        function(setting) {
          barcodingEnabled = setting;
          return barcodingEnabled;
        }
      );
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

      showSpecimenDetail: showSpecimenDetail,

      isBarcodingEnabled: isBarcodingEnabled
    };
  })
  .controller('SpecimenResultsView', function($scope, specimens, searchKey, SpecimenSearchSvc) {
    function init() {
      $scope.specimens = specimens;
      $scope.searchKey = searchKey;
      $scope.barcodingEnabled = false;

      SpecimenSearchSvc.isBarcodingEnabled().then(
        function(barcodingEnabled) {
          $scope.barcodingEnabled = barcodingEnabled;
        }
      );
    }

    $scope.showSpecimenDetail = SpecimenSearchSvc.showSpecimenDetail

    init();
  });
