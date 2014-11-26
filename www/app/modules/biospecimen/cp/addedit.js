
angular.module('openspecimen')
  .controller('CollectionProtocolAddEditCtrl', function(
    $scope, $modalInstance,
    AlertService, CollectionProtocolService, UserService,
    SiteService, PvManager) {

    $scope.collectionProtocol = {
      pi: '',
      coordinator: [],
      statements: []
    };

    $scope.currentView = 'add-edit-event';

    $scope.collectionProtocol.events = [{val: 'Collection Protocol', children:[]}];
    $scope.event = {};

    $scope.treeOpts = {
      treeData: $scope.collectionProtocol.events,
      nodeTmpl: 'node-tmpl.html',

      nodeChecked: function(node) {
        console.log(node.type);
        if (node.type == 'event') {
          $scope.event = node;
          $scope.currentView = 'add-edit-event';
        }
      }
    };



    $scope.addEvents = function() {
      $scope.event = {};
      $scope.currentView = 'add-edit-event';
    }

    $scope.addSpecimenRequirements = function() {
      $scope.currentView = 'specimen-requirement';
    }

    $scope.saveOrUpdateEvents = function() {
      $scope.event.type='event';
      $scope.event.val=$scope.event.pointLabel;

      $scope.collectionProtocol.events[0].children.push($scope.event);
      $scope.currentView = 'home';
    };

    $scope.saveSpecimenRequirements = function() {
      $scope.event
    };

    UserService.getUsers().then(
      function(result) {
        if (result.status != 'ok') {
          AlertService.display($scope, 'Failed to load users information', 'danger');
        }
        $scope.users = result.data.users;
      }
    );

    SiteService.getSites().then(
      function(result) {
        if (result.status != "ok") {
          alert("Failed to load sites information");
        }
        $scope.sites = result.data;
      }
    );

    PvManager.loadPvs($scope, 'clinicalDiagnosis');
    PvManager.loadPvs($scope, 'clinicalStatus');

    $scope.addStatement = function() {
      $scope.collectionProtocol.statements.push({text:''});
    }

    $scope.removeStatement = function(index) {
      $scope.collectionProtocol.statements.splice(index,1);
    }

    var handelCPResult = function(result) {
      if(result.status == 'ok') {
        $modalInstance.close('ok');
      } else {
        AlertService.display($scope, 'Create Collection Protocol Failed', 'danger');
      }
    }

    $scope.save = function() {
      CollectionProtocolService.createCollectionProtocol($scope.collectionProtocol).then(handelCPResult);
    }

    $scope.cancel = function() {
      $modalInstance.dismiss('cancel');
    };

  });
