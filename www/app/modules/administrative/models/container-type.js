
angular.module('os.administrative.models.containertype', ['os.common.models'])
  .factory('ContainerType', function(osModel) {
    var ContainerType = new osModel('container-types');

    ContainerType.prototype.getType = function() {
      return 'container_type';
    }

    ContainerType.prototype.getDisplayName = function() {
      return this.name;
    }
    
    return ContainerType;
  });
