function openLinkProtocolWindow(operation, pageOf)
{
	document.getElementById("errorRow").textContent = '';
	var pageOperation = "SearchRemote";
	var entityName = document.getElementById('title').value;
	var inputParams = "&entityName="+entityName;
	var ctrpUserAjaxUrl= 'CTRPStudyProtocolAjax.do?pageOperation='+pageOperation+inputParams;
	var ds = new Ext.data.Store(
			{proxy: new Ext.data.HttpProxy({url: ctrpUserAjaxUrl}),
			reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'},
					[{name: 'title', mapping: 'title'},
					{name: 'shortTitle', mapping: 'shortTitle'},
					{name: 'startDate', mapping: 'startDate'}]
					)
			}
	);
	ds.on('beforeload', function() {
		document.getElementById("storeLoadingDiv").style.display="";
		document.getElementById("findUserDiv").style.display="none";
	});
	ds.on('load', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
		if (ds.getTotalCount()>0) {
			//Remote matches found
		var ctrpUserURL = 'CTRPStudyProtocol.do?operation='+operation+'&pageof='+pageOf+'&'+inputParams;
		userwindow=dhtmlmodal.open('CTRPSelectProtocol', 'iframe', ctrpUserURL,'Select NCI Enterprise Study Protocol', 'width=600px,height=300px,max-height=200px,center=1,resize=0,scrolling=1')
		userwindow.onclose=function()
	    {
			document.getElementById("findUserDiv").style.display="";
			return true;
	    }
		}else{
			//No remote matches found
			 document.getElementById("errorRow").textContent = ctrpSearchNoMatchMessage;
			 document.getElementById("errorRow").className = "messagetexterror";
			 document.getElementById("title").focus();
			document.getElementById("storeLoadingDiv").style.display="none";
			document.getElementById("findUserDiv").style.display="";
		}
	});
	ds.on('loadexception', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
		document.getElementById("findUserDiv").style.display="";
	});
	ds.load();
}

function getRemoteUserDetails(remoteId,operation)
{
	var inputParams = "&remoteId="+remoteId;
	var pageOperation = "GetRemoteById";
	var ctrpUserAjaxUrl= 'CTRPStudyProtocolAjax.do?pageOperation='+pageOperation+inputParams;
	var ds = new Ext.data.Store(
			{proxy: new Ext.data.HttpProxy({url: ctrpUserAjaxUrl}),
			reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'},
					[{name: 'title', mapping: 'title'},
					{name: 'shortTitle', mapping: 'shortTitle'},
					{name: 'startDate', mapping: 'startDate'}]
					)
			}
	);
	ds.on('beforeload', function() {
		document.getElementById("storeLoadingDiv").style.display="";
	});
	ds.on('load', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
		var remoteRecord =  ds.getAt(0);
		//Pre-populate fields from CTRP 
		document.getElementById('title').value = remoteRecord.get('title');
		document.getElementById('shortTitle').value = remoteRecord.get('shortTitle').blank()?'N/A':remoteRecord.get('shortTitle');
		document.getElementById('startDate').value = remoteRecord.get('startDate');
		//Hide Find Enterprise User Link
//		document.getElementById('lookupNCI').style.display = "none";
		if (operation == 'Link') { 
			document.getElementById('remoteId').value = remoteRecord.id;
			document.getElementById('remoteManagedFlag').value = 'true';
			document.getElementById('dirtyEditFlag').value = 'false';
			disableRemoteManagedFields();
//			updateRemoteInstitutions(remoteRecord.id);
		}else {
			document.getElementById("findUserDiv").style.display="";
		}
	});
	ds.on('loadexception', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
	});
	ds.load();
}
function disableRemoteManagedFields() {
	//Disable Remote managed fields
	document.getElementById('title').readOnly = true;
	document.getElementById('shortTitle').readOnly = true;
	document.getElementById('startDate').readOnly = true;
	//Change the CSS class to gray out
	document.getElementById('title').className = "black_ar_disabled";
	document.getElementById('shortTitle').className = "black_ar_disabled";
	document.getElementById('startDate').className = "black_ar_disabled";
}

