angular.module('os.administrative.container.addedit', ['os.administrative.models'])
  .controller('ContainerAddEditCtrl', function(
    $scope, $state, $stateParams, container, 
    Site, Container, CollectionProtocol, PvManager){

    function init() {
      $scope.container = container;
      $scope.cps = [];
      loadPvs();

      $scope.specimenTypeSelectorOpts = {
        items: $scope.specimenTypes,
        selectedCategories: container.allowedSpecimenClasses,
        selectedCategoryItems: container.allowedSpecimenTypes,
        categoryAttr: 'class',
        valueAttr: 'type'
      };

      if ($stateParams.parentContainerName && $stateParams.parentContainerId &&
          $stateParams.posOne && $stateParams.posTwo) {
        //
        // This happens when user adds container from container map
        //

        $scope.locationSelected = true;
        container.position = {posOne: $stateParams.posOne, posTwo: $stateParams.posTwo};
        container.parentContainerName = $stateParams.parentContainerName;
      }
    };

    function loadPvs() {
      $scope.positionLabelingSchemes = PvManager.getPvs('container-position-labeling-schemes');
      $scope.sites = PvManager.getSites();

      CollectionProtocol.query().then(
        function(cps) {
          angular.forEach(cps, function(cp) {
            $scope.cps.push(cp.shortTitle);
          });
        }
      );

      // Will go away 
      $scope.specimenTypes = [
        {class: 'Molecular', type: 'RNA, poly-A enriched'},
        {class: 'Molecular', type: 'RNA, nuclear'},
        {class: 'Cell', type: 'Frozen Cell Pellet'},
        {class: 'Cell', type: 'Fixed Cell Block'},
        {class: 'Fluid', type: 'Whole Bone Marrow'},
        {class: 'Fluid', type: 'Saliva'},
        {class: 'Tissue', type: 'Fixed Tissue'},
        {class: 'Tissue', type: 'Frozen Tissue'}
      ];
    }

    $scope.loadContainers = function(siteName) {
      Container.listForSite(siteName, true, true).then(
        function(result) {
          $scope.containers = [];
          angular.forEach(result, function(container) {
            $scope.containers.push(container.name);
          });
        }
      );
    };

    $scope.save = function() {
      var container = angular.copy($scope.container);
      container.createdBy = {id: 1}; // TODO: Handle this in server side.
      container.$saveOrUpdate().then(
        function(result) {
          if (!$scope.locationSelected) {
            $state.go('container-detail.overview', {containerId: result.id});
          } else {
            $state.go('container-detail.locations', {containerId: $stateParams.parentContainerId});
          }
        }
      );
    };

    init();
  });
