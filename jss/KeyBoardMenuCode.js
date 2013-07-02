		var strLInk=window.location.href;
		var stringIndexValue=strLInk.lastIndexOf("/");
		var strLinkWithoutSlash=strLInk.substring(0,stringIndexValue);
		
		shortcut.add("alt+shift+c", function() {
			var strLinkForOrder=strLinkWithoutSlash.concat("/OpenStorageContainer.do?operation=showMap");
				window.top.location.href =strLinkForOrder;
           });
		   
		shortcut.add("alt+o", function() {
			var strLinkForOrder=strLinkWithoutSlash.concat("/RequestListView.do");
				window.top.location.href =strLinkForOrder;
           });
			
		 shortcut.add("alt+q", function() {
				var strLinkForQueries=strLinkWithoutSlash.concat("/ShowQueryDashboardAction.do");
					window.top.location.href =strLinkForQueries;
				});
				
		shortcut.add("alt+c", function() {
				var strLinkForDataEntry=strLinkWithoutSlash.concat("/CpBasedSearch.do");
					window.top.location.href = strLinkForDataEntry;
				});
		
		shortcut.add("alt+b", function() {
			var strLinkForBulkUpload=strLinkWithoutSlash.concat("/BulkOperation.do?pageOf=pageOfBulkOperation");
				window.top.location.href =strLinkForBulkUpload; 
           });
	