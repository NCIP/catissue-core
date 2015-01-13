
angular.module('os.administrative.models.role', ['os.common.models'])
  .factory('Role', function(osModel, $q) {
    var Role = osModel('roles');
    
    Role.list = function() { 
      var result = $q.defer();
      var data =   [{"id":1, "name":"Super Admin", "description":"Super Admin user have all access for user and site",
                      "acl" : [{"resourceName":"user", "privileges":["insert","update","delete","create"]},
                               {"resourceName":"site", "privileges":["insert","update","delete","create"]}]
                     },
                     {"id":2,"name":"Admin","description":"Admin user have insert,update,delete access for user and site object",
                      "acl" : [{"resourceName":"user", "privileges":["insert","update","delete"] },
                               {"resourceName":"site", "privileges":["insert","update"] }]
                    }];
                    
      result.resolve(data);
      return result.promise;
    };
    
    Role.getById = function(id) {  
      return {"id":1, "name":"Super Admin", "description":"Super Admin user have all access for user and site",
              "acl" : [{"resourceName":"user", "privileges":["insert","update","delete","create"]},
                       {"resourceName":"site", "privileges":["insert","update","delete","create"]}]
             };
    };
    
    Role.prototype.newResource = function() {
      return {resourceName:'', privileges: ''};
    }
                
    Role.prototype.addResource = function (resource) {
      if(!this.acl) {
        this.acl = [];
      }
      
      this.acl.push(resource);
    }
    
    Role.prototype.removeResource = function (index) {
      this.acl.splice(index, 1);
    }
    
    return Role;
  });

