
angular.module('os.administrative.models.orderrequirement', ['os.common.models'])
  .factory('OrderRequirement', function(osModel, $q){
    var OrderRequirement = osModel('distribution-order-requirements');
    
    OrderRequirement.prototype.getType = function() {
      return 'distribution_order_requirements';
    }
    
    var dpReq = [{"id": "1", "type": "Body Cavity Fluid", "anatomicSite": "Jejunum", "pathology": "Metastatic", 
                  "specimenReq": 20, "specimenDistributed": 0, "price": 34.78, "comments": ""},
                 {"id": "2", "type": "cDNA", "anatomicSite": "Cornea, NOS", "pathology": "Malignant, Invasive", 
                  "specimenReq": 56, "specimenDistributed": 0, "price": 190.00, "comments": "Sample Test"},
                 {"id": "3", "type": "Feces", "anatomicSite": "Ileum", "pathology": "Not Specified", 
                  "specimenReq": 150, "specimenDistributed": 0, "price": 50.55, "comments": ""},
                 {"id": "4", "type": "RNA", "anatomicSite": "Lower gum", "pathology": "Pre-Malignant", 
                  "specimenReq": 6, "specimenDistributed": 0, "price": 855.00, "comments": 
                  "Large comments will be shown as an ellipsis and tooltip will open on hover."},
                 {"id": "5", "type": "Sweat", "anatomicSite": "Ovary", "pathology": "Non-Malignant", 
                  "specimenReq": 100, "specimenDistributed": 0, "price": 74.23, "comments": ""},
                 {"id": "6", "type": "Whole Blood", "anatomicSite": "Pelvis, NOS", "pathology": "Metastatic", 
                  "specimenReq": 75, "specimenDistributed": 0, "price": 350.00, "comments": ""}
                ];

    function getRequirements() {
      var def = $q.defer();
      def.resolve({"data": dpReq});
      return def.promise;
    }
    
    function updateReq(newReq) {
      var def = $q.defer();
      var existing;
      dpReq.forEach(function(ele) {
        if (ele.id == newReq.id) {
          existing = ele;
        }
      });
      var index = dpReq.indexOf(existing);
      dpReq[index].type = newReq.type;
      dpReq[index].anatomicSite = newReq.anatomicSite;
      dpReq[index].pathology = newReq.pathology;
      dpReq[index].specimenReq = newReq.specimenReq;
      dpReq[index].price = newReq.price;
      dpReq[index].comments = newReq.comments;
      def.resolve({"data": dpReq[index]});
      return def.promise;
    }
    
    function addReq(req) {
      req.id = dpReq.length + 1;
      var def = $q.defer();
      dpReq.push(req);
      def.resolve({"data": dpReq[dpReq.indexOf(req)]});
      return def.promise;
    }
    
    function getReqById(reqId) {
      var def = $q.defer();
      var req;
      dpReq.forEach(function(item) {
        if(item.id == reqId) {
          req = item;
        }
      });
      def.resolve({"data": req});
      return def.promise;
    }
    
    OrderRequirement.query = function(dpId) {
      return getRequirements().then(
        function(resp) {
          return resp.data;
        }
      );
    }
    
    OrderRequirement.getById = function(reqId) {
      return getReqById(reqId).then(function(resp) {
        return resp.data;
      });
    }
    
    OrderRequirement.saveUpdate = function(req) {
      var resp;
      if(req.id) {
        resp = updateReq(req);
      } else {
        resp = addReq(req);
      }
      return resp.then(function(data){
        return data;
      });
    }
    
    return OrderRequirement;
  });
