angular.module('os.biospecimen.cp')
  .controller('CpContainerSettingsCtrl', function($scope, $translate, cp, Container) {
    function init() {
      $scope.contSettingsCtx = {}
      setViewCtx();
    }

    function setViewCtx() {
      angular.extend($scope.contSettingsCtx, {view: 'view_settings', cp: cp});
      setStrategy();
    }

    function setStrategy() {
      $translate('cp.container.title').then(
        function() {
          if (cp.containerSelectionStrategy) {
            var alloc = cp.containerSelectionStrategy.replace(/-/g, '_');
            $scope.contSettingsCtx.strategy = $translate.instant('container.alloc_strategies.' + alloc);
          } else {       
            $scope.contSettingsCtx.strategy = $translate.instant('common.not_specified');
          }
        }
      );
    }

    function setEditCtx() {
      var ctx = {view: 'edit_settings', cp: angular.copy(cp)};
      angular.extend($scope.contSettingsCtx, ctx);
      loadStrategies();
    }

    function loadStrategies() {
      if ($scope.contSettingsCtx.allocStrategies) {
        return;
      }

      Container.getAutoAllocStrategies().then(
        function(strategies) {
          $scope.contSettingsCtx.allocStrategies = strategies;
        }
      );
    }

    $scope.showEditForm = function() {
      setEditCtx();
    }

    $scope.revertEdit = function() {
      setViewCtx();
    }

    $scope.clearStoreAliquots = function() {
      if (!$scope.contSettingsCtx.cp.containerSelectionStrategy) {
        $scope.contSettingsCtx.cp.aliquotsInSameContainer = false;
      }
    }

    $scope.save = function() {
      delete $scope.contSettingsCtx.cp.repositoryNames;
      delete $scope.contSettingsCtx.cp.extensionDetail;
      delete $scope.contSettingsCtx.cp.catalogSetting;

      $scope.contSettingsCtx.cp.$saveOrUpdate().then(
        function(savedCp) {
          angular.extend(cp, angular.extend($scope.cp, savedCp));
          setViewCtx();
        }
      );
    }

    init();
  });
