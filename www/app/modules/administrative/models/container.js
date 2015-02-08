
angular.module('os.administrative.models.container', ['os.common.models'])
  .factory('Container', function(osModel, $q, $http) {
    var Container = new osModel('storage-containers');

    Container.list = function() {
      return Container.query();
    };

    Container.listForSite = function(siteName) {
      // TODO : Write a REST API to get Containers by Site.
      containerList = []; //['Box A', 'Box B', 'Box C'];
      var d = $q.defer();
      d.resolve(containerList);
      return d.promise;
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
