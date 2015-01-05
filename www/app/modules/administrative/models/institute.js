
angular.module('os.administrative.models.institute', ['os.common.models'])
  .factory('Institute', function(osModel, $q) {
    var Institute = osModel('institutes');
    
    Institute.getDepartments = function (institute) {
      var d = $q.defer();
      var departments = [{"id" : 1, "name": "HBRC"}];
      d.resolve(departments);
      return d.promise;
    }
    
    return Institute;
  });

