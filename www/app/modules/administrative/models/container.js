
angular.module('os.administrative.models.container', ['os.common.models'])
  .factory('Container', function(osModel, $q) {
    var Container = new osModel('storage-containers');

    Container.list = function() {
      return Container.query();
    }

    Container.listForSite = function(siteName) {
      // TODO : Write a REST API to get Containers by Site.
      containerList = ['Box A', 'Box B', 'Box C'];
      var d = $q.defer();
      d.resolve(containerList);
      return d.promise;
    }

    return Container;
  });