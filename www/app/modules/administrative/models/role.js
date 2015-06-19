
angular.module('os.administrative.models.role', ['os.common.models'])
  .factory('Role', function(osModel) {
    var Role = osModel('rbac/roles');

    Role.prototype.newResource = function(operations) {
      return {resourceName:'', operations: operations};
    }

    Role.prototype.addResource = function(resource) {
      if (!this.acl) {
        this.acl = [];
      }
      this.acl.push(resource);
    }

    Role.prototype.removeResource = function(index) {
      this.acl.splice(index, 1);
    }

    Role.prototype.$saveProps = function() {
      this.acl = getAclForSave(this.acl);
      return this;
    };

    function getAclForSave(inputAcl) {
      var acl = [];
      angular.forEach(inputAcl, function(ac) {
        var operations = [];
        angular.forEach(ac.operations, function(operation) {
          if (operation.selected) {
            operations.push({operationName: operation.name});
          }
        });

        ac.operations = operations;
        acl.push(ac);
      });

      return acl;
    };
   
    return Role;
  })
  .factory('Operation', function(osModel) {
    var Operation = osModel('rbac/operations');

    Operation.getOrderedOperations = function() {
      var operations = ['Read', 'Create', 'Update', 'Delete', 'Lock', 'Unlock'];
      return operations;
    }

    return Operation;
  })
  .factory('Resource', function(osModel) {
    var Resource = osModel('rbac/resources');
    return Resource;
  })



