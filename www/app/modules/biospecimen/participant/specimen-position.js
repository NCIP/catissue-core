
angular.module('os.biospecimen.participant.specimen-position', ['os.administrative.models'])
  .directive('osSpecimenPosition', function(Container) {
    function loadContainers(name, scope) {
      var params = {
        name: name, 
        onlyFreeContainers: true,
        cpId: scope.cpId,
        specimenClass: scope.specimen.specimenClass,
        specimenType: scope.specimen.type
      };

      return Container.query(params).then(
        function(containers) {
          scope.containers = containers.map(
            function(container) { 
              return container.name; 
            }
          );
        }
      );
    };

    function linker(scope, element, attrs) {
      var specimen = scope.specimen;

      scope.onContainerChange = function() {
        specimen.storageLocation = {name: specimen.storageLocation.name};
      };

      scope.searchContainer = function(name) {
        if (!specimen.type) {
          return;
        }

        loadContainers(name, scope);
      };

      scope.$watch('specimen.type', function(newVal, oldVal) {
        if (!newVal || newVal == oldVal) {
          return;
        }

        loadContainers('', scope);
      });
    };

    return {
      restrict: 'E',
  
      replace: true,

      priority: 100,

      terminal: true,

      scope: {
        specimen: '=',
        cpId: '='
      },

      compile: function(tElem, tAttrs) {
        if (tAttrs.hasOwnProperty('osMdInput')) {
          var select = tElem.find('os-select');
          var input = tElem.find('input');

          select.attr('os-md-input', '');
          input.attr('os-md-input', '');

          if (tAttrs.hasOwnProperty('editWhen')) {
            select.attr('edit-when', tAttrs.editWhen);
            input.attr('edit-when', tAttrs.editWhen);
          }

          tElem.find('button').addClass('btn-xs');
        }
 
        return linker;
      },


      templateUrl: 'modules/biospecimen/participant/specimen-position.html'
    }
  });
    
