
angular.module('os.administrative.models.container', ['os.common.models'])
  .factory('Container', function(osModel, $q, $http) {
    var Container = new osModel('storage-containers');

    Container.list = function() {
      return Container.query();
    };

    Container.listForSite = function(siteName, onlyFreeContainers, flatten) {
      var params = {
        site: siteName,
        anyLevelContainers: true,
        onlyFreeContainers: !!onlyFreeContainers
      };

      var ret = Container.query(params);
      if (flatten == true) {
        ret = ret.then(
          function(containers) {
            return Container._flatten(containers, 'childContainers');
          }
        );
      }
        
      return ret;
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
