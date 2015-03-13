
angular.module('os.administrative.models.container', ['os.common.models'])
  .factory('Container', function(osModel, $q, $http) {
    var Container = new osModel('storage-containers');

    Container.list = function(opts) {
      var defOpts = {topLevelContainers: true};
      return Container.query(angular.extend(defOpts, opts || {}));
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
      return Container.query({parentContainerId: this.$id(), includeChildren: true});
    };

    Container.prototype.getOccupiedPositions = function() {
      return $http.get(Container.url() + '/' + this.$id() + '/occupied-positions').then(
        function(result) {
          return result.data;
        }
      );
    };

    Container.prototype.isSpecimenAllowed = function(cpId, specimenClass, specimenType) {
      var params = {cpId: cpId, specimenClass: specimenClass, specimenType: specimenType};
      return $http.head(Container.url() + '/' + this.$id(), {params: params}).then(
        function(result) {
          return true;
        },

        function(err) {
          return false;
        }
      );
    };

    return Container;
  });
