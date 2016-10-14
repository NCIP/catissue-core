angular.module('os.administrative.container.detail', ['os.administrative.models'])
  .controller('ContainerDetailCtrl', function(
    $state, $scope, $q, containerViewState, container, containerTree, Container) {

    function init() {
      if (!container) {
        $state.reload();
        return;
      }

      $scope.ctx = {
        viewState      : '',
        container      : container,
        containerTree  : containerTree,
        names          : [container.name],
        showTree       : true
      };

      if (!container.storageLocation || !container.storageLocation.name) {
        if (!container.$$metrics) {
          container.$$metrics = computeMetrics(container);
        }

        $scope.ctx.charts = {
          spmnType: container.$$metrics.spmnTypeChartOpts
        }
      } else {
        $scope.ctx.charts = undefined;
      }

      selectContainer(container);

      var opts = {sites: [container.siteName]};
      angular.extend($scope.containerResource.updateOpts, opts);
      angular.extend($scope.containerResource.deleteOpts, opts);
    }

    function computeMetrics(container) {
      var types = Object.keys(container.specimensByType).sort(
        function(t1, t2) {
          return container.specimensByType[t1] - container.specimensByType[t2];
        }
      );

      var data = {categories: [], values: []}, total = 0;
      angular.forEach(types,
        function(type) {
          if (data.categories.length < 5) {
            data.categories.push(type);
            data.values.push(container.specimensByType[type]);
          }

          total += container.specimensByType[type];
        }
      );

      container.storedSpecimens = total;
      if (container.capacity) {
        container.utilisation = Math.round(total / container.capacity * 100);
      }

      return {spmnTypeChartOpts: {data: data, options: {type: 'doughnut'}}};
    }

    function selectContainer(container) {
      var names = [], c = container;
      while (c) {
        names.unshift(c.name);
        c = c.parent;
      }
      $scope.ctx.names = names;

      c = container.parent;
      while (c) {
        c.isOpened = true;
        c = c.parent;
      }

      if (!container.parent && !container.isOpened) {
        loadChildren(container);
      }

      containerViewState.scrollTo(container.id);
    }

    function loadChildren(container) {
      container.isOpened = !container.isOpened;
      if (!container.isOpened) {
        return;
      }

      container.lazyLoadFlattenedChildren($scope.ctx.containerTree);
    }

    $scope.selectContainer = function(container) {
      containerViewState.recordScrollPosition();
      $state.go($scope.ctx.viewState || 'container-detail.overview', {containerId: container.id});
    }

    $scope.loadChildren = loadChildren;

    init();
  });
