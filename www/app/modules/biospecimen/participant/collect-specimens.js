
angular.module('os.biospecimen.participant.collect-specimens', 
  [ 
    'os.biospecimen.models',
    'os.biospecimen.participant.specimen-position'
  ])
  .factory('CollectSpecimensSvc', function($state) {
    var data = {};
    return {
      collect: function(visit, specimens) {
        data.specimens = specimens;
        data.visit = visit;
        $state.go('participant-detail.collect-specimens', {visitId: visit.id});
      },

      clear: function() {
        data.specimens = [];
        data.visit = undefined;
      },

      getSpecimens: function() {
        return data.specimens; 
      },

      getVisit: function() {
        return data.visit;
      }
    };
  })
  .controller('CollectSpecimensCtrl', function($scope, Specimen, PvManager, CollectSpecimensSvc, Container) {
    function init() {
      $scope.specimens = CollectSpecimensSvc.getSpecimens();
      $scope.visit = CollectSpecimensSvc.getVisit();
      $scope.storageTypes = PvManager.getPvs('storage-type');
    };

    $scope.applyFirstLocationToAll = function() {
      for (var i = 1; i < $scope.specimens.length; i++) {
        $scope.specimens[i].storageLocation = {
          name: $scope.specimens[0].storageLocation.name
        };
      }
    };

    $scope.openSpecimenNode = function(specimen) {
      specimen.isOpened = true;
    };

    $scope.closeSpecimenNode = function(specimen) {
      specimen.isOpened = false;
    };

    $scope.saveSpecimens = function() {
      if (areDuplicateLabelsPresent($scope.specimens)) {
        $scope.errorCode = 'One or more specimens are using same label';
        $timeout(function() { $scope.errorCode = '' }, 3000);
        return;
      }

      var specimensToSave = getSpecimensToSave($scope.specimens, []);
      Specimen.save(specimensToSave).then(
        function(result) {
          if (result && result.length > 0) {
            //
            // TODO: a better way could be to merge results with existing data
            //
            //$state.go('participant-detail.visits', {}, {reload: true});
            CollectSpecimensSvc.clear();
            $scope.back();
          }
        }
      );
    };

    function areDuplicateLabelsPresent(input) {
      var labels = [];
      for (var i = 0; i < input.length; ++i) {
        if (labels.indexOf(input[i].label) != -1) {
          return true;
        }

        labels.push(input[i].label);
      }

      return false;
    };

    function getSpecimensToSave(uiSpecimens, visited) {
      var result = [];
      angular.forEach(uiSpecimens, function(uiSpecimen) {
        if (uiSpecimen.status == 'Collected' || // when specimen is already collected
            !uiSpecimen.selected ||  // when specimen is not selected on ui
            visited.indexOf(uiSpecimen) > 0) { // when specimen is already visited
          return;
        }

        visited.push(uiSpecimen);

        var specimen = getSpecimenToSave(uiSpecimen);
        specimen.children = getSpecimensToSave(uiSpecimen.children, visited);
        result.push(specimen);
        return result;
      });

      return result;
    };

    function getSpecimenToSave(uiSpecimen) {
      var specimen = {
        id: uiSpecimen.id,
        initialQty: uiSpecimen.initialQty,
        label: uiSpecimen.label,
        reqId: uiSpecimen.reqId,
        visitId: $scope.visit.id,
        storageLocation: uiSpecimen.storageLocation,
        parentId: uiSpecimen.parentId,
        lineage: uiSpecimen.lineage
      };

      if (!!specimen.reqId || specimen.lineage == 'Aliquot') {
        return specimen;
      }

      if (uiSpecimen.lineage == 'Derived') {
        specimen.specimenClass = uiSpecimen.specimenClass;
        specimen.type = uiSpecimen.type;
        specimen.pathology = uiSpecimen.pathology;
        return specimen;
      }

      specimen.anatomicSite = uiSpecimen.anatomicSite;
      specimen.laterality = uiSpecimen.laterality;
      return specimen;
    };

    init();
  });
