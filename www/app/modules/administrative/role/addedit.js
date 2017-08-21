
angular.module('os.administrative.role.addedit', ['os.administrative.models'])
  .controller('RoleAddEditCtrl', function(
    $scope, $state, $translate, role,
    Operation, Resource) {

    function init() {
      $scope.role = role;
      loadPvs();
      sprExists();
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
             var show = !(operation.name == 'Lock' || operation.name == 'Unlock');
             $scope.sortedOperations[operationsOrder.indexOf(operation.name)] = {
               name: operation.name,
               selected: false,
               disabled: false,
               show: show
             };
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

            var show = true;
            if (acl.resourceName != 'SurgicalPathologyReport' &&
                 (operation.name == 'Lock' || operation.name == 'Unlock')) {
              show = false;
            }

            return {name: operation.name, selected: selected, disabled: disabled, show: show};
          }
        );

        acl.operations = operations;
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

      sprExists();
    };
  
    $scope.save = function() {
      var role = angular.copy($scope.role);
      role.$saveOrUpdate().then(
        function(savedRole) {
          $state.go('role-detail.overview', {roleId: savedRole.id});
        }
      );
    };

    $scope.onResourceSelect = function(ac) {
      if (ac.resourceName != 'SurgicalPathologyReport') {
        return;
      }
      $scope.sprExists = true;
      angular.forEach(ac.operations, function(operation) {
        if (operation.name == 'Lock' || operation.name == 'Unlock') {
          operation.show = true;
        }
      });
    }

    function sprExists() {
      $scope.sprExists = false;
      for (var key in $scope.role.acl) {
        if ($scope.role.acl[key].resourceName == 'SurgicalPathologyReport') {
          $scope.sprExists = true;
          break;
        }
      }
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
