angular.module('os.biospecimen.models.sr', ['os.common.models'])
  .factory('SpecimenRequirement', function(osModel, $http) {
    var defaultValue = "Not Specified";
    var Sr = osModel(
      'specimen-requirements',
      function(sr) {
        if (!sr.children) {
          return;
        }

        sr.children = sr.children.map(function(child) {
            return new Sr(child);
        });
      }, 
      {
        anatomicSite: defaultValue,
        laterality: defaultValue,
        pathologyStatus: defaultValue,
        collectionContainer: defaultValue,
        collectionProcedure: defaultValue
      }
    );
 
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
      var reqQty = aliquotReq.noOfAliquots * aliquotReq.qtyPerAliquot;
      return reqQty <= this.availableQty();
    };

    return Sr;
  });
