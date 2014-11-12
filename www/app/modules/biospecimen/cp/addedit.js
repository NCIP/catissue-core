
angular.module('openspecimen')
  .controller('CollectionProtocolAddEditCtrl', function($scope, UserService) {

    $scope.collectionProtocol = {
      pi: '',
      coordinator: [],
      statements: []
    };

    UserService.getUsers().then(
      function(result) {
        if (result.status != "ok") {
          alert("Failed to load users information");
        }
        $scope.users = result.data.users;
      }
    );

    $scope.addStatement = function(statements) {
      statements.push({text:''});
    }

    $scope.removeStatement = function(index) {
      $scope.collectionProtocol.statements.splice(index,1);
    }

    $scope.save = function() {
      alert(JSON.stringify($scope.collectionProtocol))
    }

  });
