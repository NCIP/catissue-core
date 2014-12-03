
angular.module('openspecimen')
  .controller('ParticipantSpecimensTreeCtrl', function($scope, visits, specimens) {
    var flatten = function(specimens) {
      var result = [];
      flatten0(specimens, undefined, 0, result);
      return result;
    }

    var flatten0 = function(specimens, parent, depth, result) {
      if (!specimens) {
        return;
      }

      for (var i = 0; i < specimens.length; ++i) {
        result.push(specimens[i]);
        specimens[i].depth = depth;
        specimens[i].parent = parent;
        specimens[i].hasChildren = (!!specimens[i].children && specimens[i].children.length > 0);
        if (specimens[i].hasChildren) {
          flatten0(specimens[i].children, specimens[i], depth + 1, result);
        }
      }
    };

    $scope.specimens = flatten(specimens);

    $scope.openSpecimenNode = function(specimen) {
      specimen.isOpened = true;
    };

    $scope.closeSpecimenNode = function(specimen) {
      specimen.isOpened = false;
    };
  })
  .filter('openedSpecimenNodes', function() {
    var showSpecimen = function(specimen) {
      if (specimen.depth == 0) {
        return true;
      }

      var show = true;
      while (!!specimen.parent) {
        if (!specimen.parent.isOpened) {
          show = false;
          break;
        }

        specimen = specimen.parent;
      }

      return show;
    };

    return function(input) {
      if (!input) {
        return [];
      }

      var result = [];
      angular.forEach(input, function(specimen) {
        if (showSpecimen(specimen)) {
          result.push(specimen);
        }
      });

      return result;
    };
  });
