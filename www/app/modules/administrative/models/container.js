
angular.module('os.administrative.models.container', ['os.common.models'])
  .factory('Container', function(osModel, $q, $http) {
    var Container = new osModel('storage-containers');

    Container.list = function() {
      return Container.query();
    };

    Container.listForSite = function(siteName, onlyFreeContainers, flatten) {
      var params = {
        site: siteName,
        onlyFreeContainers: !!onlyFreeContainers,
        flatten: !flatten
      };
      return Container.query(params);
    };

    Container.flatten = function(containers) {
      return Container._flatten(containers, 'childContainers');
    };

    Container.prototype.getChildContainers = function(anyLevel) {
      return Container.query({parentContainerId: this.$id(), anyLevelContainers: anyLevel});
    };

    Container.prototype.getOccupiedPositions = function() {
      return $http.get(Container.url() + '/' + this.$id() + '/occupied-positions').then(
        function(result) {
          return result.data;
        }
      );
    };

    return Container;
  });
