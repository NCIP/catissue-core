
angular.module('os.administrative.container.overview', ['os.administrative.models'])
  .controller('ContainerOverviewCtrl', function($scope, $state, rootId, container, Container, DeleteUtil) {
    function init() {
      $scope.ctx.showTree  = true;
      $scope.ctx.viewState = 'container-detail.overview';
      $scope.ctx.downloadUri = Container.url() + container.$id() + '/export-map';
    }

    function depth(container) {
      var nodes = 1;
      angular.forEach(container.childContainers,
        function(childContainer) {
          nodes += depth(childContainer);
        }
      );

      return nodes;
    }

    $scope.deleteContainer = function() {
      var toDelete = new Container({id: container.id, name: container.name});
      DeleteUtil.delete(toDelete, {
        onDeletion: function() {
          if (container.id == rootId) {
            //
            // current top-level container got deleted, navigate users to containers list
            //
            $state.go('container-list');
          } else {
            //
            // remove container and all of its loaded descendants from the hierarchy tree
            //
            var tree = $scope.ctx.containerTree;
            var idx = tree.indexOf(container);
            tree.splice(idx, depth(container));

            var parent = container.parent;
            idx = parent.childContainers.indexOf(container);
            parent.childContainers.splice(idx, 1);
            parent.hasChildren = (parent.childContainers.length > 0);
            $scope.selectContainer(parent);
          }
        },
        confirmDelete: 'container.confirm_delete'
      });
    }

    init();
  });
