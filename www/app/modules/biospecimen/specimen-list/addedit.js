angular.module('os.biospecimen.specimenlist.addedit', ['os.biospecimen.models'])
  .controller('AddEditSpecimenListCtrl', function(
    $scope, $state, list, barcodingEnabled, SpecimenList, SpecimensHolder, DeleteUtil, SpecimenUtil, Util) {
 
    function init() { 
      $scope.list = list;
      $scope.list.specimens = SpecimensHolder.getSpecimens() || list.specimens;
      $scope.list.isAllowedToDeleteList = isAllowedToDeleteList(); 
      $scope.isQueryOrSpecimenPage =  SpecimensHolder.getSpecimens() != undefined;
      SpecimensHolder.setSpecimens(undefined);
      $scope.input = {
        labelText: '',
        barcodingEnabled: barcodingEnabled,
        useBarcode: false
      };
    }

    function isAllowedToDeleteList() {
       return !!$scope.list.id &&
              !$scope.list.defaultList &&
              ($scope.list.owner.id == $scope.currentUser.id || $scope.currentUser.admin)
    }

    function getAddedSpecimens(specimens) {
      if (specimens.length == 0) {
        return [];
      }

      if (!$scope.list.specimens || $scope.list.specimens.length == 0) {
        return specimens;
      }

      var map = $scope.list.specimens.reduce(function(map, spmn) {
        map[spmn.id] = spmn;
        return map;
      }, {});

      var addedSpmns = [];
      angular.forEach(specimens, function(spmn) {
        if (!map[spmn.id]) {
          addedSpmns.push(spmn);
          map[spmn.id] = spmn;
        }
      });

      return addedSpmns;
    }

    function updateSpecimenList(specimenList, labels, status) {
      var filterOpts = {};
      if (!!$scope.input.useBarcode) {
        filterOpts.barcode = labels;
        labels = undefined;
      }

      return SpecimenUtil.getSpecimens(labels, filterOpts).then(
        function(specimens) {
          if (!specimens) {
            return undefined;
          }

          var addedSpmns = getAddedSpecimens(specimens);
          if (addedSpmns.length == 0 && !!specimenList.id) {
            status.patched = true;
            return specimenList.$patch(specimenList);
          }

          specimenList.specimens = $scope.list.specimens || [];
          Util.appendAll(specimenList.specimens, addedSpmns);
          status.patched = false;

          return specimenList.$saveOrUpdate();
        }
      );
    }

    $scope.saveOrUpdateList = function() {
      var labels = Util.splitStr($scope.input.labelText, /,|\t|\n/);

      var sharedWith = $scope.list.sharedWith.map(
        function(user) { 
          return {id: user.id} 
        }
      );

      var specimenList =  new SpecimenList({
        id: $scope.list.id,
        name: $scope.list.name,
        description: $scope.list.description,
        sharedWith: sharedWith
      });

      var status = {patched: false};
      var promise = undefined;
      if (labels.length == 0 && !!specimenList.id) {
        promise = specimenList.$patch(specimenList);
        status.patched = true;
      } else {
        promise = updateSpecimenList(specimenList, labels, status);
      }

      promise.then(
        function(savedList) {
          if (!savedList) {
            return;
          }

          if ($scope.isQueryOrSpecimenPage) {
            $scope.back();
          } else {
            if (!status.patched) {
              $scope.list.specimens = savedList.specimens;
            }

            $state.go('specimen-list', {listId: savedList.id});
          }
        }
      );
    }

    $scope.deleteList = function() {
      DeleteUtil.delete($scope.list, {
        onDeleteState: 'specimen-lists',
        deleteWithoutCheck: true
      });
    }

    init();
  }
);
