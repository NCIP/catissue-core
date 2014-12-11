angular.module('os.biospecimen.models.specimen', ['os.common.models'])
  .factory('Specimen', function(osModel, $http) {
    var Specimen = osModel('specimens');
 
    Specimen.listFor = function(cprId, visitDetail) {
      return Specimen.query(angular.extend({cprId: cprId}, visitDetail || {}));
    };

    Specimen.flatten = function(specimens, parent, depth) {
      var result = [];
      if (!specimens) {
        return result;
      }

      depth = depth || 0;
      for (var i = 0; i < specimens.length; ++i) {
        result.push(specimens[i]);
        specimens[i].depth = depth || 0;
        specimens[i].parent = parent;
        specimens[i].hasChildren = (!!specimens[i].children && specimens[i].children.length > 0);
        if (specimens[i].hasChildren) {
          result = result.concat(Specimen.flatten(specimens[i].children, specimens[i], depth +1));
        }
      }

      return result;
    };

    return Specimen;
  });
