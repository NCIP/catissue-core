angular.module('os.administrative.container.addedit', ['os.administrative.models'])
  .controller('ContainerAddEditCtrl', function($scope, $state, container, Site, Container, CollectionProtocol, PvManager){

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
      Container.listForSite(siteName).then(function(containerList) {
        $scope.containers = containerList;
      });
    };

    $scope.save = function() {
      var container = angular.copy($scope.container);
      container.createdBy = {id: 1}; // TODO: Handle this in server side.
      container.$saveOrUpdate().then(
        function(result) {
          $state.go('container-detail.overview', {containerId: result.id});
        }
      );
    };

    init();
  });