function enableRemoteManagedFields() {
	//Disable Remote managed fields
	document.getElementById('title').readOnly = false;
	document.getElementById('shortTitle').readOnly = false;
	document.getElementById('startDate').readOnly = false;
	//Change the CSS class to gray out
	document.getElementById('title').className = "black_ar";
	document.getElementById('shortTitle').className = "black_ar";
	document.getElementById('startDate').className = "black_ar";
}

function markupRemoteFields() {
	// Remote Managed 
	if (("true" == document.getElementById("remoteManagedFlag").value) 
			&& (("edit" == document.getElementById("operation").value))
			&& ("false" == document.getElementById("dirtyEditFlag").value)){
		disableRemoteManagedFields();
		document.getElementById("findUserDiv").style.display = "none";
		document.getElementById("editLocalDiv").style.display = "";
		document.getElementById("syncRemoteDiv").style.display = "none";
	}
	//Remote Managed and Dirty Edit
	if (("true" == document.getElementById("remoteManagedFlag").value) 
			&& (("edit" == document.getElementById("operation").value))
			&& ("true" == document.getElementById("dirtyEditFlag").value)){
		document.getElementById("findUserDiv").style.display = "none";
		document.getElementById("editLocalDiv").style.display = "none";
		document.getElementById("syncRemoteDiv").style.display = "";
	}
}
function enableLocalChangesFunc(){
	var prompt = confirm(ctrpDirtyEditConfMessage);
	if (prompt == false){
		return;
	}
	enableRemoteManagedFields();
	document.getElementById("dirtyEditFlag").value = true;
}

function syncRemoteChangesFunc() {
	var prompt = confirm(ctrpSyncConfMessage);
	if (prompt == false){
		return;
	}
	var inputParams = "&remoteId="+document.getElementById("remoteId").value;
	var pageOperation = "GetRemoteById";
	var ctrpUserAjaxUrl= 'CTRPStudyProtocolAjax.do?pageOperation='+pageOperation+inputParams;
	var ds = new Ext.data.Store(
			{proxy: new Ext.data.HttpProxy({url: ctrpUserAjaxUrl}),
			reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'},
					[{name: 'title', mapping: 'title'},
					{name: 'shortTitle', mapping: 'shortTitle'},
					{name: 'startDate', mapping: 'startDate'}]
					)
			}
	);
	ds.on('beforeload', function() {
		document.getElementById("storeLoadingDiv").style.display="";
		document.getElementById("syncRemoteDiv").style.display="none";
	});
	ds.on('load', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
		var remoteRecord =  ds.getAt(0);
		document.getElementById('remoteId').value = remoteRecord.id;
		document.getElementById('remoteManagedFlag').value = 'true';
		document.getElementById('dirtyEditFlag').value = 'false';
		//Pre-populate fields from CTRP 
		document.getElementById('title').value = remoteRecord.get('title');
		document.getElementById('shortTitle').value = remoteRecord.get('shortTitle').blank()?'N/A':remoteRecord.get('shortTitle');
		document.getElementById('startDate').value = remoteRecord.get('startDate');
		disableRemoteManagedFields();
		//Hide Find Enterprise User Link
		document.getElementById('lookupNCI').style.display = "none";
		document.getElementById("dirtyEditFlag").value = false;
//		parent.submitCP();
	});
	ds.on('loadexception', function() {
		document.getElementById("storeLoadingDiv").style.display="none";
		document.getElementById("syncRemoteDiv").style.display="";
	});
	ds.load();
}

