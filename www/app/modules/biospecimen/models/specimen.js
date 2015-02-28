angular.module('os.biospecimen.models.specimen', ['os.common.models', 'os.biospecimen.models.form'])
  .factory('Specimen', function(osModel, $http, SpecimenRequirement, Form) {
    var Specimen = osModel(
      'specimens',
      function(specimen) {
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

    Specimen.flatten = function(specimens, parent, depth) {
      var result = [];
      if (!specimens) {
        return result;
      }

      depth = depth || 0;
      for (var i = 0; i < specimens.length; ++i) {
        result.push(specimens[i]);
        specimens[i].depth = depth || 0;
        specimens[i].parent = parent;
        specimens[i].hasChildren = (!!specimens[i].children && specimens[i].children.length > 0);
        if (specimens[i].hasChildren) {
          result = result.concat(Specimen.flatten(specimens[i].children, specimens[i], depth +1));
        }
      }

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

    Specimen.prototype.getForms = function() {
      return Form.listFor(Specimen.url(), this.$id());
    };

    Specimen.prototype.getRecords = function(formCtxId) {
      return Form.listRecords(Specimen.url(), this.$id(), formCtxId);
    };

    Specimen.prototype.$saveProps = function() {
      delete this.concentration;
      delete this.children;
      return this;
    };

    function toSpecimenAttrs(sr) {
      sr.reqId = sr.id;
      sr.reqLabel = sr.name;
      sr.pathology = sr.pathologyStatus;
          
      var attrs = ['id', 'name', 'pathologyStatus', 
                   'collector', 'collectionProcedure', 'collectionContainer', 
                   'receiver', 'labelFmt'];
      attrs.forEach(function(attr) {
        delete sr[attr];
      });

      if (sr.children) {
        sr.children = sr.children.map(
          function(childSr) {
            return toSpecimenAttrs(childSr);
          }
        );
      }

      return sr;
    };

    return Specimen;
  });
