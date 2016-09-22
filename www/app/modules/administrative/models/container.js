
angular.module('os.administrative.models.container', ['os.common.models'])
  .factory('Container', function(osModel, $q, $http, $translate) {
    var Container = new osModel(
      'storage-containers',
      function(container) {
        if (container.childContainers) {
          container.childContainers = container.childContainers.map(
            function(child) {
              return new Container(child);
            }
          );
        }
      }
    );

    Container.prototype.getType = function() {
      return 'storage_container';
    }

    Container.prototype.getDisplayName = function() {
      return this.name;
    }

    Container.prototype.getChildContainers = function() {
      return $http.get(Container.url() + this.$id() + '/child-containers').then(Container.modelArrayRespTransform);
    }

    Container.prototype.getOccupiedPositions = function() {
      return $http.get(Container.url() + '/' + this.$id() + '/occupied-positions').then(
        function(result) {
          return result.data;
        }
      );
    };

    Container.prototype.isSpecimenAllowed = function(cpId, specimenClass, specimenType) {
      var params = {cpId: cpId, specimenClass: specimenClass, specimenType: specimenType};
      return $http.head(Container.url() + '/' + this.$id(), {params: params}).then(
        function(result) {
          return true;
        },

        function(err) {
          return false;
        }
      );
    };

    Container.prototype.assignPositions = function(positions) {
      return $http.post(Container.url() + this.$id() + '/occupied-positions', positions).then(
        function(result) {
          return result.data;
        }
      )
    }

    Container.prototype.lazyLoadFlattenedChildren = function(containers, placeholder) {
      if (this.childContainersLoaded) {
        return containers;
      }

      if (!placeholder) {
        placeholder = {name: 'Loading...'};
        $translate('common.loading').then(function(val) { placeholder.name = val; });
      }

      var container = this;
      return container.getChildContainers().then(
        function(childContainers) {
          angular.forEach(childContainers, function(childContainer, idx) {
            var dummyId = -1 * container.id + "." + idx;
            childContainer.childContainers = [angular.extend({id: dummyId}, placeholder)];
            childContainer.childContainersLoaded = false;
          });
          container.childContainers = childContainers;
          container.childContainersLoaded = true;

          var flattened = Container._flatten(childContainers, 'childContainers', container, container.depth + 1);
          var idx = containers.indexOf(container);
          var args = [idx + 1, 1].concat(flattened)
          Array.prototype.splice.apply(containers, args);
          if (childContainers.length == 0) {
            container.hasChildren = false;
          }

          return containers;
        }
      );
    };

    Container.prototype.replicate = function(destinations) {
      return $http.post(Container.url() + this.$id() + '/replica', {destinations: destinations}).then(
        function(resp) {
          return resp.data;
        }
      );
    };

    Container.prototype.getVacantPositions = function(startRow, startCol, startPos, numPos) {
      var params = {startRow: startRow, startColumn: startCol, startPosition: startPos, numPositions: numPos};
      return $http.get(Container.url() + this.$id() + '/vacant-positions', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    };

    Container.list = function(opts) {
      var defOpts = {topLevelContainers: true};
      return Container.query(angular.extend(defOpts, opts || {}));
    };

    Container.listForSite = function(siteName, onlyFreeContainers, flatten, name) {
      var params = {
        site: siteName,
        name: name,
        anyLevelContainers: true,
        onlyFreeContainers: !!onlyFreeContainers
      };

      var ret = Container.query(params);
      if (flatten == true) {
        ret = ret.then(
          function(containers) {
            return Container._flatten(containers, 'childContainers');
          }
        );
      }

      return ret;
    };

    Container.flatten = function(containers) {
      return Container._flatten(containers, 'childContainers');
    };

    Container.getByName = function(name) {
      return $http.get(Container.url() + '/byname/', {params: {name: name}}).then(
        function(result) {
          return new Container(result.data);
        }
      );
    };

    Container.getAncestorsHierarchy = function(containerId) {
      return $http.get(Container.url() + containerId + '/ancestors-hierarchy').then(
        function(result) {
          return new Container(result.data);
        }
      );
    }

    Container.createHierarchy = function(hierarchyDetail) {
      return $http.post(Container.url() + 'create-hierarchy', hierarchyDetail).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    Container.createContainers = function(containers) {
      return $http.post(Container.url() + 'multiple', containers).then(Container.modelArrayRespTransform);
    }

    Container.getVacantPositions = function(name, startRow, startCol, startPos, numPos) {
      var params = {name: name, startRow: startRow, startColumn: startCol, startPosition: startPos, numPositions: numPos};
      return $http.get(Container.url() + 'vacant-positions', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    };

    Container.getReservedPositions = function(reserveOp) {
      return $http.post(Container.url() + 'reserve-positions', reserveOp).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    Container.reservePositionForSpecimen = function(cpId, spmn, reservationToCancel) {
      var op = {
        cpId: cpId,
        reservationToCancel: reservationToCancel,
        tenants: [{
          specimenClass: spmn.specimenClass,
          specimenType:  spmn.type,
          lineage: spmn.lineage
        }]
      };

      return Container.getReservedPositions(op).then(
        function(positions) {
          return (positions && positions.length > 0) ? positions[0] : {};
        }
      );
    }

    Container.cancelReservation = function(reservationId) {
      var params = {reservationId: reservationId};
      return $http.delete(Container.url() + 'reserve-positions', {params: params}).then(
        function(resp) {
          return resp.data;
        }
      );
    }

    Container.getAutoAllocStrategies = function() {
      return $http.get(Container.url() + 'auto-allocation-strategies').then(
        function(resp) {
          return resp.data;
        }
      );
    }

    return Container;
  });
