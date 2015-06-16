
angular.module('os.administrative.role.addedit', ['os.administrative.models'])
  .controller('RoleAddEditCtrl', function(
    $scope, $state, $translate, role,
    Operation, Resource) {
    
    var init = function() {
      $scope.role = role;
      loadPvs();
    }

    function loadPvs() {
       $scope.resources = [];
       Resource.query().then(function(resources) {
         $scope.resources = resources.map(function(resource) {
           return {
             displayName: $translate.instant('role.resources.' + resource.name),
             name: resource.name
           };
         });
       });

       var operationsOrder =  Operation.getOrderedOperations();
       $scope.sortedOperations = [];
       Operation.query().then(
         function(operations) {
           angular.forEach(operations, function(operation) {
             $scope.sortedOperations[operationsOrder.indexOf(operation.name)] = {name: operation.name, selected: false, disabled: false};
           });

           getSelectedOperations($scope.role);

           if (!role.acl) {
             $scope.addResource();
           }
         }
       );
    }

    function getSelectedOperations(role) {
      angular.forEach(role.acl, function(acl) {

        var selectedOperations = acl.operations.map(function(op) {
          return op.operationName;
        });

        var operations = $scope.sortedOperations.map(
          function(operation) {
            var selected = selectedOperations.indexOf(operation.name) != -1;
            var disabled = false;
            if (operation.name == 'Read' || operation.name == 'Update') {
              disabled = selectedOperations.indexOf('Create') != -1;
            }

            return {name: operation.name, selected: selected, disabled: disabled};
          }
        );

        acl.operations = operations;

        if (acl.resourceName == 'SurgicalPathologyReport') {
          // For Lock and Unlock
          var extraOps = ['Lock', 'Unlock'];
          $scope.sprExtraOps = [];
          angular.forEach(extraOps, function(op) {
            var selected = selectedOperations.indexOf(op) != -1;
            $scope.sprExtraOps.push({name: op, selected: selected, disabled: false});
          });
        }

      });
    }

    $scope.addResource = function() {
      $scope.role.addResource($scope.role.newResource(angular.copy($scope.sortedOperations)));
    };

    $scope.removeResource = function(index) {
      $scope.role.removeResource(index);
      if ($scope.role.acl.length == 0) {
        $scope.addResource();
      }
    };
  
    $scope.save = function() {
      addSprExtraOps();
      var role = angular.copy($scope.role);
      role.$saveOrUpdate().then(
        function(savedRole) {
          $state.go('role-detail.overview', {roleId: savedRole.id});
        }
      );
    };

    $scope.addSprExtraOps = function(resource) {
      if (resource == 'SurgicalPathologyReport') {
        var sprExtraOps = ['Lock', 'Unlock'];
        $scope.sprExtraOps = [];
        angular.forEach(sprExtraOps, function(op) {
          $scope.sprExtraOps.push({name: op, selected: false, disabled: false});
        });
      }
    }

    function addSprExtraOps() {
      angular.forEach($scope.role.acl, function(acl) {
        if (acl.resourceName == 'SurgicalPathologyReport') {
          angular.forEach($scope.sprExtraOps, function(operation) {
            acl.operations.push(operation);
          });
        }
      });
    }

    $scope.sprExist = function() {
      var sprExist = false;
      angular.forEach($scope.role.acl, function(acl) {
        if (acl.resourceName == 'SurgicalPathologyReport') {
          sprExist = true;
        }
      });

      return sprExist;
    }

    $scope.setOperations = function(operation, operations) {
      if (operation.name != 'Create') {
        return;
      }

      angular.forEach(operations, function(op) {
        if (op.name == 'Read' || op.name == 'Update') {
          op.selected = operation.selected;
          op.disabled = operation.selected;
        }
      });
    }

    init();
  });
