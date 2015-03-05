
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
      angular.forEach(uiSpecimens, function(specimen) {
        if (specimen.status == 'Collected' || !specimen.selected || visited.indexOf(specimen) > 0) {
          return;
        }

        visited.push(specimen);
        result.push({
          id: specimen.id,
          initialQty: specimen.initialQty,
          label: specimen.label,
          reqId: specimen.reqId,
          visitId: $scope.visit.id,
          storageLocation: specimen.storageLocation,
          children: getSpecimensToSave(specimen.children, visited)
        });  
      });

      return result;
    };

    init();
  });
