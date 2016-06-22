angular.module('os.administrative.container')
  .directive('osContainerCreator', function(Site, Container, ContainerType, Alerts) {
    function selectType(container, types) {
      if (types.length != 1) {
        return;
      }

      container.typeName = types[0].name;
    }

    function linker(scope, element, attrs) {
      scope.container  = new Container(scope.container || {});
       
      var data = {};
      if (!!attrs.data) {
        data = JSON.parse(attrs.data);
      }

      var allowedTypes = data.allowedTypes || [];
      if (allowedTypes.length == 0) {
        scope.types = [];
        ContainerType.query().then(
          function(types) {
            scope.types = types;
            selectType(scope.container, scope.types);
          }
        );
      } else {
        scope.types = allowedTypes.map(function(type) { return {name: type}; });
        selectType(scope.container, scope.types);
      }

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
