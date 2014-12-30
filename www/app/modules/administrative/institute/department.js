
angular.module('os.administrative.institute.departments', [])
  .filter('showDepartments', function() {
    return function(departments) {
      if(!departments) {
        return "";
      }

      var result = [];
      angular.forEach(departments, function(department) {
        result.push(department.name);
      });

      return result.join(", ");
    }
  });