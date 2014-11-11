
angular.module('openspecimen')
  .controller('CollectionProtocolAddEditCtrl', function($scope, UserService) {

    $scope.collectionProtocol = {
      pi: '',
      coordinator: []
    };

    UserService.getUsers().then(
      function(result) {
        if (result.status != "ok") {
          alert("Failed to load users information");
        }
        $scope.users = result.data.users;
      }
    );

    $scope.save = function() {
      alert(JSON.stringify($scope.collectionProtocol))
    }

  });
