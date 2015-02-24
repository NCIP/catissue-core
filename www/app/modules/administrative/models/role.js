
angular.module('os.administrative.models.role', ['os.common.models'])
  .factory('Role', function(osModel, $q) {
    var Role = osModel('roles');
    
    Role.list = function() { 
      var result = $q.defer();
      var data = [ {"id":1, "name":"Super Admin"},
                   {"id":2,"name":"Admin"}
                 ];
                    
      result.resolve(data);
      return result.promise;
    };
   
    return Role;
  });

