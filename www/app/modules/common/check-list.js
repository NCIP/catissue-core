angular.module('openspecimen')
  .factory('CheckList', function() {

    var CheckList = function(list) {
      this.list        = list || [];
      this.allSelected = false;
      this.anySelected = false;
      this.items       = this.list.map(function() { return {selected: false}; });
    }
     
    CheckList.prototype.toggleSelectAll = function() {
      var selected = this.anySelected = this.allSelected;
      angular.forEach(this.items,
        function(item) {
          item.selected = selected;
        }
      );
    }

    CheckList.prototype.toggleSelectItem = function(idx) {
      this.anySelected = false;
      this.allSelected = true;

      for (var i = 0; i < this.items.length; ++i) {
        if (this.items[i].selected) {
          this.anySelected = true;
        } else {
          this.allSelected = false;
        }

        if (this.anySelected && !this.allSelected) {
          break;
        }
      }
    }

    CheckList.prototype.getSelectedItems = function() {
      if (!this.anySelected) {
        return [];
      }

      var list = this.list;
      var result = [];
      angular.forEach(this.items,
        function(item, idx) {
          if (item.selected) {
            result.push(list[idx]);
          }
        }
      );

      return result;
    }

    return CheckList;
  });
