angular.module('os.biospecimen.models.specimen', ['os.common.models', 'os.biospecimen.models.form'])
  .factory('Specimen', function(osModel, $http, SpecimenRequirement, Form, Util) {
    var Specimen = osModel(
      'specimens',
      function(specimen) {
        if (specimen.specimensPool) {
          specimen.specimensPool = specimen.specimensPool.map(
            function(poolSpmn) {
              return new Specimen(poolSpmn);
            }
          );
        }

        if (specimen.children) {
          specimen.children = specimen.children.map(
            function(child) {
              return new Specimen(child);
            }
          );
        }
      }
    );

    Specimen.listFor = function(cprId, visitDetail) {
      return Specimen.query(angular.extend({cprId: cprId}, visitDetail || {}));
    };

    Specimen.listByLabels = function(labels) {
      return Specimen.query({label: labels});
    };
    
    Specimen.flatten = function(specimens, parent, depth, pooledSpecimen) {
      var result = [];
      if (!specimens) {
        return result;
      }

      depth = depth || 0;
      angular.forEach(specimens, function(specimen) {
        result.push(specimen);
        specimen.depth = depth || 0;

        specimen.parent = specimen.parent || parent;
        specimen.pooledSpecimen = specimen.pooledSpecimen || pooledSpecimen;

        var hasSpecimensPool = false;
        if (depth == 0) {
          hasSpecimensPool = !!specimen.specimensPool && specimen.specimensPool.length > 0;
          if (hasSpecimensPool) {
            result = result.concat(Specimen.flatten(specimen.specimensPool, specimen, depth + 1, specimen));
          }
        }

        var hasChildren = (!!specimen.children && specimen.children.length > 0);
        specimen.hasChildren = hasSpecimensPool || hasChildren;
        if (hasChildren) {
          result = result.concat(Specimen.flatten(specimen.children, specimen, depth + 1));
        }

        if (specimen instanceof SpecimenRequirement) {
          return;
        }

        specimen.hasOnlyPendingChildren = hasOnlyPendingChildren(specimen.children);
        if (specimen.hasOnlyPendingChildren) {
          specimen.hasOnlyPendingChildren = hasOnlyPendingChildren(specimen.specimensPool);
        }
      });

      return result;
    };

    Specimen.save = function(specimens) {
      return $http.post(Specimen.url() + 'collect', specimens).then(
        function(result) {
          return result.data;
        }
      );
    };

    Specimen.isUniqueLabel = function(label) {
      return $http.head(Specimen.url(), {params: {label: label}}).then(
        function(result) {
          return false;
        },

        function(result) {
          return true;
        }
      );
    };

    Specimen.getAnticipatedSpecimen = function(srId) {
      return SpecimenRequirement.getById(srId).then(
        function(sr) {
          return new Specimen(toSpecimenAttrs(sr));
        }
      );
    };

    Specimen.getRouteIds = function(specimenId) {
      return $http.get(Specimen.url() + specimenId + '/cpr-visit-ids').then(
        function(resp) {
          return resp.data;
        }
      );
    }

    Specimen.bulkDelete = function(specimenIds) {
      return $http.delete(Specimen.url(), {params: {id: specimenIds}}).then(
        function(result) {
          return result.data;
        }
      );
    }

    Specimen.bulkStatusUpdate = function(statusSpecs) {
      return $http.put(Specimen.url() + '/status', statusSpecs).then(
        function(result) {
          return result.data;
        }
      );
    }

    Specimen.getByIds = function(ids) {
      return Specimen.query({id: ids});
    }

    Specimen.prototype.getType = function() {
      return 'specimen';
    }

    Specimen.prototype.getDisplayName = function() {
      return this.label;
    };

    Specimen.prototype.hasSufficientQty = function() {
      var qty = this.initialQty;
      angular.forEach(this.children, function(child) {
        if (child.lineage == 'Aliquot') {
          qty -= child.initialQty;
        }
      });

      return qty >= 0;
    }

    Specimen.prototype.rootSpecimen = function() {
      var curr = this;
      while (curr.parent) {
        curr = curr.parent;
      }

      return curr;
    };

    Specimen.prototype.getExtensionCtxt = function(params) {
      return Specimen.getExtensionCtxt(params);
    }

    Specimen.prototype.getForms = function(params) {
      return Form.listFor(Specimen.url(), this.$id(), params);
    };

    Specimen.prototype.getRecords = function() {
      var url = Specimen.url() + this.$id() + '/extension-records';
      return Form.listRecords(url);
    };

    Specimen.prototype.getEvents = function(callback) {
      var events = [];

      if (!!this.$id()) {
        $http.get(Specimen.url() + '/' + this.$id() + '/events').then(
          function(result) {
            Util.unshiftAll(events, getEventsList(result.data));
            if (typeof callback == 'function') {
              callback(events);
            }
          }
        );
      }

      return events;
    };

    Specimen.prototype.$saveProps = function() {
      var props = ['children', 'depth', 'hasChildren', 'isOpened', 'specimensPool'];
      var that = this;
      props.forEach(function(prop) { delete that[prop]; });
      return this;
    };

    Specimen.prototype.close = function(reason) {
      var statusSpec = {status: 'Closed', reason: reason};
      return updateSpecimenStatus(this, statusSpec);
    }

    Specimen.prototype.reopen = function() {
      var statusSpec = {status: 'Active'};
      return updateSpecimenStatus(this, statusSpec);
    };

    function toSpecimenAttrs(sr) {
      sr.reqId = sr.id;
      sr.reqLabel = sr.name;
      sr.poolSpecimen = !!sr.pooledSpecimenReqId;

      var attrs = [
        'id', 'name', 'pooledSpecimenReqId',
        'collector', 'collectionProcedure', 'collectionContainer',
        'receiver', 'labelPrintCopies'
      ];

      attrs.forEach(function(attr) {
        delete sr[attr];
      });

      if (sr.children) {
        sr.children = sr.children.map(toSpecimenAttrs);
      }

      if (sr.specimensPool) {
        sr.specimensPool = sr.specimensPool.map(toSpecimenAttrs);
      }

      return sr;
    };

    function getEventsList(eventsRecs) { /* eventsRecs: [{event info, records: [event records]}] */
      var eventsList = [];
      angular.forEach(eventsRecs, function(eventRecs) {
        angular.forEach(eventRecs.records, function(record) {
          var user = null, time = null;
          for (var i = 0; i < record.fieldValues.length; ++i) {
            if (record.fieldValues[i].name == 'user') {
              user = record.fieldValues[i].value;
            } else if (record.fieldValues[i].name == 'time') {
              time = record.fieldValues[i].value;
            }
          }

          eventsList.push({
            id: record.recordId,
            formId: eventRecs.id,
            formCtxtId: record.fcId,
            name: eventRecs.caption,
            updatedBy: record.user,
            updateTime: record.updateTime,
            user: user,
            time: time
          });
        });
      });

      eventsList.sort(
        function(e1, e2) { 
          var t1 = e1.time || 0;
          var t2 = e2.time || 0;
          return t2 - t1;
        }
      );
      return eventsList;
    }

    function updateSpecimenStatus(specimen, statusSpec) {
      return $http.put(Specimen.url() + '/' + specimen.$id() + '/status', statusSpec).then(
        function(result) {
          angular.extend(specimen, result.data);
          return new Specimen(result);
        }
      );
    }

    function hasOnlyPendingChildren(specimens) {
      if (!specimens || specimens.length == 0) {
        return true;
      }

      return specimens.every(
        function(spmn) {
          return !spmn.status || spmn.status == 'Pending';
        }
      );
    }

    return Specimen;
  });
