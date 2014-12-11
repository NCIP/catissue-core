
angular.module('os.biospecimen.models.cp', ['os.common.models'])
  .factory('CollectionProtocol', function(osModel, $http) {
    var CollectionProtocol = osModel('collection-protocols');

    return CollectionProtocol;
  });