function removeRemoteLinkFunc(){
	var prompt = confirm(ctrpUnlinkConfMessage);
	if (prompt == false){
		return;
	}
	document.getElementById("dirtyEditFlag").value = 'false';
	document.getElementById("remoteManagedFlag").value = 'false';
	document.getElementById("remoteId").value = '';
	parent.submitCP();
}

function populateUserExtCombobox(){
	   Ext.QuickTips.init();
	   
		var pageOperation = "GetUsers";
		var ctrpUserAjaxUrl=  'CTRPStudyProtocolAjax.do?pageOperation='+pageOperation;
		var ds = new Ext.data.Store({proxy: new Ext.data.HttpProxy({url: ctrpUserAjaxUrl}),
		        reader: new Ext.data.JsonReader({root: 'row',totalProperty: 'totalCount',id: 'id'
		        }, [{name: 'excerpt', mapping: 'name'},
		            {name: 'remoteId', mapping: 'remoteUserId'},
		            {name: 'localId', mapping: 'localUserId'},
		            {name: 'remoteFlag', mapping: 'remoteFlag'}])
		    });
		var initValue="";
		var remoteId = 0;
		var protocolId = 0;
		
//		ds.on('beforeload', function() {
////			document.getElementById("storeLoadingDiv").style.display="";
	//
//			
//		});
		ds.on('load', function() {
//			document.getElementById("storeLoadingDiv").style.display="none";
			if ((ds.getCount()>0) && (protocolId !=0)){
				// Pre select the value only for edit functionality and if any institutions exist
				var insRecord = ds.getAt(0);
				initValue = insRecord.get('excerpt');
				document.getElementById("userId").value = insRecord.get('localId');
				document.getElementById("userId").innerText = insRecord.get('excerpt');
				document.getElementById("remoteUserId").value = insRecord.get('remoteId');
			}
			 Ext.getCmp('ins_cp_user_cb').setValue(initValue);
		});

		if (document.getElementById('remoteId') != null){
			remoteId = document.getElementById('remoteId').value;
		}
		if (document.getElementById('id') != null){
			protocolId = document.getElementById('id').value;
		}
//		updateUsers(userId,remoteId);
		ds.load({params:{'protocolId':protocolId,'remoteId':remoteId}});
		
		
		var cbtpl = new Ext.XTemplate(
				'<tpl for=".">',
					'<tpl if="remoteFlag==\'Y\'">',
						'<div ext:qtip="{excerpt}" class="x-combo-list-item"><img hspace="0" height="15" width="15" vspace="0" title="NCI enterprise entity managed locally" src="images/uIEnhancementImages/nci_remote.png">{excerpt}</div>',
					'</tpl>',
					'<tpl if="remoteFlag==\'N\'">',
						'<div ext:qtip="{excerpt}" class="x-combo-list-item">{excerpt}</div>',
					'</tpl>',
				'</tpl>'
				);
		var combo = new Ext.form.ComboBox({
		    store: ds,displayField:'excerpt',typeAhead: false, pageSize:0,forceSelection: true ,
			queryParam : 'query',mode: 'local',triggerAction: 'all',minChars : 1,queryDelay : 300,
			typeAheadDelay : 400,selectOnFocus:true,
			tpl: cbtpl,
		    applyTo: 'displayinstitutionId', valueField :'excerpt',
		    id:'ins_cp_user_cb', 
		    triggerClass:'x-form-trigger-auto-complete',fieldClass:'x-form-field-auto-complete',
		    value:initValue
		   });
		combo.on("expand", function(){if(Ext.isIE || Ext.isIE7){combo.list.setStyle("width", "270");combo.innerList.setStyle("width", "270");}else{combo.list.setStyle("width", "260");combo.innerList.setStyle("width", "260");}}, {single: true});
		combo.on("select", function(box,record,index){
			
			document.getElementById("userId").value = record.get('localId');
			document.getElementById("displayUserId").innerText = record.get('excerpt');
			document.getElementById("remoteUserId").value = record.get('remoteId');
		});
	}

