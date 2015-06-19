
angular.module('os.administrative.container.replicate', ['os.administrative.models'])
  .controller('ContainerReplCtrl', function($scope, container, Site, Container, Alerts) {
    var siteContainersMap = {};

    function init() {
      $scope.container = container;
      $scope.sites = [];

      $scope.destinations = [{
        siteName: container.siteName, 
        parentContainerName: container.parentContainerName,
        newContainerNamesTxt: ''
      }];

      loadPvs();
      onSiteSelect($scope.destinations[0]);
    }

    function loadPvs() {
      Site.listForContainers('Create').then(
        function(sites) {
          $scope.sites = sites;
        }
      );
    }

    function onSiteSelect(dest) {
      if (!siteContainersMap[dest.siteName]) {
        siteContainersMap[dest.siteName] = Container.listForSite(dest.siteName, true, true);
      }

      siteContainersMap[dest.siteName].then(
        function(containers) {
          dest.containers = containers;
        }
      );
    }

    $scope.onSiteSelect = onSiteSelect;

    $scope.addDest = function() {
      $scope.destinations.push({siteName: '', parentContainerName: '', newContainerNamesTxt: ''});
    }

    $scope.removeDest = function(idx) {
      $scope.destinations.splice(idx, 1);
    }

    $scope.replicate = function() {
      var destinations = [];
      for (var i = 0; i < $scope.destinations.length; ++i) {
        var uiDest = $scope.destinations[i];
        var names = uiDest.newContainerNamesTxt.trim().split(/,|\t/);
        for (var j = 0; j < names.length; ++j) {
          if (names[j].trim().length == 0) {
            Alerts.error("container.empty_names_not_allowed");
            return;
          }
        }

        destinations.push({
          siteName: uiDest.siteName, 
          parentContainerName: uiDest.parentContainerName,
          newContainerNames: names
        });
      }
            
      $scope.container.replicate(destinations).then(
        function() {
          Alerts.success('container.replicated_successfully', $scope.container);
          $scope.back();
        }
      );
    }

    $scope.cancel = function() {
      $scope.back();
    }

    init();
  });
