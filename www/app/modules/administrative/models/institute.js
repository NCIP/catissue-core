
angular.module('os.administrative.models.institute', ['os.common.models'])
  .factory('Institute', function(osModel, $q) {
    var Institute = osModel('institutes');
    
    Institute.list = function() {
      return Institute.query();
    }
    
    Institute.getDepartments = function (instituteName) {
      var d = $q.defer();
      var departments = [{"id" : 1, "name": "HBRC"}];
      d.resolve(departments);
      return d.promise;
    }
    
    return Institute;
  });

