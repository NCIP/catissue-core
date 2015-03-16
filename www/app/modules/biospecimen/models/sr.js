angular.module('os.biospecimen.models.sr', ['os.common.models'])
  .factory('SpecimenRequirement', function(osModel, $http, PvManager) {
    var Sr = osModel(
      'specimen-requirements',
      function(sr) {
        sr.copyAttrsIfNotPresent(getDefaultProps())

        if (!sr.children) {
          return;
        }

        sr.children = sr.children.map(function(child) {
            return new Sr(child);
        });

      }
    );

    var getDefaultProps = function() {
      var notSpecified = PvManager.notSpecified();
      return {
        anatomicSite: notSpecified,
        laterality: notSpecified,
        pathologyStatus: notSpecified,
        collectionContainer: notSpecified,
        collectionProcedure: notSpecified
      }
    }
 
    Sr.listFor = function(cpeId) {
      return Sr.query({eventId: cpeId});
    };

    Sr.prototype.isAliquot = function() {
      return this.lineage == 'Aliquot';
    };

    Sr.prototype.isDerivative = function() {
      return this.lineage == 'Derived';
    };

    Sr.prototype.copy = function() {
      return $http.post(Sr.url() + this.$id() + '/copy').then(Sr.modelRespTransform);
    };

    Sr.prototype.createAliquots = function(requirement) {
      var url = Sr.url();
      return $http.post(url + this.$id() + '/aliquots', requirement)
               .then(Sr.modelArrayRespTransform);
    };

    Sr.prototype.createDerived = function(requirement) {
      var url = Sr.url();
      return $http.post(url + this.$id() + '/derived', requirement)
               .then(Sr.modelRespTransform);
    };

    Sr.prototype.availableQty = function() {
      var available = this.initialQty;
      angular.forEach(this.children, function(child) {
        if (child.lineage == 'Aliquot') {
          available -= child.initialQty;
        }
      });

      return available;
    };

    Sr.prototype.hasSufficientQty = function(aliquotReq) {
      if (!angular.isDefined(aliquotReq.qtyPerAliquot)) {
        return true;
      }
      var reqQty = aliquotReq.noOfAliquots * aliquotReq.qtyPerAliquot;
      return reqQty <= this.availableQty();
    };

    return Sr;
  });
