
angular.module('os.administrative.models.role', ['os.common.models'])
  .factory('Role', function(osModel, $q) {
    var Role = osModel('roles');
    
    Role.list = function() { 
      var result = $q.defer();
      var data = [ {"id":1, "name":"Super Admin", "description": "Super Administrator"},
                   {"id":2,"name":"Admin", "description": "Administrator"}
                 ];
                    
      result.resolve(data);
      return result.promise;
    };

    Role.getById = function(id) {
      return new Role(
        {
          "id":1,
          "name": "Super Admin",
          "description": "Super Administrator",
          "acls" : [
            {"resource": "User", "permissions": ["Create", "Read", "Update", "Delete"]},
            {"resource": "Site", "permissions": ["Read"]}
          ]
        }
      );
    };

    Role.prototype.newResource = function() {
      return {resource:'', permissions: []};
    }

    Role.prototype.addResource = function(resource) {
      if (!this.acls) {
        this.acls = [];
      }

      this.acls.push(resource);
    }

    Role.prototype.removeResource = function(index) {
      this.acls.splice(index, 1);
    }

    Role.prototype.getAcls = function() {
      var acls = [];
      angular.forEach(this.acls, function(acl) {
        if (acl.resource && acl.permissions) {
          acls.push(acl);
        }
      });
      return acls;
    };

    Role.prototype.$saveProps = function() {
      this.acls = this.getAcls();
      return this;
    };
   
    return Role;
  });

