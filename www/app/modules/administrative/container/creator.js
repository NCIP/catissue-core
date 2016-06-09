angular.module('os.administrative.container')
  .directive('osContainerCreator', function(Site, Container, ContainerType, Alerts) {
    function linker(scope, element, attrs) {
      scope.container  = new Container(scope.container || {});
       
      scope.types = []; 
      ContainerType.query().then(
        function(types) {
          scope.types = types;
        }
      );

      scope.sites = [];
      Site.listForContainers('Create').then(
        function(sites) {
          scope.sites = sites;
        }
      );

      scope.createContainer = function() {
        scope.container.$saveOrUpdate().then(
          function(savedContainer) {
            //
            // ID is purposely removed so that same widget can be used for
            // creation of multiple containers
            //
            delete savedContainer.id;
            angular.extend(scope.container, savedContainer);
            Alerts.success('container.creation_success', savedContainer);
          }
        );
      }
    }

    return {
      restrict: 'E',

      templateUrl: 'modules/administrative/container/creator.html',

      scope: {
        container: '='
      },

      compile: function(element, attrs) {
        if (attrs.mdInput == 'true') {
          element.find('input[type="text"], os-select, os-storage-position')
            .attr('os-md-input', 'os-md-input');
        }

        if (attrs.showLabels == 'false') {
          element.find('label').hide();
        }

        return linker;
      }
    }
  });
