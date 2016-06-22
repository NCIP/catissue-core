angular.module('os.administrative.container.detail', ['os.administrative.models'])
  .controller('ContainerDetailCtrl', function(
      $scope, $q, container, Container,
      Site, CollectionProtocol, DeleteUtil) {

    function init() {
      $scope.container = container;
      $scope.downloadUri = Container.url() + container.$id() + '/export-map';
      loadPvs();

      var opts = {sites: [$scope.container.siteName]};
      angular.extend($scope.containerResource.updateOpts, opts);
      angular.extend($scope.containerResource.deleteOpts, opts);
    }

    function loadPvs () {
      CollectionProtocol.query().then(
        function(cps) {
          $scope.cps = [];
          angular.forEach(cps, function(cp) {
            $scope.cps.push(cp.shortTitle);
          });
        }
      );
    }

    $scope.editContainer = function(property, value) {
      var d = $q.defer();
      d.resolve({});
      return d.promise;
    }

    $scope.deleteContainer = function() {
      var container = new Container({
        id: $scope.container.id,
        name: $scope.container.name
      });

      DeleteUtil.delete(container, {
        onDeleteState: 'container-list',
        confirmDelete: 'container.confirm_delete'
      });
    }

    init();
  });
